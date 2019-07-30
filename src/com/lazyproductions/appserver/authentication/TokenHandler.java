package com.lazyproductions.appserver.authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TokenHandler {
	private static final int TIMEOUT_TIME = 10;
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
	private static Map<String, String> tokenToNameMap = new HashMap<>();
	private static Map<String, ScheduledFuture<?>> tokenToTimerMap = new HashMap<>();
	private Random rand = new Random();

	ScheduledExecutorService timerTask = Executors.newScheduledThreadPool(1);

	public String generateToken(String username) {
		String token = generateRandomHash();
		while (tokenToNameMap.containsKey(token)) {
			token = generateRandomHash();
		}
		tokenToNameMap.put(token, username);

		final String tokenRemove = token;
		ScheduledFuture<?> future = timerTask.schedule(() -> {
			removeToken(tokenRemove);
		}, TIMEOUT_TIME, TimeUnit.MINUTES);

		tokenToTimerMap.put(token, future);
		return token;
	}

	public String validateToken(String token) {
		ScheduledFuture<?> future = tokenToTimerMap.get(token);
		if (future != null) {
			future.cancel(true);
			final String tokenRemove = token;
			ScheduledFuture<?> newFuture = timerTask.schedule(() -> {
				removeToken(tokenRemove);
			}, TIMEOUT_TIME, TimeUnit.MINUTES);

			tokenToTimerMap.put(tokenRemove, newFuture);
		}
		return tokenToNameMap.get(token);
	}

	public void removeToken(String token) {
		tokenToNameMap.remove(token);
		ScheduledFuture<?> future = tokenToTimerMap.get(token);
		if (future != null) {
			future.cancel(true);
			tokenToTimerMap.remove(token);
		}
	}

	private String generateRandomHash() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i <= 100; i++) {
			int character = rand.nextInt(CHARACTERS.length());
			builder.append(CHARACTERS.charAt(character));
		}
		return builder.toString();
	}
}

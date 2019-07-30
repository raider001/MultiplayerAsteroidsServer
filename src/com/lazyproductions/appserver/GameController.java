package com.lazyproductions.appserver;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameController {
	Logger LOGGER = LoggerFactory.getLogger(GameController.class);
	private static final ScheduledExecutorService ses = Executors
			.newScheduledThreadPool(3);
	private ScheduledFuture<?> registrationFuture;
	public GameController() {
		registrationFuture = ses.scheduleAtFixedRate(() -> {
			LOGGER.info("All sockets registered");
			// Everything is now registered and can now set up the real
			// registration.
			cancelRegistrationCheck();
			ses.scheduleAtFixedRate(new GameRunnable(), 0, 20,
					TimeUnit.MILLISECONDS);
		}, 0, 100, TimeUnit.MILLISECONDS);
	}

	private void cancelRegistrationCheck() {
		registrationFuture.cancel(true);
	}

}

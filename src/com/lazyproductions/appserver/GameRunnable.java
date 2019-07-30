package com.lazyproductions.appserver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lazyproductions.appserver.Data.BulletData;
import com.lazyproductions.appserver.Data.GameSettingsData;
import com.lazyproductions.appserver.Data.PlayerData;
import com.lazyproductions.appserver.Data.PlayerMapping;
import com.lazyproductions.appserver.Data.PlayerStatisticData;
import com.lazyproductions.appserver.Data.PlayerVectorData;
import com.lazyproductions.appserver.Data.Vector2D;
import com.lazyproductions.appserver.utils.HitCalculator;
import com.lazyproductions.appserver.websockets.Socket;
import com.lazyproductions.appserver.websockets.game.BulletSocket;
import com.lazyproductions.appserver.websockets.game.GameSocket;
import com.lazyproductions.appserver.websockets.game.PlayerStatSocket;

public class GameRunnable implements Runnable {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(GameRunnable.class);

	private static List<PlayerStatSocket> statSockets = new ArrayList<>();
	private static List<BulletSocket> bulletSockets = new ArrayList<>();
	private static List<GameSocket> gameSockets = new ArrayList<>();
	private static final GameSettingsData gameSettingsData = new GameSettingsData();

	public static void registerSocket(Socket<?> socket) {
		if (socket instanceof BulletSocket) {
			bulletSockets.add((BulletSocket) socket);
		} else if (socket instanceof GameSocket) {
			gameSockets.add((GameSocket) socket);
		} else if (socket instanceof PlayerStatSocket) {
			statSockets.add((PlayerStatSocket) socket);
		} else {
			LOGGER.warn("{} not supported.", socket.getClass());
		}
	}

	public static void deregisterSocket(Socket<?> socket) {
		if (socket instanceof BulletSocket) {
			bulletSockets.remove(socket);
		} else if (socket instanceof GameSocket) {
			gameSockets.remove(socket);
		} else if (socket instanceof PlayerStatSocket) {
			statSockets.remove(socket);
		} else {
			LOGGER.warn("{} not supported.", socket.getClass());
		}
	}

	public void sendPlayerData() {
		List<PlayerData> data = PlayerMapping.registeredPlayers.values()
				.parallelStream().map(item -> item.getPlayerData())
				.collect(Collectors.toList());

		gameSockets.stream().forEach((socket) -> {
			socket.sendRawData(data.toArray(new PlayerData[0]));
		});
	}

	public void sendPlayerStatData() {
		List<PlayerStatisticData> data = PlayerMapping.registeredPlayers
				.values().parallelStream().map(item -> item.getPlayerStats())
				.collect(Collectors.toList());

		statSockets.stream().forEach((socket) -> {
			socket.sendRawData(data.toArray(new PlayerStatisticData[0]));
		});
	}

	public void sendBulletData() {

		BulletData[] data = PlayerMapping.liveBullets
				.toArray(new BulletData[0]);

		bulletSockets.stream().forEach((socket) -> {
			socket.sendRawData(data);
		});
	}

	@Override
	public void run() {
		try {
			PlayerMapping.registeredPlayers.values().parallelStream()
					.forEach((item) -> {
						item.update();
					});

			PlayerMapping.liveBullets.removeIf((item) -> {
				return item.instantiationTime < System.currentTimeMillis();
			});
			List<BulletData> bulletsToRemove = new ArrayList<>();
			PlayerMapping.liveBullets.parallelStream().forEach((item) -> {
				try {
					item.x += item.velocity.x * 2;
					item.y += item.velocity.y * 2;

					if (item.x < 2) {
						item.x = 500;
					} else if (item.x > 500 + 2) {
						item.x = 0;
					}

					if (item.y < 2) {
						item.y = 500;
					} else if (item.y > 500 + 2) {
						item.y = 0;
					}

					PlayerMapping.registeredPlayers.values()
							.forEach((player) -> {
								if (HitCalculator.didTargetHit(item.x, item.y,
										player.getPlayerData().rotation,
										new Vector2D(player.getPlayerData().x,
												player.getPlayerData().y))) {

									double damage = 8;
									double shieldVal = player
											.getPlayerData().shield;

									if (shieldVal < damage) {
										damage -= shieldVal;
										player.getPlayerData().shield = 0;
									} else {
										player.getPlayerData().shield -= damage;
										damage = 0;
									}

									player.getPlayerData().health -= damage;

									if (player.getPlayerData().health <= 0) {
										player.getPlayerStats().deaths++;
										PlayerVectorData enemyShooter = PlayerMapping.registeredPlayers
												.get(item.name);

										if (enemyShooter != null) {
											enemyShooter
													.getPlayerStats().kills++;
										}

										player.getPlayerData().health = PlayerData.maxHealth;
										player.getPlayerData().shield = PlayerData.maxShield;
										player.getPlayerData().x = Math.random()
												* gameSettingsData.gameWidth;
										player.getPlayerData().y = Math.random()
												* gameSettingsData.gameHeight;
										player.getPlayerData().rotation = Math
												.random() * 6.2832;
									}

									bulletsToRemove.add(item);
								}
							});

				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			PlayerMapping.liveBullets.removeAll(bulletsToRemove);
			sendPlayerData();
			sendBulletData();
			sendPlayerStatData();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

package com.lazyproductions.appserver.websockets.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lazyproductions.appserver.GameRunnable;
import com.lazyproductions.appserver.Data.PlayerControlData;
import com.lazyproductions.appserver.Data.PlayerData;
import com.lazyproductions.appserver.Data.PlayerMapping;
import com.lazyproductions.appserver.Data.PlayerStatisticData;
import com.lazyproductions.appserver.Data.PlayerVectorData;
import com.lazyproductions.appserver.authentication.Header;
import com.lazyproductions.appserver.authentication.TokenHandler;
import com.lazyproductions.appserver.websockets.Socket;

@ServerEndpoint(value = "/game/player")
public class GameSocket extends Socket<PlayerControlData> {
	Logger LOGGER = LoggerFactory.getLogger(GameSocket.class);
	private static final ScheduledExecutorService ses = Executors
			.newScheduledThreadPool(1);
	private PlayerData playerData = new PlayerData();
	private TokenHandler tokenHandler = new TokenHandler();

	public GameSocket() {
		super(PlayerControlData.class);

		this.addOnOpenEvent((Session session, EndpointConfig config) -> {
			GameRunnable.registerSocket(this);
		});

		this.addOnCloseEvent((Session session, CloseReason closeReason) -> {
			GameRunnable.deregisterSocket(this);
		});

		this.addOnMessageEvent((PlayerControlData data, Session session) -> {
			String name = tokenHandler.validateToken(data.token);

			if (name != null) {
				playerData.name = name;
				PlayerVectorData vectorData = PlayerMapping.registeredPlayers
						.get(name);
				if (vectorData == null) {
					PlayerData pd = new PlayerData();
					PlayerStatisticData ps = new PlayerStatisticData();
					pd.name = name;
					pd.x = 250;
					pd.y = 250;
					pd.health = PlayerData.maxHealth;
					pd.shield = PlayerData.maxShield;
					vectorData = new PlayerVectorData(pd, ps);

					PlayerMapping.registeredPlayers.put(name, vectorData);
				}
				vectorData.setControls(data.firing, data.rotateLeft,
						data.rotateRight, data.accelerating);
			}
		});
	}

	public void sendRawData(PlayerData[] array) {
		Header header = new Header();
		header.data = getGson().toJson(array);
		sendToSelf(header);
	}
}

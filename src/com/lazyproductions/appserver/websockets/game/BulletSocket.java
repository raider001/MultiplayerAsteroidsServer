package com.lazyproductions.appserver.websockets.game;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.lazyproductions.appserver.GameRunnable;
import com.lazyproductions.appserver.Data.BulletData;
import com.lazyproductions.appserver.authentication.Header;
import com.lazyproductions.appserver.websockets.Socket;

@ServerEndpoint(value = "/game/bullet")
public class BulletSocket extends Socket<BulletData> {

	public BulletSocket() {
		super(BulletData.class);
		this.addOnOpenEvent((Session session, EndpointConfig config) -> {
			GameRunnable.registerSocket(this);
		});

		this.addOnCloseEvent((Session session, CloseReason closeReason) -> {
			GameRunnable.deregisterSocket(this);
		});
	}

	public void sendRawData(BulletData[] array) {
		Header header = new Header();
		header.data = getGson().toJson(array);
		sendToSelf(header);
	}
}

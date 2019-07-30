package com.lazyproductions.appserver.authentication;

import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.json.JSONException;

import com.google.gson.Gson;
import com.lazyproductions.appserver.database.DataBase;
import com.lazyproductions.appserver.database.Encryption;

@ServerEndpoint(value = "/register")
public class RegisterRequest {
	private final DataBase database = new DataBase();

	private final Gson gson = new Gson();

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {

		Header header = new Header();
		UserData loginData = gson.fromJson(message, UserData.class);

		if (database.doesUserExist(loginData.userName)) {
			header.successfulRequest = false;
			header.data = "User already exists.";
			session.getBasicRemote().sendText(gson.toJson(header));
			session.close();
			return;
		}

		Encryption encryption = Encryption.getInstance();
		try {
			database.registerUser(loginData.userName,
					encryption.encrypt(loginData.password));
			header.successfulRequest = true;
			header.data = "Registered.";
			session.getBasicRemote().sendText(gson.toJson(header));
		} catch (JSONException | IllegalBlockSizeException
				| BadPaddingException e) {
			header.successfulRequest = false;
			header.data = "An unkknown error occurred.";
			session.getBasicRemote().sendText(gson.toJson(header));
			session.close();
			return;
		}
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
	}

	@OnError
	public void onError(Throwable t) {
		System.out.println("onError::" + t.getMessage());
	}
}

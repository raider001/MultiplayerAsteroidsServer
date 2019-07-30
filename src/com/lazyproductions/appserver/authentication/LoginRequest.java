package com.lazyproductions.appserver.authentication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;

import com.google.gson.Gson;
import com.lazyproductions.appserver.Data.GameSettingsData;
import com.lazyproductions.appserver.database.DataBase;
import com.lazyproductions.appserver.database.Encryption;

@ServerEndpoint(value = "/login")
public class LoginRequest {
	private final DataBase database = new DataBase();
	private final Gson gson = new Gson();
	private final TokenHandler tokenHandler = new TokenHandler();

	@OnMessage
	public void onMessage(String message, Session session) {
		Header header = new Header();
		UserData loginData = gson.fromJson(message, UserData.class);
		Encryption encryption = Encryption.getInstance();
		try {
			String pw = encryption.encrypt(loginData.password);
			if (database.validateUser(loginData.userName, pw)) {
				header.successfulRequest = true;
				header.data = "Logging in.";
				header.token = tokenHandler.generateToken(loginData.userName);
				String gameSettings = gson.toJson(new GameSettingsData());
				header.data = gameSettings;
			} else {
				header.successfulRequest = false;
				header.data = "Invalid username and/or password.";
			}
		} catch (IllegalBlockSizeException | BadPaddingException | JSONException
				| UnsupportedEncodingException e) {
			e.printStackTrace();
			header.successfulRequest = false;
			header.data = "An unknown issue occurred. we couldnt log you in!";
		}
		try {
			session.getBasicRemote().sendText(gson.toJson(header));
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnError
	public void onError(Throwable t) {
		System.out.println("onError::" + t.getMessage());
	}
}

package com.lazyproductions.appserver.websockets;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.google.gson.Gson;

/**
 *
 * Provides a genericSocket with extended communication capabilities to simplify
 * the needs to implement similar functionality multiple times.
 *
 * @param <T>
 *                - The Data type the socket is listening for.
 */
public abstract class Socket<T> {
	private final List<Session> sessions = new ArrayList<Session>();
	private Session session;
	private List<MessageEvent<T>> onMessageEvents = new ArrayList<>();
	private List<CloseEvent> onCloseEvents = new ArrayList<>();
	private List<OpenEvent> onOpenEvents = new ArrayList<>();
	private List<ErrorEvent> onErrorEvents = new ArrayList<>();
	private final Gson gson = new Gson();
	private final Type incommingDataType;

	public Socket(Class<T> incomming) {
		incommingDataType = incomming;
	}

	public Gson getGson() {
		return gson;
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		// We know this will inherently be the same data type as T. because we
		// must define the type of T via the constructor.
		try {
			@SuppressWarnings("unchecked")
			T data = (T) gson.fromJson(message, incommingDataType);
			onMessageEvents.parallelStream().forEach(event -> {
				event.runMethod(data, session);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnError
	public void onError(Session session, Throwable t) {
		onErrorEvents.parallelStream().forEach(event -> {
			event.runMethod(session, t);
		});
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		sessions.remove(session);

		onCloseEvents.parallelStream().forEach(event -> {
			event.runMethod(session, reason);
		});
	}

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		this.session = session;
		sessions.add(session);

		onOpenEvents.parallelStream().forEach(event -> {
			event.runMethod(session, config);
		});
	}

	public void addOnOpenEvent(OpenEvent open) {
		onOpenEvents.add(open);
	}

	public void addOnCloseEvent(CloseEvent close) {
		onCloseEvents.add(close);
	}

	public void addOnMessageEvent(MessageEvent<T> message) {
		onMessageEvents.add(message);
	}

	public void addOnErrorEvent(ErrorEvent error) {
		onErrorEvents.add(error);
	}

	public Session getSession() {
		return session;
	}

	public void sendToAllSessions(Object message) {
	}

	public void sendToSelf(Object message) {
		String data = gson.toJson(message);
		try {
			session.getBasicRemote().sendText(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Closes the current session.
	 */
	public void closeSession() {
		sessions.remove(session);
		session = null;
	}
}

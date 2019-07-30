package com.lazyproductions.appserver.websockets;

import javax.websocket.Session;

/**
 * This interface is used to handle message events for the Socket class.
 * 
 * @see Socket
 */
public interface MessageEvent<T> {
	void runMethod(T data, Session session);
}

package com.lazyproductions.appserver.websockets;

import javax.websocket.Session;

/**
 * This interface is used to handle open events for the Socket class.
 * 
 * @see Socket
 */
public interface ErrorEvent {
	void runMethod(Session session, Throwable config);
}

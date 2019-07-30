package com.lazyproductions.appserver.websockets;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * This interface is used to handle close events for the Socket class.
 * 
 * @see Socket
 */
public interface CloseEvent {
	void runMethod(Session session, CloseReason reason);
}

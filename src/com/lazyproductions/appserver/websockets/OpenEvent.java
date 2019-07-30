package com.lazyproductions.appserver.websockets;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;

/**
 * This interface is used to handle open events for the Socket class.
 * 
 * @see Socket
 */
public interface OpenEvent {
	void runMethod(Session session, EndpointConfig config);
}

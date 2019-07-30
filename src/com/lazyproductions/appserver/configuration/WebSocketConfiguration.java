package com.lazyproductions.appserver.configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

import com.lazyproductions.appserver.websockets.game.GameSocket;

public class WebSocketConfiguration implements ServerApplicationConfig {

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> arg0) {
		HashSet<Class<?>> set = new HashSet<>();
		set.add(GameSocket.class);
		return set;
	}

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> arg0) {
		return Collections.emptySet();
	}

}

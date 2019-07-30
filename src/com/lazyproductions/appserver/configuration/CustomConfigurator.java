package com.lazyproductions.appserver.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class CustomConfigurator extends Configurator {

	@Override
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		// response.getHeaders().put("Access-Control-Allow-Origin",
		// Arrays.asList("true"));
		List<String> origin = new ArrayList<String>();
		origin.add("*");
		response.getHeaders().put("Origin", origin);
		System.out.println("GOT HERE");
		super.modifyHandshake(sec, request, response);
	}
}

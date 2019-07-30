package com.lazyproductions.appserver.websockets;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

import com.lazyproductions.appserver.authentication.TokenHandler;

public class AuthenticationConfigurator extends Configurator {

	TokenHandler tokenHandler = new TokenHandler();
	private boolean isValid = false;

	@Override
	public void modifyHandshake(ServerEndpointConfig conf, HandshakeRequest req,
			HandshakeResponse resp) {
		conf.getUserProperties().put("handshakereq", req);
		List<String> items = req.getHeaders().get("Sec-WebSocket-Protocol");

		if (items != null && items.size() > 0) {
			if (tokenHandler.validateToken(items.get(0)) != null) {
				List<String> list = new ArrayList<>();
				list.add(items.get(0));
				resp.getHeaders().put("Sec-WebSocket-Protocol", list);
				isValid = true;
				System.out.println("validation successful");
				return;
			}
		}
		System.out.println("rejecting");
		isValid = false;
	}

	@Override
	public <T> T getEndpointInstance(Class<T> endpointClass)
			throws InstantiationException {
		if (!isValid) {
			Socket<?> s = (Socket<?>) super.getEndpointInstance(endpointClass);
			s.closeSession();
		}
		return super.getEndpointInstance(endpointClass);

	}
}

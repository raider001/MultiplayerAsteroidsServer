package com.lazyproductions.appserver;

import javax.websocket.server.ServerContainer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lazyproductions.appserver.authentication.LoginRequest;
import com.lazyproductions.appserver.authentication.RegisterRequest;
import com.lazyproductions.appserver.websockets.game.BulletSocket;
import com.lazyproductions.appserver.websockets.game.GameSocket;
import com.lazyproductions.appserver.websockets.game.PlayerStatSocket;

public class AppServerMain {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AppServerMain.class);

	public static void main(String[] args) throws Exception {
		Server server = new Server(27015);

		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		// Add javax.websocket support
		ServerContainer container = WebSocketServerContainerInitializer
				.configureContext(context);

		// Add echo endpoint to server container
		container.addEndpoint(GameSocket.class);
		container.addEndpoint(LoginRequest.class);
		container.addEndpoint(RegisterRequest.class);
		container.addEndpoint(BulletSocket.class);
		container.addEndpoint(PlayerStatSocket.class);

		ServletHolder defHolder = new ServletHolder("default",
				new DefaultServlet());
		defHolder.setInitParameter("Origin", "*");
		defHolder.setInitParameter("Access-Control-Allow-Origin", "true");
		context.addServlet(defHolder, "/");

		new GameController();

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

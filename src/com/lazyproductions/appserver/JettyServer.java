package com.lazyproductions.appserver;

import javax.websocket.server.ServerContainer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import com.lazyproductions.appserver.websockets.game.GameSocket;

public class JettyServer {
	public JettyServer() throws Exception {
        Server server = new Server(8082);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Add javax.websocket support
        ServerContainer container = WebSocketServerContainerInitializer.configureContext(context);

        // Add echo endpoint to server container
        container.addEndpoint(GameSocket.class);

        // Add default servlet (to serve the html/css/js)
        // Figure out where the static files are stored.
        //URL urlStatics = Thread.currentThread().getContextClassLoader().getResource("index.html");
        //Objects.requireNonNull(urlStatics,"Unable to find index.html in classpath");
        //String urlBase = urlStatics.toExternalForm().replaceFirst("/[^/]*$","/");
        ServletHolder defHolder = new ServletHolder("default",new DefaultServlet());
      //  defHolder.setInitParameter("resourceBase",urlBase);
      //  defHolder.setInitParameter("dirAllowed","true");
        context.addServlet(defHolder,"/");

        try
        {
            server.start();
            server.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
}

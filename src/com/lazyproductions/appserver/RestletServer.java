//package com.lazyproductions.appserver;
//
//import java.awt.Component;
//import java.util.HashSet;
//
//import org.restlet.Application;
//import org.restlet.Restlet;
//import org.restlet.routing.Router;
//import org.restlet.service.CorsService;
//
//import com.lazyproductions.appserver.requests.LoginUser;
//import com.lazyproductions.appserver.requests.RegisterUser;
//import com.sun.mail.iap.Protocol;
//
//public class RestletServer extends Application {
//	public RestletServer() throws Exception {
//		Component component = new Component();
//		Server server = new Server(Protocol.HTTP, 8090);
//		component.getServers().add(server);
//
//		// Create an application
//		CorsService corsService = new CorsService();
//
//		HashSet<String> set = new HashSet<>();
//		set.add("*");
//		corsService.setAllowedOrigins(set);
//		corsService.setAllowedCredentials(true);
//		corsService.setAllowingAllRequestedHeaders(true);
//
//		HashSet<Method> methodSet = new HashSet<>();
//		methodSet.add(Method.ALL);
//		corsService.setDefaultAllowedMethods(methodSet);
//		getServices().add(corsService);
//		component.getDefaultHost().attachDefault(this);
//		component.start();
//	}
//
//	@Override
//	public Restlet createInboundRoot() {
//		// Create a router
//		Router router = new Router(getContext());
//		// Attach the resources to the router
//		router.attach("/register", RegisterUser.class);
//		router.attach("/login", LoginUser.class);
//		// Return the root router
//		return router;
//	}
//}

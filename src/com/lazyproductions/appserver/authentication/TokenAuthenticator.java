package com.lazyproductions.appserver.authentication;

import java.security.Principal;

import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.eclipse.jetty.security.Authenticator;
import org.eclipse.jetty.security.ServerAuthException;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.Authentication.User;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.server.UserIdentity.Scope;

public class TokenAuthenticator implements Authenticator {

	@Override
	public String getAuthMethod() {
		// Only called on instantiation. I think this is what kind of method is
		// being sent.
		// i.e POST, GET, OPTIONS
		return "GET";
	}

	@Override
	public void prepareRequest(ServletRequest arg0) {
		// TODO Auto-generated method stub
		System.out.println("prepareRequest");
	}

	@Override
	public boolean secureResponse(ServletRequest arg0, ServletResponse arg1,
			boolean arg2, User arg3) throws ServerAuthException {
		// TODO Auto-generated method stub
		System.out.println("secureResponse");
		return true;
	}

	@Override
	public void setConfiguration(AuthConfiguration arg0) {
		// TODO Auto-generated method stub
		System.out.println("setConfiguration");

	}

	@Override
	public Authentication validateRequest(ServletRequest arg0,
			ServletResponse arg1, boolean arg2) throws ServerAuthException {
		// TODO Auto-generated method stub
		System.out.println("validateRequest");
		boolean simulateSuccess = false;
		if (simulateSuccess) {
			return new MiniResponse();
		} else {

			return Authentication.SEND_FAILURE;
		}
	}

	private class MiniResponse implements Authentication.User {

		@Override
		public Authentication logout(ServletRequest arg0) {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public String getAuthMethod() {
			// TODO Auto-generated method stub
			return "";
		}

		@Override
		public UserIdentity getUserIdentity() {
			// TODO Auto-generated method stub
			return new UserId();
		}

		@Override
		public boolean isUserInRole(Scope arg0, String arg1) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void logout() {
			// TODO Auto-generated method stub

		}

	}

	private class UserId implements UserIdentity {

		@Override
		public Subject getSubject() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Principal getUserPrincipal() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isUserInRole(String arg0, Scope arg1) {
			return true;
		}

	}
}

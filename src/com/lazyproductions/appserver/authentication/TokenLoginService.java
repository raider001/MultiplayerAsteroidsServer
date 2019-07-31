package com.lazyproductions.appserver.authentication;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.util.security.Credential;

public class TokenLoginService extends AbstractLoginService {

	TokenHandler tokenHandler = new TokenHandler();
	@Override
	protected String[] loadRoleInfo(UserPrincipal arg0) {
		// TODO Auto-generated method stub
		if (tokenHandler.validateToken(arg0.getName()) != null) {
			return new String[]{"access"};
		} else {
			return new String[]{""};
		}
	}

	@Override
	protected UserPrincipal loadUserInfo(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("loadUserInfo");
		UserPrincipal p = new UserPrincipal(arg0,
				new AuthenticationCredential());
		return p;
	}

	private class AuthenticationCredential extends Credential {
		@Override
		public boolean check(Object arg0) {
			// Not really using this. Makes loadRoleInfo work
			return true;
		}

	}
}

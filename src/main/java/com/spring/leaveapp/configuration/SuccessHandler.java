package com.spring.leaveapp.configuration;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SuccessHandler implements AuthenticationSuccessHandler {

	protected Log logger = LogFactory.getLog(this.getClass());
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {
		String targetUrl = findTargetUrl(auth);
		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	private String findTargetUrl(Authentication auth) {
		String targetUrl = "/";
		Boolean isAdmin = false;
		Boolean isUser = false;
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
				isAdmin = true;
			else if (grantedAuthority.getAuthority().equals("ROLE_USER"))
				isUser = true;
		}
		if (isAdmin)
			targetUrl = "/manager";
		else if (isUser) {
			targetUrl = "/employee";
		}
		return targetUrl;
	}

}

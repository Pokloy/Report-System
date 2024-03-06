package jp.co.cyzennt.report.common.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
	@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
		// get the user's authentication roles
				Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
				
				// Check if the user has the "ROLE_ADMIN" authority
		        if (authorities.stream().anyMatch(role -> role.getAuthority().equals("ADMIN"))) {
		            // Redirect to the admin page if the user has the ROLE_ADMIN
		            response.sendRedirect("/admin");
		        } else if (authorities.stream().anyMatch(role -> role.getAuthority().equals("USER"))) {
		            // Redirect to the default user page for default users
		            response.sendRedirect("/userTop");
		        } else if (authorities.stream().anyMatch(role -> role.getAuthority().equals("LEADER"))) {
		        	// redirect to leader page if the user has the leader role
		        	response.sendRedirect("/view/leader");
		        }				
		}
}

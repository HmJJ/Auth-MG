package com.springboot.code.security.manager;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import lombok.Getter;

/**
 * 
 * @since manager 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4993086434873977995L;

	@Getter private final String token;

	public CustomWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		token = request.getParameter("kaptcha");
	}

}

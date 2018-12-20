package com.springboot.code.security.manager;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.springboot.basic.utils.StringUtils;
import com.springboot.code.security.exception.DefaultAuthenticationException;
import com.springboot.code.security.vo.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * 
 * @since manager 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	public final static String CURRENT_USER = "CURRENT_USER";

	@Autowired @Getter private HttpSession session;
	@Autowired @Getter private HttpServletRequest request;
	@Autowired @Getter private UserDetailsService userDetailsService;
	
	/**
	 * 
	 */
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	/**
	 * @param authentication
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
//		details.getRemoteAddress();
//		details.getSessionId();
		String username = (String) authentication.getName();
		String password = (String) authentication.getCredentials();
		String token    = (String) details.getToken();
		
		if (StringUtils.isBlank(username)) {
			throw new DefaultAuthenticationException("用户名为空");
		}
		if (StringUtils.isBlank(password)) {
			throw new DefaultAuthenticationException("密码为空");
		}
		if (StringUtils.isBlank(token)) {
//			throw new DefaultAuthenticationException("验证码为空");
		}

//		String kaptcha = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
//		if (!StringUtils.inCollection(token, kaptcha)) {
//			throw new DefaultAuthenticationException("验证码错误");
//		}

		CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
		// 可直接抛出错误（AuthenticationException的子类，在登录验证不通过重定向至登录页时可通过session.SPRING_SECURITY_LAST_EXCEPTION.message获取具体错误提示信息）

		if (encoder.matches(password, user.getPassword())) {
			Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
			session.setAttribute(CURRENT_USER, username);
			return new UsernamePasswordAuthenticationToken(user, password, authorities);
		} else {
			throw new DefaultAuthenticationException("用户名密码不匹配");
		}

	}

	/**
	 * @param authentication
	 * @return
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}

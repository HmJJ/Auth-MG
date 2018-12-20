package com.springboot.code.security.service;

import com.springboot.code.entity.User;
import com.springboot.code.security.vo.CustomUserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.Getter;

/**
 * 
 * @since manager 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
@Service("userDetailsService")
@Transactional(rollbackFor = Exception.class)
public class DefaultUserDetailsService implements UserDetailsService {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired @Getter private MessageSource messageSource;
	@Autowired @Getter private UserService userService;

	/*
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MessageSourceAccessor messages;

		logger.debug(new StringBuffer("Method: loadUserByUsername(); ").append("Username: " + username + "; ").toString());

		User user = userService.findByUsername(username);

		if (user == null) {
			messages = new MessageSourceAccessor(messageSource);
			throw new UsernameNotFoundException(messages.getMessage("DefaultUserDetailsService.usernameNotFound", new Object[] { username }, "Username {0} not found"));
		}

		return new CustomUserDetails(user);
	}

}

package com.springboot.code.security.manager;

import java.util.Collection;
import java.util.Iterator;

import com.springboot.basic.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

/**
 * 
 * @since manager 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
//@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

	protected final Log logger = LogFactory.getLog(getClass());

	/* (non-Javadoc)
	 * @see org.springframework.security.access.AccessDecisionManager#decide(org.springframework.security.core.Authentication, java.lang.Object, java.util.Collection)
	 */
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {

		FilterInvocation invocation = (FilterInvocation) object;

		StringBuffer buffer = new StringBuffer();
		buffer.append("Username: " + authentication.getName() + "; ");
		buffer.append("RequestUrl: " + invocation.getRequestUrl() + "; ");

		logger.info(buffer.toString());
		//
		if (configAttributes == null || configAttributes.isEmpty()) {
			throw new AccessDeniedException("Access Denied !!!");
		}

		Iterator<ConfigAttribute> iterator = configAttributes.iterator();
		while (iterator.hasNext()) {

			ConfigAttribute attribute = (ConfigAttribute) iterator.next();
			String role = ((SecurityConfig) attribute).getAttribute();

			for (GrantedAuthority authority: authentication.getAuthorities()) {
				if (StringUtils.trim(role).equals(StringUtils.trim(authority.getAuthority()))) {
					return ;
				}
			}
		}

		throw new AccessDeniedException("Access Denied !!!");
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.access.AccessDecisionManager#supports(org.springframework.security.access.ConfigAttribute)
	 */
	@Override
	public boolean supports(ConfigAttribute attribute) {
		logger.debug("DefaultAccessDecisionManager_supports_1");
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.access.AccessDecisionManager#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		logger.debug("DefaultAccessDecisionManager_supports_2");
		return true;
	}

}

package com.springboot.code.security.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.code.entity.Resource;
import com.springboot.code.entity.Role;
import com.springboot.code.security.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import lombok.Getter;

/**
 * 
 * @since manager 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
//@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired @Getter private ResourceService resourceService;

	private static final List<String> irregularity = new ArrayList<String>();
	private static final Map<String, Collection<ConfigAttribute>> MAP = new HashMap<String, Collection<ConfigAttribute>>();

	static {
		irregularity.add("/login");
	}

	/**
	 * @see org.springframework.security.access.SecurityMetadataSource#getAttributes(java.lang.Object)
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

		FilterInvocation invocation = (FilterInvocation) object;

		logger.debug(new StringBuffer("Method: getAttributes(); ").append(invocation.getClass().getName() + ": " + invocation.getFullRequestUrl()).toString());

		String url = invocation.getRequest().getServletPath();

		for (String u : irregularity) {
			if (u.equals(url)) {
				// 返回 null，跳过验证
				return null;
			}
		}

		synchronized (MAP) {

			logger.debug("url: " + url);

			Collection<ConfigAttribute> attributes = MAP.get(url);
			if (attributes != null) {
				return attributes;
			}

			Resource resource = resourceService.findByUrl(url);
			if (resource != null) {
				attributes = new ArrayList<ConfigAttribute>();
				for (Role role : resource.getRoles()) {
					attributes.add(new SecurityConfig(role.getCode()));
				}
			}

			if (attributes == null || attributes.isEmpty()) {
				attributes = new ArrayList<ConfigAttribute>();
				attributes.add(new SecurityConfig("ROLE_ACCESSDENIED"));
			}

			MAP.put(url, attributes);

			return attributes;
		}

	}

	/**
	 * 
	 */
	public void clean() {
		MAP.clear();
	}

	/**
	 * @see org.springframework.security.access.SecurityMetadataSource#getAllConfigAttributes()
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		logger.debug("SecurityMetadataSource_getAllConfigAttributes");
		List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
		return attributes;
	}

	/**
	 * @see org.springframework.security.access.SecurityMetadataSource#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		logger.debug("SecurityMetadataSource_supports");
		return true;
	}

}

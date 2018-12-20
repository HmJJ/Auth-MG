package com.springboot.code.security.manager;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import lombok.Getter;

/**
 * 
 * @since manager 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
//@Component
public class CustomFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

	@Autowired @Getter private FilterInvocationSecurityMetadataSource securityMetadataSource;

	@Autowired
	public void setCustomAccessDecisionManager(CustomAccessDecisionManager customAccessDecisionManager) {
		super.setAccessDecisionManager(customAccessDecisionManager);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		FilterInvocation invocation = new FilterInvocation(request, response, chain);
		invoke(invocation);
	}

	/**
	 * @param invocation
	 * @throws IOException
	 * @throws ServletException
	 */
	public void invoke(FilterInvocation invocation) throws IOException, ServletException {
		InterceptorStatusToken token = super.beforeInvocation(invocation);
		try {
			invocation.getChain().doFilter(invocation.getRequest(), invocation.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.springframework.security.access.intercept.AbstractSecurityInterceptor#getSecureObjectClass()
	 */
	@Override
	public Class<?> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.access.intercept.AbstractSecurityInterceptor#obtainSecurityMetadataSource()
	 */
	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return securityMetadataSource;
	}

}

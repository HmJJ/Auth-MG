package com.springboot.code.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 
 * @since manager 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
public class DefaultAuthenticationException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7973656568027362094L;

	/**
	 * Constructs an <code>ValidateException</code> with the
	 * specified message.
	 * 
	 * @param msg the detail message
	 */
	public DefaultAuthenticationException(String msg) {
		super(msg);
	}

	/**
	 * Constructs an <code>ValidateException</code> with the
	 * specified message and root cause.
	 * 
	 * @param msg the detail message
	 * @param t root cause
	 */
	public DefaultAuthenticationException(String msg, Throwable t) {
		super(msg, t);
	}

}

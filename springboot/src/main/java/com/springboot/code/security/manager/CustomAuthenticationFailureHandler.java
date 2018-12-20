package com.springboot.code.security.manager;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.springboot.code.vo.VueCommonRespVO;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @since manager 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
 
	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.AuthenticationFailureHandler#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		response.setHeader("Content-Type", "application/json;charset=utf-8");
		response.getWriter().print(JSON.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_FAILURE, null, exception.getMessage())));
		response.getWriter().flush();
	}

}

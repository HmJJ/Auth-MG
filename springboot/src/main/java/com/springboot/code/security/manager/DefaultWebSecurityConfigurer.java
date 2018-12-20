package com.springboot.code.security.manager;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.cors.CorsUtils;

import lombok.Getter;

/**
 * 
 * @since manager 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class DefaultWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired @Getter private CustomAccessDeniedHandler accessDeniedHandler;
	@Autowired @Getter private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
	@Autowired @Getter private CustomAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired @Getter private CustomAuthenticationFailureHandler authenticationFailureHandler;
	@Autowired @Getter private AuthenticationProvider authenticationProvider;
	@Autowired @Getter private CustomAuthenticationSuccessHandler authenticationSuccessHandler;
//	@Autowired @Getter private CustomFilterSecurityInterceptor filterSecurityInterceptor;
	@Autowired @Getter private CustomLogoutSuccessHandler logoutSuccessHandler;
	@Autowired @Getter private CustomCorsFilter corsFilter;

	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.WebSecurity)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/kaptcha/verify");
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
			.addFilterBefore(corsFilter, FilterSecurityInterceptor.class)
			.exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler)
				.authenticationEntryPoint(authenticationEntryPoint)
			.and().authorizeRequests()
				.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
				.antMatchers("/security/**").hasAuthority("ADMIN")
				.anyRequest().authenticated()
			.and().formLogin()
				.loginProcessingUrl("/login").permitAll()
				.successHandler(authenticationSuccessHandler)
				.authenticationDetailsSource(authenticationDetailsSource)
				.failureHandler(authenticationFailureHandler)
			.and().logout()
				.logoutUrl("/logout")
				.logoutSuccessHandler(logoutSuccessHandler)
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
			.and().requiresChannel()
				.antMatchers("/pomer").requiresSecure()
				.anyRequest().requiresInsecure()
			.and().rememberMe()
				.tokenValiditySeconds(1800)
				.key("token_key")
			.and().cors();

//		http.addFilterBefore(filterSecurityInterceptor, FilterSecurityInterceptor.class);
	}

}

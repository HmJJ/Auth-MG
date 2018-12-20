package com.springboot.code.security.manager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 
 * @since manager 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
@Configuration
public class CustomCorsConfigurer {

	/**
	 * @return
	 */
	@Bean
	public CorsFilter corsFilter() {

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowCredentials(true);
		// 如访问出错，将 [ Origin ] 的 [ * ] 改成对应客户端 [ 域名 ]
		configuration.addAllowedOrigin("*");
		// 其他都设置为 [ * ]
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", configuration);

		return new CorsFilter(source);

	}

}
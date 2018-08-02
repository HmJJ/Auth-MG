package com.springboot.quartz.config;

import java.io.IOException;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {
	
	@Autowired
	private SpringJobFactory springJobFactory;
	
	/**
	 * 把job添加到spring bean管理
	 * @return
	 * @throws IOException
	 */
	@Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(springJobFactory);
        return factory;
    }
	
	/**
	 * 初始注入schedule
	 * @throws IOException 
	 */
	@Bean(name="NewScheduler")
	public Scheduler scheduler() throws IOException{
		return schedulerFactoryBean().getScheduler();
	}
	
//	@Bean
//	public Scheduler scheduler() throws SchedulerException{
//		SchedulerFactory schedulerFactoryBean = new StdSchedulerFactory();
//		return schedulerFactoryBean.getScheduler();
//	}
	
}

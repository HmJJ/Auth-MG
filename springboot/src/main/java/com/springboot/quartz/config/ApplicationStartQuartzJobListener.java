package com.springboot.quartz.config;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private QuartzSchedule quartzSchedule;
	
	/**
	 * 初始启动quartz
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			quartzSchedule.startJob();
			System.out.println("任务已启动...");
		} catch (SchedulerException e) {
			log.error(e.getMessage());
		}
	}
	
}


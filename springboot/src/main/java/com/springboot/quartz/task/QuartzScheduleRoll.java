package com.springboot.quartz.task;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import com.springboot.quartz.config.QuartzSchedule;
import com.springboot.quartz.entity.Quartz;
import com.springboot.quartz.service.QuartzService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuartzScheduleRoll implements Job, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1453121334205835255L;
	public final static String NAME = "roll";
	public final static String GROUP = "group1";
	public final static String CLASSNAME = "QuartzScheduleRoll";
	public static String CRON = "0 0/5 * * * ?";
	public static String DETAIL = "通过轮询来确认交易是否入链";
	
	@Autowired
    private QuartzService quartzService;
	
	@Autowired
    private QuartzSchedule quartzScheduler;
	
	private String before () {
		System.out.println("任务"+ QuartzScheduleRoll.NAME +"开始");
		if(quartzService == null) {
			return null;
		}
		Quartz entity = quartzService.findByName(QuartzScheduleRoll.NAME);
		return entity==null ? "" : entity.getStatus();
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String status = before();
//		String status = Quartz.STATUS_RUNNING;
		if(status == null || status.equals("")) {
			try {
				quartzScheduler.pauseJob(QuartzScheduleRoll.NAME, QuartzScheduleRoll.GROUP);
				System.out.println("确认交易发生了错误,任务被关闭");
			} catch (SchedulerException e) {
				log.error(e.getMessage());
			}
		}else {
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				System.out.println("确认交易开始：" + sdf.format(now));
				System.out.println("确认交易结束：" + sdf.format(now));
			} catch (Exception e) {
				log.error(sdf.format(now)+":确认交易发生了错误==="+e.getMessage());
			}
			after();
		}
	}
	
	private void after() {
		System.out.println("任务"+ QuartzScheduleRoll.NAME +"结束");
	}
	
}


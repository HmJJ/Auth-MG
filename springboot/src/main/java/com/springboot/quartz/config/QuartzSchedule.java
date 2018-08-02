package com.springboot.quartz.config;

import java.util.ArrayList;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import com.springboot.quartz.entity.Quartz;
import com.springboot.quartz.service.QuartzService;
import com.springboot.quartz.task.QuartzScheduleRoll;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class QuartzSchedule {

	// 任务调度
	@Autowired @Qualifier("NewScheduler")
	private Scheduler scheduler;

	@Autowired
	private QuartzService quartzService;

	/**
	 * 开始执行所有任务
	 * 
	 * @throws SchedulerException
	 */
	public void startJob() throws SchedulerException {
		startRoll(scheduler);
		scheduler.start();
	}

	/**
	 * 获取Job信息
	 * 
	 * @param name
	 * @param group
	 * @return
	 * @throws SchedulerException
	 */
	public String getJobInfo(String name, String group) throws SchedulerException {
		TriggerKey triggerKey = new TriggerKey(name, group);
		CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
				scheduler.getTriggerState(triggerKey).name());
	}

	/**
	 * 修改某个任务的执行时间
	 * 
	 * @param name
	 * @param group
	 * @param time
	 * @return
	 * @throws SchedulerException
	 */
	public boolean modifyJob(String name, String group, String time) {
		Date date = null;
		try {
			TriggerKey triggerKey = new TriggerKey(name, group);
			CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			String oldTime = cronTrigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
				CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
						.withSchedule(cronScheduleBuilder).build();
				date = scheduler.rescheduleJob(triggerKey, trigger);
				
				quartzService.modifyCron(name, time);
			}
		} catch (SchedulerException e) {
			log.error(e.getMessage());
		}
		return date != null;
	}

	/**
	 * 暂停所有任务
	 * 
	 * @throws SchedulerException
	 */
	public void pauseAllJob() throws SchedulerException {
		scheduler.pauseAll();

		ArrayList<Quartz> jobs = (ArrayList<Quartz>) quartzService.findAll();
		for (Quartz job : jobs) {
			quartzService.modify(job.getName(), Quartz.STATUS_STOP);
		}
	}

	/**
	 * 暂停某个任务
	 * 
	 * @param name
	 * @param group
	 * @throws SchedulerException
	 */
	public void pauseJob(String name, String group) throws SchedulerException {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail == null)
			return;
		scheduler.pauseJob(jobKey);

		quartzService.modify(name, Quartz.STATUS_STOP);
	}

	/**
	 * 恢复所有任务
	 * 
	 * @throws SchedulerException
	 */
	public void resumeAllJob() throws SchedulerException {
		scheduler.resumeAll();

		ArrayList<Quartz> jobs = (ArrayList<Quartz>) quartzService.findAll();
		for (Quartz job : jobs) {
			quartzService.modify(job.getName(), Quartz.STATUS_RUNNING);
		}
	}

	/**
	 * 恢复某个任务
	 * 
	 * @param name
	 * @param group
	 * @throws SchedulerException
	 */
	public void resumeJob(String name, String group) throws SchedulerException {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail == null)
			return;
		scheduler.resumeJob(jobKey);

		quartzService.modify(name, Quartz.STATUS_RUNNING);
	}

	/**
	 * 删除某个任务
	 * 
	 * @param name
	 * @param group
	 * @throws SchedulerException
	 */
	public void deleteJob(String name, String group) throws SchedulerException {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail == null)
			return;
		scheduler.deleteJob(jobKey);

		quartzService.modify(name, Quartz.STATUS_DELETE);
	}

	private void startRoll(Scheduler scheduler) throws SchedulerException {
		Quartz entity = quartzService.findByName(QuartzScheduleRoll.NAME);
		String cron = QuartzScheduleRoll.CRON;
		if (entity == null) {
			entity = new Quartz(QuartzScheduleRoll.NAME, QuartzScheduleRoll.GROUP, QuartzScheduleRoll.CRON,
					Quartz.STATUS_RUNNING, QuartzScheduleRoll.DETAIL, QuartzScheduleRoll.CLASSNAME);
			quartzService.commit(entity);
		}else {
			String realCron = entity.getCron();
			if(!cron.equals(realCron)) {
				cron = realCron;
			}
		}
		
		JobDetail jobDetail = JobBuilder.newJob(QuartzScheduleRoll.class)
				.withIdentity(QuartzScheduleRoll.NAME, QuartzScheduleRoll.GROUP).build();
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity(QuartzScheduleRoll.NAME, QuartzScheduleRoll.GROUP).withSchedule(cronScheduleBuilder)
				.build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}
	
}

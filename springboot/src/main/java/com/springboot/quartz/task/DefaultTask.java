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
import com.springboot.quartz.support.DefaultSupportTask;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultTask<PK extends Serializable> extends DefaultSupportTask<PK> implements Job{

	@Getter
	@Setter
	public String NAME;
	@Getter
	@Setter
	public String GROUP;
	@Getter
	@Setter
	public String CLASSNAME;
	@Getter
	@Setter
	public String CRON;
	@Getter
	@Setter
	public String DETAIL;
	
	@Autowired
    private QuartzService quartzService;
	
	@Autowired
    private QuartzSchedule quartzScheduler;
	
	public void init() {
		
	}
	
	public String before () {
		System.out.println("任务"+ this.NAME +"开始");
		init();
		if(quartzService == null) {
			return null;
		}
		Quartz entity = quartzService.findByName(this.NAME);
		return entity==null ? "" : entity.getStatus();
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String status = before();
		if(status == null || status.equals("")) {
			try {
				quartzScheduler.pauseJob(this.NAME, this.GROUP);
				System.out.println("确认交易发生了错误,任务被关闭");
			} catch (SchedulerException e) {
				log.error(e.getMessage());
			}
		}else {
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				System.out.println("确认交易开始：" + sdf.format(now));
				doMain();
				System.out.println("确认交易结束：" + sdf.format(now));
			} catch (Exception e) {
				log.error(sdf.format(now)+":确认交易发生了错误==="+e.getMessage());
			}
			after();
		}
	}
	
	public void doMain() {
		
	}
	
	private void after() {
		System.out.println("任务"+ this.NAME +"结束");
	}

}

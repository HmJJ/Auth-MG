package com.springboot.quartz.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class QuartzVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5790149805134368248L;
	
	/**
	 * 任务名
	 */
	@Getter
	@Setter
	private String name;

	/**
	 * 任务分组名
	 */
	@Getter
	@Setter
	private String group;

	/**
	 * 任务轮询时间
	 */
	@Getter
	@Setter
	private String cron;

	/**
	 * 任务状态
	 * STATUS_RUNNING = "0";
	 * STATUS_STOP = "1";
	 * STATUS_DELETE = "2";
	 */
	@Getter
	@Setter
	private String status;

	/**
	 * 任务详情描述
	 */
	@Getter
	@Setter
	private String detail;

	/**
	 * 任务所属java类的名称
	 */
	@Getter
	@Setter
	private String classname;

	/**
	 * 任务的创建时间
	 */
	@Getter
	@Setter
	private String createdate;

	/**
	 * 该任务上一次的修改时间
	 */
	@Getter
	@Setter
	private String modifydate;
}

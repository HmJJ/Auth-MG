package com.springboot.quartz.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.springboot.basic.entity.DefaultModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="TBL_NOTT_QUARTZ")
public class Quartz extends DefaultModel {

	public final static String STATUS_RUNNING = "0";
	public final static String STATUS_STOP = "1";
	public final static String STATUS_DELETE = "2";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1623781867155481060L;
	
	@Column(name="TASK_NAME",length = 32)
	@Getter
	@Setter
	private String name;
	
	
	@Column(name="TASK_GROUP", length = 8)
	@Getter
	@Setter
	private String group;
	
	@Column(name="TASK_CRON", length = 16)
	@Getter
	@Setter
	private String cron;
	
	@Column(name="TASK_STATUS", length = 8)
	@Getter
	@Setter
	private String status;
	
	@Column(name="TASK_DETAIL", length = 64)
	@Getter
	@Setter
	private String detail;
	
	@Column(name="TASK_CLASSNAME", length = 32)
	@Getter
	@Setter
	private String classname;
	
	public Quartz(){
		
	}
	
	public Quartz(String name, String group, String cron, String status, String detail, String classname){
		this.name = name;
		this.group = group;
		this.cron = cron;
		this.status = status;
		this.detail = detail;
		this.classname = classname;
	}

}

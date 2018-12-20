package com.springboot.code.security.vo;

import java.io.Serializable;

import lombok.Data;
@Data
public class RoleVO implements Serializable{

	private static final long serialVersionUID = 6010080761010354028L;

	private String id;
	private String code;

	private String name;

	private String type;

	private String icon;

	private String description;

	private String status;

	private String sort;
	
	private String createDate;
	
	private String modifyDate;

	

}

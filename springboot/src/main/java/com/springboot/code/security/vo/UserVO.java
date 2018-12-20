package com.springboot.code.security.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3036252313642299861L;
	
	private String id;

	/**
	 * 用户名
	 */
	
	private String username;
	
	/**
	 * 密码
	 */
	
	private String password;
	
	/**
	 * 姓名
	 */
	
	private String name;
	
	/**
	 * phone
	 */
	
	private String phone;
	
	/**
	 * E-Mail
	 */
	
	private String email;
	
	/**
	 * 用户类型
	 */
	
	private String type;
	
	/**
	 * 用户状态
	 */

	private String status;
	

	private String createDate;
	private String modifyDate;

}

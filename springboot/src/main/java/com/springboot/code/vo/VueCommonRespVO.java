package com.springboot.code.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * web项目的返回都用这个啦
 */
@Data
public class VueCommonRespVO implements Serializable {
	private static final long serialVersionUID = -8035611884310026534L;

	/** 成功 */
	public static final int CODE_SUCCESS = 200;
	/** 未授权，返回这个让用户重新登陆去 */
	public static final int CODE_UNAUTH = 401;
	/** 失败 */
	public static final int CODE_FAILURE = 500;

	private Integer code = CODE_SUCCESS;
	private Object data;
	private String msg;// 没用
	private String version = "v0.01";// 没用
	private String signature = "";// 没用

	public VueCommonRespVO() {
		super();
	}

	/**
	 * 默认成功
	 * 
	 * @param data
	 */
	public VueCommonRespVO(Object data) {
		super();
		this.data = data;
	}

	public VueCommonRespVO(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public VueCommonRespVO(Integer code, Object data, String msg) {
		super();
		this.code = code;
		this.data = data;
		this.msg = msg;
	}

	public VueCommonRespVO(Integer code, Object data, String msg, String version, String signature) {
		super();
		this.code = code;
		this.data = data;
		this.msg = msg;
		this.version = version;
		this.signature = signature;
	}

}

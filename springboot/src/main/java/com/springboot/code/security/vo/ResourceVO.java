package com.springboot.code.security.vo;

import java.io.Serializable;
import java.util.List;


import lombok.Data;

@Data
public class ResourceVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6693164432846078263L;
	private String id;
	private String title;//	资源名称
	private String code;
	private String url;
	private String description;
	private String type;
	private String parentId;
	private List<ResourceVO> children;
	private String status;
	private Integer sort;
	private boolean selected;// 由于iview树型插件的原因，这个必须要，默认false
}

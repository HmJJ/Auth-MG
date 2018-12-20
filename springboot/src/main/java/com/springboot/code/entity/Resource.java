package com.springboot.code.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.springboot.basic.entity.DefaultModel;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OrderBy;

import lombok.Getter;
import lombok.Setter;

/**
 * 资源（菜单、按钮、超链接）
 * 
 * @since entity 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
@Entity
@Table(name = "SYS_RESOURCE")
@BatchSize(size = 20)
@DiscriminatorValue(value = "resource")
public class Resource extends DefaultModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4753244547379660956L;

	/** 菜单 */
	public static final String TYPE_MENU = "TYPE_MENU";
	/** 超链接 */
	public static final String TYPE_URL = "TYPE_URL";
	/** 按钮 */
	public static final String TYPE_BUTTON = "TYPE_BUTTON";

	/**
	 * 资源名称
	 */
	@Column(name = "NAME", length = 64)
	@Getter
	@Setter
	private String name;
	
	/**
	 * 资源代码
	 */
	@Column(name = "CODE", length = 32)
	@Getter
	@Setter
	private String code;

	/**
	 * URL
	 */
	@Column(name = "URL", length = 2048)
	@Getter
	@Setter
	private String url;

	/**
	 * 备注、说明
	 */
	@Column(name = "DESCRIPTION", length = 1024)
	@Getter
	@Setter
	private String description;

	/**
	 * 
	 */
	@Column(name = "TYPE", length = 16)
	@Getter
	@Setter
	private String type;

	/**
	 * 
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	@Getter
	@Setter
	private Resource parent;

	/**
	 * 
	 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@Getter
	@Setter
	@OrderBy(clause = "sort asc")
	private List<Resource> sublevel;

	/**
	 * 
	 */
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "REL_ROLE_RESOURCE",
		joinColumns        = {@JoinColumn(name = "RESOURCE_ID", referencedColumnName = "ID") },
		inverseJoinColumns = {@JoinColumn(name = "ROLE_ID",     referencedColumnName = "ID") })
	@Getter
	@Setter
	private Set<Role> roles;

	/**
	 * 0启用	1禁用
	 */
	@Column(name = "STATUS", length = 4)
	@Getter
	@Setter
	private String status;

	/**
	 * 排序
	 */
	@Column(name = "SORT_ORDER", length = 4)
	@Getter
	@Setter
	private Integer sort;

	/**
	 * 
	 */
	public Resource() {}

	/**
	 * @param orig
	 */
	public Resource(Resource orig) {
		this.setId(orig.getId());
		this.code = orig.getCode();
		this.url = orig.getUrl();
		this.description = orig.getDescription();
		this.type = orig.getType();
		this.status = orig.getStatus();
		this.sort = orig.getSort();
	}

}

package com.springboot.code.entity;

import java.util.HashSet;
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
import javax.persistence.Table;
import javax.persistence.Transient;

import com.springboot.basic.entity.DefaultModel;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OrderBy;

import lombok.Getter;
import lombok.Setter;

/**
 * 角色
 * 
 * @since entity 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
@Entity
@Table(name = "SYS_ROLE")
@BatchSize(size = 20)
@DiscriminatorValue(value = "role")
public class Role extends DefaultModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2594130674930898182L;

	/**
	 * 角色代码
	 */
	@Column(name = "CODE", length = 32)
	@Getter
	@Setter
	private String code;

	/**
	 * 角色名称
	 */
	@Column(name = "NAME", length = 64)
	@Getter
	@Setter
	private String name;

	/**
	 * 角色类型
	 */
	@Column(name = "TYPE", length = 16)
	@Getter
	@Setter
	private String type;

	/**
	 * 角色图标
	 */
	@Column(name = "ICON", length = 32)
	@Getter
	@Setter
	private String icon;

	/**
	 * 备注、说明
	 */
	@Column(name = "DESCRIPTION", length = 1024)
	@Getter
	@Setter
	private String description;

	/**
	 * 角色下有权限访问的资源项
	 */
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "REL_ROLE_RESOURCE",
		joinColumns        = {@JoinColumn(name = "ROLE_ID",     referencedColumnName = "ID") },
		inverseJoinColumns = {@JoinColumn(name = "RESOURCE_ID", referencedColumnName = "ID") })
	@Getter
	@Setter
	@OrderBy(clause = "sort asc")
	private List<Resource> resources;

	/**
	 * 拥有该角色的用户
	 */
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "REL_USER_ROLE",
		joinColumns        = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") },
		inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID") })
	@Getter
	@Setter
	private Set<User> users;

	/**
	 * 角色状态
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
	public Role() {}

	/**
	 * @return
	 * 
	 */
	@Transient
	public Set<Resource> gatherResources() {
		Set<Resource> resources = new HashSet<Resource>();
		if (this.resources != null && !this.resources.isEmpty()) {
			resources.addAll(this.resources);
		}
		return resources;
	}

	/**
	 * @return
	 * 
	 */
	@Transient
	public Set<String> gatherResourceIds() {
		Set<String> ids = new HashSet<String>();
		if (this.resources != null && !this.resources.isEmpty()) {
			for (Resource resource : this.resources) {
				ids.add(resource.getId());
			}
		}
		return ids;
	}

}

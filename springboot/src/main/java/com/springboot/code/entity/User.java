package com.springboot.code.entity;

import java.util.HashSet;

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

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 后台pc端登录用户（运维人员）
 *
 */
@Entity
@Table(name = "SYS_USER")
@BatchSize(size = 20)
@DiscriminatorValue(value = "user")
public class User extends DefaultModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8934667427192617398L;

	/**
	 * 用户名
	 */
	@Column(name = "USERNAME", length = 64, nullable = false)
	@Getter
	@Setter
	private String username;
	
	/**
	 * 密码
	 */
	@Column(name = "PASSWORD", length = 128, nullable = false)
	@Getter
	@Setter
	private String password;
	
	/**
	 * 姓名
	 */
	@Column(name = "NAME", length = 64, nullable = false)
	@Getter
	@Setter
	private String name;
	
	/**
	 * phone
	 */
	@Column(name = "PHONE", length = 64)
	@Getter
	@Setter
	private String phone;
	
	/**
	 * E-Mail
	 */
	@Column(name = "EMAIL", length = 64)
	@Getter
	@Setter
	private String email;
	
	/**
	 * 用户类型
	 */
	@Column(name = "TYPE", length = 16)
	@Getter
	@Setter
	private String type;
	
	/**
	 * 用户状态
	 */
	@Column(name = "STATUS", length = 4)
	@Getter
	@Setter
	private String status;
	
	/**
	 * 用户拥有的角色
	 */
	@ManyToMany(targetEntity = Role.class, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable(name = "REL_USER_ROLE", 
		joinColumns        = { @JoinColumn(name = "USER_ID", referencedColumnName = "ID") },
		inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
	@Getter
	@Setter
	private Set<Role> roles;

	/**
	 * @return
	 * 
	 */
	@Transient
	public Set<Resource> gatherResources() {
		Set<Resource> resources = new HashSet<Resource>();
		if (this.roles != null && !this.roles.isEmpty()) {
			for (Role role : this.roles) {
				resources.addAll(role.gatherResources());
			}
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
		if (this.roles != null && !this.roles.isEmpty()) {
			for (Role role : this.roles) {
				ids.addAll(role.gatherResourceIds());
			}
		}
		return ids;
	}

	/**
	 * @return
	 * 
	 */
	@Transient
	public Set<String> gatherRoleIds() {
		Set<String> ids = new HashSet<String>();
		if (this.roles != null && !this.roles.isEmpty()) {
			for (Role role : roles) {
				ids.add(role.getId());
			}
		}
		return ids;
	}


}

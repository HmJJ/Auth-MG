package com.springboot.entity.security;

import com.springboot.basic.entity.DefaultModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户信息
 */
@Entity
@Table(name = "SYS_USER")
public class User extends DefaultModel {

    /**
     * 用户名
     */
    @Column(name = "USERNAME", length = 32, nullable = false)
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
     * 电话
     */
    @Column(name = "PHONE", length = 64)
    @Getter
    @Setter
    private String phone;

    /**
     * E-mail
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
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    @Getter
    @Setter
    private Set<Role> roles;

    /**
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
     *
     */
    @Transient
    public Set<String> gatherRoleIds() {
        Set<String> ids = new HashSet<String>();
        if (this.roles != null && !this.roles.isEmpty()) {
            for (Role role : this.roles) {
                ids.add(role.getId());
            }
        }
        return ids;
    }

    public User() {
    }

    public User(String username, String password, String name, String phone, String email, String type, String status) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.type = type;
        this.status = status;
    }

    @Override
    public String toString() {
        return "[username:" + username + ", password:" + password + ", name:" +
                name + ", phone:" + phone + ", email:" + email + ", type:" + type + ", status:" + status + "]";
    }
}

package com.springboot.entity.security;

import com.springboot.basic.entity.DefaultModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 角色信息
 */
@Entity
@Table(name = "SYS_ROLE")
public class Role extends DefaultModel {

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
     * 该角色下有权限访问的资源项
     */
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "REL_ROLE_RESOURCE",
            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "RESOURCE_ID", referencedColumnName = "ID")})
    @Getter
    @Setter
    @OrderBy(clause = "sort asc")
    private Set<Resource> resources;

    /**
     * 拥有该角色的用户
     */
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "REL_USER_ROLE",
            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")})
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

    public Role() {
    }

    public Role(String code, String name, String type, String icon, String description, String status, Integer sort) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.description = description;
        this.status = status;
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "[code:" + code + ", name:" + name + ", type:" + type + ", icon:" + icon
                + ", description:" + description + ", status:" + status + ", sort" + sort + "]";
    }

    /**
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

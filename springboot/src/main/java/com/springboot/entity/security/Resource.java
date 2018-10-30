package com.springboot.entity.security;

import com.springboot.basic.entity.DefaultModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import org.hibernate.annotations.OrderBy;

import java.util.List;
import java.util.Set;

/**
 * 资源信息（按钮、菜单、超链接）
 */
@Entity
@Table(name = "SYS_RESOURCE")
public class Resource extends DefaultModel {

    /* 按钮 */
    public static final String TYPE_BUTTON = "TYPE_BUTTON";
    /* 菜单 */
    public static final String TYPE_MENU = "TYPE_MENU";
    /* 超链接 */
    public static final String TYPE_URL = "TYPE_URL";

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
     * 类型
     */
    @Column(name = "TYPE", length = 16)
    @Getter
    @Setter
    private String type;

    /**
     * 该资源对应的父级资源
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    @Getter
    @Setter
    private Resource parent;

    /**
     * 该资源作为父级资源
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @Getter
    @Setter
    @OrderBy(clause = "sort asc")
    private List<Resource> sublevel;

    /**
     * 拥有该资源的角色
     */
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "REL_ROLE_RESOURCE",
            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "RESOURCE_ID", referencedColumnName = "ID")})
    @Getter
    @Setter
    private Set<Role> roles;

    /**
     * 状态
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

    public Resource() {

    }

    public Resource(String code, String url, String description, String type, String status, Integer sort) {
        this.code = code;
        this.url = url;
        this.description = description;
        this.type = type;
        this.status = status;
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "[code:" + code + ", url:" + url + ", description:" + description + ", type:" + type
                + ", status:" + status + ", sort:" + sort + "]";
    }

}

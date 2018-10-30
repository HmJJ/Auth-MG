package com.springboot.code.user.vo;

import com.springboot.entity.security.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1786977842518881264L;

    /**
     * id
     */
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
     * 电话
     */
    private String phone;

    /**
     * E-mail
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

    /**
     * 用户拥有的角色
     */
    private Set<Role> roles;

}

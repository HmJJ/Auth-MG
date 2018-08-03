package com.springboot.code.service;

import com.springboot.basic.service.DefaultService;
import com.springboot.code.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("roleService")
@Transactional(rollbackFor = Exception.class)
public class RoleService extends DefaultService<Role, String> {



}

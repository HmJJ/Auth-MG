package com.springboot.code.user.service;

import com.springboot.basic.service.DefaultService;
import com.springboot.basic.utils.StringUtils;
import com.springboot.entity.security.User;
import com.springboot.code.user.vo.UserVO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "loginService")
@Transactional(rollbackFor = Exception.class)
public class LoginService extends DefaultService<User, String>{

    /**
     * 根据id查询该用户是否存在
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public User findById(String id) {
        if(StringUtils.isBlank(id)){
            return null;
        }
        List<User> users = new ArrayList<User>();
        users = findBy(new AssembleCriteriaParamsCallback() {

            @Override
            public DetachedCriteria assembleParams(DetachedCriteria criteria) {
                if(StringUtils.isNotBlank(id)) {
                    criteria.add(Restrictions.eq("id", id));
                }
                criteria.add(Restrictions.eq("delete", Boolean.FALSE));
                return criteria;
            }
        });
        return (users != null && users.size() > 0) ? users.get(0) : null;
    }

    /**
     * 根据用户信息查询该用户是否存在
     * @param entity
     * @return
     */
    @Transactional(readOnly = true)
    public User findByUserInfo(UserVO entity) {
        if(entity == null){
            return null;
        }
        if((StringUtils.isBlank(entity.getUsername()) && StringUtils.isBlank(entity.getPassword())) ||
                (StringUtils.isBlank(entity.getPhone()) && StringUtils.isBlank(entity.getPassword())) ||
                (StringUtils.isBlank(entity.getEmail()) && StringUtils.isBlank(entity.getPassword()))) {
            return null;
        }
        List<User> users = new ArrayList<User>();
        users = findBy(new AssembleCriteriaParamsCallback() {

            @Override
            public DetachedCriteria assembleParams(DetachedCriteria criteria) {
                if(StringUtils.isNotBlank(entity.getUsername())) {
                    criteria.add(Restrictions.eq("username", entity.getUsername()));
                }
                if(StringUtils.isNotBlank(entity.getPhone())) {
                    criteria.add(Restrictions.eq("phone", entity.getPhone()));
                }
                if(StringUtils.isNotBlank(entity.getEmail())) {
                    criteria.add(Restrictions.eq("email", entity.getEmail()));
                }
                if(StringUtils.isNotBlank(entity.getPassword())) {
                    criteria.add(Restrictions.eq("password", entity.getPassword()));
                }
                criteria.add(Restrictions.eq("delete", Boolean.FALSE));
                return criteria;
            }
        });
        return (users != null && users.size() > 0) ? users.get(0) : null;
    }

    /**
     * 新增或者修改user实体
     *
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public Boolean commit(UserVO entity) {
        try {

            User temp = null;
            if (StringUtils.isNotBlank(entity.getId())) {
                temp = findBy(entity.getId());
            }
            if (temp == null) {
                temp = new User();
            }
            if (StringUtils.isNotBlank(entity.getUsername())) {
                temp.setUsername(entity.getUsername());
            }
            if (StringUtils.isNotBlank(entity.getPassword())) {
                temp.setPassword(entity.getPassword());
            }
            if (StringUtils.isNotBlank(entity.getName())) {
                temp.setName(entity.getName());
            }
            if (StringUtils.isNotBlank(entity.getPhone())) {
                temp.setPhone(entity.getPhone());
            }
            if (StringUtils.isNotBlank(entity.getEmail())) {
                temp.setEmail(entity.getEmail());
            }
            if (StringUtils.isNotBlank(entity.getType())) {
                temp.setType(entity.getType());
            }
            if (StringUtils.isNotBlank(entity.getStatus())) {
                temp.setStatus(entity.getStatus());
            }
            if (!entity.getRoles().isEmpty()) {
                temp.setRoles(entity.getRoles());
            }
            temp.setModifydate(new Date());
            temp = merge(temp);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }

}

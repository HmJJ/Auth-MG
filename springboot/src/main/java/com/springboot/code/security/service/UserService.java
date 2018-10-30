package com.springboot.code.security.service;

import com.springboot.basic.service.DefaultService;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.BeanUtils;
import com.springboot.basic.utils.StringUtils;
import com.springboot.entity.security.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class UserService extends DefaultService<User, String> {

    public User findByName(final String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        List<User> users = findBy(new AssembleCriteriaParamsCallback() {
            @Override
            public DetachedCriteria assembleParams(DetachedCriteria criteria) {
                criteria.add(Restrictions.eq("name", name));
                criteria.add(Restrictions.eq("delete", Boolean.FALSE));
                return criteria;
            }
        });
        return users != null && !users.isEmpty() ? users.get(0) : null;
    }

    @Transactional(readOnly = false)
    public CommonResponse commit(User entity) {
        CommonResponse retval = new CommonResponse(false);
        try {
            User temp = findBy(entity.getId());
            if(temp == null) {
                temp = new User();
                BeanUtils.copyProperties(entity, temp);
            }
            temp.setUsername(entity.getUsername());
            temp.setPassword(entity.getPassword());
            temp.setName(entity.getName());
            temp.setPhone(entity.getPhone());
            temp.setEmail(entity.getEmail());
            temp.setStatus(entity.getStatus());
            temp.setType(entity.getType());
            temp.setModifydate(new Date());
            temp = merge(temp);
            retval.setResult(true);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return retval;
    }

}

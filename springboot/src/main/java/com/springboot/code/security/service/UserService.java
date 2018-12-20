/**
 * 
 */
package com.springboot.code.security.service;

import java.util.Date;
import java.util.List;

import com.springboot.basic.service.DefaultService;
import com.springboot.basic.support.CommonRequestAttributes;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.StringUtils;
import com.springboot.basic.utils.time.DateUtils;
import com.springboot.code.entity.User;
import com.springboot.code.security.vo.UserVO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

/**
 * 
 * @since manager 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService extends DefaultService<User, String> {

	/**
	 * @param username
	 * @return
	 */
	public User findByUsername(final String username) {
		if (StringUtils.isBlank(username)) {
			return null;
		}
		List<User> users = findBy(new AssembleCriteriaParamsCallback() {
			@Override
			public DetachedCriteria assembleParams(DetachedCriteria criteria) {
				criteria.add(Restrictions.eq("username", username));
				criteria.add(Restrictions.eq("delete", Boolean.FALSE));
				return criteria;
			}
		});
		return users != null && !users.isEmpty() ? users.get(0) : null;
	}

	/**
	 * @param attributes
	 * @param model
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public CommonResponse commithandler(CommonRequestAttributes attributes, Model model, User entity) {
		CommonResponse retval = new CommonResponse(false);
		try {
			User temp = findBy(entity.getId());
			if (temp == null) {
				temp = new User();
				BeanUtils.copyProperties(entity, temp);
			}
			temp.setModifydate(new Date());
			temp.setUsername(entity.getUsername());
			if (StringUtils.isNotBlank(entity.getPassword())) {
				temp.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));
			}
			temp.setName(entity.getName());
			temp.setEmail(entity.getEmail());
			temp.setStatus(entity.getStatus());
			temp.setType(entity.getType());
			temp = merge(temp);
			retval.setResult(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return retval;
	}
	
	/**
	 * 新增/修改用户
	 * @param entity
	 * @param isEdit
	 * @return
	 */
	@Transactional(readOnly = false)
	public CommonResponse commit(User entity, boolean isEdit) {
		CommonResponse retval = new CommonResponse(false);
		try {
			User temp = new User();
			if (StringUtils.isNotBlank(entity.getId())) {
				 temp = findBy(entity.getId());
			}
			if (temp == null) {
				temp = new User();
				BeanUtils.copyProperties(entity, temp);
			}
			temp.setModifydate(new Date());
			temp.setUsername(entity.getUsername());
			if(isEdit) {
				if (StringUtils.isNotBlank(entity.getPassword())) {
					temp.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));
				}
			}
			temp.setName(entity.getName());
			temp.setEmail(entity.getEmail());
			temp.setStatus(entity.getStatus());
			temp.setType(entity.getType());
			temp.setRoles(entity.getRoles());
			temp.setPhone(entity.getPhone());
			temp = merge(temp);
			retval.setResult(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return retval;
	}
	
	/**
	 * 删除用户
	 */
	@Transactional(readOnly=false)
	public void deleteUser(String id) {
		if (StringUtils.isNotBlank(id)) {
			// 逻辑删除
			this.delete(id);
		}
	}
	
	/**
	 * username 和 name查询
	 */
	@Transactional(readOnly=true)
	public boolean getUser(final String username, final String name) {
		List<User> roles = this.findBy(new AssembleCriteriaParamsCallback() {
			@Override
			public DetachedCriteria assembleParams(DetachedCriteria criteria) {
				if (StringUtils.isNotBlank(name)) {
					criteria.add(Restrictions.eq("name", name));
				}
				if (StringUtils.isNotBlank(username)) {
					criteria.add(Restrictions.eq("username", username));
				}
				criteria.add(Restrictions.eq("delete", Boolean.FALSE));
				return criteria;
			}
		});
		return roles != null && roles.size() > 0 ? true : false;
	}
	
	
	/**
	 * id查询
	 */
	@Transactional(readOnly=true)
	public User getUserById(final String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		List<User> users = this.findBy(new AssembleCriteriaParamsCallback() {
			@Override
			public DetachedCriteria assembleParams(DetachedCriteria criteria) {
					criteria.add(Restrictions.eq("id", id));
				criteria.add(Restrictions.eq("delete", Boolean.FALSE));
				return criteria;
			}
		});
		return users != null && users.size() > 0 ? users.get(0) : null;
	}

	public UserVO uservoSet(User user) {
		UserVO userVO = new UserVO();
		userVO.setId(user.getId() == null ? "" : user.getId());
		userVO.setName(user.getName() == null ? "" : user.getName() );
		userVO.setType(user.getType() == null ? "" : user.getType());
		userVO.setEmail(user.getEmail() == null ? "" : user.getEmail());
		userVO.setUsername(user.getUsername() == null ? "" : user.getUsername());
		userVO.setStatus(user.getStatus() == null ? "" : user.getStatus());
		userVO.setPhone(user.getPhone() == null ? "" : user.getPhone());
		userVO.setCreateDate(user.getCreatedate() == null ? "" : DateUtils.format(user.getCreatedate(),"yyyy-MM-dd"));
		userVO.setModifyDate(user.getModifydate() == null ? "" : DateUtils.format(user.getModifydate(),"yyyy-MM-dd"));
		return userVO;
	}
}

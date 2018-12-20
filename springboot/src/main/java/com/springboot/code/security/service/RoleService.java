package com.springboot.code.security.service;

import java.util.Date;
import java.util.List;

import com.springboot.basic.service.DefaultService;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.StringUtils;
import com.springboot.basic.utils.time.DateUtils;
import com.springboot.code.entity.Role;
import com.springboot.code.security.vo.RoleVO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @since manager 1.0
 * @author <a href="mailto:kklazy@live.cn">kk</a>
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleService extends DefaultService<Role, String> {

	/**
	 * 通过name和code进行模糊查询
	 * @param name 
	 * @param code
	 * @return
	 */
	public List<Role> findByUsername(final String name, final String code) {
		List<Role> roles = findBy(new AssembleCriteriaParamsCallback() {
			@Override
			public DetachedCriteria assembleParams(DetachedCriteria criteria) {
				if (StringUtils.isNotBlank(name)) {
					criteria.add(Restrictions.like("name", name.trim(), MatchMode.ANYWHERE));
				}
				if (StringUtils.isNotBlank(code)) {
					criteria.add(Restrictions.like("code", code.trim(), MatchMode.ANYWHERE));
				}
				criteria.add(Restrictions.eq("delete", Boolean.FALSE));
				return criteria;
			}
		});
		return roles != null && !roles.isEmpty() ? roles : null;
	}
	
	/**
	 * 新增/修改角色
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public CommonResponse commit(Role entity) {
		CommonResponse retval = new CommonResponse(false);
		try {
			Role temp = new Role();
			if (StringUtils.isNotBlank(entity.getId())) {
				 temp = findBy(entity.getId());
			}
			
			if (temp == null) {
				temp = new Role();
				BeanUtils.copyProperties(entity, temp);
			}
			temp.setModifydate(new Date());
			temp.setName(entity.getName());
			temp.setCode(entity.getCode());
			temp.setIcon(entity.getIcon());
			temp.setDescription(entity.getDescription());
			temp.setSort(entity.getSort());
			temp.setType(entity.getType());
			temp.setStatus(entity.getStatus());
			temp = merge(temp);
			retval.setResult(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return retval;
	}
	
	/**
	 * 删除角色
	 */
	@Transactional(readOnly=false)
	public void deleteRole(String id) {
		if (StringUtils.isNotBlank(id)) {
			// 逻辑删除
			this.delete(id);
		}
	}
	
	/**
	 * id查询
	 */
	@Transactional(readOnly=true)
	public Role getRoleById(final String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		List<Role> roles = this.findBy(new AssembleCriteriaParamsCallback() {
			@Override
			public DetachedCriteria assembleParams(DetachedCriteria criteria) {
					criteria.add(Restrictions.eq("id", id));
				criteria.add(Restrictions.eq("delete", Boolean.FALSE));
				return criteria;
			}
		});
		return roles != null && roles.size() > 0 ? roles.get(0) : null;
	}
	
	/**
	 * 角色名字 和 code查询
	 */
	@Transactional(readOnly=true)
	public boolean getRole(final String name, final String code) {
		List<Role> roles = this.findBy(new AssembleCriteriaParamsCallback() {
			@Override
			public DetachedCriteria assembleParams(DetachedCriteria criteria) {
				if (StringUtils.isNotBlank(name)) {
					criteria.add(Restrictions.eq("name", name));
				}
				if (StringUtils.isNotBlank(code)) {
					criteria.add(Restrictions.eq("code", code));
				}
				criteria.add(Restrictions.eq("delete", Boolean.FALSE));
				return criteria;
			}
		});
		return roles != null && roles.size() > 0 ? true : false;
	}
	
	public RoleVO rolevoSet(Role role) {
		RoleVO roleVO = new RoleVO();
		if(role == null) {
			return roleVO;
		}
		roleVO.setId(role.getId() == null ? "" : role.getId());
		roleVO.setCode(role.getCode() == null ? "" : role.getCode());
		roleVO.setCreateDate(role.getCreatedate() == null ? "" : DateUtils.format(role.getCreatedate(),"yyyy-MM-dd"));
		roleVO.setDescription(role.getDescription() == null ? "" : role.getDescription() );
		roleVO.setIcon(role.getIcon() == null ? "" : role.getIcon());
		roleVO.setModifyDate(role.getModifydate() == null ? "" : DateUtils.format(role.getModifydate(),"yyyy-MM-dd"));
		roleVO.setName(role.getName() == null ? "" : role.getName());
		roleVO.setSort(role.getSort() == null ? "" : role.getSort().toString());
		roleVO.setStatus(role.getStatus() == null ? "" : role.getStatus());
		roleVO.setType(role.getType() == null ? "" : role.getType());
		return roleVO;
	}
	
}

package com.springboot.code.security.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.springboot.basic.page.Sort;
import com.springboot.basic.service.DefaultService;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.StringUtils;
import com.springboot.code.entity.Resource;
import com.springboot.code.security.vo.ResourceVO;
import org.hibernate.criterion.DetachedCriteria;
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
public class ResourceService extends DefaultService<Resource, String> {

	/**
	 * @param url
	 * @return
	 */
	public Resource findByUrl(final String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		List<Resource> resources = findBy(new AssembleCriteriaParamsCallback() {
			@Override
			public DetachedCriteria assembleParams(DetachedCriteria criteria) {
				criteria.add(Restrictions.eq("url", url));
				criteria.add(Restrictions.eq("delete", Boolean.FALSE));
				return criteria;
			}
		});
		return resources != null && !resources.isEmpty() ? resources.get(0) : null;
	}

	/**
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public CommonResponse commithandler(Resource entity) {
		CommonResponse retval = new CommonResponse();
		try {
			Resource temp = null;
			if (StringUtils.isNotBlank(entity.getId())) {
				temp = findBy(entity.getId());
			} 
			if (temp == null) {
				temp = new Resource();
				BeanUtils.copyProperties(entity, temp);
			}
			if (entity.getParent() != null && StringUtils.isNotBlank(entity.getParent().getId())) {
				temp.setParent(findBy(entity.getParent().getId()));
			} else {
				temp.setParent(null);
			}
			temp.setModifydate(new Date());
			temp.setName(entity.getName());
			temp.setCode(entity.getCode());
			temp.setUrl(entity.getUrl());
			temp.setDescription(entity.getDescription());
			temp.setType(entity.getType());
			temp.setStatus(entity.getStatus());
			temp.setSort(entity.getSort());
			temp = getPageRepository().merge(temp);
			retval.setResult(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return retval;
	}
	
	/**
	 * 获取所有菜单
	 * @return
	 */
	@Transactional(readOnly = false)
	public List<Resource> findByParent() {
		List<Resource> resources = this.findBy(new AssembleCriteriaParamsCallback() {
			@Override
			public DetachedCriteria assembleParams(DetachedCriteria criteria) {
				criteria.add(Restrictions.eq("delete", Boolean.FALSE));
				return criteria;
			}
		}, new Sort(new Sort.Order(Sort.Direction.ASC, "sort"),
				new Sort.Order(Sort.Direction.ASC, "createdate")));
		return resources != null && !resources.isEmpty() ? resources : null;
	}
	
	// 获取子节点的递归方法
	public List<ResourceVO> getChildrenResource(String id, List<ResourceVO> resourceList) {
		List<ResourceVO> lists = new ArrayList<>();
		if (resourceList != null && resourceList.size() > 0) {
			for (ResourceVO resourceVO : resourceList) {
				if (id.equals(resourceVO.getParentId())) {
					// 递归获取子节点
					resourceVO.setChildren(getChildrenResource(resourceVO.getId(), resourceList));
					lists.add(resourceVO);
				}
			}
			// 排序
		}
		return lists;
	}
	
	@Transactional(readOnly = false)
	public List<Resource> findUserResource(String id) {
		List<Resource> resources = this.findBy(new AssembleCriteriaParamsCallback() {
			@Override
			public DetachedCriteria assembleParams(DetachedCriteria criteria) {
				criteria.createAlias("roles", "roles");
				criteria.add(Restrictions.eq("roles.id", id));
				criteria.add(Restrictions.eq("delete", Boolean.FALSE));
				return criteria;
			}
		});
		return resources != null && !resources.isEmpty() ? resources : null;
	}
	
	@Transactional(readOnly = false)
	public List<Resource> findResourceByParent(Resource resource) {
		List<Resource> resources = this.findBy(new AssembleCriteriaParamsCallback() {
			@Override
			public DetachedCriteria assembleParams(DetachedCriteria criteria) {
				criteria.createAlias("parent", "parent");
				criteria.add(Restrictions.eq("parent.id", resource.getId()));
				return criteria;
			}
		});
		return resources != null && !resources.isEmpty() ? resources : null;
	}

}

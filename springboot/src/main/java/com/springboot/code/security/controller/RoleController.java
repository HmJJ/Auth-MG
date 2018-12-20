package com.springboot.code.security.controller;

import java.util.*;

import com.springboot.basic.controller.DefaultRestfulController;
import com.springboot.basic.entity.SupportModel;
import com.springboot.basic.page.Page;
import com.springboot.basic.page.PageRequest;
import com.springboot.basic.page.Sort;
import com.springboot.basic.service.Service;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.StringUtils;
import com.springboot.code.entity.Resource;
import com.springboot.code.entity.Role;
import com.springboot.code.security.service.ResourceService;
import com.springboot.code.security.service.RoleService;
import com.springboot.code.security.vo.ResourceVO;
import com.springboot.code.security.vo.RoleVO;
import com.springboot.code.vo.ReturnPageVO;
import com.springboot.code.vo.VueCommonRespVO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import groovy.util.logging.Slf4j;

@Controller
@RequestMapping("role")
@Slf4j
public class RoleController extends DefaultRestfulController<SupportModel, String> {

	@Autowired
	private RoleService roleService;
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * @param roleVO
	 * @return 添加/修改角色
	 */
	@RequestMapping(value = "addRole", method = RequestMethod.POST)
	@ResponseBody
	public String addRole(@RequestBody RoleVO roleVO) {
		Date date1 = new Date();
		if (StringUtils.isBlank(roleVO.getName()) || StringUtils.isBlank(roleVO.getCode()) || StringUtils.isBlank(roleVO.getStatus()) || StringUtils.isBlank(roleVO.getType())) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "参数为空,添加失败"));
		}
		Role entity = new Role();
		if (StringUtils.isNotBlank(roleVO.getId())) {
			entity = roleService.getRoleById(roleVO.getId());
			if(!entity.getName().equals(roleVO.getName())) {
				if (roleService.getRole(roleVO.getName(), null)) {
					return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "角色名称已存在,修改失败"));
				}
			}
			if(!entity.getCode().equals(roleVO.getCode())) {
				if (roleService.getRole(null, roleVO.getCode())) {
					return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "角色代码已存在,修改失败"));
				}
			}
		} else {
			if (roleService.getRole(null, roleVO.getCode())) {
				return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "角色代码已存在,添加失败"));
			}
			if (roleService.getRole(roleVO.getName(), null)) {
				return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "角色名称已存在,添加失败"));
			}
		}
		// 封装role
		entity.setId(roleVO.getId());
		entity.setName(roleVO.getName());
		entity.setCode(roleVO.getCode());
		entity.setDescription(roleVO.getDescription());
		entity.setIcon(roleVO.getIcon());
		entity.setSort(Integer.parseInt(roleVO.getSort()));
		entity.setStatus(roleVO.getStatus());
		entity.setType(roleVO.getType());
		CommonResponse commonResponse = roleService.commit(entity);
		if (commonResponse.isResult()) {
			Date date2 = new Date();
			System.out.println("添加/修改角色用时："+(date2.getTime() - date1.getTime()));
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.TRUE, "操作成功"));
		}
		Date date2 = new Date();
		System.out.println("添加/修改角色用时："+(date2.getTime() - date1.getTime()));
		return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "操作失败"));
	}
	
	
	/**
	 * 角色查询
	 * @param name	角色名称
	 * @param code	角色代码
	 * @param pageNum	页码
	 * @param pageSize	条数
	 * @return
	 */
	@RequestMapping(value = "queryRolePage")
	@ResponseBody
	public String search(@RequestParam String name, String code, int pageNum, int pageSize) {
		Date date1 = new Date();
		Page<Role> page = null;
		ReturnPageVO<RoleVO> emPageVO = new ReturnPageVO<>();
		try {
			page = roleService.pageBy(new Service.AssembleCriteriaParamsCallback() {
				@Override
				public DetachedCriteria assembleParams(DetachedCriteria criteria) {
					if (StringUtils.isNotBlank(name)) {
						criteria.add(Restrictions.like("name", name.trim(), MatchMode.ANYWHERE));
					}
					if (StringUtils.isNotBlank(code)) {
						criteria.add(Restrictions.like("code", name.trim(), MatchMode.ANYWHERE));
					}
					criteria.add(Restrictions.eq("delete", Boolean.FALSE));
					return criteria;
				}
			}, new Sort(new Sort.Order(Sort.Direction.DESC, "createdate")),
					new PageRequest(pageNum - 1, pageSize));
			List<RoleVO> roleVOs = new ArrayList<>();
			if (page != null && page.hasContent()) {
				for(int i = 0; i < page.getContent().size(); i++) {
					RoleVO roleVO = new RoleVO();
					 roleVO = roleService.rolevoSet(page.getContent().get(i));
					roleVOs.add(roleVO);
				}
			}
			emPageVO.setNumber(page.getNumber());
			emPageVO.setSize(page.getSize());
			emPageVO.setTotalPages(page.getTotalPages());
			emPageVO.setNumberOfElements(page.getNumberOfElements());
			emPageVO.setTotalElements(page.getTotalElements());
			emPageVO.setHasPreviousPage(page.hasPreviousPage());
			emPageVO.setFirstPage(page.isFirstPage());
			emPageVO.setHasNextPage(page.hasNextPage());
			emPageVO.setLastPage(page.isLastPage());
			emPageVO.setContent(roleVOs);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		Date date2 = new Date();
		System.out.println("角色查询用时："+(date2.getTime() - date1.getTime()));
		return JSON.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, emPageVO, "query success"));
	}
	
	/**
	 * 角色查询 不分页
	 * @return
	 */
	@RequestMapping(value = "queryRole")
	@ResponseBody
	public String search() {
		Date date1 = new Date();
		List<Role> roles = roleService.findAll();
		List<RoleVO> roleVOs = new ArrayList<>();
		if(roles.isEmpty()) {
			roleVOs = null;
		} else {
		
			for(Role role : roles) {
				RoleVO roleVO = new RoleVO();
				 roleVO = roleService.rolevoSet(role);
				roleVOs.add(roleVO);
			}
		}
		Date date2 = new Date();
		System.out.println("角色查询 不分页用时："+(date2.getTime() - date1.getTime()));
		return JSON.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, roleVOs, "query success"));
	}
	
	/**
	 * 逻辑删除角色
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "deleteRole", method = RequestMethod.POST)
	@ResponseBody
	public String deleteRole(@RequestParam String id) {
		roleService.deleteRole(id);
		return JSON.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.TRUE, "delete success"));
	}
	
	/**
	 * 修改状态
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "modifyRoleStatus")
	@ResponseBody
	public String modifyRoleStatus(@RequestParam String id, String status) {
		Date date1 = new Date();
		if (StringUtils.isBlank(id) || StringUtils.isBlank(status)) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "参数为空,修改失败"));
		}
		Role entity = roleService.getRoleById(id);
		if(entity == null) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "不存在该角色,修改失败"));
		}
		
		entity.setStatus(status);
		CommonResponse commonResponse = roleService.commit(entity);
		if (commonResponse.isResult()) {
			Date date2 = new Date();
			System.out.println("修改角色状态用时:"+(date2.getTime() - date1.getTime()));
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.TRUE, "操作成功"));
		}
		Date date2 = new Date();
		System.out.println("修改角色状态用时:"+(date2.getTime() - date1.getTime()));
		return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "操作失败"));
	}
	
	@RequestMapping(value = "getRoleById")
	@ResponseBody
	public String getRoleById(@RequestParam String id) {
		Role role = roleService.getRoleById(id);
		RoleVO roleVO = new RoleVO();
		roleVO = roleService.rolevoSet(role);
		return JSON.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, roleVO, "query success"));
	}
	
	/**
	 * 分配资源	提交	  最好是在前台优化或者用队列优化
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "addOrModifyPermission", method = RequestMethod.POST)
	@ResponseBody
	public String addOrModifyPermission(@RequestParam String id, @RequestParam(value = "list[]") String[] list) {
		Date date1 = new Date();
		Role role = roleService.findBy(id);
		List<Resource> resources = new ArrayList<>();
		Set<Resource> res = new HashSet<>();
		if (list != null && list.length > 0) {
			List<Resource> all = new ArrayList<>();
			// 查询出所有资源实体
			Arrays.asList(list).forEach((resourceId) -> {
				if (StringUtils.isNotBlank(resourceId)) {
					Resource resource = resourceService.findBy(resourceId);
					all.add(resource);
				}
			});
			// 将没有点击子类的父类分离出来
			all.forEach((entity) -> {
				if (entity.getParent() != null) {
					res.add(entity);
					recursive(res, entity, all);
				}
			});
//			all.removeAll(res);
			// 添加分离父类所有的子类
			recursiveByParent(res, all);
			res.addAll(all);
			res.forEach((resource) -> resources.add(resource));
		}
		role.setResources(resources);
		roleService.merge(role);
		Date date2 = new Date();
		System.out.println(" 分配资源用时："+(date2.getTime() - date1.getTime()));
		return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, id, "操作成功"));
	}
	
	public void recursive(Set<Resource> resources, Resource resource, List<Resource> all) {
		if (resource.getParent() != null) {
			//子类
			Resource parent = resource.getParent();
			resources.add(parent);
			if (all.contains(parent)) {
				all.remove(parent);
			}
			if (parent.getParent() != null) {
				recursive(resources, parent, all);
			}
		}
	}
	
	public void recursiveByParent(Set<Resource> resources, List<Resource> all) {
		boolean temp = true;
		if (all != null && all.size() > 0) {
			for (Resource resource : all) {
				List<Resource> rList = resourceService.findResourceByParent(resource);
				if (rList != null && !rList.isEmpty() && rList.size() > 0) {
					for (Resource resource2 : rList) {
						if (all.contains(resource2)) {
							temp = false;
						}
					}
					if (temp) {
						resources.addAll(rList);
					}
					this.recursiveByParent(resources, rList);
				}
			}
		}
	}
	
	/**
	 * 取出该角色所拥有的资源
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getUserResource", method = RequestMethod.POST)
	@ResponseBody
	public String getUserResource(@RequestParam String id) {
		Date date1 = new Date();
		if (StringUtils.isBlank(id)) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_FAILURE, null, "操作失败"));
		}
		List<Resource> resources = resourceService.findUserResource(id);
		List<ResourceVO> resourceVOs = new ArrayList<>();
		if (resources != null && resources.size() > 0) {
			for (Resource resource : resources) {
				ResourceVO resourceVO = new ResourceVO();
				resourceVO.setId(resource.getId());
//				resourceVO.setSelected(true);
				resourceVOs.add(resourceVO);
			}
		}
		Date date2 = new Date();
		System.out.println("取出该角色所拥有的资源用时："+(date2.getTime() - date1.getTime()));
		return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, resourceVOs, "操作成功"));
	}
	
	
}

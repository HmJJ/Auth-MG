package com.springboot.code.security.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.springboot.basic.controller.DefaultRestfulController;
import com.springboot.basic.entity.SupportModel;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.StringUtils;
import com.springboot.code.entity.Resource;
import com.springboot.code.security.service.ResourceService;
import com.springboot.code.security.vo.ResourceVO;
import com.springboot.code.utils.BCsiteConstants;
import com.springboot.code.vo.VueCommonRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import groovy.util.logging.Slf4j;
import lombok.Getter;

@Controller
@RequestMapping("resource")
@Slf4j
public class ResourceController extends DefaultRestfulController<SupportModel, String> {
	@Autowired @Getter private ResourceService resourceService;
	
	/**
	 * 添加/修改字节点	添加一级菜单
	 * @param resourceVO
	 * @return
	 */
	@RequestMapping(value = "addOrModifyResource", method = RequestMethod.POST)
	@ResponseBody
	public String addOrModifyResource(@RequestBody ResourceVO resourceVO){
		Resource entity = new Resource();
		entity.setId(resourceVO.getId());
//		entity.setParent(resourceVO.getParent());
		if (StringUtils.isNotBlank(resourceVO.getParentId())) {
			entity.setParent(resourceService.findBy(resourceVO.getParentId()));
		}else {
			entity.setParent(null);
		}
		entity.setName(resourceVO.getTitle());
		entity.setCode(resourceVO.getCode());
		entity.setUrl(resourceVO.getUrl());
		entity.setDescription(resourceVO.getDescription());
		if (Resource.TYPE_MENU.equals(resourceVO.getType())
				|| Resource.TYPE_BUTTON.equals(resourceVO.getType())
				|| Resource.TYPE_URL.equals(resourceVO.getType())) {
			entity.setType(resourceVO.getType());
		}
		if (BCsiteConstants.RESOURCE_STATUS_YES.equals(resourceVO.getStatus())
				|| BCsiteConstants.RESOURCE_STATUS_NO.equals(resourceVO.getStatus())) {
			entity.setStatus(resourceVO.getStatus());
		}else {
			entity.setStatus(BCsiteConstants.RESOURCE_STATUS_NO);
		}
		entity.setSort(resourceVO.getSort());
		CommonResponse retval = resourceService.commithandler(entity);
		if (retval.isResult()) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, resourceVO, "操作成功"));
		}
		return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, null, "操作失败"));
	}
	
	/**
	 * 构造多叉树
	 * @return
	 */
	@RequestMapping(value = "queryResource", method = RequestMethod.POST)
	@ResponseBody
	public String queryResource() {
		List<Resource> resources = resourceService.findByParent();
		List<ResourceVO> resourceVOs = new ArrayList<>();
		if (resources == null) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, resources, "操作成功"));
		}
		// 把resources变成vo
		for (Resource resource : resources) {
			ResourceVO resourceVO = new ResourceVO();
			resourceVO.setId(resource.getId());
			resourceVO.setTitle(resource.getName());
			resourceVO.setCode(resource.getCode());
			resourceVO.setUrl(resource.getUrl());
			resourceVO.setDescription(resource.getDescription());
			resourceVO.setType(resource.getType());
			if (resource != null && resource.getParent() != null) {
				resourceVO.setParentId(resource.getParent().getId());
			}
			resourceVO.setStatus(resource.getStatus());
			resourceVO.setSort(resource.getSort());
			resourceVO.setSelected(false);
			resourceVOs.add(resourceVO);
		}
		List<ResourceVO> result = new ArrayList<>();
		for (ResourceVO resourceVO : resourceVOs) {
			if (resourceVO != null && StringUtils.isBlank(resourceVO.getParentId())) {
				resourceVO.setChildren(resourceService.getChildrenResource(resourceVO.getId(), resourceVOs));
				result.add(resourceVO);
			}
		}
		return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, result, "操作成功"));
	}
	
	/**
	 * 批量删除(逻辑删除)
	 * @param selectList
	 * @return
	 */
	@RequestMapping(value = "deletePermission", method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestParam(value = "selectList[]") String[] selectList) {
		if (selectList != null && selectList.length > 0) {
			Arrays.asList(selectList).forEach(id -> resourceService.delete(id));
		} else {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_FAILURE, null, "发生错误"));
		}
		return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, selectList, "操作成功"));
	}
	
}

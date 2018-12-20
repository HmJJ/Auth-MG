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
import com.springboot.code.entity.Role;
import com.springboot.code.entity.User;
import com.springboot.code.security.service.RoleService;
import com.springboot.code.security.service.UserService;
import com.springboot.code.security.vo.CustomUserDetails;
import com.springboot.code.security.vo.RoleVO;
import com.springboot.code.security.vo.UserVO;
import com.springboot.code.vo.ReturnPageVO;
import com.springboot.code.vo.VueCommonRespVO;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import groovy.util.logging.Slf4j;

@Controller
@RequestMapping("user")
@Slf4j
public class UserController extends DefaultRestfulController<SupportModel, String> {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(value = { "getUserInfo" }, produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getUserInfo(@RequestParam String token, Model model) {
		CustomUserDetails details = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (details == null) {
			return null;
		}
		User user = details.getUser();
		JSONObject data = new JSONObject();
		data.put("name", user.getName());
		data.put("username", user.getUsername());
		data.put("user_id", user.getId());
		JSONArray list = new JSONArray();
		list.add("super_admin");
		list.add("admin");
		data.put("access", list);
		data.put("token", token);
		data.put("avator", "https://file.iviewui.com/dist/a0e88e83800f138b94d2414621bd9704.png");
		VueCommonRespVO respVO = new VueCommonRespVO(data);
		return JSONObject.toJSONString(respVO);
	}
	
	/**
	 * @return 添加/修改用户
	 * @param uservo
	 */
	@RequestMapping(value = "addUser", method = RequestMethod.POST)
	@ResponseBody
	public String addUser(@RequestBody UserVO uservo) {
		if(StringUtils.isBlank(uservo.getUsername()) || StringUtils.isBlank(uservo.getName())) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "参数不正确或者为空，添加失败"));
		}
		User user = new User();
		boolean isedit  = false;
		if(StringUtils.isNotBlank(uservo.getId())) {
			user = userService.getUserById(uservo.getId());
			if(!user.getUsername().equals(uservo.getUsername())){
				if (userService.getUser(uservo.getUsername(), null)) {
					return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "该用户名已存在,修改失败"));
				}
			}
		} else {
			isedit  = true;
			if(StringUtils.isBlank(uservo.getPassword())) {
				return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "参数不正确或者为空，添加失败"));
			}
			if (userService.getUser(uservo.getUsername(), null)) {
				return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "该用户名已存在,添加失败"));
			}
		}
		
		user.setId(uservo.getId());
		user.setUsername(uservo.getUsername());
		user.setName(uservo.getName());
		if(StringUtils.isNotBlank(uservo.getPassword())) {
			isedit = true;
			user.setPassword(uservo.getPassword());
		}
		user.setEmail(uservo.getEmail());
		user.setPhone(uservo.getPhone());
		user.setStatus(uservo.getStatus());
		user.setType(uservo.getType());
		/*if( !uservo.getRoles().isEmpty()) {
			Set<Role> roles = new HashSet<Role>(uservo.getRoles());
			user.setRoles(roles);
		}else {
			user.setRoles(null);
		}*/
		CommonResponse commonResponse = userService.commit(user, isedit);
		if (commonResponse.isResult()) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.TRUE, "操作成功"));
		}
		return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "操作失败"));
	}
	
	
	/**
	 * 用户查询
	 * @param username	员工姓名
	 * @param pageNum	页码
	 * @param pageSize	条数
	 * @return
	 */
	@RequestMapping(value = "queryUser")
	@ResponseBody
	public String search(@RequestParam String username, String name, int pageNum, int pageSize) {
		Page<User> page = null;
		ReturnPageVO<UserVO> emPageVO = new ReturnPageVO<>();
		try {
			page = userService.pageBy(new Service.AssembleCriteriaParamsCallback() {
				@Override
				public DetachedCriteria assembleParams(DetachedCriteria criteria) {
					criteria.setFetchMode("roles", FetchMode.SELECT);
					if (StringUtils.isNotBlank(name)) {
						criteria.add(Restrictions.like("name", name.trim(), MatchMode.ANYWHERE));
					}
					if (StringUtils.isNotBlank(username)) {
						criteria.add(Restrictions.like("username", username.trim(), MatchMode.ANYWHERE));
					}
					criteria.add(Restrictions.eq("delete", Boolean.FALSE));
					return criteria;
				}
			}, new Sort(new Sort.Order(Sort.Direction.DESC, "createdate")),
					new PageRequest(pageNum - 1, pageSize));
			List<UserVO> userVOs = new ArrayList<>();
			if (page != null && page.hasContent()) {
				for(int i = 0; i < page.getContent().size(); i++) {
					UserVO userVO = new UserVO();
					userVO = userService.uservoSet(page.getContent().get(i));
					userVOs.add(userVO);
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
			emPageVO.setContent(userVOs);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return JSON.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, emPageVO, "query success"));
	}
	
	/**
	 * 逻辑删除角色
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "deleteUser", method = RequestMethod.POST)
	@ResponseBody
	public String deleteRole(@RequestParam String id) {
		userService.deleteUser(id);
		return JSON.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.TRUE, "delete success"));
	}
	
	/**
	 * 修改状态
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "modifyUserStatus")
	@ResponseBody
	public String modifyRoleStatus(@RequestParam String id, String status) {
		if (StringUtils.isBlank(id) || StringUtils.isBlank(status)) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "参数为空,修改失败"));
		}
		User entity = userService.getUserById(id);
		if(entity == null) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "不存在该角色,修改失败"));
		}
		
		entity.setStatus(status);
		CommonResponse commonResponse = userService.commit(entity, false);
		if (commonResponse.isResult()) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.TRUE, "操作成功"));
		}
		return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "操作失败"));
	}
	
	
	/**
	 * 修改密码
	 * @param id
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "modifyUserPassword")
	@ResponseBody
	public String modifyUserPassword(@RequestParam String id, String password) {
		if (StringUtils.isBlank(id) || StringUtils.isBlank(password)) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "参数为空,修改失败"));
		}
		User entity = userService.getUserById(id);
		if(entity == null) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "不存在该角色,修改失败"));
		}
		
		entity.setPassword(password);
		CommonResponse commonResponse = userService.commit(entity, true);
		if (commonResponse.isResult()) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.TRUE, "操作成功"));
		}
		return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.FALSE, "操作失败"));
	}
	
	/**
	 * 通过id查找用户拥有的角色
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getUserRoleById")
	@ResponseBody
	public String getUserRoleById(@RequestParam String id) {
		User user = userService.getUserById(id);
		List<RoleVO> roleVOs = new ArrayList<>();
		if(user.getRoles().isEmpty()) {
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, roleVOs, "操作成功")); 
		}else {
			for(Role role : user.getRoles()) {
				RoleVO roleVO = new RoleVO();
			    roleVO = roleService.rolevoSet(role);
				roleVOs.add(roleVO);
			}
			return JSONObject.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, roleVOs, "操作成功"));
		}
	}
	
	/**
	 * 给用户添加角色
	 */
	@RequestMapping(value = "grandRole")
	@ResponseBody
	public String grandRole(@RequestParam String id, @RequestParam(value = "checkAllGroup[]") String[] checkAllGroup) {
		User user = userService.getUserById(id);
		Set<Role> roles = new HashSet<>();
		if (checkAllGroup != null && checkAllGroup.length > 0) {
			for(int i = 0;i < checkAllGroup.length;i++) {
				if (StringUtils.isNotBlank(checkAllGroup[i])) {
					Role role = roleService.getRoleById(checkAllGroup[i]);
					if (role != null) {
						roles.add(role);
					}
				}
			}
		}
		user.setRoles(roles);
		userService.merge(user);
		return JSON.toJSONString(new VueCommonRespVO(VueCommonRespVO.CODE_SUCCESS, Boolean.TRUE, "success"));
	}
	

}

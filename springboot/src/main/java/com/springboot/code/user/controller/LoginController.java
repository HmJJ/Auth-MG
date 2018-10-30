package com.springboot.code.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.springboot.basic.controller.DefaultRestfulController;
import com.springboot.entity.security.User;
import com.springboot.code.user.service.LoginService;
import com.springboot.code.user.vo.UserVO;
import com.springboot.entity.vo.VueCommonRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user")
@Slf4j
public class LoginController extends DefaultRestfulController<User, String>{

    @Autowired
    private LoginService loginService;

    public static Map<Object, Object> data = new HashMap<Object, Object>();


    /**
     * 用户详情
     *
     * @param userVO
     * @return
     */
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public String detail(@RequestBody UserVO userVO) {
        log.info("find detail start");
        VueCommonRespVO retval = new VueCommonRespVO();
        Boolean flag = false;
        User user = loginService.findById(userVO.getId());
        data.clear();
        data.put("user", user);
        retval.setData(data);
        if(flag) {
            retval.setCode(VueCommonRespVO.CODE_SUCCESS);
            retval.setMsg("查找成功");
        }else {
            retval.setCode(VueCommonRespVO.CODE_FAILURE);
            retval.setMsg("查找失败");
        }
        log.info("find detail end");
        return JSONObject.toJSONString(retval);
    }

    /**
     * 用户登录
     *
     * @param userVO
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String checkLogin(@RequestBody UserVO userVO) {
        log.info("check login start");
        VueCommonRespVO retval = new VueCommonRespVO();
        Boolean flag = false;
        User user = loginService.findByUserInfo(userVO);
        data.clear();
        data.put("user", user);
        retval.setData(data);
        if(flag) {
            retval.setCode(VueCommonRespVO.CODE_SUCCESS);
            retval.setMsg("登录成功");
        }else {
            retval.setCode(VueCommonRespVO.CODE_FAILURE);
            retval.setMsg("用户名或密码错误");
        }
        log.info("check login end");
        return JSONObject.toJSONString(retval);
    }

    /**
     * 用户注册
     *
     * @param userVO
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String register(@RequestBody UserVO userVO) {
        log.info("check register start");
        VueCommonRespVO retval = new VueCommonRespVO();
        Boolean flag = false;
        flag = commit(userVO);
        data.clear();
        data.put("userVO", userVO);
        if(flag) {
            retval.setCode(VueCommonRespVO.CODE_SUCCESS);
            retval.setMsg("注册成功");
        }else {
            retval.setCode(VueCommonRespVO.CODE_FAILURE);
            retval.setMsg("注册失败");
        }
        log.info("check register end");
        return JSONObject.toJSONString(retval);
    }

    /**
     * 用户修改
     *
     * @param userVO
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String modify(@RequestBody UserVO userVO) {
        log.info("check register start");
        VueCommonRespVO retval = new VueCommonRespVO();
        Boolean flag = false;
        flag = commit(userVO);
        data.clear();
        data.put("userVO", userVO);
        if(flag) {
            retval.setCode(VueCommonRespVO.CODE_SUCCESS);
            retval.setMsg("修改成功");
        }else {
            retval.setCode(VueCommonRespVO.CODE_FAILURE);
            retval.setMsg("修改失败");
        }
        log.info("check register end");
        return JSONObject.toJSONString(retval);
    }

    public boolean commit(UserVO userVO) {
        Boolean flag = false;

        flag = loginService.commit(userVO);

        return flag;
    }

}

package com.springboot.quartz.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.springboot.basic.controller.DefaultRestfulController;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.BeanUtils;
import com.springboot.quartz.config.QuartzSchedule;
import com.springboot.quartz.entity.Quartz;
import com.springboot.quartz.service.QuartzService;
import com.springboot.quartz.vo.QuartzVO;

@Controller
@RequestMapping("/quartz")
@CrossOrigin(origins="http://localhost:8080",maxAge=3600)
public class QuartzController extends DefaultRestfulController<Quartz, String> {

	@Autowired
	private QuartzSchedule quartzScheduler;

	@Autowired
	private QuartzService quartzService;

	private static ArrayList<Quartz> quartzs = new ArrayList<>();
	private static ArrayList<QuartzVO> quartzVOs = new ArrayList<>();
	private static Map<String, Object> map = new HashMap<>();

	@RequestMapping(value = "/getAll", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getAll() {
		CommonResponse retval = new CommonResponse(false);
		try {
			getAllTasks();
			map.clear();
			map.put("quartzVOs", quartzVOs);
			retval.setMessage("所有任务已列出");
			retval.setResult(true);
			retval.setData(map);
			retval.setCode("200");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return JSON.toJSONString(retval);
	}
	
	@RequestMapping(value = "/start", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String startQuartzJob() {
		CommonResponse retval = new CommonResponse(false);
		try {
			quartzScheduler.startJob();
			System.out.println("所有任务已启动");
			getAllTasks();
			map.clear();
			map.put("quartzVOs", quartzVOs);
			retval.setMessage("所有任务已启动");
			retval.setResult(true);
			retval.setData(map);
			retval.setCode("200");
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
		}
		return JSON.toJSONString(retval);
	}

	@RequestMapping(value = "/resumeJob", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String resumeJob(@RequestParam(name = "name", required = true) String name, @RequestParam(name = "group", required = true) String group) {
		CommonResponse retval = new CommonResponse(false);
		try {
			quartzScheduler.resumeJob(name, group);
			System.out.println("任务" + name + "已恢复");
			getAllTasks();
			map.clear();
			map.put("status", "正在运行");
			map.put("quartzVOs", quartzVOs);
			retval.setMessage("任务" + name + "已恢复");
			retval.setResult(true);
			retval.setData(map);
			retval.setCode("200");
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
		}
		return JSON.toJSONString(retval);
	}

	@RequestMapping(value = "/resumeAllJob", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String resumeAllJob() {
		CommonResponse retval = new CommonResponse(false);
		try {
			quartzScheduler.resumeAllJob();
			System.out.println("所有任务已恢复");
			getAllTasks();
			map.clear();
			map.put("quartzVOs", quartzVOs);
			retval.setMessage("所有任务已恢复");
			retval.setResult(true);
			retval.setData(map);
			retval.setCode("200");
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
		}
		return JSON.toJSONString(retval);
	}

	@RequestMapping(value = "/info", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getQuartzJob(@RequestParam(name = "name", required = true) String name, @RequestParam(name = "group", required = true) String group) {
		CommonResponse retval = new CommonResponse(false);
		String info = null;
		try {
			info = quartzScheduler.getJobInfo(name, group);
			getAllTasks();
			retval.setMessage("任务" + name + "已暂停");
			retval.setResult(true);
			map.clear();
			map.put("info", info);
			map.put("quartzVOs", quartzVOs);
			retval.setData(map);
			retval.setCode("200");
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
		}
		return JSON.toJSONString(retval);
	}

	@RequestMapping(value = "/modify", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String modifyQuartzJob(@RequestParam(name = "name", required = true) String name, @RequestParam(name = "group", required = true) String group, @RequestParam(name = "cron", required = true) String cron) {
		CommonResponse retval = new CommonResponse(false);
		boolean flag = true;
		try {
			flag = quartzScheduler.modifyJob(name, group, cron);
			if(flag) {
				System.out.println("任务" + name + "已修改");
				quartzScheduler.resumeJob(name, group);
				getAllTasks();
				map.clear();
				map.put("status", "正在运行");
				map.put("quartzVOs", quartzVOs);
				retval.setMessage("任务" + name + "已修改");
				retval.setResult(true);
				retval.setData(map);
				retval.setCode("200");
			}else {
				map.clear();
				map.put("quartzVOs", quartzVOs);
				retval.setMessage("输入的时间重复或格式错误");
				retval.setResult(false);
				retval.setCode("200");
				retval.setData(map);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			retval.setMessage("输入的时间格式错误");
			retval.setCode("200");
		}
		return JSON.toJSONString(retval);
	}

	@RequestMapping(value = "/pause", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String pauseQuartzJob(@RequestParam(name = "name", required = true) String name, @RequestParam(name = "group", required = true) String group) {
		CommonResponse retval = new CommonResponse(false);
		try {
			quartzScheduler.pauseJob(name, group);
			System.out.println("任务" + name + "已暂停");
			getAllTasks();
			map.clear();
			map.put("status", "已停止");
			map.put("quartzVOs", quartzVOs);
			retval.setMessage("任务" + name + "已暂停");
			retval.setResult(true);
			retval.setData(map);
			retval.setCode("200");
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
		}
		return JSON.toJSONString(retval);
	}

	@RequestMapping(value = "/pauseAll", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String pauseAllQuartzJob() {
		CommonResponse retval = new CommonResponse(false);
		try {
			quartzScheduler.pauseAllJob();
			System.out.println("所有任务已暂停");
			getAllTasks();
			map.clear();
			map.put("quartzVOs", quartzVOs);
			retval.setMessage("所有任务已暂停");
			retval.setResult(true);
			retval.setData(map);
			retval.setCode("200");
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
		}
		return JSON.toJSONString(retval);
	}

	@RequestMapping(value = "/delete", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String deleteJob(@RequestParam(name = "name", required = true) String name, @RequestParam(name = "group", required = true) String group) {
		CommonResponse retval = new CommonResponse(false);
		try {
			quartzScheduler.deleteJob(name, group);
			System.out.println("任务" + name + "已删除");
			getAllTasks();
			map.clear();
			map.put("status", "已删除");
			map.put("quartzVOs", quartzVOs);
			retval.setMessage("任务" + name + "已删除");
			retval.setResult(true);
			retval.setData(map);
			retval.setCode("200");
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
		}
		return JSON.toJSONString(retval);
	}

	private void getAllTasks() {
		quartzs = (ArrayList<Quartz>) quartzService.findAll();
		quartzVOs.clear();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			for (Quartz q : quartzs) {
				QuartzVO qVO = new QuartzVO();
				BeanUtils.copyProperties(qVO, q);
				qVO.setCreatedate(sdf.format(q.getCreatedate()));
				qVO.setModifydate(sdf.format(q.getModifydate()));
				quartzVOs.add(qVO);
			}
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage());
		}
	}

}

package com.springboot.quartz.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.basic.service.DefaultService;
import com.springboot.basic.utils.BeanUtils;
import com.springboot.quartz.entity.Quartz;


@Service("quartzService")
public class QuartzService extends DefaultService<Quartz, String>{
	
	public Quartz findByName(final String name) {
		List<Quartz> jobs = findBy(new AssembleCriteriaParamsCallback() {
			
			@Override
			public DetachedCriteria assembleParams(DetachedCriteria criteria) {
				criteria.add(Restrictions.eq("name", name));
				criteria.add(Restrictions.eq("delete", Boolean.FALSE));
				return criteria;
			}
		});
		return jobs.isEmpty() ? null : jobs.get(0);
	}
	
	@Transactional(readOnly=false)
	public void commit(Quartz entity) {
		try {
			Quartz temp = findByName(entity.getName());
			if(temp == null) {
				temp = new Quartz();
			}
			BeanUtils.copyProperties(temp, entity);
			merge(temp);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@Transactional(readOnly=false)
	public void modify(String name, String status) {
		try {
			Quartz temp = findByName(name);
			if(temp == null) {
				temp = new Quartz();
			}
			temp.setStatus(status);
			temp.setModifydate(new Date());
			merge(temp);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@Transactional(readOnly=false)
	public void modifyCron(String name, String cron) {
		try {
			Quartz temp = findByName(name);
			if(temp == null) {
				temp = new Quartz();
			}
			temp.setCron(cron);
			temp.setModifydate(new Date());
			merge(temp);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
}


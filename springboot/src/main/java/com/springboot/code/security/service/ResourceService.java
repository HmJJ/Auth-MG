package com.springboot.code.security.service;

import com.springboot.basic.service.DefaultService;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.BeanUtils;
import com.springboot.basic.utils.StringUtils;
import com.springboot.entity.security.Resource;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("resourceService")
@Transactional(rollbackFor = Exception.class)
public class ResourceService extends DefaultService<Resource, String> {

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

    @Transactional(readOnly = false)
    public CommonResponse commit(Resource entity) {
        CommonResponse retval = new CommonResponse(false);
        try {
            Resource temp = findBy(entity.getId());
            if(temp == null) {
                temp = new Resource();
                BeanUtils.copyProperties(entity, temp);
            }
            if (entity.getParent() != null && StringUtils.isNotBlank(entity.getParent().getId())) {
                temp.setParent(findBy(entity.getParent().getId()));
            } else {
                temp.setParent(null);
            }
            temp.setModifydate(new Date());
            temp.setCode(entity.getCode());
            temp.setUrl(entity.getUrl());
            temp.setDescription(entity.getDescription());
            temp.setType(entity.getType());
            temp.setStatus(entity.getStatus());
            temp.setSort(entity.getSort());
            temp = getPageRepository().merge(temp);
            retval.setResult(true);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return retval;
    }

}

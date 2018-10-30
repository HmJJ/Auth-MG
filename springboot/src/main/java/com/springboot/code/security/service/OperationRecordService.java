package com.springboot.code.security.service;

import com.springboot.basic.service.DefaultService;
import com.springboot.basic.support.CommonResponse;
import com.springboot.basic.utils.BeanUtils;
import com.springboot.entity.security.OperatingRecord;
import com.springboot.entity.security.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("operationRecordService")
@Transactional(rollbackFor = Exception.class)
public class OperationRecordService extends DefaultService<OperatingRecord, String> {

    public OperatingRecord findByUser(final User user) {
        if (user == null) {
            return null;
        }
        List<OperatingRecord> operatingRecords = findBy(new AssembleCriteriaParamsCallback() {
            @Override
            public DetachedCriteria assembleParams(DetachedCriteria criteria) {
                criteria.add(Restrictions.eq("user", user));
                criteria.add(Restrictions.eq("delete", Boolean.FALSE));
                return criteria;
            }
        });
        if (operatingRecords != null && !operatingRecords.isEmpty()) {
            return operatingRecords.get(0);
        } else {
            return null;
        }
    }

    @Transactional(readOnly = false)
    public CommonResponse commit(OperatingRecord entity) {
        CommonResponse retval = new CommonResponse(false);
        try {
            OperatingRecord temp = findBy(entity.getId());
            if (temp == null) {
                temp = new OperatingRecord();
                BeanUtils.copyProperties(entity, temp);
            }
            temp.setUser(entity.getUser());
            temp.setRole(entity.getRole());
            temp.setResource(entity.getResource());
            temp.setModifydate(new Date());
            temp = merge(temp);
            retval.setResult(true);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return retval;
    }

}

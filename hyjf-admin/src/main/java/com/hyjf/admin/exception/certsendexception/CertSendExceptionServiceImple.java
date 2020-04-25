/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.exception.certsendexception;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.datacenter.certreportlog.CertReportLogBean;
import com.hyjf.admin.datacenter.certreportlog.CertReportLogService;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.CertErrLog;
import com.hyjf.mybatis.model.auto.CertErrLogExample;
import com.hyjf.mybatis.model.auto.CertLog;
import com.hyjf.mybatis.model.auto.CertLogExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CertSendExceptionServiceImple extends BaseServiceImpl implements CertSendExceptionService {


    /**
     * 根据分页查找数据
     *
     * @param limtStart
     * @param limtEnd
     * @param form
     * @return
     */
    @Override
    public List<CertErrLog> selectCertErrLog(int limtStart, int limtEnd, CertSendExceptionBean form) {
        CertErrLogExample example = new CertErrLogExample();
        CertErrLogExample.Criteria creteria = example.createCriteria();
        if (null != form) {
            if (StringUtils.isNotEmpty(form.getSendStartTimeStr())) {
                Integer startTime = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getSendStartTimeStr()+" 00:00:00");
                creteria.andSendTimeGreaterThanOrEqualTo(startTime);
            }
            if (StringUtils.isNotEmpty(form.getSendEndtTimeStr())) {
                Integer endTime = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getSendStartTimeStr()+" 23:59:59");
                creteria.andSendTimeLessThanOrEqualTo(endTime);
            }
            if (form.getInfType() != null && form.getInfType().intValue() >= 0) {
                creteria.andInfTypeEqualTo(form.getInfType());
            }
            if (form.getSendStatus() != null && form.getSendStatus().intValue() > 0) {
                creteria.andSendStatusEqualTo(form.getSendStatus());
            }
            if (StringUtils.isNotEmpty(form.getLogOrdId())) {
                creteria.andLogOrdIdEqualTo(form.getLogOrdId());
            }
        }
        example.setLimitStart(limtStart);
        example.setLimitEnd(limtEnd);
        example.setOrderByClause(" send_time DESC");
        return certErrLogMapper.selectByExample(example);
    }

    /**
     * 查找数量
     *
     * @param form
     * @return
     */
    @Override
    public Integer selectCertErrLogCount(CertSendExceptionBean form) {
        CertErrLogExample example = new CertErrLogExample();
        CertErrLogExample.Criteria creteria = example.createCriteria();
        if (null != form) {
            if (StringUtils.isNotEmpty(form.getSendStartTimeStr())) {
                Integer startTime = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getSendStartTimeStr()+" 00:00:00");
                creteria.andSendTimeGreaterThanOrEqualTo(startTime);
            }
            if (StringUtils.isNotEmpty(form.getSendEndtTimeStr())) {
                Integer endTime = GetDate.strYYYYMMDDHHMMSS2Timestamp2(form.getSendStartTimeStr()+" 23:59:59");
                creteria.andSendTimeLessThanOrEqualTo(endTime);
            }
            if (form.getInfType() != null && form.getInfType().intValue() > 0) {
                creteria.andInfTypeEqualTo(form.getInfType());
            }
            if (form.getSendStatus() != null && form.getSendStatus().intValue() >= 0) {
                creteria.andSendStatusEqualTo(form.getSendStatus());
            }
            if (StringUtils.isNotEmpty(form.getLogOrdId())) {
                creteria.andLogOrdIdEqualTo(form.getLogOrdId());
            }
        }
        return certErrLogMapper.countByExample(example);
    }

    /**
     * 重新跑批
     *
     * @param id
     */
    @Override
    public void updateErrorCount(Integer id) {
        CertErrLog certErrLog = new CertErrLog();
        certErrLog.setSendCount(3);
        certErrLog.setId(id);
        certErrLogMapper.updateByPrimaryKeySelective(certErrLog);
    }
}

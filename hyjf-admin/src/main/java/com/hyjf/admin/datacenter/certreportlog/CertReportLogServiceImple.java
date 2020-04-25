/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.datacenter.certreportlog;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
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
public class CertReportLogServiceImple extends BaseServiceImpl implements CertReportLogService {

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * 根据分页查找数据
     *
     * @param limtStart
     * @param limtEnd
     * @param form
     * @return
     */
    @Override
    public List<CertLog> selectCertReportLog(int limtStart, int limtEnd, CertReportLogBean form) {
        CertLogExample example = new CertLogExample();
        CertLogExample.Criteria creteria = example.createCriteria();
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
            // 对账状态
            if (form.getQueryStatus()!=null && form.getQueryStatus().intValue()>=0) {
                creteria.andQueryResultEqualTo(form.getQueryStatus());
            }
        }
        example.setLimitStart(limtStart);
        example.setLimitEnd(limtEnd);
        example.setOrderByClause(" send_time DESC");
        return certLogMapper.selectByExample(example);
    }

    /**
     * 查找数量
     *
     * @param form
     * @return
     */
    @Override
    public Integer selectCertReportLogCount(CertReportLogBean form) {
        CertLogExample example = new CertLogExample();
        CertLogExample.Criteria creteria = example.createCriteria();
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
            if (form.getSendStatus() != null && form.getSendStatus().intValue() > 0) {
                creteria.andSendStatusEqualTo(form.getSendStatus());
            }
            if (StringUtils.isNotEmpty(form.getLogOrdId())) {
                creteria.andLogOrdIdEqualTo(form.getLogOrdId());
            }
            // 对账状态
            if (form.getQueryStatus()!=null && form.getQueryStatus().intValue()>=0) {
                creteria.andQueryResultEqualTo(form.getQueryStatus());
            }
        }
        return certLogMapper.countByExample(example);
    }

    /**
     * 数据同步 发送MQ
     *
     * @param dataType
     */
    @Override
    public void doDdataSyn(String dataType) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("nowtime", GetDate.getNowTime10()+"");

        if("1".equals(dataType)){
            // 投资人数据同步
            this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_OLD_DATA_TENDER_USER, JSONObject.toJSONString(params));
            return;
        }
        if("2".equals(dataType)){
            // 借款人数据同步
            this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_BORROW_USER, JSONObject.toJSONString(params));
            return;
        }
        if("3".equals(dataType)){
            // 标的数据同步
            this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_BORROW_OLD, JSONObject.toJSONString(params));
            return;
        }
        if("4".equals(dataType)){
            // 散标状态数据同步
            this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_BORROW_STATUS, JSONObject.toJSONString(params));
            return;
        }
        if("5".equals(dataType)){
            // 还款计划数据同步
            this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_BORROW_REPAYMENTPLAN, JSONObject.toJSONString(params));
            return;
        }
        if("6".equals(dataType)){
            // 债权信息数据同步
            this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_TENDER_INFO, JSONObject.toJSONString(params));
            return;
        }
        if("7".equals(dataType)){
            // 转让项目数据同步
        	this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_OLD_CREDITINFO, JSONObject.toJSONString(params));
            return;
        }
        if("8".equals(dataType)){
            // 转让状态数据同步
        	this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_OLD_CREDIT_STATUS, JSONObject.toJSONString(params));
            return;
        }
        if("9".equals(dataType)){
            // 承接信息数据同步
            this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_CREDITTENDERINFO, JSONObject.toJSONString(params));
            return;
        }
        if("10".equals(dataType)){
            // 交易流水数据同步
        	this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_OLD_TRANSACT, JSONObject.toJSONString(params));
            return;
        }
        if("11".equals(dataType)){
            // 交易流水mongo数据上报
            this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_OLD_ACCOUNT_LIST, JSONObject.toJSONString(params));
            return;
        }
    }

}

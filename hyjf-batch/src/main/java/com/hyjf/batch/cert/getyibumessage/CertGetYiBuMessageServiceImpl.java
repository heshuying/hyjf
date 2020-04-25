package com.hyjf.batch.cert.getyibumessage;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.CertLogExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 合规数据上报 CERT 查询批次数据入库消息
 * @Author nxl
 * @Date 2018/12/25 14:10
 */
@Service
public class CertGetYiBuMessageServiceImpl extends BaseServiceImpl implements CertGetYiBuMessageService {

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
    Logger logger = LoggerFactory.getLogger(CertGetYiBuMessageTask.class);

    @Override
    public void getYibuMessage() {
        CertLogExample example = new CertLogExample();
        CertLogExample.Criteria creteria = example.createCriteria();
        // 查询上报结果成功
        creteria.andSendStatusEqualTo(1);
        // 查询结果为初始
        creteria.andQueryResultEqualTo(0);
        int intCount = certLogMapper.countByExample(example);
        if (intCount>0) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("mqMsgId", GetCode.getRandomCode(10));
            logger.info("查询批次数据入库消息 执行MQ,参数为:" + param.toString());
            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_YIBUMESSAGE, JSONObject.toJSONString(param));
        }else {
            logger.info("查询批次数据入库消息查找批次数量为:"+intCount);
        }
    }

}


package com.hyjf.batch.cert.mobilehash;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.exception.cert.CertSendExceptionService;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.RabbitMQConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 合规数据上报 CERT 国家互联网应急中心手机号哈希值
 * @Author sunss
 * @Date 2018/11/30 14:10
 */
public class CertMobileHashTask {
	Logger logger = LoggerFactory.getLogger(CertMobileHashTask.class);

	private String thisMessName = "手机号哈希值";
	private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private CertSendExceptionService certSendExceptionService;

	public void run() {
		runJob();
	}

	private boolean runJob() {
		if (isRun == 0) {
			isRun = 1;
			try {
				// 调用MQ处理
				logger.info(logHeader+"开始调用MQ：");
				// 加入到消息队列
				Map<String, String> params = new HashMap<String, String>();
				params.put("mqMsgId", GetCode.getRandomCode(10));
				rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_MOBILE_HASH, JSONObject.toJSONString(params));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
		}
		return true;
	}
}

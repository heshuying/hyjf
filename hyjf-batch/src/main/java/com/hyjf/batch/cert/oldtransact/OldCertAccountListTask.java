package com.hyjf.batch.cert.oldtransact;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 合规数据上报 CERT 国家互联网应急中心交易流水历史数据发送
 * @Author pcc
 * @Date 2018/12/03 14:10
 */
public class OldCertAccountListTask {
	Logger _log = LoggerFactory.getLogger(OldCertAccountListTask.class);
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	public void run() {
		runJob();
	}

	private boolean runJob() {
		_log.info("国家互联网应急中心交易流水历史数据发送 start");
		if (isRun == 0) {
			isRun = 1;
			try {
				Map<String, String> params = new HashMap<String, String>();
				params.put("nowtime", GetDate.getNowTime10()+"");
				this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_OLD_ACCOUNT_LIST, JSONObject.toJSONString(params));

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
		}
		_log.info("国家互联网应急中心交易流水历史数据发送 end");
		return true;
	}
}

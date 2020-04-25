package com.hyjf.mq;

import org.springframework.amqp.AmqpException;

/**
 * 合规数据上报
 */
public interface ApiMqService {

	/**
	 * 发送消息到MQ（带标准错误日志输出_钉钉警报用）
	 * @param exchange
	 * @param routingKey
	 * @param object
	 * @throws AmqpException
	 */
	void convertAndSend(String exchange, String routingKey, Object object) throws AmqpException;
}

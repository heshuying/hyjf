package com.hyjf.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 合规数据上报
 */
@Service
public class ApiMqServiceImpl implements ApiMqService {
	Logger _log = LoggerFactory.getLogger(ApiMqServiceImpl.class);

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	/**
	 * 发送消息到MQ（带标准错误日志输出_钉钉警报用）
	 * @param exchange
	 * @param routingKey
	 * @param object
	 * @throws AmqpException
	 */
	@Override
	public void convertAndSend(String exchange, String routingKey, Object object) throws AmqpException {
		try {
			this.rabbitTemplate.convertAndSend(exchange, routingKey, object);
			_log.info("互金临时日志：：：：：：：：：：：：：：：：：：：消息发送到MQ完成！（exchange:" + exchange + ", routingKey:" + routingKey + ", object:" + object + "）");
		} catch (AmqpException e) {
			_log.error("消息发送到MQ失败！（exchange:" + exchange + ", routingKey:" + routingKey + ", object:" + object + "）", e);
		}
	}
}

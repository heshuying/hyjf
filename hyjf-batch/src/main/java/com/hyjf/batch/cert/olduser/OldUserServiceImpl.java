package com.hyjf.batch.cert.olduser;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.customize.CertSendUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 合规数据上报 CERT 国家互联网应急中心 投资人老数据上报
 * @Author sunss
 * @Date 2018/11/30 14:20
 */
@Service
public class OldUserServiceImpl extends BaseServiceImpl implements OldUserService {

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private MqService mqService;
	/**
	 * 发送Mq
	 * @param routingkey
	 */
	@Override
	public void sendToMQ(String routingkey) {
		// 调用MQ处理 用户信息
		Map<String, String> params = new HashMap<String, String>();
		params.put("nowtime", GetDate.getNowTime10()+"");
		this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, routingkey, JSONObject.toJSONString(params));
	}

	/**
	 * 查询未上报的投资人
	 *
	 * @return
	 */
	@Override
	public List<CertSendUser> selectCertUserNotSend() {
		List<CertSendUser> users = usersCustomizeMapper.selectCertUserNotSend();
		return users;
	}

	/**
	 * 查询未上报的投资人 数量
	 *
	 * @return
	 */
	@Override
	public Integer getCertUserNotSendCount() {
		Integer counts = usersCustomizeMapper.getCertUserNotSendCount();
		return counts;
	}
}


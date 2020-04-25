package com.hyjf.batch.cert.oldtransact;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.RabbitMQConstants;

/**
 * @Description 应急中心交易流水上报
 * @Author pcc
 * @Date 2018/12/03 14:10
 */
@Service
public class OldCertTransactServiceImpl extends BaseServiceImpl implements OldCertTransactService {

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;
	
	
	@Override
	public void certTransact() {
		String oldCertTransactId = RedisUtils.get("oldCertTransactId");
		if(oldCertTransactId==null||oldCertTransactId.length()==0){
			oldCertTransactId=accountDetailCustomizeMapper.queryOldCertAccountListMaxId()+"";
			RedisUtils.set("oldCertTransactId", oldCertTransactId);
		}
		Map<String, Object> param =new HashMap<String, Object>();
		param.put("oldCertTransactId", oldCertTransactId);
		this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_OLD_TRANSACT, JSONObject.toJSONString(param));
		
	}
	
	
	@Override
	public void certTransactRepaySuccess() {
		String oldCertTransactId = RedisUtils.get("oldCertTransactId");
		if(oldCertTransactId==null||oldCertTransactId.length()==0){
			oldCertTransactId=accountDetailCustomizeMapper.queryOldCertAccountListMaxId()+"";
			RedisUtils.set("oldCertTransactId", oldCertTransactId);
		}
		Map<String, Object> param =new HashMap<String, Object>();
		param.put("oldCertTransactId", oldCertTransactId);
		this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_OLD_TRANSACT_REPAY_SUCCESS, JSONObject.toJSONString(param));
		
	}
	
}


package com.hyjf.batch.cert.transact;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.customize.CertAccountListIdCustomize;

/**
 * @Description 应急中心交易流水上报
 * @Author pcc
 * @Date 2018/12/03 14:10
 */
@Service
public class CertTransactServiceImpl extends BaseServiceImpl implements CertTransactService {

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;


	@Override
	public void certTransact() {
		
		Integer page=1;
		Integer size=1000;
		CertAccountListIdCustomize customize=new CertAccountListIdCustomize();
		do {
			String certTransactMaxId = RedisUtils.get("certTransactOtherMaxId");
			Map<String, Object> param =  new HashMap<String, Object>();
			param.put("minId", certTransactMaxId==null?"0":certTransactMaxId);
			param.put("limitStart", (page-1)*size);
			param.put("limitEnd", size);
			customize=accountDetailCustomizeMapper.queryCertAccountListId(param); 
			if(certTransactMaxId==null||"".equals(certTransactMaxId)){
				RedisUtils.set("certTransactOtherMaxId", customize.getMaxId()+"");
				RedisUtils.set("oldCertTransactMaxId",customize.getMaxId()+"");
				return;
			}
			String minId=customize.getLimitMinId()+"";
			String maxId=customize.getLimitMaxId()+"";
			String sumCount=customize.getSumCount()+"";
			if("0".equals(sumCount)){
				return;
			}
			
			// 加入到消息队列
			Map<String, String> params = new HashMap<String, String>();
			params.put("mqMsgId", GetCode.getRandomCode(10));
			params.put("minId", minId);
			params.put("maxId", maxId);
			rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_CERT_TRANSACT, JSONObject.toJSONString(params));
			RedisUtils.set("certTransactOtherMaxId", maxId);
			page=page+1;
		} while (customize.getMaxId()>customize.getLimitMaxId());
		
	}
	
}


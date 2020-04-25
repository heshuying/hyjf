package com.hyjf.mqreceiver.wrb;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.customize.wrb.WrbTenderNotifyCustomize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.validator.Validator;
import com.hyjf.mqreceiver.wrb.service.WrbCallBackService;
import com.rabbitmq.client.Channel;
import org.springframework.stereotype.Component;

/**
 * @author xiasq
 * @version WrbCallBackHandle, v0.1 2018/3/8 9:40 风车理财回调处理： 出借通知 + 回款通知，
 *          只发一次，不管结果
 */

@Component
public class WrbCallBackHandle implements ChannelAwareMessageListener {
	private Logger _log = LoggerFactory.getLogger(WrbCallBackHandle.class);
	public static final String WRB_CALLBACK_NOTIFY_URL = PropUtils.getSystem(CustomConstants.WRB_CALLBACK_NOTIFY_URL);

	@Autowired
	private WrbCallBackService wrbCallBackService;

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		_log.info("风车理财回调通知开始....");

		// 1. 解析请求参数
		if (message == null || message.getBody() == null) {
			_log.error("接收到的消息为null");
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}
		String msgBody = new String(message.getBody());
		_log.info("接受到的消息内容: {}", msgBody);
		JSONObject requestJson;
		try {
			requestJson = JSONObject.parseObject(msgBody);
		} catch (Exception e) {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			_log.error("解析消息体失败...", e);
			return;
		}

		// 2.验证请求参数
		Integer userId = requestJson.getInteger("userId");
		String nid = requestJson.getString("nid");
		// 回调类型， 1-出借回调 2-回款回调
		Integer returnType = requestJson.getInteger("returnType");
		// 回款时间
		String backTime = requestJson.getString("backTime");
		// 回款金额
		String backMoney = requestJson.getString("backMoney");
		if (Validator.isNull(userId) || Validator.isNull(nid) || Validator.isNull(returnType)) {
			_log.error("风车理财回调通知参数不全.....");
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}
		if (returnType.intValue() == 2) {
			if (Validator.isNull(backTime) || Validator.isNull(backMoney)) {
				_log.error("风车理财回款通知参数不全.....");
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}
		}

		// 3.业务逻辑
		try {
			this.doHandle(userId, nid, returnType, backTime, backMoney, channel);
		} catch (Exception e) {
			_log.error("风车理财回调失败...", e);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		_log.info("风车理财回调通知处理完成...");
	}

	private void doHandle(Integer userId, String nid, Integer returnType, String backTime, String backMoney,
			Channel channel) throws Exception {
		// 调用风车理财回调接口
		Map<String, String> allParams = new HashMap<>();
		//allParams.put("from", CustomConstants.WRB_CHANNEL_CODE);
		allParams.put("pf_user_id", WrbCoopDESUtil.desEncrypt(WrbCoopDESUtil.PF_KEY, String.valueOf(userId)));

		WrbTenderNotifyCustomize customize = wrbCallBackService.searchBorrowTenderByNid(nid, userId);
		if (customize == null) {
			_log.error("请求参数错误，找不到出借数据....");
			throw new RuntimeException();
		}
		// returnType ：1-出借回调 ， 2-回款回调
		if (returnType.intValue() == 1) {
			allParams.put("invest_time", customize.getAddtime());
			allParams.put("invest_sno", nid);
			allParams.put("invest_money", customize.getAccount().setScale(4, BigDecimal.ROUND_HALF_UP).toString());
			allParams.put("invest_limit", customize.getInvestPeriod());
			allParams.put("invest_rate", customize.getBorrowApr());
			allParams.put("back_way", customize.getBorrowStyleName());
			allParams.put("invest_title", customize.getBorrowName());
		} else if (returnType.intValue() == 2) {
			allParams.put("back_time", backTime);
			allParams.put("bid_id", customize.getBorrowNid());
			allParams.put("invest_sno", nid);
			allParams.put("back_money", backMoney);
			allParams.put("invest_title", customize.getBorrowName());
		} else {
			_log.error("错误的回调类型....");
			throw new RuntimeException();
		}
		allParams.put("param","test");
		allParams.put("param","test");

		_log.info("param is :{}", JSONObject.toJSONString(allParams));

		String result = WrbParseParamUtil.wrbCallback(WRB_CALLBACK_NOTIFY_URL, allParams);
		_log.info("风车理财回调结果：{}", result);
	}
}

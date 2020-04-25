package com.hyjf.batch.exception.cert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.batch.exception.bankwithdraw.BankWithdrawExceptionService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.result.CheckResult;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 应急中心异常处理
 * @Author sunss
 * @Date 2018/11/30 14:20
 */
@Service
public class CertSendExceptionServiceImpl extends BaseServiceImpl implements CertSendExceptionService {

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	/**
	 * 检索上报失败的记录
	 *
	 * @return
	 */
	@Override
	public List<CertErrLog> selectCertSendErrLogList() {
		CertErrLogExample example = new CertErrLogExample();
		CertErrLogExample.Criteria cra = example.createCriteria();
		// 默认是1  发三次是 4  所以查询小于5的
		cra.andSendCountLessThan(4);
		example.setLimitStart(0);
		example.setLimitEnd(500);
		return this.certErrLogMapper.selectByExample(example);
	}

	/**
	 * 发送Mq
	 * @param data
	 * @param routingkey
	 */
	@Override
	public void sendToMQ(JSONArray data,String routingkey) {
		// 加入到消息队列
		Map<String, String> params = new HashMap<String, String>();
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("errors", data.toJSONString());
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, routingkey, JSONObject.toJSONString(params));
	}

	/**
	 * 组装数据
	 *
	 * @param certErrLogs
	 * @return
	 */
	@Override
	public JSONArray getErrorData(List<CertErrLog> certErrLogs) {
		JSONArray result = new JSONArray();
		for (CertErrLog item :certErrLogs) {
			JSONObject data = new JSONObject();
			data.put("id",item.getId());
			data.put("logOrdId",item.getLogOrdId());
			data.put("count",item.getSendCount());

			result.add(data);
		}
		return result;
	}
}


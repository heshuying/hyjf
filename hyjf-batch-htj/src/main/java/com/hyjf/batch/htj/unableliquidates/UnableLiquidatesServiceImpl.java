package com.hyjf.batch.htj.unableliquidates;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanExample;

@Service
public class UnableLiquidatesServiceImpl extends BaseServiceImpl implements UnableLiquidatesService {

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	/**
	 * 查询相应的募集中的计划（按buy_begin_time申购开始时间升序排列）
	 * 
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<DebtPlan> selectDebtPlanLiquidates() {
		int nowTime = GetDate.getNowTime10();
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria crt = example.createCriteria();
		crt.andDebtPlanStatusEqualTo(7);
		crt.andLiquidateFactTimeLessThanOrEqualTo(nowTime - 24 * 60 * 60);
		example.setOrderByClause("buy_begin_time ASC");
		List<DebtPlan> debtPlans = debtPlanMapper.selectByExample(example);
		return debtPlans;
	}

	@Override
	public BigDecimal countDebtCreditSum(String planNid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planNid);
		BigDecimal sum =this.batchDebtCreditCustomizeMapper.countDebtCreditSum(params);
		return sum;
	}

	

	/**
	 * 发送短信
	 * 
	 * @param planNid
	 * @param count
	 * @author Administrator
	 */

	@Override
	public void sendSms(String planNid, BigDecimal total) {
		// 发送短信
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("val_htj_title", planNid);
		replaceStrs.put("val_amount", String.valueOf(total));
        SmsMessage smsMessage =
                new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null,
                        CustomConstants.PARAM_TPL_JYTZ_HTJ_QSFAILED, CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);

	}

	/**
	 * 发送邮件
	 * 
	 * @param planNid
	 * @param count
	 * @author Administrator
	 */

	@Override
	public void sendEmail(String planNid, BigDecimal total) {
		// 发送邮件
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("val_htj_title", planNid);
		replaceStrs.put("val_amount", String.valueOf(total));
		// 取得是否线上
		String online = "生产环境";
		String payUrl = PropUtils.getSystem(CustomConstants.HYJF_ONLINE);
		if (payUrl == null || !payUrl.contains("online")) {
			online = "测试环境";
		}
		String[] toMail = new String[] {};
		if ("测试环境".equals(online)) {
			toMail = new String[] { "jiangying@hyjf.com", "liudandan@hyjf.com" };
		} else {
			toMail = new String[] { "wangkun@hyjf.com", "gaohonggang@hyjf.com", "sunjianhua@hyjf.com" };
		}
		MailMessage smsMessage = new MailMessage(null, replaceStrs, "[" + online + "] " + planNid + "计划清算未完成，报警", null, null, toMail, CustomConstants.EMAILPARAM_TPL_JYTZ_HTJ_QSFAILED, MessageDefine.MAILSENDFORMAILINGADDRESS);
		mailMessageProcesser.gather(smsMessage);

	}
}

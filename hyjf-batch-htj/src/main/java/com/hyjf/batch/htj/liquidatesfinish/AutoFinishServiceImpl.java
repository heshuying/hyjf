package com.hyjf.batch.htj.liquidatesfinish;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;

@Service
public class AutoFinishServiceImpl extends BaseServiceImpl implements AutoFinishService {

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	@Override
	public void sendEmail(DebtPlan debtPlanLiquidates) {

		String planName = debtPlanLiquidates.getDebtPlanNid();
		String valAmount = debtPlanLiquidates.getRepayAccountCapitalWait().toString();
		String valBalance = debtPlanLiquidates.getRepayAccountInterestWait().toString();
		String valProfit = debtPlanLiquidates.getServiceFee().toString();
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("val_htj_title", planName);
		replaceStrs.put("val_amount", valAmount);
		replaceStrs.put("val_balance", valBalance);
		replaceStrs.put("val_profit", valProfit);
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
		MailMessage smsMessage = new MailMessage(null, replaceStrs, "[" + online + "] " + "计划已经清算完成", null, null, toMail, CustomConstants.EMAILPARAM_TPL_JYTZ_HTJ_JJHK, MessageDefine.MAILSENDFORMAILINGADDRESS);
		mailMessageProcesser.gather(smsMessage);

	}

	@Override
	public void sendSms(DebtPlan debtPlanLiquidates) {
		String planName = debtPlanLiquidates.getDebtPlanNid();
		String valAmount = debtPlanLiquidates.getRepayAccountCapitalWait().toString();
		String valBalance = debtPlanLiquidates.getRepayAccountInterestWait().toString();
		String valProfit = debtPlanLiquidates.getServiceFee().toString();
		// 发送短信
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("val_htj_title", planName);
		replaceStrs.put("val_amount", valAmount);
		replaceStrs.put("val_balance", valBalance);
		replaceStrs.put("val_profit", valProfit);
		SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_JYTZ_HTJ_JJHK, CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);

	}

	/**
	 * 查询相应的已清算的计划一条（liquidate_fact_time升序排列）
	 * 
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<DebtPlan> selectDebtPlanLiquidates() {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria crt = example.createCriteria();
		crt.andDebtPlanStatusEqualTo(7);
		example.setOrderByClause("liquidate_fact_time ASC");
		List<DebtPlan> debtPlans = debtPlanMapper.selectByExample(example);
		if (debtPlans != null && debtPlans.size() > 0) {
			return debtPlans;
		} else {
			return null;
		}
	}

	@Override
	public int countDebtCreditsAll(String liquidatesPlanNid) {
		// 拼接相应的查询参数
		Map<String, Object> debtCreditParams = new HashMap<String, Object>();
		debtCreditParams.put("liquidatesPlanNid", liquidatesPlanNid);
		int count = this.batchDebtCreditCustomizeMapper.countDebtCreditsAll(debtCreditParams);
		return count;
	}

	@Override
	public boolean updateDebtPlan(DebtPlan debtPlanLiquidates) throws Exception {
		// 清算的计划编号
		String planNid = debtPlanLiquidates.getDebtPlanNid();
		debtPlanLiquidates.setDebtPlanStatus(8);
		boolean debtPlanFlag = this.debtPlanMapper.updateByPrimaryKey(debtPlanLiquidates) > 0 ? true : false;
		if (debtPlanFlag) {
			DebtPlanAccedeExample example = new DebtPlanAccedeExample();
			DebtPlanAccedeExample.Criteria crt = example.createCriteria();
			crt.andPlanNidEqualTo(planNid);
			DebtPlanAccede debtPlanAccede = new DebtPlanAccede();
			debtPlanAccede.setStatus(3);// 1出借完成 2清算中 3清算完成
			boolean debtPlanAccedeFlag = this.debtPlanAccedeMapper.updateByExampleSelective(debtPlanAccede, example) > 0 ? true : false;
			if (debtPlanAccedeFlag) {
				return true;
			} else {
				throw new Exception("债权承接完成，修改计划订单为清算完成失败，计划订单号：" + planNid);
			}
		} else {
			throw new Exception("债权承接完成，修改计划为清算完成失败，计划订单号：" + planNid);
		}
	}
}
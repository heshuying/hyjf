package com.hyjf.batch.htj.liquidatewill;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;

/**
 * 
 * @author: zhouxiaoshuai
 * @email: 287424494@qq.com
 * @description: 平台数据定时任务
 * @version: 1
 * @date: 2016年7月8日 下午4:06:01
 */
public class LiquidateWillTask {

	@Autowired
	private LiquidateWillService liquidateWillService;
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	public void dataTask() {
		System.out.println("----------------汇添金即将清算提醒定时任务-------------");
		// 搜索出 在预计清算日前一1天的9:00，短信提醒运营人员，注意配置新计划
		List<DebtPlan> planList = liquidateWillService.selectLiquidateWill();
		for (DebtPlan debtPlan : planList) {
			// 发送成功短信
			Map<String, String> replaceStrs = new HashMap<String, String>();
			replaceStrs.put("val_htj_title", debtPlan.getDebtPlanNid());
			List<DebtPlanAccede> accedeList = liquidateWillService.selectAllExFireValue(debtPlan.getDebtPlanNid());
			BigDecimal ammount = BigDecimal.ZERO;
			for (DebtPlanAccede debtPlanAccede : accedeList) {
				ammount = ammount.add(debtPlanAccede.getExpireFairValue());
			}
			replaceStrs.put("val_amount", ammount + "");
			SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER,
					null, CustomConstants.JYTZ_HTJ_JJQS, CustomConstants.CHANNEL_TYPE_NORMAL);
			smsProcesser.gather(smsMessage);
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
				toMail = new String[] { "wangkun@hyjf.com", "gaohonggang@hyjf.com",
						"sunjianhua@hyjf.com" };
			}
			MailMessage message = new MailMessage(null, replaceStrs, "计划即将清算", null, null, toMail,
					CustomConstants.JYTZ_HTJ_JJQS, MessageDefine.MAILSENDFORMAILINGADDRESS);
			mailMessageProcesser.gather(message);

		}
	}

}

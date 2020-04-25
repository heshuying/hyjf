package com.hyjf.batch.htj.planweakasset;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;

@Service
public class WeakAssetServiceImpl extends BaseServiceImpl implements WeakAssetService {
	
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
	public List<DebtPlan> selectDebtPlanInvest() {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria crt = example.createCriteria();
		//crt.andDebtPlanStatusEqualTo(4);
		//产品要求募集中和锁定中的计划都可以再投    并且轮询次数小于限制次数 并且 用户的剩余可投金额
		List<Integer> list=new ArrayList<Integer>();
		list.add(4);
		list.add(5);
		crt.andDebtPlanStatusIn(list);
		example.setOrderByClause("buy_begin_time ASC");
		List<DebtPlan> debtPlans = debtPlanMapper.selectByExample(example);
		return debtPlans;
	}
	
	/**
	 * 根据计划id查询相应的计划加入记录
	 * 
	 * @param planNid
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countDebtPlanAccede(String planNid, BigDecimal minSurplusInvestAccount, int cycleTimes) {

		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria crt = example.createCriteria();
		crt.andPlanNidEqualTo(planNid);
		crt.andAccedeBalanceGreaterThan(minSurplusInvestAccount);
		crt.andStatusEqualTo(1);
		crt.andCycleTimesGreaterThanOrEqualTo(cycleTimes);
		crt.andDelFlagEqualTo(0);
		example.setOrderByClause("create_time ASC");
		int count= this.debtPlanAccedeMapper.countByExample(example);
		return count;

	}

	/**
	 * 发送短信
	 * @param planNid
	 * @param count
	 * @author Administrator
	 */
		
	@Override
	public void sendSms(String planNid, int count, int cycleTimes) {
		// 发送短信
		Map<String, String> replaceStrs = new HashMap<String, String>();
        replaceStrs.put("val_htj_title", planNid);
        replaceStrs.put("val_number", String.valueOf(count));
        replaceStrs.put("cycleTimes", String.valueOf(cycleTimes));
        SmsMessage smsMessage =
                new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null,
                        CustomConstants.PARAM_TPL_JYTZ_HTJ_SDTZ, CustomConstants.CHANNEL_TYPE_NORMAL);
        smsProcesser.gather(smsMessage);
			
	}

	/**
	 * 发送邮件
	 * @param planNid
	 * @param count
	 * @author Administrator
	 */
		
	@Override
	public void sendEmail(String planNid, int count, int cycleTimes) {
		// 发送邮件
		Map<String, String> replaceStrs = new HashMap<String, String>();
        replaceStrs.put("val_htj_title", planNid);
        replaceStrs.put("val_number", String.valueOf(count));
        replaceStrs.put("cycleTimes", String.valueOf(cycleTimes));
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
            toMail = new String[] { "wangkun@hyjf.com", "gaohonggang@hyjf.com","sunjianhua@hyjf.com"};
        }
        MailMessage smsMessage = new MailMessage(null, replaceStrs, "[" + online + "] " + planNid + "关联资产不足，手动出借警报", null, null, toMail, CustomConstants.EMAILPARAM_TPL_JYTZ_HTJ_SDTZ,MessageDefine.MAILSENDFORMAILINGADDRESS);
        mailMessageProcesser.gather(smsMessage);
			
	}
}

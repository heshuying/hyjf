package com.hyjf.batch.borrow.repay.reminder;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.jpush.api.utils.StringUtils;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;

/**
 * 还款前三天提醒借款人还款短信Service实现类
 * 
 * @author liuyang
 *
 */
@Service
public class RepayReminderServiceImpl extends BaseServiceImpl implements RepayReminderService {

	/** 用户ID */
	private static final String VAL_USERID = "userId";
	
	/** 性别 */
	private static final String VAL_SEX = "val_sex";
	/** 用户名 */
	private static final String VAL_NAME = "val_name";

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	/**
	 * 检索正在还款中的标的
	 */
	@Override
	public List<BorrowWithBLOBs> selectRepayBorrowList() {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(3);
		cra.andRepayFullStatusEqualTo(0);
		return this.borrowMapper.selectByExampleWithBLOBs(example);
	}

	/**
	 * 根据标的编号
	 */
	@Override
	public List<BorrowRepayPlan> selectBorrowRepayPlan(String borrowNid, Integer repaySmsReminder) {
		BorrowRepayPlanExample example = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(borrowNid)) {
			cra.andBorrowNidEqualTo(borrowNid);
		}
		cra.andRepaySmsReminderEqualTo(repaySmsReminder);
		// 未还款
		cra.andRepayStatusEqualTo(0);
		// 还款期数升序
		example.setOrderByClause("repay_period ASC");
		return this.borrowRepayPlanMapper.selectByExample(example);
	}

	/**
	 * 发送短信(还款成功)
	 *
	 */
	@Override
	public void sendSms(List<Map<String, String>> msgList, String temp) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && NumberUtils.isNumber(msg.get(VAL_USERID))) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null || Validator.isNull(users.getMobile()) || (users.getRecieveSms() != null && users.getRecieveSms() == 1)) {
						return;
					}
					UsersInfo userInfo = this.getUsersInfoByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (StringUtils.isEmpty(userInfo.getTruename())) {
						msg.put(VAL_NAME, users.getUsername());
					} else if (userInfo.getTruename().length() > 1) {
						msg.put(VAL_NAME, userInfo.getTruename().substring(0, 1));
					} else {
						msg.put(VAL_NAME, userInfo.getTruename());
					}
					Integer sex = userInfo.getSex();
					if (Validator.isNotNull(sex)) {
						if (sex.intValue() == 2) {
							msg.put(VAL_SEX, "女士");
						} else {
							msg.put(VAL_SEX, "先生");
						}
					}
					SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null,
							temp, CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				}
			}
		}
	}

	/**
	 * 短信发送后更新borrowRecoverPlan表
	 */
	@Override
	public boolean updateBorrowRepayPlan(BorrowRepayPlan borrowRepayPlan, Integer repaySmsReminder) {
		borrowRepayPlan.setRepaySmsReminder(repaySmsReminder);
		return this.borrowRepayPlanMapper.updateByPrimaryKeySelective(borrowRepayPlan) > 0 ? true : false;
	}

	/**
	 * 获取borrowRecover数据
	 */
	@Override
	public List<BorrowRepay> selectBorrowRepayList(String borrowNid, int i) {
		BorrowRepayExample example = new BorrowRepayExample();
		BorrowRepayExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		cra.andRepaySmsReminderEqualTo(i);
		return this.borrowRepayMapper.selectByExample(example);
	}

	/**
	 * 更新borrowRecover
	 */
	@Override
	public boolean updateBorrowRepay(BorrowRepay borrowRepay, int repaySmsReminder) {
		borrowRepay.setRepaySmsReminder(repaySmsReminder);
		return this.borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay) > 0 ? true : false;
	}
}

package com.hyjf.batch.htj.debttender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;

@Service
public class DebtOntimeBorrowServiceImpl extends BaseServiceImpl implements DebtOntimeBorrowService {
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	/**
	 * 资金明细（列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	@Override
	public List<DebtBorrow> queryOntimeTenderList() {
		int onTime = GetDate.getNowTime10();
		List<DebtBorrow> list = this.batchDebtOntimeBorrowCustomizeMapper.queryOntimeTenderList(onTime);
		return list;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param borrow
	 * @return
	 * @author Administrator
	 */

	@Override
	public boolean updateSendBorrow(int borrowId) {

		// 当前时间
		int nowTime = GetDate.getNowTime10();
		DebtBorrowWithBLOBs borrow = this.debtBorrowMapper.selectByPrimaryKey(borrowId);
		borrow.setBorrowEndTime(String.valueOf(nowTime + borrow.getBorrowValidTime() * 86400));
		// 是否可以进行借款
		borrow.setBorrowStatus(1);
		// 发标的状态
		borrow.setVerifyStatus(1);
		// 状态
		borrow.setStatus(1);
		// 初审时间
		borrow.setVerifyTime(nowTime + "");
		boolean flag = this.debtBorrowMapper.updateByPrimaryKeySelective(borrow) > 0 ? true : false;
		if (flag) {
			// 写入redis
			RedisUtils.set(CustomConstants.DEBT_REDITS + borrow.getBorrowNid(), borrow.getAccount().toString());
			// 发送发标短信
			Map<String, String> params = new HashMap<String, String>();
			params.put("val_title", borrow.getBorrowNid());
			SmsMessage smsMessage = new SmsMessage(null, params, null, null, MessageDefine.SMSSENDFORMANAGER, "【汇盈金服】", CustomConstants.PARAM_TPL_DSFB, CustomConstants.CHANNEL_TYPE_NORMAL);
			smsProcesser.gather(smsMessage);
			return true;
		} else {
			return false;
		}

	}

}

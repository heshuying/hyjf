package com.hyjf.batch.borrow.repay.reminder;

import java.util.List;
import java.util.Map;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;

/**
 * 还款前三天提醒借款人还款短信Service
 * 
 * @author liuyang
 *
 */
public interface RepayReminderService extends BaseService {

	/**
	 * 检索正在还款中的标的
	 * 
	 * @return
	 */
	List<BorrowWithBLOBs> selectRepayBorrowList();

	/**
	 * 根据标的编号检索还款计划
	 * 
	 * @param borrowNid
	 * @return
	 */
	List<BorrowRepayPlan> selectBorrowRepayPlan(String borrowNid, Integer repaySmsReminder);

	/**
	 * 发送短信
	 * 
	 * @param msgList
	 * @param temp
	 */
	public void sendSms(List<Map<String, String>> msgList, String temp);

	/**
	 * 短信发送后更新borrowRecoverPlan
	 * 
	 * @param borrowRecoverPlan
	 * @return
	 */
	boolean updateBorrowRepayPlan(BorrowRepayPlan borrowRepayPlan, Integer repaySmsReminder);

	/**
	 * 检索borrowRecover数据
	 * 
	 * @param borrowNid
	 * @param i
	 * @return
	 */
	List<BorrowRepay> selectBorrowRepayList(String borrowNid, int i);

	/**
	 * 更新borrowRepay
	 * 
	 * @param borrowRepay
	 * @param i
	 * @return
	 */
	boolean updateBorrowRepay(BorrowRepay borrowRepay, int i);

}

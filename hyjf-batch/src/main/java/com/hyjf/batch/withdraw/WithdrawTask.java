package com.hyjf.batch.withdraw;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Accountwithdraw;

/**
 * 提现相关任务,定时扫描提现表,与汇付比对,进行成功或失败处理
 * 
 * @author 孙亮
 * @since 2016年4月5日13:57:48
 *
 */
public class WithdrawTask {

	@Autowired
	private WithdrawService withdrawService;
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	/**
	 * 
	 * 定时检测没有支付结果（提现中）的支付记录，与汇付天下进行交易比对，确认支付结果
	 * 
	 * @author renxingchen
	 */
	public void checkWithdrawStatus() {
		if (isOver) {
			isOver = false;
			LogUtil.startLog(WithdrawTask.class.toString(), "checkWithdrawStatus");
			// 查询数据库中充值结果为充值中的结果集(10天内)
			Date date = GetDate.getTodayBeforeOrAfter(-2);
			String startDateTime = GetDate.get10Time(GetDate.dateToString(date));
			List<Accountwithdraw> accountwithdraws = this.withdrawService.queryNoResultWithdrawList(startDateTime);
			if (null != accountwithdraws && !accountwithdraws.isEmpty()) {
				// 如果有提现状态为提现中的数据
				for (Accountwithdraw accountwithdraw : accountwithdraws) {
					try {
						// 更新状态
						this.withdrawService.handleWithdrawStatus(accountwithdraw);
					} catch (Exception e) {
						LogUtil.errorLog(WithdrawTask.class.toString(), "checkWithdrawStatus", e);
						continue;
					}
				}
			}
			LogUtil.endLog(WithdrawTask.class.toString(), "checkWithdrawStatus");
			isOver = true;
		}
	}
}

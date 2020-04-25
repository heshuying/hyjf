package com.hyjf.batch.htj.debtautoreview;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.batch.htj.debtautoreview.enums.BorrowSendTypeEnum;
import com.hyjf.common.log.LogUtil;

/**
 * 自动复审任务
 * 
 * @author 孙亮
 * @since 2015年12月18日 上午8:53:40
 */
public class DebtAutoReviewTask {
	public static String METHODNAME = "run";

	@Autowired
	private DebtAutoReviewService autoReviewService;

	public void run() {
		Integer afterTime;
		try {
			try {
				// 给到期未满标项目发短信
				autoReviewService.sendMsgToNotFullBorrow();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 复审
			afterTime = autoReviewService.getAfterTime(BorrowSendTypeEnum.FUSHENSEND_CD);
			autoReviewService.updateDebtBorrow(afterTime);
		} catch (Exception e) {
			LogUtil.errorLog(DebtAutoReviewTask.class.toString(), METHODNAME, e);
		}
	}

}

package com.hyjf.batch.borrow.autoReview;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.batch.borrow.autoReview.enums.BorrowSendTypeEnum;
import com.hyjf.common.log.LogUtil;

/**
 * 自动复审任务
 * 
 * @author 孙亮
 * @since 2015年12月18日 上午8:53:40
 */
public class AutoReviewTask {
	public static String METHODNAME = "run";

	@Autowired
	private AutoReviewService autoReviewService;

	public void run() {
		Integer afterTime;
		try {
			try {
				//给到期未满标项目发短信
				autoReviewService.sendMsgToNotFullBorrow();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//获取过期时间
			afterTime = autoReviewService.getAfterTime(BorrowSendTypeEnum.FUSHENSEND_CD);
			//复审
			autoReviewService.updateBorrow(afterTime);
		} catch (Exception e) {
			LogUtil.errorLog(AutoReviewTask.class.toString(), METHODNAME, e);
		}
	}

}

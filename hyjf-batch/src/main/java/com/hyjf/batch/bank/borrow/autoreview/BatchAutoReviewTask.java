package com.hyjf.batch.bank.borrow.autoreview;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.batch.borrow.autoReview.enums.BorrowSendTypeEnum;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;

/**
 * 自动复审任务
 * 
 * @author wangkun
 * @since 2015年12月18日 上午8:53:40
 */
public class BatchAutoReviewTask {

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private BatchAutoReviewService autoReviewService;

	public void run() {
		autoReview();
	}

	private boolean autoReview() {
		String methodName = "autoReview";
		if (isRun == 0) {
			isRun = 1;
			System.out.println(methodName + "自动放复审任务开始。");
			try {
				try {
					// 给到期未满标项目发短信
					autoReviewService.sendMsgToNotFullBorrow();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 获取过期时间
				Integer afterTime = autoReviewService.getAfterTime(BorrowSendTypeEnum.FUSHENSEND_CD);
				List<BorrowWithBLOBs> borrowList = this.autoReviewService.selectAutoReview();
				if (borrowList != null && borrowList.size() > 0) {
					for (BorrowWithBLOBs borrow : borrowList) {
						try {
							autoReviewService.updateBorrow(borrow, afterTime);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				LogUtil.errorLog(BatchAutoReviewTask.class.toString(), methodName, e);
			} finally {
				isRun = 0;
			}
			System.out.println(methodName + "自动放复审任务结束。");
		}
		return true;
	}
}

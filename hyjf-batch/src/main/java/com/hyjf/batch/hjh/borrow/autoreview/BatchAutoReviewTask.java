package com.hyjf.batch.hjh.borrow.autoreview;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;

/**
 * 汇计划自动复审任务
 * 
 * @author cwyang
 * @since 2017年08月17日 
 */
public class BatchAutoReviewTask {

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private BatchHjhAutoReviewService autoReviewService;

	Logger _log = LoggerFactory.getLogger(BatchAutoReviewTask.class);
	
	public void run() {
		autoReview();
	}

	private boolean autoReview() {
		if (isRun == 0) {
			isRun = 1;
			_log.info("=========自动放款复审任务开始。");
			try {
				try {
					// 给到期未满标项目发短信
					autoReviewService.sendMsgToNotFullBorrow();
				} catch (Exception e) {
					_log.info("=======给到期未满标项目发短信错误!" + e.getMessage());
				}
				List<BorrowWithBLOBs> borrowList = this.autoReviewService.selectAutoReview();

				if (borrowList != null && borrowList.size() > 0) {
					_log.info("--------------复审标的数量:" + borrowList.size());
					for (BorrowWithBLOBs borrow : borrowList) {
						_log.info("--------------复审标的号:" + borrow.getBorrowNid());	
						try {
							autoReviewService.updateBorrow(borrow);
						} catch (Exception e) {
							_log.info("=============自动复审异常!" + e.getMessage());
						}
					}
				}else{
					_log.info("--------------复审标的数量:0" );
				}
			} catch (Exception e) {
				_log.info("=============自动复审异常!" + e.getMessage());
			} finally {
				isRun = 0;
			}
			_log.info("=========自动放款复审任务结束。");
		}
		return true;
	}
}

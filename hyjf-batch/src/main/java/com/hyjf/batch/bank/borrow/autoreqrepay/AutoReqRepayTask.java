package com.hyjf.batch.bank.borrow.autoreqrepay;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.customize.AutoReqRepayBorrowCustomize;

public class AutoReqRepayTask {
	Logger _log = LoggerFactory.getLogger(AutoReqRepayTask.class);
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	AutoReqRepayService autoReqRepayService;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
	public void run() {
		// 调用还款接口
		autoReqRepay();
	}

	/**
	 * 调用还款接口
	 *
	 * @return
	 */
	private boolean autoReqRepay() {
		String nowTime = GetDate.getDataString(GetDate.datetimeFormat);
		_log.info("自动请求还款任务开始。。。" + nowTime);
		if (isRun == 0) {
			isRun = 1;
			try {
				// 取得本日应还款标的列表
				List<AutoReqRepayBorrowCustomize> autoReqRepayBorrowList = autoReqRepayService.getAutoReqRepayBorrow();
				if (autoReqRepayBorrowList != null && autoReqRepayBorrowList.size() > 0) {
					_log.info("需要自动请求还款的标的数量：" + autoReqRepayBorrowList.size());
					// 循环请求还款
					for (AutoReqRepayBorrowCustomize autoReqRepayBorrow : autoReqRepayBorrowList) {
						_log.info("标的"+autoReqRepayBorrow.getBorrowNid()+"自动请求还款开始。");
						_log.info("[borrowNid=" + autoReqRepayBorrow.getBorrowNid() + 
								", userId=" + autoReqRepayBorrow.getUserId() + 
								", username=" + autoReqRepayBorrow.getUsername() + 
								", repayOrgUserId="	+ autoReqRepayBorrow.getRepayOrgUserId() + 
								", repayOrgUsername="	+ autoReqRepayBorrow.getRepayOrgUsername() + 
								", repayerType=" + autoReqRepayBorrow.getRepayerType() + "]");
						// 标的请求还款
						if (this.autoReqRepayService.repayUserBorrowProject(autoReqRepayBorrow)) {
							_log.info("标的"+autoReqRepayBorrow.getBorrowNid()+"自动请求还款成功。");
						}else {
							_log.info("标的"+autoReqRepayBorrow.getBorrowNid()+"自动请求还款失败。");
						}
					}
				}else {
					_log.info("没有需要自动请求还款的标的。。。");
				}
					

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
				_log.info("自动请求还款任务结束。。。" + nowTime);
			}
		}
		return true;
	}
}

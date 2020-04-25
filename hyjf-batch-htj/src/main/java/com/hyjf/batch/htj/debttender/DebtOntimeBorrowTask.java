package com.hyjf.batch.htj.debttender;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.DebtBorrow;

/**
 * 定时发标
 * 
 * @author wangkun
 */
public class DebtOntimeBorrowTask {
	/** 运行状态 */
	private static int isRun = 0;
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	DebtOntimeBorrowService ontimeBorrowService;

	/**
	 * 定时发标
	 */
	public void run() {
		onTimeTender();
	}

	/**
	 * 调用定时发标
	 *
	 * @return
	 */
	private boolean onTimeTender() {
		if (isRun == 0) {
			System.out.println("汇添金专属标定时发标 DebtOntimeBorrowTask.run ... ");
			isRun = 1;
			try {
				// 2。定时发标标的进行自动出借
				// 查询相应的自动发标的标的信息
				List<DebtBorrow> borrowOntimes = this.ontimeBorrowService.queryOntimeTenderList();
				if (borrowOntimes != null && borrowOntimes.size() > 0) {
					for (DebtBorrow borrow : borrowOntimes) {
						try {
							// b.标的自动发标
							boolean flag = this.ontimeBorrowService.updateSendBorrow(borrow.getId());
							if (!flag) {
								throw new Exception("标的自动发标失败！" + "[借款编号：" + borrow.getBorrowNid() + "]");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
			System.out.println("汇添金专属标定时发标DebtOntimeBorrowTask.end ... ");
		}
		return false;
	}
}

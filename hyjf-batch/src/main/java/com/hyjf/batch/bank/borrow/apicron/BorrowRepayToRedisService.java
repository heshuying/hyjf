package com.hyjf.batch.bank.borrow.apicron;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.BorrowApicron;

import java.util.List;

public interface BorrowRepayToRedisService extends BaseService {

	/**
	 * 取得借款API任务表
	 *
	 * @return
	 */
	public List<BorrowApicron> getBorrowApicronList(Integer type);
	
	/**
	 * 获取相应的还款记录
	 * @param id
	 * @return
	 */
	public BorrowApicron getBorrowApicron(Integer id);

	/**
	 * 处理本地异常导致的放款失败
	 * @param apicron
	 */
	public Boolean updateBatchFailLoan(BorrowApicron apicron);
	
}

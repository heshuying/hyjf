package com.hyjf.bank.service.borrow.batchloan;

import java.util.List;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountBorrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface BatchLoanService extends BaseService {


	/**
	 * 取得借款API任务表
	 *
	 * @return
	 */
	public List<BorrowApicron> getBorrowApicronList(Integer status, Integer apiType);

	/**
	 * 更新借款API任务表
	 *
	 * @return
	 * @throws Exception 
	 */
	public boolean updateBorrowApicron(BorrowApicron apicron,int status) throws Exception;

	/**
	 * 获取放款日志表
	 * @param borrowNid
	 * @return
	 */
	public AccountBorrow getAccountBorrow(String borrowNid);

	/**
	 * 取出账户信息
	 *
	 * @param userId
	 * @return
	 */
	public Account getAccountByUserId(Integer userId);

	/**
	 * 取得借款列表
	 *
	 * @return
	 */
	public List<BorrowTender> getBorrowTenderList(String borrowNid);

	/**
	 * 获取相应的放款任务
	 * 
	 * @param borrowNid
	 * @param borrowUserId
	 * @return
	 */
	public BorrowApicron selectBorrowApicron(String bankSeqNo);

	/**
	 * 查询批次结果
	 * @param apicron
	 * @return
	 */
	public boolean batchDetailsQuery(BorrowApicron apicron);

	public BankCallBean batchQuery(BorrowApicron apicron);

}

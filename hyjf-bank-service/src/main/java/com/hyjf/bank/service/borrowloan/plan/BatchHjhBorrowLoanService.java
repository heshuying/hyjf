package com.hyjf.bank.service.borrowloan.plan;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface BatchHjhBorrowLoanService extends BaseService {

	/**
	 * 取得借款API任务表
	 *
	 * @return
	 */
	public List<BorrowApicron> getBorrowApicronList(Integer apiType);

	/**
	 * 更新借款API任务表
	 * 
	 * @param borrowNid
	 * @param batchNo
	 *
	 * @return
	 * @throws Exception 
	 */
	public boolean updateBorrowApicron(BorrowApicron borrowApicron,int status) throws Exception;

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
	 * 获取江西银行账户信息
	 * 
	 * @param userId
	 * @return
	 */
	BankOpenAccount getBankOpenAccount(Integer userId);

	/**
	 * 发送放款请求
	 * 
	 * @param batchNo
	 * @param borrowUserId
	 * @param txAmountSum
	 * @param txCounts
	 * @param subPacksJson
	 * @return
	 */
	BankCallBean requestLoans(BorrowApicron apicron, JSONArray subPacksJson);

	/**
	 * 查询批次放款状态
	 * 
	 * @param apicron
	 * @return
	 */
	public BankCallBean batchQuery(BorrowApicron apicron);

	/***
	 * 发起放款请求
	 * 
	 * @param apicron
	 * @return
	 */
	public boolean requestLoans(BorrowApicron apicron);

	/**
	 * 查询放款请求明细
	 * 
	 * @param apicron
	 * @return
	 */
	public boolean batchDetailsQuery(BorrowApicron apicron);

	/**
	 * 获取相应的放款记录
	 * @param id
	 * @return
	 */
	public BorrowApicron getBorrowApicron(Integer id);
}

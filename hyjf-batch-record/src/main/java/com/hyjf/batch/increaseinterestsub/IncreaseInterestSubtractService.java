package com.hyjf.batch.increaseinterestsub;

import java.util.List;

import com.hyjf.mybatis.model.auto.IncreaseInterestLoan;

/**
 * 加息还款统计累计收益
 * 
 * @author liuyang
 *
 */
public interface IncreaseInterestSubtractService {

	/**
	 * 检索未还款的加息收益
	 * 
	 * @return
	 */
	public List<IncreaseInterestLoan> searchIncreaseInterestLoanList();

	/**
	 * 循环更新出借人账户信息
	 * 
	 * @param increaseInterestLoan
	 */
	public void updateTenderUserAccount(List<IncreaseInterestLoan> increaseInterestList);

	/**
	 * 检索2017-07-03 00:00:00 之后的还款
	 * @return
	 */
	public List<IncreaseInterestLoan> searchIncreaseInterestLoanRepayList();

	/**
	 * 更新2017-07-03 00:00:00之后的还款
	 * @param increaseInterestList
	 */
	public void updateIncreaseInterestRepayList(List<IncreaseInterestLoan> increaseInterestList);
}

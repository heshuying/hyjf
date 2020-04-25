package com.hyjf.batch.increaseinterest;

import java.util.List;

import com.hyjf.mybatis.model.auto.IncreaseInterestLoan;

/**
 * 加息还款统计累计收益
 * 
 * @author liuyang
 *
 */
public interface IncreaseInterestService {

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
}

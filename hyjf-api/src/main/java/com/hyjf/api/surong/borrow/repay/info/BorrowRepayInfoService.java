package com.hyjf.api.surong.borrow.repay.info;

import java.math.BigDecimal;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.Users;

public interface BorrowRepayInfoService extends BaseService {
	
	/**
     * 取得借款标的
     *
     * @return
     */
	Borrow getBorrow(String borrowNid);
	
	/**
	 * 根据条件查询用户信息
	 * @return
	 */
	Users getUsersByExample(String username,String mobile);
	
	/**
	 * 根据条件查询用户账户信息
	 * @return
	 */
	Account getUserAccountByExample(Integer userId);
	
	BigDecimal getUserBalance(Integer userId, String accountId);
	
	BorrowApicron getBorrowApicron(BorrowRepayInfoParamBean paramBean);
	
	BorrowRepay getBorrowRepayTimeByStatus(String borrowNid,Integer status);
	
	/**
	 * 取得用户在江西银行客户号
	 */
	BankOpenAccount getBankOpenAccount(Integer userId);

}

/**
 * Description:用户充值
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.app.bank.user.synbalance;

import java.math.BigDecimal;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

public interface AppSynBalanceService extends BaseService {

    
	/**
	 * 查询用户银行交易明细
	 * @param accountId 电子账号
	 * @param startDate 起始日期 （YYYYMMDD）
	 * @param endDate	结束日期（YYYYMMDD）
	 * @param type  0-所有交易 1-转入交易 2-转出交易 9-指定交易类型
	 * @param transType  type=9必填，后台交易类型
	 * @param pageNum  
	 * @param pageSize
	 * @return
	 */
	public BankCallBean queryAccountDetails(Integer userId,String accountId,String startDate,String endDate,String type,String transType,String pageNum,String pageSize);
	
	/**
	 * 处理线下充值
	 * @param account
	 * @param transAmount
	 * @param string 
	 * @param user 
	 * @return
	 */
	public boolean insertAccountDetails(Account account,BigDecimal transAmount,String txData,String txTime,String traceNo, String username, String ip)throws Exception;
	
}

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
package com.hyjf.bank.service.user.synbalance;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.UnderLineRecharge;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import java.util.List;

public interface SynBalanceService extends BaseService {

    
	/**
	 * 查询用户银行交易明细
	 * @param accountId 电子账号
	 * @param startDate 起始日期 （YYYYMMDD）
	 * @param endDate	结束日期（YYYYMMDD）
	 * @param type  0-所有交易 1-转入交易 2-转出交易 9-指定交易类型
	 * @param transType  type=9必填，后台交易类型
	 * @param pageNum  
	 * @param pageSize
	 * @param platform 
	 * @return
	 */
	public BankCallBean queryAccountDetails(Integer userId,String accountId,String startDate,String endDate,String type,String transType,String pageNum,String pageSize,String inpDate,String inpTime,String relDate,String traceNo);
	
	/**
	 * 处理线下充值
	 * @param account
	 * @param transAmount
	 * @param string 
	 * @param user 
	 * @return
	 */
	public boolean insertAccountDetails(Account account,SynBalanceBean synBalanceBean, String username, String ip) throws Exception;

	/**
	 * 获取数据表中线下充值类型
	 * @return
	 * @Author : huanghui
	 */
	public List<UnderLineRecharge> selectByExample();
}

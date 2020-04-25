/**
 * Description:商户子账户转账（自动）
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.batch.merchant.transfer;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.MerchantAccount;

public interface TransferService extends BaseService {

	/**
	 * 查询商户子账户列表
	 * 
	 * @return
	 */

	List<MerchantAccount> selectMerchantAccountList(int status);

	/**
	 * 查询子账户余额信息
	 * 
	 * @return
	 */

	JSONArray merchantQueryAccts();

	/**
	 * 充值手续费整点保存
	 * 
	 * @param accCodeIn
	 * @param avlBal
	 */

	void insertRechargeFeeBalanceStatistics(String accCodeIn, BigDecimal avlBal);

	/**
	 * 调用商户子账户转账接口转账
	 * 
	 * @param merchantAccountOut
	 * @param merchantAccountIn
	 * @param transferAmount
	 * @return
	 */

	boolean merchantTransfer(MerchantAccount merchantAccountOut, MerchantAccount merchantAccountIn, BigDecimal transferAmount);

	/**
	 * 发送邮件
	 * 
	 * @param inAccountCode
	 * @param avlBalIn
	 * @param balanceLimit
	 */

	void sendEmail(String inAccountCode, BigDecimal avlBalIn, BigDecimal balanceLimit);

}

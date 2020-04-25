/**
 * Description：用户交易明细service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.bank.service.user.trade;

import java.util.List;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.web.WebUserRechargeListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserTradeListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserWithdrawListCustomize;

public interface TradeDetailBankService extends BaseService {

	/**
	 * 查询用户交易明细的交易类型
	 * @return
	 */
	List<AccountTrade> searchTradeTypes();

	/**
	 * 查询用户收支明细列表
	 * @param trade
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	List<WebUserTradeListCustomize> searchUserTradeList(TradeListBankBean trade, int limitStart, int limitEnd);

	/**
	 * 统计用户收支明细的数据总数
	 * @param form
	 * @return
	 */
	int countUserTradeRecordTotal(TradeListBankBean form);

	/**
	 * 查询用户充值记录
	 * @param recharge
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	List<WebUserRechargeListCustomize> searchUserRechargeList(RechargeListBankBean recharge, int limitStart, int limitEnd);

	/**
	 * 获取用户充值记录数
	 * @param form
	 * @return
	 */
	int countUserRechargeRecordTotal(RechargeListBankBean form);

	/**
	 * 查询用户取现记录
	 * @param withdraw
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	List<WebUserWithdrawListCustomize> searchUserWithdrawList(WithdrawListBankBean withdraw, int limitStart, int limitEnd);

	/**
	 * 查询用户取现记录总数
	 * @param form
	 * @return
	 */
	int countUserWithdrawRecordTotal(WithdrawListBankBean form);
	
	/**
	 * 交易类型列表
	 * @return
	 */
	List<AccountTrade> selectTradeTypes();

}

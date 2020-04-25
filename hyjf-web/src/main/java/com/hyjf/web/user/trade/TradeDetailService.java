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
package com.hyjf.web.user.trade;

import java.util.List;

import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.web.WebUserRechargeListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserTradeListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserWithdrawListCustomize;
import com.hyjf.web.BaseService;

public interface TradeDetailService extends BaseService {

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
	List<WebUserTradeListCustomize> searchUserTradeList(TradeListBean trade, int limitStart, int limitEnd);

	/**
	 * 统计用户收支明细的数据总数
	 * @param form
	 * @return
	 */
	int countUserTradeRecordTotal(TradeListBean form);

	/**
	 * 查询用户充值记录
	 * @param recharge
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	List<WebUserRechargeListCustomize> searchUserRechargeList(RechargeListBean recharge, int limitStart, int limitEnd);

	/**
	 * 获取用户充值记录数
	 * @param form
	 * @return
	 */
	int countUserRechargeRecordTotal(RechargeListBean form);

	/**
	 * 查询用户取现记录
	 * @param withdraw
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	List<WebUserWithdrawListCustomize> searchUserWithdrawList(WithdrawListBean withdraw, int limitStart, int limitEnd);

	/**
	 * 查询用户取现记录总数
	 * @param form
	 * @return
	 */
	int countUserWithdrawRecordTotal(WithdrawListBean form);
	
	/**
	 * 交易类型列表
	 * @return
	 */
	List<AccountTrade> selectTradeTypes();

}

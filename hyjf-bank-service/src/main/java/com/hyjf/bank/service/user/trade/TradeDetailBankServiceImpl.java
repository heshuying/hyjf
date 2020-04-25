/**
 * Description：用户交易明细service接口实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.bank.service.user.trade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.auto.AccountTradeExample;
import com.hyjf.mybatis.model.customize.web.WebUserRechargeListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserTradeListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserWithdrawListCustomize;

@Service
public class TradeDetailBankServiceImpl extends BaseServiceImpl implements TradeDetailBankService {

	@Override
	public List<AccountTrade> searchTradeTypes() {
		AccountTradeExample example = new AccountTradeExample();
		AccountTradeExample.Criteria crt = example.createCriteria();
		crt.andStatusEqualTo(1);
		List<AccountTrade> list = accountTradeMapper.selectByExample(example);
		return list;
	}

	@Override
	public List<WebUserTradeListCustomize> searchUserTradeList(TradeListBankBean trade, int limitStart, int limitEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		String userId = StringUtils.isNotEmpty(trade.getUserId()) ? trade.getUserId() : null;
		String tradeType = StringUtils.isNotEmpty(trade.getTrade()) ? trade.getTrade() : null;//交易类型
		String startDate = StringUtils.isNotEmpty(trade.getStartDate()) ? trade.getStartDate() : null;
		String endDate = StringUtils.isNotEmpty(trade.getEndDate()) ? trade.getEndDate() : null;
		params.put("userId", userId);
		params.put("trade", tradeType);//下拉列表中的 交易类型
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
		List<WebUserTradeListCustomize> list = webUserTradeDetailCustomizeMapper.selectUserTradeList(params);//查数据库会带着交易类型
		return list;
	}

	@Override
	public int countUserTradeRecordTotal(TradeListBankBean form) {
		Map<String, Object> params = new HashMap<String, Object>();
		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
		String trade = StringUtils.isNotEmpty(form.getTrade()) ? form.getTrade() : null;
		String startDate = StringUtils.isNotEmpty(form.getStartDate()) ? form.getStartDate() : null;
		String endDate = StringUtils.isNotEmpty(form.getEndDate()) ? form.getEndDate() : null;
		params.put("userId", userId);
		params.put("trade", trade);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		int total = webUserTradeDetailCustomizeMapper.countUserTradeRecordTotal(params);
		return total;
	}

	@Override
	public List<WebUserRechargeListCustomize> searchUserRechargeList(RechargeListBankBean recharge, int limitStart,
			int limitEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		String userId = StringUtils.isNotEmpty(recharge.getUserId()) ? recharge.getUserId() : null;
		String status = StringUtils.isNotEmpty(recharge.getStatus()) ? recharge.getStatus() : null;
		String startDate = StringUtils.isNotEmpty(recharge.getStartDate()) ? recharge.getStartDate() : null;
		String endDate = StringUtils.isNotEmpty(recharge.getEndDate()) ? recharge.getEndDate() : null;
		
		params.put("userId", userId);
		params.put("status", status);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
		List<WebUserRechargeListCustomize> list = webUserTradeDetailCustomizeMapper.selectUserRechargeList(params);
		return list;
	}

	@Override
	public int countUserRechargeRecordTotal(RechargeListBankBean form) {
		Map<String, Object> params = new HashMap<String, Object>();
		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
		String status = StringUtils.isNotEmpty(form.getStatus()) ? form.getStatus() : null;
		String startDate = StringUtils.isNotEmpty(form.getStartDate()) ? form.getStartDate() : null;
		String endDate = StringUtils.isNotEmpty(form.getEndDate()) ? form.getEndDate() : null;
		params.put("userId", userId);
		params.put("status", status);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		int total = webUserTradeDetailCustomizeMapper.countUserRechargeRecordTotal(params);
		return total;
	}

	@Override
	public List<WebUserWithdrawListCustomize> searchUserWithdrawList(WithdrawListBankBean withdraw, int limitStart,
			int limitEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		String userId = StringUtils.isNotEmpty(withdraw.getUserId()) ? withdraw.getUserId() : null;
		String status = StringUtils.isNotEmpty(withdraw.getStatus()) ? withdraw.getStatus() : null;
		String startDate = StringUtils.isNotEmpty(withdraw.getStartDate()) ? withdraw.getStartDate() : null;
		String endDate = StringUtils.isNotEmpty(withdraw.getEndDate()) ? withdraw.getEndDate() : null;
		params.put("userId", userId);
		params.put("status", status);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
		List<WebUserWithdrawListCustomize> list = webUserTradeDetailCustomizeMapper.selectUserWithdrawList(params);
		return list;
	}

	@Override
	public int countUserWithdrawRecordTotal(WithdrawListBankBean form) {
		Map<String, Object> params = new HashMap<String, Object>();
		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
		String status = StringUtils.isNotEmpty(form.getStatus()) ? form.getStatus() : null;
		String startDate = StringUtils.isNotEmpty(form.getStartDate()) ? form.getStartDate() : null;
		String endDate = StringUtils.isNotEmpty(form.getEndDate()) ? form.getEndDate() : null;
		params.put("userId", userId);
		params.put("status", status);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		int total = webUserTradeDetailCustomizeMapper.countUserWithdrawRecordTotal(params);
		return total;
	}

	/**
	 * 查询用户交易明细的交易类型
	 * @return
	 */
	@Override
	public List<AccountTrade> selectTradeTypes() {
		AccountTradeExample example = new AccountTradeExample();
		AccountTradeExample.Criteria crt=example.createCriteria();
		crt.andStatusEqualTo(1);
		List<AccountTrade> list=accountTradeMapper.selectByExample(example);
		return list;
	}

}

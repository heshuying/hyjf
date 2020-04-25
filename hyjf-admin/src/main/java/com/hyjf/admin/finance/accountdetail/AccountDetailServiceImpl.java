package com.hyjf.admin.finance.accountdetail;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.auto.AccountTradeExample;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.mybatis.model.customize.AccountManageCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminAccountDetailDataRepairCustomize;

@Service
public class AccountDetailServiceImpl extends BaseServiceImpl implements AccountDetailService {

	/**
	 * 查询符合条件的资金明细数量
	 * 
	 * @param accountInfoCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public Integer queryAccountDetailCount(AccountDetailCustomize accountDetailCustomize) {
		Integer accountCount = null;
		
		// 为了优化检索查询，判断参数是否全为空，为空不进行带join count
		if(checkFormAllBlank(accountDetailCustomize)){
			accountCount = this.accountDetailCustomizeMapper.queryAccountDetailCountAll(accountDetailCustomize);
		}else{
			accountCount = this.accountDetailCustomizeMapper.queryAccountDetailCount(accountDetailCustomize);
		}
		return accountCount;

	}
	
	private boolean checkFormAllBlank(AccountDetailCustomize accountDetailCustomize) {
		boolean reuslt = true;
		
		if (accountDetailCustomize.getUserId() != null) {
			return false;
		}
		if (StringUtils.isNotBlank(accountDetailCustomize.getReferrerName())) {
			return false;
		}
		if (StringUtils.isNotBlank(accountDetailCustomize.getTradeTypeSearch())) {
			return false;
		}
		if (StringUtils.isNotBlank(accountDetailCustomize.getUsername())) {
			return false;
		}
		if (StringUtils.isNotBlank(accountDetailCustomize.getNid())) {
			return false;
		}
		if (StringUtils.isNotBlank(accountDetailCustomize.getTypeSearch())) {
			return false;
		}
		if (StringUtils.isNotBlank(accountDetailCustomize.getIsBank())) {
			return false;
		}
		if (StringUtils.isNotBlank(accountDetailCustomize.getCheckStatus())) {
			return false;
		}
		if (StringUtils.isNotBlank(accountDetailCustomize.getTradeStatus())) {
			return false;
		}
		if (StringUtils.isNotBlank(accountDetailCustomize.getAccountId())) {
			return false;
		}
		if (StringUtils.isNotBlank(accountDetailCustomize.getSeqNo())) {
			return false;
		}
		if (accountDetailCustomize.getBalance() != null) {
			return false;
		}
		if (accountDetailCustomize.getFrost() != null) {
			return false;
		}
		if (StringUtils.isNotBlank(accountDetailCustomize.getStartDate())) {
			return false;
		}
		if (StringUtils.isNotBlank(accountDetailCustomize.getEndDate())) {
			return false;
		}
		
		if (StringUtils.isNotBlank(accountDetailCustomize.getRemarkSrch())) {
			return false;
		}
		
		return reuslt;
	}


	/**
	 * 资金明细列表查询
	 * 
	 * @param accountInfoCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public List<AccountDetailCustomize> queryAccountDetails(AccountDetailCustomize accountDetailCustomize) {
		List<AccountDetailCustomize> accountInfos = this.accountDetailCustomizeMapper
				.queryAccountDetails(accountDetailCustomize);
		return accountInfos;

	}

	/**
	 * 查询用户交易明细的交易类型
	 * 
	 * @return
	 */
	@Override
	public List<AccountTrade> selectTradeTypes() {
		AccountTradeExample example = new AccountTradeExample();
		AccountTradeExample.Criteria crt = example.createCriteria();
		crt.andStatusEqualTo(1);
		List<AccountTrade> list = accountTradeMapper.selectByExample(example);
		return list;
	}

	@Override
	public void repayDataRepair() throws Exception {
		// 查询出20170120还款后,交易明细有问题的用户ID
		List<AdminAccountDetailDataRepairCustomize> userList = this.accountDetailCustomizeMapper
				.queryAccountDetailErrorUserList();
		if (userList != null && userList.size() > 0) {
			for (AdminAccountDetailDataRepairCustomize adminAccountDetailDataRepairCustomize : userList) {
				Integer userId = adminAccountDetailDataRepairCustomize.getUserId();
				// 查询交易明细最小的id
				AdminAccountDetailDataRepairCustomize accountList = this.accountDetailCustomizeMapper
						.queryAccountDetailIdByUserId(userId);
				if (accountList != null) {
					Integer accountListId = Integer.parseInt(accountList.getId());
					this.repayDataRepair(userId, accountListId);
				}
			}
		}
	}

	private void repayDataRepair(Integer userId, Integer accountListId) throws Exception {
		// 根据Id查询此条交易明细
		AccountList accountList = this.accountListMapper.selectByPrimaryKey(accountListId);
		if (accountList != null) {
			// 获取账户可用余额
			BigDecimal balance = accountList.getBalance();
			// 查询此用户的下一条交易明细
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			param.put("id", accountListId);
			AccountList nextAccountList = this.accountDetailCustomizeMapper.selectNextAccountList(param);
			// 如果下一条交易明细不为空
			if (nextAccountList != null) {
				// 根据查询用交易类型查询用户操作金额
				AccountTrade accountTrade = this.accountDetailCustomizeMapper.selectAccountTrade(nextAccountList
						.getTrade());
				if (accountTrade != null && Validator.isNotNull(accountTrade.getOperation())) {
					// 更新交易明细的账户余额
					if ("ADD".equals(accountTrade.getOperation())) {
						nextAccountList.setBalance(balance.add(nextAccountList.getAmount()));
					} else if ("SUB".equals(accountTrade.getOperation())&&!"cash_success".equals(nextAccountList.getTrade())) {
						nextAccountList.setBalance(balance.subtract(nextAccountList.getAmount()));
					} else if ("SUB".equals(accountTrade.getOperation())&&"cash_success".equals(nextAccountList.getTrade())){
						// 提现不处理
						return;
					}else if ("UNCHANGED".equals(accountTrade.getOperation())) {
						nextAccountList.setBalance(balance);
					}
					// 更新用户的交易明细
					boolean isAccountListUpdateFlag = this.accountListMapper
							.updateByPrimaryKeySelective(nextAccountList) > 0 ? true : false;
					if (isAccountListUpdateFlag) {
						// 递归更新下一条交易明细
						this.repayDataRepair(userId, nextAccountList.getId());
					} else {
						throw new Exception("交易明细更新失败,交易明细ID:" + nextAccountList.getId());
					}
				} else {
					throw new Exception("查询huiyingdai_account_trade交易类型失败,交易明细Value:" + nextAccountList.getTrade());
				}
			} else {
				System.out.println("未查询到下一条交易明细,上一条交易明细ID:" + accountListId);
			}
		} else {
			throw new Exception("获取交易明细失败" + accountListId);
		}
	}
}

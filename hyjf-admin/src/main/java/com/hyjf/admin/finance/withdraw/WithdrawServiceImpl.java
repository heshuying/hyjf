package com.hyjf.admin.finance.withdraw;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.AccountwithdrawExample;
import com.hyjf.mybatis.model.customize.WithdrawCustomize;

@Service(value="adminWithdrawServiceImpl")
public class WithdrawServiceImpl extends BaseServiceImpl implements WithdrawService {

	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private TransactionDefinition transactionDefinition;
	// 提现状态:提现中
	private static final int WITHDRAW_STATUS_WAIT = 1;
	// 提现状态:失败
	private static final int WITHDRAW_STATUS_FAIL = 3;
	// 提现状态:成功
	private static final int WITHDRAW_STATUS_SUCCESS = 2;

	/**
	 * 获取提现列表数量
	 *
	 * @param form
	 * @return
	 */
	@Override
	public int getWithdrawRecordCount(WithdrawBean form) {
		WithdrawCustomize withdrawCustomize = new WithdrawCustomize();
		BeanUtils.copyProperties(form, withdrawCustomize);
		return withdrawCustomizeMapper.selectWithdrawCount(withdrawCustomize);
	}

	/**
	 * 获取提现列表
	 *
	 * @return
	 */
	@Override
	public List<WithdrawCustomize> getWithdrawRecordList(WithdrawBean form) {
		WithdrawCustomize withdrawCustomize = new WithdrawCustomize();
		BeanUtils.copyProperties(form, withdrawCustomize);
		return withdrawCustomizeMapper.selectWithdrawList(withdrawCustomize);
	}

	@Override
	public Account getAccountByUserId(Integer userId) {
		AccountExample example = new AccountExample();
		AccountExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		List<Account> accountList = this.accountMapper.selectByExample(example);
		if (accountList != null && accountList.size() > 0) {
			return accountList.get(0);
		}
		return null;
	}

	/**
	 * 根据订单号查询提现订单
	 */
	@Override
	public Accountwithdraw queryAccountwithdrawByNid(String nid, Integer userId) {
		AccountwithdrawExample example = new AccountwithdrawExample();
		AccountwithdrawExample.Criteria cra = example.createCriteria();
		cra.andNidEqualTo(nid);
		cra.andUserIdEqualTo(userId);
		cra.andStatusEqualTo(WITHDRAW_STATUS_WAIT);
		List<Accountwithdraw> accountwithdrawList = this.accountwithdrawMapper.selectByExample(example);
		if (accountwithdrawList != null && accountwithdrawList.size() == 1) {
			return accountwithdrawList.get(0);
		}
		return null;
	}

	/**
	 * 提现成功后,更新用户账户信息,提现记录
	 * 
	 * @param userId
	 * @param nid
	 * @return
	 */
	@Override
	public boolean updateAccountAfterWithdraw(Integer userId, String nid, Map<String, String> param) {
		Integer nowTime = GetDate.getNowTime10();
		// 根据用户ID查询用户账户信息
		Account account = this.getAccountByUserId(userId);
		// 根据提现订单号查询用户充值信息
		Accountwithdraw accountwithdraw = this.queryAccountwithdrawByNid(nid, userId);
		if (accountwithdraw != null) {
			// 如果信息未被处理
			if (accountwithdraw.getStatus() == WITHDRAW_STATUS_SUCCESS) {
				// 如果是已经提现成功了
				System.out.println("提现订单已被处理:提现订单号:" + nid + ",用户ID:" + userId);
				return false;
			} else {
				// 查询是否已经处理过
				AccountListExample accountListExample = new AccountListExample();
				accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("cash_success");
				int accountlistCnt = this.accountListMapper.countByExample(accountListExample);
				// 未被处理
				if (accountlistCnt == 0) {
					try {
						accountwithdraw.setStatus(WITHDRAW_STATUS_SUCCESS);// 4:成功
						accountwithdraw.setUpdateTime(nowTime);
						boolean isAccountwithdrawFlag = this.accountwithdrawMapper.updateByPrimaryKeySelective(accountwithdraw) > 0 ? true : false;
						if (!isAccountwithdrawFlag) {
							throw new Exception("提现后,更新用户提现记录表失败!");
						}
						Account newAccount = new Account();
						// 更新账户信息
						newAccount.setUserId(userId);// 用户Id
						newAccount.setBankTotal(accountwithdraw.getTotal()); // 累加到账户总资产
						newAccount.setBankBalance(accountwithdraw.getTotal()); // 累加可用余额
						newAccount.setBankBalanceCash(accountwithdraw.getTotal());// 江西银行可用余额
						boolean isAccountUpdateFlag = this.adminAccountCustomizeMapper.updateBankWithdrawSuccess(newAccount) > 0 ? true : false;
						if (!isAccountUpdateFlag) {
							throw new Exception("提现后,更新用户Account表失败!");
						}
						// 写入收支明细
						AccountList accountList = new AccountList();
						// 重新获取用户信息
						account = this.getAccountByUserId(userId);
						accountList.setNid(nid);
						accountList.setUserId(userId);
						accountList.setAmount(accountwithdraw.getTotal());
						accountList.setType(2);
						accountList.setTrade("cash_success");
						accountList.setTradeCode("balance");
						accountList.setTotal(account.getTotal());
						accountList.setBalance(account.getBalance());
						accountList.setFrost(account.getFrost());
						accountList.setAwait(account.getAwait());
						accountList.setRepay(account.getRepay());
						accountList.setBankTotal(account.getBankTotal()); // 银行总资产
						accountList.setBankBalance(account.getBankBalance()); // 银行可用余额
						accountList.setBankFrost(account.getBankFrost());// 银行冻结金额
						accountList.setBankWaitCapital(account.getBankWaitCapital());// 银行待还本金
						accountList.setBankWaitInterest(account.getBankWaitInterest());// 银行待还利息
						accountList.setBankAwaitCapital(account.getBankAwaitCapital());// 银行待收本金
						accountList.setBankAwaitInterest(account.getBankAwaitInterest());// 银行待收利息
						accountList.setBankAwait(account.getBankAwait());// 银行待收总额
						accountList.setBankInterestSum(account.getBankInterestSum()); // 银行累计收益
						accountList.setBankInvestSum(account.getBankInvestSum());// 银行累计出借
						accountList.setBankWaitRepay(account.getBankWaitRepay());// 银行待还金额
						accountList.setPlanBalance(account.getPlanBalance());//汇计划账户可用余额
						accountList.setPlanFrost(account.getPlanFrost());
						accountList.setSeqNo(String.valueOf(accountwithdraw.getSeqNo()));
						accountList.setTxDate(accountwithdraw.getTxDate());
						accountList.setTxTime(accountwithdraw.getTxTime());
						accountList.setBankSeqNo(String.valueOf(accountwithdraw.getTxDate() + accountwithdraw.getTxTime() + accountwithdraw.getSeqNo()));
						accountList.setAccountId(accountwithdraw.getAccountId());
						accountList.setRemark("网站提现");
						accountList.setCreateTime(nowTime);
						accountList.setBaseUpdate(nowTime);
						accountList.setOperator(param.get("userName"));
						accountList.setIp(param.get("ip"));
						accountList.setIsUpdate(0);
						accountList.setBaseUpdate(0);
						accountList.setInterest(null);
						accountList.setIsBank(1);
						accountList.setWeb(0);
						accountList.setCheckStatus(0);// 对账状态0：未对账 1：已对账
						accountList.setTradeStatus(1);// 0失败1成功2失败
						boolean isAccountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
						if (!isAccountListFlag) {
							throw new Exception("提现成功后,插入交易明细表失败~!");
						}
						return isAccountListFlag;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	/**
	 * 提现失败后,更新用户的提现记录
	 * 
	 * @param userId
	 * @param nid
	 * @return
	 */
	@Override
	public boolean updateAccountAfterWithdrawFail(Integer userId, String nid) throws Exception {
		// 根据提现订单号查询用户充值信息
		Accountwithdraw accountwithdraw = this.queryAccountwithdrawByNid(nid, userId);
		if (accountwithdraw != null) {
			if (accountwithdraw.getStatus() == WITHDRAW_STATUS_WAIT) {
				accountwithdraw.setStatus(WITHDRAW_STATUS_FAIL);
				boolean isAccountWithdrawFlag = this.accountwithdrawMapper.updateByPrimaryKeySelective(accountwithdraw) > 0 ? true : false;
				if (!isAccountWithdrawFlag) {
					throw new Exception("提现失败后,手动确认,更新提现记录状态失败~~~!,提现订单号:" + nid + ",用户ID:" + userId);
				}
				return isAccountWithdrawFlag;
			}
		}
		return false;
	}
}

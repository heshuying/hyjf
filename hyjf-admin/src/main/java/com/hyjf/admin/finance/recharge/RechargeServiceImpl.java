package com.hyjf.admin.finance.recharge;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.finance.returncash.ReturncashDefine;
import com.hyjf.admin.finance.transfer.TransferDefine;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.AccountRechargeExample;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.BankMerchantAccount;
import com.hyjf.mybatis.model.auto.BankMerchantAccountExample;
import com.hyjf.mybatis.model.auto.BankMerchantAccountList;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.BanksConfigExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

@Service(value="adminRechargeServiceImpl")
public class RechargeServiceImpl extends BaseServiceImpl implements RechargeService {
	// 充值状态:充值中
	private static final int RECHARGE_STATUS_WAIT = 1;
	// 充值状态:成功
	private static final int RECHARGE_STATUS_SUCCESS = 2;
	// 充值状态:失败
	private static final int RECHARGE_STATUS_FAIL = 3;

	/**
	 * 查询符合条件的充值记录数量
	 * 
	 * @param rechargeCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public Integer queryRechargeCount(RechargeCustomize rechargeCustomize) {
		Integer accountCount = this.rechargeCustomizeMapper.queryRechargeCount(rechargeCustomize);
		return accountCount;

	}

	/**
	 * 充值管理列表查询
	 * 
	 * @param rechargeCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public List<RechargeCustomize> queryRechargeList(RechargeCustomize rechargeCustomize) {
		List<RechargeCustomize> accountInfos = this.rechargeCustomizeMapper.queryRechargeList(rechargeCustomize);
		return accountInfos;

	}

	/**
	 * 根据订单号nid获取充值信息
	 * 
	 * @param nid
	 * @return
	 */
	public AccountRecharge queryRechargeByNid(String nid) {

		AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
		AccountRechargeExample.Criteria aCriteria = accountRechargeExample.createCriteria();
		aCriteria.andNidEqualTo(nid);
		aCriteria.andStatusEqualTo(RECHARGE_STATUS_WAIT);
		List<AccountRecharge> accountRecharges = this.accountRechargeMapper.selectByExample(accountRechargeExample);
		if (accountRecharges != null && accountRecharges.size() == 1) {
			return accountRecharges.get(0);
		}
		return null;
	}

	/**
	 * 校验用户
	 * 
	 * @param outUserName
	 * @param ret
	 * @author Michael
	 */
	@Override
	public void checkTransfer(String outUserName, JSONObject ret) {
		UsersExample userExample = new UsersExample();
		UsersExample.Criteria userCrt = userExample.createCriteria();
		userCrt.andUsernameEqualTo(outUserName);
		List<Users> users = this.usersMapper.selectByExample(userExample);
		if (users != null && users.size() == 1) {
			Users user = users.get(0);
			BankOpenAccount bankAccount = getBankOpenAccount(user.getUserId());
			if (bankAccount != null && !Validator.isNull(bankAccount.getAccount())) {
				ret.put(ReturncashDefine.JSON_VALID_STATUS_KEY, ReturncashDefine.JSON_VALID_STATUS_OK);
			} else {
				ret.put(TransferDefine.JSON_VALID_INFO_KEY, "用户未开户，无法转账!");
			}
		} else {
			ret.put(TransferDefine.JSON_VALID_INFO_KEY, "未查询到正确的用户信息!");
		}
	}

	/**
	 * 手动充值处理
	 * 
	 * @param form
	 * @param chinapnrBean
	 * @return
	 */
	public int updateHandRechargeRecord(RechargeBean form, BankCallBean bankBean, UserInfoCustomize userInfo, String accountId) {
		int ret = 0;

		// 增加时间
		Integer time = GetDate.getMyTimeInMillis();
		// 商户userID
		// Integer merUserId = Integer.valueOf(ShiroUtil.getLoginUserId());
		// 客户userID
		Integer cusUserId = form.getUserId();
		// 操作者用户名
		String operator = ShiroUtil.getLoginUsername();

		// 更新账户信息
        AccountExample accountExample = new AccountExample();
        AccountExample.Criteria accountCriteria = accountExample.createCriteria();
        accountCriteria.andUserIdEqualTo(cusUserId);
        Account account = accountMapper.selectByExample(accountExample).get(0);
        BigDecimal bankBalanceCash =
                account.getBankBalanceCash() == null ? BigDecimal.ZERO : account.getBankBalanceCash();
        BigDecimal money = form.getMoney();// 充值金额
        account.setBankBalance(account.getBankBalance().add(money));
        account.setBankTotal(account.getBankTotal().add(money)); // 累加到账户总资产
        account.setBankBalanceCash(bankBalanceCash.add(money));
        ret += this.accountMapper.updateByExampleSelective(account, accountExample);

        // 写入充值表
        AccountRecharge accountRecharge = new AccountRecharge();
        accountRecharge.setNid(bankBean.getLogOrderId());
        accountRecharge.setUserId(cusUserId);
        accountRecharge.setStatus(RECHARGE_STATUS_SUCCESS);
        accountRecharge.setMoney(money);
        accountRecharge.setBalance(account.getBankBalance());
        accountRecharge.setFee(new BigDecimal(0));
        accountRecharge.setGateType("ADMIN");
        accountRecharge.setType(0);// 线下充值
        accountRecharge.setRemark(form.getRemark());
        accountRecharge.setCreateTime(time);
        accountRecharge.setOperator(operator);
        accountRecharge.setAddtime(time.toString());
        accountRecharge.setAddip(bankBean.getLogIp());
        accountRecharge.setUpdateTime(time);
        accountRecharge.setNok(0);
        accountRecharge.setDianfuFee(new BigDecimal(0));
        accountRecharge.setIsok(0);
        accountRecharge.setClient(0);// 0pc 1app
        accountRecharge.setIsok11(0);
        accountRecharge.setFlag(0);
        accountRecharge.setActivityFlag(0);
        accountRecharge.setUsername(userInfo.getUserName());
        accountRecharge.setIsBank(1);// 资金托管平台 0:汇付,1:江西银行
        accountRecharge.setTxDate(Integer.parseInt(bankBean.getTxDate()));// 交易日期
        accountRecharge.setTxTime(Integer.parseInt(bankBean.getTxTime()));// 交易时间
        accountRecharge.setSeqNo(Integer.parseInt(bankBean.getSeqNo())); // 交易流水号
        accountRecharge.setBankSeqNo(bankBean.getTxDate() + bankBean.getTxTime() + bankBean.getSeqNo()); // 交易日期+交易时间+交易流水号
        accountRecharge.setAccountId(accountId);// 电子账号
        ret += this.accountRechargeMapper.insertSelective(accountRecharge);

        
	    // 写入收支明细
        AccountList accountList = new AccountList();
        accountList.setNid(bankBean.getLogOrderId());
        accountList.setSeqNo(bankBean.getSeqNo());
        accountList.setTxDate(Integer.parseInt(bankBean.getTxDate()));
        accountList.setTxTime(Integer.parseInt(bankBean.getTxTime()));
        accountList.setBankSeqNo(bankBean.getTxDate() + bankBean.getTxTime() + bankBean.getSeqNo());
        accountList.setCheckStatus(0);
        accountList.setTradeStatus(1);
        accountList.setUserId(form.getUserId());
        accountList.setAccountId(accountId);
        accountList.setAmount(money);
        accountList.setType(1);// 1收入2支出3冻结
        accountList.setTrade("platform_transfer");
        accountList.setTradeCode("balance");
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
        accountList.setTotal(account.getTotal());
        accountList.setBalance(account.getBalance());
        accountList.setFrost(account.getFrost());
        accountList.setAwait(account.getAwait());
        accountList.setRepay(account.getRepay());
        accountList.setRemark("平台转账");
        accountList.setCreateTime(time);
        accountList.setOperator(operator);
        accountList.setIp(bankBean.getLogIp());
        accountList.setIsUpdate(0);
        accountList.setBaseUpdate(0);
        accountList.setInterest(null);
        accountList.setWeb(2);
        accountList.setIsBank(1);
        ret += this.accountListMapper.insertSelective(accountList);

		// 写入网站收支
		AccountWebList accountWebList = new AccountWebList();
		accountWebList.setOrdid(bankBean.getLogOrderId());
		accountWebList.setAmount(money);
		accountWebList.setType(2);// 1收入2支出
		accountWebList.setTrade("platform_transfer");
		accountWebList.setTradeType("平台转账");
		accountWebList.setUserId(form.getUserId());
		accountWebList.setUsrcustid(accountId);
		accountWebList.setTruename(userInfo.getTrueName());
		accountWebList.setRegionName(userInfo.getRegionName());
		accountWebList.setBranchName(userInfo.getBranchName());
		accountWebList.setDepartmentName(userInfo.getDepartmentName());
		accountWebList.setRemark(form.getRemark());
		accountWebList.setCreateTime(time);
		accountWebList.setOperator(operator);
		accountWebList.setFlag(1);
		ret += this.accountWebListMapper.insertSelective(accountWebList);

		// 添加红包账户明细
        BankMerchantAccount nowBankMerchantAccount = this.getBankMerchantAccount(bankBean.getAccountId());
        nowBankMerchantAccount.setAvailableBalance(nowBankMerchantAccount.getAvailableBalance().subtract(money));
        nowBankMerchantAccount.setAccountBalance(nowBankMerchantAccount.getAccountBalance().subtract(money));
        nowBankMerchantAccount.setUpdateTime(GetDate.getNowTime10());
        
        // 更新红包账户信息
        int updateCount = this.updateBankMerchantAccount(nowBankMerchantAccount);
        if(updateCount > 0){
            UserInfoCustomize userInfoCustomize = this.queryUserInfoByUserId(form.getUserId());
            
            // 添加红包明细
            BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
            bankMerchantAccountList.setOrderId(bankBean.getLogOrderId());
            bankMerchantAccountList.setUserId(form.getUserId());
            bankMerchantAccountList.setAccountId(accountId);
            bankMerchantAccountList.setAmount(money);
            bankMerchantAccountList.setBankAccountCode(bankBean.getAccountId());
            bankMerchantAccountList.setBankAccountBalance(nowBankMerchantAccount.getAccountBalance());
            bankMerchantAccountList.setBankAccountFrost(nowBankMerchantAccount.getFrost());
            bankMerchantAccountList.setTransType(CustomConstants.BANK_MER_TRANS_TYPE_AUTOMATIC);
            bankMerchantAccountList.setType(CustomConstants.BANK_MER_TYPE_EXPENDITURE);
            bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS);
            bankMerchantAccountList.setTxDate(Integer.parseInt(bankBean.getTxDate()));
            bankMerchantAccountList.setTxTime(Integer.parseInt(bankBean.getTxTime()));
            bankMerchantAccountList.setSeqNo(bankBean.getSeqNo());
            bankMerchantAccountList.setCreateTime(new Date());
            bankMerchantAccountList.setUpdateTime(new Date());
            bankMerchantAccountList.setCreateUserId(form.getUserId());
            bankMerchantAccountList.setUpdateUserId(form.getUserId());
            bankMerchantAccountList.setRegionName(userInfoCustomize.getRegionName());
            bankMerchantAccountList.setBranchName(userInfoCustomize.getBranchName());
            bankMerchantAccountList.setDepartmentName(userInfoCustomize.getDepartmentName());
            bankMerchantAccountList.setCreateUserName(userInfoCustomize.getUserName());
            bankMerchantAccountList.setUpdateUserName(userInfoCustomize.getUserName());
            bankMerchantAccountList.setRemark("平台转账");
            
            ret += this.bankMerchantAccountListMapper.insertSelective(bankMerchantAccountList);
        }
		return ret;
	}
	
    /**
     * 
     * 更新红包账户
     * @param account
     * @return
     */
    public int updateBankMerchantAccount(BankMerchantAccount account){
        return bankMerchantAccountMapper.updateByPrimaryKeySelective(account);
    }
    
    /**
     * 
     * 加载红包账户
     * @param accountCode
     * @return
     */
    public BankMerchantAccount getBankMerchantAccount(String accountCode) {
         BankMerchantAccountExample bankMerchantAccountExample = new BankMerchantAccountExample();
         bankMerchantAccountExample.createCriteria().andAccountCodeEqualTo(accountCode);
         List<BankMerchantAccount> bankMerchantAccounts = bankMerchantAccountMapper.selectByExample(bankMerchantAccountExample);
         if (bankMerchantAccounts != null && bankMerchantAccounts.size() != 0) {
             return bankMerchantAccounts.get(0);
         } else {
             return null;
         }
     }
    
    public UserInfoCustomize queryUserInfoByUserId(Integer userId) {
        return userInfoCustomizeMapper.queryUserInfoByUserId(userId);
    }


	/**
	 * 根据用户名查询用户ID
	 * 
	 * @param username
	 * @return
	 */
	public Users queryUserInfoByUserName(String username) {
		UsersExample usersExample = new UsersExample();
		UsersExample.Criteria uCriteria = usersExample.createCriteria();
		uCriteria.andUsernameEqualTo(username);
		List<Users> cususers = this.usersMapper.selectByExample(usersExample);

		if (cususers != null && cususers.size() == 1) {
			return cususers.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据用户名查询用户信息
	 * 
	 * @param username
	 * @return
	 */
	public UserInfoCustomize queryUserInfoByName(String username) {
		// 如果该用户是员工
		UserInfoCustomize userInfoCustomize = this.userInfoCustomizeMapper.queryUserInfoByEmployeeName(username);
		if (userInfoCustomize == null) {
			// 该用户不是员工
			userInfoCustomize = this.userInfoCustomizeMapper.queryUserInfoByName(username);
		}

		return userInfoCustomize;
	}

	/**
	 * 充值掉单后,更新用户的账户信息
	 */
	@Override
	public boolean updateAccountAfterRecharge(Integer userId, String nid) throws Exception {
		Integer nowTime = GetDate.getNowTime10();
		// 根据用户ID查询用户账户信息
		Account account = this.getAccountByUserId(userId);
		// 根据充值订单号查询用户充值信息
		AccountRecharge accountRecharge = this.getAccountRecharge(nid, userId);
		if (accountRecharge != null) {
			// 更新提现记录状态:更新为充值成功
			accountRecharge.setStatus(RECHARGE_STATUS_SUCCESS);
			boolean isRechargeFlag = this.accountRechargeMapper.updateByPrimaryKeySelective(accountRecharge) > 0 ? true : false;
			if (!isRechargeFlag) {
				throw new Exception("后台确认充值:更新用户充值记录失败~~~~! 用户ID:" + userId);
			}
			// 更新用户账户信息
			Account newAccount = new Account();
			newAccount.setUserId(account.getUserId());
			newAccount.setBankTotal(accountRecharge.getBalance()); // 累加到账户总资产
			newAccount.setBankBalance(accountRecharge.getBalance()); // 累加可用余额
			newAccount.setBankBalanceCash(accountRecharge.getBalance());// 银行账户可用户
			boolean isAccountUpdateFlag = this.adminAccountCustomizeMapper.updateBankRechargeSuccess(newAccount) > 0 ? true : false;
			if (!isAccountUpdateFlag) {
				throw new Exception("后台确认充值:更新用户账户信息失败~~~~! 用户ID:" + userId);
			}
			// 插入交易明细
			// 重新获取用户账户信息
			account = this.getAccountByUserId(userId);
			// 生成交易明细
			AccountList accountList = new AccountList();
			accountList.setNid(accountRecharge.getNid());
			accountList.setUserId(userId);
			accountList.setAmount(accountRecharge.getBalance());
			accountList.setTxDate(accountRecharge.getTxDate());// 交易日期
			accountList.setTxTime(accountRecharge.getTxTime());// 交易时间
			accountList.setSeqNo(String.valueOf(accountRecharge.getSeqNo()));// 交易流水号
			accountList.setBankSeqNo(String.valueOf((accountRecharge.getTxDate() + accountRecharge.getTxTime() + accountRecharge.getSeqNo())));
			accountList.setType(1);
			accountList.setTrade("recharge");
			accountList.setTradeCode("balance");
			accountList.setAccountId(accountRecharge.getAccountId());
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
			accountList.setTotal(account.getTotal());
			accountList.setBalance(account.getBalance());
			accountList.setFrost(account.getFrost());
			accountList.setAwait(account.getAwait());
			accountList.setRepay(account.getRepay());
			accountList.setRemark("快捷充值");
			accountList.setCreateTime(nowTime);
			accountList.setBaseUpdate(nowTime);
			accountList.setOperator(String.valueOf(userId));
			accountList.setIp("");
			accountList.setIsUpdate(0);
			accountList.setBaseUpdate(0);
			accountList.setInterest(null);
			accountList.setWeb(0);
			accountList.setIsBank(1);// 是否是银行的交易记录 0:否 ,1:是
			accountList.setCheckStatus(0);// 对账状态0：未对账 1：已对账
			accountList.setTradeStatus(1);// 成功状态
			// 插入交易明细
			boolean isAccountListUpdateFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
			if (!isAccountListUpdateFlag) {
				throw new Exception("手动处理充值掉单,插入用户交易明细失败~~,用户ID:" + userId);
			}
			return isAccountListUpdateFlag;
		}
		return false;
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
	 * 根据订单号查询用户充值记录
	 * 
	 * @param nid
	 * @return
	 */
	private AccountRecharge getAccountRecharge(String nid, Integer userId) {
		AccountRechargeExample example = new AccountRechargeExample();
		AccountRechargeExample.Criteria cra = example.createCriteria();
		cra.andNidEqualTo(nid);
		cra.andUserIdEqualTo(userId);
		cra.andStatusEqualTo(RECHARGE_STATUS_WAIT);
		List<AccountRecharge> rechargeList = this.accountRechargeMapper.selectByExample(example);
		if (rechargeList != null && rechargeList.size() > 0) {
			return rechargeList.get(0);
		}
		return null;
	}

	/**
	 * 获取银行列表
	 */
	@Override
	public List<BanksConfig> getBankcardList() {
		return banksConfigMapper.selectByExample(new BanksConfigExample());
	}

	/**
	 * 充值失败后,更新用户充值订单状态
	 * 
	 * @throws Exception
	 */
	@Override
	public boolean updateAccountAfterRechargeFail(Integer userId, String nid) throws Exception {
		boolean isUpdateRechargeFalse = false;
		// 根据订单号,用户ID查询用户充值订单
		AccountRecharge accountRecharge = this.getAccountRecharge(nid, userId);
		if (accountRecharge != null) {
			if (accountRecharge.getStatus() == RECHARGE_STATUS_WAIT) {
				accountRecharge.setStatus(RECHARGE_STATUS_FAIL);
				isUpdateRechargeFalse = this.accountRechargeMapper.updateByPrimaryKeySelective(accountRecharge) > 0 ? true : false;
				if (!isUpdateRechargeFalse) {
					throw new Exception("充值失败确认,更新充值订单状态失败~~~,充值订单号:" + nid + ",用户ID:" + userId);
				}
				return isUpdateRechargeFalse;
			}
		}
		return isUpdateRechargeFalse;
	}

	/**
	 * 更新充值状态
	 * 
	 * @throws Exception
	 */
	@Override
	public boolean updateRechargeStatus(Integer userId, String nid) throws Exception {
		AccountRechargeExample example = new AccountRechargeExample();
		AccountRechargeExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		cra.andNidEqualTo(nid);
		cra.andStatusEqualTo(RECHARGE_STATUS_FAIL);
		List<AccountRecharge> rechargeList = this.accountRechargeMapper.selectByExample(example);
		if (rechargeList != null && rechargeList.size() == 1) {
			AccountRecharge recharge = rechargeList.get(0);
			// 充值状态改为充值中
			recharge.setStatus(RECHARGE_STATUS_WAIT);
			boolean isUpdateFlag = this.accountRechargeMapper.updateByPrimaryKeySelective(recharge) > 0 ? true : false;
			if (!isUpdateFlag) {
				throw new Exception("更新充值状体失败:充值订单号:" + nid + "用户ID:" + userId);
			}
			return isUpdateFlag;
		}
		return false;
	}
}

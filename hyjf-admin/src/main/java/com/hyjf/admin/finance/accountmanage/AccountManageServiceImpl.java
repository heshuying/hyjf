package com.hyjf.admin.finance.accountmanage;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.customize.AccountManageCustomize;
@Service
public class AccountManageServiceImpl extends BaseServiceImpl implements AccountManageService {

	/**
	 * 查询符合条件的用户数量
	 * @param accountInfoCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public Integer queryAccountCount(AccountManageCustomize accountInfoCustomize) {
		// 部门
		if (Validator.isNotNull(accountInfoCustomize.getCombotreeSrch())) {
		    if (accountInfoCustomize.getCombotreeSrch().contains(StringPool.COMMA)) {
		        String[] list = accountInfoCustomize.getCombotreeSrch().split(StringPool.COMMA);
		        accountInfoCustomize.setCombotreeListSrch(list);
		    } else {
		        accountInfoCustomize.setCombotreeListSrch(new String[]{accountInfoCustomize.getCombotreeSrch()});
		    }
		}

		Integer accountCount = null;
		// 为了优化检索查询，判断参数是否全为空，为空不进行带join count
		if(checkFormAllBlank(accountInfoCustomize)){
			accountCount = this.accountManageCustomizeMapper.queryAccountCountAll(accountInfoCustomize);
		}else{
			accountCount = this.accountManageCustomizeMapper.queryAccountCount(accountInfoCustomize);
		}
		return accountCount;

	}

	private boolean checkFormAllBlank(AccountManageCustomize accountInfoCustomize) {
		boolean reuslt = true;
		
		if (StringUtils.isNotBlank(accountInfoCustomize.getUserId())) {
			return false;
		}
		if (accountInfoCustomize.getTotal() != null) {
			return false;
		}
		if (accountInfoCustomize.getIncome() != null) {
			return false;
		}
		if (accountInfoCustomize.getExpend() != null) {
			return false;
		}
		if (accountInfoCustomize.getBalance() != null) {
			return false;
		}
		if (accountInfoCustomize.getBalanceCash() != null) {
			return false;
		}
		if (accountInfoCustomize.getBalanceFrost() != null) {
			return false;
		}
		if (accountInfoCustomize.getFrost() != null) {
			return false;
		}
		if (accountInfoCustomize.getAwait() != null) {
			return false;
		}
		if (accountInfoCustomize.getRepay() != null) {
			return false;
		}
		if (accountInfoCustomize.getFrostCash() != null) {
			return false;
		}
		if (accountInfoCustomize.getRecMoney() != null) {
			return false;
		}
		if (accountInfoCustomize.getFee() != null) {
			return false;
		}
		if (StringUtils.isNotBlank(accountInfoCustomize.getTruename())) {
			return false;
		}
		if (StringUtils.isNotBlank(accountInfoCustomize.getUsername())) {
			return false;
		}
		if (accountInfoCustomize.getUserTypeSearche()==1) {
			return false;
		}
		if (StringUtils.isNotBlank(accountInfoCustomize.getCombotreeSrch())) {
			return false;
		}
		
		
		return reuslt;
	}

	/**
	 * 账户管理列表查询
	 * @param accountInfoCustomize
	 * @return
	 * @author HBZ
	 */
	@Override
	public List<AccountManageCustomize> queryAccountInfos(AccountManageCustomize accountInfoCustomize) {
		// 为了优化检索查询，判断参数是否全为空，为空不进行带join count
		if(checkFormAllBlank(accountInfoCustomize)){
			accountInfoCustomize.setInitQuery(1);
	    }
		
		List<AccountManageCustomize> accountInfos= this.accountManageCustomizeMapper.queryAccountInfos(accountInfoCustomize);
		return accountInfos;

	}

    /**
     * 更新账户可用余额和冻结金额
     *
     * @return
     */
    public int updateAccount(Integer userId, String balance, String force) {
        Account account = new Account();
        // 可提现
        if (Validator.isNotNull(balance) && NumberUtils.isNumber(StringUtils.replace(balance, StringPool.COMMA, ""))) {
            account.setBalanceCash(new BigDecimal(StringUtils.replace(balance, StringPool.COMMA, "")));
        }
        // 不可提现
        if (Validator.isNotNull(force) && NumberUtils.isNumber(StringUtils.replace(force, StringPool.COMMA, ""))) {
            account.setBalanceFrost(new BigDecimal(StringUtils.replace(force, StringPool.COMMA, "")));
        }
        AccountExample example = new AccountExample();
        example.createCriteria().andUserIdEqualTo(userId);
        // 更新账户表
        return this.accountMapper.updateByExampleSelective(account, example);
    }

	/**
	 * 更新账户信息
	 * @param account
	 * @return
	 */
	public int updateAccountSelective(Account account){
		
		int result= this.accountMapper.updateByPrimaryKeySelective(account);
		return result;
	}
	
	/**
	 * insert account_list
	 * @param list
	 * @return
	 */
	public int insertAccountList(AccountList accountList){
		int ret = this.accountListMapper.insertSelective(accountList);
		return ret;
	}
	
	
	/**
	 * 根据userId获取账户信息
	 * @param userId
	 * @return
	 */
	public Account queryAccountInfoByUserId(Integer userId){
		AccountExample accountExample= new AccountExample();
		AccountExample.Criteria aCriteria= accountExample.createCriteria();
		aCriteria.andUserIdEqualTo(userId);
		
		List<Account> accounts = this.accountMapper.selectByExample(accountExample);
		if(accounts!=null && accounts.size()==1){
			return accounts.get(0);
		}
		
		return null;
	}
	
	
	
}


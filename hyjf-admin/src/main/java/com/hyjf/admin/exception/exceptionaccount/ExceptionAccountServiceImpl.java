package com.hyjf.admin.exception.exceptionaccount;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.ExceptionAccount;
import com.hyjf.mybatis.model.auto.ExceptionAccountExample;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class ExceptionAccountServiceImpl extends BaseServiceImpl implements ExceptionAccountService {
	/**
	 * 获取总记录数
	 */
	@Override
	public int countRecordTotal(String userName,Long customId,String mobile) {
		ExceptionAccountExample example = new ExceptionAccountExample();
		ExceptionAccountExample.Criteria cra = example.createCriteria();
		if (customId != null) {
			cra.andCustomIdEqualTo(customId);
		}
		if(StringUtils.isNotBlank(userName)){
			cra.andUsernameLike("%"+userName+"%");
		}
		if(StringUtils.isNotBlank(mobile)){
			cra.andMobileLike("%"+mobile+"%");
		}
		return exceptionAccountMapper.countByExample(example);
	}

	/**
	 * 查询符合条件的注册记录
	 */
	@Override
	public List<ExceptionAccount> searchRecord(String userName,Long customId,String mobile, int limitStart, int limitEnd) {
		ExceptionAccountExample example = new ExceptionAccountExample();
		ExceptionAccountExample.Criteria cra = example.createCriteria();
		if (customId != null) {
			cra.andCustomIdEqualTo(customId);
		}
		if(StringUtils.isNotBlank(userName)){
			cra.andUsernameLike("%"+userName+"%");
		}
		if(StringUtils.isNotBlank(mobile)){
			cra.andMobileLike("%"+mobile+"%");
		}
		example.setLimitStart(limitStart);
		example.setLimitEnd(limitEnd);
		return exceptionAccountMapper.selectByExample(example);
	}

	@Override
	@Transactional
	public void syncAccount(Integer id) {
		ExceptionAccount exceptionAccount = exceptionAccountMapper.selectByPrimaryKey(id);
		//获取账户实时余额信息
		AccountExample example = new AccountExample();
		AccountExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(exceptionAccount.getUserId());
		List<Account> accounts = accountMapper.selectByExample(example);
		Account account = null;
		if(!accounts.isEmpty()){
			account = accounts.get(0);
		}
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion("10");
		bean.setCmdId("QueryBalanceBg");
		bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
		bean.setUsrCustId(String.valueOf(exceptionAccount.getCustomId()));
		ChinapnrBean result = ChinapnrUtil.callApiBg(bean);
		if (result != null && StringUtils.equals("000", result.RespCode)){
			// 可用余额或冻结余额不一致的话更新account_exception表
    		String balance_huifu = result.getAvlBal() == null ? "0.00" : result.getAvlBal();
    		String frz_huifu = result.getFrzBal()== null ? "0.00" : result.getFrzBal();
    		balance_huifu = StringUtils.replace(balance_huifu,",",StringUtils.EMPTY);
    		frz_huifu = StringUtils.replace(frz_huifu,",",StringUtils.EMPTY);
    		BigDecimal balance = Validator.isNotNull(account.getBalance()) ? account.getBalance() : BigDecimal.ZERO;
			BigDecimal planBalance = Validator.isNotNull(account.getPlanBalance()) ? account.getPlanBalance() : BigDecimal.ZERO;
			BigDecimal frost = Validator.isNotNull(account.getFrost()) ? account.getFrost() : BigDecimal.ZERO;
			BigDecimal planFrost = Validator.isNotNull(account.getPlanFrost()) ? account.getPlanFrost() : BigDecimal.ZERO;
			BigDecimal planBalanceHF = new BigDecimal(balance_huifu);
			BigDecimal planFrostHF = new BigDecimal(frz_huifu);
			if((planBalanceHF.compareTo(balance.add(planBalance))!=0) || (planFrostHF.compareTo(frost.add(planFrost))!=0)){
    		   exceptionAccount.setBalancePlat(balance.add(planBalance));
    		   exceptionAccount.setFrostPlat(frost.add(planFrost));
    		   exceptionAccount.setBalanceHuifu(new BigDecimal(balance_huifu));
    		   exceptionAccount.setFrostHuifu(new BigDecimal(frz_huifu));
    		   exceptionAccountMapper.updateByPrimaryKey(exceptionAccount);
    		}else{
    			//可用余额或冻结余额一致的话删除异常数据
    			exceptionAccountMapper.deleteByPrimaryKey(id);
    		}
		}
	}

}

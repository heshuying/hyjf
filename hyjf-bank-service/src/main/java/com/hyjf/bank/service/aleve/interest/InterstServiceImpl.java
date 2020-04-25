package com.hyjf.bank.service.aleve.interest;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.mapper.customize.AleveCustomizeMapper;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.auto.AdminExample;
import com.hyjf.mybatis.model.auto.AleveLogExample;
import com.hyjf.mybatis.model.auto.BankMerchantAccount;
import com.hyjf.mybatis.model.auto.BankMerchantAccountExample;
import com.hyjf.mybatis.model.auto.BankMerchantAccountList;
import com.hyjf.mybatis.model.customize.AleveLogCustomize;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *自动同步利息业务类
 */
@Service
public class InterstServiceImpl extends BaseServiceImpl implements InterstService {
    Logger _log = LoggerFactory.getLogger(InterstServiceImpl.class);

	@Autowired
	private AleveCustomizeMapper aleveCustomizeMapper;
	
	/**
	 * 检查导入的ALEVE数据中是否含有利息相关记录
	 * @param map
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	
	@Override
	public List<AleveLogCustomize> selectAleveInterstList(List<String> tranStype) {
		return this.aleveCustomizeMapper.queryAleveLogListByTranstype(tranStype);	
	}

	/**
	 * 网站收支中插入对应入账记录huiyingdai_account_web_list
	 * @param aleveLog
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public boolean insertAccountWebList(AleveLogCustomize aleveLogCustomize) {
        //当前系统时间（数字类型、入库用）
        int nowTime = GetDate.getNowTime10();
        
		AccountWebList accountWebList = new AccountWebList();
		// 利息
		accountWebList.setAmount(aleveLogCustomize.getAmount()); 
		// 类型1收入,2支出
		accountWebList.setType(CustomConstants.TYPE_IN); 
		// 收支类型：利息
		accountWebList.setTrade(CustomConstants.TRADE_INTEREST); 
		// 利息
		accountWebList.setTradeType(CustomConstants.TRADE_INTEREST_NM); 
		// 订单号
		accountWebList.setOrdid(aleveLogCustomize.getOrderId());
		// 当前时间戳
		accountWebList.setCreateTime(nowTime);
		return this.accountWebListMapper.insertSelective(accountWebList) > 0 ? true : false;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param accountList
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public boolean insertAccountList(AleveLogCustomize aleveLogCustomize) {
		//利息金额
		BigDecimal total = aleveLogCustomize.getAmount();
        //当前系统时间（数字类型、入库用）
        int nowTime = GetDate.getNowTime10();

		//通过账号ID获取用户信息
        List<String> userIds = this.selectUserIdsByAccount(aleveLogCustomize.getCardnbr());
        if (userIds.isEmpty()) {
        	_log.error("【利息自动同步异常】：获取用户信息失败、电子账号：" + aleveLogCustomize.getCardnbr());
        	return false;
        } else if (userIds.size() > 1) {
        	_log.error("【利息自动同步异常】：获取用户信息不唯一、电子账号：" + aleveLogCustomize.getCardnbr());
			return false;
		}
        
        String userIdStr = userIds.get(0);
        if (StringUtils.isBlank(userIdStr)) {
            _log.error("【利息自动同步异常】：获取用户信息异常、电子账号：" + aleveLogCustomize.getCardnbr());
            return false;
        }
        Integer userId = Integer.parseInt(userIdStr);
        
		// 获取用户账户信息
		Account account = this.getAccount(userId);
		// 写入收支明细
		AccountList accountList = new AccountList();
		// 账户信息
		// 订单号：空
		//accountList.setNid(GetOrderIdUtils.getOrderId2(user.getUserId()));
		accountList.setUserId(userId);
		accountList.setAmount(total);
		//list表->1收入2支出
		accountList.setType(1);
		//收入
		accountList.setTrade("account_interest");
		//操作识别码 balance余额操作 frost冻结操作 await待收操作
		accountList.setTradeCode("balance");
		accountList.setTotal(account.getTotal());
		accountList.setBalance(account.getBalance());
		accountList.setFrost(account.getFrost());
		accountList.setAwait(account.getAwait());
		accountList.setRepay(account.getRepay());
		accountList.setBankTotal(account.getBankTotal().add(total)); // 银行总资产
		accountList.setBankBalance(account.getBankBalance().add(total)); // 银行可用余额
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
		accountList.setSeqNo(aleveLogCustomize.getSeqno().toString());
		accountList.setTxDate(nowTime);
		accountList.setTxTime(nowTime);
		//accountList.setBankSeqNo(form.getBankSeqNo());
		accountList.setAccountId(aleveLogCustomize.getCardnbr());
		//accountList.setRemark("原交易订单号 " + form.getBankSeqNo());
		accountList.setCreateTime(nowTime);
		accountList.setBaseUpdate(nowTime);
		//操作员
		//accountList.setOperator(createUserId);
		//操作IP
		//accountList.setIp(createIp);
		accountList.setIsUpdate(0);
		accountList.setBaseUpdate(0);
		accountList.setInterest(null);
		accountList.setIsBank(1);
		accountList.setWeb(0);
		accountList.setCheckStatus(1);// 对账状态0：未对账 1：已对账
		accountList.setTradeStatus(1);// 0失败1成功2失败
		//插入资产明细
		boolean isAccountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
		if (!isAccountListFlag) {
			_log.error("【利息自动同步异常】：资产明细更新失败、电子账号：" + aleveLogCustomize.getCardnbr());
			return false;
		}
		//用户相应余额增加
		Account newAccount = new Account();
		
		newAccount.setUserId(userId);// 用户Id
		newAccount.setBankTotal(total); // 累加到账户总资产
		newAccount.setBankBalance(total); // 累加可用余额
		newAccount.setBankBalanceCash(total);// 江西银行可用余额
		//余额增加
		boolean isAccountUpdateFlag = this.adminAccountCustomizeMapper.updateManualReverseSuccess(newAccount) > 0 ? true : false;
		if (!isAccountUpdateFlag) {
			_log.error("【利息自动同步异常】：更新用户余额失败、电子账号：" + aleveLogCustomize.getCardnbr());
			return false;
		}

		//同步冲正后更新处理flg
		if(!this.updateAleveLog(aleveLogCustomize)) {
			_log.error("【利息自动同步异常】：aleve数据处理完成字段更新失败:" + aleveLogCustomize.getCardnbr() + "----Seqno:" + aleveLogCustomize.getSeqno() + "----CreateTime:" + aleveLogCustomize.getCreateTime());
			return false;
		}
		return true;
	}

	/**
	 * 更新公司账户余额hyjf_bank_merchant_account hyjf_bank_merchant_account_list
	 * @param aleveLog
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	@Override
	public boolean updateMerchantAccount(AleveLogCustomize aleveLogCustomize) {
		
		BankMerchantAccount bankMerchantAccount = new BankMerchantAccount();
		//获取公司账户
		bankMerchantAccount.setAccountCode(aleveLogCustomize.getCardnbr());
		// 账户余额
		bankMerchantAccount.setAccountBalance(aleveLogCustomize.getAmount());
		// 可用余额
		bankMerchantAccount.setAvailableBalance(aleveLogCustomize.getAmount());
		
		boolean isAccountUpdateFlag = this.bankMerchantAccountListCustomizeMapper.updateMerchantByAccountCode(bankMerchantAccount)> 0 ? true : false;
		if (!isAccountUpdateFlag) {
			_log.error("【利息自动同步异常】：更新账户余额失败、公司账号:" + aleveLogCustomize.getCardnbr());
			return false;
		}
		
		// 更新账户信息
		//boolean isAccountUpdateFlag = this.bankMerchantAccountMapper.updateByExampleSelective(bankMerchantAccount, bankMerchantAccountExample) > 0 ? true : false;
		BankMerchantAccountExample bankMerchantAccountExample = new BankMerchantAccountExample();
		BankMerchantAccountExample.Criteria bankMerchantAccountCriteria = bankMerchantAccountExample.createCriteria();
		bankMerchantAccountCriteria.andAccountCodeEqualTo(aleveLogCustomize.getCardnbr());
		BankMerchantAccount bankMerchantAccountTotal = this.bankMerchantAccountMapper.selectByExample(bankMerchantAccountExample).get(0);
		
		BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
		//表中非null字段
		bankMerchantAccountList.setAccountId(aleveLogCustomize.getCardnbr());
		//创建人为admin
		bankMerchantAccountList.setCreateUserName("admin");
		bankMerchantAccountList.setCreateUserId(aleveLogCustomize.getUserId());
		
		//生成订单号
		bankMerchantAccountList.setOrderId(aleveLogCustomize.getOrderId());
		//操作金额
		bankMerchantAccountList.setAmount(aleveLogCustomize.getAmount());
		// bankMerchantAccountList.setBankAccountType(bankAccountType);
		//商户子账户电子账号
		bankMerchantAccountList.setBankAccountCode(aleveLogCustomize.getCardnbr());
		//银行账户可用金额
		bankMerchantAccountList.setBankAccountBalance(bankMerchantAccountTotal.getAccountBalance());
		//银行账户冻结金额
		bankMerchantAccountList.setBankAccountFrost(bankMerchantAccountTotal.getFrost());
		//交易类型
		bankMerchantAccountList.setTransType(CustomConstants.BANK_MER_TRANS_TYPE_AUTOMATIC);
		//收支类型
		bankMerchantAccountList.setType(CustomConstants.BANK_MER_TYPE_INCOME);
		//交易状态
		bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS);
		//流水号
		bankMerchantAccountList.setSeqNo(aleveLogCustomize.getSeqno().toString());
		//交易日期
		bankMerchantAccountList.setTxDate(Integer.parseInt(aleveLogCustomize.getInpdate()));
		//交易时间
		bankMerchantAccountList.setTxTime(aleveLogCustomize.getInptime());
		bankMerchantAccountList.setCreateTime(new Date());
		boolean isBankMerchantAccountList = bankMerchantAccountListMapper.insertSelective(bankMerchantAccountList) > 0 ? true : false;
		if (!isBankMerchantAccountList) {
			_log.error("【利息自动同步异常】：更新网站收支失败、公司账号:" + aleveLogCustomize.getCardnbr());
			return false;
		}
		
		//同步冲正后更新处理flg
		if(!this.updateAleveLog(aleveLogCustomize)) {
			_log.error("【利息自动同步异常】：aleve数据处理完成字段更新失败:" + aleveLogCustomize.getCardnbr() + "----Seqno:" + aleveLogCustomize.getSeqno() + "----CreateTime:" + aleveLogCustomize.getCreateTime());
			return false;
		}
		return true;
	}
	
	@Override
	public Admin getAdminId() {
		//获取admin的id信息
		AdminExample example = new AdminExample();
    	example.createCriteria().andUsernameEqualTo("admin");
    	List<Admin> admin = adminMapper.selectByExample(example);
    	
    	if (null==admin||0 == admin.size()) {
    		return null;
    	}
    	return admin.get(0);
	}
}

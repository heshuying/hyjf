package com.hyjf.api.surong.borrow.repay.info;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * 融东风-标的状态同步
 * 
 * @author Administrator
 * 
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class BorrowRepayInfoServiceImpl extends BaseServiceImpl implements BorrowRepayInfoService {

	Logger _log = LoggerFactory.getLogger(BorrowRepayInfoServiceImpl.class);

	@Override
	public Borrow getBorrow(String borrowNid) {
		BorrowExample example =  new BorrowExample();
		BorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<Borrow> borrowList = this.borrowMapper.selectByExample(example);
		if(null != borrowList && borrowList.size()>0){
			return borrowList.get(0);
		}
		return null;
	}
	
	
	
	/***********************借款人还款如下***************************/
	
	

	/**
	 * 根据用户名和手机号取得用户信息
	 */
	@Override
	public Users getUsersByExample(String username,String mobile) {
		// 
		UsersExample example = new UsersExample();
		UsersExample.Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		criteria.andMobileEqualTo(mobile);
		List<Users> userList = this.usersMapper.selectByExample(example);
		if(null != userList&&userList.size()>0){
			return userList.get(0);
		}
		return null;
	}
	
	/**
	 * 查询客户在江西银行的余额
	 *
	 * @param usrCustId
	 * @return
	 */
	@Override
	public BigDecimal getUserBalance(Integer userId, String accountId) {
		// 账户可用余额
        BigDecimal balance = BigDecimal.ZERO;
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_QUERY);// 交易代码
        bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
        bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
        bean.setAccountId(accountId);// 电子账号
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.valueOf(userId)));// 订单号
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogClient(0);// 平台
        try {
            BankCallBean resultBean = BankCallUtils.callApiBg(bean);
            if (resultBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
                balance = new BigDecimal(resultBean.getAvailBal().replace(",", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
	}

	/**
	 * 查询标的指定期数的还款状态
	 */
	@Override
	public BorrowApicron getBorrowApicron(BorrowRepayInfoParamBean paramBean) {
		// 
		BorrowApicronExample example = new BorrowApicronExample();
		BorrowApicronExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(paramBean.getBorrowNid());
		criteria.andPeriodNowEqualTo(paramBean.getRepayPeriod());
		criteria.andApiTypeEqualTo(1);
		List<BorrowApicron> apiList =  this.borrowApicronMapper.selectByExample(example);
		if(null != apiList && apiList.size() > 0){
			return apiList.get(0);
		}
		return null;
	}

	/**
	 * 根据标的编号和标的状态取得标的最后还款时间
	 */
	@Override
	public BorrowRepay getBorrowRepayTimeByStatus(String borrowNid, Integer status) {
		BorrowRepayExample example = new BorrowRepayExample();
		example.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayStatusEqualTo(status);
		List<BorrowRepay> repayList = this.borrowRepayMapper.selectByExample(example);
		if(null != repayList && repayList.size()>0){
			return repayList.get(0);
		}
		return null;
	}
	
	/**
	 * 取得用户在江西银行客户号
	 */
	@Override
	public BankOpenAccount getBankOpenAccount(Integer userId){
		BankOpenAccountExample example = new BankOpenAccountExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<BankOpenAccount> accountList = this.bankOpenAccountMapper.selectByExample(example);
		if(null != accountList && accountList.size() > 0){
			return accountList.get(0);
		}
		return null;
	}



	/**
	 * 取得用户账户信息
	 */
	@Override
	public Account getUserAccountByExample(Integer userId) {
		AccountExample example = new AccountExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<Account> accountList = this.accountMapper.selectByExample(example);
		if(null != accountList && accountList.size() > 0){
			return accountList.get(0);
		}
		return null;
	}
	
}

package com.hyjf.app.bank.user.synbalance;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.AccountRechargeExample;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class AppSynBalanceServiceImpl extends BaseServiceImpl implements AppSynBalanceService {
	// 充值状态:成功
	private static final int RECHARGE_STATUS_SUCCESS = 2;

	/**
	 * 查询用户银行交易明细
	 * 
	 * @param accountId
	 * @param startDate
	 * @param endDate
	 * @param type
	 * @param transType
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @author Michael
	 */

	@Override
	public BankCallBean queryAccountDetails(Integer userId, String accountId, String startDate, String endDate,
			String type, String transType, String pageNum, String pageSize) {
		// 参数不正确
		if (StringUtils.isEmpty(accountId) || StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)
				|| StringUtils.isEmpty(type)) {
			return null;
		}
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_DETAILS_QUERY);// 消息类型
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_PC);
		bean.setAccountId(accountId);// 电子账号
		bean.setStartDate(startDate);// 起始日期
		bean.setEndDate(endDate);// 结束日期
		bean.setType(type);// 交易种类 0-所有交易 1-转入交易 2-转出交易 9-指定交易类型
		if ("9".equals(type)) {
			bean.setTranType(transType);// 交易类型
		}
		if (StringUtils.isNotEmpty(pageNum)) {
			bean.setPageNum(pageNum);
		} else {
			bean.setPageNum("1");
		}
		if (StringUtils.isNotEmpty(pageSize)) {
			bean.setPageSize(pageSize);
		} else {
			bean.setPageSize("10");
		}
		// 操作者ID
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
		bean.setLogRemark("资金交易明细查询");
		// 调用接口
		return BankCallUtils.callApiBg(bean);
	}

	/**
	 * 处理线下充值
	 * 
	 * @return
	 * @author Michael
	 */
	@Override
	public boolean insertAccountDetails(Account account, BigDecimal transAmount, String txData, String txTime,
			String traceNo, String username, String ip) throws Exception{

		// 添加重复校验
		try {
			// 校验交易明细是否已经插入当前笔充值
			AccountListExample accountListExample = new AccountListExample();
			accountListExample.createCriteria().andTxDateEqualTo(Integer.parseInt(txData))
					.andTxTimeEqualTo(Integer.parseInt(txTime)).andSeqNoEqualTo(traceNo)
					.andTypeEqualTo(CustomConstants.TYPE_IN).andBankSeqNoEqualTo(txData + txTime + traceNo);
			List<AccountList> accountLists = accountListMapper.selectByExample(accountListExample);
			if (accountLists != null && accountLists.size() != 0) {
				return false;
			}
			// 校验充值信息是否已经插入当前笔充值
			AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
			accountRechargeExample.createCriteria().andTxDateEqualTo(Integer.parseInt(txData))
					.andTxTimeEqualTo(Integer.parseInt(txTime)).andSeqNoEqualTo(Integer.parseInt(traceNo))
					.andBankSeqNoEqualTo(txData + txTime + traceNo);
			List<AccountRecharge> accountRecharges = accountRechargeMapper.selectByExample(accountRechargeExample);
			if (accountRecharges != null && accountRecharges.size() != 0) {
				return false;
			}
            Account updateAccount=new Account();
            updateAccount.setUserId(account.getUserId());
            updateAccount.setBankTotal(transAmount); // 累加到账户总资产
            updateAccount.setBankBalance(transAmount);// 累加可用余额(江西账户)
			// 更新账户信息
			boolean isAccountUpdateFlag = adminAccountCustomizeMapper.updateOfSynBalance(updateAccount) > 0 ? true : false;
			if (!isAccountUpdateFlag) {
				throw new RuntimeException("同步线下充值,更新用户账户信息失败~~~~,用户ID:"+account.getUserId()); 
			}
			// 重新获取用户账户信息
			account = this.getAccount(account.getUserId());
			// 生成交易明细
			AccountList accountList = new AccountList();
			accountList.setNid(GetOrderIdUtils.getOrderId2(account.getUserId())); // 订单号
			accountList.setUserId(account.getUserId());
			accountList.setAmount(transAmount);
			accountList.setType(CustomConstants.TYPE_IN);// 收入
			accountList.setTrade("recharge_offline");
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
			accountList.setRemark("线下充值");
			accountList.setCreateTime(GetDate.getNowTime10());
			accountList.setBaseUpdate(GetDate.getNowTime10());
			accountList.setOperator(account.getUserId() + "");
			accountList.setIp(ip);
			accountList.setIsUpdate(0);
			accountList.setBaseUpdate(0);
			accountList.setInterest(null);
			accountList.setWeb(0);
			accountList.setTxDate(Integer.parseInt(txData));
			accountList.setTxTime(Integer.parseInt(txTime));
			accountList.setSeqNo(traceNo);
			accountList.setBankSeqNo(txData + txTime + traceNo);
			accountList.setIsBank(1);//资金托管平台 0:汇付,1:江西银行
			accountList.setTradeStatus(1);
			this.accountListMapper.insertSelective(accountList);// 插入交易明细

			// 插入充值信息
			BankCardExample example = new BankCardExample();
			example.createCriteria().andUserIdEqualTo(account.getUserId());
			List<BankCard> bankCardList = bankCardMapper.selectByExample(example);
			BankCard bankCard = new BankCard();
			if (bankCardList != null && bankCardList.size() > 0) {
				bankCard = bankCardList.get(0);
			}
			Integer nowTime = GetDate.getNowTime10();
			AccountRecharge record = new AccountRecharge();
			record.setNid(GetOrderIdUtils.getOrderId2(account.getUserId())); // 订单号
			record.setUserId(account.getUserId()); // 用户ID
			record.setUsername(username);// 用户 名
			record.setStatus(RECHARGE_STATUS_SUCCESS); // 状态 0：失败；1：成功 2:充值中
			record.setMoney(transAmount); // 金额
			record.setCardid(bankCard.getCardNo());// 银行卡号
			record.setFeeFrom(null);// 手续费扣除方式
			record.setFee(BigDecimal.ZERO); // 费用
			record.setDianfuFee(BigDecimal.ZERO);// 垫付费用
			record.setBalance(transAmount); // 实际到账余额
			record.setPayment(""); // 所属银行
			record.setGateType("B2C"); // 网关类型：QP快捷充值 B2C个人网银充值 B2B企业网银充值
			record.setType(0); // 类型.1网上充值.0线下充值
			record.setRemark("线下充值");// 备注
			record.setCreateTime(nowTime);
			record.setOperator(account.getUserId() + "");
			record.setAddtime(String.valueOf(nowTime));
			record.setAddip(ip);
			record.setClient(4); // 0pc 1WX 2AND 3IOS 4other
			record.setIsBank(1);// 资金托管平台 0:汇付,1:江西银行
			record.setTxDate(Integer.parseInt(txData));
			record.setTxTime(Integer.parseInt(txTime));
			record.setSeqNo(Integer.parseInt(traceNo));
			record.setBankSeqNo(txData + txTime + traceNo);
			// 插入用户充值记录表
			this.accountRechargeMapper.insertSelective(record);

		} catch (Exception e) {
			e.printStackTrace();
			new RuntimeException("同步线下充值,更新用户账户信息失败~~~~,用户ID:"+account.getUserId());
		}
		return true;
	}

}

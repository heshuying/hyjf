package com.hyjf.batch.bank.borrow.repayrepair;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditRepayExample;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;

@Service
public class CreditRepayRepairServiceImpl extends BaseServiceImpl implements CreditRepayRepairService {

	@Override
	public List<CreditRepay> selectCreditRepayList() {
		CreditRepayExample example = new CreditRepayExample();
		CreditRepayExample.Criteria cra = example.createCriteria();
		cra.andBidNidEqualTo("HXD160804702");
		cra.andStatusEqualTo(0);
		return this.creditRepayMapper.selectByExample(example);
	}

	@Override
	public void creditRepayRepair(CreditRepay creditRepay) throws Exception {
		// 当前时间
		Integer nowTime = 1502461341;
		// 出借人用户ID
		Integer userId = creditRepay.getUserId();
		// 债转承接订单号
		String assignNid = creditRepay.getAssignNid();
		// 待还本金
		BigDecimal repayCapital = creditRepay.getAssignCapital();
		// 待还利息
		BigDecimal repayInterest = creditRepay.getAssignInterest();
		// 待还总额
		BigDecimal repayAccount = creditRepay.getAssignAccount();
		// 还款订单号
		String repayOrderId = creditRepay.getCreditRepayOrderId();
		// 更新huiyingdai_credit_repay
		creditRepay.setStatus(1);// 还款状态
		creditRepay.setAssignRepayAccount(repayAccount);// 已还总额
		creditRepay.setAssignRepayCapital(repayCapital);// 已还本金
		creditRepay.setAssignRepayInterest(repayInterest);// 已还利息
		creditRepay.setAssignRepayYesTime(nowTime);// 实际还款时间
		boolean isCreditRepayFlag = this.creditRepayMapper.updateByPrimaryKey(creditRepay) > 0 ? true : false;
		if (!isCreditRepayFlag) {
			throw new Exception("更新huiyingdai_credit_Repay表失败!!!!" + creditRepay.getUserId());
		}
		// 更新credit_tender表
		CreditTender creditTender = this.getCreditTender(assignNid);
		if (creditTender == null) {
			throw new Exception("根据承接订单号获取债转出借记录失败~~~,承接订单号:" + assignNid);
		}
		creditTender.setStatus(1);
		creditTender.setAssignRepayAccount(repayAccount);
		creditTender.setAssignRepayCapital(repayCapital);
		creditTender.setAssignRepayInterest(repayInterest);
		creditTender.setAssignRepayYesTime(nowTime);// 实际还款时间
		// 更新交易明细
		// 更新账户信息(出借人)
		Account assignUserAccount = new Account();
		assignUserAccount.setUserId(userId);
		assignUserAccount.setBankTotal(repayInterest);// 出借人资金总额
		assignUserAccount.setBankBalance(repayAccount);// 出借人可用余额
		assignUserAccount.setBankAwait(repayCapital);// 出借人待收金额
		assignUserAccount.setBankAwaitCapital(repayCapital);
		assignUserAccount.setBankAwaitInterest(repayInterest);
		assignUserAccount.setBankInterestSum(repayInterest);
		assignUserAccount.setBankBalanceCash(repayAccount);
		boolean investAccountFlag = this.adminAccountCustomizeMapper.updateOfRepayTender(assignUserAccount) > 0 ? true : false;
		if (!investAccountFlag) {
			throw new Exception("承接人资金记录(huiyingdai_account)更新失败！" + "[出借订单号：" + assignNid + "]");
		}
		// 取得承接人账户信息
		assignUserAccount = this.getAccountByUserId(creditRepay.getUserId());
		if (Validator.isNull(assignUserAccount)) {
			throw new Exception("承接人账户信息不存在。[出借人ID：" + creditTender.getUserId() + "]，" + "[出借订单号：" + assignNid + "]");
		}
		// 出借用户开户信息
		BankOpenAccount assignBankAccount = getBankOpenAccount(userId);
		// 写入收支明细
		AccountList accountList = new AccountList();
		accountList.setNid(repayOrderId); // 还款订单号
		accountList.setUserId(userId); // 出借人
		accountList.setAmount(repayAccount); // 出借总收入
		/** 银行相关 */
		accountList.setAccountId(assignBankAccount.getAccount());
		accountList.setBankAwait(assignUserAccount.getBankAwait());
		accountList.setBankAwaitCapital(assignUserAccount.getBankAwaitCapital());
		accountList.setBankAwaitInterest(assignUserAccount.getBankAwaitInterest());
		accountList.setBankBalance(assignUserAccount.getBankBalance());
		accountList.setBankFrost(assignUserAccount.getBankFrost());
		accountList.setBankInterestSum(assignUserAccount.getBankInterestSum());
		accountList.setBankInvestSum(assignUserAccount.getBankInvestSum());
		accountList.setBankTotal(assignUserAccount.getBankTotal());
		accountList.setBankWaitCapital(assignUserAccount.getBankWaitCapital());
		accountList.setBankWaitInterest(assignUserAccount.getBankWaitInterest());
		accountList.setBankWaitRepay(assignUserAccount.getBankWaitRepay());
		accountList.setCheckStatus(1);
		accountList.setTradeStatus(1);// 交易状态 0:失败 1:成功
		accountList.setIsBank(1);
		accountList.setTxDate(20170811);
		accountList.setTxTime(134115);
		accountList.setSeqNo("132598");
		accountList.setBankSeqNo("20170811134115132598");
		/** 非银行相关 */
		accountList.setType(1); // 1收入
		accountList.setTrade("credit_tender_recover_yes"); // 出借成功
		accountList.setTradeCode("balance"); // 余额操作
		accountList.setTotal(assignUserAccount.getTotal()); // 出借人资金总额
		accountList.setBalance(assignUserAccount.getBalance()); // 出借人可用金额
		accountList.setPlanFrost(assignUserAccount.getPlanFrost());// 汇添金冻结金额
		accountList.setPlanBalance(assignUserAccount.getPlanBalance());// 汇添金可用金额
		accountList.setFrost(assignUserAccount.getFrost()); // 出借人冻结金额
		accountList.setAwait(assignUserAccount.getAwait()); // 出借人待收金额
		accountList.setCreateTime(nowTime); // 创建时间
		accountList.setBaseUpdate(nowTime); // 更新时间
		accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY); // 操作者
		accountList.setRemark("HXD160804702");
		accountList.setIp(""); // 操作IP
		accountList.setIsUpdate(0);
		accountList.setBaseUpdate(0);
		accountList.setInterest(BigDecimal.ZERO); // 利息
		accountList.setWeb(0); // PC
		boolean assignAccountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
		if (!assignAccountListFlag) {
			throw new Exception("收支明细(huiyingdai_account_list)写入失败！" + "[承接订单号：" + assignNid + "]");
		}
	}

	private CreditTender getCreditTender(String assignNid) {
		CreditTenderExample example = new CreditTenderExample();
		CreditTenderExample.Criteria cra = example.createCriteria();
		cra.andAssignNidEqualTo(assignNid);
		List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(example);
		if (creditTenderList != null && creditTenderList.size() > 0) {
			return creditTenderList.get(0);
		}
		return null;
	}

	private Account getAccountByUserId(Integer userId) {
		AccountExample accountExample = new AccountExample();
		accountExample.createCriteria().andUserIdEqualTo(userId);
		List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}
}

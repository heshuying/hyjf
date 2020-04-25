package com.hyjf.batch.borrow.increaseinterestrepay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.IncreaseInterestInvest;
import com.hyjf.mybatis.model.auto.IncreaseInterestInvestExample;
import com.hyjf.mybatis.model.auto.IncreaseInterestLoan;
import com.hyjf.mybatis.model.auto.IncreaseInterestLoanDetail;
import com.hyjf.mybatis.model.auto.IncreaseInterestLoanDetailExample;
import com.hyjf.mybatis.model.auto.IncreaseInterestLoanExample;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepay;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepayDetail;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepayDetailExample;
import com.hyjf.mybatis.model.auto.IncreaseInterestRepayExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * 融通宝加息还款Service实现类
 * 
 * @ClassName IncreaseInterestRepayServiceImpl
 * @author liuyang
 * @date 2017年1月3日 上午9:17:54
 */
@Service
public class IncreaseInterestRepayServiceImpl extends BaseServiceImpl implements IncreaseInterestRepayService {

	/** 类名 */
	private static final String THIS_CLASS = IncreaseInterestRepayServiceImpl.class.getName();

	/** 用户ID */
	private static final String VAL_USERID = "userId";

	/** 性别 */
	private static final String VAL_SEX = "val_sex";

	/** 用户名 */
	private static final String VAL_NAME = "val_name";

	/** 放款金额 */
	private static final String VAL_AMOUNT = "val_amount";

	/** 预期收益 */
	private static final String VAL_PROFIT = "val_profit";

	/** 项目标题 */
	private static final String VAL_TITLE = "val_title";

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	/**
	 * 检索未执行的还款任务
	 * 
	 * @Title selectBorrowApicronList
	 * @param statusWait
	 * @param status
	 * @return
	 */
	@Override
	public List<BorrowApicron> selectBorrowApicronList(Integer statusWait, int status) {
		BorrowApicronExample example = new BorrowApicronExample();
		BorrowApicronExample.Criteria cra = example.createCriteria();
		cra.andExtraYieldRepayStatusEqualTo(statusWait);
		cra.andApiTypeEqualTo(status);
		// 标的还款已完成
		cra.andStatusEqualTo(6);
		example.setOrderByClause(" id asc ");
		List<BorrowApicron> list = this.borrowApicronMapper.selectByExample(example);
		return list;
	}

	/**
	 * 更新还款任务
	 * 
	 * @Title updateBorrowApicron
	 * @param id
	 * @param statusError
	 * @param errorMsg
	 */
	@Override
	public int updateBorrowApicron(Integer id, Integer statusError, String errorMsg) {
		BorrowApicron record = new BorrowApicron();
		record.setId(id);
		record.setExtraYieldRepayStatus(statusError);
		if (Validator.isNotNull(errorMsg) || statusError == 1) {
			record.setData(errorMsg);
		}
		if (record.getWebStatus() == null) {
			record.setWebStatus(0);
		}
		record.setUpdateTime(GetDate.getMyTimeInMillis());
		return this.borrowApicronMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 根据借款编号检索借款详情
	 * 
	 * @Title selectBorrowInfo
	 * @param borrowNid
	 * @return
	 */
	@Override
	public BorrowWithBLOBs selectBorrowInfo(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<BorrowWithBLOBs> list = this.borrowMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 更新还款任务状态
	 * 
	 * @Title updateBorrowApicron
	 * @param id
	 * @param statusWait
	 */
	@Override
	public int updateBorrowApicron(Integer id, Integer statusWait) {
		return updateBorrowApicron(id, statusWait, null);
	}

	/**
	 * 根据借款编号检索还款信息
	 * 
	 * @Title selectIncreaseInterestLoanList
	 * @param borrowNid
	 * @return
	 */
	@Override
	public List<IncreaseInterestLoan> selectIncreaseInterestLoanList(String borrowNid) {
		IncreaseInterestLoanExample example = new IncreaseInterestLoanExample();
		IncreaseInterestLoanExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		return this.increaseInterestLoanMapper.selectByExample(example);
	}

	/**
	 * 根据借款人userId检索借款人账户信息
	 * 
	 * @Title selectAccountByUserId
	 * @param borrowUserId
	 * @return
	 */
	@Override
	public Account selectAccountByUserId(Integer borrowUserId) {
		AccountExample example = new AccountExample();
		AccountExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(borrowUserId);
		List<Account> listAccount = this.accountMapper.selectByExample(example);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	/**
	 * 根据借款编号,还款期数,还款方式取得还款金额
	 * 
	 * @Title selectBorrowAccountWithPeriod
	 * @param borrowNid
	 * @param borrowStyle
	 * @param periodNow
	 * @return
	 */
	@Override
	public BigDecimal selectBorrowAccountWithPeriod(String borrowNid, String borrowStyle, Integer periodNow) {

		BigDecimal account = BigDecimal.ZERO;
		// 是否分期(true:分期, false:单期)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		if (isMonth) {
			IncreaseInterestLoanDetailExample example = new IncreaseInterestLoanDetailExample();
			IncreaseInterestLoanDetailExample.Criteria cra = example.createCriteria();
			cra.andBorrowNidEqualTo(borrowNid);
			cra.andRepayStatusEqualTo(0);
			cra.andRepayPeriodEqualTo(periodNow);
			List<IncreaseInterestLoanDetail> list = this.increaseInterestLoanDetailMapper.selectByExample(example);
			if (list != null && list.size() > 0) {
				for (IncreaseInterestLoanDetail increaseInterestLoanDetail : list) {
					account = account.add(increaseInterestLoanDetail.getLoanInterest());
				}
			}
		} else {
			IncreaseInterestLoanExample example2 = new IncreaseInterestLoanExample();
			IncreaseInterestLoanExample.Criteria cra2 = example2.createCriteria();
			cra2.andBorrowNidEqualTo(borrowNid);
			cra2.andRepayStatusEqualTo(0);
			List<IncreaseInterestLoan> list2 = this.increaseInterestLoanMapper.selectByExample(example2);
			if (list2 != null && list2.size() > 0) {
				for (IncreaseInterestLoan increaseInterestLoan : list2) {
					account = account.add(increaseInterestLoan.getLoanInterest());
				}
			}
		}
		return account;
	}

	/**
	 * 检索公司子账户可用余额
	 * 
	 * @Title selectCompanyAccount
	 * @return
	 */
	@Override
	public BigDecimal selectCompanyAccount() {
		// 账户可用余额
		BigDecimal balance = BigDecimal.ZERO;
		// 查询商户子账户余额
		String merrpAccount = PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT);
		BankOpenAccountExample example = new BankOpenAccountExample();
		BankOpenAccountExample.Criteria cra = example.createCriteria();
		cra.andAccountEqualTo(merrpAccount);
		List<BankOpenAccount> list = this.bankOpenAccountMapper.selectByExample(example);
		BankOpenAccount comAccount = null;
		if (list != null && list.size() > 0) {
			comAccount = list.get(0);
		}
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_QUERY);// 交易代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(merrpAccount);// 电子账号
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(comAccount == null ? 0 : comAccount.getUserId()));// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(comAccount == null ? "0" : String.valueOf(comAccount.getUserId()));
		bean.setLogClient(0);// 平台
		try {
			BankCallBean resultBean = BankCallUtils.callApiBg(bean);
			if (resultBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
				// 账户余额
				balance = new BigDecimal(resultBean.getAvailBal().replace(",", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balance;
	}

	/**
	 * 自动还款
	 * 
	 * @Title updateBorrowRepay
	 * @param apicron
	 * @param increaseInterestLoan
	 * @param borrowUserCust
	 * @return
	 */
	@Override
	public List<Map<String, String>> updateBorrowRepay(BorrowApicron apicron, IncreaseInterestLoan increaseInterestLoan, BankOpenAccount borrowUserCust) {
		String methodName = "updateBorrowRepay";
		System.out.println("-----------融通宝加息还款开始---" + apicron.getBorrowNid() + "---------");
		List<Map<String, String>> retMsgList = new ArrayList<Map<String, String>>();
		Map<String, String> msg = new HashMap<String, String>();
		retMsgList.add(msg);
		/** 基本变量 */
		// 还款订单号
		String repayOrderId = null;
		// 还款订单日期
		String repayOrderDate = null;
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 借款编号
		String borrowNid = apicron.getBorrowNid();
		// 当前期数
		Integer periodNow = apicron.getPeriodNow();
		/** 标的基本数据 */
		// 取得借款详情
		BorrowWithBLOBs borrow = selectBorrowInfo(borrowNid);
		// 取得还款详情
		IncreaseInterestRepay increaseInterestRepay = selectIncreaseInterestRepay(borrowNid);
		// 还款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 剩余还款期数
		Integer periodNext = borrowPeriod - periodNow;
		// 还款利息
		BigDecimal repayInterest = BigDecimal.ZERO;
		// 还款时间
		String repayTime = null;
		// 出借订单号
		String investOrderId = increaseInterestLoan.getInvestOrderId();
		// 出借人用户ID
		Integer investUserId = increaseInterestLoan.getUserId();
		// 取得融通宝加息出借信息
		IncreaseInterestInvest increaseInterestInvest = selectIncreaseInterestInvestInfo(investOrderId);
		// 出借人在汇付的账户信息
		BankOpenAccount investUserCust = this.getBankOpenAccount(investUserId);
		// 交易流水号
		Integer seqNo = 0;
		// 交易日期
		Integer txDate = 0;
		// 交易时间
		Integer txTime = 0;
		// 融通宝放款详情
		IncreaseInterestLoanDetail increaseInterestLoanDetail = null;
		if (investUserCust == null || StringUtils.isEmpty(investUserCust.getAccount())) {
			throw new RuntimeException("出借人未开户。[用户ID:" + investUserId + "]，" + "[融通宝加息出借订单号:" + investOrderId + "]");
		}
		// 出借人客户号
		String investUserCustId = investUserCust.getAccount();

		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);

		// [principal: 等额本金, month:等额本息, month:等额本息, endmonth:先息后本]
		if (isMonth) {
			// 取得分期还款计划表
			increaseInterestLoanDetail = selectIncreaseInterestLoanDetail(borrowNid, periodNow, investUserId, increaseInterestLoan.getInvestId());
			if (increaseInterestLoanDetail != null) {
				// 保存还款订单号
				if (StringUtils.isBlank(increaseInterestLoanDetail.getRepayOrderId())) {
					// 还款订单号
					repayOrderId = GetOrderIdUtils.getOrderId2(increaseInterestLoan.getUserId());
					// 还款订单日期
					repayOrderDate = GetOrderIdUtils.getOrderDate();
					// 设置还款订单号
					increaseInterestLoanDetail.setRepayOrderId(repayOrderId);
					// 设置还款时间
					increaseInterestLoanDetail.setRepayOrderDate(repayOrderDate);
					// 更新还款信息
					boolean increaseInterestLoanDetailFlag = this.updateIncreaseInterestLoanDetail(increaseInterestLoanDetail) > 0 ? true : false;
					if (!increaseInterestLoanDetailFlag) {
						throw new RuntimeException("添加还款订单号，更新hyjf_increase_interest_loan_detail表失败" + "，[出借订单号:" + investOrderId + "]");
					}
					// 更新融通宝加息项目放款总表
					increaseInterestLoan.setRepayOrderId(repayOrderId);
					increaseInterestLoan.setRepayOrderDate(repayOrderDate);
					// 更新还款信息
					boolean increaseInterestLoanFlag = this.updateIncreaseInterestLoan(increaseInterestLoan) > 0 ? true : false;
					if (!increaseInterestLoanFlag) {
						throw new RuntimeException("添加还款订单号，更新hyjf_increase_interest_loan表失败" + "，[出借订单号:" + investOrderId + "]");
					}
				} else {
					// 还款订单号
					repayOrderId = increaseInterestLoanDetail.getRepayOrderId();
					// 还款订单日期
					repayOrderDate = increaseInterestLoanDetail.getRepayOrderDate();
				}
				// 还款时间
				repayTime = increaseInterestLoanDetail.getRepayTime();
				// 还款利息
				repayInterest = increaseInterestLoanDetail.getRepayInterestWait();
			} else {
				throw new RuntimeException("分期还款计划表数据不存在。[借款编号:" + borrowNid + "]，" + "[出借订单号:" + investOrderId + "]，" + "[期数:" + periodNow + "]");
			}
		}
		// [endday: 按天计息, end:按月计息]
		else {
			// 保存还款订单号
			if (StringUtils.isBlank(increaseInterestLoan.getRepayOrderId())) {
				// 还款订单号
				repayOrderId = GetOrderIdUtils.getOrderId2(increaseInterestLoan.getUserId());
				// 还款订单日期
				repayOrderDate = GetOrderIdUtils.getOrderDate();
				// 设置还款订单号
				increaseInterestLoan.setRepayOrderId(repayOrderId);
				// 设置还款时间
				increaseInterestLoan.setRepayOrderDate(repayOrderDate);
				// 更新还款信息
				boolean increaseInterestLoanFlag = this.updateIncreaseInterestLoan(increaseInterestLoan) > 0 ? true : false;
				if (!increaseInterestLoanFlag) {
					throw new RuntimeException("添加还款订单号，更新hyjf_increase_interest_loan表失败" + "，[出借订单号:" + investOrderId + "]");
				}
			} else {
				// 还款订单号
				repayOrderId = increaseInterestLoan.getRepayOrderId();
				// 还款订单日期
				repayOrderDate = increaseInterestLoan.getRepayOrderDate();
			}

			// 还款时间
			repayTime = increaseInterestLoan.getRepayTime();
			// 还款利息
			repayInterest = increaseInterestLoan.getRepayInterestWait();
		}
		// 判断该收支明细是否存在时,跳出本次循环
		if (countAccountListByNid(repayOrderId) > 0) {
			return retMsgList;
		}
		// 获取公司子账户金额
		BigDecimal account = selectCompanyAccount();

		//调用银行接口
		if (repayInterest.compareTo(BigDecimal.ZERO) > 0 && account.compareTo(repayInterest) > 0) {
			BankCallBean transferBean = transfer(investUserId, investUserCustId, repayInterest,repayOrderId);
			String respCode = transferBean == null ? "" : transferBean.getRetCode();
			// 调用接口失败时(000以外)
			if (!BankCallStatusConstant.RESPCODE_SUCCESS.equals(respCode)) {
				String message = transferBean == null ? "" : transferBean.getRetMsg();
				LogUtil.errorLog(THIS_CLASS, methodName, "融通宝自动还款调用转账接口失败。" + message + "，[出借订单号:" + investOrderId + "]", null);
				throw new RuntimeException("融通宝自动还款调用转账接口失败。" + respCode + ":" + message + "，[出借订单号:" + investOrderId + "]");
			}
			txDate = StringUtils.isNotBlank(transferBean.getTxDate()) ? Integer.parseInt(transferBean.getTxDate()) : 0;
			txTime = StringUtils.isNotBlank(transferBean.getTxTime()) ? Integer.parseInt(transferBean.getTxTime()) : 0;
			seqNo = StringUtils.isNotBlank(transferBean.getSeqNo()) ? Integer.parseInt(transferBean.getSeqNo()) : 0;
			;
		} else {
			System.out.println("融通宝自动还款。" + "[还款金额:" + repayInterest + "，[公司子账户可用余额:" + account + "，[出借订单号:" + investOrderId + "]");
		}

		// 判断该收支明细是否存在时,跳出本次循环
		if (countAccountListByNid(repayOrderId) == 0) {
			// 更新账户信息(出借人)
			Account investUserAccount = new Account();
			investUserAccount.setUserId(investUserId);
			// 出借人可用余额
			investUserAccount.setBankBalance(repayInterest);
			// 出借人待收金额
			investUserAccount.setBankAwait(repayInterest);
			// 出借人待收收益
			investUserAccount.setBankAwaitInterest(repayInterest);
			// 江西银行可用余额
			investUserAccount.setBankBalanceCash(repayInterest);
			// 累计收益
			investUserAccount.setBankInterestSum(repayInterest);
			// 还款后更新出借人的账户信息
			boolean investAccountFlag = this.batchAccountCustomizeMapper.updateAccountAfterRepay(investUserAccount) > 0 ? true : false;
			if (investAccountFlag) {
				// 取得账户信息(出借人)
				investUserAccount = this.selectAccountByUserId(investUserId);
				if (investUserAccount != null) {
					// 写入收支明细
					AccountList accountList = new AccountList();
					accountList.setNid(repayOrderId); // 还款订单号
					accountList.setUserId(investUserId); // 出借人
					accountList.setAmount(repayInterest); // 出借总收入
					accountList.setAccountId(investUserCust.getAccount());// 出借人客户号
					accountList.setType(1); // 1收入
					accountList.setTrade("increase_interest_repay_yes"); // 出借成功
					accountList.setTradeCode("balance"); // 余额操作
					accountList.setTxDate(txDate);
					accountList.setTxTime(txTime);
					accountList.setSeqNo(String.valueOf(seqNo));
					accountList.setBankSeqNo(String.valueOf(txDate) + String.valueOf(txTime) + String.valueOf(seqNo));
					accountList.setBankTotal(investUserAccount.getBankTotal()); // 银行总资产
					accountList.setBankBalance(investUserAccount.getBankBalance()); // 银行可用余额
					accountList.setBankFrost(investUserAccount.getBankFrost());// 银行冻结金额
					accountList.setBankWaitCapital(investUserAccount.getBankWaitCapital());// 银行待还本金
					accountList.setBankWaitInterest(investUserAccount.getBankWaitInterest());// 银行待还利息
					accountList.setBankAwaitCapital(investUserAccount.getBankAwaitCapital());// 银行待收本金
					accountList.setBankAwaitInterest(investUserAccount.getBankAwaitInterest());// 银行待收利息
					accountList.setBankAwait(investUserAccount.getBankAwait());// 银行待收总额
					accountList.setBankInterestSum(investUserAccount.getBankInterestSum()); // 银行累计收益
					accountList.setBankInvestSum(investUserAccount.getBankInvestSum());// 银行累计出借
					accountList.setBankWaitRepay(investUserAccount.getBankWaitRepay());// 银行待还金额
					accountList.setTotal(investUserAccount.getTotal());
					accountList.setBalance(investUserAccount.getBalance());
					accountList.setFrost(investUserAccount.getFrost());
					accountList.setAwait(investUserAccount.getAwait());
					accountList.setRepay(investUserAccount.getRepay());
					accountList.setCreateTime(nowTime); // 创建时间
					accountList.setBaseUpdate(nowTime); // 更新时间
					accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY); // 操作者
					accountList.setRemark(borrowNid);
					accountList.setIp(borrow.getAddip()); // 操作IP
					accountList.setIsUpdate(0);
					accountList.setBaseUpdate(0);
					accountList.setInterest(BigDecimal.ZERO); // 利息
					accountList.setWeb(0); // PC
					accountList.setIsBank(1);
					accountList.setCheckStatus(0);
					boolean investAccountListFlag = insertAccountList(accountList) > 0 ? true : false;
					if (investAccountListFlag) {
						// 更新还款明细表
						// 分期并且不是最后一期
						if (increaseInterestLoanDetail != null && Validator.isNotNull(periodNext) && periodNext > 0) {
							increaseInterestLoan.setRepayStatus(0); // 未还款
							// 取得分期还款计划表下一期的还款
							IncreaseInterestLoanDetail loanDetail = selectIncreaseInterestLoanDetail(borrowNid, periodNow + 1, investUserId, increaseInterestLoan.getInvestId());
							increaseInterestLoan.setRepayTime(loanDetail.getRepayTime()); // 计算下期时间
						} else {
							increaseInterestLoan.setRepayStatus(1); // 已还款
							increaseInterestLoan.setRepayActionTime(String.valueOf(nowTime)); // 实际还款时间
							increaseInterestLoan.setRepayTime(repayTime);
						}
						// 分期时
						if (increaseInterestLoanDetail != null) {
							increaseInterestLoan.setRepayPeriod(periodNext);
						}
						increaseInterestLoan.setRepayInterestYes(increaseInterestLoan.getRepayInterestYes().add(repayInterest));
						increaseInterestLoan.setRepayInterestWait(increaseInterestLoan.getRepayInterestWait().subtract(repayInterest));
						increaseInterestLoan.setWeb(2); // 写入网站收支
						boolean increaseInterestLoanFlag = this.increaseInterestLoanMapper.updateByPrimaryKeySelective(increaseInterestLoan) > 0 ? true : false;
						if (increaseInterestLoanFlag) {
							// 更新总的还款明细
							// 分期并且不是最后一期
							log.info("-----------------------加息还款判断是否分期，标的号：" + borrowNid + ",periodNext:" + periodNext);
							if (increaseInterestLoanDetail != null && Validator.isNotNull(periodNext) && periodNext > 0) {
								increaseInterestRepay.setRepayStatus(0); // 未还款
								// 取得分期还款计划表下一期的还款
								IncreaseInterestLoanDetail loanDetail = selectIncreaseInterestLoanDetail(borrowNid, periodNow + 1, investUserId, increaseInterestLoan.getInvestId());
								increaseInterestRepay.setRepayTime(loanDetail.getRepayTime()); // 计算下期时间
							} else {
								increaseInterestRepay.setRepayActionTime(String.valueOf(nowTime)); // 实际还款时间
								increaseInterestRepay.setRepayTime(repayTime);
							}
							// 分期时
							if (increaseInterestLoanDetail != null) {
								increaseInterestRepay.setRepayPeriod(periodNow);// 当前还款期数
								increaseInterestRepay.setRemainPeriod(periodNext);// 剩余还款期数
							} else {
								// 不分期
								increaseInterestRepay.setRepayPeriod(1);
								increaseInterestRepay.setRemainPeriod(0);
							}
							// 更新总的还款明细
							increaseInterestRepay.setRepayInterestYes(increaseInterestRepay.getRepayInterestYes().add(repayInterest));
							increaseInterestRepay.setWeb(0);
							boolean increaseInterestRepayFlag = this.increaseInterestRepayMapper.updateByPrimaryKeySelective(increaseInterestRepay) > 0 ? true : false;
							if (increaseInterestRepayFlag) {
								increaseInterestInvest.setRepayInterestYes(increaseInterestInvest.getRepayInterestYes().add(repayInterest));
								increaseInterestInvest.setRepayInterestWait(increaseInterestInvest.getRepayInterestWait().subtract(repayInterest));
								increaseInterestInvest.setRepayTimes(increaseInterestInvest.getRepayTimes() + 1);
								// 分期时并且是最后一期
								if (increaseInterestLoanDetail != null && Validator.isNotNull(periodNext) && periodNext == 0) {
									increaseInterestInvest.setRepayActionTime(nowTime);// add by cwyang 20180730 产品加息需求新增实际还款时间
								} else {
									// 不分期
									increaseInterestInvest.setRepayActionTime(nowTime);// add by cwyang 20180730 产品加息需求新增实际还款时间

								}

								boolean increaseInterestInvestFlag = this.increaseInterestInvestMapper.updateByPrimaryKeySelective(increaseInterestInvest) > 0 ? true : false;
								if (increaseInterestInvestFlag) {
									// 分期时
									if (increaseInterestLoanDetail != null) {
										// 更新还款计划表
										increaseInterestLoanDetail.setRepayStatus(1);
										increaseInterestLoanDetail.setRepayActionTime(String.valueOf(nowTime));
										increaseInterestLoanDetail.setRepayInterestYes(repayInterest);
										increaseInterestLoanDetail.setRepayInterestWait(BigDecimal.ZERO);
										increaseInterestLoanDetail.setRepayOrderDate(repayOrderDate);
										increaseInterestLoanDetail.setRepayOrderId(repayOrderId);
										boolean increaseInterestLoanDetailFlag = this.increaseInterestLoanDetailMapper.updateByPrimaryKeySelective(increaseInterestLoanDetail) > 0 ? true : false;
										if (increaseInterestLoanDetailFlag) {
											// 更新总的还款计划
											IncreaseInterestRepayDetail increaseInterestRepayDetail = selectIncreaseInterestRepayDetail(borrowNid, periodNow);
											if (increaseInterestRepayDetail != null) {
												increaseInterestRepayDetail.setRepayStatus(1);
												increaseInterestRepayDetail.setRepayActionTime(String.valueOf(nowTime));
												increaseInterestRepayDetail.setRepayInterestYes(increaseInterestRepayDetail.getRepayInterestYes().add(repayInterest));
												increaseInterestRepayDetail.setRepayInterestWait(increaseInterestRepayDetail.getRepayInterestWait().subtract(repayInterest));
												increaseInterestRepayDetail.setOrderId(repayOrderId);
												increaseInterestRepayDetail.setOrderDate(repayOrderDate);
												boolean increaseInterestRepayDetailFlag = this.increaseInterestRepayDetailMapper.updateByPrimaryKeySelective(increaseInterestRepayDetail) > 0 ? true
														: false;
												if (!increaseInterestRepayDetailFlag) {
													throw new RuntimeException("融通宝加息还款详情表(hyjf_increase_interest_repay_detail)更新失败!" + "[出借订单号:" + investOrderId + "]");
												}

											} else {
												throw new RuntimeException("融通宝加息还款详情表(hyjf_increase_interest_repay_detail)查询失败!" + "[出借订单号:" + investOrderId + "]");
											}
										} else {
											throw new RuntimeException("融通宝加息出借表(hyjf_increase_interest_invest)更新失败!" + "[出借订单号:" + investOrderId + "]");
										}
									}
									// 写入网站收支
									if (repayInterest.compareTo(BigDecimal.ZERO) > 0) {
										// 插入网站收支明细记录
										AccountWebList accountWebList = new AccountWebList();
										accountWebList.setOrdid(increaseInterestInvest.getOrderId() + "_" + periodNow);// 订单号
										accountWebList.setBorrowNid(borrowNid); // 出借编号
										accountWebList.setUserId(investUserId); // 借款人
										accountWebList.setAmount(repayInterest); // 产品加息收益
										accountWebList.setType(CustomConstants.TYPE_OUT); // 类型1收入,2支出
										accountWebList.setTrade(CustomConstants.TRADE_REPAY); // 产品加息收益
										accountWebList.setTradeType(CustomConstants.TRADE_REPAY_NM); // 产品加息收益
										accountWebList.setRemark(borrowNid); // 项目编号
										accountWebList.setCreateTime(nowTime);
										int accountWebListCnt = insertAccountWebList(accountWebList);
										if (accountWebListCnt == 0) {
											throw new RuntimeException("网站收支记录(huiyingdai_account_web_list)更新失败!" + "[出借订单号:" + investOrderId + "]");
										}
										msg.put(VAL_TITLE, borrowNid);
										msg.put(VAL_USERID, String.valueOf(increaseInterestLoan.getUserId()));
										msg.put(VAL_AMOUNT, repayInterest == null ? "0.00" : repayInterest.toString());
										msg.put(VAL_PROFIT, repayInterest == null ? "0.00" : repayInterest.toString());
									}
								} else {
									throw new RuntimeException("融通宝加息出借表(hyjf_increase_interest_invest)更新失败!" + "[出借订单号:" + investOrderId + "]");
								}
							} else {
								throw new RuntimeException("总的还款明细表(hyjf_increase_interest_repay)更新失败!" + "[出借订单号:" + investOrderId + "]");
							}
						} else {
							throw new RuntimeException("融通宝加息项目放款总表(hyjf_increase_interest_loan)更新失败!" + "[出借订单号:" + investOrderId + "]");
						}
					} else {
						throw new RuntimeException("收支明细(huiyingdai_account_list)写入失败!" + "[出借订单号:" + investOrderId + "]");
					}
				} else {
					throw new RuntimeException("出借人账户信息不存在。[出借人ID:" + investUserId + "]，" + "[出借订单号:" + investOrderId + "]");
				}
			} else {
				throw new RuntimeException("出借人资金记录(huiyingdai_account)更新失败!" + "[出借订单号:" + investOrderId + "]");
			}
		}
		System.out.println("-----------融通宝加息还款结束-----------" + apicron.getBorrowNid() + "-----[还款订单号:----" + repayOrderId + "]");
		return retMsgList;
	}

	/**
	 * 根据项目编号检索融通宝加息还款信息
	 * 
	 * @Title selectIncreaseInterestRepay
	 * @param borrowNid
	 * @return
	 */
	private IncreaseInterestRepay selectIncreaseInterestRepay(String borrowNid) {
		IncreaseInterestRepayExample example = new IncreaseInterestRepayExample();
		IncreaseInterestRepayExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<IncreaseInterestRepay> increaseInterestRepayList = this.increaseInterestRepayMapper.selectByExample(example);
		if (increaseInterestRepayList != null && increaseInterestRepayList.size() > 0) {
			return increaseInterestRepayList.get(0);
		}
		return null;
	}

	/**
	 * 根据出借订单号检索融通宝加息出借信息
	 * 
	 * @Title selectIncreaseInterestInvestInfo
	 * @param investOrderId
	 * @return
	 */
	private IncreaseInterestInvest selectIncreaseInterestInvestInfo(String investOrderId) {
		IncreaseInterestInvestExample example = new IncreaseInterestInvestExample();
		IncreaseInterestInvestExample.Criteria cra = example.createCriteria();
		cra.andOrderIdEqualTo(investOrderId);
		List<IncreaseInterestInvest> list = this.increaseInterestInvestMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据标的编号,还款期数,出借用户Id,出借Id检索还款信息
	 * 
	 * @Title selectIncreaseInterestLoanDetail
	 * @param borrowNid
	 * @param periodNow
	 * @param investUserId
	 * @param investId
	 * @return
	 */
	private IncreaseInterestLoanDetail selectIncreaseInterestLoanDetail(String borrowNid, Integer periodNow, Integer investUserId, Integer investId) {
		IncreaseInterestLoanDetailExample example = new IncreaseInterestLoanDetailExample();
		IncreaseInterestLoanDetailExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		cra.andRepayPeriodEqualTo(periodNow);
		cra.andUserIdEqualTo(investUserId);
		cra.andInvestIdEqualTo(investId);
		cra.andRepayStatusEqualTo(0);// 未转账
		List<IncreaseInterestLoanDetail> list = this.increaseInterestLoanDetailMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 更新融通宝加息项目放款详情表
	 * 
	 * @Title updateIncreaseInterestLoanDetail
	 * @param increaseInterestLoanDetail
	 * @return
	 */
	private int updateIncreaseInterestLoanDetail(IncreaseInterestLoanDetail increaseInterestLoanDetail) {
		int cnt = this.increaseInterestLoanDetailMapper.updateByPrimaryKeySelective(increaseInterestLoanDetail);
		return cnt;
	}

	/**
	 * 更新融通宝加息项目放款总表
	 * 
	 * @Title updateIncreaseInterestLoan
	 * @param increaseInterestLoan
	 * @return
	 */
	private int updateIncreaseInterestLoan(IncreaseInterestLoan increaseInterestLoan) {
		int cnt = this.increaseInterestLoanMapper.updateByPrimaryKeySelective(increaseInterestLoan);
		return cnt;
	}

	/**
	 * 根据还款订单号检索交易明细
	 * 
	 * @Title countAccountListByNid
	 * @param repayOrderId
	 * @return
	 */
	private int countAccountListByNid(String repayOrderId) {
		AccountListExample accountListExample = new AccountListExample();
		accountListExample.createCriteria().andNidEqualTo(repayOrderId).andTradeEqualTo("increase_interest_repay_yes");
		return this.accountListMapper.countByExample(accountListExample);
	}

	/**
	 * 调用银行红包发放接口
	 * @param investUserId
	 * @param investUserCustId
	 * @param repayInterest
	 * @param repayOrderId
	 * @return
	 */
	private BankCallBean transfer(Integer investUserId, String investUserCustId, BigDecimal repayInterest,String repayOrderId) {
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_VOUCHER_PAY);// 交易代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT));// 电子账号
		bean.setTxAmount(repayInterest.toString());
		bean.setForAccountId(investUserCustId);
		bean.setDesLineFlag("1");
		bean.setDesLine(repayOrderId);// add by cwyang 用于红包发放的银行对账依据,对应accountList 表的Nid
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(investUserId));// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(investUserId)); // 操作者ID
		bean.setLogRemark("产品加息收益"); // 备注
		bean.setLogClient(0);// 平台
		return BankCallUtils.callApiBg(bean);
	}

	/**
	 * 写入收支明细
	 *
	 * @param accountList
	 * @return
	 */
	private int insertAccountList(AccountList accountList) {
		// 写入收支明细
		return this.accountListMapper.insertSelective(accountList);
	}

	/**
	 * 根据借款编号,还款期数检索当期还款详情
	 * 
	 * @Title selectIncreaseInterestRepayDetail
	 * @param borrowNid
	 * @param periodNow
	 * @return
	 */
	private IncreaseInterestRepayDetail selectIncreaseInterestRepayDetail(String borrowNid, Integer periodNow) {
		IncreaseInterestRepayDetailExample example = new IncreaseInterestRepayDetailExample();
		IncreaseInterestRepayDetailExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		cra.andRepayPeriodEqualTo(periodNow);
		List<IncreaseInterestRepayDetail> list = this.increaseInterestRepayDetailMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * 插入网站收支记录
	 *
	 * @return
	 */
	private int insertAccountWebList(AccountWebList accountWebList) {
		if (countAccountWebList(accountWebList.getOrdid(), accountWebList.getTrade()) == 0) {
			// 设置部门信息
			setDepartments(accountWebList);
			// 插入
			return this.accountWebListMapper.insertSelective(accountWebList);
		}
		return 0;
	}

	/**
	 * 判断网站收支是否存在
	 *
	 * @param nid
	 * @return
	 */
	private int countAccountWebList(String nid, String trade) {
		AccountWebListExample example = new AccountWebListExample();
		example.createCriteria().andOrdidEqualTo(nid).andTradeEqualTo(trade);
		return this.accountWebListMapper.countByExample(example);
	}

	/**
	 * 设置部门名称
	 *
	 * @param accountWebList
	 */
	private void setDepartments(AccountWebList accountWebList) {
		if (accountWebList != null) {
			Integer userId = accountWebList.getUserId();
			UsersInfo usersInfo = getUsersInfoByUserId(userId);
			if (usersInfo != null) {
				Integer attribute = usersInfo.getAttribute();
				if (attribute != null) {
					// 查找用户的的推荐人
					Users users = getUsersByUserId(userId);
					Integer refUserId = users.getReferrer();
					SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
					SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
					spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
					List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
					if (sList != null && !sList.isEmpty()) {
						refUserId = sList.get(0).getSpreadsUserid();
					}
					// 如果是线上员工或线下员工，推荐人的userId和username不插
					if (users != null && (attribute == 2 || attribute == 3)) {
						// 查找用户信息
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是无主单，全插
					else if (users != null && (attribute == 1)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是有主单
					else if (users != null && (attribute == 0)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
				}
				accountWebList.setTruename(usersInfo.getTruename());
				accountWebList.setFlag(1);
			}
		}
	}

	/**
	 * 还款成功后,更新标的状态
	 * 
	 * @Title updateBorrowStatus
	 * @param borrowNid
	 * @param periodNow
	 * @param borrowUserId
	 */
	@Override
	public void updateBorrowStatus(String borrowNid, Integer periodNow, Integer borrowUserId) {
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 标的项目详情
		Borrow borrow = selectBorrowInfo(borrowNid);
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle())
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle());
		// 查询未债转的数据
		IncreaseInterestLoanExample example = new IncreaseInterestLoanExample();
		example.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayStatusEqualTo(0);
		int increaseInterestLoanCnt = this.increaseInterestLoanMapper.countByExample(example);
		int repayStatus = 0;
		if (increaseInterestLoanCnt == 0) {
			repayStatus = 1;
		}
		// 借款人还款表更新
		IncreaseInterestRepay increaseInterestRepay = new IncreaseInterestRepay();
//		increaseInterestRepay.setRepayActionTime(String.valueOf(nowTime));
		if (increaseInterestLoanCnt == 0) {
			increaseInterestRepay.setRepayStatus(repayStatus); // 已还款
			increaseInterestRepay.setRepayActionTime(String.valueOf(nowTime)); // 实际还款时间
		} else {
			increaseInterestRepay.setRepayStatus(repayStatus);// 未还款
			if (isMonth) {
				// 分期的场合，根据借款编号和还款期数从还款计划表中取得还款时间
				IncreaseInterestRepayDetailExample increaseInterestRepayDetailExample = new IncreaseInterestRepayDetailExample();
				IncreaseInterestRepayDetailExample.Criteria increaseInterestRepayDetailCriteria = increaseInterestRepayDetailExample.createCriteria();
				increaseInterestRepayDetailCriteria.andBorrowNidEqualTo(borrowNid);
				increaseInterestRepayDetailCriteria.andRepayPeriodEqualTo(periodNow + 1);
				List<IncreaseInterestRepayDetail> increaseInterestRepayDetails = increaseInterestRepayDetailMapper.selectByExample(increaseInterestRepayDetailExample);
				if (increaseInterestRepayDetails != null && increaseInterestRepayDetails.size() > 0) {
					IncreaseInterestRepayDetail IncreaseInterestRepayDetail = increaseInterestRepayDetails.get(0);
					// 设置下期还款时间
					increaseInterestRepay.setRepayTime(IncreaseInterestRepayDetail.getRepayTime());
				}
			}
		}
		// 更新IncreaseInterestRepay
		IncreaseInterestRepayExample increaseInterestRepayExample = new IncreaseInterestRepayExample();
		increaseInterestRepayExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayStatusEqualTo(0);
		this.increaseInterestRepayMapper.updateByExampleSelective(increaseInterestRepay, increaseInterestRepayExample);
	}

	/**
	 * 还款成功后,发送短信
	 * 
	 * @Title sendSms
	 * @param msgList
	 */
	@Override
	public void sendSms(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && NumberUtils.isNumber(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null || Validator.isNull(users.getMobile()) || (users.getRecieveSms() != null && users.getRecieveSms() == 1)) {
						return;
					}
					UsersInfo userInfo = this.getUsersInfoByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (StringUtils.isEmpty(userInfo.getTruename())) {
						msg.put(VAL_NAME, users.getUsername());
					} else if (userInfo.getTruename().length() > 1) {
						msg.put(VAL_NAME, userInfo.getTruename().substring(0, 1));
					} else {
						msg.put(VAL_NAME, userInfo.getTruename());
					}
					Integer sex = userInfo.getSex();
					if (Validator.isNotNull(sex)) {
						if (sex.intValue() == 2) {
							msg.put(VAL_SEX, "女士");
						} else {
							msg.put(VAL_SEX, "先生");
						}
					}
					SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_JIAXIHUANKUAN,
							CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				}
			}
		}
	}

	/**
	 * 推送消息
	 * 
	 * @param msgList
	 * @author Administrator
	 */

	@Override
	public void sendMessage(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT)) && new BigDecimal(msg.get(VAL_AMOUNT)).compareTo(BigDecimal.ZERO) > 0) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null) {
						return;
					} else {
						UsersInfo userInfo = this.getUsersInfoByUserId(Integer.valueOf(msg.get(VAL_USERID)));
						if (StringUtils.isEmpty(userInfo.getTruename())) {
							msg.put(VAL_NAME, users.getUsername());
						} else if (userInfo.getTruename().length() > 1) {
							msg.put(VAL_NAME, userInfo.getTruename().substring(0, 1));
						} else {
							msg.put(VAL_NAME, userInfo.getTruename());
						}
						Integer sex = userInfo.getSex();
						if (Validator.isNotNull(sex)) {
							if (sex.intValue() == 2) {
								msg.put(VAL_SEX, "女士");
							} else {
								msg.put(VAL_SEX, "先生");
							}
						}
						AppMsMessage smsMessage = new AppMsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_JIAXIHUANKUAN);
						appMsProcesser.gather(smsMessage);
					}
				}
			}
		}
	}

	/**
	 * 一次性还款获得优先处理任务
	 * @param borrowNid
	 * @return
	 */
	@Override
	public BorrowApicron getRepayPeriodSort(String borrowNid) {
		BorrowApicronExample example = new BorrowApicronExample();
		example.createCriteria().andBorrowNidEqualTo(borrowNid).andApiTypeEqualTo(1).andExtraYieldRepayStatusNotEqualTo(1);
		example.setOrderByClause("period_now");
		List<BorrowApicron> apicronList = this.borrowApicronMapper.selectByExample(example);
		if (apicronList != null && apicronList.size() > 0){
			BorrowApicron borrowApicron = apicronList.get(0);
			return borrowApicron;
		}
		return null;
	}

}

package com.hyjf.batch.htj.planloans;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.calculate.InterestInfo;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.pdf.PdfGenerator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.DebtAccountList;
import com.hyjf.mybatis.model.auto.DebtApicron;
import com.hyjf.mybatis.model.auto.DebtApicronExample;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.DebtDetail;
import com.hyjf.mybatis.model.auto.DebtFreeze;
import com.hyjf.mybatis.model.auto.DebtFreezeExample;
import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtInvestExample;
import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtLoanDetail;
import com.hyjf.mybatis.model.auto.DebtLoanLog;
import com.hyjf.mybatis.model.auto.DebtLoanLogExample;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;
import com.hyjf.mybatis.model.auto.DebtRepay;
import com.hyjf.mybatis.model.auto.DebtRepayDetail;
import com.hyjf.mybatis.model.auto.DebtRepayDetailExample;
import com.hyjf.mybatis.model.auto.DebtRepayExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * 自动扣款(放款服务)
 *
 * @author Administrator
 *
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class PlanLoansServiceImpl extends BaseServiceImpl implements PlanLoansService {

	private static final String THIS_CLASS = PlanLoansServiceImpl.class.getName();

	/** 等待 */
	private static final String TYPE_WAIT = "wait";

	/** 用户ID */
	private static final String VAL_USERID = "userId";

	/** 用户名 */
	private static final String VAL_NAME = "val_name";

	/** 出借订单号 */
	private static final String VAL_ORDER_ID = "order_id";

	/** 性别 */
	private static final String VAL_SEX = "val_sex";

	/** 性别 */
	private static final String VAL_TITLE = "val_title";

	/** 放款金额 */
	private static final String VAL_AMOUNT = "val_amount";

	/** 出借本金 */
	private static final String VAL_BALANCE = "val_balance";

	/** 预期收益 */
	private static final String VAL_PROFIT = "val_profit";
	/** 放款时间 */
	private static final String VAL_LOAN_TIME = "loan_time";

	/** 还款明细ID */
	private static final String PARAM_BORROWRECOVERID = "param_borrowrecoverid";

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	/**
	 * 自动放款
	 *
	 * @throws Exception
	 */
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public List<Map<String, String>> updateDebtLoans(DebtApicron apicron, DebtInvest debtInvest) throws Exception {

		String methodName = "updateDebtLoans";
		System.out.println(
				"-----------汇添金专属标放款开始---" + apicron.getBorrowNid() + "---------" + debtInvest.getLoanOrderId());
		List<Map<String, String>> retMsgList = new ArrayList<Map<String, String>>();
		/** 基本变量 */
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 标识ID
		String nid = apicron.getNid();
		// 借款编号
		String borrowNid = apicron.getBorrowNid();
		// 借款人ID
		Integer borrowUserid = apicron.getUserId();
		Users borrowUser = getUsersByUserId(borrowUserid);
		/** 标的基本数据 */
		// 取得标的详情
		DebtBorrowWithBLOBs borrow = getBorrow(borrowNid);
		// 标的是否进行银行托管
		int bankInputFlag = Validator.isNull(borrow.getBankInputFlag()) ? 0 : borrow.getBankInputFlag();
		Map<String, String> msg = new HashMap<String, String>();
		retMsgList.add(msg);
		// 借款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 服务费率
		BigDecimal serviceFee = Validator.isNull(borrow.getServiceFeeRate()) ? BigDecimal.ZERO
				: new BigDecimal(borrow.getServiceFeeRate());
		// 年利率
		BigDecimal borrowApr = borrow.getBorrowApr();
		// 月利率(算出管理费用[上限])
		BigDecimal borrowMonthRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO
				: new BigDecimal(borrow.getManageFeeRate());
		// 月利率(算出管理费用[下限])
		BigDecimal borrowManagerScaleEnd = Validator.isNull(borrow.getBorrowManagerScaleEnd()) ? BigDecimal.ZERO
				: new BigDecimal(borrow.getBorrowManagerScaleEnd());
		// 差异费率
		BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO
				: new BigDecimal(borrow.getDifferentialRate());
		// 初审时间
		int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
		// 借款成功时间
		Integer borrowSuccessTime = borrow.getBorrowSuccessTime();
		// 项目类型
		Integer projectType = borrow.getProjectType();
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		// 借款人在汇付的账户信息
		AccountChinapnr inCust = null;
		// 借款人用户ID
		Integer inUserId = null;
		// 借款人客户号
		Long inCustId = null;
		// 出借人在汇付的账户信息
		AccountChinapnr outCust = null;
		// 借款人客户号
		Long outCustId = null;
		// 服务费
		BigDecimal userFee = BigDecimal.ZERO;
		// 出借费用
		BigDecimal tenderAccount = BigDecimal.ZERO;
		// 利息
		BigDecimal interestTender = BigDecimal.ZERO;
		// 本金
		BigDecimal capitalTender = BigDecimal.ZERO;
		// 本息
		BigDecimal accountTender = BigDecimal.ZERO;
		// 管理费
		BigDecimal recoverFee = BigDecimal.ZERO;
		// 估计还款时间
		Integer recoverTime = null;
		// 出借订单号
		String ordId = debtInvest.getOrderId();
		// 取出冻结订单信息
		DebtFreeze freezeList = getFreezeList(ordId);
		if (freezeList == null) {
			throw new Exception("冻结订单表(hyjf_debt_freeze)查询失败！, " + "出借订单号[" + ordId + "]");
		}
		// 本平台交易唯一标识(解冻订单用)
		String trxId = freezeList.getTrxId();
		/** 写入或更新数据库(写入或更新0条时,抛出异常) */
		// 若此笔订单已经解冻
		if (freezeList.getStatus() == 1) {
			return retMsgList;
		}
		/** 自动放款处理开始 */
		// 借款人的账户信息
		inCust = super.getChinapnrUserInfo(borrowUserid);
		if (inCust == null) {
			throw new Exception("借款人未开户。[借款人ID：" + borrowUserid + "]，" + "[专属标出借订单号：" + ordId + "]");
		}
		inCustId = inCust.getChinapnrUsrcustid();
		inUserId = inCust.getUserId();
		// 出借人的账户信息
		outCust = super.getChinapnrUserInfo(debtInvest.getUserId());
		if (outCust == null) {
			throw new Exception("出借人未开户。[出借人ID：" + debtInvest.getUserId() + "]，" + "[专属标出借订单号：" + ordId + "]");
		}
		outCustId = outCust.getChinapnrUsrcustid();
		// 调用交易查询接口(解冻)
		ChinapnrBean queryTransStatBean = queryTransStat(debtInvest.getOrderId(), debtInvest.getOrderDate(), "FREEZE");
		String respCode = queryTransStatBean == null ? "" : queryTransStatBean.getRespCode();
		// 调用接口失败时(000以外)
		if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
			String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
			LogUtil.errorLog(THIS_CLASS, methodName, "调用交易查询接口(解冻)失败。" + message + ",[专属标出借订单号：" + ordId + "]", null);
			throw new Exception("调用交易查询接口(解冻)失败。" + respCode + "：" + message + ",[专属标出借订单号：" + ordId + "]");
		}
		// 汇付交易状态
		String transStat = queryTransStatBean == null ? "" : queryTransStatBean.getTransStat();
		// 冻结请求已经被解冻 U:已解冻 N:无需解冻，对于解冻交易
		if ("U".equals(transStat) && "N".equals(transStat)) {
			LogUtil.errorLog(THIS_CLASS, methodName, "订单状态错误，订单状态：【" + transStat + "】," + "专属标出借订单号：【" + ordId + "】",
					null);
			throw new Exception("订单状态错误，订单状态：【" + transStat + "】," + "专属标出借订单号：【" + ordId + "】");
		}
		// 出借金额
		tenderAccount = debtInvest.getAccount();
		// 服务费
		userFee = getUserFee(serviceFee, tenderAccount, borrowStyle, borrowPeriod);
		// 放款订单号
		String loanOrdId = debtInvest.getLoanOrderId();
		// 放款订单时间
		String loanOrdDate = debtInvest.getLoanOrderDate();
		// 调用交易查询接口(放款)
		ChinapnrBean queryTransStatBean2 = queryTransStat(loanOrdId, loanOrdDate, "LOANS");
		respCode = queryTransStatBean2 == null ? "" : queryTransStatBean2.getRespCode();
		// 调用接口失败时(000,421以外)
		if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)
				&& !ChinaPnrConstant.RESPCODE_NO_LOANS_RECORD.equals(respCode)) {
			String message = queryTransStatBean2 == null ? "" : queryTransStatBean2.getRespDesc();
			LogUtil.errorLog(THIS_CLASS, methodName, "调用交易查询接口(放款)失败。" + message + ",[专属标出借订单号：" + ordId + "]", null);
			throw new Exception("调用交易查询接口(放款)失败。" + respCode + "：" + message + ",[专属标出借订单号：" + ordId + "]");
		}
		// 汇付交易状态
		transStat = queryTransStatBean2 == null ? "" : queryTransStatBean2.getTransStat();
		if (!"I".equals(transStat) && !"P".equals(transStat)) {
			// 分账账户串（当 服务费！=0 时是必填项）
			String divDetails = "";
			if (userFee.compareTo(BigDecimal.ZERO) > 0) {
				JSONArray ja = new JSONArray();
				JSONObject jo = new JSONObject();
				// 分账商户号(商户号,从配置文件中取得)
				jo.put(ChinaPnrConstant.PARAM_DIVCUSTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
				// 分账账户号(子账户号,从配置文件中取得)
				jo.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT02));
				// 分账金额
				jo.put(ChinaPnrConstant.PARAM_DIVAMT, userFee.toString());
				ja.add(jo);
				divDetails = ja.toString();
			}
			// 入参扩展域(2.0用)
			String reqExts = "";
			if (bankInputFlag == 1) {
				JSONObject jo = new JSONObject();
				jo.put(ChinaPnrConstant.PARAM_PROID, borrowNid);
				reqExts = jo.toString();
			}
			// 调用汇付天下接口[自动扣款（放款）]
			ChinapnrBean loansBean = this.loans(trxId, inUserId, GetterUtil.getString(outCustId),
					tenderAccount.toString(), userFee.toString(), loanOrdId, loanOrdDate, ordId,
					GetOrderIdUtils.getOrderDate(freezeList.getCreateTime()), GetterUtil.getString(inCustId),
					divDetails, reqExts);
			respCode = loansBean == null ? "" : loansBean.getRespCode();
			// 调用接口失败时(000以外)
			if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
				String message = "调用自动扣款（放款）接口失败。" + respCode + "：" + loansBean == null ? ""
						: loansBean.getRespDesc() + "，[出借订单号：" + ordId + "]";
				LogUtil.errorLog(THIS_CLASS, methodName, message, null);
				throw new Exception(message);
			}
		}
		// 更新订单为已经解冻
		boolean flag = this.updateDebtFreeze(freezeList) > 0 ? true : false;
		if (!flag) {
			throw new Exception("冻结订单表(hyjf_debt_freeze)更新失败!" + "[出借订单号：" + ordId + "]");
		}
		// 计算利息
		InterestInfo interestInfo = CalculatesUtil.getInterestInfo(tenderAccount, borrowPeriod, borrowApr, borrowStyle,
				borrowSuccessTime, borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate,
				borrowVerifyTime);
		if (interestInfo != null) {
			interestTender = interestInfo.getRepayAccountInterest(); // 利息
			capitalTender = interestInfo.getRepayAccountCapital(); // 本金
			accountTender = interestInfo.getRepayAccount(); // 本息
			recoverTime = interestInfo.getRepayTime(); // 估计还款时间
			recoverFee = interestInfo.getFee(); // 总管理费
		}
		DebtPlanExample examplePlan = new DebtPlanExample();
		DebtPlanExample.Criteria criPlan = examplePlan.createCriteria();
		criPlan.andDebtPlanNidEqualTo(debtInvest.getPlanNid());
		List<DebtPlan> debtPlanList = debtPlanMapper.selectByExample(examplePlan);
		if (debtPlanList == null) {
			throw new Exception("计划表(hyjf_debt_plan)查询失败!" + "[出借订单号：" + ordId + "]");
		} else if (debtPlanList.size() <= 0) {
			throw new Exception("计划表(hyjf_debt_plan)查询失败!" + "[出借订单号：" + ordId + "]");
		}
		DebtPlan debtPlan = debtPlanList.get(0);
		// 写入还款明细表(hyjf_debt_loan)
		DebtLoan debtLoan = new DebtLoan();
		debtLoan.setUserId(debtInvest.getUserId()); // 出借人
		debtLoan.setUserName(debtInvest.getUserName()); // 出借人
		debtLoan.setBorrowNid(borrowNid); // 借款编号
		debtLoan.setInvestOrderId(ordId); // 出借订单号
		debtLoan.setBorrowUserId(borrowUserid); // 借款人
		debtLoan.setBorrowUserName(borrowUser.getUsername()); // 借款人用户名
		debtLoan.setPlanNid(debtInvest.getPlanNid());// 计划编号
		debtLoan.setPlanOrderId(debtInvest.getPlanOrderId());// 计划订单号
		debtLoan.setInvestId(debtInvest.getId()); // 出借表主键ID
		debtLoan.setLoanAccount(accountTender); // 预还金额
		debtLoan.setLoanInterest(interestTender); // 预还利息
		debtLoan.setLoanCapital(capitalTender); // 预还本金
		debtLoan.setAdvanceStatus(0); // 提前还款
		debtLoan.setAdvanceDays(0); // 提前还款天数
		debtLoan.setDelayDays(0); // 延期天数
		debtLoan.setLateDays(0); // 逾期天数
		debtLoan.setRepayAdvanceInterest(BigDecimal.ZERO); // 提前还款利息
		debtLoan.setRepayDelayInterest(BigDecimal.ZERO); // 延期利息
		debtLoan.setRepayLateInterest(BigDecimal.ZERO); // 逾期费用收入
		debtLoan.setRepayAdvanceInterestYes(BigDecimal.ZERO); // 提前还款利息
		debtLoan.setRepayDelayInterestYes(BigDecimal.ZERO); // 延期利息
		debtLoan.setRepayLateInterestYes(BigDecimal.ZERO); // 逾期费用收入
		debtLoan.setRepayAccountWait(accountTender); // 未还金额
		debtLoan.setRepayInterestWait(interestTender); // 未还利息
		debtLoan.setRepayCapitalWait(capitalTender); // 未还本金
		debtLoan.setRepayAccountYes(BigDecimal.ZERO); // 实还金额
		debtLoan.setRepayInterestYes(BigDecimal.ZERO); // 实还利息
		debtLoan.setRepayCapitalYes(BigDecimal.ZERO); // 实还本金
		debtLoan.setReceiveAccountYes(BigDecimal.ZERO);
		debtLoan.setReceiveCapitalYes(BigDecimal.ZERO);
		debtLoan.setReceiveInterestYes(BigDecimal.ZERO);
		debtLoan.setReceiveAdvanceInterest(BigDecimal.ZERO);
		debtLoan.setReceiveDelayInterest(BigDecimal.ZERO);
		debtLoan.setReceiveLateInterest(BigDecimal.ZERO);
		debtLoan.setReceiveAdvanceInterestYes(BigDecimal.ZERO);
		debtLoan.setReceiveDelayInterestYes(BigDecimal.ZERO);
		debtLoan.setReceiveLateInterestYes(BigDecimal.ZERO);
		debtLoan.setRemainPeriod(isMonth ? borrowPeriod : 1); // 剩余期数
		debtLoan.setRepayPeriod(0); // 已还期数
		debtLoan.setRepayTime(GetterUtil.getString(recoverTime)); // 估计还款时间
		debtLoan.setLoanType(TYPE_WAIT); // 还款状态:等待
		debtLoan.setRepayStatus(0);// 还款状态 0未还款 1还款中 2已还款
		debtLoan.setManageFee(recoverFee); // 账户管理费
		debtLoan.setLateDays(0); // 逾期天数
		debtLoan.setWeb(0); // 网站待还
		debtLoan.setAddip(debtInvest.getAddip());
		debtLoan.setCreditAmount(BigDecimal.ZERO);
		debtLoan.setCreditInterestAmount(BigDecimal.ZERO);
		debtLoan.setCreditTime(0);
		debtLoan.setCreateTime(nowTime);
		debtLoan.setCreateUserId(debtInvest.getUserId());
		debtLoan.setCreateUserName(debtInvest.getUserName());
		debtLoan.setSendmail(0);
		boolean debtLoanFlag = this.insertDebtLoan(debtLoan) > 0 ? true : false;
		if (!debtLoanFlag) {
			throw new Exception("还款明细表(hyjf_debt_loan)写入失败!" + "[出借订单号：" + ordId + "]");
		}

		// 更新出借详情表
		DebtInvest newDebtInvest = new DebtInvest();
		newDebtInvest.setId(debtInvest.getId()); // ID
		newDebtInvest.setRepayAccountWait(debtLoan.getRepayAccountWait()); // 待收总额
		newDebtInvest.setRepayAccount(debtLoan.getRepayAccountWait()); // 收款总额
		newDebtInvest.setRepayInterestWait(debtLoan.getRepayInterestWait()); // 待收利息
		newDebtInvest.setRepayInterest(debtLoan.getRepayInterestWait()); // 收款总利息
		newDebtInvest.setRepayCapitalWait(debtLoan.getRepayCapitalWait()); // 待收本金
		newDebtInvest.setRepayCapital(debtLoan.getRepayCapitalWait()); // 待收本金
		newDebtInvest.setLoanAmount(tenderAccount.subtract(userFee)); // 实际放款金额
		newDebtInvest.setLoanFee(userFee); // 服务费
		newDebtInvest.setStatus(1); // 状态 0，出借状态 0冻结成功 1放款成功2还款中 3还款成功
		newDebtInvest.setLoanOrderId(loanOrdId);// 放款订单号
		newDebtInvest.setLoanOrderDate(loanOrdDate);// 放款日期
		newDebtInvest.setWeb(2); // 写入网站收支明细
		boolean debtInvestFlag = this.updateDebtInvest(newDebtInvest) > 0 ? true : false;
		if (!debtInvestFlag) {
			throw new Exception("出借详情(hyjf_debt_invest)更新失败!" + "[出借订单号：" + ordId + "]");
		}

		// 更新借款表
		DebtBorrowWithBLOBs newBrrow = new DebtBorrowWithBLOBs();
		newBrrow.setId(borrow.getId());
		newBrrow.setRepayAccountAll(borrow.getRepayAccountAll().add(debtLoan.getRepayAccountWait())); // 应还款总额
		newBrrow.setRepayAccountInterest(borrow.getRepayAccountInterest().add(debtLoan.getRepayInterestWait())); // 总还款利息
		newBrrow.setRepayAccountCapital(borrow.getRepayAccountCapital().add(debtLoan.getRepayCapitalWait())); // 总还款本金
		newBrrow.setRepayAccountWait(borrow.getRepayAccountWait().add(debtLoan.getRepayAccountWait())); // 未还款总额
		newBrrow.setRepayAccountInterestWait(borrow.getRepayAccountInterestWait().add(debtLoan.getRepayInterestWait())); // 未还款利息
		newBrrow.setRepayAccountCapitalWait(borrow.getRepayAccountCapitalWait().add(debtLoan.getRepayCapitalWait())); // 未还款本金
		newBrrow.setRepayLastTime(
				GetterUtil.getString(DateUtils.getRepayDate(borrowStyle, new Date(), borrowPeriod, borrowPeriod))); // 最后还款时间
		newBrrow.setRepayNextTime(recoverTime); // 下次还款时间
		newBrrow.setRepayEachTime("每月" + GetDate.getServerDateTime(15, new Date()) + "日");// 每次还款的时间
		boolean borrowFlag = this.debtBorrowMapper.updateByPrimaryKeySelective(newBrrow) > 0 ? true : false;
		if (!borrowFlag) {
			throw new Exception("借款详情(huiyingdai_borrow)更新失败!" + "[出借订单号：" + ordId + "]");
		}

		// 写入借款满标日志(原复审业务)
		boolean isInsert = false;
		DebtLoanLog accountBorrow = getAccountBorrow(borrowNid);
		if (accountBorrow == null) {
			isInsert = true;
			accountBorrow = new DebtLoanLog();
			accountBorrow.setNid(nid); // 生成规则：BorrowNid_userid_期数
			accountBorrow.setBorrowNid(borrowNid); // 借款编号
			accountBorrow.setUserId(borrowUserid); // 借款人编号
			accountBorrow.setMoney(BigDecimal.ZERO);// 总收入金额
			accountBorrow.setFee(BigDecimal.ZERO);// 计算服务费
			accountBorrow.setBalance(BigDecimal.ZERO); // 实际到账金额
			accountBorrow.setCreateTime(nowTime); // 创建时间
		}
		accountBorrow.setMoney(accountBorrow.getMoney().add(tenderAccount));// 总收入金额
		accountBorrow.setFee(accountBorrow.getFee().add(userFee));// 计算服务费
		accountBorrow.setBalance(accountBorrow.getBalance().add(tenderAccount.subtract(userFee))); // 实际到账金额
		accountBorrow
				.setRemark("专属标借款成功[" + borrow.getBorrowNid() + "]，扣除服务费{" + accountBorrow.getFee().toString() + "}元");
		accountBorrow.setUpdateTime(nowTime); // 更新时间
		boolean accountBorrowFlag = false;
		if (isInsert) {
			accountBorrowFlag = this.debtLoanLogMapper.insertSelective(accountBorrow) > 0 ? true : false;
		} else {
			accountBorrowFlag = this.debtLoanLogMapper.updateByPrimaryKeySelective(accountBorrow) > 0 ? true : false;
		}
		if (!accountBorrowFlag) {
			throw new Exception("借款满标日志(huiyingdai_account_borrow)更新失败!" + "[出借订单号：" + ordId + "]");
		}
		// 插入每个标的总的还款信息
		isInsert = false;
		DebtRepay debtRepay = getDebtRepay(borrowNid);
		if (debtRepay == null) {
			isInsert = true;
			debtRepay = new DebtRepay();
			debtRepay.setLoanStatus(0); // 状态
			debtRepay.setUserId(borrowUserid); // 借款人ID
			debtRepay.setUserName(borrowUser.getUsername());// 借款人用户名
			debtRepay.setBorrowNid(borrowNid); // 借款标号
			debtRepay.setRepayNid(nid); // 标识
			debtRepay.setPlanNid(debtInvest.getPlanNid()); // 计划标识
			debtRepay.setManageFee(BigDecimal.ZERO); // 还款费用
			debtRepay.setRepayStatus(0);// 还款状态0未还款1还款中2已还款
			debtRepay.setRemainPeriod(isMonth ? borrowPeriod : 1);// 剩余期数
			debtRepay.setRepayPeriod(1);
			debtRepay.setAlreadyRepayPeriod(0);// 已还期数
			debtRepay.setRepayActionTime(""); // 执行还款的时间
			debtRepay.setRepayStatus(0); // 还款状态
			debtRepay.setRepayTime(GetterUtil.getString(recoverTime)); // 估计还款时间
			debtRepay.setRepayAccountAll(BigDecimal.ZERO); // 还款总额，加上费用
			debtRepay.setRepayAccount(BigDecimal.ZERO); // 预还金额
			debtRepay.setRepayInterest(BigDecimal.ZERO); // 预还利息
			debtRepay.setRepayCapital(BigDecimal.ZERO); // 预还本金
			debtRepay.setRepayAccountYes(BigDecimal.ZERO); // 实还金额
			debtRepay.setLateDays(0); // 逾期的天数
			debtRepay.setRepayInterestYes(BigDecimal.ZERO); // 实还利息
			debtRepay.setRepayCapitalYes(BigDecimal.ZERO); // 实还本金
			debtRepay.setRepayCapitalWait(BigDecimal.ZERO);// 未还本金
			debtRepay.setRepayInterestWait(BigDecimal.ZERO); // 未还利息
			debtRepay.setAdvanceStatus(0); // 是否提前还款 0正常还款1提前还款2延期还款3逾期还款
			debtRepay.setAdvanceDays(0);// 提前还款天数
			debtRepay.setAdvanceInterest(BigDecimal.ZERO);// 提前还款利息
			debtRepay.setLateDays(0); // 逾期天数
			debtRepay.setLateInterest(BigDecimal.ZERO); // 逾期利息
			debtRepay.setDelayDays(0); // 逾期天数
			debtRepay.setDelayInterest(BigDecimal.ZERO); // 逾期利息
			debtRepay.setDelayRemark(""); // 备注
			debtRepay.setAddip(borrow.getAddip()); // 发标ip
			debtRepay.setCreateTime(nowTime); // 创建时间
			debtRepay.setCreateUserId(debtInvest.getUserId()); // 创建人
			debtRepay.setCreateUserName(debtInvest.getUserName()); // 创建人姓名
		}
		debtRepay.setManageFee(debtRepay.getManageFee().add(debtLoan.getManageFee())); // 还款费用
		debtRepay.setRepayAccount(debtRepay.getRepayAccount().add(debtLoan.getRepayAccountWait())); // 预还金额
		debtRepay.setRepayInterest(debtRepay.getRepayInterest().add(debtLoan.getRepayInterestWait())); // 预还利息
		debtRepay.setRepayCapital(debtRepay.getRepayCapital().add(debtLoan.getRepayCapitalWait())); // 预还本金
		boolean debtRepayFlag = false;
		if (isInsert) {
			debtRepayFlag = this.debtRepayMapper.insertSelective(debtRepay) > 0 ? true : false;
		} else {
			debtRepayFlag = this.debtRepayMapper.updateByPrimaryKeySelective(debtRepay) > 0 ? true : false;
		}
		if (!debtRepayFlag) {
			throw new Exception(
					"每个标的总的还款信息(hyjf_debt_repay)" + (isInsert ? "插入" : "更新") + "失败!" + "[出借订单号：" + ordId + "]");
		}
		// [principal: 等额本金,month:等额本息,end:先息后本]
		if (isMonth) {
			// 更新分期还款计划表(huiyingdai_borrow_recover_plan)
			if (interestInfo != null && interestInfo.getListMonthly() != null) {
				DebtLoanDetail debtLoanDetail = null;
				InterestInfo monthly = null;
				for (int j = 0; j < interestInfo.getListMonthly().size(); j++) {
					monthly = interestInfo.getListMonthly().get(j);
					debtLoanDetail = new DebtLoanDetail();
					debtLoanDetail.setUserId(debtInvest.getUserId()); // 出借人
					debtLoanDetail.setUserName(debtInvest.getUserName()); // 出借人
					debtLoanDetail.setBorrowNid(borrowNid); // 借款编号
					debtLoanDetail.setInvestOrderId(ordId); // 出借订单号
					debtLoanDetail.setBorrowUserId(borrowUserid); // 借款人
					debtLoanDetail.setBorrowUserName(borrowUser.getUsername()); // 借款人用户名
					debtLoanDetail.setPlanNid(debtInvest.getPlanNid());// 计划编号
					debtLoanDetail.setPlanOrderId(debtInvest.getPlanOrderId());// 计划订单号
					debtLoanDetail.setInvestId(debtInvest.getId()); // 出借表主键ID
					debtLoanDetail.setLoanAccount(monthly.getRepayAccount()); // 预还金额
					debtLoanDetail.setLoanInterest(monthly.getRepayAccountInterest()); // 预还利息
					debtLoanDetail.setLoanCapital(monthly.getRepayAccountCapital()); // 预还本金
					debtLoanDetail.setAdvanceStatus(0); // 提前还款
					debtLoanDetail.setAdvanceDays(0); // 提前还款天数
					debtLoanDetail.setDelayDays(0); // 延期天数
					debtLoanDetail.setLateDays(0); // 逾期天数
					debtLoanDetail.setRepayDelayInterest(BigDecimal.ZERO);
					debtLoanDetail.setRepayLateInterest(BigDecimal.ZERO);
					debtLoanDetail.setRepayAdvanceInterest(BigDecimal.ZERO);
					debtLoanDetail.setRepayDelayInterestYes(BigDecimal.ZERO);
					debtLoanDetail.setRepayLateInterestYes(BigDecimal.ZERO);
					debtLoanDetail.setRepayAdvanceInterestYes(BigDecimal.ZERO);
					debtLoanDetail.setRepayAccountWait(monthly.getRepayAccount()); // 未还金额
					debtLoanDetail.setRepayCapitalWait(monthly.getRepayAccountCapital()); // 未还本金
					debtLoanDetail.setRepayInterestWait(monthly.getRepayAccountInterest()); // 未还利息
					debtLoanDetail.setRepayAccountYes(BigDecimal.ZERO); // 实还金额
					debtLoanDetail.setRepayInterestYes(BigDecimal.ZERO); // 实还利息
					debtLoanDetail.setRepayCapitalYes(BigDecimal.ZERO); // 实还本金
					debtLoanDetail.setReceiveAccountYes(BigDecimal.ZERO);
					debtLoanDetail.setReceiveCapitalYes(BigDecimal.ZERO);
					debtLoanDetail.setReceiveInterestYes(BigDecimal.ZERO);
					debtLoanDetail.setReceiveAdvanceInterest(BigDecimal.ZERO);
					debtLoanDetail.setReceiveDelayInterest(BigDecimal.ZERO);
					debtLoanDetail.setReceiveLateInterest(BigDecimal.ZERO);
					debtLoanDetail.setReceiveAdvanceInterestYes(BigDecimal.ZERO);
					debtLoanDetail.setReceiveDelayInterestYes(BigDecimal.ZERO);
					debtLoanDetail.setReceiveLateInterestYes(BigDecimal.ZERO);
					debtLoanDetail.setManageFee(monthly.getFee()); // 预还管理费
					debtLoanDetail.setRepayTime(GetterUtil.getString(monthly.getRepayTime())); // 估计还款时间
					debtLoanDetail.setLoanType(TYPE_WAIT); // 等待
					debtLoanDetail.setRepayStatus(0); // 还款状态0未还款1还款中2已还款
					debtLoanDetail.setRepayPeriod(j + 1); // 还款期数
					debtLoanDetail.setWeb(0); // 网站待还
					debtLoanDetail.setAddip(debtInvest.getAddip());
					debtLoanDetail.setCreditAmount(BigDecimal.ZERO);
					debtLoanDetail.setCreditInterestAmount(BigDecimal.ZERO);
					debtLoanDetail.setCreditTime(0);
					debtLoanDetail.setCreateTime(nowTime);
					debtLoanDetail.setCreateUserId(debtInvest.getUserId());
					debtLoanDetail.setCreateUserName(debtInvest.getUserName());
					debtLoanDetail.setSendmail(0);
					boolean debtLoanDetailFlag = this.debtLoanDetailMapper.insertSelective(debtLoanDetail) > 0 ? true
							: false;
					if (!debtLoanDetailFlag) {
						throw new Exception("专属标分期还款计划表(huiyingdai_borrow_recover_plan)写入失败!" + "[出借订单号：" + ordId + "]，"
								+ "[期数：" + j + 1 + "]");
					}
					// 更新总的还款计划表(huiyingdai_borrow_repay_plan)
					isInsert = false;
					DebtRepayDetail debtRepayDetail = getDebtRepayDetail(borrowNid, j + 1);
					if (debtRepayDetail == null) {
						isInsert = true;
						debtRepayDetail = new DebtRepayDetail();
						debtRepayDetail.setLoanStatus(0); // 状态
						debtRepayDetail.setUserId(borrowUserid); // 借款人
						debtRepayDetail.setUserName(borrowUser.getUsername());// 借款人用户名
						debtRepayDetail.setBorrowNid(borrowNid); // 借款订单id
						debtRepayDetail.setRepayNid(nid); // 标识
						debtRepayDetail.setPlanNid(debtInvest.getPlanNid()); // 计划标识
						debtRepayDetail.setManageFee(BigDecimal.ZERO); // 还款费用
						debtRepayDetail.setRepayActionTime(""); // 执行还款的时间
						debtRepayDetail.setRepayStatus(0); // 还款状态
						debtRepayDetail.setRepayPeriod(j + 1); // 还款期数
						debtRepayDetail.setRepayTime(debtLoanDetail.getRepayTime()); // 估计还款时间
						debtRepayDetail.setRepayAccountAll(BigDecimal.ZERO); // 还款总额，加上费用
						debtRepayDetail.setRepayAccount(BigDecimal.ZERO); // 预还金额
						debtRepayDetail.setRepayInterest(BigDecimal.ZERO); // 预还利息
						debtRepayDetail.setRepayCapital(BigDecimal.ZERO); // 预还本金
						debtRepayDetail.setRepayAccountYes(BigDecimal.ZERO); // 实还金额
						debtRepayDetail.setRepayInterestYes(BigDecimal.ZERO); // 实还利息
						debtRepayDetail.setRepayCapitalYes(BigDecimal.ZERO); // 实还本金
						debtRepayDetail.setRepayAccountWait(BigDecimal.ZERO); // 预还金额
						debtRepayDetail.setRepayCapitalWait(BigDecimal.ZERO); // 未还本金
						debtRepayDetail.setRepayInterestWait(BigDecimal.ZERO); // 未还利息
						debtRepayDetail.setAdvanceStatus(0);// 是否提前还款0正常还款1提前还款2延期还款3逾期还款
						debtRepayDetail.setAdvanceDays(0);// 提前还款天数
						debtRepayDetail.setAdvanceInterest(BigDecimal.ZERO);// 提前还款利息
						debtRepayDetail.setLateDays(0); // 逾期天数
						debtRepayDetail.setLateInterest(BigDecimal.ZERO); // 逾期利息
						debtRepayDetail.setDelayDays(0); // 延期天数
						debtRepayDetail.setDelayInterest(BigDecimal.ZERO); // 延期利息
						debtRepayDetail.setDelayRemark(""); // 延期备注
						debtRepayDetail.setAddip(debtInvest.getAddip());
						debtRepayDetail.setCreateTime(nowTime); // 创建时间
						debtRepayDetail.setCreateUserId(debtInvest.getUserId()); // 创建人
						debtRepayDetail.setCreateUserName(debtInvest.getUserName()); // 创建人姓名
					}
					debtRepayDetail.setManageFee(debtRepayDetail.getManageFee().add(debtLoanDetail.getManageFee())); // 还款费用
					debtRepayDetail.setRepayAccount(
							debtRepayDetail.getRepayAccount().add(debtLoanDetail.getRepayAccountWait())); // 预还金额
					debtRepayDetail.setRepayInterest(
							debtRepayDetail.getRepayInterest().add(debtLoanDetail.getRepayInterestWait())); // 预还利息
					debtRepayDetail.setRepayCapital(
							debtRepayDetail.getRepayCapital().add(debtLoanDetail.getRepayCapitalWait())); // 预还本金
					boolean debtRepayDetailFlag = false;
					if (isInsert) {
						debtRepayDetailFlag = this.debtRepayDetailMapper.insertSelective(debtRepayDetail) > 0 ? true
								: false;
					} else {
						debtRepayDetailFlag = this.debtRepayDetailMapper
								.updateByPrimaryKeySelective(debtRepayDetail) > 0 ? true : false;
					}
					if (!debtRepayDetailFlag) {
						throw new Exception("专属标总的还款计划表(huiyingdai_borrow_repay_plan)写入失败!" + "[出借订单号：" + ordId + "]，"
								+ "[期数：" + j + 1 + "]");
					}
					// TODO 分期的hyjf_debt_detail
					DebtDetail debtDetail = new DebtDetail();
					debtDetail.setUserId(debtInvest.getUserId());// 出借人用户ID
					debtDetail.setUserName(debtInvest.getUserName());// 出借人用户名
					debtDetail.setBorrowUserId(borrowUserid);// 借款人用户ID
					debtDetail.setBorrowUserName(borrowUser.getUsername());// 借款人用户名
					debtDetail.setBorrowNid(borrowNid);// 借款编号
					debtDetail.setPlanNid(debtInvest.getPlanNid());// 加入计划编号
					debtDetail.setPlanOrderId(debtInvest.getPlanOrderId());// 加入计划订单号
					debtDetail.setInvestOrderId(debtInvest.getOrderId());// 出借订单号
					debtDetail.setOrderId(debtInvest.getOrderId());// 出借订单号
					debtDetail.setOrderDate(debtInvest.getOrderDate());// 出借订单日期
					debtDetail.setOrderType(0);// 订单类型 0 直投项目出借 1 债权承接
					debtDetail.setSourceType(1);// 是否原始债权 0非原始 1原始
					debtDetail.setAccount(monthly.getRepayAccountCapital());// 出借金额或者债权承接金额
					debtDetail.setLoanCapital(monthly.getRepayAccountCapital()); // 放款本金(应还本金)
					debtDetail.setLoanInterest(monthly.getRepayAccountInterest());// 放款利息(应还利息)
					debtDetail.setRepayPeriod(j + 1);// 还款期数
					debtDetail.setRepayActionTime(0);// 实际还款日期
					debtDetail.setRepayStatus(0);// 还款状态 0未还款 1已还款
					debtDetail.setStatus(1);// 债权是否有效（0失效 1有效）
					debtDetail.setClient(0);
					debtDetail.setRepayTime(monthly.getRepayTime()); // 应还时间
					debtDetail.setRepayInterestWait(monthly.getRepayAccountInterest()); // 待还利息
					debtDetail.setRepayCapitalWait(monthly.getRepayAccountCapital()); // 待还本金
					debtDetail.setManageFee(monthly.getFee()); // 账户管理费
					debtDetail.setCreateTime(GetDate.getNowTime10());// 创建时间
					debtDetail.setLoanTime(GetDate.getNowTime10());// 放款时间
					debtDetail.setCreateUserId(debtInvest.getUserId());// 创建用户ID
					debtDetail.setCreateUserName(debtInvest.getUserName());// 创建用户名
					debtDetail.setBorrowName(borrow.getName());// 原标标题
					debtDetail.setBorrowApr(borrowApr);// 原标年化利率
					debtDetail.setBorrowPeriod(borrowPeriod);// 借款期限
					debtDetail.setBorrowStyle(borrow.getBorrowStyle()); // 借款类型
					debtDetail.setDelayInterest(BigDecimal.ZERO);// 延期利息
					debtDetail.setLateInterest(BigDecimal.ZERO);// 逾期利息
					debtDetail.setDelayInterestAssigned(BigDecimal.ZERO); // 已承接延期利息
					debtDetail.setLateInterestAssigned(BigDecimal.ZERO);// 已承接逾期利息
					boolean debtDetailFlag = debtDetailMapper.insertSelective(debtDetail) > 0 ? true : false;
					if (!debtDetailFlag) {
						throw new Exception(
								"出借人债权记录(hyjf_debt_detail)分期插入失败!第" + (j + 1) + "期[专属标出借订单号：" + ordId + "]");
					}
				}
			}
		} else {
			// TODO 不分期的hyjf_debt_detail
			DebtDetail debtDetail = new DebtDetail();
			debtDetail.setUserId(debtInvest.getUserId());
			debtDetail.setUserName(debtInvest.getUserName());
			debtDetail.setBorrowUserId(borrowUserid);
			debtDetail.setBorrowUserName(borrowUser.getUsername());
			debtDetail.setBorrowNid(borrowNid);
			debtDetail.setPlanNid(debtInvest.getPlanNid());
			debtDetail.setPlanOrderId(debtInvest.getPlanOrderId());
			debtDetail.setInvestOrderId(debtInvest.getOrderId());
			debtDetail.setOrderId(debtInvest.getOrderId());
			debtDetail.setOrderDate(debtInvest.getOrderDate());
			debtDetail.setOrderType(0);
			debtDetail.setSourceType(1);
			debtDetail.setAccount(debtInvest.getAccount());
			debtDetail.setLoanCapital(capitalTender);
			debtDetail.setLoanInterest(interestTender);
			debtDetail.setRepayPeriod(1);
			debtDetail.setRepayActionTime(0);
			debtDetail.setRepayStatus(0);
			debtDetail.setStatus(1);
			debtDetail.setClient(0);
			debtDetail.setRepayTime(recoverTime);
			debtDetail.setRepayInterestWait(interestTender); // 未还利息
			debtDetail.setRepayCapitalWait(capitalTender); // 未还本金
			debtDetail.setManageFee(recoverFee); // 账户管理费
			debtDetail.setCreateTime(GetDate.getNowTime10());
			debtDetail.setLoanTime(GetDate.getNowTime10());
			debtDetail.setCreateUserId(debtInvest.getUserId());
			debtDetail.setCreateUserName(debtInvest.getUserName());
			debtDetail.setBorrowName(borrow.getName());
			debtDetail.setBorrowApr(borrowApr);
			debtDetail.setBorrowPeriod(borrowPeriod);
			debtDetail.setBorrowStyle(borrow.getBorrowStyle());
			debtDetail.setDelayInterest(BigDecimal.ZERO);
			debtDetail.setLateInterest(BigDecimal.ZERO);
			debtDetail.setDelayInterestAssigned(BigDecimal.ZERO);
			debtDetail.setLateInterestAssigned(BigDecimal.ZERO);
			boolean debtDetailFlag = debtDetailMapper.insertSelective(debtDetail) > 0 ? true : false;
			if (!debtDetailFlag) {
				throw new Exception("出借人债权记录(hyjf_debt_detail)插入失败!" + "[专属标出借订单号：" + ordId + "]");
			}
		}
		// 更新账户信息(出借人)
		Account account = new Account();
		account.setUserId(debtInvest.getUserId());
		// 出借人冻结金额 -
		// 出借金额(等额本金时出借金额可能会大于计算出的本金之和)
		account.setPlanFrost(tenderAccount);
		account.setPlanAccedeFrost(tenderAccount);
		boolean investaccountFlag = this.adminAccountCustomizeMapper.updateOfDebtInvest(account) > 0 ? true : false;
		if (!investaccountFlag) {
			throw new Exception("出借人资金记录(huiyingdai_account)更新失败!" + "[专属标出借订单号：" + ordId + "]");
		}

		// 更新hyjf_debt_plan
		DebtPlanWithBLOBs newDebtPlan = new DebtPlanWithBLOBs();
		newDebtPlan.setDebtPlanFrost(debtPlan.getDebtPlanFrost().subtract(tenderAccount));
		boolean debtPlanFlag = debtPlanMapper.updateByExampleSelective(newDebtPlan, examplePlan) > 0 ? true : false;
		if (!debtPlanFlag) {
			throw new Exception("计划表(hyjf_debt_plan)更新失败!" + "[出借订单号：" + ordId + "]");
		}
		// 更新hyjf_debt_plan_accede
		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria cri = example.createCriteria();
		cri.andAccedeOrderIdEqualTo(debtInvest.getPlanOrderId());
		cri.andPlanNidEqualTo(debtInvest.getPlanNid());
		List<DebtPlanAccede> debtPlanAccedeList = debtPlanAccedeMapper.selectByExample(example);
		if (debtPlanAccedeList == null) {
			throw new Exception("计划加入表(hyjf_debt_plan_accede)查询失败!" + "[出借订单号：" + ordId + "]");
		} else if (debtPlanAccedeList.size() <= 0) {
			throw new Exception("计划加入表(hyjf_debt_plan_accede)查询失败!" + "[出借订单号：" + ordId + "]");
		}
		DebtPlanAccede planAccede = debtPlanAccedeList.get(0);
		DebtPlanAccede newDebtPlanAccede = new DebtPlanAccede();
		newDebtPlanAccede.setId(planAccede.getId());
		newDebtPlanAccede.setAccedeFrost(tenderAccount);
		boolean debtPlanAccedeFlag = this.batchDebtPlanAccedeCustomizeMapper
				.updateDebtPlanAccedeLoans(newDebtPlanAccede) > 0 ? true : false;
		if (!debtPlanAccedeFlag) {
			throw new Exception("计划加入表(hyjf_debt_plan_accede)更新失败!" + "[出借订单号：" + ordId + "]");
		}
		// 取得账户信息(出借人)
		account = this.getAccountByUserId(debtInvest.getUserId());
		if (account == null) {
			throw new Exception("出借人账户信息不存在。[专属标出借人ID：" + debtInvest.getUserId() + "]，" + "[出借订单号：" + ordId + "]");
		}
		planAccede = selectDebtPlanAccede(planAccede.getId());
		// 写入收支明细
		DebtAccountList debtAccountList = new DebtAccountList();
		debtAccountList.setNid(ordId); // 出借订单号
		debtAccountList.setUserId(debtInvest.getUserId()); // 出借人
		debtAccountList.setUserName(debtInvest.getUserName()); // 出借人
		debtAccountList.setPlanNid(debtInvest.getPlanNid());// 计划编号
		debtAccountList.setPlanOrderId(debtInvest.getPlanOrderId());// 计划订单号
		debtAccountList.setTotal(account.getTotal());
		debtAccountList.setBalance(account.getBalance());
		debtAccountList.setFrost(account.getFrost());
		debtAccountList.setAccountWait(account.getAwait());
		debtAccountList.setRepayWait(account.getRepay());
		debtAccountList.setInterestWait(BigDecimal.ZERO);
		debtAccountList.setCapitalWait(BigDecimal.ZERO);
		debtAccountList.setPlanBalance(account.getPlanBalance());// 计划余额
		debtAccountList.setPlanFrost(account.getPlanFrost());// 计划冻结
		debtAccountList.setPlanOrderBalance(planAccede.getAccedeBalance());// 计划冻结
		debtAccountList.setPlanOrderFrost(planAccede.getAccedeFrost());
		debtAccountList.setAmount(tenderAccount); // 出借本金
		debtAccountList.setType(2); // 2支出
		debtAccountList.setTrade("plan_loan"); // 放款
		debtAccountList.setTradeCode("frost"); // 余额操作
		debtAccountList.setCreateTime(nowTime); // 创建时间
		debtAccountList.setCreateUserId(debtInvest.getUserId());
		debtAccountList.setCreateUserName(CustomConstants.OPERATOR_AUTO_LOANS);// 操作者
		debtAccountList.setIp(borrow.getAddip()); // 操作IP
		debtAccountList.setRemark(debtInvest.getPlanOrderId());
		debtAccountList.setWeb(0); // PC
		// 用户属性 0=>无主单 1=>有主单
		// 2=>线下员工 3=>线上员工
		Integer attribute = null;
		UsersInfo userInfo = getUsersInfoByUserId(debtInvest.getUserId());
		if (Validator.isNotNull(userInfo)) {
			// 获取出借用户的用户属性
			attribute = userInfo.getAttribute();
			if (Validator.isNotNull(attribute)) {
				// 如果是线上员工或线下员工，推荐人的userId和username不插
				if (attribute == 1) {
					SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
					SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
					spreadsUsersExampleCriteria.andUserIdEqualTo(debtInvest.getUserId());
					List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
					if (sList != null && sList.size() == 1) {
						int refUserId = sList.get(0).getSpreadsUserid();
						// 查找用户推荐人
						Users refererUser = getUsersByUserId(refUserId);
						if (Validator.isNotNull(refererUser)) {
							debtAccountList.setRefererUserId(refererUser.getUserId());
							debtAccountList.setRefererUserName(refererUser.getUsername());
						}
					}
				} else if (attribute == 0) {
					SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
					SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
					spreadsUsersExampleCriteria.andUserIdEqualTo(debtInvest.getUserId());
					List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
					if (sList != null && sList.size() == 1) {
						int refUserId = sList.get(0).getSpreadsUserid();
						// 查找推荐人
						Users refererUser = getUsersByUserId(refUserId);
						if (Validator.isNotNull(refererUser)) {
							debtAccountList.setRefererUserId(refererUser.getUserId());
							debtAccountList.setRefererUserName(refererUser.getUsername());
						}
					}
				}
			}
		}
		boolean debtAccountListFlag = insertDebtAccountList(debtAccountList) > 0 ? true : false;
		if (!debtAccountListFlag) {
			throw new Exception("出借人收支明细(hyjf_debt_account_list)写入失败!" + "[出借订单号：" + ordId + "]");
		}
		// 更新借款人账户表(原复审业务)
		Account newBorrowAccount = new Account();
		newBorrowAccount.setUserId(borrowUserid);
		newBorrowAccount.setTotal(tenderAccount.subtract(userFee));// 累加到账户总资产
		newBorrowAccount.setBalance(tenderAccount.subtract(userFee)); // 累加到可用余额
		newBorrowAccount.setIncome(tenderAccount.subtract(userFee)); // 累加到总收入
		newBorrowAccount.setRepay(accountTender); // 待还金额
		boolean borrowAccountFlag = this.adminAccountCustomizeMapper.updateOfLoansBorrow(newBorrowAccount) > 0 ? true
				: false;
		if (!borrowAccountFlag) {
			throw new Exception("借款人资金记录(huiyingdai_account)更新失败!" + "[出借订单号：" + ordId + "]");
		}
		// 服务费大于0时,插入网站收支明细
		if (userFee.compareTo(BigDecimal.ZERO) > 0) {
			// 插入网站收支明细记录
			AccountWebList accountWebList = new AccountWebList();
			accountWebList.setOrdid(ordId);// 订单号
			accountWebList.setBorrowNid(borrowNid); // 出借编号
			accountWebList.setUserId(debtInvest.getUserId()); // 出借者
			accountWebList.setAmount(userFee); // 服务费
			accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入 2支出
			accountWebList.setTrade(CustomConstants.TRADE_LOANFEE); // 服务费
			accountWebList.setTradeType(CustomConstants.TRADE_LOANFEE_NM); // 服务费
			accountWebList.setCreateTime(debtLoan.getCreateTime());
			accountWebList.setRemark(borrowNid);
			boolean accountWebListFlag = insertAccountWebList(accountWebList) > 0 ? true : false;
			if (!accountWebListFlag) {
				throw new Exception("专属标放款网站收支记录(huiyingdai_account_web_list)更新失败!" + "[出借订单号：" + ordId + "]");
			}
		}
		// 放款更新成功
		List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper
				.selectByExample(new CalculateInvestInterestExample());
		if (calculates != null && calculates.size() > 0) {
			CalculateInvestInterest calculateNew = new CalculateInvestInterest();
			calculateNew.setInterestSum(debtLoan.getRepayInterestWait());
			calculateNew.setId(calculates.get(0).getId());
			this.webCalculateInvestInterestCustomizeMapper.updateCalculateInvestByPrimaryKey(calculateNew);
		}
		msg.put(VAL_USERID, debtLoan.getUserId().toString());
		msg.put(VAL_TITLE, debtLoan.getBorrowNid());
		msg.put(VAL_AMOUNT, accountTender.toString());
		msg.put(VAL_BALANCE, capitalTender.toString());
		msg.put(VAL_PROFIT, interestTender.toString());
		msg.put(PARAM_BORROWRECOVERID, debtLoan.getId().toString());
		msg.put(VAL_ORDER_ID, ordId);
		msg.put(VAL_LOAN_TIME, GetDate.getDate("yyyy-MM-dd HH:mm:ss"));
		System.out
				.println("-----------专属标放款结束---" + apicron.getBorrowNid() + "---------" + debtInvest.getLoanOrderId());
		return retMsgList;
	}

	/**
	 * @param freezeList
	 * @return
	 */

	private int updateDebtFreeze(DebtFreeze freezeList) {
		freezeList.setStatus(1);
		return this.debtFreezeMapper.updateByPrimaryKeySelective(freezeList);

	}

	/**
	 * 更新放款完成状态
	 *
	 * @param borrowNid
	 * @param periodNow
	 */
	public void updateBorrowStatus(String nid, String borrowNid, Integer borrowUserId) {
		// 当前时间
		int nowTime = GetDate.getNowTime10();

		// 查询tender
		DebtInvestExample tenderExample = new DebtInvestExample();
		tenderExample.createCriteria().andBorrowNidEqualTo(borrowNid).andStatusEqualTo(0);
		int tenderCnt = this.debtInvestMapper.countByExample(tenderExample);

		DebtRepay newBorrowRepay = new DebtRepay();
		DebtRepayDetail newBorrowRepayPlan = new DebtRepayDetail();
		DebtBorrowWithBLOBs newBrrow = new DebtBorrowWithBLOBs();
		if (tenderCnt == 0) {
			// 更新BorrowRepay
			newBorrowRepay.setLoanStatus(1); // 已放款
			DebtRepayExample repayExample = new DebtRepayExample();
			repayExample.createCriteria().andBorrowNidEqualTo(borrowNid);
			this.debtRepayMapper.updateByExampleSelective(newBorrowRepay, repayExample);

			// 更新BorrowRepayPlan
			newBorrowRepayPlan.setLoanStatus(1); // 已放款
			DebtRepayDetailExample repayPlanExample = new DebtRepayDetailExample();
			repayPlanExample.createCriteria().andBorrowNidEqualTo(borrowNid);
			this.debtRepayDetailMapper.updateByExampleSelective(newBorrowRepayPlan, repayPlanExample);

			// 更新Borrow
			newBrrow.setRecoverLastTime(GetDate.getMyTimeInMillis()); // 最后一笔放款时间
			DebtBorrowExample borrowExample = new DebtBorrowExample();
			borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
			this.debtBorrowMapper.updateByExampleSelective(newBrrow, borrowExample);

			// 放款总信息表
			DebtLoanLog accountBorrow = getAccountBorrow(borrowNid);
			if (accountBorrow != null) {
				BigDecimal amount = accountBorrow.getBalance();
				// 取得借款人账户信息
				Account borrowAccount = getAccountByUserId(borrowUserId);
				// 插入借款人的收支明细表(原复审业务)
				AccountList accountListRecord = new AccountList();
				accountListRecord.setNid(nid); // 交易凭证号生成规则BorrowNid_userid_期数
				accountListRecord.setUserId(borrowUserId); // 借款人id
				accountListRecord.setAmount(amount); // 操作金额
				accountListRecord.setType(1); // 收支类型1收入2支出3冻结
				accountListRecord.setTrade("borrow_success"); // 交易类型
				accountListRecord.setTradeCode("balance"); // 操作识别码
				accountListRecord.setTotal(borrowAccount.getTotal()); // 资金总额
				accountListRecord.setBalance(borrowAccount.getBalance()); // 可用金额
				accountListRecord.setFrost(borrowAccount.getFrost()); // 冻结金额
				accountListRecord.setAwait(borrowAccount.getAwait()); // 待收金额
				accountListRecord.setRepay(borrowAccount.getRepay()); // 待还金额
				accountListRecord.setCreateTime(nowTime); // 创建时间
				accountListRecord.setOperator(CustomConstants.OPERATOR_AUTO_LOANS); // 操作员
				accountListRecord.setRemark(borrowNid);
				accountListRecord.setIp(""); // 操作IP
				accountListRecord.setBaseUpdate(0);
				this.accountListMapper.insertSelective(accountListRecord);
			}
		}
	}

	/**
	 * 自动扣款（放款）(调用汇付天下接口)
	 *
	 * @param outCustId
	 * @return
	 */
	private ChinapnrBean loans(String trxId, Integer borrowUserId, String outCustId, String transAmt, String fee,
			String ordId, String ordDate, String subOrdId, String subOrdDate, String inCustId, String divDetails,
			String reqExt) {
		String methodName = "loans";

		// 调用汇付接口(自动扣款（放款）)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_20); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_LOANS); // 消息类型(必须)
		bean.setOrdId(ordId); // 订单号(必须)
		bean.setOrdDate(ordDate); // 订单日期(必须)
		bean.setOutCustId(outCustId); // 出账客户号(必须)
		bean.setTransAmt(CustomUtil.formatAmount(transAmt)); // 交易金额(必须)
		bean.setFee(CustomUtil.formatAmount(fee)); // 扣款手续费(必须)
		bean.setSubOrdId(subOrdId); // 订单号(必须)
		bean.setSubOrdDate(subOrdDate); // 订单日期(必须)
		bean.setInCustId(inCustId); // 入账客户号(必须)
		bean.setDivDetails(divDetails); // 分账账户串(必须)
		bean.setFeeObjFlag("I");// 放款服务费收取方（借款人）
		bean.setIsDefault("N"); // 是否默认(必须)
		bean.setIsUnFreeze("Y");// 是否解冻
		bean.setUnFreezeOrdId(ordId);// 解冻订单号
		bean.setFreezeTrxId(trxId);// 冻结标识
		bean.setReqExt(reqExt);// 入参扩展域
		bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)

		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("自动扣款（放款）"); // 备注
		bean.setLogClient("0"); // PC

		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);

		if (chinapnrBean == null) {
			LogUtil.errorLog(THIS_CLASS, methodName, new Exception("调用自动扣款（放款）接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}

		return chinapnrBean;
	}

	/**
	 * 交易状态查询(调用汇付天下接口)
	 *
	 * @return
	 */
	private ChinapnrBean queryTransStat(String ordId, String ordDate, String queryTransType) {
		String methodName = "queryTransStat";

		// 调用汇付接口(交易状态查询)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERYTRANSSTAT); // 消息类型(必须)
		bean.setOrdId(ordId); // 订单号(必须)
		bean.setOrdDate(ordDate); // 订单日期(必须)
		bean.setQueryTransType(queryTransType); // 交易查询类型

		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("交易状态查询"); // 备注
		bean.setLogClient("0"); // PC

		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean == null) {
			LogUtil.errorLog(THIS_CLASS, methodName, new Exception("调用交易状态查询接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}

		return chinapnrBean;
	}

	/**
	 * 取得服务费（服务费不四舍五入） 去尾
	 *
	 * @param serviceFee
	 *            服务费率
	 * @param account
	 *            金额
	 * @param borrowStyle
	 *            还款类型
	 * @param borrowPeriod
	 *            期数
	 * @return
	 */
	private BigDecimal getUserFee(BigDecimal serviceFee, BigDecimal account, String borrowStyle, Integer borrowPeriod) {
		BigDecimal userFee = BigDecimal.ZERO;

		// 计算放款金额
		if (serviceFee == null || account == null) {
			return userFee;
		}
		// 按天计息时,服务费乘以天数
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
			userFee = serviceFee.multiply(account).multiply(new BigDecimal(borrowPeriod)).setScale(2,
					BigDecimal.ROUND_DOWN);
		} else {
			userFee = serviceFee.multiply(account).setScale(2, BigDecimal.ROUND_DOWN);
		}

		return userFee;
	}

	/**
	 * 取得借款API任务表
	 *
	 * @return
	 */
	public List<DebtApicron> getDebtApicronList(Integer status, Integer apiType) {
		DebtApicronExample example = new DebtApicronExample();
		DebtApicronExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(status);
		criteria.andApiTypeEqualTo(apiType);
		example.setOrderByClause(" id asc ");
		List<DebtApicron> list = this.debtApicronMapper.selectByExample(example);

		return list;
	}

	/**
	 * 取得借款API任务表
	 *
	 * @return
	 */
	public List<DebtApicron> getDebtApicronListLate(Integer status, Integer apiType) {
		DebtApicronExample example = new DebtApicronExample();
		DebtApicronExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(status);
		criteria.andApiTypeEqualTo(apiType);
		criteria.andUpdateTimeLessThan(GetDate.getNowTime10() - 3600);
		example.setOrderByClause(" id asc ");
		List<DebtApicron> list = this.debtApicronMapper.selectByExample(example);

		return list;
	}

	/**
	 * 取得借款API任务表
	 *
	 * @return
	 */
	public List<DebtApicron> getDebtApicronListWithRepayStatus(Integer status, Integer apiType) {
		DebtApicronExample example = new DebtApicronExample();
		DebtApicronExample.Criteria criteria = example.createCriteria();
		criteria.andRepayStatusEqualTo(status);
		criteria.andApiTypeEqualTo(apiType);
		example.setOrderByClause(" id asc ");
		List<DebtApicron> list = this.debtApicronMapper.selectByExample(example);

		return list;
	}

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	public int updateDebtApicron(Integer id, Integer status) {
		return updateDebtApicron(id, status, null);
	}

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	public int updateDebtApicronOfRepayStatus(Integer id, Integer status) {
		DebtApicron record = new DebtApicron();
		record.setId(id);
		record.setRepayStatus(status);
		record.setUpdateTime(GetDate.getMyTimeInMillis());
		return this.debtApicronMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @param data
	 * @return
	 */
	public int updateDebtApicron(Integer id, Integer status, String data) {
		DebtApicron record = new DebtApicron();
		record.setId(id);
		record.setStatus(status);
		if (Validator.isNotNull(data) || status == 1) {
			record.setData(data);
		}
		if (record.getWebStatus() == null) {
			record.setWebStatus(0);
		}
		record.setUpdateTime(GetDate.getMyTimeInMillis());
		return this.debtApicronMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 取得标的详情
	 *
	 * @return
	 */
	public DebtBorrowWithBLOBs getBorrow(String borrowNid) {
		DebtBorrowExample example = new DebtBorrowExample();
		DebtBorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<DebtBorrowWithBLOBs> list = this.debtBorrowMapper.selectByExampleWithBLOBs(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得借款信息
	 *
	 * @return
	 */
	public DebtRepay getDebtRepay(String borrowNid) {
		DebtRepayExample example = new DebtRepayExample();
		DebtRepayExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<DebtRepay> list = this.debtRepayMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得借款计划信息
	 *
	 * @return
	 */
	public DebtRepayDetail getDebtRepayDetail(String borrowNid, Integer period) {
		DebtRepayDetailExample example = new DebtRepayDetailExample();
		DebtRepayDetailExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andRepayPeriodEqualTo(period);
		List<DebtRepayDetail> list = this.debtRepayDetailMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得满标日志
	 *
	 * @return
	 */
	public DebtLoanLog getAccountBorrow(String borrowNid) {
		DebtLoanLogExample example = new DebtLoanLogExample();
		DebtLoanLogExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<DebtLoanLog> list = this.debtLoanLogMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取出冻结订单
	 *
	 * @return
	 */
	private DebtFreeze getFreezeList(String ordId) {
		DebtFreezeExample example = new DebtFreezeExample();
		DebtFreezeExample.Criteria criteria = example.createCriteria();
		criteria.andOrderIdEqualTo(ordId);
		criteria.andDelFlagEqualTo(0);
		criteria.andStatusEqualTo(0);
		List<DebtFreeze> list = this.debtFreezeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取出账户信息
	 *
	 * @param userId
	 * @return
	 */
	public Account getAccountByUserId(Integer userId) {
		AccountExample accountExample = new AccountExample();
		accountExample.createCriteria().andUserIdEqualTo(userId);
		List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	/**
	 * 写入收支明细
	 *
	 * @param accountList
	 * @return
	 */
	private int insertDebtAccountList(DebtAccountList debtAccountList) {
		// 写入收支明细
		return this.debtAccountListMapper.insertSelective(debtAccountList);
	}

	/**
	 * 更新放款状态
	 *
	 * @param accountList
	 * @return
	 */
	public int updateBorrowTender(BorrowTender borrowTender) {
		return borrowTenderMapper.updateByPrimaryKeySelective(borrowTender);
	}

	/**
	 * 更新放款状态(优惠券)
	 *
	 * @param accountList
	 * @return
	 */
	public int updateBorrowTenderCpn(BorrowTenderCpn borrowTenderCpn) {
		return borrowTenderCpnMapper.updateByPrimaryKeySelective(borrowTenderCpn);
	}

	/**
	 * 写入还款明细
	 *
	 * @param accountList
	 * @return
	 */
	private int insertDebtLoan(DebtLoan debtLoan) {
		return debtLoanMapper.insertSelective(debtLoan);
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
	 * 插入网站收支记录
	 *
	 * @param nid
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
	 * 借款列表
	 * 
	 * @return
	 */
	public List<DebtBorrowCustomize> selectBorrowList(DebtBorrowCommonCustomize borrowCommonCustomize) {
		return this.debtBorrowCustomizeMapper.selectBorrowList(borrowCommonCustomize);
	}

	public List<WebUserInvestListCustomize> selectUserInvestList(String borrowNid, String userId, String nid,
			int limitStart, int limitEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", borrowNid);
		params.put("userId", userId);
		params.put("nid", nid);
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
		List<WebUserInvestListCustomize> list = webUserInvestListCustomizeMapper.selectUserDebtInvestList(params);
		return list;
	}

	public int countProjectRepayPlanRecordTotal(String borrowNid, String userId, String nid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		int total = webUserInvestListCustomizeMapper.countDebtLoanDetailRecordTotal(params);
		return total;
	}

	public List<WebProjectRepayListCustomize> selectProjectRepayPlanList(String borrowNid, String userId, String nid,
			int offset, int limit) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		List<WebProjectRepayListCustomize> projectRepayList = webUserInvestListCustomizeMapper
				.selectDebtLoanDetailList(params);
		return projectRepayList;
	}

	/**
	 * 发送短信(出借成功)
	 *
	 * @param userId
	 */
	public void sendSms(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))
						&& new BigDecimal(msg.get(VAL_AMOUNT)).compareTo(BigDecimal.ZERO) > 0) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null || Validator.isNull(users.getMobile())
							|| (users.getInvestSms() != null && users.getInvestSms() == 1)) {
						return;
					}
					System.err.println("userid=" + msg.get(VAL_USERID) + ";专属标开始发送短信,发送金额" + msg.get(VAL_AMOUNT));
					SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null,
							MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_TOUZI_SUCCESS,
							CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				}
			}
		}
	}

	/**
	 * 发送短信(优惠券出借成功)
	 *
	 * @param userId
	 */
	@Override
	public void sendSmsCoupon(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))
						&& new BigDecimal(msg.get(VAL_AMOUNT)).compareTo(BigDecimal.ZERO) > 0) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null || Validator.isNull(users.getMobile())
							|| (users.getInvestSms() != null && users.getInvestSms() == 1)) {
						return;
					}
					SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null,
							MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_COUPON_TENDER,
							CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				}
			}
		}
	}

	/**
	 * 发送邮件(出借成功)
	 *
	 * @param userId
	 */
	public void sendMail(List<Map<String, String>> msgList, String borrowNid) {
		if (msgList != null && msgList.size() > 0 && Validator.isNotNull(borrowNid)) {
			for (Map<String, String> msg : msgList) {
				try {
					// 向每个出借人发送邮件
					if (Validator.isNotNull(msg.get(VAL_USERID)) && NumberUtils.isNumber(msg.get(VAL_USERID))) {
						String userId = msg.get(VAL_USERID);
						String orderId = msg.get(VAL_ORDER_ID);
						Users users = getUsersByUserId(Integer.valueOf(userId));
						if (users == null || Validator.isNull(users.getEmail())) {
							return;
						}
						String email = users.getEmail();
						msg.put(VAL_NAME, users.getUsername());
						UsersInfo usersInfo = this.getUsersInfoByUserId(Integer.valueOf(userId));
						if (Validator.isNotNull(usersInfo) && Validator.isNotNull(usersInfo.getSex())) {
							if (usersInfo.getSex() % 2 == 0) {
								msg.put(VAL_SEX, "女士");
							} else {
								msg.put(VAL_SEX, "先生");
							}
						}
						String fileName = borrowNid + "_" + orderId + ".pdf";
						String filePath = PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "BorrowLoans_"
								+ GetDate.getMillis() + StringPool.FORWARD_SLASH;
						// 查询借款人用户名
						DebtBorrowCommonCustomize borrowCommonCustomize = new DebtBorrowCommonCustomize();
						// 借款编码
						borrowCommonCustomize.setBorrowNidSrch(borrowNid);
						List<DebtBorrowCustomize> recordList = this.selectBorrowList(borrowCommonCustomize);
						if (recordList != null && recordList.size() == 1) {
							Map<String, Object> contents = new HashMap<String, Object>();
							contents.put("borrowNid", borrowNid);
							contents.put("nid", orderId);
							// 借款人用户名
							contents.put("borrowUsername", recordList.get(0).getUsername().substring(0,1)+"**");
							contents.put("record", recordList.get(0));
							// 本笔的放款完成时间 (协议签订日期)
							contents.put("recoverTime", msg.get(VAL_LOAN_TIME));
							// 用户出借列表
							List<WebUserInvestListCustomize> userInvestList = this.selectUserInvestList(borrowNid,
									userId, orderId, -1, -1);
							if (userInvestList != null && userInvestList.size() > 0) {
		                        WebUserInvestListCustomize userInvest = userInvestList.get(0);
	                            /*userInvest.setRealName(userInvest.getRealName().substring(0,1)+"**");
	                            userInvest.setUsername(userInvest.getUsername().substring(0,1)+"*****");
	                            userInvest.setIdCard(userInvest.getIdCard().substring(0,4)+"**************");*/
		                        contents.put("userInvest", userInvest);
		                    }else {
								System.out
										.println("专属标的出借信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。出借订单号:" + orderId);
								return;
							}
							// 如果是分期还款，查询分期信息
							String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
							if (borrowStyle != null) {
							  //计算预期收益
                                BigDecimal earnings = new BigDecimal("0");
                                // 收益率
                                
                                String borrowAprString = StringUtils.isEmpty(recordList.get(0).getBorrowApr())?"0.00":recordList.get(0).getBorrowApr().replace("%", "");
                                BigDecimal borrowApr = new BigDecimal(borrowAprString);
                                //出借金额
                                String accountString = StringUtils.isEmpty(recordList.get(0).getAccount())?"0.00":recordList.get(0).getAccount().replace(",", "");
                                BigDecimal account = new BigDecimal(accountString);
                               // 周期
                                String borrowPeriodString = StringUtils.isEmpty(recordList.get(0).getBorrowPeriod())?"0":recordList.get(0).getBorrowPeriod();
                                String regEx="[^0-9]";   
                                Pattern p = Pattern.compile(regEx);   
                                Matcher m = p.matcher(borrowPeriodString); 
                                borrowPeriodString = m.replaceAll("").trim();
                                Integer borrowPeriod = Integer.valueOf(borrowPeriodString);
                                if (StringUtils.equals("endday", borrowStyle)){
                                    // 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*年化收益÷365*锁定期；
                                    earnings = DuePrincipalAndInterestUtils.getDayInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
                                } else {
                                    // 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
                                    earnings = DuePrincipalAndInterestUtils.getMonthInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);

                                }
                                contents.put("earnings", earnings);
								if ("month".equals(borrowStyle) || "principal".equals(borrowStyle)
										|| "endmonth".equals(borrowStyle)) {
									int recordTotal = this.countProjectRepayPlanRecordTotal(borrowNid, userId, orderId);
									if (recordTotal > 0) {
										Paginator paginator = new Paginator(1, recordTotal);
										List<WebProjectRepayListCustomize> repayList = this.selectProjectRepayPlanList(
												borrowNid, userId, orderId, paginator.getOffset(),
												paginator.getLimit());
										contents.put("paginator", paginator);
										contents.put("repayList", repayList);
									} else {
										Paginator paginator = new Paginator(1, recordTotal);
										contents.put("paginator", paginator);
										contents.put("repayList", "");
									}
								}
							}
							String pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.TENDER_CONTRACT,
									contents);
							if (StringUtils.isNotEmpty(pdfUrl)) {
								File path = new File(filePath);
								if (!path.exists()) {
									path.mkdirs();
								}
								FileUtil.getRemoteFile(pdfUrl.substring(0, pdfUrl.length() - 1), filePath + fileName);
							}
							String[] emails = { email };
							MailMessage message = new MailMessage(Integer.valueOf(userId), msg, "汇盈金服互联网金融服务平台居间服务协议",
									null, new String[] { filePath + fileName }, emails,
									CustomConstants.EMAILPARAM_TPL_LOANS, MessageDefine.MAILSENDFORMAILINGADDRESS);
							mailMessageProcesser.gather(message);
							// 更新DebtLoan邮件发送状态
							String borrowRecoverId = msg.get(PARAM_BORROWRECOVERID);
							if (Validator.isNotNull(borrowRecoverId) && NumberUtils.isNumber(borrowRecoverId)) {
								DebtLoan debtLoan = new DebtLoan();
								debtLoan.setId(Integer.valueOf(borrowRecoverId));
								debtLoan.setSendmail(1);
								this.debtLoanMapper.updateByPrimaryKeySelective(debtLoan);
							}
						} else {
							System.out.println("标的信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
							return;
						}
					}
				} catch (Exception e) {
					LogUtil.errorLog(THIS_CLASS, "sendMail", e);
				}
			}
		}
	}

	/**
	 * 
	 * App消息推送（优惠券出借成功）
	 * 
	 * @author hsy
	 * @param msgList
	 */
	@Override
	public void sendAppMSCoupon(List<Map<String, String>> msgList) {
		System.out.println("msgList size: " + msgList.size());
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_PROFIT))
						&& new BigDecimal(msg.get(VAL_PROFIT)).compareTo(BigDecimal.ZERO) > 0) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null) {
						System.out.println("不满足发送push消息条件，推送失败");
						return;
					}
					System.out.println("开始调用推送消息接口");
					AppMsMessage appMsMessage = new AppMsMessage(users.getUserId(), msg, null,
							MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_COUPON_TENDER);
					appMsProcesser.gather(appMsMessage);
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
				if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))
						&& new BigDecimal(msg.get(VAL_AMOUNT)).compareTo(BigDecimal.ZERO) > 0) {
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
						AppMsMessage smsMessage = new AppMsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null,
								MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_TOUZI_SUCCESS);
						appMsProcesser.gather(smsMessage);
					}
				}
			}
		}
	}

	// 新做++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * 取得借款列表
	 *
	 * @return
	 */
	@Override
	public List<DebtInvest> getDebtInvestList(String borrowNid) {
		DebtInvestExample example = new DebtInvestExample();
		DebtInvestExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andStatusEqualTo(0);
		example.setOrderByClause(" id asc ");
		List<DebtInvest> list = this.debtInvestMapper.selectByExample(example);
		return list;
	}

	/**
	 * 更新放款状态
	 *
	 * @param accountList
	 * @return
	 */
	public int updateDebtInvest(DebtInvest debtInvest) {
		return debtInvestMapper.updateByPrimaryKeySelective(debtInvest);
	}

	/**
	 * 根据相应的计划加入订单记录id获取计划加入记录
	 * 
	 * @param id
	 * @return
	 * @author Administrator
	 */

	@Override
	public DebtPlanAccede selectDebtPlanAccede(Integer id) {
		DebtPlanAccede debtPlanAccede = this.debtPlanAccedeMapper.selectByPrimaryKey(id);
		return debtPlanAccede;

	}

}

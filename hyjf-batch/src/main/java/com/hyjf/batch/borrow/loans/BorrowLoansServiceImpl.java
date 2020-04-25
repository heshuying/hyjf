package com.hyjf.batch.borrow.loans;

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
import com.hyjf.mybatis.model.auto.AccountBorrow;
import com.hyjf.mybatis.model.auto.AccountBorrowExample;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.CouponRecover;
import com.hyjf.mybatis.model.auto.CouponRecoverExample;
import com.hyjf.mybatis.model.auto.FreezeList;
import com.hyjf.mybatis.model.auto.FreezeListExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
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
public class BorrowLoansServiceImpl extends BaseServiceImpl implements BorrowLoansService {

	private static final String THIS_CLASS = BorrowLoansServiceImpl.class.getName();

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

	/** 优惠券出借 */
	private static final String COUPON_TYPE = "coupon_type";

	/** 优惠券出借订单编号 */
	private static final String TENDER_NID = "tender_nid";

	/** 还款时间 */
	private static final String VAL_RECOVERTIME = "val_recovertime";

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
	public List<Map<String, String>> updateBorrowLoans(BorrowApicron apicron, BorrowTender borrowTender)
			throws Exception {

		String methodName = "updateBorrowLoans";
		System.out.println("-----------放款开始---" + apicron.getBorrowNid() + "---------" + borrowTender.getLoanOrdid());
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
		/** 标的基本数据 */
		// 取得标的详情
		BorrowWithBLOBs borrow = getBorrow(borrowNid);
		// 标的是否进行银行托管
		int bankInputFlag = Validator.isNull(borrow.getBankInputFlag()) ? 0 : borrow.getBankInputFlag();
		Map<String, String> msg = new HashMap<String, String>();
		retMsgList.add(msg);
		// 借款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 服务费率
		BigDecimal serviceFee = Validator.isNull(borrow.getServiceFeeRate()) ? BigDecimal.ZERO : new BigDecimal(
				borrow.getServiceFeeRate());
		// 年利率
		BigDecimal borrowApr = borrow.getBorrowApr();
		// 月利率(算出管理费用[上限])
		BigDecimal borrowMonthRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(
				borrow.getManageFeeRate());
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
		// 出借人用户ID
		Integer outUserId = null;
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
		String ordId = borrowTender.getNid();
		// 取出冻结订单信息
		FreezeList freezeList = getFreezeList(ordId);
		if (freezeList != null) {
			// 本平台交易唯一标识(解冻订单用)
			String trxId = freezeList.getTrxid();
			/** 写入或更新数据库(写入或更新0条时,抛出异常) */
			// 若此笔订单已经解冻
			if (freezeList.getStatus() == 1) {
				return retMsgList;
			}
			/** 自动放款处理开始 */
			// 借款人的账户信息
			inCust = super.getChinapnrUserInfo(borrowUserid);
			if (inCust == null) {
				throw new Exception("借款人未开户。[借款人ID：" + borrowUserid + "]，" + "[出借订单号：" + ordId + "]");
			}
			inCustId = inCust.getChinapnrUsrcustid();
			inUserId = inCust.getUserId();
			// 出借人的账户信息
			outCust = super.getChinapnrUserInfo(borrowTender.getUserId());
			if (outCust == null) {
				throw new Exception("出借人未开户。[出借人ID：" + borrowTender.getUserId() + "]，" + "[出借订单号：" + ordId + "]");
			}
			outCustId = outCust.getChinapnrUsrcustid();
			outUserId = outCust.getUserId();
			// 调用交易查询接口(解冻)
			ChinapnrBean queryTransStatBean = queryTransStat(borrowTender.getNid(), borrowTender.getOrderDate(),
					"FREEZE");
			String respCode = queryTransStatBean == null ? "" : queryTransStatBean.getRespCode();
			// 调用接口失败时(000以外)
			if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
				String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
				LogUtil.errorLog(THIS_CLASS, methodName, "调用交易查询接口(解冻)失败。" + message + ",[出借订单号：" + ordId + "]", null);
				throw new Exception("调用交易查询接口(解冻)失败。" + respCode + "：" + message + ",[出借订单号：" + ordId + "]");
			}
			// 汇付交易状态
			String transStat = queryTransStatBean == null ? "" : queryTransStatBean.getTransStat();
			// 冻结请求已经被解冻 U:已解冻 N:无需解冻，对于解冻交易
			if ("U".equals(transStat) && "N".equals(transStat)) {
				LogUtil.errorLog(THIS_CLASS, methodName, "订单状态错误，订单状态：【" + transStat + "】," + "出借订单号：【" + ordId + "】",
						null);
				throw new Exception("订单状态错误，订单状态：【" + transStat + "】," + "出借订单号：【" + ordId + "】");
			}
			// 出借金额
			tenderAccount = borrowTender.getAccount();
			// 服务费
			userFee = getUserFee(serviceFee, tenderAccount, borrowStyle, borrowPeriod);
			// 放款订单号
			String loanOrdId = borrowTender.getLoanOrdid();
			// 放款订单时间
			String loanOrdDate = borrowTender.getLoanOrderDate();
			// 调用交易查询接口(放款)
			ChinapnrBean queryTransStatBean2 = queryTransStat(loanOrdId, loanOrdDate, "LOANS");
			respCode = queryTransStatBean2 == null ? "" : queryTransStatBean2.getRespCode();
			// 调用接口失败时(000,421以外)
			if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)
					&& !ChinaPnrConstant.RESPCODE_NO_LOANS_RECORD.equals(respCode)) {
				String message = queryTransStatBean2 == null ? "" : queryTransStatBean2.getRespDesc();
				LogUtil.errorLog(THIS_CLASS, methodName, "调用交易查询接口(放款)失败。" + message + ",[出借订单号：" + ordId + "]", null);
				throw new Exception("调用交易查询接口(放款)失败。" + respCode + "：" + message + ",[出借订单号：" + ordId + "]");
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
						borrowTender.getOrderDate(), GetterUtil.getString(inCustId), divDetails, reqExts);
				respCode = loansBean == null ? "" : loansBean.getRespCode();
				// 调用接口失败时(000以外)
				if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
					String message = "调用自动扣款（放款）接口失败。" + respCode + "：" + loansBean == null ? "" : loansBean
							.getRespDesc() + "，[出借订单号：" + ordId + "]";
					LogUtil.errorLog(THIS_CLASS, methodName, message, null);
					throw new Exception(message);
				}
			}
			// 更新订单为已经解冻
			boolean flag = this.updateFreezeList(freezeList) > 0 ? true : false;
			if (flag) {
				// 计算利息
				InterestInfo interestInfo = CalculatesUtil.getInterestInfo(tenderAccount, borrowPeriod, borrowApr,
						borrowStyle, borrowSuccessTime, borrowMonthRate, borrowManagerScaleEnd, projectType,
						differentialRate, borrowVerifyTime);
				if (interestInfo != null) {
					interestTender = interestInfo.getRepayAccountInterest(); // 利息
					capitalTender = interestInfo.getRepayAccountCapital(); // 本金
					accountTender = interestInfo.getRepayAccount(); // 本息
					recoverTime = interestInfo.getRepayTime(); // 估计还款时间
					recoverFee = interestInfo.getFee(); // 总管理费
				}
				// 写入还款明细表(huiyingdai_borrow_recover)
				BorrowRecover borrowRecover = new BorrowRecover();
				borrowRecover.setStatus(1); // 状态
				borrowRecover.setUserId(borrowTender.getUserId()); // 出借人
				borrowRecover.setBorrowNid(borrowNid); // 借款编号
				borrowRecover.setNid(ordId); // 出借订单号
				borrowRecover.setBorrowUserid(borrowUserid); // 借款人
				borrowRecover.setTenderId(borrowTender.getId()); // 出借表主键ID
				borrowRecover.setRecoverStatus(0); // 还款状态
				borrowRecover.setRecoverPeriod(isMonth ? borrowPeriod : 1); // 还款期数
				borrowRecover.setRecoverTime(GetterUtil.getString(recoverTime)); // 估计还款时间
				borrowRecover.setRecoverAccount(accountTender); // 预还金额
				borrowRecover.setRecoverInterest(interestTender); // 预还利息
				borrowRecover.setRecoverCapital(capitalTender); // 预还本金
				borrowRecover.setRecoverAccountYes(BigDecimal.ZERO); // 实还金额
				borrowRecover.setRecoverInterestYes(BigDecimal.ZERO); // 实还利息
				borrowRecover.setRecoverCapitalYes(BigDecimal.ZERO); // 实还本金
				borrowRecover.setRecoverAccountWait(accountTender); // 未还金额
				borrowRecover.setRecoverInterestWait(interestTender); // 未还利息
				borrowRecover.setRecoverCapitalWait(capitalTender); // 未还本金
				borrowRecover.setRecoverType(TYPE_WAIT); // 还款状态:等待
				borrowRecover.setRecoverFee(recoverFee); // 账户管理费
				borrowRecover.setRecoverLateFee(BigDecimal.ZERO); // 逾期费用收入
				borrowRecover.setRecoverWeb(0); // 网站待还
				borrowRecover.setRecoverWebTime("");
				borrowRecover.setRecoverVouch(0); // 担保人还款
				borrowRecover.setAdvanceStatus(0); // 提前还款
				borrowRecover.setAheadDays(0); // 提前还款天数
				borrowRecover.setChargeDays(0); // 罚息天数
				borrowRecover.setChargeInterest(BigDecimal.ZERO); // 罚息总额
				borrowRecover.setLateDays(0); // 逾期天数
				borrowRecover.setLateInterest(BigDecimal.ZERO); // 逾期利息
				borrowRecover.setLateForfeit(BigDecimal.ZERO); // 逾期滞纳金
				borrowRecover.setLateReminder(BigDecimal.ZERO); // 逾期崔收费
				borrowRecover.setAddtime(GetterUtil.getString(borrow.getBorrowSuccessTime()));
				borrowRecover.setCreateTime(nowTime);
				borrowRecover.setAddip(borrowTender.getAddip());
				boolean borrowRecoverFlag = this.insertBorrowRecover(borrowRecover) > 0 ? true : false;
				if (borrowRecoverFlag) {

					// 更新出借详情表
					BorrowTender newBorrowTender = new BorrowTender();
					newBorrowTender.setId(borrowTender.getId()); // ID
					newBorrowTender.setRecoverAccountWait(borrowRecover.getRecoverAccount()); // 待收总额
					newBorrowTender.setRecoverAccountAll(borrowRecover.getRecoverAccount()); // 收款总额
					newBorrowTender.setRecoverAccountInterestWait(borrowRecover.getRecoverInterest()); // 待收利息
					newBorrowTender.setRecoverAccountInterest(borrowRecover.getRecoverInterest()); // 收款总利息
					newBorrowTender.setRecoverAccountCapitalWait(borrowRecover.getRecoverCapital()); // 待收本金
					newBorrowTender.setLoanAmount(tenderAccount.subtract(userFee)); // 实际放款金额
					newBorrowTender.setLoanFee(userFee); // 服务费
					newBorrowTender.setStatus(1); // 状态 0，未放款，1，已放款
					newBorrowTender.setTenderStatus(1); // 出借状态 0，未放款，1，已放款
					newBorrowTender.setApiStatus(1); // 放款状态 0，未放款，1，已放款
					newBorrowTender.setWeb(2); // 写入网站收支明细
					boolean borrowTenderFlag = this.updateBorrowTender(newBorrowTender) > 0 ? true : false;
					if (borrowTenderFlag) {
						// 更新借款表
						BorrowWithBLOBs newBrrow = new BorrowWithBLOBs();
						newBrrow.setId(borrow.getId());
						newBrrow.setRepayAccountAll(borrow.getRepayAccountAll().add(borrowRecover.getRecoverAccount())); // 应还款总额
						newBrrow.setRepayAccountInterest(borrow.getRepayAccountInterest().add(
								borrowRecover.getRecoverInterest())); // 总还款利息
						newBrrow.setRepayAccountCapital(borrow.getRepayAccountCapital().add(
								borrowRecover.getRecoverCapital())); // 总还款本金
						newBrrow.setRepayAccountWait(borrow.getRepayAccountWait()
								.add(borrowRecover.getRecoverAccount())); // 未还款总额
						newBrrow.setRepayAccountInterestWait(borrow.getRepayAccountInterestWait().add(
								borrowRecover.getRecoverInterest())); // 未还款利息
						newBrrow.setRepayAccountCapitalWait(borrow.getRepayAccountCapitalWait().add(
								borrowRecover.getRecoverCapital())); // 未还款本金
						newBrrow.setRepayLastTime(GetterUtil.getString(DateUtils.getRepayDate(borrowStyle, new Date(),
								borrowPeriod, borrowPeriod))); // 最后还款时间
						newBrrow.setRepayNextTime(recoverTime); // 下次还款时间
						newBrrow.setRepayEachTime("每月" + GetDate.getServerDateTime(15, new Date()) + "日");// 每次还款的时间
						boolean borrowFlag = this.borrowMapper.updateByPrimaryKeySelective(newBrrow) > 0 ? true : false;
						if (borrowFlag) {
							// 写入借款满标日志(原复审业务)
							boolean isInsert = false;
							AccountBorrow accountBorrow = getAccountBorrow(borrowNid);
							if (accountBorrow == null) {
								isInsert = true;
								accountBorrow = new AccountBorrow();
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
							accountBorrow.setRemark("借款成功[" + borrow.getBorrowNid() + "]，扣除服务费{"
									+ accountBorrow.getFee().toString() + "}元");
							accountBorrow.setUpdateTime(nowTime); // 更新时间
							int accountBorrowCnt = isInsert ? this.accountBorrowMapper.insertSelective(accountBorrow)
									: this.accountBorrowMapper.updateByPrimaryKeySelective(accountBorrow);
							if (accountBorrowCnt > 0 ? true : false) {

								// 插入每个标的总的还款信息
								isInsert = false;
								BorrowRepay borrowRepay = getBorrowRepay(borrowNid);
								if (borrowRepay == null) {
									isInsert = true;
									borrowRepay = new BorrowRepay();
									borrowRepay.setStatus(0); // 状态
									borrowRepay.setUserId(borrowUserid); // 借款人ID
									borrowRepay.setBorrowNid(borrowNid); // 借款标号
									borrowRepay.setNid(nid); // 标识
									borrowRepay.setRepayType(TYPE_WAIT); // 还款状态(等待)
									borrowRepay.setRepayFee(BigDecimal.ZERO); // 还款费用
									borrowRepay.setRepayDays(""); // 还款时间间距
									borrowRepay.setRepayStep(0); // 还款步骤
									borrowRepay.setRepayActionTime(""); // 执行还款的时间
									borrowRepay.setRepayStatus(0); // 还款状态
									borrowRepay.setRepayPeriod(isMonth ? borrowPeriod : 1); // 还款期数
									borrowRepay.setRepayTime(GetterUtil.getString(recoverTime)); // 估计还款时间
									borrowRepay.setRepayYestime(""); // 实际还款时间
									borrowRepay.setRepayAccountAll(BigDecimal.ZERO); // 还款总额，加上费用
									borrowRepay.setRepayAccount(BigDecimal.ZERO); // 预还金额
									borrowRepay.setRepayInterest(BigDecimal.ZERO); // 预还利息
									borrowRepay.setRepayCapital(BigDecimal.ZERO); // 预还本金
									borrowRepay.setRepayAccountYes(BigDecimal.ZERO); // 实还金额
									borrowRepay.setLateRepayDays(0); // 逾期的天数
									borrowRepay.setRepayInterestYes(BigDecimal.ZERO); // 实还利息
									borrowRepay.setRepayCapitalYes(BigDecimal.ZERO); // 实还本金
									borrowRepay.setRepayCapitalWait(BigDecimal.ZERO);// 未还本金
									borrowRepay.setRepayInterestWait(BigDecimal.ZERO); // 未还利息
									borrowRepay.setRepayWeb(0); // 网站待还
									borrowRepay.setRepayWebTime(""); //
									borrowRepay.setRepayWebStep(0); // 提前还款
									borrowRepay.setRepayWebAccount(BigDecimal.ZERO); // 网站垫付金额
									borrowRepay.setRepayVouch(0); // 担保人还款
									borrowRepay.setAdvanceStatus(0); // 进展
									borrowRepay.setLateRepayStatus(0); // 是否逾期还款
									borrowRepay.setLateDays(0); // 逾期天数
									borrowRepay.setLateInterest(BigDecimal.ZERO); // 逾期利息
									borrowRepay.setLateForfeit(BigDecimal.ZERO); // 逾期滞纳金
									borrowRepay.setLateReminder(BigDecimal.ZERO); // 逾期崔收费
									borrowRepay.setDelayDays(0); // 逾期天数
									borrowRepay.setDelayInterest(BigDecimal.ZERO); // 逾期利息
									borrowRepay.setDelayRemark(""); // 备注
									borrowRepay.setAddtime(GetterUtil.getString(borrowSuccessTime)); // 发标时间
									borrowRepay.setAddip(borrow.getAddip()); // 发标ip
									borrowRepay.setCreateTime(nowTime); // 创建时间
									borrowRepay.setChargeDays(0);
									borrowRepay.setChargeInterest(BigDecimal.ZERO);
								}
								borrowRepay.setRepayFee(borrowRepay.getRepayFee().add(borrowRecover.getRecoverFee())); // 还款费用
								borrowRepay.setRepayAccount(borrowRepay.getRepayAccount().add(
										borrowRecover.getRecoverAccount())); // 预还金额
								borrowRepay.setRepayInterest(borrowRepay.getRepayInterest().add(
										borrowRecover.getRecoverInterest())); // 预还利息
								borrowRepay.setRepayCapital(borrowRepay.getRepayCapital().add(
										borrowRecover.getRecoverCapital())); // 预还本金
								int borrowRepayCnt = isInsert ? this.borrowRepayMapper.insertSelective(borrowRepay)
										: this.borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay);
								if (borrowRepayCnt > 0 ? true : false) {
									// [principal: 等额本金, month:等额本息,
									// month:等额本息,end:先息后本]
									if (isMonth) {
										// 更新分期还款计划表(huiyingdai_borrow_recover_plan)
										if (interestInfo != null && interestInfo.getListMonthly() != null) {

											BorrowRecoverPlan recoverPlan = null;
											InterestInfo monthly = null;
											for (int j = 0; j < interestInfo.getListMonthly().size(); j++) {
												monthly = interestInfo.getListMonthly().get(j);
												recoverPlan = new BorrowRecoverPlan();
												recoverPlan.setStatus(1); // 状态
												recoverPlan.setUserId(outUserId); // 出借人id
												recoverPlan.setBorrowNid(borrowNid); // 借款订单id
												recoverPlan.setNid(ordId); // 出借订单号
												recoverPlan.setBorrowUserid(borrowUserid); // 借款人ID
												recoverPlan.setTenderId(borrowTender.getId()); // 借款人ID
												recoverPlan.setRecoverStatus(0); //
												recoverPlan.setRecoverPeriod(j + 1); // 还款期数
												recoverPlan
														.setRecoverTime(GetterUtil.getString(monthly.getRepayTime())); // 估计还款时间
												recoverPlan.setRecoverAccount(monthly.getRepayAccount()); // 预还金额
												recoverPlan.setRecoverInterest(monthly.getRepayAccountInterest()); // 预还利息
												recoverPlan.setRecoverCapital(monthly.getRepayAccountCapital()); // 预还本金
												recoverPlan.setRecoverFee(monthly.getFee()); // 预还管理费
												recoverPlan.setRecoverYestime(""); // 实际还款时间
												recoverPlan.setRecoverAccountYes(BigDecimal.ZERO); // 实还金额
												recoverPlan.setRecoverInterestYes(BigDecimal.ZERO); // 实还利息
												recoverPlan.setRecoverCapitalYes(BigDecimal.ZERO); // 实还本金
												recoverPlan.setRecoverAccountWait(monthly.getRepayAccount()); // 未还金额
												recoverPlan.setRecoverCapitalWait(monthly.getRepayAccountCapital()); // 未还本金
												recoverPlan.setRecoverInterestWait(monthly.getRepayAccountInterest()); // 未还利息
												recoverPlan.setRecoverType(TYPE_WAIT); // 等待
												recoverPlan.setRecoverLateFee(BigDecimal.ZERO); // 逾期管理费
												recoverPlan.setRecoverWeb(0); // 网站待还
												recoverPlan.setRecoverWebTime(""); //
												recoverPlan.setRecoverVouch(0); // 担保人还款
												recoverPlan.setAdvanceStatus(0); //
												recoverPlan.setAheadDays(0); // 提前还款天数
												recoverPlan.setChargeDays(0); // 罚息天数
												recoverPlan.setChargeInterest(BigDecimal.ZERO); // 罚息总额
												recoverPlan.setLateDays(0); // 逾期天数
												recoverPlan.setLateInterest(BigDecimal.ZERO); // 逾期利息
												recoverPlan.setLateForfeit(BigDecimal.ZERO); // 逾期滞纳金
												recoverPlan.setLateReminder(BigDecimal.ZERO); // 逾期崔收费
												recoverPlan.setDelayDays(0); // 延期天数
												recoverPlan.setDelayInterest(BigDecimal.ZERO); // 延期利息
												recoverPlan.setDelayRate(BigDecimal.ZERO); // 延期费率
												recoverPlan.setAddtime(GetterUtil.getString(borrowSuccessTime)); // 借款成功时间
												recoverPlan.setCreateTime(nowTime); // 创建时间
												recoverPlan.setAddip(borrowTender.getAddip());
												recoverPlan.setSendmail(0);
												boolean borrowRecoverPlanFlag = this.borrowRecoverPlanMapper
														.insertSelective(recoverPlan) > 0 ? true : false;
												if (borrowRecoverPlanFlag) {
													// 更新总的还款计划表(huiyingdai_borrow_repay_plan)
													isInsert = false;
													BorrowRepayPlan repayPlan = getBorrowRepayPlan(borrowNid, j + 1);
													if (repayPlan == null) {
														isInsert = true;
														repayPlan = new BorrowRepayPlan();
														repayPlan.setStatus(0); // 状态
														repayPlan.setUserId(borrowUserid); // 借款人
														repayPlan.setBorrowNid(borrowNid); // 借款订单id
														repayPlan.setNid(nid); // 标识
														repayPlan.setRepayType(TYPE_WAIT); // 还款类型
														repayPlan.setRepayFee(BigDecimal.ZERO); // 还款费用
														repayPlan.setRepayDays(""); // 还款时间间距
														repayPlan.setRepayStep(0); // 还款步骤
														repayPlan.setRepayActionTime(""); // 执行还款的时间
														repayPlan.setRepayStatus(0); // 还款状态
														repayPlan.setRepayPeriod(j + 1); // 还款期数
														repayPlan.setRepayTime(recoverPlan.getRecoverTime()); // 估计还款时间
														repayPlan.setRepayYestime(""); // 实际还款时间
														repayPlan.setRepayAccountAll(BigDecimal.ZERO); // 还款总额，加上费用
														repayPlan.setRepayAccount(BigDecimal.ZERO); // 预还金额
														repayPlan.setRepayInterest(BigDecimal.ZERO); // 预还利息
														repayPlan.setRepayCapital(BigDecimal.ZERO); // 预还本金
														repayPlan.setRepayAccountYes(BigDecimal.ZERO); // 实还金额
														repayPlan.setRepayInterestYes(BigDecimal.ZERO); // 实还利息
														repayPlan.setRepayCapitalYes(BigDecimal.ZERO); // 实还本金
														repayPlan.setRepayCapitalWait(BigDecimal.ZERO); // 未还本金
														repayPlan.setRepayInterestWait(BigDecimal.ZERO); // 未还利息
														repayPlan.setRepayWeb(0); // 网站待还
														repayPlan.setRepayWebTime("");
														repayPlan.setRepayWebStep(0); // 提前还款
														repayPlan.setRepayWebAccount(BigDecimal.ZERO); // 网站垫付金额
														repayPlan.setRepayVouch(0); // 担保人还款
														repayPlan.setAdvanceStatus(0); // 进展
														repayPlan.setLateRepayStatus(0); // 是否逾期还款
														repayPlan.setLateDays(0); // 逾期天数
														repayPlan.setLateInterest(BigDecimal.ZERO); // 逾期利息
														repayPlan.setLateForfeit(BigDecimal.ZERO); // 逾期滞纳金
														repayPlan.setLateReminder(BigDecimal.ZERO); // 逾期崔收费
														repayPlan.setLateRepayDays(0); // 逾期还款天数
														repayPlan.setDelayDays(0); // 延期天数
														repayPlan.setDelayInterest(BigDecimal.ZERO); // 延期利息
														repayPlan.setDelayRemark(""); // 延期备注
														repayPlan.setAddtime(GetterUtil.getString(borrowSuccessTime)); // 借款成功时间
														repayPlan.setAddip(borrowTender.getAddip());
														repayPlan.setCreateTime(nowTime); // 创建时间
														repayPlan.setChargeDays(0);
														repayPlan.setChargeInterest(BigDecimal.ZERO);
													}
													repayPlan.setRepayFee(repayPlan.getRepayFee().add(
															recoverPlan.getRecoverFee())); // 还款费用
													repayPlan.setRepayAccount(repayPlan.getRepayAccount().add(
															recoverPlan.getRecoverAccount())); // 预还金额
													repayPlan.setRepayInterest(repayPlan.getRepayInterest().add(
															recoverPlan.getRecoverInterest())); // 预还利息
													repayPlan.setRepayCapital(repayPlan.getRepayCapital().add(
															recoverPlan.getRecoverCapital())); // 预还本金

													int borrowRepayPlanCnt = isInsert ? this.borrowRepayPlanMapper
															.insertSelective(repayPlan) : this.borrowRepayPlanMapper
															.updateByPrimaryKeySelective(repayPlan);
													if (borrowRepayPlanCnt > 0 ? false : true) {
														throw new Exception(
																"总的还款计划表(huiyingdai_borrow_repay_plan)写入失败!"
																		+ "[出借订单号：" + ordId + "]，" + "[期数：" + j + 1
																		+ "]");
													}
												} else {
													throw new Exception("分期还款计划表(huiyingdai_borrow_recover_plan)写入失败!"
															+ "[出借订单号：" + ordId + "]，" + "[期数：" + j + 1 + "]");
												}
											}
										}
									}
									// 更新账户信息(出借人)
									Account account = new Account();
									account.setUserId(borrowTender.getUserId());
									// 出借人资金总额 + 利息
									account.setTotal(interestTender);
									// 出借人冻结金额 - 出借金额(等额本金时出借金额可能会大于计算出的本金之和)
									account.setFrost(tenderAccount);
									// 出借人待收金额 + 利息+ 本金
									account.setAwait(accountTender);
									boolean investaccountFlag = this.adminAccountCustomizeMapper
											.updateOfLoansTender(account) > 0 ? true : false;
									if (investaccountFlag) {
										// 取得账户信息(出借人)
										account = this.getAccountByUserId(borrowTender.getUserId());
										if (account != null) {
											// 写入收支明细
											AccountList accountList = new AccountList();
											accountList.setNid(ordId); // 出借订单号
											accountList.setUserId(borrowTender.getUserId()); // 出借人
											accountList.setAmount(tenderAccount); // 出借本金
											accountList.setType(2); // 2支出
											accountList.setTrade("tender_success"); // 出借成功
											accountList.setTradeCode("balance"); // 余额操作
											accountList.setTotal(account.getTotal()); // 出借人资金总额
											accountList.setBalance(account.getBalance()); // 出借人可用金额
											accountList.setFrost(account.getFrost()); // 出借人冻结金额
											accountList.setAwait(account.getAwait()); // 出借人待收金额
											// accountList.setRemark("出借放款");
											// //出借放款
											accountList.setCreateTime(nowTime); // 创建时间
											accountList.setBaseUpdate(nowTime); // 更新时间
											accountList.setOperator(CustomConstants.OPERATOR_AUTO_LOANS); // 操作者
											accountList.setIp(borrow.getAddip()); // 操作IP
											accountList.setRemark(borrowNid);
											accountList.setIsUpdate(0);
											accountList.setBaseUpdate(0);
											accountList.setInterest(BigDecimal.ZERO); // 利息
											accountList.setWeb(0); // PC
											boolean investAccountListFlag = insertAccountList(accountList) > 0 ? true
													: false;
											if (investAccountListFlag) {
												// 更新借款人账户表(原复审业务)
												Account newBorrowAccount = new Account();
												newBorrowAccount.setUserId(borrowUserid);
												newBorrowAccount.setTotal(tenderAccount.subtract(userFee));// 累加到账户总资产
												newBorrowAccount.setBalance(tenderAccount.subtract(userFee)); // 累加到可用余额
												newBorrowAccount.setIncome(tenderAccount.subtract(userFee)); // 累加到总收入
												newBorrowAccount.setRepay(accountTender); // 待还金额
												boolean borrowAccountFlag = this.adminAccountCustomizeMapper
														.updateOfLoansBorrow(newBorrowAccount) > 0 ? true : false;
												if (borrowAccountFlag) {
													// 服务费大于0时,插入网站收支明细
													if (userFee.compareTo(BigDecimal.ZERO) > 0) {
														// 插入网站收支明细记录
														AccountWebList accountWebList = new AccountWebList();
														accountWebList.setOrdid(ordId);// 订单号
														accountWebList.setBorrowNid(borrowNid); // 出借编号
														accountWebList.setUserId(borrowTender.getUserId()); // 出借者
														accountWebList.setAmount(userFee); // 服务费
														accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入
																											// 2支出
														accountWebList.setTrade(CustomConstants.TRADE_LOANFEE); // 服务费
														accountWebList.setTradeType(CustomConstants.TRADE_LOANFEE_NM); // 服务费
														accountWebList.setCreateTime(borrowRecover.getCreateTime());
														accountWebList.setRemark(borrowNid);
														boolean accountWebListFlag = insertAccountWebList(accountWebList) > 0 ? true
																: false;
														if (!accountWebListFlag) {
															throw new Exception(
																	"网站收支记录(huiyingdai_account_web_list)更新失败!"
																			+ "[出借订单号：" + ordId + "]");
														}
													}
													List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper
															.selectByExample(new CalculateInvestInterestExample());
													if (calculates != null && calculates.size() > 0) {
														CalculateInvestInterest calculateNew = new CalculateInvestInterest();
														calculateNew.setInterestSum(borrowRecover.getRecoverInterest());
														calculateNew.setId(calculates.get(0).getId());
														this.webCalculateInvestInterestCustomizeMapper
																.updateCalculateInvestByPrimaryKey(calculateNew);
													}
													// add by zhangjp 放款更新用户V值
													// start
//													CommonSoaUtils.updateVipValue(borrowTender.getNid(),
//															borrowTender.getUserId());
													// add by zhangjp 放款更新用户V值
													// start
													msg.put(VAL_USERID, borrowRecover.getUserId().toString());
													msg.put(VAL_TITLE, borrowRecover.getBorrowNid());
													msg.put(VAL_AMOUNT, accountTender.toString());
													msg.put(VAL_BALANCE, capitalTender.toString());// 出借本金
													msg.put(VAL_PROFIT, interestTender.toString());// 待收收益
													msg.put(VAL_RECOVERTIME,
															isMonth ? "每月" + GetDate.getServerDateTime(15, new Date())
																	+ "日" : GetDate.times10toStrYYYYMMDD(recoverTime));// 还款时间
													msg.put(PARAM_BORROWRECOVERID, borrowRecover.getId().toString());
													msg.put(VAL_ORDER_ID, ordId);
													msg.put(VAL_LOAN_TIME, GetDate.getDate("yyyy-MM-dd HH:mm:ss"));
												} else {
													throw new Exception("借款人资金记录(huiyingdai_account)更新失败!" + "[出借订单号："
															+ ordId + "]");
												}
											} else {
												throw new Exception("出借人收支明细(huiyingdai_account_list)写入失败!" + "[出借订单号："
														+ ordId + "]");
											}
										} else {
											throw new Exception("出借人账户信息不存在。[出借人ID：" + borrowTender.getUserId() + "]，"
													+ "[出借订单号：" + ordId + "]");
										}
									} else {
										throw new Exception("出借人资金记录(huiyingdai_account)更新失败!" + "[出借订单号：" + ordId
												+ "]");
									}
								} else {
									throw new Exception("每个标的总的还款信息(huiyingdai_borrow_repay)"
											+ (isInsert ? "插入" : "更新") + "失败!" + "[出借订单号：" + ordId + "]");
								}
							} else {
								throw new Exception("借款满标日志(huiyingdai_account_borrow)更新失败!" + "[出借订单号：" + ordId + "]");
							}
						} else {
							throw new Exception("借款详情(huiyingdai_borrow)更新失败!" + "[出借订单号：" + ordId + "]");
						}
					} else {
						throw new Exception("出借详情(huiyingdai_borrow_tender)更新失败!" + "[出借订单号：" + ordId + "]");
					}
				} else {
					throw new Exception("还款明细表(huiyingdai_borrow_tender)写入失败!" + "[出借订单号：" + ordId + "]");
				}
			} else {
				throw new Exception("冻结订单表(huiyingdai_freezeList)更新失败!" + "[出借订单号：" + ordId + "]");
			}
		} else {
			throw new Exception("冻结订单表(huiyingdai_freezeList)查询失败！, " + "出借订单号[" + ordId + "]");
		}
		System.out.println("-----------放款结束---" + apicron.getBorrowNid() + "---------" + borrowTender.getLoanOrdid());
		return retMsgList;
	}

	/**
	 * @param freezeList
	 * @return
	 */

	private int updateFreezeList(FreezeList freezeList) {
		freezeList.setStatus(1);
		return this.freezeListMapper.updateByPrimaryKeySelective(freezeList);

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
		BorrowTenderExample tenderExample = new BorrowTenderExample();
		tenderExample.createCriteria().andBorrowNidEqualTo(borrowNid).andApiStatusEqualTo(0);
		int tenderCnt = this.borrowTenderMapper.countByExample(tenderExample);

		BorrowRepay newBorrowRepay = new BorrowRepay();
		BorrowRepayPlan newBorrowRepayPlan = new BorrowRepayPlan();
		BorrowWithBLOBs newBrrow = new BorrowWithBLOBs();
		if (tenderCnt == 0) {
			// 更新BorrowRepay
			newBorrowRepay.setStatus(1); // 已放款
			BorrowRepayExample repayExample = new BorrowRepayExample();
			repayExample.createCriteria().andBorrowNidEqualTo(borrowNid);
			this.borrowRepayMapper.updateByExampleSelective(newBorrowRepay, repayExample);

			// 更新BorrowRepayPlan
			newBorrowRepayPlan.setStatus(1); // 已放款
			BorrowRepayPlanExample repayPlanExample = new BorrowRepayPlanExample();
			repayPlanExample.createCriteria().andBorrowNidEqualTo(borrowNid);
			this.borrowRepayPlanMapper.updateByExampleSelective(newBorrowRepayPlan, repayPlanExample);

			// 更新Borrow
			newBrrow.setRecoverLastTime(GetDate.getMyTimeInMillis()); // 最后一笔放款时间
			BorrowExample borrowExample = new BorrowExample();
			borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
			this.borrowMapper.updateByExampleSelective(newBrrow, borrowExample);

			// 放款总信息表
			AccountBorrow accountBorrow = getAccountBorrow(borrowNid);
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
			userFee = serviceFee.multiply(account).multiply(new BigDecimal(borrowPeriod))
					.setScale(2, BigDecimal.ROUND_DOWN);
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
	public List<BorrowApicron> getBorrowApicronList(Integer status, Integer apiType) {
		BorrowApicronExample example = new BorrowApicronExample();
		BorrowApicronExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(status);
		criteria.andApiTypeEqualTo(apiType);
		example.setOrderByClause(" id asc ");
		List<BorrowApicron> list = this.borrowApicronMapper.selectByExample(example);

		return list;
	}

	/**
	 * 取得借款API任务表
	 *
	 * @return
	 */
	public List<BorrowApicron> getBorrowApicronListWithRepayStatus(Integer status, Integer apiType) {
		BorrowApicronExample example = new BorrowApicronExample();
		BorrowApicronExample.Criteria criteria = example.createCriteria();
		criteria.andRepayStatusEqualTo(status);
		criteria.andApiTypeEqualTo(apiType);
		example.setOrderByClause(" id asc ");
		List<BorrowApicron> list = this.borrowApicronMapper.selectByExample(example);

		return list;
	}

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	public int updateBorrowApicron(Integer id, Integer status) {
		return updateBorrowApicron(id, status, null);
	}

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	public int updateBorrowApicronOfRepayStatus(Integer id, Integer status) {
		BorrowApicron record = new BorrowApicron();
		record.setId(id);
		record.setRepayStatus(status);
		record.setUpdateTime(GetDate.getMyTimeInMillis());
		return this.borrowApicronMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @param data
	 * @return
	 */
	public int updateBorrowApicron(Integer id, Integer status, String data) {
		BorrowApicron record = new BorrowApicron();
		record.setId(id);
		record.setStatus(status);
		if (Validator.isNotNull(data) || status == 1) {
			record.setData(data);
		}
		if (record.getWebStatus() == null) {
			record.setWebStatus(0);
		}
		record.setUpdateTime(GetDate.getMyTimeInMillis());
		return this.borrowApicronMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 取得借款列表
	 *
	 * @return
	 */
	public List<BorrowTender> getBorrowTenderList(String borrowNid) {
		BorrowTenderExample example = new BorrowTenderExample();
		BorrowTenderExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andApiStatusEqualTo(0);
		example.setOrderByClause(" id asc ");
		List<BorrowTender> list = this.borrowTenderMapper.selectByExample(example);

		return list;
	}

	/**
	 * 取得标的详情
	 *
	 * @return
	 */
	public BorrowWithBLOBs getBorrow(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<BorrowWithBLOBs> list = this.borrowMapper.selectByExampleWithBLOBs(example);

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
	public BorrowRepay getBorrowRepay(String borrowNid) {
		BorrowRepayExample example = new BorrowRepayExample();
		BorrowRepayExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<BorrowRepay> list = this.borrowRepayMapper.selectByExample(example);

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
	public BorrowRepayPlan getBorrowRepayPlan(String borrowNid, Integer period) {
		BorrowRepayPlanExample example = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andRepayPeriodEqualTo(period);
		List<BorrowRepayPlan> list = this.borrowRepayPlanMapper.selectByExample(example);

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
	public AccountBorrow getAccountBorrow(String borrowNid) {
		AccountBorrowExample example = new AccountBorrowExample();
		AccountBorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<AccountBorrow> list = this.accountBorrowMapper.selectByExample(example);

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
	private FreezeList getFreezeList(String ordId) {
		FreezeListExample example = new FreezeListExample();
		FreezeListExample.Criteria criteria = example.createCriteria();
		criteria.andOrdidEqualTo(ordId);
		List<FreezeList> list = this.freezeListMapper.selectByExample(example);
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
	private int insertAccountList(AccountList accountList) {
		// 写入收支明细
		return this.accountListMapper.insertSelective(accountList);
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
	 * 写入还款明细
	 *
	 * @param accountList
	 * @return
	 */
	private int insertBorrowRecover(BorrowRecover borrowRecover) {
		return borrowRecoverMapper.insertSelective(borrowRecover);
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
	public List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize) {
		return this.borrowCustomizeMapper.selectBorrowList(borrowCommonCustomize);
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
		List<WebUserInvestListCustomize> list = webUserInvestListCustomizeMapper.selectUserInvestList(params);
		return list;
	}

	public int countProjectRepayPlanRecordTotal(String borrowNid, String userId, String nid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		int total = webUserInvestListCustomizeMapper.countProjectRepayPlanRecordTotal(params);
		return total;
	}

	public List<WebProjectRepayListCustomize> selectProjectRepayPlanList(String borrowNid, String userId, String nid,
			int offset, int limit) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		List<WebProjectRepayListCustomize> projectRepayList = webUserInvestListCustomizeMapper
				.selectProjectRepayPlanList(params);
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
					System.err.println("userid=" + msg.get(VAL_USERID) + ";开始发送短信,发送金额" + msg.get(VAL_AMOUNT));
					SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null,
							MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_TOUZI_SUCCESS,
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
		System.out.println("=====cwyang 开始发送邮件===========");
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
						System.out.println("=============cwyang 发送email " + email);
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
						BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
						// 借款编码
						borrowCommonCustomize.setBorrowNidSrch(borrowNid);
						List<BorrowCustomize> recordList = this.selectBorrowList(borrowCommonCustomize);
						if (recordList != null && recordList.size() == 1) {
							Map<String, Object> contents = new HashMap<String, Object>();
							contents.put("record", recordList.get(0));
							contents.put("borrowNid", borrowNid);
							contents.put("nid", orderId);
							// 借款人用户名
							contents.put("borrowUsername", recordList.get(0).getUsername().substring(0,1)+"**");
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
		                    } else {
								System.out.println("标的出借信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。出借订单号:" + orderId);
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
										List<WebProjectRepayListCustomize> repayList = this
												.selectProjectRepayPlanList(borrowNid, userId, orderId,
														paginator.getOffset(), paginator.getLimit());
										contents.put("paginator", paginator);
										contents.put("repayList", repayList);
									} else {
										Paginator paginator = new Paginator(1, recordTotal);
										contents.put("paginator", paginator);
										contents.put("repayList", "");
									}
								}
							}
							String pdfUrl = "";
							// 融通宝居然协议不同
							if (recordList.get(0).getProjectType().equals("13")) {
								UsersInfo userinfo = this.getUsersInfoByUserId(Integer.parseInt(userId));
								if (userInvestList != null && userInvestList.size() > 0) {
									contents.put("investDeatil", userInvestList.get(0));
								}
								contents.put("projectDeatil", recordList.get(0));
								contents.put("truename", userinfo.getTruename());
								contents.put("idcard", userinfo.getIdcard());
								contents.put("borrowNid", borrowNid);// 标的号
								contents.put("assetNumber", recordList.get(0).getBorrowAssetNumber());// 资产编号
								contents.put("projectType", recordList.get(0).getProjectType());// 项目类型
								String moban = CustomConstants.RTB_TENDER_CONTRACT;
								if (recordList.get(0) != null && recordList.get(0).getBorrowPublisher() != null
										&& recordList.get(0).getBorrowPublisher().equals("中商储")) {
									moban = CustomConstants.RTB_TENDER_CONTRACT_ZSC;
								}
								if (recordList.get(0) != null) {
									recordList.get(0).setBorrowPeriod(
											recordList.get(0).getBorrowPeriod()
													.substring(0, recordList.get(0).getBorrowPeriod().length() - 1));
								}
								pdfUrl = PdfGenerator.generateLocal(fileName, moban, contents);
							} else {
								pdfUrl = PdfGenerator
										.generateLocal(fileName, CustomConstants.TENDER_CONTRACT, contents);
							}
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
							// modify by zhangjp 优惠券放款相关 start
							// 是否优惠券出借
							if (StringUtils.equals(msg.get(COUPON_TYPE), "1")) {
								CouponRecoverExample example = new CouponRecoverExample();
								example.createCriteria().andTenderIdEqualTo(msg.get(TENDER_NID));
								CouponRecover rc = new CouponRecover();
								rc.setNoticeFlg(1);
								// 将所有该笔出借的放款记录（分期或不分期）都改成通知状态
								this.couponRecoverMapper.updateByExampleSelective(rc, example);
							} else {
								// 更新BorrowRecover邮件发送状态
								String borrowRecoverId = msg.get(PARAM_BORROWRECOVERID);
								if (Validator.isNotNull(borrowRecoverId) && NumberUtils.isNumber(borrowRecoverId)) {
									BorrowRecover borrowRecover = new BorrowRecover();
									borrowRecover.setId(Integer.valueOf(borrowRecoverId));
									borrowRecover.setSendmail(1);
									this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover);
								}
							}
							// modify by zhangjp 优惠券放款相关 end
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
		System.out.println("=====cwyang 结束发送邮件===========");
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

}

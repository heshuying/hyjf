package com.hyjf.batch.htj.plancreditrepay;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.batch.htj.debtrepay.DebtBorrowRepayService;
import com.hyjf.common.calculate.AccountManagementFeeUtils;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.calculate.UnnormalRepayUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.DebtAccountList;
import com.hyjf.mybatis.model.auto.DebtAccountListExample;
import com.hyjf.mybatis.model.auto.DebtApicron;
import com.hyjf.mybatis.model.auto.DebtApicronExample;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.DebtCredit;
import com.hyjf.mybatis.model.auto.DebtCreditExample;
import com.hyjf.mybatis.model.auto.DebtCreditRepay;
import com.hyjf.mybatis.model.auto.DebtCreditRepayExample;
import com.hyjf.mybatis.model.auto.DebtCreditTender;
import com.hyjf.mybatis.model.auto.DebtCreditTenderExample;
import com.hyjf.mybatis.model.auto.DebtDetail;
import com.hyjf.mybatis.model.auto.DebtDetailExample;
import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtLoanDetail;
import com.hyjf.mybatis.model.auto.DebtLoanDetailExample;
import com.hyjf.mybatis.model.auto.DebtLoanExample;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.DebtRepay;
import com.hyjf.mybatis.model.auto.DebtRepayDetail;
import com.hyjf.mybatis.model.auto.DebtRepayDetailExample;
import com.hyjf.mybatis.model.auto.DebtRepayDetailExample.Criteria;
import com.hyjf.mybatis.model.auto.DebtRepayExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * 债转自动还款(还款服务)
 * 
 * 注：此次不考虑逾期还款，延期还款，提前还款， 均按照按照正常还款进行
 * 
 * @author Administrator
 *
 */
@Service
public class PlanCreditRepayServiceImpl extends BaseServiceImpl implements PlanCreditRepayService {

	private static final String THIS_CLASS = PlanCreditRepayServiceImpl.class.getName();

	@Autowired
	DebtBorrowRepayService debtBorrowRepayService;

	/**
	 * 自动还款
	 *
	 * @throws Exception
	 */
	private boolean updateBorrowCreditRepay(DebtApicron apicron, DebtBorrowWithBLOBs borrow, DebtLoan debtLoan,
			AccountChinapnr borrowUserCust, DebtCredit debtCredit, DebtCreditTender debtCreditTender,
			DebtCreditRepay debtCreditRepay) throws Exception {

		String methodName = "updateBorrowCreditRepay";
		System.out.println("------债转还款承接部分开始---债转编号：" + debtCredit.getCreditNid() + "---------");
		System.out.println("------债转还款承接部分开始---承接订单号：" + debtCreditTender.getAssignOrderId() + "---------");

		/** 基本变量 */
		// 还款订单号
		String repayOrderId = null;
		// 还款订单日期
		String repayOrderDate = null;
		// 借款人延期利息
		BigDecimal borrowRepayDelayInterest = BigDecimal.ZERO;
		// 借款人逾期利息
		BigDecimal borrowRepayLateInterest = BigDecimal.ZERO;
		// 借款人提前减息
		BigDecimal borrowRepayAdvanceInterest = BigDecimal.ZERO;
		// 还款本金
		BigDecimal recoverCapital = BigDecimal.ZERO;
		// 应收管理费
		BigDecimal manageFee = BigDecimal.ZERO;
		// 汇添金服务费
		BigDecimal serviceFee = BigDecimal.ZERO;
		// 出借人延期利息
		BigDecimal receiveDelayInterest = BigDecimal.ZERO;
		// 出借人逾期利息
		BigDecimal receiveLateInterest = BigDecimal.ZERO;
		// 出借人提前减息
		BigDecimal receiveAdvanceInterest = BigDecimal.ZERO;
		// 提前,延期,逾期状态
		Integer advanceStatus = 0;
		// 提前天数
		int advanceDays = 0;
		// 延期天数
		int delayDays = 0;
		// 逾期天数
		int lateDays = 0;
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 借款编号
		String borrowNid = apicron.getBorrowNid();
		// 借款人ID
		Integer borrowUserid = apicron.getUserId();
		// 当前期数
		Integer periodNow = apicron.getPeriodNow();
		/** 标的基本数据 */
		// 标的是否进行银行托管
		int bankInputFlag = Validator.isNull(borrow.getBankInputFlag()) ? 0 : borrow.getBankInputFlag();
		// 项目总期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 管理费率
		BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO
				: new BigDecimal(borrow.getManageFeeRate());
		// 差异费率
		BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO
				: new BigDecimal(borrow.getDifferentialRate());
		// 初审时间
		int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
		// 剩余还款期数
		Integer periodNext = borrowPeriod - periodNow;
		// 出借订单号
		String investOrderId = debtLoan.getInvestOrderId();
		// 出借人用户ID
		Integer tenderUserId = debtLoan.getUserId();
		// 出借ID
		Integer tenderId = debtLoan.getInvestId();
		// 出借人在汇付的账户信息
		AccountChinapnr tenderUserCust = getChinapnrUserInfo(tenderUserId);
		if (tenderUserCust == null) {
			throw new Exception("出借人未开户。[用户ID：" + tenderUserId + "]，" + "[出借订单号：" + investOrderId + "]");
		}
		// 借款人客户号
		Long borrowUserCustId = borrowUserCust.getChinapnrUsrcustid();
		// 取得还款详情
		DebtRepay debtRepay = getBorrowRepay(borrowNid);
		// 取得出借信息
		DebtInvest debtInvest = getBorrowTender(tenderId);
		// 承接订单号
		String assignOrderId = debtCreditRepay.getAssignOrderId();
		// 更新DebtDetail表
		DebtDetail debtDetail = this.getDebtDetail(assignOrderId, periodNow);
		if (debtDetail == null) {
			throw new Exception("还款计划表(hyjf_debt_repay_detail)查询失败！" + "[承接订单号：" + debtCreditRepay.getAssignOrderId()
					+ "]" + "[期数：" + periodNow + "]");
		}
		// 出借人延期利息
		receiveDelayInterest = debtDetail.getDelayInterest();
		// 出借人逾期利息
		receiveLateInterest = debtDetail.getLateInterest();
		// 分期还款计划表
		DebtLoanDetail debtLoanDetail = null;
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		if (isMonth) {
			// 取得分期还款计划表
			debtLoanDetail = getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId, debtLoan.getInvestId());
			if (debtLoanDetail != null) {
				// 应还款本金
				recoverCapital = debtLoanDetail.getLoanCapital();
				// 提前,延期,逾期状态
				advanceStatus = debtLoanDetail.getAdvanceStatus();
				// 提前天数
				advanceDays = debtLoanDetail.getAdvanceDays();
				// 延期天数
				delayDays = debtLoanDetail.getDelayDays();
				// 逾期天数
				lateDays = debtLoanDetail.getLateDays();
				// 管理费
				manageFee = debtLoanDetail.getManageFee();
			} else {
				throw new Exception("分期还款计划表数据不存在。[借款编号：" + borrowNid + "]，" + "[出借订单号：" + debtCredit.getInvestOrderId()
						+ "]，" + "[期数：" + periodNow + "]");
			}
		} else {// [endday: 按天计息, end:按月计息]
			debtLoan = this.getBorrowRecover(debtLoan.getId());
			// 还款本金
			recoverCapital = debtLoan.getLoanCapital();
			// 提前,延期,逾期状态
			advanceStatus = debtLoan.getAdvanceStatus();
			// 提前天数
			advanceDays = debtLoan.getAdvanceDays();
			// 延期天数
			delayDays = debtLoan.getDelayDays();
			// 逾期天数
			lateDays = debtLoan.getLateDays();
			// 管理费
			manageFee = debtLoan.getManageFee();
		}
		// 延期,逾期的计算
		if (advanceStatus == 1) {
			// 提前还款
			// 如果项目类型为融通宝，调用新的提前还款利息计算公司
			if (borrow.getProjectType() == 13 && borrow.getBorrowStyle().equals(CustomConstants.BORROW_STYLE_ENDDAY)) {
				// 用户提前还款减少的利息
				borrowRepayAdvanceInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(
						debtCreditRepay.getRepayCapital(), borrow.getBorrowApr(), advanceDays);
			} else {
				// 用户提前还款减少的利息
				borrowRepayAdvanceInterest = UnnormalRepayUtils.aheadRepayChargeInterest(
						debtCreditRepay.getRepayCapital(), borrow.getBorrowApr(), advanceDays);
			}
			borrowRepayAdvanceInterest = borrowRepayAdvanceInterest.multiply(new BigDecimal(-1));
			receiveAdvanceInterest = borrowRepayAdvanceInterest;
		} else if (advanceStatus == 2) {
			// 计算用户延期利息
			borrowRepayDelayInterest = UnnormalRepayUtils.delayRepayInterest(debtCreditRepay.getRepayCapital(),
					borrow.getBorrowApr(), delayDays);
		} else if (advanceStatus == 3) {
			// 计算用户延期利息
			borrowRepayDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(debtCreditRepay.getRepayCapital(),
					borrow.getBorrowApr(), delayDays);
			// 计算用户逾期利息
			borrowRepayLateInterest = UnnormalRepayUtils.overdueRepayOverdueInterest(debtCreditRepay.getRepayAccount(),
					lateDays);
		}
		// 借款人应还款利息
		BigDecimal borrowRepayInterest = debtCreditRepay.getRepayInterest().add(borrowRepayAdvanceInterest)
				.add(borrowRepayDelayInterest).add(borrowRepayLateInterest);
		// 借款人应还本息
		BigDecimal borrowRepayAccount = debtCreditRepay.getRepayCapital().add(borrowRepayInterest);
		// 承接人待收取还款利息
		BigDecimal receiveInterest = debtCreditRepay.getRepayInterest().add(receiveAdvanceInterest)
				.add(receiveDelayInterest).add(receiveLateInterest);
		// 承接人待收取的还款本息
		BigDecimal receiveAccount = debtCreditRepay.getRepayCapital().add(receiveInterest);
		if (debtCreditTender != null) {
			// 还款订单号
			repayOrderId = debtCreditRepay.getCreditRepayOrderId();
			// 还款订单日期
			repayOrderDate = debtCreditRepay.getCreditRepayOrderDate();
			// 判断该收支明细是否存在时,跳出本次循环
			if (countAccountCreditListByNid(repayOrderId) > 0) {
				return true;
			}
			// 管理费
			BigDecimal perManage = new BigDecimal("0");
			// 按月计息，到期还本还息end
			if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
				perManage = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(
						debtCreditRepay.getRepayCapital(), feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
			}
			// 按天计息到期还本还息
			else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
				perManage = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(debtCreditRepay.getRepayCapital(),
						feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
			}
			// 等额本息month、等额本金principal
			else if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
					|| CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
				if (periodNow.intValue() == borrowPeriod.intValue()) {
					perManage = AccountManagementFeeUtils.getHTJMonthAccountManagementFee(
							debtCreditRepay.getRepayCapital(), feeRate, periodNow, differentialRate, 1, recoverCapital,
							borrowPeriod, borrowVerifyTime, manageFee);
				} else {
					perManage = AccountManagementFeeUtils.getMonthAccountManagementFee(
							debtCreditRepay.getRepayCapital(), feeRate, periodNow, differentialRate, 0, recoverCapital,
							borrowPeriod, borrowVerifyTime);
				}
			}
			// 先息后本endmonth
			else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
				BigDecimal tenderAssignCapital = debtCreditTender.getAssignCapital();
				BigDecimal sellerCapital = this.selectDebtCreditAssignSum(debtCreditTender);
				tenderAssignCapital = tenderAssignCapital.subtract(sellerCapital);
				if (periodNow.intValue() == borrowPeriod.intValue()) {
					perManage = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(
							tenderAssignCapital, feeRate, borrowPeriod, periodNow, differentialRate, 1,
							borrowVerifyTime);
				} else {
					perManage = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(
							tenderAssignCapital, feeRate, borrowPeriod, periodNow, differentialRate, 0,
							borrowVerifyTime);
				}
			}
			// 判断是否是清算日前三天还款
			boolean isLiquidatesPlanFlag = this.isLiquidatesPlan(debtCreditRepay.getAssignPlanNid());
			// 如果是延期逾期
			if (advanceStatus == 2 || advanceStatus == 3) {
				serviceFee = borrowRepayInterest.subtract(receiveInterest);
			} else if (isLiquidatesPlanFlag) {
				// 汇添金服务费
				serviceFee = this.calculateServiceFee(debtCreditRepay.getAssignPlanOrderId(), receiveAccount);
				if (serviceFee.compareTo(BigDecimal.ZERO) > 0
						&& receiveInterest.subtract(serviceFee).compareTo(BigDecimal.ZERO) < 0) {
					serviceFee = debtCreditRepay.getRepayInterest();
				}
				receiveInterest = receiveInterest.subtract(serviceFee);
				receiveAccount = receiveAccount.subtract(serviceFee);
			}
			// 债权承接人在汇付的账户信息
			AccountChinapnr creditUserCust = getChinapnrUserInfo(debtCreditRepay.getUserId());
			if (creditUserCust == null) {
				throw new Exception("债权承接人未开户。[用户ID：" + debtCreditRepay.getUserId() + "]，" + "[承接订单号："
						+ debtCreditRepay.getAssignOrderId() + "]");
			}
			Long creditUserCustId = creditUserCust.getChinapnrUsrcustid();
			// 查询债转还款状态 调用交易查询接口
			ChinapnrBean queryCreditTransStatBean = queryTransStat(repayOrderId, repayOrderDate + "", "REPAYMENT");
			String creditRespCode = queryCreditTransStatBean == null ? "" : queryCreditTransStatBean.getRespCode();
			// 调用接口失败时(000,422以外)
			if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(creditRespCode)
					&& !ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(creditRespCode)) {
				String message = queryCreditTransStatBean == null ? "" : queryCreditTransStatBean.getRespDesc();
				LogUtil.errorLog(THIS_CLASS, methodName,
						"调用交易查询接口失败。" + message + "，[承接订单号：" + debtCreditRepay.getAssignOrderId() + "]", null);
				throw new Exception("调用交易查询接口失败。" + creditRespCode + "：" + message + "，[承接订单号："
						+ debtCreditRepay.getAssignOrderId() + "]");
			}
			// 汇付交易状态
			String creditTransStat = queryCreditTransStatBean.getTransStat();

			// I:初始 P:部分成功
			if (ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(creditRespCode)
					|| (!"I".equals(creditTransStat) && !"P".equals(creditTransStat))) {
				// 分账账户串（当 管理费！=0 时是必填项）
				String divDetails = "";
				if (perManage.compareTo(BigDecimal.ZERO) > 0) {
					JSONArray ja = new JSONArray();
					JSONObject jo = new JSONObject();
					// 分账商户号(商户号,从配置文件中取得)
					jo.put(ChinaPnrConstant.PARAM_DIVCUSTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
					// 分账账户号(子账户号,从配置文件中取得)
					jo.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT03));
					// 分账金额
					jo.put(ChinaPnrConstant.PARAM_DIVAMT, perManage.toString());
					ja.add(jo);
					if (serviceFee.compareTo(BigDecimal.ZERO) > 0) {
						JSONObject jo1 = new JSONObject();
						// 分账商户号(商户号,从配置文件中取得)
						jo1.put(ChinaPnrConstant.PARAM_DIVCUSTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
						// 分账账户号(子账户号,从配置文件中取得)
						jo1.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT17));
						// 分账金额
						jo1.put(ChinaPnrConstant.PARAM_DIVAMT, serviceFee.toString());
						ja.add(jo1);
					}
					divDetails = ja.toString();
				}
				// 入参扩展域(2.0用)
				String reqExts = "";
				// 调用汇付接口
				if (receiveAccount.compareTo(BigDecimal.ZERO) > 0) {
					ChinapnrBean repaymentBean = null;
					repaymentBean = repayment(borrowNid, borrowUserid, String.valueOf(borrowUserCustId),
							debtCreditRepay.getRepayCapital().toString(), receiveInterest.toString(), perManage,
							serviceFee, repayOrderId, repayOrderDate, debtCreditRepay.getAssignOrderId(),
							String.valueOf(debtCreditRepay.getAssignCreateDate()), String.valueOf(creditUserCustId),
							divDetails, reqExts, bankInputFlag);
					creditRespCode = repaymentBean == null ? "" : repaymentBean.getRespCode();
					// 调用接口失败时(000以外)
					if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(creditRespCode)
							&& !ChinaPnrConstant.RESPCODE_REPEAT_REPAY.equals(creditRespCode)) {
						String message = repaymentBean == null ? "" : repaymentBean.getRespDesc();
						LogUtil.errorLog(THIS_CLASS, methodName,
								"调用自动扣款（还款）接口失败。" + message + "，[承接订单号：" + debtCreditRepay.getAssignOrderId() + "]",
								null);
						throw new Exception("调用债转自动扣款（还款）接口失败。" + creditRespCode + "：" + message + "，[承接订单号："
								+ debtCreditRepay.getAssignOrderId() + "]");
					}
				}
			}
			// 该笔债转的收支明细存在时,跳出本次循环
			if (countAccountCreditListByNid(repayOrderId) > 0) {
				return true;
			}
			// 债转的下次还款时间
			int creditRepayNextTime = debtCreditRepay.getAssignRepayNextTime();
			// 更新承接人账户信息
			Account account = new Account();
			account.setUserId(debtCreditRepay.getUserId());
			// 承接人计划可用余额
			account.setPlanBalance(receiveAccount);
			// 非清算时,订单加入可用余额
			if (!isLiquidatesPlanFlag) {
				account.setPlanAccedeBalance(receiveAccount);
			}
			// 更新承接人账户信息
			boolean creditUserAccountFlag = this.adminAccountCustomizeMapper.updateOfPlanBalance(account) > 0 ? true
					: false;
			if (!creditUserAccountFlag) {
				throw new Exception(
						"承接人资金记录(huiyingdai_account)更新失败！" + "[承接订单号：" + debtCreditRepay.getAssignOrderId() + "]");
			}
			// 根据加入订单号 查询加入信息
			DebtPlanAccede debtPlanAccede = this.getDebtPlanAccedeInfo(debtCreditRepay.getAssignPlanOrderId());
			if (Validator.isNull(debtPlanAccede)) {
				throw new Exception("用户计划加入记录不存在!" + "[加入订单号：" + debtCreditRepay.getAssignPlanOrderId());
			}
			boolean debtPlanAccedeUpdateFlag = false;
			DebtPlanAccede upateDebtPlanAccede = new DebtPlanAccede();
			// 清算前三天还款
			if (isLiquidatesPlanFlag) {
				upateDebtPlanAccede.setLiquidatesRepayFrost(receiveAccount);
				upateDebtPlanAccede.setId(debtPlanAccede.getId());
				upateDebtPlanAccede.setServiceFee(serviceFee);
				debtPlanAccedeUpdateFlag = this.batchDebtPlanAccedeCustomizeMapper
						.updateDebtPlanLiquidatesBalance(upateDebtPlanAccede) > 0 ? true : false;
			} else {
				upateDebtPlanAccede.setAccedeBalance(receiveAccount);
				upateDebtPlanAccede.setServiceFee(serviceFee);
				upateDebtPlanAccede.setId(debtPlanAccede.getId());
				debtPlanAccedeUpdateFlag = this.batchDebtPlanAccedeCustomizeMapper
						.updateDebtPlanRepayBalance(upateDebtPlanAccede) > 0 ? true : false;
			}
			if (!debtPlanAccedeUpdateFlag) {
				throw new Exception("更新用户计划账户失败!" + "[承接订单号：" + debtCreditRepay.getAssignOrderId() + "]" + "[期数："
						+ periodNow + "]");
			}
			// 取得承接人账户信息
			account = this.getAccountByUserId(debtCreditRepay.getUserId());
			// 根据加入订单号 重新查询加入信息
			debtPlanAccede = this.getDebtPlanAccedeInfo(debtCreditRepay.getAssignPlanOrderId());
			// 写入承接人收支明细
			DebtAccountList accountList = new DebtAccountList();
			accountList.setNid(repayOrderId); // 还款订单号
			accountList.setUserId(debtCreditRepay.getUserId());// 承接人
			accountList.setUserName(debtCreditTender.getUserName());// 承接人用户名
			accountList.setAmount(receiveAccount);// 承接人总收入
			accountList.setPlanNid(debtCreditRepay.getAssignPlanNid()); // 计划编号
			accountList.setPlanOrderId(debtCreditRepay.getAssignPlanOrderId());// 计划订单号
			accountList.setPlanBalance(account.getPlanBalance());// 计划余额
			accountList.setPlanFrost(account.getPlanFrost());
			accountList.setPlanOrderBalance(debtPlanAccede.getAccedeBalance());// 计划订单余额
			accountList.setPlanOrderFrost(debtPlanAccede.getAccedeFrost());// 计划订单冻结
			UsersInfo userInfo = getUsersInfoByUserId(debtCreditRepay.getUserId());
			// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
			Integer attribute = null;
			if (Validator.isNotNull(userInfo)) {
				// 获取出借用户的用户属性
				attribute = userInfo.getAttribute();
				if (Validator.isNotNull(attribute)) {
					if (attribute == 1) {
						SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
						SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
						spreadsUsersExampleCriteria.andUserIdEqualTo(debtCreditRepay.getUserId());
						List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
						if (sList != null && sList.size() == 1) {
							int refUserId = sList.get(0).getSpreadsUserid();
							// 查找用户推荐人
							Users refererUser = getUsersByUserId(refUserId);
							if (Validator.isNotNull(refererUser)) {
								accountList.setRefererUserId(refererUser.getUserId());// 推荐人用户ID
								accountList.setRefererUserName(refererUser.getUsername());// 推荐人用户名
							}
						}
					} else if (attribute == 0) {
						SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
						SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
						spreadsUsersExampleCriteria.andUserIdEqualTo(debtCreditRepay.getUserId());
						List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
						if (sList != null && sList.size() == 1) {
							int refUserId = sList.get(0).getSpreadsUserid();
							// 查找推荐人
							Users refererUser = getUsersByUserId(refUserId);
							if (Validator.isNotNull(refererUser)) {
								accountList.setRefererUserId(refererUser.getUserId());// 推荐人用户ID
								accountList.setRefererUserName(refererUser.getUsername());// 推荐人用户名
							}
						}
					}
				}
			}
			accountList.setType(1); // 1收入
			accountList.setTrade("credit_tender_recover_yes");// 承接人收到还款成功
			accountList.setTradeCode("balance"); // 余额操作
			accountList.setTotal(account.getTotal()); // 出借人资金总额
			accountList.setBalance(account.getBalance()); // 出借人可用金额
			accountList.setFrost(account.getFrost()); // 出借人冻结金额
			accountList.setAccountWait(BigDecimal.ZERO); // 出借人待收金额
			accountList.setCapitalWait(BigDecimal.ZERO);// 待收本金
			accountList.setInterestWait(BigDecimal.ZERO);// 待还收益
			accountList.setRepayWait(BigDecimal.ZERO);// 待还金额
			accountList.setCreateTime(nowTime);// 创建时间
			accountList.setUpdateTime(nowTime);// 更新时间
			accountList.setRemark(debtCreditRepay.getAssignPlanNid()); // 计划编号
			accountList.setIp(borrow.getAddip()); // 操作IP
			accountList.setWeb(0); // PC
			accountList.setCreateUserId(debtCreditRepay.getUserId());// 承接人ID
			accountList.setCreateUserName(debtCreditRepay.getUserName());// 承接人用户名
			boolean creditUserAccountListFlag = insertAccountList(accountList) > 0 ? true : false;
			if (!creditUserAccountListFlag) {
				throw new Exception(
						"收支明细(hyjf_debt_account_list)写入失败！" + "[承接订单号：" + debtCreditRepay.getAssignOrderId() + "]");
			}
			// 更新相应的债转出借表
			// 债转已还款总额
			debtCreditTender.setRepayAccountYes(debtCreditTender.getRepayAccountYes().add(borrowRepayAccount));
			// 债转已还款本金
			debtCreditTender
					.setRepayCapitalYes(debtCreditTender.getRepayCapitalYes().add(debtCreditRepay.getRepayCapital()));
			// 债转已还款利息
			debtCreditTender.setRepayInterestYes(debtCreditTender.getRepayInterestYes().add(borrowRepayInterest));
			// 债转未还款总额
			debtCreditTender.setRepayAccountWait(
					debtCreditTender.getRepayAccountWait().subtract(debtCreditRepay.getRepayAccount()));
			// 债转未还款本金
			debtCreditTender.setRepayCapitalWait(
					debtCreditTender.getRepayCapitalWait().subtract(debtCreditRepay.getRepayCapital()));
			// 债转未还款利息
			debtCreditTender.setRepayInterestWait(
					debtCreditTender.getRepayInterestWait().subtract(debtCreditRepay.getRepayInterest()));
			// 债转最近还款时间
			debtCreditTender.setAssignRepayLastTime(!isMonth ? nowTime : 0);
			// 债转下次还款时间
			debtCreditTender.setAssignRepayNextTime(!isMonth ? 0 : creditRepayNextTime);
			// 债转最后还款时间
			debtCreditTender.setAssignRepayYesTime(!isMonth ? nowTime : 0);
			// 债转还款状态
			if (isMonth) {
				// 债转状态
				if (debtLoanDetail != null && Validator.isNotNull(periodNext) && periodNext > 0) {
					debtCreditTender.setStatus(0);
				} else {
					debtCreditTender.setStatus(1);
				}
			} else {
				debtCreditTender.setStatus(1);
			}
			// 债转还款期
			debtCreditTender.setRepayPeriod(periodNow);
			boolean creditTenderFlag = this.debtCreditTenderMapper.updateByPrimaryKeySelective(debtCreditTender) > 0
					? true : false;
			if (!creditTenderFlag) {
				throw new Exception(
						"债转出借表(hyjf_debt_credit_tender)更新失败！" + "[承接订单号：" + debtCreditRepay.getAssignOrderId() + "]");
			}

			debtCreditRepay.setRepayAccountYes(debtCreditRepay.getRepayAccountYes().add(borrowRepayAccount));
			debtCreditRepay
					.setRepayCapitalYes(debtCreditRepay.getRepayCapitalYes().add(debtCreditRepay.getRepayCapital()));
			debtCreditRepay.setRepayInterestYes(debtCreditRepay.getRepayInterestYes().add(borrowRepayInterest));
			debtCreditRepay.setRepayAccountWait(
					debtCreditRepay.getRepayAccountWait().subtract(debtCreditRepay.getRepayAccount()));
			debtCreditRepay.setRepayCapitalWait(
					debtCreditRepay.getRepayCapitalWait().subtract(debtCreditRepay.getRepayCapital()));
			debtCreditRepay.setRepayInterestWait(
					debtCreditRepay.getRepayInterestWait().subtract(debtCreditRepay.getRepayInterest()));
			debtCreditRepay.setAssignRepayLastTime(nowTime);
			debtCreditRepay.setAssignRepayYesTime(nowTime);
			debtCreditRepay.setManageFee(perManage);
			debtCreditRepay.setAdvanceStatus(advanceStatus);
			debtCreditRepay.setAdvanceDays(advanceDays);
			debtCreditRepay.setDelayDays(delayDays);
			debtCreditRepay.setLateDays(lateDays);
			debtCreditRepay.setRepayAdvanceInterest(borrowRepayAdvanceInterest);
			debtCreditRepay.setRepayDelayInterest(borrowRepayDelayInterest);
			debtCreditRepay.setRepayLateInterest(borrowRepayLateInterest);
			debtCreditRepay.setReceiveAccountYes(receiveAccount);
			debtCreditRepay.setReceiveCapitalYes(debtCreditRepay.getRepayCapital());
			debtCreditRepay.setReceiveInterestYes(receiveInterest);
			debtCreditRepay.setReceiveAdvanceInterestYes(receiveAdvanceInterest);
			debtCreditRepay.setReceiveDelayInterestYes(receiveDelayInterest);
			debtCreditRepay.setReceiveLateInterestYes(receiveLateInterest);
			debtCreditRepay.setRepayStatus(1);
			boolean creditRepayFlag = this.debtCreditRepayMapper.updateByPrimaryKeySelective(debtCreditRepay) > 0 ? true
					: false;
			if (!creditRepayFlag) {
				throw new Exception(
						"承接人还款表(hyjf_debt_credit_repay)更新失败！" + "[债转编号：" + debtCreditRepay.getCreditNid() + "]");
			}
			// 债转总表数据更新
			// 更新债转已还款总额
			debtCredit.setRepayAccount(debtCredit.getRepayAccount().add(borrowRepayAccount));
			// 更新债转已还款本金
			debtCredit.setRepayCapital(debtCredit.getRepayCapital().add(debtCreditRepay.getRepayCapital()));
			// 更新债转已还款利息
			debtCredit.setRepayInterest(debtCredit.getRepayInterest().add(borrowRepayInterest));
			// 更新债转未还款本息
			debtCredit
					.setRepayAccountWait(debtCredit.getRepayAccountWait().subtract(debtCreditRepay.getRepayAccount()));
			// 更新债转未还款本金
			debtCredit
					.setRepayCapitalWait(debtCredit.getRepayCapitalWait().subtract(debtCreditRepay.getRepayCapital()));
			// 更新债转未还款利息
			debtCredit.setRepayInterestWait(
					debtCredit.getRepayInterestWait().subtract(debtCreditRepay.getRepayInterest()));
			// 债转下次还款时间
			debtCredit.setCreditRepayNextTime(isMonth ? creditRepayNextTime : 0);
			// 更新债转总表
			boolean borrowCreditFlag = this.debtCreditMapper.updateByPrimaryKeySelective(debtCredit) > 0 ? true : false;
			// 债转总表更新成功
			if (!borrowCreditFlag) {
				throw new Exception("债转记录(hyjf_debt_credit)更新失败！" + "[承接订单号：" + debtCreditRepay.getCreditNid() + "]");
			}
			// 已还款总额
			debtLoan.setRepayAccountYes(debtLoan.getRepayAccountYes().add(borrowRepayAccount));
			// 已还款本金
			debtLoan.setRepayCapitalYes(debtLoan.getRepayCapitalYes().add(debtCreditRepay.getRepayCapital()));
			// 已还款利息
			debtLoan.setRepayInterestYes(debtLoan.getRepayInterestYes().add(borrowRepayInterest));
			// 待还金额
			debtLoan.setRepayAccountWait(debtLoan.getRepayAccountWait().subtract(debtCreditRepay.getRepayAccount()));
			// 待还利息
			debtLoan.setRepayInterestWait(debtLoan.getRepayInterestWait().subtract(debtCreditRepay.getRepayInterest()));
			// 待还本金
			debtLoan.setRepayCapitalWait(debtLoan.getRepayCapitalWait().subtract(debtCreditRepay.getRepayCapital()));
			// 借款人已还提前还款利息
			debtLoan.setRepayAdvanceInterestYes(debtLoan.getRepayAdvanceInterestYes().add(borrowRepayAdvanceInterest));
			// 借款人已还延期还款利息
			debtLoan.setRepayDelayInterestYes(debtLoan.getRepayDelayInterestYes().add(borrowRepayDelayInterest));
			// 借款人已还逾期还款利息
			debtLoan.setRepayLateInterestYes(debtLoan.getRepayLateInterestYes().add(borrowRepayLateInterest));
			// 出借人收取已还提前还款利息
			debtLoan.setReceiveAdvanceInterestYes(debtLoan.getReceiveAdvanceInterestYes().add(receiveAdvanceInterest));
			// 出借人收取已还延期还款利息
			debtLoan.setReceiveDelayInterestYes(debtLoan.getReceiveDelayInterestYes().add(receiveDelayInterest));
			// 出借人收取已还逾期还款利息
			debtLoan.setReceiveLateInterestYes(debtLoan.getReceiveLateInterestYes().add(receiveLateInterest));
			// 出借人收取已还还款利息
			debtLoan.setReceiveAccountYes(debtLoan.getReceiveAccountYes().add(receiveAccount));
			// 出借人收取已还本金
			debtLoan.setReceiveCapitalYes(debtLoan.getReceiveCapitalYes().add(debtCreditRepay.getRepayCapital()));
			// 出借人收取已还利息
			debtLoan.setReceiveInterestYes(debtLoan.getReceiveInterestYes().add(receiveInterest));
			// 更新还款表
			boolean creditBorrowRecoverFlag = this.debtLoanMapper.updateByPrimaryKeySelective(debtLoan) > 0 ? true
					: false;
			if (!creditBorrowRecoverFlag) {
				throw new Exception("出借人还款表(hyjf_debt_loan)更新失败！" + "[债转编号：" + debtCreditRepay.getCreditNid() + "]");
			}
			// 已还款本息包含管理费
			debtRepay.setRepayAccountAll(debtRepay.getRepayAccountAll().add(borrowRepayAccount).add(perManage));
			// 已还款本息
			debtRepay.setRepayAccountYes(debtRepay.getRepayAccountYes().add(borrowRepayAccount));
			// 已还款利息
			debtRepay.setRepayInterestYes(debtRepay.getRepayInterestYes().add(borrowRepayInterest));
			// 已还款本金
			debtRepay.setRepayCapitalYes(debtRepay.getRepayCapitalYes().add(debtCreditRepay.getRepayCapital()));
			// 清算服务费
			debtRepay.setLiquidatesServiceFee(debtRepay.getLiquidatesServiceFee().add(serviceFee));
			debtRepay.setAdvanceStatus(debtLoan.getAdvanceStatus()); // 用户是否提前还款
			debtRepay.setAdvanceDays(advanceDays);
			debtRepay.setAdvanceInterest(debtRepay.getAdvanceInterest().add(borrowRepayAdvanceInterest));
			debtRepay.setDelayDays(delayDays);
			debtRepay.setDelayInterest(debtRepay.getDelayInterest().add(borrowRepayDelayInterest));
			debtRepay.setLateDays(lateDays);
			debtRepay.setLateInterest(debtRepay.getLateInterest().add(borrowRepayLateInterest));
			boolean creditBorrowRepayFlag = this.debtRepayMapper.updateByPrimaryKeySelective(debtRepay) > 0 ? true
					: false;
			if (!creditBorrowRepayFlag) {
				throw new Exception("总的还款明细表(huiyingdai_borrow_repay)更新失败！" + "[出借订单号：" + investOrderId + "]");
			}
			// 如果分期
			if (isMonth) {
				// 已还款总额
				debtLoanDetail.setRepayAccountYes(debtLoanDetail.getRepayAccountYes().add(borrowRepayAccount));
				// 已还款本金
				debtLoanDetail
						.setRepayCapitalYes(debtLoanDetail.getRepayCapitalYes().add(debtCreditRepay.getRepayCapital()));
				// 已还款利息
				debtLoanDetail.setRepayInterestYes(debtLoanDetail.getRepayInterestYes().add(borrowRepayInterest));
				// 待还金额
				debtLoanDetail.setRepayAccountWait(
						debtLoanDetail.getRepayAccountWait().subtract(debtCreditRepay.getRepayAccount()));
				// 待还利息
				debtLoanDetail.setRepayInterestWait(
						debtLoanDetail.getRepayInterestWait().subtract(debtCreditRepay.getRepayInterest()));
				// 待还本金
				debtLoanDetail.setRepayCapitalWait(
						debtLoanDetail.getRepayCapitalWait().subtract(debtCreditRepay.getRepayCapital()));
				// 借款人已还提前还款利息
				debtLoanDetail.setRepayAdvanceInterestYes(
						debtLoanDetail.getRepayAdvanceInterestYes().add(borrowRepayAdvanceInterest));
				// 借款人已还延期还款利息
				debtLoanDetail.setRepayDelayInterestYes(
						debtLoanDetail.getRepayDelayInterestYes().add(borrowRepayDelayInterest));
				// 借款人已还逾期还款利息
				debtLoanDetail
						.setRepayLateInterestYes(debtLoanDetail.getRepayLateInterestYes().add(borrowRepayLateInterest));
				// 出借人收取已还提前还款利息
				debtLoanDetail.setReceiveAdvanceInterestYes(
						debtLoanDetail.getReceiveAdvanceInterestYes().add(receiveAdvanceInterest));
				// 出借人收取已还延期还款利息
				debtLoanDetail.setReceiveDelayInterestYes(
						debtLoanDetail.getReceiveDelayInterestYes().add(receiveDelayInterest));
				// 出借人收取已还逾期还款利息
				debtLoanDetail
						.setReceiveLateInterestYes(debtLoanDetail.getReceiveLateInterestYes().add(receiveLateInterest));
				// 出借人收取已还还款利息
				debtLoanDetail.setReceiveAccountYes(debtLoanDetail.getReceiveAccountYes().add(receiveAccount));
				// 出借人收取已还本金
				debtLoanDetail.setReceiveCapitalYes(
						debtLoanDetail.getReceiveCapitalYes().add(debtCreditRepay.getRepayCapital()));
				// 出借人收取已还利息
				debtLoanDetail.setReceiveInterestYes(debtLoanDetail.getReceiveInterestYes().add(receiveInterest));
				// 更新还款计划表
				boolean borrowRecoverPlanFlag = this.debtLoanDetailMapper
						.updateByPrimaryKeySelective(debtLoanDetail) > 0 ? true : false;
				if (!borrowRecoverPlanFlag) {
					throw new Exception("分期还款计划表更新失败。[借款编号：" + borrowNid + "]，" + "[承接订单号："
							+ debtCreditRepay.getAssignOrderId() + "]" + "[期数：" + periodNow + "]");
				}
				// 更新总的还款计划
				DebtRepayDetail debtRepayDetail = getBorrowRepayPlan(borrowNid, periodNow);
				if (debtRepayDetail == null) {
					throw new Exception("还款计划表(hyjf_debt_repay_detail)查询失败！" + "[承接订单号："
							+ debtCreditRepay.getAssignOrderId() + "]" + "[期数：" + periodNow + "]");
				}
				// 还款总额
				debtRepayDetail.setRepayAccountAll(
						debtRepayDetail.getRepayAccountAll().add(borrowRepayAccount).add(perManage));
				// 已还金额
				debtRepayDetail.setRepayAccountYes(debtRepayDetail.getRepayAccountYes().add(borrowRepayAccount));
				// 已还利息
				debtRepayDetail.setRepayInterestYes(debtRepayDetail.getRepayInterestYes().add(borrowRepayInterest));
				// 已还本金
				debtRepayDetail.setRepayCapitalYes(
						debtRepayDetail.getRepayCapitalYes().add(debtCreditRepay.getRepayCapital()));
				// 清算时已收服务费
				debtRepayDetail.setLiquidatesServiceFee(debtRepayDetail.getLiquidatesServiceFee().add(serviceFee));
				debtRepayDetail.setLateDays(lateDays);
				debtRepayDetail.setLateInterest(debtRepayDetail.getLateInterest().add(borrowRepayLateInterest));
				debtRepayDetail.setDelayDays(delayDays);
				debtRepayDetail.setDelayInterest(debtRepayDetail.getDelayInterest().add(borrowRepayDelayInterest));
				debtRepayDetail.setAdvanceDays(advanceDays);
				debtRepayDetail
						.setAdvanceInterest(debtRepayDetail.getAdvanceInterest().add(borrowRepayAdvanceInterest));
				// 用户是否提前还款
				debtRepay.setAdvanceStatus(advanceStatus);
				// 用户是否提前还款
				debtRepayDetail.setAdvanceStatus(advanceStatus);
				boolean debtRepayDetailFlag = this.debtRepayDetailMapper
						.updateByPrimaryKeySelective(debtRepayDetail) > 0 ? true : false;
				if (!debtRepayDetailFlag) {
					throw new Exception("还款计划表(hyjf_debt_repay_detail)更新失败！" + "[承接订单号："
							+ debtCreditRepay.getAssignOrderId() + "]" + "[期数：" + periodNow + "]");
				}
			}
			debtDetail.setRepayActionTime(nowTime);
			// 已还款
			debtDetail.setRepayStatus(1);
			// 账户管理费
			debtDetail.setManageFee(perManage);
			// 已还本金
			debtDetail.setRepayCapitalYes(debtCreditRepay.getRepayCapital());
			// 账户服务费
			debtDetail.setServiceFee(serviceFee);
			// 已还利息
			debtDetail.setRepayInterestYes(receiveInterest);
			// 未收本金
			debtDetail.setRepayCapitalWait(BigDecimal.ZERO);
			// 未收利息
			debtDetail.setRepayInterestWait(BigDecimal.ZERO);
			// 提前还款状态
			debtDetail.setAdvanceStatus(advanceStatus);
			// 提前还款天数
			debtDetail.setAdvanceDays(advanceDays);
			// 提前还款利息
			debtDetail.setAdvanceInterest(receiveAdvanceInterest);
			// 延期天数
			debtDetail.setDelayDays(delayDays);
			// 延期利息
			debtDetail.setDelayInterest(receiveDelayInterest);
			// 逾期天数
			debtDetail.setLateDays(lateDays);
			// 逾期利息
			debtDetail.setLateInterest(receiveLateInterest);
			// 还款订单号
			debtDetail.setRepayOrderId(repayOrderId);
			// 还款日期
			debtDetail.setRepayOrderDate(repayOrderDate);
			// 债权更新时间
			debtDetail.setUpdateTime(nowTime);
			// 到期公允价值
			debtDetail.setExpireFairValue(BigDecimal.ZERO);
			// 债权详情表更新
			boolean debtDetailFlg = this.debtDetailMapper.updateByPrimaryKeySelective(debtDetail) > 0 ? true : false;
			if (!debtDetailFlg) {
				throw new Exception(
						"还款分期计划表(hyjf_debt_detail)更新失败！" + "[承接订单号：" + debtCreditRepay.getAssignOrderId() + "]");
			}
			// 更新借款表
			borrow = getBorrow(borrowNid);
			DebtBorrowWithBLOBs newBrrow = new DebtBorrowWithBLOBs();
			newBrrow.setId(borrow.getId());
			BigDecimal borrowManager = borrow.getBorrowManager() == null ? BigDecimal.ZERO
					: new BigDecimal(borrow.getBorrowManager());
			newBrrow.setBorrowManager(borrowManager.add(perManage).toString());
			// 总还款利息
			newBrrow.setRepayAccountYes(borrow.getRepayAccountYes().add(borrowRepayAccount));
			// 总还款利息
			newBrrow.setRepayAccountInterestYes(borrow.getRepayAccountInterestYes().add(borrowRepayInterest));
			// 总还款本金
			newBrrow.setRepayAccountCapitalYes(
					borrow.getRepayAccountCapitalYes().add(debtCreditRepay.getRepayCapital()));
			// 未还款总额
			newBrrow.setRepayAccountWait(borrow.getRepayAccountWait().subtract(debtCreditRepay.getRepayAccount()));
			// 未还款利息
			newBrrow.setRepayAccountInterestWait(
					borrow.getRepayAccountInterestWait().subtract(debtCreditRepay.getRepayInterest()));
			// 未还款本金
			newBrrow.setRepayAccountCapitalWait(
					borrow.getRepayAccountCapitalWait().subtract(debtCreditRepay.getRepayCapital()));
			// 项目的管理费
			newBrrow.setRepayFeeNormal(borrow.getRepayFeeNormal().add(perManage));
			boolean borrowFlag = this.debtBorrowMapper.updateByPrimaryKeySelective(newBrrow) > 0 ? true : false;
			if (!borrowFlag) {
				throw new Exception("借款详情(hyjf_debt_borrow)更新失败！" + "[出借订单号：" + investOrderId + "]");
			}
			// 更新出借表
			// 已还款金额
			debtInvest.setRepayAccountYes(debtInvest.getRepayAccountYes().add(borrowRepayAccount));
			// 已还款利息
			debtInvest.setRepayInterestYes(debtInvest.getRepayInterestYes().add(borrowRepayInterest));
			// 已还款本金
			debtInvest.setRepayCapitalYes(debtInvest.getRepayCapitalYes().add(debtCreditRepay.getRepayCapital()));
			// 待还金额
			debtInvest
					.setRepayAccountWait(debtInvest.getRepayAccountWait().subtract(debtCreditRepay.getRepayAccount()));
			// 待还利息
			debtInvest.setRepayInterestWait(
					debtInvest.getRepayInterestWait().subtract(debtCreditRepay.getRepayInterest()));
			// 待还本金
			debtInvest
					.setRepayCapitalWait(debtInvest.getRepayCapitalWait().subtract(debtCreditRepay.getRepayCapital()));
			boolean borrowTenderFlag = debtInvestMapper.updateByPrimaryKeySelective(debtInvest) > 0 ? true : false;
			if (!borrowTenderFlag) {
				throw new Exception("出借表(hyjf_debt_invest)更新失败！" + "[出借订单号：" + investOrderId + "]");
			}
			DebtPlan plan = new DebtPlan();
			plan.setDebtPlanNid(debtCreditRepay.getAssignPlanNid());
			// 清算时还款
			if (isLiquidatesPlanFlag) {
				plan.setLiquidateArrivalAmount(receiveAccount);
			} else {
				// 非清算还款
				plan.setDebtPlanBalance(receiveAccount);
			}
			if (serviceFee.compareTo(BigDecimal.ZERO) > 0) {
				plan.setServiceFee(serviceFee);
			}
			boolean planUpdateFlag = this.batchDebtPlanCustomizeMapper.updatePlanCredit(plan) > 0 ? true : false;
			if (!planUpdateFlag) {
				throw new Exception(
						"债权详情表(hyjf_debt_plan)更新失败!" + "[债转承接订单号:" + debtCreditRepay.getAssignOrderId() + "]");
			}
			// 管理费大于0时,插入网站收支明细
			if (perManage.compareTo(BigDecimal.ZERO) > 0) {
				// 插入网站收支明细记录
				AccountWebList accountWebList = new AccountWebList();
				accountWebList.setOrdid(debtCreditRepay.getAssignOrderId() + "_" + periodNow);// 订单号
				accountWebList.setBorrowNid(borrowNid); // 出借编号
				accountWebList.setUserId(borrowUserid); // 借款人
				accountWebList.setAmount(perManage); // 管理费
				accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
				accountWebList.setTrade(CustomConstants.TRADE_REPAYFEE); // 管理费
				accountWebList.setTradeType(CustomConstants.TRADE_REPAYFEE_NM); // 账户管理费
				accountWebList.setRemark(borrowNid); // 出借编号
				accountWebList.setCreateTime(nowTime);
				boolean accountWebFlag = insertAccountWebList(accountWebList) > 0 ? true : false;
				if (!accountWebFlag) {
					throw new Exception("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[债转承接订单号："
							+ debtCreditRepay.getAssignOrderId() + "]");
				}
			}
			// 服务费大于0时,插入网站收支明细
			if (serviceFee.compareTo(BigDecimal.ZERO) > 0) {
				// 插入网站收支明细记录
				AccountWebList accountWebList = new AccountWebList();
				accountWebList.setOrdid(debtCreditRepay.getAssignOrderId() + borrowNid + "_" + periodNow);// 订单号
				accountWebList.setBorrowNid(borrowNid); // 出借编号
				accountWebList.setUserId(borrowUserid); // 借款人
				accountWebList.setAmount(serviceFee); // 管理费
				accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
				accountWebList.setTrade(CustomConstants.HTJ_TRADE_LOANFEE); // 汇添金服务费
				accountWebList.setTradeType(CustomConstants.HTJ_TRADE_LOANFEE_NM); // 汇添金服务费-还款
				accountWebList.setRemark(borrowNid); // 出借编号
				accountWebList.setCreateTime(nowTime);
				int accountWebListCnt = insertAccountWebList(accountWebList);
				if (accountWebListCnt == 0) {
					throw new Exception("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号："
							+ debtCreditRepay.getAssignOrderId() + "]");
				}
			}
		}
		System.out.println(
				"------债转还款承接部分完成---承接订单号：" + debtCreditTender.getAssignOrderId() + "---------还款订单号" + repayOrderId);
		return true;
	}

	/**
	 * 获取债权详情
	 * 
	 * @Title getDebtDetail
	 * @param investOrderId
	 * @param repayPeriod
	 * @return
	 */
	private DebtDetail getDebtDetail(String investOrderId, Integer repayPeriod) {
		DebtDetailExample example = new DebtDetailExample();
		DebtDetailExample.Criteria cra = example.createCriteria();
		cra.andOrderIdEqualTo(investOrderId).andRepayPeriodEqualTo(repayPeriod).andStatusEqualTo(1);
		List<DebtDetail> resultList = this.debtDetailMapper.selectByExample(example);
		if (resultList != null && resultList.size() > 0) {
			return resultList.get(0);
		}
		return null;
	}

	/**
	 * 未承接完成的债权查询相应的二次债转记录中已被承接的金额
	 * 
	 * @param debtCreditTender
	 * @return
	 */
	private BigDecimal selectDebtCreditAssignSum(DebtCreditTender debtCreditTender) {
		BigDecimal assignSum = BigDecimal.ZERO;
		DebtCreditExample example = new DebtCreditExample();
		DebtCreditExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(debtCreditTender.getBorrowNid());
		crt.andSellOrderIdEqualTo(debtCreditTender.getAssignOrderId());
		List<DebtCredit> debtCreditList = this.debtCreditMapper.selectByExample(example);
		if (debtCreditList != null && debtCreditList.size() > 0) {
			for (DebtCredit debtCredit : debtCreditList) {
				assignSum = assignSum.add(debtCredit.getCreditCapitalAssigned());
			}
		}
		return assignSum;
	}

	/**
	 * 更新还款完成状态
	 *
	 * @param borrowNid
	 * @param periodNow
	 */
	@Override
	public void updateBorrowStatus(String borrowNid, Integer periodNow, Integer borrowUserId) {

		System.out.println("-----------债转还款完成，更新状态开始---" + borrowNid + "---------【还款期数】" + periodNow);
		// 当前时间
		int nowTime = GetDate.getNowTime10();

		// 查询recover
		DebtLoanExample loanExample = new DebtLoanExample();
		loanExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayStatusEqualTo(0);
		int recoverCnt = this.debtLoanMapper.countByExample(loanExample);
		// 项目详情
		DebtBorrow borrow = getBorrow(borrowNid);
		// 已还款本息
		BigDecimal repayAccount = BigDecimal.ZERO;
		// 还款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 剩余还款期数
		Integer periodNext = borrowPeriod - periodNow;
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle())
				|| CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle())
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle());
		int repayStatus = 0;
		String repayYesTime = "0";
		// 如果还款全部完成
		if (recoverCnt == 0) {
			repayStatus = 1;
			repayYesTime = String.valueOf(nowTime);
		}
		// 标的总表信息
		DebtBorrowWithBLOBs newBrrow = new DebtBorrowWithBLOBs();
		// 还款总表
		DebtRepay newBorrowRepay = new DebtRepay();
		newBorrowRepay.setRepayStatus(repayStatus); // 已还款
		newBorrowRepay.setRepayActionTime(String.valueOf(nowTime));// 实际还款时间、
		newBorrowRepay.setAlreadyRepayPeriod(periodNow);// 已经还款期数
		newBorrowRepay.setRemainPeriod(isMonth ? periodNext : 0);// 剩余期数
		if (periodNext == 0) {
			newBorrowRepay.setRepayPeriod(isMonth ? borrowPeriod : 1);// 下期应还第几期
		} else {
			newBorrowRepay.setRepayPeriod(isMonth ? periodNow + 1 : 1);// 下期应还第几期
		}
		if (isMonth) {
			// 分期的场合，根据借款编号和还款期数从还款计划表中取得还款时间
			DebtRepayDetailExample example = new DebtRepayDetailExample();
			Criteria borrowCriteria = example.createCriteria();
			borrowCriteria.andBorrowNidEqualTo(borrowNid);
			borrowCriteria.andRepayPeriodEqualTo(periodNow + 1);
			List<DebtRepayDetail> replayPlan = debtRepayDetailMapper.selectByExample(example);
			if (replayPlan.size() > 0) {
				DebtRepayDetail borrowRepayPlanNext = replayPlan.get(0);
				if (borrowRepayPlanNext != null) {
					// 取得下期还款时间
					String repayTime = borrowRepayPlanNext.getRepayTime();
					// 设置下期还款时间
					newBorrowRepay.setRepayTime(repayTime);
					// 设置下期还款时间
					newBrrow.setRepayNextTime(Integer.parseInt(repayTime));
				}
			} else {
				// 还款成功最后时间
				newBorrowRepay.setRepayActionTime(repayYesTime);
			}
			// 更新相应的还款计划表
			DebtRepayDetail debtRepayDetail = getBorrowRepayPlan(borrowNid, periodNow);
			if (debtRepayDetail != null) {
				debtRepayDetail.setRepayActionTime(String.valueOf(nowTime));
				debtRepayDetail.setRepayStatus(1);
				debtRepayDetail.setRepayActionTime(String.valueOf(nowTime));
				this.debtRepayDetailMapper.updateByPrimaryKeySelective(debtRepayDetail);
				// 已还款本息
				repayAccount = debtRepayDetail.getRepayAccountAll();
			}
		} else {
			// 还款成功最后时间
			newBorrowRepay.setRepayActionTime(repayYesTime);
			// 还款金额
			DebtRepay debtRepay = getBorrowRepay(borrowNid);
			// 已还款本息
			repayAccount = debtRepay.getRepayAccountAll();
		}
		// 更新BorrowRepay
		DebtRepayExample repayExample = new DebtRepayExample();
		repayExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayStatusEqualTo(0);
		this.debtRepayMapper.updateByExampleSelective(newBorrowRepay, repayExample);

		// 更新Borrow
		newBrrow.setRepayFullStatus(repayStatus);
		DebtBorrowExample borrowExample = new DebtBorrowExample();
		borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
		this.debtBorrowMapper.updateByExampleSelective(newBrrow, borrowExample);
		// add by wangkun 20170314
		// 前台还款金额
		BigDecimal repayWebAdvance = Validator.isNotNull(borrow.getBorrowRepayWebAdvance())
				? borrow.getBorrowRepayWebAdvance() : BigDecimal.ZERO;
		if (repayAccount.compareTo(repayWebAdvance) > 0) {
			System.out.println("还款出现错误，实际还款金额大于借款人还款金额，项目编号" + borrowNid + "---------【还款期数】" + periodNow);
		} else if (repayAccount.compareTo(repayWebAdvance) < 0) {
			BigDecimal distanceMoney = repayWebAdvance.subtract(repayAccount);
			AccountExample accountExample = new AccountExample();
			AccountExample.Criteria criteria = accountExample.createCriteria();
			criteria.andUserIdEqualTo(borrowUserId);
			List<Account> accountlist = accountMapper.selectByExample(accountExample);
			if (accountlist != null && accountlist.size() == 1) {
				Account accountBean = accountlist.get(0);
				accountBean.setTotal(accountBean.getTotal().add(distanceMoney));
				accountBean.setBalance(accountBean.getBalance().add(distanceMoney));
				accountMapper.updateByPrimaryKey(accountBean);
				AccountList accountList = new AccountList();
				accountList.setNid("");
				accountList.setUserId(borrowUserId);
				accountList.setAmount(distanceMoney);
				accountList.setFrost(accountBean.getFrost());
				accountList.setBalance(accountBean.getBalance());
				accountList.setInterest(new BigDecimal(0));
				accountList.setAwait(accountBean.getAwait());
				accountList.setPlanBalance(accountBean.getPlanBalance());
				accountList.setPlanFrost(accountBean.getPlanFrost());
				accountList.setIp("");
				accountList.setIsUpdate(0);
				accountList.setOperator(borrowUserId + "");
				accountList.setRemark(borrowNid);
				accountList.setRepay(new BigDecimal(0));
				accountList.setTotal(accountBean.getTotal());
				accountList.setTrade("cash_tiaozhang");// 计划加入
				accountList.setTradeCode("balance");//
				accountList.setType(1);// 收支类型1收入2支出3冻结
				accountList.setWeb(0);
				accountList.setBaseUpdate(0);
				accountList.setCreateTime(nowTime);
				System.out.println("加入计划用户:" + borrowUserId + "***********************************预插入accountList："
						+ JSON.toJSONString(accountList));
				accountListMapper.insertSelective(accountList);
			} else {
				throw new RuntimeException("查询还款人的账户信息失败，用户userId：" + borrowUserId);
			}
		}
		// add by wangkun 20170314
		System.out.println("-----------债转还款完成，更新状态完成---" + borrowNid + "---------【还款期数】" + periodNow);
	}

	/**
	 * 自动扣款（还款）(调用汇付天下接口3.0)
	 *
	 * @return
	 */
	private ChinapnrBean repayment(String borrowNid, Integer borrowUserId, String outCustId, String recoverCapital,
			String recoverInterestAll, BigDecimal manageFee, BigDecimal serviceFee, String ordId, String ordDate,
			String subOrdId, String subOrdDate, String inCustId, String divDetails, String reqExt, int bankInputFlag) {
		String methodName = "repayment";

		// 调用汇付接口(自动扣款（还款）)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_30); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_REPAYMENT); // 消息类型(必须)
		// 是否已切银行存管
		if (bankInputFlag == 1) {
			bean.setProId(borrowNid);// 标的id
		}
		bean.setOrdId(ordId); // 订单号(必须)
		bean.setOrdDate(ordDate); // 订单日期(必须)
		bean.setOutCustId(outCustId); // 出账客户号(必须)
		bean.setSubOrdId(subOrdId); // 订单号(必须)
		bean.setSubOrdDate(subOrdDate); // 订单日期(必须)
		bean.setPrincipalAmt(recoverCapital);// 本次还款本金
		bean.setInterestAmt(recoverInterestAll);// 还款利息
		BigDecimal totalFee = manageFee.add(serviceFee);
		bean.setFee(CustomUtil.formatAmount(totalFee.toString())); // 扣款手续费(必须)
		bean.setFeeObjFlag("O");// 向借款人收取手续费
		bean.setInCustId(inCustId); // 入账客户号(必须)
		bean.setDivDetails(divDetails); // 分账账户串(必须)
		bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)

		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("自动扣款（还款）"); // 备注
		bean.setLogClient("0"); // PC

		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean == null) {
			LogUtil.errorLog(THIS_CLASS, methodName, new Exception("调用自动扣款（还款）接口失败![参数：" + bean.getAllParams() + "]"));
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
	 * 取得还款API任务表
	 *
	 * @return
	 */
	@Override
	public List<DebtApicron> getBorrowApicronList(Integer apiType, Integer status, Integer creditStatus) {
		DebtApicronExample example = new DebtApicronExample();
		DebtApicronExample.Criteria criteria = example.createCriteria();
		criteria.andRepayStatusEqualTo(status);
		criteria.andApiTypeEqualTo(apiType);
		criteria.andCreditRepayStatusEqualTo(creditStatus);
		example.setOrderByClause(" id asc ");
		List<DebtApicron> list = this.debtApicronMapper.selectByExample(example);
		return list;
	}

	/**
	 * 更新借款债转任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	@Override
	public int updateBorrowApicron(Integer id, Integer status) {
		DebtApicron record = new DebtApicron();
		record.setId(id);
		record.setCreditRepayStatus(status);
		record.setUpdateTime(GetDate.getNowTime10());
		return this.debtApicronMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 更新借款API任务表
	 *
	 * @return
	 */
	public int updateBorrowApicron(Integer id, Integer status, String data) {
		DebtApicron record = new DebtApicron();
		record.setId(id);
		record.setCreditRepayStatus(status);
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
	 * 取得还款明细列表
	 *
	 * @return
	 */
	@Override
	public List<DebtLoan> getBorrowRecoverList(String borrowNid) {
		DebtLoanExample example = new DebtLoanExample();
		DebtLoanExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andRepayStatusEqualTo(0); // 未还款
		criteria.andCreditStatusEqualTo(1);
		example.setOrderByClause(" id asc ");
		List<DebtLoan> list = this.debtLoanMapper.selectByExample(example);

		return list;
	}

	/**
	 * 取得本期应还金额
	 *
	 * @return
	 */
	@Override
	public BigDecimal getBorrowAccountWithPeriod(String borrowNid, String borrowStyle, Integer period) {
		BigDecimal account = BigDecimal.ZERO;

		// 是否分期(true:分期, false:单期)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);

		if (isMonth) {
			DebtLoanDetailExample example = new DebtLoanDetailExample();
			DebtLoanDetailExample.Criteria criteria = example.createCriteria();
			criteria.andBorrowNidEqualTo(borrowNid);
			criteria.andRepayStatusEqualTo(0);
			criteria.andRepayPeriodEqualTo(period);
			List<DebtLoanDetail> list = this.debtLoanDetailMapper.selectByExample(example);

			if (list != null && list.size() > 0) {
				for (DebtLoanDetail p : list) {
					account = account.add(p.getLoanAccount().add(p.getRepayAdvanceInterest())
							.add(p.getRepayDelayInterest()).add(p.getRepayLateInterest()).add(p.getManageFee()));
				}
			}
		} else {
			DebtLoanExample example2 = new DebtLoanExample();
			DebtLoanExample.Criteria criteria2 = example2.createCriteria();
			criteria2.andBorrowNidEqualTo(borrowNid);
			criteria2.andRepayStatusEqualTo(0);
			List<DebtLoan> list2 = this.debtLoanMapper.selectByExample(example2);
			if (list2 != null && list2.size() > 0) {
				for (DebtLoan p : list2) {
					account = account.add(p.getLoanAccount().add(p.getRepayAdvanceInterest())
							.add(p.getRepayDelayInterest()).add(p.getRepayLateInterest()).add(p.getManageFee()));
				}
			}
		}
		return account;
	}

	/**
	 * 取得还款信息
	 *
	 * @return
	 */
	private DebtRepay getBorrowRepay(String borrowNid) {
		DebtRepayExample example = new DebtRepayExample();
		DebtRepayExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andRepayStatusEqualTo(0);
		example.setOrderByClause(" id asc ");
		List<DebtRepay> list = this.debtRepayMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得标的详情
	 *
	 * @return
	 */
	@Override
	public DebtBorrowWithBLOBs getBorrow(String borrowNid) {
		DebtBorrowExample example = new DebtBorrowExample();
		DebtBorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		example.setOrderByClause(" id asc ");
		List<DebtBorrowWithBLOBs> list = this.debtBorrowMapper.selectByExampleWithBLOBs(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得借款列表
	 *
	 * @return
	 */
	@Override
	public DebtInvest getBorrowTender(Integer tenderId) {
		DebtInvest borrowTender = this.debtInvestMapper.selectByPrimaryKey(tenderId);
		return borrowTender;
	}

	/**
	 * 取出账户信息
	 *
	 * @param userId
	 * @return
	 */
	@Override
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
	 * 判断该收支明细是否存在
	 *
	 * @param accountList
	 * @return
	 */
	private int countAccountCreditListByNid(String nid) {
		DebtAccountListExample accountListExample = new DebtAccountListExample();
		accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("credit_tender_recover_yes");
		return this.debtAccountListMapper.countByExample(accountListExample);
	}

	/**
	 * 写入收支明细
	 *
	 * @param accountList
	 * @return
	 */
	private int insertAccountList(DebtAccountList accountList) {
		// 写入收支明细
		return this.debtAccountListMapper.insertSelective(accountList);
	}

	/**
	 * 取得总的还款计划表
	 *
	 * @param borrowRepayPlan
	 * @param borrowNid
	 * @param period
	 * @return
	 */
	private DebtRepayDetail getBorrowRepayPlan(String borrowNid, Integer period) {
		DebtRepayDetailExample example = new DebtRepayDetailExample();
		example.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayPeriodEqualTo(period);
		List<DebtRepayDetail> list = this.debtRepayDetailMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 更新还款信息
	 *
	 * @param record
	 * @return
	 */
	@Override
	public int updateBorrowRecover(DebtLoan recoder) {
		int cnt = this.debtLoanMapper.updateByPrimaryKeySelective(recoder);
		return cnt;
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
	 * 根据项目原标号，债转编号查询相应的债转出借记录
	 * 
	 * @param borrowNid
	 * @param creditId
	 * @return
	 */
	@Override
	public List<DebtCreditTender> selectCreditTenderList(String borrowNid, String investOrderId, String creditNid) {
		DebtCreditTenderExample example = new DebtCreditTenderExample();
		DebtCreditTenderExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andInvestOrderIdEqualTo(investOrderId);
		crt.andCreditNidEqualTo(creditNid);
		crt.andStatusEqualTo(0);
		List<DebtCreditTender> creditTenderList = this.debtCreditTenderMapper.selectByExample(example);
		return creditTenderList;
	}

	/**
	 * 未查询到债转标的出借记录，更新债转记录
	 * 
	 * @param creditId
	 * @param repayPeriod
	 * @param creditStatus
	 */
	@Override
	public boolean updateBorrowCredit(String creditNid, int repayPeriod, int repayStatus, int nowTime,
			int repayNextTime) {
		DebtCreditExample example = new DebtCreditExample();
		DebtCreditExample.Criteria crt = example.createCriteria();
		crt.andCreditNidEqualTo(creditNid);
		crt.andRepayPeriodEqualTo(repayPeriod);
		List<DebtCredit> borrowCreditList = this.debtCreditMapper.selectByExample(example);
		if (borrowCreditList != null && borrowCreditList.size() == 1) {
			DebtCredit borrowCredit = borrowCreditList.get(0);
			if (borrowCredit.getCreditStatus() == 0) {
				borrowCredit.setCreditStatus(1);
			}
			borrowCredit.setRepayStatus(repayStatus);
			borrowCredit.setRepayPeriod(repayPeriod + 1);
			// 债转最近还款时间
			borrowCredit.setCreditRepayLastTime(nowTime);
			// 债转最后还款时间
			borrowCredit.setCreditRepayYesTime(nowTime);
			// 债转下次还款时间
			borrowCredit.setCreditRepayNextTime(repayNextTime);
			boolean creditFlag = this.debtCreditMapper.updateByPrimaryKeySelective(borrowCredit) > 0 ? true : false;
			return creditFlag;
		} else {
			throw new RuntimeException("更新相应的债转数据失败,债转编号：" + creditNid);
		}
	}

	/**
	 * 查询债转的还款记录表
	 * 
	 * @param borrowNid
	 * @param userId
	 * @param assignNid
	 * @param assignNid2
	 * @param creditPeriod
	 * @param i
	 * @return
	 */
	@Override
	public DebtCreditRepay selectCreditRepay(String borrowNid, int userId, String investOrderId, String assignOrderId,
			int period, int status) {
		DebtCreditRepayExample example = new DebtCreditRepayExample();
		DebtCreditRepayExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andUserIdEqualTo(userId);
		crt.andInvestOrderIdEqualTo(investOrderId);
		crt.andAssignOrderIdEqualTo(assignOrderId);
		crt.andRepayPeriodEqualTo(period);
		crt.andRepayStatusEqualTo(status);
		crt.andDelFlagEqualTo(0);
		List<DebtCreditRepay> creditRepayList = this.debtCreditRepayMapper.selectByExample(example);
		if (creditRepayList != null && creditRepayList.size() == 1) {
			return creditRepayList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<DebtCredit> getBorrowCreditList(String borrowNid, int recoverPeriod, int status) {
		DebtCreditExample example = new DebtCreditExample();
		DebtCreditExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andRepayPeriodEqualTo(recoverPeriod);
		crt.andCreditStatusNotEqualTo(status);
		List<DebtCredit> borrowCreditList = debtCreditMapper.selectByExample(example);
		return borrowCreditList;
	}

	/**
	 * 查询未还款的债转项目
	 * 
	 * @param borrowNid
	 * @param tenderOrdId
	 * @param i
	 * @param j
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<DebtCredit> selectBorrowCreditList(String borrowNid, String investOrderId, int period, int status) {

		DebtCreditExample borrowCreditExample = new DebtCreditExample();
		DebtCreditExample.Criteria crt = borrowCreditExample.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andInvestOrderIdEqualTo(investOrderId);
		crt.andRepayPeriodEqualTo(period);
		crt.andRepayStatusNotEqualTo(status);
		List<DebtCredit> borrowCredits = this.debtCreditMapper.selectByExample(borrowCreditExample);
		return borrowCredits;
	}

	/**
	 * 查询分期还款数据
	 * 
	 * @param borrowNid
	 * @param periodNow
	 * @param tenderUserId
	 * @param nid
	 * @return
	 * @author Administrator
	 */

	@Override
	public DebtLoanDetail getBorrowRecoverPlan(String borrowNid, int periodNow, int tenderUserId, int tenderId) {

		DebtLoanDetailExample example = new DebtLoanDetailExample();
		DebtLoanDetailExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andInvestIdEqualTo(tenderId);
		crt.andUserIdEqualTo(tenderUserId);
		crt.andRepayPeriodEqualTo(periodNow);
		List<DebtLoanDetail> recoverPlanList = this.debtLoanDetailMapper.selectByExample(example);
		if (recoverPlanList != null && recoverPlanList.size() == 1) {
			return recoverPlanList.get(0);
		} else {
			return null;
		}

	}

	/**
	 * 更新债转还款总表
	 * 
	 * @param borrowCredit
	 * @return
	 * @author Administrator
	 */

	@Override
	public int updateBorrowCredit(DebtCredit borrowCredit) {
		return this.debtCreditMapper.updateByPrimaryKeySelective(borrowCredit);
	}

	/**
	 * 更新债转还款
	 * 
	 * @param creditRepay
	 * @return
	 */
	@Override
	public int updateCreditRepay(DebtCreditRepay creditRepay) {
		return this.debtCreditRepayMapper.updateByPrimaryKeySelective(creditRepay);
	}

	/**
	 * 更新还款信息
	 *
	 * @param record
	 * @return
	 */
	public int updateBorrowRecoverPlan(DebtLoanDetail recoder) {
		return this.debtLoanDetailMapper.updateByPrimaryKeySelective(recoder);
	}

	/**
	 * 计算计划是否是清算前三天还款
	 * 
	 * @Title isLiquidatesPlan
	 * @param loan
	 * @return
	 * @throws Exception
	 */
	private boolean isLiquidatesPlan(String planNid) throws Exception {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		cra.andDebtPlanNidEqualTo(planNid);
		cra.andDebtPlanStatusLessThanOrEqualTo(CustomConstants.DEBT_PLAN_STATUS_8);
		List<DebtPlan> list = this.debtPlanMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			DebtPlan plan = list.get(0);
			// 应清算时间为空的情况
			if (plan.getLiquidateShouldTime() == null || plan.getLiquidateShouldTime() == 0) {
				return false;
			}
			// 获取应清算日期前三天
			String liquidateShouldTime = GetDate.getDateMyTimeInMillis(plan.getLiquidateShouldTime());
			String now = GetDate.getDateMyTimeInMillis(GetDate.getMyTimeInMillis());
			int count = GetDate.daysBetween(now, liquidateShouldTime);
			if (count <= 3) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据加入订单号检索加入信息
	 * 
	 * @Title getDebtPlanAccedeInfo
	 * @param planOrderId
	 * @return
	 */
	private DebtPlanAccede getDebtPlanAccedeInfo(String planOrderId) {

		DebtPlanAccedeExample accedeExample = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria accedeCra = accedeExample.createCriteria();
		accedeCra.andAccedeOrderIdEqualTo(planOrderId);
		List<DebtPlanAccede> list = this.debtPlanAccedeMapper.selectByExample(accedeExample);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 解冻订单
	 * 
	 * @param borrowUserId
	 * @param investUserId
	 * @param orderId
	 * @param trxId
	 * @param ordDate
	 * @return
	 * @throws Exception
	 */
	public boolean unFreezeOrder(int investUserId, String orderId, String trxId, String ordDate, String unfreezeOrderId,
			String unfreezeOrderDate) throws Exception {

		// 出借人的账户信息
		AccountChinapnr outCust = this.getAccountChinapnr(investUserId);
		if (outCust == null) {
			throw new Exception("出借人未开户。[出借人ID：" + investUserId + "]，" + "[出借订单号：" + orderId + "]");
		}
		// 调用交易查询接口(解冻)
		ChinapnrBean queryTransStatBean = queryTransStat(orderId, ordDate, "FREEZE");
		if (queryTransStatBean == null) {
			throw new Exception("调用交易查询接口(解冻)失败。" + ",[出借订单号：" + orderId + "]");
		} else {
			String queryRespCode = queryTransStatBean.getRespCode();
			System.out.println("解冻接口查询接口返回码：" + queryRespCode);
			// 调用接口失败时(000以外)
			if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(queryRespCode)) {
				String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
				LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder",
						"调用交易查询接口(解冻)失败。" + message + ",[出借订单号：" + orderId + "]", null);
				throw new Exception("调用交易查询接口(解冻)失败。" + queryRespCode + "：" + message + ",[出借订单号：" + orderId + "]");
			} else {
				// 汇付交易状态
				String transStat = queryTransStatBean == null ? "" : queryTransStatBean.getTransStat();
				// 冻结请求已经被解冻 U:已解冻 N:无需解冻，对于解冻交易
				if (!"U".equals(transStat) && !"N".equals(transStat)) {
					/** 解冻订单 */
					ChinapnrBean unFreezeBean = usrUnFreeze(trxId, unfreezeOrderId, unfreezeOrderDate);
					String respCode = unFreezeBean == null ? "" : unFreezeBean.getRespCode();
					System.out.println("自动解冻接口返回码：" + respCode);
					// 调用接口失败时(000 或 107 以外)
					if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)
							&& !ChinaPnrConstant.RESPCODE_REPEAT_DEAL.equals(respCode)) {
						String message = unFreezeBean == null ? "" : unFreezeBean.getRespDesc();
						message = "调用解冻接口失败。" + respCode + "：" + message + "，出借订单号[" + orderId + "]";
						LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder", message, null);
						return false;
					} else {
						return true;
					}
				} else {
					return true;
				}
			}
		}
	}

	/**
	 * 获取用户的汇付信息
	 *
	 * @param userId
	 * @return 用户的汇付信息
	 */

	private AccountChinapnr getAccountChinapnr(Integer userId) {
		AccountChinapnr accountChinapnr = null;
		if (userId == null) {
			return null;
		}
		AccountChinapnrExample example = new AccountChinapnrExample();
		AccountChinapnrExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			accountChinapnr = list.get(0);
		}
		return accountChinapnr;
	}

	/**
	 * 资金（货款）解冻(调用汇付天下接口)
	 *
	 * @param trxId
	 * @return
	 * @throws Exception
	 */
	private ChinapnrBean usrUnFreeze(String trxId, String unfreezeOrderId, String unfreezeOrderDate) throws Exception {

		String methodName = "usrUnFreeze";
		// 调用汇付接口
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_USR_UN_FREEZE); // 消息类型(必须)
		bean.setOrdId(unfreezeOrderId); // 订单号(必须)
		bean.setOrdDate(unfreezeOrderDate); // 订单日期(必须)
		bean.setTrxId(trxId); // 本平台交易唯一标识(必须)
		bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("资金（货款）解冻"); // 备注
		bean.setLogClient("0"); // PC
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (Validator.isNull(chinapnrBean)) {
			LogUtil.errorLog(this.getClass().getName(), methodName,
					new Exception("调用解冻接口失败![参数：" + bean.getAllParams() + "]"));
			throw new Exception("调用交易查询接口(解冻)失败,[冻结标识：" + trxId + "]");
		} else if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinapnrBean.getRespCode())) {
			return chinapnrBean;
		} else {
			throw new Exception("调用交易查询接口(解冻)返回错误,[冻结标识：" + trxId + "]");
		}
	}

	/**
	 * 冻结用户的相应的订单金额
	 *
	 * @param tenderUsrcustid
	 * @param account
	 * @param borrowerUsrcustid
	 * @param OrdId
	 * @return
	 * @author b
	 */

	public String freezeOrder(int userId, String tenderUsrcustid, BigDecimal account, String orderId,
			String orderDate) {

		ChinapnrBean chinapnrBean = new ChinapnrBean();
		// 接口版本号
		chinapnrBean.setVersion("10");
		// 消息类型(冻结)
		chinapnrBean.setCmdId("UsrFreezeBg");
		// 出借用户客户号
		chinapnrBean.setUsrCustId(tenderUsrcustid);
		// 订单号(必须)
		chinapnrBean.setOrdId(orderId);
		// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		chinapnrBean.setOrdDate(orderDate);
		// 交易金额(必须)
		chinapnrBean.setTransAmt(CustomUtil.formatAmount(account.toString()));
		// 页面返回
		chinapnrBean.setRetUrl("");
		// 商户后台应答地址(必须)
		chinapnrBean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
		// 日志类型
		chinapnrBean.setType("user_freeze");
		chinapnrBean.setLogUserId(userId);
		ChinapnrBean bean = ChinapnrUtil.callApiBg(chinapnrBean);
		// 处理冻结返回信息
		if (bean != null) {
			String respCode = bean.getRespCode();
			if (StringUtils.isNotEmpty(respCode) && respCode.equals(ChinaPnrConstant.RESPCODE_SUCCESS)) {
				System.out.println("用户:" + userId + "***********************************冻结订单号：" + bean.getTrxId());
				return bean.getTrxId();
			} else {
				System.out.println("用户:" + userId + "***********************************冻结失败错误码：" + respCode);
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param apicron
	 * @param debtLoan
	 * @param borrowUserCust
	 * @return
	 * @throws Exception
	 */
	public boolean updateBorrowRepay(DebtApicron apicron, DebtLoan debtLoan, AccountChinapnr borrowUserCust)
			throws Exception {

		String methodName = "updateBorrowRepay";
		System.out.println("------债转还款未承接部分开始---项目编号：" + apicron.getBorrowNid() + "---------");
		/** 基本变量 */
		// 还款订单号
		String repayOrderId = null;
		// 还款订单日期
		String repayOrderDate = null;
		// 应还款总额
		BigDecimal recoverAccount = BigDecimal.ZERO;
		// 应还款本金
		BigDecimal recoverCapital = BigDecimal.ZERO;
		// 应还款利息
		BigDecimal recoverInterest = BigDecimal.ZERO;
		// 借款人逾期利息
		BigDecimal lateInterest = BigDecimal.ZERO;
		// 借款人延期利息
		BigDecimal delayInterest = BigDecimal.ZERO;
		// 借款人提前还款利息
		BigDecimal advanceInterest = BigDecimal.ZERO;
		// 借款人已还款总额
		BigDecimal recoverAccountYesAlready = BigDecimal.ZERO;
		// 借款人已还款本金
		BigDecimal recoverCapitalYesAlready = BigDecimal.ZERO;
		// 借款人已还款利息
		BigDecimal recoverInterestYesAlready = BigDecimal.ZERO;
		// 借款人剩余未债转本息
		BigDecimal recoverAccountRemain = BigDecimal.ZERO;
		// 借款人剩余未债转利息
		BigDecimal recoverInterestRemain = BigDecimal.ZERO;
		// 借款人剩余未债转本金
		BigDecimal recoverCapitalRemain = BigDecimal.ZERO;
		// 借款人剩余待还债转本息
		BigDecimal borrowRepayAccount = BigDecimal.ZERO;
		// 借款人剩余待还债转利息
		BigDecimal borrowRepayInterest = BigDecimal.ZERO;
		// 借款人剩余待还债转本金
		BigDecimal borrowRepayCapital = BigDecimal.ZERO;
		// 借款人逾期利息
		BigDecimal borrowRepayLateInterest = BigDecimal.ZERO;
		// 借款人延期利息
		BigDecimal borrowRepayDelayInterest = BigDecimal.ZERO;
		// 借款人提前还款利息
		BigDecimal borrowRepayAdvanceInterest = BigDecimal.ZERO;
		// 出让人剩余待收债转本息
		BigDecimal receiveAccount = BigDecimal.ZERO;
		// 出让人剩余待收债转利息
		BigDecimal receiveInterest = BigDecimal.ZERO;
		// 出让人剩余待收债转本金
		BigDecimal receiveCapital = BigDecimal.ZERO;
		// 出让人剩余待收提前本息
		BigDecimal receiveAdvanceInterest = BigDecimal.ZERO;
		// 出让人剩余待收延期利息
		BigDecimal receiveDelayInterest = BigDecimal.ZERO;
		// 出让人剩余待收逾期利息
		BigDecimal receiveLateInterest = BigDecimal.ZERO;
		// 应收管理费
		BigDecimal manageFee = BigDecimal.ZERO;
		// 汇添金服务费
		BigDecimal serviceFee = BigDecimal.ZERO;
		// 还款状态 0正常还款 1提前还款 2延期还款 3逾期还款
		int advanceStatus = debtLoan.getAdvanceStatus();
		// 提前天数
		Integer advanceDays = 0;
		// 延期天数
		Integer delayDays = 0;
		// 延期天数
		Integer lateDays = 0;
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 借款编号
		String borrowNid = apicron.getBorrowNid();
		// 借款人ID
		Integer borrowUserid = apicron.getUserId();
		// 当前期数
		Integer periodNow = apicron.getPeriodNow();
		/** 标的基本数据 */
		// 取得借款详情
		DebtBorrowWithBLOBs debtBorrow = getBorrow(borrowNid);
		// 标的是否进行银行托管
		int bankInputFlag = Validator.isNull(debtBorrow.getBankInputFlag()) ? 0 : debtBorrow.getBankInputFlag();
		// 项目总期数
		Integer borrowPeriod = Validator.isNull(debtBorrow.getBorrowPeriod()) ? 1 : debtBorrow.getBorrowPeriod();
		// 还款方式
		String borrowStyle = debtBorrow.getBorrowStyle();
		// 管理费率
		BigDecimal feeRate = Validator.isNull(debtBorrow.getManageFeeRate()) ? BigDecimal.ZERO
				: new BigDecimal(debtBorrow.getManageFeeRate());
		// 差异费率
		BigDecimal differentialRate = Validator.isNull(debtBorrow.getDifferentialRate()) ? BigDecimal.ZERO
				: new BigDecimal(debtBorrow.getDifferentialRate());
		// 初审时间
		int borrowVerifyTime = Validator.isNull(debtBorrow.getVerifyTime()) ? 0
				: Integer.parseInt(debtBorrow.getVerifyTime());
		// 剩余还款期数
		Integer periodNext = borrowPeriod - periodNow;
		// 出借订单号
		String tenderOrdId = debtLoan.getInvestOrderId();
		// 出借人用户ID
		Integer tenderUserId = debtLoan.getUserId();
		// 出借ID
		Integer tenderId = debtLoan.getInvestId();
		// 出借人在汇付的账户信息
		AccountChinapnr tenderUserCust = getChinapnrUserInfo(tenderUserId);
		if (tenderUserCust == null) {
			throw new Exception("出借人未开户。[用户ID：" + tenderUserId + "]，" + "[出借订单号：" + tenderOrdId + "]");
		}
		Long tenderUserCustId = tenderUserCust.getChinapnrUsrcustid();
		// 借款人客户号
		Long borrowUserCustId = borrowUserCust.getChinapnrUsrcustid();
		// 取得还款详情
		DebtRepay debtRepay = getBorrowRepay(borrowNid);
		// 取得出借信息
		DebtInvest debtInvest = getBorrowTender(tenderId);
		// 分期还款计划表
		DebtLoanDetail debtLoanDetail = null;
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		if (isMonth) {
			// 取得分期还款计划表
			debtLoanDetail = getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId, debtLoan.getInvestId());
			if (debtLoanDetail != null) {
				// 还款订单号
				repayOrderId = debtLoanDetail.getRepayOrderId();
				// 还款订单日期
				repayOrderDate = debtLoanDetail.getRepayOrderDate();
				// 应还款金额
				recoverAccount = debtLoanDetail.getLoanAccount();
				// 应还款本金
				recoverCapital = debtLoanDetail.getLoanCapital();
				// 还款状态
				advanceStatus = debtLoanDetail.getAdvanceStatus();
				// 逾期天数
				lateDays = debtLoanDetail.getLateDays();
				// 应还款逾期利息
				lateInterest = debtLoanDetail.getRepayLateInterest();
				// 逾期利息
				borrowRepayLateInterest = debtLoanDetail.getRepayLateInterest()
						.subtract(debtLoanDetail.getRepayLateInterestYes());
				// 延期天数
				delayDays = debtLoanDetail.getDelayDays();
				// 应还款延期利息
				delayInterest = debtLoanDetail.getRepayDelayInterest();
				// 延期利息
				borrowRepayDelayInterest = debtLoanDetail.getRepayDelayInterest()
						.subtract(debtLoanDetail.getRepayDelayInterestYes());
				// 借款人还款提前天数
				advanceDays = debtLoanDetail.getAdvanceDays();
				// 应还款延期利息
				advanceInterest = debtLoanDetail.getRepayAdvanceInterest();
				// 提前还款利息
				borrowRepayAdvanceInterest = debtLoanDetail.getRepayAdvanceInterest()
						.subtract(debtLoanDetail.getRepayAdvanceInterestYes());
				// 应还款利息
				recoverInterest = debtLoanDetail.getLoanInterest();
				// 还款金额(已还)
				recoverAccountYesAlready = debtLoanDetail.getRepayAccountYes();
				// 还款本金(已还)
				recoverCapitalYesAlready = debtLoanDetail.getRepayCapitalYes();
				// 还款利息(已还)
				recoverInterestYesAlready = debtLoanDetail.getRepayInterestYes();
				// 借款人剩余待还款金额
				recoverAccountRemain = debtLoanDetail.getRepayAccountWait();
				// 借款人剩余待还本金
				recoverCapitalRemain = debtLoanDetail.getRepayCapitalWait();
				// 借款人剩余待还利息
				recoverInterestRemain = debtLoanDetail.getRepayInterestWait();
				// 借款人实际剩余待还款金额
				borrowRepayAccount = recoverAccount.add(lateInterest).add(delayInterest).add(advanceInterest)
						.subtract(recoverAccountYesAlready);
				// 借款人实际剩余待还本金
				borrowRepayCapital = recoverCapital.subtract(recoverCapitalYesAlready);
				// 借款人实际剩余待还利息
				borrowRepayInterest = recoverInterest.add(lateInterest).add(delayInterest).add(advanceInterest)
						.subtract(recoverInterestYesAlready);
				// 应收管理费
				manageFee = debtLoanDetail.getManageFee();
			} else {
				throw new Exception("分期还款计划表数据不存在。[借款编号：" + borrowNid + "]，" + "[出借订单号：" + tenderOrdId + "]，" + "[期数："
						+ periodNow + "]");
			}
		} else {// [endday: 按天计息, end:按月计息]
			debtLoan = this.getBorrowRecover(debtLoan.getId());
			// 还款订单号
			repayOrderId = debtLoan.getRepayOrderId();
			// 还款订单日期
			repayOrderDate = debtLoan.getRepayOrderDate();
			// 还款金额
			recoverAccount = debtLoan.getLoanAccount();
			// 还款本金
			recoverCapital = debtLoan.getLoanCapital();
			// 还款利息
			recoverInterest = debtLoan.getLoanInterest();
			// 还款状态
			advanceStatus = debtLoan.getAdvanceStatus();
			// 逾期天数
			lateDays = debtLoan.getLateDays();
			// 应还款逾期利息
			lateInterest = debtLoan.getRepayLateInterest();
			// 逾期利息
			borrowRepayLateInterest = debtLoan.getRepayLateInterest().subtract(debtLoan.getRepayLateInterestYes());
			// 延期天数
			delayDays = debtLoan.getDelayDays();
			// 应还款延期利息
			delayInterest = debtLoan.getRepayDelayInterest();
			// 延期利息
			borrowRepayDelayInterest = debtLoan.getRepayDelayInterest().subtract(debtLoan.getRepayDelayInterestYes());
			// 借款人还款提前天数
			advanceDays = debtLoan.getAdvanceDays();
			// 应还款提前还款利息
			advanceInterest = debtLoan.getRepayAdvanceInterest();
			// 提前还款少还利息
			borrowRepayAdvanceInterest = debtLoan.getRepayAdvanceInterest()
					.subtract(debtLoan.getRepayAdvanceInterestYes());
			// 还款金额(已还)
			recoverAccountYesAlready = debtLoan.getRepayAccountYes();
			// 还款本金(已还)
			recoverCapitalYesAlready = debtLoan.getRepayCapitalYes();
			// 还款利息(已还)
			recoverInterestYesAlready = debtLoan.getRepayInterestYes();
			// 借款人剩余待还款金额
			recoverAccountRemain = debtLoan.getRepayAccountWait();
			// 借款人剩余待还本金
			recoverCapitalRemain = debtLoan.getRepayCapitalWait();
			// 借款人剩余待还利息
			recoverInterestRemain = debtLoan.getRepayInterestWait();
			// 借款人实际剩余待还款金额
			borrowRepayAccount = recoverAccount.add(lateInterest).add(delayInterest).add(advanceInterest)
					.subtract(recoverAccountYesAlready);
			// 借款人实际剩余待还本金
			borrowRepayCapital = recoverCapital.subtract(recoverCapitalYesAlready);
			// 借款人实际剩余待还利息
			borrowRepayInterest = recoverInterest.add(lateInterest).add(delayInterest).add(advanceInterest)
					.subtract(recoverInterestYesAlready);
			// 应收管理费
			manageFee = debtLoan.getManageFee();
		}
		// 判断该收支明细是否存在时,跳出本次循环
		if (countAccountTenderListByNid(repayOrderId) > 0) {
			return true;
		}
		// 债转还款总金额
		BigDecimal creditRepayTotal = this.selectCreditRepayTotal(debtLoan.getBorrowNid(), debtLoan.getInvestOrderId(),
				periodNow);
		// 债转已还款总额
		creditRepayTotal = creditRepayTotal.setScale(2, BigDecimal.ROUND_DOWN);
		// 已还款总额
		recoverAccountYesAlready = recoverAccountYesAlready.setScale(2, BigDecimal.ROUND_DOWN);
		// 判断已还款总额是否相等
		if (creditRepayTotal.compareTo(recoverAccountYesAlready) == 0) {
			// 更新DebtDetail表
			DebtDetail debtDetail = this.getDebtDetail(tenderOrdId, periodNow);
			// 债转剩余金额大于0
			if (borrowRepayAccount.compareTo(BigDecimal.ZERO) > 0 && Validator.isNotNull(debtDetail)) {
				System.out
						.println("------未债转部分还款开始---项目编号：" + apicron.getBorrowNid() + "---------还款订单号：" + repayOrderId);
				if (isMonth) {
					// 取得分期还款计划表
					debtLoanDetail = getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId, debtLoan.getInvestId());
					if (debtLoanDetail != null) {
						// 出让人剩余待收提前本息
						receiveAdvanceInterest = debtLoanDetail.getRepayAdvanceInterest()
								.subtract(debtLoanDetail.getRepayAdvanceInterestYes());
						// 出让人剩余待收延期利息
						receiveDelayInterest = debtDetail.getDelayInterest()
								.subtract(debtDetail.getDelayInterestAssigned());
						// 出让人剩余待收逾期利息
						receiveLateInterest = debtDetail.getLateInterest()
								.subtract(debtDetail.getLateInterestAssigned());
						// 出让人剩余待收本息
						receiveAccount = recoverCapitalRemain.add(recoverInterestRemain).add(receiveAdvanceInterest)
								.add(receiveDelayInterest).add(receiveLateInterest);
						// 出让人剩余待收本金
						receiveCapital = recoverCapitalRemain;
						// 出让人剩余待收利息
						receiveInterest = recoverInterestRemain.add(receiveAdvanceInterest).add(receiveDelayInterest)
								.add(receiveLateInterest);
					} else {
						throw new Exception("分期还款计划表数据不存在。[借款编号：" + borrowNid + "]，" + "[出借订单号：" + tenderOrdId + "]，"
								+ "[期数：" + periodNow + "]");
					}
				} else {// [endday: 按天计息, end:按月计息]
					// 出让人剩余待收提前本息
					receiveAdvanceInterest = debtLoan.getRepayAdvanceInterest()
							.subtract(debtLoan.getRepayAdvanceInterestYes());
					// 出让人剩余待收延期利息
					receiveDelayInterest = debtDetail.getDelayInterest()
							.subtract(debtDetail.getDelayInterestAssigned());
					// 出让人剩余待收逾期利息
					receiveLateInterest = debtDetail.getLateInterest().subtract(debtDetail.getLateInterestAssigned());
					// 出让人剩余待收本息
					receiveAccount = recoverCapitalRemain.add(recoverInterestRemain).add(receiveAdvanceInterest)
							.add(receiveDelayInterest).add(receiveLateInterest);
					// 出让人剩余待收本金
					receiveCapital = recoverCapitalRemain;
					// 出让人剩余待收利息
					receiveInterest = recoverInterestRemain.add(receiveAdvanceInterest).add(receiveDelayInterest)
							.add(receiveLateInterest);
				}
				// 管理费
				BigDecimal perManage = new BigDecimal("0");
				// 按月计息，到期还本还息end
				if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
					perManage = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(borrowRepayCapital, feeRate,
							borrowPeriod, differentialRate, borrowVerifyTime);
				}
				// 按天计息到期还本还息
				else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
					perManage = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(borrowRepayCapital, feeRate,
							borrowPeriod, differentialRate, borrowVerifyTime);
				}
				// 等额本息month、等额本金principal
				else if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
						|| CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
					if (periodNow.intValue() == borrowPeriod.intValue()) {
						perManage = AccountManagementFeeUtils.getHTJMonthAccountManagementFee(borrowRepayCapital,
								feeRate, periodNow, differentialRate, 1, recoverCapital, borrowPeriod, borrowVerifyTime,
								manageFee);
					} else {
						perManage = AccountManagementFeeUtils.getMonthAccountManagementFee(borrowRepayCapital, feeRate,
								periodNow, differentialRate, 0, recoverCapital, borrowPeriod, borrowVerifyTime);
					}
				}
				// 先息后本endmonth
				else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
					if (periodNow.intValue() == borrowPeriod.intValue()) {
						perManage = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(
								debtLoan.getLoanCapital().subtract(debtLoan.getCreditAmount()), feeRate, borrowPeriod,
								periodNow, differentialRate, 1, borrowVerifyTime);
					} else {
						perManage = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(
								debtLoan.getLoanCapital().subtract(debtLoan.getCreditAmount()), feeRate, borrowPeriod,
								periodNow, differentialRate, 0, borrowVerifyTime);
					}
				}
				// 判断是否是清算日前三天还款
				boolean isLiquidatesPlanFlag = this.isLiquidatesPlan(debtLoan.getPlanNid());
				if (advanceStatus == 2 || advanceStatus == 3) {
					serviceFee = borrowRepayInterest.subtract(receiveInterest);
				} else if (isLiquidatesPlanFlag) {
					// 汇添金服务费
					serviceFee = this.calculateServiceFee(debtLoan.getPlanOrderId(), borrowRepayAccount);
					if (serviceFee.compareTo(BigDecimal.ZERO) > 0
							&& receiveInterest.subtract(serviceFee).compareTo(BigDecimal.ZERO) < 0) {
						serviceFee = receiveInterest;
					}
					receiveInterest = receiveInterest.subtract(serviceFee);
					receiveAccount = receiveAccount.subtract(serviceFee);
				}
				// 调用交易查询接口
				ChinapnrBean queryTransStatBean = queryTransStat(repayOrderId, repayOrderDate, "REPAYMENT");
				String respCode = queryTransStatBean == null ? "" : queryTransStatBean.getRespCode();
				// 调用接口失败时(000,422以外)
				if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)
						&& !ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(respCode)) {
					String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
					LogUtil.errorLog(THIS_CLASS, methodName, "调用交易查询接口失败。" + message + "，[出借订单号：" + tenderOrdId + "]",
							null);
					throw new Exception("调用交易查询接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
				}
				// 汇付交易状态
				String transStat = queryTransStatBean.getTransStat();
				// I:初始 P:部分成功
				if (ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(respCode)
						|| (!"I".equals(transStat) && !"P".equals(transStat))) {
					// 分账账户串（当 管理费！=0 时是必填项）
					String divDetails = "";
					if (perManage.compareTo(BigDecimal.ZERO) > 0) {
						JSONArray ja = new JSONArray();
						JSONObject jo = new JSONObject();
						// 分账商户号(商户号,从配置文件中取得)
						jo.put(ChinaPnrConstant.PARAM_DIVCUSTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
						// 分账账户号(子账户号,从配置文件中取得)
						jo.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT03));
						// 分账金额
						jo.put(ChinaPnrConstant.PARAM_DIVAMT, perManage.toString());
						ja.add(jo);
						if (serviceFee.compareTo(BigDecimal.ZERO) > 0) {
							JSONObject jo1 = new JSONObject();
							// 分账商户号(商户号,从配置文件中取得)
							jo1.put(ChinaPnrConstant.PARAM_DIVCUSTID,
									PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
							// 分账账户号(子账户号,从配置文件中取得)
							jo1.put(ChinaPnrConstant.PARAM_DIVACCTID,
									PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT17));
							// 分账金额
							jo1.put(ChinaPnrConstant.PARAM_DIVAMT, serviceFee.toString());
							ja.add(jo1);
						}
						divDetails = ja.toString();
					}
					// 入参扩展域(3.0用)
					String reqExts = "";
					if (receiveAccount.compareTo(BigDecimal.ZERO) > 0) {
						// 调用汇付接口
						ChinapnrBean repaymentBean = null;
						repaymentBean = repayment(borrowNid, borrowUserid, String.valueOf(borrowUserCustId),
								receiveCapital.toString(), receiveInterest.toString(), perManage, serviceFee,
								repayOrderId, repayOrderDate, tenderOrdId, debtInvest.getOrderDate(),
								String.valueOf(tenderUserCustId), divDetails, reqExts, bankInputFlag);
						respCode = repaymentBean == null ? "" : repaymentBean.getRespCode();
						// 调用接口失败时(000以外)
						if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)
								&& !ChinaPnrConstant.RESPCODE_REPEAT_REPAY.equals(respCode)) {
							String message = repaymentBean == null ? "" : repaymentBean.getRespDesc();
							LogUtil.errorLog(THIS_CLASS, methodName,
									"调用自动扣款（还款）接口失败。" + message + "，[出借订单号：" + tenderOrdId + "]", null);
							throw new Exception(
									"调用自动扣款（还款）接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
						}
					}
				}
				// 判断该收支明细是否存在时,跳出本次循环
				if (countAccountTenderListByNid(repayOrderId) > 0) {
					System.out.println(
							"------未债转部分还款结束---项目编号：" + apicron.getBorrowNid() + "---------还款订单号：" + repayOrderId);
					return true;
				}
				// 更新账户信息(出借人)
				Account account = new Account();
				// 出借人用户id
				account.setUserId(tenderUserId);
				// 出借人可用余额
				account.setPlanBalance(receiveAccount);
				// 非清算时,订单加入可用余额
				if (!isLiquidatesPlanFlag) {
					account.setPlanAccedeBalance(receiveAccount);
				}
				// 更新出借人账户
				boolean accountTenderFlag = this.adminAccountCustomizeMapper.updateOfPlanBalance(account) > 0 ? true
						: false;
				if (!accountTenderFlag) {
					throw new Exception("承接人资金记录(huiyingdai_account)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
				}
				// 取得账户信息(出借人)
				account = this.getAccountByUserId(debtInvest.getUserId());
				if (Validator.isNull(account)) {
					throw new Exception(
							"出借人账户信息不存在。[出借人ID：" + debtInvest.getUserId() + "]，" + "[出借订单号：" + tenderOrdId + "]");
				}
				// 根据加入订单号 查询加入信息
				DebtPlanAccede debtPlanAccede = this.getDebtPlanAccedeInfo(debtLoan.getPlanOrderId());
				if (Validator.isNull(debtPlanAccede)) {
					throw new Exception("出借人加入记录不存在。[加入订单号：" + debtLoan.getPlanOrderId() + "]");
				}
				boolean debtPlanAccedeUpdateFlag = false;
				DebtPlanAccede upateDebtPlanAccede = new DebtPlanAccede();
				// 清算前三天还款
				if (isLiquidatesPlanFlag) {
					upateDebtPlanAccede.setLiquidatesRepayFrost(receiveAccount);
					upateDebtPlanAccede.setId(debtPlanAccede.getId());
					upateDebtPlanAccede.setServiceFee(serviceFee);
					debtPlanAccedeUpdateFlag = this.batchDebtPlanAccedeCustomizeMapper
							.updateDebtPlanLiquidatesBalance(upateDebtPlanAccede) > 0 ? true : false;
				} else {
					upateDebtPlanAccede.setAccedeBalance(receiveAccount);
					upateDebtPlanAccede.setServiceFee(serviceFee);
					upateDebtPlanAccede.setId(debtPlanAccede.getId());
					debtPlanAccedeUpdateFlag = this.batchDebtPlanAccedeCustomizeMapper
							.updateDebtPlanRepayBalance(upateDebtPlanAccede) > 0 ? true : false;
				}
				if (!debtPlanAccedeUpdateFlag) {
					throw new Exception("更新用户加入失败!" + "[出借订单号：" + tenderOrdId + "]");
				}
				// 写入承接人收支明细
				DebtAccountList accountList = new DebtAccountList();
				// 根据加入订单号 重新查询加入信息
				debtPlanAccede = this.getDebtPlanAccedeInfo(debtLoan.getPlanOrderId());
				accountList.setNid(repayOrderId); // 还款订单号
				accountList.setUserId(tenderUserId);// 出借人
				accountList.setUserName(debtLoan.getUserName());// 出借人用户名
				accountList.setAmount(receiveAccount);// 承接人总收入
				accountList.setPlanNid(debtLoan.getPlanNid()); // 计划编号
				accountList.setPlanOrderId(debtLoan.getPlanOrderId());// 计划订单号
				accountList.setPlanBalance(account.getPlanBalance());// 计划余额
				accountList.setPlanFrost(account.getPlanFrost());
				accountList.setPlanOrderBalance(debtPlanAccede.getAccedeBalance());// 计划订单余额
				accountList.setPlanOrderFrost(debtPlanAccede.getAccedeFrost());// 计划订单冻结
				UsersInfo userInfo = getUsersInfoByUserId(tenderUserId);
				// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
				Integer attribute = null;
				if (Validator.isNotNull(userInfo)) {
					// 获取出借用户的用户属性
					attribute = userInfo.getAttribute();
					if (Validator.isNotNull(attribute)) {
						if (attribute == 1) {
							SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
							SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
									.createCriteria();
							spreadsUsersExampleCriteria.andUserIdEqualTo(tenderUserId);
							List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
							if (sList != null && sList.size() == 1) {
								int refUserId = sList.get(0).getSpreadsUserid();
								// 查找用户推荐人
								Users refererUser = getUsersByUserId(refUserId);
								if (Validator.isNotNull(refererUser)) {
									accountList.setRefererUserId(refererUser.getUserId());// 推荐人用户ID
									accountList.setRefererUserName(refererUser.getUsername());// 推荐人用户名
								}
							}
						} else if (attribute == 0) {
							SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
							SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample
									.createCriteria();
							spreadsUsersExampleCriteria.andUserIdEqualTo(tenderUserId);
							List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
							if (sList != null && sList.size() == 1) {
								int refUserId = sList.get(0).getSpreadsUserid();
								// 查找推荐人
								Users refererUser = getUsersByUserId(refUserId);
								if (Validator.isNotNull(refererUser)) {
									accountList.setRefererUserId(refererUser.getUserId());// 推荐人用户ID
									accountList.setRefererUserName(refererUser.getUsername());// 推荐人用户名
								}
							}
						}
					}
				}
				accountList.setType(1); // 1收入
				accountList.setTrade("credit_tender_recover_yes");// 承接人收到还款成功
				accountList.setTradeCode("balance"); // 余额操作
				accountList.setTotal(account.getTotal()); // 出借人资金总额
				accountList.setBalance(account.getBalance()); // 出借人可用金额
				accountList.setFrost(account.getFrost()); // 出借人冻结金额
				accountList.setAccountWait(BigDecimal.ZERO); // 出借人待收金额
				accountList.setCapitalWait(BigDecimal.ZERO);// 待收本金
				accountList.setInterestWait(BigDecimal.ZERO);// 待还收益
				accountList.setRepayWait(BigDecimal.ZERO);// 待还金额
				accountList.setCreateTime(nowTime);// 创建时间
				accountList.setUpdateTime(nowTime);// 更新时间
				accountList.setRemark(debtPlanAccede.getAccedeOrderId()); // 计划加入订单号
				accountList.setIp(debtBorrow.getAddip()); // 操作IP
				accountList.setWeb(0); // PC
				accountList.setCreateUserId(debtLoan.getUserId());// 承接人ID
				accountList.setCreateUserName(debtLoan.getUserName());// 承接人用户名
				accountList.setWeb(0);// PC
				boolean tenderAccountListFlag = insertAccountList(accountList) > 0 ? true : false;
				if (!tenderAccountListFlag) {
					throw new Exception("收支明细(hyjf_debt_account_list)写入失败！" + "[出借订单号：" + tenderOrdId + "]");
				}
				// 分期
				if (debtLoanDetail != null) {
					// 不是最后一期
					if (Validator.isNotNull(periodNext) && periodNext > 0) {
						debtLoan.setRepayStatus(0); // 未还款
						// 取得分期还款计划表下一期的还款
						DebtLoanDetail borrowRecoverPlanNext = getBorrowRecoverPlan(borrowNid, periodNow + 1,
								tenderUserId, debtLoan.getInvestId());
						debtLoan.setRepayTime(borrowRecoverPlanNext.getRepayTime()); // 计算下期时间
					} else {
						debtLoan.setRepayStatus(1); // 未还款
						debtLoan.setRepayActionTime(String.valueOf(nowTime)); // 实际还款时间
					}
					debtLoan.setRepayPeriod(periodNow);// 当前还款期数
					debtLoan.setRemainPeriod(periodNext);// 剩余还款期数
				} else {
					debtLoan.setRepayStatus(1); // 已还款
					debtLoan.setRepayActionTime(String.valueOf(nowTime)); // 实际还款时间
					debtLoan.setRepayPeriod(1);
					debtLoan.setRemainPeriod(0);
				}
				debtLoan.setWeb(2); // 写入网站收支
				debtLoan.setRepayAccountYes(debtLoan.getRepayAccountYes().add(borrowRepayAccount));
				debtLoan.setRepayInterestYes(debtLoan.getRepayInterestYes().add(borrowRepayInterest));
				debtLoan.setRepayCapitalYes(debtLoan.getRepayCapitalYes().add(borrowRepayCapital));
				debtLoan.setRepayAccountWait(debtLoan.getRepayAccountWait().subtract(recoverAccountRemain));
				debtLoan.setRepayInterestWait(debtLoan.getRepayInterestWait().subtract(recoverInterestRemain));
				debtLoan.setRepayCapitalWait(debtLoan.getRepayCapitalWait().subtract(recoverCapitalRemain));
				// 借款人已还提前还款利息
				debtLoan.setRepayAdvanceInterestYes(
						debtLoan.getRepayAdvanceInterestYes().add(borrowRepayAdvanceInterest));
				// 借款人已还延期还款利息
				debtLoan.setRepayDelayInterestYes(debtLoan.getRepayDelayInterestYes().add(borrowRepayDelayInterest));
				// 借款人已还逾期还款利息
				debtLoan.setRepayLateInterestYes(debtLoan.getRepayLateInterestYes().add(borrowRepayLateInterest));
				// 出借人收取已还提前还款利息
				debtLoan.setReceiveAdvanceInterestYes(
						debtLoan.getReceiveAdvanceInterestYes().add(receiveAdvanceInterest));
				// 出借人收取已还延期还款利息
				debtLoan.setReceiveDelayInterestYes(debtLoan.getReceiveDelayInterestYes().add(receiveDelayInterest));
				// 出借人收取已还逾期还款利息
				debtLoan.setReceiveLateInterestYes(debtLoan.getReceiveLateInterestYes().add(receiveLateInterest));
				// 出借人收取已还还款利息
				debtLoan.setReceiveAccountYes(debtLoan.getReceiveAccountYes().add(receiveAccount));
				// 出借人收取已还本金
				debtLoan.setReceiveCapitalYes(debtLoan.getReceiveCapitalYes().add(receiveCapital));
				// 出借人收取已还利息
				debtLoan.setReceiveInterestYes(debtLoan.getReceiveInterestYes().add(receiveInterest));
				boolean tenderBorrowRecoverFlag = this.debtLoanMapper.updateByPrimaryKeySelective(debtLoan) > 0 ? true
						: false;
				if (!tenderBorrowRecoverFlag) {
					throw new Exception("还款明细(hyjf_debt_loan)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
				}
				// 更新总的还款明细
				debtRepay.setRepayAccountAll(debtRepay.getRepayAccountAll().add(borrowRepayAccount).add(perManage));
				debtRepay.setRepayAccountYes(debtRepay.getRepayAccountYes().add(borrowRepayAccount));
				debtRepay.setRepayInterestYes(debtRepay.getRepayInterestYes().add(borrowRepayInterest));
				debtRepay.setRepayCapitalYes(debtRepay.getRepayCapitalYes().add(borrowRepayCapital));
				debtRepay.setAdvanceStatus(debtLoan.getAdvanceStatus());// 用户是否提前还款
				debtRepay.setAdvanceDays(advanceDays);
				debtRepay.setAdvanceInterest(debtRepay.getAdvanceInterest().add(borrowRepayAdvanceInterest));
				debtRepay.setDelayDays(delayDays);
				debtRepay.setDelayInterest(debtRepay.getDelayInterest().add(borrowRepayDelayInterest));
				debtRepay.setLateDays(lateDays);
				debtRepay.setLateInterest(debtRepay.getLateInterest().add(borrowRepayLateInterest));
				boolean tenderBorrowRepayFlag = this.debtRepayMapper.updateByPrimaryKeySelective(debtRepay) > 0 ? true
						: false;
				if (!tenderBorrowRepayFlag) {
					throw new Exception("总的还款明细表(hyjf_debt_repay)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
				}
				// 分期时
				if (isMonth) {
					if (debtLoanDetail != null) {
						// 更新还款计划表
						debtLoanDetail.setRepayStatus(1);
						debtLoanDetail.setRepayActionTime(String.valueOf(nowTime));
						debtLoanDetail.setRepayAccountYes(debtLoanDetail.getRepayAccountYes().add(borrowRepayAccount));
						debtLoanDetail
								.setRepayInterestYes(debtLoanDetail.getRepayInterestYes().add(borrowRepayInterest));
						debtLoanDetail.setRepayCapitalYes(debtLoanDetail.getRepayCapitalYes().add(borrowRepayCapital));
						debtLoanDetail.setRepayAccountWait(
								debtLoanDetail.getRepayAccountWait().subtract(recoverAccountRemain));
						debtLoanDetail.setRepayCapitalWait(
								debtLoanDetail.getRepayCapitalWait().subtract(recoverCapitalRemain));
						debtLoanDetail.setRepayInterestWait(
								debtLoanDetail.getRepayInterestWait().subtract(recoverInterestRemain));
						// 借款人已还提前还款利息
						debtLoanDetail.setRepayAdvanceInterestYes(
								debtLoanDetail.getRepayAdvanceInterestYes().add(borrowRepayAdvanceInterest));
						// 借款人已还延期还款利息
						debtLoanDetail.setRepayDelayInterestYes(
								debtLoanDetail.getRepayDelayInterestYes().add(borrowRepayDelayInterest));
						// 借款人已还逾期还款利息
						debtLoanDetail.setRepayLateInterestYes(
								debtLoanDetail.getRepayLateInterestYes().add(borrowRepayLateInterest));
						// 出借人收取已还提前还款利息
						debtLoanDetail.setReceiveAdvanceInterestYes(
								debtLoanDetail.getReceiveAdvanceInterestYes().add(receiveAdvanceInterest));
						// 出借人收取已还延期还款利息
						debtLoanDetail.setReceiveDelayInterestYes(
								debtLoanDetail.getReceiveDelayInterestYes().add(receiveDelayInterest));
						// 出借人收取已还逾期还款利息
						debtLoanDetail.setReceiveLateInterestYes(
								debtLoanDetail.getReceiveLateInterestYes().add(receiveLateInterest));
						// 出借人收取已还还款利息
						debtLoanDetail.setReceiveAccountYes(debtLoanDetail.getReceiveAccountYes().add(receiveAccount));
						// 出借人收取已还本金
						debtLoanDetail.setReceiveCapitalYes(debtLoanDetail.getReceiveCapitalYes().add(receiveCapital));
						// 出借人收取已还利息
						debtLoanDetail
								.setReceiveInterestYes(debtLoanDetail.getReceiveInterestYes().add(receiveInterest));
						boolean tenderBorrowRecoverPlanFlag = this.debtLoanDetailMapper
								.updateByPrimaryKeySelective(debtLoanDetail) > 0 ? true : false;
						if (!tenderBorrowRecoverPlanFlag) {
							throw new Exception("还款计划表(hyjf_debt_loan_detail)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
						}
						// 更新总的还款计划
						DebtRepayDetail debtRepayDetail = getBorrowRepayPlan(borrowNid, periodNow);
						if (debtRepayDetail == null) {
							throw new Exception("还款计划表(hyjf_debt_repay_detail)查询失败！" + "[出借订单号：" + tenderOrdId + "]");
						}
						debtRepayDetail.setRepayAccountAll(
								debtRepayDetail.getRepayAccountAll().add(borrowRepayAccount).add(perManage));
						debtRepayDetail
								.setRepayAccountYes(debtRepayDetail.getRepayAccountYes().add(borrowRepayAccount));
						debtRepayDetail.setRepayInterestYes(
								debtRepayDetail.getRepayInterestYes().add(borrowRepayInterest).subtract(serviceFee));
						debtRepayDetail
								.setRepayCapitalYes(debtRepayDetail.getRepayCapitalYes().add(borrowRepayCapital));
						debtRepayDetail.setAdvanceInterest(
								debtRepayDetail.getAdvanceInterest().add(borrowRepayAdvanceInterest));
						debtRepayDetail
								.setDelayInterest(debtRepayDetail.getDelayInterest().add(borrowRepayDelayInterest));
						debtRepayDetail.setLateInterest(debtRepayDetail.getLateInterest().add(borrowRepayLateInterest));
						debtRepayDetail.setLateDays(debtLoanDetail.getLateDays());
						debtRepayDetail.setAdvanceDays(debtLoanDetail.getAdvanceDays());
						debtRepayDetail.setDelayDays(debtLoanDetail.getDelayDays());
						// 用户是否提前还款
						debtRepayDetail.setAdvanceStatus(debtLoanDetail.getAdvanceStatus());
						boolean debtRepayDetailFlag = this.debtRepayDetailMapper
								.updateByPrimaryKeySelective(debtRepayDetail) > 0 ? true : false;
						if (!debtRepayDetailFlag) {
							throw new Exception("还款计划表(hyjf_debt_repay_detail)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
						}
					} else {
						throw new Exception("还款计划表(hyjf_debt_loan_detail)查询失败！" + "[出借订单号：" + tenderOrdId + "]");
					}
				}
				// 更新hyjf_debt_detail表
				debtDetail.setRepayActionTime(nowTime);
				// 已还款
				debtDetail.setRepayStatus(1);
				// 账户管理费
				debtDetail.setManageFee(perManage);
				// 已还本金
				debtDetail.setRepayCapitalYes(debtDetail.getRepayCapitalYes().add(receiveCapital));
				// 账户服务费
				debtDetail.setServiceFee(debtDetail.getServiceFee().add(serviceFee));
				// 已还利息
				debtDetail.setRepayInterestYes(debtDetail.getRepayInterestYes().add(receiveInterest));
				// 未收本金
				debtDetail.setRepayCapitalWait(BigDecimal.ZERO);
				// 未收利息
				debtDetail.setRepayInterestWait(BigDecimal.ZERO);
				// 提前还款状态
				debtDetail.setAdvanceStatus(advanceStatus);
				// 提前还款天数
				debtDetail.setAdvanceDays(advanceDays);
				// 提前还款利息
				debtDetail.setAdvanceInterest(receiveAdvanceInterest);
				// 延期天数
				debtDetail.setDelayDays(delayDays);
				// 延期利息
				debtDetail.setDelayInterest(receiveDelayInterest);
				// 逾期天数
				debtDetail.setLateDays(lateDays);
				// 逾期利息
				debtDetail.setLateInterest(receiveLateInterest);
				// 还款订单号
				debtDetail.setRepayOrderId(repayOrderId);
				// 债权更新时间
				debtDetail.setUpdateTime(nowTime);
				// 还款日期
				debtDetail.setRepayOrderDate(repayOrderDate);
				// 到期公允价值
				debtDetail.setExpireFairValue(BigDecimal.ZERO);
				// 债权详情表更新
				boolean debtDetailFlg = this.debtDetailMapper.updateByPrimaryKeySelective(debtDetail) > 0 ? true
						: false;
				if (!debtDetailFlg) {
					throw new Exception("还款分期计划表(hyjf_debt_detail)更新失败！" + "[承接订单号：" + tenderOrdId + "]");
				}
				// 更新借款表
				BigDecimal borrowManager = debtBorrow.getBorrowManager() == null ? BigDecimal.ZERO
						: new BigDecimal(debtBorrow.getBorrowManager());
				debtBorrow.setBorrowManager(borrowManager.add(perManage).toString());
				// 总还款利息
				debtBorrow.setRepayAccountYes(debtBorrow.getRepayAccountYes().add(borrowRepayAccount));
				// 总还款利息
				debtBorrow.setRepayAccountInterestYes(debtBorrow.getRepayAccountInterestYes().add(borrowRepayInterest));
				// 总还款本金
				debtBorrow.setRepayAccountCapitalYes(debtBorrow.getRepayAccountCapitalYes().add(borrowRepayCapital));
				// 未还款总额
				debtBorrow.setRepayAccountWait(debtBorrow.getRepayAccountWait().subtract(recoverAccountRemain));
				// 未还款利息
				debtBorrow.setRepayAccountInterestWait(
						debtBorrow.getRepayAccountInterestWait().subtract(recoverInterestRemain));
				// 未还款本金
				debtBorrow.setRepayAccountCapitalWait(
						debtBorrow.getRepayAccountCapitalWait().subtract(recoverCapitalRemain));
				debtBorrow.setRepayFeeNormal(debtBorrow.getRepayFeeNormal().add(perManage));
				boolean borrowFlag = this.debtBorrowMapper.updateByPrimaryKeySelective(debtBorrow) > 0 ? true : false;
				if (!borrowFlag) {
					throw new Exception("借款详情(hyjf_debt_borrow)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
				}
				// 更新出借表
				debtInvest.setRepayAccountYes(debtInvest.getRepayAccountYes().add(borrowRepayAccount));
				debtInvest.setRepayInterestYes(debtInvest.getRepayInterestYes().add(borrowRepayInterest));
				debtInvest.setRepayCapitalYes(debtInvest.getRepayCapitalYes().add(borrowRepayCapital));
				debtInvest.setRepayAccountWait(debtInvest.getRepayAccountWait().subtract(recoverAccountRemain));
				debtInvest.setRepayInterestWait(debtInvest.getRepayInterestWait().subtract(recoverInterestRemain));
				debtInvest.setRepayCapitalWait(debtInvest.getRepayCapitalWait().subtract(recoverCapitalRemain));
				if (isMonth && Validator.isNotNull(periodNext) && periodNext > 0) {
					debtInvest.setStatus(2);// 还款已完成
				} else {
					debtInvest.setStatus(3);// 还款已完成
				}
				boolean borrowTenderFlag = debtInvestMapper.updateByPrimaryKeySelective(debtInvest) > 0 ? true : false;
				if (!borrowTenderFlag) {
					throw new Exception("出借表(hyjf_debt_invest)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
				}
				DebtPlan plan = new DebtPlan();
				plan.setDebtPlanNid(debtLoan.getPlanNid());
				if (isLiquidatesPlanFlag) {
					plan.setLiquidateArrivalAmount(receiveAccount);
				}
				if (serviceFee.compareTo(BigDecimal.ZERO) > 0) {
					plan.setServiceFee(serviceFee);
				}
				boolean planUpdateFlag = this.batchDebtPlanCustomizeMapper.updatePlanCredit(plan) > 0 ? true : false;
				if (!planUpdateFlag) {
					throw new Exception("债权详情表(hyjf_debt_plan)更新失败!" + "[出借订单号:" + tenderOrdId + "]");
				}
				// 管理费大于0时,插入网站收支明细
				if (perManage.compareTo(BigDecimal.ZERO) > 0) {
					// 插入网站收支明细记录
					AccountWebList accountWebList = new AccountWebList();
					accountWebList.setOrdid(debtInvest.getOrderId() + "_" + periodNow);// 订单号
					accountWebList.setBorrowNid(borrowNid); // 出借编号
					accountWebList.setUserId(borrowUserid); // 借款人
					accountWebList.setAmount(perManage); // 管理费
					accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
					accountWebList.setTrade(CustomConstants.TRADE_REPAYFEE); // 管理费
					accountWebList.setTradeType(CustomConstants.TRADE_REPAYFEE_NM); // 账户管理费
					accountWebList.setRemark(borrowNid); // 出借编号
					accountWebList.setCreateTime(nowTime);
					boolean accountWebFlag = insertAccountWebList(accountWebList) > 0 ? true : false;
					if (!accountWebFlag) {
						throw new Exception(
								"网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号：" + debtInvest.getOrderId() + "]");
					}
				}
				// 服务费大于0时,插入网站收支明细
				if (serviceFee.compareTo(BigDecimal.ZERO) > 0) {
					// 插入网站收支明细记录
					AccountWebList accountWebList = new AccountWebList();
					// 订单号
					accountWebList.setOrdid(debtInvest.getOrderId() + "_" + borrowNid + "_" + periodNow);// 订单号
					accountWebList.setBorrowNid(borrowNid); // 出借编号
					accountWebList.setUserId(borrowUserid); // 借款人
					accountWebList.setAmount(serviceFee); // 管理费
					accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
					accountWebList.setTrade(CustomConstants.HTJ_TRADE_LOANFEE); // 汇添金服务费
					accountWebList.setTradeType(CustomConstants.HTJ_TRADE_LOANFEE_NM); // 汇添金服务费-还款
					accountWebList.setRemark(borrowNid); // 出借编号
					accountWebList.setCreateTime(nowTime);
					int accountWebListCnt = insertAccountWebList(accountWebList);
					if (accountWebListCnt == 0) {
						throw new Exception(
								"网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号：" + debtInvest.getOrderId() + "]");
					}
				}
				System.out.println("-----------部分债转承接部分还款结束---" + apicron.getBorrowNid() + "---------" + repayOrderId);
			} else if (borrowRepayAccount.compareTo(BigDecimal.ZERO) == 0 || Validator.isNull(debtDetail)) {
				System.out.println("------全部债转还款后续处理开始---" + apicron.getBorrowNid() + "---------" + repayOrderId);
				// 分期并且不是最后一期
				if (debtLoanDetail != null) {
					// 不是最后一期
					if (Validator.isNotNull(periodNext) && periodNext > 0) {
						debtLoan.setRepayStatus(0); // 未还款
						// 取得分期还款计划表下一期的还款
						DebtLoanDetail borrowRecoverPlanNext = getBorrowRecoverPlan(borrowNid, periodNow + 1,
								tenderUserId, debtLoan.getInvestId());
						debtLoan.setRepayTime(borrowRecoverPlanNext.getRepayTime()); // 计算下期时间
					} else {
						debtLoan.setRepayStatus(1); // 未还款
						debtLoan.setRepayActionTime(String.valueOf(nowTime)); // 实际还款时间
					}
					debtLoan.setRepayPeriod(periodNow);// 当前还款期数
					debtLoan.setRemainPeriod(periodNext);// 剩余还款期数
				} else {
					debtLoan.setRepayStatus(1); // 已还款
					debtLoan.setRepayActionTime(String.valueOf(nowTime)); // 实际还款时间
					debtLoan.setRepayPeriod(1);
					debtLoan.setRemainPeriod(0);
				}
				// 还款日期
				debtLoan.setRepayOrderDate(repayOrderDate);
				// 还款订单号
				debtLoan.setRepayOrderId(repayOrderId);
				// 写入网站收支
				debtLoan.setWeb(2);
				// 更新还款表
				boolean borrowRecoverFlag = this.debtLoanMapper.updateByPrimaryKeySelective(debtLoan) > 0 ? true
						: false;
				if (!borrowRecoverFlag) {
					throw new Exception("还款明细(hyjf_debt_loan)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
				}
				// 用户是否提前还款
				debtRepay.setAdvanceStatus(debtLoan.getAdvanceStatus());
				debtRepay.setDelayDays(debtLoan.getDelayDays());
				debtRepay.setLateDays(debtLoan.getLateDays());
				debtRepay.setAdvanceDays(debtLoan.getAdvanceDays());
				boolean borrowRepayFlag = this.debtRepayMapper.updateByPrimaryKeySelective(debtRepay) > 0 ? true
						: false;
				if (!borrowRepayFlag) {
					throw new Exception("还款表(hyjf_debt_repay)查询失败！" + "[出借订单号：" + tenderOrdId + "]");
				}
				if (isMonth && Validator.isNotNull(periodNext) && periodNext > 0) {
					debtInvest.setStatus(2);// 还款已完成
				} else {
					debtInvest.setStatus(3);// 还款已完成
				}
				boolean borrowTenderFlag = debtInvestMapper.updateByPrimaryKeySelective(debtInvest) > 0 ? true : false;
				if (!borrowTenderFlag) {
					throw new Exception("出借表(hyjf_debt_invest)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
				}
				if (isMonth) {
					// 最后还款时间
					debtLoanDetail.setRepayActionTime(String.valueOf(!isMonth ? nowTime : 0));
					// 债转还款状态
					debtLoanDetail.setRepayStatus(1);
					// 还款日期
					debtLoanDetail.setRepayOrderDate(repayOrderDate);
					// 还款订单号
					debtLoanDetail.setRepayOrderId(repayOrderId);
					// 更新还款计划表
					boolean borrowRecoverPlanFlag = this.debtLoanDetailMapper
							.updateByPrimaryKeySelective(debtLoanDetail) > 0 ? true : false;
					if (!borrowRecoverPlanFlag) {
						throw new Exception("还款明细(hyjf_debt_loan_detail)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
					}
					// 更新总的还款计划
					DebtRepayDetail debtRepayDetail = getBorrowRepayPlan(borrowNid, periodNow);
					if (debtRepayDetail == null) {
						throw new Exception("还款计划表(hyjf_debt_repay_detail)查询失败！" + "[出借订单号：" + tenderOrdId + "]");
					}
					debtRepayDetail.setLateDays(debtLoanDetail.getLateDays());
					debtRepayDetail.setAdvanceDays(debtLoanDetail.getAdvanceDays());
					debtRepayDetail.setDelayDays(debtLoanDetail.getDelayDays());
					// 用户是否提前还款
					debtRepayDetail.setAdvanceStatus(debtLoanDetail.getAdvanceStatus());
					boolean borrowRepayPlanFlag = this.debtRepayDetailMapper
							.updateByPrimaryKeySelective(debtRepayDetail) > 0 ? true : false;
					if (!borrowRepayPlanFlag) {
						throw new Exception("还款计划表(huiyingdai_borrow_repay_plan)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
					}
				}
				System.out.println("------全部债转还款后续处理结束---" + apicron.getBorrowNid() + "---------还款订单号：" + repayOrderId);
			} else if (borrowRepayAccount.compareTo(BigDecimal.ZERO) < 0) {
				// 给洪刚发送短信告诉剩余金额出现负数
				Map<String, String> replaceStrs = new HashMap<String, String>();
				replaceStrs.put("val_title",
						"项目编号：【" + debtBorrow.getBorrowNid() + "】+订单号：【" + debtLoan.getInvestOrderId() + "】");
				replaceStrs.put("val_period", isMonth ? "第" + apicron.getPeriodNow() + "期" : "");
				replaceStrs.put("val_package_error", "债转还款后,剩余金额出现负数");
				throw new Exception("债转还款后,剩余金额出现负数！" + "[出借订单号：" + tenderOrdId + "]");
			}
		} else {
			// 给洪刚发短信，告诉他还款金额不相等
			Map<String, String> replaceStrs = new HashMap<String, String>();
			replaceStrs.put("val_title",
					"项目编号：【" + debtBorrow.getBorrowNid() + "】+订单号：【" + debtLoan.getInvestOrderId() + "】");
			replaceStrs.put("val_period", isMonth ? "第" + apicron.getPeriodNow() + "期" : "");
			replaceStrs.put("val_package_error", "债转还款金额不相等");
			throw new Exception("债转还款金额不相等！" + "[出借订单号：" + tenderOrdId + "]");
		}
		System.out.println("------债转还款未承接部分结束---项目编号：" + apicron.getBorrowNid() + "---------还款订单号：" + repayOrderId);
		return true;
	}

	/**
	 * @param debtCredit
	 * @param debtPlanAccede
	 * @param account
	 * @return
	 * @throws Exception
	 */

	private BigDecimal calculateServiceFee(String planOrderId, BigDecimal account) throws Exception {

		DebtPlanAccede liquidatesDebtPlanAccede = this.getDebtPlanAccedeInfo(planOrderId);
		if (Validator.isNotNull(liquidatesDebtPlanAccede)) {
			BigDecimal serviceFeeRate = liquidatesDebtPlanAccede.getServiceFeeRate();
			BigDecimal serviceFee = account.multiply(serviceFeeRate).divide(new BigDecimal(100), 2,
					BigDecimal.ROUND_DOWN);
			return serviceFee;
		} else {
			throw new Exception("未查询到相应的计划加入记录,计划订单号：" + planOrderId);
		}
	}

	/**
	 * 查询相应的recover数据
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public DebtLoan getBorrowRecover(Integer id) {
		DebtLoan borrowRecover = this.debtLoanMapper.selectByPrimaryKey(id);
		return borrowRecover;
	}

	/**
	 * 判断该收支明细是否存在
	 *
	 * @param accountList
	 * @return
	 */
	private int countAccountTenderListByNid(String nid) {
		DebtAccountListExample accountListExample = new DebtAccountListExample();
		accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("tender_recover_yes");
		return this.debtAccountListMapper.countByExample(accountListExample);
	}

	private BigDecimal selectCreditRepayTotal(String borrowNid, String nid, Integer period) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		params.put("period", period);
		String sumStr = this.batchDebtCreditRepayCustomizeMapper.countCreditRepaySum(params);
		BigDecimal sum = StringUtils.isNotBlank(sumStr) ? new BigDecimal(sumStr) : new BigDecimal(0);
		return sum;
	}

	@Override
	public DebtCredit selectDebtCredit(Integer id) {
		DebtCredit debtCredit = this.debtCreditMapper.selectByPrimaryKey(id);
		return debtCredit;
	}

	@Override
	public boolean creditRepay(DebtApicron apicron, DebtBorrowWithBLOBs borrow, AccountChinapnr borrowUserCust,
			DebtLoan debtLoan, DebtCredit debtCredit) throws Exception {
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 原始项目编号
		String borrowNid = debtCredit.getBorrowNid();
		// 原始出借订单号
		String investOrderId = debtCredit.getInvestOrderId();
		// 债转编号
		String creditNid = debtCredit.getCreditNid();
		// 还款时间
		Integer recoverLastTime = debtLoan.getCreateTime();
		// 还款时间
		Date recoverLastDate = GetDate.getDate(recoverLastTime * 1000L);
		// 当前还款期数
		int periodNow = apicron.getPeriodNow();
		// 出借用户userId
		int tenderUserId = debtLoan.getUserId();
		// 项目总期数
		int borrowPeriod = borrow.getBorrowPeriod();
		// 剩余还款期数
		Integer periodNext = borrowPeriod - periodNow;
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		// 查询债转的出借记录
		List<DebtCreditTender> creditTenderList = this.selectCreditTenderList(borrowNid, investOrderId, creditNid);
		if (creditTenderList != null && creditTenderList.size() > 0) {
			System.out.println("------此笔债转承接部分还款开始---项目编号：" + borrowNid + "----债转编号：" + creditNid + "---------");
			// 遍历债转出借
			for (int j = 0; j < creditTenderList.size(); j++) {
				try {
					// 债转出借信息
					DebtCreditTender creditTender = creditTenderList.get(j);
					if (creditTender != null) {
						// 承接用户userId
						int assignUserId = creditTender.getUserId();
						// 承接订单号
						String assignNid = creditTender.getAssignOrderId();
						// 查询此笔债转承接的还款情况
						DebtCreditRepay creditRepay = this.selectCreditRepay(borrowNid, assignUserId, investOrderId,
								assignNid, periodNow, 0);
						// 判断债转承接记录是否存在
						if (Validator.isNotNull(creditRepay)) {
							// 还款订单号
							String repayOrderId = null;
							// 还款订单日期
							String repayOrderDate = null;
							// 保存债转还款订单号
							if (StringUtils.isBlank(creditRepay.getCreditRepayOrderId())) {
								// 还款订单号
								repayOrderId = GetOrderIdUtils.getOrderId2(creditRepay.getUserId());
								// 还款订单日期
								repayOrderDate = GetOrderIdUtils.getOrderDate();
								// 还款订单号
								creditRepay.setCreditRepayOrderId(repayOrderId);
								// 还款订单日期
								creditRepay.setCreditRepayOrderDate(repayOrderDate);
								// 更新还款订单号
								boolean flag = this.updateCreditRepay(creditRepay) > 0 ? true : false;
								if (!flag) {
									throw new Exception("添加还款订单号，更新credit_repay表失败" + "，[承接订单号："
											+ creditRepay.getAssignOrderId() + "]+,期数：" + periodNow);
								}
							} else {
								// 还款订单号
								repayOrderId = creditRepay.getCreditRepayOrderId();
								// 还款订单日期
								repayOrderDate = creditRepay.getCreditRepayOrderDate();
							}
							// 自动还款
							boolean creditRepayFlag = this.updateBorrowCreditRepay(apicron, borrow, debtLoan,
									borrowUserCust, debtCredit, creditTender, creditRepay);
							if (creditRepayFlag) {
								boolean flag = this.debtBorrowRepayService.repayBackUpdate(creditTender.getUserId(),
										creditTender.getUserName(), creditTender.getAssignPlanNid(),
										creditTender.getAssignPlanOrderId());
								if (!flag) {
									throw new Exception("还款后后续解冻冻结失败，加入订单号：" + debtLoan.getPlanOrderId());
								}
							} else {
								throw new Exception("债转部分还款失败！" + "[债转编号：" + creditNid + "]");
							}
						} else {
							return true;
						}
					} else {
						throw new Exception("债转记录(hyjf_credit_tender)查询失败，债转记录为空！" + "[出借订单号：" + investOrderId + "]");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 取得分期还款计划表
			DebtLoanDetail debtLoanDetail = this.getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId,
					debtLoan.getInvestId());
			// 债转状态
			if (debtLoanDetail != null && Validator.isNotNull(periodNext) && periodNext > 0) {
				debtCredit.setRepayStatus(1);
			} else {
				debtCredit.setRepayStatus(2);
				// 债转最后还款时间
				debtCredit.setCreditRepayYesTime(isMonth ? 0 : nowTime);
			}
			if (debtCredit.getCreditStatus() == 0) {
				debtCredit.setCreditStatus(1);
			}
			// 下期还款时间
			int repayNextTime = DateUtils.getRepayDate(borrowStyle, recoverLastDate, periodNow + 1, 0);
			debtCredit.setCreditRepayNextTime(repayNextTime);
			// 债转最近还款时间
			debtCredit.setCreditRepayLastTime(nowTime);
			// 债转还款期
			debtCredit.setRepayPeriod(periodNow);
			boolean flag = this.updateBorrowCredit(debtCredit) > 0 ? true : false;
			if (flag) {
				System.out.println("------此笔债转承接部分还款结束---项目编号：" + borrowNid + "----债转编号：" + creditNid + "---------");
				return true;
			} else {
				throw new Exception("债转记录(hyjf_debt_credit)更新失败！" + "[债转编号：" + creditNid + "]");
			}
		} else {
			System.out.println("------此笔债转未有承接记录,或者债转还款已经完成，更新borrowCredit---项目编号：" + borrowNid + "----债转编号："
					+ creditNid + "---------");
			// 下期还款时间
			int repayNextTime = DateUtils.getRepayDate(borrowStyle, recoverLastDate, periodNow + 1, 0); // 取得分期还款计划表
			DebtLoanDetail borrowRecoverPlan = this.getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId,
					debtLoan.getInvestId());
			boolean debtCreditFlag = false;
			if (borrowRecoverPlan != null && Validator.isNotNull(periodNext) && periodNext > 0) {
				// 债转项目没有出借记录
				debtCreditFlag = this.updateBorrowCredit(creditNid, periodNow - 1, 1, nowTime,
						isMonth ? repayNextTime : 0);
			} else {
				// 债转项目没有出借记录
				debtCreditFlag = this.updateBorrowCredit(creditNid, periodNow - 1, 2, nowTime, nowTime);
			}
			if (debtCreditFlag) {
				System.out.println("------此笔债转未有承接记录,或者债转还款已经完成，更新borrowCredit---项目编号：" + borrowNid + "----债转编号："
						+ creditNid + "---------");
				return true;
			} else {
				throw new Exception("债转记录(hyjf_debt_credit)更新失败！" + "[债转编号：" + creditNid + "]");
			}
		}
	}

	@Override
	public boolean updateCreditRecord(DebtCredit debtCredit, DebtBorrowWithBLOBs borrow, DebtApicron debtApicron) {

		Integer periodNow = debtApicron.getPeriodNow();
		// 项目总期数
		int borrowPeriod = borrow.getBorrowPeriod();
		// 当前期放款明细
		DebtLoanDetail loanDetail = null;
		// 最后一期放款明细
		DebtLoanDetail loanLastDetail = null;
		// 上期还款时间
		String repayTime = null;
		// 下一期还款时间
		String repayNextTime = null;
		// 最后一期还款时间
		String repayLastTime = null;
		// 剩余天数
		int creditTerm = 0;
		// 当前期剩余天数
		Integer remainDays = 0;

		DebtLoanDetailExample example = new DebtLoanDetailExample();
		DebtLoanDetailExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrow.getBorrowNid());
		cra.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
		cra.andRepayPeriodEqualTo(periodNow);
		List<DebtLoanDetail> loanList = this.debtLoanDetailMapper.selectByExample(example);
		if (loanList != null && loanList.size() > 0) {
			loanDetail = loanList.get(0);
			repayTime = loanDetail.getRepayTime();
		}
		DebtLoanDetailExample exampleNext = new DebtLoanDetailExample();
		DebtLoanDetailExample.Criteria craNext = exampleNext.createCriteria();
		craNext.andBorrowNidEqualTo(borrow.getBorrowNid());
		craNext.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
		craNext.andRepayPeriodEqualTo(periodNow + 1);
		List<DebtLoanDetail> loanNextList = this.debtLoanDetailMapper.selectByExample(exampleNext);
		if (loanNextList != null && loanNextList.size() > 0) {
			loanDetail = loanNextList.get(0);
			repayNextTime = loanDetail.getRepayTime();
		}
		// 剩余天数
		try {
			remainDays = GetDate.daysBetween(GetDate.timestamptoStrYYYYMMDD(Integer.parseInt(repayTime)),
					GetDate.timestamptoStrYYYYMMDD(Integer.parseInt(repayNextTime)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 最后一期还款时间
		DebtLoanDetailExample example1 = new DebtLoanDetailExample();
		DebtLoanDetailExample.Criteria cra1 = example1.createCriteria();
		cra1.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
		cra1.andBorrowNidEqualTo(borrow.getBorrowNid());
		cra1.andRepayPeriodEqualTo(borrowPeriod);
		List<DebtLoanDetail> loanLastList = this.debtLoanDetailMapper.selectByExample(example1);
		if (loanLastList != null && loanLastList.size() > 0) {
			loanLastDetail = loanLastList.get(0);
			repayLastTime = loanLastDetail.getRepayTime();
		}
		// 剩余天数
		try {
			creditTerm = GetDate.daysBetween(GetDate.timestamptoStrYYYYMMDD(Integer.parseInt(repayTime)),
					GetDate.timestamptoStrYYYYMMDD(Integer.parseInt(repayLastTime)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DebtDetailExample detailExample = new DebtDetailExample();
		DebtDetailExample.Criteria detailCrt = detailExample.createCriteria();
		detailCrt.andPlanNidEqualTo(debtCredit.getPlanNid());
		detailCrt.andPlanOrderIdEqualTo(debtCredit.getPlanOrderId());
		detailCrt.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
		detailCrt.andOrderIdEqualTo(debtCredit.getSellOrderId());
		detailCrt.andRepayPeriodGreaterThanOrEqualTo(debtCredit.getLiquidatesPeriod() + 1);
		detailCrt.andStatusEqualTo(1);
		detailCrt.andRepayStatusEqualTo(0);
		List<DebtDetail> debtDetailList = this.debtDetailMapper.selectByExample(detailExample);
		BigDecimal waitCapitalSum = BigDecimal.ZERO;
		BigDecimal waitInterestSum = BigDecimal.ZERO;
		BigDecimal waitDelayInterestSum = BigDecimal.ZERO;
		BigDecimal waitLateInterestSum = BigDecimal.ZERO;
		BigDecimal capitalSum = BigDecimal.ZERO;
		if (debtDetailList != null && debtDetailList.size() > 0) {
			for (int i = 0; i < debtDetailList.size(); i++) {
				DebtDetail debtDetail = debtDetailList.get(i);
				waitCapitalSum = waitCapitalSum.add(debtDetail.getRepayCapitalWait());
				waitInterestSum = waitInterestSum.add(debtDetail.getRepayInterestWait());
				waitDelayInterestSum = waitDelayInterestSum
						.add(debtDetail.getDelayInterest().subtract(debtDetail.getDelayInterestAssigned()));
				waitLateInterestSum = waitLateInterestSum
						.add(debtDetail.getLateInterest().subtract(debtDetail.getLateInterestAssigned()));
				capitalSum = capitalSum.add(debtDetail.getLoanCapital());
			}
		}
		// 如果债权未被清算
		if (debtCredit.getIsLiquidates() == 0) {
			int nowTime = GetDate.getNowTime10();
			DebtCredit newDebtCredit = new DebtCredit();
			newDebtCredit.setUserId(debtCredit.getUserId());
			newDebtCredit.setUserName(debtCredit.getUserName());
			// 计划编号
			newDebtCredit.setPlanNid(debtCredit.getPlanNid());
			// 计划加入订单号
			newDebtCredit.setPlanOrderId(debtCredit.getPlanOrderId());
			// 借款编号
			newDebtCredit.setBorrowNid(debtCredit.getBorrowNid());
			// 借款人用户名
			newDebtCredit.setBorrowName(debtCredit.getBorrowName());
			// 原标年化利率
			newDebtCredit.setBorrowApr(debtCredit.getBorrowApr());
			// 清算后债权实际年华收益率（清算时计算，可能会影响计划发布）
			newDebtCredit.setActualApr(debtCredit.getBorrowApr());
			// 原始标的出借订单号
			newDebtCredit.setInvestOrderId(debtCredit.getInvestOrderId());
			// 原出借订单号nid
			newDebtCredit.setSellOrderId(debtCredit.getSellOrderId());
			// 债转nid
			newDebtCredit.setCreditNid(GetOrderIdUtils.getOrderId0(debtCredit.getUserId()));
			// 转让状态
			newDebtCredit.setCreditStatus(0);
			// 还款状态
			newDebtCredit.setRepayStatus(0);
			// 排序
			newDebtCredit.setCreditOrder(0);
			// 持有天数
			newDebtCredit.setHoldDays(0);
			// 剩余天数
			newDebtCredit.setRemainDays(remainDays);
			// 承接所在期
			newDebtCredit.setAssignPeriod(debtCredit.getAssignPeriod() + 1);
			// 清算所在期
			newDebtCredit.setLiquidatesPeriod(debtCredit.getLiquidatesPeriod() + 1);
			// 债转期数
			newDebtCredit.setCreditPeriod(debtCredit.getCreditPeriod() - 1);
			// 已还期数
			newDebtCredit.setRepayPeriod(debtCredit.getRepayPeriod());
			// 债转期限
			newDebtCredit.setCreditTerm(creditTerm);
			// 是否原始债权 0非原始 1原始
			newDebtCredit.setSourceType(debtCredit.getSourceType());
			// 公允价值
			newDebtCredit.setLiquidationFairValue(debtCredit.getLiquidationFairValue());
			// 清算总本金
			newDebtCredit.setLiquidatesCapital(capitalSum);
			// 债转总额
			newDebtCredit.setCreditAccount(waitCapitalSum.add(waitInterestSum));
			// 债转总本金
			newDebtCredit.setCreditCapital(waitCapitalSum);
			// 债转总利息
			newDebtCredit.setCreditInterest(waitInterestSum);
			// 已承接总金额
			newDebtCredit.setCreditAccountAssigned(BigDecimal.ZERO);
			// 已承接本金
			newDebtCredit.setCreditCapitalAssigned(BigDecimal.ZERO);
			// 已承接待还总利息
			newDebtCredit.setCreditInterestAssigned(BigDecimal.ZERO);
			// 待承接总金额
			newDebtCredit.setCreditAccountWait(waitCapitalSum.add(waitInterestSum));
			// 待承接本金
			newDebtCredit.setCreditCapitalWait(waitCapitalSum);
			// 待承接利息
			newDebtCredit.setCreditInterestWait(waitInterestSum);
			// 垫付利息总额
			newDebtCredit.setCreditInterestAdvance(waitDelayInterestSum.add(waitLateInterestSum));
			// 剩余待垫付利息
			newDebtCredit.setCreditInterestAdvanceWait(waitDelayInterestSum.add(waitLateInterestSum));
			// 已承接垫付总利息
			newDebtCredit.setCreditInterestAdvanceAssigned(BigDecimal.ZERO);
			// 待承接垫付延期利息
			newDebtCredit.setCreditDelayInterest(waitDelayInterestSum);
			// 待承接垫付延期利息
			newDebtCredit.setCreditDelayInterestAssigned(BigDecimal.ZERO);
			// 待承接垫付延期利息
			newDebtCredit.setCreditLateInterest(waitLateInterestSum);
			// 已承接垫付延期利息
			newDebtCredit.setCreditLateInterestAssigned(BigDecimal.ZERO);
			// 下次还款时间
			newDebtCredit.setCreditRepayNextTime(debtCredit.getCreditRepayNextTime());
			// 折价率
			newDebtCredit.setCreditDiscount(BigDecimal.ZERO);
			// 总收入
			newDebtCredit.setCreditIncome(BigDecimal.ZERO);
			// 服务费
			newDebtCredit.setCreditServiceFee(BigDecimal.ZERO);
			// 出让价格
			newDebtCredit.setCreditPrice(BigDecimal.ZERO);
			// 已还款总额
			newDebtCredit.setRepayAccount(BigDecimal.ZERO);
			// 已还本金
			newDebtCredit.setRepayCapital(BigDecimal.ZERO);
			// 已还利息
			newDebtCredit.setRepayInterest(BigDecimal.ZERO);
			// 待承接总金额
			newDebtCredit.setRepayAccountWait(waitCapitalSum.add(waitInterestSum));
			// 待承接本金
			newDebtCredit.setRepayCapitalWait(waitCapitalSum);
			// 待承接利息
			newDebtCredit.setRepayInterestWait(waitInterestSum);
			// 债转计划最后还款时间
			newDebtCredit.setCreditRepayEndTime(debtCredit.getCreditRepayEndTime());
			// 上次还款时间
			newDebtCredit.setCreditRepayLastTime(debtCredit.getCreditRepayLastTime());
			// 下次还款时间
			newDebtCredit.setCreditRepayNextTime(debtCredit.getCreditRepayNextTime());
			// 最终实际还款时间
			newDebtCredit.setCreditRepayYesTime(0);
			// 债转结束时间
			newDebtCredit.setEndTime(0);
			// 承接次数
			newDebtCredit.setAssignNum(0);
			// 删除标识
			newDebtCredit.setDelFlag(0);
			// 债转发起客户端
			newDebtCredit.setClient(debtCredit.getClient());
			// 当前时间
			newDebtCredit.setCreateTime(nowTime);
			// 创建者Id
			newDebtCredit.setCreateUserId(debtCredit.getCreateUserId());
			// 创建者用户名
			newDebtCredit.setCreateUserName(debtCredit.getCreateUserName());
			// 新的债权插入
			boolean isinsertFlag = this.debtCreditMapper.insertSelective(newDebtCredit) > 0 ? true : false;
			if (isinsertFlag) {
				debtCredit.setIsLiquidates(1);
				boolean liquidatesFlag = this.debtCreditMapper.updateByPrimaryKeySelective(debtCredit) > 0 ? true
						: false;
				return liquidatesFlag;
			} else {
				throw new RuntimeException();
			}
		} else {
			return true;
		}
	}

	@Override
	public boolean creditAssignRepay(DebtApicron apicron, DebtBorrowWithBLOBs borrow, AccountChinapnr borrowUserCust,
			DebtLoan debtLoan, DebtCredit debtCredit) {

		// 当前还款的期数
		int periodNow = apicron.getPeriodNow();
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 项目总期数
		int borrowPeriod = borrow.getBorrowPeriod();
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		try {
			try {
				// 2.债转还款相应的已承接部分
				boolean creditRepayFlag = this.creditRepay(apicron, borrow, borrowUserCust, debtLoan, debtCredit);
				if (creditRepayFlag) {
					try {
						// 如果是分期项目且不是还款最后一期
						if (isMonth && periodNow < borrowPeriod) {
							if (debtCredit.getIsLiquidates() == 0) {
								// 清算相应的分期数据
								boolean newCreditFlag = this.updateCreditRecord(debtCredit, borrow, apicron);
								if (newCreditFlag) {
									return true;
								} else {
									throw new Exception("还款后,清算新债权失败,原债权编号" + debtCredit.getCreditNid());
								}
							} else {
								return true;
							}
						} else {
							boolean liquidatesFlag = this.updateCreditLiquidates(borrow, apicron, debtCredit);
							if (liquidatesFlag) {
								return true;
							} else {
								throw new Exception("债转承接中还款后更新相应的清算状态失败，债转编号：" + debtCredit.getCreditNid());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					throw new Exception("债转承接中债转部分还款失败，债转编号：" + debtCredit.getCreditNid());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateCreditLiquidates(DebtBorrowWithBLOBs borrow, DebtApicron apicron, DebtCredit debtCredit) {

		int nowTime = GetDate.getNowTime10();
		// 借款编号
		String borrowNid = apicron.getBorrowNid();
		// 当前还款的期数
		int periodNow = apicron.getPeriodNow();
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		// 债转下次还款时间
		if (isMonth) {
			// 分期的场合，根据借款编号和还款期数从还款计划表中取得还款时间
			DebtRepayDetailExample example = new DebtRepayDetailExample();
			Criteria borrowCriteria = example.createCriteria();
			borrowCriteria.andBorrowNidEqualTo(borrowNid);
			borrowCriteria.andRepayPeriodEqualTo(periodNow + 1);
			List<DebtRepayDetail> repayDetailList = debtRepayDetailMapper.selectByExample(example);
			if (repayDetailList.size() > 0) {
				DebtRepayDetail debtRepayDetail = repayDetailList.get(0);
				debtCredit.setCreditRepayNextTime(Integer.valueOf(debtRepayDetail.getRepayTime())); // 下期还款时间
				debtCredit.setRepayStatus(1);
			} else {
				debtCredit.setRepayStatus(2);
				// 债转最后还款时间
				debtCredit.setCreditRepayYesTime(nowTime);
			}
		} else {
			debtCredit.setRepayStatus(2);
			// 债转最后还款时间
			debtCredit.setCreditRepayYesTime(nowTime);
		}
		if (debtCredit.getCreditStatus() == 0) {
			debtCredit.setCreditStatus(1);
		}
		// 债转最近还款时间
		debtCredit.setCreditRepayLastTime(nowTime);
		// 债转还款期
		debtCredit.setRepayPeriod(periodNow);
		// 债权是否已经清算
		debtCredit.setIsLiquidates(1);
		boolean liquidatesFlag = this.debtCreditMapper.updateByPrimaryKeySelective(debtCredit) > 0 ? true : false;
		return liquidatesFlag;
	}

}

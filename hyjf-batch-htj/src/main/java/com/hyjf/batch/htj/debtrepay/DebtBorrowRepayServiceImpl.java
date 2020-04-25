package com.hyjf.batch.htj.debtrepay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.DebtAccountList;
import com.hyjf.mybatis.model.auto.DebtAccountListExample;
import com.hyjf.mybatis.model.auto.DebtApicron;
import com.hyjf.mybatis.model.auto.DebtApicronExample;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
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
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * 自动扣款(还款服务)
 *
 * @author Administrator
 *
 */
@Service
public class DebtBorrowRepayServiceImpl extends BaseServiceImpl implements DebtBorrowRepayService {

	private static final String THIS_CLASS = DebtBorrowRepayServiceImpl.class.getName();

	/** 用户ID */
	private static final String VAL_USERID = "userId";
	/** 放款金额 */
	private static final String VAL_AMOUNT = "val_amount";

	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	/**
	 * 自动还款
	 *
	 * @throws Exception
	 */
	@Override
	public boolean updateBorrowRepay(DebtApicron apicron, DebtLoan debtLoan, AccountChinapnr borrowUserCust)
			throws Exception {

		String methodName = "updateBorrowRepay";
		System.out.println("-----------还款开始---" + apicron.getBorrowNid() + "---------");
		/** 借款人相关信息 */
		// 还款订单号
		String repayOrderId = null;
		// 还款订单日期
		String repayOrderDate = null;
		// 借款人应还款本息
		BigDecimal recoverAccount = BigDecimal.ZERO;
		// 借款人应还款本金
		BigDecimal recoverCapital = BigDecimal.ZERO;
		// 借款人应还款利息
		BigDecimal recoverInterest = BigDecimal.ZERO;
		// 借款人总还款金额（包含管理费）
		BigDecimal borrowRepayAccountAll = BigDecimal.ZERO;
		// 借款人还款本息
		BigDecimal borrowRepayAccount = BigDecimal.ZERO;
		// 借款人还款本金
		BigDecimal borrowRepayCapital = BigDecimal.ZERO;
		// 借款人还款利息
		BigDecimal borrowRepayInterest = BigDecimal.ZERO;
		// 借款人提前还款少还利息
		BigDecimal borrowRepayAdvanceInterest = BigDecimal.ZERO;
		// 借款人延期利息
		BigDecimal borrowRepayDelayInterest = BigDecimal.ZERO;
		// 借款人逾期利息
		BigDecimal borrowRepayLateInterest = BigDecimal.ZERO;
		// 还款状态 0正常还款 1提前还款 2延期还款 3逾期还款
		int advanceStatus = 0;
		// 提前天数
		Integer advanceDays = 0;
		// 延期天数
		Integer delayDays = 0;
		// 延期天数
		Integer lateDays = 0;
		// 管理费
		BigDecimal recoverFee = BigDecimal.ZERO;
		// 汇添金服务费
		BigDecimal serviceFee = BigDecimal.ZERO;

		/** 出借人相关信息 */
		// 出借人实际到账本息
		BigDecimal receiveAccount = BigDecimal.ZERO;
		// 出借人实际到账本金
		BigDecimal receiveCapital = BigDecimal.ZERO;
		// 出借人实际到账利息
		BigDecimal receiveInterest = BigDecimal.ZERO;
		// 出借人提前还款少还利息
		BigDecimal receiveAdvanceInterest = BigDecimal.ZERO;
		// 出借人延期利息
		BigDecimal receiveDelayInterest = BigDecimal.ZERO;
		// 出借人逾期利息
		BigDecimal receiveLateInterest = BigDecimal.ZERO;

		/** 参数信息 */
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 借款编号
		String borrowNid = apicron.getBorrowNid();
		// 借款人ID
		Integer borrowUserid = apicron.getUserId();
		// 当前期数
		Integer periodNow = apicron.getPeriodNow();
		// 取得借款详情
		DebtBorrowWithBLOBs borrow = getBorrow(borrowNid);
		// 标的是否进行银行托管
		int bankInputFlag = Validator.isNull(borrow.getBankInputFlag()) ? 0 : borrow.getBankInputFlag();
		// 取得还款详情
		DebtRepay borrowRepay = getBorrowRepay(borrowNid);
		// 还款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 剩余还款期数
		Integer periodNext = borrowPeriod - periodNow;
		// 出借订单号
		String tenderOrdId = debtLoan.getInvestOrderId();
		// 出借人用户ID
		Integer tenderUserId = debtLoan.getUserId();
		// 出借ID
		Integer tenderId = debtLoan.getInvestId();
		// 取得出借信息
		DebtInvest borrowTender = getBorrowTender(tenderId);
		// 出借人在汇付的账户信息
		AccountChinapnr tenderUserCust = getChinapnrUserInfo(tenderUserId);
		if (tenderUserCust == null) {
			throw new RuntimeException("出借人未开户。[用户ID：" + tenderUserId + "]，" + "[出借订单号：" + tenderOrdId + "]");
		}
		// 出借人客户号
		Long tenderUserCustId = tenderUserCust.getChinapnrUsrcustid();
		// 借款人客户号
		Long borrowUserCustId = borrowUserCust.getChinapnrUsrcustid();
		// 更新DebtDetail表
		DebtDetail debtDetail = this.getDebtDetail(tenderOrdId, periodNow);
		// 分期还款计划表
		DebtLoanDetail debtLoanDetail = null;
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		// [principal: 等额本金, month:等额本息, month:等额本息, endmonth:先息后本]
		if (isMonth) {
			// 取得分期还款计划表
			debtLoanDetail = getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId, debtLoan.getInvestId());
			if (debtLoanDetail != null) {
				// 还款订单号
				repayOrderId = debtLoanDetail.getRepayOrderId();
				// 还款订单日期
				repayOrderDate = debtLoanDetail.getRepayOrderDate();
				// 应还款本息
				recoverAccount = debtLoanDetail.getLoanAccount();
				// 应还款本金
				recoverCapital = debtLoanDetail.getLoanCapital();
				// 应还款利息
				recoverInterest = debtLoanDetail.getLoanInterest();
				// 借款人提前还款状态
				advanceStatus = debtLoanDetail.getAdvanceStatus();
				// 借款人还款提前天数
				advanceDays = debtLoanDetail.getAdvanceDays();
				// 借款人提前还款少还利息
				borrowRepayAdvanceInterest = debtLoanDetail.getRepayAdvanceInterest();
				// 延期天数
				delayDays = debtLoanDetail.getDelayDays();
				// 借款人应还延期利息
				borrowRepayDelayInterest = debtLoanDetail.getRepayDelayInterest();
				// 逾期天数
				lateDays = debtLoanDetail.getLateDays();
				// 借款人应还逾期利息
				borrowRepayLateInterest = debtLoanDetail.getRepayLateInterest();
				// 管理费
				recoverFee = debtLoanDetail.getManageFee();
				// 借款人总还款金额（包含管理费）
				borrowRepayAccountAll = debtLoanDetail.getLoanAccount().add(borrowRepayLateInterest)
						.add(borrowRepayDelayInterest).add(borrowRepayAdvanceInterest).add(recoverFee);
				// 借款人应还款本息
				borrowRepayAccount = debtLoanDetail.getLoanAccount().add(borrowRepayLateInterest)
						.add(borrowRepayDelayInterest).add(borrowRepayAdvanceInterest);
				// 借款人应还款本金
				borrowRepayCapital = debtLoanDetail.getLoanCapital();
				// 借款人应还款利息
				borrowRepayInterest = debtLoanDetail.getLoanInterest().add(borrowRepayLateInterest)
						.add(borrowRepayDelayInterest).add(borrowRepayAdvanceInterest);

				// 提前还款的计算
				if (advanceStatus == 1) {
					receiveAdvanceInterest = borrowRepayAdvanceInterest;
				}
				// 延期的计算
				else if (advanceStatus == 2) {
					// 计算用户延期利息
					receiveDelayInterest = debtDetail.getDelayInterest();
				}
				// 逾期的计算
				else if (advanceStatus == 3) {
					// 计算用户延期利息
					receiveDelayInterest = debtDetail.getDelayInterest();
					// 计算用户逾期利息
					receiveLateInterest = debtDetail.getLateInterest();
				}
				// 出借人实际到账本息
				receiveAccount = debtLoanDetail.getLoanAccount().add(receiveAdvanceInterest).add(receiveDelayInterest)
						.add(receiveLateInterest);
				// 出借人实际到账本金
				receiveCapital = debtLoanDetail.getLoanCapital();
				// 出借人实际到账利息
				receiveInterest = debtLoanDetail.getLoanInterest().add(receiveAdvanceInterest).add(receiveDelayInterest)
						.add(receiveLateInterest);
			} else {
				throw new RuntimeException("分期还款计划表数据不存在。[借款编号：" + borrowNid + "]，" + "[出借订单号：" + tenderOrdId + "]，"
						+ "[期数：" + periodNow + "]");
			}
		}
		// [endday: 按天计息, end:按月计息]
		else {// 还款订单号
			repayOrderId = debtLoan.getRepayOrderId();
			// 还款订单日期
			repayOrderDate = debtLoan.getRepayOrderDate();
			// 应还款本息
			recoverAccount = debtLoan.getLoanAccount();
			// 应还款本金
			recoverCapital = debtLoan.getLoanCapital();
			// 应还款利息
			recoverInterest = debtLoan.getLoanInterest();
			// 借款人提前还款状态
			advanceStatus = debtLoan.getAdvanceStatus();
			// 借款人还款提前天数
			advanceDays = debtLoan.getAdvanceDays();
			// 借款人提前还款少还利息
			borrowRepayAdvanceInterest = debtLoan.getRepayAdvanceInterest();
			// 延期天数
			delayDays = debtLoan.getDelayDays();
			// 借款人应还延期利息
			borrowRepayDelayInterest = debtLoan.getRepayDelayInterest();
			// 逾期天数
			lateDays = debtLoan.getLateDays();
			// 借款人应还逾期利息
			borrowRepayLateInterest = debtLoan.getRepayLateInterest();
			// 借款人应还款本金
			borrowRepayCapital = debtLoan.getLoanCapital();
			// 借款人应还款本息
			borrowRepayAccount = debtLoan.getLoanAccount().add(borrowRepayLateInterest).add(borrowRepayDelayInterest)
					.add(borrowRepayAdvanceInterest);
			// 借款人应还款利息
			borrowRepayInterest = debtLoan.getLoanInterest().add(borrowRepayLateInterest).add(borrowRepayDelayInterest)
					.add(borrowRepayAdvanceInterest);
			// 管理费
			recoverFee = debtLoan.getManageFee();
			// 借款人总还款金额（包含管理费）
			borrowRepayAccountAll = debtLoan.getLoanAccount().add(borrowRepayLateInterest).add(borrowRepayDelayInterest)
					.add(borrowRepayAdvanceInterest).add(recoverFee);

			// 提前还款的计算
			if (advanceStatus == 1) {
				receiveAdvanceInterest = borrowRepayAdvanceInterest;
			}
			// 延期的计算
			else if (advanceStatus == 2) {
				// 计算用户延期利息
				receiveDelayInterest = debtDetail.getDelayInterest();
			}
			// 逾期的计算
			else if (advanceStatus == 3) {
				// 计算用户延期利息
				receiveDelayInterest = debtDetail.getDelayInterest();
				// 计算用户逾期利息
				receiveLateInterest = debtDetail.getLateInterest();
			}
			// 出借人实际到账本息
			receiveAccount = debtLoan.getLoanAccount().add(receiveAdvanceInterest).add(receiveDelayInterest)
					.add(receiveLateInterest);
			// 出借人实际到账本金
			receiveCapital = debtLoan.getLoanCapital();
			// 出借人实际到账利息
			receiveInterest = debtLoan.getLoanInterest().add(receiveAdvanceInterest).add(receiveDelayInterest)
					.add(receiveLateInterest);
		}

		// 如果是清算日前三天还款
		boolean isLiquidatesPlan = this.isLiquidatesPlan(debtLoan.getPlanNid());
		// 如果是延期逾期
		if (advanceStatus == 2 || advanceStatus == 3) {
			serviceFee = borrowRepayInterest.subtract(receiveInterest);
		} else if (isLiquidatesPlan) {
			// 汇添金服务费
			serviceFee = this.calculateServiceFee(debtLoan.getPlanOrderId(), receiveAccount);
			if (serviceFee.compareTo(BigDecimal.ZERO) > 0
					&& receiveInterest.subtract(serviceFee).compareTo(BigDecimal.ZERO) < 0) {
				serviceFee = receiveInterest;
			}
			receiveInterest = receiveInterest.subtract(serviceFee);
			receiveAccount = receiveAccount.subtract(serviceFee);
		}
		// 判断该收支明细是否存在时,跳出本次循环
		if (countAccountListByNid(repayOrderId) > 0) {
			return true;
		}
		// 调用交易查询接口
		ChinapnrBean queryTransStatBean = queryTransStat(repayOrderId, repayOrderDate, "REPAYMENT");
		String respCode = queryTransStatBean == null ? "" : queryTransStatBean.getRespCode();
		// 调用接口失败时(000,422以外)
		if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)
				&& !ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(respCode)) {
			String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
			LogUtil.errorLog(THIS_CLASS, methodName, "调用交易查询接口失败。" + message + "，[出借订单号：" + tenderOrdId + "]", null);
			throw new RuntimeException("调用交易查询接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
		}
		// 汇付交易状态
		String transStat = queryTransStatBean.getTransStat();
		// I:初始 P:部分成功
		if (ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(respCode)
				|| (!"I".equals(transStat) && !"P".equals(transStat))) {

			// 分账账户串（当 管理费！=0 时是必填项）
			String divDetails = "";
			if (recoverFee.compareTo(BigDecimal.ZERO) > 0) {
				JSONArray ja = new JSONArray();
				JSONObject jo = new JSONObject();
				// 分账商户号(商户号,从配置文件中取得)
				jo.put(ChinaPnrConstant.PARAM_DIVCUSTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
				// 分账账户号(子账户号,从配置文件中取得)
				jo.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT03));
				// 分账金额
				jo.put(ChinaPnrConstant.PARAM_DIVAMT, recoverFee.toString());
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
						recoverCapital.toString(), receiveInterest.toString(), recoverFee, serviceFee, repayOrderId,
						repayOrderDate, tenderOrdId, borrowTender.getOrderDate(), String.valueOf(tenderUserCustId),
						divDetails, reqExts, bankInputFlag);
				respCode = repaymentBean == null ? "" : repaymentBean.getRespCode();
				// 调用接口失败时(000以外)
				if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)
						&& !ChinaPnrConstant.RESPCODE_REPEAT_REPAY.equals(respCode)) {
					String message = repaymentBean == null ? "" : repaymentBean.getRespDesc();
					LogUtil.errorLog(THIS_CLASS, methodName,
							"调用自动扣款（还款）接口失败。" + message + "，[出借订单号：" + tenderOrdId + "]", null);
					throw new RuntimeException(
							"调用自动扣款（还款）接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
				}
			}
		}
		// 判断该收支明细是否存在时,跳出本次循环
		if (countAccountListByNid(repayOrderId) > 0) {
			return true;
		}
		// 判断是否是清算日前三天还款
		boolean isLiquidatesPlanFlag = this.isLiquidatesPlan(debtLoan.getPlanNid());
		// 冻结成功账户更新账户信息(出借人)
		Account newAccount = new Account();
		newAccount.setUserId(tenderUserId);
		// 出借人可用余额
		newAccount.setPlanBalance(receiveAccount);
		// 非清算时
		if (!isLiquidatesPlanFlag) {
			newAccount.setPlanAccedeBalance(receiveAccount);
		}
		boolean investAccountFlag = this.adminAccountCustomizeMapper.updateOfPlanBalance(newAccount) > 0 ? true : false;
		if (!investAccountFlag) {
			throw new RuntimeException("账户表(huiyingdai_account)更新失败!" + "[出借订单号:" + tenderOrdId + "]");
		}
		DebtPlanAccede debtPlanAccede = this.getDebtPlanAccedeInfo(debtLoan.getPlanOrderId());
		if (Validator.isNull(debtPlanAccede)) {
			throw new RuntimeException("用户计划加入表(debtplanaccede)查询失败!" + "[加入订单号:" + debtLoan.getPlanOrderId() + "]");
		}
		// 更新DebtDetail表
		DebtDetail detail = this.getDebtDetail(tenderOrdId, periodNow);
		boolean debtPlanAccedeUpdateFlag = false;
		DebtPlanAccede upateDebtPlanAccede = new DebtPlanAccede();
		upateDebtPlanAccede.setExpireFairValue(detail.getExpireFairValue());
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
			throw new RuntimeException("用户计划加入表(debtplanaccede)更新失败!" + "[加入订单号:" + debtLoan.getPlanOrderId() + "]");
		}

		debtPlanAccede = selectDebtPlanAccede(upateDebtPlanAccede.getId());
		// 更新账户信息(出借人)
		Account account = this.selectAccountByUserId(tenderUserId);
		DebtPlanAccede newAccede = this.getDebtPlanAccedeInfo(debtLoan.getPlanOrderId());
		// 写入收支明细
		DebtAccountList accountRepayList = new DebtAccountList();
		accountRepayList.setNid(repayOrderId); // 还款订单号
		accountRepayList.setUserId(tenderUserId); // 出借人
		accountRepayList.setUserName(borrowTender.getUserName());// 出借人用户名
		accountRepayList.setPlanNid(debtLoan.getPlanNid()); // 计划编号
		accountRepayList.setPlanOrderId(debtLoan.getPlanOrderId());// 计划订单号
		accountRepayList.setPlanBalance(account.getPlanBalance());// 计划余额
		accountRepayList.setPlanFrost(account.getPlanFrost());
		accountRepayList.setPlanOrderBalance(newAccede.getAccedeBalance());// 计划订单余额
		accountRepayList.setPlanOrderFrost(newAccede.getAccedeFrost());// 计划订单冻结
		UsersInfo userInfo = getUsersInfoByUserId(tenderUserId);
		// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
		Integer attribute = null;
		if (Validator.isNotNull(userInfo)) {
			// 获取出借用户的用户属性
			attribute = userInfo.getAttribute();
			if (Validator.isNotNull(attribute)) {
				if (attribute == 1) {
					SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
					SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
					spreadsUsersExampleCriteria.andUserIdEqualTo(tenderUserId);
					List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
					if (sList != null && sList.size() == 1) {
						int refUserId = sList.get(0).getSpreadsUserid();
						// 查找用户推荐人
						Users refererUser = getUsersByUserId(refUserId);
						if (Validator.isNotNull(refererUser)) {
							accountRepayList.setRefererUserId(refererUser.getUserId());
							accountRepayList.setRefererUserName(refererUser.getUsername());
						}
					}
				} else if (attribute == 0) {
					SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
					SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
					spreadsUsersExampleCriteria.andUserIdEqualTo(tenderUserId);
					List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
					if (sList != null && sList.size() == 1) {
						int refUserId = sList.get(0).getSpreadsUserid();
						// 查找推荐人
						Users refererUser = getUsersByUserId(refUserId);
						if (Validator.isNotNull(refererUser)) {
							accountRepayList.setRefererUserId(refererUser.getUserId());
							accountRepayList.setRefererUserName(refererUser.getUsername());
						}
					}
				}
			}
		}
		accountRepayList.setAmount(receiveAccount); // 出借总收入
		accountRepayList.setType(1); // 1收入
		accountRepayList.setTrade("plan_borrow_repay"); // 出借成功
		accountRepayList.setTradeCode("balance"); // 余额操作
		accountRepayList.setTotal(account.getTotal()); // 出借人资金总额
		accountRepayList.setBalance(account.getBalance()); // 出借人可用金额
		accountRepayList.setFrost(account.getFrost()); // 出借人冻结金额
		accountRepayList.setAccountWait(BigDecimal.ZERO); // 出借人待收金额
		accountRepayList.setCapitalWait(BigDecimal.ZERO);// 待收本金
		accountRepayList.setInterestWait(BigDecimal.ZERO);// 待还收益
		accountRepayList.setRepayWait(BigDecimal.ZERO);// 待还金额
		accountRepayList.setCreateTime(nowTime); // 创建时间
		accountRepayList.setRemark(debtLoan.getPlanOrderId()); // 计划编号
		accountRepayList.setIp(borrow.getAddip()); // 操作IP
		accountRepayList.setWeb(0); // PC
		accountRepayList.setCreateTime(nowTime);
		accountRepayList.setCreateUserId(tenderUserId);
		accountRepayList.setCreateUserName(borrowTender.getUserName());
		// 插入一条交易明细
		boolean accountRepayListFlag = insertDebtAccountList(accountRepayList) > 0 ? true : false;
		if (!accountRepayListFlag) {
			throw new RuntimeException(
					"账户交易明细表(huiyingdai_account_list)插入失败!" + "[加入订单号:" + debtLoan.getPlanOrderId() + "]");
		}
		// 分期
		if (debtLoanDetail != null) {
			// 不是最后一期
			if (Validator.isNotNull(periodNext) && periodNext > 0) {
				debtLoan.setRepayStatus(0); // 未还款
				// 取得分期还款计划表下一期的还款
				DebtLoanDetail borrowRecoverPlanNext = getBorrowRecoverPlan(borrowNid, periodNow + 1, tenderUserId,
						debtLoan.getInvestId());
				debtLoan.setRepayTime(borrowRecoverPlanNext.getRepayTime()); // 计算下期时间
			} else {
				debtLoan.setRepayStatus(1); // 已还款
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
		debtLoan.setRepayAccountYes(debtLoan.getRepayAccountYes().add(borrowRepayAccount));
		debtLoan.setRepayInterestYes(debtLoan.getRepayInterestYes().add(borrowRepayInterest));
		debtLoan.setRepayCapitalYes(debtLoan.getRepayCapitalYes().add(borrowRepayCapital));
		debtLoan.setRepayAccountWait(debtLoan.getRepayAccountWait().subtract(recoverAccount));
		debtLoan.setRepayInterestWait(debtLoan.getRepayInterestWait().subtract(recoverInterest));
		debtLoan.setRepayCapitalWait(debtLoan.getRepayCapitalWait().subtract(recoverCapital));
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
		debtLoan.setReceiveCapitalYes(debtLoan.getReceiveCapitalYes().add(receiveCapital));
		// 出借人收取已还利息
		debtLoan.setReceiveInterestYes(debtLoan.getReceiveInterestYes().add(receiveInterest));
		debtLoan.setWeb(2); // 写入网站收支
		boolean borrowRecoverFlag = this.debtLoanMapper.updateByPrimaryKeySelective(debtLoan) > 0 ? true : false;
		if (!borrowRecoverFlag) {
			throw new RuntimeException("还款明细(hyjf_debt_loan)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
		}
		borrowRepay.setRepayAccountAll(borrowRepay.getRepayAccountAll().add(borrowRepayAccountAll));
		borrowRepay.setRepayAccountYes(borrowRepay.getRepayAccountYes().add(borrowRepayAccount));
		borrowRepay.setRepayInterestYes(borrowRepay.getRepayInterestYes().add(borrowRepayInterest));
		borrowRepay.setRepayCapitalYes(borrowRepay.getRepayCapitalYes().add(borrowRepayCapital));
		borrowRepay.setLiquidatesServiceFee(borrowRepay.getLiquidatesServiceFee().add(serviceFee));// 清算时已收服务费
		borrowRepay.setAdvanceStatus(debtLoan.getAdvanceStatus());// 用户是否提前还款
		borrowRepay.setAdvanceDays(advanceDays);
		borrowRepay.setAdvanceInterest(borrowRepay.getAdvanceInterest().add(borrowRepayAdvanceInterest));
		borrowRepay.setDelayDays(delayDays);
		borrowRepay.setDelayInterest(borrowRepay.getDelayInterest().add(borrowRepayDelayInterest));
		borrowRepay.setLateDays(lateDays);
		borrowRepay.setLateInterest(borrowRepay.getLateInterest().add(borrowRepayLateInterest));
		boolean borrowRepayFlag = this.debtRepayMapper.updateByPrimaryKeySelective(borrowRepay) > 0 ? true : false;
		if (!borrowRepayFlag) {
			throw new RuntimeException("总的还款明细表(hyjf_debt_repay)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
		}
		// 更新借款表
		BigDecimal borrowManager = borrow.getBorrowManager() == null ? BigDecimal.ZERO
				: new BigDecimal(borrow.getBorrowManager());
		borrow.setBorrowManager(borrowManager.add(recoverFee).toString());
		borrow.setRepayAccountYes(borrow.getRepayAccountYes().add(borrowRepayAccount)); // 总还款利息
		borrow.setRepayAccountInterestYes(borrow.getRepayAccountInterestYes().add(borrowRepayInterest)); // 总还款利息
		borrow.setRepayAccountCapitalYes(borrow.getRepayAccountCapitalYes().add(borrowRepayCapital)); // 总还款本金
		borrow.setRepayAccountWait(borrow.getRepayAccountWait().subtract(recoverAccount)); // 未还款总额
		borrow.setRepayAccountInterestWait(borrow.getRepayAccountInterestWait().subtract(recoverInterest)); // 未还款利息
		borrow.setRepayAccountCapitalWait(borrow.getRepayAccountCapitalWait().subtract(recoverCapital)); // 未还款本金
		borrow.setRepayFeeNormal(borrow.getRepayFeeNormal().add(recoverFee));
		boolean borrowFlag = this.debtBorrowMapper.updateByPrimaryKeySelective(borrow) > 0 ? true : false;
		if (!borrowFlag) {
			throw new RuntimeException("借款详情(hyjf_debt_borrow)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
		}
		// 更新出借表
		borrowTender.setRepayAccountYes(borrowTender.getRepayAccountYes().add(borrowRepayAccount));
		borrowTender.setRepayInterestYes(borrowTender.getRepayInterestYes().add(borrowRepayInterest));
		borrowTender.setRepayCapitalYes(borrowTender.getRepayCapitalYes().add(borrowRepayCapital));
		borrowTender.setRepayAccountWait(borrowTender.getRepayAccountWait().subtract(recoverAccount));
		borrowTender.setRepayInterestWait(borrowTender.getRepayInterestWait().subtract(recoverInterest));
		borrowTender.setRepayCapitalWait(borrowTender.getRepayCapitalWait().subtract(recoverCapital));
		borrowTender.setUpdateTime(nowTime);
		boolean borrowTenderFlag = debtInvestMapper.updateByPrimaryKeySelective(borrowTender) > 0 ? true : false;
		if (!borrowTenderFlag) {
			throw new RuntimeException("出借表(hyjf_debt_invest)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
		}
		// 分期时
		if (isMonth) {
			// 更新还款计划表
			debtLoanDetail.setRepayStatus(1);
			debtLoanDetail.setRepayActionTime(String.valueOf(nowTime));
			debtLoanDetail.setRepayAccountYes(borrowRepayAccount);
			debtLoanDetail.setRepayInterestYes(borrowRepayInterest);
			debtLoanDetail.setRepayCapitalYes(borrowRepayCapital);
			debtLoanDetail.setRepayAccountWait(BigDecimal.ZERO);
			debtLoanDetail.setRepayCapitalWait(BigDecimal.ZERO);
			debtLoanDetail.setRepayInterestWait(BigDecimal.ZERO);
			// 借款人已还提前还款利息
			debtLoanDetail.setRepayAdvanceInterestYes(
					debtLoanDetail.getRepayAdvanceInterestYes().add(borrowRepayAdvanceInterest));
			// 借款人已还延期还款利息
			debtLoanDetail
					.setRepayDelayInterestYes(debtLoanDetail.getRepayDelayInterestYes().add(borrowRepayDelayInterest));
			// 借款人已还逾期还款利息
			debtLoanDetail
					.setRepayLateInterestYes(debtLoanDetail.getRepayLateInterestYes().add(borrowRepayLateInterest));
			// 出借人收取已还提前还款利息
			debtLoanDetail.setReceiveAdvanceInterestYes(
					debtLoanDetail.getReceiveAdvanceInterestYes().add(receiveAdvanceInterest));
			// 出借人收取已还延期还款利息
			debtLoanDetail
					.setReceiveDelayInterestYes(debtLoanDetail.getReceiveDelayInterestYes().add(receiveDelayInterest));
			// 出借人收取已还逾期还款利息
			debtLoanDetail
					.setReceiveLateInterestYes(debtLoanDetail.getReceiveLateInterestYes().add(receiveLateInterest));
			// 出借人收取已还还款利息
			debtLoanDetail.setReceiveAccountYes(debtLoanDetail.getReceiveAccountYes().add(receiveAccount));
			// 出借人收取已还本金
			debtLoanDetail.setReceiveCapitalYes(debtLoanDetail.getReceiveCapitalYes().add(receiveCapital));
			// 出借人收取已还利息
			debtLoanDetail.setReceiveInterestYes(debtLoanDetail.getReceiveInterestYes().add(receiveInterest));
			boolean borrowRecoverPlanFlag = this.debtLoanDetailMapper.updateByPrimaryKeySelective(debtLoanDetail) > 0
					? true : false;
			if (!borrowRecoverPlanFlag) {
				throw new RuntimeException("还款分期计划表(hyjf_debt_loan_detail)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
			}
			// 更新总的还款计划
			DebtRepayDetail borrowRepayPlan = getBorrowRepayPlan(borrowNid, periodNow);
			if (borrowRepayPlan == null) {
				throw new RuntimeException("还款分期计划表(hyjf_debt_repay_detail)查询失败！" + "[出借订单号：" + tenderOrdId + "]");
			}
			borrowRepayPlan.setRepayActionTime(String.valueOf(nowTime));
			borrowRepayPlan.setRepayStatus(1);
			borrowRepayPlan.setRepayActionTime(String.valueOf(nowTime));
			borrowRepayPlan.setRepayAccountAll(borrowRepayPlan.getRepayAccountAll().add(borrowRepayAccountAll));
			borrowRepayPlan.setRepayAccountYes(borrowRepayPlan.getRepayAccountYes().add(borrowRepayAccount));
			borrowRepayPlan.setRepayInterestYes(borrowRepayPlan.getRepayInterestYes().add(borrowRepayInterest));
			borrowRepayPlan.setRepayCapitalYes(borrowRepay.getRepayCapitalYes().add(borrowRepayCapital));
			// 清算时已收服务费
			borrowRepayPlan.setLiquidatesServiceFee(borrowRepayPlan.getLiquidatesServiceFee().add(serviceFee));
			borrowRepayPlan.setLateDays(lateDays);
			borrowRepayPlan.setLateInterest(borrowRepayPlan.getLateInterest().add(borrowRepayLateInterest));
			borrowRepayPlan.setDelayDays(delayDays);
			borrowRepayPlan.setDelayInterest(borrowRepayPlan.getDelayInterest().add(borrowRepayDelayInterest));
			borrowRepayPlan.setAdvanceDays(advanceDays);
			borrowRepayPlan.setAdvanceInterest(borrowRepayPlan.getAdvanceInterest().add(borrowRepayAdvanceInterest));
			// 用户是否提前还款
			borrowRepayPlan.setAdvanceStatus(debtLoanDetail.getAdvanceStatus());
			boolean borrowRepayPlanFlag = this.debtRepayDetailMapper.updateByPrimaryKeySelective(borrowRepayPlan) > 0
					? true : false;
			if (!borrowRepayPlanFlag) {
				throw new RuntimeException("还款分期计划表(hyjf_debt_repay_detail)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
			}
		}
		// 更新DebtDetail表
		debtDetail.setRepayActionTime(nowTime);
		// 已还款
		debtDetail.setRepayStatus(1);
		// 账户管理费
		debtDetail.setManageFee(recoverFee);
		// 已还本金
		debtDetail.setRepayCapitalYes(receiveCapital);
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
			throw new RuntimeException("还款分期计划表(hyjf_debt_detail)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
		}
		DebtPlan debtPlanNew = new DebtPlan();
		debtPlanNew.setDebtPlanNid(debtLoan.getPlanNid());
		if (!isLiquidatesPlan) {
			// 非清算还款
			debtPlanNew.setDebtPlanBalance(receiveAccount);
		} else {
			// 清算还款
			if (serviceFee.compareTo(BigDecimal.ZERO) > 0) {
				debtPlanNew.setServiceFee(serviceFee);
			}
			debtPlanNew.setLiquidateArrivalAmount(receiveAccount);
		}
		// 更新计划总表
		boolean planUpdateFlg = this.batchDebtPlanCustomizeMapper.updatePlanRepay(debtPlanNew) > 0 ? true : false;
		if (!planUpdateFlg) {
			throw new RuntimeException("债权详情表(hyjf_debt_plan)更新失败!" + "[出借订单号:" + tenderOrdId + "]");
		}
		// 管理费大于0时,插入网站收支明细
		if (recoverFee.compareTo(BigDecimal.ZERO) > 0) {
			// 插入网站收支明细记录
			AccountWebList accountWebManageList = new AccountWebList();
			// 订单号
			accountWebManageList.setOrdid(borrowTender.getOrderId() + "_" + periodNow);// 订单号
			accountWebManageList.setBorrowNid(borrowNid); // 出借编号
			accountWebManageList.setUserId(borrowUserid); // 借款人
			accountWebManageList.setAmount(recoverFee); // 管理费
			accountWebManageList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
			accountWebManageList.setTrade(CustomConstants.TRADE_REPAYFEE); // 管理费
			accountWebManageList.setTradeType(CustomConstants.TRADE_REPAYFEE_NM); // 账户管理费
			accountWebManageList.setRemark(borrowNid); // 出借编号
			accountWebManageList.setCreateTime(nowTime);
			boolean accountWebListManageFlag = insertAccountWebList(accountWebManageList) > 0 ? true : false;
			if (!accountWebListManageFlag) {
				throw new RuntimeException(
						"网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号：" + borrowTender.getOrderId() + "]");
			}
		}
		// 服务费大于0时,插入网站收支明细
		if (serviceFee.compareTo(BigDecimal.ZERO) > 0) {
			// 插入网站收支明细记录
			AccountWebList accountWebList = new AccountWebList();
			// 订单号
			accountWebList.setOrdid(borrowTender.getOrderId() + "_" + borrowTender.getBorrowNid() + "_" + periodNow);// 订单号
			accountWebList.setBorrowNid(borrowNid); // 出借编号
			accountWebList.setUserId(borrowUserid); // 借款人
			accountWebList.setAmount(serviceFee); // 管理费
			accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
			accountWebList.setTrade(CustomConstants.HTJ_TRADE_LOANFEE); // 汇添金服务费
			accountWebList.setTradeType(CustomConstants.HTJ_TRADE_LOANFEE_NM); // 汇添金服务费-还款
			accountWebList.setRemark(borrowNid); // 出借编号
			accountWebList.setCreateTime(nowTime);
			boolean accountWebListFlag = insertAccountWebList(accountWebList) > 0 ? true : false;
			if (!accountWebListFlag) {
				throw new RuntimeException(
						"网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号：" + borrowTender.getOrderId() + "]");
			}
		}
		System.out.println("-----------还款结束---" + apicron.getBorrowNid() + "---------" + repayOrderId);
		return true;
	}

	/**
	 * 更新还款完成状态
	 *
	 * @param borrowNid
	 * @param periodNow
	 */
	@Override
	public void updateBorrowStatus(String borrowNid, Integer periodNow, Integer borrowUserId) {

		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 标的项目详情
		DebtBorrow borrow = getBorrow(borrowNid);
		// 还款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle())
				|| CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle())
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle());
		// 查询未债转的数据
		DebtLoanExample recoverExample = new DebtLoanExample();
		recoverExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayStatusEqualTo(0)
				.andCreditStatusEqualTo(0);
		int recoverCnt = this.debtLoanMapper.countByExample(recoverExample);
		// 查询是否有债转数据
		recoverExample = new DebtLoanExample();
		recoverExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayStatusEqualTo(0)
				.andCreditStatusEqualTo(1);
		int recoverCnt2 = this.debtLoanMapper.countByExample(recoverExample);
		// 项目总表更新数据
		DebtBorrowWithBLOBs newBrrow = new DebtBorrowWithBLOBs();
		newBrrow.setRepayTimes(borrow.getRepayTimes() + 1); // 还款次数
		// 如果有债转数据未还款,不更新为已还款
		int repayStatus = 0;
		if (recoverCnt2 == 0 && recoverCnt == 0) {
			repayStatus = 1;
		}
		// 借款人还款表更新
		DebtRepay newBorrowRepay = new DebtRepay();
		newBorrowRepay.setRepayActionTime(String.valueOf(nowTime));
		// 如果沒有未还款数据，
		if (recoverCnt2 == 0 && recoverCnt == 0) {
			newBorrowRepay.setRepayStatus(repayStatus); // 已还款
			newBorrowRepay.setRepayActionTime(String.valueOf(GetDate.getNowTime10())); // 实际还款时间
			newBorrowRepay.setRepayPeriod(borrowPeriod);
			newBorrowRepay.setAlreadyRepayPeriod(borrowPeriod);// 已经还款期数
			newBorrowRepay.setRemainPeriod(0);// 剩余期数
			newBrrow.setRepayFullStatus(repayStatus);
		} else {
			if (isMonth) {
				// 分期的场合，根据借款编号和还款期数从还款计划表中取得还款时间
				DebtRepayDetailExample example = new DebtRepayDetailExample();
				Criteria borrowCriteria = example.createCriteria();
				borrowCriteria.andBorrowNidEqualTo(borrowNid);
				borrowCriteria.andRepayPeriodEqualTo(periodNow + 1);
				List<DebtRepayDetail> replayPlan = debtRepayDetailMapper.selectByExample(example);
				if (replayPlan.size() > 0) {
					DebtRepayDetail borrowRepayPlan = replayPlan.get(0);
					// 设置下期还款时间
					newBorrowRepay.setRepayTime(borrowRepayPlan.getRepayTime());
					newBrrow.setRepayNextTime(Integer.valueOf(newBorrowRepay.getRepayTime())); // 下期还款时间
				}
			}
		}
		// 更新BorrowRepay
		DebtRepayExample repayExample = new DebtRepayExample();
		repayExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayStatusEqualTo(0);
		this.debtRepayMapper.updateByExampleSelective(newBorrowRepay, repayExample);
		// 更新Borrow
		DebtBorrowExample borrowExample = new DebtBorrowExample();
		borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
		this.debtBorrowMapper.updateByExampleSelective(newBrrow, borrowExample);
	}

	/**
	 * 自动扣款（还款）(调用汇付天下接口)
	 *
	 * @return
	 */
	private ChinapnrBean repayment(String borrowNid, Integer borrowUserId, String outCustId, String recoverCapital,
			String recoverInterestAll, BigDecimal fee, BigDecimal serviceFee, String ordId, String ordDate,
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
		BigDecimal totalFee = fee.add(serviceFee);
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
	public List<DebtApicron> getBorrowApicronList(Integer status, Integer apiType) {

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
	@Override
	public int updateBorrowApicron(Integer id, Integer status) {
		return updateBorrowApicron(id, status, null);
	}

	/**
	 * 更新借款API任务表
	 *
	 * @return
	 */
	@Override
	public int updateBorrowApicron(Integer id, Integer status, String data) {
		DebtApicron record = new DebtApicron();
		record.setId(id);
		record.setRepayStatus(status);
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
		criteria.andCreditTimeEqualTo(0); // 排除债转
		criteria.andCreditStatusEqualTo(0);
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
	 * 取得还款计划列表
	 *
	 * @return
	 */
	@Override
	public DebtLoanDetail getBorrowRecoverPlan(String borrowNid, Integer period, Integer userId, Integer tenderId) {

		DebtLoanDetailExample example = new DebtLoanDetailExample();
		DebtLoanDetailExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andRepayStatusEqualTo(0);
		criteria.andRepayPeriodEqualTo(period);
		criteria.andInvestIdEqualTo(tenderId);
		criteria.andUserIdEqualTo(userId);
		example.setOrderByClause(" id asc ");
		List<DebtLoanDetail> list = this.debtLoanDetailMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
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
	private int countAccountListByNid(String nid) {
		DebtAccountListExample accountListExample = new DebtAccountListExample();
		accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("tender_recover_yes");
		return this.debtAccountListMapper.countByExample(accountListExample);
	}

	/**
	 * 写入收支明细
	 *
	 * @param accountList
	 * @return
	 */
	private int insertDebtAccountList(DebtAccountList accountList) {
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
	 * 更新还款信息
	 *
	 * @param record
	 * @return
	 */
	public int updateBorrowRecoverPlan(DebtLoanDetail recoder) {
		int cnt = this.debtLoanDetailMapper.updateByPrimaryKeySelective(recoder);
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
	 * 发送push消息(优惠券还款成功)
	 *
	 * @param userId
	 */
	public void sendPushMsgCoupon(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && NumberUtils.isNumber(msg.get(VAL_USERID))
						&& Validator.isNotNull(msg.get(VAL_AMOUNT))) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null) {
						return;
					}
					AppMsMessage appMsMessage = new AppMsMessage(users.getUserId(), msg, null,
							MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_COUPON_PROFIT);
					appMsProcesser.gather(appMsMessage);
				}
			}
		}
	}

	/**
	 * 取得该标的下的优惠券出借列表
	 */
	@Override
	public List<CouponTenderCustomize> getCouponTenderList(String borrowNid) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("borrowNid", borrowNid);
		return this.couponRecoverCustomizeMapper.selectCouponRecoverAll(paramMap);
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

	private Account selectAccountByUserId(Integer userId) {
		AccountExample example = new AccountExample();
		AccountExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		List<Account> list = this.accountMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
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

		for (int i = 0; i < 3; i++) {
			try {
				/** 冻结订单 */
				ChinapnrBean freezeBean = usrFreeze(userId, tenderUsrcustid, account, orderId, orderDate);
				if (Validator.isNotNull(freezeBean)) {
					String respCode = freezeBean.getRespCode();
					if (StringUtils.isNotBlank(respCode) && respCode.equals(ChinaPnrConstant.RESPCODE_SUCCESS)) {
						System.out.println(
								"用户:" + userId + "冻结订单*******************************冻结标识：" + freezeBean.getTrxId());
						return freezeBean.getTrxId();
					} else {
						// 调用交易查询接口(解冻)
						ChinapnrBean queryTransStatBean = queryTransStat(orderId, orderDate, "FREEZE");
						if (Validator.isNotNull(queryTransStatBean)) {
							String queryRespCode = queryTransStatBean.getRespCode();
							System.out.println("调用交易查询接口(冻结)返回码：" + queryRespCode);
							// 调用接口失败时(000以外)
							if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(queryRespCode)) {
								String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
								LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder",
										"调用交易查询接口(冻结)失败。" + message + ",[冻结订单号：" + orderId + "]", null);
								throw new Exception(
										"调用交易查询接口(冻结)失败。" + queryRespCode + "：" + message + ",[冻结订单号：" + orderId + "]");
							} else {
								// 汇付交易状态
								String transStat = queryTransStatBean == null ? "" : queryTransStatBean.getTransStat();
								// 冻结请求已经被解冻 U:已解冻 N:无需解冻，对于解冻交易F:冻结
								if (!"F".equals(transStat)) {
									System.out.println("用户:" + userId + "***********************************冻结失败错误码："
											+ respCode + ",*****冻结订单号:" + orderId);
									Thread.sleep(500);
									continue;
								} else {
									return queryTransStatBean.getTrxId();
								}
							}
						} else {
							Thread.sleep(500);
							continue;
						}
					}
				} else {
					Thread.sleep(500);
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
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

	private ChinapnrBean usrFreeze(int userId, String tenderUsrcustid, BigDecimal account, String orderId,
			String orderDate) {

		ChinapnrBean chinapnrBean = new ChinapnrBean();
		chinapnrBean.setVersion("10");// 接口版本号
		chinapnrBean.setCmdId("UsrFreezeBg");// 消息类型(冻结)
		chinapnrBean.setUsrCustId(tenderUsrcustid);// 出借用户客户号
		chinapnrBean.setOrdId(orderId);// 订单号(必须)
		chinapnrBean.setOrdDate(orderDate); // 订单时间(必须)格式为yyyyMMdd，例如：20130307
		chinapnrBean.setTransAmt(CustomUtil.formatAmount(account.toString()));// 交易金额(必须)
		chinapnrBean.setRetUrl("");// 商户后台应答地址(必须)
		chinapnrBean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
		chinapnrBean.setType("user_freeze");// 日志类型
		chinapnrBean.setLogUserId(userId);
		ChinapnrBean bean = ChinapnrUtil.callApiBg(chinapnrBean);
		// 处理冻结返回信息
		if (Validator.isNotNull(bean)) {
			return bean;
		} else {
			System.out.println("用户:" + userId + "冻结相应的账户余额未收到返回，");
			return null;
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
	 * 计算计划是否是清算前三天还款
	 * 
	 * @Title isLiquidatesPlan
	 * @param loan
	 * @return
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
	public boolean unFreezeOrder(int userId, String orderId, String trxId, String ordDate, String unfreezeOrderId,
			String unfreezeOrderDate) throws Exception {

		// 出借人的账户信息
		AccountChinapnr outCust = this.getAccountChinapnr(userId);
		if (outCust == null) {
			throw new Exception("出借人未开户。[出借人ID：" + userId + "]，" + "[出借订单号：" + orderId + "]");
		}
		for (int i = 0; i < 3; i++) {
			try {
				// 调用交易查询接口(解冻)
				ChinapnrBean queryTransStatBean = queryTransStat(orderId, ordDate, "FREEZE");
				if (Validator.isNotNull(queryTransStatBean)) {
					String queryRespCode = queryTransStatBean.getRespCode();
					System.out.println("解冻接口查询接口返回码：" + queryRespCode);
					// 调用接口失败时(000以外)
					if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(queryRespCode)) {
						String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
						LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder",
								"调用交易查询接口(解冻)失败。" + message + ",[出借订单号：" + orderId + "]", null);
						throw new Exception(
								"调用交易查询接口(解冻)失败。" + queryRespCode + "：" + message + ",[出借订单号：" + orderId + "]");
					} else {
						// 汇付交易状态
						String transStat = queryTransStatBean == null ? "" : queryTransStatBean.getTransStat();
						// 冻结请求已经被解冻 U:已解冻 N:无需解冻，对于解冻交易
						if (!"U".equals(transStat) && !"N".equals(transStat)) {
							/** 解冻订单 */
							ChinapnrBean unFreezeBean = usrUnFreeze(trxId, unfreezeOrderId, unfreezeOrderDate);
							if (Validator.isNotNull(unFreezeBean)) {
								String respCode = unFreezeBean.getRespCode();
								System.out.println("自动解冻接口返回码：" + respCode);
								// 调用接口失败时(000 或 107 以外)
								if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)
										&& !ChinaPnrConstant.RESPCODE_REPEAT_DEAL.equals(respCode)) {
									String message = unFreezeBean == null ? "" : unFreezeBean.getRespDesc();
									message = "调用解冻接口失败。" + respCode + "：" + message + "，出借订单号[" + orderId + "]";
									LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder", message, null);
									throw new Exception(
											"调用解冻接口失败。" + queryRespCode + "：" + message + ",[冻结订单号：" + orderId + "]");
								} else {
									return true;
								}
							} else {
								Thread.sleep(500);
								continue;
							}
						} else {
							return true;
						}
					}
				} else {
					Thread.sleep(500);
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
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
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param borrowRecover
	 * @param borrowUserCust
	 * @return
	 * @author Administrator
	 */

	@Override
	public boolean repayBackUpdate(Integer userId, String userName, String planNid, String planOrderId)
			throws Exception {

		boolean isLiquidatesPlan = this.isLiquidatesPlan(planNid);
		DebtPlanAccede debtPlanAccede = this.getDebtPlanAccedeInfo(planOrderId);
		// 清算三天内
		if (!isLiquidatesPlan) {
			try {
				// 如果不是其他项目还款中
				if (debtPlanAccede.getRepayRunningStatus() == 0) {
					// 更新相应的加入记录为还款中
					boolean debtPlanAccedeBeforeFlag = this.updateDebtPlanAccedeRepayStatus(debtPlanAccede, 1);
					if (debtPlanAccedeBeforeFlag) {
						// 承接人计划订单信息更新
						boolean accedeFlag = this.updateRepayDebtPlanAccede(debtPlanAccede);
						if (accedeFlag) {
							return true;
						} else {
							// 更新相应的加入记录为还款中
							boolean debtPlanAccedeAfterFlag = this.updateDebtPlanAccedeRepayStatus(debtPlanAccede, 0);
							if (debtPlanAccedeAfterFlag) {
								throw new Exception("还款后复投后续操作失败，加入订单号：" + planOrderId);
							} else {
								throw new Exception("还款后复投后续操作失败，加入订单号：" + planOrderId + ",还款完成后还款状态未更新为0");
							}
						}
					} else {
						return true;
					}
				}
				// 如果此时其他项目正在还款
				else {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 修改相应的加入订单的还款状态
	 * 
	 * @param debtPlanAccede
	 * @param status
	 * @return
	 */
	private boolean updateDebtPlanAccedeRepayStatus(DebtPlanAccede debtPlanAccede, int status) {
		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria crt = example.createCriteria();
		if (status == 0) {
			crt.andRepayRunningStatusEqualTo(1);
		} else {
			crt.andRepayRunningStatusEqualTo(0);
		}
		crt.andIdEqualTo(debtPlanAccede.getId());
		debtPlanAccede.setRepayRunningStatus(status);
		boolean debtPlanFlag = this.debtPlanAccedeMapper.updateByExampleSelective(debtPlanAccede, example) > 0 ? true
				: false;
		if (debtPlanFlag) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据相应的计划加入订单记录id获取计划加入记录
	 * 
	 * @param id
	 * @return
	 * @author Administrator
	 */

	private DebtPlanAccede selectDebtPlanAccede(Integer id) {
		DebtPlanAccede debtPlanAccede = this.debtPlanAccedeMapper.selectByPrimaryKey(id);
		return debtPlanAccede;

	}

	/**
	 * 还款时,若在复投,更新accede表
	 * 
	 * @Title updateDebtPlanAccede
	 * @param debtPlanAccede
	 */
	private boolean updateRepayDebtPlanAccede(DebtPlanAccede debtPlanAccede) throws Exception {
		// 承接人计划订单信息更新，余额扣减
		DebtPlanAccede sellerDebtPlanAccedeNew = new DebtPlanAccede();
		if (debtPlanAccede.getStatus() == 1) {
			sellerDebtPlanAccedeNew.setStatus(0);
			sellerDebtPlanAccedeNew.setReinvestStatus(1);
		} else {
			if (debtPlanAccede.getReinvestStatus() == 0) {
				sellerDebtPlanAccedeNew.setStatus(0);
				sellerDebtPlanAccedeNew.setReinvestStatus(0);
			} else {
				sellerDebtPlanAccedeNew.setStatus(0);
				sellerDebtPlanAccedeNew.setReinvestStatus(1);
			}
		}
		sellerDebtPlanAccedeNew.setId(debtPlanAccede.getId());
		boolean sellerdebtPlanAccedeFlag = this.batchDebtPlanAccedeCustomizeMapper
				.updateDebtPlanAccedeRepayFreeze(sellerDebtPlanAccedeNew) > 0 ? true : false;
		if (sellerdebtPlanAccedeFlag) {
			return true;
		} else {
			throw new Exception("还款时,若在复投,更新Accede表失,加入订单号:" + debtPlanAccede.getAccedeOrderId());
		}
	}

}

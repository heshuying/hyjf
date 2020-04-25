package com.hyjf.admin.manager.plan.planlock;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.HTJServiceFeeUtils;
import com.hyjf.common.chinapnr.MerPriv;
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
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.DebtAccountList;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtCredit;
import com.hyjf.mybatis.model.auto.DebtCreditExample;
import com.hyjf.mybatis.model.auto.DebtCreditRepay;
import com.hyjf.mybatis.model.auto.DebtCreditRepayExample;
import com.hyjf.mybatis.model.auto.DebtCreditTender;
import com.hyjf.mybatis.model.auto.DebtCreditTenderExample;
import com.hyjf.mybatis.model.auto.DebtCreditTenderLog;
import com.hyjf.mybatis.model.auto.DebtCreditTenderLogExample;
import com.hyjf.mybatis.model.auto.DebtDetail;
import com.hyjf.mybatis.model.auto.DebtDetailExample;
import com.hyjf.mybatis.model.auto.DebtFreeze;
import com.hyjf.mybatis.model.auto.DebtFreezeExample;
import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtInvestExample;
import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtLoanExample;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanBorrow;
import com.hyjf.mybatis.model.auto.DebtPlanBorrowExample;
import com.hyjf.mybatis.model.auto.DebtRepay;
import com.hyjf.mybatis.model.auto.DebtRepayDetail;
import com.hyjf.mybatis.model.auto.DebtRepayDetailExample;
import com.hyjf.mybatis.model.auto.DebtRepayExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * 计划转让类产品认购Service
 * 
 * @ClassName PlanCreditTenderServiceImpl
 * @author 刘阳
 * @date 2016年11月9日 下午2:56:02
 */
@Service
public class PlanAssignCreditServiceImpl extends BaseServiceImpl implements PlanAssignCreditService {

	// 分期数据结果
	private final String ASSIGN_RESULT = "assignResult";
	// 承接总金额
	private final String ASSIGN_ACCOUNT = "assignAccount";
	// 承接本金
	private final String ASSIGN_CAPITAL = "assignCapital";
	// 承接利息
	private final String ASSIGN_INTEREST = "assignInterest";
	// 承接支付金额
	private final String ASSIGN_PAY = "assignPay";
	// 承接垫付利息
	private final String ASSIGN_ADVANCEMENT_INTEREST = "assignAdvanceMentInterest";
	// 承接人应收取延期利息
	private final String ASSIGN_REPAY_DELAY_INTEREST = "assignRepayDelayInterest";
	// 承接人应收取逾期利息
	private final String ASSIGN_REPAY_LATE_INTEREST = "assignRepayLateInterest";
	// 分期本金
	private final String ASSIGN_PERIOD_CAPITAL = "assignPeriodCapital";
	// 分期利息
	private final String ASSIGN_PERIOD_INTEREST = "assignPeriodInterest";
	// 分期垫付利息
	private final String ASSIGN_PERIOD_ADVANCEMENT_INTEREST = "assignPeriodAdvanceMentInterest";
	// 分期承接人应收取延期利息
	private final String ASSIGN_PERIOD_REPAY_DELAY_INTEREST = "assignPeriodRepayDelayInterest";
	// 分期承接人应收取延期利息
	private final String ASSIGN_PERIOD_REPAY_LATE_INTEREST = "assignPeriodRepayLateInterest";
	// 承接服务费
	private final String SERVICE_FEE = "serviceFee";

	/**
	 * 承接债权
	 * 
	 * @param planNid
	 * @param planOrderId
	 * @param userId
	 * @param creditNid
	 * @param accedeBalance
	 * @return
	 * @author Administrator
	 * @param minSurplusInvestAccount
	 * @param creditCapitalWait
	 * 
	 */
	public boolean assignCredit(DebtCredit debtCredit, DebtPlanAccede assignDebtPlanAccede, BigDecimal account, BigDecimal expectApr, BigDecimal minSurplusInvestAccount) {

		System.out.println("开始调用债权承接,计划加入订单号：" + assignDebtPlanAccede.getAccedeOrderId() + ",项目编号：" + debtCredit.getBorrowNid() + ",授权服务金额：" + account.toString());
		// 清算出的债权编号
		String creditNid = debtCredit.getCreditNid();
		// 发起转让的的用户的userId
		int sellerUserId = debtCredit.getUserId();
		// 清算债权标号
		String liquidatesPlanNid = debtCredit.getPlanNid();
		// 清算债权加入订单号
		String liquidatesPlanOrderId = debtCredit.getPlanOrderId();
		// 加入用户userId
		int userId = assignDebtPlanAccede.getUserId();
		// 计划加入订单号
		String planOrderId = assignDebtPlanAccede.getAccedeOrderId();
		try {
			// 出让人计划加入记录
			DebtPlanAccede sellerDebtPlanAccede = this.selectDebtPlanAccede(sellerUserId, liquidatesPlanNid, liquidatesPlanOrderId);
			try {
				// 债权承接订单号
				String creditOrderId = GetOrderIdUtils.getOrderId0(userId);
				// 债权承接订单日期
				String creditOrderDate = GetDate.getServerDateTime(1, new Date());
				// 生成承接日志
				Map<String, Object> resultMap = this.saveCreditTenderLog(debtCredit, assignDebtPlanAccede, creditOrderId, creditOrderDate, account);
				if (Validator.isNotNull(resultMap)) {
					BigDecimal serviceFee = (BigDecimal) resultMap.get(this.SERVICE_FEE);
					try {
						// 调用相应的自动债权承接接口
						ChinapnrBean bean = this.creditTender(debtCredit, userId, creditOrderId, creditOrderDate, (BigDecimal) resultMap.get(this.ASSIGN_PAY), (BigDecimal) resultMap.get(this.ASSIGN_CAPITAL), serviceFee);
						if (Validator.isNotNull(bean)) {
							// 主动承接债权接口返回码
							String respCode = bean.getRespCode();
							if (StringUtils.isNotBlank(respCode)) {
								// 汇付返回成功
								if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
									try {
										expectApr = expectApr.divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);
										// 债权承接成功后后续处理
										boolean creditTenderFlag = this.saveCreditTender(sellerDebtPlanAccede, debtCredit, assignDebtPlanAccede, bean, creditOrderId, creditOrderDate, serviceFee, expectApr, resultMap);
										if (creditTenderFlag) {
											System.out.println("调用债权承接接口成功,计划加入订单号：" + assignDebtPlanAccede.getAccedeOrderId() + ",项目编号：" + debtCredit.getBorrowNid() + ",授权服务金额：" + ((BigDecimal) resultMap.get(this.ASSIGN_PAY)).toString());
											return true;
										} else {
											System.out.println("调用债权承接接口成功后，后续处理失败,计划加入订单号：" + assignDebtPlanAccede.getAccedeOrderId() + ",项目编号：" + debtCredit.getBorrowNid() + ",授权服务金额：" + account.toString());
											try {
												assignDebtPlanAccede = this.selectDebtPlanAccede(assignDebtPlanAccede.getId());
												boolean updateDebtCreditTenderLogFlag = this.updateDebtCreditTenderLog(debtCredit, userId, creditOrderId);
												if (!updateDebtCreditTenderLogFlag) {
													throw new Exception("自动债权承接调用汇付接口返回失败后后续处理失败后，更新出借日志为失败失败，用户id：" + userId + ",计划加入订单号：" + planOrderId + ",承接订单号：" + creditOrderId);
												}
											} catch (Exception e1) {
												e1.printStackTrace();
											}
											throw new Exception("债转承接成功后，后续处理失败，承接订单号：" + creditOrderId);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									throw new Exception("自动债权承接调用汇付接口返回信息错误，承接订单号：" + creditOrderId + ",返回码为：" + respCode);
								}
							} else {
								throw new Exception("自动债权承接调用汇付接口返回信息错误，承接订单号：" + creditOrderId + ",返回码为：" + respCode);
							}
						} else {
							throw new Exception("债权承接失败，计划订单号：" + planOrderId + ",债权编号：" + creditNid);
						}
					} catch (Exception e) {
						System.out.println("调用债权承接接口失败后，后续处理冻结用户余额,计划加入订单号：" + assignDebtPlanAccede.getAccedeOrderId() + ",项目编号：" + debtCredit.getBorrowNid() + ",授权服务金额：" + account.toString());
						e.printStackTrace();
						try {
							assignDebtPlanAccede = this.selectDebtPlanAccede(assignDebtPlanAccede.getId());
							boolean updateDebtCreditTenderLogFlag = this.updateDebtCreditTenderLog(debtCredit, userId, creditOrderId);
							if (!updateDebtCreditTenderLogFlag) {
								throw new Exception("自动债权承接调用汇付接口返回失败后后续处理失败后，更新出借日志为失败失败，用户id：" + userId + ",计划加入订单号：" + planOrderId + ",承接订单号：" + creditOrderId);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				} else {
					throw new Exception("保存creditTenderLog表失败，计划订单号：" + planOrderId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查询用户的相应的加入订单
	 * 
	 * @param userId
	 * @param liquidatesPlanNid
	 * @param liquidatesAccedeOrderId
	 * @return
	 */

	private DebtPlanAccede selectDebtPlanAccede(int userId, String liquidatesPlanNid, String liquidatesAccedeOrderId) {

		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		crt.andPlanNidEqualTo(liquidatesPlanNid);
		crt.andAccedeOrderIdEqualTo(liquidatesAccedeOrderId);
		List<DebtPlanAccede> debtPlanAccedeList = this.debtPlanAccedeMapper.selectByExample(example);
		if (debtPlanAccedeList != null && debtPlanAccedeList.size() == 1) {
			return debtPlanAccedeList.get(0);
		}
		return null;

	}

	/**
	 * 根据计划nid，计划加入订单号，查询相应的冻结记录
	 * 
	 * @param planNid
	 * @param planOrderId
	 * @return
	 * @author Administrator
	 */
	@Override
	public DebtFreeze selectDebtFreeze(String planNid, String planOrderId) {

		DebtFreezeExample example = new DebtFreezeExample();
		DebtFreezeExample.Criteria crt = example.createCriteria();
		crt.andPlanNidEqualTo(planNid);
		crt.andPlanOrderIdEqualTo(planOrderId);
		crt.andDelFlagEqualTo(0);
		crt.andFreezeTypeEqualTo(1);
		List<DebtFreeze> debtFreezes = this.debtFreezeMapper.selectByExample(example);
		if (debtFreezes != null && debtFreezes.size() > 0) {
			return debtFreezes.get(0);
		} else {
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
			LogUtil.errorLog(this.getClass().getName(), methodName, new Exception("调用交易状态查询接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}
		return chinapnrBean;
	}

	/**
	 * 保存用户的债转承接log数据
	 * 
	 * @param assignCapital
	 * 
	 * @param liquidatesPlanNid
	 * @param planNid
	 * @param userId
	 * @param creditNid
	 * @param creditOrderDate
	 * @param creditOrderId
	 * @param serviceFee
	 * @param sellOrderId
	 * @param assignCapital
	 * @param client
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveCreditTenderLog(DebtCredit debtCredit, DebtPlanAccede debtPlanAccede, String creditOrderId, String creditOrderDate, BigDecimal account) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		// 清算出的债权编号
		String creditNid = debtCredit.getCreditNid();
		// 清算债权标号
		String liquidatesPlanNid = debtCredit.getPlanNid();
		// 清算债权加入订单号
		String liquidatesPlanOrderId = debtCredit.getPlanOrderId();
		// 债权原有出借订单号
		String sellOrderId = debtCredit.getSellOrderId();
		// 项目原标标号
		String borrowNid = debtCredit.getBorrowNid();
		// 已还多少期
		int repayPeriod = debtCredit.getRepayPeriod();
		// 债转期数
		int creditPeriod = debtCredit.getCreditPeriod();
		// 清算所在期数(本期应还款期数)
		int liquidatesPeriod = debtCredit.getLiquidatesPeriod();
		// 承接垫付利息
		BigDecimal sellerInterestAdvance = debtCredit.getCreditInterestAdvance();
		// 待承接垫付利息
		BigDecimal sellerInterestAdvanceWait = debtCredit.getCreditInterestAdvanceWait();
		// 剩余待承接本金
		BigDecimal sellerCapitalWait = debtCredit.getCreditCapitalWait();
		// 此次债权总本金
		BigDecimal sellerTotalCapital = debtCredit.getLiquidatesCapital();
		// 此次债权本金
		BigDecimal sellerCapital = debtCredit.getCreditCapital();
		// 出让前的待收收益
		BigDecimal sellerInterest = debtCredit.getCreditInterest();
		// 待承接的待收收益
		BigDecimal sellerInterestWait = debtCredit.getCreditInterestWait();
		// 出让前的延期利息
		BigDecimal sellerDelayInterestWait = debtCredit.getCreditDelayInterest().subtract(debtCredit.getCreditDelayInterestAssigned());
		// 出让前的延期利息
		BigDecimal sellerLateInterestWait = debtCredit.getCreditLateInterest().subtract(debtCredit.getCreditLateInterestAssigned());
		// 正常垫付利息
		BigDecimal sellerInterestAdvanceNormalWait = debtCredit.getCreditInterestAdvanceWait().subtract(sellerDelayInterestWait).subtract(sellerLateInterestWait);
		// 持有天数
		BigDecimal sellerHoldDays = Validator.isNotNull(debtCredit.getHoldDays()) ? new BigDecimal(debtCredit.getHoldDays()) : BigDecimal.ZERO;
		// 加入用户userId
		int userId = debtPlanAccede.getUserId();
		// 加入用户userName
		String userName = debtPlanAccede.getUserName();
		// 计划nid
		String planNid = debtPlanAccede.getPlanNid();
		// 计划加入订单号
		String planOrderId = debtPlanAccede.getAccedeOrderId();
		// 加入平台
		int client = debtPlanAccede.getClient();
		// 获取借款数据
		DebtBorrowExample borrowExample = new DebtBorrowExample();
		DebtBorrowExample.Criteria borrowCra = borrowExample.createCriteria();
		borrowCra.andBorrowNidEqualTo(debtCredit.getBorrowNid());
		List<DebtBorrow> borrowList = this.debtBorrowMapper.selectByExample(borrowExample);
		if (borrowList != null && borrowList.size() > 0) {
			DebtBorrow borrow = borrowList.get(0);
			String borrowStyle = borrow.getBorrowStyle();
			// 承接本息
			BigDecimal assignAccount = BigDecimal.ZERO;
			// 承接本金
			BigDecimal assignCapital = BigDecimal.ZERO;
			// 债转利息
			BigDecimal assignInterest = BigDecimal.ZERO;
			// 垫付利息
			BigDecimal assignAdvanceMentInterest = BigDecimal.ZERO;
			// 承接人本期正常垫付利息
			BigDecimal assignAdvanceInterest = BigDecimal.ZERO;
			// 出借人收取延期利息
			BigDecimal assignRepayDelayInterest = BigDecimal.ZERO;
			// 出借人收取逾期利息
			BigDecimal assignRepayLateInterest = BigDecimal.ZERO;
			// 实付金额
			BigDecimal assignPay = BigDecimal.ZERO;
			// 按天
			if (borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
				// 承接人承接本金
				assignCapital = HTJServiceFeeUtils.getAssignCapital(account, sellerCapitalWait, sellerInterestAdvanceWait, sellerCapital, sellerInterestAdvance);
				// 出让人的债权信息
				DebtDetailExample debtDetailOldExample = new DebtDetailExample();
				DebtDetailExample.Criteria debtDetailOldCrt = debtDetailOldExample.createCriteria();
				debtDetailOldCrt.andPlanNidEqualTo(liquidatesPlanNid);
				debtDetailOldCrt.andPlanOrderIdEqualTo(liquidatesPlanOrderId);
				debtDetailOldCrt.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
				debtDetailOldCrt.andOrderIdEqualTo(debtCredit.getSellOrderId());
				debtDetailOldCrt.andRepayStatusEqualTo(0);
				debtDetailOldCrt.andDelFlagEqualTo(1);
				debtDetailOldCrt.andStatusEqualTo(1);
				debtDetailOldExample.setOrderByClause("repay_period ASC");
				List<DebtDetail> debtDetailOldList = this.debtDetailMapper.selectByExample(debtDetailOldExample);
				if (debtDetailOldList != null && debtDetailOldList.size() == 1) {
					DebtDetail debtDetailOld = debtDetailOldList.get(0);
					// 延期天数
					BigDecimal delayDays = new BigDecimal(debtDetailOld.getDelayDays());
					// 逾期天数
					BigDecimal lateDays = new BigDecimal(debtDetailOld.getLateDays());
					// 总天数，不分期为总天数 分期为当前期总天数
					BigDecimal sellerTotalDays = this.calculateDaysByRepay(borrowNid, liquidatesPeriod, CalculatesUtil.STYLE_ENDDAY);
					// 承接人债转利息
					assignInterest = HTJServiceFeeUtils.getEndDayAssignInterest(sellerInterest, sellerInterestWait, sellerCapital, sellerCapitalWait, assignCapital);
					// 承接人本期正常垫付利息
					assignAdvanceInterest = HTJServiceFeeUtils.getEndDayAssignInterestAdvance(sellerInterest, sellerInterestAdvanceNormalWait, sellerCapital, sellerCapitalWait, sellerHoldDays, sellerTotalDays, assignCapital);
					// 承接人待收取延期利息
					assignRepayDelayInterest = HTJServiceFeeUtils.getEndDayAssignRepayInterestDelay(sellerInterest, delayDays, sellerTotalDays, assignCapital, sellerCapital, sellerCapitalWait, sellerDelayInterestWait);
					// 承接人待收取逾期利息
					assignRepayLateInterest = HTJServiceFeeUtils.getEndDayAssignRepayInterestLate(sellerInterest, lateDays, sellerTotalDays, assignCapital, sellerCapital, sellerCapitalWait, sellerLateInterestWait);
					// 垫付利息
					assignAdvanceMentInterest = assignAdvanceInterest.add(assignRepayDelayInterest).add(assignRepayLateInterest);
				}
				// 债转本息
				assignAccount = assignCapital.add(assignInterest);
				// 实付金额 承接本金*（1-折价率）+应垫付利息+延期利息+逾期利息
				assignPay = assignCapital.add(assignAdvanceMentInterest);
			}
			// 按月
			else if (borrowStyle.equals(CalculatesUtil.STYLE_END)) {
				// 承接人承接本金
				assignCapital = HTJServiceFeeUtils.getAssignCapital(account, sellerCapitalWait, sellerInterestAdvanceWait, sellerCapital, sellerInterestAdvance);
				// 出让人的债权信息
				DebtDetailExample debtDetailOldExample = new DebtDetailExample();
				DebtDetailExample.Criteria debtDetailOldCrt = debtDetailOldExample.createCriteria();
				debtDetailOldCrt.andPlanNidEqualTo(liquidatesPlanNid);
				debtDetailOldCrt.andPlanOrderIdEqualTo(liquidatesPlanOrderId);
				debtDetailOldCrt.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
				debtDetailOldCrt.andOrderIdEqualTo(debtCredit.getSellOrderId());
				debtDetailOldCrt.andRepayStatusEqualTo(0);
				debtDetailOldCrt.andDelFlagEqualTo(1);
				debtDetailOldCrt.andStatusEqualTo(1);
				debtDetailOldExample.setOrderByClause("repay_period ASC");
				List<DebtDetail> debtDetailOldList = this.debtDetailMapper.selectByExample(debtDetailOldExample);
				if (debtDetailOldList != null && debtDetailOldList.size() == 1) {
					DebtDetail debtDetailOld = debtDetailOldList.get(0);
					// 延期天数
					BigDecimal delayDays = new BigDecimal(debtDetailOld.getDelayDays());
					// 逾期天数
					BigDecimal lateDays = new BigDecimal(debtDetailOld.getLateDays());
					// 计算总天数
					BigDecimal sellerTotalDays = this.calculateDaysByRepay(borrowNid, liquidatesPeriod, CalculatesUtil.STYLE_END);
					// 承接人债转总利息
					assignInterest = HTJServiceFeeUtils.getEndAssignInterest(sellerInterest, sellerInterestWait, sellerCapital, sellerCapitalWait, assignCapital);
					// 承接人垫付利息
					assignAdvanceInterest = HTJServiceFeeUtils.getEndAssignInterestAdvance(sellerInterest, sellerInterestAdvanceNormalWait, sellerCapital, sellerCapitalWait, sellerHoldDays, sellerTotalDays, assignCapital);
					// 承接人延期利息
					assignRepayDelayInterest = HTJServiceFeeUtils.getEndDayAssignRepayInterestDelay(sellerInterest, delayDays, sellerTotalDays, assignCapital, sellerCapital, sellerCapitalWait, sellerDelayInterestWait);
					// 承接人逾期利息
					assignRepayLateInterest = HTJServiceFeeUtils.getEndDayAssignRepayInterestLate(sellerInterest, lateDays, sellerTotalDays, assignCapital, sellerCapital, sellerCapitalWait, sellerLateInterestWait);
					// 垫付利息
					assignAdvanceMentInterest = assignAdvanceInterest.add(assignRepayDelayInterest).add(assignRepayLateInterest);
				}
				// 债转本息
				assignAccount = assignCapital.add(assignInterest);
				// 实付金额 承接本金*（1-折价率）+应垫付利息+延期利息+逾期利息
				assignPay = assignCapital.add(assignAdvanceMentInterest);
			}
			// 先息后本
			else if (borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
				// 承接人承接本金
				assignCapital = HTJServiceFeeUtils.getAssignCapital(account, sellerCapitalWait, sellerInterestAdvanceWait, sellerCapital, sellerInterestAdvance);
				// 出让人的债权信息
				DebtDetailExample debtDetailOldExample = new DebtDetailExample();
				DebtDetailExample.Criteria debtDetailOldCrt = debtDetailOldExample.createCriteria();
				debtDetailOldCrt.andPlanNidEqualTo(liquidatesPlanNid);
				debtDetailOldCrt.andPlanOrderIdEqualTo(liquidatesPlanOrderId);
				debtDetailOldCrt.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
				debtDetailOldCrt.andOrderIdEqualTo(debtCredit.getSellOrderId());
				debtDetailOldCrt.andRepayStatusEqualTo(0);
				debtDetailOldCrt.andDelFlagEqualTo(1);
				debtDetailOldCrt.andStatusEqualTo(1);
				debtDetailOldExample.setOrderByClause("repay_period ASC");
				List<DebtDetail> debtDetailOldList = this.debtDetailMapper.selectByExample(debtDetailOldExample);
				// 承接人此次承接的总待收本金
				BigDecimal assignPeriodCapitalTotal = BigDecimal.ZERO;
				// 承接人此次承接的总待收利息
				BigDecimal assignPeriodInterestTotal = BigDecimal.ZERO;
				// 承接人此次承接的总垫付利息
				BigDecimal assignPeriodAdvanceMentInterestTotal = BigDecimal.ZERO;
				// 承接人此次承接的正常总垫付利息
				BigDecimal assignPeriodAdvanceInterestTotal = BigDecimal.ZERO;
				// 承接人此次承接待收取的总待收利息
				BigDecimal assignPeriodRepayDelayInterestTotal = BigDecimal.ZERO;
				// 承接人此次承接待收取的总待收利息
				BigDecimal assignPeriodRepayLateInterestTotal = BigDecimal.ZERO;
				// 判断数据完整性
				if (debtDetailOldList != null && debtDetailOldList.size() > 0) {
					Map<Integer, Object> assignResult = new HashMap<Integer, Object>();
					for (int i = 0; i < debtDetailOldList.size(); i++) {
						// 承接人此次承接的分期承接本金
						BigDecimal assignPeriodCapital = BigDecimal.ZERO;
						// 承接人此次承接的分期承接利息
						BigDecimal assignPeriodInterest = BigDecimal.ZERO;
						// 承接人此次承接的分期垫付利息
						BigDecimal assignPeriodAdvanceMentInterest = BigDecimal.ZERO;
						// 承接人此次承接的分期正常垫付利息
						BigDecimal assignPeriodAdvanceInterest = BigDecimal.ZERO;
						// 承接人此次承接的待收取分期延期利息
						BigDecimal assignPeriodRepayDelayInterest = BigDecimal.ZERO;
						// 承接人此次承接的待收取分期逾期利息
						BigDecimal assignPeriodRepayLateInterest = BigDecimal.ZERO;
						// 债权信息
						DebtDetail debtDetailOld = debtDetailOldList.get(i);
						// 延期天数
						BigDecimal delayDays = new BigDecimal(debtDetailOld.getDelayDays());
						// 逾期天数
						BigDecimal lateDays = new BigDecimal(debtDetailOld.getLateDays());
						// 还款期数
						int waitRepayPeriod = debtDetailOld.getRepayPeriod();
						// 出让人本期的待收本金
						BigDecimal sellerPeriodCapitalWait = debtDetailOld.getRepayCapitalWait();
						// 出让人本期的待收利息
						BigDecimal sellerPeriodInterestWait = debtDetailOld.getRepayInterestWait();
						// 分期此期本金
						BigDecimal sellerPeriodCapital = debtDetailOld.getLoanCapital();
						// 出让前的待收收益
						BigDecimal sellerPeriodInterest = debtDetailOld.getLoanInterest();
						// 剩余的延期利息
						BigDecimal sellerPeriodDelayInterestWait = debtDetailOld.getDelayInterest().subtract(debtDetailOld.getDelayInterestAssigned());
						// 剩余的逾期利息
						BigDecimal sellerPeriodLateInterestWait = debtDetailOld.getLateInterest().subtract(debtDetailOld.getLateInterestAssigned());
						// 计算总天数
						BigDecimal sellerPeriodTotalDays = this.calculateDaysByRepay(borrowNid, waitRepayPeriod, CalculatesUtil.STYLE_PRINCIPAL);
						// 承接人此次承接的分期待收本金
						assignPeriodCapital = HTJServiceFeeUtils.getAssignPeriodCapital(assignCapital, sellerTotalCapital, sellerCapitalWait, sellerPeriodCapital, sellerPeriodCapitalWait);
						// 承接人此次承接的分期待收利息
						assignPeriodInterest = HTJServiceFeeUtils.getAssignPeriodInterest(assignCapital, sellerTotalCapital, sellerCapitalWait, sellerPeriodCapital, sellerPeriodInterest, sellerPeriodInterestWait);
						// 承接人此次承接的分期延期利息
						assignPeriodRepayDelayInterest = HTJServiceFeeUtils.getEndMonthAssignRepayInterestDelay(sellerPeriodInterest, delayDays, sellerPeriodTotalDays, assignCapital, assignPeriodCapital, sellerPeriodCapital, sellerCapitalWait, sellerPeriodDelayInterestWait);
						// 承接人此次承接的分期逾期利息
						assignPeriodRepayLateInterest = HTJServiceFeeUtils.getEndMonthAssignRepayInterestLate(sellerPeriodInterest, lateDays, sellerPeriodTotalDays, assignCapital, assignPeriodCapital, sellerPeriodCapital, sellerCapitalWait, sellerPeriodLateInterestWait);
						// 如果还款期数为清算期
						assignPeriodAdvanceInterest = HTJServiceFeeUtils.getEndMonthAssignInterestAdvance(sellerPeriodInterest, sellerInterestAdvanceNormalWait, sellerCapital, sellerCapitalWait, sellerHoldDays, sellerPeriodTotalDays, assignCapital);
						// 承接人此次承接的总待收本金
						assignPeriodCapitalTotal = assignPeriodCapitalTotal.add(assignPeriodCapital);
						// 承接人此次承接的总待收利息
						assignPeriodInterestTotal = assignPeriodInterestTotal.add(assignPeriodInterest);
						// 承接人此次承接的总垫付利息
						assignPeriodAdvanceInterestTotal = assignPeriodAdvanceInterestTotal.add(assignPeriodAdvanceInterest);
						// 承接人此次承接的待收取总延期利息
						assignPeriodRepayDelayInterestTotal = assignPeriodRepayDelayInterestTotal.add(assignPeriodRepayDelayInterest);
						// 承接人此次承接的待收取总逾期利息
						assignPeriodRepayLateInterestTotal = assignPeriodRepayLateInterestTotal.add(assignPeriodRepayLateInterest);
						// 承接人此次承接的总垫付利息
						assignPeriodAdvanceMentInterestTotal = assignPeriodAdvanceMentInterestTotal.add(assignPeriodAdvanceInterestTotal).add(assignPeriodRepayDelayInterestTotal).add(assignPeriodRepayLateInterestTotal);
						// 返回结果集
						Map<String, BigDecimal> assignPeriodMap = new HashMap<String, BigDecimal>();
						assignPeriodMap.put(ASSIGN_PERIOD_CAPITAL, assignPeriodCapital);
						assignPeriodMap.put(ASSIGN_PERIOD_INTEREST, assignPeriodInterest);
						assignPeriodMap.put(ASSIGN_PERIOD_ADVANCEMENT_INTEREST, assignPeriodAdvanceMentInterest);
						assignPeriodMap.put(ASSIGN_PERIOD_REPAY_DELAY_INTEREST, assignPeriodRepayDelayInterest);
						assignPeriodMap.put(ASSIGN_PERIOD_REPAY_LATE_INTEREST, assignPeriodRepayLateInterest);
						assignResult.put(waitRepayPeriod, assignPeriodMap);
					}
					// 分期利息计算结果
					result.put(ASSIGN_RESULT, assignResult);
				}
				// 债转本息
				assignAccount = assignCapital.add(assignInterest);
				// 重置承接人承接本金
				assignCapital = assignPeriodCapitalTotal;
				// 债转总利息
				assignInterest = assignPeriodInterestTotal;
				// 垫付总利息
				assignAdvanceInterest = assignPeriodAdvanceInterestTotal;
				// 承接人此次承接待收取的总延期利息
				assignRepayDelayInterest = assignPeriodRepayDelayInterestTotal;
				// 承接人此次承接待收取的总逾期利息
				assignRepayLateInterest = assignPeriodRepayLateInterestTotal;
				// 垫付总利息
				assignAdvanceMentInterest = assignPeriodAdvanceMentInterestTotal;
				// 实付金额 承接本金*（1-折价率）+应垫付利息
				assignPay = assignCapital.add(assignAdvanceMentInterest);
			}
			// 等额本金
			else if (borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL)) {
				// 计算承接本金
				assignCapital = HTJServiceFeeUtils.getAssignCapital(account, sellerCapitalWait, sellerInterestAdvanceWait, sellerCapital, sellerInterestAdvance);
				// 出让人的债权信息
				DebtDetailExample debtDetailOldExample = new DebtDetailExample();
				DebtDetailExample.Criteria debtDetailOldCrt = debtDetailOldExample.createCriteria();
				debtDetailOldCrt.andPlanNidEqualTo(liquidatesPlanNid);
				debtDetailOldCrt.andPlanOrderIdEqualTo(liquidatesPlanOrderId);
				debtDetailOldCrt.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
				debtDetailOldCrt.andOrderIdEqualTo(debtCredit.getSellOrderId());
				debtDetailOldCrt.andRepayStatusEqualTo(0);
				debtDetailOldCrt.andDelFlagEqualTo(1);
				debtDetailOldCrt.andStatusEqualTo(1);
				debtDetailOldExample.setOrderByClause("repay_period ASC");
				List<DebtDetail> debtDetailOldList = this.debtDetailMapper.selectByExample(debtDetailOldExample);
				// 承接人此次承接的总待收本金
				BigDecimal assignPeriodCapitalTotal = BigDecimal.ZERO;
				// 承接人此次承接的总待收利息
				BigDecimal assignPeriodInterestTotal = BigDecimal.ZERO;
				// 承接人此次承接的总垫付利息
				BigDecimal assignPeriodAdvanceMentInterestTotal = BigDecimal.ZERO;
				// 承接人此次承接的正常总垫付利息
				BigDecimal assignPeriodAdvanceInterestTotal = BigDecimal.ZERO;
				// 承接人此次承接待收取的总待收利息
				BigDecimal assignPeriodRepayDelayInterestTotal = BigDecimal.ZERO;
				// 承接人此次承接待收取的总待收利息
				BigDecimal assignPeriodRepayLateInterestTotal = BigDecimal.ZERO;
				// 判断数据完整性
				if (debtDetailOldList != null && debtDetailOldList.size() > 0) {
					Map<Integer, Object> assignResult = new HashMap<Integer, Object>();
					for (int i = 0; i < debtDetailOldList.size(); i++) {
						// 承接人此次承接的分期承接本金
						BigDecimal assignPeriodCapital = BigDecimal.ZERO;
						// 承接人此次承接的分期承接利息
						BigDecimal assignPeriodInterest = BigDecimal.ZERO;
						// 承接人此次承接的分期垫付利息
						BigDecimal assignPeriodAdvanceMentInterest = BigDecimal.ZERO;
						// 承接人此次承接的分期正常垫付利息
						BigDecimal assignPeriodAdvanceInterest = BigDecimal.ZERO;
						// 承接人此次承接的待收取分期延期利息
						BigDecimal assignPeriodRepayDelayInterest = BigDecimal.ZERO;
						// 承接人此次承接的待收取分期逾期利息
						BigDecimal assignPeriodRepayLateInterest = BigDecimal.ZERO;
						// 债权信息
						DebtDetail debtDetailOld = debtDetailOldList.get(i);
						// 延期天数
						BigDecimal delayDays = new BigDecimal(debtDetailOld.getDelayDays());
						// 逾期天数
						BigDecimal lateDays = new BigDecimal(debtDetailOld.getLateDays());
						// 还款期数
						int waitRepayPeriod = debtDetailOld.getRepayPeriod();
						// 出让人本期的待收本金
						BigDecimal sellerPeriodCapitalWait = debtDetailOld.getRepayCapitalWait();
						// 出让人本期的待收利息
						BigDecimal sellerPeriodInterestWait = debtDetailOld.getRepayInterestWait();
						// 分期此期本金
						BigDecimal sellerPeriodCapital = debtDetailOld.getLoanCapital();
						// 出让前的待收收益
						BigDecimal sellerPeriodInterest = debtDetailOld.getLoanInterest();
						// 剩余的延期利息
						BigDecimal sellerPeriodDelayInterestWait = debtDetailOld.getDelayInterest().subtract(debtDetailOld.getDelayInterestAssigned());
						// 剩余的逾期利息
						BigDecimal sellerPeriodLateInterestWait = debtDetailOld.getLateInterest().subtract(debtDetailOld.getLateInterestAssigned());
						// 计算总天数
						BigDecimal sellerPeriodTotalDays = this.calculateDaysByRepay(borrowNid, waitRepayPeriod, CalculatesUtil.STYLE_PRINCIPAL);
						// 承接人此次承接的分期待收本金
						assignPeriodCapital = HTJServiceFeeUtils.getAssignPeriodCapital(assignCapital, sellerTotalCapital, sellerCapitalWait, sellerPeriodCapital, sellerPeriodCapitalWait);
						// 承接人此次承接的分期待收利息
						assignPeriodInterest = HTJServiceFeeUtils.getAssignPeriodInterest(assignCapital, sellerTotalCapital, sellerCapitalWait, sellerPeriodCapital, sellerPeriodInterest, sellerPeriodInterestWait);
						// 承接人此次承接的待收取分期延期利息
						assignPeriodRepayDelayInterest = HTJServiceFeeUtils.getPrincipalAssignInterestDelay(sellerPeriodInterest, delayDays, sellerPeriodTotalDays, assignCapital, assignPeriodCapital, sellerPeriodCapital, sellerCapitalWait, sellerPeriodDelayInterestWait);
						// 承接人此次承接的待收取分期逾期利息
						assignPeriodRepayLateInterest = HTJServiceFeeUtils.getPrincipalAssignInterestLate(sellerPeriodInterest, lateDays, sellerPeriodTotalDays, assignCapital, assignPeriodCapital, sellerPeriodCapital, sellerCapitalWait, sellerPeriodLateInterestWait);
						// 如果还款期数为清算期
						assignPeriodAdvanceInterest = HTJServiceFeeUtils.getPrincipalAssignInterestAdvance(sellerPeriodInterest, sellerInterestAdvanceNormalWait, sellerCapital, sellerCapitalWait, sellerHoldDays, sellerPeriodTotalDays, assignCapital);
						// 承接人此次承接的总待收本金
						assignPeriodCapitalTotal = assignPeriodCapitalTotal.add(assignPeriodCapital);
						// 承接人此次承接的总待收利息
						assignPeriodInterestTotal = assignPeriodInterestTotal.add(assignPeriodInterest);
						// 承接人此次承接的总垫付利息
						assignPeriodAdvanceInterestTotal = assignPeriodAdvanceInterestTotal.add(assignPeriodAdvanceInterest);
						// 承接人此次承接的待收取总延期利息
						assignPeriodRepayDelayInterestTotal = assignPeriodRepayDelayInterestTotal.add(assignPeriodRepayDelayInterest);
						// 承接人此次承接的待收取总逾期利息
						assignPeriodRepayLateInterestTotal = assignPeriodRepayLateInterestTotal.add(assignPeriodRepayLateInterest);
						// 承接人此次承接的总垫付利息
						assignPeriodAdvanceMentInterestTotal = assignPeriodAdvanceMentInterestTotal.add(assignPeriodAdvanceInterestTotal).add(assignPeriodRepayDelayInterestTotal).add(assignPeriodRepayLateInterestTotal);
						// 返回结果集
						Map<String, BigDecimal> assignPeriodMap = new HashMap<String, BigDecimal>();
						assignPeriodMap.put(ASSIGN_PERIOD_CAPITAL, assignPeriodCapital);
						assignPeriodMap.put(ASSIGN_PERIOD_INTEREST, assignPeriodInterest);
						assignPeriodMap.put(ASSIGN_PERIOD_ADVANCEMENT_INTEREST, assignPeriodAdvanceMentInterest);
						assignPeriodMap.put(ASSIGN_PERIOD_REPAY_DELAY_INTEREST, assignPeriodRepayDelayInterest);
						assignPeriodMap.put(ASSIGN_PERIOD_REPAY_LATE_INTEREST, assignPeriodRepayLateInterest);
						assignResult.put(waitRepayPeriod, assignPeriodMap);
					}
					// 分期利息计算结果
					result.put(ASSIGN_RESULT, assignResult);
				}
				// 债转本息
				assignAccount = assignCapital.add(assignInterest);
				// 重置承接人承接本金
				assignCapital = assignPeriodCapitalTotal;
				// 债转总利息
				assignInterest = assignPeriodInterestTotal;
				// 垫付总利息
				assignAdvanceInterest = assignPeriodAdvanceInterestTotal;
				// 承接人此次承接待收取的总延期利息
				assignRepayDelayInterest = assignPeriodRepayDelayInterestTotal;
				// 承接人此次承接待收取的总逾期利息
				assignRepayLateInterest = assignPeriodRepayLateInterestTotal;
				// 垫付总利息
				assignAdvanceMentInterest = assignPeriodAdvanceMentInterestTotal;
				// 实付金额 承接本金*（1-折价率）+应垫付利息
				assignPay = assignCapital.add(assignAdvanceMentInterest);
			}
			// 等额本息
			else if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH)) {
				// 计算承接本金
				assignCapital = HTJServiceFeeUtils.getAssignCapital(account, sellerCapitalWait, sellerInterestAdvanceWait, sellerCapital, sellerInterestAdvance);
				// 出让人的债权信息
				DebtDetailExample debtDetailOldExample = new DebtDetailExample();
				DebtDetailExample.Criteria debtDetailOldCrt = debtDetailOldExample.createCriteria();
				debtDetailOldCrt.andPlanNidEqualTo(liquidatesPlanNid);
				debtDetailOldCrt.andPlanOrderIdEqualTo(liquidatesPlanOrderId);
				debtDetailOldCrt.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
				debtDetailOldCrt.andOrderIdEqualTo(debtCredit.getSellOrderId());
				debtDetailOldCrt.andRepayStatusEqualTo(0);
				debtDetailOldCrt.andDelFlagEqualTo(1);
				debtDetailOldCrt.andStatusEqualTo(1);
				debtDetailOldExample.setOrderByClause("repay_period ASC");
				List<DebtDetail> debtDetailOldList = this.debtDetailMapper.selectByExample(debtDetailOldExample);
				// 承接人此次承接的总待收本金
				BigDecimal assignPeriodCapitalTotal = BigDecimal.ZERO;
				// 承接人此次承接的总待收利息
				BigDecimal assignPeriodInterestTotal = BigDecimal.ZERO;
				// 承接人此次承接的总垫付利息
				BigDecimal assignPeriodAdvanceMentInterestTotal = BigDecimal.ZERO;
				// 承接人此次承接的正常总垫付利息
				BigDecimal assignPeriodAdvanceInterestTotal = BigDecimal.ZERO;
				// 承接人此次承接待收取的总待收利息
				BigDecimal assignPeriodRepayDelayInterestTotal = BigDecimal.ZERO;
				// 承接人此次承接待收取的总待收利息
				BigDecimal assignPeriodRepayLateInterestTotal = BigDecimal.ZERO;
				// 校验数据完整性
				if (debtDetailOldList != null && debtDetailOldList.size() > 0) {
					Map<Integer, Object> assignResult = new HashMap<Integer, Object>();
					for (int i = 0; i < debtDetailOldList.size(); i++) {
						// 承接人此次承接的分期承接本金
						BigDecimal assignPeriodCapital = BigDecimal.ZERO;
						// 承接人此次承接的分期承接利息
						BigDecimal assignPeriodInterest = BigDecimal.ZERO;
						// 承接人此次承接的分期垫付利息
						BigDecimal assignPeriodAdvanceMentInterest = BigDecimal.ZERO;
						// 承接人此次承接的分期正常垫付利息
						BigDecimal assignPeriodAdvanceInterest = BigDecimal.ZERO;
						// 承接人此次承接的待收取分期延期利息
						BigDecimal assignPeriodRepayDelayInterest = BigDecimal.ZERO;
						// 承接人此次承接的待收取分期逾期利息
						BigDecimal assignPeriodRepayLateInterest = BigDecimal.ZERO;
						// 债权信息
						DebtDetail debtDetailOld = debtDetailOldList.get(i);
						// 延期天数
						BigDecimal delayDays = new BigDecimal(debtDetailOld.getDelayDays());
						// 逾期天数
						BigDecimal lateDays = new BigDecimal(debtDetailOld.getLateDays());
						// 还款期数
						int waitRepayPeriod = debtDetailOld.getRepayPeriod();
						// 出让人本期的待收本金
						BigDecimal sellerPeriodCapitalWait = debtDetailOld.getRepayCapitalWait();
						// 出让人本期的待收利息
						BigDecimal sellerPeriodInterestWait = debtDetailOld.getRepayInterestWait();
						// 分期此期本金
						BigDecimal sellerPeriodCapital = debtDetailOld.getLoanCapital();
						// 出让前的待收收益
						BigDecimal sellerPeriodInterest = debtDetailOld.getLoanInterest();
						// 剩余的延期利息
						BigDecimal sellerPeriodDelayInterestWait = debtDetailOld.getDelayInterest().subtract(debtDetailOld.getDelayInterestAssigned());
						// 剩余的逾期利息
						BigDecimal sellerPeriodLateInterestWait = debtDetailOld.getLateInterest().subtract(debtDetailOld.getLateInterestAssigned());
						// 计算总天数
						BigDecimal sellerPeriodTotalDays = this.calculateDaysByRepay(borrowNid, waitRepayPeriod, CalculatesUtil.STYLE_PRINCIPAL);
						// 承接人此次承接的分期待收本金
						assignPeriodCapital = HTJServiceFeeUtils.getAssignPeriodCapital(assignCapital, sellerTotalCapital, sellerCapitalWait, sellerPeriodCapital, sellerPeriodCapitalWait);
						// 承接人此次承接的分期待收利息
						assignPeriodInterest = HTJServiceFeeUtils.getAssignPeriodInterest(assignCapital, sellerTotalCapital, sellerCapitalWait, sellerPeriodCapital, sellerPeriodInterest, sellerPeriodInterestWait);
						// 本期延期利息
						assignPeriodRepayDelayInterest = HTJServiceFeeUtils.getMonthAssignRepayInterestDelay(sellerPeriodInterest, delayDays, sellerPeriodTotalDays, assignCapital, assignPeriodCapital, sellerPeriodCapital, sellerCapitalWait, sellerPeriodDelayInterestWait);
						// 本期逾期利息
						assignPeriodRepayLateInterest = HTJServiceFeeUtils.getMonthAssignRepayInterestLate(sellerPeriodInterest, lateDays, sellerPeriodTotalDays, assignCapital, assignPeriodCapital, sellerPeriodCapital, sellerCapitalWait, sellerPeriodLateInterestWait);
						// 如果还款期数为清算期
						assignPeriodAdvanceInterest = HTJServiceFeeUtils.getEndMonthAssignInterestAdvance(sellerPeriodInterest, sellerInterestAdvanceNormalWait, sellerCapital, sellerCapitalWait, sellerHoldDays, sellerPeriodTotalDays, assignCapital);
						// 承接人此次承接的总待收本金
						assignPeriodCapitalTotal = assignPeriodCapitalTotal.add(assignPeriodCapital);
						// 承接人此次承接的总待收利息
						assignPeriodInterestTotal = assignPeriodInterestTotal.add(assignPeriodInterest);
						// 承接人此次承接的正常总垫付利息
						assignPeriodAdvanceInterestTotal = assignPeriodAdvanceInterestTotal.add(assignPeriodAdvanceInterest);
						// 承接人此次承接的待收取总延期利息
						assignPeriodRepayDelayInterestTotal = assignPeriodRepayDelayInterestTotal.add(assignPeriodRepayDelayInterest);
						// 承接人此次承接的待收取总逾期利息
						assignPeriodRepayLateInterestTotal = assignPeriodRepayLateInterestTotal.add(assignPeriodRepayLateInterest);
						// 承接人此次承接的总垫付利息
						assignPeriodAdvanceMentInterestTotal = assignPeriodAdvanceMentInterestTotal.add(assignPeriodAdvanceInterestTotal).add(assignPeriodRepayDelayInterestTotal).add(assignPeriodRepayLateInterestTotal);
						// 返回结果集
						Map<String, BigDecimal> assignPeriodMap = new HashMap<String, BigDecimal>();
						assignPeriodMap.put(ASSIGN_PERIOD_CAPITAL, assignPeriodCapital);
						assignPeriodMap.put(ASSIGN_PERIOD_INTEREST, assignPeriodInterest);
						assignPeriodMap.put(ASSIGN_PERIOD_ADVANCEMENT_INTEREST, assignPeriodAdvanceMentInterest);
						assignPeriodMap.put(ASSIGN_PERIOD_REPAY_DELAY_INTEREST, assignPeriodRepayDelayInterest);
						assignPeriodMap.put(ASSIGN_PERIOD_REPAY_LATE_INTEREST, assignPeriodRepayLateInterest);
						assignResult.put(waitRepayPeriod, assignPeriodMap);
					}
					// 分期利息计算结果
					result.put(ASSIGN_RESULT, assignResult);
				}
				// 债转本息
				assignAccount = assignCapital.add(assignInterest);
				// 重置承接人承接本金
				assignCapital = assignPeriodCapitalTotal;
				// 债转总利息
				assignInterest = assignPeriodInterestTotal;
				// 垫付正常总利息
				assignAdvanceInterest = assignPeriodAdvanceInterestTotal;
				// 承接人此次承接待收取的总延期利息
				assignRepayDelayInterest = assignPeriodRepayDelayInterestTotal;
				// 承接人此次承接待收取的总逾期利息
				assignRepayLateInterest = assignPeriodRepayLateInterestTotal;
				// 垫付总利息
				assignAdvanceMentInterest = assignPeriodAdvanceMentInterestTotal;
				// 实付金额 承接本金*（1-折价率）+应垫付利息
				assignPay = assignCapital.add(assignAdvanceMentInterest);
			}
			// 计算服务费
			BigDecimal serviceFee = this.calculateServiceFee(debtCredit, assignCapital.add(assignAdvanceMentInterest));
			// 返回结果封装
			result.put(ASSIGN_ACCOUNT, assignAccount);
			result.put(ASSIGN_CAPITAL, assignCapital);
			result.put(ASSIGN_INTEREST, assignInterest);
			result.put(ASSIGN_ADVANCEMENT_INTEREST, assignAdvanceMentInterest);
			result.put(ASSIGN_REPAY_DELAY_INTEREST, assignRepayDelayInterest);
			result.put(ASSIGN_REPAY_LATE_INTEREST, assignRepayLateInterest);
			result.put(ASSIGN_PAY, assignPay);
			result.put(SERVICE_FEE, serviceFee);
			// 获取相应的用户信息
			Users user = this.usersMapper.selectByPrimaryKey(userId);
			if (Validator.isNotNull(user)) {
				// 保存credit_tender_log表
				DebtCreditTenderLog debtCreditTenderLog = new DebtCreditTenderLog();
				debtCreditTenderLog.setUserId(userId);
				debtCreditTenderLog.setUserName(user.getUsername());
				debtCreditTenderLog.setCreditUserId(debtCredit.getUserId());
				debtCreditTenderLog.setCreditUserName(debtCredit.getUserName());
				debtCreditTenderLog.setStatus(0);
				debtCreditTenderLog.setLiquidatesPlanNid(liquidatesPlanNid);
				debtCreditTenderLog.setLiquidatesPlanOrderId(liquidatesPlanOrderId);
				debtCreditTenderLog.setAssignPlanNid(planNid);
				debtCreditTenderLog.setAssignPlanOrderId(planOrderId);
				debtCreditTenderLog.setBorrowNid(debtCredit.getBorrowNid());
				debtCreditTenderLog.setCreditNid(debtCredit.getCreditNid());
				debtCreditTenderLog.setInvestOrderId(debtCredit.getInvestOrderId());
				debtCreditTenderLog.setSellOrderId(debtCredit.getSellOrderId());
				debtCreditTenderLog.setAssignOrderId(creditOrderId);
				debtCreditTenderLog.setAssignOrderDate(creditOrderDate);
				debtCreditTenderLog.setAssignCapital(assignCapital);
				debtCreditTenderLog.setAssignAccount(assignAccount);
				debtCreditTenderLog.setAssignInterest(assignInterest);
				debtCreditTenderLog.setAssignRepayDelayInterest(assignRepayDelayInterest);
				debtCreditTenderLog.setAssignRepayLateInterest(assignRepayLateInterest);
				debtCreditTenderLog.setAssignInterestAdvance(assignAdvanceMentInterest);
				debtCreditTenderLog.setAssignPrice(assignPay);
				debtCreditTenderLog.setAssignPay(assignPay);
				debtCreditTenderLog.setAssignRepayAccount(assignCapital.add(assignInterest));
				debtCreditTenderLog.setAssignRepayCapital(assignCapital);
				debtCreditTenderLog.setAssignRepayInterest(assignInterest);
				debtCreditTenderLog.setAssignRepayEndTime(debtCredit.getCreditRepayEndTime());
				debtCreditTenderLog.setAssignRepayLastTime(debtCredit.getCreditRepayLastTime());
				debtCreditTenderLog.setAssignRepayNextTime(debtCredit.getCreditRepayNextTime());
				debtCreditTenderLog.setAssignRepayYesTime(0);
				debtCreditTenderLog.setAssignRepayPeriod(debtCredit.getCreditPeriod());
				debtCreditTenderLog.setRepayPeriod(repayPeriod);
				debtCreditTenderLog.setAssignRepayPeriod(creditPeriod);
				debtCreditTenderLog.setAssignOrderDate(creditOrderDate);
				debtCreditTenderLog.setAssignServiceFee(serviceFee);
				debtCreditTenderLog.setCreateUserId(userId);
				debtCreditTenderLog.setCreateUserName(userName);
				debtCreditTenderLog.setCreateTime(GetDate.getNowTime10());
				debtCreditTenderLog.setClient(client);
				boolean flag = this.debtCreditTenderLogMapper.insertSelective(debtCreditTenderLog) > 0 ? true : false;
				if (flag) {
					return result;
				} else {
					throw new Exception("债转日志debtCrditTenderLog表保存失败，债权标号：" + creditNid + ",出借编号：" + sellOrderId);
				}
			} else {
				throw new Exception("未查询到相应的用户信息，用户userId：" + userId);
			}
		} else {
			throw new Exception("未查询到相应的标的信息，项目编号：" + borrowNid + ",还款期数：" + repayPeriod + 1);
		}
	}

	/**
	 * 调用汇付接口承接相应的债权
	 * 
	 * @param debtCredit
	 * @param userId
	 * @param creditOrderId
	 * @param creditOrderDate
	 * @param accedeBalance
	 * @param serviceFee
	 * @return
	 */

	private ChinapnrBean creditTender(DebtCredit debtCredit, int userId, String creditOrderId, String creditOrderDate, BigDecimal assignAccount, BigDecimal assignCapital, BigDecimal serviceFee) {

		String borrowNid = debtCredit.getBorrowNid();
		// 获取借款信息
		DebtBorrow borrow = this.selectDebtBorrowByNid(borrowNid);
		// 取得债权诚接人用户在汇付天下的客户号
		AccountChinapnr accountChinapnrSeller = this.getAccountChinapnr(debtCredit.getUserId());
		// 取得承接人在汇付天下的客户号
		AccountChinapnr accountChinapnrAssigner = this.getAccountChinapnr(userId);
		// 取得借款人在汇付天下的客户号
		AccountChinapnr accountChinapnrBorrower = this.getAccountChinapnr(borrow.getUserId());

		ChinapnrBean bean = new ChinapnrBean();
		// 调用汇付接口
		String retUrl = "";
		bean.setVersion(ChinaPnrConstant.VERSION_30);// 接口版本号
		bean.setCmdId(ChinaPnrConstant.CMDID_AUTO_CREDIT_ASSIGN); // 消息类型(充值)
		bean.setSellCustId(String.valueOf(accountChinapnrSeller.getChinapnrUsrcustid().toString()));// 债权转让转让人客户号
		bean.setCreditAmt(CustomUtil.formatAmount(assignCapital.toString()));// 债权转让转出的本金
		bean.setCreditDealAmt(CustomUtil.formatAmount(assignAccount.toString()));// 债权转让承接人付给的金额
		// 根据相应的比例计算服务费用
		bean.setFee(CustomUtil.formatAmount(serviceFee.toString()));// 扣款手续费
		bean.setBuyCustId(accountChinapnrAssigner.getChinapnrUsrcustid().toString());// 承接人客户号
		bean.setOrdId(creditOrderId);// 订单号
		bean.setOrdDate(creditOrderDate);// 订单日期
		bean.setRetUrl(retUrl); // 页面返回 URL
		bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
		MerPriv merPriv = new MerPriv();
		merPriv.setUserId(userId);
		bean.setMerPrivPo(merPriv);// 商户私有域
		// 债权转让明细
		Map<String, Object> borrowerDetailMap = new HashMap<String, Object>();
		borrowerDetailMap.put("BorrowerCustId", accountChinapnrBorrower.getChinapnrUsrcustid().toString());// 借款人汇付客户号
		borrowerDetailMap.put("BorrowerCreditAmt", CustomUtil.formatAmount(assignCapital.toString()));// 明细转让金额
		// 出让人收到的已还款金额
		BigDecimal sellerRepayCapitalYes = BigDecimal.ZERO;
		String bidOrdDate = "";
		if (debtCredit.getSourceType() == 1) {
			// 获取原始债权已还款数据
			DebtInvest debtInvest = this.selectDebtInvest(borrowNid, debtCredit.getSellOrderId());
			bidOrdDate = debtInvest.getOrderDate();
			BigDecimal creditRepayCapitalSum = this.selectDebtCreditRepaySum(borrowNid, debtCredit.getSellOrderId());
			sellerRepayCapitalYes = debtInvest.getRepayCapitalYes().subtract(creditRepayCapitalSum);
		} else {
			// 获取承接债权已还款数据
			DebtCreditTender debtCreditTender = this.selectDebtCreditTender(borrowNid, debtCredit.getSellOrderId());
			bidOrdDate = debtCreditTender.getAssignOrderDate();
			BigDecimal creditRepayCapitalSum = this.selectDebtCreditRepaySum(borrowNid, debtCredit.getSellOrderId());
			sellerRepayCapitalYes = debtCreditTender.getRepayCapitalYes().subtract(creditRepayCapitalSum);
		}
		borrowerDetailMap.put("PrinAmt", CustomUtil.formatAmount(sellerRepayCapitalYes.toString()));// 已还款金额
		JSONArray borrowDetailsArryJson = new JSONArray();
		borrowDetailsArryJson.add(borrowerDetailMap);
		Map<String, Object> bidDetailMap = new HashMap<String, Object>();
		bidDetailMap.put("BidOrdId", debtCredit.getSellOrderId());// 被转让的投标订单号
		bidDetailMap.put("BidOrdDate", bidOrdDate);// 被转让的投标订单日期
		bidDetailMap.put("BidCreditAmt", CustomUtil.formatAmount(assignCapital.toString()));// 转让金额
		if (borrow.getBankInputFlag() != null && borrow.getBankInputFlag() == 1) {
			borrowerDetailMap.put("ProId", borrow.getBorrowNid());// 3.0预留项目ID
		}
		bidDetailMap.put("BorrowerDetails", borrowDetailsArryJson);
		JSONArray bidDetailsArryJson = new JSONArray();
		bidDetailsArryJson.add(bidDetailMap);
		Map<String, Object> bidDetailsMap = new HashMap<String, Object>();
		bidDetailsMap.put("BidDetails", bidDetailsArryJson);
		bean.setBidDetails(JSON.toJSONString(bidDetailsMap));// 债权转让明细
		// 分账账户串（当 管理费！=0 时是必填项）
		String divDetails = "";
		if (serviceFee.compareTo(BigDecimal.ZERO) > 0) {
			JSONArray ja = new JSONArray();
			JSONObject jo = new JSONObject();
			// 分账账户号(子账户号,从配置文件中取得)
			jo.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT17));
			// 分账金额
			jo.put(ChinaPnrConstant.PARAM_DIVAMT, CustomUtil.formatAmount(serviceFee.toString()));
			ja.add(jo);
			divDetails = ja.toString();
		}
		bean.setDivDetails(divDetails);// 分账户串
		// 跳转到汇付天下画面
		try {
			ChinapnrBean result = ChinapnrUtil.callApiBg(bean);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询相应的债转的已还款金额
	 * 
	 * @param borrowNid
	 * @param sellOrderId
	 * @return
	 */
	private BigDecimal selectDebtCreditRepaySum(String borrowNid, String sellOrderId) {

		BigDecimal repaySum = BigDecimal.ZERO;
		DebtCreditExample example = new DebtCreditExample();
		DebtCreditExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andSellOrderIdEqualTo(sellOrderId);
		List<DebtCredit> debtCreditList = this.debtCreditMapper.selectByExample(example);
		if (debtCreditList != null && debtCreditList.size() > 0) {
			for (DebtCredit debtCredit : debtCreditList) {
				repaySum = repaySum.add(debtCredit.getRepayCapital());
			}
		}
		return repaySum;
	}

	/**
	 * 查询相应的债权承接记录
	 * 
	 * @param borrowNid
	 * @param sellOrderId
	 * @return
	 */
	private DebtCreditTender selectDebtCreditTender(String borrowNid, String sellOrderId) {

		DebtCreditTenderExample example = new DebtCreditTenderExample();
		DebtCreditTenderExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andAssignOrderIdEqualTo(sellOrderId);
		List<DebtCreditTender> debtCreditTenderList = this.debtCreditTenderMapper.selectByExample(example);
		if (debtCreditTenderList != null && debtCreditTenderList.size() == 1) {
			return debtCreditTenderList.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 查询相应的原始标的放款记录
	 * 
	 * @param borrowNid
	 * @param investOrderId
	 * @return
	 */
	private DebtInvest selectDebtInvest(String borrowNid, String investOrderId) {

		DebtInvestExample example = new DebtInvestExample();
		DebtInvestExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andOrderIdEqualTo(investOrderId);
		List<DebtInvest> debtInvestList = this.debtInvestMapper.selectByExample(example);
		if (debtInvestList != null && debtInvestList.size() == 1) {
			return debtInvestList.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 获取借款信息
	 *
	 * @param borrowId
	 * @return 借款信息
	 */
	@Override
	public DebtBorrow selectDebtBorrowByNid(String borrowNid) {
		DebtBorrow borrow = null;
		DebtBorrowExample example = new DebtBorrowExample();
		DebtBorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<DebtBorrow> list = debtBorrowMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			borrow = list.get(0);
		}
		return borrow;
	}

	/**
	 * 债转汇付交易成功后回调处理
	 * 
	 * @param sellerDebtPlanAccede
	 * 
	 * @param creditOrderDate
	 * @param creditOrderId
	 * @param serviceFee
	 * @param expectApr
	 * @param resultMap
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean saveCreditTender(DebtPlanAccede sellerDebtPlanAccede, DebtCredit debtCredit, DebtPlanAccede assignDebtPlanAccede, ChinapnrBean bean, String creditOrderId, String creditOrderDate, BigDecimal serviceFee, BigDecimal expectApr, Map<String, Object> resultMap) throws Exception {

		// 清算出的债权编号
		String creditNid = debtCredit.getCreditNid();
		// 清算债权标号
		String liquidatesPlanNid = debtCredit.getPlanNid();
		// 清算债权加入订单号
		String liquidatesPlanOrderId = debtCredit.getPlanOrderId();
		// 清算所在期数
		int liquidatesPeriod = debtCredit.getLiquidatesPeriod();
		// 债权原有出借订单号
		String sellerOrderId = debtCredit.getSellOrderId();
		// 原标标号
		String borrowNid = debtCredit.getBorrowNid();
		// 原标年华收益率
		BigDecimal borrowApr = debtCredit.getBorrowApr();
		// 已还款期数
		int repayPeriod = debtCredit.getRepayPeriod();
		// 出让人用户id
		int sellerUserId = debtCredit.getUserId();
		// 出让人用户名
		String sellerUserName = debtCredit.getUserName();
		// 债权出让剩余本金
		BigDecimal sellerCapitalWait = debtCredit.getCreditCapitalWait();
		// 债权上次清算时间
		int lastLiquidationTime = debtCredit.getCreateTime();
		// 加入用户userId
		int userId = assignDebtPlanAccede.getUserId();
		// 加入用户名
		String userName = assignDebtPlanAccede.getUserName();
		// 计划nid
		String planNid = assignDebtPlanAccede.getPlanNid();
		// 计划加入订单号
		String planOrderId = assignDebtPlanAccede.getAccedeOrderId();
		// 加入平台
		int client = assignDebtPlanAccede.getClient();
		// 债权承接订单号
		String orderId = bean.getOrdId();
		// 债权承接日期
		String orderDate = bean.getOrdDate();
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		DebtBorrow borrow = this.selectDebtBorrowByNid(borrowNid);
		if (Validator.isNotNull(borrow)) {
			String borrowStyle = borrow.getBorrowStyle();
			// 是否月标(true:月标, false:天标)
			boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
			// 检验回调是否已经存入CreditTender表中
			DebtCreditTenderExample creditTenderExample = new DebtCreditTenderExample();
			DebtCreditTenderExample.Criteria debtCreditTenderCrt = creditTenderExample.createCriteria();
			debtCreditTenderCrt.andLiquidatesPlanNidEqualTo(liquidatesPlanNid);
			debtCreditTenderCrt.andAssignPlanNidEqualTo(planNid);
			debtCreditTenderCrt.andCreditNidEqualTo(creditNid);
			debtCreditTenderCrt.andSellOrderIdEqualTo(sellerOrderId);
			debtCreditTenderCrt.andAssignOrderIdEqualTo(creditOrderId);
			debtCreditTenderCrt.andUserIdEqualTo(userId);
			List<DebtCreditTender> creditTenderList = this.debtCreditTenderMapper.selectByExample(creditTenderExample);
			if (creditTenderList == null || creditTenderList.size() <= 0) {
				// 获取CreditTenderLog信息
				DebtCreditTenderLogExample debtCreditTenderLogExample = new DebtCreditTenderLogExample();
				DebtCreditTenderLogExample.Criteria debtCreditTenderLogCrt = debtCreditTenderLogExample.createCriteria();
				debtCreditTenderLogCrt.andLiquidatesPlanNidEqualTo(liquidatesPlanNid);
				debtCreditTenderLogCrt.andAssignPlanNidEqualTo(planNid);
				debtCreditTenderLogCrt.andCreditNidEqualTo(creditNid);
				debtCreditTenderLogCrt.andSellOrderIdEqualTo(sellerOrderId);
				debtCreditTenderLogCrt.andAssignOrderIdEqualTo(creditOrderId);
				debtCreditTenderLogCrt.andUserIdEqualTo(userId);
				List<DebtCreditTenderLog> debtCreditTenderLogList = this.debtCreditTenderLogMapper.selectByExample(debtCreditTenderLogExample);
				if (debtCreditTenderLogList != null && debtCreditTenderLogList.size() == 1) {
					// 首先更新CreditTenderLog状态是10
					DebtCreditTenderLog debtCreditTenderLog = debtCreditTenderLogList.get(0);
					debtCreditTenderLog.setStatus(10);
					debtCreditTenderLog.setUpdateTime(nowTime);
					debtCreditTenderLog.setUpdateUserId(userId);
					debtCreditTenderLog.setUpdateUserName(userName);
					boolean debtCreditTenderLogFlag = this.debtCreditTenderLogMapper.updateByPrimaryKeySelective(debtCreditTenderLog) > 0 ? true : false;
					if (debtCreditTenderLogFlag) {
						// 取得债权出让人的用户在汇付天下的客户号
						AccountChinapnr accountChinapnrSeller = this.getAccountChinapnr(debtCreditTenderLog.getCreditUserId());
						// 取得承接债转的用户在汇付天下的客户号
						AccountChinapnr accountChinapnrTender = this.getAccountChinapnr(userId);
						if (orderId.equals(creditOrderId) && orderDate.equals(creditOrderDate) && accountChinapnrSeller.getChinapnrUsrcustid().toString().equals(bean.getSellCustId()) && accountChinapnrTender.getChinapnrUsrcustid().toString().equals(bean.getBuyCustId())) {
							// 承接总本金
							BigDecimal assignTotalCapital = debtCreditTenderLog.getAssignCapital();
							// 1.插入credit_tender
							DebtCreditTender debtCreditTender = new DebtCreditTender();
							debtCreditTender.setLiquidatesPlanNid(liquidatesPlanNid);// 清算计划nid
							debtCreditTender.setLiquidatesPlanOrderId(liquidatesPlanOrderId);// 清算计划订单号
							debtCreditTender.setAssignPlanNid(planNid);// 承接计划编号
							debtCreditTender.setAssignPlanOrderId(planOrderId);// 承接计划加入订单号
							debtCreditTender.setBorrowNid(borrowNid);// 原标标号
							debtCreditTender.setCreditNid(creditNid);// 债转标号
							debtCreditTender.setCreditUserId(debtCredit.getUserId());// 出让人id
							debtCreditTender.setCreditUserName(debtCredit.getUserName());
							debtCreditTender.setInvestOrderId(debtCredit.getInvestOrderId());
							debtCreditTender.setSellOrderId(sellerOrderId);// 债转投标单号
							debtCreditTender.setAssignOrderId(creditOrderId);// 认购单号
							debtCreditTender.setAssignOrderDate(creditOrderDate);// 认购日期
							debtCreditTender.setAssignPay(debtCreditTenderLog.getAssignPay());// 支付金额
							debtCreditTender.setAssignServiceFee(serviceFee); // 服务费
							debtCreditTender.setUserId(userId);// 用户id
							debtCreditTender.setUserName(userName);// 承接人用户名
							debtCreditTender.setStatus(0);// 状态
							debtCreditTender.setAssignAccount(debtCreditTenderLog.getAssignAccount());// 回收总额
							debtCreditTender.setAssignCapital(debtCreditTenderLog.getAssignCapital()); // 出借本金
							debtCreditTender.setAssignInterest(debtCreditTenderLog.getAssignInterest());// 债转利息
							debtCreditTender.setAssignInterestAdvance(debtCreditTenderLog.getAssignInterestAdvance());// 垫付利息
							debtCreditTender.setAssignRepayDelayInterest(debtCreditTenderLog.getAssignRepayDelayInterest());
							debtCreditTender.setAssignRepayLateInterest(debtCreditTenderLog.getAssignRepayLateInterest());
							debtCreditTender.setAssignPrice(debtCreditTenderLog.getAssignPrice());// 购买价格
							debtCreditTender.setRepayAccountWait(debtCreditTenderLog.getAssignRepayAccount());// 已还总额
							debtCreditTender.setRepayCapitalWait(debtCreditTenderLog.getAssignRepayCapital());// 已还本金
							debtCreditTender.setRepayInterestWait(debtCreditTenderLog.getAssignRepayInterest());// 已还利息
							debtCreditTender.setAssignRepayEndTime(debtCreditTenderLog.getAssignRepayEndTime());// 最后还款日
							debtCreditTender.setAssignRepayLastTime(debtCreditTenderLog.getAssignRepayLastTime());// 上次还款时间
							debtCreditTender.setAssignRepayNextTime(debtCreditTenderLog.getAssignRepayNextTime());// 下次还款时间
							debtCreditTender.setAssignRepayYesTime(0);// 最终实际还款时间
							debtCreditTender.setAssignType(1);// 承接方式
							debtCreditTender.setAssignRepayPeriod(debtCreditTenderLog.getAssignRepayPeriod());// 还款期数
							debtCreditTender.setClient(client);// 客户端
							debtCreditTender.setRepayPeriod(repayPeriod);
							debtCreditTender.setCreateUserId(userId);
							debtCreditTender.setCreateUserName(userName);
							debtCreditTender.setCreateTime(nowTime);
							boolean debtCreditTenderFlag = this.debtCreditTenderMapper.insertSelective(debtCreditTender) > 0 ? true : false;
							if (debtCreditTenderFlag) {
								// 5.更新borrow_credit
								debtCredit.setCreditAccountWait(debtCredit.getCreditAccountWait().subtract(debtCreditTender.getAssignAccount()));// 认购本息（不包含垫付利息）
								debtCredit.setCreditCapitalWait(debtCredit.getCreditCapitalWait().subtract(debtCreditTender.getAssignCapital()));// 已认购本金
								debtCredit.setCreditInterestWait(debtCredit.getCreditInterestWait().subtract(debtCreditTender.getAssignInterest()));// 承接总利息
								debtCredit.setCreditInterestAdvanceWait(debtCredit.getCreditInterestAdvanceWait().subtract(debtCreditTender.getAssignInterestAdvance()));// 垫付总利息
								debtCredit.setCreditAccountAssigned(debtCredit.getCreditAccountAssigned().add(debtCreditTender.getAssignAccount()));// 认购本息（不包含垫付利息）
								debtCredit.setCreditCapitalAssigned(debtCredit.getCreditCapitalAssigned().add(debtCreditTender.getAssignCapital()));// 已认购本金
								debtCredit.setCreditInterestAssigned(debtCredit.getCreditInterestAssigned().add(debtCreditTender.getAssignInterest()));// 承接总利息
								debtCredit.setCreditInterestAdvanceAssigned(debtCredit.getCreditInterestAdvanceAssigned().add(debtCreditTender.getAssignInterestAdvance()));// 垫付总利息
								debtCredit.setCreditDelayInterestAssigned(debtCredit.getCreditDelayInterestAssigned().add(debtCreditTender.getAssignRepayDelayInterest()));
								debtCredit.setCreditLateInterestAssigned(debtCredit.getCreditLateInterestAssigned().add(debtCreditTender.getAssignRepayLateInterest()));
								debtCredit.setCreditServiceFee(debtCredit.getCreditServiceFee().add(debtCreditTender.getAssignServiceFee()));// 服务费
								debtCredit.setCreditIncome(debtCredit.getCreditIncome().add(debtCreditTender.getAssignPay().subtract(serviceFee)));// 总收入,本金+垫付利息-服务费
								debtCredit.setCreditPrice(debtCredit.getCreditPrice().add(debtCreditTender.getAssignPay()));// 总收入,本金+垫付利息
								debtCredit.setUpdateTime(nowTime);// 认购时间
								debtCredit.setUpdateUserId(userId);// 认购时间
								debtCredit.setUpdateUserName(userName);// 认购时间
								debtCredit.setAssignNum(debtCredit.getAssignNum() + 1);// 出借次数
								if (sellerCapitalWait.compareTo(assignTotalCapital) == 0) {
									debtCredit.setCreditStatus(2);
									debtCredit.setIsLiquidates(1);
								}
								boolean debtCreditFlag = debtCreditMapper.updateByPrimaryKey(debtCredit) > 0 ? true : false;
								if (!debtCreditFlag) {
									throw new Exception("债权信息表debtCredit更新失败，债权编号：" + debtCredit.getCreditNid());
								}
								// 承接人计划订单信息更新，余额扣减
								DebtPlanAccede assignDebtPlanAccedeNew = new DebtPlanAccede();
								assignDebtPlanAccedeNew.setAccedeBalance(debtCreditTenderLog.getAssignPay());
								assignDebtPlanAccedeNew.setId(assignDebtPlanAccede.getId());
								boolean assignDebtPlanAccedeFlag = this.batchDebtPlanAccedeCustomizeMapper.updateDebtPlanAccedeAssign(assignDebtPlanAccedeNew) > 0 ? true : false;
								if (assignDebtPlanAccedeFlag) {
									DebtPlan assignDebtPlan = new DebtPlan();
									assignDebtPlan.setDebtPlanNid(planNid);
									assignDebtPlan.setDebtPlanBalance(debtCreditTenderLog.getAssignPay());
									assignDebtPlan.setUpdateTime(nowTime);
									assignDebtPlan.setUpdateUserId(userId);
									assignDebtPlan.setUpdateUserName(userName);
									boolean debtPlanFlag = batchDebtPlanCustomizeMapper.updateDebtPlanAssign(assignDebtPlan) > 0 ? true : false;
									if (debtPlanFlag) {
										// 更新承接用户账户余额表
										Account creditAssignAccount = new Account();
										// 承接用户id
										creditAssignAccount.setUserId(userId);
										// 承接人可用余额
										creditAssignAccount.setPlanBalance(debtCreditTenderLog.getAssignPay());
										// 承接人计划真实可用余额
										creditAssignAccount.setPlanAccedeBalance(debtCreditTenderLog.getAssignPay());
										// 更新承接人计划账户
										boolean assignAccountFlag = this.adminAccountCustomizeMapper.updateOfPlanCreditAssign(creditAssignAccount) > 0 ? true : false;
										if (assignAccountFlag) {
											Account assignAccount = this.selectUserAccount(userId);
											if (Validator.isNotNull(assignAccount)) {
												assignDebtPlanAccede = selectDebtPlanAccede(assignDebtPlanAccede.getId());
												// 插入相应的承接人汇添金资金明细表
												DebtAccountList assignDebtAccountList = new DebtAccountList();
												assignDebtAccountList.setNid(creditOrderId);
												assignDebtAccountList.setUserId(userId);
												assignDebtAccountList.setPlanNid(planNid);
												assignDebtAccountList.setPlanOrderId(planOrderId);
												assignDebtAccountList.setUserName(userName);
												assignDebtAccountList.setBalance(assignAccount.getBalance());
												assignDebtAccountList.setFrost(assignAccount.getFrost());
												assignDebtAccountList.setTotal(assignAccount.getTotal());
												assignDebtAccountList.setAccountWait(assignAccount.getAwait());
												assignDebtAccountList.setCapitalWait(BigDecimal.ZERO);
												assignDebtAccountList.setInterestWait(BigDecimal.ZERO);
												assignDebtAccountList.setPlanBalance(assignAccount.getPlanBalance());
												assignDebtAccountList.setPlanFrost(assignAccount.getPlanFrost());
												assignDebtAccountList.setPlanOrderBalance(assignDebtPlanAccede.getAccedeBalance());
												assignDebtAccountList.setPlanOrderFrost(assignDebtPlanAccede.getAccedeFrost());
												assignDebtAccountList.setAmount(debtCreditTenderLog.getAssignPay());
												assignDebtAccountList.setType(2);
												assignDebtAccountList.setTrade("accede_assign");
												assignDebtAccountList.setTradeCode("balance");
												assignDebtAccountList.setRemark(planOrderId);
												assignDebtAccountList.setWeb(0);
												UsersInfo assignUserInfo = getUsersInfoByUserId(userId);
												// 用户属性 0=>无主单 1=>有主单
												// 2=>线下员工
												// 3=>线上员工
												Integer assignAttribute = null;
												if (Validator.isNotNull(assignUserInfo)) {
													// 获取出借用户的用户属性
													assignAttribute = assignUserInfo.getAttribute();
													if (Validator.isNotNull(assignAttribute)) {
														if (assignAttribute == 1) {
															SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
															SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
															spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
															List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
															if (sList != null && sList.size() == 1) {
																int refUserId = sList.get(0).getSpreadsUserid();
																// 查找用户推荐人
																Users refererUser = getUsersByUserId(refUserId);
																if (Validator.isNotNull(refererUser)) {
																	assignDebtAccountList.setRefererUserId(refererUser.getUserId());
																	assignDebtAccountList.setRefererUserName(refererUser.getUsername());
																}
															}
														} else if (assignAttribute == 0) {
															SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
															SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
															spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
															List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
															if (sList != null && sList.size() == 1) {
																int refUserId = sList.get(0).getSpreadsUserid();
																// 查找推荐人
																Users refererUser = getUsersByUserId(refUserId);
																if (Validator.isNotNull(refererUser)) {
																	assignDebtAccountList.setRefererUserId(refererUser.getUserId());
																	assignDebtAccountList.setRefererUserName(refererUser.getUsername());
																}
															}
														}
													}
												}
												assignDebtAccountList.setCreateUserId(userId);
												assignDebtAccountList.setCreateUserName(userName);
												assignDebtAccountList.setCreateTime(nowTime);
												// 插入交易明细
												boolean assignDebtAccountListFlag = this.debtAccountListMapper.insertSelective(assignDebtAccountList) > 0 ? true : false;
												if (assignDebtAccountListFlag) {

													DebtPlan sellerDebtPlan = new DebtPlan();
													sellerDebtPlan.setDebtPlanNid(liquidatesPlanNid);
													sellerDebtPlan.setServiceFee(serviceFee);
													sellerDebtPlan.setLiquidateArrivalAmount(debtCreditTenderLog.getAssignPay().subtract(serviceFee));
													Map<String, Object> debtCreditParams = new HashMap<String, Object>();
													debtCreditParams.put("liquidatesPlanNid", liquidatesPlanNid);
													int creditCount = this.batchDebtCreditCustomizeMapper.countDebtCreditsAll(debtCreditParams);
													// 可承接债转为0
													if (creditCount == 0) {
														sellerDebtPlan.setDebtPlanStatus(CustomConstants.DEBT_PLAN_STATUS_8);
													}
													sellerDebtPlan.setUpdateTime(nowTime);
													sellerDebtPlan.setUpdateUserId(userId);
													sellerDebtPlan.setUpdateUserName(userName);
													boolean debtPlanSellerFlag = batchDebtPlanCustomizeMapper.updateDebtPlanSeller(sellerDebtPlan) > 0 ? true : false;
													if (debtPlanSellerFlag) {
														// 出让人计划订单信息更新，清算承接债权余额增加
														DebtPlanAccede sellerDebtPlanAccedeNew = new DebtPlanAccede();
														sellerDebtPlanAccedeNew.setLiquidatesCreditFrost(debtCreditTenderLog.getAssignPay().subtract(serviceFee));
														sellerDebtPlanAccedeNew.setServiceFee(serviceFee);
														sellerDebtPlanAccedeNew.setId(sellerDebtPlanAccede.getId());
														// 可承接债转为0
														if (creditCount == 0) {
															sellerDebtPlanAccedeNew.setStatus(3);
															DebtPlanAccedeExample example = new DebtPlanAccedeExample();
															DebtPlanAccedeExample.Criteria cra = example.createCriteria();
															cra.andPlanNidEqualTo(liquidatesPlanNid);
															DebtPlanAccede accede = new DebtPlanAccede();
															accede.setStatus(3);
															boolean isDebtPlanAccedeUpdateFlag = this.debtPlanAccedeMapper.updateByExampleSelective(accede, example) > 0 ? true : false;
															if (!isDebtPlanAccedeUpdateFlag) {
																throw new Exception("手动债转承接最后一笔更新计划状态失败!计划编号:" + liquidatesPlanNid);
															}
														} else {
															sellerDebtPlanAccedeNew.setStatus(2);// 1出借完成2清算中3清算完成
														}
														boolean sellerdebtPlanAccedeFlag = this.batchDebtPlanAccedeCustomizeMapper.updateDebtPlanAccedeSeller(sellerDebtPlanAccedeNew) > 0 ? true : false;
														if (sellerdebtPlanAccedeFlag) {
															// 更新出让人用户账户余额表
															Account creditSellerAccount = new Account();
															// 出让人用户id
															creditSellerAccount.setUserId(sellerUserId);
															// 出让人计划真实可用余额
															creditSellerAccount.setPlanBalance(debtCreditTenderLog.getAssignPay().subtract(serviceFee));
															// 更新出让人计划账户
															boolean sellerAccountFlag = this.adminAccountCustomizeMapper.updateOfPlanCreditSeller(creditSellerAccount) > 0 ? true : false;
															if (sellerAccountFlag) {
																Account sellerAccount = this.selectUserAccount(sellerUserId);
																if (Validator.isNotNull(sellerAccount)) {
																	sellerDebtPlanAccede = selectDebtPlanAccede(sellerDebtPlanAccede.getId());
																	// 插入相应的出让人汇添金资金明细表
																	DebtAccountList sellerDebtAccountList = new DebtAccountList();
																	sellerDebtAccountList.setNid(creditOrderId);
																	sellerDebtAccountList.setUserId(sellerUserId);
																	sellerDebtAccountList.setUserName(sellerUserName);
																	sellerDebtAccountList.setPlanNid(planNid);
																	sellerDebtAccountList.setPlanOrderId(planOrderId);
																	sellerDebtAccountList.setBalance(sellerAccount.getBalance());
																	sellerDebtAccountList.setFrost(sellerAccount.getFrost());
																	sellerDebtAccountList.setTotal(sellerAccount.getTotal());
																	sellerDebtAccountList.setAccountWait(sellerAccount.getAwait());
																	sellerDebtAccountList.setRepayWait(sellerAccount.getRepay());
																	sellerDebtAccountList.setInterestWait(BigDecimal.ZERO);
																	sellerDebtAccountList.setCapitalWait(BigDecimal.ZERO);
																	sellerDebtAccountList.setPlanBalance(sellerAccount.getPlanBalance());
																	sellerDebtAccountList.setPlanFrost(sellerAccount.getPlanFrost());
																	sellerDebtAccountList.setPlanOrderBalance(sellerDebtPlanAccede.getAccedeBalance());
																	sellerDebtAccountList.setPlanOrderFrost(sellerDebtPlanAccede.getAccedeFrost());
																	sellerDebtAccountList.setAmount(debtCreditTenderLog.getAssignPay().subtract(serviceFee));
																	sellerDebtAccountList.setType(1);
																	sellerDebtAccountList.setTrade("liquidates_sell");
																	sellerDebtAccountList.setTradeCode("balance");
																	sellerDebtAccountList.setRemark(planOrderId);
																	sellerDebtAccountList.setWeb(0);
																	UsersInfo sellerUserInfo = getUsersInfoByUserId(sellerUserId);
																	// 用户属性0=>无主单
																	// 1=>有主单2=>线下员工3=>线上员工
																	Integer sellerAttribute = null;
																	if (Validator.isNotNull(sellerUserInfo)) {
																		// 获取出借用户的用户属性
																		sellerAttribute = sellerUserInfo.getAttribute();
																		if (Validator.isNotNull(sellerAttribute)) {
																			if (sellerAttribute == 1) {
																				SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
																				SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
																				spreadsUsersExampleCriteria.andUserIdEqualTo(sellerUserId);
																				List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
																				if (sList != null && sList.size() == 1) {
																					int refUserId = sList.get(0).getSpreadsUserid();
																					// 查找用户推荐人
																					Users refererUser = getUsersByUserId(refUserId);
																					if (Validator.isNotNull(refererUser)) {
																						sellerDebtAccountList.setRefererUserId(refererUser.getUserId());
																						sellerDebtAccountList.setRefererUserName(refererUser.getUsername());
																					}
																				}
																			} else if (sellerAttribute == 0) {
																				SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
																				SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
																				spreadsUsersExampleCriteria.andUserIdEqualTo(sellerUserId);
																				List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
																				if (sList != null && sList.size() == 1) {
																					int refUserId = sList.get(0).getSpreadsUserid();
																					// 查找推荐人
																					Users refererUser = getUsersByUserId(refUserId);
																					if (Validator.isNotNull(refererUser)) {
																						sellerDebtAccountList.setRefererUserId(refererUser.getUserId());
																						sellerDebtAccountList.setRefererUserName(refererUser.getUsername());
																					}
																				}
																			}
																		}
																	}
																	sellerDebtAccountList.setCreateUserId(sellerUserId);
																	sellerDebtAccountList.setCreateUserName(sellerUserName);
																	sellerDebtAccountList.setCreateTime(nowTime);
																	// 插入交易明细
																	boolean sellerDebtAccountListFlag = this.debtAccountListMapper.insertSelective(sellerDebtAccountList) > 0 ? true : false;
																	if (sellerDebtAccountListFlag) {
																		// 7.生成还款信息
																		// 不分期
																		if (!isMonth) {
																			// 出让人的债权信息
																			DebtDetailExample debtDetailExample = new DebtDetailExample();
																			DebtDetailExample.Criteria debtDetailCrt = debtDetailExample.createCriteria();
																			debtDetailCrt.andPlanNidEqualTo(liquidatesPlanNid);
																			debtDetailCrt.andPlanOrderIdEqualTo(liquidatesPlanOrderId);
																			debtDetailCrt.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
																			debtDetailCrt.andOrderIdEqualTo(sellerOrderId);
																			debtDetailCrt.andRepayPeriodGreaterThanOrEqualTo(liquidatesPeriod);
																			debtDetailCrt.andDelFlagEqualTo(1);
																			debtDetailCrt.andStatusEqualTo(1);
																			debtDetailExample.setOrderByClause("repay_period ASC");
																			List<DebtDetail> debtDetailList = this.debtDetailMapper.selectByExample(debtDetailExample);
																			if (debtDetailList != null && debtDetailList.size() == 1) {
																				DebtDetail debtDetailOld = debtDetailList.get(0);
																				// 还款期数
																				int waitRepayPeriod = debtDetailOld.getRepayPeriod();
																				// 承接人此次承接的分期待收本金
																				BigDecimal assignPeriodCapital = debtCreditTenderLog.getAssignCapital();
																				// 承接人此次承接的分期待收利息
																				BigDecimal assignPeriodInterest = debtCreditTenderLog.getAssignInterest();
																				// 债转本息
																				BigDecimal assignPeriodAccount = assignPeriodCapital.add(assignPeriodInterest);
																				// 承接人此次承接的分期延期利息
																				BigDecimal assignPeriodRepayDelayInterest = debtCreditTenderLog.getAssignRepayDelayInterest();
																				// 承接人此次承接的分期逾期利息
																				BigDecimal assignPeriodRepayLateInterest = debtCreditTenderLog.getAssignRepayLateInterest();
																				DebtDetail debtDetail = new DebtDetail();
																				debtDetail.setUserId(userId);
																				debtDetail.setUserName(userName);
																				debtDetail.setBorrowUserId(borrow.getUserId());
																				debtDetail.setBorrowUserName(borrow.getBorrowUserName());
																				debtDetail.setBorrowName(borrow.getName());
																				debtDetail.setBorrowNid(borrowNid);
																				debtDetail.setBorrowApr(borrowApr);
																				debtDetail.setBorrowStyle(borrow.getBorrowStyle());
																				debtDetail.setBorrowPeriod(borrow.getBorrowPeriod());
																				debtDetail.setPlanNid(planNid);
																				debtDetail.setPlanOrderId(planOrderId);
																				debtDetail.setCreditNid(creditNid);
																				debtDetail.setInvestOrderId(debtCredit.getInvestOrderId());
																				debtDetail.setOrderId(creditOrderId);
																				debtDetail.setOrderDate(creditOrderDate);
																				debtDetail.setOrderType(1);
																				debtDetail.setSourceType(0);
																				debtDetail.setAccount(assignPeriodCapital);
																				debtDetail.setLoanCapital(assignPeriodCapital);
																				debtDetail.setLoanInterest(assignPeriodInterest);
																				debtDetail.setLoanTime(debtDetailOld.getLoanTime());
																				debtDetail.setRepayCapitalWait(assignPeriodCapital);
																				debtDetail.setRepayInterestWait(assignPeriodInterest);
																				debtDetail.setRepayCapitalYes(BigDecimal.ZERO);
																				debtDetail.setRepayInterestYes(BigDecimal.ZERO);
																				debtDetail.setServiceFee(BigDecimal.ZERO);
																				debtDetail.setManageFee(BigDecimal.ZERO);
																				debtDetail.setClient(client);
																				debtDetail.setStatus(1);
																				debtDetail.setRepayStatus(0);
																				debtDetail.setAdvanceStatus(debtDetailOld.getAdvanceStatus());
																				debtDetail.setAdvanceDays(debtDetailOld.getAdvanceDays());
																				debtDetail.setAdvanceInterest(BigDecimal.ZERO);
																				debtDetail.setDelayDays(debtDetailOld.getDelayDays());
																				debtDetail.setDelayInterest(debtDetailOld.getDelayInterest());
																				debtDetail.setLateDays(debtDetailOld.getLateDays());
																				debtDetail.setLateInterest(debtDetailOld.getLateInterest());
																				debtDetail.setRepayPeriod(waitRepayPeriod);
																				debtDetail.setRepayTime(debtDetailOld.getRepayTime());
																				debtDetail.setRepayActionTime(0);
																				debtDetail.setDelFlag(0);
																				debtDetail.setLastLiquidationTime(lastLiquidationTime);
																				debtDetail.setCreateTime(nowTime);
																				debtDetail.setCreateUserId(userId);
																				debtDetail.setCreateUserName(userName);
																				boolean debtDetailFlag = this.debtDetailMapper.insertSelective(debtDetail) > 0 ? true : false;
																				if (debtDetailFlag) {
																					DebtCreditRepay debtCreditRepay = new DebtCreditRepay();
																					debtCreditRepay.setUserId(userId);// 用户名称
																					debtCreditRepay.setUserName(userName);
																					debtCreditRepay.setCreditUserId(debtCreditTender.getCreditUserId());// 出让人id
																					debtCreditRepay.setCreditUserName(debtCreditTender.getCreditUserName());
																					debtCreditRepay.setBorrowNid(debtCreditTender.getBorrowNid());// 原标标号
																					debtCreditRepay.setCreditNid(debtCreditTender.getCreditNid());// 债转标号
																					debtCreditRepay.setInvestOrderId(debtCredit.getInvestOrderId());
																					debtCreditRepay.setSellOrderId(debtCreditTender.getSellOrderId());// 认购单号
																					debtCreditRepay.setAssignPlanNid(debtCreditTender.getAssignPlanNid());// 债转承接计划编号
																					debtCreditRepay.setAssignPlanOrderId(debtCreditTender.getAssignPlanOrderId());// 债转承接订单号
																					debtCreditRepay.setAssignOrderId(debtCreditTender.getAssignOrderId());// 债转投标单号
																					debtCreditRepay.setAdvanceStatus(debtDetailOld.getAdvanceStatus());
																					debtCreditRepay.setAdvanceDays(debtDetailOld.getAdvanceDays());
																					debtCreditRepay.setDelayDays(debtDetailOld.getDelayDays());
																					debtCreditRepay.setLateDays(debtDetailOld.getLateDays());
																					debtCreditRepay.setRepayAccount(assignPeriodAccount);// 应还总额
																					debtCreditRepay.setRepayCapital(assignPeriodCapital);// 应还本金
																					debtCreditRepay.setRepayInterest(assignPeriodInterest);// 应还利息
																					debtCreditRepay.setRepayAccountWait(assignPeriodAccount);// 应还总额
																					debtCreditRepay.setRepayCapitalWait(assignPeriodCapital);// 应还本金
																					debtCreditRepay.setRepayInterestWait(assignPeriodInterest);// 应还利息
																					debtCreditRepay.setRepayAccountYes(BigDecimal.ZERO);// 已还总额
																					debtCreditRepay.setRepayCapitalYes(BigDecimal.ZERO);// 已还本金
																					debtCreditRepay.setRepayInterestYes(BigDecimal.ZERO);// 已还利息
																					debtCreditRepay.setRepayAdvanceInterest(BigDecimal.ZERO);
																					debtCreditRepay.setRepayDelayInterest(BigDecimal.ZERO);
																					debtCreditRepay.setRepayLateInterest(BigDecimal.ZERO);
																					debtCreditRepay.setReceiveAccountYes(BigDecimal.ZERO);
																					debtCreditRepay.setReceiveCapitalYes(BigDecimal.ZERO);
																					debtCreditRepay.setReceiveInterestYes(BigDecimal.ZERO);
																					debtCreditRepay.setReceiveAdvanceInterest(BigDecimal.ZERO);
																					debtCreditRepay.setReceiveDelayInterest(assignPeriodRepayDelayInterest);
																					debtCreditRepay.setReceiveLateInterest(assignPeriodRepayLateInterest);
																					debtCreditRepay.setAssignRepayEndTime(debtCreditTender.getAssignRepayEndTime());// 最后还款日
																					debtCreditRepay.setAssignRepayLastTime(debtCreditTender.getAssignRepayLastTime());// 上次还款时间
																					debtCreditRepay.setAssignRepayNextTime(debtCreditTender.getAssignRepayNextTime());// 下次还款时间
																					debtCreditRepay.setAssignRepayYesTime(0);// 最终实际还款时间
																					debtCreditRepay.setAssignRepayTime(debtDetailOld.getRepayTime());
																					debtCreditRepay.setAssignRepayPeriod(1);// 还款期数
																					debtCreditRepay.setAssignCreateDate(Integer.valueOf(creditOrderDate));// 认购日期
																					debtCreditRepay.setAddip(debtCreditTender.getAddip());// ip
																					debtCreditRepay.setClient(0);// 客户端
																					debtCreditRepay.setDelFlag(0);
																					debtCreditRepay.setRepayStatus(0);// 状态
																					debtCreditRepay.setRepayPeriod(1);// 原标还款期数
																					debtCreditRepay.setManageFee(BigDecimal.ZERO);// 管理费
																					debtCreditRepay.setLiquidatesServiceFee(BigDecimal.ZERO);
																					debtCreditRepay.setUniqueNid(creditOrderId + "_" + waitRepayPeriod);// 唯一nid
																					debtCreditRepay.setCreateTime(nowTime);// 添加时间
																					debtCreditRepay.setCreateUserId(userId);// 添加用户
																					debtCreditRepay.setCreateUserName(userName);// 添加用户名
																					boolean debtCreditRepayFlag = debtCreditRepayMapper.insertSelective(debtCreditRepay) > 0 ? true : false;
																					if (debtCreditRepayFlag) {
																						// 剩余本金
																						debtDetailOld.setUpdateTime(nowTime);
																						debtDetailOld.setUpdateUserId(sellerUserId);
																						debtDetailOld.setUpdateUserName(sellerUserName);
																						debtDetailOld.setStatus(0);
																						// 更新老债权数据的待还本金，待还利息
																						boolean debtDetailOldFlag = this.debtDetailMapper.updateByPrimaryKeySelective(debtDetailOld) > 0 ? true : false;
																						if (debtDetailOldFlag) {
																							if (sellerCapitalWait.compareTo(assignTotalCapital) > 0) {
																								DebtDetail debtDetailNew = new DebtDetail();
																								debtDetailNew.setUserId(debtDetailOld.getUserId());
																								debtDetailNew.setUserName(debtDetailOld.getUserName());
																								debtDetailNew.setBorrowNid(debtDetailOld.getBorrowNid());
																								debtDetailNew.setBorrowName(debtDetailOld.getBorrowName());
																								debtDetailNew.setBorrowUserId(borrow.getUserId());
																								debtDetailNew.setBorrowUserName(borrow.getBorrowUserName());
																								debtDetailNew.setBorrowApr(debtDetailOld.getBorrowApr());
																								debtDetailNew.setBorrowPeriod(debtDetailOld.getBorrowPeriod());
																								debtDetailNew.setBorrowStyle(debtDetailOld.getBorrowStyle());
																								debtDetailNew.setPlanNid(debtDetailOld.getPlanNid());
																								debtDetailNew.setPlanOrderId(debtDetailOld.getPlanOrderId());
																								debtDetailNew.setCreditNid(debtDetailOld.getCreditNid());
																								debtDetailNew.setInvestOrderId(debtCredit.getInvestOrderId());
																								debtDetailNew.setOrderId(debtDetailOld.getOrderId());
																								debtDetailNew.setOrderDate(debtDetailOld.getOrderDate());
																								debtDetailNew.setOrderType(debtDetailOld.getOrderType());
																								debtDetailNew.setSourceType(debtDetailOld.getSourceType());
																								debtDetailNew.setAccount(debtDetailOld.getAccount().subtract(assignPeriodCapital));
																								debtDetailNew.setLoanCapital(debtDetailOld.getLoanCapital());
																								debtDetailNew.setLoanInterest(debtDetailOld.getLoanInterest());
																								debtDetailNew.setLoanTime(debtDetailOld.getLoanTime());
																								debtDetailNew.setRepayCapitalWait(debtDetailOld.getRepayCapitalWait().subtract(assignPeriodCapital));
																								debtDetailNew.setRepayInterestWait(debtDetailOld.getRepayInterestWait().subtract(assignPeriodInterest));
																								debtDetailNew.setRepayCapitalYes(BigDecimal.ZERO);
																								debtDetailNew.setRepayInterestYes(BigDecimal.ZERO);
																								debtDetailNew.setServiceFee(debtDetailOld.getServiceFee().add(serviceFee));
																								debtDetailNew.setManageFee(BigDecimal.ZERO);
																								debtDetailNew.setClient(client);
																								debtDetailNew.setStatus(1);
																								debtDetailNew.setRepayStatus(0);
																								debtDetailNew.setAdvanceStatus(debtDetailOld.getAdvanceStatus());
																								debtDetailNew.setAdvanceDays(debtDetailOld.getAdvanceDays());
																								debtDetailNew.setAdvanceInterest(debtDetailOld.getAdvanceInterest());
																								debtDetailNew.setDelayDays(debtDetailOld.getDelayDays());
																								debtDetailNew.setDelayInterest(debtDetailOld.getDelayInterest());
																								debtDetailNew.setDelayInterestAssigned(debtDetailOld.getDelayInterestAssigned().add(assignPeriodRepayDelayInterest));
																								debtDetailNew.setLateDays(debtDetailOld.getLateDays());
																								debtDetailNew.setLateInterest(debtDetailOld.getLateInterest());
																								debtDetailNew.setLateInterestAssigned(debtDetailOld.getLateInterestAssigned().add(assignPeriodRepayLateInterest));
																								debtDetailNew.setRepayTime(debtDetailOld.getRepayTime());
																								debtDetailNew.setRepayActionTime(0);
																								debtDetailNew.setRepayPeriod(waitRepayPeriod);
																								debtDetailNew.setDelFlag(1);
																								debtDetailNew.setExpireFairValue(debtDetailOld.getExpireFairValue());
																								debtDetailNew.setLastLiquidationTime(debtDetailOld.getLastLiquidationTime());
																								debtDetailNew.setCreateTime(nowTime);
																								debtDetailNew.setCreateUserId(debtDetailOld.getUserId());
																								debtDetailNew.setCreateUserName(debtDetailOld.getUserName());
																								boolean debtDetailNewFlag = this.debtDetailMapper.insertSelective(debtDetailNew) > 0 ? true : false;
																								if (!debtDetailNewFlag) {
																									throw new Exception("出让人债权详情表debtdetail插入失败，加入订单号：" + liquidatesPlanOrderId);
																								}
																							}
																						} else {
																							throw new Exception("出让人债权详情表debtdetail老数据更新失败，加入订单号：" + planOrderId);
																						}
																						// 如果是非原始债权
																						if (debtDetailOld.getSourceType() == 0) {
																							DebtCreditRepay sellerDebtCreditRepayOld = this.selectDebtCreditRepay(liquidatesPlanNid, liquidatesPlanOrderId, sellerOrderId, waitRepayPeriod);
																							if (Validator.isNotNull(sellerDebtCreditRepayOld)) {
																								sellerDebtCreditRepayOld.setDelFlag(1);
																								boolean sellerDebtCreditRepayOldFlag = this.debtCreditRepayMapper.updateByPrimaryKeySelective(sellerDebtCreditRepayOld) > 0 ? true : false;
																								if (sellerDebtCreditRepayOldFlag) {
																									if (sellerCapitalWait.compareTo(assignTotalCapital) > 0) {
																										DebtCreditRepay sellerDebtCreditRepay = new DebtCreditRepay();
																										sellerDebtCreditRepay.setUserId(sellerDebtCreditRepayOld.getUserId());// 用户名称
																										sellerDebtCreditRepay.setUserName(sellerDebtCreditRepayOld.getUserName());
																										sellerDebtCreditRepay.setCreditUserId(sellerDebtCreditRepayOld.getCreditUserId());// 出让人id
																										sellerDebtCreditRepay.setCreditUserName(sellerDebtCreditRepayOld.getCreditUserName());
																										sellerDebtCreditRepay.setBorrowNid(sellerDebtCreditRepayOld.getBorrowNid());// 原标标号
																										sellerDebtCreditRepay.setCreditNid(sellerDebtCreditRepayOld.getCreditNid());// 债转标号
																										sellerDebtCreditRepay.setInvestOrderId(debtCredit.getInvestOrderId());
																										sellerDebtCreditRepay.setSellOrderId(sellerDebtCreditRepayOld.getSellOrderId());// 认购单号
																										sellerDebtCreditRepay.setAssignPlanNid(sellerDebtCreditRepayOld.getAssignPlanNid());// 债转承接计划编号
																										sellerDebtCreditRepay.setAssignPlanOrderId(sellerDebtCreditRepayOld.getAssignPlanOrderId());// 债转承接订单号
																										sellerDebtCreditRepay.setAssignOrderId(sellerDebtCreditRepayOld.getAssignOrderId());// 债转投标单号
																										sellerDebtCreditRepay.setAdvanceStatus(debtDetailOld.getAdvanceStatus());
																										sellerDebtCreditRepay.setAdvanceDays(debtDetailOld.getAdvanceDays());
																										sellerDebtCreditRepay.setDelayDays(debtDetailOld.getDelayDays());
																										sellerDebtCreditRepay.setLateDays(debtDetailOld.getLateDays());
																										sellerDebtCreditRepay.setRepayAccount(sellerDebtCreditRepayOld.getRepayAccount().subtract(assignPeriodAccount));// 应还总额
																										sellerDebtCreditRepay.setRepayCapital(sellerDebtCreditRepayOld.getRepayCapital().subtract(assignPeriodCapital));// 应还本金
																										sellerDebtCreditRepay.setRepayInterest(sellerDebtCreditRepayOld.getRepayInterest().subtract(assignPeriodInterest));// 应还利息
																										sellerDebtCreditRepay.setRepayAdvanceInterest(BigDecimal.ZERO);
																										sellerDebtCreditRepay.setRepayDelayInterest(BigDecimal.ZERO);
																										sellerDebtCreditRepay.setRepayLateInterest(BigDecimal.ZERO);
																										sellerDebtCreditRepay.setRepayAccountWait(sellerDebtCreditRepayOld.getRepayAccountWait().subtract(assignPeriodAccount));// 应还总额
																										sellerDebtCreditRepay.setRepayCapitalWait(sellerDebtCreditRepayOld.getRepayCapital().subtract(assignPeriodCapital));// 应还本金
																										sellerDebtCreditRepay.setRepayInterestWait(sellerDebtCreditRepayOld.getRepayInterest().subtract(assignPeriodInterest));// 应还利息
																										sellerDebtCreditRepay.setRepayAccountYes(BigDecimal.ZERO);// 已还总额
																										sellerDebtCreditRepay.setRepayCapitalYes(BigDecimal.ZERO);// 已还本金
																										sellerDebtCreditRepay.setRepayInterestYes(BigDecimal.ZERO);// 已还利息
																										sellerDebtCreditRepay.setReceiveAccountYes(BigDecimal.ZERO);
																										sellerDebtCreditRepay.setReceiveCapitalYes(BigDecimal.ZERO);
																										sellerDebtCreditRepay.setReceiveInterestYes(BigDecimal.ZERO);
																										sellerDebtCreditRepay.setReceiveAdvanceInterest(BigDecimal.ZERO);
																										sellerDebtCreditRepay.setReceiveDelayInterest(sellerDebtCreditRepay.getReceiveDelayInterest().subtract(assignPeriodRepayDelayInterest));
																										sellerDebtCreditRepay.setReceiveLateInterest(sellerDebtCreditRepay.getReceiveLateInterest().subtract(assignPeriodRepayLateInterest));
																										sellerDebtCreditRepay.setAssignRepayEndTime(debtCreditTender.getAssignRepayEndTime());// 最后还款日
																										sellerDebtCreditRepay.setAssignRepayLastTime(debtCreditTender.getAssignRepayLastTime());// 上次还款时间
																										sellerDebtCreditRepay.setAssignRepayNextTime(debtCreditTender.getAssignRepayNextTime());// 下次还款时间
																										sellerDebtCreditRepay.setAssignRepayYesTime(0);// 最终实际还款时间
																										sellerDebtCreditRepay.setAssignRepayTime(debtDetailOld.getRepayTime());
																										sellerDebtCreditRepay.setAssignRepayPeriod(1);// 还款期数
																										sellerDebtCreditRepay.setAssignCreateDate(sellerDebtCreditRepayOld.getAssignCreateDate());// 认购日期
																										sellerDebtCreditRepay.setAddip(debtCreditTender.getAddip());// ip
																										sellerDebtCreditRepay.setRepayStatus(0);// 状态
																										sellerDebtCreditRepay.setClient(0);// 客户端
																										sellerDebtCreditRepay.setDelFlag(0);
																										sellerDebtCreditRepay.setRepayPeriod(waitRepayPeriod);// 原标还款期数
																										sellerDebtCreditRepay.setManageFee(BigDecimal.ZERO);// 管理费
																										sellerDebtCreditRepay.setLiquidatesServiceFee(debtDetailOld.getServiceFee().add(serviceFee));
																										sellerDebtCreditRepay.setUniqueNid(sellerDebtCreditRepayOld.getAssignOrderId() + "_" + creditOrderId + "_" + waitRepayPeriod);// 唯一nid
																										sellerDebtCreditRepay.setCreateTime(nowTime);// 添加时间
																										sellerDebtCreditRepay.setCreateUserId(sellerUserId);// 添加时间
																										sellerDebtCreditRepay.setCreateUserName(sellerUserName);// 添加时间
																										boolean sellerDebtCreditRepayFlag = debtCreditRepayMapper.insertSelective(sellerDebtCreditRepay) > 0 ? true : false;
																										if (!sellerDebtCreditRepayFlag) {
																											throw new Exception("出让人债转还款表debtcreditrepay插入失败,承接订单号" + debtDetailOld.getOrderId());
																										}
																									}
																								} else {
																									throw new Exception("出让人债转还款表debtcreditrepay更新为无效失败，加入订单号：" + liquidatesPlanOrderId);
																								}
																							}
																						} else {
																							// 更新相应的债转承接金额
																							DebtLoanExample debtLoanExample = new DebtLoanExample();
																							DebtLoanExample.Criteria debtLoanCrt = debtLoanExample.createCriteria();
																							debtLoanCrt.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
																							List<DebtLoan> debtLoanList = this.debtLoanMapper.selectByExample(debtLoanExample);
																							if (debtLoanList != null && debtLoanList.size() == 1) {
																								DebtLoan debtLoan = debtLoanList.get(0);
																								debtLoan.setCreditAmount(debtLoan.getCreditAmount().add(assignPeriodCapital));
																								debtLoan.setCreditInterestAmount(debtLoan.getCreditInterestAmount().add(assignPeriodInterest));
																								boolean debtLoanFlag = this.debtLoanMapper.updateByPrimaryKeySelective(debtLoan) > 0 ? true : false;
																								if (!debtLoanFlag) {
																									throw new Exception("更新相应的出借订单的放款信息失败，出借订单号：" + debtCredit.getInvestOrderId());
																								}
																							} else {
																								throw new Exception("未查询到相应的出借订单的放款信息，出借订单号：" + debtCredit.getInvestOrderId());
																							}
																						}
																					} else {
																						throw new Exception("承接人债转还款表debtcreditrepay插入失败，加入订单号：" + planOrderId);
																					}
																				} else {
																					throw new Exception("承接人债权详情表debtdetail插入失败，加入订单号：" + planOrderId);
																				}
																			} else {
																				throw new Exception("出让人债权详情表debtdetail老数据查询失败，清算计划，计划加入订单号：" + liquidatesPlanOrderId);
																			}
																		}
																		// 分期
																		else if (borrow.getBorrowStyle().equals(CalculatesUtil.STYLE_ENDMONTH) || borrow.getBorrowStyle().equals(CalculatesUtil.STYLE_PRINCIPAL) || borrow.getBorrowStyle().equals(CalculatesUtil.STYLE_MONTH)) {
																			// 出让人的债权信息
																			DebtDetailExample debtDetailExample = new DebtDetailExample();
																			DebtDetailExample.Criteria debtDetailCrt = debtDetailExample.createCriteria();
																			debtDetailCrt.andPlanNidEqualTo(liquidatesPlanNid);
																			debtDetailCrt.andPlanOrderIdEqualTo(liquidatesPlanOrderId);
																			debtDetailCrt.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
																			debtDetailCrt.andOrderIdEqualTo(debtCreditTender.getSellOrderId());
																			debtDetailCrt.andRepayPeriodGreaterThanOrEqualTo(liquidatesPeriod);
																			debtDetailCrt.andDelFlagEqualTo(1);
																			debtDetailCrt.andStatusEqualTo(1);
																			debtDetailExample.setOrderByClause("repay_period ASC");
																			List<DebtDetail> debtDetailList = this.debtDetailMapper.selectByExample(debtDetailExample);
																			if (debtDetailList != null && debtDetailList.size() > 0) {
																				for (int i = 0; i < debtDetailList.size(); i++) {
																					DebtDetail debtDetailOld = debtDetailList.get(i);
																					// 还款期数
																					int waitRepayPeriod = debtDetailOld.getRepayPeriod();
																					@SuppressWarnings("unchecked")
																					Map<Integer, Object> result = (Map<Integer, Object>) resultMap.get(this.ASSIGN_RESULT);
																					@SuppressWarnings("unchecked")
																					Map<String, BigDecimal> periodResult = (Map<String, BigDecimal>) result.get(waitRepayPeriod);
																					// 承接人此次承接的分期待收本金
																					BigDecimal assignPeriodCapital = periodResult.get(this.ASSIGN_PERIOD_CAPITAL);
																					// 承接人此次承接的分期待收利息
																					BigDecimal assignPeriodInterest = periodResult.get(this.ASSIGN_PERIOD_INTEREST);
																					// 承接人此次承接的分期延期利息
																					BigDecimal assignPeriodRepayDelayInterest = periodResult.get(this.ASSIGN_PERIOD_REPAY_DELAY_INTEREST);
																					// 承接人此次承接的分期逾期利息
																					BigDecimal assignPeriodRepayLateInterest = periodResult.get(this.ASSIGN_PERIOD_REPAY_LATE_INTEREST);
																					// 债转本息
																					BigDecimal assignPeriodAccount = assignPeriodCapital.add(assignPeriodInterest);
																					DebtDetail debtDetail = new DebtDetail();
																					debtDetail.setUserId(userId);
																					debtDetail.setUserName(userName);
																					debtDetail.setBorrowUserId(borrow.getUserId());
																					debtDetail.setBorrowName(borrow.getName());
																					debtDetail.setBorrowUserName(borrow.getBorrowUserName());
																					debtDetail.setBorrowNid(borrowNid);
																					debtDetail.setBorrowApr(borrowApr);
																					debtDetail.setBorrowPeriod(borrow.getBorrowPeriod());
																					debtDetail.setBorrowStyle(borrow.getBorrowStyle());
																					debtDetail.setPlanNid(planNid);
																					debtDetail.setPlanOrderId(planOrderId);
																					debtDetail.setCreditNid(creditNid);
																					debtDetail.setInvestOrderId(debtCredit.getInvestOrderId());
																					debtDetail.setOrderId(creditOrderId);
																					debtDetail.setOrderDate(creditOrderDate);
																					debtDetail.setOrderType(1);
																					debtDetail.setSourceType(0);
																					debtDetail.setAccount(assignPeriodCapital);
																					debtDetail.setLoanCapital(assignPeriodCapital);
																					debtDetail.setLoanInterest(assignPeriodInterest);
																					debtDetail.setLoanTime(debtDetailOld.getLoanTime());
																					debtDetail.setRepayCapitalWait(assignPeriodCapital);
																					debtDetail.setRepayInterestWait(assignPeriodInterest);
																					debtDetail.setRepayCapitalYes(BigDecimal.ZERO);
																					debtDetail.setRepayInterestYes(BigDecimal.ZERO);
																					debtDetail.setServiceFee(BigDecimal.ZERO);
																					debtDetail.setManageFee(BigDecimal.ZERO);
																					debtDetail.setClient(client);
																					debtDetail.setStatus(1);
																					debtDetail.setRepayStatus(0);
																					debtDetail.setAdvanceStatus(debtDetailOld.getAdvanceStatus());
																					debtDetail.setAdvanceDays(debtDetailOld.getAdvanceDays());
																					debtDetail.setAdvanceInterest(BigDecimal.ZERO);
																					debtDetail.setDelayDays(debtDetailOld.getDelayDays());
																					debtDetail.setDelayInterest(assignPeriodRepayDelayInterest);
																					debtDetail.setDelayInterestAssigned(BigDecimal.ZERO);
																					debtDetail.setLateDays(debtDetailOld.getLateDays());
																					debtDetail.setLateInterest(assignPeriodRepayLateInterest);
																					debtDetail.setLateInterestAssigned(BigDecimal.ZERO);
																					debtDetail.setRepayPeriod(waitRepayPeriod);
																					debtDetail.setRepayTime(debtDetailOld.getRepayTime());
																					debtDetail.setRepayActionTime(0);
																					debtDetail.setDelFlag(0);
																					debtDetail.setLastLiquidationTime(lastLiquidationTime);
																					debtDetail.setCreateTime(nowTime);
																					debtDetail.setCreateUserId(userId);
																					debtDetail.setCreateUserName(userName);
																					boolean debtDetailFlag = this.debtDetailMapper.insertSelective(debtDetail) > 0 ? true : false;
																					if (debtDetailFlag) {
																						DebtCreditRepay debtCreditRepay = new DebtCreditRepay();
																						debtCreditRepay.setUserId(userId);// 用户名称
																						debtCreditRepay.setUserName(userName);
																						debtCreditRepay.setCreditUserId(sellerUserId);// 出让人id
																						debtCreditRepay.setCreditUserName(sellerUserName);
																						debtCreditRepay.setRepayStatus(0);// 状态
																						debtCreditRepay.setBorrowNid(debtCreditTender.getBorrowNid());// 原标标号
																						debtCreditRepay.setCreditNid(debtCreditTender.getCreditNid());// 债转标号
																						debtCreditRepay.setInvestOrderId(debtCredit.getInvestOrderId());
																						debtCreditRepay.setSellOrderId(debtCreditTender.getSellOrderId());// 认购单号
																						debtCreditRepay.setAssignPlanNid(debtCreditTender.getAssignPlanNid());// 债转承接计划编号
																						debtCreditRepay.setAssignPlanOrderId(debtCreditTender.getAssignPlanOrderId());// 债转承接订单号
																						debtCreditRepay.setAssignOrderId(debtCreditTender.getAssignOrderId());// 债转投标单号
																						debtCreditRepay.setAdvanceStatus(debtDetailOld.getAdvanceStatus());
																						debtCreditRepay.setAdvanceDays(debtDetailOld.getAdvanceDays());
																						debtCreditRepay.setDelayDays(debtDetailOld.getDelayDays());
																						debtCreditRepay.setLateDays(debtDetailOld.getLateDays());
																						debtCreditRepay.setRepayAccount(assignPeriodAccount);// 应还总额
																						debtCreditRepay.setRepayCapital(assignPeriodCapital);// 应还本金
																						debtCreditRepay.setRepayInterest(assignPeriodInterest);// 应还利息
																						debtCreditRepay.setRepayAccountWait(assignPeriodAccount);// 应还总额
																						debtCreditRepay.setRepayCapitalWait(assignPeriodCapital);// 应还本金
																						debtCreditRepay.setRepayInterestWait(assignPeriodInterest);// 应还利息
																						debtCreditRepay.setRepayAccountYes(BigDecimal.ZERO);// 已还总额
																						debtCreditRepay.setRepayCapitalYes(BigDecimal.ZERO);// 已还本金
																						debtCreditRepay.setRepayInterestYes(BigDecimal.ZERO);// 已还利息
																						debtCreditRepay.setRepayAdvanceInterest(BigDecimal.ZERO);
																						debtCreditRepay.setRepayDelayInterest(BigDecimal.ZERO);
																						debtCreditRepay.setRepayLateInterest(BigDecimal.ZERO);
																						debtCreditRepay.setReceiveAccountYes(BigDecimal.ZERO);
																						debtCreditRepay.setReceiveCapitalYes(BigDecimal.ZERO);
																						debtCreditRepay.setReceiveInterestYes(BigDecimal.ZERO);
																						debtCreditRepay.setReceiveAdvanceInterest(BigDecimal.ZERO);
																						debtCreditRepay.setReceiveDelayInterest(assignPeriodRepayDelayInterest);
																						debtCreditRepay.setReceiveLateInterest(assignPeriodRepayLateInterest);
																						debtCreditRepay.setAssignRepayEndTime(debtCreditTender.getAssignRepayEndTime());// 最后还款日
																						if (i == 0) {
																							if (waitRepayPeriod <= 1) {
																								debtCreditRepay.setAssignRepayLastTime(0);// 上次还款时间
																							} else {
																								debtCreditRepay.setAssignRepayLastTime(debtCredit.getCreditRepayLastTime());// 上次还款时间
																							}
																							if (debtDetailList.size() > 1) {
																								debtCreditRepay.setAssignRepayNextTime(debtDetailList.get(i + 1).getRepayTime());// 下次还款时间
																							} else {
																								debtCreditRepay.setAssignRepayNextTime(debtDetailList.get(i).getRepayTime());// 下次还款时间
																							}
																						} else if (i == debtDetailList.size() - 1) {
																							debtCreditRepay.setAssignRepayLastTime(debtDetailList.get(i - 1).getRepayTime());// 上次还款时间
																							debtCreditRepay.setAssignRepayNextTime(debtDetailList.get(i).getRepayTime());// 下次还款时间
																						} else {
																							debtCreditRepay.setAssignRepayLastTime(debtDetailList.get(i - 1).getRepayTime());// 上次还款时间
																							debtCreditRepay.setAssignRepayNextTime(debtDetailList.get(i + 1).getRepayTime());// 下次还款时间
																						}
																						debtCreditRepay.setAssignRepayYesTime(0);// 最终实际还款时间
																						debtCreditRepay.setAssignRepayTime(debtDetailOld.getRepayTime());
																						debtCreditRepay.setAssignRepayPeriod(i + 1);// 还款期数
																						debtCreditRepay.setAssignCreateDate(Integer.valueOf(creditOrderDate));// 认购日期
																						debtCreditRepay.setAddip(debtCreditTender.getAddip());// ip
																						debtCreditRepay.setClient(0);// 客户端
																						debtCreditRepay.setDelFlag(0);
																						debtCreditRepay.setRepayPeriod(debtDetailOld.getRepayPeriod());// 原标还款期数
																						debtCreditRepay.setManageFee(BigDecimal.ZERO);// 管理费
																						debtCreditRepay.setLiquidatesServiceFee(BigDecimal.ZERO);// 服务费
																						debtCreditRepay.setUniqueNid(creditOrderId + "_" + waitRepayPeriod);// 唯一nid
																						debtCreditRepay.setCreateTime(nowTime);
																						debtCreditRepay.setCreateUserId(debtDetailOld.getUserId());
																						debtCreditRepay.setCreateUserName(debtDetailOld.getUserName());
																						boolean debtCreditRepayFlag = debtCreditRepayMapper.insertSelective(debtCreditRepay) > 0 ? true : false;
																						if (debtCreditRepayFlag) {
																							debtDetailOld.setUpdateTime(nowTime);
																							debtDetailOld.setUpdateUserId(sellerUserId);
																							debtDetailOld.setUpdateUserName(sellerUserName);
																							debtDetailOld.setStatus(0);
																							// 更新老债权数据的待还本金，待还利息
																							boolean debtDetailOldFlag = this.debtDetailMapper.updateByPrimaryKeySelective(debtDetailOld) > 0 ? true : false;
																							if (debtDetailOldFlag) {
																								if (sellerCapitalWait.compareTo(assignTotalCapital) > 0) {
																									DebtDetail debtDetailNew = new DebtDetail();
																									debtDetailNew.setUserId(debtDetailOld.getUserId());
																									debtDetailNew.setUserName(debtDetailOld.getUserName());
																									debtDetailNew.setBorrowNid(debtDetailOld.getBorrowNid());
																									debtDetailNew.setBorrowName(debtDetailOld.getBorrowName());
																									debtDetailNew.setBorrowUserId(borrow.getUserId());
																									debtDetailNew.setBorrowUserName(borrow.getBorrowUserName());
																									debtDetailNew.setBorrowApr(debtDetailOld.getBorrowApr());
																									debtDetailNew.setBorrowPeriod(debtDetailOld.getBorrowPeriod());
																									debtDetailNew.setBorrowStyle(debtDetailOld.getBorrowStyle());
																									debtDetailNew.setPlanNid(debtDetailOld.getPlanNid());
																									debtDetailNew.setPlanOrderId(debtDetailOld.getPlanOrderId());
																									debtDetailNew.setCreditNid(debtDetailOld.getCreditNid());
																									debtDetailNew.setInvestOrderId(debtCredit.getInvestOrderId());
																									debtDetailNew.setOrderId(debtDetailOld.getOrderId());
																									debtDetailNew.setOrderDate(debtDetailOld.getOrderDate());
																									debtDetailNew.setOrderType(debtDetailOld.getOrderType());
																									debtDetailNew.setSourceType(debtDetailOld.getSourceType());
																									debtDetailNew.setAccount(debtDetailOld.getAccount().subtract(assignPeriodCapital));
																									debtDetailNew.setLoanCapital(debtDetailOld.getLoanCapital());
																									debtDetailNew.setLoanInterest(debtDetailOld.getLoanInterest());
																									debtDetailNew.setLoanTime(debtDetailOld.getLoanTime());
																									debtDetailNew.setRepayCapitalWait(debtDetailOld.getRepayCapitalWait().subtract(assignPeriodCapital));
																									debtDetailNew.setRepayInterestWait(debtDetailOld.getRepayInterestWait().subtract(assignPeriodInterest));
																									debtDetailNew.setRepayCapitalYes(BigDecimal.ZERO);
																									debtDetailNew.setRepayInterestYes(BigDecimal.ZERO);
																									if (i == 0) {
																										debtDetailNew.setServiceFee(debtDetailOld.getServiceFee().add(serviceFee));
																									} else {
																										debtDetailNew.setServiceFee(BigDecimal.ZERO);
																									}
																									debtDetailNew.setManageFee(BigDecimal.ZERO);
																									debtDetailNew.setClient(client);
																									debtDetailNew.setStatus(1);
																									debtDetailNew.setRepayStatus(0);
																									debtDetailNew.setAdvanceStatus(debtDetailOld.getAdvanceStatus());
																									debtDetailNew.setAdvanceDays(debtDetailOld.getAdvanceDays());
																									debtDetailNew.setAdvanceInterest(debtDetailOld.getAdvanceInterest());
																									debtDetailNew.setDelayDays(debtDetailOld.getDelayDays());
																									debtDetailNew.setDelayInterest(debtDetailOld.getDelayInterest());
																									debtDetailNew.setLateDays(debtDetailOld.getLateDays());
																									debtDetailNew.setLateInterest(debtDetailOld.getLateInterest());
																									debtDetailNew.setRepayTime(debtDetailOld.getRepayTime());
																									debtDetailNew.setRepayActionTime(0);
																									debtDetailNew.setRepayPeriod(waitRepayPeriod);
																									debtDetailNew.setDelFlag(1);
																									debtDetailNew.setExpireFairValue(debtDetailOld.getExpireFairValue());
																									debtDetailNew.setLastLiquidationTime(debtDetailOld.getLastLiquidationTime());
																									debtDetailNew.setCreateTime(nowTime);
																									debtDetailNew.setCreateUserId(debtDetailOld.getUserId());
																									debtDetailNew.setCreateUserName(debtDetailOld.getUserName());
																									boolean debtDetailNewFlag = this.debtDetailMapper.insertSelective(debtDetailNew) > 0 ? true : false;
																									if (!debtDetailNewFlag) {
																										throw new Exception("出让人债权详情表debtdetail插入失败，加入订单号：" + liquidatesPlanOrderId);
																									}
																								}
																							} else {
																								throw new Exception("出让人债权详情表debtdetail老数据更新失败，加入订单号：" + planOrderId);
																							}
																							// 如果是非原始债权
																							if (debtDetailOld.getSourceType() == 0) {
																								DebtCreditRepay sellerDebtCreditRepayOld = this.selectDebtCreditRepay(liquidatesPlanNid, liquidatesPlanOrderId, sellerOrderId, waitRepayPeriod);
																								if (Validator.isNotNull(sellerDebtCreditRepayOld)) {
																									sellerDebtCreditRepayOld.setDelFlag(1);
																									boolean sellerDebtCreditRepayOldFlag = this.debtCreditRepayMapper.updateByPrimaryKeySelective(sellerDebtCreditRepayOld) > 0 ? true : false;
																									if (sellerDebtCreditRepayOldFlag) {
																										if (sellerCapitalWait.compareTo(assignTotalCapital) > 0) {
																											DebtCreditRepay sellerDebtCreditRepay = new DebtCreditRepay();
																											sellerDebtCreditRepay.setUserId(sellerDebtCreditRepayOld.getUserId());// 用户名称
																											sellerDebtCreditRepay.setUserName(sellerDebtCreditRepayOld.getUserName());
																											sellerDebtCreditRepay.setCreditUserId(sellerDebtCreditRepayOld.getCreditUserId());// 出让人id
																											sellerDebtCreditRepay.setCreditUserName(sellerDebtCreditRepayOld.getCreditUserName());
																											sellerDebtCreditRepay.setRepayStatus(0);// 状态
																											sellerDebtCreditRepay.setBorrowNid(sellerDebtCreditRepayOld.getBorrowNid());// 原标标号
																											sellerDebtCreditRepay.setCreditNid(sellerDebtCreditRepayOld.getCreditNid());// 债转标号
																											sellerDebtCreditRepay.setInvestOrderId(debtCredit.getInvestOrderId());
																											sellerDebtCreditRepay.setSellOrderId(sellerDebtCreditRepayOld.getSellOrderId());// 认购单号
																											sellerDebtCreditRepay.setAssignPlanNid(sellerDebtCreditRepayOld.getAssignPlanNid());// 债转承接计划编号
																											sellerDebtCreditRepay.setAssignPlanOrderId(sellerDebtCreditRepayOld.getAssignPlanOrderId());// 债转承接订单号
																											sellerDebtCreditRepay.setAssignOrderId(sellerDebtCreditRepayOld.getAssignOrderId());// 债转投标单号
																											sellerDebtCreditRepay.setAdvanceStatus(debtDetailOld.getAdvanceStatus());
																											sellerDebtCreditRepay.setAdvanceDays(debtDetailOld.getAdvanceDays());
																											sellerDebtCreditRepay.setDelayDays(debtDetailOld.getDelayDays());
																											sellerDebtCreditRepay.setLateDays(debtDetailOld.getLateDays());
																											sellerDebtCreditRepay.setRepayAccount(sellerDebtCreditRepayOld.getRepayAccount().subtract(assignPeriodAccount));// 应还总额
																											sellerDebtCreditRepay.setRepayCapital(sellerDebtCreditRepayOld.getRepayCapital().subtract(assignPeriodCapital));// 应还本金
																											sellerDebtCreditRepay.setRepayInterest(sellerDebtCreditRepayOld.getRepayInterest().subtract(assignPeriodInterest));// 应还利息
																											sellerDebtCreditRepay.setRepayAdvanceInterest(BigDecimal.ZERO);
																											sellerDebtCreditRepay.setRepayDelayInterest(BigDecimal.ZERO);
																											sellerDebtCreditRepay.setRepayLateInterest(BigDecimal.ZERO);
																											sellerDebtCreditRepay.setRepayAccountWait(sellerDebtCreditRepayOld.getRepayAccountWait().subtract(assignPeriodAccount));// 应还总额
																											sellerDebtCreditRepay.setRepayCapitalWait(sellerDebtCreditRepayOld.getRepayCapital().subtract(assignPeriodCapital));// 应还本金
																											sellerDebtCreditRepay.setRepayInterestWait(sellerDebtCreditRepayOld.getRepayInterest().subtract(assignPeriodInterest));// 应还利息
																											sellerDebtCreditRepay.setRepayAccountYes(BigDecimal.ZERO);// 已还总额
																											sellerDebtCreditRepay.setRepayCapitalYes(BigDecimal.ZERO);// 已还本金
																											sellerDebtCreditRepay.setRepayInterestYes(BigDecimal.ZERO);// 已还利息
																											sellerDebtCreditRepay.setReceiveAccountYes(BigDecimal.ZERO);
																											sellerDebtCreditRepay.setReceiveCapitalYes(BigDecimal.ZERO);
																											sellerDebtCreditRepay.setReceiveInterestYes(BigDecimal.ZERO);
																											sellerDebtCreditRepay.setReceiveAdvanceInterest(BigDecimal.ZERO);
																											sellerDebtCreditRepay.setReceiveDelayInterest(sellerDebtCreditRepay.getReceiveDelayInterest().subtract(assignPeriodRepayDelayInterest));
																											sellerDebtCreditRepay.setReceiveLateInterest(sellerDebtCreditRepay.getReceiveLateInterest().subtract(assignPeriodRepayLateInterest));
																											sellerDebtCreditRepay.setAssignRepayEndTime(debtCreditTender.getAssignRepayEndTime());// 最后还款日
																											sellerDebtCreditRepay.setAssignRepayLastTime(debtCreditTender.getAssignRepayLastTime());// 上次还款时间
																											sellerDebtCreditRepay.setAssignRepayNextTime(debtCreditTender.getAssignRepayNextTime());// 下次还款时间
																											sellerDebtCreditRepay.setAssignRepayYesTime(0);// 最终实际还款时间
																											sellerDebtCreditRepay.setAssignRepayTime(debtDetailOld.getRepayTime());
																											sellerDebtCreditRepay.setAssignRepayPeriod(1);// 还款期数
																											sellerDebtCreditRepay.setAssignCreateDate(sellerDebtCreditRepayOld.getAssignCreateDate());// 认购日期
																											sellerDebtCreditRepay.setAddip(debtCreditTender.getAddip());// ip
																											sellerDebtCreditRepay.setClient(0);// 客户端
																											sellerDebtCreditRepay.setDelFlag(0);
																											sellerDebtCreditRepay.setRepayPeriod(1);// 原标还款期数
																											sellerDebtCreditRepay.setManageFee(BigDecimal.ZERO);// 管理费
																											if (i == 0) {
																												sellerDebtCreditRepay.setLiquidatesServiceFee(debtDetailOld.getServiceFee().add(serviceFee));// 清算服务费
																											} else {
																												sellerDebtCreditRepay.setLiquidatesServiceFee(BigDecimal.ZERO);// 清算服务费
																											}
																											sellerDebtCreditRepay.setUniqueNid(sellerDebtCreditRepayOld.getAssignOrderId() + "_" + creditOrderId + "_" + waitRepayPeriod);// 唯一nid
																											sellerDebtCreditRepay.setCreateTime(nowTime);// 添加时间
																											sellerDebtCreditRepay.setCreateUserId(sellerUserId);// 添加时间
																											sellerDebtCreditRepay.setCreateUserName(sellerUserName);// 添加时间
																											boolean sellerDebtCreditRepayFlag = debtCreditRepayMapper.insertSelective(sellerDebtCreditRepay) > 0 ? true : false;
																											if (!sellerDebtCreditRepayFlag) {
																												throw new Exception("出让人债转还款表debtcreditrepay插入失败,承接订单号" + debtDetailOld.getOrderId());
																											}
																										}
																									} else {
																										throw new Exception("出让人债转还款表debtcreditrepay更新为无效失败，加入订单号：" + liquidatesPlanOrderId);
																									}
																								}
																							} else {
																								// 更新相应的债转承接金额
																								DebtLoanExample debtLoanExample = new DebtLoanExample();
																								DebtLoanExample.Criteria debtLoanCrt = debtLoanExample.createCriteria();
																								debtLoanCrt.andInvestOrderIdEqualTo(debtCredit.getInvestOrderId());
																								List<DebtLoan> debtLoanList = this.debtLoanMapper.selectByExample(debtLoanExample);
																								if (debtLoanList != null && debtLoanList.size() == 1) {
																									DebtLoan debtLoan = debtLoanList.get(0);
																									debtLoan.setCreditAmount(debtLoan.getCreditAmount().add(assignPeriodCapital));
																									debtLoan.setCreditInterestAmount(debtLoan.getCreditInterestAmount().add(assignPeriodInterest));
																									boolean debtLoanFlag = this.debtLoanMapper.updateByPrimaryKeySelective(debtLoan) > 0 ? true : false;
																									if (!debtLoanFlag) {
																										throw new Exception("更新相应的出借订单的放款信息失败，出借订单号：" + debtCredit.getInvestOrderId());
																									}
																								} else {
																									throw new Exception("未查询到相应的出借订单的放款信息，出借订单号：" + debtCredit.getInvestOrderId());
																								}
																							}
																						} else {
																							throw new Exception("承接人债转还款表debtcreditrepay插入失败,承接订单号" + debtDetailOld.getOrderId());
																						}
																					} else {
																						throw new Exception("承接人债权详情表debtdetail插入失败，承接订单号：" + debtDetailOld.getOrderId());
																					}
																				}
																			} else {
																				throw new Exception("出让人债权详情表debtdetail老数据查询失败，加入订单号：" + planOrderId);
																			}
																		}
																		DebtPlanBorrowExample planBorrowExample = new DebtPlanBorrowExample();
																		DebtPlanBorrowExample.Criteria planBorrowCrt = planBorrowExample.createCriteria();
																		planBorrowCrt.andDebtPlanNidEqualTo(planNid);
																		planBorrowCrt.andBorrowNidEqualTo(creditNid);
																		planBorrowCrt.andTypeEqualTo(1);
																		List<DebtPlanBorrow> debtPlanBorrows = this.debtPlanBorrowMapper.selectByExample(planBorrowExample);
																		if (debtPlanBorrows != null && debtPlanBorrows.size() > 0) {
																			DebtPlanBorrow debtPlanBorrow = new DebtPlanBorrow();
																			debtPlanBorrow.setDelFlag(0);
																			debtPlanBorrow.setAddType(1);
																			debtPlanBorrow.setUpdateTime(nowTime);
																			debtPlanBorrow.setUpdateUserId(userId);
																			debtPlanBorrow.setUpdateUserName(userName);
																			boolean debtPlanBorrowFlag = this.debtPlanBorrowMapper.updateByExampleSelective(debtPlanBorrow, planBorrowExample) > 0 ? true : false;
																			if (!debtPlanBorrowFlag) {
																				throw new Exception("债权同计划关联表插入失败，计划编号：" + planNid + ",债转编号：" + creditNid);
																			}
																		} else {
																			DebtPlanBorrow debtPlanBorrow = new DebtPlanBorrow();
																			debtPlanBorrow.setDebtPlanNid(planNid);
																			debtPlanBorrow.setBorrowNid(creditNid);
																			debtPlanBorrow.setDelFlag(0);
																			debtPlanBorrow.setType(1);
																			debtPlanBorrow.setAddType(1);
																			debtPlanBorrow.setCreateTime(nowTime);
																			debtPlanBorrow.setCreateUserId(userId);
																			debtPlanBorrow.setCreateUserName(userName);
																			boolean debtPlanBorrowFlag = this.debtPlanBorrowMapper.insertSelective(debtPlanBorrow) > 0 ? true : false;
																			if (!debtPlanBorrowFlag) {
																				throw new Exception("债权同计划关联表插入失败，计划编号：" + planNid + ",债转编号：" + creditNid);
																			}
																		}
																		// 承接人用户详细信息
																		UsersInfoExample usersInfoExample = new UsersInfoExample();
																		UsersInfoExample.Criteria userInfoCra = usersInfoExample.createCriteria();
																		userInfoCra.andUserIdEqualTo(userId);
																		List<UsersInfo> usersInfoList = this.usersInfoMapper.selectByExample(usersInfoExample);
																		if (usersInfoList != null && usersInfoList.size() == 1) {
																			// 4.添加网站收支明细
																			AccountWebList accountWebList = new AccountWebList();
																			accountWebList.setOrdid(debtCreditTender.getAssignOrderId());
																			accountWebList.setBorrowNid(debtCreditTender.getBorrowNid());
																			accountWebList.setAmount(debtCreditTender.getAssignServiceFee());
																			accountWebList.setType(1);
																			accountWebList.setTrade("debt_plan_fee");
																			accountWebList.setTradeType("汇添金服务费");
																			accountWebList.setUserId(debtCreditTender.getUserId());
																			accountWebList.setUsrcustid(String.valueOf(accountChinapnrTender.getChinapnrUsrcustid()));
																			accountWebList.setTruename(usersInfoList != null ? usersInfoList.get(0).getTruename() : "");
																			accountWebList.setRegionName(null);
																			accountWebList.setBranchName(null);
																			accountWebList.setDepartmentName(null);
																			accountWebList.setRemark(debtCreditTender.getAssignOrderId());
																			accountWebList.setNote(null);
																			accountWebList.setCreateTime(nowTime);
																			accountWebList.setOperator(null);
																			accountWebList.setFlag(1);
																			// 设置部门信息
																			setDepartments(accountWebList);
																			// 插入
																			boolean accountWebListFlag = this.accountWebListMapper.insertSelective(accountWebList) > 0 ? true : false;
																			if (accountWebListFlag) {
																				return true;
																			} else {
																				throw new Exception("网站收支插入失败，用户userId" + userId + ",承接订单号：" + debtCreditTender.getAssignOrderId());
																			}
																		} else {
																			throw new Exception("未查询到相应的债权诚接人的userInfo信息，用户userId" + userId);
																		}
																	} else {
																		throw new Exception("汇添金交易明细表debtaccountlist出让人插入失败，用户id：" + sellerUserId);
																	}
																} else {
																	throw new Exception("汇添金出让人用户账户表account表出让人查询失败，用户id：" + sellerUserId);
																}
															} else {
																throw new Exception("汇添金用户账户表account表出让人更新失败，用户id：" + sellerUserId);
															}
														} else {
															throw new Exception("汇添金清算账户debtplanaccede表出让人更新失败，用户id：" + sellerUserId + ",计划订单号：" + sellerDebtPlanAccede.getAccedeOrderId());
														}
													} else {
														throw new Exception("更新出让计划debtplan信息失败，计划编号：" + liquidatesPlanNid);
													}
												} else {
													throw new Exception("汇添金交易明细表debtaccountlist承接人插入失败，用户id：" + userId);
												}
											} else {
												throw new Exception("查询承接用户account表失败，用户userId：" + userId);
											}
										} else {
											throw new Exception("汇添金用户账户表account表承接人更新失败" + userId);
										}
									} else {
										throw new Exception("更新承接计划debtplan信息失败，计划编号：" + planNid);
									}
								} else {
									throw new Exception("汇添金计划加入表debtplanaccede更新失败，加入订单号：" + assignDebtPlanAccede.getAccedeOrderId());
								}
							} else {
								throw new Exception("汇添金债权承接表debtcredittender表插入失败，债权编号：" + debtCredit.getCreditNid());
							}
						} else {
							throw new Exception("汇添金债权承接数据校验错误：" + JSONObject.toJSONString(bean));
						}
					} else {
						throw new Exception("汇添金债权承接log表debtcredittenderlog表更新失败，承接订单号编号：" + debtCreditTenderLog.getAssignOrderId());
					}
				} else {
					throw new Exception("汇添金债权承接log表debtcredittenderlog表查询失败，债权编号：" + debtCredit.getCreditNid());
				}
			} else {
				throw new Exception("汇添金债权承接表debtcredittender表数据已经存在，承接订单号：" + creditOrderId);
			}
		} else {
			throw new Exception("未查询到相应的标的信息，标号：" + borrowNid);
		}
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
	 * 根据计划nid，计划加入订单号，查询相应的冻结记录
	 * 
	 * @param planNid
	 * @param planOrderId
	 * @return
	 * @author Administrator
	 */
	public DebtFreeze selectSellerDebtFreeze(String planNid, String planOrderId) {

		DebtFreezeExample example = new DebtFreezeExample();
		DebtFreezeExample.Criteria crt = example.createCriteria();
		crt.andPlanNidEqualTo(planNid);
		crt.andPlanOrderIdEqualTo(planOrderId);
		crt.andDelFlagEqualTo(0);
		crt.andFreezeTypeEqualTo(2);
		crt.andStatusEqualTo(0);
		List<DebtFreeze> debtFreezes = this.debtFreezeMapper.selectByExample(example);
		if (debtFreezes != null && debtFreezes.size() > 0) {
			return debtFreezes.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 冻结用户的余额
	 * 
	 * @param userId
	 * @param tenderUsrcustid
	 * @param account
	 * @param orderId
	 * @param orderDate
	 * @return
	 */
	public String freezeOrder(int userId, String tenderUsrcustid, BigDecimal account, String orderId, String orderDate) {

		for (int i = 0; i < 3; i++) {
			try {
				/** 冻结订单 */
				ChinapnrBean freezeBean = usrFreeze(userId, tenderUsrcustid, account, orderId, orderDate);
				if (Validator.isNotNull(freezeBean)) {
					String respCode = freezeBean.getRespCode();
					if (StringUtils.isNotBlank(respCode) && respCode.equals(ChinaPnrConstant.RESPCODE_SUCCESS)) {
						System.out.println("用户:" + userId + "冻结订单*******************************冻结标识：" + freezeBean.getTrxId());
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
								LogUtil.errorLog(this.getClass().getName(), "unFreezeOrder", "调用交易查询接口(冻结)失败。" + message + ",[冻结订单号：" + orderId + "]", null);
								throw new Exception("调用交易查询接口(冻结)失败。" + queryRespCode + "：" + message + ",[冻结订单号：" + orderId + "]");
							} else {
								// 汇付交易状态
								String transStat = queryTransStatBean == null ? "" : queryTransStatBean.getTransStat();
								// 冻结请求已经被解冻 U:已解冻 N:无需解冻，对于解冻交易F:冻结
								if (!"F".equals(transStat)) {
									System.out.println("用户:" + userId + "***********************************冻结失败错误码：" + respCode);
									return null;
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

	private ChinapnrBean usrFreeze(int userId, String tenderUsrcustid, BigDecimal account, String orderId, String orderDate) {

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
	 * 根据userId获取用户账户信息
	 * 
	 * @param userId
	 * @return
	 */

	private Account selectUserAccount(Integer userId) {

		AccountExample example = new AccountExample();
		AccountExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		List<Account> accountList = this.accountMapper.selectByExample(example);
		if (accountList != null && accountList.size() == 1) {
			return accountList.get(0);
		}
		return null;

	}

	/**
	 * 更新计划订单为完成
	 * 
	 * @param debtPlanAccede
	 * @return
	 * @author Administrator
	 */

	@Override
	public boolean updateDebtPlanAccedeFinish(DebtPlanAccede debtPlanAccede) {

		debtPlanAccede = this.debtPlanAccedeMapper.selectByPrimaryKey(debtPlanAccede.getId());
		debtPlanAccede.setStatus(1);
		debtPlanAccede.setReinvestStatus(0);
		boolean flag = this.debtPlanAccedeMapper.updateByPrimaryKey(debtPlanAccede) > 0 ? true : false;
		return flag;
	}

	/**
	 * 更新此笔债权承接为失败
	 * 
	 * @param debtCredit
	 * @param userId
	 * @param creditOrderId
	 * @return
	 */

	private boolean updateDebtCreditTenderLog(DebtCredit debtCredit, int userId, String creditOrderId) {
		String creditNid = debtCredit.getCreditNid();
		String sellerOrderId = debtCredit.getSellOrderId();
		DebtCreditTenderLogExample debtCreditTenderLogExample = new DebtCreditTenderLogExample();
		DebtCreditTenderLogExample.Criteria crt = debtCreditTenderLogExample.createCriteria();
		crt.andCreditNidEqualTo(creditNid);
		crt.andSellOrderIdEqualTo(sellerOrderId);
		crt.andAssignOrderIdEqualTo(creditOrderId);
		crt.andUserIdEqualTo(userId);
		DebtCreditTenderLog debtCreditTenderLog = new DebtCreditTenderLog();
		debtCreditTenderLog.setStatus(2);
		boolean debtCredtTenderLogFlag = this.debtCreditTenderLogMapper.updateByExampleSelective(debtCreditTenderLog, debtCreditTenderLogExample) > 0 ? true : false;
		return debtCredtTenderLogFlag;
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

	@Override
	public DebtCredit selectCreditListByCreditNid(String creditNid) {
		DebtCreditExample example = new DebtCreditExample();
		DebtCreditExample.Criteria cra = example.createCriteria();
		cra.andCreditNidEqualTo(creditNid).andCreditStatusEqualTo(0);
		List<DebtCredit> list = this.debtCreditMapper.selectByExample(example);
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

	private BigDecimal calculateServiceFee(DebtCredit debtCredit, BigDecimal account) throws Exception {

		// 清算出的用户id
		int userId = debtCredit.getUserId();
		// 清算出的计划编号
		String liquidatesPlanNid = debtCredit.getPlanNid();
		// 清算出的债权的加入订单号
		String liquidatesAccedeOrderId = debtCredit.getPlanOrderId();
		DebtPlanAccede liquidatesDebtPlanAccede = this.selectDebtPlanAccede(userId, liquidatesPlanNid, liquidatesAccedeOrderId);
		if (Validator.isNotNull(liquidatesDebtPlanAccede)) {
			BigDecimal serviceFeeRate = liquidatesDebtPlanAccede.getServiceFeeRate();
			BigDecimal serviceFee = account.multiply(serviceFeeRate).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);
			return serviceFee;
		} else {
			throw new Exception("未查询到相应的清算的债权的用户的加入记录,用户id：" + userId + "计划编号：" + liquidatesPlanNid + "计划加入订单号：" + liquidatesAccedeOrderId);
		}
	}

	/**
	 * 查询相应的清算数据的原始还款数据
	 * 
	 * @param liquidatesPlanNid
	 * @param liquidatesPlanOrderId
	 * @param sellerOrderId
	 * @param waitRepayPeriod
	 * @return
	 * @throws Exception
	 */
	private DebtCreditRepay selectDebtCreditRepay(String liquidatesPlanNid, String liquidatesPlanOrderId, String sellerOrderId, int waitRepayPeriod) throws Exception {

		DebtCreditRepayExample example = new DebtCreditRepayExample();
		DebtCreditRepayExample.Criteria crt = example.createCriteria();
		crt.andAssignPlanNidEqualTo(liquidatesPlanNid);
		crt.andAssignPlanOrderIdEqualTo(liquidatesPlanOrderId);
		crt.andAssignOrderIdEqualTo(sellerOrderId);
		crt.andRepayPeriodEqualTo(waitRepayPeriod);
		crt.andDelFlagEqualTo(0);
		List<DebtCreditRepay> debtCreditRepays = this.debtCreditRepayMapper.selectByExample(example);
		if (debtCreditRepays != null && debtCreditRepays.size() == 1) {
			return debtCreditRepays.get(0);
		} else {
			throw new Exception("未查询到相应的债转还款记录" + "承接订单号：" + sellerOrderId + ",还款期数：" + waitRepayPeriod);
		}
	}

	/**
	 * 计算总天数
	 * 
	 * @param borrowNid
	 * @param liquidatesPeriod
	 * @param repayStyle
	 * @return
	 * @throws Exception
	 */
	private BigDecimal calculateDaysByRepay(String borrowNid, int liquidatesPeriod, String repayStyle) throws Exception {
		BigDecimal totalDays = BigDecimal.ZERO;
		// 按天
		if (repayStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
			DebtBorrow debtBorrow = this.selectDebtBorrowByNid(borrowNid);
			if (Validator.isNotNull(debtBorrow)) {
				int recoverLastTime = debtBorrow.getRecoverLastTime();
				DebtRepay debtRepay = this.selectDebtRepay(borrowNid, liquidatesPeriod);
				if (Validator.isNotNull(debtRepay)) {
					int repayTime = Integer.parseInt(debtRepay.getRepayTime());
					// 用户放款时间
					String recoverLastTimeStr = GetDate.getDateTimeMyTimeInMillis(recoverLastTime);
					// 用户实际还款时间
					String repayTimeStr = GetDate.getDateTimeMyTimeInMillis(repayTime);
					try {
						totalDays = new BigDecimal(GetDate.daysBetween(recoverLastTimeStr, repayTimeStr));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					throw new Exception("未查询到debtRepayDetail的还款信息,项目编号：" + borrowNid + "还款期数：" + liquidatesPeriod);
				}
			} else {
				throw new Exception("未查询到debtBorrow的项目信息,项目编号：" + borrowNid);
			}
		}
		// 按月
		else if (repayStyle.equals(CalculatesUtil.STYLE_END)) {
			if (liquidatesPeriod == 1) {
				DebtBorrow debtBorrow = this.selectDebtBorrowByNid(borrowNid);
				if (Validator.isNotNull(debtBorrow)) {
					int recoverLastTime = debtBorrow.getRecoverLastTime();
					DebtRepay debtRepay = this.selectDebtRepay(borrowNid, liquidatesPeriod);
					if (Validator.isNotNull(debtRepay)) {
						int repayTime = Integer.parseInt(debtRepay.getRepayTime());
						// 用户放款时间
						String recoverLastTimeStr = GetDate.getDateTimeMyTimeInMillis(recoverLastTime);
						// 用户实际还款时间
						String repayTimeStr = GetDate.getDateTimeMyTimeInMillis(repayTime);
						try {
							totalDays = new BigDecimal(GetDate.daysBetween(recoverLastTimeStr, repayTimeStr));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						throw new Exception("未查询到debtRepayDetail的还款信息,项目编号：" + borrowNid + "还款期数：" + liquidatesPeriod);
					}
				} else {
					throw new Exception("未查询到debtBorrow的项目信息,项目编号：" + borrowNid);
				}
			} else {
				DebtRepayDetail debtRepayDetailLast = this.selectDebtRepayDetail(borrowNid, liquidatesPeriod - 1);
				if (Validator.isNotNull(debtRepayDetailLast)) {
					int repayLastTime = Integer.parseInt(debtRepayDetailLast.getRepayTime());
					DebtRepayDetail debtRepayDetail = this.selectDebtRepayDetail(borrowNid, liquidatesPeriod);
					if (Validator.isNotNull(debtRepayDetailLast)) {
						int repayTime = Integer.parseInt(debtRepayDetail.getRepayTime());
						// 用户放款时间
						String recoverLastTimeStr = GetDate.getDateTimeMyTimeInMillis(repayLastTime);
						// 用户实际还款时间
						String repayTimeStr = GetDate.getDateTimeMyTimeInMillis(repayTime);
						try {
							totalDays = new BigDecimal(GetDate.daysBetween(recoverLastTimeStr, repayTimeStr));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						throw new Exception("未查询到debtRepayDetail的还款信息,项目编号：" + borrowNid + "还款期数：" + liquidatesPeriod);
					}
				} else {
					throw new Exception("未查询到debtRepayDetail的还款信息,项目编号：" + borrowNid + ",还款期数：" + (liquidatesPeriod - 1));
				}
			}
		}
		// 先息后本
		else if (repayStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
			if (liquidatesPeriod == 1) {
				DebtBorrow debtBorrow = this.selectDebtBorrowByNid(borrowNid);
				if (Validator.isNotNull(debtBorrow)) {
					int recoverLastTime = debtBorrow.getRecoverLastTime();
					DebtRepayDetail debtRepayDetail = this.selectDebtRepayDetail(borrowNid, liquidatesPeriod);
					if (Validator.isNotNull(debtRepayDetail)) {
						int repayTime = Integer.parseInt(debtRepayDetail.getRepayTime());
						// 用户放款时间
						String recoverLastTimeStr = GetDate.getDateTimeMyTimeInMillis(recoverLastTime);
						// 用户实际还款时间
						String repayTimeStr = GetDate.getDateTimeMyTimeInMillis(repayTime);
						try {
							totalDays = new BigDecimal(GetDate.daysBetween(recoverLastTimeStr, repayTimeStr));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						throw new Exception("未查询到debtRepayDetail的还款信息,项目编号：" + borrowNid + "还款期数：" + liquidatesPeriod);
					}
				} else {
					throw new Exception("未查询到debtBorrow的项目信息,项目编号：" + borrowNid);
				}

			} else {
				DebtRepayDetail debtRepayDetailLast = this.selectDebtRepayDetail(borrowNid, liquidatesPeriod - 1);
				if (Validator.isNotNull(debtRepayDetailLast)) {
					int repayLastTime = Integer.parseInt(debtRepayDetailLast.getRepayTime());
					DebtRepayDetail debtRepayDetail = this.selectDebtRepayDetail(borrowNid, liquidatesPeriod);
					if (Validator.isNotNull(debtRepayDetailLast)) {
						int repayTime = Integer.parseInt(debtRepayDetail.getRepayTime());
						// 用户放款时间
						String recoverLastTimeStr = GetDate.getDateTimeMyTimeInMillis(repayLastTime);
						// 用户实际还款时间
						String repayTimeStr = GetDate.getDateTimeMyTimeInMillis(repayTime);
						try {
							totalDays = new BigDecimal(GetDate.daysBetween(recoverLastTimeStr, repayTimeStr));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						throw new Exception("未查询到debtRepayDetail的还款信息,项目编号：" + borrowNid + "还款期数：" + liquidatesPeriod);
					}
				} else {
					throw new Exception("未查询到debtRepayDetail的还款信息,项目编号：" + borrowNid + ",还款期数：" + (liquidatesPeriod - 1));
				}
			}

		}
		// 等额本金
		else if (repayStyle.equals(CalculatesUtil.STYLE_PRINCIPAL)) {

			if (liquidatesPeriod == 1) {
				DebtBorrow debtBorrow = this.selectDebtBorrowByNid(borrowNid);
				if (Validator.isNotNull(debtBorrow)) {
					int recoverLastTime = debtBorrow.getRecoverLastTime();
					DebtRepayDetail debtRepayDetail = this.selectDebtRepayDetail(borrowNid, liquidatesPeriod);
					if (Validator.isNotNull(debtRepayDetail)) {
						int repayTime = Integer.parseInt(debtRepayDetail.getRepayTime());
						// 用户放款时间
						String recoverLastTimeStr = GetDate.getDateTimeMyTimeInMillis(recoverLastTime);
						// 用户实际还款时间
						String repayTimeStr = GetDate.getDateTimeMyTimeInMillis(repayTime);
						try {
							totalDays = new BigDecimal(GetDate.daysBetween(recoverLastTimeStr, repayTimeStr));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						throw new Exception("未查询到debtRepayDetail的还款信息,项目编号：" + borrowNid + "还款期数：" + liquidatesPeriod);
					}
				} else {
					throw new Exception("未查询到debtBorrow的项目信息,项目编号：" + borrowNid);
				}

			} else {
				DebtRepayDetail debtRepayDetailLast = this.selectDebtRepayDetail(borrowNid, liquidatesPeriod - 1);
				if (Validator.isNotNull(debtRepayDetailLast)) {
					int repayLastTime = Integer.parseInt(debtRepayDetailLast.getRepayTime());
					DebtRepayDetail debtRepayDetail = this.selectDebtRepayDetail(borrowNid, liquidatesPeriod);
					if (Validator.isNotNull(debtRepayDetailLast)) {
						int repayTime = Integer.parseInt(debtRepayDetail.getRepayTime());
						// 用户放款时间
						String recoverLastTimeStr = GetDate.getDateTimeMyTimeInMillis(repayLastTime);
						// 用户实际还款时间
						String repayTimeStr = GetDate.getDateTimeMyTimeInMillis(repayTime);
						try {
							totalDays = new BigDecimal(GetDate.daysBetween(recoverLastTimeStr, repayTimeStr));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						throw new Exception("未查询到debtRepayDetail的还款信息,项目编号：" + borrowNid + "还款期数：" + liquidatesPeriod);
					}
				} else {
					throw new Exception("未查询到debtRepayDetail的还款信息,项目编号：" + borrowNid + ",还款期数：" + (liquidatesPeriod - 1));
				}
			}

		}
		// 等额本息
		else if (repayStyle.equals(CalculatesUtil.STYLE_MONTH)) {

			if (liquidatesPeriod == 1) {
				DebtBorrow debtBorrow = this.selectDebtBorrowByNid(borrowNid);
				if (Validator.isNotNull(debtBorrow)) {
					int recoverLastTime = debtBorrow.getRecoverLastTime();
					DebtRepayDetail debtRepayDetail = this.selectDebtRepayDetail(borrowNid, liquidatesPeriod);
					if (Validator.isNotNull(debtRepayDetail)) {
						int repayTime = Integer.parseInt(debtRepayDetail.getRepayTime());
						// 用户放款时间
						String recoverLastTimeStr = GetDate.getDateTimeMyTimeInMillis(recoverLastTime);
						// 用户实际还款时间
						String repayTimeStr = GetDate.getDateTimeMyTimeInMillis(repayTime);
						try {
							totalDays = new BigDecimal(GetDate.daysBetween(recoverLastTimeStr, repayTimeStr));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						throw new Exception("未查询到debtRepayDetail的还款信息,项目编号：" + borrowNid + "还款期数：" + liquidatesPeriod);
					}
				} else {
					throw new Exception("未查询到debtBorrow的项目信息,项目编号：" + borrowNid);
				}

			} else {
				DebtRepayDetail debtRepayDetailLast = this.selectDebtRepayDetail(borrowNid, liquidatesPeriod - 1);
				if (Validator.isNotNull(debtRepayDetailLast)) {
					int repayLastTime = Integer.parseInt(debtRepayDetailLast.getRepayTime());
					DebtRepayDetail debtRepayDetail = this.selectDebtRepayDetail(borrowNid, liquidatesPeriod);
					if (Validator.isNotNull(debtRepayDetailLast)) {
						int repayTime = Integer.parseInt(debtRepayDetail.getRepayTime());
						// 用户放款时间
						String recoverLastTimeStr = GetDate.getDateTimeMyTimeInMillis(repayLastTime);
						// 用户实际还款时间
						String repayTimeStr = GetDate.getDateTimeMyTimeInMillis(repayTime);
						try {
							totalDays = new BigDecimal(GetDate.daysBetween(recoverLastTimeStr, repayTimeStr));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						throw new Exception("未查询到debtRepayDetail的还款信息,项目编号：" + borrowNid + "还款期数：" + liquidatesPeriod);
					}
				} else {
					throw new Exception("未查询到debtRepayDetail的还款信息,项目编号：" + borrowNid + ",还款期数：" + (liquidatesPeriod - 1));
				}
			}

		}
		return totalDays;
	}

	/**
	 * 查询还款总数据
	 * 
	 * @param borrowNid
	 * @param liquidatesPeriod
	 * @return
	 */
	private DebtRepay selectDebtRepay(String borrowNid, int liquidatesPeriod) {
		DebtRepayExample example = new DebtRepayExample();
		DebtRepayExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid).andRepayPeriodLessThanOrEqualTo(liquidatesPeriod);
		List<DebtRepay> debtRepays = this.debtRepayMapper.selectByExample(example);
		if (debtRepays != null && debtRepays.size() == 1) {
			DebtRepay debtRepay = debtRepays.get(0);
			return debtRepay;
		}
		return null;
	}

	/**
	 * 查询相应的还款期数所在的还款分期数据
	 * 
	 * @param borrowNid
	 * @param liquidatesPeriod
	 * @return
	 */
	private DebtRepayDetail selectDebtRepayDetail(String borrowNid, int liquidatesPeriod) {

		DebtRepayDetailExample example = new DebtRepayDetailExample();
		DebtRepayDetailExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid).andRepayPeriodEqualTo(liquidatesPeriod);
		List<DebtRepayDetail> debtRepayDetails = this.debtRepayDetailMapper.selectByExample(example);
		if (debtRepayDetails != null && debtRepayDetails.size() == 1) {
			DebtRepayDetail debtRepayDetail = debtRepayDetails.get(0);
			return debtRepayDetail;
		}
		return null;
	}

}

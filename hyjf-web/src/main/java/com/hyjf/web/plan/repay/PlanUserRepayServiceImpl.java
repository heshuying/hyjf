/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 * 
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\ = /O
 * ____/`---'\____
 * .' \\| |// `.
 * / \\||| : |||// \
 * / _||||| -:- |||||- \
 * | | \\\ - /// | |
 * | \_| ''\---/'' | |
 * \ .-\__ `-` ___/-. /
 * ___`. .' /--.--\ `. . __
 * ."" '< `.___\_<|>_/___.' >'"".
 * | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * \ \ `-. \_ __\ /__ _/ .-` / /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 佛祖保佑 永无BUG
 */

package com.hyjf.web.plan.repay;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.calculate.UnnormalRepayUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.DebtAccountListExample;
import com.hyjf.mybatis.model.auto.DebtApicron;
import com.hyjf.mybatis.model.auto.DebtApicronExample;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtLoanDetail;
import com.hyjf.mybatis.model.auto.DebtLoanDetailExample;
import com.hyjf.mybatis.model.auto.DebtLoanExample;
import com.hyjf.mybatis.model.auto.DebtRepay;
import com.hyjf.mybatis.model.auto.DebtRepayDetail;
import com.hyjf.mybatis.model.auto.DebtRepayDetailExample;
import com.hyjf.mybatis.model.auto.DebtRepayExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseServiceImpl;
import com.hyjf.web.user.repay.UserRepayBean;
import com.hyjf.web.user.repay.UserRepayDetailBean;
import com.hyjf.web.user.repay.UserRepayProjectBean;

/**
 * 计划专属标的借款用户还款接口实现类
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年10月10日
 * @see 上午10:47:03
 */
@Service
public class PlanUserRepayServiceImpl extends BaseServiceImpl implements PlanUserRepayService {

	@Override
	public DebtRepayByTermBean calculateRepay(Integer userId, DebtBorrow borrow) throws ParseException {
		DebtRepayByTermBean repay = new DebtRepayByTermBean();
		// 获取还款总表数据
		DebtRepay debtRepay = this.searchDebtRepay(userId, borrow.getBorrowNid());
		// 判断是否存在还款数据
		if (debtRepay != null) {
			// 获取相应的还款信息
			BeanUtils.copyProperties(debtRepay, repay);
			repay.setBorrowPeriod(String.valueOf(borrow.getBorrowPeriod()));
			if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle())) {// 单期还款
				// 计划还款时间
				String repayTimeStr = debtRepay.getRepayTime();
				// 获取用户申请的延期天数
				int delayDays = debtRepay.getDelayDays().intValue();
				// 未分期默认传分期为0
				this.calculateRecover(repay, borrow, repayTimeStr, delayDays);
			} else {// 分期还款 TODO
				int period = repay.getRepayPeriod();
				this.calculateRepayPlan(repay, borrow, period);
			}
		}
		return repay;
	}

	/**
	 * 
	 * 计算分期用户的还款信息
	 * 
	 * @author renxingchen
	 * @param repay
	 * @param borrow
	 * @param borrow
	 * @param period
	 * @throws ParseException
	 */
	private void calculateRepayPlan(DebtRepayByTermBean repay, DebtBorrow borrow, int period) throws ParseException {
		List<DebtRepayDetailLoanDetailBean> debtRepayDetails = new ArrayList<DebtRepayDetailLoanDetailBean>();
		List<DebtRepayDetail> repayDetails = searchDebtRepayDetail(repay.getUserId(), borrow.getBorrowNid());
		BigDecimal repayAccountAll = new BigDecimal("0");
		if (repayDetails != null && repayDetails.size() > 0) {
			DebtRepayDetailLoanDetailBean debtRepayDetailLoanDetailBean;
			String repayTimeStart = null;
			// 用户实际还款额
			for (int i = 0; i < repayDetails.size(); i++) {
				debtRepayDetailLoanDetailBean = new DebtRepayDetailLoanDetailBean();
				DebtRepayDetail debtRepayDetail = repayDetails.get(i);
				if (period == debtRepayDetail.getRepayPeriod()) {
					if (i == 0) {
						repayTimeStart = debtRepayDetail.getCreateTime().toString();
					} else {
						repayTimeStart = repayDetails.get(i - 1).getRepayTime();
					}
					// 计算还款期的数据
					BeanUtils.copyProperties(debtRepayDetail, debtRepayDetailLoanDetailBean);
					this.calculateLoanDetail(debtRepayDetailLoanDetailBean, borrow, period, repayTimeStart);
					debtRepayDetails.add(debtRepayDetailLoanDetailBean);
					repay.setRepayAccount(debtRepayDetailLoanDetailBean.getRepayAccount());
					repay.setRepayAccountAll(debtRepayDetailLoanDetailBean.getRepayAccountAll());
					repay.setRepayInterest(debtRepayDetailLoanDetailBean.getRepayInterest());
					repay.setRepayCapital(debtRepayDetailLoanDetailBean.getRepayCapital());
					repay.setManageFee(debtRepayDetailLoanDetailBean.getManageFee());
					repay.setAdvanceDays(debtRepayDetailLoanDetailBean.getAdvanceDays());
					repay.setAdvanceInterest(debtRepayDetailLoanDetailBean.getAdvanceInterest());
					repay.setDelayDays(debtRepayDetailLoanDetailBean.getDelayDays());
					repay.setDelayInterest(debtRepayDetailLoanDetailBean.getDelayInterest());
					repay.setLateDays(debtRepayDetailLoanDetailBean.getLateDays());
					repay.setLateInterest(debtRepayDetailLoanDetailBean.getLateInterest());
					repayAccountAll = debtRepayDetailLoanDetailBean.getRepayAccountAll().add(debtRepayDetailLoanDetailBean.getManageFee());
				} else {
					debtRepayDetail.setRepayAccountAll(debtRepayDetail.getRepayAccount());
					BeanUtils.copyProperties(debtRepayDetail, debtRepayDetailLoanDetailBean);
					List<DebtLoanDetail> debtLoanDetails = this.searchDebtLoanDetail(borrow.getBorrowNid(), debtRepayDetail.getRepayPeriod());
					debtRepayDetailLoanDetailBean.setLoanDetailList(debtLoanDetails);
					debtRepayDetails.add(debtRepayDetailLoanDetailBean);
				}

			}
			repay.setRepayDetailList(debtRepayDetails);
			repay.setRepayTotal(repayAccountAll);
		}

	}

	/**
	 * 
	 * 计算用户分期还款本期应还本金
	 * 
	 * @author renxingchen
	 * @param debtRepayDetailLoanDetailBean
	 * @param borrow
	 * @param borrow
	 * @param period
	 * @param repayTimeStart
	 * @throws ParseException
	 */
	private void calculateLoanDetail(DebtRepayDetailLoanDetailBean debtRepayDetailLoanDetailBean, DebtBorrow borrow, int period, String repayTimeStart) throws ParseException {
		int delayDays = debtRepayDetailLoanDetailBean.getDelayDays().intValue();
		String repayTimeStr = debtRepayDetailLoanDetailBean.getRepayTime();
		int time = GetDate.getNowTime10();
		// 用户计划还款时间
		String repayTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(repayTimeStr));
		// 用户实际还款时间
		String factRepayTime = GetDate.getDateTimeMyTimeInMillis(time);
		// 获取实际还款同计划还款时间的时间差
		int distanceDays = GetDate.daysBetween(factRepayTime, repayTime);
		if (distanceDays < 0) {// 用户延期或者逾期了
			int lateDays = delayDays + distanceDays;
			if (lateDays >= 0) {// 用户延期还款
				delayDays = -distanceDays;
				this.calculateLoanDetailDelay(debtRepayDetailLoanDetailBean, borrow, delayDays);
			} else {// 用户逾期还款
				lateDays = -lateDays;
				this.calculateLoanDetailLate(debtRepayDetailLoanDetailBean, borrow, delayDays, lateDays);
			}
		} else {// 用户正常或者提前还款
				// 获取提前还款的阀值
			String repayAdvanceDay = this.getBorrowConfig("REPAY_ADVANCE_TIME");
			int advanceDays = distanceDays;
			if (Integer.parseInt(repayAdvanceDay) < advanceDays) {// 用户提前还款
				// 计算用户实际还款总额
				this.calculateLoanDetailAdvance(debtRepayDetailLoanDetailBean, borrow, advanceDays, repayTimeStart);
			} else {// 用户正常还款
					// 计算用户实际还款总额
				this.calculateLoanDetail(debtRepayDetailLoanDetailBean, borrow, advanceDays);
			}
		}

	}

	/**
	 * 
	 * 计算分期还款延期还款状态下本期用户应还的金额
	 * 
	 * @author renxingchen
	 * @param debtRepayDetailLoanDetailBean
	 * @param borrow
	 * @param borrow
	 * @param delayDays
	 */
	private void calculateLoanDetailDelay(DebtRepayDetailLoanDetailBean debtRepayDetailLoanDetailBean, DebtBorrow borrow, int delayDays) {
		List<DebtLoan> debtLoans = this.selectDebtLoanList(borrow.getBorrowNid());
		List<DebtLoanDetail> debtLoanDetails = this.searchDebtLoanDetail(borrow.getBorrowNid(), debtRepayDetailLoanDetailBean.getRepayPeriod());
		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		if (debtLoans != null && debtLoans.size() > 0) {
			if (debtLoanDetails != null && debtLoanDetails.size() > 0) {
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = new BigDecimal(0);
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = new BigDecimal(0);
				for (DebtLoan debtLoan : debtLoans) {
					for (DebtLoanDetail debtLoanDetail : debtLoanDetails) {
						if (debtLoan.getInvestOrderId().equals(debtLoanDetail.getInvestOrderId()) && debtLoan.getUserId().intValue() == debtLoanDetail.getUserId().intValue() && debtLoan.getBorrowNid().equals(debtLoanDetail.getBorrowNid())) {
							userAccount = debtLoanDetail.getLoanAccount();
							userCapital = debtLoanDetail.getLoanCapital();
							// 计算用户实际获得的本息和
							BigDecimal userAccountFact = UnnormalRepayUtils.delayRepayPrincipalInterest(userAccount, userCapital, borrow.getBorrowApr(), delayDays);
							// 计算用户延期利息
							BigDecimal userDelayInterest = UnnormalRepayUtils.delayRepayInterest(userCapital, borrow.getBorrowApr(), delayDays);
							debtLoanDetail.setRepayDelayInterest(userDelayInterest);
							// 统计总和
							userAccountTotal = userAccountTotal.add(userAccountFact);
							userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
							debtLoanDetail.setDelayDays(delayDays);
							debtLoanDetail.setAdvanceStatus(2);
						}
					}
				}
				debtRepayDetailLoanDetailBean.setLoanDetailList(debtLoanDetails);
			}
		}
		debtRepayDetailLoanDetailBean.setRepayAccountAll(userAccountTotal);
		debtRepayDetailLoanDetailBean.setRepayAccount(userAccountTotal);
		debtRepayDetailLoanDetailBean.setDelayDays(delayDays);
		debtRepayDetailLoanDetailBean.setDelayInterest(userDelayInterestTotal);
		debtRepayDetailLoanDetailBean.setAdvanceStatus(2);

	}

	/**
	 * 
	 * 计算分期还款逾期还款状态下本期用户应还的金额
	 * 
	 * @author renxingchen
	 * @param debtRepayDetailLoanDetailBean
	 * @param borrow
	 * @param borrow
	 * @param delayDays
	 * @param lateDays
	 */
	private void calculateLoanDetailLate(DebtRepayDetailLoanDetailBean debtRepayDetailLoanDetailBean, DebtBorrow borrow, int delayDays, int lateDays) {
		List<DebtLoan> debtLoans = this.selectDebtLoanList(borrow.getBorrowNid());
		List<DebtLoanDetail> debtLoanDetails = this.searchDebtLoanDetail(borrow.getBorrowNid(), debtRepayDetailLoanDetailBean.getRepayPeriod());
		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		// 统计借款用户总逾期利息
		BigDecimal userOverdueInterestTotal = new BigDecimal(0);
		if (debtLoans != null && debtLoans.size() > 0) {
			if (debtLoanDetails != null && debtLoanDetails.size() > 0) {
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = new BigDecimal(0);
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = new BigDecimal(0);
				for (DebtLoan debtLoan : debtLoans) {
					for (DebtLoanDetail debtLoanDetail : debtLoanDetails) {
						if (debtLoan.getInvestOrderId().equals(debtLoanDetail.getInvestOrderId()) && debtLoan.getUserId().intValue() == debtLoanDetail.getUserId().intValue() && debtLoan.getBorrowNid().equals(debtLoanDetail.getBorrowNid())) {
							userAccount = debtLoanDetail.getLoanAccount();
							userCapital = debtLoanDetail.getLoanCapital();
							// 计算用户实际获得的本息和
							BigDecimal userAccountFact = UnnormalRepayUtils.overdueRepayPrincipalInterest(userAccount, userCapital, borrow.getBorrowApr(), delayDays, lateDays);
							// 计算用户逾期利息
							BigDecimal userOverdueInterest = UnnormalRepayUtils.overdueRepayOverdueInterest(userAccount, lateDays);
							// 计算用户延期利息
							BigDecimal userDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(userCapital, borrow.getBorrowApr(), delayDays);
							// 保存相应的延期数据
							debtLoanDetail.setRepayDelayInterest(userDelayInterest);
							debtLoanDetail.setRepayLateInterest(userOverdueInterest);
							// 统计总和
							userAccountTotal = userAccountTotal.add(userAccountFact);
							userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
							userOverdueInterestTotal = userOverdueInterestTotal.add(userOverdueInterest);
							debtLoanDetail.setDelayDays(delayDays);
							debtLoanDetail.setLateDays(lateDays);
							debtLoanDetail.setAdvanceStatus(3);
						}
					}
					debtRepayDetailLoanDetailBean.setLoanDetailList(debtLoanDetails);
				}
			}
		}
		debtRepayDetailLoanDetailBean.setRepayAccountAll(userAccountTotal);
		debtRepayDetailLoanDetailBean.setRepayAccount(userAccountTotal);
		debtRepayDetailLoanDetailBean.setDelayDays(delayDays);
		debtRepayDetailLoanDetailBean.setDelayInterest(userDelayInterestTotal);
		debtRepayDetailLoanDetailBean.setLateDays(lateDays);
		debtRepayDetailLoanDetailBean.setLateInterest(userOverdueInterestTotal);
		debtRepayDetailLoanDetailBean.setAdvanceStatus(3);
	}

	/**
	 * 
	 * 计算分期还款提前还款状态下本期用户应还的金额
	 * 
	 * @author renxingchen
	 * @param debtRepayDetailLoanDetailBean
	 * @param borrow
	 * @param borrow
	 * @param advanceDays
	 * @param repayTimeStart
	 * @throws ParseException
	 */
	private void calculateLoanDetailAdvance(DebtRepayDetailLoanDetailBean debtRepayDetailLoanDetailBean, DebtBorrow borrow, int advanceDays, String repayTimeStart) throws ParseException {
		int repayPeriod = debtRepayDetailLoanDetailBean.getRepayPeriod();
		List<DebtLoan> debtLoans = this.selectDebtLoanList(borrow.getBorrowNid());
		List<DebtLoanDetail> debtLoanDetails = this.searchDebtLoanDetail(borrow.getBorrowNid(), repayPeriod);
		// 用户实际还款额
		BigDecimal repayTotal = new BigDecimal(0);
		BigDecimal repayChargeInterest = new BigDecimal(0);
		if (debtLoans != null && debtLoans.size() > 0) {
			if (debtLoanDetails != null && debtLoanDetails.size() > 0) {
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = new BigDecimal(0);
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = new BigDecimal(0);
				// 计算用户实际获得的本息和
				BigDecimal userAccountFact = new BigDecimal(0);
				// 计算用户提前还款减少的的利息
				BigDecimal userChargeInterest = new BigDecimal(0);
				for (DebtLoan debtLoan : debtLoans) {
					for (DebtLoanDetail debtLoanDetail : debtLoanDetails) {
						if (debtLoan.getInvestId().equals(debtLoanDetail.getInvestId()) && debtLoan.getUserId().intValue() == debtLoanDetail.getUserId().intValue() && debtLoan.getBorrowNid().equals(debtLoanDetail.getBorrowNid())) {
							String recoverTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(debtLoanDetail.getRepayTime()));
							String repayStartTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(repayTimeStart));
							// 获取这两个时间之间有多少天
							int totalDays = GetDate.daysBetween(repayStartTime, recoverTime);
							userAccount = debtLoanDetail.getLoanAccount();
							userCapital = debtLoanDetail.getLoanCapital();
							BigDecimal userInterest = debtLoanDetail.getLoanInterest();
							if (debtLoan.getCreditStatus() == 0) {
								//TODO 判断是否为先息后本
								boolean isStyle = CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle());

								// 用户获得的利息
								// 提前还款不应该大于本次计息时间
								if (totalDays < advanceDays) {
									// 计算出借用户实际获得的本息和
									userAccountFact = UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount, userCapital, borrow.getBorrowApr(), totalDays);
									userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrow.getBorrowApr(), totalDays);
								} else {
									// 计算出借用户实际获得的本息和
									userAccountFact = UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount, userCapital, borrow.getBorrowApr(), advanceDays);
									userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrow.getBorrowApr(), advanceDays);
								}
								if(isStyle){
									if(advanceDays >= 30){
										userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,totalDays);
									}else{
										userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,advanceDays);
									}
								}
								debtLoanDetail.setRepayAdvanceInterest(userChargeInterest.multiply(new BigDecimal(-1)));
								repayTotal = repayTotal.add(userAccountFact);
								repayChargeInterest = repayChargeInterest.add(userChargeInterest);
							} else {
								debtLoanDetail.setRepayAdvanceInterest(new BigDecimal(0));
								repayTotal = repayTotal.add(userAccount);
								repayChargeInterest = repayChargeInterest.add(new BigDecimal(0));
							}
							debtLoanDetail.setAdvanceStatus(1);
							debtLoanDetail.setAdvanceDays(advanceDays);
						}

					}
				}
				debtRepayDetailLoanDetailBean.setLoanDetailList(debtLoanDetails);
			}
		}
		debtRepayDetailLoanDetailBean.setAdvanceDays(advanceDays);
		debtRepayDetailLoanDetailBean.setAdvanceInterest(repayChargeInterest.multiply(new BigDecimal(-1)));
		debtRepayDetailLoanDetailBean.setRepayAccount(repayTotal);
		debtRepayDetailLoanDetailBean.setRepayAccountAll(repayTotal);
		debtRepayDetailLoanDetailBean.setAdvanceStatus(1);
	}

	/**
	 * 
	 * 根据借款编号查询放款信息
	 * 
	 * @author renxingchen
	 * @param borrowNid
	 * @return
	 */
	private List<DebtLoan> selectDebtLoanList(String borrowNid) {
		DebtLoanExample example = new DebtLoanExample();
		DebtLoanExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		List<DebtLoan> debtLoans = this.debtLoanMapper.selectByExample(example);
		return debtLoans;
	}

	/**
	 * 
	 * 计算分期还款正常还款状态下本期用户应还的金额
	 * 
	 * @author renxingchen
	 * @param debtRepayDetailLoanDetailBean
	 * @param borrow
	 * @param borrow
	 * @param advanceDays
	 */
	private void calculateLoanDetail(DebtRepayDetailLoanDetailBean debtRepayDetailLoanDetailBean, DebtBorrow borrow, int advanceDays) {
		List<DebtLoanDetail> debtLoanDetails = searchDebtLoanDetail(borrow.getBorrowNid(), debtRepayDetailLoanDetailBean.getRepayPeriod());
		if (debtLoanDetails != null && debtLoanDetails.size() > 0) {
			for (DebtLoanDetail debtLoanDetail : debtLoanDetails) {
				debtLoanDetail.setAdvanceDays(advanceDays);
				debtLoanDetail.setAdvanceStatus(0);
			}
			debtRepayDetailLoanDetailBean.setLoanDetailList(debtLoanDetails);
		}
		debtRepayDetailLoanDetailBean.setAdvanceDays(advanceDays);
		debtRepayDetailLoanDetailBean.setRepayAccountAll(debtRepayDetailLoanDetailBean.getRepayAccount());
		debtRepayDetailLoanDetailBean.setAdvanceStatus(0);
	}

	/**
	 * 
	 * 此处为方法说明
	 * 
	 * @author renxingchen
	 * @param borrowNid
	 * @param repayPeriod
	 * @return
	 */
	private List<DebtLoanDetail> searchDebtLoanDetail(String borrowNid, Integer repayPeriod) {
		DebtLoanDetailExample debtLoanDetailExample = new DebtLoanDetailExample();
		DebtLoanDetailExample.Criteria debtLoanDetailCrt = debtLoanDetailExample.createCriteria();
		debtLoanDetailCrt.andBorrowNidEqualTo(borrowNid);
		debtLoanDetailCrt.andRepayPeriodEqualTo(repayPeriod);
		List<DebtLoanDetail> debtLoanDetails = debtLoanDetailMapper.selectByExample(debtLoanDetailExample);
		return debtLoanDetails;
	}

	/**
	 * 
	 * 计算单期用户的还款信息
	 * 
	 * @author renxingchen
	 * @param repay
	 * @param borrow
	 * @param repayTimeStr
	 * @param delayDays
	 * @throws ParseException
	 */
	private void calculateRecover(DebtRepayByTermBean repay, DebtBorrow borrow, String repayTimeStr, int delayDays) throws ParseException {
		int time = GetDate.getNowTime10();
		// 用户计划还款时间
		String repayTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(repayTimeStr));
		// 用户实际还款时间
		String factRepayTime = GetDate.getDateTimeMyTimeInMillis(time);
		int distanceDays = GetDate.daysBetween(factRepayTime, repayTime);
		if (distanceDays < 0) {// 用户延期或者逾期了
			int lateDays = delayDays + distanceDays;
			if (lateDays >= 0) {// 用户延期还款
				delayDays = -distanceDays;
				// 计算延期用户实际还款总额
				this.calculateRecoverTotalDelay(repay, borrow.getBorrowNid(), borrow.getBorrowApr(), delayDays);
			} else {// 用户逾期还款
				lateDays = -lateDays;
				// 计算预期用户实际还款总额
				this.calculateRecoverTotalLate(repay, borrow.getBorrowNid(), borrow.getBorrowApr(), delayDays, lateDays);
			}
		} else {// 用户正常或者提前还款
			// 获取提前还款的阀值
			String repayAdvanceDay = this.getBorrowConfig("REPAY_ADVANCE_TIME");
			int advanceDays = distanceDays;
			// 融通宝提前几天
			if ((borrow.getProjectType() == 13 && borrow.getBorrowStyle().equals(CustomConstants.BORROW_STYLE_ENDDAY) && advanceDays > 0) || (borrow.getProjectType() == 13 && borrow.getBorrowStyle().equals(CustomConstants.BORROW_STYLE_END) && Integer.parseInt(repayAdvanceDay) < advanceDays)
					|| (borrow.getProjectType() != 13 && Integer.parseInt(repayAdvanceDay) < advanceDays)) {// 用户提前还款
				// 计算用户实际还款总额
				this.calculateRecoverTotalAdvance(repay, borrow.getBorrowNid(), borrow.getBorrowApr(), advanceDays);
			} else {// 用户正常还款
				// 计算用户实际还款总额
				this.calculateLoanTotal(repay, borrow.getBorrowNid(), borrow.getBorrowApr(), advanceDays);
			}
		}
		repay.setRepayTotal(repay.getRepayAccount().add(repay.getManageFee()));
	}

	/**
	 * 
	 * 计算逾期还款的总额
	 * 
	 * @author renxingchen
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param delayDays
	 * @param lateDays
	 */
	private void calculateRecoverTotalLate(DebtRepayByTermBean repay, String borrowNid, BigDecimal borrowApr, int delayDays, int lateDays) {
		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		// 统计借款用户总逾期利息
		BigDecimal userOverdueInterestTotal = new BigDecimal(0);
		// 获取未还款前用户能够获取的本息和
		BigDecimal userAccount = new BigDecimal(0);
		// 获取用户出借项目分期后的出借本金
		BigDecimal userCapital = new BigDecimal(0);
		List<DebtLoan> debtLoans = this.searchDebtLoan(borrowNid);
		if (debtLoans != null && debtLoans.size() > 0) {
			for (DebtLoan debtLoan : debtLoans) {
				// 获取未还款前用户能够获取的本息和
				userAccount = debtLoan.getLoanAccount();
				// 获取用户出借项目分期后的出借本金
				userCapital = debtLoan.getLoanCapital();
				// 计算用户实际获得的本息和
				BigDecimal userAccountFact = UnnormalRepayUtils.overdueRepayPrincipalInterest(userAccount, userCapital, borrowApr, delayDays, lateDays);
				// 计算用户逾期利息
				BigDecimal userOverdueInterest = UnnormalRepayUtils.overdueRepayOverdueInterest(userAccount, lateDays);
				// 计算用户延期利息
				BigDecimal userDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(userCapital, borrowApr, delayDays);

				debtLoan.setRepayDelayInterest(userDelayInterest);
				debtLoan.setRepayLateInterest(userOverdueInterest);
				// 统计总和
				userAccountTotal = userAccountTotal.add(userAccountFact);
				userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
				userOverdueInterestTotal = userOverdueInterestTotal.add(userOverdueInterest);

				debtLoan.setDelayDays(delayDays);
				debtLoan.setLateDays(lateDays);
				debtLoan.setAdvanceStatus(3);
			}
			repay.setLoanList(debtLoans);
		}
		repay.setRepayAccountAll(userAccountTotal);
		repay.setRepayAccount(userAccountTotal);
		repay.setDelayDays(delayDays);
		repay.setDelayInterest(userDelayInterestTotal);
		repay.setLateDays(lateDays);
		repay.setLateInterest(userOverdueInterestTotal);
		repay.setAdvanceStatus(3);

	}

	/**
	 * 
	 * 计算延期还款的总额
	 * 
	 * @author renxingchen
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param delayDays
	 */
	private void calculateRecoverTotalDelay(DebtRepayByTermBean repay, String borrowNid, BigDecimal borrowApr, int delayDays) {
		// 用户延期
		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		List<DebtLoan> debtLoans = this.searchDebtLoan(borrowNid);
		if (debtLoans != null && debtLoans.size() > 0) {
			// 获取未还款前用户能够获取的本息和
			BigDecimal userAccount = new BigDecimal(0);
			// 获取用户出借项目分期后的出借本金
			BigDecimal userCapital = new BigDecimal(0);
			for (DebtLoan debtLoan : debtLoans) {
				userAccount = debtLoan.getLoanAccount();
				userCapital = debtLoan.getLoanCapital();
				// 计算用户实际获得的本息和
				BigDecimal userAccountFact = UnnormalRepayUtils.delayRepayPrincipalInterest(userAccount, userCapital, borrowApr, delayDays);
				// 计算用户延期利息
				BigDecimal userDelayInterest = UnnormalRepayUtils.delayRepayInterest(userCapital, borrowApr, delayDays);
				debtLoan.setRepayDelayInterest(userDelayInterest);
				// 统计总和
				userAccountTotal = userAccountTotal.add(userAccountFact);
				userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
				// 用户延期还款
				debtLoan.setAdvanceStatus(2);
				debtLoan.setDelayDays(delayDays);
			}
			repay.setLoanList(debtLoans);
		}
		repay.setRepayAccountAll(userAccountTotal);
		repay.setRepayAccount(userAccountTotal);
		repay.setDelayDays(delayDays);
		repay.setDelayInterest(userDelayInterestTotal);
		repay.setAdvanceStatus(2);
	}

	/**
	 * 
	 * 计算提前还款的总额
	 * 
	 * @author renxingchen
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param interestDay
	 * @throws ParseException
	 */
	private void calculateRecoverTotalAdvance(DebtRepayByTermBean repay, String borrowNid, BigDecimal borrowApr, int interestDay) throws ParseException {
		// 用户提前还款
		// 用户实际还款额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 提前还款利息
		BigDecimal repayChargeInterest = new BigDecimal(0);
		List<DebtLoan> debtLoans = searchDebtLoan(borrowNid);
		if (debtLoans != null && debtLoans.size() > 0) {
			for (int i = 0; i < debtLoans.size(); i++) {
				DebtLoan debtLoan = debtLoans.get(i);
				String recoverTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(debtLoan.getRepayTime()));
				String createTime = GetDate.getDateTimeMyTimeInMillis(debtLoan.getCreateTime());
				// 获取这两个时间之间有多少天
				int totalDays = GetDate.daysBetween(createTime, recoverTime);
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = debtLoan.getLoanAccount();
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = debtLoan.getLoanCapital();
				//获取用户出借分期后的出借利息
				BigDecimal userInterest = debtLoan.getLoanInterest();
				// 计算用户实际获得的本息和
				BigDecimal userAccountFact = new BigDecimal(0);
				// 计算用户提前还款减少的的利息
				BigDecimal userChargeInterest = new BigDecimal(0);
				// 获取相应的项目详情
				DebtBorrow borrow = this.selectBorrow(borrowNid);
				// 如果项目类型为融通宝，调用新的提前还款利息计算公司
				if (borrow.getProjectType() == 13 && borrow.getBorrowStyle().equals(CustomConstants.BORROW_STYLE_ENDDAY)) {
					// 提前还款不应该大于本次计息时间
					if (totalDays < interestDay) {
						// 计算出借用户实际获得的本息和
						userAccountFact = UnnormalRepayUtils.aheadRTBRepayPrincipalInterest(userAccount, userCapital, borrowApr, totalDays);
						// 用户提前还款减少的利息
						userChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(userCapital, borrowApr, totalDays);
					} else {
						// 计算出借用户实际获得的本息和
						userAccountFact = UnnormalRepayUtils.aheadRTBRepayPrincipalInterest(userAccount, userCapital, borrowApr, interestDay);
						// 用户提前还款减少的利息
						userChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(userCapital, borrowApr, interestDay);
					}
				} else {
					//TODO 判断是否为先息后本
					boolean isStyle = CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle());
					// 提前还款不应该大于本次计息时间
					if (totalDays < interestDay) {
						// 计算出借用户实际获得的本息和
						userAccountFact = UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount, userCapital, borrowApr, totalDays);

						// 用户提前还款减少的利息
						userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrowApr, totalDays);

					} else {
						// 计算出借用户实际获得的本息和
						userAccountFact = UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount, userCapital, borrowApr, interestDay);

						// 用户提前还款减少的利息
						userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrowApr, interestDay);

					}
					if(isStyle){
						if(interestDay >= 30){
							userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,totalDays);
						}else{
							userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,interestDay);
						}
					}
				}
				debtLoans.get(i).setRepayAdvanceInterest(userChargeInterest.multiply(new BigDecimal(-1)));
				// 统计本息总和
				userAccountTotal = userAccountTotal.add(userAccountFact);
				// 统计提前还款减少的利息
				repayChargeInterest = repayChargeInterest.add(userChargeInterest);
				debtLoans.get(i).setAdvanceStatus(1);
				debtLoans.get(i).setAdvanceDays(interestDay);
			}
			repay.setLoanList(debtLoans);
		}
		repay.setRepayAccount(userAccountTotal);
		repay.setRepayAccountAll(userAccountTotal);
		repay.setAdvanceDays(interestDay);
		repay.setAdvanceInterest(repayChargeInterest.multiply(new BigDecimal(-1)));
		repay.setAdvanceStatus(1);
	}

	private DebtBorrow selectBorrow(String borrowNid) {
		DebtBorrowExample example = new DebtBorrowExample();
		DebtBorrowExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		List<DebtBorrowWithBLOBs> borrows = this.debtBorrowMapper.selectByExampleWithBLOBs(example);
		if (borrows != null && borrows.size() == 1) {
			return borrows.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 计算正常还款的总额
	 * 
	 * @author renxingchen
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param advanceDays
	 */
	private void calculateLoanTotal(DebtRepayByTermBean repay, String borrowNid, BigDecimal borrowApr, int advanceDays) {
		// 正常还款
		List<DebtLoan> debtLoans = searchDebtLoan(borrowNid);
		if (debtLoans != null && debtLoans.size() > 0) {
			for (DebtLoan debtLoan : debtLoans) {
				debtLoan.setAdvanceDays(advanceDays);
				debtLoan.setAdvanceStatus(0);
			}
			repay.setLoanList(debtLoans);
		}
		repay.setAdvanceDays(advanceDays);
		repay.setRepayAccountAll(repay.getRepayAccount());
		repay.setAdvanceStatus(0);
	}

	private List<DebtLoan> searchDebtLoan(String borrowNid) {
		DebtLoanExample example = new DebtLoanExample();
		DebtLoanExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		List<DebtLoan> debtLoans = debtLoanMapper.selectByExample(example);
		return debtLoans;
	}

	private DebtRepay searchDebtRepay(Integer userId, String borrowNid) {
		// 获取还款总表数据
		DebtRepayExample debtRepayExample = new DebtRepayExample();
		DebtRepayExample.Criteria debtRepayCrt = debtRepayExample.createCriteria();
		debtRepayCrt.andUserIdEqualTo(userId);
		debtRepayCrt.andBorrowNidEqualTo(borrowNid);
		List<DebtRepay> debtRepays = debtRepayMapper.selectByExample(debtRepayExample);
		if (debtRepays != null && debtRepays.size() == 1) {
			return debtRepays.get(0);
		} else {
			return null;
		}
	}

	@Override
	public DebtBorrow searchDebtBorrowProject(Integer userId, String borrowNid) {
		// 获取当前的用户还款的项目
		DebtBorrowExample example = new DebtBorrowExample();
		DebtBorrowExample.Criteria borrowCrt = example.createCriteria();
		borrowCrt.andBorrowNidEqualTo(borrowNid);
		borrowCrt.andUserIdEqualTo(userId);
		List<DebtBorrow> borrows = debtBorrowMapper.selectByExample(example);
		if (borrows != null && borrows.size() == 1) {
			return borrows.get(0);
		} else {
			return null;
		}
	}

	@Override
	public boolean updateRepayMoney(DebtRepayByTermBean repay) {
		int time = GetDate.getNowTime10();
		String borrowNid = repay.getBorrowNid();
		int period = repay.getRepayPeriod();
		int userId = repay.getUserId();
		BigDecimal account = repay.getRepayAccount();// 用户实际还款本金
		BigDecimal fee = repay.getManageFee();// 用户实际还款管理费
		BigDecimal repayAccount = new BigDecimal("0");// 用户应还款金额
		String nid = "";
		Boolean repayFlag = false;
		int errorCount = 0;
		// 不分期还款
		List<DebtLoan> debtLoans = repay.getLoanList();
		if (debtLoans != null && debtLoans.size() > 0) {
			// 获取用户本次应还的金额
			DebtRepay borrowRepay = this.searchDebtRepay(userId, borrowNid);
			repayAccount = borrowRepay.getRepayAccount();
			DebtApicronExample example = new DebtApicronExample();
			DebtApicronExample.Criteria crt = example.createCriteria();
			crt.andBorrowNidEqualTo(borrowNid);
			crt.andApiTypeEqualTo(1);
			List<DebtApicron> debtApicrons = debtApicronMapper.selectByExample(example);
			if (debtApicrons != null && debtApicrons.size() > 0) {// 如果已经有还款任务了，则修改还款任务的相关状态
				DebtApicron debtApicron = debtApicrons.get(0);
				if (debtApicron.getRepayStatus() == null) {
					boolean borrowRecoverFlag = true;
					for (DebtLoan debtLoan : debtLoans) {
						DebtLoan debtLoanOld = debtLoanMapper.selectByPrimaryKey(debtLoan.getId());
						debtLoanOld.setAdvanceDays(debtLoan.getAdvanceDays());
						debtLoanOld.setRepayAdvanceInterest(debtLoan.getRepayAdvanceInterest());
						debtLoanOld.setAdvanceStatus(debtLoan.getAdvanceStatus());
						debtLoanOld.setDelayDays(debtLoan.getDelayDays());
						debtLoanOld.setRepayDelayInterest(debtLoan.getRepayDelayInterest());
						debtLoanOld.setLateDays(debtLoan.getLateDays());
						debtLoanOld.setRepayLateInterest(debtLoan.getRepayLateInterest());
						boolean flag = debtLoanMapper.updateByPrimaryKey(debtLoan) > 0 ? true : false;
						if (!flag) {
							errorCount = errorCount + 1;
						}
						borrowRecoverFlag = borrowRecoverFlag && flag;
					}
					if (borrowRecoverFlag) {
						debtApicron.setPeriodNow(1);
						debtApicron.setRepayStatus(0);
						int updateTime = debtApicron.getUpdateTime();
						debtApicron.setUpdateTime(GetDate.getNowTime10());
						crt.andUpdateTimeEqualTo(updateTime);
						boolean apicronFlag = debtApicronMapper.updateByExampleWithBLOBs(debtApicron, example) > 0 ? true : false;
						if (!apicronFlag) {
							throw new RuntimeException("重复还款");
						} else {
							repayFlag = true;
						}
					} else {
						throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
					}
				} else {
					repayFlag = true;
				}
			} else {// 如果没有还款任务，则新生成一个还款任务
				boolean borrowRecoverFlag = true;
				for (DebtLoan debtLoan : debtLoans) {
					DebtLoan debtLoanOld = debtLoanMapper.selectByPrimaryKey(debtLoan.getId());
					debtLoanOld.setAdvanceStatus(debtLoan.getAdvanceStatus());
					debtLoanOld.setRepayAdvanceInterest(debtLoan.getRepayAdvanceInterest());
					debtLoanOld.setAdvanceDays(debtLoan.getAdvanceDays());
					debtLoanOld.setDelayDays(debtLoan.getDelayDays());
					debtLoanOld.setRepayDelayInterest(debtLoan.getRepayDelayInterest());
					debtLoanOld.setLateDays(debtLoan.getLateDays());
					debtLoanOld.setRepayLateInterest(debtLoan.getRepayLateInterest());
					boolean flag = debtLoanMapper.updateByPrimaryKey(debtLoanOld) > 0 ? true : false;
					if (!flag) {
						errorCount = errorCount + 1;
					}
					borrowRecoverFlag = borrowRecoverFlag && flag;
				}
				if (borrowRecoverFlag) {
					int nowTime = GetDate.getNowTime10();
					DebtApicron debtApicron = new DebtApicron();
					debtApicron.setUserId(repay.getUserId());
					debtApicron.setBorrowNid(borrowNid);
					nid = repay.getBorrowNid() + "_" + repay.getUserId() + "_1";
					debtApicron.setNid(nid);
					debtApicron.setApiType(1);
					debtApicron.setPeriodNow(1);
					debtApicron.setRepayStatus(0);
					debtApicron.setStatus(1);
					debtApicron.setCreditRepayStatus(0);
					debtApicron.setCreateTime(nowTime);
					debtApicron.setUpdateTime(nowTime);
					boolean apiCronFlag = debtApicronMapper.insertSelective(debtApicron) > 0 ? true : false;
					if (apiCronFlag) {
						repayFlag = true;
					}
				} else {
					throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
				}
			}
		}
		List<DebtRepayDetailLoanDetailBean> repayDetailList = repay.getRepayDetailList();
		// 分期还款
		if (repayDetailList != null && repayDetailList.size() > 0) {
			for (DebtRepayDetailLoanDetailBean debtRepayDetail : repayDetailList) {
				if (debtRepayDetail.getRepayPeriod() == period) {
					DebtRepayDetail repayDetail = this.searchDebtRepayDetail(userId, borrowNid, period);
					repayAccount = repayDetail.getRepayAccount();
					DebtApicronExample example = new DebtApicronExample();
					DebtApicronExample.Criteria crt = example.createCriteria();
					crt.andBorrowNidEqualTo(borrowNid);
					crt.andApiTypeEqualTo(1);
					crt.andPeriodNowEqualTo(period);
					List<DebtApicron> debtApicrons = debtApicronMapper.selectByExample(example);
					if (debtApicrons != null && debtApicrons.size() > 0) {
						DebtApicron debtApicron = debtApicrons.get(0);
						if (debtApicron.getRepayStatus() == null) {
							boolean borrowRecoverPlanFlag = true;
							List<DebtLoanDetail> debtLoanDetails = debtRepayDetail.getLoanDetailList();
							if (debtLoanDetails != null && debtLoanDetails.size() > 0) {
								for (DebtLoanDetail debtLoanDetail : debtLoanDetails) {
									DebtLoanDetail debtLoanDetailOld = debtLoanDetailMapper.selectByPrimaryKey(debtLoanDetail.getId());
									debtLoanDetailOld.setAdvanceDays(debtLoanDetail.getAdvanceDays());
									debtLoanDetailOld.setRepayAdvanceInterest(debtLoanDetail.getRepayAdvanceInterest());
									debtLoanDetailOld.setAdvanceStatus(debtLoanDetail.getAdvanceStatus());
									debtLoanDetailOld.setDelayDays(debtLoanDetail.getDelayDays());
									debtLoanDetailOld.setRepayDelayInterest(debtLoanDetail.getRepayDelayInterest());
									debtLoanDetailOld.setLateDays(debtLoanDetail.getLateDays());
									debtLoanDetailOld.setRepayLateInterest(debtLoanDetail.getRepayLateInterest());
									boolean flag = debtLoanDetailMapper.updateByPrimaryKey(debtLoanDetailOld) > 0 ? true : false;
									if (!flag) {
										errorCount = errorCount + 1;
									}
									borrowRecoverPlanFlag = borrowRecoverPlanFlag && flag;
								}
							}
							if (borrowRecoverPlanFlag) {
								debtApicron.setPeriodNow(period);
								debtApicron.setRepayStatus(0);
								int updateTime = debtApicron.getUpdateTime();
								debtApicron.setUpdateTime(GetDate.getNowTime10());
								crt.andUpdateTimeEqualTo(updateTime);
								boolean apiCronFlag = debtApicronMapper.updateByExampleWithBLOBs(debtApicron, example) > 0 ? true : false;
								if (apiCronFlag) {
									repayFlag = true;
								} else {
									throw new RuntimeException("重复还款");
								}
							} else {
								throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
							}
						} else {
							repayFlag = true;
						}
					} else {
						boolean borrowRecoverPlanFlag = true;
						List<DebtLoanDetail> debtLoanDetails = debtRepayDetail.getLoanDetailList();
						if (debtLoanDetails != null && debtLoanDetails.size() > 0) {
							for (DebtLoanDetail debtLoanDetail : debtLoanDetails) {
								DebtLoanDetail debtLoanDetailOld = debtLoanDetailMapper.selectByPrimaryKey(debtLoanDetail.getId());
								debtLoanDetailOld.setAdvanceDays(debtLoanDetail.getAdvanceDays());
								debtLoanDetailOld.setRepayAdvanceInterest(debtLoanDetail.getRepayAdvanceInterest());
								debtLoanDetailOld.setAdvanceStatus(debtLoanDetail.getAdvanceStatus());
								debtLoanDetailOld.setDelayDays(debtLoanDetail.getDelayDays());
								debtLoanDetailOld.setRepayDelayInterest(debtLoanDetail.getRepayDelayInterest());
								debtLoanDetailOld.setLateDays(debtLoanDetail.getLateDays());
								debtLoanDetailOld.setRepayLateInterest(debtLoanDetail.getRepayLateInterest());
								boolean flag = debtLoanDetailMapper.updateByPrimaryKey(debtLoanDetailOld) > 0 ? true : false;
								if (!flag) {
									errorCount = errorCount + 1;
								}
								borrowRecoverPlanFlag = borrowRecoverPlanFlag && flag;
							}
						}
						if (borrowRecoverPlanFlag) {
							int nowTime = GetDate.getNowTime10();
							DebtApicron debtApicron = new DebtApicron();
							debtApicron.setUserId(debtRepayDetail.getUserId());
							nid = repay.getBorrowNid() + "_" + repay.getUserId() + "_" + period;
							debtApicron.setNid(nid);
							debtApicron.setBorrowNid(borrowNid);
							debtApicron.setApiType(1);
							debtApicron.setPeriodNow(period);
							debtApicron.setRepayStatus(0);
							debtApicron.setStatus(1);
							debtApicron.setCreditRepayStatus(0);
							debtApicron.setCreateTime(nowTime);
							debtApicron.setUpdateTime(nowTime);
							boolean apiCronFlag = debtApicronMapper.insertSelective(debtApicron) > 0 ? true : false;
							if (apiCronFlag) {
								repayFlag = true;
							} else {
								throw new RuntimeException("重复还款");
							}
						} else {
							throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
						}
					}
				}
			}
		}
		if (repayFlag) {
			if (this.countDebtAccountListByNid(nid) == 0) {
				// 更新account表
				BigDecimal balance = new BigDecimal(0);// 可用金额
				BigDecimal total = new BigDecimal(0);// 账户总额
				BigDecimal expand = new BigDecimal(0);// 账户总支出
				BigDecimal repayMoney = new BigDecimal(0);// 账户还款总额
				AccountExample accountExample = new AccountExample();
				AccountExample.Criteria criteria = accountExample.createCriteria();
				criteria.andUserIdEqualTo(userId);
				List<Account> accountlist = accountMapper.selectByExample(accountExample);
				if (accountlist != null && accountlist.size() > 0) {
					Account accountBean = accountlist.get(0);
					if (account.add(fee).compareTo(accountBean.getBalance()) == 0 || account.add(fee).compareTo(accountBean.getBalance()) == -1) {
						AccountChinapnr accountChinapnr = this.getChinapnrUserInfo(userId);
						if (accountChinapnr != null) {
							BigDecimal userBalance = this.getUserBalance(accountChinapnr.getChinapnrUsrcustid());
							if (account.add(fee).compareTo(userBalance) == 0 || account.add(fee).compareTo(userBalance) == -1) {
								// ** 用户符合还款条件，可以还款 *//*
								total = accountBean.getTotal().subtract(account.add(fee));// 减去账户总资产
								balance = accountBean.getBalance().subtract(account.add(fee)); // 减去可用余额
								expand = accountBean.getExpend().add(account.add(fee));// 累加到总支出
								repayMoney = accountBean.getRepay().subtract(repayAccount);// 减去待还金额(提前还款利息)
								accountBean.setTotal(total);
								accountBean.setBalance(balance);
								accountBean.setExpend(expand);
								accountBean.setRepay(repayMoney);
								System.out.println("用户:" + userId + "***********************************扣除相应的还款金额account：" + JSON.toJSONString(accountBean));
								boolean accountFlag = accountMapper.updateByPrimaryKey(accountBean) > 0 ? true : false;
								if (accountFlag) {
									// 插入huiyingdai_account_list表
									AccountList accountListRecord = new AccountList();
									// 生成规则BorrowNid_userid_期数
									accountListRecord.setNid(borrowNid + "_" + userId + "_" + period);
									// 借款人id
									accountListRecord.setUserId(userId);
									// 操作金额
									accountListRecord.setAmount(account.add(fee));
									// 收支类型1收入2支出3冻结
									accountListRecord.setType(2);
									// 交易类型
									accountListRecord.setTrade("repay_success");
									// 操作识别码
									accountListRecord.setTradeCode("balance");
									// 资金总额
									accountListRecord.setTotal(accountBean.getTotal());
									// 可用金额
									accountListRecord.setBalance(accountBean.getBalance());
									// 冻结金额
									accountListRecord.setFrost(accountBean.getFrost());
									// 待收金额
									accountListRecord.setAwait(accountBean.getAwait());
									// 待还金额
									accountListRecord.setRepay(accountBean.getRepay());
									// 创建时间
									accountListRecord.setCreateTime(time);
									// 操作员
									accountListRecord.setOperator(CustomConstants.OPERATOR_AUTO_REPAY);
									accountListRecord.setRemark(borrowNid);
									// 操作IP
									accountListRecord.setIp(repay.getIp());
									accountListRecord.setBaseUpdate(0);
									accountListRecord.setWeb(0);
									System.out.println("用户:" + userId + "***********************************预插入accountList：" + JSON.toJSONString(accountListRecord));
									boolean accountListFlag = this.accountListMapper.insertSelective(accountListRecord) > 0 ? true : false;
									if (accountListFlag) {
										DebtBorrowExample borrowExample = new DebtBorrowExample();
										DebtBorrowExample.Criteria borrowCrt = borrowExample.createCriteria();
										borrowCrt.andBorrowNidEqualTo(borrowNid);
										List<DebtBorrowWithBLOBs> debtBorrowList = this.debtBorrowMapper.selectByExampleWithBLOBs(borrowExample); 
										if(debtBorrowList!=null&&debtBorrowList.size()==1){
											DebtBorrowWithBLOBs borrow = debtBorrowList.get(0);
											borrow.setBorrowRepayWebAdvance(account.add(fee));
											boolean debtBorrowFlag = this.debtBorrowMapper.updateByPrimaryKeySelective(borrow)>0?true:false;
											if(debtBorrowFlag){
												return true;
											}else{
												throw new RuntimeException("还款失败！更新debtborrow借款人还款金额失败！项目编号："+borrowNid);
											}
										}else{
											throw new RuntimeException("还款失败！查询debtborrow标的信息失败！项目编号："+borrowNid);
										}
									} else {
										throw new RuntimeException("还款失败！插入借款人交易明细表AccountList失败！");
									}
								} else {
									throw new RuntimeException("还款失败！更新借款人账户余额表Account失败！");
								}
							} else {
								throw new RuntimeException("用户汇付账户余额不足!");
							}
						} else {
							throw new RuntimeException("用户开户信息不存在!");
						}
					} else {
						throw new RuntimeException("用户余额不足,还款失败");
					}
				} else {
					throw new RuntimeException("未查询到用户的账户信息，account表查询失败");
				}
			} else {
				throw new RuntimeException("此笔还款的交易明细已存在,请勿重复还款");
			}
		} else {
			throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
		}
	}

	/**
	 * 
	 * 查询还款详情列表
	 * 
	 * @author renxingchen
	 * @param userId
	 * @param borrowNid
	 * @return
	 */
	private List<DebtRepayDetail> searchDebtRepayDetail(int userId, String borrowNid) {
		DebtRepayDetailExample debtRepayDetailExample = new DebtRepayDetailExample();
		DebtRepayDetailExample.Criteria debtRepayDetailCrt = debtRepayDetailExample.createCriteria();
		debtRepayDetailCrt.andUserIdEqualTo(userId);
		debtRepayDetailCrt.andBorrowNidEqualTo(borrowNid);
		List<DebtRepayDetail> debtRepayDetails = debtRepayDetailMapper.selectByExample(debtRepayDetailExample);
		return debtRepayDetails;
	}

	/**
	 * 
	 * 根据当前期数查询还款详情
	 * 
	 * @author renxingchen
	 * @param userId
	 * @param borrowNid
	 * @param period
	 * @return
	 */
	private DebtRepayDetail searchDebtRepayDetail(int userId, String borrowNid, int period) {
		DebtRepayDetailExample debtRepayDetailExample = new DebtRepayDetailExample();
		DebtRepayDetailExample.Criteria debtRepayDetailCrt = debtRepayDetailExample.createCriteria();
		debtRepayDetailCrt.andUserIdEqualTo(userId);
		debtRepayDetailCrt.andBorrowNidEqualTo(borrowNid);
		debtRepayDetailCrt.andRepayPeriodEqualTo(period);
		List<DebtRepayDetail> debtRepayDetails = debtRepayDetailMapper.selectByExample(debtRepayDetailExample);
		if (debtRepayDetails != null && debtRepayDetails.size() == 1) {
			return debtRepayDetails.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 获取用户在汇付天下的余额
	 * 
	 * @author renxingchen
	 * @param chinapnrUsrcustid
	 * @return
	 */
	private BigDecimal getUserBalance(Long chinapnrUsrcustid) {
		BigDecimal balance = BigDecimal.ZERO;
		// 调用汇付接口(交易状态查询)
		ChinapnrBean bean = new ChinapnrBean();
		// 版本号(必须)
		bean.setVersion(ChinaPnrConstant.VERSION_10);
		// 消息类型(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_BALANCE_BG);
		// 用户客户号(必须)
		bean.setUsrCustId(String.valueOf(chinapnrUsrcustid));
		// 写log用参数
		bean.setLogUserId(0);
		// 备注
		bean.setLogRemark("用户余额查询");
		// PC
		bean.setLogClient("0");
		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean != null) {
			try {
				balance = new BigDecimal(chinapnrBean.getAvlBal().replace(",", ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return balance;
	}

	/**
	 * 
	 * 获取用户在汇付天下的账号信息
	 * 
	 * @author renxingchen
	 * @param userId
	 * @return
	 */
	private AccountChinapnr getChinapnrUserInfo(Integer userId) {
		if (userId != null) {
			AccountChinapnrExample example = new AccountChinapnrExample();
			AccountChinapnrExample.Criteria criteria = example.createCriteria();
			criteria.andUserIdEqualTo(userId);
			List<AccountChinapnr> list = this.accountChinapnrMapper.selectByExample(example);
			if (list != null && list.size() == 1) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * 
	 * 查看是否已经存在还款资金明细
	 * 
	 * @author renxingchen
	 * @param nid
	 * @return
	 */
	private int countDebtAccountListByNid(String nid) {
		DebtAccountListExample debtAccountListExample = new DebtAccountListExample();
		debtAccountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("epay_success");
		return this.debtAccountListMapper.countByExample(debtAccountListExample);
	}

	@Override
	public UserRepayProjectBean searchRepayProjectDetail(UserRepayProjectBean form) throws NumberFormatException, ParseException {
		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
		String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
		DebtBorrowExample example = new DebtBorrowExample();
		DebtBorrowExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andUserIdEqualTo(Integer.parseInt(userId));
		List<DebtBorrow> projects = debtBorrowMapper.selectByExample(example);// 查询相应的用户还款项目
		if (projects != null && projects.size() > 0) {
			DebtBorrow borrow = projects.get(0);
			form.settType("1");// 设置为汇添金专属项目
			// 设置相应的项目名称
			form.setBorrowName(borrow.getName());
			// 获取相应的项目还款方式
			String borrowStyle = StringUtils.isNotEmpty(borrow.getBorrowStyle()) ? borrow.getBorrowStyle() : null;
			form.setBorrowStyle(borrowStyle);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("borrowNid", borrowNid);
			params.put("userId", userId);
			// 还款总期数
			int periodTotal = borrow.getBorrowPeriod();
			if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
				// 查询还款信息
				DebtRepayByTermBean repay = this.calculateRepay(Integer.parseInt(userId), borrow);
				form.setBorrowPeriod("0");
				form.setBorrowFee(repay.getManageFee().toString());
				// 计算的是还款总额
				form.setBorrowTotal(repay.getRepayAccountAll().add(repay.getManageFee()).toString());
				form.setBorrowAccount(repay.getRepayAccount().toString());
				form.setBorrowCapital(repay.getRepayCapital().toString());
				form.setBorrowInterest(repay.getRepayInterest().toString());
				// 判断当前期是否在还款
				DebtApicronExample debtApicronExample = new DebtApicronExample();
				DebtApicronExample.Criteria crtBorrowApicron = debtApicronExample.createCriteria();
				crtBorrowApicron.andBorrowNidEqualTo(borrowNid);
				crtBorrowApicron.andApiTypeEqualTo(1);
				crtBorrowApicron.andPeriodNowEqualTo(1);
				List<DebtApicron> debtApicrons = debtApicronMapper.selectByExample(debtApicronExample);
				if (debtApicrons != null && debtApicrons.size() > 0) {
					DebtApicron DebtApicron = debtApicrons.get(0);
					if (DebtApicron.getRepayStatus().intValue() != 1 || DebtApicron.getCreditRepayStatus().intValue() != 1) {
						// 用户还款当前期
						form.setBorrowStatus("1");
					} else {// 用户未还款当前期
						form.setBorrowStatus("0");
					}
				} else {// 用户未还款当前期
					form.setBorrowStatus("0");
				}
				form.setAdvanceStatus(String.valueOf(repay.getAdvanceStatus()));
				form.setAdvanceDays(repay.getAdvanceDays().toString());
				form.setAdvanceInterest(repay.getAdvanceInterest().multiply(new BigDecimal("-1")).toString());
				form.setDelayDays(repay.getDelayDays().toString());
				form.setDelayInterest(repay.getDelayInterest().toString());
				form.setLateDays(repay.getLateDays().toString());
				form.setLateInterest(repay.getLateInterest().toString());
				List<UserRepayBean> userRepayList = new ArrayList<UserRepayBean>();
				UserRepayBean userRepayBean = new UserRepayBean();
				// 此处是本息和
				userRepayBean.setAdvanceDays(repay.getAdvanceDays().toString());
				userRepayBean.setAdvanceInterest(repay.getAdvanceInterest().multiply(new BigDecimal("-1")).toString());
				userRepayBean.setRepayAccount(repay.getRepayAccount().toString());
				userRepayBean.setRepayCapital(repay.getRepayCapital().toString());
				userRepayBean.setRepayInterest(repay.getRepayInterest().toString());
				userRepayBean.setDelayDays(repay.getDelayDays().toString());
				userRepayBean.setDelayInterest(repay.getDelayInterest().toString());
				userRepayBean.setFinanceManage(repay.getManageFee().toString());
				userRepayBean.setLateDays(repay.getLateDays().toString());
				userRepayBean.setLateInterest(repay.getLateInterest().toString());
				userRepayBean.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(repay.getRepayTime())));
				userRepayBean.setRepayTotal(repay.getRepayAccountAll().toString());
				userRepayBean.setStatus(repay.getRepayStatus().toString());
				userRepayBean.setUserId(repay.getUserId().toString());
				userRepayBean.setRepayPeriod("1");
				userRepayBean.setAdvanceStatus(repay.getAdvanceStatus().toString());
				List<DebtLoan> debtLoans = repay.getLoanList();
				if (debtLoans != null && debtLoans.size() > 0) {
					List<UserRepayDetailBean> userRepayDetails = new ArrayList<UserRepayDetailBean>();
					UserRepayDetailBean userRepayDetail;
					for (DebtLoan debtLoan : debtLoans) {
						userRepayDetail = new UserRepayDetailBean();
						userRepayDetail.setAdvanceDays(debtLoan.getAdvanceDays().toString());
						userRepayDetail.setAdvanceInterest(debtLoan.getRepayAdvanceInterest().multiply(new BigDecimal("-1")).toString());
						userRepayDetail.setRepayAccount(debtLoan.getLoanAccount().toString());
						userRepayDetail.setRepayCapital(debtLoan.getLoanCapital().toString());
						userRepayDetail.setRepayInterest(debtLoan.getLoanInterest().toString());
						userRepayDetail.setDelayDays(debtLoan.getDelayDays().toString());
						userRepayDetail.setDelayInterest(debtLoan.getRepayDelayInterest().toString());
						userRepayDetail.setFinanceManage(debtLoan.getManageFee().toString());
						userRepayDetail.setLateDays(debtLoan.getLateDays().toString());
						userRepayDetail.setLateInterest(debtLoan.getRepayLateInterest().toString());
						userRepayDetail.setAdvanceStatus(debtLoan.getAdvanceStatus().toString());
						userRepayDetail.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(debtLoan.getRepayTime())));
						BigDecimal total = new BigDecimal("0");
						if (debtLoan.getRepayStatus() == 2) {
							total = debtLoan.getRepayAccountYes().add(debtLoan.getManageFee());
						} else {
							// recover中account未更新
							total = debtLoan.getLoanAccount().add(debtLoan.getManageFee()).add(debtLoan.getRepayAdvanceInterest()).add(debtLoan.getRepayDelayInterest()).add(debtLoan.getRepayLateInterest());
						}
						userRepayDetail.setRepayTotal(total.toString());
						userRepayDetail.setStatus(debtLoan.getRepayStatus().toString());
						userRepayDetail.setUserId(debtLoan.getUserId().toString());
						String userName = this.searchUserNameById(debtLoan.getUserId());
						String userNameStr = userName.substring(0, 1).concat("**");
						userRepayDetail.setUserName(userNameStr);
						userRepayDetails.add(userRepayDetail);
					}
					userRepayBean.setUserRepayDetailList(userRepayDetails);
					userRepayList.add(userRepayBean);
				}
				form.setUserRepayList(userRepayList);
			} else {
				// 计算分期的项目还款信息
				DebtRepayByTermBean repayByTerm = this.calculateRepay(Integer.parseInt(userId), borrow);
				// 计算当前还款期数
				int repayPeriod = repayByTerm.getRepayPeriod();
				// 如果用户不是还款最后一期
				if (repayPeriod <= periodTotal) {
					DebtApicronExample debtApicronExample = new DebtApicronExample();
					DebtApicronExample.Criteria crtBorrowApicron = debtApicronExample.createCriteria();
					crtBorrowApicron.andBorrowNidEqualTo(borrowNid);
					crtBorrowApicron.andPeriodNowEqualTo(repayPeriod);
					crtBorrowApicron.andApiTypeEqualTo(1);
					List<DebtApicron> debtApicrons = this.debtApicronMapper.selectByExample(debtApicronExample);
					// 正在还款当前期
					if (debtApicrons != null && debtApicrons.size() > 0) {
						DebtApicron debtApicron = debtApicrons.get(0);
						if (debtApicron.getRepayStatus().intValue() != 1 || debtApicron.getCreditRepayStatus().intValue() != 1) {
							// 用户还款当前期
							form.setBorrowStatus("1");
						} else {// 用户当前期正在还款
							form.setBorrowStatus("0");
						}
					} else {// 用户未还款当前期
						form.setBorrowStatus("0");
					}
				} else {// 用户正在还款最后一期
					form.setBorrowStatus("1");
				}
				// 设置当前的还款期数
				form.setBorrowPeriod(String.valueOf(repayPeriod));
				// 获取统计的用户还款计划列表
				List<DebtRepayDetailLoanDetailBean> userRepayPlans = repayByTerm.getRepayDetailList();
				if (userRepayPlans != null && userRepayPlans.size() > 0) {
					List<UserRepayBean> recoverList = new ArrayList<UserRepayBean>();
					// 声明需拼接数据的实体
					UserRepayBean userRepayBean;
					List<UserRepayDetailBean> userRepayDetails;
					UserRepayDetailBean userRepayDetail;
					// 遍历计划还款信息，拼接数据
					for (DebtRepayDetailLoanDetailBean userRepayPlan : userRepayPlans) {
						userRepayBean = new UserRepayBean();
						// 设置本期的用户本息和
						userRepayBean.setRepayAccount(userRepayPlan.getRepayAccount().toString());
						// 设置本期的用户本金
						userRepayBean.setRepayCapital(userRepayPlan.getRepayCapital().toString());
						// 设置本期的用户利息
						userRepayBean.setRepayInterest(userRepayPlan.getRepayInterest().toString());
						if (userRepayPlan.getRepayStatus() == 1) {// 如果本期已经还款完成
							// 获取本期的用户已还款本息
							userRepayBean.setRepayTotal(userRepayPlan.getRepayAccountYes().toString());
						} else {// 用户未还款本息
								// 此处分期计算的是本息和
							userRepayBean.setRepayTotal(userRepayPlan.getRepayAccount().toString());
						}
						userRepayBean.setUserId(userRepayPlan.getUserId().toString());
						userRepayBean.setRepayPeriod(userRepayPlan.getRepayPeriod().toString());
						userRepayBean.setAdvanceStatus(userRepayPlan.getAdvanceStatus().toString());
						userRepayBean.setStatus(userRepayPlan.getRepayStatus().toString());
						userRepayBean.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(userRepayPlan.getRepayTime())));
						userRepayBean.setAdvanceDays(userRepayPlan.getAdvanceDays().toString());
						userRepayBean.setAdvanceInterest(userRepayPlan.getAdvanceInterest().multiply(new BigDecimal("-1")).toString());
						userRepayBean.setDelayDays(userRepayPlan.getDelayDays().toString());
						userRepayBean.setDelayInterest(userRepayPlan.getDelayInterest().toString());
						userRepayBean.setFinanceManage(userRepayPlan.getManageFee().toString());
						userRepayBean.setLateDays(userRepayPlan.getLateDays().toString());
						userRepayBean.setLateInterest(userRepayPlan.getLateInterest().toString());

						if (repayPeriod == userRepayPlan.getRepayPeriod()) {
							form.setBorrowFee(userRepayPlan.getManageFee().toString());
							// 此处计算的是还款总额包含管理费
							form.setBorrowTotal(userRepayPlan.getRepayAccountAll().add(userRepayPlan.getManageFee()).toString());
							form.setBorrowAccount(userRepayPlan.getRepayAccount().toString());
							form.setBorrowCapital(userRepayPlan.getRepayCapital().toString());
							form.setBorrowInterest(userRepayPlan.getRepayInterest().toString());
							form.setAdvanceStatus(userRepayPlan.getAdvanceStatus().toString());
							form.setAdvanceDays(userRepayPlan.getAdvanceDays().toString());
							form.setAdvanceInterest(userRepayPlan.getAdvanceInterest().toString());
							form.setDelayDays(userRepayPlan.getDelayDays().toString());
							form.setDelayInterest(userRepayPlan.getDelayInterest().toString());
							form.setLateDays(userRepayPlan.getLateDays().toString());
							form.setLateInterest(userRepayPlan.getLateInterest().toString());
						}
						List<DebtLoanDetail> debtLoanDetails = userRepayPlan.getLoanDetailList();
						userRepayDetails = new ArrayList<UserRepayDetailBean>();
						for (DebtLoanDetail debtLoanDetail : debtLoanDetails) {
							userRepayDetail = new UserRepayDetailBean();
							userRepayDetail.setRepayAccount(debtLoanDetail.getLoanAccount().toString());
							userRepayDetail.setRepayCapital(debtLoanDetail.getLoanCapital().toString());
							userRepayDetail.setRepayInterest(debtLoanDetail.getLoanInterest().toString());
							userRepayDetail.setAdvanceStatus(debtLoanDetail.getAdvanceStatus().toString());
							userRepayDetail.setAdvanceDays(debtLoanDetail.getAdvanceDays().toString());
							userRepayDetail.setAdvanceInterest(debtLoanDetail.getRepayAdvanceInterest().multiply(new BigDecimal("-1")).toString());
							userRepayDetail.setDelayDays(debtLoanDetail.getDelayDays().toString());
							userRepayDetail.setDelayInterest(debtLoanDetail.getRepayDelayInterest().toString());
							userRepayDetail.setFinanceManage(debtLoanDetail.getManageFee().toString());
							userRepayDetail.setLateDays(debtLoanDetail.getLateDays().toString());
							userRepayDetail.setLateInterest(debtLoanDetail.getRepayLateInterest().toString());
							userRepayDetail.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(debtLoanDetail.getRepayTime())));
							BigDecimal total = new BigDecimal("0");
							if (debtLoanDetail.getRepayStatus() == 2) {
								total = debtLoanDetail.getRepayAccountYes().add(debtLoanDetail.getManageFee());
							} else {
								// 因recover_plan未进行account字段更新
								total = debtLoanDetail.getLoanAccount().add(debtLoanDetail.getManageFee()).add(debtLoanDetail.getRepayAdvanceInterest()).add(debtLoanDetail.getRepayDelayInterest()).add(debtLoanDetail.getRepayLateInterest());
							}
							userRepayDetail.setRepayTotal(total.toString());
							userRepayDetail.setStatus(debtLoanDetail.getRepayStatus().toString());
							userRepayDetail.setUserId(debtLoanDetail.getUserId().toString());
							String userName = this.searchUserNameById(debtLoanDetail.getUserId());
							String userNameStr = userName.substring(0, 1).concat("**");
							userRepayDetail.setUserName(userNameStr);
							userRepayDetails.add(userRepayDetail);
						}
						userRepayBean.setUserRepayDetailList(userRepayDetails);
						recoverList.add(userRepayBean);
					}
					form.setUserRepayList(recoverList);
				}
			}
			return form;

		} else {
			return null;
		}
	}

	private String searchUserNameById(Integer userId) {
		Users user = usersMapper.selectByPrimaryKey(userId);
		return user.getUsername();
	}

}

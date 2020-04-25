package com.hyjf.server.module.wkcd.repay;


import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.AccountLog;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlanExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.server.BaseServiceImpl;

@Service
public class WkcdRepayServiceImpl extends BaseServiceImpl implements WkcdRepayService {


	/**
	 * 查询待还款项目信息
	 * 
	 * @param userId
	 * @param borrowNid
	 * @return
	 */
	@Override
	public Borrow searchRepayProject(int userId, String borrowNid) {
		// 获取当前的用户还款的项目
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria borrowCrt = example.createCriteria();
		borrowCrt.andBorrowNidEqualTo(borrowNid);
		borrowCrt.andUserIdEqualTo(userId);
		List<Borrow> borrows = borrowMapper.selectByExample(example);
		if (borrows != null && borrows.size() == 1) {
			return borrows.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 统计用户的相应的还款总额 单期
	 * 
	 * @param userId
	 * @param borrowNid
	 * @param borrowApr
	 * @param borrowStyle
	 * @param periodTotal
	 * @return
	 * @throws ParseException
	 */
	@Override
	public BigDecimal searchRepayTotal(int userId, String borrowNid, BigDecimal borrowApr, String borrowStyle, int periodTotal)
			throws ParseException{
		RepayByTermBean RepayBean = this.calculateRepay(userId, borrowNid, borrowApr, borrowStyle, periodTotal);
		return RepayBean.getRepayAccount().add(RepayBean.getRepayFee());
	}
	
    @Override
    public BigDecimal searchRepayByTermTotal(int userId, String borrowNid, BigDecimal borrowApr, String borrowStyle,
        int periodTotal) throws ParseException {
        BorrowRepay borrowRepay = this.searchRepay(userId, borrowNid);
        BigDecimal repayPlanTotal = new BigDecimal(0);
        // 判断用户的余额是否足够还款
        if (borrowRepay != null) {
            RepayByTermBean repayByTerm = new RepayByTermBean();
            // 获取相应的还款信息
            BeanUtils.copyProperties(borrowRepay, repayByTerm);
            // 计算当前还款期数
            int period = periodTotal - borrowRepay.getRepayPeriod() + 1;
            repayPlanTotal = calculateRepayPlan(repayByTerm, borrowNid, borrowApr, borrowStyle, period);
        }
        return repayPlanTotal;
    }
    
    /***
     * 计算用户分期还款本期应还金额
     * 
     * @param repay
     * @param borrowNid
     * @param borrowApr
     * @param repayTimeStr
     * @param delayDays
     * @throws ParseException
     */
    private BigDecimal calculateRepayPlan(RepayByTermBean repay, String borrowNid, BigDecimal borrowApr,
        String borrowStyle, int period) throws ParseException {

        List<RepayplanRecoverplanBean> borrowRepayPlanDeails = new ArrayList<RepayplanRecoverplanBean>();
        List<BorrowRepayPlan> borrowRepayPlans = searchRepayPlan(repay.getUserId(), borrowNid);
        BigDecimal repayAccountAll = new BigDecimal("0");
        if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
            // 用户实际还款额
            for (int i = 0; i < borrowRepayPlans.size(); i++) {
                RepayplanRecoverplanBean repayPlanDetail = new RepayplanRecoverplanBean();
                BorrowRepayPlan borrowRepayPlan = borrowRepayPlans.get(i);
                if (period == borrowRepayPlan.getRepayPeriod()) {
                    String repayTimeStart = null;
                    if (i == 0) {
                        repayTimeStart = borrowRepayPlan.getCreateTime().toString();
                    } else {
                        repayTimeStart = borrowRepayPlans.get(i - 1).getRepayTime();
                    }
                    // 计算还款期的数据
                    BeanUtils.copyProperties(borrowRepayPlan, repayPlanDetail);
                    this.calculateRecoverPlan(repayPlanDetail, borrowNid, borrowApr, borrowStyle, period,
                            repayTimeStart);
                    borrowRepayPlanDeails.add(repayPlanDetail);
                    repay.setRepayAccount(repayPlanDetail.getRepayAccount());
                    repay.setRepayAccountAll(repayPlanDetail.getRepayAccountAll());
                    repay.setRepayInterest(repayPlanDetail.getRepayInterest());
                    repay.setRepayCapital(repayPlanDetail.getRepayCapital());
                    repay.setRepayFee(repayPlanDetail.getRepayFee());
                    repay.setChargeDays(repayPlanDetail.getChargeDays());
                    repay.setChargeInterest(repayPlanDetail.getChargeInterest());
                    repay.setDelayDays(repayPlanDetail.getDelayDays());
                    repay.setDelayInterest(repayPlanDetail.getDelayInterest());
                    repay.setLateDays(repayPlanDetail.getLateDays());
                    repay.setLateInterest(repayPlanDetail.getLateInterest());
                    repayAccountAll = repayPlanDetail.getRepayAccountAll().add(repayPlanDetail.getRepayFee());
                } else {
                    borrowRepayPlan.setRepayAccountAll(borrowRepayPlan.getRepayAccount());
                    BeanUtils.copyProperties(borrowRepayPlan, repayPlanDetail);
                    List<BorrowRecoverPlan> borrowRecoverPlans =
                            searchBorrowRecoverPlan(borrowNid, borrowRepayPlan.getRepayPeriod());
                    repayPlanDetail.setRecoverPlanList(borrowRecoverPlans);
                    borrowRepayPlanDeails.add(repayPlanDetail);
                }

            }
            repay.setRepayPlanList(borrowRepayPlanDeails);
        }
        return repayAccountAll;
    }
    
    /***
     * 计算用户分期还款本期应还金额
     * 
     * @param repay
     * @param borrowNid
     * @param borrowApr
     * @param repayTimeStr
     * @param delayDays
     * @throws ParseException
     */
    private void calculateRecoverPlan(RepayplanRecoverplanBean borrowRepayPlan, String borrowNid, BigDecimal borrowApr,
        String borrowStyle, int period, String repayTimeStart) throws ParseException {

        int delayDays = borrowRepayPlan.getDelayDays().intValue();
        String repayTimeStr = borrowRepayPlan.getRepayTime();
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
                this.calculateRecoverPlanDelay(borrowRepayPlan, borrowNid, borrowApr, delayDays);
            } else {// 用户逾期还款
                lateDays = -lateDays;
                this.calculateRecoverPlanLate(borrowRepayPlan, borrowNid, borrowApr, delayDays, lateDays);
            }
        } else {// 用户正常或者提前还款
                // 获取提前还款的阀值
            String repayAdvanceDay = this.getBorrowConfig("REPAY_ADVANCE_TIME");
            int advanceDays = distanceDays;
            if (Integer.parseInt(repayAdvanceDay) < advanceDays) {// 用户提前还款
                // 计算用户实际还款总额
                this.calculateRecoverPlanAdvance(borrowRepayPlan, borrowNid, borrowApr, advanceDays, repayTimeStart);
            } else {// 用户正常还款
                    // 计算用户实际还款总额
                this.calculateRecoverPlan(borrowRepayPlan, borrowNid, borrowApr, advanceDays);
            }
        }
    }
    
    /**
     * 统计分期还款用户正常还款的总标
     * 
     * @param repay
     * @param borrowNid
     * @param borrowStyle
     * @param borrowApr
     * @param interestDay
     * @throws ParseException
     */
    private void calculateRecoverPlan(RepayplanRecoverplanBean borrowRepayPlan, String borrowNid, BigDecimal borrowApr,
        int interestDay) throws ParseException {

        List<BorrowRecoverPlan> borrowRecoverPlans =
                searchBorrowRecoverPlan(borrowNid, borrowRepayPlan.getRepayPeriod());
        if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
            for (int j = 0; j < borrowRecoverPlans.size(); j++) {
                borrowRecoverPlans.get(j).setChargeDays(interestDay);
                borrowRecoverPlans.get(j).setAdvanceStatus(0);
            }
            borrowRepayPlan.setRecoverPlanList(borrowRecoverPlans);
        }
        borrowRepayPlan.setChargeDays(interestDay);
        borrowRepayPlan.setRepayAccountAll(borrowRepayPlan.getRepayAccount());
        borrowRepayPlan.setAdvanceStatus(0);
    }

    
    /**
     * 统计分期还款用户提前还款的总标
     * 
     * @param repay
     * @param borrowNid
     * @param borrowApr
     * @param interestDay
     * @return
     * @throws ParseException
     */
    private void calculateRecoverPlanAdvance(RepayplanRecoverplanBean borrowRepayPlan, String borrowNid,
        BigDecimal borrowApr, int advanceDays, String repayTimeStart) throws ParseException {

        int repayPeriod = borrowRepayPlan.getRepayPeriod();
        List<BorrowRecover> borrowRecoverList = this.selectBorrowRecoverList(borrowNid);
        List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrowNid, repayPeriod);
        // 用户实际还款额
        BigDecimal repayTotal = new BigDecimal(0);
        BigDecimal repayChargeInterest = new BigDecimal(0);
        if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
            if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
                for (int i = 0; i < borrowRecoverList.size(); i++) {
                    BorrowRecover borrowRecover = borrowRecoverList.get(i);
                    for (int j = 0; j < borrowRecoverPlans.size(); j++) {
                        BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
                        if (borrowRecover.getNid().equals(borrowRecoverPlan.getNid())
                                && borrowRecover.getUserId().intValue() == borrowRecoverPlan.getUserId().intValue()
                                && borrowRecover.getBorrowNid().equals(borrowRecoverPlan.getBorrowNid())) {
                            String recoverTime =
                                    GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRecoverPlan
                                            .getRecoverTime()));
                            String repayStartTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(repayTimeStart));
                            // 获取这两个时间之间有多少天
                            int totalDays = GetDate.daysBetween(repayStartTime, recoverTime);
                            // 获取未还款前用户能够获取的本息和
                            BigDecimal userAccount = borrowRecoverPlan.getRecoverAccount();
                            // 获取用户出借项目分期后的出借本金
                            BigDecimal userCapital = borrowRecoverPlan.getRecoverCapital();
                            if (borrowRecover.getCreditAmount().intValue() == 0) {
                                // 用户获得的利息
                                // 计算用户实际获得的本息和
                                BigDecimal userAccountFact = new BigDecimal(0);
                                // 计算用户提前还款减少的的利息
                                BigDecimal userChargeInterest = new BigDecimal(0);
                                // 提前还款不应该大于本次计息时间
                                if (totalDays < advanceDays) {
                                    // 计算出借用户实际获得的本息和
                                    userAccountFact =
                                            UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount, userCapital,
                                                    borrowApr, totalDays);
                                    userChargeInterest =
                                            UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrowApr,
                                                    totalDays);
                                } else {
                                    // 计算出借用户实际获得的本息和
                                    userAccountFact =
                                            UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount, userCapital,
                                                    borrowApr, advanceDays);
                                    userChargeInterest =
                                            UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrowApr,
                                                    advanceDays);
                                }
                                borrowRecoverPlans.get(j).setChargeInterest(
                                        userChargeInterest.multiply(new BigDecimal(-1)));
                                repayTotal = repayTotal.add(userAccountFact);
                                repayChargeInterest = repayChargeInterest.add(userChargeInterest);
                            } else {
                                borrowRecoverPlans.get(j).setChargeInterest(new BigDecimal(0));
                                repayTotal = repayTotal.add(userAccount);
                                repayChargeInterest = repayChargeInterest.add(new BigDecimal(0));
                            }
                            borrowRecoverPlans.get(j).setAdvanceStatus(1);
                            borrowRecoverPlans.get(j).setChargeDays(advanceDays);
                        }
                    }
                }
                borrowRepayPlan.setRecoverPlanList(borrowRecoverPlans);
            }
        }
        borrowRepayPlan.setChargeDays(advanceDays);
        borrowRepayPlan.setChargeInterest(repayChargeInterest.multiply(new BigDecimal(-1)));
        borrowRepayPlan.setRepayAccount(repayTotal);
        borrowRepayPlan.setRepayAccountAll(repayTotal);
        borrowRepayPlan.setAdvanceStatus(1);
    }
    
    /**
     * 统计分期还款用户逾期还款的总标
     * 
     * @param repay
     * @param borrowNid
     * @param borrowApr
     * @param delayDays
     * @param lateDays
     * @throws ParseException
     */
    private void calculateRecoverPlanLate(RepayplanRecoverplanBean borrowRepayPlan, String borrowNid,
        BigDecimal borrowApr, int delayDays, int lateDays) throws ParseException {

        List<BorrowRecover> borrowRecoverList = this.selectBorrowRecoverList(borrowNid);
        List<BorrowRecoverPlan> borrowRecoverPlans =
                searchBorrowRecoverPlan(borrowNid, borrowRepayPlan.getRepayPeriod());
        // 统计借款用户还款总额
        BigDecimal userAccountTotal = new BigDecimal(0);
        // 统计借款用户总延期利息
        BigDecimal userDelayInterestTotal = new BigDecimal(0);
        // 统计借款用户总逾期利息
        BigDecimal userOverdueInterestTotal = new BigDecimal(0);
        if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
            if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
                for (int i = 0; i < borrowRecoverList.size(); i++) {
                    BorrowRecover borrowRecover = borrowRecoverList.get(i);
                    for (int j = 0; j < borrowRecoverPlans.size(); j++) {
                        BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
                        if (borrowRecover.getNid().equals(borrowRecoverPlan.getNid())
                                && borrowRecover.getUserId().intValue() == borrowRecoverPlan.getUserId().intValue()
                                && borrowRecover.getBorrowNid().equals(borrowRecoverPlan.getBorrowNid())) {
                            // 获取未还款前用户能够获取的本息和
                            BigDecimal userAccount = borrowRecoverPlan.getRecoverAccount();
                            // 获取用户出借项目分期后的出借本金
                            BigDecimal userCapital = borrowRecoverPlan.getRecoverCapital();
                            if (borrowRecover.getCreditAmount().intValue() == 0) {
                                // 计算用户实际获得的本息和
                                BigDecimal userAccountFact =
                                        UnnormalRepayUtils.overdueRepayPrincipalInterest(userAccount, userCapital,
                                                borrowApr, delayDays, lateDays);
                                // 计算用户逾期利息
                                BigDecimal userOverdueInterest =
                                        UnnormalRepayUtils.overdueRepayOverdueInterest(userAccount, lateDays);
                                // 计算用户延期利息
                                BigDecimal userDelayInterest =
                                        UnnormalRepayUtils.overdueRepayDelayInterest(userCapital, borrowApr, delayDays);
                                // 保存相应的延期数据
                                borrowRecoverPlans.get(j).setDelayInterest(userDelayInterest);
                                borrowRecoverPlans.get(j).setLateInterest(userOverdueInterest);
                                // 统计总和
                                userAccountTotal = userAccountTotal.add(userAccountFact);
                                userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
                                userOverdueInterestTotal = userOverdueInterestTotal.add(userOverdueInterest);
                            } else {
                                // 保存相应的延期数据
                                borrowRecoverPlans.get(j).setDelayInterest(new BigDecimal(0));
                                borrowRecoverPlans.get(j).setLateInterest(new BigDecimal(0));
                                // 统计总和
                                userAccountTotal = userAccountTotal.add(userAccount);
                                userDelayInterestTotal = userDelayInterestTotal.add(new BigDecimal(0));
                                userOverdueInterestTotal = userOverdueInterestTotal.add(new BigDecimal(0));
                            }
                            borrowRecoverPlans.get(j).setDelayDays(delayDays);
                            borrowRecoverPlans.get(j).setLateDays(lateDays);
                            borrowRecoverPlans.get(j).setAdvanceStatus(3);
                        }
                    }
                }
                borrowRepayPlan.setRecoverPlanList(borrowRecoverPlans);
            }
        }
        borrowRepayPlan.setRepayAccountAll(userAccountTotal);
        borrowRepayPlan.setRepayAccount(userAccountTotal);
        borrowRepayPlan.setDelayDays(delayDays);
        borrowRepayPlan.setDelayInterest(userDelayInterestTotal);
        borrowRepayPlan.setLateDays(lateDays);
        borrowRepayPlan.setLateInterest(userOverdueInterestTotal);
        borrowRepayPlan.setAdvanceStatus(3);
    }
    
    /**
     * 统计分期还款用户延期还款的总标
     * 
     * @param repay
     * @param borrowNid
     * @param borrowApr
     * @param delayDays
     * @throws ParseException
     */
    private void calculateRecoverPlanDelay(RepayplanRecoverplanBean borrowRepayPlan, String borrowNid,
        BigDecimal borrowApr, int delayDays) throws ParseException {

        List<BorrowRecover> borrowRecoverList = this.selectBorrowRecoverList(borrowNid);
        List<BorrowRecoverPlan> borrowRecoverPlans =
                searchBorrowRecoverPlan(borrowNid, borrowRepayPlan.getRepayPeriod());
        // 统计借款用户还款总额
        BigDecimal userAccountTotal = new BigDecimal(0);
        // 统计借款用户总延期利息
        BigDecimal userDelayInterestTotal = new BigDecimal(0);
        if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
            if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
                for (int i = 0; i < borrowRecoverList.size(); i++) {
                    BorrowRecover borrowRecover = borrowRecoverList.get(i);
                    for (int j = 0; j < borrowRecoverPlans.size(); j++) {
                        BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
                        if (borrowRecover.getNid().equals(borrowRecoverPlan.getNid())
                                && borrowRecover.getUserId().intValue() == borrowRecoverPlan.getUserId().intValue()
                                && borrowRecover.getBorrowNid().equals(borrowRecoverPlan.getBorrowNid())) {
                            // 获取未还款前用户能够获取的本息和
                            BigDecimal userAccount = borrowRecoverPlan.getRecoverAccount();
                            // 获取用户出借项目分期后的出借本金
                            BigDecimal userCapital = borrowRecoverPlan.getRecoverCapital();
                            if (borrowRecover.getCreditAmount().intValue() == 0) {
                                // 计算用户实际获得的本息和
                                BigDecimal userAccountFact =
                                        UnnormalRepayUtils.delayRepayPrincipalInterest(userAccount, userCapital,
                                                borrowApr, delayDays);
                                // 计算用户延期利息
                                BigDecimal userDelayInterest =
                                        UnnormalRepayUtils.delayRepayInterest(userCapital, borrowApr, delayDays);
                                borrowRecoverPlans.get(j).setDelayInterest(userDelayInterest);
                                // 统计总和
                                userAccountTotal = userAccountTotal.add(userAccountFact);
                                userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
                            } else {
                                borrowRecoverPlans.get(j).setDelayInterest(new BigDecimal(0));
                                // 统计总和
                                userAccountTotal = userAccountTotal.add(userAccount);
                                userDelayInterestTotal = userDelayInterestTotal.add(new BigDecimal(0));
                            }
                            borrowRecoverPlans.get(j).setDelayDays(delayDays);
                            borrowRecoverPlans.get(j).setAdvanceStatus(2);
                        }
                    }
                }
                borrowRepayPlan.setRecoverPlanList(borrowRecoverPlans);
            }
        }
        borrowRepayPlan.setRepayAccountAll(userAccountTotal);
        borrowRepayPlan.setRepayAccount(userAccountTotal);
        borrowRepayPlan.setDelayDays(delayDays);
        borrowRepayPlan.setDelayInterest(userDelayInterestTotal);
        borrowRepayPlan.setAdvanceStatus(2);
    }

    /**
     * 计算多期的总的还款信息
     * 
     * @param userId
     * @param borrowNid
     * @param borrowStyle
     * @param borrowApr
     * @return
     * @throws ParseException
     */
    @Override
    public RepayByTermBean calculateRepayByTerm(int userId, String borrowNid, BigDecimal borrowApr, String borrowStyle,
        int periodTotal) throws ParseException {

        RepayByTermBean repay = new RepayByTermBean();
        // 获取还款总表数据
        BorrowRepay borrowRepay = this.searchRepay(userId, borrowNid);
        // 判断用户的余额是否足够还款
        if (borrowRepay != null) {
            // 获取相应的还款信息
            BeanUtils.copyProperties(borrowRepay, repay);
            repay.setBorrowPeriod(String.valueOf(periodTotal));
            int period = periodTotal - repay.getRepayPeriod() + 1;
            this.calculateRepayPlan(repay, borrowNid, borrowApr, borrowStyle, period);
        }
        return repay;
    }

	/**
	 * 计算单期的总的还款信息
	 * 
	 * @param userId
	 * @param borrowNid
	 * @param borrowStyle
	 * @param borrowApr
	 * @return
	 * @throws ParseException
	 */
	@Override
	public RepayByTermBean calculateRepay(int userId, String borrowNid, BigDecimal borrowApr, String borrowStyle,
			int periodTotal) throws ParseException {

		RepayByTermBean repay = new RepayByTermBean();
		// 获取还款总表数据
		BorrowRepay borrowRepay = this.searchRepay(userId, borrowNid);
		// 判断是否存在还款数据
		if (borrowRepay != null) {
			// 获取相应的还款信息
			BeanUtils.copyProperties(borrowRepay, repay);
			// 计划还款时间
			String repayTimeStr = borrowRepay.getRepayTime();
			// 获取用户申请的延期天数
			int delayDays = borrowRepay.getDelayDays().intValue();
			repay.setBorrowPeriod(String.valueOf(periodTotal));
			// 未分期默认传分期为0
			this.calculateRecover(repay, borrowNid, borrowApr, repayTimeStr, delayDays);
		}
		return repay;
	}


	/**
	 * 计算单期的用户的还款信息
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowStyle
	 * @param borrowApr
	 * @param repayTimeStr
	 * @param delayDays
	 * @throws ParseException
	 */
	private void calculateRecover(RepayByTermBean repay, String borrowNid, BigDecimal borrowApr, String repayTimeStr,
			int delayDays) throws ParseException {
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
				this.calculateRecoverTotalDelay(repay, borrowNid, borrowApr, delayDays);
			} else {// 用户逾期还款
				lateDays = -lateDays;
				this.calculateRecoverTotalLate(repay, borrowNid, borrowApr, delayDays, lateDays);
			}
		} else {// 用户正常或者提前还款
			// 获取提前还款的阀值
			String repayAdvanceDay = this.getBorrowConfig("REPAY_ADVANCE_TIME");
			int advanceDays = distanceDays;
			if (Integer.parseInt(repayAdvanceDay) < advanceDays) {// 用户提前还款
				// 计算用户实际还款总额
				this.calculateRecoverTotalAdvance(repay, borrowNid, borrowApr, advanceDays);
			} else {// 用户正常还款
				// 计算用户实际还款总额
				this.calculateRecoverTotal(repay, borrowNid, borrowApr, advanceDays);
			}
		}
	}

	
	/**
	 * 获取还款总表数据
	 * @param userId
	 * @param borrowNid
	 * @return
	 */
	public BorrowRepay searchRepay(int userId, String borrowNid) {
		// 获取还款总表数据
		BorrowRepayExample borrowRepayExample = new BorrowRepayExample();
		BorrowRepayExample.Criteria borrowRepayCrt = borrowRepayExample.createCriteria();
		borrowRepayCrt.andUserIdEqualTo(userId);
		borrowRepayCrt.andBorrowNidEqualTo(borrowNid);
		List<BorrowRepay> borrowRepays = borrowRepayMapper.selectByExample(borrowRepayExample);
		if (borrowRepays != null && borrowRepays.size() == 1) {
			return borrowRepays.get(0);
		} else {
			return null;
		}
	}


	/**
	 * 统计单期还款用户提前还款的总标
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param interestDay
	 * @throws ParseException
	 */
	private void calculateRecoverTotalAdvance(RepayByTermBean repay, String borrowNid, BigDecimal borrowApr,
			int interestDay) throws ParseException {

		// 用户提前还款
		// 用户实际还款额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 提前还款利息
		BigDecimal repayChargeInterest = new BigDecimal(0);
		List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrowNid);
		if (borrowRecovers != null && borrowRecovers.size() > 0) {
			for (int i = 0; i < borrowRecovers.size(); i++) {
				BorrowRecover borrowRecover = borrowRecovers.get(i);
				String recoverTime = GetDate
						.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRecover.getRecoverTime()));
				String createTime = GetDate.getDateTimeMyTimeInMillis(borrowRecover.getCreateTime());
				// 获取这两个时间之间有多少天
				int totalDays = GetDate.daysBetween(createTime, recoverTime);
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = borrowRecover.getRecoverAccount();
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = borrowRecover.getRecoverCapital();
				if (borrowRecovers.get(i).getCreditAmount().intValue() == 0) {
					// 计算用户实际获得的本息和
					BigDecimal userAccountFact = new BigDecimal(0);
					// 计算用户提前还款减少的的利息
					BigDecimal userChargeInterest = new BigDecimal(0);
					// 提前还款不应该大于本次计息时间
					if (totalDays < interestDay) {
						// 计算出借用户实际获得的本息和
						userAccountFact = UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount, userCapital,
								borrowApr, totalDays);
						// 用户提前还款减少的利息
						userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrowApr,
								totalDays);
					} else {
						// 计算出借用户实际获得的本息和
						userAccountFact = UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount, userCapital,
								borrowApr, interestDay);
						// 用户提前还款减少的利息
						userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrowApr,
								interestDay);
					}
					borrowRecovers.get(i).setChargeInterest(userChargeInterest.multiply(new BigDecimal(-1)));
					// 统计本息总和
					userAccountTotal = userAccountTotal.add(userAccountFact);
					// 统计提前还款减少的利息
					repayChargeInterest = repayChargeInterest.add(userChargeInterest);
				} else {
					borrowRecovers.get(i).setChargeInterest(new BigDecimal("0"));
					// 统计本息总和
					userAccountTotal = userAccountTotal.add(userAccount);
					// 统计提前还款减少的利息
					repayChargeInterest = repayChargeInterest.add(new BigDecimal("0"));
				}
				borrowRecovers.get(i).setAdvanceStatus(1);
				borrowRecovers.get(i).setChargeDays(interestDay);
			}
			repay.setRecoverList(borrowRecovers);
		}
		repay.setRepayAccount(userAccountTotal);
		repay.setRepayAccountAll(userAccountTotal);
		repay.setChargeDays(interestDay);
		repay.setChargeInterest(repayChargeInterest.multiply(new BigDecimal(-1)));
		repay.setAdvanceStatus(1);

	}

	/**
	 * 统计单期还款用户正常还款的总标
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowStyle
	 * @param borrowApr
	 * @param interestDay
	 * @throws ParseException
	 */
	private void calculateRecoverTotal(RepayByTermBean repay, String borrowNid, BigDecimal borrowApr, int interestDay)
			throws ParseException {

		// 正常还款
		List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrowNid);
		if (borrowRecovers != null && borrowRecovers.size() > 0) {
			for (int i = 0; i < borrowRecovers.size(); i++) {
				borrowRecovers.get(i).setChargeDays(interestDay);
				borrowRecovers.get(i).setAdvanceStatus(0);
			}
			repay.setRecoverList(borrowRecovers);
		}
		repay.setChargeDays(interestDay);
		repay.setRepayAccountAll(repay.getRepayAccount());
		repay.setAdvanceStatus(0);
	}


	/**
	 * 统计单期还款用户延期还款的总标
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param delayDays
	 * @throws ParseException
	 */
	private void calculateRecoverTotalDelay(RepayByTermBean repay, String borrowNid, BigDecimal borrowApr,
			int delayDays) throws ParseException {

		// 用户延期
		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrowNid);
		if (borrowRecovers != null && borrowRecovers.size() > 0) {
			for (int i = 0; i < borrowRecovers.size(); i++) {
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = borrowRecovers.get(i).getRecoverAccount();
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = borrowRecovers.get(i).getRecoverCapital();
				if (borrowRecovers.get(i).getCreditAmount().intValue() == 0) {
					// 计算用户实际获得的本息和
					BigDecimal userAccountFact = UnnormalRepayUtils.delayRepayPrincipalInterest(userAccount,
							userCapital, borrowApr, delayDays);
					// 计算用户延期利息
					BigDecimal userDelayInterest = UnnormalRepayUtils.delayRepayInterest(userCapital, borrowApr,
							delayDays);
					borrowRecovers.get(i).setDelayInterest(userDelayInterest);
					// 统计总和
					userAccountTotal = userAccountTotal.add(userAccountFact);
					userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
				} else {
					borrowRecovers.get(i).setDelayInterest(new BigDecimal(0));
					// 统计总和
					userAccountTotal = userAccountTotal.add(userAccount);
					userDelayInterestTotal = userDelayInterestTotal.add(new BigDecimal(0));
				}
				// 用户延期还款
				borrowRecovers.get(i).setAdvanceStatus(2);
				borrowRecovers.get(i).setDelayDays(delayDays);
			}
			repay.setRecoverList(borrowRecovers);
		}
		repay.setRepayAccountAll(userAccountTotal);
		repay.setRepayAccount(userAccountTotal);
		repay.setDelayDays(delayDays);
		repay.setDelayInterest(userDelayInterestTotal);
		repay.setAdvanceStatus(2);
	}

	/**
	 * 统计单期还款用户逾期还款的总标
	 * 
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param delayDays
	 * @param lateDays
	 * @throws ParseException
	 */
	private void calculateRecoverTotalLate(RepayByTermBean repay, String borrowNid, BigDecimal borrowApr, int delayDays,
			int lateDays) throws ParseException {

		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		// 统计借款用户总逾期利息
		BigDecimal userOverdueInterestTotal = new BigDecimal(0);
		List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrowNid);
		if (borrowRecovers != null && borrowRecovers.size() > 0) {
			for (int i = 0; i < borrowRecovers.size(); i++) {
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = borrowRecovers.get(i).getRecoverAccount();
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = borrowRecovers.get(i).getRecoverCapital();
				if (borrowRecovers.get(i).getCreditAmount().intValue() == 0) {
					// 计算用户实际获得的本息和
					BigDecimal userAccountFact = UnnormalRepayUtils.overdueRepayPrincipalInterest(userAccount,
							userCapital, borrowApr, delayDays, lateDays);
					// 计算用户逾期利息
					BigDecimal userOverdueInterest = UnnormalRepayUtils.overdueRepayOverdueInterest(userAccount,
							lateDays);
					// 计算用户延期利息
					BigDecimal userDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(userCapital, borrowApr,
							delayDays);

					borrowRecovers.get(i).setDelayInterest(userDelayInterest);
					borrowRecovers.get(i).setLateInterest(userOverdueInterest);
					// 统计总和
					userAccountTotal = userAccountTotal.add(userAccountFact);
					userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
					userOverdueInterestTotal = userOverdueInterestTotal.add(userOverdueInterest);
				} else {
					borrowRecovers.get(i).setDelayInterest(new BigDecimal(0));
					borrowRecovers.get(i).setLateInterest(new BigDecimal(0));
					// 统计总和
					userAccountTotal = userAccountTotal.add(userAccount);
					userDelayInterestTotal = userDelayInterestTotal.add(new BigDecimal(0));
					userOverdueInterestTotal = userOverdueInterestTotal.add(new BigDecimal(0));
				}
				borrowRecovers.get(i).setDelayDays(delayDays);
				borrowRecovers.get(i).setLateDays(lateDays);
				borrowRecovers.get(i).setAdvanceStatus(3);
			}
			repay.setRecoverList(borrowRecovers);
		}
		repay.setRepayAccountAll(userAccountTotal);
		repay.setRepayAccount(userAccountTotal);
		repay.setDelayDays(delayDays);
		repay.setDelayInterest(userDelayInterestTotal);
		repay.setLateDays(lateDays);
		repay.setLateInterest(userOverdueInterestTotal);
		repay.setAdvanceStatus(3);
	}


	public List<BorrowRepayPlan> searchRepayPlan(int userId, String borrowNid) {
		BorrowRepayPlanExample borrowRepayPlanExample = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria borrowRepayPlanCrt = borrowRepayPlanExample.createCriteria();
		borrowRepayPlanCrt.andUserIdEqualTo(userId);
		borrowRepayPlanCrt.andBorrowNidEqualTo(borrowNid);
		List<BorrowRepayPlan> borrowRepayPlans = borrowRepayPlanMapper.selectByExample(borrowRepayPlanExample);
		return borrowRepayPlans;
	}

	public BorrowRepayPlan searchRepayPlan(int userId, String borrowNid, int period) {
		BorrowRepayPlanExample borrowRepayPlanExample = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria borrowRepayPlanCrt = borrowRepayPlanExample.createCriteria();
		borrowRepayPlanCrt.andUserIdEqualTo(userId);
		borrowRepayPlanCrt.andBorrowNidEqualTo(borrowNid);
		borrowRepayPlanCrt.andRepayPeriodEqualTo(period);
		List<BorrowRepayPlan> borrowRepayPlans = borrowRepayPlanMapper.selectByExample(borrowRepayPlanExample);
		if (borrowRepayPlans != null && borrowRepayPlans.size() == 1) {
			return borrowRepayPlans.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据项目id查询相应的用户的待还款信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	private List<BorrowRecover> searchBorrowRecover(String borrowNid) {
		BorrowRecoverExample example = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		List<BorrowRecover> borrowRecovers = borrowRecoverMapper.selectByExample(example);
		return borrowRecovers;
	}

	/**
	 * 查询出借用户分期的详情
	 * 
	 * @param borrowNid
	 * @param period
	 * @return
	 */
	private List<BorrowRecoverPlan> searchBorrowRecoverPlan(String borrowNid, int period) {
		BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
		BorrowRecoverPlanExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andRecoverPeriodEqualTo(period);
		List<BorrowRecoverPlan> borrowRecovers = borrowRecoverPlanMapper.selectByExample(example);
		return borrowRecovers;
	}

	/**
	 * 获取用户在汇付天下的账号信息
	 *
	 * @return
	 */
	@Override
	public AccountChinapnr getChinapnrUserInfo(Integer userId) {
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
	 * 查询客户在汇付的余额
	 *
	 * @param usrCustId
	 * @return
	 */
	public BigDecimal getUserBalance(Long usrCustId) {
		
		BigDecimal balance = BigDecimal.ZERO;
		// 调用汇付接口(交易状态查询)
		ChinapnrBean bean = new ChinapnrBean();
		// 版本号(必须)
		bean.setVersion(ChinaPnrConstant.VERSION_10);
		// 消息类型(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_BALANCE_BG);
		// 用户客户号(必须)
		bean.setUsrCustId(String.valueOf(usrCustId));
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
	 * 用户还款
	 */
	@Override
	public Map<String, Object> updateRepayMoney(RepayByTermBean repay) {
	    Map<String, Object> returnMap = new HashMap<String, Object>();
	    String repayMsg = "";
		int time = GetDate.getNowTime10();
		String borrowNid = repay.getBorrowNid();
		String periodtotal = repay.getBorrowPeriod();
		int remainRepayPeriod = repay.getRepayPeriod();
		int period = Integer.parseInt(periodtotal) - remainRepayPeriod + 1;
		int userId = repay.getUserId();
		BigDecimal account = repay.getRepayAccount();// 用户实际还款本金
		BigDecimal fee = repay.getRepayFee();// 用户实际还款管理费
		BigDecimal repayAccount = new BigDecimal("0");// 用户应还款金额
		String nid = "";
		Boolean repayFlag = false;
		int errorCount = 0;
		// 不分期还款
		List<BorrowRecover> recoverList = repay.getRecoverList();
		if (recoverList != null && recoverList.size() > 0) {
			// 获取用户本次应还的金额
			BorrowRepay borrowRepay = this.searchRepay(userId, borrowNid);
			repayAccount = borrowRepay.getRepayAccount();
			BorrowApicronExample example = new BorrowApicronExample();
			BorrowApicronExample.Criteria crt = example.createCriteria();
			crt.andBorrowNidEqualTo(borrowNid);
			crt.andApiTypeEqualTo(1);
			List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(example);
			if (borrowApicrons != null && borrowApicrons.size() > 0) {
				BorrowApicron borrowApicron = borrowApicrons.get(0);
				if (borrowApicron.getRepayStatus() == null) {
					boolean borrowRecoverFlag = true;
					for (int i = 0; i < recoverList.size(); i++) {
						BorrowRecover borrowRecover = recoverList.get(i);
						BorrowRecover borrowRecoverOld = borrowRecoverMapper.selectByPrimaryKey(borrowRecover.getId());
						borrowRecoverOld.setAdvanceStatus(borrowRecover.getAdvanceStatus());
						borrowRecoverOld.setChargeDays(borrowRecover.getChargeDays());
						borrowRecoverOld.setChargeInterest(borrowRecover.getChargeInterest());
						borrowRecoverOld.setDelayDays(borrowRecover.getDelayDays());
						borrowRecoverOld.setDelayInterest(borrowRecover.getDelayInterest());
						borrowRecoverOld.setLateDays(borrowRecover.getLateDays());
						borrowRecoverOld.setLateInterest(borrowRecover.getLateInterest());
						boolean flag = borrowRecoverMapper.updateByPrimaryKey(borrowRecoverOld) > 0 ? true : false;
						if (!flag) {
							errorCount = errorCount + 1;
						}
						borrowRecoverFlag = borrowRecoverFlag && flag;
					}
					if (borrowRecoverFlag) {
						borrowApicron.setPeriodNow(1);
						borrowApicron.setRepayStatus(0);
						int updateTime = borrowApicron.getUpdateTime();
						borrowApicron.setUpdateTime(GetDate.getNowTime10());
						crt.andUpdateTimeEqualTo(updateTime);
						boolean apicronFlag = borrowApicronMapper.updateByExampleWithBLOBs(borrowApicron, example) > 0? true : false;
						if (!apicronFlag) {
							//throw new RuntimeException("重复还款");
							repayMsg = "重复还款";
							returnMap.put("success", repayFlag);
							returnMap.put("msg", repayMsg);
							return returnMap;
						} else {
							repayFlag = true;
							updateAccount(userId, nid, account, fee, repayAccount, borrowNid, period, time, repay);
							repayMsg = "还款完成";
							returnMap.put("success", repayFlag);
                            returnMap.put("msg", repayMsg);
                            return returnMap;
						}
					} else {
						//throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
						repayMsg = "还款失败！" + "失败数量【" + errorCount + "】";
						returnMap.put("success", repayFlag);
                        returnMap.put("msg", repayMsg);
                        return returnMap;
					}
				} else {
					repayFlag = true;
					updateAccount(userId, nid, account, fee, repayAccount, borrowNid, period, time, repay);
					repayMsg = "还款完成";
					returnMap.put("success", repayFlag);
                    returnMap.put("msg", repayMsg);
                    return returnMap;
				}
			} else {
				boolean borrowRecoverFlag = true;
				for (int i = 0; i < recoverList.size(); i++) {
					BorrowRecover borrowRecover = recoverList.get(i);
					BorrowRecover borrowRecoverOld = borrowRecoverMapper.selectByPrimaryKey(borrowRecover.getId());
					// 出借人信息
					Integer tenderUserId = borrowRecoverOld.getUserId();
					Users users = getUsers(tenderUserId);
					if (users != null) {
						// 获取出借人属性
						UsersInfo userInfo = getUsersInfoByUserId(tenderUserId);
						// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
						Integer attribute = null;
						if (userInfo != null) {
							// 获取出借用户的用户属性
							attribute = userInfo.getAttribute();
							if (attribute != null) {
								// 出借人用户属性
								borrowRecoverOld.setTenderUserAttribute(attribute);
								// 如果是线上员工或线下员工，推荐人的userId和username不插
								if (attribute == 2 || attribute == 3) {
									EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(tenderUserId);
									if (employeeCustomize != null) {
										borrowRecoverOld.setInviteRegionId(employeeCustomize.getRegionId());
										borrowRecoverOld.setInviteRegionName(employeeCustomize.getRegionName());
										borrowRecoverOld.setInviteBranchId(employeeCustomize.getBranchId());
										borrowRecoverOld.setInviteBranchName(employeeCustomize.getBranchName());
										borrowRecoverOld.setInviteDepartmentId(employeeCustomize.getDepartmentId());
										borrowRecoverOld.setInviteDepartmentName(employeeCustomize.getDepartmentName());
									}
								} else if (attribute == 1) {
									SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
									SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
									spreadsUsersExampleCriteria.andUserIdEqualTo(borrowRecoverOld.getUserId());
									List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
									if (sList != null && sList.size() == 1) {
										int refUserId = sList.get(0).getSpreadsUserid();
										// 查找用户推荐人
										Users userss = getUsers(refUserId);
										if (userss != null) {
											borrowRecoverOld.setInviteUserId(userss.getUserId());
											borrowRecoverOld.setInviteUserName(userss.getUsername());
										}
										// 推荐人信息
										UsersInfo refUsers = getUsersInfoByUserId(refUserId);
										// 推荐人用户属性
										if (refUsers != null) {
											borrowRecoverOld.setInviteUserAttribute(refUsers.getAttribute());
										}
										// 查找用户推荐人部门
										EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
										if (employeeCustomize != null) {
											borrowRecoverOld.setInviteRegionId(employeeCustomize.getRegionId());
											borrowRecoverOld.setInviteRegionName(employeeCustomize.getRegionName());
											borrowRecoverOld.setInviteBranchId(employeeCustomize.getBranchId());
											borrowRecoverOld.setInviteBranchName(employeeCustomize.getBranchName());
											borrowRecoverOld.setInviteDepartmentId(employeeCustomize.getDepartmentId());
											borrowRecoverOld.setInviteDepartmentName(employeeCustomize.getDepartmentName());
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
										Users userss = getUsers(refUserId);
										if (userss != null) {
											borrowRecoverOld.setInviteUserId(userss.getUserId());
											borrowRecoverOld.setInviteUserName(userss.getUsername());
										}
										// 推荐人信息
										UsersInfo refUsers = getUsersInfoByUserId(refUserId);
										// 推荐人用户属性
										if (refUsers != null) {
											borrowRecoverOld.setInviteUserAttribute(refUsers.getAttribute());
										}
									}
								}
							}
						}
					}
					borrowRecoverOld.setAdvanceStatus(borrowRecover.getAdvanceStatus());
					borrowRecoverOld.setChargeDays(borrowRecover.getChargeDays());
					borrowRecoverOld.setChargeInterest(borrowRecover.getChargeInterest());
					borrowRecoverOld.setDelayDays(borrowRecover.getDelayDays());
					borrowRecoverOld.setDelayInterest(borrowRecover.getDelayInterest());
					borrowRecoverOld.setLateDays(borrowRecover.getLateDays());
					borrowRecoverOld.setLateInterest(borrowRecover.getLateInterest());
					boolean flag = borrowRecoverMapper.updateByPrimaryKey(borrowRecoverOld) > 0 ? true : false;
					if (!flag) {
						errorCount = errorCount + 1;
					}
					borrowRecoverFlag = borrowRecoverFlag && flag;
				}
				if (borrowRecoverFlag) {
					int nowTime = GetDate.getNowTime10();
					BorrowApicron borrowApicron = new BorrowApicron();
					borrowApicron.setUserId(repay.getUserId());
					borrowApicron.setBorrowNid(borrowNid);
					nid = repay.getBorrowNid() + "_" + repay.getUserId() + "_1";
					borrowApicron.setNid(nid);
					borrowApicron.setApiType(1);
					borrowApicron.setPeriodNow(1);
					borrowApicron.setRepayStatus(0);
					borrowApicron.setStatus(1);
					borrowApicron.setCreditRepayStatus(0);
					borrowApicron.setCreateTime(nowTime);
					borrowApicron.setUpdateTime(nowTime);
					boolean apiCronFlag = borrowApicronMapper.insertSelective(borrowApicron) > 0 ? true : false;
					if (apiCronFlag) {
						repayFlag =  true;
						updateAccount(userId, nid, account, fee, repayAccount, borrowNid, period, time, repay);
						repayMsg = "还款完成";
						returnMap.put("success", repayFlag);
                        returnMap.put("msg", repayMsg);
                        return returnMap;
					}
				} else {
					//throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
					repayMsg = "还款失败！" + "失败数量【" + errorCount + "】";
					returnMap.put("success", repayFlag);
                    returnMap.put("msg", repayMsg);
                    return returnMap;
				}
			}
		}
		List<RepayplanRecoverplanBean> repayPLanList = repay.getRepayPlanList();
		// 分期还款
		if (repayPLanList != null && repayPLanList.size() > 0) {
			for (int i = 0; i < repayPLanList.size(); i++) {
				RepayplanRecoverplanBean borrowRepayPlan = repayPLanList.get(i);
				if (borrowRepayPlan.getRepayPeriod() == period) {
					BorrowRepayPlan borrowRepay = this.searchRepayPlan(userId, borrowNid, period);
					repayAccount = borrowRepay.getRepayAccount();
					BorrowApicronExample example = new BorrowApicronExample();
					BorrowApicronExample.Criteria crt = example.createCriteria();
					crt.andBorrowNidEqualTo(borrowNid);
					crt.andApiTypeEqualTo(1);
					crt.andPeriodNowEqualTo(period);
					List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(example);
					if (borrowApicrons != null && borrowApicrons.size() > 0) {
						BorrowApicron borrowApicron = borrowApicrons.get(0);
						if (borrowApicron.getRepayStatus() == null) {
							boolean borrowRecoverPlanFlag = true;
							List<BorrowRecoverPlan> borrowRecoverPLans = borrowRepayPlan.getRecoverPlanList();
							if (borrowRecoverPLans != null && borrowRecoverPLans.size() > 0) {
								for (int j = 0; j < borrowRecoverPLans.size(); j++) {
									BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPLans.get(j);
									BorrowRecoverPlan borrowRecoverPlanOld = borrowRecoverPlanMapper.selectByPrimaryKey(borrowRecoverPlan.getId());

									borrowRecoverPlanOld.setAdvanceStatus(borrowRecoverPlan.getAdvanceStatus());
									borrowRecoverPlanOld.setChargeDays(borrowRecoverPlan.getChargeDays());
									borrowRecoverPlanOld.setChargeInterest(borrowRecoverPlan.getChargeInterest());
									borrowRecoverPlanOld.setDelayDays(borrowRecoverPlan.getDelayDays());
									borrowRecoverPlanOld.setDelayInterest(borrowRecoverPlan.getDelayInterest());
									borrowRecoverPlanOld.setLateDays(borrowRecoverPlan.getLateDays());
									borrowRecoverPlanOld.setLateInterest(borrowRecoverPlan.getLateInterest());
									boolean flag = borrowRecoverPlanMapper.updateByPrimaryKey(borrowRecoverPlanOld) > 0 ? true : false;
									if (!flag) {
										errorCount = errorCount + 1;
									}
									borrowRecoverPlanFlag = borrowRecoverPlanFlag && flag;
								}
							}
							if (borrowRecoverPlanFlag) {
								borrowApicron.setPeriodNow(period);
								borrowApicron.setRepayStatus(0);
								int updateTime = borrowApicron.getUpdateTime();
								borrowApicron.setUpdateTime(GetDate.getNowTime10());
								crt.andUpdateTimeEqualTo(updateTime);
								boolean apiCronFlag = borrowApicronMapper.updateByExampleWithBLOBs(borrowApicron,
										example) > 0 ? true : false;
								if (apiCronFlag) {
									repayFlag = true;
									updateAccount(userId, nid, account, fee, repayAccount, borrowNid, period, updateTime, repay);
									repayMsg = "还款完成";
									returnMap.put("success", repayFlag);
		                            returnMap.put("msg", repayMsg);
		                            return returnMap;
								} else {
									//throw new RuntimeException("重复还款");
									repayMsg = "重复还款";
									returnMap.put("success", repayFlag);
		                            returnMap.put("msg", repayMsg);
		                            return returnMap;
								}
							} else {
								//throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
								repayMsg = "还款失败！" + "失败数量【" + errorCount + "】";
								returnMap.put("success", repayFlag);
	                            returnMap.put("msg", repayMsg);
	                            return returnMap;
							}
						} else {
							repayFlag =  true;
							repayMsg = "还款完成";
							updateAccount(userId, nid, account, fee, repayAccount, borrowNid, period, time, repay);
							returnMap.put("success", repayFlag);
                            returnMap.put("msg", repayMsg);
                            return returnMap;
						}
					} else {
						boolean borrowRecoverPlanFlag = true;
						List<BorrowRecoverPlan> borrowRecoverPLans = borrowRepayPlan.getRecoverPlanList();
						if (borrowRecoverPLans != null && borrowRecoverPLans.size() > 0) {
							for (int j = 0; j < borrowRecoverPLans.size(); j++) {
								BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPLans.get(j);
								BorrowRecoverPlan borrowRecoverPlanOld = borrowRecoverPlanMapper.selectByPrimaryKey(borrowRecoverPlan.getId());
								
								// 出借人信息
								Integer tenderUserId = borrowRecoverPlanOld.getUserId();
								Users users = getUsers(tenderUserId);
								if (users != null) {
									// 获取出借人属性
									UsersInfo userInfo = getUsersInfoByUserId(tenderUserId);
									// 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
									Integer attribute = null;
									if (userInfo != null) {
										// 获取出借用户的用户属性
										attribute = userInfo.getAttribute();
										if (attribute != null) {
											// 出借人用户属性
											borrowRecoverPlanOld.setTenderUserAttribute(attribute);
											// 如果是线上员工或线下员工，推荐人的userId和username不插
											if (attribute == 2 || attribute == 3) {
												EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(tenderUserId);
												if (employeeCustomize != null) {
													borrowRecoverPlanOld.setInviteRegionId(employeeCustomize.getRegionId());
													borrowRecoverPlanOld.setInviteRegionName(employeeCustomize.getRegionName());
													borrowRecoverPlanOld.setInviteBranchId(employeeCustomize.getBranchId());
													borrowRecoverPlanOld.setInviteBranchName(employeeCustomize.getBranchName());
													borrowRecoverPlanOld.setInviteDepartmentId(employeeCustomize.getDepartmentId());
													borrowRecoverPlanOld.setInviteDepartmentName(employeeCustomize.getDepartmentName());
												}
											} else if (attribute == 1) {
												SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
												SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
												spreadsUsersExampleCriteria.andUserIdEqualTo(tenderUserId);
												List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
												if (sList != null && sList.size() == 1) {
													int refUserId = sList.get(0).getSpreadsUserid();
													// 查找用户推荐人
													Users userss = getUsers(refUserId);
													if (userss != null) {
														borrowRecoverPlanOld.setInviteUserId(userss.getUserId());
														borrowRecoverPlanOld.setInviteUserName(userss.getUsername());
													}
													// 推荐人信息
													UsersInfo refUsers = getUsersInfoByUserId(refUserId);
													// 推荐人用户属性
													if (refUsers != null) {
														borrowRecoverPlanOld.setInviteUserAttribute(refUsers.getAttribute());
													}
													// 查找用户推荐人部门
													EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
													if (employeeCustomize != null) {
														borrowRecoverPlanOld.setInviteRegionId(employeeCustomize.getRegionId());
														borrowRecoverPlanOld.setInviteRegionName(employeeCustomize.getRegionName());
														borrowRecoverPlanOld.setInviteBranchId(employeeCustomize.getBranchId());
														borrowRecoverPlanOld.setInviteBranchName(employeeCustomize.getBranchName());
														borrowRecoverPlanOld.setInviteDepartmentId(employeeCustomize.getDepartmentId());
														borrowRecoverPlanOld.setInviteDepartmentName(employeeCustomize.getDepartmentName());
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
													Users userss = getUsers(refUserId);
													if (userss != null) {
														borrowRecoverPlanOld.setInviteUserId(userss.getUserId());
														borrowRecoverPlanOld.setInviteUserName(userss.getUsername());
													}
													// 推荐人信息
													UsersInfo refUsers = getUsersInfoByUserId(refUserId);
													// 推荐人用户属性
													if (refUsers != null) {
														borrowRecoverPlanOld.setInviteUserAttribute(refUsers.getAttribute());
													}
												}
											}
										}
									}
								}
								borrowRecoverPlanOld.setAdvanceStatus(borrowRecoverPlan.getAdvanceStatus());
								borrowRecoverPlanOld.setChargeDays(borrowRecoverPlan.getChargeDays());
								borrowRecoverPlanOld.setChargeInterest(borrowRecoverPlan.getChargeInterest());
								borrowRecoverPlanOld.setDelayDays(borrowRecoverPlan.getDelayDays());
								borrowRecoverPlanOld.setDelayInterest(borrowRecoverPlan.getDelayInterest());
								borrowRecoverPlanOld.setLateDays(borrowRecoverPlan.getLateDays());
								borrowRecoverPlanOld.setLateInterest(borrowRecoverPlan.getLateInterest());
								boolean flag = borrowRecoverPlanMapper.updateByPrimaryKey(borrowRecoverPlanOld) > 0 ? true : false;
								if (!flag) {
									errorCount = errorCount + 1;
								}
								borrowRecoverPlanFlag = borrowRecoverPlanFlag && flag;
							}
						}
						if (borrowRecoverPlanFlag) {
							int nowTime = GetDate.getNowTime10();
							BorrowApicron borrowApicron = new BorrowApicron();
							borrowApicron.setUserId(borrowRepayPlan.getUserId());
							nid = repay.getBorrowNid() + "_" + repay.getUserId() + "_" + period;
							borrowApicron.setNid(nid);
							borrowApicron.setBorrowNid(borrowNid);
							borrowApicron.setApiType(1);
							borrowApicron.setPeriodNow(period);
							borrowApicron.setRepayStatus(0);
							borrowApicron.setStatus(1);
							borrowApicron.setCreditRepayStatus(0);
							borrowApicron.setCreateTime(nowTime);
							borrowApicron.setUpdateTime(nowTime);
							boolean apiCronFlag = borrowApicronMapper.insertSelective(borrowApicron) > 0 ? true : false;
							if (apiCronFlag) {
								repayFlag = true;
								repayMsg = "还款完成";
								updateAccount(userId, nid, account, fee, repayAccount, borrowNid, period, time, repay);
								returnMap.put("success", repayFlag);
	                            returnMap.put("msg", repayMsg);
	                            return returnMap;
							} else {
								//throw new RuntimeException("重复还款");
								repayMsg = "重复还款";
								returnMap.put("success", repayFlag);
	                            returnMap.put("msg", repayMsg);
	                            return returnMap;
							}
						} else {
							//throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
							repayMsg = "还款失败！" + "失败数量【" + errorCount + "】";
							returnMap.put("success", repayFlag);
                            returnMap.put("msg", repayMsg);
                            return returnMap;
						}
					}
				}
			}
		}
		if(repayFlag){
			if (countRepayAccountListByNid(nid) == 0) {
				// 更新account表
				BigDecimal frost = new BigDecimal(0);// 冻结金额
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
								System.out.println("用户:" + userId + "***********************************扣除相应的还款金额account："+ JSON.toJSONString(accountBean));
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
									System.out.println("用户:" + userId + "***********************************预插入accountList："
											+ JSON.toJSONString(accountListRecord));
									boolean accountListFlag = this.accountListMapper.insertSelective(accountListRecord) > 0 ? true: false;
									if (accountListFlag) {
										// 写入account_log日志
										AccountLog accountLog = new AccountLog();
										accountLog.setUserId(userId);// 操作用户id
										accountLog.setNid("repay_freeze" + "_" + borrowNid + "_" + userId + "_" + period);
										accountLog.setTotalOld(BigDecimal.ZERO);
										accountLog.setCode("borrow");
										accountLog.setCodeType("repay_freeze");
										accountLog.setCodeNid(borrowNid + "_" + userId + "_" + period);
										accountLog.setBorrowNid(borrowNid);// 收入
										accountLog.setIncomeOld(BigDecimal.ZERO);
										accountLog.setIncomeNew(BigDecimal.ZERO);
										accountLog.setAccountWebStatus(0);
										accountLog.setAccountUserStatus(0);
										accountLog.setAccountType("");
										accountLog.setMoney(account);// 操作金额
										accountLog.setIncome(BigDecimal.ZERO);// 收入
										accountLog.setExpend(BigDecimal.ZERO);// 支出
										accountLog.setExpendNew(BigDecimal.ZERO);
										accountLog.setBalanceOld(BigDecimal.ZERO);
										accountLog.setBalanceNew(BigDecimal.ZERO);
										accountLog.setBalanceCash(BigDecimal.ZERO);
										accountLog.setBalanceCashNew(BigDecimal.ZERO);
										accountLog.setBalanceCashOld(BigDecimal.ZERO);
										accountLog.setExpendOld(BigDecimal.ZERO);
										accountLog.setBalanceCash(BigDecimal.ZERO);// 可提现金额
										accountLog.setBalanceFrost(account.multiply(new BigDecimal(-1)));// 不可提现金额
										accountLog.setFrost(frost);// 冻结金额
										accountLog.setFrostOld(BigDecimal.ZERO);
										accountLog.setFrostNew(BigDecimal.ZERO);
										accountLog.setAwait(BigDecimal.ZERO);// 待收金额
										accountLog.setRepay(BigDecimal.ZERO);// 待还金额
										accountLog.setRepayOld(BigDecimal.ZERO);
										accountLog.setRepayNew(BigDecimal.ZERO);
										accountLog.setAwait(BigDecimal.ZERO);
										accountLog.setAwaitNew(BigDecimal.ZERO);
										accountLog.setAwaitOld(BigDecimal.ZERO);
										accountLog.setType("repay_freeze");// 类型
										accountLog.setToUserid(userId); // 付给谁
										accountLog.setRemark("还款[" + borrowNid + "]扣除资金");// 备注
										accountLog.setAddtime(String.valueOf(time));
										accountLog.setAddip(repay.getIp());
										accountLog.setBalanceFrostNew(BigDecimal.ZERO);
										accountLog.setBalanceFrostOld(BigDecimal.ZERO);
										System.out.println("用户:" + userId + "***********************************预插入accountLog："
												+ JSON.toJSONString(accountLog));
										boolean accountLogFlag = this.accountLogMapper.insertSelective(accountLog) > 0 ? true : false;
										if (accountLogFlag) {
										    repayFlag = true;
										    repayMsg = "还款完成";
										    returnMap.put("success", repayFlag);
				                            returnMap.put("msg", repayMsg);
				                            return returnMap;
										} else {
											//throw new RuntimeException("还款失败！" + "插入借款人交易明细日志表accountLog失败！");
											repayMsg = "还款失败！" + "插入借款人交易明细日志表accountLog失败！";
											returnMap.put("success", repayFlag);
				                            returnMap.put("msg", repayMsg);
				                            return returnMap;
										}
									} else {
										//throw new RuntimeException("还款失败！" + "插入借款人交易明细表AccountList失败！");
										repayMsg = "还款失败！" + "插入借款人交易明细表AccountList失败！";
										returnMap.put("success", repayFlag);
			                            returnMap.put("msg", repayMsg);
			                            return returnMap;
									}
								} else {
									//throw new RuntimeException("还款失败！" + "更新借款人账户余额表Account失败！");
									repayMsg = "还款失败！" + "更新借款人账户余额表Account失败！";
									returnMap.put("success", repayFlag);
		                            returnMap.put("msg", repayMsg);
		                            return returnMap;
								}
							} else {
								//throw new RuntimeException("用户汇付账户余额不足!");
								repayMsg = "用户汇付账户余额不足!";
								returnMap.put("success", repayFlag);
	                            returnMap.put("msg", repayMsg);
	                            return returnMap;
							}
						} else {
							//throw new RuntimeException("用户开户信息不存在!");
							repayMsg = "用户开户信息不存在!";
							returnMap.put("success", repayFlag);
                            returnMap.put("msg", repayMsg);
                            return returnMap;
						}
					} else {
						//throw new RuntimeException("用户余额不足,还款失败");
						repayMsg = "用户余额不足,还款失败";
						returnMap.put("success", repayFlag);
                        returnMap.put("msg", repayMsg);
                        return returnMap;
					}
				} else {
					//throw new RuntimeException("未查询到用户的账户信息，account表查询失败");
					repayMsg = "未查询到用户的账户信息，account表查询失败";
					returnMap.put("success", repayFlag);
                    returnMap.put("msg", repayMsg);
                    return returnMap;
				}
			}else{
				//throw new RuntimeException("此笔还款的交易明细已存在,请勿重复还款");
				repayMsg = "此笔还款的交易明细已存在,请勿重复还款";
				returnMap.put("success", repayFlag);
                returnMap.put("msg", repayMsg);
                return returnMap;
			}
		}else{
			//throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
			repayMsg = "还款失败！" + "失败数量【" + errorCount + "】";
			returnMap.put("success", repayFlag);
            returnMap.put("msg", repayMsg);
            return returnMap;
		}
	}

    /**
     * 根据项目编号，出借用户，订单号获取用户的放款总记录
     * 
     * @param borrowNid
     * @param userId
     * @param nid
     * @return
     */
    private List<BorrowRecover> selectBorrowRecoverList(String borrowNid) {
        BorrowRecoverExample example = new BorrowRecoverExample();
        BorrowRecoverExample.Criteria crt = example.createCriteria();
        crt.andBorrowNidEqualTo(borrowNid);
        List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(example);
        return borrowRecoverList;
    }
	
	/**
	 * 判断该收支明细是否存在
	 *
	 * @param accountList
	 * @return
	 */
	private int countRepayAccountListByNid(String nid) {
		AccountListExample accountListExample = new AccountListExample();
		accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("repay_success");
		return this.accountListMapper.countByExample(accountListExample);
	}
	
	/**
	 * 更新用户在平台账户的余额
	 * 
	 */
	private void updateAccount(int userId,String nid,BigDecimal account,BigDecimal fee,BigDecimal repayAccount,String borrowNid,int period,int time,RepayByTermBean repay){
		if (countRepayAccountListByNid(nid) == 0) {
			// 更新account表
			BigDecimal frost = new BigDecimal(0);// 冻结金额
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
							System.out.println("用户:" + userId + "***********************************扣除相应的还款金额account："+ JSON.toJSONString(accountBean));
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
								System.out.println("用户:" + userId + "***********************************预插入accountList："
										+ JSON.toJSONString(accountListRecord));
								boolean accountListFlag = this.accountListMapper.insertSelective(accountListRecord) > 0 ? true: false;
								if (accountListFlag) {
									// 写入account_log日志
									AccountLog accountLog = new AccountLog();
									accountLog.setUserId(userId);// 操作用户id
									accountLog.setNid("repay_freeze" + "_" + borrowNid + "_" + userId + "_" + period);
									accountLog.setTotalOld(BigDecimal.ZERO);
									accountLog.setCode("borrow");
									accountLog.setCodeType("repay_freeze");
									accountLog.setCodeNid(borrowNid + "_" + userId + "_" + period);
									accountLog.setBorrowNid(borrowNid);// 收入
									accountLog.setIncomeOld(BigDecimal.ZERO);
									accountLog.setIncomeNew(BigDecimal.ZERO);
									accountLog.setAccountWebStatus(0);
									accountLog.setAccountUserStatus(0);
									accountLog.setAccountType("");
									accountLog.setMoney(account);// 操作金额
									accountLog.setIncome(BigDecimal.ZERO);// 收入
									accountLog.setExpend(BigDecimal.ZERO);// 支出
									accountLog.setExpendNew(BigDecimal.ZERO);
									accountLog.setBalanceOld(BigDecimal.ZERO);
									accountLog.setBalanceNew(BigDecimal.ZERO);
									accountLog.setBalanceCash(BigDecimal.ZERO);
									accountLog.setBalanceCashNew(BigDecimal.ZERO);
									accountLog.setBalanceCashOld(BigDecimal.ZERO);
									accountLog.setExpendOld(BigDecimal.ZERO);
									accountLog.setBalanceCash(BigDecimal.ZERO);// 可提现金额
									accountLog.setBalanceFrost(account.multiply(new BigDecimal(-1)));// 不可提现金额
									accountLog.setFrost(frost);// 冻结金额
									accountLog.setFrostOld(BigDecimal.ZERO);
									accountLog.setFrostNew(BigDecimal.ZERO);
									accountLog.setAwait(BigDecimal.ZERO);// 待收金额
									accountLog.setRepay(BigDecimal.ZERO);// 待还金额
									accountLog.setRepayOld(BigDecimal.ZERO);
									accountLog.setRepayNew(BigDecimal.ZERO);
									accountLog.setAwait(BigDecimal.ZERO);
									accountLog.setAwaitNew(BigDecimal.ZERO);
									accountLog.setAwaitOld(BigDecimal.ZERO);
									accountLog.setType("repay_freeze");// 类型
									accountLog.setToUserid(userId); // 付给谁
									accountLog.setRemark("还款[" + borrowNid + "]扣除资金");// 备注
									accountLog.setAddtime(String.valueOf(time));
									accountLog.setAddip(repay.getIp());
									accountLog.setBalanceFrostNew(BigDecimal.ZERO);
									accountLog.setBalanceFrostOld(BigDecimal.ZERO);
									System.out.println("用户:" + userId + "***********************************预插入accountLog："
											+ JSON.toJSONString(accountLog));
								} 
							} 
						} 
					} 
				} 
			} 
		}
	}

	
}



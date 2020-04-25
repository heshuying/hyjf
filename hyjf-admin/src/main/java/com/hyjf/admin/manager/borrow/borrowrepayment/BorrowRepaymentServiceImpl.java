package com.hyjf.admin.manager.borrow.borrowrepayment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.InterestInfo;
import com.hyjf.common.calculate.UnnormalRepayUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
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
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.customize.BorrowRepaymentCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminRepayDelayCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class BorrowRepaymentServiceImpl extends BaseServiceImpl implements BorrowRepaymentService {

	/**
	 * 出借明细列表
	 *
	 * @param borrowRepaymentCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<BorrowRepaymentCustomize> selectBorrowRepaymentList(BorrowRepaymentCustomize borrowRepaymentCustomize) {
		return this.borrowRepaymentCustomizeMapper.selectBorrowRepaymentList(borrowRepaymentCustomize);
	}
	/**
	 * 出借明细记录 总数COUNT
	 *
	 * @param borrowRepaymentCustomize
	 * @return
	 */
	public BorrowRepaymentCustomize sumBorrowRepaymentInfo(BorrowRepaymentCustomize borrowRepaymentCustomize) {
		return this.borrowRepaymentCustomizeMapper.sumBorrowRepayment(borrowRepaymentCustomize);
	}
	
	/**
	 * 统计合计
	 *
	 * @param borrowRepaymentCustomize
	 * @return
	 */
	public Long countBorrowRepayment(BorrowRepaymentCustomize borrowRepaymentCustomize) {
		return this.borrowRepaymentCustomizeMapper.countBorrowRepayment(borrowRepaymentCustomize);
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
	 * 延期画面初始化
	 *
	 * @param borrowNid
	 * @return
	 */
	public AdminRepayDelayCustomize selectBorrowInfo(String borrowNid) {
		AdminRepayDelayCustomize repayDelayCustomize = new AdminRepayDelayCustomize();
		repayDelayCustomize.setBorrowNid(borrowNid);
		AdminRepayDelayCustomize repayDelay = adminRepayDelayCustomizeMapper.selectBorrowInfo(repayDelayCustomize);
		return repayDelay;
	}

	private BorrowRepay getBorrowRepay(String borrowNid){
		BorrowRepayExample example = new BorrowRepayExample();
		BorrowRepayExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<BorrowRepay> list = this.borrowRepayMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new BorrowRepay();
	}

	private BorrowRepayPlan getBorrowRepayPlan(String borrowNid){
		BorrowRepayPlanExample example = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		cra.andRepayTypeEqualTo("wait");
		cra.andRepayStatusEqualTo(0);
		example.setOrderByClause(" repay_period ASC ");

		List<BorrowRepayPlan> list = this.borrowRepayPlanMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new BorrowRepayPlan();
	}

	@Override
	public BorrowRepayBean getBorrowRepayDelay(String borrowNid, String borrowApr, String borrowStyle)
			throws ParseException {

		BorrowRepayExample example = new BorrowRepayExample();
		BorrowRepayExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<BorrowRepay> list = this.borrowRepayMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			BorrowRepayBean borrowRepayBean = new BorrowRepayBean();
			BorrowRepay borrowRepay = list.get(0);
			BeanUtils.copyProperties(borrowRepay, borrowRepayBean);
			Date nowDate = new Date();
			Date date = new Date(Long.valueOf(borrowRepay.getRepayTime()) * 1000L);
			int distanceDays = GetDate.daysBetween(nowDate, date);
			// 提前还款
			if (distanceDays >= 0) {
				return borrowRepayBean;
			} else {
				// 延迟天数
				int delayDays = borrowRepayBean.getDelayDays().intValue();
				int lateDays = delayDays + distanceDays;
				// 用户延期还款（未逾期）
				if (lateDays >= 0) {
					delayDays = -distanceDays;
					calculateRepayDelay(borrowRepayBean, borrowNid, new BigDecimal(borrowApr), delayDays);
				} else {
					lateDays = -lateDays;
					// 用户逾期还款
					calculateRepayLate(borrowRepayBean, borrowNid, new BigDecimal(borrowApr), delayDays, lateDays);
				}
			}
			return borrowRepayBean;
		}
		return new BorrowRepayBean();
	}

	@Override
	public BorrowRepayPlanBean getBorrowRepayPlanDelay(String borrowNid, String borrowApr, String borrowStyle)
			throws ParseException {
		BorrowRepayPlanExample example = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		cra.andRepayTypeEqualTo("wait");
		cra.andRepayStatusEqualTo(0);
		example.setOrderByClause(" repay_period ASC ");
		List<BorrowRepayPlan> list = this.borrowRepayPlanMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			BorrowRepayPlanBean borrowRepayBean = new BorrowRepayPlanBean();
			BorrowRepayPlan repayPlan = list.get(0);
			BeanUtils.copyProperties(repayPlan, borrowRepayBean);
			Date nowDate = new Date();
			Date date = new Date(Long.valueOf(repayPlan.getRepayTime()) * 1000L);
			// 获取实际还款同计划还款时间的时间差
			int distanceDays = GetDate.daysBetween(nowDate, date);
			// 提前还款
			if (distanceDays >= 0) {
				return borrowRepayBean;
			} else {
				// 延迟天数
				int delayDays = repayPlan.getDelayDays().intValue();
				int lateDays = delayDays + distanceDays;
				// 用户延期还款（未逾期）
				if (lateDays >= 0) {
					delayDays = -distanceDays;
					calculateRepayPlanDelay(borrowRepayBean, borrowNid, new BigDecimal(borrowApr), delayDays);
				} else {
					// 用户逾期还款
					lateDays = -lateDays;
					calculateRepayPlanLate(borrowRepayBean, borrowNid, new BigDecimal(borrowApr), delayDays, lateDays);
				}
			}
			return borrowRepayBean;
		}
		return new BorrowRepayPlanBean();
	}

	/**
	 * 单期还款数据
	 *
	 * @param borrowNid
	 * @return
	 * @throws ParseException
	 */
	@Override
	public BorrowRepayBean getBorrowRepayInfo(String borrowNid, String borrowApr, String borrowStyle)
			throws ParseException {

		BorrowRepayExample example = new BorrowRepayExample();
		BorrowRepayExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<BorrowRepay> list = this.borrowRepayMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			BorrowRepayBean borrowRepayBean = new BorrowRepayBean();
			BorrowRepay borrowRepay = list.get(0);
			BeanUtils.copyProperties(borrowRepay, borrowRepayBean);
			Date nowDate = new Date();
			Date date = new Date(Long.valueOf(borrowRepayBean.getRepayTime()) * 1000L);
			int distanceDays = GetDate.daysBetween(nowDate, date);
			// 提前还款
			if (distanceDays >= 0) {
				// 获取提前还款的阀值
				String repayAdvanceDay = this.getBorrowConfig("REPAY_ADVANCE_TIME");
				int advanceDays = distanceDays;
				// 未大于提前还款的阀值（正常还款）
				if (advanceDays <= Integer.parseInt(repayAdvanceDay)) {
					// 计算正常还款利息
					calculateRepay(borrowRepayBean, borrowNid, new BigDecimal(borrowApr), advanceDays);
				} else {// 大于提前还款阀值（提前还款）
						// 计算提前还款利息
					calculateRepayAdvance(borrowRepayBean, borrowNid, new BigDecimal(borrowApr), advanceDays);
				}
			} else {
				// 延迟天数
				int delayDays = borrowRepayBean.getDelayDays().intValue();
				int lateDays = delayDays + distanceDays;
				// 用户延期还款（未逾期）
				if (lateDays >= 0) {
					delayDays = -distanceDays;
					calculateRepayDelay(borrowRepayBean, borrowNid, new BigDecimal(borrowApr), delayDays);
				} else {
					lateDays = -lateDays;
					// 用户逾期还款
					calculateRepayLate(borrowRepayBean, borrowNid, new BigDecimal(borrowApr), delayDays, lateDays);
				}
			}
			borrowRepayBean
					.setRepayTimeStr(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrowRepayBean.getRepayTime())));
			// 判断当前期是否在还款
			BorrowApicronExample exampleBorrowApicron = new BorrowApicronExample();
			BorrowApicronExample.Criteria crtBorrowApicron = exampleBorrowApicron.createCriteria();
			crtBorrowApicron.andBorrowNidEqualTo(borrowNid);
			crtBorrowApicron.andPeriodNowEqualTo(1);
			crtBorrowApicron.andApiTypeEqualTo(1);
			List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(exampleBorrowApicron);

			if (borrowApicrons != null && borrowApicrons.size() > 0) {
				BorrowApicron borrowApicron = borrowApicrons.get(0);
				if (borrowApicron.getRepayStatus() == null) { // 正在还款当前期
					borrowRepayBean.setBorrowStatus("0");
				} else {// 用户未还款当前期
					borrowRepayBean.setBorrowStatus("1");
				}
			} else {
				borrowRepayBean.setBorrowStatus("0");
			}
			return borrowRepayBean;
		}
		return new BorrowRepayBean();
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
	private void calculateRepayAdvance(BorrowRepayBean repay, String borrowNid, BigDecimal borrowApr, int interestDay)
			throws ParseException {

		List<BorrowRecover> borrowRecovers = this.searchBorrowRecover(borrowNid);
		// 用户实际还款额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 用户提前还款利息
		BigDecimal repayChargeInterest = new BigDecimal(0);
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
					userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrowApr, totalDays);
				} else {
					// 计算出借用户实际获得的本息和
					userAccountFact = UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount, userCapital,
							borrowApr, interestDay);
					// 用户提前还款减少的利息
					userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrowApr,
							interestDay);
				}
				borrowRecovers.get(i).setChargeDays(interestDay);
				borrowRecovers.get(i).setChargeInterest(userChargeInterest);
				// 统计本息总和
				userAccountTotal = userAccountTotal.add(userAccountFact);
				// 统计提前还款减少的利息
				repayChargeInterest = repayChargeInterest.add(userChargeInterest);
			}
			repay.setRecoverList(borrowRecovers);
		}
		repay.setRepayAccount(userAccountTotal);
		repay.setRepayAccountAll(userAccountTotal.add(repay.getRepayFee()));
		repay.setChargeDays(interestDay);
		repay.setChargeInterest(repayChargeInterest);
	}

	/**
	 * 统计单期还款用户正常还款的总标
	 *
	 * @param repay
	 * @param borrowNid
	 * @param borrowApr
	 * @param interestDay
	 * @throws ParseException
	 */
	private void calculateRepay(BorrowRepayBean repay, String borrowNid, BigDecimal borrowApr, int interestDay)
			throws ParseException {
		List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrowNid);
		if (borrowRecovers != null && borrowRecovers.size() > 0) {
			for (int i = 0; i < borrowRecovers.size(); i++) {
				borrowRecovers.get(i).setChargeDays(interestDay);
			}
			repay.setRecoverList(borrowRecovers);
		}
		// 正常还款
		repay.setRepayAccountAll(repay.getRepayAccount().add(repay.getRepayFee()));
		repay.setChargeDays(interestDay);
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
	private void calculateRepayLate(BorrowRepayBean repay, String borrowNid, BigDecimal borrowApr, int delayDays,
			int lateDays) throws ParseException {

		List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrowNid);
		// 用户逾期
		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		// 统计借款用户总逾期利息
		BigDecimal userOverdueInterestTotal = new BigDecimal(0);
		if (borrowRecovers != null && borrowRecovers.size() > 0) {
			for (int i = 0; i < borrowRecovers.size(); i++) {
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = borrowRecovers.get(i).getRecoverAccount();
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = borrowRecovers.get(i).getRecoverCapital();
				// 计算用户实际获得的本息和
				BigDecimal userAccountFact = UnnormalRepayUtils.overdueRepayPrincipalInterest(userAccount, userCapital,
						borrowApr, delayDays, lateDays);
				// 计算用户逾期利息
				BigDecimal userOverdueInterest = UnnormalRepayUtils.overdueRepayOverdueInterest(userAccount, lateDays);
				// 计算用户延期利息
				BigDecimal userDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(userCapital, borrowApr,
						delayDays);
				borrowRecovers.get(i).setDelayInterest(userDelayInterest);
				borrowRecovers.get(i).setDelayDays(delayDays);
				borrowRecovers.get(i).setLateInterest(userOverdueInterest);
				borrowRecovers.get(i).setLateDays(lateDays);
				// 统计总和
				userAccountTotal = userAccountTotal.add(userAccountFact);
				userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
				userOverdueInterestTotal = userOverdueInterestTotal.add(userOverdueInterest);
			}
			repay.setRecoverList(borrowRecovers);
		}
		repay.setRepayAccountAll(userAccountTotal.add(repay.getRepayFee()));
		repay.setRepayAccount(userAccountTotal);
		repay.setDelayDays(delayDays);
		repay.setDelayInterest(userDelayInterestTotal);
		repay.setLateDays(lateDays);
		repay.setLateInterest(userOverdueInterestTotal);
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
	private void calculateRepayDelay(BorrowRepayBean repay, String borrowNid, BigDecimal borrowApr, int delayDays)
			throws ParseException {

		List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrowNid);
		// 用户延期
		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		if (borrowRecovers != null && borrowRecovers.size() > 0) {
			for (int i = 0; i < borrowRecovers.size(); i++) {
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = borrowRecovers.get(i).getRecoverAccount();
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = borrowRecovers.get(i).getRecoverCapital();
				// 计算用户实际获得的本息和
				BigDecimal userAccountFact = UnnormalRepayUtils.delayRepayPrincipalInterest(userAccount, userCapital,
						borrowApr, delayDays);
				// 计算用户延期利息
				BigDecimal userDelayInterest = UnnormalRepayUtils.delayRepayInterest(userCapital, borrowApr, delayDays);
				borrowRecovers.get(i).setDelayInterest(userDelayInterest);
				borrowRecovers.get(i).setDelayDays(delayDays);
				// 统计总和
				userAccountTotal = userAccountTotal.add(userAccountFact);
				userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
			}
			repay.setRecoverList(borrowRecovers);
		}
		repay.setRepayAccountAll(userAccountTotal.add(repay.getRepayFee()));
		repay.setRepayAccount(userAccountTotal);
		repay.setDelayDays(delayDays);
		repay.setDelayInterest(userDelayInterestTotal);
	}

	/**
	 * 多期还款数据
	 *
	 * @param borrowNid
	 * @return
	 * @throws ParseException
	 */
	@Override
	public BorrowRepayPlanBean getBorrowRepayPlanInfo(String borrowNid, String borrowApr, String borrowStyle)
			throws ParseException {

		BorrowRepayPlanExample example = new BorrowRepayPlanExample();
		BorrowRepayPlanExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		cra.andRepayTypeEqualTo("wait");
		cra.andRepayStatusEqualTo(0);
		example.setOrderByClause(" repay_period ASC ");
		List<BorrowRepayPlan> list = this.borrowRepayPlanMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			BorrowRepayPlanBean repayPlanBean = new BorrowRepayPlanBean();
			BorrowRepayPlan repayPlan = list.get(0);
			BeanUtils.copyProperties(repayPlan, repayPlanBean);
			Date nowDate = new Date();
			Date date = new Date(Long.valueOf(repayPlan.getRepayTime()) * 1000L);

			// 获取实际还款同计划还款时间的时间差
			int distanceDays = GetDate.daysBetween(nowDate, date);
			// 提前还款
			if (distanceDays >= 0) {
				// 获取提前还款的阀值
				String repayAdvanceDay = this.getBorrowConfig("REPAY_ADVANCE_TIME");
				int advanceDays = distanceDays;
				// 未大于提前还款的阀值（正常还款）
				if (advanceDays <= Integer.parseInt(repayAdvanceDay)) {
					// 计算正常还款利息
					calculateRepayPlan(repayPlanBean, borrowNid, new BigDecimal(borrowApr), advanceDays);
				} else {
					// 大于提前还款阀值（提前还款）
					String repayTimeStart = null;
					// 获取上次提前还款的时间
					BorrowRepayPlanExample exampleLast = new BorrowRepayPlanExample();
					BorrowRepayPlanExample.Criteria craLast = exampleLast.createCriteria();
					craLast.andBorrowNidEqualTo(borrowNid);
					craLast.andRepayTypeEqualTo("wait_yes");
					craLast.andRepayStatusEqualTo(1);
					exampleLast.setOrderByClause(" repay_period DESC ");
					List<BorrowRepayPlan> listLast = this.borrowRepayPlanMapper.selectByExample(exampleLast);
					if (listLast != null && listLast.size() > 0) {
						repayTimeStart = listLast.get(0).getRepayTime();
					} else {
						repayTimeStart = String.valueOf(repayPlanBean.getCreateTime());
					}
					calculateRepayPlanAdvance(repayPlanBean, borrowNid, new BigDecimal(borrowApr), advanceDays,
							repayTimeStart);
				}
			} else {
				// 延迟天数
				int delayDays = repayPlan.getDelayDays().intValue();
				int lateDays = delayDays + distanceDays;
				// 用户延期还款（未逾期）
				if (lateDays >= 0) {
					delayDays = -distanceDays;
					calculateRepayPlanDelay(repayPlanBean, borrowNid, new BigDecimal(borrowApr), delayDays);
				} else {
					// 用户逾期还款
					lateDays = -lateDays;
					calculateRepayPlanLate(repayPlanBean, borrowNid, new BigDecimal(borrowApr), delayDays, lateDays);
				}
			}
			repayPlanBean
					.setRepayTimeStr(GetDate.getDateMyTimeInMillis(Integer.parseInt(repayPlanBean.getRepayTime())));
			// 如果用户不是还款最后一期
			int repayPeriod = repayPlanBean.getRepayPeriod();
			BorrowApicronExample exampleBorrowApicron = new BorrowApicronExample();
			BorrowApicronExample.Criteria crtBorrowApicron = exampleBorrowApicron.createCriteria();
			crtBorrowApicron.andBorrowNidEqualTo(borrowNid);
			crtBorrowApicron.andApiTypeEqualTo(1);
			crtBorrowApicron.andPeriodNowEqualTo(repayPeriod);
			List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(exampleBorrowApicron);
			// 正在还款当前期
			if (borrowApicrons != null && borrowApicrons.size() > 0) {
				BorrowApicron borrowApicron = borrowApicrons.get(0);
				if (borrowApicron.getRepayStatus() == null) {
					repayPlanBean.setBorrowStatus("0");
				} else {
					repayPlanBean.setBorrowStatus("1");
				}
			} else {// 用户当前期未还款
				repayPlanBean.setBorrowStatus("0");
			}

			return repayPlanBean;
		}
		return new BorrowRepayPlanBean();
	}

	/**
	 * 统计分期还款用户提前还款的总标
	 *
	 * @param repayTimeStart
	 * @param borrowNid
	 * @param borrowApr
	 * @param repayTimeStart
	 * @return
	 * @throws ParseException
	 */
	private BorrowRepayPlanBean calculateRepayPlanAdvance(BorrowRepayPlanBean borrowRepayPlan, String borrowNid,
			BigDecimal borrowApr, int advanceDays, String repayTimeStart) throws ParseException {

		int repayPeriod = borrowRepayPlan.getRepayPeriod();
		List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrowNid, repayPeriod);
		// 用户实际还款额
		BigDecimal repayTotal = new BigDecimal(0);
		// 用户提前还款利息
		BigDecimal repayChargeInterest = new BigDecimal(0);
		BorrowWithBLOBs borrow = getBorrowByNid(borrowNid);
		if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
			for (int i = 0; i < borrowRecoverPlans.size(); i++) {
				BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(i);
				String recoverTime = GetDate
						.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRecoverPlan.getRecoverTime()));
				String repayStartTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(repayTimeStart));
				// 获取这两个时间之间有多少天
				int totalDays = GetDate.daysBetween(repayStartTime, recoverTime);
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = borrowRecoverPlan.getRecoverAccount();
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = borrowRecoverPlan.getRecoverCapital();
				// 用户获得的利息
				// 计算用户实际获得的本息和
				BigDecimal userAccountFact = new BigDecimal(0);
				// 计算用户提前还款减少的的利息
				BigDecimal userChargeInterest = new BigDecimal(0);
				//利息
				BigDecimal userInterest = borrowRecoverPlan.getRecoverInterest();
				// 提前还款不应该大于本次计息时间
				//TODO 判断是否为先息后本
				boolean isStyle = CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle());
				// 提前还款不应该大于本次计息时间
				if (totalDays < advanceDays) {
					// 计算出借用户实际获得的本息和
					userAccountFact = UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount, userCapital,
							borrowApr, totalDays);

					userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrowApr, totalDays);

				} else {
					// 计算出借用户实际获得的本息和
					userAccountFact = UnnormalRepayUtils.aheadRepayPrincipalInterest(userAccount, userCapital,
							borrowApr, advanceDays);

					userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrowApr,
							advanceDays);

				}
				if(isStyle){
					if(advanceDays >= 30){
						userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,totalDays);
					}else{
						userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,advanceDays);
					}
				}
				borrowRecoverPlans.get(i).setChargeDays(advanceDays);
				borrowRecoverPlans.get(i).setChargeInterest(userChargeInterest);
				repayTotal = repayTotal.add(userAccountFact);
				repayChargeInterest = repayChargeInterest.add(userChargeInterest);
			}
			borrowRepayPlan.setRecoverPlanList(borrowRecoverPlans);
		}
		borrowRepayPlan.setChargeDays(advanceDays);
		borrowRepayPlan.setChargeInterest(repayChargeInterest);
		borrowRepayPlan.setRepayAccount(repayTotal);
		borrowRepayPlan.setRepayAccountAll(repayTotal.add(borrowRepayPlan.getRepayFee()));
		return borrowRepayPlan;
	}

	/**
	 * 统计分期还款用户正常还款的总标
	 *
	 * @param borrowRepayPlan
	 * @param borrowNid
	 * @param borrowRepayPlan
	 * @param borrowApr
	 * @param interestDay
	 * @throws ParseException
	 */
	private BorrowRepayPlanBean calculateRepayPlan(BorrowRepayPlanBean borrowRepayPlan, String borrowNid,
			BigDecimal borrowApr, int interestDay) throws ParseException {

		List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrowNid,
				borrowRepayPlan.getRepayPeriod());
		if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
			borrowRepayPlan.setChargeDays(interestDay);
			for (int j = 0; j < borrowRecoverPlans.size(); j++) {
				borrowRecoverPlans.get(j).setChargeDays(interestDay);
			}
			borrowRepayPlan.setRepayAccountAll(borrowRepayPlan.getRepayAccount().add(borrowRepayPlan.getRepayFee()));
			borrowRepayPlan.setRecoverPlanList(borrowRecoverPlans);
		}
		return borrowRepayPlan;
	}

	/**
	 * 统计分期还款用户逾期还款的总标
	 *
	 * @param borrowRepayPlan
	 * @param borrowNid
	 * @param borrowApr
	 * @param delayDays
	 * @param lateDays
	 * @throws ParseException
	 */
	private BorrowRepayPlanBean calculateRepayPlanLate(BorrowRepayPlanBean borrowRepayPlan, String borrowNid,
			BigDecimal borrowApr, int delayDays, int lateDays) throws ParseException {

		List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrowNid,
				borrowRepayPlan.getRepayPeriod());
		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		// 统计借款用户总逾期利息
		BigDecimal userOverdueInterestTotal = new BigDecimal(0);
		if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
			for (int i = 0; i < borrowRecoverPlans.size(); i++) {
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = borrowRecoverPlans.get(i).getRecoverAccount();
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = borrowRecoverPlans.get(i).getRecoverCapital();
				// 计算用户实际获得的本息和
				BigDecimal userAccountFact = UnnormalRepayUtils.overdueRepayPrincipalInterest(userAccount, userCapital,
						borrowApr, delayDays, lateDays);
				// 计算用户逾期利息
				BigDecimal userOverdueInterest = UnnormalRepayUtils.overdueRepayOverdueInterest(userAccount, lateDays);
				// 计算用户延期利息
				BigDecimal userDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(userCapital, borrowApr,
						delayDays);
				// 保存相应的延期数据
				borrowRecoverPlans.get(i).setDelayInterest(userDelayInterest);
				borrowRecoverPlans.get(i).setDelayDays(delayDays);
				borrowRecoverPlans.get(i).setLateInterest(userOverdueInterest);
				borrowRecoverPlans.get(i).setLateDays(lateDays);
				// 统计总和
				userAccountTotal = userAccountTotal.add(userAccountFact);
				userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
				userOverdueInterestTotal = userOverdueInterestTotal.add(userOverdueInterest);
			}
			borrowRepayPlan.setRecoverPlanList(borrowRecoverPlans);
		}
		borrowRepayPlan.setRepayAccountAll(userAccountTotal.add(borrowRepayPlan.getRepayFee()));
		borrowRepayPlan.setRepayAccount(userAccountTotal);
		borrowRepayPlan.setDelayDays(delayDays);
		borrowRepayPlan.setDelayInterest(userDelayInterestTotal);
		borrowRepayPlan.setLateDays(lateDays);
		borrowRepayPlan.setLateInterest(userOverdueInterestTotal);
		return borrowRepayPlan;
	}

	/**
	 * 统计分期还款用户延期还款的总标
	 *
	 * @param borrowRepayPlan
	 * @param borrowNid
	 * @param borrowApr
	 * @param delayDays
	 * @throws ParseException
	 */
	private BorrowRepayPlanBean calculateRepayPlanDelay(BorrowRepayPlanBean borrowRepayPlan, String borrowNid,
			BigDecimal borrowApr, int delayDays) throws ParseException {

		List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrowNid,
				borrowRepayPlan.getRepayPeriod());
		// 统计借款用户还款总额
		BigDecimal userAccountTotal = new BigDecimal(0);
		// 统计借款用户总延期利息
		BigDecimal userDelayInterestTotal = new BigDecimal(0);
		if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
			for (int i = 0; i < borrowRecoverPlans.size(); i++) {
				// 获取未还款前用户能够获取的本息和
				BigDecimal userAccount = borrowRecoverPlans.get(i).getRecoverAccount();
				// 获取用户出借项目分期后的出借本金
				BigDecimal userCapital = borrowRecoverPlans.get(i).getRecoverCapital();
				// 计算用户实际获得的本息和
				BigDecimal userAccountFact = UnnormalRepayUtils.delayRepayPrincipalInterest(userAccount, userCapital,
						borrowApr, delayDays);
				// 计算用户延期利息
				BigDecimal userDelayInterest = UnnormalRepayUtils.delayRepayInterest(userCapital, borrowApr, delayDays);

				borrowRecoverPlans.get(i).setDelayInterest(userDelayInterest);
				borrowRecoverPlans.get(i).setDelayDays(delayDays);

				// 统计总和
				userAccountTotal = userAccountTotal.add(userAccountFact);
				userDelayInterestTotal = userDelayInterestTotal.add(userDelayInterest);
			}
			borrowRepayPlan.setRecoverPlanList(borrowRecoverPlans);
		}
		borrowRepayPlan.setRepayAccountAll(userAccountTotal.add(borrowRepayPlan.getRepayFee()));
		borrowRepayPlan.setRepayAccount(userAccountTotal);
		borrowRepayPlan.setDelayDays(delayDays);
		borrowRepayPlan.setDelayInterest(userDelayInterestTotal);
		return borrowRepayPlan;
	}

	/**
	 * 添加延期时间
	 *
	 * @param borrowNid
	 * @param afterDay
	 * @throws ParseException
	 */
	public void updateBorrowRepayDelayDays(String borrowNid, String afterDay) throws ParseException {
		AdminRepayDelayCustomize repayDelay = this.selectBorrowInfo(borrowNid);
		// 单期标
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(repayDelay.getBorrowStyle())
				|| CustomConstants.BORROW_STYLE_END.equals(repayDelay.getBorrowStyle())) {
			BorrowRepay borrowRepay = this.getBorrowRepay(borrowNid);
			borrowRepay.setDelayDays(Integer.parseInt(afterDay));
			this.borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay);
		} else {
			BorrowRepayPlan borrowRepay = this.getBorrowRepayPlan(borrowNid);
			borrowRepay.setDelayDays(Integer.parseInt(afterDay));
			this.borrowRepayPlanMapper.updateByPrimaryKeySelective(borrowRepay);
		}
	}

	public void updateBorrowRecoverPlan(BorrowRepayPlanBean borrowRepay) {

		int period = borrowRepay.getRepayPeriod();
		int nowTime = GetDate.getNowTime10();
		String borrowNid = borrowRepay.getBorrowNid();
		int userId = borrowRepay.getUserId();
		BigDecimal account = borrowRepay.getRepayAccount();// 用户实际还款本金
		BigDecimal fee = borrowRepay.getRepayFee();// 用户实际还款管理费
		BigDecimal repayAccount = new BigDecimal("0");// 用户应还款金额
		BorrowRepayPlan repay =getBorrowRepayPlan(borrowNid);
		repayAccount=repay.getRepayAccount();

		// 放款任务表
		BorrowApicronExample borrowApicronExample = new BorrowApicronExample();
		BorrowApicronExample.Criteria borrowApicronCra = borrowApicronExample.createCriteria();
		borrowApicronCra.andBorrowNidEqualTo(borrowRepay.getBorrowNid());
		borrowApicronCra.andPeriodNowEqualTo(period);
		borrowApicronCra.andApiTypeEqualTo(1);
		List<BorrowApicron> borrowApicrons = this.borrowApicronMapper.selectByExample(borrowApicronExample);
		if (borrowApicrons != null && borrowApicrons.size() > 0) {
			BorrowApicron borrowApicron = borrowApicrons.get(0);
			if (borrowApicron.getRepayStatus() == null) {
				List<BorrowRecoverPlan> borrowRecoverPlans = borrowRepay.getRecoverPlanList();
				if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
					for (int j = 0; j < borrowRecoverPlans.size(); j++) {
						BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
						BorrowRecoverPlan borrowRecoverPlanOld = borrowRecoverPlanMapper
								.selectByPrimaryKey(borrowRecoverPlan.getId());
						borrowRecoverPlanOld.setChargeDays(borrowRecoverPlan.getChargeDays());
						borrowRecoverPlanOld
								.setChargeInterest(borrowRecoverPlan.getChargeInterest().multiply(new BigDecimal(-1)));
						borrowRecoverPlanOld.setDelayDays(borrowRecoverPlan.getDelayDays());
						borrowRecoverPlanOld.setDelayInterest(borrowRecoverPlan.getDelayInterest());
						borrowRecoverPlanOld.setLateDays(borrowRecoverPlan.getLateDays());
						borrowRecoverPlanOld.setLateInterest(borrowRecoverPlan.getLateInterest());
						borrowRecoverPlanMapper.updateByPrimaryKey(borrowRecoverPlanOld);
					}
				}
				int updateTime = borrowApicron.getUpdateTime();
				// 更新时间
				borrowApicron.setUpdateTime(nowTime);
				// 汇租赁当前期数
				borrowApicron.setPeriodNow(period);
				// 还款状态
				borrowApicron.setRepayStatus(0);
				borrowApicronCra.andUpdateTimeEqualTo(updateTime);
				int updateRecordNum = this.borrowApicronMapper.updateByExampleWithBLOBs(borrowApicron,
						borrowApicronExample);
				if (updateRecordNum == 0) {
					throw new RuntimeException("重复还款");
				}
			} else {
				return;
			}
		} else {
			List<BorrowRecoverPlan> borrowRecoverPlans = borrowRepay.getRecoverPlanList();
			if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
				for (int j = 0; j < borrowRecoverPlans.size(); j++) {
					BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
					BorrowRecoverPlan borrowRecoverPlanOld = borrowRecoverPlanMapper
							.selectByPrimaryKey(borrowRecoverPlan.getId());
					borrowRecoverPlanOld.setChargeDays(borrowRecoverPlan.getChargeDays());
					borrowRecoverPlanOld
							.setChargeInterest(borrowRecoverPlan.getChargeInterest().multiply(new BigDecimal(-1)));
					borrowRecoverPlanOld.setDelayDays(borrowRecoverPlan.getDelayDays());
					borrowRecoverPlanOld.setDelayInterest(borrowRecoverPlan.getDelayInterest());
					borrowRecoverPlanOld.setLateDays(borrowRecoverPlan.getLateDays());
					borrowRecoverPlanOld.setLateInterest(borrowRecoverPlan.getLateInterest());
					borrowRecoverPlanMapper.updateByPrimaryKey(borrowRecoverPlanOld);
				}
			}
			BorrowApicron borrowApicron = new BorrowApicron();
			String nid = borrowRepay.getBorrowNid() + "_" + borrowRepay.getUserId() + "_" + period;
			borrowApicron.setNid(nid);
			borrowApicron.setBorrowNid(borrowRepay.getBorrowNid());
			borrowApicron.setUserId(borrowRepay.getUserId());
			borrowApicron.setApiType(1);
			borrowApicron.setPeriodNow(period);
			borrowApicron.setRepayStatus(0);
			borrowApicron.setStatus(1);
			borrowApicron.setCreditRepayStatus(0);
			borrowApicron.setCreateTime(nowTime);
		     borrowApicron.setExtraYieldStatus(0);
             borrowApicron.setExtraYieldRepayStatus(0);
			borrowApicronMapper.insertSelective(borrowApicron);
		}
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
			if (account.add(fee).compareTo(accountBean.getBalance()) == 0
					|| account.add(fee).compareTo(accountBean.getBalance()) == -1) {
				AccountChinapnr accountChinapnr = this.getChinapnrUserInfo(userId);
				if (accountChinapnr != null) {
					BigDecimal userBalance = this.getUserBalance(accountChinapnr.getChinapnrUsrcustid());
					if (account.add(fee).compareTo(userBalance) == 0 || account.add(fee).compareTo(userBalance) == -1) {
						total = accountBean.getTotal().subtract(account.add(fee));// 减去账户总资产
						balance = accountBean.getBalance().subtract(account.add(fee)); // 减去可用余额
						expand = accountBean.getExpend().add(account.add(fee));// 累加到总支出
						repayMoney = accountBean.getRepay().subtract(repayAccount);// 减去待还金额(提前还款利息)
						/*
						 * frost =
						 * accountBean.getFrost().add(account).add(fee);//
						 * 添加到冻结金额
						 */

						accountBean.setTotal(total);
						accountBean.setBalance(balance);
						accountBean.setExpend(expand);
						accountBean.setRepay(repayMoney);
						/* accountBean.setFrost(frost); */

						System.out.println("用户:" + userId + "***********************************扣除相应的还款金额account："
								+ JSON.toJSONString(accountBean));
						accountMapper.updateByPrimaryKey(accountBean);

						// 插入huiyingdai_account_list表
						AccountList accountListRecord = new AccountList();
						accountListRecord.setNid(borrowNid + "_" + userId + "_" + period); // 生成规则BorrowNid_userid_期数
						accountListRecord.setUserId(userId); // 借款人id
						accountListRecord.setAmount(account.add(fee)); // 操作金额
						accountListRecord.setType(2); // 收支类型1收入2支出3冻结
						accountListRecord.setTrade("repay_success"); // 交易类型
						accountListRecord.setTradeCode("balance"); // 操作识别码
						accountListRecord.setTotal(accountBean.getTotal()); // 资金总额
						accountListRecord.setBalance(accountBean.getBalance()); // 可用金额
						accountListRecord.setFrost(accountBean.getFrost()); // 冻结金额
						accountListRecord.setAwait(accountBean.getAwait()); // 待收金额
						accountListRecord.setRepay(accountBean.getRepay()); // 待还金额
						accountListRecord.setCreateTime(nowTime); // 创建时间
						accountListRecord.setOperator(CustomConstants.OPERATOR_AUTO_LOANS); // 操作员
						accountListRecord.setRemark(borrowNid);
						accountListRecord.setIp(borrowRepay.getIp()); // 操作IP
						accountListRecord.setBaseUpdate(0);
						accountListRecord.setWeb(0);
						System.out.println("用户:" + userId + "***********************************预插入accountList："
								+ JSON.toJSONString(accountListRecord));
						this.accountListMapper.insertSelective(accountListRecord);

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
						accountLog.setAddtime(String.valueOf(nowTime));
						accountLog.setAddip(borrowRepay.getIp());
						accountLog.setBalanceFrostNew(BigDecimal.ZERO);
						accountLog.setBalanceFrostOld(BigDecimal.ZERO);
						System.out.println("用户:" + userId + "***********************************预插入accountLog："
								+ JSON.toJSONString(accountLog));
						this.accountLogMapper.insertSelective(accountLog);
					} else {
						throw new RuntimeException("用户汇付账户余额不足!");
					}
				} else {
					throw new RuntimeException("用户开户信息不存在!");
				}
			} else {
				throw new RuntimeException("用户余额不足,还款失败");
			}
		}

	}

	public void updateBorrowRecover(BorrowRepayBean borrowRepay) {

		int nowTime = GetDate.getNowTime10();
		String borrowNid = borrowRepay.getBorrowNid();
		int userId = borrowRepay.getUserId();
		BigDecimal account = borrowRepay.getRepayAccount();// 用户实际还款本金
		BigDecimal fee = borrowRepay.getRepayFee();// 用户实际还款管理费
		BigDecimal repayAccount = new BigDecimal("0");// 用户应还款金额
		BorrowRepay repay =getBorrowRepay(borrowNid);
		repayAccount=repay.getRepayAccount();
		// 放款任务表
		BorrowApicronExample borrowApicronExample = new BorrowApicronExample();
		BorrowApicronExample.Criteria borrowApicronCra = borrowApicronExample.createCriteria();
		borrowApicronCra.andBorrowNidEqualTo(borrowRepay.getBorrowNid());
		borrowApicronCra.andPeriodNowEqualTo(1);
		borrowApicronCra.andApiTypeEqualTo(1);
		List<BorrowApicron> borrowApicrons = this.borrowApicronMapper.selectByExample(borrowApicronExample);
		if (borrowApicrons != null && borrowApicrons.size() > 0) {
			BorrowApicron borrowApicron = borrowApicrons.get(0);
			if (borrowApicron.getRepayStatus() == null) {
				List<BorrowRecover> recoverList = borrowRepay.getRecoverList();
				if (recoverList != null && recoverList.size() > 0) {
					for (int i = 0; i < recoverList.size(); i++) {
						BorrowRecover borrowRecover = recoverList.get(i);
						BorrowRecover borrowRecoverOld = borrowRecoverMapper.selectByPrimaryKey(borrowRecover.getId());
						borrowRecoverOld.setChargeDays(borrowRecover.getChargeDays());
						borrowRecoverOld
								.setChargeInterest(borrowRecover.getChargeInterest().multiply(new BigDecimal(-1)));
						borrowRecoverOld.setDelayDays(borrowRecover.getDelayDays());
						borrowRecoverOld.setDelayInterest(borrowRecover.getDelayInterest());
						borrowRecoverOld.setLateDays(borrowRecover.getLateDays());
						borrowRecoverOld.setLateInterest(borrowRecover.getLateInterest());
						borrowRecoverMapper.updateByPrimaryKey(borrowRecoverOld);
					}
				}
				int updateTime = borrowApicron.getUpdateTime();
				// 更新时间
				borrowApicron.setUpdateTime(nowTime);
				// 汇租赁当前期数
				borrowApicron.setPeriodNow(1);
				// 还款状态
				borrowApicron.setRepayStatus(0);
				borrowApicronCra.andUpdateTimeEqualTo(updateTime);
				int updateRecordNum = this.borrowApicronMapper.updateByExampleWithBLOBs(borrowApicron,
						borrowApicronExample);
				if (updateRecordNum == 0) {
					throw new RuntimeException("重复还款");
				}
			} else {
				return;
			}
		} else {
			List<BorrowRecover> recoverList = borrowRepay.getRecoverList();
			if (recoverList != null && recoverList.size() > 0) {
				for (int i = 0; i < recoverList.size(); i++) {
					BorrowRecover borrowRecover = recoverList.get(i);
					BorrowRecover borrowRecoverOld = borrowRecoverMapper.selectByPrimaryKey(borrowRecover.getId());
					borrowRecoverOld.setChargeDays(borrowRecover.getChargeDays());
					borrowRecoverOld.setChargeInterest(borrowRecover.getChargeInterest().multiply(new BigDecimal(-1)));
					borrowRecoverOld.setDelayDays(borrowRecover.getDelayDays());
					borrowRecoverOld.setDelayInterest(borrowRecover.getDelayInterest());
					borrowRecoverOld.setLateDays(borrowRecover.getLateDays());
					borrowRecoverOld.setLateInterest(borrowRecover.getLateInterest());
					borrowRecoverMapper.updateByPrimaryKey(borrowRecoverOld);
				}
			}
			BorrowApicron borrowApicron = new BorrowApicron();
			String nid = borrowRepay.getBorrowNid() + "_" + borrowRepay.getUserId() + "_1";
			borrowApicron.setNid(nid);
			borrowApicron.setBorrowNid(borrowRepay.getBorrowNid());
			borrowApicron.setUserId(borrowRepay.getUserId());
			borrowApicron.setApiType(1);
			borrowApicron.setPeriodNow(1);
			borrowApicron.setRepayStatus(0);
			borrowApicron.setStatus(1);
			borrowApicron.setCreditRepayStatus(0);
			borrowApicron.setCreateTime(nowTime);
		     borrowApicron.setExtraYieldStatus(0);
             borrowApicron.setExtraYieldRepayStatus(0);
			borrowApicronMapper.insertSelective(borrowApicron);
		}
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
						total = accountBean.getTotal().subtract(account.add(fee));// 减去账户总资产
						balance = accountBean.getBalance().subtract(account.add(fee)); // 减去可用余额
						expand = accountBean.getExpend().add(account.add(fee));// 累加到总支出
						repayMoney = accountBean.getRepay().subtract(repayAccount);// 减去待还金额(提前还款利息)
						/*
						 * frost =
						 * accountBean.getFrost().add(account).add(fee);//
						 * 添加到冻结金额
						 */

						accountBean.setTotal(total);
						accountBean.setBalance(balance);
						accountBean.setExpend(expand);
						accountBean.setRepay(repayMoney);
						/* accountBean.setFrost(frost); */

						System.out.println("用户:" + userId + "***********************************扣除相应的还款金额account："
								+ JSON.toJSONString(accountBean));
						accountMapper.updateByPrimaryKey(accountBean);

						// 插入huiyingdai_account_list表
						AccountList accountListRecord = new AccountList();
						accountListRecord.setNid(borrowNid + "_" + userId + "_" + 1); // 生成规则BorrowNid_userid_期数
						accountListRecord.setUserId(userId); // 借款人id
						accountListRecord.setAmount(account.add(fee)); // 操作金额
						accountListRecord.setType(2); // 收支类型1收入2支出3冻结
						accountListRecord.setTrade("repay_success"); // 交易类型
						accountListRecord.setTradeCode("balance"); // 操作识别码
						accountListRecord.setTotal(accountBean.getTotal()); // 资金总额
						accountListRecord.setBalance(accountBean.getBalance()); // 可用金额
						accountListRecord.setFrost(accountBean.getFrost()); // 冻结金额
						accountListRecord.setAwait(accountBean.getAwait()); // 待收金额
						accountListRecord.setRepay(accountBean.getRepay()); // 待还金额
						accountListRecord.setCreateTime(nowTime); // 创建时间
						accountListRecord.setOperator(CustomConstants.OPERATOR_AUTO_LOANS); // 操作员
						accountListRecord.setRemark(borrowNid);
						accountListRecord.setIp(borrowRepay.getIp()); // 操作IP
						accountListRecord.setBaseUpdate(0);
						accountListRecord.setWeb(0);
						System.out.println("用户:" + userId + "***********************************预插入accountList："
								+ JSON.toJSONString(accountListRecord));
						this.accountListMapper.insertSelective(accountListRecord);

						// 写入account_log日志
						AccountLog accountLog = new AccountLog();
						accountLog.setUserId(userId);// 操作用户id
						accountLog.setNid("repay_freeze" + "_" + borrowNid + "_" + userId + "_" + 1);
						accountLog.setTotalOld(BigDecimal.ZERO);
						accountLog.setCode("borrow");
						accountLog.setCodeType("repay_freeze");
						accountLog.setCodeNid(borrowNid + "_" + userId + "_" + 1);
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
						accountLog.setAddtime(String.valueOf(nowTime));
						accountLog.setAddip(borrowRepay.getIp());
						accountLog.setBalanceFrostNew(BigDecimal.ZERO);
						accountLog.setBalanceFrostOld(BigDecimal.ZERO);
						System.out.println("用户:" + userId + "***********************************预插入accountLog："
								+ JSON.toJSONString(accountLog));
						this.accountLogMapper.insertSelective(accountLog);
					} else {
						throw new RuntimeException("用户汇付账户余额不足!");
					}
				} else {
					throw new RuntimeException("用户开户信息不存在!");
				}
			} else {
				throw new RuntimeException("用户余额不足,还款失败");
			}
		}

	}

	@Override
	public Account getUserAccount(String userId) {
		AccountExample example = new AccountExample();
		AccountExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(Integer.parseInt(userId));
		List<Account> accounts = this.accountMapper.selectByExample(example);
		if (accounts != null && accounts.size() > 0) {
			return accounts.get(0);
		}
		return new Account();
	}

	/**
	 * 重新还款
	 *
	 * @param borrowNid
	 */
	@Override
	public void updateBorrowApicronRecord(String borrowNid) {

		Date systemNowDate = new Date();
		Long systemNowDateLong = systemNowDate.getTime() / 1000;
		Integer time = Integer.valueOf(String.valueOf(systemNowDateLong));
		if (StringUtils.isNotEmpty(borrowNid)) {

			BorrowApicronExample borrowExample = new BorrowApicronExample();
			BorrowApicronExample.Criteria borrowCra = borrowExample.createCriteria();
			borrowCra.andBorrowNidEqualTo(borrowNid);
			borrowCra.andRepayStatusEqualTo(9);
			// 放款任务表
			BorrowApicron borrowApicron = new BorrowApicron();
			// Status
			borrowApicron.setRepayStatus(0);
			// 更新时间
			borrowApicron.setUpdateTime(time);

			this.borrowApicronMapper.updateByExampleSelective(borrowApicron, borrowExample);
		}
	}

	/**
	 * 计算管理费
	 *
	 * @return
	 */
	public int incomeFeeService() throws Exception {

		Integer recoverStatus = 0;
		Integer repayStatus = 0;

		BorrowExample borrowExample = new BorrowExample();
		borrowExample.createCriteria().andRepayFullStatusEqualTo(0).andStatusEqualTo(3);
		List<Borrow> listBorrow = borrowMapper.selectByExample(borrowExample);
		if (listBorrow != null && listBorrow.size() > 0) {
			for (Borrow borrow : listBorrow) {
				String borrowNid = borrow.getBorrowNid();
				// 差异费率
				BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
				//初审时间
				int borrowVerifyTime =Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());

				String borrowStyle = borrow.getBorrowStyle();

				// 是否月标(true:月标, false:天标)
				boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
						|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
						|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);

				BorrowRecoverExample recoverExample = new BorrowRecoverExample();
				recoverExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRecoverStatusEqualTo(recoverStatus).andRecoverFeeEqualTo(BigDecimal.ZERO);
				List<BorrowRecover> listBorrowRecover = borrowRecoverMapper.selectByExample(recoverExample);
			 
				if (listBorrowRecover != null && listBorrowRecover.size() > 0) {
					for (BorrowRecover recover : listBorrowRecover) {

						// 计算管理费
						InterestInfo interestInfo = CalculatesUtil.getInterestInfo(recover.getRecoverCapital(),borrow.getBorrowPeriod(),
								borrow.getBorrowApr(), borrow.getBorrowStyle(),borrow.getBorrowSuccessTime(), new BigDecimal(borrow.getManageFeeRate()),
								new BigDecimal(Validator.isNull(borrow.getBorrowManagerScaleEnd()) ? "0.00": borrow.getBorrowManagerScaleEnd()),
								borrow.getProjectType(),differentialRate,borrowVerifyTime);

						BigDecimal recoverfee = interestInfo.getFee();

						if (recoverfee.compareTo(BigDecimal.ZERO) > 0) {
							// 更新Recover
							recover.setRecoverFee(recoverfee);
							borrowRecoverMapper.updateByPrimaryKey(recover);

							// 更新Repay
							BorrowRepayExample repayExample = new BorrowRepayExample();
							repayExample.createCriteria().andBorrowNidEqualTo(borrowNid)
									.andRepayStatusEqualTo(repayStatus);
							BorrowRepay repay = borrowRepayMapper.selectByExample(repayExample).get(0);

							repay.setRepayFee(repay.getRepayFee().add(recoverfee));
							this.borrowRepayMapper.updateByPrimaryKeySelective(repay);

							if (isMonth) {
								List<InterestInfo> listMonth = interestInfo.getListMonthly();
								for (InterestInfo month : listMonth) {
									BorrowRecoverPlanExample planExample = new BorrowRecoverPlanExample();
									planExample.createCriteria().andBorrowNidEqualTo(borrowNid)
											.andRecoverPeriodEqualTo(month.getMontyNo()).andNidEqualTo(recover.getNid())
											.andRecoverStatusEqualTo(recoverStatus)
											.andRecoverFeeEqualTo(BigDecimal.ZERO);
									List<BorrowRecoverPlan> listBorrowRecoverPlan = borrowRecoverPlanMapper
											.selectByExample(planExample);
									if (listBorrowRecoverPlan != null && listBorrowRecoverPlan.size() > 0) {
										// 更新Recover_plan
										BorrowRecoverPlan recoverPlan = listBorrowRecoverPlan.get(0);
										recoverPlan.setRecoverFee(month.getFee());
										this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(recoverPlan);

										// 更新Repay_plan
										BorrowRepayPlanExample repayPlanExample = new BorrowRepayPlanExample();
										repayPlanExample.createCriteria().andBorrowNidEqualTo(borrowNid)
												.andRepayPeriodEqualTo(month.getMontyNo())
												.andRepayStatusEqualTo(repayStatus);
										BorrowRepayPlan repayPlan = borrowRepayPlanMapper
												.selectByExample(repayPlanExample).get(0);
										repayPlan.setRepayFee(repayPlan.getRepayFee().add(month.getFee()));
										this.borrowRepayPlanMapper.updateByPrimaryKeySelective(repayPlan);
									}
								}
							}
						}

					}
				}

			}
		}

		return 0;
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
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_BALANCE_BG); // 消息类型(必须)
		bean.setUsrCustId(String.valueOf(usrCustId));
		; // 用户客户号(必须)

		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("用户余额查询"); // 备注
		bean.setLogClient("0"); // PC

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
}

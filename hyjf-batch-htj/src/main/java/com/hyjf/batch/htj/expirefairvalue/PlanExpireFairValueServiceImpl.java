package com.hyjf.batch.htj.expirefairvalue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.calculate.HTJServiceFeeUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.DebtBorrow;
import com.hyjf.mybatis.model.auto.DebtBorrowExample;
import com.hyjf.mybatis.model.auto.DebtDetail;
import com.hyjf.mybatis.model.auto.DebtDetailExample;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.DebtRepay;
import com.hyjf.mybatis.model.auto.DebtRepayDetail;
import com.hyjf.mybatis.model.auto.DebtRepayDetailExample;
import com.hyjf.mybatis.model.auto.DebtRepayExample;

/**
 * 计算公允价值Service实现类
 * 
 * @ClassName PlanExpireFairValueServiceImpl
 * @author liuyang
 * @date 2016年11月22日 下午1:53:49
 */
@Service
public class PlanExpireFairValueServiceImpl extends BaseServiceImpl implements PlanExpireFairValueService {

	/**
	 * 检索锁定中的计划列表
	 * 
	 * @Title selectLockPlanList
	 * @return
	 */
	@Override
	public List<DebtPlan> selectLockPlanList() {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		cra.andDebtPlanStatusEqualTo(CustomConstants.DEBT_PLAN_STATUS_5);
		cra.andDelFlagEqualTo(0);
		return this.debtPlanMapper.selectByExample(example);
	}

	/**
	 * 计算到期公允价值
	 * 
	 * @Title calculation
	 * @param plan
	 */
	@Override
	@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void calculation(DebtPlan plan) throws Exception {
		System.out.println("-----------公允价值计算开始---" + plan.getDebtPlanNid() + "---------");
		// 到期公允价值
		BigDecimal expireFairValue = BigDecimal.ZERO;
		// 总的到期公允价值
		BigDecimal totalExpireFairValue = BigDecimal.ZERO;
		// 延期利息
		BigDecimal delayInterest = BigDecimal.ZERO;
		// 逾期利息
		BigDecimal lateInterest = BigDecimal.ZERO;

		// 计划编号
		String debtPlanNid = plan.getDebtPlanNid();
		// 计划应清算时间
		Integer liquidateShouldTime = plan.getLiquidateShouldTime();
		// 清算日
		Date liquidateShouldDate = GetDate.getDate(liquidateShouldTime);
		// 还款时间
		Integer repayTime = null;
		// 标的放款时间
		Integer loanTime = null;
		// 当前时间
		Integer nowTime = GetDate.getNowTime10();
		// 当前日期
		Date nowDate = new Date();
		// 根据计划编号查询计划的加入
		List<DebtPlanAccede> accedeList = this.selectPlanAccedeByPlanNid(debtPlanNid);
		// 计划加入列表不为空的场合
		if (accedeList != null && accedeList.size() > 0) {
			// 提前,延期,逾期状态 0正常还款 1提前还款 2延期还款 3逾期还款
			boolean serviceFeeRateUpdate = false;
			// 循环加入列表
			for (DebtPlanAccede debtPlanAccede : accedeList) {
				String accedeOrderId = debtPlanAccede.getAccedeOrderId();
				totalExpireFairValue = BigDecimal.ZERO;
				// 根据计划订单号查询有效的债权详情列表
				List<DebtDetail> detailList = this.selectDebtDetailListByAccedeOrderId(accedeOrderId);
				if (detailList != null && detailList.size() > 0) {
					// 循环债权详情列表
					for (int i = 0; i < detailList.size(); i++) {
						// 债权详情
						DebtDetail debtDetail = detailList.get(i);
						// 还款方式
						String borrowStyle = debtDetail.getBorrowStyle();
						// 还款状态
						Integer repayStatus = debtDetail.getRepayStatus();
						// 获取标的详情
						DebtBorrow borrow = this.selectDebtBorrowByBorrowNid(debtDetail.getBorrowNid());
						// 延期天数
						int delayDays = 0;
						int distanceDays = 0;
						// 逾期天数
						int lateDays = 0;
						int advanceStatus = 0;
						// 如果是未还款
						if (repayStatus == 0) {
							// 是否月标(true:月标, false:天标)
							boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
							// 取得还款期数
							Integer repayPeriod = debtDetail.getRepayPeriod();
							// 本期应还时间
							repayTime = debtDetail.getRepayTime();
							if (isMonth) {
								// [principal: 等额本金, month:等额本息,
								// month:等额本息,endmonth:先息后本]
								// 上期债权详情
								DebtDetail preDebtDetail = null;
								// 上一期还款时间
								Integer preRepayTime = null;
								// 放款时间
								loanTime = debtDetail.getLoanTime();
								// 分期项目取延期天数
								DebtRepayDetail debtRepayDetail = this.getDebtRepayDetail(debtDetail.getBorrowNid());
								// 延期天数
								delayDays = debtRepayDetail.getDelayDays();
								distanceDays = 0;
								if (nowTime > liquidateShouldTime) {
									distanceDays = GetDate.daysBetween(liquidateShouldDate, GetDate.getDate(repayTime));
								} else {
									distanceDays = GetDate.daysBetween(nowDate, GetDate.getDate(repayTime));
								}
								if (distanceDays < 0) {
									lateDays = delayDays + distanceDays;

								}
								// 第一期的情况
								if (repayPeriod == 1) {
									// 上一期还款时间=放款时间
									preRepayTime = debtDetail.getLoanTime();
								} else {
									preDebtDetail = this.selectPreDebtDetailByOrderId(debtDetail.getInvestOrderId(), repayPeriod - 1);
									// 上一期还款时间
									preRepayTime = preDebtDetail.getRepayTime();
								}
								// 清算日<=上一期应还日期<本期应还日期
								if (liquidateShouldTime <= preRepayTime && liquidateShouldTime < repayTime) {
									// 公允价值 =本期应还本金
									expireFairValue = debtDetail.getLoanCapital();

								} else if (preRepayTime < liquidateShouldTime && liquidateShouldTime <= repayTime) {
									// 上期还款时间到当前期还款时间的天数
									Integer totalDays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(preRepayTime), GetDate.getDateTimeMyTimeInMillis(repayTime));
									// 上期还款时间到清算日的持有天数
									int holdDays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(preRepayTime), GetDate.getDateTimeMyTimeInMillis(liquidateShouldTime));
									// 用户延期还款（未逾期）
									if (lateDays >= 0) {
										delayDays = -distanceDays;
										// 计算延期利息
										delayInterest = debtDetail.getLoanInterest().multiply(new BigDecimal(delayDays)).divide(new BigDecimal(totalDays), 8, BigDecimal.ROUND_DOWN).multiply(debtDetail.getLoanCapital().divide(debtDetail.getLoanCapital(), 8, BigDecimal.ROUND_DOWN));
										delayInterest = delayInterest.setScale(2, BigDecimal.ROUND_DOWN);
										advanceStatus = 2;
										lateDays = 0;
										lateInterest = BigDecimal.ZERO;
										serviceFeeRateUpdate = true;
									} else {
										lateDays = -lateDays;
										// 用户逾期还款
										// 延期利息
										delayInterest = debtDetail.getLoanInterest().multiply(new BigDecimal(delayDays)).divide(new BigDecimal(totalDays), 8, BigDecimal.ROUND_DOWN).multiply(debtDetail.getLoanCapital().divide(debtDetail.getLoanCapital(), 8, BigDecimal.ROUND_DOWN));
										delayInterest = delayInterest.setScale(2, BigDecimal.ROUND_DOWN);
										// 逾期利息
										lateInterest = debtDetail.getLoanInterest().multiply(new BigDecimal(lateDays)).divide(new BigDecimal(totalDays), 8, BigDecimal.ROUND_DOWN).multiply(debtDetail.getLoanCapital().divide(debtDetail.getLoanCapital(), 8, BigDecimal.ROUND_DOWN));
										lateInterest = lateInterest.setScale(2, BigDecimal.ROUND_DOWN);
										advanceStatus = 3;
										serviceFeeRateUpdate = true;
									}
									// 计算到期公允价值
									expireFairValue = HTJServiceFeeUtils.calculationMonthExpireFairValue(debtDetail.getLoanCapital(), debtDetail.getLoanInterest(), new BigDecimal(holdDays), new BigDecimal(totalDays), delayInterest, lateInterest);
								} else if (preRepayTime < repayTime && repayTime < liquidateShouldTime) {
									// 到期公允价值 = 应还本金 +应还利息
									expireFairValue = debtDetail.getLoanCapital().add(debtDetail.getLoanInterest());
								}
								// 计划订单的总的到期公允价值
								totalExpireFairValue = totalExpireFairValue.add(expireFairValue);

								// 计划债权详情更新
								DebtDetail newDetail = this.getDebtDetail(debtDetail);
								newDetail.setExpireFairValue(expireFairValue);
								newDetail.setDelayDays(delayDays);
								newDetail.setDelayInterest(delayInterest);
								newDetail.setLateDays(lateDays);
								newDetail.setLateInterest(lateInterest);
								newDetail.setAdvanceStatus(advanceStatus);
								// 更新到期公允价值
								boolean isUpdateDetailFlag = this.debtDetailMapper.updateByPrimaryKeySelective(newDetail) > 0 ? true : false;
								if (!isUpdateDetailFlag) {
									throw new Exception("债权详情表(hyjf_debt_detail)公允价值更新失败！" + "[计划订单号：" + newDetail.getPlanOrderId() + "]");
								}
							} else {
								// 分期项目取延期天数
								DebtRepay debtRepay = this.getDebtRepay(debtDetail.getBorrowNid());
								// 取得放款时间
								loanTime = debtDetail.getLoanTime();
								// 计划总天数
								int totalDays = 0;
								delayDays = debtRepay.getDelayDays();
								if (nowTime > liquidateShouldTime) {
									distanceDays = GetDate.daysBetween(liquidateShouldDate, GetDate.getDate(repayTime));
								} else {
									distanceDays = GetDate.daysBetween(nowDate, GetDate.getDate(repayTime));
								}
								// 天标的总的期限等于项目的期限
								if (borrowStyle.equals(CustomConstants.BORROW_STYLE_ENDDAY)) {
									totalDays = borrow.getBorrowPeriod();
								} else {
									totalDays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(loanTime), GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(borrow.getRepayLastTime())));
								}
								if (distanceDays < 0) {
									lateDays = delayDays + distanceDays;
									// 用户延期还款（未逾期）
									if (lateDays >= 0) {
										delayDays = -distanceDays;
										// 计算延期利息
										delayInterest = debtDetail.getLoanInterest().multiply(new BigDecimal(delayDays)).divide(new BigDecimal(totalDays), 8, BigDecimal.ROUND_DOWN).multiply(debtDetail.getLoanCapital().divide(debtDetail.getLoanCapital(), 8, BigDecimal.ROUND_DOWN));
										delayInterest = delayInterest.setScale(2, BigDecimal.ROUND_DOWN);
										advanceStatus = 2;
										lateDays = 0;
										lateInterest = BigDecimal.ZERO;
										serviceFeeRateUpdate = true;
									} else {
										lateDays = -lateDays;
										// 用户逾期还款
										// 延期利息
										delayInterest = debtDetail.getLoanInterest().multiply(new BigDecimal(delayDays)).divide(new BigDecimal(totalDays), 8, BigDecimal.ROUND_DOWN).multiply(debtDetail.getLoanCapital().divide(debtDetail.getLoanCapital(), 8, BigDecimal.ROUND_DOWN));
										delayInterest = delayInterest.setScale(2, BigDecimal.ROUND_DOWN);
										// 逾期利息
										lateInterest = debtDetail.getLoanInterest().multiply(new BigDecimal(lateDays)).divide(new BigDecimal(totalDays), 8, BigDecimal.ROUND_DOWN).multiply(debtDetail.getLoanCapital().divide(debtDetail.getLoanCapital(), 8, BigDecimal.ROUND_DOWN));
										lateInterest = lateInterest.setScale(2, BigDecimal.ROUND_DOWN);
										advanceStatus = 3;
										serviceFeeRateUpdate = true;
									}
								}

								if (Integer.parseInt(borrow.getRepayLastTime()) > liquidateShouldTime) {
									// [endday: 按天计息, end:按月计息]
									// 计算债权的持有天数
									int holdDays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(loanTime), GetDate.getDateTimeMyTimeInMillis(liquidateShouldTime));
									// 计算到期公允价值
									expireFairValue = HTJServiceFeeUtils.calculationExpireFairValue(debtDetail.getLoanCapital(), debtDetail.getLoanInterest(), new BigDecimal(holdDays), new BigDecimal(totalDays), delayInterest, lateInterest);
									// 计划订单的到期公允价值
									totalExpireFairValue = totalExpireFairValue.add(expireFairValue);
									// 计划债权详情更新
									DebtDetail newDetail = this.getDebtDetail(debtDetail);
									newDetail.setExpireFairValue(expireFairValue);
									newDetail.setDelayDays(delayDays);
									newDetail.setDelayInterest(delayInterest);
									newDetail.setLateDays(lateDays);
									newDetail.setLateInterest(lateInterest);
									newDetail.setAdvanceStatus(advanceStatus);
									// 更新到期公允价值
									boolean isUpdateDetailFlag = this.debtDetailMapper.updateByPrimaryKeySelective(newDetail) > 0 ? true : false;
									if (!isUpdateDetailFlag) {
										throw new Exception("债权详情表(hyjf_debt_detail)公允价值更新失败！" + "[计划订单号：" + newDetail.getPlanOrderId() + "]");
									}
								} else {
									// 计算到期公允价值
									expireFairValue = debtDetail.getLoanCapital().add(debtDetail.getLoanInterest()).add(delayInterest).add(lateInterest);
									// 计划订单的到期公允价值
									totalExpireFairValue = totalExpireFairValue.add(expireFairValue);
									// 计划债权详情更新
									DebtDetail newDetail = this.getDebtDetail(debtDetail);
									newDetail.setExpireFairValue(expireFairValue);
									newDetail.setDelayDays(delayDays);
									newDetail.setDelayInterest(delayInterest);
									newDetail.setLateDays(lateDays);
									newDetail.setLateInterest(lateInterest);
									newDetail.setAdvanceStatus(advanceStatus);
									// 更新到期公允价值
									boolean isUpdateDetailFlag = this.debtDetailMapper.updateByPrimaryKeySelective(newDetail) > 0 ? true : false;
									if (!isUpdateDetailFlag) {
										throw new Exception("债权详情表(hyjf_debt_detail)公允价值更新失败！" + "[计划订单号：" + newDetail.getPlanOrderId() + "]");
									}
								}
							}
						}
					}

					// 更新计划加入表的到期公允价值
					DebtPlanAccede newAccede = this.getPlanAccedeById(debtPlanAccede);
					newAccede.setExpireFairValue(totalExpireFairValue);
					// 清算服务费率
					BigDecimal serviceFeeRate = HTJServiceFeeUtils.calculdateServiceFeeRate(newAccede.getAccedeAccount(), newAccede.getAccedeBalance(), newAccede.getAccedeFrost(), newAccede.getRepayInterest(), totalExpireFairValue);
					// 检索计划详情
					DebtPlan debtPlan = this.selectPlanInfoByDebtPlanNid(newAccede.getPlanNid());
					// 判断计划是否处于清算前三天
					boolean isLiquidatesPlan = this.isLiquidatesPlan(debtPlan);
					if (!isLiquidatesPlan) {
						newAccede.setServiceFeeRate(serviceFeeRate);
					} else if (serviceFeeRateUpdate) {
						newAccede.setServiceFeeRate(serviceFeeRate);
					}
					newAccede.setCalculationStatus(1);
					// 更新加入的到期公允价值,汇添金服务费率
					boolean isUpdateAccedeFlag = this.debtPlanAccedeMapper.updateByPrimaryKeySelective(newAccede) > 0 ? true : false;
					if (!isUpdateAccedeFlag) {
						throw new Exception("计划加入表(hyjf_debt_plan_accede)的公允价值更新失败!" + "[计划订单号：" + debtPlanAccede.getAccedeOrderId() + "]");
					}
				} else {
					// 更新计划加入表的到期公允价值
					DebtPlanAccede newAccede = this.getPlanAccedeById(debtPlanAccede);
					newAccede.setExpireFairValue(totalExpireFairValue);
					// 清算服务费率
					BigDecimal serviceFeeRate = HTJServiceFeeUtils.calculdateServiceFeeRate(newAccede.getAccedeAccount(), newAccede.getAccedeBalance(), newAccede.getAccedeFrost(), newAccede.getRepayInterest(), totalExpireFairValue);
					// 检索计划详情
					DebtPlan debtPlan = this.selectPlanInfoByDebtPlanNid(newAccede.getPlanNid());
					// 判断计划是否处于清算前三天
					boolean isLiquidatesPlan = this.isLiquidatesPlan(debtPlan);
					if (!isLiquidatesPlan) {
						newAccede.setServiceFeeRate(serviceFeeRate);
					}
					newAccede.setCalculationStatus(1);
					// 更新加入的到期公允价值,汇添金服务费率
					boolean isUpdateAccedeFlag = this.debtPlanAccedeMapper.updateByPrimaryKeySelective(newAccede) > 0 ? true : false;
					if (!isUpdateAccedeFlag) {
						throw new Exception("计划加入表(hyjf_debt_plan_accede)的公允价值更新失败!" + "[计划订单号：" + debtPlanAccede.getAccedeOrderId() + "]");
					}
				}
			}

		}
		System.out.println("-----------公允价值计算结束---" + plan.getDebtPlanNid() + "---------");

	}

	/**
	 * 不分期项目取还款信息
	 * 
	 * @param borrowNid
	 * @return
	 */
	private DebtRepay getDebtRepay(String borrowNid) {
		DebtRepayExample example = new DebtRepayExample();
		DebtRepayExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		cra.andRepayStatusEqualTo(0);
		List<DebtRepay> list = this.debtRepayMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 分期项目取最近一期的还款计划
	 * 
	 * @param borrowNid
	 * @return
	 */
	private DebtRepayDetail getDebtRepayDetail(String borrowNid) {
		DebtRepayDetailExample example = new DebtRepayDetailExample();
		DebtRepayDetailExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		cra.andRepayStatusEqualTo(0);
		example.setOrderByClause(" repay_period ASC ");
		List<DebtRepayDetail> list = debtRepayDetailMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询债权详情
	 * 
	 * @Title getDebtDetail
	 * @param detail
	 * @return
	 */
	private DebtDetail getDebtDetail(DebtDetail detail) {
		DebtDetailExample example = new DebtDetailExample();
		DebtDetailExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(detail.getId());
		List<DebtDetail> list = debtDetailMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询计划计入详情
	 * 
	 * @Title getPlanAccedeById
	 * @param accede
	 * @return
	 */
	private DebtPlanAccede getPlanAccedeById(DebtPlanAccede accede) {
		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(accede.getId());
		List<DebtPlanAccede> list = this.debtPlanAccedeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据计划加入订单号查询有效的债权列表
	 * 
	 * @Title selectDebtDetailListByAccedeOrderId
	 * @param accedeOrderId
	 * @return
	 */
	private List<DebtDetail> selectDebtDetailListByAccedeOrderId(String accedeOrderId) {
		DebtDetailExample example = new DebtDetailExample();
		DebtDetailExample.Criteria cra = example.createCriteria();
		cra.andPlanOrderIdEqualTo(accedeOrderId);
		// 债权是否有效:1:有效
		cra.andStatusEqualTo(1);
		// 还款状态:未还款
		cra.andRepayStatusEqualTo(0);
		return this.debtDetailMapper.selectByExample(example);
	}

	/**
	 * 根据订单号,还款期数查询债权详情
	 * 
	 * @Title selectPreDebtDetailByOrderId
	 * @param orderId
	 * @param repayPeriod
	 * @return
	 */
	private DebtDetail selectPreDebtDetailByOrderId(String orderId, Integer repayPeriod) {
		DebtDetailExample example = new DebtDetailExample();
		DebtDetailExample.Criteria cra = example.createCriteria();
		cra.andInvestOrderIdEqualTo(orderId);
		cra.andRepayPeriodEqualTo(repayPeriod);
		cra.andStatusEqualTo(1);
		List<DebtDetail> list = this.debtDetailMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据计划编号检索计划加入列表
	 * 
	 * @Title selectPlanAccedeByPlanNid
	 * @param debtPlanNid
	 * @return
	 */
	private List<DebtPlanAccede> selectPlanAccedeByPlanNid(String debtPlanNid) {
		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria cra = example.createCriteria();
		cra.andPlanNidEqualTo(debtPlanNid);
		return this.debtPlanAccedeMapper.selectByExample(example);
	}

	/**
	 * 根据标号获取标的详情
	 * 
	 * @Title selectDebtBorrowByBorrowNid
	 * @param borrowNid
	 * @return
	 */
	private DebtBorrow selectDebtBorrowByBorrowNid(String borrowNid) {
		DebtBorrowExample example = new DebtBorrowExample();
		DebtBorrowExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<DebtBorrow> list = this.debtBorrowMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据计划编号检索计划详情
	 * 
	 * @Title selectPlanInfoByDebtPlanNid
	 * @param debtPlanNid
	 * @return
	 */
	private DebtPlan selectPlanInfoByDebtPlanNid(String debtPlanNid) {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		cra.andDebtPlanNidEqualTo(debtPlanNid);
		List<DebtPlan> list = this.debtPlanMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 计算计划是否是清算前三天
	 * 
	 * @Title isLiquidatesPlan
	 * @param loan
	 * @return
	 * @throws Exception
	 */
	private boolean isLiquidatesPlan(DebtPlan debtPlan) throws Exception {
		// 应清算时间为空的情况
		if (debtPlan.getLiquidateShouldTime() == null || debtPlan.getLiquidateShouldTime() == 0) {
			return false;
		}
		// 获取应清算日期前三天
		String liquidateShouldTime = GetDate.getDateMyTimeInMillis(debtPlan.getLiquidateShouldTime());
		String now = GetDate.getDateMyTimeInMillis(GetDate.getMyTimeInMillis());
		int count = GetDate.daysBetween(now, liquidateShouldTime);
		if (count <= 3) {
			return true;
		}

		return false;
	}
}

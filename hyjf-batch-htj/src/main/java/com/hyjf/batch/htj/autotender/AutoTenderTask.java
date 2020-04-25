package com.hyjf.batch.htj.autotender;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;

/**
 * 汇添金定时自动出借
 * 
 * @author wangkun
 */
public class AutoTenderTask {

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	AutoTenderService autoTenderService;

	/**
	 * 汇添金自动出借
	 */
	public void run() {
		autoTender();
	}

	/**
	 * 汇添金主动出借逻辑
	 * 
	 * @return
	 */
	private boolean autoTender() {

		// 调用自动投标接口
		if (isRun == 0) {
			isRun = 1;
			System.out.println("汇添金自动出借 AutoTenderTask.run ... ");
			try {
				// 1.查询相应的出借中计划
				List<DebtPlan> debtPlanInvests = this.autoTenderService.selectDebtPlanInvest();
				if (debtPlanInvests != null && debtPlanInvests.size() > 0) {
					for (DebtPlan debtPlan : debtPlanInvests) {
						// 加入清算前三天设置
						if (Validator.isNotNull(debtPlan.getLiquidateShouldTime()) && debtPlan.getLiquidateShouldTime() > 0) {
							// 应清算时间
							int liquidateShouldTime = debtPlan.getLiquidateShouldTime();
							// 当前时间
							int nowThreeTime = GetDate.getNowTime10() + 3 * 24 * 60 * 60;
							if (nowThreeTime > liquidateShouldTime) {
								continue;
							}
						}
						// 计划编号
						String planNid = debtPlan.getDebtPlanNid();
						// 计划最小余额
						BigDecimal minSurplusInvestAccount = debtPlan.getMinSurplusInvestAccount();
						// 计划设置按规则出借遍历次数
						int cycleTimes = debtPlan.getCycleTimes();
						// 计划设置不按规则出借遍历次数
						int unableCycleTimes = debtPlan.getUnableCycleTimes();
						// 2.查询该计划对应的未完成计划加入记录，取出可用余额，拆分最大金额，拆分最小金额，承接次数
						List<DebtPlanAccede> debtPlanAccedes = this.autoTenderService.selectDebtPlanAccede(planNid, minSurplusInvestAccount);
						if (debtPlanAccedes != null && debtPlanAccedes.size() > 0) {
							for (DebtPlanAccede debtPlanAccede : debtPlanAccedes) {
								// 加入用户userId
								int userId = debtPlanAccede.getUserId();
								// 计划加入订单号
								String accedeOrderId = debtPlanAccede.getAccedeOrderId();
								// 此笔计划订单可用余额
								BigDecimal accedeBalance = debtPlanAccede.getAccedeBalance();
								// 计划设置遍历次数
								int accedeCycleTimes = debtPlanAccede.getCycleTimes();
								// 加入的订单是否复投
								int reinvestStatus = debtPlanAccede.getReinvestStatus();
								try {
									// 用户实际出借前校验,校验内容为是否已经授权，并校验有效的冻结记录是否存在
									boolean checkFlag = this.autoTenderService.checkUserAuthInfo(userId, accedeBalance, planNid, accedeOrderId);
									if (checkFlag) {
										// 汇天金调用出借逻辑
										if (reinvestStatus == 0) {
											if (accedeCycleTimes < cycleTimes) {
												// 按规则承接
												try {
													boolean assignFlag = this.autoTenderService.ruleCreditAssign(debtPlan, debtPlanAccede);
													if (!assignFlag) {
														throw new Exception("遍历进行规则进行债权承接发生错误，计划加入订单号：" + accedeOrderId);
													}
												} catch (Exception e) {
													e.printStackTrace();
												}
												// 按规则出借
												try {
													boolean tenderFlag = this.autoTenderService.ruleDebtTender(debtPlan, debtPlanAccede);
													if (!tenderFlag) {
														throw new Exception("遍历进行规则进行自动出借发生错误，计划加入订单号：" + accedeOrderId);
													}
												} catch (Exception e) {
													e.printStackTrace();
												}
												// 更新相应的计划加入订单的遍历次数
												boolean debtAccedeFlag = this.autoTenderService.updateDebtPlanAccede(debtPlanAccede);
												if (!debtAccedeFlag) {
													throw new Exception("更新用户计划加入订单失败遍历次数失败，计划订单号：" + accedeOrderId);
												}
											}
											// 非规则出借
											else if (unableCycleTimes > 0 && accedeCycleTimes < cycleTimes + unableCycleTimes) {
												// 非规则承接
												try {
													boolean assignFlag = this.autoTenderService.unableRuleCreditAssign(debtPlan, debtPlanAccede);
													if (!assignFlag) {
														throw new Exception("遍历进行非规则进行债权承接发生错误，计划加入订单号：" + accedeOrderId);
													}
												} catch (Exception e) {
													e.printStackTrace();
												}
												// 非规则出借
												try {
													boolean tenderFlag = this.autoTenderService.unableRuleDebtTender(debtPlan, debtPlanAccede);
													if (!tenderFlag) {
														throw new Exception("遍历进行非规则进行自动出借发生错误，计划加入订单号：" + accedeOrderId);
													}
												} catch (Exception e) {
													e.printStackTrace();
												}
												// 更新相应的计划加入订单的遍历次数
												boolean debtAccedeFlag = this.autoTenderService.updateDebtPlanAccede(debtPlanAccede);
												if (!debtAccedeFlag) {
													throw new Exception("更新用户计划加入订单失败遍历次数失败，计划订单号：" + accedeOrderId);
												}
											} else {
												// 更新相应的计划加入订单数据为出借完成
												boolean debtAccedeFlag = this.autoTenderService.updateDebtPlanAccedeFinish(debtPlanAccede);
												if (!debtAccedeFlag) {
													throw new Exception("更新用户计划加入订单失败，计划订单号：" + accedeOrderId);
												}
											}
										}
										// 复投逻辑
										else {
											// 复投承接
											try {
												boolean assignFlag = this.autoTenderService.reinvestCreditAssign(debtPlan, debtPlanAccede);
												if (!assignFlag) {
													throw new Exception("遍历复投进行债权承接发生错误，计划加入订单号：" + accedeOrderId);
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
											// 复投投标
											try {
												boolean tenderFlag = this.autoTenderService.reinvestDebtTender(debtPlan, debtPlanAccede);
												if (!tenderFlag) {
													throw new Exception("遍历复投进行自动出借发生错误，计划加入订单号：" + accedeOrderId);
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
											// 更新相应的计划加入订单的遍历次数
											boolean debtAccedeFlag = this.autoTenderService.updateDebtPlanAccede(debtPlanAccede);
											if (!debtAccedeFlag) {
												throw new Exception("更新用户计划加入订单失败遍历次数失败，计划订单号：" + accedeOrderId);
											}
										}
									} else {
										throw new Exception("校验用户汇添金计划加入资格失败，用户userId：" + userId + ",加入订单号：" + accedeOrderId);
									}

								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} else {
							continue;
						}
					}
					return true;
				} else {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
				System.out.println("汇添金自动出借 AutoTenderTask.end ... ");
			}
		}
		return false;
	}
}

package com.hyjf.batch.htj.planrepay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;
import com.hyjf.soa.apiweb.CommonSoaUtils;

public class PlanRepayTask {
	/** 类名 */
	private static final String THIS_CLASS = PlanRepayTask.class.getName();

	/** 运行状态 */
	private static int isRun = 0;

	/** 任务状态:未执行 */
	private static final Integer STATUS_WAIT = 9;

	/** 任务状态:已完成 */
	private static final Integer STATUS_SUCCESS = 11;

	/** 任务状态:执行中 */
	private static final Integer STATUS_RUNNING = 10;

	/** 任务执行次数 */
	public Map<String, TimesBean> runTimes = new HashMap<String, TimesBean>();

	@Autowired
	PlanRepayService planRepayService;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	public void run() {
		// 调用放款接口
		repay();
	}

	/**
	 * 调用放款接口
	 *
	 * @return
	 */
	private boolean repay() {
		String methodName = "planrepay";
		if (isRun == 0) {
			isRun = 1;

			try {
				// 取得未还款任务
				List<DebtPlan> listDebtPlan = planRepayService.getDebtPlanList(STATUS_WAIT);
				if (listDebtPlan != null && listDebtPlan.size() > 0) {
					// 循环进行放款
					for (DebtPlan debtPlan : listDebtPlan) {
						System.out.println("——————————————————————————————————————————————————"
								+ debtPlan.getDebtPlanNid()
								+ "计划还款开始——————————————————————————————————————————————————");

						// 计划编号
						String planNid = debtPlan.getDebtPlanNid();
						int errorCnt = 0;
						Long startTime = GetDate.getMillis();
						// 错误信息
						StringBuffer sbError = new StringBuffer();
						try {
							LogUtil.startLog(THIS_CLASS, methodName, "计划自动放款任务开始。[订单号：" + debtPlan.getDebtPlanNid()
									+ "]");
							// 更新任务API状态为进行中
							planRepayService.updateDebtPlan(debtPlan.getId(), STATUS_RUNNING);
							// 取得专属标加入详情列表
							List<DebtPlanAccede> listInvest = planRepayService.getdebtPlanAccedeList(planNid);
							if (listInvest != null && listInvest.size() > 0) {
								// 初始化plan防止数据错误
								debtPlan.setRepayAccountYes(BigDecimal.ZERO);
								debtPlan.setRepayAccountCapitalYes(BigDecimal.ZERO);
								debtPlan.setRepayAccountInterestYes(BigDecimal.ZERO);
								// 出借信息
								DebtPlanAccede debtPlanAccede = null;
								// 出借总件数
								int size = listInvest.size();
								/** 循环出借详情列表 */
								for (int i = 0; i < size; i++) {
									debtPlanAccede = listInvest.get(i);
									try {
										// 更新还款状态 还款中
										boolean accedeFlag = this.planRepayService
												.updateAccedeStatus(debtPlanAccede, 4);
										if (!accedeFlag) {
											throw new Exception("更新还款状态为还款中时出错，订单号" + debtPlanAccede.getAccedeOrderId());
										}
										// 自动还款
										boolean updateFlag = planRepayService.updateDebtRepay(debtPlan, debtPlanAccede);
										if (!updateFlag) {
											throw new Exception("计划款时发生错误。" + "[计划编号：" + planNid + "]," + "[加入订单号："
													+ debtPlanAccede.getAccedeOrderId() + "]");
										}
										// 更新还款状态 已还款
										boolean accedeFlag1 = this.planRepayService.updateAccedeStatus(debtPlanAccede,
												5);
										if (!accedeFlag1) {
											throw new Exception("更新还款状态为已还款时出错，订单号" + debtPlanAccede.getAccedeOrderId());
										}
									} catch (Exception e) {
										sbError.append(e.getMessage()).append("<br/>");
										LogUtil.errorLog(THIS_CLASS, methodName, e);
										errorCnt++;
									}
								}

								// 有错误时
								if (errorCnt > 0) {
									throw new Exception("计划款时发生错误。" + "[计划编号：" + planNid + "]," + "[错误件数：" + errorCnt
											+ "]");
								} else {
									// 发送成功短信
									Map<String, String> replaceStrs = new HashMap<String, String>();
									replaceStrs.put("val_htj_title", planNid);
									replaceStrs.put("val_balance", debtPlan.getRepayAccountInterestYes() + "");
									replaceStrs.put("val_amount", debtPlan.getRepayAccountCapitalYes() + "");
									replaceStrs.put("val_profit", debtPlan.getServiceFee() + "");
									SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null,
											MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.JYTZ_HTJ_HKCG,
											CustomConstants.CHANNEL_TYPE_NORMAL);
									smsProcesser.gather(smsMessage);
									// 取得是否线上
									String online = "生产环境";
									String payUrl = PropUtils.getSystem(CustomConstants.HYJF_ONLINE);
									if (payUrl == null || !payUrl.contains("online")) {
										online = "测试环境";
									}
									String[] toMail = new String[] {};
									if ("测试环境".equals(online)) {
										toMail = new String[] { "jiangying@hyjf.com",
												"liudandan@hyjf.com" };
									} else {
										toMail = new String[] { "wangkun@hyjf.com",
												"gaohonggang@hyjf.com", "sunjianhua@hyjf.com" };
									}
									MailMessage message = new MailMessage(null, replaceStrs, "计划还款", null, null,
											toMail, CustomConstants.JYTZ_HTJ_HKCG,
											MessageDefine.MAILSENDFORMAILINGADDRESS);
									mailMessageProcesser.gather(message);

								}
							} else {
								throw new Exception("计划还款，计划下没有加入记录，计划编号" + debtPlan.getDebtPlanNid());
							}
							// 更新任务API状态为还款成功
							DebtPlanWithBLOBs debtPlanInsert = new DebtPlanWithBLOBs();
							debtPlanInsert.setId(debtPlan.getId());
							debtPlanInsert.setDebtPlanBalance(BigDecimal.ZERO);
							debtPlanInsert.setRepayAccountYes(debtPlan.getRepayAccountYes());
							debtPlanInsert.setRepayAccountCapitalYes(debtPlan.getRepayAccountCapitalYes());
							debtPlanInsert.setRepayAccountInterestYes(debtPlan.getRepayAccountInterestYes());
							debtPlanInsert.setRepayTime(GetDate.getNowTime10());
							debtPlanInsert.setDebtPlanStatus(STATUS_SUCCESS);
							boolean planFlag = planRepayService.updateDebtPlanInfoById(debtPlanInsert);
							if (!planFlag) {
								throw new Exception("更新计划还款状态为已还款时出错，计划编号" + debtPlan.getDebtPlanNid());
							}
							// 清除重新放款任务
							runTimes.remove(planNid);
							// add by zhjangjp 汇添金优惠券还款相关 start
							CommonSoaUtils.couponRepayHTJ(planNid);
							// add by zhjangjp 汇添金优惠券还款相关 end
						} catch (Exception e) {
							int runCnt = 1;
							if (runTimes.containsKey(planNid)) {
								TimesBean bean = runTimes.get(planNid);
								bean.setCnt(bean.getCnt() + 1);
								bean.setTime(GetDate.getMyTimeInMillis());
								runCnt = bean.getCnt();
								runTimes.put(planNid, bean);
							} else {
								TimesBean bean = new TimesBean();
								bean.setCnt(runCnt);
								bean.setTime(GetDate.getMyTimeInMillis());
								bean.setStatus(1);
								runTimes.put(planNid, bean);
							}
							LogUtil.errorLog(THIS_CLASS, methodName, e);
							if (runCnt >= 3) {
								// 清除重新放款任务
								runTimes.remove(planNid);
							} else {
								// 更新任务API状态为重新执行
								planRepayService.updateDebtPlan(debtPlan.getId(), STATUS_WAIT);
							}
						} finally {
							Long endTime = GetDate.getMillis();
							LogUtil.endLog(THIS_CLASS, methodName, "汇添金计划还款任务结束。[计划编号：" + planNid + "], 耗时："
									+ (endTime - startTime) / 1000 + "s");
						}
						System.out.println("——————————————————————————————————————————————————"
								+ debtPlan.getDebtPlanNid()
								+ "计划还款结束——————————————————————————————————————————————————");
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
		}
		return false;
	}
}

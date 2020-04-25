package com.hyjf.batch.htj.plancreditrepay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hyjf.batch.htj.debtrepay.DebtBorrowRepayService;
import com.hyjf.batch.htj.planloans.TimesBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.DebtApicron;
import com.hyjf.mybatis.model.auto.DebtBorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.DebtCredit;
import com.hyjf.mybatis.model.auto.DebtLoan;
import com.hyjf.mybatis.model.auto.DebtLoanDetail;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

public class PlanCreditRepayTask {

	/** 类名 */
	private static final String THIS_CLASS = PlanCreditRepayTask.class.getName();
	/** 运行状态 */
	private static int isRun = 0;
	/** 任务状态:未执行 */
	private static final Integer STATUS_WAIT = 0;
	/** 任务状态:已完成 */
	private static final Integer STATUS_SUCCESS = 1;
	/** 任务状态:执行中 */
	private static final Integer STATUS_RUNNING = 2;
	/** 任务状态:错误 */
	private static final Integer STATUS_ERROR = 9;
	/** 任务执行次数 */
	public Map<String, TimesBean> runTimes = new HashMap<String, TimesBean>();

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	@Autowired
	PlanCreditRepayService creditRepayService;

	@Autowired
	DebtBorrowRepayService debtBorrowRepayService;

	public void run() {
		// 调用还款接口
		repay();
	}

	/**
	 * 调用还款接口
	 *
	 * @return
	 */
	private boolean repay() {

		String methodName = "repay";
		if (isRun == 0) {
			isRun = 1;
			try {
				// 取得债转未还款任务
				List<DebtApicron> listApicron = creditRepayService.getBorrowApicronList(1, STATUS_SUCCESS, STATUS_WAIT);
				if (listApicron != null && listApicron.size() > 0) {
					// 循环进行还款
					for (DebtApicron apicron : listApicron) {
						// 还款错误数
						int errorCnt = 0;
						// 还款时间
						Long startTime = GetDate.getMillis();
						// 错误信息
						StringBuffer sbError = new StringBuffer();
						try {
							LogUtil.startLog(THIS_CLASS, methodName, "债转自动还款任务开始。[订单号：" + apicron.getBorrowNid() + "]");
							// 借款编号
							String borrowNid = apicron.getBorrowNid();
							// 借款人ID
							Integer borrowUserId = apicron.getUserId();
							// 当前还款的期数
							int periodNow = apicron.getPeriodNow();
							// 查询相应的borrow表的数据
							DebtBorrowWithBLOBs borrow = this.creditRepayService.getBorrow(borrowNid);
							if (borrow == null) {
								throw new Exception(
										"借款详情不存在。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
							}
							// 还款方式
							String borrowStyle = borrow.getBorrowStyle();
							// 是否月标(true:月标, false:天标)
							boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
									|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
									|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
							// 取得借款人账户信息
							Account borrowAccount = creditRepayService.getAccountByUserId(borrowUserId);
							if (borrowAccount == null) {
								throw new Exception(
										"借款人账户不存在。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
							}
							// 借款人在汇付的账户信息
							AccountChinapnr borrowUserCust = creditRepayService.getChinapnrUserInfo(borrowUserId);
							if (borrowUserCust == null) {
								throw new Exception("借款人未开户。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
							}
							// 借款人可用余额小于还款金额时报错
							BigDecimal returnAccount = creditRepayService.getBorrowAccountWithPeriod(borrowNid,
									borrowStyle, apicron.getPeriodNow());
							// 借款人在汇付的可用余额小于还款金额时报错
							BigDecimal chinapnrBalance = getUserBalance(borrowUserCust.getChinapnrUsrcustid());
							if (chinapnrBalance.compareTo(returnAccount) < 0) {
								System.out.println("借款人在汇付平台的金额不足。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid
										+ "]，" + "[可用余额：" + chinapnrBalance + "]，" + "[还款金额：" + returnAccount + "]");
								throw new Exception("借款人在汇付平台的金额不足。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid
										+ "]，" + "[可用余额：" + chinapnrBalance + "]，" + "[还款金额：" + returnAccount + "]");
							}
							// 取得还款明细列表
							List<DebtLoan> debtLoanList = creditRepayService.getBorrowRecoverList(borrowNid);
							// 还款总件数
							int size = debtLoanList.size();
							// 还款件数大于0
							if (debtLoanList != null && size > 0) {
								// 更新任务债转状态为进行中
								creditRepayService.updateBorrowApicron(apicron.getId(), STATUS_RUNNING);
								/** 循环出借详情列表 */
								for (int i = 0; i < size; i++) {
									// 还款信息
									DebtLoan debtLoan = debtLoanList.get(i);
									try {
										// 出借订单号
										String investOrderId = debtLoan.getInvestOrderId();
										// 出借用户id
										int tenderUserId = debtLoan.getUserId();
										// 查询债转记录表，此标这笔出借此期是否有债转
										List<DebtCredit> borrowCreditList = this.creditRepayService
												.selectBorrowCreditList(borrowNid, investOrderId, periodNow - 1, 2);
										// 如果发生债转
										if (borrowCreditList != null && borrowCreditList.size() > 0) {
											// 遍历循环处理债转还款
											for (DebtCredit debtCredit : borrowCreditList) {
												if (debtCredit != null) {
													// 查询相应的债权
													debtCredit = this.creditRepayService
															.selectDebtCredit(debtCredit.getId());
													// 如果债权正在承接中
													if (debtCredit.getCreditStatus() == 0) {
														boolean creditRepayFlag = this.creditRepayService
																.creditAssignRepay(apicron, borrow, borrowUserCust,
																		debtLoan, debtCredit);
														// 还款成功后更新相应的债转数据为清算完成
														if (!creditRepayFlag) {
															throw new Exception(
																	"债转承接中还款失败，债转编号：" + debtCredit.getCreditNid());
														}
													}
													// 承接中止
													else if (debtCredit.getCreditStatus() == 1) {
														// 债转还款相应的已承接部分
														boolean creditRepayFlag = this.creditRepayService.creditRepay(
																apicron, borrow, borrowUserCust, debtLoan, debtCredit);
														if (creditRepayFlag) {
															boolean liquidatesFlag = this.creditRepayService
																	.updateCreditLiquidates(borrow, apicron,
																			debtCredit);
															if (!liquidatesFlag) {
																throw new Exception("债转承接完成还款后更新相应的清算状态失败，债转编号："
																		+ debtCredit.getCreditNid());
															}
														} else {
															throw new Exception(
																	"债转承接完成债转部分还款失败，债转编号：" + debtCredit.getCreditNid());
														}
													}
													// 完全承接完成
													else if (debtCredit.getCreditStatus() == 2) {
														// 债转还款相应的已承接部分
														boolean creditRepayFlag = this.creditRepayService.creditRepay(
																apicron, borrow, borrowUserCust, debtLoan, debtCredit);
														if (creditRepayFlag) {
															boolean liquidatesFlag = this.creditRepayService
																	.updateCreditLiquidates(borrow, apicron,
																			debtCredit);
															if (!liquidatesFlag) {
																throw new Exception("债转完全承接还款后更新相应的清算状态失败，债转编号："
																		+ debtCredit.getCreditNid());
															}
														} else {
															throw new Exception(
																	"债转完全承接债转部分还款失败，债转编号：" + debtCredit.getCreditNid());
														}
													}
												} else {
													throw new Exception("债转记录(hyjf_debt_credit)为空！" + "[出借订单号："
															+ investOrderId + "]");
												}
											}
										}
										// 债转还款完成后，还剩余部分
										debtLoan = this.creditRepayService.getBorrowRecover(debtLoan.getId());
										// 还款订单号
										String repayOrderId = null;
										// 还款订单日期
										String repayOrderDate = null;
										if (isMonth) {
											DebtLoanDetail borrowRecoverPlan = null;
											// 取得分期还款计划表
											borrowRecoverPlan = creditRepayService.getBorrowRecoverPlan(borrowNid,
													periodNow, tenderUserId, debtLoan.getInvestId());
											if (borrowRecoverPlan != null) {
												// 保存还款订单号
												if (StringUtils.isBlank(borrowRecoverPlan.getRepayOrderId())) {
													// 还款订单号
													repayOrderId = GetOrderIdUtils.getOrderId2(debtLoan.getUserId());
													// 还款订单日期
													repayOrderDate = GetOrderIdUtils.getOrderDate();
													// 设置还款订单号
													borrowRecoverPlan.setRepayOrderId(repayOrderId);
													// 设置还款时间
													borrowRecoverPlan.setRepayOrderDate(repayOrderDate);
													// 更新还款信息
													boolean recoverPlanFlag = this.creditRepayService
															.updateBorrowRecoverPlan(borrowRecoverPlan) > 0 ? true
																	: false;
													if (!recoverPlanFlag) {
														throw new RuntimeException("添加还款订单号，更新borrow_recover_plan表失败"
																+ "，[出借订单号：" + investOrderId + "]");
													}
													// 设置还款订单号
													debtLoan.setRepayOrderId(repayOrderId);
													// 设置还款时间
													debtLoan.setRepayOrderDate(repayOrderDate);
													// 更新还款信息
													boolean recoverFlag = this.creditRepayService
															.updateBorrowRecover(debtLoan) > 0 ? true : false;
													if (!recoverFlag) {
														throw new RuntimeException("添加还款订单号，更新borrow_recover表失败"
																+ "，[出借订单号：" + investOrderId + "]");
													}
												}
											} else {
												throw new RuntimeException("分期还款计划表数据不存在。[借款编号：" + borrowNid + "]，"
														+ "[出借订单号：" + investOrderId + "]，" + "[期数：" + periodNow + "]");
											}
										}
										// [endday: 按天计息, end:按月计息]
										else {
											// 保存还款订单号
											if (StringUtils.isBlank(debtLoan.getRepayOrderId())) {
												// 还款订单号
												repayOrderId = GetOrderIdUtils.getOrderId2(debtLoan.getUserId());
												// 还款订单日期
												repayOrderDate = GetOrderIdUtils.getOrderDate();
												// 设置还款订单号
												debtLoan.setRepayOrderId(repayOrderId);
												// 设置还款时间
												debtLoan.setRepayOrderDate(repayOrderDate);
												// 更新还款信息
												boolean flag = this.creditRepayService.updateBorrowRecover(debtLoan) > 0
														? true : false;
												if (!flag) {
													throw new RuntimeException("添加还款订单号，更新borrow_recover表失败"
															+ "，[出借订单号：" + investOrderId + "]");
												}
											}
										}
										// 自动还款
										boolean borrowRepayFlag = this.creditRepayService.updateBorrowRepay(apicron,
												debtLoan, borrowUserCust);
										if (!borrowRepayFlag) {
											throw new Exception("债转未承接债转部分还款失败，出借订单号：" + debtLoan.getInvestOrderId());
										}
									} catch (Exception e) {
										errorCnt++;
										sbError.append(errorCnt).append(".").append(e.getMessage()).append("<br/>");
										LogUtil.errorLog(THIS_CLASS, methodName, e);
									}
								}
								// 有错误时
								if (errorCnt > 0) {
									throw new Exception(
											"债转还款时发生错误。" + "[借款编号：" + borrowNid + "]，" + "[错误件数：" + errorCnt + "]");
								} else {
									// 更新最后还款状态
									this.creditRepayService.updateBorrowStatus(borrowNid, apicron.getPeriodNow(),
											borrowUserId);
									// 更新任务API状态为完成
									creditRepayService.updateBorrowApicron(apicron.getId(), STATUS_SUCCESS);
								}
							} else {
								// 更新最后还款状态
								this.creditRepayService.updateBorrowStatus(borrowNid, apicron.getPeriodNow(),
										borrowUserId);
								// 项目没有发生债转
								this.creditRepayService.updateBorrowApicron(apicron.getId(), STATUS_SUCCESS);
								LogUtil.debugLog(THIS_CLASS, methodName, "还款明细件数为0件。[标号：" + borrowNid + "]");
							}
						} catch (Exception e) {
							int runCnt = 1;
							if (runTimes.containsKey(apicron.getBorrowNid())) {
								TimesBean bean = runTimes.get(apicron.getBorrowNid());
								bean.setCnt(bean.getCnt() + 1);
								bean.setTime(GetDate.getMyTimeInMillis());
								runCnt = bean.getCnt();
								runTimes.put(apicron.getBorrowNid(), bean);
							} else {
								TimesBean bean = new TimesBean();
								bean.setCnt(runCnt);
								bean.setTime(GetDate.getMyTimeInMillis());
								bean.setStatus(1);
								runTimes.put(apicron.getBorrowNid(), bean);
							}
							LogUtil.errorLog(THIS_CLASS, methodName, e);
							if (runCnt >= 3) {
								// 清除重新还款任务
								runTimes.remove(apicron.getBorrowNid());
								if (sbError.length() == 0) {
									sbError.append(e.getMessage());
								}
								// 更新任务API状态为错误
								creditRepayService.updateBorrowApicron(apicron.getId(), STATUS_ERROR,
										sbError.toString());
							} else {
								// 更新任务API状态为重新执行
								creditRepayService.updateBorrowApicron(apicron.getId(), STATUS_WAIT);
							}
							// 取得是否线上
							String online = "生产环境";
							String payUrl = PropUtils.getSystem(CustomConstants.HYJF_PAY_URL);
							if (payUrl == null || !payUrl.contains("www.hyjf.com")) {
								online = "测试环境";
							}
							// 发送错误邮件
							StringBuffer msg = new StringBuffer();
							msg.append("借款标号：").append(apicron.getBorrowNid()).append("<br/>");
							msg.append("当前期数：").append(apicron.getPeriodNow()).append("<br/>");
							msg.append("还款时间：").append(GetDate.formatTime()).append("<br/>");
							msg.append("执行次数：").append("第" + runCnt + "次").append("<br/>");
							msg.append("错误信息：").append(e.getMessage()).append("<br/>");
							msg.append("详细错误信息：<br/>").append(sbError.toString());
							// 发送邮件
							String[] toMail = new String[] {};
							if ("测试环境".equals(online)) {
								toMail = new String[] { "jiangying@hyjf.com", "liudandan@hyjf.com" };
							} else {
								toMail = new String[] { "wangkun@hyjf.com",
										"gaohonggang@hyjf.com" };
							}
							MailMessage message = new MailMessage(null, null,
									"[" + online + "] " + apicron.getBorrowNid() + "-" + apicron.getPeriodNow() + " 第"
											+ runCnt + "次还款失败",
									msg.toString(), null, toMail, null, MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
							mailMessageProcesser.gather(message);
							LogUtil.endLog(THIS_CLASS, methodName,
									"债转自动还款任务发生错误。[项目编号：" + apicron.getBorrowNid() + "]");
						} finally {
							Long endTime = GetDate.getMillis();
							LogUtil.endLog(THIS_CLASS, methodName, "债转自动还款任务结束。[项目编号：" + apicron.getBorrowNid()
									+ "]， 耗时：" + (endTime - startTime) / 1000 + "s");
						}
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

	/**
	 * 查询客户在汇付的余额
	 *
	 * @param usrCustId
	 * @return
	 */
	private BigDecimal getUserBalance(Long usrCustId) {
		BigDecimal balance = BigDecimal.ZERO;

		// 调用汇付接口(交易状态查询)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_BALANCE_BG); // 消息类型(必须)
		bean.setUsrCustId(String.valueOf(usrCustId));
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
}

package com.hyjf.batch.borrow.repay;

import com.hyjf.batch.borrow.loans.TimesBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class BorrowRepayTask {

	/** 类名 */
	private static final String THIS_CLASS = BorrowRepayTask.class.getName();
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
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	@Autowired
	BorrowRepayService borrowRepayService;

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
				// 取得未还款任务
				List<BorrowApicron> listApicron = borrowRepayService.getBorrowApicronList(STATUS_WAIT, 1);
				if (listApicron != null && listApicron.size() > 0) {
					// 循环进行还款
					for (BorrowApicron apicron : listApicron) {
						int errorCnt = 0;
						Long startTime = GetDate.getMillis();
						// 错误信息
						StringBuffer sbError = new StringBuffer();
						// 当前期数
						Integer periodNow = apicron.getPeriodNow();
						try {
							LogUtil.startLog(THIS_CLASS, methodName, "自动还款任务开始。[订单号：" + apicron.getBorrowNid() + "]");
							// 更新任务API状态为进行中
							borrowRepayService.updateBorrowApicron(apicron.getId(), STATUS_RUNNING);
							// 借款编号
							String borrowNid = apicron.getBorrowNid();
							// 借款人ID
							Integer borrowUserId = apicron.getUserId();
							// 取得还款明细列表
							List<BorrowRecover> listRecover = borrowRepayService.getBorrowRecoverList(borrowNid);
							// 还款总件数
							int size = listRecover.size();
							if (listRecover != null && size > 0) {
								// 取得借款人账户信息
								Account borrowAccount = borrowRepayService.getAccountByUserId(borrowUserId);
								if (borrowAccount == null) {
									throw new Exception("借款人账户不存在。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
								}
								// 借款人在汇付的账户信息
								AccountChinapnr borrowUserCust = borrowRepayService.getChinapnrUserInfo(borrowUserId);
								if (borrowUserCust == null) {
									throw new Exception("借款人未开户。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
								}
								// 取得借款详情
								BorrowWithBLOBs borrow = borrowRepayService.getBorrow(borrowNid);
								if (borrow == null) {
									throw new Exception("借款详情不存在。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
								}
								// 还款方式
								String borrowStyle = borrow.getBorrowStyle();
								// 借款人可用余额小于还款金额时报错
								BigDecimal returnAccount = borrowRepayService.getBorrowAccountWithPeriod(borrowNid, borrow.getBorrowStyle(), apicron.getPeriodNow());
								// 借款人在汇付的可用余额小于还款金额时报错
								BigDecimal chinapnrBalance = getUserBalance(borrowUserCust.getChinapnrUsrcustid());
								if (chinapnrBalance.compareTo(returnAccount) < 0) {
									System.out.println("借款人在汇付平台的金额不足。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]，" + "[可用余额：" + chinapnrBalance + "]，" + "[还款金额：" + returnAccount + "]");
									throw new Exception("借款人在汇付平台的金额不足。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]，" + "[可用余额：" + chinapnrBalance + "]，" + "[还款金额：" + returnAccount + "]");
								}
								BorrowRecover borrowRecover = null;
								/** 循环出借详情列表 */
								for (int i = 0; i < size; i++) {
									// 还款信息
									borrowRecover = listRecover.get(i);
									// 出借用户userId
									int tenderUserId = borrowRecover.getUserId();
									// 出借订单号
									String tenderOrdId = borrowRecover.getNid();
									try {
										// 是否月标(true:月标, false:天标)
										boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
												|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
										// 还款订单号
										String repayOrderId = null;
										// 还款订单日期
										String repayOrderDate = null;
										if (isMonth) {
											BorrowRecoverPlan borrowRecoverPlan = null;
											// 取得分期还款计划表
											borrowRecoverPlan = borrowRepayService.getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId, borrowRecover.getTenderId());
											if (borrowRecoverPlan != null) {
												// 保存还款订单号
												if (StringUtils.isBlank(borrowRecoverPlan.getRepayOrderId())) {
													// 还款订单号
													repayOrderId = GetOrderIdUtils.getOrderId2(borrowRecover.getUserId());
													// 还款订单日期
													repayOrderDate = GetOrderIdUtils.getOrderDate();
													// 设置还款订单号
													borrowRecoverPlan.setRepayOrderId(repayOrderId);
													// 设置还款时间
													borrowRecoverPlan.setRepayOrderDate(repayOrderDate);
													// 更新还款信息
													boolean recoverPlanFlag = this.borrowRepayService.updateBorrowRecoverPlan(borrowRecoverPlan) > 0 ? true : false;
													if (!recoverPlanFlag) {
														throw new RuntimeException("添加还款订单号，更新borrow_recover_plan表失败" + "，[出借订单号：" + tenderOrdId + "]");
													}
													// 设置还款订单号
													borrowRecover.setRepayOrdid(repayOrderId);
													// 设置还款时间
													borrowRecover.setRepayOrddate(repayOrderDate);
													// 更新还款信息
													boolean recoverFlag = this.borrowRepayService.updateBorrowRecover(borrowRecover) > 0 ? true : false;
													if (!recoverFlag) {
														throw new RuntimeException("添加还款订单号，更新borrow_recover表失败" + "，[出借订单号：" + tenderOrdId + "]");
													}
												}
											} else {
												throw new RuntimeException("分期还款计划表数据不存在。[借款编号：" + borrowNid + "]，" + "[出借订单号：" + tenderOrdId + "]，" + "[期数：" + periodNow + "]");
											}
										}
										// [endday: 按天计息, end:按月计息]
										else {
											// 保存还款订单号
											if (StringUtils.isBlank(borrowRecover.getRepayOrdid())) {
												// 还款订单号
												repayOrderId = GetOrderIdUtils.getOrderId2(borrowRecover.getUserId());
												// 还款订单日期
												repayOrderDate = GetOrderIdUtils.getOrderDate();
												// 设置还款订单号
												borrowRecover.setRepayOrdid(repayOrderId);
												// 设置还款时间
												borrowRecover.setRepayOrddate(repayOrderDate);
												// 更新还款信息
												boolean flag = this.borrowRepayService.updateBorrowRecover(borrowRecover) > 0 ? true : false;
												if (!flag) {
													throw new RuntimeException("添加还款订单号，更新borrow_recover表失败" + "，[出借订单号：" + tenderOrdId + "]");
												}
											}
										}
										// 自动还款
										List<Map<String, String>> msgList = borrowRepayService.updateBorrowRepay(apicron, borrowRecover, borrowUserCust);
										// 发送短信
										borrowRepayService.sendSms(msgList);
										// 推送消息
										borrowRepayService.sendMessage(msgList);
									} catch (Exception e) {
										errorCnt++;
										sbError.append(errorCnt).append(".").append(e.getMessage()).append("<br/>");
										LogUtil.errorLog(THIS_CLASS, methodName, e);
									}
								}
								// 有错误时
								if (errorCnt > 0) {
									throw new Exception("还款时发生错误。" + "[借款编号：" + borrowNid + "]，" + "[错误件数：" + errorCnt + "]");
								} else {
									// 更新最后还款状态
									this.borrowRepayService.updateBorrowStatus(borrowNid, apicron.getPeriodNow(), borrowUserId);
									// 发送成功短信
									Map<String, String> replaceStrs = new HashMap<String, String>();
									replaceStrs.put("val_title", borrow.getBorrowNid());
									replaceStrs.put("val_time", GetDate.formatTime());
									SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_HUANKUAN_SUCCESS,
											CustomConstants.CHANNEL_TYPE_NORMAL);
									smsProcesser.gather(smsMessage);
								}
							} else {
								LogUtil.debugLog(THIS_CLASS, methodName, "还款明细件数为0件。[标号：" + borrowNid + "]");
							}
							// 更新任务API状态为完成
							borrowRepayService.updateBorrowApicron(apicron.getId(), STATUS_SUCCESS);

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
								borrowRepayService.updateBorrowApicron(apicron.getId(), STATUS_ERROR, sbError.toString());
								// 发送失败短信
								BorrowWithBLOBs borrow = borrowRepayService.getBorrow(apicron.getBorrowNid());
								// 是否分期(true:分期, false:单期)
								boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle())
										|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle());
								Map<String, String> replaceStrs = new HashMap<String, String>();
								replaceStrs.put("val_title", borrow.getBorrowNid());
								replaceStrs.put("val_period", isMonth ? "第" + apicron.getPeriodNow() + "期" : "");
								replaceStrs.put("val_package_error", String.valueOf(errorCnt));
								SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_HUANKUAN_FAILD,
										CustomConstants.CHANNEL_TYPE_NORMAL);
								smsProcesser.gather(smsMessage);
							} else {
								// 更新任务API状态为重新执行
								borrowRepayService.updateBorrowApicron(apicron.getId(), STATUS_WAIT);
							}
							// 取得是否线上
							String online = "生产环境";
							String payUrl = PropUtils.getSystem(CustomConstants.HYJF_ONLINE);
							if (payUrl == null || !payUrl.contains("online")) {
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
							String[] toMail = new String[] {};
							if ("测试环境".equals(online)) {
								toMail = new String[] { "jiangying@hyjf.com", "liudandan@hyjf.com" };
							} else {
								toMail = new String[] { "wangkun@hyjf.com", "gaohonggang@hyjf.com" };
							}
							MailMessage message = new MailMessage(null, null, "[" + online + "] " + apicron.getBorrowNid() + "-" + apicron.getPeriodNow() + " 第" + runCnt + "次还款失败", msg.toString(),
									null, toMail, null, MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
							mailMessageProcesser.gather(message);
							LogUtil.endLog(THIS_CLASS, methodName, "自动还款任务发生错误。[订单号：" + apicron.getBorrowNid() + "]");
						} finally {
							Long endTime = GetDate.getMillis();
							LogUtil.endLog(THIS_CLASS, methodName, "自动还款任务结束。[订单号：" + apicron.getBorrowNid() + "]， 耗时：" + (endTime - startTime) / 1000 + "s");
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
		bean.setUsrCustId(String.valueOf(usrCustId));// 用户客户号(必须)
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

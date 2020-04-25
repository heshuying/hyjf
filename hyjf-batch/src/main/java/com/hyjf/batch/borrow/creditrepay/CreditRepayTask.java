package com.hyjf.batch.borrow.creditrepay;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hyjf.batch.borrow.loans.TimesBean;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.soa.apiweb.CommonSoaUtils;

public class CreditRepayTask {

	/** 类名 */
	private static final String THIS_CLASS = CreditRepayTask.class.getName();
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
	CreditRepayService creditRepayService;
	
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
				List<BorrowApicron> listApicron = creditRepayService.getBorrowApicronList(1, STATUS_SUCCESS,STATUS_WAIT);
				if (listApicron != null && listApicron.size() > 0) {
					// 循环进行还款
					for (BorrowApicron apicron : listApicron) {
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
							BorrowWithBLOBs borrow = this.creditRepayService.getBorrow(borrowNid);
							if (borrow == null) {
								throw new Exception("借款详情不存在。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
							}
							// 项目总期数
							Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
							// 还款方式
							String borrowStyle = borrow.getBorrowStyle();
							// 是否月标(true:月标, false:天标)
							boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
									|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
									|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
							// 取得借款人账户信息
							Account borrowAccount = creditRepayService.getAccountByUserId(borrowUserId);
							if (borrowAccount == null) {
								throw new Exception("借款人账户不存在。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
							}
							// 借款人在汇付的账户信息
							AccountChinapnr borrowUserCust = creditRepayService.getChinapnrUserInfo(borrowUserId);
							if (borrowUserCust == null) {
								throw new Exception("借款人未开户。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
							}
							// 借款人可用余额小于还款金额时报错
							BigDecimal returnAccount = creditRepayService.getBorrowAccountWithPeriod(borrowNid,borrowStyle, apicron.getPeriodNow());
							// 借款人在汇付的可用余额小于还款金额时报错
							BigDecimal chinapnrBalance = getUserBalance(borrowUserCust.getChinapnrUsrcustid());
							if (chinapnrBalance.compareTo(returnAccount) < 0) {
								System.out.println("借款人在汇付平台的金额不足。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]，" + "[可用余额：" + chinapnrBalance + "]，" + "[还款金额：" + returnAccount + "]");
								throw new Exception("借款人在汇付平台的金额不足。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]，" + "[可用余额：" + chinapnrBalance + "]，" + "[还款金额：" + returnAccount + "]");
							}
							// 取得还款明细列表
							List<BorrowRecover> listRecover = creditRepayService.getBorrowRecoverList(borrowNid);
							// 还款总件数
							int size = listRecover.size();
							// 还款件数大于0
							if (listRecover != null && size > 0) {
								// 更新任务债转状态为进行中
								creditRepayService.updateBorrowApicron(apicron.getId(), STATUS_RUNNING);
								/** 循环出借详情列表 */
								for (int i = 0; i < size; i++) {
									// 还款信息
									BorrowRecover borrowRecover = listRecover.get(i);
									// 还款时间
									Integer recoverLastTime = borrowRecover.getCreateTime();
									// 还款时间
									Date recoverLastDate = GetDate.getDate(recoverLastTime * 1000L);
									// 剩余还款期数
									Integer periodNext = borrowPeriod - periodNow;
									try {
										// 出借订单号
										String tenderOrdId = borrowRecover.getNid();
										// 出借用户id
										int tenderUserId = borrowRecover.getUserId();
										// 查询债转记录表，此标这笔出借此期是否有债转
										List<BorrowCredit> borrowCreditList = this.creditRepayService.selectBorrowCreditList(borrowNid, tenderOrdId, periodNow - 1, 2);
										// 如果发生债转
										if (borrowCreditList != null && borrowCreditList.size() > 0) {
											// 遍历循环处理债转还款
											for (BorrowCredit borrowCredit : borrowCreditList) {
												if (borrowCredit != null) {
													// 当前时间
													int nowTime = GetDate.getNowTime10();
													// 债转编号
													int creditNid = borrowCredit.getCreditNid();
													// 查询债转的出借记录
													List<CreditTender> creditTenderList = this.creditRepayService.selectCreditTenderList(borrowNid, creditNid);
													if (creditTenderList != null && creditTenderList.size() > 0) {
														System.out.println("------此笔债转承接部分还款开始---项目编号："+borrowNid+"----债转编号：" + creditNid + "---------");
														// 遍历债转出借
														for (int j = 0; j < creditTenderList.size(); j++) {
															// 债转出借信息
															CreditTender creditTender = creditTenderList.get(j);
															if (creditTender != null) {
																// 承接用户userId
																int assignUserId = creditTender.getUserId();
																// 承接订单号
																String assignNid = creditTender.getAssignNid();
																// 查询此笔债转承接的还款情况
																CreditRepay creditRepay = this.creditRepayService.selectCreditRepay(borrowNid, assignUserId, tenderOrdId, assignNid, periodNow, 0);
																if (Validator.isNotNull(creditRepay)) {
																	// 还款订单号
																	String repayOrderId = null;
																	// 还款订单日期
																	String repayOrderDate = null;
																	// 保存债转还款订单号
																	if (StringUtils.isBlank(creditRepay.getCreditRepayOrderId())) {
																		// 还款订单号
																		repayOrderId = GetOrderIdUtils.getOrderId2(borrowRecover.getUserId());
																		// 还款订单日期
																		repayOrderDate = GetOrderIdUtils.getOrderDate();
																		// 还款订单号
																		creditRepay.setCreditRepayOrderId(repayOrderId);
																		// 还款订单日期
																		creditRepay.setCreditRepayOrderDate(repayOrderDate);
																		// 更新还款订单号
																		boolean flag = this.creditRepayService.updateCreditRepay(creditRepay) > 0 ? true : false;
																		if (!flag) {
																			throw new Exception("添加还款订单号，更新credit_repay表失败" + "，[承接订单号：" + creditRepay.getAssignNid() + "]+,期数：" + periodNow);
																		}
																	}
																	// 自动还款
																	List<Map<String, String>> msgList = creditRepayService.updateBorrowCreditRepay(apicron, borrowRecover, borrowUserCust, borrowCredit, creditTender, creditRepay);
																	// 发送短信
																	creditRepayService.sendSms(msgList);
																	// 推送债转收到还款消息
																	creditRepayService.sendCreditMessage(msgList);
																} else {
																	throw new Exception("creditRepay未有此笔债转订单的还款信息" + "，[承接订单号：" + assignNid + "]");
																}
															} else {
																throw new Exception("债转记录(huiyingdai_credit_tender)查询失败，债转记录为空！"+ "[出借订单号：" + tenderOrdId + "]");
															}
														}
														// 取得分期还款计划表
														BorrowRecoverPlan borrowRecoverPlan = creditRepayService.getBorrowRecoverPlan(borrowNid, periodNow,tenderUserId, borrowRecover.getTenderId());
														// 债转状态
														if (borrowRecoverPlan != null && Validator.isNotNull(periodNext)&& periodNext > 0) {
															borrowCredit.setCreditStatus(1);
														} else {
															borrowCredit.setCreditStatus(2);
															// 债转最后还款时间
															borrowCredit.setCreditRepayYesTime(isMonth ? 0 : nowTime);
														}
														// 下期还款时间
														int repayNextTime = DateUtils.getRepayDate(borrowStyle,recoverLastDate, periodNow + 1, 0);	
														borrowCredit.setCreditRepayNextTime(repayNextTime);
														// 债转最近还款时间
														borrowCredit.setCreditRepayLastTime(nowTime);
														// 债转还款期
														borrowCredit.setRecoverPeriod(periodNow);
														boolean flag = this.creditRepayService.updateBorrowCredit(borrowCredit) > 0 ? true : false;
														if (!flag) {
															throw new Exception("债转记录(huiyingdai_borrow_credit)更新失败！"+ "[债转编号：" + borrowCredit.getCreditNid() + "]");
														}else{
															//推送消息债转结束
															creditRepayService.sendCreditEndMessage(borrowCredit);
														}
														System.out.println("------此笔债转承接部分还款结束---项目编号："+borrowNid+"----债转编号：" + creditNid + "---------");
													} else {
														System.out.println("------此笔债转未有承接记录,或者债转还款已经完成，更新borrowCredit---项目编号："+borrowNid+"----债转编号：" + creditNid + "---------");
														// 下期还款时间
														int repayNextTime = DateUtils.getRepayDate(borrowStyle,recoverLastDate, periodNow + 1, 0);														// 取得分期还款计划表
														BorrowRecoverPlan borrowRecoverPlan = creditRepayService.getBorrowRecoverPlan(borrowNid, periodNow,tenderUserId, borrowRecover.getTenderId());
														if (borrowRecoverPlan != null && Validator.isNotNull(periodNext)&& periodNext > 0) {
															// 债转项目没有出借记录
															this.creditRepayService.updateBorrowCredit(creditNid,periodNow - 1, 1, nowTime,isMonth ? repayNextTime : 0);
															// 推送消息债转结束
															creditRepayService.sendCreditEndMessage(borrowCredit);
														} else {
															// 债转项目没有出借记录
															this.creditRepayService.updateBorrowCredit(creditNid,periodNow - 1, 2, nowTime, nowTime);
															// 推送消息债转结束
															creditRepayService.sendCreditEndMessage(borrowCredit);
														}
														System.out.println("------此笔债转未有承接记录,或者债转还款已经完成，更新borrowCredit---项目编号："+borrowNid+"----债转编号：" + creditNid + "---------");
													}
												} else {
													throw new Exception("债转记录(huiyingdai_borrow_credit)为空！"+ "[出借订单号：" + tenderOrdId + "]");
												}
											}
											// 债转还款完成后，还剩余部分
											// 还款订单号
											String repayOrderId = null;
											// 还款订单日期
											String repayOrderDate = null;
											if (isMonth) {
												BorrowRecoverPlan borrowRecoverPlan = null;
												// 取得分期还款计划表
												borrowRecoverPlan = creditRepayService.getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId, borrowRecover.getTenderId());
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
														boolean recoverPlanFlag = this.creditRepayService.updateBorrowRecoverPlan(borrowRecoverPlan) > 0 ? true : false;
														if (!recoverPlanFlag) {
															throw new RuntimeException("添加还款订单号，更新borrow_recover_plan表失败" + "，[出借订单号：" + tenderOrdId + "]");
														}
														// 设置还款订单号
														borrowRecover.setRepayOrdid(repayOrderId);
														// 设置还款时间
														borrowRecover.setRepayOrddate(repayOrderDate);
														// 更新还款信息
														boolean recoverFlag = this.creditRepayService.updateBorrowRecover(borrowRecover) > 0 ? true : false;
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
													boolean flag = this.creditRepayService.updateBorrowRecover(borrowRecover) > 0 ? true : false;
													if (!flag) {
														throw new RuntimeException("添加还款订单号，更新borrow_recover表失败" + "，[出借订单号：" + tenderOrdId + "]");
													}
												}
											}
											// 自动还款
	                                        List<Map<String, String>> msgList = creditRepayService.updateBorrowRepay(apicron, borrowRecover, borrowUserCust);
	                                        // 发送短信
	                                        creditRepayService.sendSms(msgList);
	                                        // 推送消息收到还款
	                                        creditRepayService.sendMessage(msgList);
										}else{
											//未债转还款
											throw new Exception("未查询到相应的债转记录(huiyingdai_borrow_credit)！"+ "[出借订单号：" + tenderOrdId + "]");
										}
									} catch (Exception e) {
										errorCnt++;
										sbError.append(errorCnt).append(".").append(e.getMessage()).append("<br/>");
										LogUtil.errorLog(THIS_CLASS, methodName, e);
									}
								}
								// 有错误时
								if (errorCnt > 0) {
									throw new Exception("债转还款时发生错误。" + "[借款编号：" + borrowNid + "]，" + "[错误件数：" + errorCnt + "]");
								} else {
									// 更新最后还款状态
									this.creditRepayService.updateBorrowStatus(borrowNid, apicron.getPeriodNow(),borrowUserId);
									// 发送成功短信
									Map<String, String> replaceStrs = new HashMap<String, String>();
									replaceStrs.put("val_title", borrowNid);
									replaceStrs.put("val_time", GetDate.formatTime());
									SmsMessage smsMessage =
						                    new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null,
						                    		CustomConstants.PARAM_TPL_HUANKUAN_SUCCESS, CustomConstants.CHANNEL_TYPE_NORMAL);
								    smsProcesser.gather(smsMessage);
									// 更新任务API状态为完成
									creditRepayService.updateBorrowApicron(apicron.getId(), STATUS_SUCCESS);
								}

								// add by zhjangjp 优惠券还款相关 start
								CommonSoaUtils.couponRepay(borrowNid, periodNow);
								// add by zhjangjp 优惠券还款相关 end
							} else {
								// 更新最后还款状态
								this.creditRepayService.updateBorrowStatus(borrowNid, apicron.getPeriodNow(),borrowUserId);
								// 项目没有发生债转
								this.creditRepayService.updateBorrowApicron(apicron.getId(), STATUS_SUCCESS);
								LogUtil.debugLog(THIS_CLASS, methodName, "还款明细件数为0件。[标号：" + borrowNid + "]");

								// add by zhjangjp 优惠券还款相关 start
								CommonSoaUtils.couponRepay(borrowNid, periodNow);
								// add by zhjangjp 优惠券还款相关 end
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
								creditRepayService.updateBorrowApicron(apicron.getId(), STATUS_ERROR,sbError.toString());
								// 发送失败短信
								BorrowWithBLOBs borrow = creditRepayService.getBorrow(apicron.getBorrowNid());
								// 是否分期(true:分期, false:单期)
								boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle())
										|| CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle())
										|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle());
								Map<String, String> replaceStrs = new HashMap<String, String>();
								replaceStrs.put("val_title", borrow.getBorrowNid());
								replaceStrs.put("val_period", isMonth ? "第" + apicron.getPeriodNow() + "期" : "");
								replaceStrs.put("val_package_error", String.valueOf(errorCnt));
								SmsMessage smsMessage =
					                    new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null,
					                    		CustomConstants.PARAM_TPL_HUANKUAN_FAILD, CustomConstants.CHANNEL_TYPE_NORMAL);
							    smsProcesser.gather(smsMessage);
							} else {
								// 更新任务API状态为重新执行
								creditRepayService.updateBorrowApicron(apicron.getId(), STATUS_WAIT);
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
							// 发送邮件
							String[] toMail = new String[] {};
							if ("测试环境".equals(online)) {
						           toMail = new String[] { "jiangying@hyjf.com", "liudandan@hyjf.com" };
							} else {
								toMail = new String[] { "wangkun@hyjf.com", "gaohonggang@hyjf.com" };
							}
							MailMessage message = new MailMessage(null, null, "[" + online + "] " + apicron.getBorrowNid() + "-" + apicron.getPeriodNow() + " 第" + runCnt + "次还款失败",msg.toString(),null, toMail, null, MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
			                mailMessageProcesser.gather(message);
							LogUtil.endLog(THIS_CLASS, methodName,"债转自动还款任务发生错误。[项目编号：" + apicron.getBorrowNid() + "]");
						} finally {
							Long endTime = GetDate.getMillis();
							LogUtil.endLog(THIS_CLASS, methodName, "债转自动还款任务结束。[项目编号：" + apicron.getBorrowNid()+ "]， 耗时：" + (endTime - startTime) / 1000 + "s");
						}
					}
				}
			}catch(Exception e){
            	e.printStackTrace();
            }finally{
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

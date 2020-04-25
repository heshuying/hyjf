package com.hyjf.batch.borrow.loans;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BorrowLoansTask {
	/** 类名 */
	private static final String THIS_CLASS = BorrowLoansTask.class.getName();

	private static DecimalFormat DF_COM_VIEW = new DecimalFormat("######0.00");

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
	BorrowLoansService borrowLoansService;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	public void run() {
		// 调用放款接口
		loans();
	}

	/**
	 * 调用放款接口
	 *
	 * @return
	 */
	private boolean loans() {
		String methodName = "loans";
		if (isRun == 0) {
			isRun = 1;

			try {
				// 取得未放款任务
				List<BorrowApicron> listApicron = borrowLoansService.getBorrowApicronList(STATUS_WAIT, 0);
				if (listApicron != null && listApicron.size() > 0) {
					// 循环进行放款
					for (BorrowApicron apicron : listApicron) {
						int errorCnt = 0;
						Long startTime = GetDate.getMillis();
						// 错误信息
						StringBuffer sbError = new StringBuffer();
						try {
							LogUtil.startLog(THIS_CLASS, methodName, "自动放款任务开始。[订单号：" + apicron.getBorrowNid() + "]");
							// 更新任务API状态为进行中
							borrowLoansService.updateBorrowApicron(apicron.getId(), STATUS_RUNNING);
							// 借款编号
							String borrowNid = apicron.getBorrowNid();
							// 借款人ID
							Integer borrowUserId = apicron.getUserId();
							// 取得出借详情列表
							List<BorrowTender> listTender = borrowLoansService.getBorrowTenderList(borrowNid);
							if (listTender != null && listTender.size() > 0) {
								// 取得借款人账户信息
								Account borrowAccount = borrowLoansService.getAccountByUserId(borrowUserId);
								if (borrowAccount == null) {
									throw new Exception("借款人账户不存在。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
								}
								// 借款人在汇付的账户信息
								AccountChinapnr borrowUserCust = borrowLoansService.getChinapnrUserInfo(borrowUserId);
								if (borrowUserCust == null) {
									throw new Exception("借款人未开户。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
								}
								// 取得借款详情
								BorrowWithBLOBs borrow = borrowLoansService.getBorrow(borrowNid);
								if (borrow == null) {
									throw new Exception("借款详情不存在。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
								}
								// 出借信息
								BorrowTender borrowTender = null;
								// 出借总件数
								int size = listTender.size();
								/** 循环出借详情列表 */
								for (int i = 0; i < size; i++) {
									borrowTender = listTender.get(i);
									try {
										if (Validator.isNull(borrowTender.getLoanOrdid())) {
											// 设置放款订单号
											borrowTender.setLoanOrdid(GetOrderIdUtils.getOrderId2(borrowTender.getUserId()));
											// 设置放款时间
											borrowTender.setLoanOrderDate(GetOrderIdUtils.getOrderDate());
											// 更新放款信息
											borrowLoansService.updateBorrowTender(borrowTender);
										}
										// 自动放款
										List<Map<String, String>> msgList = borrowLoansService.updateBorrowLoans(apicron, borrowTender);
										if (msgList != null && msgList.size() > 0) {
											System.out.println(msgList);
											// 发送短信
											borrowLoansService.sendSms(msgList);
											// 发送邮件
											borrowLoansService.sendMail(msgList, apicron.getBorrowNid());
											// 发送推送消息
											borrowLoansService.sendMessage(msgList);
										}
									} catch (Exception e) {
										sbError.append(e.getMessage()).append("<br/>");
										LogUtil.errorLog(THIS_CLASS, methodName, e);
										errorCnt++;
									}
								}

								// 有错误时
								if (errorCnt > 0) {
									throw new Exception("放款时发生错误。" + "[借款编号：" + borrowNid + "]," + "[错误件数：" + errorCnt + "]");
								} else {
									// 更新最后放款状态
									this.borrowLoansService.updateBorrowStatus(apicron.getNid(), borrowNid, borrowUserId);
									// 给借款人发送邮件
									List<Map<String, String>> msgList = new ArrayList<Map<String, String>>();
									Map<String, String> borrowUserInfo = new HashMap<String, String>();
									borrowUserInfo.put("userId", String.valueOf(borrow.getUserId()));
									UsersInfo borrowUsersInfo = this.borrowLoansService.getUsersInfoByUserId(borrow.getUserId());
									if (borrowUsersInfo != null) {
										borrowUserInfo.put("val_name", borrowUsersInfo.getTruename().substring(0, 1));
										borrowUserInfo.put("val_sex", borrowUsersInfo.getSex() == 1 ? "先生" : "女士");
										msgList.add(borrowUserInfo);
										borrowLoansService.sendMail(msgList, apicron.getBorrowNid());
									}
									// 管理员发送成功短信
									borrow = borrowLoansService.getBorrow(apicron.getBorrowNid());
									Map<String, String> replaceStrs = new HashMap<String, String>();
									replaceStrs.put("val_title", borrow.getBorrowNid());
									replaceStrs.put("val_time", GetDate.formatTime());
									SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_FANGKUAN_SUCCESS,
											CustomConstants.CHANNEL_TYPE_NORMAL);
									smsProcesser.gather(smsMessage);

									// 借款人发送成功短信
									AccountBorrow accountBorrow = borrowLoansService.getAccountBorrow(borrowNid);
									Map<String, String> borrowerReplaceStrs = new HashMap<String, String>();
									borrowerReplaceStrs.put("val_borrownid", borrow.getBorrowNid());
									borrowerReplaceStrs.put("val_amount", DF_COM_VIEW.format(accountBorrow.getBalance()));
									SmsMessage borrowerSmsMessage = new SmsMessage(borrow.getUserId(), borrowerReplaceStrs, null, null, MessageDefine.SMSSENDFORUSER, null,
											CustomConstants.PARAM_TPL_JIEKUAN_SUCCESS, CustomConstants.CHANNEL_TYPE_NORMAL);
									smsProcesser.gather(borrowerSmsMessage);

									// 放款成功,更新mongo运营数据
									JSONObject params = new JSONObject();
									params.put("invest", borrow.getAccount());
									params.put("type", 1);// 散标
									rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_OPERATION_DATA, JSONObject.toJSONString(params));
								}
								// insert by zhangjp 增加优惠券放款区分 start
								CommonSoaUtils.couponLoans(borrowNid);
								// insert by zhangjp 增加优惠券放款区分 end
							}
							// 更新任务API状态为完成
							borrowLoansService.updateBorrowApicron(apicron.getId(), STATUS_SUCCESS);
							// 清除重新放款任务
							runTimes.remove(apicron.getBorrowNid());
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
								// 清除重新放款任务
								runTimes.remove(apicron.getBorrowNid());
								if (sbError.length() == 0) {
									sbError.append(e.getMessage());
								}
								// 更新任务API状态为错误
								borrowLoansService.updateBorrowApicron(apicron.getId(), STATUS_ERROR, sbError.toString());
								LogUtil.endLog(THIS_CLASS, methodName, "自动放款任务发生错误。[标号：" + apicron.getBorrowNid() + "]");
								// 发送失败短信
								BorrowWithBLOBs borrow = borrowLoansService.getBorrow(apicron.getBorrowNid());
								Map<String, String> replaceStrs = new HashMap<String, String>();
								replaceStrs.put("val_title", borrow.getBorrowNid());
								replaceStrs.put("val_package_error", String.valueOf(errorCnt));
								replaceStrs.put("val_balance", borrow.getAmountAccount() == null ? "0.00" : borrow.getAmountAccount().toString());
								replaceStrs.put("val_amount_yes", borrow.getRepayAccountCapital() == null ? "0.00" : borrow.getRepayAccountCapital().toString());
								SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_FANGKUAN_FAILD,
										CustomConstants.CHANNEL_TYPE_NORMAL);
								smsProcesser.gather(smsMessage);
							} else {
								// 更新任务API状态为重新执行
								borrowLoansService.updateBorrowApicron(apicron.getId(), STATUS_WAIT);
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
							msg.append("放款时间：").append(GetDate.formatTime()).append("<br/>");
							msg.append("执行次数：").append("第" + runCnt + "次").append("<br/>");
							msg.append("错误信息：").append(e.getMessage()).append("<br/>");
							msg.append("详细错误信息：<br/>").append(sbError.toString());
							String[] toMail = new String[] {};
							if ("测试环境".equals(online)) {
								toMail = new String[] { "jiangying@hyjf.com", "liudandan@hyjf.com" };
							} else {
								toMail = new String[] { "wangkun@hyjf.com", "gaohonggang@hyjf.com", };
							}
							MailMessage message = new MailMessage(null, null, "[" + online + "] " + apicron.getBorrowNid() + " 第" + runCnt + "次放款失败", msg.toString(), null, toMail, null,
									MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
							mailMessageProcesser.gather(message);
						} finally {
							Long endTime = GetDate.getMillis();
							LogUtil.endLog(THIS_CLASS, methodName, "自动放款任务结束。[订单号：" + apicron.getBorrowNid() + "], 耗时：" + (endTime - startTime) / 1000 + "s");
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
	 * 定时更新任务(23点,1点,3点,5点,7点)
	 */
	public void autoloans() {
		// 取得放款失败任务
		List<BorrowApicron> listApicron = borrowLoansService.getBorrowApicronList(STATUS_ERROR, 0);
		if (listApicron != null && listApicron.size() > 0) {
			for (BorrowApicron apicron : listApicron) {
				// 更新任务API状态为重新执行
				borrowLoansService.updateBorrowApicron(apicron.getId(), STATUS_WAIT);
			}
		}
		// 取得放款失败任务
		List<BorrowApicron> listApicron2 = borrowLoansService.getBorrowApicronListWithRepayStatus(STATUS_ERROR, 1);
		if (listApicron2 != null && listApicron2.size() > 0) {
			for (BorrowApicron apicron : listApicron2) {
				// 更新任务API状态为重新执行
				borrowLoansService.updateBorrowApicronOfRepayStatus(apicron.getId(), STATUS_WAIT);
			}
		}
	}
}

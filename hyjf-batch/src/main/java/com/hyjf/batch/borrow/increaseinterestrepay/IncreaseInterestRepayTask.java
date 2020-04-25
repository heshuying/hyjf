package com.hyjf.batch.borrow.increaseinterestrepay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.IncreaseInterestLoan;

public class IncreaseInterestRepayTask {
	/** 类名 */
	private static final String THIS_CLASS = IncreaseInterestRepayTask.class.getName();

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

	Logger _log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IncreaseInterestRepayService increaseInterestRepayService;

	/** 任务执行次数 */
	public Map<String, TimesBean> runTimes = new HashMap<String, TimesBean>();

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

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
				List<BorrowApicron> listApicron = increaseInterestRepayService.selectBorrowApicronList(STATUS_WAIT, 1);
				// 还款任务不为空的情况
				if (listApicron != null && listApicron.size() > 0) {
					// 循环进行还款
					for (BorrowApicron apicron : listApicron) {
						//判断是否为一次性还款，并且排序正确
						if("1".equals(apicron.getIsAllrepay()) && !sortRepay(apicron)){
							continue;
						}
						int errorCnt = 0;
						Long startTime = GetDate.getMillis();
						// 错误信息
						StringBuffer sbError = new StringBuffer();
						try {
							_log.info("融通宝加息自动还款任务开始。[借款编号:" + apicron.getBorrowNid() + "]");
							// 更新任务API状态为进行中
							increaseInterestRepayService.updateBorrowApicron(apicron.getId(), STATUS_RUNNING);
							// 借款编号
							String borrowNid = apicron.getBorrowNid();
							// 借款人ID
							Integer borrowUserId = apicron.getUserId();
							// 取得还款明细列表
							List<IncreaseInterestLoan> increaseInterestLoans = increaseInterestRepayService.selectIncreaseInterestLoanList(borrowNid);

							if (increaseInterestLoans != null && increaseInterestLoans.size() > 0) {
								// 取得借款人账户信息
								Account borrowAccount = increaseInterestRepayService.selectAccountByUserId(borrowUserId);
								if (borrowAccount == null) {
									throw new Exception("借款人账户不存在。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
								}
								// 借款人在银行的账户信息
								BankOpenAccount borrowUserCust = increaseInterestRepayService.getBankOpenAccount(borrowUserId);
								if (borrowUserCust == null || StringUtils.isEmpty(borrowUserCust.getAccount())) {
									throw new Exception("借款人未开户。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
								}
								// 取得借款详情
								BorrowWithBLOBs borrow = increaseInterestRepayService.selectBorrowInfo(borrowNid);
								if (borrow == null) {
									throw new Exception("借款详情不存在。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
								}

								// 取得还款金额
								BigDecimal repayAccount = increaseInterestRepayService.selectBorrowAccountWithPeriod(borrowNid, borrow.getBorrowStyle(), apicron.getPeriodNow());

								// 查询公司子账户金额
								BigDecimal account = this.increaseInterestRepayService.selectCompanyAccount();
								// 还款金额大于公司子账户可用余额
								if (repayAccount.compareTo(account) > 0) {
									System.out.println("公司子账户可用金额不足。" + "[借款编号：" + borrowNid + "]，" + "[可用余额：" + account + "]，" + "[还款金额：" + repayAccount + "]");
									throw new Exception("公司子账户可用金额不足。" + "[借款编号：" + borrowNid + "]，" + "[可用余额：" + account + "]，" + "[还款金额：" + repayAccount + "]");
								}

								IncreaseInterestLoan increaseInterestLoan = null;
								// 循环还款列表
								for (int i = 0; i < increaseInterestLoans.size(); i++) {
									increaseInterestLoan = increaseInterestLoans.get(i);
									try {
										// 自动还款
										List<Map<String, String>> msgList = increaseInterestRepayService.updateBorrowRepay(apicron, increaseInterestLoan, borrowUserCust);
										// 发送短信
										increaseInterestRepayService.sendSms(msgList);
										// 推送消息
										increaseInterestRepayService.sendMessage(msgList);
									} catch (Exception e) {
										errorCnt++;
										sbError.append(errorCnt).append(".").append(e.getMessage()).append("<br/>");
										LogUtil.errorLog(THIS_CLASS, methodName, e);
									}
								}

								// 还款有错误时
								if (errorCnt > 0) {
									throw new Exception("融通宝加息还款时发生错误。" + "[借款编号：" + borrowNid + "]，" + "[错误件数：" + errorCnt + "]");
								} else {
									// 更新最后还款状态
									this.increaseInterestRepayService.updateBorrowStatus(borrowNid, apicron.getPeriodNow(), borrowUserId);
									// 发送成功短信
									Map<String, String> replaceStrs = new HashMap<String, String>();
									replaceStrs.put("val_title", borrow.getBorrowNid());
									replaceStrs.put("val_time", GetDate.formatTime());
									SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_HUANKUAN_SUCCESS,
											CustomConstants.CHANNEL_TYPE_NORMAL);
									smsProcesser.gather(smsMessage);
								}
							} else {
								_log.info("还款明细件数为0件。[标号：" + borrowNid + "]");
							}
							// 更新任务API状态为完成
							this.increaseInterestRepayService.updateBorrowApicron(apicron.getId(), STATUS_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
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
								increaseInterestRepayService.updateBorrowApicron(apicron.getId(), STATUS_ERROR, sbError.toString());
								// 发送失败短信
								BorrowWithBLOBs borrow = increaseInterestRepayService.selectBorrowInfo(apicron.getBorrowNid());
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
								increaseInterestRepayService.updateBorrowApicron(apicron.getId(), STATUS_WAIT);
							}
							// 取得是否线上
							String online = "生产环境";
							String isOnline = PropUtils.getSystem(CustomConstants.HYJF_ONLINE);
							if (isOnline == null || !isOnline.contains("online")) {
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
								String payUrl = PropUtils.getSystem(CustomConstants.HYJF_EMAIL_REPAY_TEST);
								if(StringUtils.isNotBlank(payUrl)){
									toMail = payUrl.split(",");
								}else{
									_log.error("-------请配置还款邮件发送地址---------");
								}
							} else {
								String payUrl = PropUtils.getSystem(CustomConstants.HYJF_EMAIL_REPAY_ONLINE);
								if(StringUtils.isNotBlank(payUrl)){
									toMail = payUrl.split(",");
								}else{
									_log.error("-------请配置还款邮件发送地址---------");
								}
							}
							MailMessage message = new MailMessage(null, null, "[" + online + "] " + apicron.getBorrowNid() + "-" + apicron.getPeriodNow() + "融通宝加息还款,第" + runCnt + "次还款失败",
									msg.toString(), null, toMail, null, MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
							mailMessageProcesser.gather(message);
							LogUtil.endLog(THIS_CLASS, methodName, "融通宝加息自动还款任务发生错误。[订单号：" + apicron.getBorrowNid() + "]");
						} finally {
							Long endTime = GetDate.getMillis();
							LogUtil.endLog(THIS_CLASS, methodName, "融通宝加息自动还款任务结束。[订单号：" + apicron.getBorrowNid() + "]， 耗时：" + (endTime - startTime) / 1000 + "s");
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
	 * 对需要做还款处理的还款任务进行排序，按还款期数逐条执行
	 * add by cwyang 2018-0810
	 * @param borrowApicron
	 * @return
	 */
	private boolean sortRepay(BorrowApicron borrowApicron) {

		String borrowNid = borrowApicron.getBorrowNid();
		Integer periodNow = borrowApicron.getPeriodNow();
		BorrowApicron apicron = this.increaseInterestRepayService.getRepayPeriodSort(borrowNid);
		if (apicron != null){
			Integer period = apicron.getPeriodNow();
			if (periodNow == period){
				return true;
			}
		}
		return false;
	}
}

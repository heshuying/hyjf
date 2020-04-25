package com.hyjf.batch.borrow.increaseinterestloan;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncreaseinterestLoansTask {
	/** 类名 */
	private static final String THIS_CLASS = IncreaseinterestLoansTask.class.getName();

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

	private Logger _log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IncreaseinterestLoansService increaseinterestLoansService;


	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

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
				List<BorrowApicron> listApicron = increaseinterestLoansService.getBorrowApicronList(STATUS_WAIT, 0);
				if (listApicron != null && listApicron.size() > 0) {
					// 循环进行放款
					for (BorrowApicron apicron : listApicron) {
						int errorCnt = 0;
						Long startTime = GetDate.getMillis();
						// 错误信息
						StringBuffer sbError = new StringBuffer();
						try {
							LogUtil.startLog(THIS_CLASS, methodName, "融通宝加息自动放款任务开始。[订单号：" + apicron.getBorrowNid() + "]");
							// 更新任务API状态为进行中
							increaseinterestLoansService.updateBorrowApicron(apicron.getId(), STATUS_RUNNING);
							// 借款编号
							String borrowNid = apicron.getBorrowNid();
							// 借款人ID
							Integer borrowUserId = apicron.getUserId();
							// 取得出借详情列表
							List<IncreaseInterestInvest> listTender = increaseinterestLoansService.getBorrowTenderList(borrowNid);
							if (listTender != null && listTender.size() > 0) {
								// 取得借款人账户信息
								Account borrowAccount = increaseinterestLoansService.getAccountByUserId(borrowUserId);
								if (borrowAccount == null) {
									throw new Exception("借款人账户不存在。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
								}
								// 借款人在银行的账户信息
								BankOpenAccount borrowUserCust = increaseinterestLoansService.getBankOpenAccount(borrowUserId);
								if (borrowUserCust == null || StringUtils.isEmpty(borrowUserCust.getAccount())) {
									throw new Exception("借款人未开户。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
								}
								// 取得借款详情
								BorrowWithBLOBs borrow = increaseinterestLoansService.getBorrow(borrowNid);
								if (borrow == null) {
									throw new Exception("借款详情不存在。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
								}
								// 出借信息
								IncreaseInterestInvest borrowTender = null;
								// 出借总件数
								int size = listTender.size();
								/** 循环出借详情列表 */
								for (int i = 0; i < size; i++) {
									borrowTender = listTender.get(i);
									try {
										if (Validator.isNull(borrowTender.getLoanOrderId())) {
											// 设置放款订单号
											borrowTender.setLoanOrderId(GetOrderIdUtils.getOrderId2(borrowTender.getUserId()));
											// 设置放款时间
											borrowTender.setOrderDate(GetOrderIdUtils.getOrderDate());
											// 更新放款信息
											increaseinterestLoansService.updateBorrowTender(borrowTender);
										}
										increaseinterestLoansService.updateBorrowLoans(apicron, borrowTender);
									} catch (Exception e) {
										sbError.append(e.getMessage()).append("<br/>");
										LogUtil.errorLog(THIS_CLASS, methodName, e);
										errorCnt++;
									}
								}

								// 有错误时
								if (errorCnt > 0) {
									throw new Exception("融通宝加息放款时发生错误。" + "[借款编号：" + borrowNid + "]," + "[错误件数：" + errorCnt + "]");
								}
							}
							// 更新任务API状态为完成
							increaseinterestLoansService.updateBorrowApicron(apicron.getId(), STATUS_SUCCESS);
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
								increaseinterestLoansService.updateBorrowApicron(apicron.getId(), STATUS_ERROR, sbError.toString());
								LogUtil.endLog(THIS_CLASS, methodName, "融通宝自动放款任务发生错误。[标号：" + apicron.getBorrowNid() + "]");
							} else {
								// 更新任务API状态为重新执行
								increaseinterestLoansService.updateBorrowApicron(apicron.getId(), STATUS_WAIT);
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
							msg.append("放款时间：").append(GetDate.formatTime()).append("<br/>");
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
							MailMessage message = new MailMessage(null, null, "[" + online + "] " + apicron.getBorrowNid() + " 第" + runCnt + "次 融通宝加息放款失败", msg.toString(), null, toMail, null,
									MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
							mailMessageProcesser.gather(message);
						} finally {
							Long endTime = GetDate.getMillis();
							LogUtil.endLog(THIS_CLASS, methodName, "融通宝加息自动放款任务结束。[订单号：" + apicron.getBorrowNid() + "], 耗时：" + (endTime - startTime) / 1000 + "s");
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
		List<BorrowApicron> listApicron = increaseinterestLoansService.getBorrowApicronList(STATUS_ERROR, 0);
		if (listApicron != null && listApicron.size() > 0) {
			for (BorrowApicron apicron : listApicron) {
				// 更新任务API状态为重新执行
				increaseinterestLoansService.updateBorrowApicron(apicron.getId(), STATUS_WAIT);
			}
		}
		// 取得放款失败任务
		List<BorrowApicron> listApicron2 = increaseinterestLoansService.getBorrowApicronListWithRepayStatus(STATUS_ERROR, 1);
		if (listApicron2 != null && listApicron2.size() > 0) {
			for (BorrowApicron apicron : listApicron2) {
				// 更新任务API状态为重新执行
				increaseinterestLoansService.updateBorrowApicronOfRepayStatus(apicron.getId(), STATUS_WAIT);
			}
		}
	}
}

package com.hyjf.batch.borrow.repay.reminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.jpush.api.utils.StringUtils;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;

/**
 * 还款前三天提醒借款人还款短信定时
 * 
 * @author liuyang
 *
 */
public class RepayReminderTask {
	/** 用户ID */
	private static final String VAL_USERID = "userId";
	/** 应还日期 */
	private static final String VAL_DATE = "val_date";
	/** 标号 */
	private static final String VAL_BORROWNID = "val_borrownid";

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	private RepayReminderService repayReminderService;

	public void run() throws Exception {
		// 调用短信提醒方法
		smsReminder();
	}

	/**
	 * 还款前三天提醒借款人还款
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean smsReminder() throws Exception {
		if (isRun == 0) {
			isRun = 1;
			try {
				// 检索正在还款中的标的
				List<BorrowWithBLOBs> borrows = this.repayReminderService.selectRepayBorrowList();
				// 循环还款中的标的
				if (borrows != null && borrows.size() > 0) {
					for (BorrowWithBLOBs borrow : borrows) {
						// 还款前三天短信提醒
						List<Map<String, String>> msgList = new ArrayList<Map<String, String>>();
						// 还款当天短息提醒
						List<Map<String, String>> messages = new ArrayList<Map<String, String>>();
						// 标的编号
						String borrowNid = borrow.getBorrowNid();
						// 借款人用户ID
						Integer borrowUserId = borrow.getUserId();
						// 担保机构用户ID
						Integer repayOrgUserId = StringUtils.isEmpty(String.valueOf(borrow.getRepayOrgUserId())) ? 0 : borrow.getRepayOrgUserId();
						// 还款方式
						String borrowStyle = borrow.getBorrowStyle();
						// 是否月标(true:月标, false:天标)
						boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
								|| CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
								|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
						if (isMonth) {
							// 分期还款,取得还款计划表
							List<BorrowRepayPlan> borrowRepayPlanList = this.repayReminderService.selectBorrowRepayPlan(borrowNid, 0);
							// 还款计划
							if (borrowRepayPlanList != null && borrowRepayPlanList.size() > 0) {
								// 取最近一期未还款信息
								BorrowRepayPlan borrowRepayPlan = borrowRepayPlanList.get(0);
								// 应还时间
								String repayTime = GetDate.getDateMyTimeInMillis(Integer.parseInt(borrowRepayPlan.getRepayTime()));
								String now = GetDate.getDateMyTimeInMillis(GetDate.getMyTimeInMillis());
								int count = GetDate.daysBetween(now, repayTime);
								if (count <= 3) {
									Map<String, String> borrowUserMsg = new HashMap<String, String>();
									borrowUserMsg.put(VAL_USERID, String.valueOf(borrowUserId));
									borrowUserMsg.put(VAL_BORROWNID, borrowNid);
									borrowUserMsg.put(VAL_DATE, repayTime);
									msgList.add(borrowUserMsg);
									if (repayOrgUserId != 0) {
										Map<String, String> repayOrgUserMsg = new HashMap<String, String>();
										repayOrgUserMsg.put(VAL_USERID, String.valueOf(repayOrgUserId));
										repayOrgUserMsg.put(VAL_BORROWNID, borrowNid);
										repayOrgUserMsg.put(VAL_DATE, repayTime);
										msgList.add(repayOrgUserMsg);
									}
									this.repayReminderService.sendSms(msgList, CustomConstants.PARAM_TPL_HUANKUANTIXING);
									// 更新borrowrecoverPlan短信
									boolean isUpdateFlag = this.repayReminderService.updateBorrowRepayPlan(borrowRepayPlan, 1);
									if (!isUpdateFlag) {
										throw new Exception("短信发送后,更新borrow_repay_plan表失败");
									}
								}

							}

							// 还款日当天过了17:00,提醒短信提醒借款人还款
							List<BorrowRepayPlan> borrowRepayPlans = this.repayReminderService.selectBorrowRepayPlan(borrowNid, 1);
							if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
								// 取最近一期未还款信息
								BorrowRepayPlan borrowRepayPlan = borrowRepayPlans.get(0);
								// 应还时间
								// 还款日
								String repayTimeYYYYMMDD = GetDate.getDateMyTimeInMillis(Integer.parseInt(borrowRepayPlan.getRepayTime()));
								// 当前日期
								String nowDate = GetDate.getDate("yyyy-MM-dd");
								if (repayTimeYYYYMMDD.equals(nowDate) && GetDate.getNowTime10() > GetDate.dateString2Timestamp(nowDate + " 17:00:00")) {
									Map<String, String> borrowUserMsg = new HashMap<String, String>();
									borrowUserMsg.put(VAL_USERID, String.valueOf(borrowUserId));
									borrowUserMsg.put(VAL_BORROWNID, borrowNid);
									messages.add(borrowUserMsg);
									if (repayOrgUserId != 0) {
										Map<String, String> repayOrgUserMsg = new HashMap<String, String>();
										repayOrgUserMsg.put(VAL_USERID, String.valueOf(repayOrgUserId));
										repayOrgUserMsg.put(VAL_BORROWNID, borrowNid);
										messages.add(repayOrgUserMsg);
									}

									this.repayReminderService.sendSms(messages, CustomConstants.PARAM_TPL_HUANKUANGUOQI);
									// 更新borrowrecoverPlan短信
									boolean isUpdateFlag = this.repayReminderService.updateBorrowRepayPlan(borrowRepayPlan, 2);
									if (!isUpdateFlag) {
										throw new Exception("短信发送后,更新borrow_repay_plan表失败");
									}
								}
							}

						} else {
							// 不分期的还款数据
							List<BorrowRepay> borrowRepayList = this.repayReminderService.selectBorrowRepayList(borrowNid, 0);
							if (borrowRepayList != null && borrowRepayList.size() > 0) {
								BorrowRepay borrowRecover = borrowRepayList.get(0);
								// 应还时间
								String repayTime = GetDate.getDateMyTimeInMillis(Integer.parseInt(borrowRecover.getRepayTime()));
								String now = GetDate.getDateMyTimeInMillis(GetDate.getMyTimeInMillis());
								int count = GetDate.daysBetween(now, repayTime);
								if (count <= 3) {
									Map<String, String> borrowUserMsg = new HashMap<String, String>();
									borrowUserMsg.put(VAL_USERID, String.valueOf(borrowUserId));
									borrowUserMsg.put(VAL_BORROWNID, borrowNid);
									borrowUserMsg.put(VAL_DATE, repayTime);
									msgList.add(borrowUserMsg);
									if (repayOrgUserId != 0) {
										Map<String, String> repayOrgUserMsg = new HashMap<String, String>();
										repayOrgUserMsg.put(VAL_USERID, String.valueOf(repayOrgUserId));
										repayOrgUserMsg.put(VAL_BORROWNID, borrowNid);
										repayOrgUserMsg.put(VAL_DATE, repayTime);
										msgList.add(repayOrgUserMsg);
									}
									this.repayReminderService.sendSms(msgList, CustomConstants.PARAM_TPL_HUANKUANTIXING);
									// 更新borrowrecover信息
									boolean isUpdateFlag = this.repayReminderService.updateBorrowRepay(borrowRecover, 1);
									if (!isUpdateFlag) {
										throw new Exception("短信发送后,更新borrow_recover表失败");
									}
								}
							}

							// 还款日当天过了17:00,提醒短信提醒借款人还款
							List<BorrowRepay> borrowRepays = this.repayReminderService.selectBorrowRepayList(borrowNid, 1);
							if (borrowRepays != null && borrowRepays.size() > 0) {
								BorrowRepay borrowRepay = borrowRepays.get(0);
								// 应还时间
								// 还款日
								String repayTimeYYYYMMDD = GetDate.getDateMyTimeInMillis(Integer.parseInt(borrowRepay.getRepayTime()));
								// 当前日期
								String nowDate = GetDate.getDate("yyyy-MM-dd");
								if (repayTimeYYYYMMDD.equals(nowDate) && GetDate.getNowTime10() > GetDate.dateString2Timestamp(nowDate + " 17:00:00")) {
									Map<String, String> borrowUserMsg = new HashMap<String, String>();
									borrowUserMsg.put(VAL_USERID, String.valueOf(borrowUserId));
									borrowUserMsg.put(VAL_BORROWNID, borrowNid);
									messages.add(borrowUserMsg);
									if (repayOrgUserId != 0) {
										Map<String, String> repayOrgUserMsg = new HashMap<String, String>();
										repayOrgUserMsg.put(VAL_USERID, String.valueOf(repayOrgUserId));
										repayOrgUserMsg.put(VAL_BORROWNID, borrowNid);
										messages.add(repayOrgUserMsg);
									}
									this.repayReminderService.sendSms(messages, CustomConstants.PARAM_TPL_HUANKUANGUOQI);
									// 更新borrowrecoverPlan短信
									boolean isUpdateFlag = this.repayReminderService.updateBorrowRepay(borrowRepay, 2);
									if (!isUpdateFlag) {
										throw new Exception("短信发送后,更新borrow_recover_plan表失败");
									}
								}
							}
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
}

package com.hyjf.batch.borrow.repay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowApicronExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlanExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample.Criteria;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * 自动扣款(还款服务)
 *
 * @author Administrator
 *
 */
@Service
public class BorrowRepayServiceImpl extends BaseServiceImpl implements BorrowRepayService {

	private static final String THIS_CLASS = BorrowRepayServiceImpl.class.getName();

	/** 用户ID */
	private static final String VAL_USERID = "userId";
	/** 性别 */
	private static final String VAL_SEX = "val_sex";
	/** 用户名 */
	private static final String VAL_NAME = "val_name";
	/** 项目标题 */
	private static final String VAL_TITLE = "val_title";
	/** 放款金额 */
	private static final String VAL_AMOUNT = "val_amount";
	/** 预期收益 */
	private static final String VAL_PROFIT = "val_profit";
	/** 等待 */
	private static final String TYPE_WAIT = "wait";
	/** 完成 */
	private static final String TYPE_YES = "yes";
	/** 部分完成 */
	private static final String TYPE_WAIT_YES = "wait_yes";
	/** 本金 */
	private static final String VAL_CAPITAL = "val_capital";
	/** 收益 */
	private static final String VAL_INTEREST = "val_interest";
	/** 标号 */
	private static final String VAL_BORROWNID = "val_borrownid";

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	/**
	 * 自动还款
	 *
	 * @throws Exception
	 */
	@Override
	public List<Map<String, String>> updateBorrowRepay(BorrowApicron apicron, BorrowRecover borrowRecover, AccountChinapnr repayUserCust) throws Exception {
		String methodName = "updateBorrowRepay";
		System.out.println("-----------还款开始---" + apicron.getBorrowNid() + "---------");

		List<Map<String, String>> retMsgList = new ArrayList<Map<String, String>>();
		Map<String, String> msg = new HashMap<String, String>();
		retMsgList.add(msg);
		/** 基本变量 */
		// 还款订单号
		String repayOrderId = null;
		// 还款订单日期
		String repayOrderDate = null;
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 借款编号
		String borrowNid = apicron.getBorrowNid();
		// 还款人ID(借款人或代付机构)
		Integer repayUserid = apicron.getUserId();
		// 还款人用户名
		String repayUserName = null;
		// 根据还款人用户ID检索还款人信息
		UsersExample usersExample = new UsersExample();
		UsersExample.Criteria usersCra = usersExample.createCriteria();
		usersCra.andUserIdEqualTo(repayUserid);
		List<Users> repayUserList = this.usersMapper.selectByExample(usersExample);
		if (repayUserList == null || repayUserList.size() <= 0) {
			throw new Exception("查询还款人信息失败,还款人用户ID:" + repayUserid);
		}
		repayUserName = repayUserList.get(0).getUsername();
		// 当前期数
		Integer periodNow = apicron.getPeriodNow();
		/** 标的基本数据 */
		// 取得借款详情
		BorrowWithBLOBs borrow = getBorrow(borrowNid);
		// 标的是否进行银行托管
		int bankInputFlag = Validator.isNull(borrow.getBankInputFlag()) ? 0 : borrow.getBankInputFlag();
		// 标的是否可以担保机构还款
		int isRepayOrgFlag = Validator.isNull(borrow.getIsRepayOrgFlag()) ? 0 : borrow.getIsRepayOrgFlag();
		// 是否是担保机构还款
		int isApicronRepayOrgFlag = Validator.isNull(apicron.getIsRepayOrgFlag()) ? 0 : apicron.getIsRepayOrgFlag();
		// 标的借款人ID
		Integer borrowUserId = borrow.getUserId();
		// 取得还款详情
		BorrowRepay borrowRepay = getBorrowRepay(borrowNid);
		// 还款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 剩余还款期数
		Integer periodNext = borrowPeriod - periodNow;
		// 出借订单号
		String tenderOrdId = null;
		// 出借人用户ID
		Integer tenderUserId = null;
		// 总收入金额
		BigDecimal recoverAccountAll = BigDecimal.ZERO;
		// 还款总额
		BigDecimal recoverAccount = BigDecimal.ZERO;
		// 还款本金
		BigDecimal recoverCapital = BigDecimal.ZERO;
		// 还款利息
		BigDecimal recoverInterest = BigDecimal.ZERO;
		// 还款总额(实际)
		BigDecimal recoverAccountYes = BigDecimal.ZERO;
		// 还款本金(实际)
		BigDecimal recoverCapitalYes = BigDecimal.ZERO;
		// 还款利息(实际)
		BigDecimal recoverInterestYes = BigDecimal.ZERO;
		// 延期天数
		Integer lateDays = 0;
		// 逾期利息
		BigDecimal lateInterest = BigDecimal.ZERO;
		// 延期天数
		Integer delayDays = 0;
		// 延期利息
		BigDecimal delayInterest = BigDecimal.ZERO;
		// 提前天数
		Integer chargeDays = 0;
		// 提前还款少还利息
		BigDecimal chargeInterest = BigDecimal.ZERO;
		// 还款总利息
		BigDecimal recoverInterestAll = BigDecimal.ZERO;

		// 管理费
		BigDecimal recoverFee = BigDecimal.ZERO;
		// 出借ID
		Integer tenderId = null;
		// 还款时间
		String recoverTime = null;
		// 出借信息
		BorrowTender borrowTender = null;
		// 出借人在汇付的账户信息
		AccountChinapnr tenderUserCust = null;
		// 出借人客户号
		Long tenderUserCustId = null;
		// 还款人(借款人或垫付机构)客户号
		Long repayUserCustId = null;
		// 标的借款人客户号
		Long borrowUserCustId = null;
		
		// 出借订单号
		tenderOrdId = borrowRecover.getNid();
		// 出借人用户ID
		tenderUserId = borrowRecover.getUserId();
		// 出借ID
		tenderId = borrowRecover.getTenderId();
		// 取得出借信息
		borrowTender = getBorrowTender(tenderId);
		// 出借人在汇付的账户信息
		tenderUserCust = getChinapnrUserInfo(tenderUserId);
		if (tenderUserCust == null) {
			throw new RuntimeException("出借人未开户。[用户ID：" + tenderUserId + "]，" + "[出借订单号：" + tenderOrdId + "]");
		}
		// 出借人客户号
		tenderUserCustId = tenderUserCust.getChinapnrUsrcustid();
		// 还款人(借款人或垫付机构)客户号
		repayUserCustId = repayUserCust.getChinapnrUsrcustid();
		// 借款人在汇付的账户信息
		AccountChinapnr borrowUserCust = this.getChinapnrUserInfo(borrowUserId);
		if (borrowUserCust == null) {
			throw new Exception("借款人未开户。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
		}
		// 标的借款人客户号
		borrowUserCustId = borrowUserCust.getChinapnrUsrcustid();
		// 分期还款计划表
		BorrowRecoverPlan borrowRecoverPlan = null;
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		// [principal: 等额本金, month:等额本息, month:等额本息, endmonth:先息后本]
		if (isMonth) {
			// 取得分期还款计划表
			borrowRecoverPlan = getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId, borrowRecover.getTenderId());
			if (borrowRecoverPlan != null) {
				// 还款订单号
				repayOrderId = borrowRecoverPlan.getRepayOrderId();
				// 还款订单日期
				repayOrderDate = borrowRecoverPlan.getRepayOrderDate();
				// 还款时间
				recoverTime = borrowRecoverPlan.getRecoverTime();
				// 还款金额
				recoverAccount = borrowRecoverPlan.getRecoverAccount();
				// 还款本金
				recoverCapital = borrowRecoverPlan.getRecoverCapital();
				// 还款利息
				recoverInterest = borrowRecoverPlan.getRecoverInterest();
				// 逾期天数
				lateDays = borrowRecoverPlan.getLateDays();
				// 逾期利息
				lateInterest = borrowRecoverPlan.getLateInterest();
				// 延期天数
				delayDays = borrowRecoverPlan.getDelayDays();
				// 延期利息
				delayInterest = borrowRecoverPlan.getDelayInterest();
				// 提前天数
				chargeDays = borrowRecoverPlan.getChargeDays();
				// 提前还款少还利息
				chargeInterest = borrowRecoverPlan.getChargeInterest();
				// 管理费
				recoverFee = borrowRecoverPlan.getRecoverFee();
				// 还款金额(实际)
				recoverAccountYes = borrowRecoverPlan.getRecoverAccount().add(lateInterest).add(delayInterest).add(chargeInterest);
				// 还款本金(实际)
				recoverCapitalYes = borrowRecoverPlan.getRecoverCapital();
				// 还款利息(实际)
				recoverInterestYes = borrowRecoverPlan.getRecoverInterest().add(lateInterest).add(delayInterest).add(chargeInterest);
				// 总收入金额=还款金额+逾期利息+延期利息+提前还款少还利息(负数)
				recoverAccountAll = recoverAccount.add(lateInterest).add(delayInterest).add(chargeInterest);
				// 总还款利息=应还利息+逾期利息+延期利息+提前还款少还利息(负数)
				recoverInterestAll = recoverInterest.add(lateInterest).add(delayInterest).add(chargeInterest);
			} else {
				throw new RuntimeException("分期还款计划表数据不存在。[借款编号：" + borrowNid + "]，" + "[出借订单号：" + tenderOrdId + "]，" + "[期数：" + periodNow + "]");
			}
		}
		// [endday: 按天计息, end:按月计息]
		else {

			// 还款订单号
			repayOrderId = borrowRecover.getRepayOrdid();
			// 还款订单日期
			repayOrderDate = borrowRecover.getRepayOrddate();
			// 还款时间
			recoverTime = borrowRecover.getRecoverTime();
			// 还款金额
			recoverAccount = borrowRecover.getRecoverAccount();
			// 还款本金
			recoverCapital = borrowRecover.getRecoverCapital();
			// 还款利息
			recoverInterest = borrowRecover.getRecoverInterest();
			// 逾期天数
			lateDays = borrowRecover.getLateDays();
			// 逾期利息
			lateInterest = borrowRecover.getLateInterest();
			// 延期天数
			delayDays = borrowRecover.getDelayDays();
			// 延期利息
			delayInterest = borrowRecover.getDelayInterest();
			// 提前天数
			chargeDays = borrowRecover.getChargeDays();
			// 提前还款少还利息
			chargeInterest = borrowRecover.getChargeInterest();
			// 管理费
			recoverFee = borrowRecover.getRecoverFee();
			// 还款金额(实际)
			recoverAccountYes = borrowRecover.getRecoverAccount().add(lateInterest).add(delayInterest).add(chargeInterest);
			// 还款本金(实际)
			recoverCapitalYes = borrowRecover.getRecoverCapital();
			// 还款利息(实际)
			recoverInterestYes = borrowRecover.getRecoverInterest().add(lateInterest).add(delayInterest).add(chargeInterest);
			// 总收入金额=还款金额+逾期利息+延期利息+提前还款少还利息(负数)
			recoverAccountAll = recoverAccount.add(lateInterest).add(delayInterest).add(chargeInterest);
			// 总还款利息=应还利息+逾期利息+延期利息+提前还款少还利息(负数)
			recoverInterestAll = recoverInterest.add(lateInterest).add(delayInterest).add(chargeInterest);
		}
		// 判断该收支明细是否存在时,跳出本次循环
		if (countAccountListByNid(repayOrderId) > 0) {
			return retMsgList;
		}
		// 调用交易查询接口
		ChinapnrBean queryTransStatBean = queryTransStat(repayOrderId, repayOrderDate, "REPAYMENT");
		String respCode = queryTransStatBean == null ? "" : queryTransStatBean.getRespCode();
		// 调用接口失败时(000,422以外)
		if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode) && !ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(respCode)) {
			String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
			LogUtil.errorLog(THIS_CLASS, methodName, "调用交易查询接口失败。" + message + "，[出借订单号：" + tenderOrdId + "]", null);
			throw new RuntimeException("调用交易查询接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
		}
		// 汇付交易状态
		String transStat = queryTransStatBean.getTransStat();
		// I:初始 P:部分成功
		if (ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(respCode) || (!"I".equals(transStat) && !"P".equals(transStat))) {
			if (bankInputFlag == 1 || isRepayOrgFlag == 1 ) {
				// 分账账户串（当 管理费！=0 时是必填项）
				String divDetails = "";
				if (recoverFee.compareTo(BigDecimal.ZERO) > 0) {
					JSONArray ja = new JSONArray();
					JSONObject jo = new JSONObject();
					// 分账商户号(商户号,从配置文件中取得)
					jo.put(ChinaPnrConstant.PARAM_DIVCUSTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
					// 分账账户号(子账户号,从配置文件中取得)
					jo.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT03));
					// 分账金额
					jo.put(ChinaPnrConstant.PARAM_DIVAMT, recoverFee.toString());
					ja.add(jo);
					divDetails = ja.toString();
				}
				// 入参扩展域(2.0用)
				String reqExts = "";
				// 调用汇付接口
				if (recoverCapital.add(recoverInterestAll).compareTo(BigDecimal.ZERO) > 0) {
					ChinapnrBean repaymentBean = repayment(borrowNid, repayUserid, String.valueOf(repayUserCustId), recoverCapital.toString(), recoverInterestAll.toString(), recoverFee.toString(), repayOrderId, repayOrderDate, tenderOrdId, GetOrderIdUtils.getOrderDate(borrowTender.getAddtime()),
							String.valueOf(tenderUserCustId), divDetails, reqExts,bankInputFlag,isRepayOrgFlag,isApicronRepayOrgFlag,String.valueOf(borrowUserCustId));
					respCode = repaymentBean == null ? "" : repaymentBean.getRespCode();
					// 调用接口失败时(000以外)

					if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
						String message = repaymentBean == null ? "" : repaymentBean.getRespDesc();
						LogUtil.errorLog(THIS_CLASS, methodName, "调用自动扣款（还款）接口失败。" + message + "，[出借订单号：" + tenderOrdId + "]", null);
						throw new RuntimeException("调用自动扣款（还款）接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
					}
				}
			} else {
				// 分账账户串（当 管理费！=0 时是必填项）
				String divDetails = "";
				if (recoverFee.compareTo(BigDecimal.ZERO) > 0) {
					JSONArray ja = new JSONArray();
					JSONObject jo = new JSONObject();
					// 分账账户号(子账户号,从配置文件中取得)
					jo.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT03));
					// 分账金额
					jo.put(ChinaPnrConstant.PARAM_DIVAMT, recoverFee.toString());
					ja.add(jo);
					divDetails = ja.toString();
				}
				// 入参扩展域(2.0用)
				String reqExts = "";
				// 调用汇付接口
				if (recoverAccountAll.compareTo(BigDecimal.ZERO) > 0) {
					ChinapnrBean repaymentBean = repaymentOld(repayUserid, String.valueOf(repayUserCustId), recoverAccountAll.toString(), recoverFee.toString(), repayOrderId, repayOrderDate, tenderOrdId, GetOrderIdUtils.getOrderDate(borrowTender.getAddtime()), String.valueOf(tenderUserCustId),
							divDetails, reqExts);
					respCode = repaymentBean == null ? "" : repaymentBean.getRespCode();
					// 调用接口失败时(000以外)
					if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
						String message = repaymentBean == null ? "" : repaymentBean.getRespDesc();
						LogUtil.errorLog(THIS_CLASS, methodName, "调用自动扣款（还款）接口失败。" + message + "，[出借订单号：" + tenderOrdId + "]", null);
						throw new RuntimeException("调用自动扣款（还款）接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
					}
				}
			}
		}
		// 判断该收支明细是否存在时,跳出本次循环
		if (countAccountListByNid(repayOrderId) == 0) {
			// 更新账户信息(出借人)
			Account account = new Account();
			account.setUserId(tenderUserId);
			// 出借人资金总额
			account.setTotal(lateInterest.add(delayInterest).add(chargeInterest));
			// 出借人可用余额
			account.setBalance(recoverAccountAll);
			// 出借人待收金额
			account.setAwait(recoverAccount);
			boolean investAccountFlag = this.adminAccountCustomizeMapper.updateOfRepayTender(account) > 0 ? true : false;
			if (investAccountFlag) {
				// 取得账户信息(出借人)
				account = this.getAccountByUserId(borrowTender.getUserId());
				if (account != null) {
					// 写入收支明细
					AccountList accountList = new AccountList();
					accountList.setNid(repayOrderId); // 还款订单号
					accountList.setUserId(tenderUserId); // 出借人
					accountList.setAmount(recoverAccountAll); // 出借总收入
					accountList.setType(1); // 1收入
					accountList.setTrade("tender_recover_yes"); // 出借成功
					accountList.setTradeCode("balance"); // 余额操作
					accountList.setTotal(account.getTotal()); // 出借人资金总额
					accountList.setBalance(account.getBalance()); // 出借人可用金额
					accountList.setPlanFrost(account.getPlanFrost());// 汇添金冻结金额
					accountList.setPlanBalance(account.getPlanBalance());// 汇添金可用金额
					accountList.setFrost(account.getFrost()); // 出借人冻结金额
					accountList.setAwait(account.getAwait()); // 出借人待收金额
					// accountList.setRemark("出借还款"); // 出借还款
					accountList.setCreateTime(nowTime); // 创建时间
					accountList.setBaseUpdate(nowTime); // 更新时间
					accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY); // 操作者
					accountList.setRemark(borrowNid);
					accountList.setIp(borrow.getAddip()); // 操作IP
					accountList.setIsUpdate(0);
					accountList.setBaseUpdate(0);
					accountList.setInterest(BigDecimal.ZERO); // 利息
					accountList.setWeb(0); // PC
					boolean investAccountListFlag = insertAccountList(accountList) > 0 ? true : false;
					if (investAccountListFlag) {
						// 更新还款明细表
						// 分期并且不是最后一期
						if (borrowRecoverPlan != null && Validator.isNotNull(periodNext) && periodNext > 0) {
							borrowRecover.setRecoverStatus(0); // 未还款
							// 取得分期还款计划表下一期的还款
							BorrowRecoverPlan borrowRecoverPlanNext = getBorrowRecoverPlan(borrowNid, periodNow + 1, tenderUserId, borrowRecover.getTenderId());
							borrowRecover.setRecoverTime(borrowRecoverPlanNext.getRecoverTime()); // 计算下期时间
							borrowRecover.setRecoverType(TYPE_WAIT);
						} else {
							borrowRecover.setRecoverStatus(1); // 已还款
							borrowRecover.setRecoverYestime(String.valueOf(nowTime)); // 实际还款时间
							borrowRecover.setRecoverTime(recoverTime);
							borrowRecover.setRecoverType(TYPE_YES);
						}
						// 分期时
						if (borrowRecoverPlan != null) {
							borrowRecover.setRecoverPeriod(periodNext);
						}
						borrowRecover.setRecoverAccountYes(borrowRecover.getRecoverAccountYes().add(recoverAccountYes));
						borrowRecover.setRecoverInterestYes(borrowRecover.getRecoverInterestYes().add(recoverInterestYes));
						borrowRecover.setRecoverCapitalYes(borrowRecover.getRecoverCapitalYes().add(recoverCapitalYes));
						borrowRecover.setRecoverAccountWait(borrowRecover.getRecoverAccountWait().subtract(recoverAccount));
						borrowRecover.setRecoverInterestWait(borrowRecover.getRecoverInterestWait().subtract(recoverInterest));
						borrowRecover.setRecoverCapitalWait(borrowRecover.getRecoverCapitalWait().subtract(recoverCapital));
						borrowRecover.setWeb(2); // 写入网站收支
						boolean borrowRecoverFlag = this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover) > 0 ? true : false;
						if (borrowRecoverFlag) {
							// 更新总的还款明细
							borrowRepay.setRepayAccountAll(borrowRepay.getRepayAccountAll().add(recoverAccountYes).add(recoverFee));
							borrowRepay.setRepayAccountYes(borrowRepay.getRepayAccountYes().add(recoverAccountYes));
							borrowRepay.setRepayInterestYes(borrowRepay.getRepayInterestYes().add(recoverInterestYes));
							borrowRepay.setRepayCapitalYes(borrowRepay.getRepayCapitalYes().add(recoverCapitalYes));
							borrowRepay.setLateDays(lateDays);
							borrowRepay.setLateInterest(borrowRepay.getLateInterest().add(lateInterest));
							borrowRepay.setDelayDays(delayDays);
							borrowRepay.setDelayInterest(borrowRepay.getDelayInterest().add(delayInterest));
							borrowRepay.setChargeDays(chargeDays);
							borrowRepay.setChargeInterest(borrowRepay.getChargeInterest().add(chargeInterest));
							// 用户是否提前还款
							borrowRepay.setAdvanceStatus(borrowRecover.getAdvanceStatus());
							// 还款来源
							if(isRepayOrgFlag == 1 && isApicronRepayOrgFlag == 1){
								// 还款来源（1、借款人还款，2、机构垫付，3、保证金垫付）
								borrowRepay.setRepayMoneySource(2);
							} else {
								borrowRepay.setRepayMoneySource(1);
							}
							// 实际还款人（借款人、垫付机构、保证金）的用户ID
							borrowRepay.setRepayUserId(repayUserid);
							// 实际还款人（借款人、垫付机构、保证金）的用户名
							borrowRepay.setRepayUsername(repayUserName);
							boolean borrowRepayFlag = this.borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay) > 0 ? true : false;
							if (borrowRepayFlag) {
								// 更新借款表
								borrow = getBorrow(borrowNid);
								BorrowWithBLOBs newBrrow = new BorrowWithBLOBs();
								newBrrow.setId(borrow.getId());
								BigDecimal borrowManager = borrow.getBorrowManager() == null ? BigDecimal.ZERO : new BigDecimal(borrow.getBorrowManager());
								newBrrow.setBorrowManager(borrowManager.add(recoverFee).toString());
								newBrrow.setRepayAccountYes(borrow.getRepayAccountYes().add(recoverAccountYes)); // 总还款利息
								newBrrow.setRepayAccountInterestYes(borrow.getRepayAccountInterestYes().add(recoverInterestYes)); // 总还款利息
								newBrrow.setRepayAccountCapitalYes(borrow.getRepayAccountCapitalYes().add(recoverCapitalYes)); // 总还款本金
								newBrrow.setRepayAccountWait(borrow.getRepayAccountWait().subtract(recoverAccount)); // 未还款总额
								newBrrow.setRepayAccountInterestWait(borrow.getRepayAccountInterestWait().subtract(recoverInterest)); // 未还款利息
								newBrrow.setRepayAccountCapitalWait(borrow.getRepayAccountCapitalWait().subtract(recoverCapital)); // 未还款本金
								newBrrow.setRepayFeeNormal(borrow.getRepayFeeNormal().add(recoverFee));
								boolean borrowFlag = this.borrowMapper.updateByPrimaryKeySelective(newBrrow) > 0 ? true : false;
								if (borrowFlag) {
									// 更新出借表
									borrowTender.setRecoverAccountYes(borrowTender.getRecoverAccountYes().add(recoverAccountYes));
									borrowTender.setRecoverAccountInterestYes(borrowTender.getRecoverAccountInterestYes().add(recoverInterestYes));
									borrowTender.setRecoverAccountCapitalYes(borrowTender.getRecoverAccountCapitalYes().add(recoverCapitalYes));
									borrowTender.setRecoverAccountWait(borrowTender.getRecoverAccountWait().subtract(recoverAccount));
									borrowTender.setRecoverAccountInterestWait(borrowTender.getRecoverAccountInterestWait().subtract(recoverInterest));
									borrowTender.setRecoverAccountCapitalWait(borrowTender.getRecoverAccountCapitalWait().subtract(recoverCapital));
									boolean borrowTenderFlag = borrowTenderMapper.updateByPrimaryKeySelective(borrowTender) > 0 ? true : false;
									if (borrowTenderFlag) {
										// 分期时
										if (borrowRecoverPlan != null) {
											// 更新还款计划表
											borrowRecoverPlan.setRecoverStatus(1);
											borrowRecoverPlan.setRecoverYestime(String.valueOf(nowTime));
											borrowRecoverPlan.setRecoverAccountYes(recoverAccountYes);
											borrowRecoverPlan.setRecoverInterestYes(recoverInterestYes);
											borrowRecoverPlan.setRecoverCapitalYes(recoverCapitalYes);
											borrowRecoverPlan.setRecoverAccountWait(BigDecimal.ZERO);
											borrowRecoverPlan.setRecoverCapitalWait(BigDecimal.ZERO);
											borrowRecoverPlan.setRecoverInterestWait(BigDecimal.ZERO);
											borrowRecoverPlan.setRecoverType(TYPE_YES);
											boolean borrowRecoverPlanFlag = this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(borrowRecoverPlan) > 0 ? true : false;
											if (borrowRecoverPlanFlag) {
												// 更新总的还款计划
												BorrowRepayPlan borrowRepayPlan = getBorrowRepayPlan(borrowNid, periodNow);
												if (borrowRepayPlan != null) {
													borrowRepayPlan.setRepayType(TYPE_WAIT_YES);
													borrowRepayPlan.setRepayDays("0");
													borrowRepayPlan.setRepayStep(4);
													borrowRepayPlan.setRepayActionTime(String.valueOf(nowTime));
													borrowRepayPlan.setRepayStatus(1);
													borrowRepayPlan.setRepayYestime(String.valueOf(nowTime));
													borrowRepayPlan.setRepayAccountAll(borrowRepayPlan.getRepayAccountAll().add(recoverAccountYes).add(recoverFee));
													borrowRepayPlan.setRepayAccountYes(borrowRepayPlan.getRepayAccountYes().add(recoverAccountYes));
													borrowRepayPlan.setRepayInterestYes(borrowRepayPlan.getRepayInterestYes().add(recoverInterestYes));
													borrowRepayPlan.setRepayCapitalYes(borrowRepayPlan.getRepayCapitalYes().add(recoverCapitalYes));
													borrowRepayPlan.setLateDays(lateDays);
													borrowRepayPlan.setLateInterest(borrowRepayPlan.getLateInterest().add(lateInterest));
													borrowRepayPlan.setDelayDays(delayDays);
													borrowRepayPlan.setDelayInterest(borrowRepayPlan.getDelayInterest().add(delayInterest));
													borrowRepayPlan.setChargeDays(chargeDays);
													borrowRepayPlan.setChargeInterest(borrowRepayPlan.getChargeInterest().add(chargeInterest));
													// 用户是否提前还款
													borrowRepayPlan.setAdvanceStatus(borrowRecoverPlan.getAdvanceStatus());
													// 还款来源
													if(isRepayOrgFlag == 1 && isApicronRepayOrgFlag == 1){
														// 还款来源（1、借款人还款，2、机构垫付，3、保证金垫付）
														borrowRepayPlan.setRepayMoneySource(2);
													} else {
														borrowRepayPlan.setRepayMoneySource(1);
													}
													// 实际还款人（借款人、垫付机构、保证金）的用户ID
													borrowRepayPlan.setRepayUserId(repayUserid);
													// 实际还款人（借款人、垫付机构、保证金）的用户名
													borrowRepayPlan.setRepayUsername(repayUserName);
													boolean borrowRepayPlanFlag = this.borrowRepayPlanMapper.updateByPrimaryKeySelective(borrowRepayPlan) > 0 ? true : false;
													if (!borrowRepayPlanFlag){
														throw new RuntimeException("还款分期计划表(huiyingdai_borrow_repay_plan)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
													}
												} else {
													throw new RuntimeException("还款分期计划表(huiyingdai_borrow_repay_plan)查询失败！" + "[出借订单号：" + tenderOrdId + "]");
												}
											} else {
												throw new RuntimeException("还款分期计划表(huiyingdai_borrow_recover_plan)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
											}
										}
										
										// 如果是机构垫付还款
										if(isRepayOrgFlag == 1 && isApicronRepayOrgFlag ==1){
											// 更新借款人的Account表的待还款金额
											// 取得账户信息(借款人)
											Account borrowUserAccount = this.getAccountByUserId(borrowUserId);
											Account newBorrowUserAccount = new Account();
											newBorrowUserAccount.setUserId(borrowUserAccount.getUserId());
											newBorrowUserAccount.setRepay(isMonth ? borrowRecoverPlan.getRecoverAccount():borrowRecover.getRecoverAccount());
											boolean borrowUserFlag = this.adminAccountCustomizeMapper.updateOfRepayBorrowUser(newBorrowUserAccount) > 0 ? true : false ;
											if(!borrowUserFlag){
												throw new RuntimeException("借款人账户(huiyingdai_account)更新失败！" + "[借款人用户ID：" + borrowUserId + "]");
											}
										}
										// 管理费大于0时,插入网站收支明细
										if (recoverFee.compareTo(BigDecimal.ZERO) > 0) {
											// 插入网站收支明细记录
											AccountWebList accountWebList = new AccountWebList();
											accountWebList.setOrdid(borrowTender.getNid() + "_" + periodNow);// 订单号
											accountWebList.setBorrowNid(borrowNid); // 出借编号
											accountWebList.setUserId(repayUserid); // 借款人
											accountWebList.setAmount(recoverFee); // 管理费
											accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
											accountWebList.setTrade(CustomConstants.TRADE_REPAYFEE); // 管理费
											accountWebList.setTradeType(CustomConstants.TRADE_REPAYFEE_NM); // 账户管理费
											accountWebList.setRemark(borrowNid); // 出借编号
											accountWebList.setCreateTime(nowTime);
											int accountWebListCnt = insertAccountWebList(accountWebList);
											if (accountWebListCnt == 0) {
												throw new RuntimeException("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号：" + borrowTender.getNid() + "]");
											}
										}
										msg.put(VAL_USERID, String.valueOf(borrowRecover.getUserId()));
										msg.put(VAL_AMOUNT, recoverAccountAll == null ? "0.00" : recoverAccountAll.toString());
										msg.put(VAL_PROFIT, recoverInterestAll == null ? "0.00" : recoverInterestAll.toString());
										msg.put(VAL_TITLE, borrowRecover.getBorrowNid());

										msg.put(VAL_CAPITAL, recoverCapital.toString());// 本金
										msg.put(VAL_INTEREST, recoverInterestAll.toString());// 收益
										msg.put(VAL_BORROWNID, borrowNid);// 标号
									} else {
										throw new RuntimeException("出借表(huiyingdai_borrow_tender)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
									}
								} else {
									throw new RuntimeException("借款详情(huiyingdai_borrow)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
								}
							} else {
								throw new RuntimeException("总的还款明细表(huiyingdai_borrow_repay)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
							}
						} else {
							throw new RuntimeException("还款明细(huiyingdai_borrow_recover)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
						}
					} else {
						throw new RuntimeException("收支明细(huiyingdai_account_list)写入失败！" + "[出借订单号：" + tenderOrdId + "]");
					}
				} else {
					throw new RuntimeException("出借人账户信息不存在。[出借人ID：" + borrowTender.getUserId() + "]，" + "[出借订单号：" + tenderOrdId + "]");
				}
			} else {
				throw new RuntimeException("出借人资金记录(huiyingdai_account)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
			}
		}
		System.out.println("-----------还款结束---" + apicron.getBorrowNid() + "---------" + repayOrderId);
		return retMsgList;
	}

	/**
	 * 更新还款完成状态
	 *
	 * @param borrowNid
	 * @param periodNow
	 */
	@Override
	public void updateBorrowStatus(String borrowNid, Integer periodNow, Integer borrowUserId) {

		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 标的项目详情
		Borrow borrow = getBorrow(borrowNid);
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle());
		// 查询未债转的数据
		BorrowRecoverExample recoverExample = new BorrowRecoverExample();
		recoverExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRecoverStatusEqualTo(0).andCreditTimeEqualTo(0);
		int recoverCnt = this.borrowRecoverMapper.countByExample(recoverExample);
		// 查询是否有债转数据
		recoverExample = new BorrowRecoverExample();
		recoverExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRecoverStatusEqualTo(0);
		int recoverCnt2 = this.borrowRecoverMapper.countByExample(recoverExample);
		// 项目总表更新数据
		BorrowWithBLOBs newBrrow = new BorrowWithBLOBs();
		newBrrow.setRepayTimes(borrow.getRepayTimes() + 1); // 还款次数
		// 如果有债转数据未还款,不更新为已还款
		String repayType = TYPE_WAIT;
		int repayStatus = 0;
		if (recoverCnt2 == 0) {
			repayType = TYPE_WAIT_YES;
			repayStatus = 1;
		}
		// 借款人还款表更新
		BorrowRepay newBorrowRepay = new BorrowRepay();
		newBorrowRepay.setRepayDays("0");
		newBorrowRepay.setRepayStep(4);
		newBorrowRepay.setRepayActionTime(String.valueOf(nowTime));
		if (recoverCnt == 0) {
			newBorrowRepay.setRepayType(repayType);
			newBorrowRepay.setRepayStatus(repayStatus); // 已还款
			newBorrowRepay.setRepayYestime(String.valueOf(GetDate.getNowTime10())); // 实际还款时间
			newBrrow.setRepayFullStatus(repayStatus);
		} else {
			newBorrowRepay.setRepayType(repayType);
			newBorrowRepay.setRepayStatus(repayStatus);// 未还款
			if (isMonth) {
				// 分期的场合，根据借款编号和还款期数从还款计划表中取得还款时间
				BorrowRepayPlanExample example = new BorrowRepayPlanExample();
				Criteria borrowCriteria = example.createCriteria();
				borrowCriteria.andBorrowNidEqualTo(borrowNid);
				borrowCriteria.andRepayPeriodEqualTo(periodNow + 1);
				List<BorrowRepayPlan> replayPlan = borrowRepayPlanMapper.selectByExample(example);
				if (replayPlan.size() > 0) {
					BorrowRepayPlan borrowRepayPlan = replayPlan.get(0);
					// 设置下期还款时间
					newBorrowRepay.setRepayTime(borrowRepayPlan.getRepayTime());
					newBrrow.setRepayNextTime(Integer.valueOf(newBorrowRepay.getRepayTime())); // 下期还款时间
				}
			}
		}
		// 更新BorrowRepay
		BorrowRepayExample repayExample = new BorrowRepayExample();
		repayExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayStatusEqualTo(0);
		this.borrowRepayMapper.updateByExampleSelective(newBorrowRepay, repayExample);
		// 更新Borrow
		BorrowExample borrowExample = new BorrowExample();
		borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
		this.borrowMapper.updateByExampleSelective(newBrrow, borrowExample);
	}

	/**
	 * 自动扣款（还款）(调用汇付天下接口)
	 *
	 * @return
	 */
	private ChinapnrBean repayment(String borrowNid, Integer borrowUserId, String outCustId, String recoverCapital, String recoverInterestAll, String fee, String ordId, String ordDate, String subOrdId, String subOrdDate, String inCustId, String divDetails, String reqExt,Integer bankInputFlag ,Integer isRepayOrgFlag,Integer isApicronRepayOrgFlag ,String borrowUserCustId) {

		String methodName = "repayment";
		// 调用汇付接口(自动扣款（还款）)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_30); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_REPAYMENT); // 消息类型(必须)
		if (bankInputFlag == 1) {
			bean.setProId(borrowNid);// 标的id
		}
		bean.setOrdId(ordId); // 订单号(必须)
		bean.setOrdDate(ordDate); // 订单日期(必须)
		bean.setOutCustId(outCustId); // 出账客户号(必须)
		bean.setOutAcctId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCTID));// 出账子账户
		bean.setSubOrdId(subOrdId); // 订单号(必须)
		bean.setSubOrdDate(subOrdDate); // 订单日期(必须)
		bean.setPrincipalAmt(recoverCapital);// 本次还款本金
		bean.setInterestAmt(recoverInterestAll);// 还款利息
		bean.setFee(CustomUtil.formatAmount(fee)); // 扣款手续费(必须)
		bean.setFeeObjFlag("O");// 向借款人收取手续费
		bean.setInCustId(inCustId); // 入账客户号(必须)
		bean.setDivDetails(divDetails); // 分账账户串(必须)
		bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)
		// 垫付机构还款
		if(isRepayOrgFlag == 1 && isApicronRepayOrgFlag == 1){
			bean.setDzObject(borrowUserCustId);// 垫资/代偿对象:标的借款人的客户号
		}
		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("自动扣款（还款）"); // 备注
		bean.setLogClient("0"); // PC
		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean == null) {
			LogUtil.errorLog(THIS_CLASS, methodName, new Exception("调用自动扣款（还款）接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}
		return chinapnrBean;
	}

	/**
	 * 自动扣款（还款）(调用汇付天下接口)
	 *
	 * @return
	 */
	private ChinapnrBean repaymentOld(Integer borrowUserId, String outCustId, String recoverAccount, String fee, String ordId, String ordDate, String subOrdId, String subOrdDate, String inCustId, String divDetails, String reqExt) {

		String methodName = "repaymentOld";
		// 调用汇付接口(自动扣款（还款）)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_REPAYMENT); // 消息类型(必须)
		bean.setOrdId(ordId); // 订单号(必须)
		bean.setOrdDate(ordDate); // 订单日期(必须)
		bean.setOutCustId(outCustId); // 出账客户号(必须)
		bean.setSubOrdId(subOrdId); // 订单号(必须)
		bean.setSubOrdDate(subOrdDate); // 订单日期(必须)
		bean.setTransAmt(CustomUtil.formatAmount(recoverAccount)); // 交易金额(必须)
		bean.setFee(CustomUtil.formatAmount(fee)); // 扣款手续费(必须)
		bean.setInCustId(inCustId); // 入账客户号(必须)
		bean.setDivDetails(divDetails); // 分账账户串(必须)
		bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)

		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("自动扣款（还款）"); // 备注
		bean.setLogClient("0"); // PC
		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean == null) {
			LogUtil.errorLog(THIS_CLASS, methodName, new Exception("调用自动扣款（还款）接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}
		return chinapnrBean;
	}

	/**
	 * 交易状态查询(调用汇付天下接口)
	 *
	 * @return
	 */
	private ChinapnrBean queryTransStat(String ordId, String ordDate, String queryTransType) {

		String methodName = "queryTransStat";
		// 调用汇付接口(交易状态查询)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERYTRANSSTAT); // 消息类型(必须)
		bean.setOrdId(ordId); // 订单号(必须)
		bean.setOrdDate(ordDate); // 订单日期(必须)
		bean.setQueryTransType(queryTransType); // 交易查询类型
		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("交易状态查询"); // 备注
		bean.setLogClient("0"); // PC
		// 调用汇付接口
		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
		if (chinapnrBean == null) {
			LogUtil.errorLog(THIS_CLASS, methodName, new Exception("调用交易状态查询接口失败![参数：" + bean.getAllParams() + "]"));
			return null;
		}
		return chinapnrBean;
	}

	/**
	 * 取得还款API任务表
	 *
	 * @return
	 */
	@Override
	public List<BorrowApicron> getBorrowApicronList(Integer status, Integer apiType) {

		BorrowApicronExample example = new BorrowApicronExample();
		BorrowApicronExample.Criteria criteria = example.createCriteria();
		criteria.andRepayStatusEqualTo(status);
		criteria.andApiTypeEqualTo(apiType);
		example.setOrderByClause(" id asc ");
		List<BorrowApicron> list = this.borrowApicronMapper.selectByExample(example);
		return list;
	}

	/**
	 * 更新借款API任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	@Override
	public int updateBorrowApicron(Integer id, Integer status) {
		return updateBorrowApicron(id, status, null);
	}

	/**
	 * 更新借款API任务表
	 *
	 * @return
	 */
	@Override
	public int updateBorrowApicron(Integer id, Integer status, String data) {
		BorrowApicron record = new BorrowApicron();
		record.setId(id);
		record.setRepayStatus(status);
		if (Validator.isNotNull(data) || status == 1) {
			record.setData(data);
		}
		if (record.getWebStatus() == null) {
			record.setWebStatus(0);
		}
		record.setUpdateTime(GetDate.getMyTimeInMillis());
		return this.borrowApicronMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 取得还款明细列表
	 *
	 * @return
	 */
	@Override
	public List<BorrowRecover> getBorrowRecoverList(String borrowNid) {

		BorrowRecoverExample example = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andRecoverStatusEqualTo(0); // 未还款
		criteria.andCreditTimeEqualTo(0); // 排除债转
		example.setOrderByClause(" id asc ");
		List<BorrowRecover> list = this.borrowRecoverMapper.selectByExample(example);
		return list;
	}

	/**
	 * 取得本期应还金额
	 *
	 * @return
	 */
	@Override
	public BigDecimal getBorrowAccountWithPeriod(String borrowNid, String borrowStyle, Integer period) {

		BigDecimal account = BigDecimal.ZERO;
		// 是否分期(true:分期, false:单期)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		if (isMonth) {
			BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
			BorrowRecoverPlanExample.Criteria criteria = example.createCriteria();
			criteria.andBorrowNidEqualTo(borrowNid);
			criteria.andRecoverStatusEqualTo(0);
			criteria.andRecoverPeriodEqualTo(period);
			List<BorrowRecoverPlan> list = this.borrowRecoverPlanMapper.selectByExample(example);
			if (list != null && list.size() > 0) {
				for (BorrowRecoverPlan p : list) {
					account = account.add(p.getRecoverAccount().add(p.getChargeInterest()).add(p.getDelayInterest()).add(p.getLateInterest()).add(p.getRecoverFee()));
				}
			}
		} else {
			BorrowRecoverExample example2 = new BorrowRecoverExample();
			BorrowRecoverExample.Criteria criteria2 = example2.createCriteria();
			criteria2.andBorrowNidEqualTo(borrowNid);
			criteria2.andRecoverStatusEqualTo(0);
			List<BorrowRecover> list2 = this.borrowRecoverMapper.selectByExample(example2);
			if (list2 != null && list2.size() > 0) {
				for (BorrowRecover p : list2) {
					account = account.add(p.getRecoverAccount().add(p.getChargeInterest()).add(p.getDelayInterest()).add(p.getLateInterest()).add(p.getRecoverFee()));
				}
			}
		}
		return account;
	}

	/**
	 * 取得还款计划列表
	 *
	 * @return
	 */
	@Override
	public BorrowRecoverPlan getBorrowRecoverPlan(String borrowNid, Integer period, Integer userId, Integer tenderId) {

		BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
		BorrowRecoverPlanExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andRecoverStatusEqualTo(0);
		criteria.andRecoverPeriodEqualTo(period);
		criteria.andTenderIdEqualTo(tenderId);
		criteria.andUserIdEqualTo(userId);
		example.setOrderByClause(" id asc ");
		List<BorrowRecoverPlan> list = this.borrowRecoverPlanMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得还款信息
	 *
	 * @return
	 */
	private BorrowRepay getBorrowRepay(String borrowNid) {

		BorrowRepayExample example = new BorrowRepayExample();
		BorrowRepayExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andRepayStatusEqualTo(0);
		example.setOrderByClause(" id asc ");
		List<BorrowRepay> list = this.borrowRepayMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得标的详情
	 *
	 * @return
	 */
	@Override
	public BorrowWithBLOBs getBorrow(String borrowNid) {

		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		example.setOrderByClause(" id asc ");
		List<BorrowWithBLOBs> list = this.borrowMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得借款列表
	 *
	 * @return
	 */
	@Override
	public BorrowTender getBorrowTender(Integer tenderId) {
		BorrowTender borrowTender = this.borrowTenderMapper.selectByPrimaryKey(tenderId);
		return borrowTender;
	}

	/**
	 * 取出账户信息
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public Account getAccountByUserId(Integer userId) {
		AccountExample accountExample = new AccountExample();
		accountExample.createCriteria().andUserIdEqualTo(userId);
		List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	/**
	 * 判断该收支明细是否存在
	 *
	 * @param accountList
	 * @return
	 */
	private int countAccountListByNid(String nid) {
		AccountListExample accountListExample = new AccountListExample();
		accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("tender_recover_yes");
		return this.accountListMapper.countByExample(accountListExample);
	}

	/**
	 * 写入收支明细
	 *
	 * @param accountList
	 * @return
	 */
	private int insertAccountList(AccountList accountList) {
		// 写入收支明细
		return this.accountListMapper.insertSelective(accountList);
	}

	/**
	 * 取得总的还款计划表
	 *
	 * @param borrowRepayPlan
	 * @param borrowNid
	 * @param period
	 * @return
	 */
	private BorrowRepayPlan getBorrowRepayPlan(String borrowNid, Integer period) {

		BorrowRepayPlanExample example = new BorrowRepayPlanExample();
		example.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayPeriodEqualTo(period);
		List<BorrowRepayPlan> list = this.borrowRepayPlanMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 更新还款信息
	 *
	 * @param record
	 * @return
	 */
	@Override
	public int updateBorrowRecover(BorrowRecover recoder) {
		int cnt = this.borrowRecoverMapper.updateByPrimaryKeySelective(recoder);
		return cnt;
	}

	/**
	 * 更新还款信息
	 *
	 * @param record
	 * @return
	 */
	public int updateBorrowRecoverPlan(BorrowRecoverPlan recoder) {
		int cnt = this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(recoder);
		return cnt;
	}

	/**
	 * 设置部门名称
	 *
	 * @param accountWebList
	 */
	private void setDepartments(AccountWebList accountWebList) {
		if (accountWebList != null) {
			Integer userId = accountWebList.getUserId();
			UsersInfo usersInfo = getUsersInfoByUserId(userId);
			if (usersInfo != null) {
				Integer attribute = usersInfo.getAttribute();
				if (attribute != null) {
					// 查找用户的的推荐人
					Users users = getUsersByUserId(userId);
					Integer refUserId = users.getReferrer();
					SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
					SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
					spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
					List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
					if (sList != null && !sList.isEmpty()) {
						refUserId = sList.get(0).getSpreadsUserid();
					}
					// 如果是线上员工或线下员工，推荐人的userId和username不插
					if (users != null && (attribute == 2 || attribute == 3)) {
						// 查找用户信息
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是无主单，全插
					else if (users != null && (attribute == 1)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
					// 如果是有主单
					else if (users != null && (attribute == 0)) {
						// 查找用户推荐人
						EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
						if (employeeCustomize != null) {
							accountWebList.setRegionName(employeeCustomize.getRegionName());
							accountWebList.setBranchName(employeeCustomize.getBranchName());
							accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
						}
					}
				}
				accountWebList.setTruename(usersInfo.getTruename());
				accountWebList.setFlag(1);
			}
		}
	}

	/**
	 * 判断网站收支是否存在
	 *
	 * @param nid
	 * @return
	 */
	private int countAccountWebList(String nid, String trade) {
		AccountWebListExample example = new AccountWebListExample();
		example.createCriteria().andOrdidEqualTo(nid).andTradeEqualTo(trade);
		return this.accountWebListMapper.countByExample(example);
	}

	/**
	 * 插入网站收支记录
	 *
	 * @param nid
	 * @return
	 */
	private int insertAccountWebList(AccountWebList accountWebList) {
		if (countAccountWebList(accountWebList.getOrdid(), accountWebList.getTrade()) == 0) {
			// 设置部门信息
			setDepartments(accountWebList);
			// 插入
			return this.accountWebListMapper.insertSelective(accountWebList);
		}
		return 0;
	}

	/**
	 * 发送短信(还款成功)
	 *
	 * @param userId
	 */
	@Override
	public void sendSms(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && NumberUtils.isNumber(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null || Validator.isNull(users.getMobile()) || (users.getRecieveSms() != null && users.getRecieveSms() == 1)) {
						return;
					}
					SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_SHOUDAOHUANKUAN, CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				}
			}
		}
	}

	/**
	 * 推送消息
	 * 
	 * @param msgList
	 * @author Administrator
	 */

	@Override
	public void sendMessage(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT)) && new BigDecimal(msg.get(VAL_AMOUNT)).compareTo(BigDecimal.ZERO) > 0) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null) {
						return;
					} else {
						UsersInfo userInfo = this.getUsersInfoByUserId(Integer.valueOf(msg.get(VAL_USERID)));
						if (StringUtils.isEmpty(userInfo.getTruename())) {
							msg.put(VAL_NAME, users.getUsername());
						} else if (userInfo.getTruename().length() > 1) {
							msg.put(VAL_NAME, userInfo.getTruename().substring(0, 1));
						} else {
							msg.put(VAL_NAME, userInfo.getTruename());
						}
						Integer sex = userInfo.getSex();
						if (Validator.isNotNull(sex)) {
							if (sex.intValue() == 2) {
								msg.put(VAL_SEX, "女士");
							} else {
								msg.put(VAL_SEX, "先生");
							}
						}
						AppMsMessage smsMessage = new AppMsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_SHOUDAOHUANKUAN);
						appMsProcesser.gather(smsMessage);
					}
				}
			}
		}
	}

	/**
	 * 获取实际还款用户id
	 * @param userId
	 * @param borrowNid
	 * @return
	 */
	public Integer getRepayUserId(Integer userId, String borrowNid){
		Integer userid= this.borrowRepayCustomizeMapper.getRepayUserId(userId, borrowNid);
		return userid;
	}
	
}



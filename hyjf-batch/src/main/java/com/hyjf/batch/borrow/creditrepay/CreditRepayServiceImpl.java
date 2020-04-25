package com.hyjf.batch.borrow.creditrepay;

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
import com.hyjf.common.calculate.AccountManagementFeeUtils;
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
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowCreditExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlanExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlanExample.Criteria;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditRepayExample;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

/**
 * 债转自动还款(还款服务)
 * 
 * 注：此次不考虑逾期还款，延期还款，提前还款， 均按照按照正常还款进行
 * 
 * @author Administrator
 *
 */
@Service
public class CreditRepayServiceImpl extends BaseServiceImpl implements CreditRepayService {

	private static final String THIS_CLASS = CreditRepayServiceImpl.class.getName();

	/** 用户ID */
	private static final String VAL_USERID = "userId";
	/** 性别 */
	private static final String VAL_SEX = "val_sex";
	/** 用户名 */
	private static final String VAL_NAME = "val_name";
	/** 项目标题 */
	private static final String VAL_TITLE = "val_title";
	/** 项目标题 */
	private static final String VAL_BORROWNID = "val_borrownid";
	/** 放款金额 */
	private static final String VAL_AMOUNT = "val_amount";
	/** 放款金额 */
	private static final String VAL_CAPITAL = "val_capital";
	/** 预期收益 */
	private static final String VAL_PROFIT = "val_profit";
	/** 预期收益 */
	private static final String VAL_INTEREST = "val_interest";
	/** 等待 */
	private static final String TYPE_WAIT = "wait";
	/** 完成 */
	private static final String TYPE_YES = "yes";
	/** 部分完成 */
	private static final String TYPE_WAIT_YES = "wait_yes";

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
	public List<Map<String, String>> updateBorrowCreditRepay(BorrowApicron apicron, BorrowRecover borrowRecover, AccountChinapnr repayUserCust, BorrowCredit borrowCredit, CreditTender creditTender, CreditRepay creditRepay) throws Exception {

		String methodName = "updateBorrowCreditRepay";
		System.out.println("------债转还款承接部分开始---承接订单号：" + borrowCredit.getCreditNid() + "---------");
		List<Map<String, String>> retMsgList = new ArrayList<Map<String, String>>();
		Map<String, String> msgnew = new HashMap<String, String>();
		retMsgList.add(msgnew);

		/** 基本变量 */
		// 还款订单号
		String repayOrderId = null;
		// 还款订单日期
		String repayOrderDate = null;
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 借款编号
		String borrowNid = apicron.getBorrowNid();
		// 还款人(借款人或垫付机构)ID
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
		// 标的是否可用担保机构还款
		int isRepayOrgFlag = Validator.isNull(borrow.getIsRepayOrgFlag()) ? 0 : borrow.getIsRepayOrgFlag();
		// 是否是担保机构还款
		int isApicronRepayOrgFlag = Validator.isNull(apicron.getIsRepayOrgFlag()) ? 0 : apicron.getIsRepayOrgFlag();
		// 项目总期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 管理费率
		BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
		// 差异费率
		BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
		// 初审时间
		int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
		// 标的借款人ID
		Integer borrowUserId = borrow.getUserId();
		// 剩余还款期数
		Integer periodNext = borrowPeriod - periodNow;
		// 出借订单号
		String tenderOrdId = borrowRecover.getNid();
		// 出借人用户ID
		Integer tenderUserId = borrowRecover.getUserId();
		// 出借ID
		Integer tenderId = borrowRecover.getTenderId();
		// 出借人在汇付的账户信息
		AccountChinapnr tenderUserCust = null;
		// 还款人(借款人或担保机构)客户号
		Long repayUserCustId = null;
		// 标的借款人客户号
		Long borrowUserCustId = null;
		// 取得还款详情
		BorrowRepay borrowRepay = getBorrowRepay(borrowNid);
		// 取得出借信息
		BorrowTender borrowTender = getBorrowTender(tenderId);
		// 出借人在汇付的账户信息
		tenderUserCust = getChinapnrUserInfo(tenderUserId);
		if (tenderUserCust == null) {
			throw new Exception("出借人未开户。[用户ID：" + tenderUserId + "]，" + "[出借订单号：" + tenderOrdId + "]");
		}
		
		// 借款人在汇付的账户信息
		AccountChinapnr borrowUserCust = this.getChinapnrUserInfo(borrowUserId);
		if (borrowUserCust == null) {
			throw new Exception("借款人未开户。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
		}
		// 标的借款人客户号
		borrowUserCustId = borrowUserCust.getChinapnrUsrcustid();
		// 借款人客户号
		repayUserCustId = repayUserCust.getChinapnrUsrcustid();
		// 分期还款计划表
		BorrowRecoverPlan borrowRecoverPlan = null;
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);

		if (creditTender != null) {
			// 承接人承接nid
			String assignNid = creditTender.getAssignNid();
			if (creditRepay == null) {
				throw new Exception("creditRepay未有此笔债转订单的还款信息" + "，[承接订单号：" + assignNid + "]");
			}
			// 还款订单号
			repayOrderId = creditRepay.getCreditRepayOrderId();
			// 还款订单日期
			repayOrderDate = creditRepay.getCreditRepayOrderDate();
			// 判断该收支明细是否存在时,跳出本次循环
			if (countAccountCreditListByNid(repayOrderId) > 0) {
				return retMsgList;
			}
			// 管理费
			BigDecimal perManage = new BigDecimal("0");
			// 按月计息，到期还本还息end
			if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
				perManage = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(creditRepay.getAssignCapital(), feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
			}
			// 等额本息month、等额本金principal
			else if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
				if (periodNow.intValue() == borrowPeriod.intValue()) {
					perManage = AccountManagementFeeUtils.getMonthAccountManagementFee(creditRepay.getAssignCapital(), feeRate, periodNow, differentialRate, 1, borrow.getAccount(), borrowPeriod, borrowVerifyTime);
				} else {
					perManage = AccountManagementFeeUtils.getMonthAccountManagementFee(creditRepay.getAssignCapital(), feeRate, periodNow, differentialRate, 0, borrow.getAccount(), borrowPeriod, borrowVerifyTime);
				}
			}
			// 先息后本endmonth
			else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
				if (periodNow.intValue() == borrowPeriod.intValue()) {
					perManage = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate, borrowPeriod, periodNow, differentialRate, 1, borrowVerifyTime);
				} else {
					perManage = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate, borrowPeriod, periodNow, differentialRate, 0, borrowVerifyTime);
				}
			}
			// 按天计息到期还本还息
			else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
				perManage = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(creditRepay.getAssignCapital(), feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
			}
			// 债权承接人在汇付的账户信息
			AccountChinapnr creditUserCust = getChinapnrUserInfo(creditRepay.getUserId());
			if (creditUserCust == null) {
				throw new Exception("债权承接人未开户。[用户ID：" + creditRepay.getUserId() + "]，" + "[承接订单号：" + creditRepay.getAssignNid() + "]");
			}
			Long creditUserCustId = creditUserCust.getChinapnrUsrcustid();
			// 查询债转还款状态 调用交易查询接口
			ChinapnrBean queryCreditTransStatBean = queryTransStat(repayOrderId, repayOrderDate + "", "REPAYMENT");
			String creditRespCode = queryCreditTransStatBean == null ? "" : queryCreditTransStatBean.getRespCode();
			// 调用接口失败时(000,422以外)
			if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(creditRespCode) && !ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(creditRespCode)) {
				String message = queryCreditTransStatBean == null ? "" : queryCreditTransStatBean.getRespDesc();
				LogUtil.errorLog(THIS_CLASS, methodName, "调用交易查询接口失败。" + message + "，[承接订单号：" + creditRepay.getAssignNid() + "]", null);
				throw new Exception("调用交易查询接口失败。" + creditRespCode + "：" + message + "，[承接订单号：" + creditRepay.getAssignNid() + "]");
			}
			// 汇付交易状态
			String creditTransStat = queryCreditTransStatBean.getTransStat();
			// I:初始 P:部分成功
			if (ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(creditRespCode) || (!"I".equals(creditTransStat) && !"P".equals(creditTransStat))) {
				if (bankInputFlag == 1 || isRepayOrgFlag == 1) {
					// 分账账户串（当 管理费！=0 时是必填项）
					String divDetails = "";
					if (perManage.compareTo(BigDecimal.ZERO) > 0) {
						JSONArray ja = new JSONArray();
						JSONObject jo = new JSONObject();
						// 分账商户号(商户号,从配置文件中取得)
						jo.put(ChinaPnrConstant.PARAM_DIVCUSTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
						// 分账账户号(子账户号,从配置文件中取得)
						jo.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT03));
						// 分账金额
						jo.put(ChinaPnrConstant.PARAM_DIVAMT, perManage.toString());
						ja.add(jo);
						divDetails = ja.toString();
					}
					// 入参扩展域(2.0用)
					String reqExts = "";
					// 调用汇付接口
					if (creditRepay.getAssignCapital().add(creditRepay.getAssignInterest()).compareTo(BigDecimal.ZERO) > 0) {
						ChinapnrBean repaymentBean = repayment(borrowNid, repayUserid, String.valueOf(repayUserCustId), creditRepay.getAssignCapital().toString(), creditRepay.getAssignInterest().toString(), perManage.toString(), repayOrderId, repayOrderDate, creditRepay.getAssignNid(),
								String.valueOf(creditRepay.getAssignCreateDate()), String.valueOf(creditUserCustId), divDetails, reqExts,bankInputFlag,isRepayOrgFlag,isApicronRepayOrgFlag,String.valueOf(borrowUserCustId));
						creditRespCode = repaymentBean == null ? "" : repaymentBean.getRespCode();
						// 调用接口失败时(000以外)
						if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(creditRespCode)) {
							String message = repaymentBean == null ? "" : repaymentBean.getRespDesc();
							LogUtil.errorLog(THIS_CLASS, methodName, "调用自动扣款（还款）接口失败。" + message + "，[承接订单号：" + creditRepay.getAssignNid() + "]", null);
							throw new Exception("调用债转自动扣款（还款）接口失败。" + creditRespCode + "：" + message + "，[承接订单号：" + creditRepay.getAssignNid() + "]");
						}
					}
				} else {
					// 分账账户串（当 管理费！=0 时是必填项）
					String divDetails = "";
					if (perManage.compareTo(BigDecimal.ZERO) > 0) {
						JSONArray ja = new JSONArray();
						JSONObject jo = new JSONObject();
						// 分账账户号(子账户号,从配置文件中取得)
						jo.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT03));
						// 分账金额
						jo.put(ChinaPnrConstant.PARAM_DIVAMT, perManage.toString());
						ja.add(jo);
						divDetails = ja.toString();
					}
					// 入参扩展域(2.0用)
					String reqExts = "";
					// 调用汇付接口
					if (creditRepay.getAssignAccount().compareTo(BigDecimal.ZERO) > 0) {
						ChinapnrBean repaymentBean = repaymentOld(repayUserid, String.valueOf(repayUserCustId), creditRepay.getAssignAccount().toString(), perManage.toString(), repayOrderId, repayOrderDate, creditRepay.getAssignNid(), String.valueOf(creditRepay.getAssignCreateDate()),
								String.valueOf(creditUserCustId), divDetails, reqExts);
						creditRespCode = repaymentBean == null ? "" : repaymentBean.getRespCode();
						// 调用接口失败时(000以外)
						if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(creditRespCode)) {
							String message = repaymentBean == null ? "" : repaymentBean.getRespDesc();
							LogUtil.errorLog(THIS_CLASS, methodName, "调用自动扣款（还款）接口失败。" + message + "，[承接订单号：" + creditRepay.getAssignNid() + "]", null);
							throw new Exception("调用债转自动扣款（还款）接口失败。" + creditRespCode + "：" + message + "，[承接订单号：" + creditRepay.getAssignNid() + "]");
						}
					}
				}
			}
			// 该笔债转的收支明细存在时,跳出本次循环
			if (countAccountCreditListByNid(repayOrderId) == 0) {
				// 债转的下次还款时间
				int creditRepayNextTime = creditRepay.getAssignRepayNextTime();
				// 更新承接人账户信息
				Account account = new Account();
				account.setUserId(creditRepay.getUserId());
				// 承接人资金总额
				account.setTotal(new BigDecimal(0));
				// 承接人可用余额
				account.setBalance(creditRepay.getAssignAccount());
				// 承接人待收金额
				account.setAwait(creditRepay.getAssignAccount());
				// 更新承接人账户信息
				boolean creditUserAccountFlag = this.adminAccountCustomizeMapper.updateOfRepayTender(account) > 0 ? true : false;
				if (creditUserAccountFlag) {
					// 取得承接人账户信息
					account = this.getAccountByUserId(creditRepay.getUserId());
					// 写入承接人收支明细
					AccountList accountList = new AccountList();
					// 出借标识
					accountList.setNid(repayOrderId);
					// 承接人
					accountList.setUserId(creditRepay.getUserId());
					// 承接人总收入
					accountList.setAmount(creditRepay.getAssignAccount());
					// 1收入
					accountList.setType(1);
					// 承接人收到还款成功
					accountList.setTrade("credit_tender_recover_yes");
					// 余额操作
					accountList.setTradeCode("balance");
					// 承接人资金总额
					accountList.setTotal(account.getTotal());
					// 承接人可用金额
					accountList.setBalance(account.getBalance());
					// 承接人冻结金额
					accountList.setFrost(account.getFrost());
					// 承接人待收金额
					accountList.setAwait(account.getAwait());
					// 汇添金冻结金额
					accountList.setPlanFrost(account.getPlanFrost());
					// 汇添金可用金额
					accountList.setPlanBalance(account.getPlanBalance());
					// 利息
					accountList.setInterest(BigDecimal.ZERO);
					// 承接人收到还款
					accountList.setRemark("债转收到还款");
					// 创建时间
					accountList.setCreateTime(nowTime);
					// 更新时间
					accountList.setBaseUpdate(nowTime);
					// 操作者
					accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY);
					// 操作IP
					accountList.setIp(borrow.getAddip());
					accountList.setIsUpdate(0);
					accountList.setBaseUpdate(0);
					// PC
					accountList.setWeb(0);
					boolean creditUserAccountListFlag = insertAccountList(accountList) > 0 ? true : false;
					if (creditUserAccountListFlag) {
						// 更新相应的债转出借表
						// 债转已还款总额
						creditTender.setAssignRepayAccount(creditTender.getAssignRepayAccount().add(creditRepay.getAssignAccount()));
						// 债转已还款本金
						creditTender.setAssignRepayCapital(creditTender.getAssignRepayCapital().add(creditRepay.getAssignCapital()));
						// 债转已还款利息
						creditTender.setAssignRepayInterest(creditTender.getAssignRepayInterest().add(creditRepay.getAssignInterest()));
						// 债转最近还款时间
						creditTender.setAssignRepayLastTime(!isMonth ? nowTime : 0);
						// 债转下次还款时间
						creditTender.setAssignRepayNextTime(!isMonth ? 0 : creditRepayNextTime);
						// 债转最后还款时间
						creditTender.setAssignRepayYesTime(!isMonth ? nowTime : 0);
						// 债转还款状态
						if (isMonth) {
							// 取得分期还款计划表
							borrowRecoverPlan = getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId, borrowRecover.getTenderId());
							if (borrowRecoverPlan == null) {
								throw new Exception("分期还款计划表数据不存在。[借款编号：" + borrowNid + "]，" + "[承接订单号：" + creditRepay.getAssignNid() + "]" + "[期数：" + periodNow + "]");
							}
							// 债转状态
							if (borrowRecoverPlan != null && Validator.isNotNull(periodNext) && periodNext > 0) {
								creditTender.setStatus(0);
							} else {
								creditTender.setStatus(1);
							}
						} else {
							creditTender.setStatus(1);
						}
						// 债转还款期
						creditTender.setRecoverPeriod(periodNow);
						boolean creditTenderFlag = this.creditTenderMapper.updateByPrimaryKeySelective(creditTender) > 0 ? true : false;
						if (creditTenderFlag) {
							creditRepay.setAssignRepayAccount(creditRepay.getAssignRepayAccount().add(creditRepay.getAssignAccount()));
							creditRepay.setAssignRepayCapital(creditRepay.getAssignRepayCapital().add(creditRepay.getAssignCapital()));
							creditRepay.setAssignRepayInterest(creditRepay.getAssignRepayInterest().add(creditRepay.getAssignInterest()));
							creditRepay.setAssignRepayLastTime(nowTime);
							creditRepay.setAssignRepayYesTime(nowTime);
							creditRepay.setManageFee(perManage);
							creditRepay.setStatus(1);
							boolean creditRepayFlag = this.creditRepayMapper.updateByPrimaryKeySelective(creditRepay) > 0 ? true : false;
							if (creditRepayFlag) {
								// 债转总表数据更新
								// 更新债转已还款总额
								borrowCredit.setCreditRepayAccount(borrowCredit.getCreditRepayAccount().add(creditRepay.getAssignAccount()));
								// 更新债转已还款本金
								borrowCredit.setCreditRepayCapital(borrowCredit.getCreditRepayCapital().add(creditRepay.getAssignCapital()));
								// 更新债转已还款利息
								borrowCredit.setCreditRepayInterest(borrowCredit.getCreditRepayInterest().add(creditRepay.getAssignInterest()));
								// 债转下次还款时间
								borrowCredit.setCreditRepayNextTime(isMonth ? creditRepayNextTime : 0);
								// 更新债转总表
								boolean borrowCreditFlag = this.borrowCreditMapper.updateByPrimaryKeySelective(borrowCredit) > 0 ? true : false;
								// 债转总表更新成功
								if (borrowCreditFlag) {
									// 更新还款表（不分期）
									// 已还款总额
									borrowRecover.setRecoverAccountYes(borrowRecover.getRecoverAccountYes().add(creditRepay.getAssignAccount()));
									// 已还款本金
									borrowRecover.setRecoverCapitalYes(borrowRecover.getRecoverCapitalYes().add(creditRepay.getAssignCapital()));
									// 已还款利息
									borrowRecover.setRecoverInterestYes(borrowRecover.getRecoverInterestYes().add(creditRepay.getAssignInterest()));
									// 待还金额
									borrowRecover.setRecoverAccountWait(borrowRecover.getRecoverAccountWait().subtract(creditRepay.getAssignAccount()));
									// 待还利息
									borrowRecover.setRecoverInterestWait(borrowRecover.getRecoverInterestWait().subtract(creditRepay.getAssignInterest()));
									// 待还本金
									borrowRecover.setRecoverCapitalWait(borrowRecover.getRecoverCapitalWait().subtract(creditRepay.getAssignCapital()));
									// 更新还款表
									boolean creditBorrowRecoverFlag = this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover) > 0 ? true : false;
									if (creditBorrowRecoverFlag) {
										// 更新总的还款明细
										borrowRepay.setRepayAccountAll(borrowRepay.getRepayAccountAll().add(creditRepay.getAssignAccount()).add(perManage));
										borrowRepay.setRepayAccountYes(borrowRepay.getRepayAccountYes().add(creditRepay.getAssignAccount()));
										borrowRepay.setRepayInterestYes(borrowRepay.getRepayInterestYes().add(creditRepay.getAssignInterest()));
										borrowRepay.setRepayCapitalYes(borrowRepay.getRepayCapitalYes().add(creditRepay.getAssignCapital()));
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
										boolean creditBorrowRepayFlag = this.borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay) > 0 ? true : false;
										if (creditBorrowRepayFlag) {
											// 如果分期
											if (isMonth) {
												// 更新还款表（分期）
												// 已还款总额
												borrowRecoverPlan.setRecoverAccountYes(borrowRecoverPlan.getRecoverAccountYes().add(creditRepay.getAssignAccount()));
												// 已还款本金
												borrowRecoverPlan.setRecoverCapitalYes(borrowRecoverPlan.getRecoverCapitalYes().add(creditRepay.getAssignCapital()));
												// 已还款利息
												borrowRecoverPlan.setRecoverInterestYes(borrowRecoverPlan.getRecoverInterestYes().add(creditRepay.getAssignInterest()));
												// 待还金额
												borrowRecoverPlan.setRecoverAccountWait(borrowRecoverPlan.getRecoverAccountWait().subtract(creditRepay.getAssignAccount()));
												// 待还利息
												borrowRecoverPlan.setRecoverInterestWait(borrowRecoverPlan.getRecoverInterestWait().subtract(creditRepay.getAssignInterest()));
												// 待还本金
												borrowRecoverPlan.setRecoverCapitalWait(borrowRecoverPlan.getRecoverCapitalWait().subtract(creditRepay.getAssignCapital()));
												// 更新还款计划表
												boolean borrowRecoverPlanFlag = this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(borrowRecoverPlan) > 0 ? true : false;
												if (!borrowRecoverPlanFlag) {
													throw new Exception("分期还款计划表更新失败。[借款编号：" + borrowNid + "]，" + "[承接订单号：" + creditRepay.getAssignNid() + "]" + "[期数：" + periodNow + "]");
												} else {
													// 更新总的还款计划
													BorrowRepayPlan borrowRepayPlan = getBorrowRepayPlan(borrowNid, periodNow);
													if (borrowRepayPlan != null) {
														// 还款总额
														borrowRepayPlan.setRepayAccountAll(borrowRepayPlan.getRepayAccountAll().add(creditRepay.getAssignAccount()).add(perManage));
														// 已还金额
														borrowRepayPlan.setRepayAccountYes(borrowRepayPlan.getRepayAccountYes().add(creditRepay.getAssignAccount()));
														// 已还利息
														borrowRepayPlan.setRepayInterestYes(borrowRepayPlan.getRepayInterestYes().add(creditRepay.getAssignInterest()));
														// 已还本金
														borrowRepayPlan.setRepayCapitalYes(borrowRepayPlan.getRepayCapitalYes().add(creditRepay.getAssignCapital()));
														// 逾期天数
														borrowRepayPlan.setLateRepayDays(borrowRecoverPlan.getLateDays());
														// 提前天数
														borrowRepayPlan.setChargeDays(borrowRecoverPlan.getChargeDays());
														// 延期天数
														borrowRepayPlan.setDelayDays(borrowRecoverPlan.getDelayDays());
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
														if (!borrowRepayPlanFlag) {
															throw new Exception("还款计划表(huiyingdai_borrow_repay_plan)更新失败！" + "[承接订单号：" + creditRepay.getAssignNid() + "]" + "[期数：" + periodNow + "]");
														}
													} else {
														throw new Exception("还款计划表(huiyingdai_borrow_repay_plan)查询失败！" + "[承接订单号：" + creditRepay.getAssignNid() + "]" + "[期数：" + periodNow + "]");
													}
												}
											}
										} else {
											throw new Exception("总的还款明细表(huiyingdai_borrow_repay)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
										}
										// 机构还款时,更新借款人账户信息
										// 如果是机构垫付还款
										if(isRepayOrgFlag == 1 && isApicronRepayOrgFlag ==1){
											// 更新借款人的Account表的待还款金额
											// 取得账户信息(借款人)
											Account borrowUserAccount = this.getAccountByUserId(borrowUserId);
											Account newBorrowUserAccount = new Account();
											newBorrowUserAccount.setUserId(borrowUserAccount.getUserId());
											newBorrowUserAccount.setRepay(creditRepay.getAssignAccount());
											boolean borrowUserFlag = this.adminAccountCustomizeMapper.updateOfRepayBorrowUser(newBorrowUserAccount) > 0 ? true : false ;
											if(!borrowUserFlag){
												throw new Exception("垫付机构还款后,借款人待还金额更新失败！" + "[借款人用户ID：" + borrowUserId + "]");
											}
										}
										// 更新借款表
										borrow = getBorrow(borrowNid);
										BorrowWithBLOBs newBrrow = new BorrowWithBLOBs();
										newBrrow.setId(borrow.getId());
										BigDecimal borrowManager = borrow.getBorrowManager() == null ? BigDecimal.ZERO : new BigDecimal(borrow.getBorrowManager());
										newBrrow.setBorrowManager(borrowManager.add(perManage).toString());
										// 总还款利息
										newBrrow.setRepayAccountYes(borrow.getRepayAccountYes().add(creditRepay.getAssignAccount()));
										// 总还款利息
										newBrrow.setRepayAccountInterestYes(borrow.getRepayAccountInterestYes().add(creditRepay.getAssignInterest()));
										// 总还款本金
										newBrrow.setRepayAccountCapitalYes(borrow.getRepayAccountCapitalYes().add(creditRepay.getAssignCapital()));
										// 未还款总额
										newBrrow.setRepayAccountWait(borrow.getRepayAccountWait().subtract(creditRepay.getAssignAccount()));
										// 未还款利息
										newBrrow.setRepayAccountInterestWait(borrow.getRepayAccountInterestWait().subtract(creditRepay.getAssignInterest()));
										// 未还款本金
										newBrrow.setRepayAccountCapitalWait(borrow.getRepayAccountCapitalWait().subtract(creditRepay.getAssignCapital()));
										// 项目的管理费
										newBrrow.setRepayFeeNormal(borrow.getRepayFeeNormal().add(perManage));
										boolean borrowFlag = this.borrowMapper.updateByPrimaryKeySelective(newBrrow) > 0 ? true : false;
										if (borrowFlag) {
											// 更新出借表
											// 已还款金额
											borrowTender.setRecoverAccountYes(borrowTender.getRecoverAccountYes().add(creditRepay.getAssignAccount()));
											// 已还款利息
											borrowTender.setRecoverAccountInterestYes(borrowTender.getRecoverAccountInterestYes().add(creditRepay.getAssignInterest()));
											// 已还款本金
											borrowTender.setRecoverAccountCapitalYes(borrowTender.getRecoverAccountCapitalYes().add(creditRepay.getAssignCapital()));
											// 待还金额
											borrowTender.setRecoverAccountWait(borrowTender.getRecoverAccountWait().subtract(creditRepay.getAssignAccount()));
											// 待还利息
											borrowTender.setRecoverAccountInterestWait(borrowTender.getRecoverAccountInterestWait().subtract(creditRepay.getAssignInterest()));
											// 待还本金
											borrowTender.setRecoverAccountCapitalWait(borrowTender.getRecoverAccountCapitalWait().subtract(creditRepay.getAssignCapital()));
											boolean borrowTenderFlag = borrowTenderMapper.updateByPrimaryKeySelective(borrowTender) > 0 ? true : false;
											if (borrowTenderFlag) {
												// 管理费大于0时,插入网站收支明细
												if (perManage.compareTo(BigDecimal.ZERO) > 0) {
													// 插入网站收支明细记录
													AccountWebList accountWebList = new AccountWebList();
													accountWebList.setOrdid(creditRepay.getAssignNid() + "_" + periodNow);// 订单号
													accountWebList.setBorrowNid(borrowNid); // 出借编号
													accountWebList.setUserId(repayUserid); // 借款人
													accountWebList.setAmount(perManage); // 管理费
													accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
													accountWebList.setTrade(CustomConstants.TRADE_REPAYFEE); // 管理费
													accountWebList.setTradeType(CustomConstants.TRADE_REPAYFEE_NM); // 账户管理费
													accountWebList.setRemark(borrowNid); // 出借编号
													accountWebList.setCreateTime(nowTime);
													boolean accountWebFlag = insertAccountWebList(accountWebList) > 0 ? true : false;
													if (!accountWebFlag) {
														throw new Exception("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[债转承接订单号：" + creditRepay.getAssignNid() + "]");
													}
												}
												Map<String, String> msg = new HashMap<String, String>();
												// 债转还款结束
												msg.put(VAL_USERID, creditRepay.getUserId().toString());
												msg.put(VAL_AMOUNT, creditRepay.getAssignAccount() == null ? "0.00" : creditRepay.getAssignAccount().toString());
												msg.put(VAL_CAPITAL, creditRepay.getAssignCapital() == null ? "0.00" : creditRepay.getAssignCapital().toString());
												msg.put(VAL_BORROWNID, borrowRecover.getBorrowNid());
												msg.put(VAL_INTEREST, creditRepay.getAssignInterest() == null ? "0.00" : creditRepay.getAssignInterest().toString());
												msg.put(VAL_TITLE, borrowRecover.getBorrowNid());
												msg.put(VAL_PROFIT, creditRepay.getAssignInterest() == null ? "0.00" : creditRepay.getAssignInterest().toString());
												retMsgList.add(msg);
											} else {
												throw new Exception("出借表(huiyingdai_borrow_tender)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
											}
										} else {
											throw new Exception("借款详情(huiyingdai_borrow)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
										}
									} else {
										throw new Exception("出借人还款表(huiyingdai_borrow_recover)更新失败！" + "[债转编号：" + creditRepay.getCreditNid() + "]");
									}
								} else {
									throw new Exception("债转记录(huiyingdai_borrow_credit)更新失败！" + "[承接订单号：" + creditRepay.getCreditNid() + "]");
								}
							} else {
								throw new Exception("承接人还款表(huiyingdai_credit_repay)更新失败！" + "[债转编号：" + creditRepay.getCreditNid() + "]");
							}
						} else {
							throw new Exception("债转出借表(huiyingdai_credit_tender)更新失败！" + "[承接订单号：" + creditRepay.getAssignNid() + "]");
						}
					} else {
						throw new Exception("收支明细(huiyingdai_account_list)写入失败！" + "[承接订单号：" + creditRepay.getAssignNid() + "]");
					}
				} else {
					throw new Exception("承接人资金记录(huiyingdai_account)更新失败！" + "[承接订单号：" + creditRepay.getAssignNid() + "]");
				}
			}
		}
		System.out.println("------债转还款承接部分完成---承接订单号：" + borrowCredit.getCreditNid() + "---------还款订单号" + repayOrderId);
		return retMsgList;
	}

	/**
	 * 
	 * @param apicron
	 * @param borrowRecover
	 * @param repayUserCust
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> updateBorrowRepay(BorrowApicron apicron, BorrowRecover borrowRecover, AccountChinapnr repayUserCust) throws Exception {

		String methodName = "updateBorrowRepay";
		System.out.println("------债转还款未承接部分开始---项目编号：" + apicron.getBorrowNid() + "---------");
		List<Map<String, String>> retMsgList = new ArrayList<Map<String, String>>();
		Map<String, String> msgnew = new HashMap<String, String>();
		retMsgList.add(msgnew);

		/** 基本变量 */
		// 还款订单号
		String repayOrderId = null;
		// 还款订单日期
		String repayOrderDate = null;
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 借款编号
		String borrowNid = apicron.getBorrowNid();
		// 还款人(借款人或垫付机构)ID
		Integer repayUserid = apicron.getUserId();
		// 当前期数
		Integer periodNow = apicron.getPeriodNow();
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
		// 项目总期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 管理费率
		BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
		// 差异费率
		BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
		// 初审时间
		int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());

		// 剩余还款期数
		Integer periodNext = borrowPeriod - periodNow;
		// 出借订单号
		String tenderOrdId = borrowRecover.getNid();
		// 出借人用户ID
		Integer tenderUserId = borrowRecover.getUserId();
		// 出借ID
		Integer tenderId = borrowRecover.getTenderId();
		// 还款时间
		String recoverTime = null;

		// 还款总额
		BigDecimal recoverAccount = BigDecimal.ZERO;
		// 还款本金
		BigDecimal recoverCapital = BigDecimal.ZERO;
		// 还款利息
		BigDecimal recoverInterest = BigDecimal.ZERO;
		// 还款总额(已还)
		BigDecimal recoverAccountYesAlready = BigDecimal.ZERO;
		// 还款本金(已还)
		BigDecimal recoverCapitalYesAlready = BigDecimal.ZERO;
		// 还款利息(已还)
		BigDecimal recoverInterestYesAlready = BigDecimal.ZERO;
		// 剩余未债转本息
		BigDecimal recoverAccountRemain = BigDecimal.ZERO;
		// 剩余未债转利息
		BigDecimal recoverInterestRemain = BigDecimal.ZERO;
		// 剩余未债转本金
		BigDecimal recoverCapitalRemain = BigDecimal.ZERO;
		// 借款人待还金额
		BigDecimal borrowUserRepayAccount = BigDecimal.ZERO;
		// 剩余待还债转本息
		BigDecimal recoverAccountRemainRepay = BigDecimal.ZERO;
		// 剩余待还债转利息
		BigDecimal recoverInterestRemainRepay = BigDecimal.ZERO;
		// 剩余待还债转本金
		BigDecimal recoverCapitalRemainRepay = BigDecimal.ZERO;
		// 逾期利息
		BigDecimal lateInterest = BigDecimal.ZERO;
		// 延期利息
		BigDecimal delayInterest = BigDecimal.ZERO;
		// 提前还款少还利息
		BigDecimal chargeInterest = BigDecimal.ZERO;
		// 出借人在汇付的账户信息
		AccountChinapnr tenderUserCust = null;
		// 出借人客户号
		Long tenderUserCustId = null;
		// 还款人(借款人或垫付机构)客户号
		Long repayUserCustId = null;
		// 标的借款人客户号
		Long borrowUserCustId = null;

		// 取得还款详情
		BorrowRepay borrowRepay = getBorrowRepay(borrowNid);
		// 取得出借信息
		BorrowTender borrowTender = getBorrowTender(tenderId);
		// 出借人在汇付的账户信息
		tenderUserCust = getChinapnrUserInfo(tenderUserId);
		if (tenderUserCust == null) {
			throw new Exception("出借人未开户。[用户ID：" + tenderUserId + "]，" + "[出借订单号：" + tenderOrdId + "]");
		}
		// 借款人在汇付的账户信息
		AccountChinapnr borrowUserCust = this.getChinapnrUserInfo(borrowUserId);
		if (borrowUserCust == null) {
			throw new Exception("借款人未开户。[用户ID：" + borrowUserId + "]，" + "[借款编号：" + borrowNid + "]");
		}
		// 出借人客户号
		tenderUserCustId = tenderUserCust.getChinapnrUsrcustid();
		// 还款人客户号(借款人或垫付机构)
		repayUserCustId = repayUserCust.getChinapnrUsrcustid();
		// 标的借款人客户号
		borrowUserCustId = borrowUserCust.getChinapnrUsrcustid();
		// 分期还款计划表
		BorrowRecoverPlan borrowRecoverPlan = null;
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		if (isMonth) {
			// 取得分期还款计划表
			borrowRecoverPlan = getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId, borrowRecover.getTenderId());
			if (borrowRecoverPlan != null) {
				// 还款订单号
				repayOrderId = borrowRecoverPlan.getRepayOrderId();
				// 还款订单日期
				repayOrderDate = borrowRecoverPlan.getRepayOrderDate();
				// 应还款时间
				recoverTime = borrowRecoverPlan.getRecoverTime();
				// 应还款金额
				recoverAccount = borrowRecoverPlan.getRecoverAccount();
				// 应还款本金
				recoverCapital = borrowRecoverPlan.getRecoverCapital();
				// 应还款利息
				recoverInterest = borrowRecoverPlan.getRecoverInterest();
				// 还款金额(已还)
				recoverAccountYesAlready = borrowRecoverPlan.getRecoverAccountYes();
				// 还款本金(已还)
				recoverCapitalYesAlready = borrowRecoverPlan.getRecoverCapitalYes();
				// 还款利息(已还)
				recoverInterestYesAlready = borrowRecoverPlan.getRecoverInterestYes();
				// 剩余待还款金额
				recoverAccountRemain = recoverAccount.subtract(recoverAccountYesAlready);
				// 剩余待还本金
				recoverCapitalRemain = recoverCapital.subtract(recoverCapitalYesAlready);
				// 剩余待还利息
				recoverInterestRemain = recoverInterest.subtract(recoverInterestYesAlready);
				// 剩余待还款金额
				recoverAccountRemainRepay = recoverAccount.subtract(recoverAccountYesAlready);
				// 剩余待还款本金
				recoverCapitalRemainRepay = recoverCapital.subtract(recoverCapitalYesAlready);
				// 剩余待还款利息
				recoverInterestRemainRepay = recoverInterest.subtract(recoverInterestYesAlready);
				// 借款人待还金额
				borrowUserRepayAccount = recoverAccount.subtract(recoverAccountYesAlready);
				if (borrowRecover.getCreditAmount().intValue() == 0) {
					// 逾期利息
					lateInterest = borrowRecoverPlan.getLateInterest();
					// 延期利息
					delayInterest = borrowRecoverPlan.getDelayInterest();
					// 提前还款少还利息
					chargeInterest = borrowRecoverPlan.getChargeInterest();
					// 总收入金额=还款金额+逾期利息+延期利息+提前还款少还利息(负数)
					recoverAccountRemainRepay = recoverAccountRemain.add(lateInterest).add(delayInterest).add(chargeInterest);
					// 剩余待还利息
					recoverInterestRemainRepay = recoverInterestRemain.add(lateInterest).add(delayInterest).add(chargeInterest);
				} else {
					recoverAccountRemainRepay = recoverAccountRemain;
					// 剩余待还利息
					recoverInterestRemainRepay = recoverInterestRemain;
				}
			} else {
				throw new Exception("分期还款计划表数据不存在。[借款编号：" + borrowNid + "]，" + "[出借订单号：" + tenderOrdId + "]，" + "[期数：" + periodNow + "]");
			}
		} else {// [endday: 按天计息, end:按月计息]
			borrowRecover = this.getBorrowRecover(borrowRecover.getId());
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
			// 还款金额(已还)
			recoverAccountYesAlready = borrowRecover.getRecoverAccountYes();
			// 还款本金(已还)
			recoverCapitalYesAlready = borrowRecover.getRecoverCapitalYes();
			// 还款利息(已还)
			recoverInterestYesAlready = borrowRecover.getRecoverInterestYes();
			// 剩余待还款金额
			recoverAccountRemain = recoverAccount.subtract(recoverAccountYesAlready);
			// 剩余待还本金
			recoverCapitalRemain = recoverCapital.subtract(recoverCapitalYesAlready);
			// 剩余待还利息
			recoverInterestRemain = recoverInterest.subtract(recoverInterestYesAlready);
			// 剩余待还款金额
			recoverAccountRemainRepay = recoverAccount.subtract(recoverAccountYesAlready);
			// 剩余待还款本金
			recoverCapitalRemainRepay = recoverCapital.subtract(recoverCapitalYesAlready);
			// 剩余待还款利息
			recoverInterestRemainRepay = recoverInterest.subtract(recoverInterestYesAlready);
			// 借款人待还金额
			borrowUserRepayAccount =  recoverAccount.subtract(recoverAccountYesAlready);
			if (borrowRecover.getCreditAmount().intValue() == 0) {
				// 逾期利息
				lateInterest = borrowRecover.getLateInterest();
				// 延期利息
				delayInterest = borrowRecover.getDelayInterest();
				// 提前还款少还利息
				chargeInterest = borrowRecover.getChargeInterest();
				// 总收入金额=还款金额+逾期利息+延期利息+提前还款少还利息(负数)
				recoverAccountRemainRepay = recoverAccountRemain.add(lateInterest).add(delayInterest).add(chargeInterest);
				// 剩余待还利息
				recoverInterestRemainRepay = recoverInterestRemain.add(lateInterest).add(delayInterest).add(chargeInterest);
			} else {
				recoverAccountRemainRepay = recoverAccountRemain;
				// 剩余待还利息
				recoverInterestRemainRepay = recoverInterestRemain;
			}
		}
		// 判断该收支明细是否存在时,跳出本次循环
		if (countAccountTenderListByNid(repayOrderId) > 0) {
			return retMsgList;
		}
		// 债转还款总金额
		BigDecimal creditRepayTotal = this.selectCreditRepayTotal(borrowRecover.getBorrowNid(), borrowRecover.getNid(), periodNow);
		// 债转已还款总额
		creditRepayTotal = creditRepayTotal.setScale(2, BigDecimal.ROUND_DOWN);
		// 已还款总额
		recoverAccountYesAlready = recoverAccountYesAlready.setScale(2, BigDecimal.ROUND_DOWN);
		// 判断已还款总额是否相等
		if (creditRepayTotal.compareTo(recoverAccountYesAlready) == 0) {
			// 债转剩余金额大于0
			if (recoverAccountRemainRepay.compareTo(BigDecimal.ZERO) > 0) {
				System.out.println("------未债转部分还款开始---项目编号：" + apicron.getBorrowNid() + "---------还款订单号：" + repayOrderId);
				// 管理费
				BigDecimal perManage = new BigDecimal("0");
				// 按月计息，到期还本还息end
				if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
					perManage = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(recoverCapitalRemainRepay, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
				}
				// 额本息month、等额本金principal
				else if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
					if (periodNow.intValue() == borrowPeriod.intValue()) {
						perManage = AccountManagementFeeUtils.getMonthAccountManagementFee(recoverCapitalRemainRepay, feeRate, borrowPeriod, differentialRate, 1, borrow.getAccount(), borrowPeriod, borrowVerifyTime);
					} else {
						perManage = AccountManagementFeeUtils.getMonthAccountManagementFee(recoverCapitalRemainRepay, feeRate, borrowPeriod, differentialRate, 0, borrow.getAccount(), borrowPeriod, borrowVerifyTime);
					}
				}
				// 先息后本endmonth
				else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
					if (periodNow.intValue() == borrowPeriod.intValue()) {
						perManage = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(borrowRecover.getRecoverCapital().subtract(borrowRecover.getCreditAmount()), feeRate, borrowPeriod, borrowPeriod, differentialRate, 1, borrowVerifyTime);
					} else {
						perManage = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(borrowRecover.getRecoverCapital().subtract(borrowRecover.getCreditAmount()), feeRate, borrowPeriod, borrowPeriod, differentialRate, 0, borrowVerifyTime);
					}
				}
				// 按天计息到期还本还息
				else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
					perManage = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(recoverCapitalRemainRepay, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
				}
				// 调用交易查询接口
				ChinapnrBean queryTransStatBean = queryTransStat(repayOrderId, repayOrderDate, "REPAYMENT");
				String respCode = queryTransStatBean == null ? "" : queryTransStatBean.getRespCode();
				// 调用接口失败时(000,422以外)
				if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode) && !ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(respCode)) {
					String message = queryTransStatBean == null ? "" : queryTransStatBean.getRespDesc();
					LogUtil.errorLog(THIS_CLASS, methodName, "调用交易查询接口失败。" + message + "，[出借订单号：" + tenderOrdId + "]", null);
					throw new Exception("调用交易查询接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
				}
				// 汇付交易状态
				String transStat = queryTransStatBean.getTransStat();
				// I:初始 P:部分成功
				if (ChinaPnrConstant.RESPCODE_NO_REPAY_RECORD.equals(respCode) || (!"I".equals(transStat) && !"P".equals(transStat))) {
					if (bankInputFlag == 1 || isRepayOrgFlag == 1) {
						// 分账账户串（当 管理费！=0 时是必填项）
						String divDetails = "";
						if (perManage.compareTo(BigDecimal.ZERO) > 0) {
							JSONArray ja = new JSONArray();
							JSONObject jo = new JSONObject();
							// 分账商户号(商户号,从配置文件中取得)
							jo.put(ChinaPnrConstant.PARAM_DIVCUSTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID));
							// 分账账户号(子账户号,从配置文件中取得)
							jo.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT03));
							// 分账金额
							jo.put(ChinaPnrConstant.PARAM_DIVAMT, perManage.toString());
							ja.add(jo);
							divDetails = ja.toString();
						}
						// 入参扩展域(3.0用)
						String reqExts = "";
						if (recoverCapitalRemainRepay.add(recoverInterestRemainRepay).compareTo(BigDecimal.ZERO) > 0) {
							// 调用汇付接口
							ChinapnrBean repaymentBean = repayment(borrowNid, repayUserid, String.valueOf(repayUserCustId), recoverCapitalRemainRepay.toString(), recoverInterestRemainRepay.toString(), perManage.toString(), repayOrderId, repayOrderDate, tenderOrdId,
									GetOrderIdUtils.getOrderDate(borrowTender.getAddtime()), String.valueOf(tenderUserCustId), divDetails, reqExts,bankInputFlag,isRepayOrgFlag,isApicronRepayOrgFlag,String.valueOf(borrowUserCustId));
							respCode = repaymentBean == null ? "" : repaymentBean.getRespCode();
							// 调用接口失败时(000以外)
							if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
								String message = repaymentBean == null ? "" : repaymentBean.getRespDesc();
								LogUtil.errorLog(THIS_CLASS, methodName, "调用自动扣款（还款）接口失败。" + message + "，[出借订单号：" + tenderOrdId + "]", null);
								throw new Exception("调用自动扣款（还款）接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
							}
						}
					} else {
						// 分账账户串（当 管理费！=0 时是必填项）
						String divDetails = "";
						if (perManage.compareTo(BigDecimal.ZERO) > 0) {
							JSONArray ja = new JSONArray();
							JSONObject jo = new JSONObject();
							// 分账账户号(子账户号,从配置文件中取得)
							jo.put(ChinaPnrConstant.PARAM_DIVACCTID, PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT03));
							// 分账金额
							jo.put(ChinaPnrConstant.PARAM_DIVAMT, perManage.toString());
							ja.add(jo);
							divDetails = ja.toString();
						}
						// 入参扩展域(3.0用)
						String reqExts = "";
						if (recoverAccountRemainRepay.compareTo(BigDecimal.ZERO) > 0) {
							// 调用汇付接口
							ChinapnrBean repaymentBean = repaymentOld(repayUserid, String.valueOf(repayUserCustId), recoverAccountRemainRepay.toString(), perManage.toString(), repayOrderId, repayOrderDate, tenderOrdId, GetOrderIdUtils.getOrderDate(borrowTender.getAddtime()),
									String.valueOf(tenderUserCustId), divDetails, reqExts);
							respCode = repaymentBean == null ? "" : repaymentBean.getRespCode();
							// 调用接口失败时(000以外)
							if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
								String message = repaymentBean == null ? "" : repaymentBean.getRespDesc();
								LogUtil.errorLog(THIS_CLASS, methodName, "调用自动扣款（还款）接口失败。" + message + "，[出借订单号：" + tenderOrdId + "]", null);
								throw new Exception("调用自动扣款（还款）接口失败。" + respCode + "：" + message + "，[出借订单号：" + tenderOrdId + "]");
							}
						}
					}
				}
				// 判断该收支明细是否存在时,跳出本次循环
				if (countAccountTenderListByNid(repayOrderId) == 0) {
					// 更新账户信息(出借人)
					Account account = new Account();
					// 出借人用户id
					account.setUserId(tenderUserId);
					// 出借人资金总额
					account.setTotal(lateInterest.add(delayInterest).add(chargeInterest));
					// 出借人可用余额
					account.setBalance(recoverAccountRemainRepay);
					// 出借人待收金额
					account.setAwait(recoverAccountRemain);
					// 更新出借人账户
					boolean accountTenderFlag = this.adminAccountCustomizeMapper.updateOfRepayTender(account) > 0 ? true : false;
					if (accountTenderFlag) {
						// 取得账户信息(出借人)
						account = this.getAccountByUserId(borrowTender.getUserId());
						if (account != null) {
							// 写入收支明细
							AccountList accountList = new AccountList();
							// 出借标识
							accountList.setNid(repayOrderId);
							// 出借人
							accountList.setUserId(tenderUserId);
							// 出借总收入
							accountList.setAmount(recoverAccountRemainRepay);
							// 1收入
							accountList.setType(1);
							// 出借成功
							accountList.setTrade("tender_recover_yes");
							// 余额操作
							accountList.setTradeCode("balance");
							// 出借人资金总额
							accountList.setTotal(account.getTotal());
							// 出借人可用金额
							accountList.setBalance(account.getBalance());
							// 出借人冻结金额
							accountList.setFrost(account.getFrost());
							// 汇添金冻结金额
							accountList.setPlanFrost(account.getPlanFrost());
							// 汇添金可用金额
							accountList.setPlanBalance(account.getPlanBalance());
							// 出借人待收金额
							accountList.setAwait(account.getAwait());
							// 利息
							accountList.setInterest(BigDecimal.ZERO);
							// 创建时间
							accountList.setCreateTime(nowTime);
							// 更新时间
							accountList.setBaseUpdate(nowTime);
							// 操作者
							accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY);
							// 出借还款,备注为项目编号
							accountList.setRemark(borrowNid);
							// 操作IP
							accountList.setIp(borrow.getAddip());
							// PC
							accountList.setWeb(0);
							accountList.setIsUpdate(0);
							accountList.setBaseUpdate(0);
							boolean tenderAccountListFlag = insertAccountList(accountList) > 0 ? true : false;
							if (tenderAccountListFlag) {
								// 更新还款明细表
								// 分期并且不是最后一期
								if (borrowRecoverPlan != null && Validator.isNotNull(periodNext) && periodNext > 0) {
									// 未还款
									borrowRecover.setRecoverStatus(0);
									// 取得分期还款计划表下一期的还款时间
									BorrowRecoverPlan borrowRecoverPlanNext = getBorrowRecoverPlan(borrowNid, periodNow + 1, tenderUserId, borrowRecover.getTenderId());
									borrowRecover.setRecoverTime(borrowRecoverPlanNext.getRecoverTime());
									borrowRecover.setRecoverType(TYPE_WAIT);
								} else {
									// 已还款
									borrowRecover.setRecoverStatus(1);
									// 实际还款时间
									borrowRecover.setRecoverYestime(String.valueOf(nowTime));
									borrowRecover.setRecoverTime(recoverTime);
									borrowRecover.setRecoverType(TYPE_YES);
								}
								// 分期时
								if (borrowRecoverPlan != null) {
									borrowRecover.setRecoverPeriod(periodNext);
								}
								borrowRecover.setWeb(2); // 写入网站收支
								borrowRecover.setRecoverAccountYes(borrowRecover.getRecoverAccountYes().add(recoverAccountRemainRepay));
								borrowRecover.setRecoverInterestYes(borrowRecover.getRecoverInterestYes().add(recoverInterestRemainRepay));
								borrowRecover.setRecoverCapitalYes(borrowRecover.getRecoverCapitalYes().add(recoverCapitalRemainRepay));
								borrowRecover.setRecoverAccountWait(borrowRecover.getRecoverAccountWait().subtract(recoverAccountRemain));
								borrowRecover.setRecoverInterestWait(borrowRecover.getRecoverInterestWait().subtract(recoverInterestRemain));
								borrowRecover.setRecoverCapitalWait(borrowRecover.getRecoverCapitalWait().subtract(recoverCapitalRemain));
								boolean tenderBorrowRecoverFlag = this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover) > 0 ? true : false;
								if (tenderBorrowRecoverFlag) {
									// 更新总的还款明细
									borrowRepay.setRepayAccountAll(borrowRepay.getRepayAccountAll().add(recoverAccountRemainRepay).add(perManage));
									borrowRepay.setRepayAccountYes(borrowRepay.getRepayAccountYes().add(recoverAccountRemainRepay));
									borrowRepay.setRepayInterestYes(borrowRepay.getRepayInterestYes().add(recoverInterestRemainRepay));
									borrowRepay.setRepayCapitalYes(borrowRepay.getRepayCapitalYes().add(recoverCapitalRemainRepay));
									borrowRepay.setChargeInterest(borrowRepay.getChargeInterest().add(chargeInterest));
									borrowRepay.setDelayInterest(borrowRepay.getDelayInterest().add(delayInterest));
									borrowRepay.setLateInterest(borrowRepay.getLateInterest().add(lateInterest));
									borrowRepay.setLateRepayDays(borrowRecover.getLateDays());
									borrowRepay.setChargeDays(borrowRecover.getChargeDays());
									borrowRepay.setDelayDays(borrowRecover.getDelayDays());
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
									boolean tenderBorrowRepayFlag = this.borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay) > 0 ? true : false;
									if (tenderBorrowRepayFlag) {
										if (isMonth) {
											// 分期时
											if (borrowRecoverPlan != null) {
												// 更新还款计划表
												borrowRecoverPlan.setRecoverStatus(1);
												borrowRecoverPlan.setRecoverYestime(String.valueOf(nowTime));
												borrowRecoverPlan.setRecoverAccountYes(borrowRecoverPlan.getRecoverAccountYes().add(recoverAccountRemainRepay));
												borrowRecoverPlan.setRecoverInterestYes(borrowRecoverPlan.getRecoverInterestYes().add(recoverInterestRemainRepay));
												borrowRecoverPlan.setRecoverCapitalYes(borrowRecoverPlan.getRecoverCapitalYes().add(recoverCapitalRemainRepay));
												borrowRecoverPlan.setRecoverAccountWait(borrowRecoverPlan.getRecoverAccountWait().subtract(recoverAccountRemain));
												borrowRecoverPlan.setRecoverCapitalWait(borrowRecoverPlan.getRecoverCapitalWait().subtract(recoverCapitalRemain));
												borrowRecoverPlan.setRecoverInterestWait(borrowRecoverPlan.getRecoverInterestWait().subtract(recoverInterestRemain));
												borrowRecoverPlan.setRecoverType(TYPE_YES);
												boolean tenderBorrowRecoverPlanFlag = this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(borrowRecoverPlan) > 0 ? true : false;
												if (tenderBorrowRecoverPlanFlag) {
													// 更新总的还款计划
													BorrowRepayPlan borrowRepayPlan = getBorrowRepayPlan(borrowNid, periodNow);
													if (borrowRepayPlan != null) {
														borrowRepayPlan.setRepayAccountAll(borrowRepayPlan.getRepayAccountAll().add(recoverAccountRemainRepay).add(perManage));
														borrowRepayPlan.setRepayAccountYes(borrowRepayPlan.getRepayAccountYes().add(recoverAccountRemainRepay));
														borrowRepayPlan.setRepayInterestYes(borrowRepayPlan.getRepayInterestYes().add(recoverInterestRemainRepay));
														borrowRepayPlan.setRepayCapitalYes(borrowRepayPlan.getRepayCapitalYes().add(recoverCapitalRemainRepay));
														borrowRepayPlan.setChargeInterest(borrowRepayPlan.getChargeInterest().add(chargeInterest));
														borrowRepayPlan.setDelayInterest(borrowRepayPlan.getDelayInterest().add(delayInterest));
														borrowRepayPlan.setLateInterest(borrowRepayPlan.getLateInterest().add(lateInterest));
														borrowRepayPlan.setLateRepayDays(borrowRecoverPlan.getLateDays());
														borrowRepayPlan.setChargeDays(borrowRecoverPlan.getChargeDays());
														borrowRepayPlan.setDelayDays(borrowRecoverPlan.getDelayDays());
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
														if (!borrowRepayPlanFlag) {
															throw new Exception("还款计划表(huiyingdai_borrow_repay_plan)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
														}
													} else {
														throw new Exception("还款计划表(huiyingdai_borrow_repay_plan)查询失败！" + "[出借订单号：" + tenderOrdId + "]");
													}
												} else {
													throw new Exception("还款计划表(huiyingdai_borrow_recover_plan)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
												}
											} else {
												throw new Exception("还款计划表(huiyingdai_borrow_recover_plan)查询失败！" + "[出借订单号：" + tenderOrdId + "]");
											}
										}
									} else {
										throw new Exception("总的还款明细表(huiyingdai_borrow_repay)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
									}
									// 机构还款时,更新借款人账户信息
									// 如果是机构垫付还款
									if(isRepayOrgFlag == 1 && isApicronRepayOrgFlag ==1){
										// 更新借款人的Account表的待还款金额
										// 取得账户信息(借款人)
										Account borrowUserAccount = this.getAccountByUserId(borrowUserId);
										Account newBorrowUserAccount = new Account();
										newBorrowUserAccount.setUserId(borrowUserAccount.getUserId());
										newBorrowUserAccount.setRepay(borrowUserRepayAccount);
										boolean borrowUserFlag = this.adminAccountCustomizeMapper.updateOfRepayBorrowUser(newBorrowUserAccount) > 0 ? true : false ;
										if(!borrowUserFlag){
											throw new Exception("垫付机构还款后,借款人待还金额更新失败！" + "[借款人用户ID：" + borrowUserId + "]");
										}
									}
									// 更新借款表
									borrow = getBorrow(borrowNid);
									BorrowWithBLOBs newBrrow = new BorrowWithBLOBs();
									newBrrow.setId(borrow.getId());
									BigDecimal borrowManager = borrow.getBorrowManager() == null ? BigDecimal.ZERO : new BigDecimal(borrow.getBorrowManager());
									newBrrow.setBorrowManager(borrowManager.add(perManage).toString());
									// 总还款利息
									newBrrow.setRepayAccountYes(borrow.getRepayAccountYes().add(recoverAccountRemainRepay));
									// 总还款利息
									newBrrow.setRepayAccountInterestYes(borrow.getRepayAccountInterestYes().add(recoverInterestRemainRepay));
									// 总还款本金
									newBrrow.setRepayAccountCapitalYes(borrow.getRepayAccountCapitalYes().add(recoverCapitalRemainRepay));
									// 未还款总额
									newBrrow.setRepayAccountWait(borrow.getRepayAccountWait().subtract(recoverAccountRemain));
									// 未还款利息
									newBrrow.setRepayAccountInterestWait(borrow.getRepayAccountInterestWait().subtract(recoverInterestRemain));
									// 未还款本金
									newBrrow.setRepayAccountCapitalWait(borrow.getRepayAccountCapitalWait().subtract(recoverCapitalRemain));
									newBrrow.setRepayFeeNormal(borrow.getRepayFeeNormal().add(perManage));
									boolean borrowFlag = this.borrowMapper.updateByPrimaryKeySelective(newBrrow) > 0 ? true : false;
									if (borrowFlag) {
										// 更新出借表
										borrowTender.setRecoverAccountYes(borrowTender.getRecoverAccountYes().add(recoverAccountRemainRepay));
										borrowTender.setRecoverAccountInterestYes(borrowTender.getRecoverAccountInterestYes().add(recoverInterestRemainRepay));
										borrowTender.setRecoverAccountCapitalYes(borrowTender.getRecoverAccountCapitalYes().add(recoverCapitalRemainRepay));
										borrowTender.setRecoverAccountWait(borrowTender.getRecoverAccountWait().subtract(recoverAccountRemain));
										borrowTender.setRecoverAccountInterestWait(borrowTender.getRecoverAccountInterestWait().subtract(recoverInterestRemain));
										borrowTender.setRecoverAccountCapitalWait(borrowTender.getRecoverAccountCapitalWait().subtract(recoverCapitalRemain));
										boolean borrowTenderFlag = borrowTenderMapper.updateByPrimaryKeySelective(borrowTender) > 0 ? true : false;
										if (borrowTenderFlag) {
											// 管理费大于0时,插入网站收支明细
											if (perManage.compareTo(BigDecimal.ZERO) > 0) {
												// 插入网站收支明细记录
												AccountWebList accountWebList = new AccountWebList();
												accountWebList.setOrdid(borrowTender.getNid() + "_" + periodNow);// 订单号
												accountWebList.setBorrowNid(borrowNid); // 出借编号
												accountWebList.setUserId(repayUserid); // 借款人
												accountWebList.setAmount(perManage); // 管理费
												accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
												accountWebList.setTrade(CustomConstants.TRADE_REPAYFEE); // 管理费
												accountWebList.setTradeType(CustomConstants.TRADE_REPAYFEE_NM); // 账户管理费
												accountWebList.setRemark(borrowNid); // 出借编号
												accountWebList.setCreateTime(nowTime);
												boolean accountWebFlag = insertAccountWebList(accountWebList) > 0 ? true : false;
												if (!accountWebFlag) {
													throw new Exception("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号：" + borrowTender.getNid() + "]");
												}
											}
											Map<String, String> msg = new HashMap<String, String>();
											// 债转还款结束
											msg.put(VAL_USERID, String.valueOf(borrowRecover.getUserId()));
											msg.put(VAL_AMOUNT, recoverAccountRemainRepay == null ? "0.00" : recoverAccountRemainRepay.toString());
											msg.put(VAL_PROFIT, recoverInterestRemainRepay == null ? "0.00" : recoverInterestRemainRepay.toString());
											msg.put(VAL_TITLE, borrowRecover.getBorrowNid());
											msg.put(VAL_CAPITAL, borrowRecover.getRecoverCapitalYes() == null ? "0.00" : borrowRecover.getRecoverCapitalYes().toString());
											msg.put(VAL_BORROWNID, borrowRecover.getBorrowNid());
											msg.put(VAL_INTEREST, borrowRecover.getRecoverInterestYes() == null ? "0.00" : borrowRecover.getRecoverInterestYes().toString());
											retMsgList.add(msg);
											System.out.println("-----------部分债转还款结束---" + apicron.getBorrowNid() + "---------" + repayOrderId);
										} else {
											throw new Exception("出借表(huiyingdai_borrow_tender)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
										}
									} else {
										throw new Exception("借款详情(huiyingdai_borrow)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
									}
								} else {
									throw new Exception("还款明细(huiyingdai_borrow_recover)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
								}
							} else {
								throw new Exception("收支明细(huiyingdai_account_list)写入失败！" + "[出借订单号：" + tenderOrdId + "]");
							}
						} else {
							throw new Exception("出借人账户信息不存在。[出借人ID：" + borrowTender.getUserId() + "]，" + "[出借订单号：" + tenderOrdId + "]");
						}
					} else {
						throw new Exception("承接人资金记录(huiyingdai_account)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
					}
				}
				System.out.println("------未债转部分还款结束---项目编号：" + apicron.getBorrowNid() + "---------还款订单号：" + repayOrderId);
			} else if (recoverAccountRemainRepay.compareTo(BigDecimal.ZERO) == 0) {
				System.out.println("------全部债转还款后续处理开始---" + apicron.getBorrowNid() + "---------" + repayOrderId);
				// 分期并且不是最后一期
				if (borrowRecoverPlan != null && Validator.isNotNull(periodNext) && periodNext > 0) {
					borrowRecover.setRecoverStatus(0); // 未还款
					// 取得分期还款计划表下一期的还款时间
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
				// 还款日期
				borrowRecover.setRepayOrddate(repayOrderDate);
				// 还款订单号
				borrowRecover.setRepayOrdid(repayOrderId);
				// 写入网站收支
				borrowRecover.setWeb(2);
				// 更新还款表
				boolean borrowRecoverFlag = this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover) > 0 ? true : false;
				if (borrowRecoverFlag) {
					// 用户是否提前还款
					borrowRepay.setAdvanceStatus(borrowRecover.getAdvanceStatus());
					borrowRepay.setDelayDays(borrowRecover.getDelayDays());
					borrowRepay.setLateRepayDays(borrowRecover.getLateDays());
					borrowRepay.setChargeDays(borrowRecover.getChargeDays());
					boolean borrowRepayFlag = this.borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay) > 0 ? true : false;
					if (borrowRepayFlag) {
						if (isMonth) {
							// 最后还款时间
							borrowRecoverPlan.setRecoverYestime(String.valueOf(!isMonth ? nowTime : 0));
							// 债转还款状态
							borrowRecoverPlan.setRecoverStatus(1);
							// 还款类型
							borrowRecoverPlan.setRecoverType("yes");
							// 还款日期
							borrowRecoverPlan.setRepayOrderDate(repayOrderDate);
							// 还款订单号
							borrowRecoverPlan.setRepayOrderId(repayOrderId);
							// 更新还款计划表
							boolean borrowRecoverPlanFlag = this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(borrowRecoverPlan) > 0 ? true : false;
							if (borrowRecoverPlanFlag) {
								// 更新总的还款计划
								BorrowRepayPlan borrowRepayPlan = getBorrowRepayPlan(borrowNid, periodNow);
								if (borrowRepayPlan != null) {
									borrowRepayPlan.setLateRepayDays(borrowRecoverPlan.getLateDays());
									borrowRepayPlan.setChargeDays(borrowRecoverPlan.getChargeDays());
									borrowRepayPlan.setDelayDays(borrowRecoverPlan.getDelayDays());
									// 用户是否提前还款
									borrowRepayPlan.setAdvanceStatus(borrowRecoverPlan.getAdvanceStatus());
									boolean borrowRepayPlanFlag = this.borrowRepayPlanMapper.updateByPrimaryKeySelective(borrowRepayPlan) > 0 ? true : false;
									if (!borrowRepayPlanFlag) {
										throw new Exception("还款计划表(huiyingdai_borrow_repay_plan)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
									}
									System.out.println("-----------全部债转还款后续处理结束---" + apicron.getBorrowNid() + "---------" + repayOrderId);
								} else {
									throw new Exception("还款计划表(huiyingdai_borrow_repay_plan)查询失败！" + "[出借订单号：" + tenderOrdId + "]");
								}
							} else {
								throw new Exception("还款明细(huiyingdai_borrow_recover_plan)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
							}
						}
					} else {
						throw new Exception("还款表(huiyingdai_borrow_repay)查询失败！" + "[出借订单号：" + tenderOrdId + "]");
					}
				} else {
					throw new Exception("还款明细(huiyingdai_borrow_recover)更新失败！" + "[出借订单号：" + tenderOrdId + "]");
				}
				System.out.println("------全部债转还款后续处理结束---" + apicron.getBorrowNid() + "---------还款订单号：" + repayOrderId);
			} else if (recoverAccountRemainRepay.compareTo(BigDecimal.ZERO) < 0) {
				// 给洪刚发送短信告诉剩余金额出现负数
				Map<String, String> replaceStrs = new HashMap<String, String>();
				replaceStrs.put("val_title", "项目编号：【" + borrow.getBorrowNid() + "】+订单号：【" + borrowRecover.getNid() + "】");
				replaceStrs.put("val_period", isMonth ? "第" + apicron.getPeriodNow() + "期" : "");
				replaceStrs.put("val_package_error", "债转还款后,剩余金额出现负数");
				SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_HUANKUAN_FAILD, CustomConstants.CHANNEL_TYPE_NORMAL);
				smsProcesser.gather(smsMessage);
				throw new Exception("债转还款后,剩余金额出现负数！" + "[出借订单号：" + tenderOrdId + "]");
			}
		} else {
			// 给洪刚发短信，告诉他还款金额不相等
			Map<String, String> replaceStrs = new HashMap<String, String>();
			replaceStrs.put("val_title", "项目编号：【" + borrow.getBorrowNid() + "】+订单号：【" + borrowRecover.getNid() + "】");
			replaceStrs.put("val_period", isMonth ? "第" + apicron.getPeriodNow() + "期" : "");
			replaceStrs.put("val_package_error", "债转还款金额不相等");
			SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_HUANKUAN_FAILD, CustomConstants.CHANNEL_TYPE_NORMAL);
			smsProcesser.gather(smsMessage);
			throw new Exception("债转还款金额不相等！" + "[出借订单号：" + tenderOrdId + "]");
		}
		System.out.println("------债转还款未承接部分结束---项目编号：" + apicron.getBorrowNid() + "---------还款订单号：" + repayOrderId);
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

		System.out.println("-----------债转还款完成，更新状态开始---" + borrowNid + "---------【还款期数】" + periodNow);
		// 当前时间
		int nowTime = GetDate.getNowTime10();

		// 查询recover
		BorrowRecoverExample recoverExample = new BorrowRecoverExample();
		recoverExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRecoverStatusEqualTo(0);
		int recoverCnt = this.borrowRecoverMapper.countByExample(recoverExample);
		// 项目详情
		Borrow borrow = getBorrow(borrowNid);
		// 还款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 剩余还款期数
		Integer periodNext = borrowPeriod - periodNow;
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle());

		String repayType = TYPE_WAIT;
		int repayStatus = 0;
		String repayYesTime = "0";
		// 如果还款全部完成
		if (recoverCnt == 0) {
			repayType = TYPE_WAIT_YES;
			repayStatus = 1;
			repayYesTime = String.valueOf(nowTime);
		}
		// 标的总表信息
		BorrowWithBLOBs newBrrow = new BorrowWithBLOBs();
		// 还款总表
		BorrowRepay newBorrowRepay = new BorrowRepay();
		newBorrowRepay.setRepayType(repayType);
		newBorrowRepay.setRepayStatus(repayStatus); // 已还款
		newBorrowRepay.setRepayDays("0");
		newBorrowRepay.setRepayStep(4);
		newBorrowRepay.setRepayPeriod(isMonth ? periodNext : 1);
		newBorrowRepay.setRepayActionTime(String.valueOf(nowTime));// 实际还款时间
		if (isMonth) {
			// 分期的场合，根据借款编号和还款期数从还款计划表中取得还款时间
			BorrowRepayPlanExample example = new BorrowRepayPlanExample();
			Criteria borrowCriteria = example.createCriteria();
			borrowCriteria.andBorrowNidEqualTo(borrowNid);
			borrowCriteria.andRepayPeriodEqualTo(periodNow + 1);
			List<BorrowRepayPlan> replayPlan = borrowRepayPlanMapper.selectByExample(example);
			if (replayPlan.size() > 0) {
				BorrowRepayPlan borrowRepayPlanNext = replayPlan.get(0);
				if (borrowRepayPlanNext != null) {
					// 取得下期还款时间
					String repayTime = borrowRepayPlanNext.getRepayTime();
					// 设置下期还款时间
					newBorrowRepay.setRepayTime(repayTime);
					// 设置下期还款时间
					newBrrow.setRepayNextTime(Integer.parseInt(repayTime));
				}
			} else {
				// 还款成功最后时间
				newBorrowRepay.setRepayYestime(repayYesTime);
			}
			// 更新相应的还款计划表
			BorrowRepayPlan borrowRepayPlan = getBorrowRepayPlan(borrowNid, periodNow);
			if (borrowRepayPlan != null) {
				borrowRepayPlan.setRepayType(TYPE_WAIT_YES);
				borrowRepayPlan.setRepayDays("0");
				borrowRepayPlan.setRepayStep(4);
				borrowRepayPlan.setRepayActionTime(String.valueOf(nowTime));
				borrowRepayPlan.setRepayStatus(1);
				borrowRepayPlan.setRepayYestime(String.valueOf(nowTime));
				this.borrowRepayPlanMapper.updateByPrimaryKeySelective(borrowRepayPlan);
			}
		} else {
			// 还款成功最后时间
			newBorrowRepay.setRepayYestime(repayYesTime);
		}
		// 更新BorrowRepay
		BorrowRepayExample repayExample = new BorrowRepayExample();
		repayExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayStatusEqualTo(0);
		this.borrowRepayMapper.updateByExampleSelective(newBorrowRepay, repayExample);

		// 更新Borrow
		newBrrow.setRepayFullStatus(repayStatus);
		BorrowExample borrowExample = new BorrowExample();
		borrowExample.createCriteria().andBorrowNidEqualTo(borrowNid);
		this.borrowMapper.updateByExampleSelective(newBrrow, borrowExample);
		System.out.println("-----------债转还款完成，更新状态完成---" + borrowNid + "---------【还款期数】" + periodNow);
	}

	private BigDecimal selectCreditRepayTotal(String borrowNid, String nid, Integer period) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		params.put("period", period);
		String sumStr = this.creditRepayCustomizeMapper.countCreditRepaySum(params);
		BigDecimal sum = StringUtils.isNotBlank(sumStr) ? new BigDecimal(sumStr) : new BigDecimal(0);
		return sum;
	}

	/**
	 * 查询相应的recover数据
	 * 
	 * @param id
	 * @return
	 */
	private BorrowRecover getBorrowRecover(Integer id) {
		BorrowRecover borrowRecover = this.borrowRecoverMapper.selectByPrimaryKey(id);
		return borrowRecover;
	}

	/**
	 * 自动扣款（还款）(调用汇付天下接口3.0)
	 *
	 * @return
	 */
	private ChinapnrBean repayment(String borrowNid, Integer borrowUserId, String outCustId, String recoverCapital, String recoverInterestAll, String fee, String ordId, String ordDate, String subOrdId, String subOrdDate, String inCustId, String divDetails, String reqExt,Integer bankInputFlag ,Integer isRepayOrgFlag,Integer isApicronRepayOrgFlag ,String borrowUserCustId) {
		String methodName = "repayment";

		// 调用汇付接口(自动扣款（还款）)
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_30); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_REPAYMENT); // 消息类型(必须)
		if(bankInputFlag == 1){
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
	 * 自动扣款（还款）(调用汇付天下接口3.0)
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
	public List<BorrowApicron> getBorrowApicronList(Integer apiType, Integer status, Integer creditStatus) {
		BorrowApicronExample example = new BorrowApicronExample();
		BorrowApicronExample.Criteria criteria = example.createCriteria();
		criteria.andRepayStatusEqualTo(status);
		criteria.andApiTypeEqualTo(apiType);
		criteria.andCreditRepayStatusEqualTo(creditStatus);
		example.setOrderByClause(" id asc ");
		List<BorrowApicron> list = this.borrowApicronMapper.selectByExample(example);
		return list;
	}

	/**
	 * 更新借款债转任务表
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	@Override
	public int updateBorrowApicron(Integer id, Integer status) {
		BorrowApicron record = new BorrowApicron();
		record.setId(id);
		record.setCreditRepayStatus(status);
		record.setUpdateTime(GetDate.getNowTime10());
		return this.borrowApicronMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 更新借款API任务表
	 *
	 * @return
	 */
	public int updateBorrowApicron(Integer id, Integer status, String data) {
		BorrowApicron record = new BorrowApicron();
		record.setId(id);
		record.setCreditRepayStatus(status);
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
		criteria.andCreditTimeNotEqualTo(0); // 包含债转
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
	private int countAccountCreditListByNid(String nid) {
		AccountListExample accountListExample = new AccountListExample();
		accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("credit_tender_recover_yes");
		return this.accountListMapper.countByExample(accountListExample);
	}

	/**
	 * 判断该收支明细是否存在
	 *
	 * @param accountList
	 * @return
	 */
	private int countAccountTenderListByNid(String nid) {
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
	 * 发送短信(优惠券还款成功)
	 *
	 * @param userId
	 */
	public void sendSmsCoupon(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(VAL_USERID)) && NumberUtils.isNumber(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
					if (users == null || Validator.isNull(users.getMobile()) || (users.getRecieveSms() != null && users.getRecieveSms() == 1)) {
						return;
					}
					SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_COUPON_PROFIT, CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				}
			}
		}
	}

	/**
	 * 根据项目原标号，债转编号查询相应的债转出借记录
	 * 
	 * @param borrowNid
	 * @param creditId
	 * @return
	 */
	@Override
	public List<CreditTender> selectCreditTenderList(String borrowNid, int creditNid) {
		CreditTenderExample example = new CreditTenderExample();
		CreditTenderExample.Criteria crt = example.createCriteria();
		crt.andBidNidEqualTo(borrowNid);
		crt.andCreditNidEqualTo(creditNid + "");
		crt.andStatusEqualTo(0);
		List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(example);
		return creditTenderList;
	}

	/**
	 * 未查询到债转标的出借记录，更新债转记录
	 * 
	 * @param creditId
	 * @param repayPeriod
	 * @param creditStatus
	 */
	@Override
	public void updateBorrowCredit(int creditNid, int repayPeriod, int creditStatus, int nowTime, int repayNextTime) {
		BorrowCreditExample example = new BorrowCreditExample();
		BorrowCreditExample.Criteria crt = example.createCriteria();
		crt.andCreditNidEqualTo(creditNid);
		crt.andRecoverPeriodEqualTo(repayPeriod);
		List<BorrowCredit> borrowCreditList = this.borrowCreditMapper.selectByExample(example);
		if (borrowCreditList != null && borrowCreditList.size() == 1) {
			BorrowCredit borrowCredit = borrowCreditList.get(0);
			borrowCredit.setCreditStatus(creditStatus);
			borrowCredit.setRecoverPeriod(repayPeriod + 1);
			// 债转最近还款时间
			borrowCredit.setCreditRepayLastTime(nowTime);
			// 债转最后还款时间
			borrowCredit.setCreditRepayYesTime(nowTime);
			// 债转下次还款时间
			borrowCredit.setCreditRepayNextTime(repayNextTime);
			this.borrowCreditMapper.updateByPrimaryKeySelective(borrowCredit);
		}
	}

	/**
	 * 查询债转的还款记录表
	 * 
	 * @param borrowNid
	 * @param userId
	 * @param assignNid
	 * @param assignNid2
	 * @param creditPeriod
	 * @param i
	 * @return
	 */
	@Override
	public CreditRepay selectCreditRepay(String borrowNid, int userId, String tenderOrdId, String assignNid, int period, int status) {
		CreditRepayExample example = new CreditRepayExample();
		CreditRepayExample.Criteria crt = example.createCriteria();
		crt.andBidNidEqualTo(borrowNid);
		crt.andUserIdEqualTo(userId);
		crt.andCreditTenderNidEqualTo(tenderOrdId);
		crt.andAssignNidEqualTo(assignNid);
		crt.andRecoverPeriodEqualTo(period);
		crt.andStatusEqualTo(status);
		List<CreditRepay> creditRepayList = this.creditRepayMapper.selectByExample(example);
		if (creditRepayList != null && creditRepayList.size() == 1) {
			return creditRepayList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<BorrowCredit> getBorrowCreditList(String borrowNid, int recoverPeriod, int status) {
		BorrowCreditExample example = new BorrowCreditExample();
		BorrowCreditExample.Criteria crt = example.createCriteria();
		crt.andBidNidEqualTo(borrowNid);
		crt.andRecoverPeriodEqualTo(recoverPeriod);
		crt.andCreditStatusNotEqualTo(status);
		List<BorrowCredit> borrowCreditList = borrowCreditMapper.selectByExample(example);
		return borrowCreditList;
	}

	/**
	 * 查询未还款的债转项目
	 * 
	 * @param borrowNid
	 * @param tenderOrdId
	 * @param i
	 * @param j
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<BorrowCredit> selectBorrowCreditList(String borrowNid, String tenderOrdId, int period, int status) {

		BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
		BorrowCreditExample.Criteria crt = borrowCreditExample.createCriteria();
		crt.andBidNidEqualTo(borrowNid);
		crt.andTenderNidEqualTo(tenderOrdId);
		crt.andRecoverPeriodEqualTo(period);
		crt.andCreditStatusNotEqualTo(status);
		List<BorrowCredit> borrowCredits = this.borrowCreditMapper.selectByExample(borrowCreditExample);
		return borrowCredits;
	}

	/**
	 * 查询分期还款数据
	 * 
	 * @param borrowNid
	 * @param periodNow
	 * @param tenderUserId
	 * @param nid
	 * @return
	 * @author Administrator
	 */

	@Override
	public BorrowRecoverPlan getBorrowRecoverPlan(String borrowNid, int periodNow, int tenderUserId, int tenderId) {

		BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
		BorrowRecoverPlanExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
		crt.andTenderIdEqualTo(tenderId);
		crt.andUserIdEqualTo(tenderUserId);
		crt.andRecoverPeriodEqualTo(periodNow);
		List<BorrowRecoverPlan> recoverPlanList = this.borrowRecoverPlanMapper.selectByExample(example);
		if (recoverPlanList != null && recoverPlanList.size() == 1) {
			return recoverPlanList.get(0);
		} else {
			return null;
		}

	}

	/**
	 * 更新债转还款总表
	 * 
	 * @param borrowCredit
	 * @return
	 * @author Administrator
	 */

	@Override
	public int updateBorrowCredit(BorrowCredit borrowCredit) {
		return this.borrowCreditMapper.updateByPrimaryKeySelective(borrowCredit);
	}

	/**
	 * 更新债转还款
	 * 
	 * @param creditRepay
	 * @return
	 */
	@Override
	public int updateCreditRepay(CreditRepay creditRepay) {
		return this.creditRepayMapper.updateByPrimaryKeySelective(creditRepay);
	}

	/**
	 * 更新还款信息
	 *
	 * @param record
	 * @return
	 */
	public int updateBorrowRecoverPlan(BorrowRecoverPlan recoder) {
		return this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(recoder);
	}

	/**
	 * 推送债转还款信息
	 * 
	 * @param msgList
	 * @author Administrator
	 */

	@Override
	public void sendCreditMessage(List<Map<String, String>> msgList) {
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
						AppMsMessage smsMessage = new AppMsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_CJZQHK);
						appMsProcesser.gather(smsMessage);
					}
				}
			}
		}
	}

	/**
	 * 推送还款信息
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
	 * 推送债转结束信息
	 * 
	 * @param borrowCredit
	 * @author Administrator
	 */

	@Override
	public void sendCreditEndMessage(BorrowCredit borrowCredit) {
		Map<String, String> msg = new HashMap<String, String>();
		int userId = borrowCredit.getCreditUserId();
		Users users = getUsersByUserId(userId);
		if (users == null) {
			return;
		} else {
			UsersInfo userInfo = this.getUsersInfoByUserId(userId);
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
			msg.put(VAL_AMOUNT, borrowCredit.getCreditCapitalAssigned().add(borrowCredit.getCreditInterestAssigned()).toString());
			msg.put(VAL_PROFIT, borrowCredit.getCreditInterestAssigned().toString());
			msg.put(VAL_TITLE, borrowCredit.getBidNid());
			AppMsMessage smsMessage = new AppMsMessage(userId, msg, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_SHOUDAOHUANKUAN);
			appMsProcesser.gather(smsMessage);
		}
	}

}

package com.hyjf.coupon.loans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.InterestInfo;
import com.hyjf.common.enums.utils.VipValueEnum;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.BorrowTenderCpnExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponRealTender;
import com.hyjf.mybatis.model.auto.CouponRealTenderExample;
import com.hyjf.mybatis.model.auto.CouponRecover;
import com.hyjf.mybatis.model.auto.CouponRecoverExample;
import com.hyjf.mybatis.model.auto.CouponTender;
import com.hyjf.mybatis.model.auto.CouponTenderExample;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhAccedeExample;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.VipInfo;
import com.hyjf.mybatis.model.auto.VipInfoExample;
import com.hyjf.mybatis.model.auto.VipUserTender;
import com.hyjf.mybatis.model.auto.VipUserTenderExample;
import com.hyjf.mybatis.model.auto.VipUserUpgrade;
import com.hyjf.mybatis.model.customize.apiweb.BorrowTenderInfoCustomize;

/**
 * 自动扣款(放款服务)
 * 
 * @author Administrator
 * 
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class CouponLoansServiceImpl extends BaseServiceImpl implements CouponLoansService {

	// private static final String THIS_CLASS =
	// CouponLoansServiceImpl.class.getName();
	Logger _log = LoggerFactory.getLogger(CouponLoansServiceImpl.class);
	/** 预期收益 */
	private static final String VAL_PROFIT = "val_profit";
	/** 用户ID */
	private static final String USERID = "userId";
	/** 出借金额 */
	private static final String VAL_AMOUNT = "val_amount";
	/** 优惠券出借 */
	private static final String COUPON_TYPE = "coupon_type";
	/** 优惠券出借订单编号 */
	private static final String TENDER_NID = "tender_nid";
	/** 优惠券面值 */
	private static final String VAL_COUPON_BALANCE = "val_coupon_balance";
	/** Y优惠券类型 */
	private static final String VAL_COUPON_TYPE = "val_coupon_type";

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	/**
	 * 直投类自动放款（优惠券）
	 * 
	 * @throws Exception
	 */
	public List<Map<String, String>> updateCouponRecover(BorrowTenderCpn borrowTenderCpn) throws Exception {
		_log.info("优惠券自动放款开始！----标的编号：" + borrowTenderCpn.getBorrowNid());

		List<Map<String, String>> retMsgList = new ArrayList<Map<String, String>>();

		/** 基本变量 */
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 借款编号
		String borrowNid = borrowTenderCpn.getBorrowNid();
		/** 标的基本数据 */
		// 取得标的详情
		BorrowWithBLOBs borrow = getBorrow(borrowNid);

		Map<String, String> msg = null;
		// 借款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 年利率
		BigDecimal borrowApr = borrow.getBorrowApr();
		// 借款成功时间
		Integer borrowSuccessTime = borrow.getBorrowSuccessTime();
		// 项目类型
		Integer projectType = borrow.getProjectType();
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);

		// 利息
		BigDecimal interestTender = BigDecimal.ZERO;
		// 本息总额
		BigDecimal allAccount = BigDecimal.ZERO;
		// 本金
		BigDecimal allCapital = BigDecimal.ZERO;

		BigDecimal sumInterest = BigDecimal.ZERO;
		Integer recoverPeriod = 1;
		Integer recoverTime = null;
		msg = new HashMap<String, String>();
		retMsgList.add(msg);

		// 出借订单号
		String ordId = borrowTenderCpn.getNid();
		CouponConfig couponConfig = this.getCouponConfig(ordId);
		if (couponConfig == null) {
			throw new RuntimeException("优惠券出借放款失败" + "[出借订单号：" + ordId + "]");
		}
		InterestInfo interestInfo = null;
		// 月利率(算出管理费用[上限])
		BigDecimal borrowMonthRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
		// 月利率(算出管理费用[下限])
		BigDecimal borrowManagerScaleEnd = Validator.isNull(borrow.getBorrowManagerScaleEnd()) ? BigDecimal.ZERO : new BigDecimal(
				borrow.getBorrowManagerScaleEnd());
		// 差异费率
		BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
		// 初审时间
		int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
		// 检查优惠券是否重复放款
		if (checkCouponRecoverFirst(ordId)) {
			return null;
		}

		// 体验金
		if (couponConfig.getCouponType() == 1) {
			String tenderNid = borrowTenderCpn.getNid();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("nid", tenderNid);
			// 取得体验金收益期限
			Integer couponProfitTime = this.borrowTenderInfoCustomizeMapper.getCouponProfitTime(paramMap);
			// 计算体验金收益
			BigDecimal interest = this.getInterestTYJ(borrowTenderCpn.getAccount(), borrowApr,couponProfitTime);
			// 体验金按项目期限还款
			if(couponConfig.getRepayTimeConfig()==1){
				// 计算利息
				interestInfo = CalculatesUtil.getInterestInfo(borrowTenderCpn.getAccount(), borrowPeriod, borrowApr, borrowStyle, borrowSuccessTime,
						borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate, borrowVerifyTime);

				// 体验金的项目如果是分期
				if(isMonth){
					List<InterestInfo> listMonthly = interestInfo.getListMonthly();
					// 取得最后一次分期的还款时间作为体验金的还款时间
					interestInfo.setRepayTime(listMonthly.get(listMonthly.size()-1).getRepayTime());
					// 体验金的还款期数是最后一期
					recoverPeriod = listMonthly.size();
				}
			}else{
				// 体验金按收益期限还款
				interestInfo = new InterestInfo();
				Integer repayTime = GetDate.countDate(borrowSuccessTime, 5, couponProfitTime);
				interestInfo.setRepayTime(repayTime);
			}

			// 体验金收益
			interestInfo.setRepayAccountInterest(interest);
			interestTender = interestInfo.getRepayAccountInterest(); // 利息
			
		} else if (couponConfig.getCouponType() == 2) {
			// 加息券
			// 计算利息
			interestInfo = CalculatesUtil.getInterestInfo(borrowTenderCpn.getAccount(), borrowPeriod, couponConfig.getCouponQuota(), borrowStyle,
					borrowSuccessTime, borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate, borrowVerifyTime);
			interestTender = interestInfo.getRepayAccountInterest(); // 利息
		} else if (couponConfig.getCouponType() == 3) {
			// 代金券
			// 计算利息
			interestInfo = CalculatesUtil.getInterestInfo(borrowTenderCpn.getAccount(), borrowPeriod, borrowApr, borrowStyle, borrowSuccessTime,
					borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate, borrowVerifyTime);
			// 本息总额
			allAccount = interestInfo.getRepayAccount();
			// 本金
			allCapital = interestInfo.getRepayAccountCapital();
			
			interestTender = borrowTenderCpn.getAccount().add(interestInfo.getRepayAccountInterest()); // 利息
		}
		if (interestInfo != null) {
			allAccount = allAccount.compareTo(BigDecimal.ZERO) == 0 ? interestTender : allAccount;
			recoverTime = interestInfo.getRepayTime(); // 估计还款时间
		}

		// 更新出借详情表
		BorrowTenderCpn newBorrowTender = new BorrowTenderCpn();
		newBorrowTender.setId(borrowTenderCpn.getId());
		// 待收总额（优惠券利息）
		newBorrowTender.setRecoverAccountWait(allAccount);
		// 收款总额（优惠券利息）
		newBorrowTender.setRecoverAccountAll(allAccount);
		// 待收利息（优惠券利息）
		newBorrowTender.setRecoverAccountInterestWait(interestTender);
		// 收款总利息（优惠券利息）
		newBorrowTender.setRecoverAccountInterest(interestTender);
		// 待收本金 (优惠券没有本金，设为0)
		newBorrowTender.setRecoverAccountCapitalWait(allCapital);
		// 放款金额（优惠券利息）
		newBorrowTender.setLoanAmount(allAccount);
		// 服务费（优惠券收益没有服务费）
		newBorrowTender.setLoanFee(BigDecimal.valueOf(0));
		// 状态 0，未放款，1，已放款
		newBorrowTender.setStatus(1);
		// 出借状态 0，未放款，1，已放款
		newBorrowTender.setTenderStatus(1);
		// 放款状态 0，未放款，1，已放款
		newBorrowTender.setApiStatus(1);
		// 放款订单号
		newBorrowTender.setLoanOrdid(borrowTenderCpn.getLoanOrdid());
		// 写入网站收支明细
		newBorrowTender.setWeb(2);
		int borrowTenderCnt2 = this.updateBorrowTenderCpn(newBorrowTender);
		if (borrowTenderCnt2 == 0) {
			throw new RuntimeException("出借详情(huiyingdai_borrow_tender)更新失败!" + "[出借订单号：" + ordId + "]");
		}
		// [principal: 等额本金, month:等额本息, month:等额本息, end:先息后本]
		if (isMonth && couponConfig.getCouponType() != 1) {
			// 作成分期还款计划
			if (interestInfo != null && interestInfo.getListMonthly() != null) {
				InterestInfo monthly = null;
				for (int j = 0; j < interestInfo.getListMonthly().size(); j++) {
					monthly = interestInfo.getListMonthly().get(j);
					CouponRecover cr = new CouponRecover();
					// 出借订单编号
					cr.setTenderId(borrowTenderCpn.getNid());
					// 还款状态（0:未还款，1：已还款）
					cr.setRecoverStatus(0);
					// 收益领取状态（1：未回款，2：未领取，4：转账失败，5：已领取，6：已过期）
					cr.setReceivedFlg(1);
					// 还款期数
					cr.setRecoverPeriod(j + 1);
					// 估计还款时间
					cr.setRecoverTime(monthly.getRepayTime());
					// 应还利息
					cr.setRecoverInterest(monthly.getRepayAccountInterest());
					if (couponConfig.getCouponType() == 3) {
						// 代金券
						// 应还本息
						cr.setRecoverAccount(monthly.getRepayAccount());
						// 应还本金
						cr.setRecoverCapital(monthly.getRepayAccountCapital());
						sumInterest = sumInterest.add(monthly.getRepayAccount());
					} else {
						// 体验金和加息券
						// 应还本息
						cr.setRecoverAccount(monthly.getRepayAccountInterest());
						// 应还本金
						cr.setRecoverCapital(BigDecimal.ZERO);
						sumInterest = sumInterest.add(monthly.getRepayAccountInterest());
					}

					// 是否已通知用户
					cr.setNoticeFlg(0);
					// 作成时间
					cr.setAddTime(nowTime);
					// 作成用户，系统自动（system）
					cr.setAddUser(CustomConstants.OPERATOR_AUTO_LOANS);
					// 更新时间
					cr.setUpdateTime(nowTime);
					// 更新用户 系统自动（system）
					cr.setUpdateUser(CustomConstants.OPERATOR_AUTO_LOANS);
					// 删除标识
					cr.setDelFlag(0);
					this.couponRecoverMapper.insertSelective(cr);
				}
			}
		} else {
			// 不分期还款的场合
			CouponRecover cr = new CouponRecover();
			// 出借订单编号
			cr.setTenderId(borrowTenderCpn.getNid());
			// 还款状态（0:未还款，1：已还款）
			cr.setRecoverStatus(0);
			// 收益领取状态（1：未回款，2：未领取，3：转账中,4：转账失败，5：已领取，6：已过期）
			cr.setReceivedFlg(1);
			// 还款期数
			cr.setRecoverPeriod(recoverPeriod);
			// 估计还款时间
			cr.setRecoverTime(recoverTime);
			// 应还利息
			cr.setRecoverInterest(interestTender);
			// 应还本息
			cr.setRecoverAccount(allAccount);
			sumInterest = allAccount;
			if (couponConfig.getCouponType() == 3) {
				// 代金券
				// 应还本金
				cr.setRecoverCapital(allCapital);
			} else {
				// 体验金和加息券
				// 应还本金
				cr.setRecoverCapital(BigDecimal.ZERO);
			}
			// 是否已通知用户
			cr.setNoticeFlg(0);
			// 作成时间
			cr.setAddTime(nowTime);
			// 作成用户，系统自动（system）
			cr.setAddUser(CustomConstants.OPERATOR_AUTO_LOANS);
			// 更新时间
			cr.setUpdateTime(nowTime);
			// 更新用户 系统自动（system）
			cr.setUpdateUser(CustomConstants.OPERATOR_AUTO_LOANS);
			// 删除标识
			cr.setDelFlag(0);
			cr.setCurrentRecoverFlg(1);
			// 还款类别：1：直投类，2：汇添金
			cr.setRecoverType(1);
			this.couponRecoverMapper.insertSelective(cr);
		}
		this.crRecoverPeriod(borrowTenderCpn.getNid(), 1, 1);
		// 更新账户信息(出借人)
		Account account = new Account();
		
		account.setUserId(borrowTenderCpn.getUserId());
		account.setBankTotal(allAccount);// 出借人资金总额 +利息
		account.setBankFrost(BigDecimal.ZERO);// 出借人冻结金额+出借金额(等额本金时出借金额可能会大于计算出的本金之和)
		account.setBankAwait(allAccount);// 出借人待收金额+利息+
                                                    // 本金
		account.setBankAwaitCapital(BigDecimal.ZERO);// 出借人待收本金
		account.setBankAwaitInterest(interestTender);// 出借人待收利息
		account.setBankInvestSum(BigDecimal.ZERO);// 出借人累计出借
		account.setBankFrostCash(BigDecimal.ZERO);// 江西银行冻结金额

		int accountCnt = this.adminAccountCustomizeMapper.updateOfLoansTender(account);
		if (accountCnt == 0) {
			throw new RuntimeException("出借人资金记录(huiyingdai_account)更新失败!" + "[出借订单号：" + ordId + "]");
		}
		// 取得账户信息(出借人)
		account = this.getAccountByUserId(borrowTenderCpn.getUserId());
		if (account == null) {
			throw new RuntimeException("出借人账户信息不存在。[出借人ID：" + borrowTenderCpn.getUserId() + "]，" + "[出借订单号：" + ordId + "]");
		}

		// 出借人编号
		msg.put(USERID, borrowTenderCpn.getUserId().toString());
		// 出借额
		msg.put(VAL_AMOUNT, allAccount.toString());
		// 代收收益
		msg.put(VAL_PROFIT, sumInterest.toString());
		// 优惠券类型
		msg.put(VAL_COUPON_TYPE, couponConfig.getCouponType() == 1 ? "体验金" : couponConfig.getCouponType() == 2 ? "加息券" : "代金券");
		// 优惠券面值
		msg.put(VAL_COUPON_BALANCE, couponConfig.getCouponType() == 1 ? couponConfig.getCouponQuota() + "元"
				: couponConfig.getCouponType() == 2 ? couponConfig.getCouponQuota() + "%" : couponConfig.getCouponQuota() + "元");
		// 出借订单编号
		msg.put(TENDER_NID, borrowTenderCpn.getNid());
		// 优惠券
		msg.put(COUPON_TYPE, "1");
		_log.info("优惠券自动放款结束！----标的编号：" + borrowTenderCpn.getBorrowNid());
		return retMsgList;

	}
	
	
	/**
	 * 汇计划自动放款（优惠券）
	 * 
	 * @throws Exception
	 */
	@Override
	public List<Map<String, String>> updateCouponRecoverHjh(BorrowTenderCpn borrowTenderCpn, String realOrderId) throws Exception {
		_log.info("优惠券自动放款开始！----标的编号：" + borrowTenderCpn.getBorrowNid());

		List<Map<String, String>> retMsgList = new ArrayList<Map<String, String>>();

		/** 基本变量 */
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		// 计划编号
		String planNid = borrowTenderCpn.getBorrowNid();
		/** 标的基本数据 */
		// 取得标的详情
//		BorrowWithBLOBs borrow = getBorrow(borrowNid);
		
		Integer borrowSuccessTime = GetDate.getNowTime10();
		HjhPlan hjhPlan = getHjhPlan(planNid);
		if(!StringUtils.isEmpty(realOrderId)){
			HjhAccede hjhAccede = getHjhAccede(realOrderId);
			// 借款成功时间
			borrowSuccessTime = hjhAccede.getCountInterestTime() == 0 ? GetDate.getNowTime10() : hjhAccede.getCountInterestTime();
		}

		Map<String, String> msg = null;
		// 借款期数
		Integer borrowPeriod = Validator.isNull(hjhPlan.getLockPeriod()) ? 1 : hjhPlan.getLockPeriod();
		// 还款方式
		String borrowStyle = hjhPlan.getBorrowStyle();
		
		//汇计划只支持按天和按月
		if(!borrowStyle.equals("endday")){
        	borrowStyle = "end";
        }
		// 年利率
		BigDecimal borrowApr = hjhPlan.getExpectApr();
		// 项目类型
		Integer projectType = 0;
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);

		// 利息
		BigDecimal interestTender = BigDecimal.ZERO;
		// 本息总额
		BigDecimal allAccount = BigDecimal.ZERO;
		// 本金
		BigDecimal allCapital = BigDecimal.ZERO;

		BigDecimal sumInterest = BigDecimal.ZERO;
		Integer recoverPeriod = 1;
		Integer recoverTime = null;
		msg = new HashMap<String, String>();
		retMsgList.add(msg);

		// 出借订单号
		String ordId = borrowTenderCpn.getNid();
		CouponConfig couponConfig = this.getCouponConfig(ordId);
		if (couponConfig == null) {
			throw new RuntimeException("优惠券出借放款失败" + "[出借订单号：" + ordId + "]");
		}
		InterestInfo interestInfo = null;
		// 月利率(算出管理费用[上限])
		BigDecimal borrowMonthRate = BigDecimal.ZERO;
		// 月利率(算出管理费用[下限])
		BigDecimal borrowManagerScaleEnd = BigDecimal.ZERO;
		// 差异费率
		BigDecimal differentialRate = BigDecimal.ZERO;
		// 初审时间
		int borrowVerifyTime = 0;
		// 检查优惠券是否重复放款
		if (checkCouponRecoverFirst(ordId)) {
			return null;
		}

		// 体验金
		if (couponConfig.getCouponType() == 1) {
			String tenderNid = borrowTenderCpn.getNid();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("nid", tenderNid);
			// 取得体验金收益期限
			Integer couponProfitTime = this.borrowTenderInfoCustomizeMapper.getCouponProfitTime(paramMap);
			// 计算体验金收益
			BigDecimal interest = this.getInterestTYJ(borrowTenderCpn.getAccount(), borrowApr,couponProfitTime);
			// 体验金按项目期限还款
			if(couponConfig.getRepayTimeConfig()==1){
				// 计算利息
				interestInfo = CalculatesUtil.getInterestInfo(borrowTenderCpn.getAccount(), borrowPeriod, borrowApr, borrowStyle, borrowSuccessTime,
						borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate, borrowVerifyTime);

				// 体验金的项目如果是分期
				if(isMonth){
					List<InterestInfo> listMonthly = interestInfo.getListMonthly();
					// 取得最后一次分期的还款时间作为体验金的还款时间
					interestInfo.setRepayTime(listMonthly.get(listMonthly.size()-1).getRepayTime());
					// 体验金的还款期数是最后一期
					recoverPeriod = listMonthly.size();
				}
			}else{
				// 体验金按收益期限还款
				interestInfo = new InterestInfo();
				Integer repayTime = GetDate.countDate(borrowSuccessTime, 5, couponProfitTime);
				interestInfo.setRepayTime(repayTime);
			}

			// 体验金收益
			interestInfo.setRepayAccountInterest(interest);
			interestTender = interestInfo.getRepayAccountInterest(); // 利息
			
		} else if (couponConfig.getCouponType() == 2) {
			// 加息券
			// 计算利息
			interestInfo = CalculatesUtil.getInterestInfo(borrowTenderCpn.getAccount(), borrowPeriod, couponConfig.getCouponQuota(), borrowStyle,
					borrowSuccessTime, borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate, borrowVerifyTime);
			interestTender = interestInfo.getRepayAccountInterest(); // 利息
		} else if (couponConfig.getCouponType() == 3) {
			// 代金券
			// 计算利息
			interestInfo = CalculatesUtil.getInterestInfo(borrowTenderCpn.getAccount(), borrowPeriod, borrowApr, borrowStyle, borrowSuccessTime,
					borrowMonthRate, borrowManagerScaleEnd, projectType, differentialRate, borrowVerifyTime);
			// 本息总额
			allAccount = interestInfo.getRepayAccount();
			// 本金
			allCapital = interestInfo.getRepayAccountCapital();
			
			interestTender = borrowTenderCpn.getAccount().add(interestInfo.getRepayAccountInterest()); // 利息
		}
		if (interestInfo != null) {
			allAccount = allAccount.compareTo(BigDecimal.ZERO) == 0 ? interestTender : allAccount;
			
			if(StringUtils.isEmpty(realOrderId)){
				recoverTime = interestInfo.getRepayTime();
			}else if(couponConfig.getCouponType() == 1 && couponConfig.getRepayTimeConfig()==2){
				recoverTime = interestInfo.getRepayTime();
			}else{
				recoverTime = interestInfo.getRepayTime(); // 估计还款时间
			}
		}

		// 更新出借详情表
		BorrowTenderCpn newBorrowTender = new BorrowTenderCpn();
		newBorrowTender.setId(borrowTenderCpn.getId());
		// 待收总额（优惠券利息）
		newBorrowTender.setRecoverAccountWait(allAccount);
		// 收款总额（优惠券利息）
		newBorrowTender.setRecoverAccountAll(allAccount);
		// 待收利息（优惠券利息）
		newBorrowTender.setRecoverAccountInterestWait(interestTender);
		// 收款总利息（优惠券利息）
		newBorrowTender.setRecoverAccountInterest(interestTender);
		// 待收本金 (优惠券没有本金，设为0)
		newBorrowTender.setRecoverAccountCapitalWait(allCapital);
		// 放款金额（优惠券利息）
		newBorrowTender.setLoanAmount(allAccount);
		// 服务费（优惠券收益没有服务费）
		newBorrowTender.setLoanFee(BigDecimal.valueOf(0));
		// 状态 0，未放款，1，已放款
		newBorrowTender.setStatus(1);
		// 出借状态 0，未放款，1，已放款
		newBorrowTender.setTenderStatus(1);
		// 放款状态 0，未放款，1，已放款
		newBorrowTender.setApiStatus(1);
		// 放款订单号
		newBorrowTender.setLoanOrdid(borrowTenderCpn.getLoanOrdid());
		// 写入网站收支明细
		newBorrowTender.setWeb(2);
		int borrowTenderCnt2 = this.updateBorrowTenderCpn(newBorrowTender);
		if (borrowTenderCnt2 == 0) {
			throw new RuntimeException("出借详情(huiyingdai_borrow_tender)更新失败!" + "[出借订单号：" + ordId + "]");
		}
		// [principal: 等额本金, month:等额本息, month:等额本息, end:先息后本]
		if (isMonth && couponConfig.getCouponType() != 1) {
			// 作成分期还款计划
			if (interestInfo != null && interestInfo.getListMonthly() != null) {
				InterestInfo monthly = null;
				for (int j = 0; j < interestInfo.getListMonthly().size(); j++) {
					monthly = interestInfo.getListMonthly().get(j);
					if(StringUtils.isEmpty(realOrderId)){
						recoverTime = monthly.getRepayTime();
					}else if(couponConfig.getCouponType() == 1 && couponConfig.getRepayTimeConfig()==2){
						recoverTime = monthly.getRepayTime();
					}else{
						recoverTime = monthly.getRepayTime(); // 估计还款时间
					}
					CouponRecover cr = new CouponRecover();
					// 出借订单编号
					cr.setTenderId(borrowTenderCpn.getNid());
					// 还款状态（0:未还款，1：已还款）
					cr.setRecoverStatus(0);
					// 收益领取状态（1：未回款，2：未领取，4：转账失败，5：已领取，6：已过期）
					cr.setReceivedFlg(1);
					// 还款期数
					cr.setRecoverPeriod(j + 1);
					// 估计还款时间
					cr.setRecoverTime(monthly.getRepayTime());
					// 应还利息
					cr.setRecoverInterest(monthly.getRepayAccountInterest());
					if (couponConfig.getCouponType() == 3) {
						// 代金券
						// 应还本息
						cr.setRecoverAccount(monthly.getRepayAccount());
						// 应还本金
						cr.setRecoverCapital(monthly.getRepayAccountCapital());
						sumInterest = sumInterest.add(monthly.getRepayAccount());
					} else {
						// 体验金和加息券
						// 应还本息
						cr.setRecoverAccount(monthly.getRepayAccountInterest());
						// 应还本金
						cr.setRecoverCapital(BigDecimal.ZERO);
						sumInterest = sumInterest.add(monthly.getRepayAccountInterest());
					}

					// 是否已通知用户
					cr.setNoticeFlg(0);
					// 作成时间
					cr.setAddTime(nowTime);
					// 作成用户，系统自动（system）
					cr.setAddUser(CustomConstants.OPERATOR_AUTO_LOANS);
					// 更新时间
					cr.setUpdateTime(nowTime);
					// 更新用户 系统自动（system）
					cr.setUpdateUser(CustomConstants.OPERATOR_AUTO_LOANS);
					// 删除标识
					cr.setDelFlag(0);
					this.couponRecoverMapper.insertSelective(cr);
				}
			}
		} else {
			// 不分期还款的场合
			CouponRecover cr = new CouponRecover();
			// 出借订单编号
			cr.setTenderId(borrowTenderCpn.getNid());
			// 还款状态（0:未还款，1：已还款）
			cr.setRecoverStatus(0);
			// 收益领取状态（1：未回款，2：未领取，3：转账中,4：转账失败，5：已领取，6：已过期）
			cr.setReceivedFlg(1);
			// 还款期数
			cr.setRecoverPeriod(recoverPeriod);
			// 估计还款时间
			cr.setRecoverTime(recoverTime);
			// 应还利息
			cr.setRecoverInterest(interestTender);
			// 应还本息
			cr.setRecoverAccount(allAccount);
			sumInterest = allAccount;
			if (couponConfig.getCouponType() == 3) {
				// 代金券
				// 应还本金
				cr.setRecoverCapital(allCapital);
			} else {
				// 体验金和加息券
				// 应还本金
				cr.setRecoverCapital(BigDecimal.ZERO);
			}
			// 是否已通知用户
			cr.setNoticeFlg(0);
			// 作成时间
			cr.setAddTime(nowTime);
			// 作成用户，系统自动（system）
			cr.setAddUser(CustomConstants.OPERATOR_AUTO_LOANS);
			// 更新时间
			cr.setUpdateTime(nowTime);
			// 更新用户 系统自动（system）
			cr.setUpdateUser(CustomConstants.OPERATOR_AUTO_LOANS);
			// 删除标识
			cr.setDelFlag(0);
			cr.setCurrentRecoverFlg(1);
			// 还款类别：1：直投类，2：汇添金
			cr.setRecoverType(1);
			this.couponRecoverMapper.insertSelective(cr);
		}
		this.crRecoverPeriod(borrowTenderCpn.getNid(), 1, 1);
		// 更新账户信息(出借人)
		Account account = new Account();
		
		account.setUserId(borrowTenderCpn.getUserId());
		account.setBankTotal(allAccount);// 出借人资金总额 +利息
                                                    // 本金
		account.setPlanInterestWait(interestTender);
		account.setPlanAccountWait(allAccount);

		int accountCnt = this.adminAccountCustomizeMapper.updateOfLoansTenderHjh(account);
		if (accountCnt == 0) {
			throw new RuntimeException("出借人资金记录(huiyingdai_account)更新失败!" + "[出借订单号：" + ordId + "]");
		}
		// 取得账户信息(出借人)
		account = this.getAccountByUserId(borrowTenderCpn.getUserId());
		if (account == null) {
			throw new RuntimeException("出借人账户信息不存在。[出借人ID：" + borrowTenderCpn.getUserId() + "]，" + "[出借订单号：" + ordId + "]");
		}

		// 出借人编号
		msg.put(USERID, borrowTenderCpn.getUserId().toString());
		// 出借额
		msg.put(VAL_AMOUNT, allAccount.toString());
		// 代收收益
		msg.put(VAL_PROFIT, sumInterest.toString());
		// 优惠券类型
		msg.put(VAL_COUPON_TYPE, couponConfig.getCouponType() == 1 ? "体验金" : couponConfig.getCouponType() == 2 ? "加息券" : "代金券");
		// 优惠券面值
		msg.put(VAL_COUPON_BALANCE, couponConfig.getCouponType() == 1 ? couponConfig.getCouponQuota() + "元"
				: couponConfig.getCouponType() == 2 ? couponConfig.getCouponQuota() + "%" : couponConfig.getCouponQuota() + "元");
		// 出借订单编号
		msg.put(TENDER_NID, borrowTenderCpn.getNid());
		// 优惠券
		msg.put(COUPON_TYPE, "1");
		_log.info("优惠券自动放款结束！----标的编号：" + borrowTenderCpn.getBorrowNid());
		return retMsgList;

	}

	

	/**
	 * 计算V值
	 */
	public void updateVipValue(CouponLoansBean borrowLoans) {
		int nowTime = GetDate.getNowTime10();
		UsersInfo userInfo = this.getUsersInfoByUserId(borrowLoans.getUserId());
		// 是否VIP
		boolean isVip = userInfo != null && userInfo.getVipId() != null ? true : false;
		// 是否累加V值
		boolean isAdd = false;
		BorrowTenderInfoCustomize borrowTenderInfo = null;
		if (isVip) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("nid", borrowLoans.getNid());
			paramMap.put("userId", borrowLoans.getUserId());
			borrowTenderInfo = borrowTenderInfoCustomizeMapper.getBorrowTenderInfo(paramMap);
			if (null != borrowTenderInfo) {
				isAdd = this.isAddVipValue(borrowTenderInfo.getProjectType(), borrowTenderInfo.getBorrowStyle());
			}
		}
		if (isAdd) {
			// 更新V值
			VipUserTender vt = new VipUserTender();
			BigDecimal vipValue = this.calculateVipValue(borrowTenderInfo.getTenderAccount(), borrowTenderInfo.getBorrowPeriod());
			int tenderVipValue = vipValue != null ? vipValue.intValue() : 0;
			// 用户的最新累计V值(vip终身制，暂不考虑vip过期的情况)
			int userSumVipValue = userInfo.getVipValue() + tenderVipValue;
			// 出借V值
			vt.setTenderVipValue(tenderVipValue);
			// 账户V值（出借V值+最大V值）
			vt.setSumVipValue(userSumVipValue);
			vt.setUpdateTime(nowTime);
			vt.setUpdateUser(CustomConstants.OPERATOR_AUTO_LOANS);
			VipUserTenderExample vte = new VipUserTenderExample();
			VipUserTenderExample.Criteria vtCriteria = vte.createCriteria();
			vtCriteria.andUserIdEqualTo(borrowLoans.getUserId());
			vtCriteria.andTenderNidEqualTo(borrowLoans.getNid());
			// 更新vip用户出借记录表
			boolean vipUserTenderFlag = this.vipUserTenderMapper.updateByExampleSelective(vt, vte) > 0 ? true : false;
			if (vipUserTenderFlag) {
				// 根据v值 取得其匹配的vip等级编号
				List<Integer> vipIdList = this.getVipIdByVipValue(userInfo.getVipId(), userSumVipValue);
				// vip等级有变化的场合
				for (int newVipId : vipIdList) {
					VipUserUpgrade vipUserUpgrade = new VipUserUpgrade();
					// 用户编号
					vipUserUpgrade.setUserId(userInfo.getUserId());
					// 新的vip等级编号
					vipUserUpgrade.setVipId(newVipId);
					// vip等级变化时的v值
					vipUserUpgrade.setUpgradeVipValue(userSumVipValue);
					// vip等级变化的类别（1：购买，2：V值升级）
					vipUserUpgrade.setUpgradeVipType(2);
					// 是否发放礼包
					vipUserUpgrade.setGiftFlg(0);
					vipUserUpgrade.setRemark("出借V值升级");
					vipUserUpgrade.setAddTime(nowTime);
					vipUserUpgrade.setAddUser(CustomConstants.OPERATOR_AUTO_LOANS);
					vipUserUpgrade.setUpdateTime(nowTime);
					vipUserUpgrade.setUpdateUser(CustomConstants.OPERATOR_AUTO_LOANS);
					vipUserUpgrade.setDelFlg(0);
					// 作成vip用户成长履历表
					this.vipUserUpgradeMapper.insertSelective(vipUserUpgrade);
				}
				if (vipIdList.size() > 0) {
					// 最新的用户VIP等级
					userInfo.setVipId(vipIdList.get(0));
					VipInfo vinfo = vipInfoMapper.selectByPrimaryKey(vipIdList.get(0));

					// 发送push消息
					_log.info("--------------会员等级升级push消息开始---------------");
					Map<String, String> param = new HashMap<String, String>();
					if (StringUtils.isEmpty(userInfo.getTruename())) {
						param.put("val_name", "");
					} else if (userInfo.getTruename().length() > 1) {
						param.put("val_name", userInfo.getTruename().substring(0, 1));
					} else {
						param.put("val_name", userInfo.getTruename());
					}

					if (userInfo.getSex() == 1) {
						param.put("val_sex", "先生");
					} else if (userInfo.getSex() == 2) {
						param.put("val_sex", "女士");
					} else {
						param.put("val_sex", "");
					}
					param.put("val_vip_grade", vinfo.getVipName());
					_log.info("请求参数userid：" + userInfo.getUserId() + " param: " + param);
					AppMsMessage appMsMessage = new AppMsMessage(userInfo.getUserId(), param, null, MessageDefine.APPMSSENDFORUSER,
							CustomConstants.JYTZ_VIP_UPGRADED);
					appMsProcesser.gather(appMsMessage);
					_log.info("--------------会员等级升级push消息结束---------------");
				}
				// 用户V值
				userInfo.setVipValue(userSumVipValue);
				// 更新用户表
				this.usersInfoMapper.updateByPrimaryKeySelective(userInfo);
			} else {
				_log.info("出借详情(hyjf_vip__tender)更新失败!" + "[出借订单号：" + borrowLoans.getNid() + "]");
			}
		}
	}

	/**
	 * 更新还款期
	 * 
	 * @param tenderNid
	 *            出借订单编号
	 * @param currentRecoverFlg
	 *            0:非还款期，1;还款期
	 * @param period
	 *            期数
	 */
	private void crRecoverPeriod(String tenderNid, int currentRecoverFlg, int period) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("currentRecoverFlg", currentRecoverFlg);
		paramMap.put("tenderNid", tenderNid);
		paramMap.put("period", period);
		this.couponRecoverCustomizeMapper.crRecoverPeriod(paramMap);

	}

	/**
	 * 校验是否重复放款
	 * 
	 * @param tenderNid
	 * @return
	 */
	private boolean checkCouponRecoverFirst(String tenderNid) {
		CouponRecoverExample checkExample = new CouponRecoverExample();
		checkExample.createCriteria().andTenderIdEqualTo(tenderNid);
		int count = this.couponRecoverMapper.countByExample(checkExample);
		return count > 0 ? true : false;
	}

	/**
	 * 取得借款列表(优惠券)
	 * 
	 * @return
	 */
	public List<BorrowTenderCpn> getBorrowTenderCpnList(String borrowNid) {
		BorrowTenderCpnExample example = new BorrowTenderCpnExample();
		BorrowTenderCpnExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		criteria.andApiStatusEqualTo(0);
		example.setOrderByClause(" id asc ");
		List<BorrowTenderCpn> list = this.borrowTenderCpnMapper.selectByExample(example);

		return list;
	}
	
	/**
	 * 
	 * 获取汇计划出借列表（优惠券）
	 * @author hsy
	 * @param orderId
	 * @return
	 */
	@Override
    public List<BorrowTenderCpn> getBorrowTenderCpnHjhList(String orderId) {
		if(StringUtils.isEmpty(orderId)){
			return new ArrayList<BorrowTenderCpn>();
		}
		
		CouponRealTenderExample realExample = new CouponRealTenderExample();
		CouponRealTenderExample.Criteria realCriteria = realExample.createCriteria();
		realCriteria.andRealTenderIdEqualTo(orderId);
		List<CouponRealTender> realTenderList = couponRealTenderMapper.selectByExample(realExample);
		if(realTenderList == null || realTenderList.isEmpty()){
			return new ArrayList<BorrowTenderCpn>();
		}
		
        BorrowTenderCpnExample example = new BorrowTenderCpnExample();
        BorrowTenderCpnExample.Criteria criteria = example.createCriteria();
        criteria.andNidEqualTo(realTenderList.get(0).getCouponTenderId());
        criteria.andApiStatusEqualTo(0);
        example.setOrderByClause(" id asc ");
        List<BorrowTenderCpn> list = this.borrowTenderCpnMapper.selectByExample(example);

        return list;
    }
	
	
	/**
	 * 优惠券单独出借时用
	 * @param couponOrderId
	 * @return
	 */
    @Override
	public List<BorrowTenderCpn> getBorrowTenderCpnHjhCouponOnlyList(String couponOrderId) {
		if(StringUtils.isEmpty(couponOrderId)){
			return new ArrayList<BorrowTenderCpn>();
		}
		
        BorrowTenderCpnExample example = new BorrowTenderCpnExample();
        BorrowTenderCpnExample.Criteria criteria = example.createCriteria();
        criteria.andNidEqualTo(couponOrderId);
        criteria.andApiStatusEqualTo(0);
        example.setOrderByClause(" id asc ");
        List<BorrowTenderCpn> list = this.borrowTenderCpnMapper.selectByExample(example);

        return list;
    }

	/**
	 * 取得标的详情
	 * 
	 * @return
	 */
	public BorrowWithBLOBs getBorrow(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<BorrowWithBLOBs> list = this.borrowMapper.selectByExampleWithBLOBs(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 更新放款状态(优惠券)
	 * 
	 * @param accountList
	 * @return
	 */
	public int updateBorrowTenderCpn(BorrowTenderCpn borrowTenderCpn) {
		return borrowTenderCpnMapper.updateByPrimaryKeySelective(borrowTenderCpn);
	}

	/**
	 * 发送短信(优惠券出借成功)
	 * 
	 * @param userId
	 */
	@Override
	public void sendSmsCoupon(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))
						&& new BigDecimal(msg.get(VAL_AMOUNT)).compareTo(BigDecimal.ZERO) > 0) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(USERID)));
					if (users == null || Validator.isNull(users.getMobile()) || (users.getInvestSms() != null && users.getInvestSms() == 1)) {
						return;
					}
					SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null,
							CustomConstants.PARAM_TPL_COUPON_TENDER, CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				}
			}
		}
	}

	/**
	 * 
	 * App消息推送（优惠券出借成功）
	 * 
	 * @author hsy
	 * @param msgList
	 */
	@Override
	public void sendAppMSCoupon(List<Map<String, String>> msgList) {
		System.out.println("msgList size: " + msgList.size());
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(USERID)) && Validator.isNotNull(msg.get(VAL_PROFIT))
						&& new BigDecimal(msg.get(VAL_PROFIT)).compareTo(BigDecimal.ZERO) > 0) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(USERID)));
					if (users == null) {
						System.out.println("不满足发送push消息条件，推送失败");
						return;
					}
					System.out.println("开始调用推送消息接口");
					AppMsMessage appMsMessage = new AppMsMessage(users.getUserId(), msg, null, MessageDefine.APPMSSENDFORUSER,
							CustomConstants.JYTZ_COUPON_TENDER);
					appMsProcesser.gather(appMsMessage);
				}
			}
		}
	}

	/**
	 * 根据出借订单编号，检查该出借是否优惠券出借
	 */
	@Override
	public boolean checkCouponTender(String tenderNid) {
		CouponTenderExample example = new CouponTenderExample();
		example.createCriteria().andOrderIdEqualTo(tenderNid);
		List<CouponTender> tenderList = this.couponTenderMapper.selectByExample(example);
		return tenderList != null && tenderList.size() > 0 ? true : false;
	}

	/**
	 * 根据优惠券出借订单编号，取得优惠券信息
	 * 
	 * @param tenderNid
	 * @return
	 */
	private CouponConfig getCouponConfig(String tenderNid) {
		return this.couponTenderCustomizeMapper.getCouponConfig(tenderNid);
	}

	private BigDecimal calculateVipValue(BigDecimal account, int borrowPeriod) {
		BigDecimal VipValue = null;
		BigDecimal vipValueRate = VipValueEnum.getName(borrowPeriod);
		if (vipValueRate != null) {
			VipValue = vipValueRate.multiply(account).setScale(0, BigDecimal.ROUND_DOWN);
		}
		return VipValue;
	}

	/**
	 * 根据v值 取得其匹配的vip等级编号
	 * 
	 * @param vipValue
	 * @return
	 */
	private List<Integer> getVipIdByVipValue(int oldVipId, int newVipValue) {
		List<Integer> vipIdList = new ArrayList<Integer>();
		VipInfo oldVipInfo = this.vipInfoMapper.selectByPrimaryKey(oldVipId);
		int oldVipIevel = oldVipInfo.getVipLevel();

		VipInfoExample example = new VipInfoExample();
		example.createCriteria().andVipValueLessThanOrEqualTo(newVipValue);
		example.setOrderByClause("vip_value desc");
		List<VipInfo> vipInfoList = this.vipInfoMapper.selectByExample(example);

		for (VipInfo info : vipInfoList) {
			if (info.getVipLevel() > oldVipIevel) {
				vipIdList.add(info.getId());
			}
		}
		return vipIdList;
	}

	/**
	 * 是否可累计V值的标的
	 */
	private boolean isAddVipValue(Integer borrowCd, String borrowStyle) {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		example.createCriteria().andBorrowCdEqualTo(borrowCd.toString());
		List<BorrowProjectType> projectTypeList = this.borrowProjectTypeMapper.selectByExample(example);
		if (projectTypeList != null && projectTypeList.size() > 0) {
			BorrowProjectType borrowProjectType = projectTypeList.get(0);
			String type = borrowProjectType.getBorrowProjectType();
			// 汇直投的标（新手汇、尊享汇、天标除外）
			if (StringUtils.equals(type, CustomConstants.HZT) && !StringUtils.equals(borrowProjectType.getBorrowClass(), CustomConstants.NEW)
					&& !StringUtils.equals(borrowProjectType.getBorrowClass(), CustomConstants.ZXH)
					&& !StringUtils.equals(borrowStyle, CustomConstants.BORROW_STYLE_ENDDAY)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 取出账户信息
	 * 
	 * @param userId
	 * @return
	 */
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
	 * 计算体验金收益
	 * 
	 * @param borrowTenderCpn
	 * @param borrowApr
	 * @return
	 */
	private BigDecimal getInterestTYJ(BigDecimal account, BigDecimal borrowApr,Integer couponProfitTime) {
		BigDecimal interest = BigDecimal.ZERO;
		// 保留两位小数（去位）
		// 应回款=体验金面值*出借标的年化/365*收益期限（体验金发行配置）
		interest = account.multiply(borrowApr.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP))
				.divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(couponProfitTime))
				.setScale(2, BigDecimal.ROUND_DOWN);
		return interest;
	}
	
	/**
	 * 取得计划详情
	 * @param planNid
	 * @return
	 */
    public HjhPlan getHjhPlan(String planNid) {
        HjhPlanExample example=new HjhPlanExample();
        example.createCriteria().andPlanNidEqualTo(planNid);
        List<HjhPlan> list=hjhPlanMapper.selectByExample(example);
        
        if(list !=null && !list.isEmpty()){
        	return list.get(0);
        	
        }
        
        return null;
    }
    
    /**
     * 取得计划加入记录
     * @param orderId
     * @return
     */
    public HjhAccede getHjhAccede(String orderId){
    	HjhAccedeExample example = new HjhAccedeExample();
    	example.createCriteria().andAccedeOrderIdEqualTo(orderId);
    	List<HjhAccede> accedeList = hjhAccedeMapper.selectByExample(example);
    	
    	if(accedeList !=null && !accedeList.isEmpty()){
        	return accedeList.get(0);
        	
        }
        
        return null;
    }
}

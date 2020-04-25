package com.hyjf.coupon.repay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CreateUUID;
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
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.BankMerchantAccount;
import com.hyjf.mybatis.model.auto.BankMerchantAccountExample;
import com.hyjf.mybatis.model.auto.BankMerchantAccountList;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.BorrowTenderCpnExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CouponRecover;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.TransferExceptionLog;
import com.hyjf.mybatis.model.auto.TransferExceptionLogExample;
import com.hyjf.mybatis.model.auto.TransferExceptionLogWithBLOBs;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service("repayService")
public class CouponRepayServiceImpl extends BaseServiceImpl implements CouponRepayService {
	private static final String THIS_CLASS = CouponRepayServiceImpl.class.getName();
	Logger _log = LoggerFactory.getLogger(CouponRepayServiceImpl.class);
	/** 用户ID */
	private static final String USERID = "userId";
	/** 还款金额(优惠券用) */
	private static final String VAL_AMOUNT = "val_amount";
	/** 优惠券类型 */
	private static final String VAL_COUPON_TYPE = "val_coupon_type";
	/** 优惠券还款类别 1：直投类 */
	private static final Integer RECOVER_TYPE_HZT = 1;
	/** 优惠券还款类别 2：汇添金 */
	private static final Integer RECOVER_TYPE_HTJ = 2;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("appMsProcesser")
	private MessageProcesser appMsProcesser;

	@Override
	public List<CouponTenderCustomize> getCouponTenderList(String borrowNid) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("borrowNid", borrowNid);
		// 1 项目到期还款  2 收益期限到期还款
		paramMap.put("repayTimeConfig", 1);
		return this.couponRecoverCustomizeMapper.selectCouponRecoverAll(paramMap);
	}
	
	/**
	 * 汇计划优惠券还款查询
	 */
	@Override
    public List<CouponTenderCustomize> getCouponTenderListHjh(String orderId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId);
        // 1 项目到期还款  2 收益期限到期还款
        paramMap.put("repayTimeConfig", 1);
        return this.couponRecoverCustomizeMapper.selectCouponRecoverHjh(paramMap);
    }

	/**
	 * 直投类优惠券还款
	 */
	@Override
	public void updateCouponRecoverMoney(String borrowNid, Integer periodNow, CouponTenderCustomize ct) throws Exception {
		String methodName = "updateCouponRecoverMoney";
		List<Map<String, String>> retMsgList = new ArrayList<Map<String, String>>();
		Map<String, String> msg = new HashMap<String, String>();
		retMsgList.add(msg);
		/** 基本变量 */
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		/** 标的基本数据 */
		// 取得借款详情
		BorrowWithBLOBs borrow = getBorrow(borrowNid);
		// 还款期数
		Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 剩余还款期数
		Integer periodNext = borrowPeriod - periodNow;
		// 还款方式
		String borrowStyle = borrow.getBorrowStyle();
		// 出借人用户ID
		Integer tenderUserId = null;
		// 出借人用户ID
		tenderUserId = Integer.valueOf(ct.getUserId());
		String couponTenderNid = ct.getOrderId();
		// 取得优惠券出借信息
		BorrowTenderCpn borrowTenderCpn = this.getCouponTenderInfo(couponTenderNid);
		// 出借人在银行存管的账户信息
		BankOpenAccount bankOpenAccountInfo = this.getBankOpenAccount(tenderUserId);
		if (bankOpenAccountInfo == null) {
			throw new Exception("出借人未开户。[用户ID：" + tenderUserId + "]，" + "[优惠券出借编号：" + couponTenderNid + "]");
		}
		
		// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
		// 当前还款
		CouponRecoverCustomize currentRecover = null;
		// [principal: 等额本金, month:等额本息, month:等额本息, endmonth:先息后本]
		if (isMonth) {
			// 取得分期还款
			currentRecover = this.getCurrentCouponRecover(couponTenderNid, periodNow);
			
		} else {// [endday: 按天计息, end:按月计息]
			currentRecover = this.getCurrentCouponRecover(couponTenderNid, 1);

		}
		if (currentRecover == null) {
			_log.info("优惠券还款数据不存在。[借款编号：" + borrowNid + "]，" + "[优惠券出借编号：" + couponTenderNid + "]");
			//throw new Exception("优惠券还款数据不存在。[借款编号：" + borrowNid + "]，" + "[优惠券出借编号：" + couponTenderNid + "]");
			return;
		}
		// 应还利息
		String recoverInterestStr = StringUtils.isEmpty(currentRecover.getRecoverInterest()) ? "0.00" : currentRecover.getRecoverInterest();
		// 应还本息
		String recoverAccountStr = StringUtils.isEmpty(currentRecover.getRecoverAccount()) ? "0.00" : currentRecover.getRecoverAccount();
		// 应还本金
		String recoverCapitalStr = StringUtils.isEmpty(currentRecover.getRecoverCapital()) ? "0.00" : currentRecover.getRecoverCapital();
		BigDecimal recoverInterest = new BigDecimal(recoverInterestStr);
		BigDecimal recoverAccount = new BigDecimal(recoverAccountStr);
		BigDecimal recoverCapital = new BigDecimal(recoverCapitalStr);
		CouponRecover cr = new CouponRecover();
		cr.setId(currentRecover.getId());
		String orderId = GetOrderIdUtils.getOrderId2(tenderUserId);
		String seqNo = GetOrderIdUtils.getSeqNo(6);
		BankCallBean resultBean = null;
		BankCallBean bean = new BankCallBean();
		if(new BigDecimal(recoverAccountStr).compareTo(BigDecimal.ZERO) != 0){
			
			String merrpAccount = PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT);
			
            bean.setVersion(BankCallConstant.VERSION_10);// 版本号
            bean.setTxCode(BankCallMethodConstant.TXCODE_VOUCHER_PAY);// 交易代码
            bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
            bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
            bean.setSeqNo(seqNo);// 交易流水号
            bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
            bean.setAccountId(merrpAccount);// 电子账号
            bean.setTxAmount(CustomUtil.formatAmount(recoverAccountStr));
            bean.setForAccountId(bankOpenAccountInfo.getAccount());
            bean.setDesLineFlag("1");
            bean.setDesLine(orderId);
            bean.setLogOrderId(orderId);// 订单号
            bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
            bean.setLogUserId(String.valueOf(tenderUserId));
            bean.setLogClient(0);// 平台

            int transferStatus = Integer.MIN_VALUE;
            try {
                resultBean = BankCallUtils.callApiBg(bean);
                if (resultBean == null || !BankCallConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
                    _log.info("转账失败..."+"[优惠券出借编号：" + couponTenderNid + "]");
                    cr.setReceivedFlg(4);
                    this.couponRecoverMapper.updateByPrimaryKeySelective(cr);
                    String errorMsg = StringUtils.EMPTY;
                    int type = 1;
                    if (currentRecover.getCouponType() == 1) {
                        errorMsg = "体验金收益自动领取失败";
                        type = 0;
                    }else if (currentRecover.getCouponType() == 2) {
                        errorMsg = "加息券收益自动领取失败";
                    } else if (currentRecover.getCouponType() == 3) {
                        errorMsg = "代金券收益自动领取失败";
                        type = 2;
                    }
                    
                    if(resultBean != null && Validator.isNotNull(resultBean.getRetMsg())){
                        errorMsg = resultBean.getRetMsg();

                    }
                    
                    if(resultBean != null && (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(resultBean.getRetCode()) || BankCallConstant.RETCODE_YUE_FAIL.equals(resultBean.getRetCode()))){
                        errorMsg = "商户子账户余额不足，请先充值或向该子账户转账";
                        sendSmsFail(currentRecover.getCouponType());
                    }
                    
                    transferStatus = 1;
                    // 插入转账异常
                    this.insertTransferExceptionLog(bean, resultBean, tenderUserId, recoverAccount, bankOpenAccountInfo.getAccount(), cr.getId(), transferStatus,
                            errorMsg, type);
                    LogUtil.errorLog(THIS_CLASS, methodName, new Exception("转账失败！失败数据已插入转账异常表。errormsg:"+errorMsg));
                    return;
                }
                // 转账失败
                
            } catch (Exception e) {
                LogUtil.errorLog(THIS_CLASS, methodName, "系统发生异常，更新异常转账表失败,事物回滚", e);
                throw new RuntimeException("系统发生异常，更新异常转账表失败,事物回滚!" + "[优惠券出借编号：" + couponTenderNid + "]");
            }
		}
		
		// 判断该收支明细是否存在时,跳出本次循环
		if (countAccountListByNidCoupon(orderId) == 0) {
			// 更新账户信息(出借人)
			Account account = new Account();
			account.setUserId(tenderUserId);
			
			account.setBankBalance(recoverAccount); // 账户余额
		    account.setBankTotal(BigDecimal.ZERO);// 出借人资金总额 +利息
	        account.setBankFrost(BigDecimal.ZERO);// 出借人冻结金额+出借金额(等额本金时出借金额可能会大于计算出的本金之和)
	        account.setBankAwait(recoverAccount);// 出借人待收金额+利息+
	                                                    // 本金
	        account.setBankAwaitCapital(BigDecimal.ZERO);// 出借人待收本金
	        account.setBankAwaitInterest(recoverAccount);// 出借人待收利息
	        account.setBankInterestSum(recoverAccount);
	        account.setBankInvestSum(BigDecimal.ZERO);// 出借人累计出借
	        account.setBankFrostCash(BigDecimal.ZERO);// 江西银行冻结金额

			int accountCnt = this.adminAccountCustomizeMapper.updateOfRepayTender(account);
			if (accountCnt == 0) {
				throw new RuntimeException("出借人资金记录(huiyingdai_account)更新失败！" + "[优惠券出借编号：" + couponTenderNid + "]");
			}
			// 取得账户信息(出借人)
			account = this.getAccountByUserId(tenderUserId);
			if (account == null) {
				throw new RuntimeException("出借人账户信息不存在。[出借人ID：" + borrowTenderCpn.getUserId() + "]，" + "[优惠券出借编号：" + couponTenderNid + "]");
			}
			// 写入收支明细
			AccountList accountList = new AccountList();
			// 转账订单编号
			accountList.setNid(orderId);
			// 出借人
			accountList.setUserId(tenderUserId);
            accountList.setBankAwait(account.getBankAwait());
            accountList.setBankAwaitCapital(account.getBankAwaitCapital());
            accountList.setBankAwaitInterest(account.getBankAwaitInterest());
            accountList.setBankBalance(account.getBankBalance());
            accountList.setBankFrost(account.getBankFrost());
            accountList.setBankInterestSum(account.getBankInterestSum());
            accountList.setBankTotal(account.getBankTotal());
            accountList.setBankWaitCapital(account.getBankWaitCapital());
            accountList.setBankWaitInterest(account.getBankWaitInterest());
            accountList.setBankWaitRepay(account.getBankWaitRepay());
            accountList.setPlanBalance(account.getPlanBalance());//汇计划账户可用余额
			accountList.setPlanFrost(account.getPlanFrost());
            accountList.setAccountId(bankOpenAccountInfo.getAccount());
            accountList.setTxDate(Integer.parseInt(GetOrderIdUtils.getTxDate()));
            accountList.setTxTime(Integer.parseInt(GetOrderIdUtils.getTxTime()));
            accountList.setSeqNo(seqNo);
            accountList.setBankSeqNo(bean.getTxDate() + bean.getTxTime() + bean.getSeqNo());
            accountList.setCheckStatus(0);
            accountList.setTradeStatus(1);
            accountList.setIsBank(1);
			
			// 出借收入
			accountList.setAmount(recoverAccount);
			// 1收入
			accountList.setType(1);
			String trade = StringUtils.EMPTY;
			if (currentRecover.getCouponType() == 1) {
				trade = "experience_profit";
			} else if (currentRecover.getCouponType() == 2) {
				trade = "increase_interest_profit";
			} else if (currentRecover.getCouponType() == 3) {
				trade = "cash_coupon_profit";
			}
			// 还款成功
			accountList.setTrade(trade);
			// 余额操作
			accountList.setTradeCode("balance");
			// 出借人资金总额
			accountList.setTotal(account.getTotal());
			// 出借人可用金额
			accountList.setBalance(account.getBalance());
			// 出借人冻结金额
			accountList.setFrost(account.getFrost());
			// 出借人待收金额
			accountList.setAwait(account.getAwait());
			// 创建时间
			accountList.setCreateTime(nowTime);
			// 更新时间
//			accountList.setBaseUpdate(nowTime);
			// 操作者
			accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY);
			String remark = StringUtils.EMPTY;
			if (currentRecover.getCouponType() == 1) {
				remark = "体验金：" + ct.getCouponUserCode();
			} else if (currentRecover.getCouponType() == 2) {
				remark = "加息券：" + ct.getCouponUserCode();
			} else if (currentRecover.getCouponType() == 3) {
				remark = "代金券：" + ct.getCouponUserCode();
			}
			accountList.setRemark(remark);
			accountList.setIp(borrow.getAddip()); // 操作IP
			accountList.setIsUpdate(0);
			 accountList.setBaseUpdate(0);
			// accountList.setInterest(recoverInterest); // 利息
			accountList.setWeb(0); // PC
			int accountListCnt = insertAccountList(accountList);
			if (accountListCnt == 0) {
				throw new RuntimeException("收支明细(huiyingdai_account_list)写入失败！" + "[优惠券出借编号：" + couponTenderNid + "]");
			}
		}
		// 更新出借表
		// 已收总额
		borrowTenderCpn.setRecoverAccountYes(borrowTenderCpn.getRecoverAccountYes().add(recoverAccount));
		// 已收本金
		borrowTenderCpn.setRecoverAccountCapitalYes(borrowTenderCpn.getRecoverAccountCapitalYes().add(recoverCapital));
		// 已收利息
		borrowTenderCpn.setRecoverAccountInterestYes(borrowTenderCpn.getRecoverAccountInterestYes().add(recoverInterest));
		// 待收总额
		borrowTenderCpn.setRecoverAccountWait(borrowTenderCpn.getRecoverAccountWait().subtract(recoverAccount));
		// 待收本金
		borrowTenderCpn.setRecoverAccountCapitalWait(borrowTenderCpn.getRecoverAccountCapitalWait().subtract(recoverCapital));
		// 待收利息
		borrowTenderCpn.setRecoverAccountInterestWait(borrowTenderCpn.getRecoverAccountInterestWait().subtract(recoverInterest));
		int borrowTenderCnt = borrowTenderCpnMapper.updateByPrimaryKeySelective(borrowTenderCpn);
		if (borrowTenderCnt == 0) {
			throw new RuntimeException("出借表(hyjf_borrow_tender_cpn)更新失败！" + "[优惠券出借编号：" + couponTenderNid + "]");
		}
		// 更新优惠券出借还款表
		// 转账订单编号
		cr.setTransferId(orderId);
		// 已还款
		cr.setRecoverStatus(1);
		// 收益领取状态(已领取)
		cr.setReceivedFlg(5);
		// 转账时间
		cr.setTransferTime(nowTime);
		// 已经还款时间
		cr.setRecoverYestime(nowTime);
		// 已还利息
		cr.setRecoverInterestYes(recoverInterest);
		// 已还本息
		cr.setRecoverAccountYes(recoverAccount);
		if (currentRecover.getCouponType() == 3) {
			// 代金券
			// 已还本金
			cr.setRecoverCapitalYes(recoverCapital);
		} else {
			// 体验金和加息券
			// 已还本金
			cr.setRecoverCapitalYes(BigDecimal.ZERO);
		}
		// 更新时间
		cr.setUpdateTime(nowTime);
		// 更新用户
		cr.setUpdateUser(CustomConstants.OPERATOR_AUTO_REPAY);
		// 通知用户
		cr.setNoticeFlg(1);
		this.couponRecoverMapper.updateByPrimaryKeySelective(cr);
		// 插入网站收支明细记录
		AccountWebList accountWebList = new AccountWebList();
		if (!isMonth) {
			// 未分期
			accountWebList.setOrdid(borrowTenderCpn.getNid());// 订单号
		} else {
			// 分期
			accountWebList.setOrdid(borrowTenderCpn.getNid() + "_" + periodNow);// 订单号
			if (periodNext > 0) {
				// 更新还款期
				this.crRecoverPeriod(couponTenderNid, periodNow + 1);
			}
		}
		accountWebList.setBorrowNid(borrowNid); // 出借编号
		accountWebList.setUserId(tenderUserId); // 出借者
		accountWebList.setAmount(recoverAccount); // 优惠券出借受益
		accountWebList.setType(CustomConstants.TYPE_OUT); // 类型1收入,2支出
		
		String tradeType = StringUtils.EMPTY;
		if (currentRecover.getCouponType() == 1) {
			// 体验金
			accountWebList.setTrade(CustomConstants.TRADE_COUPON_TYJ); 
			// 体验金收益
			tradeType = CustomConstants.TRADE_COUPON_SY;
		} else if (currentRecover.getCouponType() == 2) {
			// 加息券
			accountWebList.setTrade(CustomConstants.TRADE_COUPON_JXQ); 
			// 加息券回款
			tradeType = CustomConstants.TRADE_COUPON_HK;
		} else if (currentRecover.getCouponType() == 3) {
			// 代金券
			accountWebList.setTrade(CustomConstants.TRADE_COUPON_DJQ); 
			// 代金券回款
			tradeType = CustomConstants.TRADE_COUPON_DJ;
		}
		accountWebList.setTradeType(tradeType); // 加息券回款
		String remark = "项目编号：" + borrowNid + "<br />优惠券:" + ct.getCouponUserCode();
		accountWebList.setRemark(remark); // 出借编号
		accountWebList.setCreateTime(nowTime);
		int accountWebListCnt = insertAccountWebList(accountWebList);
		if (accountWebListCnt == 0) {
			throw new RuntimeException("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号：" + borrowTenderCpn.getNid() + "]");
		}
		
		// 添加红包账户明细
        BankMerchantAccount nowBankMerchantAccount = this.getBankMerchantAccount(resultBean.getAccountId());
        nowBankMerchantAccount.setAvailableBalance(nowBankMerchantAccount.getAvailableBalance().subtract(recoverAccount));
        nowBankMerchantAccount.setAccountBalance(nowBankMerchantAccount.getAccountBalance().subtract(recoverAccount));
        nowBankMerchantAccount.setUpdateTime(GetDate.getNowTime10());
        
        // 更新红包账户信息
        int updateCount = this.updateBankMerchantAccount(nowBankMerchantAccount);
        if(updateCount > 0){
            UserInfoCustomize userInfoCustomize = this.queryUserInfoByUserId(tenderUserId);
            
            // 添加红包明细
            BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
            bankMerchantAccountList.setOrderId(orderId);
            bankMerchantAccountList.setBorrowNid(borrowNid);
            bankMerchantAccountList.setUserId(tenderUserId);
            bankMerchantAccountList.setAccountId(bankOpenAccountInfo.getAccount());
            bankMerchantAccountList.setAmount(recoverAccount);
            bankMerchantAccountList.setBankAccountCode(resultBean.getAccountId());
            bankMerchantAccountList.setBankAccountBalance(nowBankMerchantAccount.getAccountBalance());
            bankMerchantAccountList.setBankAccountFrost(nowBankMerchantAccount.getFrost());
            bankMerchantAccountList.setTransType(CustomConstants.BANK_MER_TRANS_TYPE_AUTOMATIC);
            bankMerchantAccountList.setType(CustomConstants.BANK_MER_TYPE_EXPENDITURE);
            bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS);
            bankMerchantAccountList.setTxDate(Integer.parseInt(resultBean.getTxDate()));
            bankMerchantAccountList.setTxTime(Integer.parseInt(resultBean.getTxTime()));
            bankMerchantAccountList.setSeqNo(resultBean.getSeqNo());
            bankMerchantAccountList.setCreateTime(new Date());
            bankMerchantAccountList.setUpdateTime(new Date());
            bankMerchantAccountList.setCreateUserId(tenderUserId);
            bankMerchantAccountList.setUpdateUserId(tenderUserId);
            bankMerchantAccountList.setRegionName(userInfoCustomize.getRegionName());
            bankMerchantAccountList.setBranchName(userInfoCustomize.getBranchName());
            bankMerchantAccountList.setDepartmentName(userInfoCustomize.getDepartmentName());
            bankMerchantAccountList.setCreateUserName(userInfoCustomize.getUserName());
            bankMerchantAccountList.setUpdateUserName(userInfoCustomize.getUserName());
            bankMerchantAccountList.setRemark("直投类优惠券还款");
            
            this.bankMerchantAccountListMapper.insertSelective(bankMerchantAccountList);
        }

		
		LogUtil.infoLog(CouponRepayServiceImpl.class.toString(), methodName,
				"-----------还款结束---" + borrowNid + "---------" + currentRecover.getTransferId() + "---------------");
		msg.put(USERID, ct.getUserId());
		msg.put(VAL_AMOUNT, StringUtils.isEmpty(recoverAccountStr) ? "0.00" : recoverAccountStr);
		msg.put(VAL_COUPON_TYPE,
				currentRecover.getCouponType() == 1 ? "体验金" : currentRecover.getCouponType() == 2 ? "加息券"
						: currentRecover.getCouponType() == 3 ? "代金券" : "");
		// 发送短信
		this.sendSmsCoupon(retMsgList);
		// 发送push消息
		this.sendPushMsgCoupon(retMsgList);
	}
	
	/**
	 * 汇添金优惠券还款
	 */
	@Override
	public void updateCouponRecoverHjh(String planNid, CouponTenderCustomize ct) throws Exception {
		String methodName = "updateCouponRecoverHtj";
		List<Map<String, String>> retMsgList = new ArrayList<Map<String, String>>();
		Map<String, String> msg = new HashMap<String, String>();
		retMsgList.add(msg);
		/** 基本变量 */
		// 当前时间
		int nowTime = GetDate.getNowTime10();
		/** 标的基本数据 */
		// 取得借款详情
		//BorrowWithBLOBs borrow = getBorrow(borrowNid);
		// 还款期数
		//Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
		// 剩余还款期数
		//Integer periodNext = borrowPeriod - periodNow;
		// 还款方式
		//String borrowStyle = borrow.getBorrowStyle();
		// 出借人用户ID
		Integer tenderUserId = null;
		// 出借人用户ID
		tenderUserId = Integer.valueOf(ct.getUserId());
		String couponTenderNid = ct.getOrderId();
		// 取得优惠券出借信息
		BorrowTenderCpn borrowTenderCpn = this.getCouponTenderInfo(couponTenderNid);
        // 出借人在银行存管的账户信息
        BankOpenAccount bankOpenAccountInfo = this.getBankOpenAccount(tenderUserId);
        if (bankOpenAccountInfo == null) {
            throw new Exception("出借人未开户。[用户ID：" + tenderUserId + "]，" + "[优惠券出借编号：" + couponTenderNid + "]");
        }
		/*// 是否月标(true:月标, false:天标)
		boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);*/
		// 当前还款
		CouponRecoverCustomize currentRecover = this.getCurrentCouponRecover(couponTenderNid, 1);
		/*// [principal: 等额本金, month:等额本息, month:等额本息, endmonth:先息后本]
		if (isMonth) {
			// 取得分期还款
			currentRecover = this.getCurrentCouponRecover(couponTenderNid, periodNow);
			
		} else {// [endday: 按天计息, end:按月计息]
			currentRecover = this.getCurrentCouponRecover(couponTenderNid, 1);

		}*/
		if (currentRecover == null) {
			_log.info("优惠券还款数据不存在。[智投编号：" + planNid + "]，" + "[优惠券出借编号：" + couponTenderNid + "]");
			//throw new Exception("优惠券还款数据不存在。[借款编号：" + borrowNid + "]，" + "[优惠券出借编号：" + couponTenderNid + "]");
			return;
		}
		// 应还利息
		String recoverInterestStr = StringUtils.isEmpty(currentRecover.getRecoverInterest()) ? "0.00" : currentRecover.getRecoverInterest();
		// 应还本息
		String recoverAccountStr = StringUtils.isEmpty(currentRecover.getRecoverAccount()) ? "0.00" : currentRecover.getRecoverAccount();
		// 应还本金
		String recoverCapitalStr = StringUtils.isEmpty(currentRecover.getRecoverCapital()) ? "0.00" : currentRecover.getRecoverCapital();
		BigDecimal recoverInterest = new BigDecimal(recoverInterestStr);
		BigDecimal recoverAccount = new BigDecimal(recoverAccountStr);
		BigDecimal recoverCapital = new BigDecimal(recoverCapitalStr);
		CouponRecover cr = new CouponRecover();
		cr.setId(currentRecover.getId());
		String orderId = GetOrderIdUtils.getOrderId2(tenderUserId);
		String seqNo = GetOrderIdUtils.getSeqNo(6);
		BankCallBean resultBean = null;
		BankCallBean bean = new BankCallBean();
		if(new BigDecimal(recoverAccountStr).compareTo(BigDecimal.ZERO) != 0){
            String merrpAccount = PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT);
            
            bean.setVersion(BankCallConstant.VERSION_10);// 版本号
            bean.setTxCode(BankCallMethodConstant.TXCODE_VOUCHER_PAY);// 交易代码
            bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
            bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
            bean.setSeqNo(seqNo);// 交易流水号
            bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
            bean.setAccountId(merrpAccount);// 电子账号
            bean.setTxAmount(CustomUtil.formatAmount(recoverAccountStr));
            bean.setForAccountId(bankOpenAccountInfo.getAccount());
            bean.setDesLineFlag("1");
            bean.setDesLine(orderId);
            bean.setLogOrderId(orderId);// 订单号
            bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
            bean.setLogUserId(String.valueOf(tenderUserId));
            bean.setLogClient(0);// 平台

            int transferStatus = Integer.MIN_VALUE;
            try {
                resultBean = BankCallUtils.callApiBg(bean);
                if (resultBean == null || !BankCallConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
                    // 转账失败
                    _log.info("转账失败..."+"[优惠券出借编号：" + couponTenderNid + "]");
                    cr.setReceivedFlg(4);
                    this.couponRecoverMapper.updateByPrimaryKeySelective(cr);
                    String errorMsg = StringUtils.EMPTY;
                    int type = 1;
                    if (currentRecover.getCouponType() == 1) {
                        errorMsg = "体验金收益自动领取失败";
                        type = 0;
                    }else if (currentRecover.getCouponType() == 2) {
                        errorMsg = "加息券收益自动领取失败";
                    } else if (currentRecover.getCouponType() == 3) {
                        errorMsg = "代金券收益自动领取失败";
                        type = 2;
                    }
                    

                    if(resultBean != null && Validator.isNotNull(resultBean.getRetMsg())){
                        errorMsg = resultBean.getRetMsg();
                    }
                    
                    if(resultBean != null && (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(resultBean.getRetCode()) || BankCallConstant.RETCODE_YUE_FAIL.equals(resultBean.getRetCode()))){
                        errorMsg = "商户子账户余额不足，请先充值或向该子账户转账";
                        sendSmsFail(currentRecover.getCouponType());
                    }
                    
                    transferStatus = 1;
                    // 插入转账异常
                    this.insertTransferExceptionLog(bean, resultBean, tenderUserId, recoverAccount, bankOpenAccountInfo.getAccount(), cr.getId(), transferStatus,
                            errorMsg, type);
                    LogUtil.errorLog(THIS_CLASS, methodName, new Exception("转账失败！失败数据已插入转账异常表。errormsg:"+errorMsg));
                    return;
                    
                }
                
            } catch (Exception e) {
                LogUtil.errorLog(THIS_CLASS, methodName, "系统发生异常，更新异常转账表失败,事物回滚", e);
                throw new RuntimeException("系统发生异常，更新异常转账表失败,事物回滚!" + "[优惠券出借编号：" + couponTenderNid + "]");
            }
		}
		
		// 判断该收支明细是否存在时,跳出本次循环
		if (countAccountListByNidCoupon(orderId) == 0) {
			// 更新账户信息(出借人)
			Account account = new Account();
			account.setUserId(tenderUserId);
			
            account.setBankBalance(recoverAccount); // 账户余额
            account.setBankInterestSum(recoverAccount);
            
			// 计划已还总利息
			account.setPlanRepayInterest(recoverAccount);
			// 计划待收收益
			account.setPlanInterestWait(recoverInterest);
			// 计划待收总额
			account.setPlanAccountWait(recoverAccount);
			// 更新用户计划账户
			int accountCnt = this.adminAccountCustomizeMapper.updateOfRepayCouponHjh(account);
			if (accountCnt == 0) {
				throw new RuntimeException("出借人资金记录(huiyingdai_account)更新失败！" + "[优惠券出借编号：" + couponTenderNid + "]");
			}
			// 取得账户信息(出借人)
			account = this.getAccountByUserId(tenderUserId);
			if (account == null) {
				throw new RuntimeException("出借人账户信息不存在。[出借人ID：" + borrowTenderCpn.getUserId() + "]，" + "[优惠券出借编号：" + couponTenderNid + "]");
			}
			// 写入收支明细
			AccountList accountList = new AccountList();
			// 转账订单编号
			accountList.setNid(orderId);
			// 出借人
			accountList.setUserId(tenderUserId);
			accountList.setBankAwait(account.getBankAwait());
            accountList.setBankAwaitCapital(account.getBankAwaitCapital());
            accountList.setBankAwaitInterest(account.getBankAwaitInterest());
            accountList.setBankBalance(account.getBankBalance());
            accountList.setBankFrost(account.getBankFrost());
            accountList.setBankInterestSum(account.getBankInterestSum());
            accountList.setBankTotal(account.getBankTotal());
            accountList.setBankWaitCapital(account.getBankWaitCapital());
            accountList.setBankWaitInterest(account.getBankWaitInterest());
            accountList.setBankWaitRepay(account.getBankWaitRepay());
            accountList.setPlanBalance(account.getPlanBalance());//汇计划账户可用余额
			accountList.setPlanFrost(account.getPlanFrost());
            accountList.setAccountId(bankOpenAccountInfo.getAccount());
            accountList.setTxDate(Integer.parseInt(GetOrderIdUtils.getTxDate()));
            accountList.setTxTime(Integer.parseInt(GetOrderIdUtils.getTxTime()));
            accountList.setSeqNo(seqNo);
            accountList.setBankSeqNo(bean.getTxDate() + bean.getTxTime() + bean.getSeqNo());
            accountList.setCheckStatus(0);
            accountList.setTradeStatus(1);
            accountList.setIsBank(1);
			// 出借收入
			accountList.setAmount(recoverAccount);
			// 1收入
			accountList.setType(1);
			String trade = StringUtils.EMPTY;
			if (currentRecover.getCouponType() == 1) {
				trade = "experience_profit";
			} else if (currentRecover.getCouponType() == 2) {
				trade = "increase_interest_profit";
			} else if (currentRecover.getCouponType() == 3) {
				trade = "cash_coupon_profit";
			}
			// 还款成功
			accountList.setTrade(trade);
			// 余额操作
			accountList.setTradeCode("balance");
			// 出借人资金总额
			accountList.setTotal(account.getTotal());
			// 出借人可用金额
			accountList.setBalance(account.getBalance());
			// 出借人冻结金额
			accountList.setFrost(account.getFrost());
			// 出借人待收金额
			accountList.setAwait(account.getAwait());
			// 创建时间
			accountList.setCreateTime(nowTime);
			// 更新时间
			accountList.setBaseUpdate(nowTime);
			// 操作者
			accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY);
			String remark = StringUtils.EMPTY;
			if (currentRecover.getCouponType() == 1) {
				remark = "体验金：" + ct.getCouponUserCode();
			} else if (currentRecover.getCouponType() == 2) {
				remark = "加息券：" + ct.getCouponUserCode();
			} else if (currentRecover.getCouponType() == 3) {
				remark = "代金券：" + ct.getCouponUserCode();
			}
			accountList.setRemark(remark);
			//accountList.setIp(borrow.getAddip()); // 操作IP
			accountList.setIsUpdate(0);
			accountList.setBaseUpdate(0);
			// accountList.setInterest(recoverInterest); // 利息
			accountList.setWeb(0); // PC
			int accountListCnt = insertAccountList(accountList);
			if (accountListCnt == 0) {
				throw new RuntimeException("收支明细(huiyingdai_account_list)写入失败！" + "[优惠券出借编号：" + couponTenderNid + "]");
			}
		}
		// 更新出借表
		// 已收总额
		borrowTenderCpn.setRecoverAccountYes(borrowTenderCpn.getRecoverAccountYes().add(recoverAccount));
		// 已收本金
		borrowTenderCpn.setRecoverAccountCapitalYes(borrowTenderCpn.getRecoverAccountCapitalYes().add(recoverCapital));
		// 已收利息
		borrowTenderCpn.setRecoverAccountInterestYes(borrowTenderCpn.getRecoverAccountInterestYes().add(recoverInterest));
		// 待收总额
		borrowTenderCpn.setRecoverAccountWait(borrowTenderCpn.getRecoverAccountWait().subtract(recoverAccount));
		// 待收本金
		borrowTenderCpn.setRecoverAccountCapitalWait(borrowTenderCpn.getRecoverAccountCapitalWait().subtract(recoverCapital));
		// 待收利息
		borrowTenderCpn.setRecoverAccountInterestWait(borrowTenderCpn.getRecoverAccountInterestWait().subtract(recoverInterest));
		int borrowTenderCnt = borrowTenderCpnMapper.updateByPrimaryKeySelective(borrowTenderCpn);
		if (borrowTenderCnt == 0) {
			throw new RuntimeException("出借表(hyjf_borrow_tender_cpn)更新失败！" + "[优惠券出借编号：" + couponTenderNid + "]");
		}
		// 更新优惠券出借还款表
		// 转账订单编号
		cr.setTransferId(orderId);
		// 已还款
		cr.setRecoverStatus(1);
		// 收益领取状态(已领取)
		cr.setReceivedFlg(5);
		// 转账时间
		cr.setTransferTime(nowTime);
		// 已经还款时间
		cr.setRecoverYestime(nowTime);
		// 已还利息
		cr.setRecoverInterestYes(recoverInterest);
		// 已还本息
		cr.setRecoverAccountYes(recoverAccount);
		if (currentRecover.getCouponType() == 3) {
			// 代金券
			// 已还本金
			cr.setRecoverCapitalYes(recoverCapital);
		} else {
			// 体验金和加息券
			// 已还本金
			cr.setRecoverCapitalYes(BigDecimal.ZERO);
		}
		// 更新时间
		cr.setUpdateTime(nowTime);
		// 更新用户
		cr.setUpdateUser(CustomConstants.OPERATOR_AUTO_REPAY);
		// 通知用户
		cr.setNoticeFlg(1);
		this.couponRecoverMapper.updateByPrimaryKeySelective(cr);
		// 插入网站收支明细记录
		AccountWebList accountWebList = new AccountWebList();
		// 未分期
		accountWebList.setOrdid(borrowTenderCpn.getNid());// 订单号
		/*if (!isMonth) {
			// 未分期
			accountWebList.setOrdid(borrowTenderCpn.getNid());// 订单号
		} else {
			// 分期
			accountWebList.setOrdid(borrowTenderCpn.getNid() + "_" + periodNow);// 订单号
			if (periodNext > 0) {
				// 更新还款期
				this.crRecoverPeriod(couponTenderNid, periodNow + 1);
			}
		}*/
		//accountWebList.setBorrowNid(borrowNid); // 出借编号
		accountWebList.setUserId(tenderUserId); // 出借者
		accountWebList.setAmount(recoverAccount); // 优惠券出借受益
		accountWebList.setType(CustomConstants.TYPE_OUT); // 类型1收入,2支出
		
		String tradeType = StringUtils.EMPTY;
		if (currentRecover.getCouponType() == 1) {
			// 体验金
			accountWebList.setTrade(CustomConstants.TRADE_COUPON_TYJ); 
			// 体验金收益
			tradeType = CustomConstants.TRADE_COUPON_SY;
		} else if (currentRecover.getCouponType() == 2) {
			// 加息券
			accountWebList.setTrade(CustomConstants.TRADE_COUPON_JXQ); 
			// 加息券回款
			tradeType = CustomConstants.TRADE_COUPON_HK;
		} else if (currentRecover.getCouponType() == 3) {
			// 代金券
			accountWebList.setTrade(CustomConstants.TRADE_COUPON_DJQ); 
			// 代金券回款
			tradeType = CustomConstants.TRADE_COUPON_DJ;
		}
		accountWebList.setTradeType(tradeType); // 加息券回款
		String remark = "智投编号：" + planNid + "<br />优惠券:" + ct.getCouponUserCode();
		accountWebList.setRemark(remark); // 出借编号
		accountWebList.setCreateTime(nowTime);
		int accountWebListCnt = insertAccountWebList(accountWebList);
		if (accountWebListCnt == 0) {
			throw new RuntimeException("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号：" + borrowTenderCpn.getNid() + "]");
		}
		
	    // 添加红包账户明细
        BankMerchantAccount nowBankMerchantAccount = this.getBankMerchantAccount(resultBean.getAccountId());
        nowBankMerchantAccount.setAvailableBalance(nowBankMerchantAccount.getAvailableBalance().subtract(recoverAccount));
        nowBankMerchantAccount.setAccountBalance(nowBankMerchantAccount.getAccountBalance().subtract(recoverAccount));
        nowBankMerchantAccount.setUpdateTime(GetDate.getNowTime10());
        
        // 更新红包账户信息
        int updateCount = this.updateBankMerchantAccount(nowBankMerchantAccount);
        if(updateCount > 0){
            UserInfoCustomize userInfoCustomize = this.queryUserInfoByUserId(tenderUserId);
            
            // 添加红包明细
            BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
            bankMerchantAccountList.setOrderId(orderId);
            bankMerchantAccountList.setBorrowNid(planNid);
            bankMerchantAccountList.setUserId(tenderUserId);
            bankMerchantAccountList.setAccountId(bankOpenAccountInfo.getAccount());
            bankMerchantAccountList.setAmount(recoverAccount);
            bankMerchantAccountList.setBankAccountCode(resultBean.getAccountId());
            bankMerchantAccountList.setBankAccountBalance(nowBankMerchantAccount.getAccountBalance());
            bankMerchantAccountList.setBankAccountFrost(nowBankMerchantAccount.getFrost());
            bankMerchantAccountList.setTransType(CustomConstants.BANK_MER_TRANS_TYPE_AUTOMATIC);
            bankMerchantAccountList.setType(CustomConstants.BANK_MER_TYPE_EXPENDITURE);
            bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS);
            bankMerchantAccountList.setTxDate(Integer.parseInt(resultBean.getTxDate()));
            bankMerchantAccountList.setTxTime(Integer.parseInt(resultBean.getTxTime()));
            bankMerchantAccountList.setSeqNo(resultBean.getSeqNo());
            bankMerchantAccountList.setCreateTime(new Date());
            bankMerchantAccountList.setUpdateTime(new Date());
            bankMerchantAccountList.setCreateUserId(tenderUserId);
            bankMerchantAccountList.setUpdateUserId(tenderUserId);
            bankMerchantAccountList.setRegionName(userInfoCustomize.getRegionName());
            bankMerchantAccountList.setBranchName(userInfoCustomize.getBranchName());
            bankMerchantAccountList.setDepartmentName(userInfoCustomize.getDepartmentName());
            bankMerchantAccountList.setCreateUserName(userInfoCustomize.getUserName());
            bankMerchantAccountList.setUpdateUserName(userInfoCustomize.getUserName());
            bankMerchantAccountList.setRemark("汇计划优惠券还款");
            
            this.bankMerchantAccountListMapper.insertSelective(bankMerchantAccountList);
        }

		
		LogUtil.infoLog(CouponRepayServiceImpl.class.toString(), methodName,
				"-----------还款结束---" + planNid + "---------" + currentRecover.getTransferId() + "---------------");
		msg.put(USERID, ct.getUserId());
		msg.put(VAL_AMOUNT, StringUtils.isEmpty(recoverAccountStr) ? "0.00" : recoverAccountStr);
		msg.put(VAL_COUPON_TYPE,
				currentRecover.getCouponType() == 1 ? "体验金" : currentRecover.getCouponType() == 2 ? "加息券"
						: currentRecover.getCouponType() == 3 ? "代金券" : "");
		// 发送短信
		this.sendSmsCoupon(retMsgList);
		// 发送push消息
		this.sendPushMsgCoupon(retMsgList);
	}

	/**
	 * 发送push消息(优惠券还款成功)
	 * 
	 * @param userId
	 */
	public void sendPushMsgCoupon(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(USERID)) && NumberUtils.isNumber(msg.get(USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(USERID)));
					if (users == null) {
						return;
					}
					AppMsMessage appMsMessage = new AppMsMessage(users.getUserId(), msg, null, MessageDefine.APPMSSENDFORUSER,
							CustomConstants.JYTZ_COUPON_PROFIT);
					appMsProcesser.gather(appMsMessage);
				}
			}
		}
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
		example.setOrderByClause(" id asc ");
		List<BorrowWithBLOBs> list = this.borrowMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 取得优惠券出借信息
	 * 
	 * @param couponTenderNid
	 * @return
	 */
	private BorrowTenderCpn getCouponTenderInfo(String couponTenderNid) {
		BorrowTenderCpnExample example = new BorrowTenderCpnExample();
		example.createCriteria().andNidEqualTo(couponTenderNid);
		List<BorrowTenderCpn> btList = this.borrowTenderCpnMapper.selectByExample(example);
		if (btList != null && btList.size() > 0) {
			return btList.get(0);
		}
		return null;
	}

	/**
	 * 根据订单编号取得该订单的还款列表
	 * 
	 * @param realTenderId
	 * @return
	 */
	private CouponRecoverCustomize getCurrentCouponRecover(String couponTenderNid, int periodNow) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tenderNid", couponTenderNid);
		paramMap.put("periodNow", periodNow);
		return this.couponRecoverCustomizeMapper.selectCurrentCouponRecover(paramMap);

	}

	/**
	 * 更新还款期
	 * 
	 * @param tenderNid
	 *            出借订单编号
	 * @param resetRecoverFlg
	 *            0:非还款期，1;还款期
	 * @param currentRecoverFlg
	 *            0:非还款期，1;还款期
	 * @param period
	 *            期数
	 */
	private void crRecoverPeriod(String tenderNid, int period) {

		Map<String, Object> paramMapAll = new HashMap<String, Object>();
		paramMapAll.put("currentRecoverFlg", 0);
		paramMapAll.put("tenderNid", tenderNid);
		this.couponRecoverCustomizeMapper.crRecoverPeriod(paramMapAll);
		Map<String, Object> paramMapCurrent = new HashMap<String, Object>();
		paramMapCurrent.put("currentRecoverFlg", 1);
		paramMapCurrent.put("tenderNid", tenderNid);
		paramMapCurrent.put("period", period);
		this.couponRecoverCustomizeMapper.crRecoverPeriod(paramMapCurrent);

	}

	/**
	 * 优惠券还款 余额不足 短信通知
	 */
	private void sendSmsFail(int couponType) {
		Map<String, String> replaceStrs = new HashMap<String, String>();
		try {
			if (2 == couponType) {
				replaceStrs.put("couponType", "加息券");
			} else if (3 == couponType) {
				replaceStrs.put("couponType", "代金券");
			} else if (1 == couponType) {
				replaceStrs.put("couponType", "体验金");
			}

			SmsMessage smsMessage =
                    new SmsMessage(null, replaceStrs, null, null,
                            MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_COUPON_JIA_YUE, CustomConstants.CHANNEL_TYPE_NORMAL);
            smsProcesser.gather(smsMessage);
			
		} catch (Exception e) {
			LogUtil.debugLog(this.getClass().toString(), "sendSmsFail", e.getMessage());
		}
	}

	/**
	 * 作成转账异常记录
	 * 
	 * @param fromBean
	 * @param resultBean
	 * @param userId
	 * @param transAmt
	 * @param tenderUserCustId
	 */
	private void insertTransferExceptionLog(BankCallBean fromBean, BankCallBean bankCallBean, int userId, BigDecimal transAmt, String accountId,
			int recoverId, int transferStatus, String errorMsg, int type) throws Exception {
		TransferExceptionLogExample example = new TransferExceptionLogExample();
		example.createCriteria().andRecoverIdEqualTo(recoverId);
		List<TransferExceptionLog> listLog = this.transferExceptionLogMapper.selectByExample(example);
		if (listLog != null && listLog.size() > 0) {
			// 异常转账记录已经存在，不再执行插入操作
			_log.info("异常转账记录已经存在，不再执行插入操作");
			return;
		}
		int nowTime = GetDate.getNowTime10();
		TransferExceptionLogWithBLOBs transferExceptionLog = new TransferExceptionLogWithBLOBs();
		transferExceptionLog.setUuid(CreateUUID.createUUID());
		// 订单编号
		transferExceptionLog.setSeqNo(fromBean.getSeqNo());
		// 发送内容
		transferExceptionLog.setContent(JSONObject.toJSONString(fromBean, true));
		// 返回内容
		transferExceptionLog.setResult(bankCallBean == null ? null : JSONObject.toJSONString(bankCallBean, true));
		// 加息券
		transferExceptionLog.setType(type);
		// 状态 失败
		transferExceptionLog.setStatus(transferStatus);
		// 返回码
		transferExceptionLog.setRespcode(bankCallBean == null ? null : bankCallBean.getRetCode());
		// 交易金额
		transferExceptionLog.setTransAmt(transAmt);
		// 出借人客户号
		transferExceptionLog.setAccountId(accountId);
		// 出借人编号
		transferExceptionLog.setUserId(userId);
		// 还款表（coupon_recover）id
		transferExceptionLog.setRecoverId(recoverId);
		// 转账订单号
		transferExceptionLog.setOrderId(fromBean.getLogOrderId());
		// 备注
		transferExceptionLog.setRemark(errorMsg);
		transferExceptionLog.setAddTime(nowTime);
		transferExceptionLog.setAddUser(CustomConstants.OPERATOR_AUTO_REPAY);
		transferExceptionLog.setUpdateTime(nowTime);
		transferExceptionLog.setUpdateUser(CustomConstants.OPERATOR_AUTO_REPAY);
		transferExceptionLog.setDelFlg(0);
		// 转账失败记录
		this.transferExceptionLogMapper.insertSelective(transferExceptionLog);
	}

	/**
	 * 判断该收支明细是否存在(加息券)
	 * 
	 * @param accountList
	 * @return
	 */
	private int countAccountListByNidCoupon(String nid) {
		AccountListExample accountListExample = new AccountListExample();
		accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("increase_interest_profit");
		return this.accountListMapper.countByExample(accountListExample);
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
	 * 发送短信(优惠券还款成功)
	 * 
	 * @param userId
	 */
	public void sendSmsCoupon(List<Map<String, String>> msgList) {
		if (msgList != null && msgList.size() > 0) {
			for (Map<String, String> msg : msgList) {
				if (Validator.isNotNull(msg.get(USERID)) && NumberUtils.isNumber(msg.get(USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))) {
					Users users = getUsersByUserId(Integer.valueOf(msg.get(USERID)));
					if (users == null || Validator.isNull(users.getMobile()) || (users.getRecieveSms() != null && users.getRecieveSms() == 1)) {
						return;
					}
					SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null,
							CustomConstants.PARAM_TPL_COUPON_PROFIT, CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				}
			}
		}
	}
	
	/**
	 * 体验金按收益期限还款
	 */
	@Override
	public void updateCouponOnlyRecover(String nid) throws Exception {
		String methodName = "updateCouponOnlyRecover";
		List<Map<String, String>> retMsgList = new ArrayList<Map<String, String>>();
		Map<String, String> msg = new HashMap<String, String>();
		retMsgList.add(msg);
		_log.info("还款开始，出借编号："+nid);
		Integer nowTime = GetDate.getNowTime10();
		// 当前还款
		CouponRecoverCustomize currentRecover = null;
		// [principal: 等额本金, month:等额本息, month:等额本息, endmonth:先息后本]
		// [endday: 按天计息, end:按月计息]
		currentRecover = this.getCurrentCouponRecover(nid, 1);
		if (currentRecover == null) {
			_log.info("优惠券还款数据不存在。[优惠券出借编号：" + nid + "]");
			return;
		}
		// 取得优惠券出借信息
		BorrowTenderCpn borrowTenderCpn = this.getCouponTenderInfo(nid);
		if(borrowTenderCpn == null){
			_log.info("出借编号不存在："+nid);
			return;
		}
		Integer tenderUserId = borrowTenderCpn.getUserId();
		String borrowNid = borrowTenderCpn.getBorrowNid();
        // 出借人在银行存管的账户信息
        BankOpenAccount bankOpenAccountInfo = this.getBankOpenAccount(tenderUserId);
        if (bankOpenAccountInfo == null) {
            throw new Exception("出借人未开户。[用户ID：" + tenderUserId + "]，" + "[优惠券出借编号：" + nid + "]");
        }
		// 应还利息
		String recoverInterestStr = StringUtils.isEmpty(currentRecover.getRecoverInterest()) ? "0.00" : currentRecover.getRecoverInterest();
		// 应还本息
		String recoverAccountStr = StringUtils.isEmpty(currentRecover.getRecoverAccount()) ? "0.00" : currentRecover.getRecoverAccount();
		// 应还本金
		String recoverCapitalStr = StringUtils.isEmpty(currentRecover.getRecoverCapital()) ? "0.00" : currentRecover.getRecoverCapital();
		BigDecimal recoverInterest = new BigDecimal(recoverInterestStr);
		BigDecimal recoverAccount = new BigDecimal(recoverAccountStr);
		BigDecimal recoverCapital = new BigDecimal(recoverCapitalStr);
		CouponRecover cr = new CouponRecover();
		cr.setId(currentRecover.getId());
		String orderId = GetOrderIdUtils.getOrderId2(borrowTenderCpn.getUserId());
		String seqNo = GetOrderIdUtils.getSeqNo(6);
		BankCallBean resultBean = null;
		BankCallBean bean = new BankCallBean();
		if(recoverAccount.compareTo(BigDecimal.ZERO) != 0){
            String merrpAccount = PropUtils.getSystem(BankCallConstant.BANK_MERRP_ACCOUNT);
            
            bean.setVersion(BankCallConstant.VERSION_10);// 版本号
            bean.setTxCode(BankCallMethodConstant.TXCODE_VOUCHER_PAY);// 交易代码
            bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
            bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
            bean.setSeqNo(seqNo);// 交易流水号
            bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
            bean.setAccountId(merrpAccount);// 电子账号
            bean.setTxAmount(CustomUtil.formatAmount(recoverAccountStr));
            bean.setForAccountId(bankOpenAccountInfo.getAccount());
            bean.setDesLineFlag("1");
            bean.setDesLine(orderId);
            bean.setLogOrderId(orderId);// 订单号
            bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
            bean.setLogUserId(String.valueOf(tenderUserId));
            bean.setLogClient(0);// 平台

            int transferStatus = Integer.MIN_VALUE;
            try {
                resultBean = BankCallUtils.callApiBg(bean);
                if (resultBean == null || !BankCallConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
                    // 转账失败
                    cr.setReceivedFlg(4);
                    this.couponRecoverMapper.updateByPrimaryKeySelective(cr);
                    String errorMsg = StringUtils.EMPTY;
                    int type = 1;
                    if (currentRecover.getCouponType() == 1) {
                        errorMsg = "体验金收益自动领取失败";
                        type = 0;
                    }else if (currentRecover.getCouponType() == 2) {
                        errorMsg = "加息券收益自动领取失败";
                    } else if (currentRecover.getCouponType() == 3) {
                        errorMsg = "代金券收益自动领取失败";
                        type = 2;
                    }
                    

                    if(resultBean != null && Validator.isNotNull(resultBean.getRetMsg())){
                        errorMsg = resultBean.getRetMsg();
                    }
                    
                    if(resultBean != null && (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(resultBean.getRetCode()) || BankCallConstant.RETCODE_YUE_FAIL.equals(resultBean.getRetCode()))){
                        errorMsg = "商户子账户余额不足，请先充值或向该子账户转账";
                        sendSmsFail(currentRecover.getCouponType());
                    }
                    
                    transferStatus = 1;
                    // 插入转账异常
                    this.insertTransferExceptionLog(bean, resultBean, tenderUserId, recoverAccount, bankOpenAccountInfo.getAccount(), cr.getId(), transferStatus,
                            errorMsg, type);
                    LogUtil.errorLog(THIS_CLASS, methodName, new Exception("转账失败！失败数据已插入转账异常表。errormsg:"+errorMsg));
                    return;
                    
                }
                
            } catch (Exception e) {
                LogUtil.errorLog(THIS_CLASS, methodName, "系统发生异常，更新异常转账表失败,事物回滚", e);
                throw new RuntimeException("系统发生异常，更新异常转账表失败,事物回滚!" + "[优惠券出借编号：" + nid + "]");
            }
		}
		
		// 判断该收支明细是否存在时,跳出本次循环
		if (countAccountListByNidCoupon(orderId) == 0) {
			// 更新账户信息(出借人)
			Account account = new Account();
			
			int accountCnt;
			// 如果是计划类出借
			if(borrowTenderCpn.getTenderType() == 3 ){
				// 更新账户信息(出借人)
				account.setUserId(tenderUserId);
				
	            account.setBankBalance(recoverAccount); // 账户余额
	            account.setBankInterestSum(recoverAccount);
	            
				// 计划已还总利息
				account.setPlanRepayInterest(recoverAccount);
				// 计划待收收益
				account.setPlanInterestWait(recoverInterest);
				// 计划待收总额
				account.setPlanAccountWait(recoverAccount);
				
				// 更新用户计划账户
				accountCnt = this.adminAccountCustomizeMapper.updateOfRepayCouponHjh(account);
			}else{
				// 直投类
				account.setUserId(tenderUserId);
	            account.setBankTotal(BigDecimal.ZERO);// 出借人资金总额 +利息
	            account.setBankFrost(BigDecimal.ZERO);// 出借人冻结金额+出借金额(等额本金时出借金额可能会大于计算出的本金之和)
	            account.setBankBalance(recoverAccount); // 账户余额
	            account.setBankAwait(recoverAccount);// 出借人待收金额+利息+ 本金
	            account.setBankAwaitCapital(BigDecimal.ZERO);// 出借人待收本金
	            account.setBankAwaitInterest(recoverAccount);// 出借人待收利息
	            account.setBankInterestSum(recoverAccount);
	            account.setBankInvestSum(BigDecimal.ZERO);// 出借人累计出借
	            account.setBankFrostCash(BigDecimal.ZERO);// 江西银行冻结金额
	            
				accountCnt = this.adminAccountCustomizeMapper.updateOfRepayTender(account);
			}
			
						
			if (accountCnt == 0) {
				throw new RuntimeException("出借人资金记录(huiyingdai_account)更新失败！" + "[优惠券出借编号：" + nid + "]");
			}
			// 取得账户信息(出借人)
			account = this.getAccountByUserId(tenderUserId);
			if (account == null) {
				throw new RuntimeException("出借人账户信息不存在。[出借人ID：" + borrowTenderCpn.getUserId() + "]，" + "[优惠券出借编号：" + nid + "]");
			}
			// 写入收支明细
			AccountList accountList = new AccountList();
			// 转账订单编号
			accountList.setNid(orderId);
			// 出借人
			accountList.setUserId(tenderUserId);
            accountList.setBankAwait(account.getBankAwait());
            accountList.setBankAwaitCapital(account.getBankAwaitCapital());
            accountList.setBankAwaitInterest(account.getBankAwaitInterest());
            accountList.setBankBalance(account.getBankBalance());
            accountList.setBankFrost(account.getBankFrost());
            accountList.setBankInterestSum(account.getBankInterestSum());
            accountList.setBankTotal(account.getBankTotal());
            accountList.setBankWaitCapital(account.getBankWaitCapital());
            accountList.setBankWaitInterest(account.getBankWaitInterest());
            accountList.setBankWaitRepay(account.getBankWaitRepay());
            accountList.setPlanBalance(account.getPlanBalance());//汇计划账户可用余额
			accountList.setPlanFrost(account.getPlanFrost());
            accountList.setAccountId(bankOpenAccountInfo.getAccount());
            accountList.setTxDate(Integer.parseInt(GetOrderIdUtils.getTxDate()));
            accountList.setTxTime(Integer.parseInt(GetOrderIdUtils.getTxTime()));
            accountList.setSeqNo(seqNo);
            accountList.setBankSeqNo(bean.getTxDate() + bean.getTxTime() + bean.getSeqNo());
            accountList.setCheckStatus(0);
            accountList.setTradeStatus(1);
            accountList.setIsBank(1);

			
			// 出借收入
			accountList.setAmount(recoverAccount);
			// 1收入
			accountList.setType(1);
			String remark = "";
			String trade = StringUtils.EMPTY;
			if (currentRecover.getCouponType() == 1) {
				trade = "experience_profit";
                remark = "体验金：" + currentRecover.getCouponUserCode();
            }else if (currentRecover.getCouponType() == 2) {
            	trade = "increase_interest_profit";
            	remark = "加息券：" + currentRecover.getCouponUserCode();
            } else if (currentRecover.getCouponType() == 3) {
            	trade = "cash_coupon_profit";
            	remark = "代金券：" + currentRecover.getCouponUserCode();
            }
			
			// 还款成功
			accountList.setTrade(trade);
			// 余额操作
			accountList.setTradeCode("balance");
			// 出借人资金总额
			accountList.setTotal(account.getTotal());
			// 出借人可用金额
			accountList.setBalance(account.getBalance());
			// 出借人冻结金额
			accountList.setFrost(account.getFrost());
			// 出借人待收金额
			accountList.setAwait(account.getAwait());
			// 创建时间
			accountList.setCreateTime(nowTime);
			// 更新时间
			accountList.setBaseUpdate(nowTime);
			// 操作者
			accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY);
			
			accountList.setRemark(remark);
			accountList.setIsUpdate(0);
			accountList.setBaseUpdate(0);
			// accountList.setInterest(recoverInterest); // 利息
			accountList.setWeb(0); // PC
			int accountListCnt = insertAccountList(accountList);
			if (accountListCnt == 0) {
				throw new RuntimeException("收支明细(huiyingdai_account_list)写入失败！" + "[优惠券出借编号：" + nid + "]");
			}
		}
		// 更新出借表
		// 已收总额
		borrowTenderCpn.setRecoverAccountYes(borrowTenderCpn.getRecoverAccountYes().add(recoverAccount));
		// 已收本金
		borrowTenderCpn.setRecoverAccountCapitalYes(borrowTenderCpn.getRecoverAccountCapitalYes().add(recoverCapital));
		// 已收利息
		borrowTenderCpn.setRecoverAccountInterestYes(borrowTenderCpn.getRecoverAccountInterestYes().add(recoverInterest));
		// 待收总额
		borrowTenderCpn.setRecoverAccountWait(borrowTenderCpn.getRecoverAccountWait().subtract(recoverAccount));
		// 待收本金
		borrowTenderCpn.setRecoverAccountCapitalWait(borrowTenderCpn.getRecoverAccountCapitalWait().subtract(recoverCapital));
		// 待收利息
		borrowTenderCpn.setRecoverAccountInterestWait(borrowTenderCpn.getRecoverAccountInterestWait().subtract(recoverInterest));
		int borrowTenderCnt = borrowTenderCpnMapper.updateByPrimaryKeySelective(borrowTenderCpn);
		if (borrowTenderCnt == 0) {
			throw new RuntimeException("出借表(hyjf_borrow_tender_cpn)更新失败！" + "[优惠券出借编号：" + nid + "]");
		}
		// 更新优惠券出借还款表
		// 转账订单编号
		cr.setTransferId(orderId);
		// 已还款
		cr.setRecoverStatus(1);
		// 收益领取状态(已领取)
		cr.setReceivedFlg(5);
		// 转账时间
		cr.setTransferTime(nowTime);
		// 已经还款时间
		cr.setRecoverYestime(nowTime);
		// 已还利息
		cr.setRecoverInterestYes(recoverInterest);
		// 已还本息
		cr.setRecoverAccountYes(recoverAccount);
		// 本金
		cr.setRecoverCapitalYes(BigDecimal.ZERO);
		// 更新时间
		cr.setUpdateTime(nowTime);
		// 更新用户
		cr.setUpdateUser(CustomConstants.OPERATOR_AUTO_REPAY);
		// 通知用户
		cr.setNoticeFlg(1);
		this.couponRecoverMapper.updateByPrimaryKeySelective(cr);
		// 插入网站收支明细记录
		AccountWebList accountWebList = new AccountWebList();
		// 未分期
		accountWebList.setOrdid(borrowTenderCpn.getNid());// 订单号
		// 直投类
		if(borrowTenderCpn.getTenderType() == RECOVER_TYPE_HZT){
			accountWebList.setBorrowNid(borrowNid); // 出借编号
		}
		accountWebList.setUserId(tenderUserId); // 出借者
		accountWebList.setAmount(recoverAccount); // 优惠券出借受益
		accountWebList.setType(CustomConstants.TYPE_OUT); // 类型1收入,2支出
		String remark = StringUtils.EMPTY;
		if (currentRecover.getCouponType() == 1) {
            remark = "体验金：" + currentRecover.getCouponUserCode();
        }else if (currentRecover.getCouponType() == 2) {
        	remark = "加息券：" + currentRecover.getCouponUserCode();
        } else if (currentRecover.getCouponType() == 3) {
        	remark = "代金券：" + currentRecover.getCouponUserCode();
        }
		String tradeType = StringUtils.EMPTY;
		if (currentRecover.getCouponType() == 1) {
			// 体验金
			accountWebList.setTrade(CustomConstants.TRADE_COUPON_TYJ); 
			// 体验金收益
			tradeType = CustomConstants.TRADE_COUPON_SY;
		} else if (currentRecover.getCouponType() == 2) {
			// 加息券
			accountWebList.setTrade(CustomConstants.TRADE_COUPON_JXQ); 
			// 加息券回款
			tradeType = CustomConstants.TRADE_COUPON_HK;
		} else if (currentRecover.getCouponType() == 3) {
			// 代金券
			accountWebList.setTrade(CustomConstants.TRADE_COUPON_DJQ); 
			// 代金券回款
			tradeType = CustomConstants.TRADE_COUPON_DJ;
		}
		accountWebList.setTradeType(tradeType); // 加息券回款
		accountWebList.setRemark(remark); // 出借编号
		accountWebList.setCreateTime(nowTime);
		int accountWebListCnt = insertAccountWebList(accountWebList);
		if (accountWebListCnt == 0) {
			throw new RuntimeException("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号：" + borrowTenderCpn.getNid() + "]");
		}
		
	      // 添加红包账户明细
        BankMerchantAccount nowBankMerchantAccount = this.getBankMerchantAccount(resultBean.getAccountId());
        nowBankMerchantAccount.setAvailableBalance(nowBankMerchantAccount.getAvailableBalance().subtract(recoverAccount));
        nowBankMerchantAccount.setAccountBalance(nowBankMerchantAccount.getAccountBalance().subtract(recoverAccount));
        nowBankMerchantAccount.setUpdateTime(GetDate.getNowTime10());
        
        // 更新红包账户信息
        int updateCount = this.updateBankMerchantAccount(nowBankMerchantAccount);
        if(updateCount > 0){
            UserInfoCustomize userInfoCustomize = this.queryUserInfoByUserId(tenderUserId);
            
            // 添加红包明细
            BankMerchantAccountList bankMerchantAccountList = new BankMerchantAccountList();
            bankMerchantAccountList.setOrderId(orderId);
            bankMerchantAccountList.setUserId(tenderUserId);
            bankMerchantAccountList.setAccountId(bankOpenAccountInfo.getAccount());
            bankMerchantAccountList.setAmount(recoverAccount);
            bankMerchantAccountList.setBankAccountCode(resultBean.getAccountId());
            bankMerchantAccountList.setBankAccountBalance(nowBankMerchantAccount.getAccountBalance());
            bankMerchantAccountList.setBankAccountFrost(nowBankMerchantAccount.getFrost());
            bankMerchantAccountList.setTransType(CustomConstants.BANK_MER_TRANS_TYPE_AUTOMATIC);
            bankMerchantAccountList.setType(CustomConstants.BANK_MER_TYPE_EXPENDITURE);
            bankMerchantAccountList.setStatus(CustomConstants.BANK_MER_TRANS_STATUS_SUCCESS);
            bankMerchantAccountList.setTxDate(Integer.parseInt(resultBean.getTxDate()));
            bankMerchantAccountList.setTxTime(Integer.parseInt(resultBean.getTxTime()));
            bankMerchantAccountList.setSeqNo(resultBean.getSeqNo());
            bankMerchantAccountList.setCreateTime(new Date());
            bankMerchantAccountList.setUpdateTime(new Date());
            bankMerchantAccountList.setRegionName(userInfoCustomize.getRegionName());
            bankMerchantAccountList.setBranchName(userInfoCustomize.getBranchName());
            bankMerchantAccountList.setDepartmentName(userInfoCustomize.getDepartmentName());
            bankMerchantAccountList.setCreateUserId(tenderUserId);
            bankMerchantAccountList.setUpdateUserId(tenderUserId);
            bankMerchantAccountList.setCreateUserName(userInfoCustomize.getUserName());
            bankMerchantAccountList.setUpdateUserName(userInfoCustomize.getUserName());
            bankMerchantAccountList.setRemark("优惠券单独出借还款");
            
            this.bankMerchantAccountListMapper.insertSelective(bankMerchantAccountList);
        }

        
		LogUtil.infoLog(CouponRepayServiceImpl.class.toString(), methodName,
				"-----------还款结束---" + borrowNid + "---------" + currentRecover.getTransferId() + "---------------");
		msg.put(USERID, tenderUserId.toString());
		msg.put(VAL_AMOUNT, StringUtils.isEmpty(recoverAccountStr) ? "0.00" : recoverAccountStr);
		msg.put(VAL_COUPON_TYPE,"体验金");
		// 发送短信
		this.sendSmsCoupon(retMsgList);
		// 发送push消息
		this.sendPushMsgCoupon(retMsgList);
		
	}
	
       /**
        * 
        * 加载红包账户
        * @param accountCode
        * @return
        */
       public BankMerchantAccount getBankMerchantAccount(String accountCode) {
            BankMerchantAccountExample bankMerchantAccountExample = new BankMerchantAccountExample();
            bankMerchantAccountExample.createCriteria().andAccountCodeEqualTo(accountCode);
            List<BankMerchantAccount> bankMerchantAccounts = bankMerchantAccountMapper.selectByExample(bankMerchantAccountExample);
            if (bankMerchantAccounts != null && bankMerchantAccounts.size() != 0) {
                return bankMerchantAccounts.get(0);
            } else {
                return null;
            }
        }
       
       /**
        * 
        * 更新红包账户
        * @param account
        * @return
        */
       public int updateBankMerchantAccount(BankMerchantAccount account){
           return bankMerchantAccountMapper.updateByPrimaryKeySelective(account);
       }
       
       public UserInfoCustomize queryUserInfoByUserId(Integer userId) {
           return userInfoCustomizeMapper.queryUserInfoByUserId(userId);
       }
}

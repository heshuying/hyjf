package com.hyjf.batch.datarecover.couponrepay;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
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
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.BorrowTenderCpnExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CouponRecover;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;

/**
 * 体验金按照收益期限还款
 * @author zhangjp
 *
 */
@Service
public class RepayDataRecoverServiceImpl extends BaseServiceImpl {
	Logger _log = LoggerFactory.getLogger(RepayDataRecoverServiceImpl.class);
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

    public List<CouponTenderCustomize> getCouponTenderList(String borrowNid) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("borrowNid", borrowNid);
        // 1 项目到期还款  2 收益期限到期还款
        paramMap.put("repayTimeConfig", 1);
        return this.couponRecoverCustomizeMapper.selectCouponRecoverAll(paramMap);
    }
    
	public void couponRepayDataRecover(String borrowNid, Integer periodNow, CouponTenderCustomize ct) throws Exception{
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
        // 出借人在汇付的账户信息
        AccountChinapnr tenderUserCust = null;
        // 出借人客户号
        Long tenderUserCustId = null;
        // 出借人用户ID
        tenderUserId = Integer.valueOf(ct.getUserId());
        String couponTenderNid = ct.getOrderId();
        // 取得优惠券出借信息
        BorrowTenderCpn borrowTenderCpn = this.getCouponTenderInfo(couponTenderNid);
        // 出借人在汇付的账户信息
        tenderUserCust = getChinapnrUserInfo(tenderUserId);
        if (tenderUserCust == null) {
            throw new Exception("出借人未开户。[用户ID：" + tenderUserId + "]，" + "[优惠券出借编号：" + couponTenderNid + "]");
        }
        // 出借人客户号
        tenderUserCustId = tenderUserCust.getChinapnrUsrcustid();
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
        
        // 判断该收支明细是否存在时,跳出本次循环
        if (countAccountListByNidCoupon(orderId) == 0) {
            // 更新账户信息(出借人)
            Account account = new Account();
            account.setUserId(tenderUserId);
            // 出借人可用余额
            account.setBalance(recoverAccount);
            // 出借人待收金额
            account.setAwait(recoverAccount);
            // 出借人资金总额
            account.setTotal(recoverAccount);
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

}

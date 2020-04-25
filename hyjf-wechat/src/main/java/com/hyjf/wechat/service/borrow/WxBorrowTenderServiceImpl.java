/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:50:02
 * Modification History:
 * Modified by :
 */

package com.hyjf.wechat.service.borrow;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyjf.bank.service.mq.MqService;
import com.alibaba.fastjson.JSON;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.tender.IncreaseInterestInvestService;
import com.hyjf.bank.service.user.tender.TenderCouponInfo;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.calculate.AverageCapitalUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.calculate.FinancingServiceChargeUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetailExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowSendType;
import com.hyjf.mybatis.model.auto.BorrowSendTypeExample;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.BorrowTenderCpnExample;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.BorrowTenderTmp;
import com.hyjf.mybatis.model.auto.BorrowTenderTmpExample;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.mybatis.model.auto.CouponRealTender;
import com.hyjf.mybatis.model.auto.CouponRealTenderExample;
import com.hyjf.mybatis.model.auto.CouponTender;
import com.hyjf.mybatis.model.auto.CouponTenderExample;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.FreezeList;
import com.hyjf.mybatis.model.auto.IncreaseInterestInvest;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UtmReg;
import com.hyjf.mybatis.model.auto.UtmRegExample;
import com.hyjf.mybatis.model.auto.VipUserTender;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.wechat.base.BaseServiceImpl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Service
public class WxBorrowTenderServiceImpl extends BaseServiceImpl implements WxBorrowTenderService {
	private Logger _log = LoggerFactory.getLogger(WxBorrowTenderServiceImpl.class);

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	public static JedisPool pool = RedisUtils.getPool();
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private TransactionDefinition transactionDefinition;

	@Autowired
    private IncreaseInterestInvestService increaseInterestInvestService;

    @Autowired
    private MqService mqService;

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;


    /**
     * 保存用戶的投資数据
     * @param borrow
     * @param bean
     * @return
     * @throws Exception 
     */
    @Override
    public JSONObject userTender(Borrow borrow, BankCallBean bean) throws Exception {

        int nowTime = GetDate.getNowTime10();
        String ip = bean.getLogIp();// 操作ip
        int client = bean.getLogClient() != 0 ? bean.getLogClient() : 0;// 操作平台
        String accountId = bean.getAccountId();// 获取出借用户的出借客户号
        Integer userId = Integer.parseInt(bean.getLogUserId());// 出借人id
        String txAmount = bean.getTxAmount();// 借款金额
        String orderId = bean.getOrderId();// 订单id
        String orderDate = bean.getLogOrderDate(); // 订单日期
        String retCode = bean.getRetCode();// 出借结果返回码
        String authCode = bean.getAuthCode();// 出借结果授权码
        String borrowNid = borrow.getBorrowNid();// 项目编号
        String borrowStyle = borrow.getBorrowStyle();// 项目的还款方式
        int projectType = borrow.getProjectType();// 项目类型
        BigDecimal serviceFeeRate = Validator.isNull(borrow.getServiceFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getServiceFeeRate()); // 服务费率
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();// 借款期数
        Integer borrowId = borrow.getId();// 借款项目主键
        BigDecimal accountDecimal = new BigDecimal(txAmount);// 冻结前验证
        JSONObject result = null;
        boolean checkTender = RedisUtils.tranactionSet("tender_orderid" + orderId, 20);
        if(!checkTender){//同步/异步 优先执行完毕
            result = new JSONObject();
            result.put("message", "同步/异步 优先执行完毕!");
            result.put("status", -1);
            return result;
        }
        // redis扣减
        result = this.redisTender(userId, borrowNid, txAmount);
        // redis结果状态
        String redisStatus = result.getString("status");
        String tenderFrom = "";//出借来源
        if (redisStatus.equals("1")) {
            // 手动控制事务
            TransactionStatus txStatus = null;
            try {
                // 手动控制事务
                txStatus = this.transactionManager.getTransaction(transactionDefinition);
                // 删除临时表
                BorrowTenderTmpExample borrowTenderTmpExample = new BorrowTenderTmpExample();
                BorrowTenderTmpExample.Criteria criteria1 = borrowTenderTmpExample.createCriteria();
                criteria1.andNidEqualTo(orderId);
                criteria1.andUserIdEqualTo(userId);
                criteria1.andBorrowNidEqualTo(borrowNid);
                //查询出临时表的tender_from字段
                List<BorrowTenderTmp> borrowTenderTmpList = borrowTenderTmpMapper.selectByExample(borrowTenderTmpExample);
                if(borrowTenderTmpList != null && borrowTenderTmpList.size() > 0){
                    tenderFrom = borrowTenderTmpList.get(0).getTenderFrom();
                }

                boolean tenderTempFlag = borrowTenderTmpMapper.deleteByExample(borrowTenderTmpExample) > 0 ? true : false;
                if (!tenderTempFlag) {
                    result.put("message", "出借失败！");
                    result.put("status", 0);
                    throw new Exception("删除borrowTenderTmp表失败");
                }
                _log.info("用户:" + userId + "***********************************删除borrowTenderTmp，订单号：" + orderId);
                // 插入冻结表
                FreezeList record = new FreezeList();
                record.setAmount(accountDecimal);
                record.setBorrowNid(borrowNid);
                record.setCreateTime(nowTime);
                record.setOrdid(orderId);
                record.setUserId(userId);
                record.setRespcode(retCode);
                record.setTrxid("");
                record.setOrdid(orderId);
                record.setUsrcustid(accountId);
                record.setXfrom(1);
                record.setStatus(0);
                record.setUnfreezeManual(0);
                boolean freezeFlag = freezeListMapper.insertSelective(record) > 0 ? true : false;
                if (!freezeFlag) {
                    result.put("message", "出借失败！");
                    result.put("status", 0);
                    throw new Exception("插入冻结表失败！");
                }
                _log.info("用户:" + userId + "***********************************插入FreezeList，订单号：" + orderId);
                BigDecimal perService = new BigDecimal(0);
                if (StringUtils.isNotEmpty(borrowStyle)) {
                    BigDecimal serviceScale = serviceFeeRate;
                    // 到期还本还息end/先息后本endmonth/等额本息month/等额本金principal
                    if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                            || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
                        perService = FinancingServiceChargeUtils.getMonthsPrincipalServiceCharge(accountDecimal, serviceScale);
                    }
                    // 按天计息到期还本还息
                    else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
                        perService = FinancingServiceChargeUtils.getDaysPrincipalServiceCharge(accountDecimal, serviceScale, borrowPeriod);
                    }
                }
                BorrowTender borrowTender = new BorrowTender();
                borrowTender.setAccount(accountDecimal);
                borrowTender.setAccountTender(new BigDecimal(0));
                borrowTender.setActivityFlag(0);//
                borrowTender.setAddip(ip);
                borrowTender.setAddtime(nowTime);
                borrowTender.setApiStatus(0);//
                borrowTender.setAutoStatus(0);//
                borrowTender.setBorrowNid(borrowNid);
                borrowTender.setChangePeriod(0);//
                borrowTender.setChangeUserid(0);
                borrowTender.setClient(1);// 
                borrowTender.setContents("");//
                borrowTender.setFlag(0);//
                borrowTender.setIsok(0);
                borrowTender.setIsReport(0);
                borrowTender.setChangeStatus(0);
                borrowTender.setLoanAmount(accountDecimal.subtract(perService));
                borrowTender.setNid(orderId);
                borrowTender.setOrderDate(orderDate);
                borrowTender.setPeriodStatus(0);//
                borrowTender.setRecoverAccountAll(new BigDecimal(0));//
                borrowTender.setRecoverAccountCapitalWait(new BigDecimal(0));//
                borrowTender.setRecoverAccountCapitalYes(new BigDecimal(0));
                borrowTender.setRecoverAccountInterest(new BigDecimal(0));
                borrowTender.setRecoverAccountInterestWait(new BigDecimal(0));
                borrowTender.setRecoverAccountInterestYes(new BigDecimal(0));
                borrowTender.setRecoverAccountWait(new BigDecimal(0));
                borrowTender.setRecoverAccountYes(new BigDecimal(0));
                borrowTender.setRecoverAdvanceFee(new BigDecimal(0));
                borrowTender.setRecoverFee(new BigDecimal(0));
                borrowTender.setRecoverFullStatus(0);
                borrowTender.setRecoverLateFee(new BigDecimal(0));
                borrowTender.setRecoverTimes(0);
                borrowTender.setRecoverType("");
                borrowTender.setStatus(0);
                borrowTender.setTenderAwardAccount(new BigDecimal(0));
                borrowTender.setTenderAwardFee(new BigDecimal(0));
                borrowTender.setTenderNid(borrowNid);
                borrowTender.setTenderStatus(0);
                borrowTender.setUserId(userId);
                borrowTender.setTenderFrom(tenderFrom);//新增出借来源
                result.put("tenderFrom",tenderFrom);//json返回中也新增出借来源
                // 出借人信息
                Users users = getUsers(userId);
                UsersInfo userInfo = null;
                if (users != null && projectType != 8) {
                    // 更新渠道统计用户累计出借
                    AppChannelStatisticsDetailExample channelExample = new AppChannelStatisticsDetailExample();
                    AppChannelStatisticsDetailExample.Criteria crt = channelExample.createCriteria();
                    crt.andUserIdEqualTo(userId);
                    List<AppChannelStatisticsDetail> appChannelStatisticsDetails = this.appChannelStatisticsDetailMapper.selectByExample(channelExample);
                    if (appChannelStatisticsDetails != null && appChannelStatisticsDetails.size() == 1) {
                        AppChannelStatisticsDetail channelDetail = appChannelStatisticsDetails.get(0);
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("id", channelDetail.getId());
                        params.put("accountDecimal", accountDecimal);
                        // 出借时间
                        params.put("investTime", nowTime);
                        // 项目类型
                        if (borrow.getProjectType() == 13) {
                            params.put("projectType", "汇金理财");
                        } else {
                            params.put("projectType", "汇直投");
                        }
                        // 首次投标项目期限
                        String investProjectPeriod = "";
                        if (borrow.getBorrowStyle().equals("endday")) {
                            investProjectPeriod = borrow.getBorrowPeriod() + "天";
                        } else {
                            investProjectPeriod = borrow.getBorrowPeriod() + "个月";
                        }
                        params.put("investProjectPeriod", investProjectPeriod);
                        // 更新渠道统计用户累计出借
                        if (users.getInvestflag() == 1) {
                            this.appChannelStatisticsDetailCustomizeMapper.updateAppChannelStatisticsDetail(params);
                        } else if (users.getInvestflag() == 0) {
                            // 更新首投出借
                            this.appChannelStatisticsDetailCustomizeMapper.updateFirstAppChannelStatisticsDetail(params);
                        }
                        _log.info("用户:" + userId + "***********************************预更新渠道统计表AppChannelStatisticsDetail，订单号：" + orderId);
                    } else {
                        // 更新huiyingdai_utm_reg的首投信息
                        UtmRegExample utmRegExample = new UtmRegExample();
                        UtmRegExample.Criteria utmRegCra = utmRegExample.createCriteria();
                        utmRegCra.andUserIdEqualTo(userId);
                        List<UtmReg> utmRegList = this.utmRegMapper.selectByExample(utmRegExample);
                        if (utmRegList != null && utmRegList.size() > 0) {
                            UtmReg utmReg = utmRegList.get(0);
                            Map<String, Object> params = new HashMap<String, Object>();
                            params.put("id", utmReg.getId());
                            params.put("accountDecimal", accountDecimal);
                            // 出借时间
                            params.put("investTime", nowTime);
                            // 项目类型
                            if (borrow.getProjectType() == 13) {
                                params.put("projectType", "汇金理财");
                            } else {
                                params.put("projectType", "汇直投");
                            }
                            // 首次投标项目期限
                            String investProjectPeriod = "";
                            if (borrow.getBorrowStyle().equals("endday")) {
                                investProjectPeriod = borrow.getBorrowPeriod() + "天";
                            } else {
                                investProjectPeriod = borrow.getBorrowPeriod() + "个月";
                            }
                            params.put("investProjectPeriod", investProjectPeriod);

                            // 更新渠道统计用户累计出借
                            if (users.getInvestflag() == 0) {
                                // 更新huiyingdai_utm_reg的首投信息
                                this.appChannelStatisticsDetailCustomizeMapper.updateFirstUtmReg(params);
                            }
                        }
                    }
                }
                if (users != null) {
                    if (users.getInvestflag() == 0) {
                        users.setInvestflag(1);
                        UsersExample userExample = new UsersExample();
                        userExample.createCriteria().andUserIdEqualTo(userId).andInvestflagEqualTo(0);
                        boolean userFlag = this.usersMapper.updateByExampleSelective(users, userExample) > 0 ? true : false;
                        if (!userFlag) {
                            result.put("message", "出借新手标失败！");
                            result.put("status", 0);
                            throw new Exception("更新新手标识失败，用户userId：" + userId);
                        }
                    } else {
                        if (projectType == 4) {
                            // 回滚事务
                            result.put("message", "您已不是新手，不能出借新手标！");
                            result.put("status", 0);
                            throw new Exception("用户已不是新手出借，用户userId：" + userId);
                        }
                    }
                    // 出借用户名
                    borrowTender.setTenderUserName(users.getUsername());
                    // 获取出借人属性
                    userInfo = this.getUsersInfoByUserId(userId);
                    // 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
                    Integer attribute = null;
                    if (userInfo != null) {
                        // 获取出借用户的用户属性
                        attribute = userInfo.getAttribute();
                        if (attribute != null) {
                            // 出借人用户属性
                            borrowTender.setTenderUserAttribute(attribute);
                            // 如果是线上员工或线下员工，推荐人的userId和username不插
                            if (attribute == 2 || attribute == 3) {
                                EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
                                if (employeeCustomize != null) {
                                    borrowTender.setInviteRegionId(employeeCustomize.getRegionId());
                                    borrowTender.setInviteRegionName(employeeCustomize.getRegionName());
                                    borrowTender.setInviteBranchId(employeeCustomize.getBranchId());
                                    borrowTender.setInviteBranchName(employeeCustomize.getBranchName());
                                    borrowTender.setInviteDepartmentId(employeeCustomize.getDepartmentId());
                                    borrowTender.setInviteDepartmentName(employeeCustomize.getDepartmentName());
                                }
                            } else if (attribute == 1) {
                                SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
                                SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
                                spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
                                List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
                                if (sList != null && sList.size() == 1) {
                                    int refUserId = sList.get(0).getSpreadsUserid();
                                    // 查找用户推荐人
                                    Users userss = getUsers(refUserId);
                                    if (userss != null) {
                                        borrowTender.setInviteUserId(userss.getUserId());
                                        borrowTender.setInviteUserName(userss.getUsername());
                                    }
                                    // 推荐人信息
                                    UsersInfo refUsers = this.getUsersInfoByUserId(refUserId);
                                    // 推荐人用户属性
                                    if (refUsers != null) {
                                        borrowTender.setInviteUserAttribute(refUsers.getAttribute());
                                    }
                                    // 查找用户推荐人部门
                                    EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
                                    if (employeeCustomize != null) {
                                        borrowTender.setInviteRegionId(employeeCustomize.getRegionId());
                                        borrowTender.setInviteRegionName(employeeCustomize.getRegionName());
                                        borrowTender.setInviteBranchId(employeeCustomize.getBranchId());
                                        borrowTender.setInviteBranchName(employeeCustomize.getBranchName());
                                        borrowTender.setInviteDepartmentId(employeeCustomize.getDepartmentId());
                                        borrowTender.setInviteDepartmentName(employeeCustomize.getDepartmentName());
                                    }
                                }
                            } else if (attribute == 0) {
                                SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
                                SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
                                spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
                                List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
                                if (sList != null && sList.size() == 1) {
                                    int refUserId = sList.get(0).getSpreadsUserid();
                                    // 查找推荐人
                                    Users userss = getUsers(refUserId);
                                    if (userss != null) {
                                        borrowTender.setInviteUserId(userss.getUserId());
                                        borrowTender.setInviteUserName(userss.getUsername());
                                    }
                                    // 推荐人信息
                                    UsersInfo refUsers = getUsersInfoByUserId(refUserId);
                                    // 推荐人用户属性
                                    if (refUsers != null) {
                                        borrowTender.setInviteUserAttribute(refUsers.getAttribute());
                                    }
                                }
                            }
                        }
                    }
                }
                borrowTender.setWeb(0);
                borrowTender.setWebStatus(0);
                borrowTender.setClient(client);
                borrowTender.setInvestType(0);
                // 单笔出借的融资服务费
                borrowTender.setLoanFee(perService);
                //出借授权码
                if(StringUtils.isNotBlank(authCode)){
                    borrowTender.setAuthCode(authCode);
                }
                borrowTender.setRemark("现金出借");
                boolean trenderFlag = borrowTenderMapper.insertSelective(borrowTender) > 0 ? true : false;
                if (!trenderFlag) {
                    result.put("message", "出借失败！");
                    result.put("status", 0);
                    throw new Exception("插入出借表失败！");
                }
                _log.info("用户:" + userId + "***********************************插入borrowTender，订单号：" + orderId);

             // 插入产品加息
                if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield())) {
                    boolean increaseFlag = increaseInterestInvestService.insertIncreaseInterest(borrow,bean,borrowTender) > 0 ? true : false;
                    if (!increaseFlag) {
                        result.put("message", "出借失败！");
                        result.put("status", 0);
                        throw new Exception("插入产品加息表失败！");
                    }
                    _log.info("用户:" + userId + "***********************************插入产品加息，订单号：" + orderId);
                }
                if (userInfo != null && userInfo.getVipId() != null) {
                    VipUserTender vt = new VipUserTender();
                    // 出借用户编号
                    vt.setUserId(userId);
                    // 出借用户vip编号
                    vt.setVipId(userInfo.getVipId());
                    // 出借编号
                    vt.setTenderNid(orderId);
                    vt.setSumVipValue(userInfo.getVipValue());
                    vt.setAddTime(nowTime);
                    vt.setAddUser(String.valueOf(userId));
                    vt.setUpdateTime(nowTime);
                    vt.setUpdateUser(String.valueOf(userId));
                    vt.setDelFlg(0);
                    this.vipUserTenderMapper.insertSelective(vt);
                }
                // 更新用户账户余额表
                Account accountBean = new Account();
                accountBean.setUserId(userId);
                // 出借人冻结金额增加
                accountBean.setBankFrost(accountDecimal);
                // 出借人可用余额扣减
                accountBean.setBankBalance(accountDecimal);
                // 此账户余额出借后应该扣减掉相应出借金额,sql已改
                accountBean.setBankBalanceCash(accountDecimal);
                // 江西银行账户冻结金额
                accountBean.setBankFrostCash(accountDecimal);
                Boolean accountFlag = this.adminAccountCustomizeMapper.updateAccountOfTender(accountBean) > 0 ? true : false;
                if (!accountFlag) {
                    result.put("message", "出借失败！");
                    result.put("status", 0);
                    throw new Exception("用户账户信息表更新失败");
                }
                // 插入account_list表
                _log.info("用户:" + userId + "***********************************更新account，订单号：" + orderId);
                Account account = this.getAccount(userId);
                AccountList accountList = new AccountList();
                accountList.setAmount(accountDecimal);
                /** 银行存管相关字段设置 */
                accountList.setAccountId(accountId);
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
                accountList.setCheckStatus(0);
                accountList.setTradeStatus(1);// 交易状态  0:失败 1:成功
                accountList.setIsBank(1);
                accountList.setTxDate(Integer.parseInt(bean.getTxDate()));
                accountList.setTxTime(Integer.parseInt(bean.getTxTime()));
                accountList.setSeqNo(bean.getSeqNo());
                accountList.setBankSeqNo(bean.getTxDate() + bean.getTxTime() + bean.getSeqNo());
                /** 非银行存管相关字段 */
                accountList.setAwait(new BigDecimal(0));
                accountList.setBalance(account.getBalance());
                accountList.setBaseUpdate(0);
                accountList.setCreateTime(nowTime);
                accountList.setFrost(account.getFrost());
                accountList.setInterest(new BigDecimal(0));
                accountList.setIp(ip);
                accountList.setIsUpdate(0);
                accountList.setNid(orderId);
                accountList.setOperator(userId + "");
                accountList.setRemark(borrowNid);
                accountList.setRepay(new BigDecimal(0));
                accountList.setTotal(account.getTotal());
                accountList.setTrade("tender");// 出借
                accountList.setTradeCode("frost");// 投标冻结后为frost
                accountList.setType(3);// 收支类型1收入2支出3冻结
                accountList.setUserId(userId);
                accountList.setWeb(0);
                accountList.setIsBank(1);// 是否是银行的交易记录(0:否,1:是)
                boolean accountListFlag = accountListMapper.insertSelective(accountList) > 0 ? true : false;
                if (!accountListFlag) {
                    result.put("message", "出借失败！");
                    result.put("status", 0);
                    throw new Exception("用户账户交易明细表更新失败");
                }
                _log.info("用户:" + userId + "***********************************插入accountList，订单号：" + orderId);
                // 更新borrow表
                Map<String, Object> borrowParam = new HashMap<String, Object>();
                borrowParam.put("borrowAccountYes", accountDecimal);
                borrowParam.put("borrowService", perService);
                borrowParam.put("borrowId", borrowId);
                boolean updateBorrowAccountFlag = borrowCustomizeMapper.updateOfBorrow(borrowParam) > 0 ? true : false;
                if (!updateBorrowAccountFlag) {
                    result.put("message", "出借失败！");
                    result.put("status", 0);
                    throw new Exception("borrow表更新失败");
                }
                _log.info("用户:" + userId + "***********************************更新borrow表，订单号：" + orderId);
                List<CalculateInvestInterest> calculates = this.calculateInvestInterestMapper.selectByExample(new CalculateInvestInterestExample());
                if (calculates != null && calculates.size() > 0) {
                    CalculateInvestInterest calculateNew = new CalculateInvestInterest();
                    calculateNew.setTenderSum(accountDecimal);
                    calculateNew.setId(calculates.get(0).getId());
                    calculateNew.setCreateTime(GetDate.getDate(nowTime));
                    this.webCalculateInvestInterestCustomizeMapper.updateCalculateInvestByPrimaryKey(calculateNew);
                }

                // 计算此时的剩余可出借金额
                BigDecimal accountWait = this.getBorrowByNid(borrowNid).getBorrowAccountWait();
                // 满标处理
                if (accountWait.compareTo(new BigDecimal(0)) == 0) {
                    _log.info("用户:" + userId + "***********************************项目满标，订单号：" + orderId);
                    Map<String, Object> borrowFull = new HashMap<String, Object>();
                    borrowFull.put("borrowId", borrowId);
                    boolean fullFlag = borrowCustomizeMapper.updateOfFullBorrow(borrowFull) > 0 ? true : false;
                    if (!fullFlag) {
                        result.put("message", "出借失败！");
                        result.put("status", 0);
                        throw new Exception("满标更新borrow表失败");
                    }
                    // 清除标总额的缓存
                    RedisUtils.del(borrowNid);
                    // 纯发短信接口
                    Map<String, String> replaceMap = new HashMap<String, String>();
                    replaceMap.put("val_title", borrowNid);
                    replaceMap.put("val_date", DateUtils.getNowDate());
                    BorrowSendTypeExample sendTypeExample = new BorrowSendTypeExample();
                    BorrowSendTypeExample.Criteria sendTypeCriteria = sendTypeExample.createCriteria();
                    sendTypeCriteria.andSendCdEqualTo("AUTO_FULL");
                    List<BorrowSendType> sendTypeList = borrowSendTypeMapper.selectByExample(sendTypeExample);
                    if (sendTypeList == null || sendTypeList.size() == 0) {
                        result.put("message", "出借失败！");
                        result.put("status", 0);
                        throw new Exception("用户:" + userId + "***********************************冻结成功后处理afterChinaPnR：" + "数据库查不到 sendTypeList == null");
                    }
                    BorrowSendType sendType = sendTypeList.get(0);
                    if (sendType.getAfterTime() == null) {
                        result.put("message", "出借失败！");
                        result.put("status", 0);
                        throw new Exception("用户:" + userId + "***********************************冻结成功后处理afterChinaPnR：" + "sendType.getAfterTime()==null");
                    }
                    replaceMap.put("val_times", sendType.getAfterTime() + "");
                    // 发送短信验证码
                    SmsMessage smsMessage = new SmsMessage(null, replaceMap, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_XMMB, CustomConstants.CHANNEL_TYPE_NORMAL);
                    smsProcesser.gather(smsMessage);

                    // add by liushouyi nifa2 20181204 start
                    // 发送满标状态埋点
                    // 发送发标成功的消息队列到互金上报数据
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("borrowNid", borrowNid);
                    this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ISSUE_INVESTED_DELAY_KEY, JSONObject.toJSONString(params));
                    // add by liushouyi nifa2 20181204 end
                } else if (accountWait.compareTo(BigDecimal.ZERO) < 0) {
                    result.put("message", "出借失败！");
                    result.put("status", 0);
                    throw new Exception("用户:" + userId + "项目编号:" + borrowNid + "***********************************项目暴标");
                }
                // 事务提交
                this.transactionManager.commit(txStatus);
                result.put("message", "出借成功！");
                result.put("status", 1);
                //add by  增加redis防重校验 
                RedisUtils.tranactionSet("tendersuccess_orderid" + orderId, 20);

                // MQ 出借触发奖励
                sendMQActivity(userId,orderId,accountDecimal,projectType);
                sendRrturnCashActivity(userId,orderId,accountDecimal,projectType);
                // 神策数据统计 add by liuyang 20180726 start
                try {
                    // 出借成功后,发送神策数据统计MQ
                    SensorsDataBean sensorsDataBean = new SensorsDataBean();
                    sensorsDataBean.setUserId(Integer.parseInt(bean.getLogUserId()));
                    sensorsDataBean.setOrderId(bean.getLogOrderId());
                    sensorsDataBean.setEventCode("submit_tender");
                    this.sendSensorsDataMQ(sensorsDataBean);
                }catch (Exception e){
                    e.printStackTrace();
                }
                // 神策数据统计 add by liuyang 20180726 end

                // add by liuyang 20181011 crm绩效统计修改 start
                // 当前时间> 20181201时,采用新的绩效统计方式,12月1日之后,此处判断可以删除
                try {
                    //crm投资推送
                    rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_POSTINTERFACE_CRM, JSON.toJSONString(borrowTender));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // add by liuyang 20181011 crm绩效统计修改 end
                return result;
            } catch (Exception e) {
                _log.info("标的:"+borrowNid+"==============出借异常!=======" + e.getMessage());
                e.printStackTrace();
                // 回滚事务
                this.transactionManager.rollback(txStatus);
                this.redisRecover(borrowNid, userId, txAmount);
                result.put("message", "出借失败！");
                result.put("status", 0);
            }
        }
        return result;
    }
    
    
    /**
     * 
     * 回滚redis
     * 
     * @param borrowNid
     * @param userId
     * @param account
     * @author Administrator
     */
    private boolean redisRecover(String borrowNid, Integer userId, String account) {
        JedisPool pool = RedisUtils.getPool();
        Jedis jedis = pool.getResource();
        BigDecimal accountBigDecimal = new BigDecimal(account);
        while ("OK".equals(jedis.watch(borrowNid))) {
            String balanceLast = RedisUtils.get(borrowNid);
            if (StringUtils.isNotBlank(balanceLast)) {
                _log.info("PC用户:" + userId + "***redis剩余金额：" + balanceLast);
                BigDecimal recoverAccount = accountBigDecimal.add(new BigDecimal(balanceLast));
                Transaction transaction = jedis.multi();
                transaction.set(borrowNid, recoverAccount.toString());
                List<Object> result = transaction.exec();
                if (result == null || result.isEmpty()) {
                    jedis.unwatch();
                } else {
                    String ret = (String) result.get(0);
                    if (ret != null && ret.equals("OK")) {
                        _log.info("用户:" + userId + "*******from redis恢复redis：" + account);
                        return true;
                    } else {
                        jedis.unwatch();
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * 操作redis
     * @param userId 
     * @param borrowNid
     * @param account
     * @return
     */
    @Override
    public JSONObject redisTender(Integer userId, String borrowNid, String txAmount) {

        Jedis jedis = pool.getResource();
        String status = BankCallConstant.STATUS_FAIL; // 发送状态
        JSONObject info = new JSONObject();
        BigDecimal accountDecimal = new BigDecimal(txAmount);// 冻结前验证
        String accountRedisWait = RedisUtils.get(borrowNid);
        if (StringUtils.isNotBlank(accountRedisWait)) {
            // 操作redis
            while ("OK".equals(jedis.watch(borrowNid))) {
                accountRedisWait = RedisUtils.get(borrowNid);
                if (StringUtils.isNotBlank(accountRedisWait)) {
                    _log.info("PC用户:" + userId + "***冻结前可投金额：" + accountRedisWait);
                    if (new BigDecimal(accountRedisWait).compareTo(BigDecimal.ZERO) == 0) {
                        info.put("message", "您来晚了，下次再来抢吧！");
                        info.put("status", status);
                        break;
                    } else {
                        if (new BigDecimal(accountRedisWait).compareTo(accountDecimal) < 0) {
                            info.put("message", "可投剩余金额为" + accountRedisWait + "元！");
                            info.put("status", status);
                            break;
                        } else {
                            Transaction transaction = jedis.multi();
                            BigDecimal lastAccount = new BigDecimal(accountRedisWait).subtract(accountDecimal);
                            transaction.set(borrowNid, lastAccount.toString());
                            List<Object> result = transaction.exec();
                            if (result == null || result.isEmpty()) {
                                jedis.unwatch();
                            } else {
                                String ret = (String) result.get(0);
                                if (ret != null && ret.equals("OK")) {
                                    status = "1";
                                    info.put("message", "redis操作成功！");
                                    info.put("status", status);
                                    _log.info("PC用户:" + userId + "***冻结前减redis：" + accountDecimal);
                                    break;
                                } else {
                                    jedis.unwatch();
                                }
                            }
                        }
                    }
                } else {
                    info.put("message", "您来晚了，下次再来抢吧！");
                    info.put("status", status);
                    break;
                }
            }
        } else {
            info.put("message", "您来晚了，下次再来抢吧！");
            info.put("status", status);
        }
        return info;
    }

    /**
     * 发放活动奖励
     * @param userId
     * @param order
     * @param investMoney
     * @param projectType 项目类型
     */
    private void sendMQActivity(Integer userId,String order,BigDecimal investMoney,int projectType){
        // 加入到消息队列
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("orderId", order);
        params.put("investMoney", investMoney.toString());
        //来源,1=新手标，2=散标，3=汇计划
        Integer productType = 2;
        //4 新手标
        if(4 == projectType){
            productType = 1;
        }
        params.put("productType", productType);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.MDIAU_ACTIVITY, JSONObject.toJSONString(params));
    }

    /**
     * 纳觅返现活动
     * @param userId
     * @param order
     */
    private void sendRrturnCashActivity(Integer userId,String order,BigDecimal investMoney,int projectType){
        // 加入到消息队列
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("orderId", order);
        params.put("investMoney", investMoney.toString());
        //来源,1=新手标，2=散标，3=汇计划
        Integer productType = 2;
        //4 新手标
        if(4 == projectType){
            productType = 1;
        }
        params.put("productType", productType);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.RETURN_CASH_ACTIVITY, JSONObject.toJSONString(params));
    }

    @Override
    public void extraUeldInvest(Borrow borrow, BankCallBean bean) {
        // 操作ip
        String ip = bean.getLogIp();
        // 操作平台
        int client = bean.getLogClient() != 0 ? bean.getLogClient() : 0;
        // 出借人id
        Integer userId = Integer.parseInt(bean.getLogUserId());
        // 借款金额
        String account = bean.getTxAmount();
        // 订单id
        String tenderOrderId = bean.getOrderId();
        // 项目编号
        String borrowNid = borrow.getBorrowNid();
        // 项目的还款方式
        String borrowStyle = borrow.getBorrowStyle();
        BorrowStyle borrowStyleMain = this.getborrowStyleByNid(borrowStyle);
        String borrowStyleName = borrowStyleMain.getName();
        // 借款期数
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
        Users users = this.getUsers(userId);
        // 生成额外利息订单
        String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
        // 根据orderid查询出借信息tender
        BorrowTender tender = this.getBorrowTenderByNidUserIdBorrowNid(tenderOrderId, userId, borrowNid);
        if (tender != null) {

            IncreaseInterestInvest increaseInterestInvest = new IncreaseInterestInvest();
            increaseInterestInvest.setUserId(userId);
            increaseInterestInvest.setInvestUserName(users.getUsername());
            increaseInterestInvest.setTenderId(tender.getId());
            increaseInterestInvest.setTenderNid(tenderOrderId);
            increaseInterestInvest.setBorrowNid(borrowNid);
            increaseInterestInvest.setBorrowApr(borrow.getBorrowApr());
            increaseInterestInvest.setBorrowExtraYield(borrow.getBorrowExtraYield());
            increaseInterestInvest.setBorrowPeriod(borrowPeriod);
            increaseInterestInvest.setBorrowStyle(borrowStyle);
            increaseInterestInvest.setBorrowStyleName(borrowStyleName);
            increaseInterestInvest.setOrderId(orderId);
            increaseInterestInvest.setOrderDate(GetDate.getServerDateTime(10, new Date()));
            increaseInterestInvest.setAccount(new BigDecimal(account));
            increaseInterestInvest.setStatus(0);
            increaseInterestInvest.setWeb(0);
            increaseInterestInvest.setClient(client);
            increaseInterestInvest.setAddip(ip);
            increaseInterestInvest.setRemark("产品加息");
            increaseInterestInvest.setInvestType(0);
            increaseInterestInvest.setCreateTime(GetDate.getNowTime10());
            increaseInterestInvest.setCreateUserId(userId);
            increaseInterestInvest.setCreateUserName(users.getUsername());
            boolean incinvflag = increaseInterestInvestMapper.insertSelective(increaseInterestInvest) > 0 ? true : false;
            if (!incinvflag) {
                _log.info("融通宝出借额外利息出借失败，插入额外出借信息失败,出借订单号:" + tenderOrderId);
            }
        } else {
            _log.info("融通宝出借额外利息出借失败，获取出借信息失败,出借订单号:" + tenderOrderId);
        }

    }
    private BorrowStyle getborrowStyleByNid(String borrowStyle) {
        BorrowStyleExample example = new BorrowStyleExample();
        BorrowStyleExample.Criteria cri = example.createCriteria();
        cri.andNidEqualTo(borrowStyle);
        List<BorrowStyle> style = borrowStyleMapper.selectByExample(example);
        return style.get(0);
    }
    private BorrowTender getBorrowTenderByNidUserIdBorrowNid(String orderId, Integer userId, String borrowNid) { // 删除临时表
        BorrowTenderExample borrowTenderExample = new BorrowTenderExample();
        BorrowTenderExample.Criteria criteria1 = borrowTenderExample.createCriteria();
        criteria1.andNidEqualTo(orderId);
        criteria1.andUserIdEqualTo(userId);
        criteria1.andBorrowNidEqualTo(borrowNid);
        List<BorrowTender> tenderList = borrowTenderMapper.selectByExample(borrowTenderExample);
        if (tenderList != null && tenderList.size() > 0) {
            return tenderList.get(0);
        } else {
            return null;
        }
    }
    
    
    /**
     * 更新出借记录临时表
     * 
     * @param userId
     * @param ordId
     * @return
     * @author Administrator
     */
    @Override
    public boolean updateBorrowTenderTmp(Integer userId, String borrowNid, String ordId) {
        BorrowTenderTmpExample example = new BorrowTenderTmpExample();
        BorrowTenderTmpExample.Criteria crt = example.createCriteria();
        crt.andUserIdEqualTo(userId);
        crt.andNidEqualTo(ordId);
        crt.andBorrowNidEqualTo(borrowNid);
        BorrowTenderTmp tenderTmp = new BorrowTenderTmp();
        tenderTmp.setStatus(1);
        boolean flag = this.borrowTenderTmpMapper.updateByExampleSelective(tenderTmp, example) > 0 ? true : false;
        return flag;

    }
    
    
    /**
     * 
     */
    @Override
    public boolean bidCancel(Integer investUserId, String productId, String orgOrderId, String txAmount) throws Exception {
        // 出借人的账户信息
        BankOpenAccount outCust = this.getBankOpenAccount(investUserId);
        if (outCust == null) {
            throw new Exception("出借人未开户。[出借人ID：" + investUserId + "]，" + "[出借订单号：" + orgOrderId + "]");
        }
        String tenderAccountId = outCust.getAccount();
        // 调用交易查询接口(出借撤销)
        BankCallBean queryTransStatBean = bidCancel(investUserId, tenderAccountId, productId, orgOrderId, txAmount);
        if (queryTransStatBean == null) {
            throw new Exception("调用投标申请撤销失败。" + ",[出借订单号：" + orgOrderId + "]");
        } else {
            String queryRespCode = queryTransStatBean.getRetCode();
            System.out.print("出借失败交易接口查询接口返回码：" + queryRespCode);
            // 调用接口失败时(000以外)
            if (!BankCallConstant.RESPCODE_SUCCESS.equals(queryRespCode)) {
                String message = queryTransStatBean == null ? "" : queryTransStatBean.getRetMsg();
                LogUtil.errorLog(this.getClass().getName(), "bidCancel", "调用交易查询接口(解冻)失败。" + message + ",[出借订单号：" + orgOrderId + "]", null);
                throw new Exception("调用投标申请撤销失败。" + queryRespCode + "：" + message + ",[出借订单号：" + orgOrderId + "]");
            }else if (queryRespCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST1) || queryRespCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_EXIST2)) {
                _log.info("===============冻结记录不存在,不予处理========");
                deleteBorrowTenderTmp(orgOrderId);
                return true;
            } else if (queryRespCode.equals(BankCallConstant.RETCODE_BIDAPPLY_NOT_RIGHT)) {
                _log.info("===============只能撤销投标状态为投标中的标的============");
                return false;
            } else {
                deleteBorrowTenderTmp(orgOrderId);
                return true;
            }
        }
    }
    
    /**
     * 投标失败后,调用出借撤销接口
     * 
     * @param ordId
     * @param ordDate
     * @param queryTransType
     * @return
     */
    private BankCallBean bidCancel(Integer investUserId, String investUserAccountId, String productId, String orgOrderId, String txAmount) {
        String methodName = "bidCancel";
        // 调用汇付接口(交易状态查询)
        BankCallBean bean = new BankCallBean();
        String orderId = GetOrderIdUtils.getOrderId2(investUserId);
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        Users investUser = this.getUsers(investUserId);
        bean.setVersion(BankCallConstant.VERSION_10); // 版本号(必须)
        bean.setTxCode(BankCallMethodConstant.TXCODE_BID_CANCEL); // 交易代码
        bean.setInstCode(instCode);
        bean.setBankCode(bankCode);
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6)); // 交易流水号
        bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
        bean.setAccountId(investUserAccountId);// 电子账号
        bean.setOrderId(orderId); // 订单号(必须)
        bean.setTxAmount(CustomUtil.formatAmount(txAmount));// 交易金额
        bean.setProductId(productId);// 标的号
        bean.setOrgOrderId(orgOrderId);// 原标的订单号
        bean.setLogOrderId(orderId);// 订单号
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单日期
        bean.setLogUserId(String.valueOf(investUserId));// 用户Id
        bean.setLogUserName(investUser == null ? "" : investUser.getUsername()); // 用户名
        bean.setLogRemark("投标申请撤销"); // 备注
        // 调用汇付接口
        BankCallBean chinapnrBean = BankCallUtils.callApiBg(bean);
        if (chinapnrBean == null) {
            LogUtil.errorLog(this.getClass().getName(), methodName, new Exception("调用交易状态查询接口失败![参数：" + bean.getAllParams() + "]"));
            return null;
        }
        return chinapnrBean;
    }
    private void deleteBorrowTenderTmp(String orgOrderId) {
        BorrowTenderTmpExample example =  new BorrowTenderTmpExample();
        example.createCriteria().andNidEqualTo(orgOrderId);
        this.borrowTenderTmpMapper.deleteByExample(example);
    }
    
    
    @Override
    public JSONObject getCouponIsUsed(String orderId,String couponGrantId,int userId) {
        _log.info("================cwyang 异步优先执行.开始获取优惠券信息===========");
        CouponRealTenderExample realExample = new CouponRealTenderExample();
        realExample.createCriteria().andRealTenderIdEqualTo(orderId);
        //判断coupon_real_tender 是否存在关联信息 没有则直接返回失败!
        List<CouponRealTender> realTender = this.couponRealTenderMapper.selectByExample(realExample);
        if (realTender==null||realTender.size()==0) {
            TenderCouponInfo info = new TenderCouponInfo();
            info.setIsSuccess(CustomConstants.RESULT_FAIL);
            info.setStatus(CustomConstants.RESULT_FAIL);
            String result = JSONObject.toJSONString(info);
            return JSONObject.parseObject(result);
        }
        CouponUser couponUser = this.couponUserMapper.selectByPrimaryKey(Integer.parseInt(couponGrantId));
        TenderCouponInfo info = new TenderCouponInfo();
        info.setIsSuccess(CustomConstants.RESULT_SUCCESS);
        info.setStatus(CustomConstants.RESULT_SUCCESS);
        if (couponUser != null) {
            if (CustomConstants.USER_COUPON_STATUS_USED == couponUser.getUsedFlag()) {// 优惠券状态为已使用
                CouponTenderExample example = new CouponTenderExample();
                example.createCriteria().andCouponGrantIdEqualTo(couponUser.getId());
                List<CouponTender> couponTender = this.couponTenderMapper.selectByExample(example);
                String orderID = "";
                if (couponTender != null && couponTender.size() == 1) {
                    orderID = couponTender.get(0).getOrderId();
                } else {
                    _log.info("==============获取 couponTender 信息异常!=====");
                    info.setIsSuccess(CustomConstants.RESULT_FAIL);
                    info.setStatus(CustomConstants.RESULT_FAIL);
                }
                // 优惠券出借额度
                BigDecimal couponAccount = null;
                // 优惠券类别
                int couponTypeInt = -1;
                // 优惠券面值
                BigDecimal couponQuota = null;
                // 优惠券收益
                String couponInterest = null;
                // 项目标号
                String borrowNid = null;

                if (StringUtils.isNotEmpty(orderID)) {
                    BorrowTenderCpnExample example2 = new BorrowTenderCpnExample();
                    example2.createCriteria().andNidEqualTo(orderID);
                    // 根据orderid获取优惠券的出借额度信息
                    List<BorrowTenderCpn> tenderCpn = this.borrowTenderCpnMapper.selectByExample(example2);
                    if (tenderCpn != null && tenderCpn.size() == 1) {
                        couponAccount = tenderCpn.get(0).getAccount();
                        borrowNid = tenderCpn.get(0).getBorrowNid();
                    } else {
                        _log.info("==============获取 borrowTenderCpn 信息异常!=====");
                        info.setIsSuccess(CustomConstants.RESULT_FAIL);
                        info.setStatus(CustomConstants.RESULT_FAIL);
                    }
                } else {
                    _log.info("==============获取 orderID 信息异常!=====");
                    info.setIsSuccess(CustomConstants.RESULT_FAIL);
                    info.setStatus(CustomConstants.RESULT_FAIL);
                }
                CouponConfigCustomizeV2 cuc = null;
                BigDecimal borrowApr = new BigDecimal(0);
                cuc = getCouponUser(couponGrantId, userId);
                if (cuc != null) {
                    couponTypeInt = cuc.getCouponType();
                    couponQuota = cuc.getCouponQuota();
                    borrowApr = cuc.getCouponQuota();
                } else {
                    _log.info("==============获取 CouponUser 信息异常!=====");
                    info.setIsSuccess(CustomConstants.RESULT_FAIL);
                    info.setStatus(CustomConstants.RESULT_FAIL);
                }
                // 根据项目编号获取相应的项目
                Borrow borrow = null;
                if (StringUtils.isNotEmpty(borrowNid)) {
                    borrow = getBorrowByNid(borrowNid);
                } else {
                    _log.info("==============获取 borrowNid 信息异常!=====");
                    info.setIsSuccess(CustomConstants.RESULT_FAIL);
                    info.setStatus(CustomConstants.RESULT_FAIL);
                }
                BigDecimal earnings = new BigDecimal(0);

                BigDecimal djBorrowApr = new BigDecimal(0);
                if (borrow != null) {
                    djBorrowApr = borrow.getBorrowApr();
                } else {
                    _log.info("==============获取 borrow 信息异常!=====");
                    info.setIsSuccess(CustomConstants.RESULT_FAIL);
                    info.setStatus(CustomConstants.RESULT_FAIL);
                }
                Integer borrowPeriod = borrow.getBorrowPeriod();
                DecimalFormat df = CustomConstants.DF_FOR_VIEW;
                BigDecimal couponInterestAccount = new BigDecimal(0);// 用来计算预期收益的本金
                if (couponTypeInt == 3) {// 代金券时用优惠券面值计算预期收益
                    couponInterestAccount = cuc.getCouponQuota();
                    borrowApr = borrow.getBorrowApr();
                } else {
                    couponInterestAccount = couponAccount;
                }
                _log.info("==============cweyang 优惠券出借利率是 " + borrowApr);
                if (couponTypeInt == 1) {// 体验金的预期收益
                    earnings = getInterestDj(cuc.getCouponQuota(), cuc.getCouponProfitTime(), djBorrowApr);
                    couponInterest = df.format(earnings);
                } else {// 加息券和代金券的预期收益
                    _log.info("============cwyang borrow.getBorrowStyle() is " + borrow.getBorrowStyle());
                    switch (borrow.getBorrowStyle()) {
                    case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
                        // 计算预期收益
                        earnings = DuePrincipalAndInterestUtils.getMonthInterest(couponInterestAccount, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                        couponInterest = df.format(earnings);
                        break;
                    case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*年化收益÷360*天数；
                        earnings = DuePrincipalAndInterestUtils.getDayInterest(couponInterestAccount, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                        couponInterest = df.format(earnings);
                        break;
                    case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：预期收益=出借金额*年化收益÷12*月数；
                        earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(couponInterestAccount, borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
                                BigDecimal.ROUND_DOWN);
                        couponInterest = df.format(earnings);
                        break;
                    case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：预期收益=出借金额*年化收益÷12*月数；
                        earnings = AverageCapitalPlusInterestUtils.getInterestCount(couponInterestAccount, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                        couponInterest = df.format(earnings);
                        break;
                    case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
                        earnings = AverageCapitalUtils.getInterestCount(couponInterestAccount, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                        couponInterest = df.format(earnings);
                        break;
                    default:
                        break;
                    }
                }
                info.setAccountDecimal(couponAccount.toString());
                // 优惠券收益
                info.setCouponInterest(couponInterest);
                _log.info("===================cwyang 优惠券预期收益为: " + couponInterest);
                // 优惠券类别
                String couponTypeString = "";
                if (couponTypeInt == 1) {
                    couponTypeString = "体验金";
                } else if (couponTypeInt == 2) {
                    couponTypeString = "加息券";
                } else if (couponTypeInt == 3) {
                    couponTypeString = "代金券";
                }
                info.setCouponTypeInt(couponTypeInt + "");
                info.setCouponType(couponTypeString);
                // 优惠券额度
                info.setCouponQuota(couponQuota.toString());
                // 跳转到成功画面

            } else {
                _log.info("==============未使用优惠券!=====");
                info.setIsSuccess(CustomConstants.RESULT_FAIL);
                info.setStatus(CustomConstants.RESULT_FAIL);
            }
        } else {
            _log.info("==============获取用户优惠券信息异常!=====");
            info.setIsSuccess(CustomConstants.RESULT_FAIL);
            info.setStatus(CustomConstants.RESULT_FAIL);
        }
        String result = JSONObject.toJSONString(info);
        return JSONObject.parseObject(result);
    }
    
    /**
     * 取得用户优惠券信息
     * 
     * @param couponGrantId
     * @return
     */
    public CouponConfigCustomizeV2 getCouponUser(String couponGrantId, Integer userId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("couponGrantId", couponGrantId);
        paramMap.put("userId", userId);
        CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
        return ccTemp;
    }
    
    private BigDecimal getInterestDj(BigDecimal couponQuota, Integer couponProfitTime, BigDecimal borrowApr) {
        BigDecimal earnings = new BigDecimal("0");

        earnings = couponQuota.multiply(borrowApr.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP))
                .divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(couponProfitTime))
                .setScale(2, BigDecimal.ROUND_DOWN);

        return earnings;

    }
    
    @Override
    public boolean updateTender(Integer userId, String borrowNid, String orderId, BankCallBean bean) {
        String authCode = bean.getAuthCode();// 出借结果授权码
        BorrowTenderExample example  =new BorrowTenderExample();
        example.createCriteria().andUserIdEqualTo(userId).andBorrowNidEqualTo(borrowNid).andNidEqualTo(orderId);
        BorrowTender borrowTender = new BorrowTender();
        borrowTender.setAuthCode(authCode);
        boolean tenderFlag = this.borrowTenderMapper.updateByExampleSelective(borrowTender, example)>0?true:false;

        return tenderFlag;
    }

    /**
     * 发送神策数据统计MQ
     *
     * @param sensorsDataBean
     */
    @Override
    public void sendSensorsDataMQ(SensorsDataBean sensorsDataBean) {
        // 加入到消息队列
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("eventCode", sensorsDataBean.getEventCode());
        params.put("userId", sensorsDataBean.getUserId());
        params.put("orderId", sensorsDataBean.getOrderId());
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.SENSORS_DATA_ROUTINGKEY_HZT_INVEST, JSONObject.toJSONString(params));
    }

    /**
     * 获取还款方式配置
     * @param borrowStyle
     * @return
     */
    @Override
    public BorrowStyle getProjectBorrowStyle(String borrowStyle) {
        BorrowStyleExample example = new BorrowStyleExample();
        BorrowStyleExample.Criteria cra = example.createCriteria();
        cra.andNidEqualTo(borrowStyle);
        List<BorrowStyle> list = this.borrowStyleMapper.selectByExample(example);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

}
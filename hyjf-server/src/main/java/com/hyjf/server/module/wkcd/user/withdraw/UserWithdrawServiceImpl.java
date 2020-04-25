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

package com.hyjf.server.module.wkcd.user.withdraw;

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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.http.URLCodec;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountBankExample;
import com.hyjf.mybatis.model.auto.AccountBankExample.Criteria;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.AccountwithdrawExample;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankConfigExample;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.mybatis.model.auto.ChinapnrLogExample;
import com.hyjf.mybatis.model.auto.FeeConfig;
import com.hyjf.mybatis.model.auto.FeeConfigExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.server.BaseServiceImpl;

@Service
public class UserWithdrawServiceImpl extends BaseServiceImpl implements UserWithdrawService {
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;
    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;
    @Autowired
    @Qualifier("appMsProcesser")
    private MessageProcesser appMsProcesser;

    /**
     * 根据用户ID取得该用户的提现卡
     *
     * @param userId
     * @return
     */
    @Override
    public List<AccountBank> getBankCardByUserId(Integer userId) {
        AccountBankExample accountBankExample = new AccountBankExample();
        accountBankExample.createCriteria().andUserIdEqualTo(userId).andStatusEqualTo(0);
        accountBankExample.setOrderByClause("card_type desc");
        List<AccountBank> listAccountBank = this.accountBankMapper.selectByExample(accountBankExample);
        return listAccountBank;
    }

    /**
     * 根据code取得银行信息
     *
     * @param userId
     * @return
     */
    @Override
    public BankConfig getBankInfo(String code) {
        BankConfigExample example = new BankConfigExample();
        example.createCriteria().andCodeEqualTo(code);
        List<BankConfig> listBankConfig = this.bankConfigMapper.selectByExample(example);
        if (listBankConfig != null && listBankConfig.size() > 0) {
            return listBankConfig.get(0);
        }
        return new BankConfig();
    }

    /**
     * 用户提现前处理
     *
     * @param bean
     * @return
     */
    @Override
    public int updateBeforeCash(ChinapnrBean bean, Map<String, String> params) {
        int ret = 0;
        String ordId = bean.getOrdId() == null ? bean.get(ChinaPnrConstant.PARAM_ORDID) : bean.getOrdId(); // 订单号

        AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
        accountWithdrawExample.createCriteria().andNidEqualTo(ordId);
        List<Accountwithdraw> listAccountWithdraw = this.accountwithdrawMapper.selectByExample(accountWithdrawExample);
        if (listAccountWithdraw != null && listAccountWithdraw.size() > 0) {
            return ret;
        }
        int nowTime = GetDate.getNowTime10(); // 当前时间
        BigDecimal money = new BigDecimal(bean.getTransAmt()); // 提现金额
        BigDecimal fee = BigDecimal.ZERO; // 取得费率
        // if (Validator.isNotNull(bean.getMerPriv())) {
        // fee = new BigDecimal(bean.getMerPriv()); // 取得费率
        // }

        BigDecimal total = money.add(fee); // 实际出账金额

        Integer userId = GetterUtil.getInteger(params.get("userId")); // 用户ID
        String bankId = params.get("bankId"); // 银行卡ID
        String bank = null;
        String account = null;
        // 取得银行信息
        AccountBank accountBank = getBankInfo(userId, bankId);
        if (accountBank != null) {
            bank = accountBank.getBank();
            account = accountBank.getAccount();
        }

        Accountwithdraw record = new Accountwithdraw();
        record.setUserId(userId);
        record.setNid(bean.getOrdId()); // 订单号
        record.setStatus(0); // 状态: 0:处理中
        record.setAccount(account);// 提现银行卡
        record.setBank(bank); // 提现银行
        record.setBankId(accountBank.getId());
        record.setBranch(null);
        record.setProvince(0);
        record.setCity(0);
        record.setTotal(total);
        record.setCredited(money);
        record.setFee(CustomUtil.formatAmount(fee.toString()));
        record.setAddtime(String.valueOf(nowTime));
        record.setAddip(params.get("ip"));
        record.setRemark("手机客户端提现");
        record.setClient(GetterUtil.getInteger(params.get("client"))); // 0pc

        // 插入用户提现记录表
        ret += this.accountwithdrawMapper.insertSelective(record);

        return ret;
    }

    /**
     * 用户提现回调方法
     *
     * @param bean
     * @return
     */
    @Override
    public synchronized int handlerAfterCash(ChinapnrBean bean, Map<String, String> params) {
     // 用户ID
        int userId = Integer.parseInt(params.get("userId"));
        // 查询账户信息
        AccountExample accountExample = new AccountExample();
        AccountExample.Criteria accountCriteria = accountExample.createCriteria();
        accountCriteria.andUserIdEqualTo(userId);
        Account account = this.accountMapper.selectByExample(accountExample).get(0);
        String ordId = bean.getOrdId() == null ? bean.get(ChinaPnrConstant.PARAM_ORDID) : bean.getOrdId(); // 订单号
        int nowTime = GetDate.getNowTime10(); // 当前时间

        int ret = 0;
        AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
        accountWithdrawExample.createCriteria().andNidEqualTo(ordId);
        List<Accountwithdraw> listAccountWithdraw = this.accountwithdrawMapper.selectByExample(accountWithdrawExample);
        if (listAccountWithdraw != null && listAccountWithdraw.size() > 0) {
            // 提现信息
            Accountwithdraw accountWithdraw = listAccountWithdraw.get(0);
            // 返回值=000成功/999审核中
            if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.getRespCode()) || ChinaPnrConstant.RESPCODE_CHECK.equals(bean.getRespCode())) {
                // 如果信息未被处理
                if (accountWithdraw.getStatus() == CustomConstants.WITHDRAW_STATUS_DEFAULT) {
                    // 查询是否已经处理过
                    AccountListExample accountListExample = new AccountListExample();
                    accountListExample.createCriteria().andNidEqualTo(ordId).andTradeEqualTo("cash_success");
                    int accountlistCnt = this.accountListMapper.countByExample(accountListExample);
                    // 未被处理
                    if (accountlistCnt == 0) {
                        // 开启事务
                        TransactionStatus txStatus = null;
                        try {
                            txStatus = this.transactionManager.getTransaction(transactionDefinition);
                            // 提现金额
                            BigDecimal transAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_REALTRANSAMT);
                            // 提现手续费
                            BigDecimal feeAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_FEEAMT);
                            // 支出金额
                            BigDecimal outAmt = transAmt.add(feeAmt);
                            // 商户提现手续费
                            String merpriv = bean.get(ChinaPnrConstant.PARAM_MERPRIV);

                            // 如果是快速取现收万分之五手续费
                            if (Validator.isNotNull(merpriv) && NumberUtils.isNumber(merpriv)) {
                                BigDecimal withdrawFee = new BigDecimal(merpriv);
                                // 汇付返回的手续费大于原来的手续费
                                if (feeAmt.compareTo(withdrawFee) > 0) {
                                    accountWithdraw.setRemark("快速取现");
                                }
                            }
                            // 更新订单信息
                            accountWithdraw.setFee(feeAmt.toString()); // 更新手续费
                            accountWithdraw.setCredited(transAmt); // 更新到账金额
                            accountWithdraw.setTotal(outAmt); // 更新到总额
                            if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.getRespCode())) {
                                accountWithdraw.setStatus(CustomConstants.WITHDRAW_STATUS_SUCCESS);// 4:成功
                            } else {
                                accountWithdraw.setStatus(CustomConstants.WITHDRAW_STATUS_WAIT);// 1:审核中
                            }
                            accountWithdraw.setUpdateTime(nowTime);
                            accountWithdraw.setAccount(bean.getOpenAcctId());
                            accountWithdraw.setBank(bean.getOpenBankId());
                            accountWithdraw.setReason("");
                            ret += this.accountwithdrawMapper.updateByExampleSelective(accountWithdraw, accountWithdrawExample);

                            // 返回状态为000/999时,写入收支明细
                            if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.getRespCode())) {
                                // 写入收支明细
                                AccountList accountList = new AccountList();
                                accountList.setNid(ordId);
                                accountList.setUserId(userId);
                                accountList.setAmount(outAmt);
                                accountList.setType(2);
                                accountList.setTrade("cash_success");
                                accountList.setTradeCode("balance");
                                accountList.setTotal(account.getTotal().subtract(outAmt));
                                accountList.setBalance(account.getBalance().subtract(outAmt));
                                accountList.setFrost(account.getFrost());
                                accountList.setAwait(account.getAwait());
                                accountList.setRepay(account.getRepay());
                                accountList.setRemark("网站提现");
                                accountList.setCreateTime(nowTime);
                                accountList.setBaseUpdate(nowTime);
                                accountList.setOperator(params.get("userId"));
                                accountList.setIp(params.get("ip"));
                                accountList.setIsUpdate(0);
                                accountList.setBaseUpdate(0);
                                accountList.setInterest(null);
                                accountList.setWeb(0);
                                ret += this.accountListMapper.insertSelective(accountList);
                            }
                            // 更新账户信息
                            account.setTotal(account.getTotal().subtract(outAmt)); // 累加到账户总资产
                            account.setBalance(account.getBalance().subtract(outAmt)); // 累加可用余额
                            account.setExpend(account.getExpend().add(outAmt)); // 累加到总收入
                            // 版本控制(防止事物重复提交)
                            BigDecimal version = account.getVersion();
                            accountCriteria.andVersionEqualTo(version);
                            account.setVersion(version.add(BigDecimal.ONE));
                            // 更新用户账户信息
                            if (this.accountMapper.updateByExampleSelective(account, accountExample) > 0) {
                                // 提交事务
                                this.transactionManager.commit(txStatus);
                                Users users = getUsers(userId);
                                // 可以发送提现短信时
                                if (users != null && users.getWithdrawSms() != null && users.getWithdrawSms() == 0) {
                                    // 替换参数
                                    Map<String, String> replaceMap = new HashMap<String, String>();
                                    replaceMap.put("val_amount", outAmt.toString());
                                    replaceMap.put("val_balance", feeAmt.toString());
                                    UsersInfo info = getUsersInfoByUserId(userId);
                                    replaceMap.put("val_name", info.getTruename().substring(0, 1));
                                    replaceMap.put("val_sex", info.getSex() == 2 ? "女士" : "先生");
                                    SmsMessage smsMessage = new SmsMessage(userId, replaceMap, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_TIXIAN_SUCCESS, CustomConstants.CHANNEL_TYPE_NORMAL);
                                    AppMsMessage appMsMessage = new AppMsMessage(userId, replaceMap, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_TIXIAN_SUCCESS);
                                    smsProcesser.gather(smsMessage);
                                    appMsProcesser.gather(appMsMessage);
                                }else{
                                 // 替换参数
                                    Map<String, String> replaceMap = new HashMap<String, String>();
                                    replaceMap.put("val_amount", outAmt.toString());
                                    replaceMap.put("val_balance", feeAmt.toString());
                                    UsersInfo info = getUsersInfoByUserId(userId);
                                    replaceMap.put("val_name", info.getTruename().substring(0, 1));
                                    replaceMap.put("val_sex", info.getSex() == 2 ? "女士" : "先生");
                                    AppMsMessage appMsMessage = new AppMsMessage(userId, replaceMap, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_TIXIAN_SUCCESS);
                                    appMsProcesser.gather(appMsMessage);
                                }
                            } else {
                                // 回滚事务
                                transactionManager.rollback(txStatus);
                                return 0;
                            }
                        } catch (Exception e) {
                            // 回滚事务
                            transactionManager.rollback(txStatus);
                            e.printStackTrace();
                        }
                    }
                }
                // 返回正常(000), 且当前表的状态为1:审核中
                else if (accountWithdraw.getStatus() == CustomConstants.WITHDRAW_STATUS_WAIT && ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.getRespCode())) {
                    // 开启事务
                    TransactionStatus txStatus = null;
                    try {
                        txStatus = this.transactionManager.getTransaction(transactionDefinition);
                        // 提现金额
                        BigDecimal transAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_REALTRANSAMT);
                        // 提现手续费
                        BigDecimal feeAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_FEEAMT);
                        // 支出金额
                        BigDecimal outAmt = transAmt.add(feeAmt);

                        // 更新状态为成功
                        accountWithdraw.setStatus(CustomConstants.WITHDRAW_STATUS_SUCCESS);// 4:成功
                        accountWithdraw.setUpdateTime(nowTime);
                        accountWithdraw.setAccount(bean.getOpenAcctId());
                        accountWithdraw.setBank(bean.getOpenBankId());
                        accountWithdraw.setReason("");
                        ret += this.accountwithdrawMapper.updateByExampleSelective(accountWithdraw, accountWithdrawExample);

                        // 写入收支明细
                        AccountList accountList = new AccountList();
                        accountList.setNid(ordId);
                        accountList.setUserId(userId);
                        accountList.setAmount(outAmt);
                        accountList.setType(2);
                        accountList.setTrade("cash_success");
                        accountList.setTradeCode("balance");
                        accountList.setTotal(account.getTotal());
                        accountList.setBalance(account.getBalance());
                        accountList.setFrost(account.getFrost());
                        accountList.setAwait(account.getAwait());
                        accountList.setRepay(account.getRepay());
                        accountList.setRemark("网站提现");
                        accountList.setCreateTime(nowTime);
                        accountList.setBaseUpdate(nowTime);
                        accountList.setOperator(params.get("userId"));
                        accountList.setIp(params.get("ip"));
                        accountList.setIsUpdate(0);
                        accountList.setBaseUpdate(0);
                        accountList.setInterest(null);
                        accountList.setWeb(0);
                        ret += this.accountListMapper.insertSelective(accountList);

                        // 版本控制(防止事物重复提交)
                        BigDecimal version = account.getVersion();
                        accountCriteria.andVersionEqualTo(version);
                        account.setVersion(version.add(BigDecimal.ONE));
                        // 更新用户账户信息
                        if (this.accountMapper.updateByExampleSelective(account, accountExample) > 0) {
                            // 提交事务
                            this.transactionManager.commit(txStatus);

                            Users users = getUsers(userId);
                            // 可以发送提现短信时
                            if (users != null && users.getWithdrawSms() != null && users.getWithdrawSms() == 0) {
                                // 替换参数
                                Map<String, String> replaceMap = new HashMap<String, String>();
                                replaceMap.put("val_amount", outAmt.toString());
                                replaceMap.put("val_fee", feeAmt.toString());
                                UsersInfo info = getUsersInfoByUserId(userId);
                                replaceMap.put("val_name", info.getTruename().substring(0, 1));
                                replaceMap.put("val_sex", info.getSex() == 2 ? "女士" : "先生");
                                SmsMessage smsMessage = new SmsMessage(userId, replaceMap, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_TIXIAN_SUCCESS, CustomConstants.CHANNEL_TYPE_NORMAL);
                                AppMsMessage appMsMessage = new AppMsMessage(userId, replaceMap, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_TIXIAN_SUCCESS);
                                smsProcesser.gather(smsMessage);
                                appMsProcesser.gather(appMsMessage);
                            }else{
                             // 替换参数
                                Map<String, String> replaceMap = new HashMap<String, String>();
                                replaceMap.put("val_amount", outAmt.toString());
                                replaceMap.put("val_fee", feeAmt.toString());
                                UsersInfo info = getUsersInfoByUserId(userId);
                                replaceMap.put("val_name", info.getTruename().substring(0, 1));
                                replaceMap.put("val_sex", info.getSex() == 2 ? "女士" : "先生");
                                AppMsMessage appMsMessage = new AppMsMessage(userId, replaceMap, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_TIXIAN_SUCCESS);
                                appMsProcesser.gather(appMsMessage);
                            }
                        } else {
                            // 回滚事务
                            transactionManager.rollback(txStatus);
                            return 0;
                        }
                    } catch (Exception e) {
                        // 回滚事务
                        transactionManager.rollback(txStatus);
                        e.printStackTrace();
                    }
                }
            }
            // 提现失败
            else {
                // 开启事务
                TransactionStatus txStatus = null;
                try {
                    txStatus = this.transactionManager.getTransaction(transactionDefinition);
                    // 提现金额
                    BigDecimal transAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_REALTRANSAMT);
                    // 提现手续费
                    BigDecimal feeAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_FEEAMT);
                    // 支出金额
                    BigDecimal outAmt = transAmt.add(feeAmt);

                    // 如果信息已被处理(400)
                    if ((accountWithdraw.getStatus() == CustomConstants.WITHDRAW_STATUS_SUCCESS || accountWithdraw.getStatus() == CustomConstants.WITHDRAW_STATUS_WAIT) && ChinaPnrConstant.RESPCODE_WITHDRAW_FAIL.equals(bean.getRespCode())) {

                        // // 查询是否已经处理过
                        // AccountListExample accountListExample = new
                        // AccountListExample();
                        // accountListExample.createCriteria().andNidEqualTo(ordId).andTradeEqualTo("cash_success");
                        // int accountlistCnt =
                        // this.accountListMapper.countByExample(accountListExample);
                        // // 未被处理
                        // if (accountlistCnt > 0) {
                        // ret +=
                        // this.accountListMapper.deleteByExample(accountListExample);
                        // }

                        // 写入收支明细
                        AccountList accountList = new AccountList();
                        accountList.setNid(ordId);
                        accountList.setUserId(userId);
                        accountList.setAmount(outAmt);
                        accountList.setType(1);
                        accountList.setTrade("cash_false");
                        accountList.setTradeCode("balance");
                        accountList.setTotal(account.getTotal().add(outAmt));
                        accountList.setBalance(account.getBalance().add(outAmt));
                        accountList.setFrost(account.getFrost());
                        accountList.setAwait(account.getAwait());
                        accountList.setRepay(account.getRepay());
                        accountList.setRemark("网站提现");
                        accountList.setCreateTime(nowTime);
                        accountList.setBaseUpdate(nowTime);
                        accountList.setOperator(params.get("userId"));
                        accountList.setIp(params.get("ip"));
                        accountList.setIsUpdate(0);
                        accountList.setBaseUpdate(0);
                        accountList.setInterest(null);
                        accountList.setWeb(0);
                        ret += this.accountListMapper.insertSelective(accountList);

                        account.setTotal(account.getTotal().add(outAmt)); // 累加到账户总资产
                        account.setBalance(account.getBalance().add(outAmt)); // 累加可用余额
                        account.setExpend(account.getExpend().subtract(outAmt)); // 累加到总收入

                        // 更新为失败状态
                        accountWithdraw.setStatus(CustomConstants.WITHDRAW_STATUS_DEFAULT);
                    }

                    // 更新订单信息
                    if (Validator.isNotNull(feeAmt) && feeAmt.compareTo(BigDecimal.ZERO) > 0) {
                        accountWithdraw.setFee(feeAmt.toString()); // 更新手续费
                        accountWithdraw.setCredited(transAmt); // 更新到账金额
                        accountWithdraw.setTotal(outAmt); // 更新到总额
                    }
                    if (Validator.isNotNull(bean.getOpenAcctId())) {
                        accountWithdraw.setAccount(bean.getOpenAcctId()); // 提现银行卡号
                    }
                    if (Validator.isNotNull(bean.getOpenBankId())) {
                        accountWithdraw.setBank(bean.getOpenBankId()); // 提现银行
                    }
                    accountWithdraw.setIsok(1);
                    accountWithdraw.setUpdateTime(nowTime);
                    String reason = bean.getRespDesc();
                    if (StringUtils.isNotEmpty(reason)) {
                        if (reason.contains("%")) {
                            reason = URLCodec.decodeURL(reason);
                        }
                    }
                    accountWithdraw.setReason(reason);
                    ret += this.accountwithdrawMapper.updateByExampleSelective(accountWithdraw, accountWithdrawExample);
                    // 版本控制(防止事物重复提交)
                    BigDecimal version = account.getVersion();
                    accountCriteria.andVersionEqualTo(version);
                    account.setVersion(version.add(BigDecimal.ONE));
                    // 更新用户信息
                    if (this.accountMapper.updateByExampleSelective(account, accountExample) > 0) {
                        // 提交事务
                        this.transactionManager.commit(txStatus);
                    } else {
                        // 回滚事务
                        transactionManager.rollback(txStatus);
                        return 0;
                    }
                } catch (Exception e) {
                    // 回滚事务
                    transactionManager.rollback(txStatus);
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    /**
     * 判断是否提现成功
     *
     * @param ordId
     * @return
     */
    @Override
    public Accountwithdraw getAccountWithdrawByOrdId(String ordId) {
        AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
        accountWithdrawExample.createCriteria().andNidEqualTo(ordId);
        List<Accountwithdraw> list = this.accountwithdrawMapper.selectByExample(accountWithdrawExample);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 判断是否提现成功
     *
     * @param ordId
     * @return S:成功 F:失败
     */
    @Override
    public String checkCashResult(String ordId) {
        if (Validator.isNull(ordId)) {
            return null;
        }
        // 调用汇付接口(交易状态查询)
        ChinapnrBean bean = new ChinapnrBean();
        bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
        bean.setCmdId(ChinaPnrConstant.CMDID_QUERYTRANSSTAT); // 消息类型(必须)
        bean.setOrdId(ordId); // 订单号(必须)
        bean.setOrdDate(GetOrderIdUtils.getOrderDate()); // 订单日期(必须)
        bean.setQueryTransType("CASH"); // 交易查询类型

        // 写log用参数
        bean.setLogUserId(0);
        bean.setLogRemark("交易状态查询(取现)"); // 备注
        bean.setLogClient("0"); // PC

        // 调用汇付接口
        ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
        if (chinapnrBean != null) {
            return chinapnrBean.getTransStat();
        }
        return null;
    }

    /**
     * 取得成功信息
     *
     * @param ordId
     * @return
     */
    @Override
    public JSONObject getMsgData(String ordId) {
        if (Validator.isNull(ordId)) {
            return null;
        }

        List<String> respCode = new ArrayList<String>();
        respCode.add(ChinaPnrConstant.RESPCODE_SUCCESS);
        respCode.add(ChinaPnrConstant.RESPCODE_CHECK);
        ChinapnrLogExample example = new ChinapnrLogExample();
        example.createCriteria().andOrdidEqualTo(ordId).andMsgTypeEqualTo("Cash").andRespCodeIn(respCode);
        example.setOrderByClause(" resp_code ");
        List<ChinapnrLog> list = chinapnrLogMapper.selectByExampleWithBLOBs(example);
        if (list != null && list.size() > 0) {
            String msgData = list.get(0).getMsgdata();
            if (Validator.isNotNull(msgData)) {
                try {
                    JSONObject jo = JSONObject.parseObject(msgData);
                    return jo;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 更新提现表
     *
     * @param ordId
     * @return
     */
    @Override
    public int updateAccountWithdrawByOrdId(String ordId, String reason) {
        Accountwithdraw record = new Accountwithdraw();
        record.setReason(reason);

        AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
        accountWithdrawExample.createCriteria().andNidEqualTo(ordId);

        int ret = this.accountwithdrawMapper.updateByExampleSelective(record, accountWithdrawExample);
        return ret;
    }

    /**
     * 获取用户的身份证号
     *
     * @param userId
     * @return 用户的身份证号
     */
    @Override
    public String getUserIdcard(Integer userId) {
        // 取得身份证号
        UsersInfoExample usersInfoExample = new UsersInfoExample();
        usersInfoExample.createCriteria().andUserIdEqualTo(userId);
        List<UsersInfo> listUsersInfo = this.usersInfoMapper.selectByExample(usersInfoExample);
        if (listUsersInfo != null && listUsersInfo.size() > 0) {
            return listUsersInfo.get(0).getIdcard();
        }
        return "";
    }

    /**
     * 获取用户的快捷卡信息
     *
     * @param userId
     * @return 用户的银行卡信息
     */
    @Override
    public AccountBank getBankInfo(Integer userId, String bankCode) {
        // 取得用户绑定的银行卡号
        AccountBankExample accountBankExample = new AccountBankExample();
        Criteria cri = accountBankExample.createCriteria();
        cri.andUserIdEqualTo(userId).andCardTypeEqualTo("2");
        if (StringUtils.isNotEmpty(bankCode)) {
            cri.andAccountEqualTo(bankCode);
        }
        List<AccountBank> listAccountBank = accountBankMapper.selectByExample(accountBankExample);
        if (listAccountBank != null && listAccountBank.size() > 0) {
            return listAccountBank.get(0);
        }

        return null;
    }

    /**
     * 获取用户的提现费率
     *
     * @param userId
     * @param bankId
     * @param type
     */
    @Override
    public String getWithdrawFee(Integer userId, String bankId, BigDecimal amount, String type) {
        AccountBank accountBank = getBankInfo(userId, bankId);
        if (accountBank != null) {
            String bankCode = accountBank.getBank();

            // 取得费率
            FeeConfigExample feeConfigExample = new FeeConfigExample();
            feeConfigExample.createCriteria().andBankCodeEqualTo(bankCode == null ? "" : bankCode);
            List<FeeConfig> listFeeConfig = feeConfigMapper.selectByExample(feeConfigExample);
            if (listFeeConfig != null && listFeeConfig.size() > 0) {
                FeeConfig feeConfig = listFeeConfig.get(0);
                BigDecimal takout = BigDecimal.ZERO;
                BigDecimal percent = BigDecimal.ZERO;
                // TODO 跟线上一致,全部用普通提现
                // if ("IMMEDIATE".equals(type)) {
                // if (Validator.isNotNull(feeConfig.getDirectTakeout()) &&
                // NumberUtils.isNumber(feeConfig.getDirectTakeout())) {
                // takout = new BigDecimal(feeConfig.getDirectTakeout());
                // }
                // if (Validator.isNotNull(feeConfig.getDirectTakeoutPercent())
                // && NumberUtils.isNumber(feeConfig.getDirectTakeoutPercent()))
                // {
                // percent = new
                // BigDecimal(feeConfig.getDirectTakeoutPercent()).multiply(amount).divide(new
                // BigDecimal(100));
                // }
                // } else if ("FAST".equals(type)) {
                // if (Validator.isNotNull(feeConfig.getQuickTakeout()) &&
                // NumberUtils.isNumber(feeConfig.getQuickTakeout())) {
                // takout = new BigDecimal(feeConfig.getQuickTakeout());
                // }
                // if (Validator.isNotNull(feeConfig.getQuickTakeoutPercent())
                // && NumberUtils.isNumber(feeConfig.getQuickTakeoutPercent()))
                // {
                // percent = new
                // BigDecimal(feeConfig.getQuickTakeoutPercent()).multiply(amount).divide(new
                // BigDecimal(100));
                // }
                // } else {
                if (Validator.isNotNull(feeConfig.getNormalTakeout()) && NumberUtils.isNumber(feeConfig.getNormalTakeout())) {
                    takout = new BigDecimal(feeConfig.getNormalTakeout());
                }
                // }

                BigDecimal result = takout.add(percent);
                return CustomConstants.DF_FOR_VIEW.format(result);
            }
        }
        return null;
    }
}

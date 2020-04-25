/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.server.module.wkcd.user.recharge;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.DesECBUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.mapper.auto.FeeConfigMapper;
import com.hyjf.mybatis.mapper.customize.AccountBankCustomizeMapper;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountBankExample;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.AccountRechargeExample;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.BankAccountLog;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankConfigExample;
import com.hyjf.mybatis.model.auto.BankRechargeLimitConfig;
import com.hyjf.mybatis.model.auto.BankRechargeLimitConfigExample;
import com.hyjf.mybatis.model.auto.FeeConfig;
import com.hyjf.mybatis.model.auto.FeeConfigExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.QpCardInfo;
import com.hyjf.mybatis.util.getfeefrom.GetFeeFromUtil;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.server.BaseServiceImpl;

/**
 * APP充值服务接口实现
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年2月20日
 * @see 上午9:35:54
 */
@Service
public class RechargeServiceImpl extends BaseServiceImpl implements RechargeService {
	
	Logger _log = LoggerFactory.getLogger(RechargeServiceImpl.class);
    @Autowired
    private AccountBankCustomizeMapper accountBankCustomizeAppMapper;

    @Autowired
    private FeeConfigMapper feeConfigMapper;

//    private static DecimalFormat df = new DecimalFormat("#######0.00");

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
     * 根据用户编号，获取用户的快捷卡信息
     * 
     * @author renxingchen
     * @param userId
     * @see com.hyjf.app.recharge.RechargeService#getUserQpCard(java.lang.Integer)
     */
    @Override
    public QpCardInfo getUserQpCard(Integer userId) {
        return accountBankCustomizeAppMapper.selectQpCardInfoByUserId(userId);
    }

    @Override
    public FeeConfig getQpCardFee(String code) {
        FeeConfigExample example = new FeeConfigExample();
        example.createCriteria().andBankCodeEqualTo(code);
        List<FeeConfig> feeConfigs = feeConfigMapper.selectByExample(example);
        return feeConfigs.get(0);
    }

    @Override
    public String getUserIdcard(Integer userId) {
        // 取得身份证号
        UsersInfoExample usersInfoExample = new UsersInfoExample();
        usersInfoExample.createCriteria().andUserIdEqualTo(userId);
        List<UsersInfo> listUsersInfo = this.usersInfoMapper.selectByExample(usersInfoExample);
        if (listUsersInfo != null && listUsersInfo.size() > 0) {
            return listUsersInfo.get(0).getIdcard() == null ? "" : StringUtils.upperCase(listUsersInfo.get(0)
                    .getIdcard());
        }
        return "";
    }

    @Override
    public int updateBeforeRecharge(ChinapnrBean bean, Map<String, String> params) {
        int ret = 0;
        String ordId = bean.getOrdId() == null ? bean.get(ChinaPnrConstant.PARAM_ORDID) : bean.getOrdId(); // 订单号

        AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
        accountRechargeExample.createCriteria().andNidEqualTo(ordId == null ? "" : ordId);
        List<AccountRecharge> listAccountRecharge = this.accountRechargeMapper.selectByExample(accountRechargeExample);
        if (listAccountRecharge != null && listAccountRecharge.size() > 0) {
            return ret;
        }
        int nowTime = GetDate.getNowTime10(); // 当前时间
        BigDecimal money = new BigDecimal(bean.getTransAmt()); // 充值金额
        String bankCode = bean.getOpenBankId();// 开户银行代号
        BigDecimal fee = getFee(bankCode, money, bean.getGateBusiId());// 手续费,第三个参数是充值类型

        AccountRecharge record = new AccountRecharge();
        record.setNid(bean.getOrdId()); // 订单号
        record.setUserId(GetterUtil.getInteger(params.get("userId"))); // 用户ID
        record.setUsername(params.get("username"));// 用户名
        record.setStatus(RechargeDefine.STATUS_RUNNING); // 状态 0：失败；1：成功
                                                         // 2:充值中
        record.setMoney(money); // 金额
        // 手续费扣除方式
        record.setFeeFrom(GetFeeFromUtil.getFeeFrom()+"");
        if (GetFeeFromUtil.getFeeFrom() == GetFeeFromUtil.FROMUSER) {
            // 用户出手续费
            record.setFee(fee); // 费用
            record.setDianfuFee(BigDecimal.ZERO);// 垫付费用
            BigDecimal balance = money.subtract(fee); // 到账金额
            record.setBalance(balance); // 实际到账余额
        } else {
            // 商户垫付手续费
            record.setFee(BigDecimal.ZERO); // 费用
            record.setDianfuFee(fee);// 垫付费用
            BigDecimal balance = money; // 到账金额
            record.setBalance(balance); // 实际到账余额
        }
        record.setPayment(bean.getOpenBankId()); // 所属银行
        record.setGateType(bean.getGateBusiId()); // 网关类型：QP快捷支付
        record.setType(1); // 类型.1网上充值.0线下充值
        record.setRemark("QP".equals(bean.getGateBusiId()) ? "快捷充值" : "网银充值"); // 备注
        record.setCreateTime(nowTime);
        record.setOperator(params.get("userId"));
        record.setAddtime(String.valueOf(nowTime));
        record.setAddip(params.get("ip"));
        record.setClient(GetterUtil.getInteger(params.get("client"))); // 0pc

        // 插入用户充值记录表
        ret += this.accountRechargeMapper.insertSelective(record);

        return ret;
    }

    @Override
    public int getAccountRechargeCnt(String ordId) {
        AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
        accountRechargeExample.createCriteria().andNidEqualTo(ordId).andStatusEqualTo(RechargeDefine.STATUS_SUCCESS);
        int ret = this.accountRechargeMapper.countByExample(accountRechargeExample);
        return ret;
    }

    @Override
    public int updateAccountRecharge(ChinapnrBean bean, Integer status) {
        String ordId = bean.getOrdId() == null ? bean.get(ChinaPnrConstant.PARAM_ORDID) : bean.getOrdId(); // 订单号
        int nowTime = GetDate.getNowTime10(); // 当前时间
        int ret = 0;
        // 更新订单信息
        AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
        accountRechargeExample.createCriteria().andNidEqualTo(ordId == null ? "" : ordId);
        List<AccountRecharge> listAccountRecharge = this.accountRechargeMapper.selectByExample(accountRechargeExample);
        if (listAccountRecharge != null && listAccountRecharge.size() == 1) {
            AccountRecharge accountRecharge = listAccountRecharge.get(0);
            // 更新处理状态
            accountRecharge.setStatus(status);
            accountRecharge.setUpdateTime(nowTime);
            ret += this.accountRechargeMapper.updateByPrimaryKeySelective(accountRecharge);
        }
        return ret;
    }
    
    @Override
    public int insertRechargeInfo(ChinapnrBean bean, Map<String, String> params) {
        int ret = 0;
        String ordId = bean.getOrdId() == null ? bean.get(ChinaPnrConstant.PARAM_ORDID) : bean.getOrdId(); // 订单号

        AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
        accountRechargeExample.createCriteria().andNidEqualTo(ordId == null ? "" : ordId);
        List<AccountRecharge> listAccountRecharge = this.accountRechargeMapper.selectByExample(accountRechargeExample);
        if (listAccountRecharge != null && listAccountRecharge.size() > 0) {
            return ret;
        }
        int nowTime = GetDate.getNowTime10(); // 当前时间
        BigDecimal money = new BigDecimal(bean.getTransAmt()); // 充值金额
        String bankCode = bean.getOpenBankId();// 开户银行代号
        BigDecimal fee = getFee(bankCode, money, bean.getGateBusiId());// 手续费,第三个参数是充值类型

        AccountRecharge record = new AccountRecharge();
        record.setNid(bean.getOrdId()); // 订单号
        record.setUserId(GetterUtil.getInteger(params.get("userId"))); // 用户ID
        record.setUsername(params.get("username"));// 用户 名
        record.setStatus(RechargeDefine.STATUS_RUNNING); // 状态 0：失败；1：成功
                                                             // 2:充值中
        record.setMoney(money); // 金额
        // 手续费扣除方式
        record.setFeeFrom(params.get("feeFrom"));
        if ("U".equals(record.getFeeFrom())) {
            // 用户出手续费
            record.setFee(fee); // 费用
            record.setDianfuFee(BigDecimal.ZERO);// 垫付费用
            BigDecimal balance = money.subtract(fee); // 到账金额
            record.setBalance(balance); // 实际到账余额
        } else {
            // 商户垫付手续费
            record.setFee(BigDecimal.ZERO); // 费用
            record.setDianfuFee(fee);// 垫付费用
            BigDecimal balance = money; // 到账金额
            record.setBalance(balance); // 实际到账余额
        }
        record.setPayment(bean.getOpenBankId()); // 所属银行
        record.setGateType(bean.getGateBusiId()); // 网关类型：QP快捷支付
        record.setType(1); // 类型.1网上充值.0线下充值
        // 备注
        if (bean.getGateBusiId().equals("B2C")) {
            record.setRemark("个人网银充值");
        } else if (bean.getGateBusiId().equals("B2B")) {
            record.setRemark("企业网银充值");
        } else if (bean.getGateBusiId().equals("QP")) {
            record.setRemark("快捷充值");
        }
        record.setCreateTime(nowTime);
        record.setOperator(params.get("userId"));
        record.setAddtime(String.valueOf(nowTime));
        record.setAddip(params.get("ip"));
        record.setClient(GetterUtil.getInteger(params.get("client"))); // 0pc

        // 插入用户充值记录表
        return this.accountRechargeMapper.insertSelective(record);
    }

    /**
     * 获取充值手续费
     * 
     * @param bankCode
     * @param money
     * @return
     */
    @Override
    public BigDecimal getFee(String bankCode, BigDecimal money, String gateBusiId) {
        BigDecimal fee = BigDecimal.ZERO;
        if (Validator.isNotNull(bankCode)) {
            // 取得费率
            try {
                FeeConfigExample feeConfigExample = new FeeConfigExample();
                feeConfigExample.createCriteria().andBankCodeEqualTo(bankCode);
                List<FeeConfig> listFeeConfig = feeConfigMapper.selectByExample(feeConfigExample);
                if (listFeeConfig != null && listFeeConfig.size() > 0) {
                    FeeConfig feeConfig = listFeeConfig.get(0);
                    if (gateBusiId.equals("B2C")) {
                        // 个人网银充值
                        fee = money.multiply(new BigDecimal(feeConfig.getPersonalCredit()));
                    } else if (gateBusiId.equals("B2B")) {
                        // 企业网银充值
                        fee = new BigDecimal(feeConfig.getEnterpriseCredit());
                    } else if (gateBusiId.equals("QP")) {
                        // 快捷充值
                        fee = money.multiply(new BigDecimal(feeConfig.getQuickPayment()));
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return fee;
    }

    @Override
    public ModelAndView handleRechargeInfo(String ip, String feeFrom, Integer userId, ChinapnrBean bean,
        ModelAndView modelAndView, String appKey, String secretKey) {
    	_log.info("handleRechargeInfo执行,userId : "+userId);
        // 检测响应状态
        String message = "未知异常";
        TransactionStatus txStatus = null;
        String ordId = bean.getOrdId();
        int nowTime = GetDate.getNowTime10();// 当前时间
        if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.getRespCode())) {
            // 查询用户账户,为了版本控制,必须把查询用户账户放在最前面
            AccountExample accountExample = new AccountExample();
            AccountExample.Criteria accountCriteria = accountExample.createCriteria();
            accountCriteria.andUserIdEqualTo(userId);
            Account account = this.accountMapper.selectByExample(accountExample).get(0);

            AccountRechargeExample example = new AccountRechargeExample();
            example.createCriteria().andNidEqualTo(bean.getOrdId());
            List<AccountRecharge> accountRecharges = accountRechargeMapper.selectByExample(example);// 查询充值记录
            AccountRecharge accountRecharge = accountRecharges.get(0);
            if (null != accountRecharge) {// 如果有充值记录
                BigDecimal transAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_TRANSAMT);// 充值金额
                BigDecimal feeAmt = bean.getBigDecimal(ChinaPnrConstant.PARAM_FEEAMT); // 实收手续费
                BigDecimal balance = BigDecimal.ZERO;// 到账金额
                // 判断充值记录状态
                if (RechargeDefine.STATUS_SUCCESS.equals(accountRecharge.getStatus())) {// 如果已经是成功状态
                	_log.info("handleRechargeInfo,userId : "+userId+" 充值记录已经是成功状态");
                    modelAndView = new ModelAndView(RechargeDefine.RECHARGE_JSP);
                    modelAndView.addObject("status", 0);
                    modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
                    modelAndView.addObject("responseObject", DesECBUtil.encrypt(handleDifFeeFrom(feeFrom, transAmt, feeAmt, userId, bean.getOpenAcctId(), bean.getOpenBankId(), accountRecharge.getAddtime()).toString(), secretKey));
                } else {// 如果不是成功状态
                    try {
                        // 开启事务
                        txStatus = this.transactionManager.getTransaction(transactionDefinition);
                        // 将数据封装更新至充值记录
                        AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
                        accountRechargeExample.createCriteria().andNidEqualTo(ordId == null ? "" : ordId);
                        if ("U".equals(feeFrom)) {
                            // 用户出手续费
                            accountRecharge.setFee(feeAmt); // 费用
                            accountRecharge.setDianfuFee(BigDecimal.ZERO);
                            balance = transAmt.subtract(feeAmt);
                            accountRecharge.setBalance(balance); // 实际到账余额
                        } else {
                            // 商户垫付手续费
                            accountRecharge.setFee(BigDecimal.ZERO); // 费用
                            accountRecharge.setDianfuFee(feeAmt);
                            balance = transAmt; // 到账金额
                            accountRecharge.setBalance(balance);// 实际到账余额
                        }
                        accountRecharge.setGateType(bean.get(ChinaPnrConstant.PARAM_GATEBUSIID));
                        accountRecharge.setPayment(bean.getGateBankId());
                        accountRecharge.setCardid(bean.getCardId());
                        accountRecharge.setUpdateTime(nowTime);
                        accountRecharge.setStatus(RechargeDefine.STATUS_SUCCESS);
                        accountRecharge.setMessage("");
                        this.accountRechargeMapper.updateByExampleSelective(accountRecharge, accountRechargeExample);
                        // 如果需要绑卡
                        if ("QP".equals(bean.getGateBusiId()) && Validator.isNotNull(bean.getGateBankId())
                                && Validator.isNotNull(bean.getCardId())) {
                        	_log.info("handleRechargeInfo执行,userId : "+userId+" 需要绑卡");
                            AccountBankExample accountBankExample = new AccountBankExample();
                            AccountBankExample.Criteria accountBankCriteria = accountBankExample.createCriteria();
                            accountBankCriteria.andUserIdEqualTo(userId).andStatusEqualTo(0)
                                    .andBankNotEqualTo(bean.getGateBankId()).andAccountNotEqualTo(bean.getCardId());
                            // 将其余的快捷支付卡改成普通卡
                            AccountBank accountBank = new AccountBank();
                            accountBank.setStatus(1); // 禁用
                            this.accountBankMapper.updateByExampleSelective(accountBank, accountBankExample);

                            accountBankExample = new AccountBankExample();
                            accountBankExample.createCriteria().andUserIdEqualTo(userId)
                                    .andBankEqualTo(bean.getGateBankId()).andAccountEqualTo(bean.getCardId())
                                    .andStatusEqualTo(0);
                            List<AccountBank> listAccountBank =
                                    this.accountBankMapper.selectByExample(accountBankExample);
                            if (listAccountBank == null || listAccountBank.size() == 0) {
                                accountBank = new AccountBank();
                                accountBank.setUserId(userId);
                                accountBank.setStatus(0);
                                accountBank.setCardType("2"); // 快捷卡
                                accountBank.setAccount(bean.getCardId());// 银行卡账号
                                accountBank.setBank(bean.getGateBankId());
                                accountBank.setAddtime(String.valueOf(nowTime));
                                accountBank.setAddip(bean.getLogIp());
                                accountBank.setRespcode(bean.getRespCode());
                                accountBank.setRespdesc(bean.getRespDesc());
                                accountBank.setUpdateTime("");
                                accountBank.setUpdateIp("");
                                this.accountBankMapper.insert(accountBank);
                                
                                // 插入操作记录表
                                BankAccountLog bankAccountLog = new BankAccountLog();
                                bankAccountLog.setUserId(accountBank.getUserId());
                                bankAccountLog.setUserName(this.getUsers(accountBank.getUserId()).getUsername());
                                bankAccountLog.setBankCode(accountBank.getBank());
                                bankAccountLog.setBankAccount(accountBank.getAccount());
                                // 获取银行卡姓名
                                BankConfig config = this.getBankcardConfig(bankAccountLog.getBankCode());
                                if (config != null) {
                                    bankAccountLog.setBankName(config.getName());
                                } else {
                                    bankAccountLog.setBankName("");
                                }
                                bankAccountLog.setCardType(2);// 卡类型
                                                              // 0普通提现卡1默认卡2快捷支付卡
                                bankAccountLog.setOperationType(0);// 操作类型 0绑定
                                                                   // 1删除
                                bankAccountLog.setStatus(0);// 成功
                                bankAccountLog.setCreateTime(nowTime);// 操作时间
                                this.bankAccountLogMapper.insert(bankAccountLog);
                            } else {
                                accountBank = listAccountBank.get(0);
                                // 非快捷卡时更新成快捷卡
                                if (!"2".equals(accountBank.getCardType())) {
                                    accountBank.setCardType("2"); // 快捷卡
                                    this.accountBankMapper.updateByExampleSelective(accountBank, accountBankExample);
                                }
                            }
                        }
                        account.setTotal(account.getTotal().add(balance)); // 累加到账户总资产
                        account.setBalance(account.getBalance().add(balance)); // 累加可用余额
                        account.setIncome(account.getIncome().add(balance)); // 累加到总收入
                        if (null == account.getRecMoney()) {
                            account.setRecMoney(transAmt);
                        } else {
                            account.setRecMoney(account.getRecMoney().add(transAmt)); // 新充值资金更新
                        }
                        BigDecimal version = account.getVersion();
                        accountCriteria.andVersionEqualTo(version);
                        account.setVersion(version.add(BigDecimal.ONE));
                        if (accountRecharge.getFeeFrom() == null || "U".equals(accountRecharge.getFeeFrom())) {
                            account.setFee(account.getFee().add(feeAmt)); // 待返手续费更新
                        } else {
                            // 增加网站收支记录(在这里将手续费的流入流出修正)
                            AccountWebList accountWebList = new AccountWebList();
                            accountWebList.setOrdid(accountRecharge.getNid());// 订单号
                            accountWebList.setUserId(userId); // 出借者
                            UsersInfoExample usersInfoExample = new UsersInfoExample();
                            usersInfoExample.createCriteria().andUserIdEqualTo(userId);
                            List<UsersInfo> usersInfos = usersInfoMapper.selectByExample(usersInfoExample);
                            if (usersInfos != null && usersInfos.size() > 0) {
                                accountWebList.setTruename(usersInfos.get(0).getTruename());
                            }
                            accountWebList.setAmount(feeAmt); // 管理费
                            accountWebList.setType(CustomConstants.TYPE_OUT); // 支出
                            accountWebList.setTrade(CustomConstants.TRADE_RECHAFEE); // 充值
                            accountWebList.setTradeType(CustomConstants.TRADE_RECHAFEE_DF); // 充值垫付手续费
                            accountWebList.setRemark(accountRecharge.getNid());
                            accountWebList.setFlag(1);
                            accountWebList.setCreateTime(GetterUtil.getInteger(nowTime));
                            this.accountWebListMapper.insert(accountWebList);
                        }
                        // 生成交易明细
                        AccountList accountList = new AccountList();
                        accountList.setNid(ordId);
                        accountList.setUserId(userId);
                        accountList.setAmount(balance);
                        accountList.setType(1);
                        accountList.setTrade("recharge");
                        accountList.setTradeCode("balance");
                        accountList.setTotal(account.getTotal());
                        accountList.setBalance(account.getBalance());
                        accountList.setFrost(account.getFrost());
                        accountList.setAwait(account.getAwait());
                        accountList.setRepay(account.getRepay());
                        accountList.setRemark("QP".equals(bean.getGateBusiId()) ? "快捷充值" : "网银充值");
                        accountList.setCreateTime(nowTime);
                        accountList.setBaseUpdate(nowTime);
                        accountList.setOperator(userId + "");
                        accountList.setIp(ip);
                        accountList.setIsUpdate(0);
                        accountList.setBaseUpdate(0);
                        accountList.setInterest(null);
                        accountList.setWeb(0);
                        this.accountListMapper.insertSelective(accountList);// 插入交易明细
                        // 更新用户账户信息
                        if (this.accountMapper.updateByExampleSelective(account, accountExample) > 0) {
                            // 提交事务
                            this.transactionManager.commit(txStatus);
                            // 如果需要短信
                            Users users = usersMapper.selectByPrimaryKey(userId);
                            // 可以发送充值短信时
                            if (users != null && users.getRechargeSms() != null && users.getRechargeSms() == 0) {
                                // 替换参数
                                Map<String, String> replaceMap = new HashMap<String, String>();
                                replaceMap.put("val_amount", balance.toString());
                                replaceMap.put("val_fee", feeAmt.toString());
                               UsersInfo info = getUsersInfoByUserId(userId);
                                replaceMap.put("val_name", info.getTruename().substring(0, 1));
                                replaceMap.put("val_sex", info.getSex() == 2 ? "女士" : "先生");
                                SmsMessage smsMessage =                                        new SmsMessage(userId, replaceMap, null, null, MessageDefine.SMSSENDFORUSER,
                                                null, CustomConstants.PARAM_TPL_CHONGZHI_SUCCESS, CustomConstants.CHANNEL_TYPE_NORMAL);
                                AppMsMessage appMsMessage =
                                        new AppMsMessage(userId, replaceMap, null, MessageDefine.APPMSSENDFORUSER,
                                                CustomConstants.JYTZ_TPL_CHONGZHI_SUCCESS);
                                smsProcesser.gather(smsMessage);
                                appMsProcesser.gather(appMsMessage);
                            } else {
                             // 替换参数
                                Map<String, String> replaceMap = new HashMap<String, String>();
                                replaceMap.put("val_amount", balance.toString());
                                replaceMap.put("val_fee", feeAmt.toString());
                                UsersInfo info = getUsersInfoByUserId(userId);
                                replaceMap.put("val_name", info.getTruename().substring(0, 1));
                                replaceMap.put("val_sex", info.getSex() == 2 ? "女士" : "先生");
                                AppMsMessage appMsMessage =
                                        new AppMsMessage(userId, replaceMap, null, MessageDefine.APPMSSENDFORUSER,
                                                CustomConstants.JYTZ_TPL_CHONGZHI_SUCCESS);
                                appMsProcesser.gather(appMsMessage);
                            }
                            modelAndView = new ModelAndView(RechargeDefine.RECHARGE_JSP);
                            modelAndView.addObject("status", 0);
                            modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
                            modelAndView.addObject("responseObject", DesECBUtil.encrypt(handleDifFeeFrom(feeFrom, transAmt, feeAmt, userId, bean.getOpenAcctId(), bean.getOpenBankId(), accountRecharge.getAddtime()).toString(), secretKey));
                        } else {
                            // 回滚事务
                            transactionManager.rollback(txStatus);
                            // 查询充值交易状态
                            accountRecharges = accountRechargeMapper.selectByExample(example);// 查询充值记录
                            accountRecharge = accountRecharges.get(0);
                            if (RechargeDefine.STATUS_SUCCESS.equals(accountRecharge.getStatus())) {
                                modelAndView = new ModelAndView(RechargeDefine.RECHARGE_JSP);
                                modelAndView.addObject("status", 0);
                                modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
                                modelAndView.addObject("responseObject", DesECBUtil.encrypt(handleDifFeeFrom(feeFrom, transAmt, feeAmt, userId, bean.getOpenAcctId(), bean.getOpenBankId(), accountRecharge.getAddtime()).toString(), secretKey));
                            } else {
                                // 账户数据过期，请查看交易明细 跳转中间页
                                modelAndView.setViewName(RechargeDefine.RECHARGE_JSP);
                                modelAndView.addObject("status", 1);
                                modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
                                modelAndView.addObject("statusDesc", "账户数据过期，请查看交易明细");
                            }
                        }
                    } catch (Exception e) {
                    	_log.info("handleRechargeInfo---"+e.getMessage());
                        // 回滚事务
                        transactionManager.rollback(txStatus);
                        modelAndView.setViewName(RechargeDefine.RECHARGE_JSP);
                        modelAndView.addObject("status", 1);
                        modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
                        modelAndView.addObject("statusDesc", e.getMessage());
                    }
                }
            } else {// 没有充值记录
                message = "没有相应的充值记录";
                _log.info("handleRechargeInfo---"+message);
                modelAndView.setViewName(RechargeDefine.RECHARGE_JSP);
                modelAndView.addObject("status", 1);
                modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
                modelAndView.addObject("statusDesc", message);
            }
        } else {
            if (StringUtils.equals(ChinaPnrConstant.RESPCODE_YUE0_FAIL, bean.getRespCode())
                    || StringUtils.equals(ChinaPnrConstant.RESPCODE_YUE1_FAIL, bean.getRespCode())
                    || StringUtils.equals(ChinaPnrConstant.RESPCODE_YUE2_FAIL, bean.getRespCode())) {
                sendSmsFail();
            }
            try {
                if (null != bean.getRespDesc()) {
                    message = URLDecoder.decode(bean.getRespDesc(), "utf-8");
                }
                if (null != bean.getSecRespDesc()) {
                    message = message + ";" + URLDecoder.decode(bean.getSecRespDesc(), "utf-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 更新订单信息
            AccountRechargeExample example = new AccountRechargeExample();
            example.createCriteria().andNidEqualTo(ordId);
            List<AccountRecharge> accountRecharges = this.accountRechargeMapper.selectByExample(example);
            if (accountRecharges != null && accountRecharges.size() == 1) {
                AccountRecharge accountRecharge = accountRecharges.get(0);
                if (RechargeDefine.STATUS_RUNNING.equals(accountRecharge.getStatus())) {
                    // 更新处理状态
                    accountRecharge.setStatus(0);
                    accountRecharge.setUpdateTime(nowTime);
                    accountRecharge.setMessage(message);
                    this.accountRechargeMapper.updateByPrimaryKeySelective(accountRecharge);
                }
            }
            modelAndView.setViewName(RechargeDefine.RECHARGE_JSP);
            modelAndView.addObject("status", 1);
            modelAndView.addObject("secretKey", DesECBUtil.encrypt(secretKey, appKey));
            modelAndView.addObject("statusDesc", message);
        }
        return modelAndView;
    }

    /**
     * 商户余额不足 短信通知
     */
    private void sendSmsFail() {
        try {
/*            SmsNoticeConfigWithBLOBs smsNoticeConfig = SmsUtil.getNoticeConfig(CustomConstants.PARAM_TPL_RECHARGE_YUE);
            SmsUtil.sendMessage(smsNoticeConfig.getValue(), smsNoticeConfig.getContent(), smsNoticeConfig.getTitle(),
                    null);*/
            SmsMessage smsMessage =
                    new SmsMessage(null, null, null, null, MessageDefine.SMSSENDFORMANAGER, null,
                            CustomConstants.PARAM_TPL_RECHARGE_YUE, CustomConstants.CHANNEL_TYPE_NORMAL);
            smsProcesser.gather(smsMessage);
        } catch (Exception e) {
            LogUtil.debugLog(this.getClass().toString(), "sendSmsFail", e.getMessage());
        }
    }

    /**
     * 
     * 返回结果JSON对象
     * @author yyc
     * @param feeFrom 手续费用收取对象
     * @param transAmt 充值金额
     * @param feeAmt 手续费用
     * @param userId 用户编号
     * @param cardNo 银行卡号
     * @param code 银行编号
     * @param addtime 添加时间
     * @return
     */
    private JSONObject handleDifFeeFrom(String feeFrom, BigDecimal transAmt, BigDecimal feeAmt, Integer userId, String cardNo, String code, String addtime) {
        JSONObject ret = new JSONObject();
        if ("U".equals(feeFrom)) {
            ret.put("sn", "Recharge");
            ret.put("userId", userId);
            ret.put("money", CustomConstants.DF_FOR_VIEW.format(transAmt));
            ret.put("cardNo", cardNo);
            ret.put("code", code);
            ret.put("balance", CustomConstants.DF_FOR_VIEW.format(transAmt.subtract(feeAmt)));
            ret.put("fee", feeAmt);
            ret.put("addtime", addtime);
        } else {
            ret.put("sn", "Recharge");
            ret.put("userId", userId);
            ret.put("money", CustomConstants.DF_FOR_VIEW.format(transAmt));
            ret.put("cardNo", cardNo);
            ret.put("code", code);
            ret.put("balance", CustomConstants.DF_FOR_VIEW.format(transAmt));
            ret.put("fee", feeAmt);
            ret.put("addtime", addtime);
        }
        return ret;
    }

    @Override
    public List<AccountChinapnr> getUserChinapnr(String usrCustId) {
        AccountChinapnrExample example = new AccountChinapnrExample();
        example.createCriteria().andChinapnrUsrcustidEqualTo(Long.parseLong(usrCustId));
        return this.accountChinapnrMapper.selectByExample(example);
    }

    /**
     * 获取银行列表(快捷支付卡)
     * 
     * @return
     * @author Michael
     */

    @Override
    public List<BankConfig> getBankQuickList() {
        BankConfigExample example = new BankConfigExample();
        BankConfigExample.Criteria cra = example.createCriteria();
        cra.andQuickPaymentEqualTo(1);// 支持快捷支付
        example.setOrderByClause(" id");
        return bankConfigMapper.selectByExample(example);
    }

    /**
     * 获取充值银行卡限额配置
     * 
     * @return
     * @author Michael
     */

    @Override
    public List<BankRechargeLimitConfig> getRechargeLimitList() {
        BankRechargeLimitConfigExample example = new BankRechargeLimitConfigExample();
        BankRechargeLimitConfigExample.Criteria cra = example.createCriteria();
        cra.andStatusEqualTo(0);// 启用
        // 条件查询
        example.setOrderByClause("create_time");
        return bankRechargeLimitConfigMapper.selectByExample(example);
    }
    

    /**
     * 获取银行列表
     * 
     * @param string
     * @return
     * @author Administrator
     */
    public BankConfig getBankcardConfig(String bankCode) {
        BankConfigExample example = new BankConfigExample();
        BankConfigExample.Criteria cra = example.createCriteria();
        if (StringUtils.isNotEmpty(bankCode)) {
            cra.andCodeEqualTo(bankCode);
        } else {
            return null;
        }
        List<BankConfig> banks = bankConfigMapper.selectByExample(example);
        if (banks != null && banks.size() > 0) {
            return banks.get(0);
        }
        return null;
    }
}

package com.hyjf.bank.service.user.directrecharge;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.AccountRechargeExample;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class DirectRechargeServerImpl extends BaseServiceImpl implements DirectRechargeServer {

    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;
    
    // 充值状态:充值中
    private static final int RECHARGE_STATUS_WAIT = 1;
    // 充值状态:失败
    private static final int RECHARGE_STATUS_FAIL = 3;
    // 充值状态:成功
    private static final int RECHARGE_STATUS_SUCCESS = 2;
    
    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;

    @Autowired
    @Qualifier("appMsProcesser")
    private MessageProcesser appMsProcesser;

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
    
    @Override
    public ModelAndView insertGetMV(UserDirectRechargeBean rechargeBean) throws Exception {
        ModelAndView mv = new ModelAndView();
        // 充值订单号
        String logOrderId = GetOrderIdUtils.getOrderId2(rechargeBean.getUserId());
        // 充值订单日期
        String orderDate = GetOrderIdUtils.getOrderDate();
        // 调用 2.3.4联机绑定卡到电子账户充值
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_DIRECT_RECHARGE_PAGE);// 交易代码
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
        bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
        bean.setChannel(rechargeBean.getChannel()); // 交易渠道
        bean.setAccountId(rechargeBean.getAccountId()); // 电子账号
        bean.setCardNo(rechargeBean.getCardNo()); // 银行卡号
        bean.setCurrency(BankCallConstant.CURRENCY_TYPE_RMB);
        bean.setTxAmount(rechargeBean.getTxAmount());
        bean.setIdType(BankCallConstant.ID_TYPE_IDCARD); // 证件类型
        bean.setIdNo(rechargeBean.getIdNo()); // 身份证号
        bean.setName(rechargeBean.getName()); // 姓名
        bean.setMobile(rechargeBean.getMobile()); // 手机号
        bean.setForgotPwdUrl(rechargeBean.getForgotPwdUrl());
        bean.setUserIP(rechargeBean.getIp());
        bean.setRetUrl(rechargeBean.getRetUrl());
        bean.setNotifyUrl(rechargeBean.getNotifyUrl());
        bean.setLogOrderId(logOrderId);// 订单号
        bean.setLogOrderDate(orderDate);// 充值订单日期
        bean.setLogUserId(String.valueOf(rechargeBean.getUserId()));
        bean.setLogUserName(rechargeBean.getUserName());
        bean.setLogRemark("充值页面");
        bean.setLogClient(Integer.parseInt(rechargeBean.getPlatform()));// 充值平台
        // 充值成功后跳转的url
        bean.setSuccessfulUrl(bean.getRetUrl()+"&isSuccess=1");
        // 页面调用必须传的
        bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_DIRECT_RECHARGE_PAGE);
        // 插入充值记录
        int result = insertRechargeInfo(bean);
        if (result == 0) {
            throw new Exception("插入充值记录失败,userid=["+rechargeBean.getUserId()+"].accountid=["+rechargeBean.getAccountId()+"]");
        }
        // 跳转到汇付天下画面
        try {
            mv = BankCallUtils.callApi(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }
    
    public int insertRechargeInfo(BankCallBean bean) {
        int ret = 0;
        String ordId = bean.getLogOrderId() == null ? "" : bean.getLogOrderId(); // 订单号
        AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
        accountRechargeExample.createCriteria().andNidEqualTo(ordId == null ? "" : ordId);
        List<AccountRecharge> listAccountRecharge = this.accountRechargeMapper.selectByExample(accountRechargeExample);
        if (listAccountRecharge != null && listAccountRecharge.size() > 0) {
            return ret;
        }
        int nowTime = GetDate.getNowTime10(); // 当前时间
        // 银行卡号
        String cardNo = bean.getCardNo();
        // 根据银行卡号检索银行卡信息
        BankCard bankCard = this.getBankCardByCardNo(Integer.parseInt(bean.getLogUserId()), cardNo);
        BigDecimal money = new BigDecimal(bean.getTxAmount()); // 充值金额
        AccountRecharge record = new AccountRecharge();
        record.setNid(bean.getLogOrderId()); // 订单号
        record.setUserId(Integer.parseInt(bean.getLogUserId())); // 用户ID
        record.setUsername(bean.getLogUserName());// 用户 名
        record.setTxDate(Integer.parseInt(bean.getTxDate()));// 交易日期
        record.setTxTime(Integer.parseInt(bean.getTxTime()));// 交易时间
        record.setSeqNo(Integer.parseInt(bean.getSeqNo())); // 交易流水号
        record.setBankSeqNo(bean.getTxDate() + bean.getTxTime() + bean.getSeqNo()); // 交易日期+交易时间+交易流水号
        record.setStatus(RECHARGE_STATUS_WAIT); // 充值状态:0:初始,1:充值中,2:充值成功,3:充值失败
        record.setAccountId(bean.getAccountId());// 电子账号
        record.setMoney(money); // 金额
        record.setCardid(cardNo);// 银行卡号
        record.setFeeFrom(null);// 手续费扣除方式
        record.setFee(BigDecimal.ZERO); // 费用
        record.setDianfuFee(BigDecimal.ZERO);// 垫付费用
        record.setBalance(money); // 实际到账余额
        record.setPayment(bankCard == null ? "" : bankCard.getBank()); // 所属银行
        record.setGateType("QP"); // 网关类型：QP快捷支付
        record.setType(1); // 类型.1网上充值.0线下充值
        record.setRemark("快捷充值");// 备注
        record.setCreateTime(nowTime);
        record.setOperator(bean.getLogUserId());
        record.setAddtime(String.valueOf(nowTime));
        record.setAddip(bean.getUserIP());
        record.setClient(bean.getLogClient()); // 0pc
        record.setIsBank(1);// 资金托管平台 0:汇付,1:江西银行
        // 插入用户充值记录表
        return this.accountRechargeMapper.insertSelective(record);
    }
    
    private BankCard getBankCardByCardNo(Integer userId, String cardNo) {
        BankCardExample example = new BankCardExample();
        BankCardExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);// 用户Id
        cra.andCardNoEqualTo(cardNo);// 银行卡号
        cra.andStatusEqualTo(1);// 银行卡是否有效 0无效 1有效
        List<BankCard> bankCardList = bankCardMapper.selectByExample(example);
        if (bankCardList != null && bankCardList.size() > 0) {
            return bankCardList.get(0);
        }
        return null;
    }
    
    //用户充值后处理
    public JSONObject handleRechargeInfo(BankCallBean bean, Map<String, String> params) {
        TransactionStatus txStatus = null;
        // 用户Id
        Integer userId = Integer.parseInt(bean.getLogUserId());
        // 充值订单号
        String orderId = bean.getLogOrderId();
        // 当前时间
        int nowTime = GetDate.getNowTime10();
        // 错误信息
        String errorMsg = this.getBankRetMsg(bean.getRetCode());
        // ip
        String ip = params.get("ip");
        String mobile = params.get("mobile");
        // 交易日期
        String txDate = bean.getTxDate();
        // 交易时间
        String txTime = bean.getTxTime();
        // 交易流水号
        String seqNo = bean.getSeqNo();
        // 电子账户
        String accountId = bean.getAccountId();
        // 充值成功
        if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            // 查询用户账户,为了版本控制,必须把查询用户账户放在最前面
            AccountExample accountExample = new AccountExample();
            AccountExample.Criteria accountCriteria = accountExample.createCriteria();
            accountCriteria.andUserIdEqualTo(userId);
            Account account = this.accountMapper.selectByExample(accountExample).get(0);
            // 查询充值记录
            AccountRechargeExample example = new AccountRechargeExample();
            example.createCriteria().andNidEqualTo(orderId);
            List<AccountRecharge> accountRecharges = accountRechargeMapper.selectByExample(example);// 查询充值记录
            AccountRecharge accountRecharge = accountRecharges.get(0);
            // 如果没有充值记录
            if (accountRecharge != null) {
                // add by cwyang 增加redis防重校验 2017-08-02
                boolean reslut = RedisUtils.tranactionSet("recharge_orderid" + orderId, 10);
                if (!reslut) {
                    return jsonMessage("充值成功", "0");
                }
                // end
                // 交易金额
                BigDecimal txAmount = bean.getBigDecimal(BankCallConstant.PARAM_TXAMOUNT);
                // 判断充值记录状态
                if (accountRecharge.getStatus() == RECHARGE_STATUS_SUCCESS) {
                    // 如果已经是成功状态
                    return jsonMessage("充值成功", "0");
                } else {
                    // 如果不是成功状态
                    try {
                        // 开启事务
                        txStatus = this.transactionManager.getTransaction(transactionDefinition);
                        // 将数据封装更新至充值记录
                        AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
                        accountRechargeExample.createCriteria().andNidEqualTo(orderId).andStatusEqualTo(accountRecharge.getStatus());
                        accountRecharge.setFee(BigDecimal.ZERO); // 费用
                        accountRecharge.setDianfuFee(BigDecimal.ZERO);// 商户垫付金额
                        accountRecharge.setBalance(txAmount);// 实际到账余额
                        accountRecharge.setUpdateTime(nowTime);// 更新时间
                        accountRecharge.setStatus(RECHARGE_STATUS_SUCCESS);// 充值状态:0:初始,1:充值中,2:充值成功,3:充值失败
                        accountRecharge.setAccountId(accountId);// 电子账户
                        accountRecharge.setBankSeqNo(txDate + txTime + seqNo);// 交易流水号
                        boolean isAccountRechargeFlag = this.accountRechargeMapper.updateByExampleSelective(accountRecharge, accountRechargeExample) > 0 ? true : false;
                        if (!isAccountRechargeFlag) {
                            throw new Exception("充值后,回调更新充值记录表失败!" + "充值订单号:" + orderId + ".用户ID:" + userId);
                        }
                        Account newAccount = new Account();
                        // 更新账户信息
                        newAccount.setUserId(userId);// 用户Id
                        newAccount.setBankTotal(txAmount); // 累加到账户总资产
                        newAccount.setBankBalance(txAmount); // 累加可用余额
                        newAccount.setBankBalanceCash(txAmount);// 银行账户可用户
                        boolean isAccountUpdateFlag = this.adminAccountCustomizeMapper.updateBankRechargeSuccess(newAccount) > 0 ? true : false;
                        if (!isAccountUpdateFlag) {
                            throw new Exception("提现后,更新用户Account表失败!");
                        }
                        // 重新获取用户账户信息
                        account = this.getAccount(userId);
                        // 生成交易明细
                        AccountList accountList = new AccountList();
                        accountList.setNid(orderId);
                        accountList.setUserId(userId);
                        accountList.setAmount(txAmount);
                        accountList.setTxDate(Integer.parseInt(bean.getTxDate()));// 交易日期
                        accountList.setTxTime(Integer.parseInt(bean.getTxTime()));// 交易时间
                        accountList.setSeqNo(bean.getSeqNo());// 交易流水号
                        accountList.setBankSeqNo((bean.getTxDate() + bean.getTxTime() + bean.getSeqNo()));
                        accountList.setType(1);
                        accountList.setTrade("recharge");
                        accountList.setTradeCode("balance");
                        accountList.setAccountId(accountId);
                        accountList.setBankTotal(account.getBankTotal()); // 银行总资产
                        accountList.setBankBalance(account.getBankBalance()); // 银行可用余额
                        accountList.setBankFrost(account.getBankFrost());// 银行冻结金额
                        accountList.setBankWaitCapital(account.getBankWaitCapital());// 银行待还本金
                        accountList.setBankWaitInterest(account.getBankWaitInterest());// 银行待还利息
                        accountList.setBankAwaitCapital(account.getBankAwaitCapital());// 银行待收本金
                        accountList.setBankAwaitInterest(account.getBankAwaitInterest());// 银行待收利息
                        accountList.setBankAwait(account.getBankAwait());// 银行待收总额
                        accountList.setBankInterestSum(account.getBankInterestSum()); // 银行累计收益
                        accountList.setBankInvestSum(account.getBankInvestSum());// 银行累计出借
                        accountList.setBankWaitRepay(account.getBankWaitRepay());// 银行待还金额
                        accountList.setPlanBalance(account.getPlanBalance());//汇计划账户可用余额
                        accountList.setPlanFrost(account.getPlanFrost());
                        accountList.setTotal(account.getTotal());
                        accountList.setBalance(account.getBalance());
                        accountList.setFrost(account.getFrost());
                        accountList.setAwait(account.getAwait());
                        accountList.setRepay(account.getRepay());
                        accountList.setRemark("快捷充值");
                        accountList.setCreateTime(nowTime);
                        accountList.setBaseUpdate(nowTime);
                        accountList.setOperator(String.valueOf(userId));
                        accountList.setIp(ip);
                        accountList.setIsUpdate(0);
                        accountList.setBaseUpdate(0);
                        accountList.setInterest(null);
                        accountList.setWeb(0);
                        accountList.setIsBank(1);// 是否是银行的交易记录 0:否 ,1:是
                        accountList.setCheckStatus(0);// 对账状态0：未对账 1：已对账
                        accountList.setTradeStatus(1);// 成功状态
                        // 插入交易明细
                        boolean isAccountListUpdateFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
                        if (isAccountListUpdateFlag) {
                            // 提交事务
                            this.transactionManager.commit(txStatus);

                            // updateBankMobile 除了异常不管
                            updateBankMobile(userId,accountRecharge.getCardid(),mobile);
                            // 如果需要短信
                            Users users = usersMapper.selectByPrimaryKey(userId);
                            // 可以发送充值短信时
                            if (users != null && users.getRechargeSms() != null && users.getRechargeSms() == 0) {
                                // 替换参数
                                Map<String, String> replaceMap = new HashMap<String, String>();
                                replaceMap.put("val_amount", txAmount.toString());
                                replaceMap.put("val_fee", "0");
                                UsersInfo info = getUsersInfoByUserId(userId);
                                replaceMap.put("val_name", info.getTruename().substring(0, 1));
                                replaceMap.put("val_sex", info.getSex() == 2 ? "女士" : "先生");
                                SmsMessage smsMessage = new SmsMessage(userId, replaceMap, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_CHONGZHI_SUCCESS,
                                        CustomConstants.CHANNEL_TYPE_NORMAL);
                                AppMsMessage appMsMessage = new AppMsMessage(userId, replaceMap, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_CHONGZHI_SUCCESS);
                                smsProcesser.gather(smsMessage);
                                appMsProcesser.gather(appMsMessage);
                            }else{
                                // 替换参数
                                Map<String, String> replaceMap = new HashMap<String, String>();
                                replaceMap.put("val_amount", txAmount.toString());
                                replaceMap.put("val_fee", "0");
                                UsersInfo info = getUsersInfoByUserId(userId);
                                replaceMap.put("val_name", info.getTruename().substring(0, 1));
                                replaceMap.put("val_sex", info.getSex() == 2 ? "女士" : "先生");
                                AppMsMessage appMsMessage = new AppMsMessage(userId, replaceMap, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_CHONGZHI_SUCCESS);
                                appMsProcesser.gather(appMsMessage);
                            }
                            //上市活动屏蔽
                            //CommonSoaUtils.listedTwoRecharge(userId, txAmount);
                            return jsonMessage("充值成功!", "0");
                        } else {
                            // 回滚事务
                            transactionManager.rollback(txStatus);
                            // 查询充值交易状态
                            accountRecharges = accountRechargeMapper.selectByExample(example);// 查询充值记录
                            accountRecharge = accountRecharges.get(0);
                            if (RECHARGE_STATUS_SUCCESS == accountRecharge.getStatus()) {
                                return jsonMessage("充值成功!", "0");
                            } else {
                                // 账户数据过期，请查看交易明细 跳转中间页
                                return jsonMessage("账户数据过期，请查看交易明细", "1");
                            }
                        }
                    } catch (Exception e) {
                        // 回滚事务
                        transactionManager.rollback(txStatus);
                        System.err.println(e);
                    }
                }
            } else {
                return jsonMessage("充值失败,未查询到相应的充值记录", "1");
            }
        } else {
            // 更新订单信息
            AccountRechargeExample example = new AccountRechargeExample();
            example.createCriteria().andNidEqualTo(orderId);
            List<AccountRecharge> accountRecharges = this.accountRechargeMapper.selectByExample(example);
            if (accountRecharges != null && accountRecharges.size() == 1) {
                AccountRecharge accountRecharge = accountRecharges.get(0);
                if (RECHARGE_STATUS_WAIT == accountRecharge.getStatus()) {
                    // 更新处理状态
                    accountRecharge.setStatus(RECHARGE_STATUS_FAIL);// 充值状态:0:初始,1:充值中,2:充值成功,3:充值失败
                    accountRecharge.setUpdateTime(nowTime);
                    accountRecharge.setMessage(errorMsg);
                    accountRecharge.setAccountId(accountId);// 电子账户
                    accountRecharge.setBankSeqNo(txDate + txTime + seqNo);// 交易流水号
                    this.accountRechargeMapper.updateByPrimaryKeySelective(accountRecharge);
                }
            }
            return jsonMessage(errorMsg, "1");
        }
        return null;
    }
    
    private JSONObject jsonMessage(String errorDesc, String error) {
        JSONObject jo = null;
        if (Validator.isNotNull(errorDesc)) {
            jo = new JSONObject();
            jo.put("error", error);
            jo.put("errorDesc", errorDesc);
        }
        return jo;
    }
    
    private void updateBankMobile(Integer userId, String cardNo, String newMobile){

        // 成功后更新银行预留手机号码
        if (StringUtils.isBlank(newMobile) || StringUtils.isBlank(cardNo)) {
            return;
        }
        BankCardExample bankCardExample = new BankCardExample();
        bankCardExample.createCriteria().andUserIdEqualTo(userId).andCardNoEqualTo(cardNo).andStatusEqualTo(1);
        List<BankCard> bankCards = this.bankCardMapper.selectByExample(bankCardExample);
        if (bankCards != null && bankCards.size() == 1) {
            BankCard bankCard = bankCards.get(0);

            if(bankCard.getMobile() == null || !bankCard.getMobile().equals(newMobile)){
                bankCard.setMobile(newMobile);
                bankCard.setUpdateTime(new Date());
                bankCard.setUpdateUserId(userId);
                this.bankCardMapper.updateByPrimaryKeySelective(bankCard);
            }
        }
    }
    
    public BankCard getBankCardByUserId(Integer userId) {
        BankCardExample example = new BankCardExample();
        BankCardExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<BankCard> list = this.bankCardMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 发送神策数据统计MQ
     * @param sensorsDataBean
     */
    @Override
    public void sendSensorsDataMQ(SensorsDataBean sensorsDataBean) {
        Map<String, Object> params = new HashMap<String,Object>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        // 事件类型
        params.put("eventCode",sensorsDataBean.getEventCode());
        // 充值订单号
        params.put("orderId",sensorsDataBean.getOrderId());
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_SENSORS_DATA_RECHARGE_RESULT, JSONObject.toJSONString(params));

    }
}

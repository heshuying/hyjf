package com.hyjf.wechat.service.withdraw;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallParamConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.wechat.base.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cuigq on 2018/2/13.
 */
@Service
public class WxBankWithdrawSerivceImpl extends BaseServiceImpl implements WxBankWithdrawService {

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

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    // add by nxl 添加体现状态
    // 体现状态：初始值
    private static  final int WITHDRAW_STATUS_DEFAULT = 0;
    // 提现状态:提现中
    private static final int WITHDRAW_STATUS_WAIT = 1;
    // 提现状态:失败
    private static final int WITHDRAW_STATUS_FAIL = 3;
    // 提现状态:成功
    private static final int WITHDRAW_STATUS_SUCCESS = 2;

    @Override
    public List<BankCard> getBankCardByUserId(Integer userId) {
        BankCardExample bankCardExample = new BankCardExample();
        bankCardExample.createCriteria().andUserIdEqualTo(userId).andStatusEqualTo(1);
        bankCardExample.setOrderByClause("id asc");
        List<BankCard> listBankCard = this.bankCardMapper.selectByExample(bankCardExample);
        return listBankCard;
    }

    @Override
    public BanksConfig getBanksConfigByBankId(Integer bankId) {
        BanksConfigExample example = new BanksConfigExample();
        example.createCriteria().andIdEqualTo(bankId).andDelFlgEqualTo(0);

        List<BanksConfig> banksConfigList = banksConfigMapper.selectByExample(example);

        if (banksConfigList != null && !banksConfigList.isEmpty()) {
            return banksConfigList.get(0);
        }
        return null;
    }

    @Override
    public String getWithdrawFee(Integer userId, String cardNo) {
        String feetmp = PropUtils.getSystem(BankCallConstant.BANK_FEE);
        if (feetmp == null) {
            feetmp = "1";
        }

        if (StringUtils.isEmpty(cardNo)) {
            return feetmp;
        }

        BankCard bankCard = getBankInfo(userId, cardNo);

        if (bankCard != null) {
            Integer bankId = bankCard.getBankId();
            // 取得费率
            BanksConfig banksConfig = this.getBanksConfigByBankId(bankId);
            if (banksConfig != null) {
                if (Validator.isNotNull(banksConfig.getFeeWithdraw())) {
                    return banksConfig.getFeeWithdraw().toString();
                } else {
                    return feetmp;
                }
            } else {
                return feetmp;
            }
        } else {
            return feetmp;
        }
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
        accountWithdrawExample.createCriteria().andNidEqualTo(ordId).andStatusEqualTo(WITHDRAW_STATUS_SUCCESS);
        List<Accountwithdraw> list = this.accountwithdrawMapper.selectByExample(accountWithdrawExample);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 用户提现前处理
     *
     * @param bean
     * @return
     */
    @Override
    public int updateBeforeCash(BankCallBean bean, Map<String, String> params) {
        int ret = 0;
        String ordId = bean.getLogOrderId() == null ? bean.get(ChinaPnrConstant.PARAM_ORDID) : bean.getLogOrderId(); // 订单号
        AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
        accountWithdrawExample.createCriteria().andNidEqualTo(ordId);
        List<Accountwithdraw> listAccountWithdraw = this.accountwithdrawMapper.selectByExample(accountWithdrawExample);
        if (listAccountWithdraw != null && listAccountWithdraw.size() > 0) {
            return ret;
        }
        int nowTime = GetDate.getNowTime10(); // 当前时间
        BigDecimal money = new BigDecimal(bean.getTxAmount()); // 提现金额
        BigDecimal fee = BigDecimal.ZERO; // 取得费率
        if (Validator.isNotNull(params.get("fee"))) {
            fee = new BigDecimal(params.get("fee")); // 取得费率
        }
        BigDecimal total = money.add(fee); // 实际出账金额
        Integer userId = GetterUtil.getInteger(params.get("userId")); // 用户ID
        String cardNo = params.get("cardNo"); // 银行卡号
        String bank = null;
        Integer bankId = null;
        // 取得银行信息
        BankCard bankCard = getBankInfo(userId, cardNo);
        if (bankCard != null) {
            bank = bankCard.getBank();
            bankId = bankCard.getBankId();
        }
        Accountwithdraw record = new Accountwithdraw();
        record.setUserId(userId);
        record.setNid(bean.getLogOrderId()); // 订单号
        // mod by nxl 修改状态为初始值
       // record.setStatus(WITHDRAW_STATUS_WAIT); // 状态: 0:处理中
        record.setStatus(WITHDRAW_STATUS_DEFAULT);//状态：0：初始值
        record.setAccount(cardNo);// 提现银行卡号
        record.setBank(bank); // 提现银行
        record.setBankId(bankId);
        record.setBranch(null);
        record.setProvince(0);
        record.setCity(0);
        record.setTotal(total);
        record.setCredited(money);
        record.setBankFlag(1);
        record.setFee(CustomUtil.formatAmount(fee.toString()));
        record.setAddtime(String.valueOf(nowTime));
        record.setAddip(params.get("ip"));
        record.setAccountId(bean.getAccountId());
        record.setBankSeqNo(bean.getTxDate() + bean.getTxTime() + bean.getSeqNo());
        record.setTxDate(Integer.parseInt(bean.getTxDate()));
        record.setTxTime(Integer.parseInt(bean.getTxTime()));
        record.setSeqNo(Integer.parseInt(bean.getSeqNo()));
        record.setRemark("网站提现");
        record.setClient(GetterUtil.getInteger(params.get("client"))); // 0pc
        record.setWithdrawType(0); // 提现类型 0主动提现  1代提现
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
    public synchronized JSONObject handlerAfterCash(BankCallBean bean, Map<String, String> params) throws Exception {
        // 用户ID
        int userId = Integer.parseInt(params.get("userId"));
        // 查询账户信息
        AccountExample accountExample = new AccountExample();
        AccountExample.Criteria accountCriteria = accountExample.createCriteria();
        accountCriteria.andUserIdEqualTo(userId);
        Account account = this.accountMapper.selectByExample(accountExample).get(0);
        // 根据用户ID查询用户银行卡信息
        BankCard bankCard = this.selectBankCardByUserId(userId);
        String ordId = bean.getLogOrderId() == null ? "" : bean.getLogOrderId(); // 订单号
        // 银联行号
        String payAllianceCode = bean.getLogAcqResBean() == null ? "" : bean.getLogAcqResBean().getPayAllianceCode();
        int nowTime = GetDate.getNowTime10(); // 当前时间
        AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
        accountWithdrawExample.createCriteria().andNidEqualTo(ordId);
        List<Accountwithdraw> listAccountWithdraw = this.accountwithdrawMapper.selectByExample(accountWithdrawExample);

        if (listAccountWithdraw != null && listAccountWithdraw.size() > 0) {
            // 提现信息
            Accountwithdraw accountWithdraw = listAccountWithdraw.get(0);
            // 返回值=000成功 ,大额提现返回值为 CE999028
            if (BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode()) || "CE999028".equals(bean.getRetCode())) {
                // 如果信息未被处理
                if (accountWithdraw.getStatus() == WITHDRAW_STATUS_SUCCESS) {
                    // 如果是已经提现成功了
                    return jsonMessage("提现成功", "0");
                } else {
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
                            // 提现成功后,更新银行联行号
                            // 大额提现返回成功 并且银联行号不为空的情况,将正确的银联行号更新到bankCard表中
//                            System.out.println("银联行号:" + payAllianceCode);
                            if ("CE999028".equals(bean.getRetCode()) && StringUtils.isNotEmpty(payAllianceCode)) {
                                BankCardExample bankCardExample = new BankCardExample();
                                BankCardExample.Criteria cra = bankCardExample.createCriteria();
                                cra.andIdEqualTo(bankCard.getId());
                                bankCard.setPayAllianceCode(payAllianceCode);
                                try {
                                    boolean isBankCardUpdateFlag = this.bankCardMapper.updateByExampleSelective(bankCard, bankCardExample) > 0 ? true : false;
                                    if (!isBankCardUpdateFlag) {
                                        throw new Exception("大额提现成功后,更新用户银行卡的银联行号失败~~~!" + bankCard.getId());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            // 提现金额
                            BigDecimal transAmt = bean.getBigDecimal(BankCallParamConstant.PARAM_TXAMOUNT);
                            String fee = this.getWithdrawFee(userId, bankCard.getCardNo());
                            // 提现手续费
                            BigDecimal feeAmt = new BigDecimal(fee);
                            // 总的交易金额
                            BigDecimal total = transAmt.add(feeAmt);
                            // 更新订单信息
                            accountWithdraw.setFee((CustomUtil.formatAmount(feeAmt.toString()))); // 更新手续费
                            accountWithdraw.setCredited(transAmt); // 更新到账金额
                            accountWithdraw.setTotal(total); // 更新到总额
                            accountWithdraw.setStatus(WITHDRAW_STATUS_SUCCESS);// 4:成功
                            accountWithdraw.setUpdateTime(nowTime);
                            accountWithdraw.setAccount(bean.getAccountId());
                            accountWithdraw.setReason("");
                            boolean isAccountwithdrawFlag = this.accountwithdrawMapper.updateByExampleSelective(accountWithdraw, accountWithdrawExample) > 0 ? true : false;
                            if (!isAccountwithdrawFlag) {
                                throw new Exception("提现后,更新用户提现记录表失败!");
                            }
                            Account newAccount = new Account();
                            // 更新账户信息
                            newAccount.setUserId(userId);// 用户Id
                            newAccount.setBankTotal(total); // 累加到账户总资产
                            newAccount.setBankBalance(total); // 累加可用余额
                            newAccount.setBankBalanceCash(total);// 江西银行可用余额
                            boolean isAccountUpdateFlag = this.adminAccountCustomizeMapper.updateBankWithdrawSuccess(newAccount) > 0 ? true : false;
                            if (!isAccountUpdateFlag) {
                                throw new Exception("提现后,更新用户Account表失败!");
                            }
                            // 重新获取用户信息
                            account = this.getAccount(userId);
                            // 写入收支明细
                            AccountList accountList = new AccountList();
                            // 重新查询用户账户信息
                            account = this.getAccount(userId);
                            accountList.setNid(ordId);
                            accountList.setUserId(userId);
                            accountList.setAmount(total);
                            accountList.setType(2);
                            accountList.setTrade("cash_success");
                            accountList.setTradeCode("balance");
                            accountList.setTotal(account.getTotal());
                            accountList.setBalance(account.getBalance());
                            accountList.setFrost(account.getFrost());
                            accountList.setAwait(account.getAwait());
                            accountList.setRepay(account.getRepay());
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
                            accountList.setSeqNo(bean.getSeqNo());
                            accountList.setTxDate(Integer.parseInt(bean.getTxDate()));
                            accountList.setTxTime(Integer.parseInt(bean.getTxTime()));
                            accountList.setBankSeqNo(bean.getTxDate() + bean.getTxTime() + bean.getSeqNo());
                            accountList.setAccountId(bean.getAccountId());
                            accountList.setRemark("网站提现");
                            accountList.setCreateTime(nowTime);
                            accountList.setBaseUpdate(nowTime);
                            accountList.setOperator(params.get("userId"));
                            accountList.setIp(params.get("ip"));
                            accountList.setIsUpdate(0);
                            accountList.setBaseUpdate(0);
                            accountList.setInterest(null);
                            accountList.setIsBank(1);
                            accountList.setWeb(0);
                            accountList.setCheckStatus(0);// 对账状态0：未对账 1：已对账
                            accountList.setTradeStatus(1);// 0失败1成功2失败
                            boolean isAccountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
                            if (!isAccountListFlag) {
                                throw new Exception("提现成功后,插入交易明细表失败~!");
                            }
                            // 提交事务
                            this.transactionManager.commit(txStatus);
                            // 更新用户账户信息
                            Users users = getUsers(userId);
                            // 可以发送提现短信时
                            if (users != null && users.getWithdrawSms() != null && users.getWithdrawSms() == 0) {
                                // 替换参数
                                Map<String, String> replaceMap = new HashMap<String, String>();
                                replaceMap.put("val_amount", total.toString());
                                replaceMap.put("val_fee", feeAmt.toString());
                                UsersInfo info = getUsersInfoByUserId(userId);
                                replaceMap.put("val_name", info.getTruename().substring(0, 1));
                                replaceMap.put("val_sex", info.getSex() == 2 ? "女士" : "先生");
                                SmsMessage smsMessage = new SmsMessage(userId, replaceMap, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_TIXIAN_SUCCESS,
                                        CustomConstants.CHANNEL_TYPE_NORMAL);
                                AppMsMessage appMsMessage = new AppMsMessage(userId, replaceMap, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_TIXIAN_SUCCESS);
                                smsProcesser.gather(smsMessage);
                                appMsProcesser.gather(appMsMessage);
                            } else {
                                // 替换参数
                                Map<String, String> replaceMap = new HashMap<String, String>();
                                replaceMap.put("val_amount", total.toString());
                                replaceMap.put("val_fee", feeAmt.toString());
                                UsersInfo info = getUsersInfoByUserId(userId);
                                replaceMap.put("val_name", info.getTruename().substring(0, 1));
                                replaceMap.put("val_sex", info.getSex() == 2 ? "女士" : "先生");
                                AppMsMessage appMsMessage = new AppMsMessage(userId, replaceMap, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_TIXIAN_SUCCESS);
                                appMsProcesser.gather(appMsMessage);
                            }
                            return jsonMessage("提现成功!", "0");
                        } catch (Exception e) {
                            // 回滚事务
                            transactionManager.rollback(txStatus);
                            e.printStackTrace();
                            return jsonMessage("提现失败,请联系客服!", "1");
                        }
                    }
                }
            } else {
                // 提现失败,更新订单状态
                AccountwithdrawExample example = new AccountwithdrawExample();
                AccountwithdrawExample.Criteria cra = example.createCriteria();
                cra.andNidEqualTo(ordId);
                List<Accountwithdraw> list = this.accountwithdrawMapper.selectByExample(example);
                if (list != null && list.size() > 0) {
                    Accountwithdraw accountwithdraw = list.get(0);
                    //mod by nxl 将状态更改为提现中
                    accountwithdraw.setStatus(WITHDRAW_STATUS_WAIT);
                    accountwithdraw.setUpdateTime(nowTime);
                    // 失败原因
                    String reason = this.getBankRetMsg(bean.getRetCode());
                    accountwithdraw.setReason(reason);
                    boolean isUpdateFlag = this.accountwithdrawMapper.updateByExample(accountwithdraw, example) > 0 ? true : false;
                    if (!isUpdateFlag) {
                        throw new Exception("提现失败后,更新提现记录表失败");
                    }
                    /*if (WITHDRAW_STATUS_WAIT == accountWithdraw.getStatus()) {
                        accountwithdraw.setStatus(WITHDRAW_STATUS_FAIL);
                        accountwithdraw.setUpdateTime(nowTime);
                        // 失败原因
                        String reason = this.getBankRetMsg(bean.getRetCode());
                        accountwithdraw.setReason(reason);
                        boolean isUpdateFlag = this.accountwithdrawMapper.updateByExample(accountwithdraw, example) > 0 ? true : false;
                        if (!isUpdateFlag) {
                            throw new Exception("提现失败后,更新提现记录表失败");
                        }
                    }*/
                }
                return jsonMessage(bean.getRetMsg() == null ? "" : bean.getRetMsg(), "1");
            }
        }
        return null;
    }


    /**
     * 根据用户ID查询用户银行卡信息
     *
     * @param userId
     * @return
     */
    private BankCard selectBankCardByUserId(Integer userId) {
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
     * 拼装返回信息
     *
     * @param errorDesc
     * @param status
     * @return
     */
    private JSONObject jsonMessage(String errorDesc, String error) {
        JSONObject jo = null;
        if (Validator.isNotNull(errorDesc)) {
            jo = new JSONObject();
            jo.put("error", error);
            jo.put("errorDesc", errorDesc);
        }
        return jo;
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
        params.put("orderId", sensorsDataBean.getOrderId());
        params.put("eventCode", sensorsDataBean.getEventCode());
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("userId", sensorsDataBean.getUserId());
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_SENSORS_DATA_WITHDRAW_RESULT, JSONObject.toJSONString(params));
    }


}

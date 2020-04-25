package com.hyjf.mqreceiver.user.openaccount;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.openaccount.UserOpenAccountService;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.mapper.auto.BankCardMapper;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.rabbitmq.client.Channel;

/**
 * 
 * 用户开户 绑定银行卡关系
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月13日
 * @see 上午9:04:09
 */
public class UserOpenAccountMessageHadnle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(UserOpenAccountMessageHadnle.class);
    private String thisMessName = "【用户开户绑定银行卡关系】";
    @Autowired
    private UserOpenAccountService userOpenAccountService;
    @Autowired
    BankCardMapper bankCardMapper;
    
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------"+thisMessName+"开始------------------------" + this.toString());
        if(message == null || message.getBody() == null){
            _log.error(thisMessName+"接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        
        _log.info(thisMessName+"接收到的消息：" + msgBody);
        
        BankCallBean bankCallBean;
        try {
            bankCallBean = JSONObject.parseObject(msgBody, BankCallBean.class);
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }
        
        //验证请求参数
        if (Validator.isNull(bankCallBean.getLogUserId())) {
            _log.error(thisMessName+"接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        //-------------------------------------
        Integer userId = Integer.parseInt(bankCallBean.getLogUserId());
        // 获取用户信息
        Users user = userOpenAccountService.getUsers(userId);
        // 查询银行卡 绑定
        boolean ok = getCardNoToBank(bankCallBean,user);
         
        _log.info("----------------------------"+thisMessName+"结束,插入结果为："+ok+"--------------------------------" + this.toString());
    }

    // 调用银行接口查询绑定的银行卡号
    private boolean getCardNoToBank(BankCallBean bankCallBean, Users user) throws ParseException {
        boolean ok = false;
        Integer userId = Integer.parseInt(bankCallBean.getLogUserId());
        // 调用汇付接口(4.2.2 用户绑卡接口)
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_CARD_BIND_DETAILS_QUERY);
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
        bean.setAccountId(bankCallBean.getAccountId());// 存管平台分配的账号
        bean.setState("1"); // 查询状态 0-所有（默认） 1-当前有效的绑定卡
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        bean.setLogUserId(String.valueOf(userId));
        // 调用汇付接口 4.4.11 银行卡查询接口
        BankCallBean call = BankCallUtils.callApiBg(bean);
        String respCode = call == null ? "" : call.getRetCode();
        // 如果接口调用成功
        if (BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
            String usrCardInfolist = bankCallBean.getSubPacks();
            JSONArray array = JSONObject.parseArray(usrCardInfolist);
            JSONObject obj = null;
            if(array != null && array.size() != 0){
                obj = array.getJSONObject(0);
            }
            BankCard bank = new BankCard();
            bank.setUserId(userId);
            // 设置绑定的手机号
            bank.setMobile(bankCallBean.getMobile());
            bank.setUserName(user.getUsername());
            bank.setStatus(1);// 默认都是1
            bank.setCardNo(obj.getString("cardNo"));
            // 根据银行卡号查询银行ID
            String bankId = userOpenAccountService.getBankIdByCardNo(obj.getString("cardNo"));
            bank.setBankId(bankId == null ? 0 : Integer.valueOf(bankId));
            bank.setBank(bankId == null ? "" : userOpenAccountService.getBankNameById(bankId));
            // 银行联号
            String payAllianceCode = "";
            // 调用江西银行接口查询银行联号
            BankCallBean payAllianceCodeQueryBean = userOpenAccountService.payAllianceCodeQuery(obj.getString("cardNo"), userId);
            if (payAllianceCodeQueryBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(payAllianceCodeQueryBean.getRetCode())) {
                payAllianceCode = payAllianceCodeQueryBean.getPayAllianceCode();
            }
            // 如果此时银联行号还是为空,根据bankId查询本地存的银联行号
            if (StringUtils.isBlank(payAllianceCode)) {
                payAllianceCode = userOpenAccountService.getPayAllianceCodeByBankId(bankId);
            }
            bank.setPayAllianceCode(payAllianceCode);
            SimpleDateFormat sdf = GetDate.yyyymmddhhmmss;
            bank.setCreateTime(sdf.parse(obj.getString("txnDate") + obj.getString("txnTime")));
            bank.setCreateUserId(userId);
            bank.setCreateUserName(user.getUsername());
            ok = bankCardMapper.insertSelective(bank) > 0 ? true : false;
        }
        return ok;
    }
}

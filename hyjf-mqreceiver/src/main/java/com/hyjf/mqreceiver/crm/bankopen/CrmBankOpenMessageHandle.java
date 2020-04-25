package com.hyjf.mqreceiver.crm.bankopen;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.common.util.GetDate;
import com.hyjf.mqreceiver.crm.utils.CheckSignUtil;
import com.hyjf.mqreceiver.crm.utils.PropUtils;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.rabbitmq.client.Channel;

@Component(value = "crmBankOpenMessageHandle")
public class CrmBankOpenMessageHandle extends BaseServiceImpl implements ChannelAwareMessageListener {


    @Autowired
    private BankOpenService bankOpenService;
    
    Logger _log = LoggerFactory.getLogger(CrmBankOpenMessageHandle.class);

    private String CRM_INSERTCUSTOMER_ACTION_URL = PropUtils.getSystem("crm.insertCustomerAction.url");

    /**
     * 消息监听
     *
     * @throws Exception
     */
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------crm开户同步开始------------------------" + this.toString());

        if (message == null || message.getBody() == null) {
            _log.error("【crm开户同步】接收到的消息为null");
            // 消息队列的指令消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息转换
        String msgBody = new String(message.getBody());
        JSONObject json = JSON.parseObject(msgBody);
        _log.info("【crm开户同步】接收到的消息：" + msgBody);

        CloseableHttpResponse result = null;
        try {
            result = this.postJson(CRM_INSERTCUSTOMER_ACTION_URL, this.buildData(json.getInteger("userId")).toJSONString());
        } catch (Exception e) {
            _log.error("【crm开户同步】异常，消息【{}】", msgBody, e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
        if (result.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            _log.error("【crm开户同步】CRM-API返回失败，消息【{}】", msgBody);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        } else {
            _log.info("【crm开户同步】投递成功");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }

    }

    private JSONObject buildData(Integer userId) {
        JSONObject ret = new JSONObject();
        Map<String, Object> map = Maps.newHashMap();

        UsersInfo userInfo = this.getUserInfo(userId);
        SpreadsUsers spreadsUsers = this.getSpreadsUserInfo(userId);
        Users user = this.getUser(userId);
        BankOpenAccount account = this.getOpenInfo(userId);
        if (spreadsUsers != null) {
            UsersInfo referrerInfo = this.getUserInfo(spreadsUsers.getSpreadsUserid());
            Users referrerUser = this.getUser(spreadsUsers.getSpreadsUserid());
            map.put("recommendCardId", referrerInfo.getIdcard());
            map.put("recommendName", referrerInfo.getTruename());
            map.put("recommondUsername", referrerUser.getUsername());
            map.put("recommendMobile", referrerUser.getMobile());
        }

        map.put("customerCardId", userInfo.getIdcard());
        map.put("availableBalance", 0);
        map.put("customerId", String.valueOf(userId));
        map.put("customerMobile", user.getMobile());
        map.put("customerName", userInfo.getTruename());
        map.put("customerUsername", user.getUsername());
        map.put("openingTimeStr", GetDate.dateToDateFormatStr(account.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        map.put("platformInstcode", 10000001);
        map.put("registerTimeStr", GetDate.timestamptoStrYYYYMMDDHHMMSS(String.valueOf(user.getRegTime())));
        map.put("recommondId", user.getReferrer());

        String sign = CheckSignUtil.encryptByRSA(map, "10000001");
        ret.put("instCode", "10000001");
        ret.put("timeStamp", String.valueOf(GetDate.getDate().getTime()));
        ret.put("object", map);
        ret.put("sign", sign);
        return ret;
    }

    // 取得用户详细信息
    private UsersInfo getUserInfo(Integer userId) {
        return bankOpenService.getUsersInfoByUserId(userId);
    }

    // 取得用户详信息
    private Users getUser(Integer userId) {
        return bankOpenService.getUsers(userId);
    }

    // 取得用户开户信息
    private BankOpenAccount getOpenInfo(Integer userId) {
        return bankOpenService.getBankOpenAccount(userId);
    }
    
    // 取得推荐人信息
    private SpreadsUsers getSpreadsUserInfo(Integer userId) {
        return bankOpenService.getSpreadsUsersByUserId(userId);
    }

    /**
     * 处理post请求.
     *
     * @param url 参数
     * @return json
     */
    public CloseableHttpResponse postJson(String url, String jsonStr) {

        // 实例化httpClient
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 实例化post方法
        HttpPost httpPost = new HttpPost(url);

        // 结果
        CloseableHttpResponse response = null;
        try {
            // 提交的参数
            StringEntity uefEntity = new StringEntity(jsonStr, "utf-8");
            uefEntity.setContentEncoding("UTF-8");
            uefEntity.setContentType("application/json");
            // 将参数给post方法
            httpPost.setEntity(uefEntity);
            // 执行post方法
            response = httpclient.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

}

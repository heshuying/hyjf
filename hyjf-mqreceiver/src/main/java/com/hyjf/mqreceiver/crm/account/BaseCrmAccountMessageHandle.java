package com.hyjf.mqreceiver.crm.account;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.hyjf.bank.service.borrow.BorrowService;
import com.hyjf.mqreceiver.crm.utils.CheckSignUtil;
import com.hyjf.mqreceiver.crm.utils.PropUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.rabbitmq.client.Channel;

public class BaseCrmAccountMessageHandle extends BaseServiceImpl implements ChannelAwareMessageListener {

    private Logger _log = LoggerFactory.getLogger(BaseCrmAccountMessageHandle.class);

    private String CRM_INSERTCUSTOMER_ACTION_URL = PropUtils.getSystem("crm.updateCustomer.url");

    /**
     * 消息监听
     * @throws Exception
     */
    public void onMessage(Message message, Channel channel) throws Exception {
        if (message == null || message.getBody() == null) {
            _log.error("【crm账户同步】接收到的消息为null");
            // 消息队列的指令消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息转换
        String msgBody = new String(message.getBody());
        JSONObject json = JSON.parseObject(msgBody);
        _log.info("【crm账户同步】接收到的消息：" + msgBody);

        CloseableHttpResponse result = null;
        try {
            _log.info("【crm账户同步】数据" + this.buildData(json).toJSONString());
            result = this.postJson(CRM_INSERTCUSTOMER_ACTION_URL, this.buildData(json).toJSONString());
        } catch (Exception e) {
            _log.info("【crm账户同步】异常");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
        if (result.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            _log.info("【crm账户同步】网络异常");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        } else {
            _log.info("【crm账户同步】投递成功");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }

    }

    private JSONObject buildData(JSONObject obj) {
        JSONObject ret = new JSONObject();
        if (obj != null && StringUtils.isNotBlank(this.getValue(obj, "userid", ""))) {
            Map<String, Object> map = Maps.newHashMap();
            String userId = this.getValue(obj, "userid", "");

            Account account = this.getAccount(Integer.valueOf(userId));
            map.put("customerId", userId);
            map.put("availableBalance",account.getBankBalance());
            map.put("pendingAmount", account.getBankAwait().add(account.getPlanAccountWait()));

            String sign = CheckSignUtil.encryptByRSA(map, "10000001");
            ret.put("instCode", "10000001");
            ret.put("object", map);
            ret.put("sign", sign);
        }
        return ret;
    }

    // 遍历JSONObject
    public String getValue(JSONObject obj, String key, String val) {
        if (StringUtils.isBlank(val)) {
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                if (entry.getValue() instanceof JSONObject) {
                    val = this.getValue((JSONObject) entry.getValue(), key, val);
                    break;
                }
                if (entry.getKey().equals(key)) {
                    val = entry.getValue().toString();
                    break;
                }
            }
        }
        return val;
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

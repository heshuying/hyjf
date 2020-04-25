package com.hyjf.mqreceiver.crm.invest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.hyjf.bank.service.hjh.borrow.tender.BankAutoTenderService;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mqreceiver.crm.utils.CheckSignUtil;
import com.hyjf.mqreceiver.crm.utils.PropUtils;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.rabbitmq.client.Channel;

@Component(value = "crmInvestMessageHandle")
public class CrmInvestMessageHandle extends BaseServiceImpl implements ChannelAwareMessageListener {

    @Autowired
    BorrowService borrowService;

    @Autowired
    BankAutoTenderService bankAutoTenderService;

    Logger _log = LoggerFactory.getLogger(CrmInvestMessageHandle.class);

    private final String CRM_INVESTMENTDETAIL_ACTOIN_URL = PropUtils.getSystem("crm.investmentdetails.url");

    /**
     * 消息监听
     * @throws Exception
     */
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------crm出借同步开始------------------------" + this.toString());

        if (message == null || message.getBody() == null) {
            _log.error("【crm出借同步】接收到的消息为null");
            // 消息队列的指令不消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // --> 消息转换
        String msgBody = new String(message.getBody());
        _log.info("【crm出借同步】接收到的消息：" + msgBody);
        JSONObject json = JSON.parseObject(msgBody);// accedeOrderId
        String accid = json.getString("planNid");
        Object obj = null;
        if (StringUtils.isNotBlank(accid)) {
            obj = JSONObject.parseObject(msgBody, HjhAccede.class);
        } else {
            obj = JSONObject.parseObject(msgBody, BorrowTender.class);
        }

        CloseableHttpResponse result = null;
        try {
            result = this.postInvestInfo(this.buildData(obj).toJSONString());
        } catch (Exception e) {
            _log.info("【crm出借同步】异常，重新投递");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
        if (result.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            _log.info("【crm出借同步】网络异常，重新投递");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        } else {
            _log.info("【crm出借同步】投递成功");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }

    }

    private CloseableHttpResponse postInvestInfo(String jsonStr) {
        _log.info("crmurl...................." + CRM_INVESTMENTDETAIL_ACTOIN_URL);
        _log.info("crmdata...................." + jsonStr);
        _log.debug("crm.investmentdetails.url=【{}】", CRM_INVESTMENTDETAIL_ACTOIN_URL);
        return this.postJson(CRM_INVESTMENTDETAIL_ACTOIN_URL, jsonStr);
    }

    /* post数据构造 所取数据为必要数据 */
    private JSONObject buildData(Object obj) {
        JSONObject ret = new JSONObject();
        Map<String, Object> map = Maps.newHashMap();
        // 根据数据类型判断出借类型。直投类
        if (obj instanceof BorrowTender) {
            BorrowTender bt = (BorrowTender) obj;
            UsersInfo userInfo = this.getUserInfo(bt.getUserId());
            Borrow borrowInfo = getBorrowInfo(bt.getBorrowNid());
            String borrowStyle = borrowInfo.getBorrowStyle();

            map.put("idNum", userInfo.getIdcard());
            map.put("referrerIdCard", "");
            map.put("status", 1);
            map.put("borrowType", borrowInfo.getName());
            map.put("borrowNid", bt.getBorrowNid());
            map.put("investmentNid", bt.getNid());
            map.put("unit",
                    CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)
                            || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                            || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)
                            || CustomConstants.BORROW_STYLE_END.equals(borrowStyle) ? 2 : 1);
            map.put("term", borrowInfo.getBorrowPeriod());
            map.put("account", bt.getAccount());
            map.put("addTime", bt.getAddtime());
           // map.put("loanTime", getDate(bt.getLoanOrderDate()));
            if(StringUtils.isNotBlank(borrowInfo.getPlanNid())){
                map.put("productNo", 1007);
            }else{
                map.put("productNo", 1001);
            }
            map.put("referrerDepartmentId", bt.getInviteDepartmentId());
        }
        // 计划类出借
        else if (obj instanceof HjhAccede) {
            HjhAccede hj = (HjhAccede) obj;
            UsersInfo userInfo = this.getUserInfo(hj.getUserId());
            HjhPlan hjhPlan = getHjhPlanInfo(hj.getPlanNid());

            map.put("idNum", userInfo.getIdcard());
            map.put("referrerIdCard", "");
            map.put("status", 1);
            map.put("borrowType", hjhPlan.getPlanName());
            map.put("borrowNid", hj.getPlanNid());
            map.put("investmentNid", hj.getAccedeOrderId());
            map.put("unit", hjhPlan.getIsMonth() == 0 ? 1 : 2);
            map.put("term", hjhPlan.getLockPeriod());
            map.put("account", hj.getAccedeAccount());
            map.put("addTime", hj.getAddTime());
            map.put("loanTime", hj.getAddTime());
            map.put("productNo", 1002);

        }
        map.put("instCode", "10000001");

        String sign = CheckSignUtil.encryptByRSA(map, "10000001");
        ret.put("instCode", "10000001");
        ret.put("object", map);
        ret.put("sign", sign);
        return ret;
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

    // 格式化时间格式
    private int getDate(String fdate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        int idate = 0;
        try {
            Date date = format.parse(fdate);

            idate = (int) (date.getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idate;
    }

    // 取得用户详细信息
    private UsersInfo getUserInfo(Integer userId) {
        return borrowService.getUsersInfoByUserId(userId);
    }

    // 取得标的信息
    private Borrow getBorrowInfo(String borrowNid) {
        return borrowService.getBorrowByBorrowNid(borrowNid);
    }

    // 取得计划信息
    private HjhPlan getHjhPlanInfo(String planNid) {
        return bankAutoTenderService.selectHjhPlanByPlanNid(planNid);
    }

}

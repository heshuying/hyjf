/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.olddata.transact.repay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.entity.CertAccountList;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.rabbitmq.client.Channel;

/**
 * @Description 合规数据上报 CERT 历史数据上报交易明细信息推送上报（延时队列）
 * @Author pcc
 * @Date 2018/11/26 17:57
 */
public class OldCertTransactRepaySuccessMessageHadnle implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(OldCertTransactRepaySuccessMessageHadnle.class);

    private String thisMessName = "历史数据易明细信息(还款信息)";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";
    private static int size = 100;

    @Autowired
    private OldCertTransactRepaySuccessService certTransactRepaySuccessService;
    

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
    	Integer now=GetDate.getMyTimeInMillis();
    	logger.info(logHeader + " 开始。"+now);
        // --> 消息内容校验
        if (message == null || message.getBody() == null) {
            logger.error(logHeader + "接收到的消息为null！！！");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        String msgBody = new String(message.getBody());
        logger.info(logHeader + "接收到的消息：" + msgBody);
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(msgBody);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.error(logHeader + "解析消息体失败！！！", e);
            return;
        }
        RedisUtils.set("oldCertTransactRepaySuccessMqIsRun","1");
        String oldCertTransactId = jsonObject.getString("oldCertTransactId");
        // --> 消息处理
        try {
        	String pageStr=RedisUtils.get("repaySuccesspage");
    		if(pageStr==null||pageStr.length()==0){
    			pageStr="1";
    		}
    		Integer page=Integer.parseInt(pageStr);
    		Map<String, Object> param =new HashMap<String, Object>();
    		param.put("maxId", oldCertTransactId);
    		param.put("limitStart", (page-1)*size);
    		param.put("limitEnd", size);
    		logger.info(logHeader + "接收到的消息：" + JSON.toJSONString(param));
            // --> 调用service组装数据
        	List<Map<String, Object>> list=certTransactRepaySuccessService.getTransactMap(param);
        	if(list==null||list.size()==0){
        		RedisUtils.set("oldCertTransactRepaySuccessMqIsRun","0");
    			return;
    		}
        	JSONArray data =JSONArray.parseArray(JSON.toJSONString(list));
    		List<CertAccountList> entitys = this.groupByDate(data,thisMessName,CertCallConstant.CERT_INF_TYPE_TRANSACT);
    		for (CertAccountList certAccountList : entitys) {
    			try {
    				certTransactRepaySuccessService.insertAndSendPostOld(certAccountList);
                    // 批量修改状态  end
                } catch (Exception e) {
                    throw e;
                }
			}
    		
    		page++;
    		RedisUtils.set("repaySuccesspage",page+"");
            logger.info(logHeader + " 处理成功。" + msgBody);
        } catch (Exception e) {
            // 错误时，以下日志必须出力（预警捕捉点）
            logger.error(logHeader + " 处理失败！！" + msgBody, e);
        } finally {
        	RedisUtils.set("oldCertTransactRepaySuccessMqIsRun","0");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.info(logHeader + " 结束。"+(GetDate.getMyTimeInMillis()-now));
        }
       
    }
    
    
    /**
     * 重新把结果分多个批次上传  按照月份
     * 对象里面的格式   groupByDate 为年月日yyyyMM
     * @param array
     * @return
     * {'2018-01':[],'2018-02:[]'}
     */
    public List<CertAccountList> groupByDate(JSONArray array,String thisMessName,String type) {
        JSONObject groupResult = new JSONObject();
        if (array == null || array.size() == 0) {
            return null;
        }
        for (int i = 0; i < array.size(); i++) {
            JSONObject item = array.getJSONObject(i);
            // 取出需要排序的日期
            String groupDate = item.getString("groupByDate");
            // 如果已经有了  就add   没有就new一个
            if (groupResult.containsKey(groupDate)) {
                // add 进去
                JSONArray itemArray = groupResult.getJSONArray(groupDate);
                item.remove("groupByDate");
                itemArray.add(item);
            } else {
                // new 一个 list 放进去
                JSONArray itemArray = new JSONArray();
                item.remove("groupByDate");
                itemArray.add(item);
                groupResult.put(groupDate, itemArray);
            }
        }
        // 遍历组装数据
        List<CertAccountList> entitys = new ArrayList<>();
        LinkedHashMap<String, JSONArray> jsonMap = JSONObject.parseObject(groupResult.toJSONString(), new TypeReference<LinkedHashMap<String, JSONArray>>() {
        });
        for (Map.Entry<String, JSONArray> item : jsonMap.entrySet()) {
        	CertAccountList entity = new CertAccountList(thisMessName, type, null, item.getValue());
            // 设置交易时间为当前月份的一号
            entity.setTradeDate(item.getKey().replace("-","")+"01");
            // 查询这个月有多少天
            entity.setDateNum(GetDate.getDaysOfMonth(item.getKey()+"-01"));
            entitys.add(entity);
        }
        return entitys;
    }
}

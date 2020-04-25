package com.hyjf.mqreceiver.borrow.eve;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.auto.AutoMapper;
import com.hyjf.mybatis.model.auto.AleveErrowLog;
import com.hyjf.mybatis.model.auto.AleveLog;
import com.rabbitmq.client.Channel;

/**
 * 导入红包账户流水明细
 * @author kaka
 *
 */
@Component(value="aleveFileMessageHadnle")
public class AleveFileMessageHadnle extends AutoMapper implements ChannelAwareMessageListener {
    private static final Logger LOG = LoggerFactory.getLogger(AleveFileMessageHadnle.class);
	@Autowired
	private AleveFileService aleveService;
	@Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        LOG.info("********************导入红包账户流水明细AlEve开始*************************");
        System.out.println("********************导入红包账户流水明细AlEve开始*************************");
        JSONObject json = new JSONObject();
        try {
            String msgBody = new String(message.getBody());
            json = JSONObject.parseObject(msgBody);
            if(Validator.isNull(json)){
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            }
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }
        String savePath = json.getString("savePath");
        String beforeDate = DateUtils.getBeforeDateOfDay();//当天日期返回时间类型 yyyyMMdd
        String filePathAleve = json.getString("filePathAleve");

        if(StringUtils.isBlank(filePathAleve)){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        File dir = new File(savePath);  
        File fin;
        try {
            System.out.println(dir.getCanonicalPath() + File.separator +filePathAleve+beforeDate);
            fin = new File(dir.getCanonicalPath() + File.separator +filePathAleve+beforeDate);
            AleveErrowLog aleveErrowLog = new AleveErrowLog();
            ArrayList<AleveLog> list = TransUtil.readFileAleve(fin,aleveErrowLog); 
            if(StringUtils.isNotBlank(aleveErrowLog.getFilestats())){
                aleveErrowLogMapper.insert(aleveErrowLog);//异常记录
            }
            aleveService.saveFile(list);
            System.out.println("已更新 " + list.size() + " 条记录");
           
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }  
        Map<String, String> map = new HashMap<String, String>();
        map.put("status", "1");
        rabbitTemplate.convertAndSend(RabbitMQConstants.ALEV_FANOUT_EXCHANGE,"",JSONObject.toJSONString(map));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        LOG.info("********************导入红包账户流水明细AlEve结束*************************");
        System.out.println("********************导入红包账户流水明细AlEve结束*************************");
    }

}

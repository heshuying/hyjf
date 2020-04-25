package com.hyjf.mqreceiver.borrow.eve;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.AleveErrowLog;
import com.hyjf.mybatis.model.auto.EveLog;
import com.rabbitmq.client.Channel;

/**
 * 导入红包账户流水明细
 * @author kaka
 *
 */
@Component(value="eveFileMessageHadnle")
public class EveFileMessageHadnle extends AutoMapper implements ChannelAwareMessageListener {
    private static final Logger LOG = LoggerFactory.getLogger(EveFileMessageHadnle.class);
	@Autowired
	private EveFileService eveService;
	
	@Autowired
    @Qualifier("mailProcesser")
    private MessageProcesser mailMessageProcesser;

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        LOG.info("********************导入红包账户流水明细Eve开始*************************");
        System.out.println("********************导入红包账户流水明细Eve开始*************************");
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
        String beforeDate = DateUtils.getBeforeDateOfDay();//获取前一天时间返回时间类型 yyyyMMdd
        String filePatheve = json.getString("filePathEve");
        if(StringUtils.isBlank(filePatheve)){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        File dir = new File(savePath);  
        File fin;
        try {
            System.out.println(dir.getCanonicalPath() + File.separator +filePatheve+beforeDate);
            fin = new File(dir.getCanonicalPath() + File.separator +filePatheve+beforeDate);
            AleveErrowLog record = new AleveErrowLog();
            ArrayList<EveLog> list = TransUtil.readFileEve(fin,record); 
            if(StringUtils.isNotBlank(record.getFilestats())){
                aleveErrowLogMapper.insert(record);//异常记录
            }
            eveService.saveFile(list);
            System.out.println("已更新 " + list.size() + " 条记录");
           
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }  
       channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
       HashMap<String, String> map = new HashMap<>();
       map.put("status", "1");
       rabbitTemplate.convertAndSend(RabbitMQConstants.ALEV_FANOUT_EXCHANGE,map);
        //rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.READ_DATA_EVE,"");
        LOG.info("********************导入红包账户流水明细Eve结束*************************");
        System.out.println("********************导入红包账户流水明细Eve结束*************************");
    }

}

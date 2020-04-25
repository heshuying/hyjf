package com.hyjf.mqreceiver.aleve.reverse;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.aleve.reverse.ReverseService;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.AleveLogCustomize;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ReverseMessageHandle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(ReverseMessageHandle.class);

    @Autowired
    ReverseService reverseService;
    
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------自动冲正开始------------------------" + this.toString());
        if(message == null || message.getBody() == null){
            _log.error("【自动冲正异常】接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        
        _log.info("【自动冲正】接收到的消息：" + msgBody);
        
        JSONObject requestJson;
        try {
        	requestJson = JSONObject.parseObject(msgBody);
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }
        
        //验证请求参数
        if (Validator.isNull(requestJson.get("status"))) {
            _log.error("【自动冲正异常】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String status = (String)requestJson.get("status");
        
        //1导入成功
        if (!"1".equals(status)) {
            _log.error("【自动冲正异常】接收到数据库导入失败信息");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        try {
        	//aleve表中获取冲正数据：调账交易类型为7616、7820且含有冲正标识
            List<String> tranStyle = new ArrayList<String>();
            //活期收益 银联通道提现 单边账调账
            tranStyle.add("7616");
            //靠档计息 人行通道提现 单边账调账
            tranStyle.add("7820");
            List<AleveLogCustomize> aleveLogCustomizes = this.reverseService.selectAleveReverseList(tranStyle);
        	//无利息数据、打印log并返回
            if (null == aleveLogCustomizes || aleveLogCustomizes.size() == 0) {
            	_log.info("【自动冲正】未查询到冲正信息");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            	return;
            }
        	//循环遍历冲正数据、
            for (AleveLogCustomize aleveLogCustomize : aleveLogCustomizes) {
            	if (!"1".equals(aleveLogCustomize.getRevind().toString())) {
            		//处理完成flg变为1下次处理不再抽出
            		if(!this.reverseService.updateAleveLog(aleveLogCustomize)) {
            			_log.error("【自动冲正异常】：非冲正flg数据处理完成字段更新失败，银行账号：" + aleveLogCustomize.getCardnbr() + "----Seqno:" + aleveLogCustomize.getSeqno() + "----CreateTime:" + aleveLogCustomize.getCreateTime());
            			continue;
            		}
                    //非冲正交易的场合处理下一条
            		_log.info("【自动冲正】非冲正交易，银行账号：" + aleveLogCustomize.getCardnbr());
            		continue;
            	}
            	//白名单校验订单号+用户名存在的情况不再自动冲正
            	boolean isExists = this.reverseService.countManualReverse(aleveLogCustomize) > 0 ? true : false;
            	if (isExists) {
            		//处理完成flg变为1下次处理不再抽出
            		if(!this.reverseService.updateAleveLog(aleveLogCustomize)) {
            			_log.error("【自动冲正异常】：手动冲正数据处理完成字段更新失败，银行账号：" + aleveLogCustomize.getCardnbr() + "----Seqno:" + aleveLogCustomize.getSeqno() + "----CreateTime:" + aleveLogCustomize.getCreateTime());
            			continue;
            		}
            		//白名单手动冲正的数据不再处理
            		_log.info("【自动冲正】白名单数据不再处理，银行账号：" + aleveLogCustomize.getCardnbr());
            		continue;
            	}
            	//冲正出错的情况打印log处理下一条
            	if (!this.reverseService.forReverse(aleveLogCustomize)) {
                	_log.error("【自动冲正异常】用户资产/余额处理失败，银行账号：" + aleveLogCustomize.getCardnbr());
            	}
            }
        	
		} catch (Exception e) {
			_log.error("【自动冲正异常】处理失败！", e);
		}

        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
         
        _log.info("----------------------------自动冲正结束 --------------------------------" + this.toString());
    }
    
}

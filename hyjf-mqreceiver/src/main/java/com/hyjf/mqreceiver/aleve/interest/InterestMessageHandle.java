package com.hyjf.mqreceiver.aleve.interest;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.aleve.interest.InterstService;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.customize.AleveLogCustomize;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class InterestMessageHandle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(InterestMessageHandle.class);
    
    @Autowired
    private InterstService interstService;
    
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------利息自动同步开始------------------------" + this.toString());
        if(message == null || message.getBody() == null){
            _log.error("【利息自动同步异常】接收到的消息为null");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        
        _log.info("【利息自动同步】接收到的消息：" + msgBody);
        
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
            _log.error("【利息自动同步异常】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String status = (String)requestJson.get("status");
        
        //1导入成功
        if (!"1".equals(status)) {
            _log.error("【利息自动同步异常】接收到数据库导入失败信息");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        try {
        	//配置文件中取得需要同步利息的月日
            //#利息同步月日
            List<String> interestDateList = new ArrayList<String>();
            String[] codeArray = null;
            String codes = PropUtils.getSystem("hyjf.aleve.interest.synchronization");
            if (StringUtils.isNotBlank(codes)) {
                codeArray = codes.split(",");
                interestDateList = Arrays.asList(codeArray);
            } else {
            	_log.error("【利息自动同步异常】利息同步日没有配置");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            	return;
            }

        	//当前系统时间的月日
            String nowDay = new SimpleDateFormat("MMdd").format(new Date());
            //判断是否是利息同步日、同步利息信息
            if (!interestDateList.contains(nowDay)) {
            	//非利息同步日、打印log并返回
            	_log.info("【利息自动同步】非利息同步日，不进行利息同步");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            	return;
            }
            
        	//aleve表中取得需同步利息数据：5500（活期收益）5504（靠档计息）、时间点前一天
            List<String> tranStyle = new ArrayList<String>();
            //活期收益
            tranStyle.add("5500");
            //靠档计息
            tranStyle.add("5504");
            List<AleveLogCustomize> aleveLogCustomizes = this.interstService.selectAleveInterstList(tranStyle);
        	//无利息数据、打印log并返回
            if (null == aleveLogCustomizes || aleveLogCustomizes.size() == 0) {
            	_log.info("【利息自动同步】未查询到利息信息");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            	return;
            }
            
            //获取平台商户服务费账户
            String mersAccount = PropUtils.getSystem("hyjf.bank.mers.account");
            if (StringUtils.isBlank(mersAccount)) {
            	_log.error("【利息自动同步异常】平台商户服务费账户没有配置");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            	return;
            }
            //获取平台商户红包账户
            String merrpAccount = PropUtils.getSystem("hyjf.bank.merrp.account");
            if (StringUtils.isBlank(merrpAccount)) {
            	_log.error("【利息自动同步异常】平台商户红包账户没有配置");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            	return;
            }
        	//取得list数据循环遍历、根据该数据的电子账号信息生成1条对应的利息收益入账记录（资金明细 huiyingdai_account_list）
            for (AleveLogCustomize aleveLogCustomize : aleveLogCustomizes) {
            	//当电子账户是我司手续费账户（6212461910000000023）红包账户（6212461910000000015）时
            	if (mersAccount.equals(aleveLogCustomize.getCardnbr()) || merrpAccount.equals(aleveLogCustomize.getCardnbr())) {
	            	//获取Admin信息
            		Admin admin = this.interstService.getAdminId();
            		aleveLogCustomize.setUserId(admin.getId());
            		//生成订单号
            		String orderId = GetOrderIdUtils.getOrderId2(admin.getId());
            		aleveLogCustomize.setOrderId(orderId);
            		//网站收支中插入对应入账记录
            		boolean insertHAWLFlag = this.interstService.insertAccountWebList(aleveLogCustomize);
	    			if (!insertHAWLFlag) {
	                	_log.error("【利息自动同步异常】公司网站收支信息同步失败，电子账号：" + aleveLogCustomize.getCardnbr() + "交易流水号 ：" + aleveLogCustomize.getTranno());
	                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
	                	continue;
	    			}
	    			
	        		//更新我司账户余额
	    			if (!this.interstService.updateMerchantAccount(aleveLogCustomize)) {
	    				_log.error("【利息自动同步异常】公司利息同步失败，电子账号：" + aleveLogCustomize.getCardnbr() + "交易流水号 ：" + aleveLogCustomize.getTranno());
	    			} else {
		    			_log.info("【利息自动同步】公司利息同步完成，电子账号：" + aleveLogCustomize.getCardnbr() + "交易流水号 ：" + aleveLogCustomize.getTranno());
	    			}
	    		//非公司账户
            	} else {
            		//插入对应入账记录、更新用户账户余额（huiyingdai_account）
            		if(!this.interstService.insertAccountList(aleveLogCustomize)) {
                    	_log.error("【利息自动同步异常】个人利息同步失败，电子账号：" + aleveLogCustomize.getCardnbr() + "交易流水号 ：" + aleveLogCustomize.getTranno());
            		} else {
    	    			_log.info("【利息自动同步】个人利息同步完成，电子账号：" + aleveLogCustomize.getCardnbr() + "交易流水号 ：" + aleveLogCustomize.getTranno());
            		}
            	}
            }
		} catch (Exception e) {
			_log.error("【利息自动同步异常】处理失败!", e);
		}
        
         channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
         
        _log.info("----------------------------利息自动同步结束 --------------------------------" + this.toString());
    }
    
}

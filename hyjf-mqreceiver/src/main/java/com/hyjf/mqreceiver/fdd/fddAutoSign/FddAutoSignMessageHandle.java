package com.hyjf.mqreceiver.fdd.fddAutoSign;


import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractConstant;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractService;
import com.hyjf.common.validator.Validator;
import com.hyjf.pay.lib.fadada.bean.DzqzCallBean;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 法大大自动签署异步处理
 */
@Component(value = "fddAutoSignMessageHandle")
public class FddAutoSignMessageHandle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FddGenerateContractService fddGenerateContractService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

            if(message == null || message.getBody() == null){
                _log.error("【法大大自动签署异步处理任务】接收到的消息为null");
                // 消息队列的指令消费
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            }

            String msgBody = new String(message.getBody());

            DzqzCallBean bean = null;
            //订单号
            String ordid = null;
            try {
                bean = JSONObject.parseObject(msgBody, DzqzCallBean.class);
                if(Validator.isNull(bean)){
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                    return;
                }
                ordid = bean.getContract_id();
                if(ordid == null){
                    throw new RuntimeException("传入参数不得为空！");
                }
                _log.info("-----------------开始处理法大大自动签章异步处理，订单号：" + ordid);
                fddGenerateContractService.updateAutoSignData(bean);
//                try {
//                    // 签署成功后更新合同模板信息
//                    if (FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == bean.getTransType()) {
//                        fddGenerateContractService.sendMQToNifa(bean.getContract_id());
//                    }
//                } catch (Exception e) {
//                    _log.error("签署发送MQ消息生成合同要素信息失败：合同ID：" + bean.getContract_id());
//                    e.printStackTrace();
//                }
            } catch (Exception e1) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                _log.info("--------------------------------------法大大自动签署异步处理任务异常，订单号：" + ordid + ",错误信息："+ e1.getMessage()+"=============");
                e1.printStackTrace();
                return;
            }
            _log.info("--------------------------------------法大大自动签署异步处理任务结束，订单号：" + ordid + "=============");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}

package com.hyjf.mqreceiver.fdd.fddgeneratecontract;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractBean;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractConstant;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractService;
import com.hyjf.common.validator.Validator;
import com.rabbitmq.client.Channel;


/**
 * 法大大生成合同处理
 */
@Component(value = "fddGenerateContractMessageHandle")
public class FddGenerateContractMessageHandle implements ChannelAwareMessageListener {

    Logger _log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FddGenerateContractService fddGenerateContractService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
            _log.info("----------------------开始生成法大大合同------------------------");
            if(message == null || message.getBody() == null){
                _log.error("【生成法大大合同任务】接收到的消息为null");
                // 消息队列的指令消费
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            }

            String msgBody = new String(message.getBody());

             _log.info("【法大大合同】接收到的消息：" + msgBody);

            FddGenerateContractBean bean = null;
            //订单号
            String ordid = null;

            //交易类型 1:散标 2：计划加入 3：直投债转 4：计划债转
            Integer transType = null;
            try {
                bean = JSONObject.parseObject(msgBody, FddGenerateContractBean.class);
                if(Validator.isNull(bean)){
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                    return;
                }
                ordid = bean.getOrdid();
                transType = bean.getTransType();
                if(ordid == null  || transType == null){
                    throw new RuntimeException("传入参数不得为空！");
                }

                if (FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == transType){//散标出借
                    fddGenerateContractService.tenderGenerateContract(bean);

                }else if(FddGenerateContractConstant.PROTOCOL_TYPE_PLAN == transType){//计划加入
                    fddGenerateContractService.planJoinGenerateContract(bean);
                }else if(FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT == transType){//债转出借

                    fddGenerateContractService.creditGenerateContract(bean);
                }else if(FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET == transType ){//计划债转
                    fddGenerateContractService.planCreditGenerateContract(bean);
                }else if(FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET == transType){//垫付债转出借

                    fddGenerateContractService.creditGenerateContractApply(bean);
                }else if(FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET == transType ){//计划垫付债转
                    fddGenerateContractService.planCreditGenerateContractApply(bean);
                }
                
            } catch (Exception e1) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                _log.info("--------------------------------------生成法大大合同任务异常，订单号：" + ordid + ",错误信息："+ e1.getMessage()+"=============");
                e1.printStackTrace();
                return;
            }
            _log.info("--------------------------------------生成法大大合同任务结束，订单号：" + ordid + "=============");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}

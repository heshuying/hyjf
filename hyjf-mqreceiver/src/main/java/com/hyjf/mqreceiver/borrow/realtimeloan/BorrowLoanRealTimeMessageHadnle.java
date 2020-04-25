package com.hyjf.mqreceiver.borrow.realtimeloan;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.BorrowService;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.bank.service.realtimeborrowloan.zhitouloan.RealTimeBorrowLoanService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 直投类项目放款任务
 * @author zhangjinpeng
 *
 */
@Component(value="borrowLoanZTRealTimeMessageHadnle")
public class BorrowLoanRealTimeMessageHadnle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(BorrowLoanRealTimeMessageHadnle.class);

    @Autowired
	RealTimeBorrowLoanService batchLoanService;

    @Autowired
	private BorrowService borrowService;
    
    @Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	@Autowired
	private MqService mqService;
    
    /**
     * 消息监听
     */
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------直投实时放款任务开始------------------------");
        if(message == null || message.getBody() == null){
            _log.error("【直投实时放款任务】接收到的消息为null");
            // 消息队列的指令消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        _log.info("----------------------直投实时放款任务开始------------------------msgBody:"+msgBody);
        BorrowApicron borrowApicron;
        try {
            borrowApicron = JSONObject.parseObject(msgBody, BorrowApicron.class);
            if(Validator.isNull(borrowApicron)){
            	channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                return;
            }
        } catch (Exception e1) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            e1.printStackTrace();
            return;
        }
        String borrowNid = borrowApicron.getBorrowNid();// 借款编号
		int borrowUserId = borrowApicron.getUserId();// 借款人userId
		int loanStatus = borrowApicron.getStatus();//放款状态
		_log.info("标的编号："+borrowNid+"，开始实时放款！");
        // 生成任务key 校验并发请求
        String redisKey = RedisConstants.ZHITOU_LOAN_TASK + ":" + borrowApicron.getBorrowNid() + "_" + borrowApicron.getPeriodNow();
        boolean result = RedisUtils.tranactionSet(redisKey, 300);
        if(!result){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("直投类放款请求中....");
            return;
        }
        Integer failTimes = borrowApicron.getFailCounts();
        try {
			// 如果放款状态为请求中
			if (loanStatus == CustomConstants.BANK_BATCH_STATUS_SENDING ) {
				//发送放款
				BankCallBean requestLoanBean = this.batchLoanService.requestLoans(borrowApicron);
				if (requestLoanBean == null) {
					borrowApicron.setFailTimes(borrowApicron.getFailTimes() + 1);
					// 放款失败处理
					boolean batchDetailFlag = this.batchLoanService.updateBatchDetailsQuery(borrowApicron,requestLoanBean);
					if (!batchDetailFlag) {
						throw new Exception("放款成功后，变更放款数据失败。" + "[借款编号：" + borrowNid + "]");
					}
					
					boolean apicronResultFlag = this.batchLoanService.updateBorrowApicron(borrowApicron, CustomConstants.BANK_BATCH_STATUS_FAIL);
					if (apicronResultFlag) {
						throw new Exception("更新状态为（放款请求失败）失败。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
					} else {
						throw new Exception("放款失败,[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
					}
				}else{//放款成功
					
					// 进行后续操作
					boolean batchDetailFlag = this.batchLoanService.updateBatchDetailsQuery(borrowApicron,requestLoanBean);
					if (!batchDetailFlag) {
						throw new Exception("放款成功后，变更放款数据失败。" + "[借款编号：" + borrowNid + "]");
					}
					// 放款成功,更新mongo运营数据
					_log.info("放款成功更新运营数据...");
					JSONObject params = new JSONObject();
					params.put("type", 1);// 散标
					BorrowWithBLOBs borrow = borrowService.getBorrowByNid(borrowNid);
					if (borrow != null) {
						params.put("money", borrow.getAccount());
					}
					mqService.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_OPERATION_DATA, JSONObject.toJSONString(params));
					// 发送MQ消息生成合同信息要素
					try {
						this.batchLoanService.sendToMQContractEssence(borrowNid);

						// add 合规数据上报 埋点 liubin 20181122 start
						// 推送数据到MQ 放款成功
						JSONObject param = new JSONObject();
						param.put("borrowNid",borrowNid);
						mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.LOAN_SUCCESS_DELAY_KEY, JSONObject.toJSONString(param));
						// add 合规数据上报 埋点 liubin 20181122 end

					} catch (Exception e) {
						_log.error("发送生成合同信息要素MQ失败，borrowNid：" + borrowNid);
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			StringBuffer sbError = new StringBuffer();// 错误信息
			sbError.append(e.getMessage()).append("<br/>");
			String online = "生产环境";// 取得是否线上
			String payUrl = PropUtils.getSystem(CustomConstants.HYJF_ONLINE);
			if (payUrl == null || !payUrl.contains("online")) {
				online = "测试环境";
			}
			// 发送错误邮件
			StringBuffer msg = new StringBuffer();
			msg.append("借款标号：").append(borrowNid).append("<br/>");
			msg.append("放款时间：").append(GetDate.formatTime()).append("<br/>");
			msg.append("执行次数：").append("第" + failTimes + "次").append("<br/>");
			msg.append("错误信息：").append(e.getMessage()).append("<br/>");
			msg.append("详细错误信息：<br/>").append(sbError.toString());
			String[] toMail = new String[] {};
			if ("测试环境".equals(online)) {
				toMail = new String[] { "jiangying@hyjf.com", "liudandan@hyjf.com" };
			} else {
				toMail = new String[] { "sunjijin@hyjf.com", "gaohonggang@hyjf.com", };
			}
			MailMessage mailmessage = new MailMessage(null, null, "[" + online + "] " + borrowNid + " 第" + failTimes + "次放款失败", msg.toString(), null, toMail, null,
					MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
			mailMessageProcesser.gather(mailmessage);
			// 消息队列指令不消费
        	channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
            _log.error("放款请求系统异常....");
            return;
		}
		_log.info("--------------------------------------放款任务结束，项目编号：" + borrowNid + "=============");
		channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		RedisUtils.del(redisKey);
		_log.info("----------------------------直投放款结束--------------------------------");
    }
    
    
    
}

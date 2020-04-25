package com.hyjf.mqreceiver.borrow.loan;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrowloan.zhitou.BatchBorrowLoanService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.rabbitmq.client.Channel;

/**
 * 直投类项目放款任务
 * @author zhangjinpeng
 *
 */
@Component(value="borrowLoanZTMessageHadnle")
public class BorrowLoanZTMessageHadnle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(BorrowLoanZTMessageHadnle.class);

    @Autowired
	BatchBorrowLoanService batchLoanService;
    
    @Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
    
    /**
     * 消息监听
     */
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------直投放款任务开始------------------------");
        if(message == null || message.getBody() == null){
            _log.error("【直投放款任务】接收到的消息为null");
            // 消息队列的指令不消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        
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
		String batchNo = borrowApicron.getBatchNo();
		String txDate = Validator.isNotNull(borrowApicron.getTxDate()) ? String.valueOf(borrowApicron.getTxDate()) : null;
		_log.info("标的编号："+borrowNid+"，直投类开始放款！");
        // 生成任务key 校验并发请求
        String redisKey = RedisConstants.ZHITOU_LOAN_TASK + ":" + borrowApicron.getBorrowNid() + "_" + borrowApicron.getPeriodNow();
        boolean result = RedisUtils.tranactionSet(redisKey, 300);
        if(!result){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("直投类放款请求中....");
            return;
        }
        
        try{
        	_log.info("标的编号："+borrowNid+"，请求成功或校验成功。。。");
			// 如果已经发生过相应的放款请求，则查询相应的状态
			if (StringUtils.isBlank(batchNo) || StringUtils.isBlank(txDate)) {
				throw new Exception("参数信息不全");
			}
			String bankSeqNo = borrowApicron.getBankSeqNo();// 放款序列号
			// 查询批次放款状态
			BankCallBean batchResult = this.batchLoanService.batchQuery(borrowApicron);
			if (Validator.isNull(batchResult)) {
				throw new Exception("放款状态查询失败！[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
			}
			_log.info("标的编号："+borrowNid+"，批次查询成功！");
			// 批次放款返回码
			String retCode = StringUtils.isNotBlank(batchResult.getRetCode()) ? batchResult.getRetCode() : "";
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				String retMsg = batchResult.getRetMsg();
				throw new Exception("放款状态查询失败！银行返回信息：" + retMsg + ",[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
			}
			// 批次放款状态
			String batchState = batchResult.getBatchState();
			if (StringUtils.isBlank(batchState)) {
				throw new Exception("放款状态查询失败！[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
			}
			
			_log.info("标的编号："+borrowNid+"，批次查询状态："+batchState);
			// 如果是批次处理失败
			if (batchState.equals(BankCallConstant.BATCHSTATE_TYPE_FAIL)) {
				_log.info("标的编号："+borrowNid+"，批次处理失败");
				String failMsg = batchResult.getFailMsg();// 失败原因
				if (StringUtils.isNotBlank(failMsg)) {
					borrowApicron.setData(failMsg);
					borrowApicron.setFailTimes(borrowApicron.getFailTimes() + 1);
					// 更新任务API状态
					boolean apicronResultFlag = this.batchLoanService.updateBorrowApicron(borrowApicron, CustomConstants.BANK_BATCH_STATUS_FAIL);
					if (!apicronResultFlag) {
						throw new Exception("更新状态为（放款请求失败）失败。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
					}
				} else {
					// 查询批次交易明细，进行后续操作
					boolean batchDetailFlag = this.batchLoanService.batchDetailsQuery(borrowApicron);
					// 进行后续失败的放款的放款请求
					if (!batchDetailFlag) {
						throw new Exception("放款失败后，查询放款明细失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
					}
				}
			}
			// 如果是批次处理成功
			else if (batchState.equals(BankCallConstant.BATCHSTATE_TYPE_SUCCESS)) {
				_log.info("标的编号："+borrowNid+"，批次处理成功");
				// 查询批次交易明细，进行后续操作
				boolean batchDetailFlag = this.batchLoanService.batchDetailsQuery(borrowApicron);
				_log.info("标的编号："+borrowNid+"，查询批次交易明细，进行后续操作，操作结果："+batchDetailFlag);
				if (!batchDetailFlag) {
					throw new Exception("放款成功后，查询放款明细失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
				_log.info("标的编号："+borrowNid+"，放款成功！");
			}
        }catch(Exception e){
        	StringBuffer sbError = new StringBuffer();// 错误信息
        	sbError.append(e.getMessage()).append("<br/>");
        	String online = "生产环境";// 取得是否线上
        	String payUrl = PropUtils.getSystem(CustomConstants.HYJF_ONLINE);
        	if (payUrl == null || !payUrl.contains("online")) {
        		online = "测试环境";
        	}
        	// 发送错误邮件
        	StringBuffer msg = new StringBuffer();
        	msg.append("借款标号：").append(borrowApicron.getBorrowNid()).append("<br/>");
        	msg.append("放款时间：").append(GetDate.formatTime()).append("<br/>");
        	msg.append("错误信息：").append(e.getMessage()).append("<br/>");
        	msg.append("详细错误信息：<br/>").append(sbError.toString());
        	String[] toMail = new String[] {};
        	if ("测试环境".equals(online)) {
        		toMail = new String[] { "jiangying@hyjf.com", "liudandan@hyjf.com" };
        	} else {
        		toMail = new String[] { "sunjijin@hyjf.com", "gaohonggang@hyjf.com","zhangjinpeng@hyjf.com" };
        	}
        	MailMessage mailMessage = new MailMessage(null, null, "[" + online + "] " + borrowApicron.getBorrowNid(), msg.toString(), null, toMail, null,
        			MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
        	mailMessageProcesser.gather(mailMessage);
        	// 消息队列指令不消费
        	channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
            _log.error("直投放款系统异常....");
            return;
        }
		channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		RedisUtils.del(redisKey);
		_log.info("----------------------------直投放款结束--------------------------------");
    }
    
}

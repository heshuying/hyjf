package com.hyjf.mqreceiver.borrow.planrepay;

import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrowrepay.plan.BatchHjhBorrowRepayService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 计划类项目还款任务
 * @author zhangjinpeng
 *
 */
@Component(value="borrowRepayPlanMessageHadnle")
public class BorrowRepayPlanMessageHadnle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(BorrowRepayPlanMessageHadnle.class);

    @Autowired
    BatchHjhBorrowRepayService batchRepayService;
    
    @Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	@Autowired
	private MqService mqService;

    /**
     * 消息监听
     */
    public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------计划还款任务开始----------------------服务类:" + new BorrowRepayPlanMessageHadnle().toString());
        if(message == null || message.getBody() == null){
            _log.error("【计划还款任务】接收到的消息为null");
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
		_log.info("标的编号："+borrowNid+"，计划类开始还款！");
        // 生成任务key 校验并发请求
        String redisKey = RedisConstants.PLAN_REPAY_TASK + ":" + borrowApicron.getBorrowNid() + "_" + borrowApicron.getPeriodNow();
        boolean result = RedisUtils.tranactionSet(redisKey, 300);
        if(!result){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            _log.error("计划类还款请求中....");
            return;
        }
        
        try{
        	_log.info("标的编号："+borrowNid+"，请求成功或校验成功。。。");
			// 如果已经发生过相应的还款请求，则查询相应的状态
			if (StringUtils.isBlank(batchNo) || StringUtils.isBlank(txDate)) {
				throw new Exception("参数信息不全");
			}
			//从数据库获取最新任务批次状态 add by cwyang 20180723
			BorrowApicron apicron = this.batchRepayService.getBorrowApicron(borrowApicron.getId());
			Integer apicronStatus = apicron.getStatus();
			Integer oldStatus = borrowApicron.getStatus();
			if(!oldStatus.equals(apicronStatus)){//任务状态已更新
				channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
				_log.info("计划还款标的：" + borrowApicron.getBorrowNid() + "，任务状态已变更，不执行该次任务！");
				return;
			}

			String bankSeqNo = borrowApicron.getBankSeqNo();// 还款序列号
			Integer isAllrepay = borrowApicron.getIsAllrepay();//判断是否为一次性还款
			if(isAllrepay == null){
				isAllrepay = 0;
			}
			// 查询批次还款状态
			BankCallBean batchResult = this.batchRepayService.batchQuery(borrowApicron);
			if (Validator.isNull(batchResult)) {
				throw new Exception("还款状态查询失败！[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
			}
			_log.info("标的编号："+borrowNid+"，批次查询成功！");
			// 批次还款返回码
			String retCode = StringUtils.isNotBlank(batchResult.getRetCode()) ? batchResult.getRetCode() : "";
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				String retMsg = batchResult.getRetMsg();
				throw new Exception("还款状态查询失败！银行返回信息：" + retMsg + ",[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
			}
			// 批次还款状态
			String batchState = batchResult.getBatchState();
			if (StringUtils.isBlank(batchState)) {
				throw new Exception("还款状态查询失败！[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
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
					boolean apicronResultFlag = this.batchRepayService.updateBorrowApicron(borrowApicron, CustomConstants.BANK_BATCH_STATUS_FAIL);
					if (!apicronResultFlag) {
						throw new Exception("更新状态为（还款请求失败）失败。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
					}
				} else {
					boolean batchDetailFlag = false;
					if(1 == isAllrepay){
						if(sortRepay(borrowApicron)){//对一次性还款的任务处理进行排序
							batchDetailFlag = this.batchRepayService.updateBatchDetailsQuery(borrowApicron);
						}
					}else{
						// 查询批次交易明细，进行后续操作
						batchDetailFlag = this.batchRepayService.updateBatchDetailsQuery(borrowApicron);
					}
					// 进行后续失败的还款的还款请求
					if (!batchDetailFlag) {
						throw new Exception("还款失败后，查询还款明细失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
					}
				}
			}
			// 如果是批次处理成功
			else if (batchState.equals(BankCallConstant.BATCHSTATE_TYPE_SUCCESS)) {
				_log.info("标的编号："+borrowNid+"，批次处理成功");
				// 查询批次交易明细，进行后续操作
				boolean batchDetailFlag = false;
				if(1 == isAllrepay){
					if(sortRepay(borrowApicron)){
						batchDetailFlag = this.batchRepayService.updateBatchDetailsQuery(borrowApicron);
					}
				}else{
					batchDetailFlag = this.batchRepayService.updateBatchDetailsQuery(borrowApicron);
				}
				_log.info("标的编号："+borrowNid+"，查询批次交易明细，进行后续操作，操作结果："+batchDetailFlag);
				if (!batchDetailFlag) {
					throw new Exception("还款成功后，查询还款明细失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
				_log.info("标的编号："+borrowNid+"，还款成功！");
				try {
					// upd by liushouyi nifa2 20181204 start
					// 发送MQ到生成还款记录、合同状态、出借人回款信息
					if (CustomConstants.BANK_BATCH_STATUS_SUCCESS.equals(borrowApicron.getStatus())) {
						// 最后一期
						if(borrowApicron.getBorrowPeriod().equals(borrowApicron.getPeriodNow())){
							Map<String, String> params = new HashMap<String, String>();
							params.put("borrowNid", borrowNid);
							params.put("flag", "2");
							// status=5标的已还款
							params.put("status", "5");
							this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.REPAY_ALL_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
						}
						// 发送发标成功的消息队列到互金上报数据
						Map<String, String> params = new HashMap<String, String>();
						params.put("borrowNid", borrowNid);
						params.put("repayPeriod", borrowApicron.getPeriodNow() + "");
						this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.REPAY_SINGLE_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
						// upd by liushouyi nifa2 20181204 end
						// 发送MQ到生成还款记录、合同状态、出借人回款信息
						this.batchRepayService.sendToMQ(borrowApicron);
						_log.info("标的编号：" + borrowNid + "，还款期次：" + borrowApicron.getBorrowPeriod() + "发送MQ生成还款记录、合同状态、回款信息成功!");
					}
				} catch (Exception e) {
					_log.info("标的编号："+borrowNid+"，还款期次：" + borrowApicron.getBorrowPeriod() + "发送MQ生成还款记录、合同状态、回款信息失败!");
					e.printStackTrace();
				}
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch(Exception e){
        	StringBuffer sbError = new StringBuffer();// 错误信息
        	sbError.append(e.getMessage()).append("<br/>");
        	String online = "生产环境";// 取得是否线上
        	String isOnline = PropUtils.getSystem(CustomConstants.HYJF_ONLINE);
        	if (isOnline == null || !isOnline.contains("online")) {
        		online = "测试环境";
        	}
        	// 发送错误邮件
        	StringBuffer msg = new StringBuffer();
        	msg.append("借款标号：").append(borrowApicron.getBorrowNid()).append("<br/>");
        	msg.append("还款时间：").append(GetDate.formatTime()).append("<br/>");
        	msg.append("错误信息：").append(e.getMessage()).append("<br/>");
        	msg.append("详细错误信息：<br/>").append(sbError.toString());
        	String[] toMail = new String[] {};
        	if ("测试环境".equals(online)) {
				String payUrl = PropUtils.getSystem(CustomConstants.HYJF_EMAIL_REPAY_TEST);
				if(StringUtils.isNotBlank(payUrl)){
					toMail = payUrl.split(",");
				}else{
					_log.error("-------请配置还款邮件发送地址---------");
				}
        	} else {
				String payUrl = PropUtils.getSystem(CustomConstants.HYJF_EMAIL_REPAY_ONLINE);
				if(StringUtils.isNotBlank(payUrl)){
					toMail = payUrl.split(",");
				}else{
					_log.error("-------请配置还款邮件发送地址---------");
				}
        	}
        	MailMessage mailMessage = new MailMessage(null, null, "[" + online + "] " + borrowApicron.getBorrowNid(), msg.toString(), null, toMail, null,
        			MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
        	mailMessageProcesser.gather(mailMessage);
        	// 消息队列指令不消费
        	channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
            _log.error("计划还款系统异常....");
            return;
        }
		RedisUtils.del(redisKey);
		_log.info("----------------------------计划还款结束--------------------------------");
    }

	/**
	 * 对需要做还款处理的还款任务进行排序，按还款期数逐条执行
	 * add by cwyang 2018-5-21
	 * @param borrowApicron
	 * @return
	 */
	private boolean sortRepay(BorrowApicron borrowApicron) {

		String borrowNid = borrowApicron.getBorrowNid();
		Integer periodNow = borrowApicron.getPeriodNow();
		BorrowApicron apicron = this.batchRepayService.getRepayPeriodSort(borrowNid);
		if (apicron != null){
			Integer period = apicron.getPeriodNow();
			if (periodNow == period){
				return true;
			}
		}
		return false;
	}

}

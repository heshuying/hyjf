package com.hyjf.mqreceiver.borrow.repayrequest;


import java.util.Map;

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
import com.hyjf.bank.service.borrowrepay.zhitou.BatchBorrowRepayService;
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
 * 项目还款请求任务
 * @author zhangjinpeng
 *
 */
@Component(value="borrowRepayRequestMessageHadnle")
public class BorrowRepayRequestMessageHadnle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(BorrowRepayRequestMessageHadnle.class);

    @Autowired
    BatchBorrowRepayService borrowRepayRequestService;
    
    @Autowired
    BatchHjhBorrowRepayService hjhBorrowRepayRequestService; 
    
    @Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
    
    /**
     * 消息监听
     */
    @SuppressWarnings("rawtypes")
	public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------本金还款请求开始----------------------服务类:" + new BorrowRepayRequestMessageHadnle().toString());
        if(message == null || message.getBody() == null){
            _log.error("【本金还款请求】接收到的消息为null");
            // 消息队列的指令不消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        
        _log.info("【本金还款请求】接收到的消息：" + msgBody);
        
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
        Integer repayUserId = borrowApicron.getUserId();// 还款用户userId
		String borrowNid = borrowApicron.getBorrowNid();// 项目编号
		Integer periodNow = borrowApicron.getPeriodNow();// 当前期数
        //验证请求参数
        if (Validator.isNull(repayUserId) || Validator.isNull(borrowNid) 
        		|| Validator.isNull(periodNow)) {
            _log.error("【本金还款请求】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        // 生成任务key 校验并发请求
        String redisKey = RedisConstants.REPAY_REQUEST_TASK + borrowApicron.getPeriodNow();
	    boolean result = this.outRepeatQueue(redisKey, borrowApicron);//modify by cwyang 数据库事故后变更,不设时间,待所有流程结束后再去除防重标示
        if(result){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            
            _log.error("本金还款请求中....");
            //TODO 还款请求重复,需要判断是否为银行请求成功,平台处理失败的情况,分情况处理
            boolean flag = checkLoanRequestException(borrowApicron, redisKey);
            if (flag) {//存在请求异常情况,进行后续处理
				boolean updateResult = updateLoanRequestException(borrowApicron);
				if (updateResult) {//异常情况修复完成
					//TODO 去除防重标示
					delRedisKey(borrowApicron);
				}else{
					_log.error("------------------标的号:" + borrowNid + ",还款请求异常修复失败,请人为处理!-----------");
				}
			}
            return;
        }
        boolean delFlag = false;
        try{
        	String planNid = borrowApicron.getPlanNid();
        	Map map = null;
        	// 全部发送还款请求
        	if (StringUtils.isNotBlank(planNid)) {//计划还款请求
        		map = this.hjhBorrowRepayRequestService.requestRepay(borrowApicron);
			}else{//直投还款请求
				map = this.borrowRepayRequestService.requestRepay(borrowApicron);
			}
        	boolean requestLoanFlag = (boolean) map.get("result");
        	delFlag = (boolean) map.get("delFlag");
        	if (!requestLoanFlag) {
        		try {
        			// 更新任务API状态
            		this.borrowRepayRequestService.updateBorrowApicron(borrowApicron, CustomConstants.BANK_BATCH_STATUS_SEND_FAIL);
				} catch (Exception e) {
					delFlag = true;
					throw new Exception("-------------" + borrowNid + "--本金还款请求完成,变更请求失败异常!-----------");
				}
        	}
        	if (!delFlag) {
        		delRedisKey(borrowApicron);
			}
        	channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
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
        	msg.append("还款时间：").append(GetDate.formatTime()).append("<br/>");
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
            _log.error("还款请求系统异常....");
			e.printStackTrace();
            if (!delFlag) {
        		delRedisKey(borrowApicron);
    		}
            return;
        }
		_log.info("----------------------------本金出借还款请求结束--------------------------------");
    }
    /**
     * 删除队列防重key
     * @param borrowApicron
     */
    private void delRedisKey(BorrowApicron borrowApicron){
    	String key = RedisConstants.REPAY_REQUEST_TASK + borrowApicron.getPeriodNow();
     	String value = borrowApicron.getBorrowNid() + "_" + borrowApicron.getPeriodNow();
     	Long srem = RedisUtils.srem(key, value);
     	_log.info("----------" + borrowApicron.getBorrowNid() + "----本金还款请求正常完成,删除队列防重标示:" + srem);
    }
    /**
     * 处理银行成功,平台处理失败的情况
     * @param borrowApicron
     * @return
     */
    private boolean updateLoanRequestException(BorrowApicron borrowApicron) {
		// TODO 处理还款请求异常的问题
    	String borrowNid = borrowApicron.getBorrowNid();
    	_log.info("------------------------标的号:" + borrowNid + "开始处理还款请求异常-------------");
    	try {
    		boolean apicronResultFlag = this.borrowRepayRequestService.updateBorrowApicron(borrowApicron, CustomConstants.BANK_BATCH_STATUS_SENDED);
    		if (apicronResultFlag) {
    			_log.info("------------------------标的号:" + borrowNid + "处理还款请求异常成功,还款请求结果为成功!-------------");
    			return true;
			}else{
				throw new Exception("-------------------标的号:" + borrowNid + "的还款异常处理失败,变更api任务状态(请求成功)失败!-----------");
			}
		} catch (Exception e) {
			_log.error(e.getMessage());
		}
		return false;
	}

	/**
     * 校验是否存在银行请求成功,平台处理失败的情况
     * @param borrowApicron
     */
	private boolean checkLoanRequestException(BorrowApicron borrowApicron,String redisKey) {
		// TODO 校验是否存在还款请求异常的问题
		//判断批次号是否为空
		String batchNo = borrowApicron.getBatchNo();
		String borrowNid = borrowApicron.getBorrowNid();
		String bankSeqNo = borrowApicron.getBankSeqNo();// 还款序列号
		_log.info("-----------------标的号:" + borrowNid + "开始校验是否存在请求异常的情况,批次号:" + batchNo + "-------------------------");
		try {
			if (StringUtils.isNotBlank(batchNo)) {
				BankCallBean batchResult = this.borrowRepayRequestService.batchQuery(borrowApicron);
				if (Validator.isNull(batchResult)) {
					throw new Exception("还款状态查询失败！[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
				_log.info("标的编号："+borrowNid+"，批次查询成功！");
				// 批次还款返回码
				String retCode = StringUtils.isNotBlank(batchResult.getRetCode()) ? batchResult.getRetCode() : "";
				if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
					String retMsg = batchResult.getRetMsg();
					//TODO 批次号不存在则删除标示,重新发起申请
					if (BankCallConstant.RESPCODE_BATCHNO_NOTEXIST.equals(retCode)) {
						delRedisKey(borrowApicron);
					}
					throw new Exception("还款状态查询失败！银行返回信息：" + retMsg + ",返回码:" + retCode + "[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
				// 批次还款状态
				String batchState = batchResult.getBatchState();
				if (StringUtils.isBlank(batchState)) {
					throw new Exception("还款状态查询失败！[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
				_log.info("标的编号："+borrowNid+"，批次查询状态："+batchState);
				_log.info("-----------------标的号:" + borrowNid + "存在还款请求异常情况,已经发起过还款请求,需要进行异常处理!");
				return true;
			}else{
				delRedisKey(borrowApicron);
			}
		} catch (Exception e) {
			_log.info("---------------标的号:" + borrowNid + "还款请求异常处理校验异常:" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 校验一个队列中的重复任务
	 * @param key
	 * @param value
	 * @param taskTpye
	 */
	private boolean outRepeatQueue(String key,BorrowApicron apicron){
		String value = apicron.getBorrowNid() + "_" + apicron.getPeriodNow();
		// 通过判断redis中是否存在该项目编号 从而判断该项目编号是否已经在消息队列中，防止重复放款
		if(RedisUtils.sismember(key, value)){
			return true;
		}
		// 将新的待放款的项目编号放入redis
		RedisUtils.sadd(key, value);
		return false;
	}
}

package com.hyjf.mqreceiver.borrow.loanrequest;

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
 * 项目放款请求任务
 * @author cwyang
 *
 */
@Component(value="borrowLoanRequestMessageHadnle")
public class BorrowLoanRequestMessageHadnle implements ChannelAwareMessageListener {
    Logger _log = LoggerFactory.getLogger(BorrowLoanRequestMessageHadnle.class);

    @Autowired
	BatchBorrowLoanService batchLoanService;
    
    @Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
    
    /**
     * 消息监听
     */
    @SuppressWarnings("rawtypes")
	public void onMessage(Message message, Channel channel) throws Exception {
        _log.info("----------------------本金放款请求开始----------------------");
        if(message == null || message.getBody() == null){
            _log.error("【本金放款请求】接收到的消息为null");
            // 消息队列的指令不消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        
        String msgBody = new String(message.getBody());
        
        _log.info("【本金放款请求】接收到的消息：" + msgBody);
        
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
        Integer borrowUserId = borrowApicron.getUserId();// 还款用户userId
		String borrowNid = borrowApicron.getBorrowNid();// 项目编号
		Integer periodNow = borrowApicron.getPeriodNow();// 当前期数
        //验证请求参数
        if (Validator.isNull(borrowUserId) || Validator.isNull(borrowNid) 
        		|| Validator.isNull(periodNow)) {
            _log.error("【本金放款请求】接收到的消息中信息不全");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        // 生成任务key 校验并发请求
        String redisKey = RedisConstants.LOAN_REQUEST_TASK + borrowApicron.getPeriodNow();
	    boolean result = this.outRepeatQueue(redisKey, borrowApicron);//modify by cwyang 数据库事故后变更,不设时间,待所有流程结束后再去除防重标示    
        if(result){
            _log.error("本金放款请求中....");
            //TODO 放款请求重复,需要判断是否为银行请求成功,平台处理失败的情况,分情况处理
            boolean flag = checkLoanRequestException(borrowApicron, redisKey);
            if (flag) {//存在请求异常情况,进行后续处理
				boolean updateResult = updateLoanRequestException(borrowApicron);
				if (updateResult) {//异常情况修复完成
					//TODO 去除防重标示
					delRedisKey(borrowApicron);
				}else{
					_log.error("------------------标的号:" + borrowNid + ",放款请求异常修复失败,请人为处理!-----------");
				}
			}
    		channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        _log.info("放款请求任务开始，项目编号：" + borrowNid);
        //确定一个删除标示,确定本次放款请求是否正常结束!(如果变更数据库异常,则认为是异常情况,不删除防重标示)
        boolean errFlag = false;
        try{
        	// 全部发送放款请求
        	Map resultMap = this.batchLoanService.startRequestLoans(borrowApicron);
			boolean requestLoanFlag = (boolean) resultMap.get("result");
			errFlag = (boolean) resultMap.get("delFlag");
			if (!requestLoanFlag) {
				try {
					// 更新任务API状态
					boolean apicronResultFlag = this.batchLoanService.updateBorrowApicron(borrowApicron, CustomConstants.BANK_BATCH_STATUS_SEND_FAIL);
					if (!apicronResultFlag) {
						errFlag = true;
						throw new Exception("更新状态为（放款请求失败）失败。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
					} else {
						throw new Exception("放款失败请求失败,[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
					}
				} catch (Exception e) {
					errFlag = true;
					throw new Exception("更新状态为（放款请求失败）失败。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
				}
			}
			if (!errFlag) {
				delRedisKey(borrowApicron);
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
            _log.error("放款请求系统异常....");
//    		RedisUtils.del(redisKey); modify by cwyang 防重标示注释,待完全成功或还没发起过放款请求或发起请求异常的情况下删除标示,以便二次请求
    		if (!errFlag) {
    			delRedisKey(borrowApicron);
    		}
            return;
        }
        String delkey = RedisConstants.LOAN_REQUEST_TASK + borrowApicron.getPeriodNow();
		String value = borrowApicron.getBorrowNid() + "_" + borrowApicron.getPeriodNow();
		Long srem = RedisUtils.srem(delkey, value);
		_log.info("==============标的号:" + borrowNid + "放款请求完成,删除队列防重标示,删除标示:" + srem);
		channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		_log.info("----------------------------本金出借放款请求结束--------------------------------");
    }

    /**
     * 处理银行成功,平台处理失败的情况
     * @param borrowApicron
     * @return
     */
    private boolean updateLoanRequestException(BorrowApicron borrowApicron) {
		// TODO 处理放款请求异常的问题
    	String borrowNid = borrowApicron.getBorrowNid();
    	_log.info("------------------------标的号:" + borrowNid + "开始处理放款请求异常-------------");
    	//如果已经发起过一次放款请求,先获取放款请求的结果
    	try {
    		boolean apicronResultFlag = this.batchLoanService.updateBorrowApicron(borrowApicron, CustomConstants.BANK_BATCH_STATUS_SENDED);
    		if (apicronResultFlag) {
    			_log.info("------------------------标的号:" + borrowNid + "处理放款请求异常成功,放款请求结果为成功!-------------");
    			return true;
			}else{
				throw new Exception("-------------------标的号:" + borrowNid + "的放款异常处理失败,变更api任务状态(请求成功)失败!-----------");
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
		// TODO 校验是否存在放款请求异常的问题
		//判断批次号是否为空
		String batchNo = borrowApicron.getBatchNo();
		String borrowNid = borrowApicron.getBorrowNid();
		String bankSeqNo = borrowApicron.getBankSeqNo();// 放款序列号
		_log.info("-----------------标的号:" + borrowNid + "开始校验是否存在请求异常的情况,批次号:" + batchNo + "-------------------------");
		try {
			if (StringUtils.isNotBlank(batchNo)) {
				BankCallBean batchResult = this.batchLoanService.batchQuery(borrowApicron);
				if (Validator.isNull(batchResult)) {
					throw new Exception("放款状态查询失败！[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
				_log.info("标的编号："+borrowNid+"，批次查询成功！");
				// 批次放款返回码
				String retCode = StringUtils.isNotBlank(batchResult.getRetCode()) ? batchResult.getRetCode() : "";
				if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
					String retMsg = batchResult.getRetMsg();
					//TODO 批次号不存在则删除标示,重新发起申请
					if (BankCallConstant.RESPCODE_BATCHNO_NOTEXIST.equals(retCode)) {
						delRedisKey(borrowApicron);
					}
					throw new Exception("放款状态查询失败！银行返回信息：" + retMsg + ",返回码:" + retCode + "[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
				// 批次放款状态
				String batchState = batchResult.getBatchState();
				if (StringUtils.isBlank(batchState)) {
					throw new Exception("放款状态查询失败！[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
				_log.info("标的编号："+borrowNid+"，批次查询状态："+batchState);
				_log.info("-----------------标的号:" + borrowNid + "存在放款请求异常情况,已经发起过放款请求,需要进行异常处理!");
				return true;
			}else{
				delRedisKey(borrowApicron);
			}
		} catch (Exception e) {
			_log.info("---------------标的号:" + borrowNid + "放款请求异常处理校验异常:" + e.getMessage());
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
	/**
	 * 删除防重标示
	 * @param borrowApicron
	 */
	public void delRedisKey(BorrowApicron borrowApicron){
		String delkey = RedisConstants.LOAN_REQUEST_TASK + borrowApicron.getPeriodNow();
		String value = borrowApicron.getBorrowNid() + "_" + borrowApicron.getPeriodNow();
		Long srem = RedisUtils.srem(delkey, value);
		_log.info("==============标的号:" + borrowApicron.getBorrowNid() + "放款请求完成,删除队列防重标示,删除标示:" + srem);
	}
	
}

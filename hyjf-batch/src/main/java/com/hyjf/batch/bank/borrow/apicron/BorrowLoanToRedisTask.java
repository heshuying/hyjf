package com.hyjf.batch.bank.borrow.apicron;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * 取得发起的放款请求，将放款信息放入redis并放入消息队列通知放款
 * @author wgx
 * @date 2018/11/30
 */
public class BorrowLoanToRedisTask {
	
	
	/** 任务类别（1：还款） */
	private static final Integer TASK_REPAY_TYPE = 1;
	/**任务类别（0：放款） */
	private static final Integer TASK_LOAN_TYPE = 0;

	Logger _log = LoggerFactory.getLogger(BorrowLoanToRedisTask.class);

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	BorrowRepayToRedisService borrowRepayToRedisService;
	
	@Resource(name="myAmqpTemplate")
    AmqpTemplate amqpTemplate;
	
    public void sendMessage(String key,Object message){
        String msgString = JSONObject.toJSONString(message);
        amqpTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME,key,msgString);
    }
    
	public void run() {
		// 调用放款接口
		taskAssign();                      
	}

	/**
	 * 对放款任务进行分派
	 *
	 * @return
	 */
	private void taskAssign() {
		if (isRun == 1) {
			return;
		}
		isRun = 1;
        // 放款请求件数
        int repayRequestCount = 0;
		_log.info("自动放款消息推送任务开始。");
		try {
			// 取得直投类未放款任务
			List<BorrowApicron> listZhitouApicron = borrowRepayToRedisService.getBorrowApicronList(TASK_LOAN_TYPE);
			// 如果当前是否存在放款任务
			if(listZhitouApicron == null || listZhitouApicron.size() == 0){
				return;
			}
			// 循环进行将放款任务压入队列中还款
			for (BorrowApicron apicron : listZhitouApicron) {
				_log.info("任务缓存:项目编号:[" + apicron.getBorrowNid() + "].计划编号:[" + (StringUtils.isEmpty(apicron.getPlanNid()) ? "" : apicron.getPlanNid()) + "].");
				apicron = this.borrowRepayToRedisService.getBorrowApicron(apicron.getId());
				Integer status = apicron.getStatus();
				// 放款请求
				if(status == CustomConstants.BANK_BATCH_STATUS_SENDING || status == CustomConstants.BANK_BATCH_STATUS_SEND_FAIL){
					if(TASK_LOAN_TYPE == apicron.getApiType() && StringUtils.isBlank(apicron.getPlanNid())){//直投放款请求
                    	_log.info("------------------标的:" + apicron.getBorrowNid() + ",发送实时放款请求~------------------------");
                    	sendMessage("hyjf-routingkey-realTimeLoan-Request", apicron);
                    }else if(TASK_LOAN_TYPE == apicron.getApiType() && StringUtils.isNotBlank(apicron.getPlanNid())){//计划放款请求
                    	_log.info("------------------标的:" + apicron.getBorrowNid() + ",发送实时放款请求~------------------------");
                    	sendMessage("hyjf-routingkey-planRealTimeLoan-Request", apicron);
                    }
				}else if(status == CustomConstants.BANK_BATCH_STATUS_PART_FAIL || status == CustomConstants.BANK_BATCH_STATUS_FAIL){
					//放款部分成功或放款失败异常处理最终版
					if (TASK_LOAN_TYPE == apicron.getApiType()) {
						//直投类异常修复
						boolean flag = borrowRepayToRedisService.updateBatchFailLoan(apicron);
						if (flag) {
							_log.info("------------开始修复标的号:" + apicron.getBorrowNid() + ",放款异常!计划编号:" + apicron.getPlanNid());
							if (StringUtils.isBlank(apicron.getPlanNid())) {//直投标的
								sendMessage("hyjf-routingkey-realTimeLoan-Request", apicron);
							}else if(StringUtils.isNotBlank(apicron.getPlanNid())){//计划标的
								sendMessage("hyjf-routingkey-planRealTimeLoan-Request", apicron);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			isRun = 0;
		}
	}
	
}

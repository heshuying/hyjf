package com.hyjf.batch.bank.borrow.apicron;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.BorrowApicron;

/**
 * 取得还款人发起的还款请求，将还款信息放入redis并放入消息队列通知还款
 * @author zhangjinpeng
 *
 */
public class BorrowRepayToRedisTask {
	
	
	/** 任务类别（1：还款） */
	private static final Integer TASK_REPAY_TYPE = 1;
	/**任务类别（0：放款） */
	private static final Integer TASK_LOAN_TYPE = 0;

	Logger _log = LoggerFactory.getLogger(BorrowRepayToRedisTask.class);

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
		// 调用还款接口
		taskAssign();                      
	}

	/**
	 * 对还款任务进行分派
	 *
	 * @return
	 */
	private void taskAssign() {
		if (isRun == 1) {
			return;
		}
		isRun = 1;
        // 还款请求件数
        int repayRequestCount = 0;
		_log.info("自动还款消息推送任务开始。");
		try {
			// 取得直投类未还款任务
			List<BorrowApicron> listZhitouApicron = borrowRepayToRedisService.getBorrowApicronList(TASK_REPAY_TYPE);
			
			// 如果当前是否存在放款或还款任务
			if(listZhitouApicron == null || listZhitouApicron.size() == 0){
				return;
			}
			// 循环进行将还款任务压入队列中还款
			for (BorrowApicron apicron : listZhitouApicron) {
				_log.info("任务缓存:项目编号:[" + apicron.getBorrowNid() + "].计划编号:[" + (StringUtils.isEmpty(apicron.getPlanNid()) ? "" : apicron.getPlanNid()) + "].");
				apicron = this.borrowRepayToRedisService.getBorrowApicron(apicron.getId());
				Integer status = apicron.getStatus();
				// 放、还款请求
				if(status == CustomConstants.BANK_BATCH_STATUS_SENDING || status == CustomConstants.BANK_BATCH_STATUS_SEND_FAIL){
					if(TASK_REPAY_TYPE == apicron.getApiType()){
						_log.info("发起还款消息通知:还款项目编号:[" + apicron.getBorrowNid() + ",还款期数:[第" + apicron.getPeriodNow() + "],计划编号:[" + (StringUtils.isEmpty(apicron.getPlanNid()) ? "" : apicron.getPlanNid()));
						// 还款请求
						// TODO  发起消息通知
						sendMessage("hyjf-routingkey-Repay-Request", apicron);
                        repayRequestCount++;
                        _log.info("发起还款请求总数:[" + repayRequestCount + "].");
                    }
				}else if(status == CustomConstants.BANK_BATCH_STATUS_SENDED || status == CustomConstants.BANK_BATCH_STATUS_VERIFY_SUCCESS
						|| status == CustomConstants.BANK_BATCH_STATUS_PART_FAIL){
					// 放、还款任务
					if(TASK_REPAY_TYPE == apicron.getApiType() && StringUtils.isBlank(apicron.getPlanNid())){
						// 直投类还款
						sendMessage("hyjf-routingkey-Repay-ZT", apicron);
					}else if(TASK_REPAY_TYPE == apicron.getApiType() && StringUtils.isNotBlank(apicron.getPlanNid())){
						// 计划类还款
						sendMessage("hyjf-routingkey-Repay-Plan", apicron);
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

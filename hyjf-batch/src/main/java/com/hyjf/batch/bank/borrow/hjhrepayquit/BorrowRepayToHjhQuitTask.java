package com.hyjf.batch.bank.borrow.hjhrepayquit;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.model.auto.HjhAccede;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * 取得还款人发起的还款请求，将还款信息放入redis并放入消息队列通知还款
 * @author zhangjinpeng
 *
 */
public class BorrowRepayToHjhQuitTask {
	

	Logger _log = LoggerFactory.getLogger(BorrowRepayToHjhQuitTask.class);

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	BorrowRepayToHjhQuitService borrowRepayToHjhQuitService;
	
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
	 * 对计划退出任务进行分派
	 *
	 * @return
	 */
	private void taskAssign() {
		if (isRun == 1) {
			return;
		}
		isRun = 1;
		_log.info("-----------------汇计划计划进入锁定期/退出计划开始--------------------------------");
		try {
			String key = "hyjf-routingkey-Repay-hjhQuit";
			List<HjhAccede> accedeList = borrowRepayToHjhQuitService.selectWaitQuitHjhList();
			if (accedeList != null) {
				for (int i = 0; i < accedeList.size(); i++) {
					HjhAccede accede = accedeList.get(i);
					if(isrepeat(accede.getAccedeOrderId())){
						sendMessage(key, accede);
					}else {
						_log.info("-----------------汇计划计划进入锁定期/退出计划执行中，请勿重复执行，订单号："+ accede.getAccedeOrderId() +"--------------------------------");
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			_log.info("-----------------汇计划计划进入锁定期/退出计划结束--------------------------------");
		} finally {
			isRun = 0;
			
		}
	}

	/**
	 * 判断是否重复推送
	 * @return
	 * @param accedeOrderId
	 */
	private boolean isrepeat(String accedeOrderId) {

		String key = RedisConstants.HJH_LOCK_REPEAT;
		Boolean sismember = RedisUtils.sismember(key, accedeOrderId);
		if (sismember){
			return false;
		}
		RedisUtils.sadd(key,accedeOrderId);
		return true;
	}

}

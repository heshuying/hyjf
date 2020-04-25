package com.hyjf.batch.newyear2016.sendprize;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.NewyearQuestionUser;

/**
 * 2016新年活动-猜灯谜发放代金券
 * 
 * @author zhangjinpeng
 * 
 */
public class NewyearDengmiTask {
	/** 类名 */
	private static final String THIS_CLASS = NewyearDengmiTask.class.getName();
	Logger _log = LoggerFactory.getLogger(NewyearDengmiTask.class);
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	NewyearDengmiService newyearDengmiService;

	public void run() {
		String methodName = "run";
		if (isOver) {
			try {
				isOver = false;
				// 发放代金券
				prizeSend();
			}catch(Exception e){
				_log.info(THIS_CLASS + "==>" + methodName + "==>" + "2016新年活动-猜灯谜发放代金券异常！");
			} finally {
				isOver = true;
			}
		}

	}

	/**
	 * 补偿发放虚拟奖品
	 * @throws Exception 
	 */
	private void prizeSend() throws Exception {
		String methodName = "prizeSend";
		_log.info(THIS_CLASS + "==>" + methodName + "==>" + "2016新年活动-猜灯谜发放代金券开始！");
		List<NewyearQuestionUser> questionUserList = this.newyearDengmiService.selectQuestionUserList();
		if(null == questionUserList){
			return;
		}
		for(NewyearQuestionUser questionUser:questionUserList){
			this.newyearDengmiService.updatePrizeSend(questionUser);
		}
		
		_log.info(THIS_CLASS + "==>" + methodName + "==>" + "2016新年活动-猜灯谜发放代金券结束！");
	}

}

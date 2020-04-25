package com.hyjf.batch.newyear2016.sendcard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hyjf.soa.apiweb.CommonSoaUtils;

/**
 * 2016新年活动-活动期内用户注册且开户
 * 或邀请好用注册且开户 发放财神卡
 * 
 * @author zhangjinpeng
 * 
 */
public class NewyearSendCardTask {
	/** 类名 */
	private static final String THIS_CLASS = NewyearSendCardTask.class.getName();
	Logger _log = LoggerFactory.getLogger(NewyearSendCardTask.class);
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	public void run() {
		String methodName = "run";
		if (isOver) {
			try {
				isOver = false;
				// 发放代金券
				cardSend();
			}catch(Exception e){
				_log.info(THIS_CLASS + "==>" + methodName + "==>" + "2016新年活动-活动期内用户注册且开户发放财神卡异常！");
			} finally {
				isOver = true;
			}
		}

	}

	/**
	 * 补偿发放虚拟奖品
	 * @throws Exception 
	 */
	private void cardSend() throws Exception {
		String methodName = "cardSend";
		_log.info(THIS_CLASS + "==>" + methodName + "==>" + "2016新年活动-活动期内用户注册且开户发放财神卡开始！");
		CommonSoaUtils.newyearSendCard();
		_log.info(THIS_CLASS + "==>" + methodName + "==>" + "2016新年活动-活动期内用户注册且开户发放财神卡结束！");
	}

}

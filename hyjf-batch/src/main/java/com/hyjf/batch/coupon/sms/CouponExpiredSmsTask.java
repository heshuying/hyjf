package com.hyjf.batch.coupon.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 优惠券过期发送短信通知
 * @author zhangjp
 * @version hyjf 1.0
 * @since hyjf 1.0 
 * @see 上午11:42:54
 */
class CouponExpiredSmsTask {
    Logger _log = LoggerFactory.getLogger(CouponExpiredSmsTask.class);
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

    @Autowired
    CouponExpiredSmsService couponExpiredSmsService;

    public void run() {
    	if (isOver) {
			try {
				isOver = false;
				//
				sendExpiredMsg();
			} finally {
				isOver = true;
			}
		}
    }
    
    /**
     * 检查优惠券是否过期
     */
    private void sendExpiredMsg(){
    	_log.info("代金券到期短信提醒检查开始。。。");
    	couponExpiredSmsService.sendExpiredMsgAct();
    	_log.info("代金券到期短信提醒检查结束。。。");
    }

    
}

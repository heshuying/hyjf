package com.hyjf.batch.vip.push;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;

/**
 * 
 * 会员过期发送push消息
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年8月22日
 * @see 上午11:42:54
 */
class VipExpiredPushTask {
    /** 类名 */
    private static final String THIS_CLASS = VipExpiredPushTask.class.getName();


    @Autowired
    VipExpiredPushService couponExpiredPushService;

    public void run() {
        // 
    	sendExpiredMsg();
    }
    
    /**
     * 检查优惠券使用是否过期
     */
    private void sendExpiredMsg(){
    	String methodName = "sendExpiredMsg";
    	LogUtil.startLog(THIS_CLASS, methodName, "会员过期检查发送push消息开始");
    	
    	couponExpiredPushService.sendExpiredMsgAct();
    	
    	LogUtil.endLog(THIS_CLASS, methodName, "会员过期检查发送push消息结束");
    }

    
}

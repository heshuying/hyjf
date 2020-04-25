package com.hyjf.batch.smsnotice.redpacket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 红包账户余额短信提醒
 * @author zhangjp
 * @version hyjf 1.0
 * @since hyjf 1.0 
 * @see 上午11:42:54
 */
class RedPacketSmsNoticeTask {
    Logger _log = LoggerFactory.getLogger(RedPacketSmsNoticeTask.class);
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

    @Autowired
    RedPacketSmsNoticeService redPacketSmsNoticeService;

    public void run() {
    	if (isOver) {
			try {
				isOver = false;
				//
				sendSmsNotice();
			} finally {
				isOver = true;
			}
		}
    }
    
    /**
     * 查询余额并通知
     */
    private void sendSmsNotice(){
    	_log.info("【红包账户余额短信提醒】开始。。。");
    	redPacketSmsNoticeService.queryAndSend();
    	_log.info("【红包账户余额短信提醒】结束。。。");
    }

    
}

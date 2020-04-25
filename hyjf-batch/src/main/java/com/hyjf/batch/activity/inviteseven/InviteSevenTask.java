package com.hyjf.batch.activity.inviteseven;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 生成中奖用户
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月27日
 * @see 上午9:54:02
 */
public class InviteSevenTask {
    
    Logger _log = LoggerFactory.getLogger(InviteSevenServiceImpl.class);

    @Autowired
    InviteSevenService inviteSevenService;
    
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

    public void run() {
        // 生成中奖用户
        if (isOver) {
            try{
                isOver = false;
                inviteStatistic();
            }finally {
                isOver = true;
            }
        }
    }
    
    /**
     * 活动统计
     */
    private void inviteStatistic(){
        _log.info("-------------------七月份活动统计开始------------------");
    	
    	try {
    	    inviteSevenService.updateInviteStatistic();
        } catch (Exception e) {
           _log.error("七月份活动统计执行失败", e);
        }
    	
    	_log.info("-------------------七月份活动统计结束-----------------");
    }

    
}

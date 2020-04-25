/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.activity.twoeleven2018;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yinhui
 * @version Twoeleven2018TokenTask, v0.1 2018/10/15 17:30
 */
public class Twoeleven2018TokenTask {

    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private Twoeleven2018Service service;

    public void run() {
        if (isOver) {
            try{
                isOver = false;
                logger.info("--------------双十一秒杀活动生成防刷token定时任务执行开始");
                service.setRedisActivityToken();
                logger.info("--------------双十一秒杀活动生成防刷token定时任务执行结束");
            }finally {
                isOver = true;
            }
        }
    }
}

/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.activity.activity2018qixi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 七夕活动定时发券
 * @author fq
 * @version Activity2018QixiTask, v0.1 2018/7/24 11:00
 */
public class Activity2018QixiTask {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private Activity2018QixiService service;

    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

    public void run() {
        if (isOver) {
            try{
                isOver = false;
                logger.info("--------------七夕活动发放优惠券定时任务执行开始");
                service.updateSendCoupon();
                logger.info("--------------七夕活动发放优惠券定时任务执行结束");
            }finally {
                isOver = true;
            }
        }
    }
}

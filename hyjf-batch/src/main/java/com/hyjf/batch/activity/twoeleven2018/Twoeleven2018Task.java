package com.hyjf.batch.activity.twoeleven2018;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 双十一秒杀活动定时任务
 * 产生实物奖励
 * @author xiehuili on 2018/10/12.
 */
public class Twoeleven2018Task {


    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private Twoeleven2018Service service;

    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

    public void run() {
        if (isOver) {
            try{
                isOver = false;
                logger.info("--------------双十一秒杀活动获取实物奖励定时任务执行开始");
                service.insertInvestReward();
                logger.info("--------------双十一秒杀活动获取实物奖励定时任务执行结束");
            }finally {
                isOver = true;
            }
        }
    }
}

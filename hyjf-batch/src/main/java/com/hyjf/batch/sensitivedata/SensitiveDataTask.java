package com.hyjf.batch.sensitivedata;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 反欺诈预警名单task
 * 每天5:00
 * 查询当天注册，开户，充值提现，无债权的用户生成Excel发送到相应的邮箱
 */
public class SensitiveDataTask {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveDataTask.class);
    @Autowired
    private SensitiveDataService sensitiveDataService;

    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

    public void run() {
        logger.info("---------反欺诈预警名单定时任务-----------isOver is :{}", isOver);
        if (isOver) {
            isOver = false;
            try {
                //获取当前时间
                String date = DateFormatUtils.format(DateUtils.addDays(new Date(), -1), "yyyy-MM-dd");
                sensitiveDataService.execTask(date);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(SensitiveDataTask.class + ":" + e.getMessage());
            }
            isOver = true;
        }
        logger.info("---------反欺诈预警名单定时任务执行完毕-----------");
    }

}

package com.hyjf.batch.activity.actten2017;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TenderRewardCouponSendTask {
    Logger _log = LoggerFactory.getLogger(TenderRewardCouponSendTask.class);
    
    @Autowired
    TenderRewardCouponSendService tenderRewardCouponSendService;
    
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

    public void run() {
        if (isOver) {
            try{
                isOver = false;
                _log.info("--------------十月份活动发放优惠券定时任务执行开始");
                tenderRewardCouponSendService.updateSendCoupon();
                _log.info("--------------十月份活动发放优惠券定时任务执行结束");
            }finally {
                isOver = true;
            }
        }
    }


}

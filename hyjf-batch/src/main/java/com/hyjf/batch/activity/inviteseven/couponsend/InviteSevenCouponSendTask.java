package com.hyjf.batch.activity.inviteseven.couponsend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.batch.activity.inviteseven.InviteSevenServiceImpl;

public class InviteSevenCouponSendTask {
    Logger _log = LoggerFactory.getLogger(InviteSevenServiceImpl.class);
    
    @Autowired
    InviteSevenCouponSendService inviteCouponSendService;
    
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

    public void run() {
        if (isOver) {
            try{
                isOver = false;
                _log.info("--------------七月份活动发放优惠券定时任务执行开始");
                inviteCouponSendService.updateSendCoupon();
                _log.info("--------------七月份活动发放优惠券定时任务执行结束");
            }finally {
                isOver = true;
            }
        }
    }


}

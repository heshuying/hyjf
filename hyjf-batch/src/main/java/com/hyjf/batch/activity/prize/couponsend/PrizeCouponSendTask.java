package com.hyjf.batch.activity.prize.couponsend;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;

public class PrizeCouponSendTask {
    @Autowired
    PrizeCouponSendService prizeCouponSendService;
    
    // 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
    private static Boolean isOver = true;

    public void run() {
        if (isOver) {
            try{
                isOver = false;
                LogUtil.startLog(this.getClass().getName(), "run", "发送代金券定时任务执行开始");
                prizeCouponSendService.updateSendCoupon();
                LogUtil.endLog(this.getClass().getName(), "run", "发送代金券定时任务执行结束");
            }finally {
                isOver = true;
            }
        }
    }


}

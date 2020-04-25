package com.hyjf.batch.activity.billion.third;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;

/**
 * 十一月份活动  满心满亿
 * @author Michael
 */
public class BillionThirdTask {
    /** 类名 */
    private static final String THIS_CLASS = BillionThirdTask.class.getName();
    @Autowired
    BillionThirdService billionThirdService;

   
    
    /**
     * 十月份活动整点开抢启动
     */
    public void startHourlyActivity(){
        try {
            String methodName = "startHourlyActivity";
            LogUtil.startLog(THIS_CLASS, methodName, "十月份活动整点开抢启动开始");
            billionThirdService.startHourlyActivity();
            LogUtil.startLog(THIS_CLASS, methodName, "十月份活动整点开抢启动结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    
    /**
     * 十月份活动重置标识
     */
    public void activityReset(){
        try {
            String methodName = "activityReset";
            LogUtil.startLog(THIS_CLASS, methodName, "十月份活动重置标识开始");
            billionThirdService.activityReset();
            LogUtil.startLog(THIS_CLASS, methodName, "十月份活动重置标识结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}

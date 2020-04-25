/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.model.customize.activity;

/**
 * 双十一活动辅助数据
 * @author yinhui
 * @version ActivityOtherDataVO, v0.1 2018/10/12 11:17
 */
public class ActivityOtherDataVO {

    //当天剩余可用秒杀次数
    private int restTimes = 2;

    //服务器当前时间 毫秒级时间戳 用于倒计时计算
    private Long now;

    // 防刷接口验证
    private String activityToken;

    public int getRestTimes() {
        return restTimes;
    }

    public void setRestTimes(int restTimes) {
        this.restTimes = restTimes;
    }

    public Long getNow() {
        return now;
    }

    public void setNow(Long now) {
        this.now = now;
    }

    public String getActivityToken() {
        return activityToken;
    }

    public void setActivityToken(String activityToken) {
        this.activityToken = activityToken;
    }
}

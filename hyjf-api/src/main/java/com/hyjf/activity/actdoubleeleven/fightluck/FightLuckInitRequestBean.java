package com.hyjf.activity.actdoubleeleven.fightluck;

import com.hyjf.base.bean.BaseResultBean;

public class FightLuckInitRequestBean extends BaseResultBean {
    
    
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -2500218256349451771L;
    //活动开始时间
    private Integer fightLuckTime;
    //系统当前时间
    private Integer nowTime;
    //活动结束时间
    private Integer fightLuckEndTime;
    //剩余张数
    private Integer surplusCount;
    //用户是否已经抢过  0未抢过1已抢过
    private String  ifGrab;
    public Integer getFightLuckTime() {
        return fightLuckTime;
    }
    public void setFightLuckTime(Integer fightLuckTime) {
        this.fightLuckTime = fightLuckTime;
    }
    public Integer getNowTime() {
        return nowTime;
    }
    public void setNowTime(Integer nowTime) {
        this.nowTime = nowTime;
    }
    public Integer getSurplusCount() {
        return surplusCount;
    }
    public void setSurplusCount(Integer surplusCount) {
        this.surplusCount = surplusCount;
    }
    public String getIfGrab() {
        return ifGrab;
    }
    public void setIfGrab(String ifGrab) {
        this.ifGrab = ifGrab;
    }
    public Integer getFightLuckEndTime() {
        return fightLuckEndTime;
    }
    public void setFightLuckEndTime(Integer fightLuckEndTime) {
        this.fightLuckEndTime = fightLuckEndTime;
    }
   
}

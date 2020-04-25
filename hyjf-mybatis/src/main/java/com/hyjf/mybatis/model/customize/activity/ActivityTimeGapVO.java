/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.model.customize.activity;

import java.util.List;

/**
 * 活动时段
 * @author yinhui
 * @version ActivityTimeGapVO, v0.1 2018/10/11 16:19
 */
public class ActivityTimeGapVO {

    // 时段秒杀开始时间 毫秒级时间戳
    private Long startTime;

    // 时段秒杀结束时间 毫秒级时间戳
    private Long endTime;

    // 时段秒杀状态 0未开始 1进行中 2已结束 3倒计时 4已抢完且已结束
    private String status;

    // 奖品
    private List<ActivityAwardsVO> awards;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ActivityAwardsVO> getListAwards() {
        return awards;
    }

    public void setListAwards(List<ActivityAwardsVO> awards) {
        this.awards = awards;
    }
}

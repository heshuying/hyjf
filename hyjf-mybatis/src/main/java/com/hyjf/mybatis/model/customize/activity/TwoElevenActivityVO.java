/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.model.customize.activity;

import java.util.List;

/**
 * 双十一活动
 * @author yinhui
 * @version TwoElevenActivityVO, v0.1 2018/10/12 9:59
 */
public class TwoElevenActivityVO {

    // 活动状态 0未开始 1进行中 2已结束
    private String status;

    //活动名称
    private String name;

    //活动开始日期 毫秒级时间戳
    private Long startDate;

    // 时段数据
    private List<ActivityTimeGapVO> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public List<ActivityTimeGapVO> getData() {
        return data;
    }

    public void setData(List<ActivityTimeGapVO> data) {
        this.data = data;
    }
}

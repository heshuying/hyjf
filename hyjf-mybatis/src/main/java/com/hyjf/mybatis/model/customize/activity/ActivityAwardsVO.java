/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.model.customize.activity;

/**
 * 活动奖品
 * @author yinhui
 * @version ActivityAwardsVO, v0.1 2018/10/11 16:00
 */
public class ActivityAwardsVO {

    //奖品ID = 优惠券编号
    private String id;

    //奖品名称 = 优惠券名称
    private String name;

    /** 奖品秒杀进度 不需要带百分号*/
    private int progress;

    // 剩余奖品数量 根据数量判断是否抢完
    private int rest;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }
}

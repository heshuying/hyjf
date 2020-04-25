package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class InviteRecommend implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer prizeAllCount;

    private Integer prizeUsedCount;

    private Integer blackUser;

    private Integer addTime;

    private Integer updateTime;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPrizeAllCount() {
        return prizeAllCount;
    }

    public void setPrizeAllCount(Integer prizeAllCount) {
        this.prizeAllCount = prizeAllCount;
    }

    public Integer getPrizeUsedCount() {
        return prizeUsedCount;
    }

    public void setPrizeUsedCount(Integer prizeUsedCount) {
        this.prizeUsedCount = prizeUsedCount;
    }

    public Integer getBlackUser() {
        return blackUser;
    }

    public void setBlackUser(Integer blackUser) {
        this.blackUser = blackUser;
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}
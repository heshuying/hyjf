package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class InviteRecommendPrize implements Serializable {
    private Integer id;

    private Integer userId;

    private String prizeGroup;

    private Integer prizeCount;

    private Integer usedRecommendCount;

    private Integer prizeType;

    private Integer prizeKind;

    private Integer prizeSendFlag;

    private String remark;

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

    public String getPrizeGroup() {
        return prizeGroup;
    }

    public void setPrizeGroup(String prizeGroup) {
        this.prizeGroup = prizeGroup == null ? null : prizeGroup.trim();
    }

    public Integer getPrizeCount() {
        return prizeCount;
    }

    public void setPrizeCount(Integer prizeCount) {
        this.prizeCount = prizeCount;
    }

    public Integer getUsedRecommendCount() {
        return usedRecommendCount;
    }

    public void setUsedRecommendCount(Integer usedRecommendCount) {
        this.usedRecommendCount = usedRecommendCount;
    }

    public Integer getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Integer prizeType) {
        this.prizeType = prizeType;
    }

    public Integer getPrizeKind() {
        return prizeKind;
    }

    public void setPrizeKind(Integer prizeKind) {
        this.prizeKind = prizeKind;
    }

    public Integer getPrizeSendFlag() {
        return prizeSendFlag;
    }

    public void setPrizeSendFlag(Integer prizeSendFlag) {
        this.prizeSendFlag = prizeSendFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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
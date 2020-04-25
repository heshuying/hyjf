package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class InviteInfo implements Serializable {
    private Integer id;

    private Integer inviteUser;

    private Integer inviteByUser;

    private String groupCode;

    private Integer recommendSource;

    private Integer recommendCount;

    private Integer sendFlag;

    private Integer tenderStatus;

    private Integer sendTime;

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

    public Integer getInviteUser() {
        return inviteUser;
    }

    public void setInviteUser(Integer inviteUser) {
        this.inviteUser = inviteUser;
    }

    public Integer getInviteByUser() {
        return inviteByUser;
    }

    public void setInviteByUser(Integer inviteByUser) {
        this.inviteByUser = inviteByUser;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode == null ? null : groupCode.trim();
    }

    public Integer getRecommendSource() {
        return recommendSource;
    }

    public void setRecommendSource(Integer recommendSource) {
        this.recommendSource = recommendSource;
    }

    public Integer getRecommendCount() {
        return recommendCount;
    }

    public void setRecommendCount(Integer recommendCount) {
        this.recommendCount = recommendCount;
    }

    public Integer getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(Integer sendFlag) {
        this.sendFlag = sendFlag;
    }

    public Integer getTenderStatus() {
        return tenderStatus;
    }

    public void setTenderStatus(Integer tenderStatus) {
        this.tenderStatus = tenderStatus;
    }

    public Integer getSendTime() {
        return sendTime;
    }

    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
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
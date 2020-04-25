package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class NewyearGetCard implements Serializable {
    private Integer id;

    private String tenderNid;

    private Integer userId;

    private Integer inviteUserId;

    private Integer getCardType;

    private Integer getFlg;

    private Integer addTime;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenderNid() {
        return tenderNid;
    }

    public void setTenderNid(String tenderNid) {
        this.tenderNid = tenderNid == null ? null : tenderNid.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(Integer inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public Integer getGetCardType() {
        return getCardType;
    }

    public void setGetCardType(Integer getCardType) {
        this.getCardType = getCardType;
    }

    public Integer getGetFlg() {
        return getFlg;
    }

    public void setGetFlg(Integer getFlg) {
        this.getFlg = getFlg;
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}
package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ActJanBargain implements Serializable {
    private Integer id;

    private Integer prizeId;

    private String prizeName;

    private String wechatName;

    private String wechatNickname;

    private String wechatNameHelp;

    private String wechatNicknameHelp;

    private BigDecimal moneyBargain;

    private String mobile;

    private String clientIp;

    private Integer createTime;

    private Integer updateTime;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName == null ? null : prizeName.trim();
    }

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName == null ? null : wechatName.trim();
    }

    public String getWechatNickname() {
        return wechatNickname;
    }

    public void setWechatNickname(String wechatNickname) {
        this.wechatNickname = wechatNickname == null ? null : wechatNickname.trim();
    }

    public String getWechatNameHelp() {
        return wechatNameHelp;
    }

    public void setWechatNameHelp(String wechatNameHelp) {
        this.wechatNameHelp = wechatNameHelp == null ? null : wechatNameHelp.trim();
    }

    public String getWechatNicknameHelp() {
        return wechatNicknameHelp;
    }

    public void setWechatNicknameHelp(String wechatNicknameHelp) {
        this.wechatNicknameHelp = wechatNicknameHelp == null ? null : wechatNicknameHelp.trim();
    }

    public BigDecimal getMoneyBargain() {
        return moneyBargain;
    }

    public void setMoneyBargain(BigDecimal moneyBargain) {
        this.moneyBargain = moneyBargain;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
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
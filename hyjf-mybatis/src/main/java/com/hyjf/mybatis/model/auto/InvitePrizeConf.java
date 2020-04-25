package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class InvitePrizeConf implements Serializable {
    private Integer id;

    private String prizeName;

    private Integer prizeQuantity;

    private Integer recommendQuantity;

    private Integer prizeReminderQuantity;

    private String prizeGroupCode;

    private Integer prizeType;

    private String couponCode;

    private BigDecimal prizeProbability;

    private String prizePicUrl;

    private Integer prizeKind;

    private Integer prizeSort;

    private Integer prizeStatus;

    private String successMessage;

    private Integer prizeApplyTime;

    private String remark;

    private Integer addTime;

    private Integer addUser;

    private Integer updateTime;

    private Integer updateUser;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName == null ? null : prizeName.trim();
    }

    public Integer getPrizeQuantity() {
        return prizeQuantity;
    }

    public void setPrizeQuantity(Integer prizeQuantity) {
        this.prizeQuantity = prizeQuantity;
    }

    public Integer getRecommendQuantity() {
        return recommendQuantity;
    }

    public void setRecommendQuantity(Integer recommendQuantity) {
        this.recommendQuantity = recommendQuantity;
    }

    public Integer getPrizeReminderQuantity() {
        return prizeReminderQuantity;
    }

    public void setPrizeReminderQuantity(Integer prizeReminderQuantity) {
        this.prizeReminderQuantity = prizeReminderQuantity;
    }

    public String getPrizeGroupCode() {
        return prizeGroupCode;
    }

    public void setPrizeGroupCode(String prizeGroupCode) {
        this.prizeGroupCode = prizeGroupCode == null ? null : prizeGroupCode.trim();
    }

    public Integer getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Integer prizeType) {
        this.prizeType = prizeType;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode == null ? null : couponCode.trim();
    }

    public BigDecimal getPrizeProbability() {
        return prizeProbability;
    }

    public void setPrizeProbability(BigDecimal prizeProbability) {
        this.prizeProbability = prizeProbability;
    }

    public String getPrizePicUrl() {
        return prizePicUrl;
    }

    public void setPrizePicUrl(String prizePicUrl) {
        this.prizePicUrl = prizePicUrl == null ? null : prizePicUrl.trim();
    }

    public Integer getPrizeKind() {
        return prizeKind;
    }

    public void setPrizeKind(Integer prizeKind) {
        this.prizeKind = prizeKind;
    }

    public Integer getPrizeSort() {
        return prizeSort;
    }

    public void setPrizeSort(Integer prizeSort) {
        this.prizeSort = prizeSort;
    }

    public Integer getPrizeStatus() {
        return prizeStatus;
    }

    public void setPrizeStatus(Integer prizeStatus) {
        this.prizeStatus = prizeStatus;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage == null ? null : successMessage.trim();
    }

    public Integer getPrizeApplyTime() {
        return prizeApplyTime;
    }

    public void setPrizeApplyTime(Integer prizeApplyTime) {
        this.prizeApplyTime = prizeApplyTime;
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

    public Integer getAddUser() {
        return addUser;
    }

    public void setAddUser(Integer addUser) {
        this.addUser = addUser;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}
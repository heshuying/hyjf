package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class NewyearCaisheCardUser implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer cardType;

    private Integer cardJinQuantity;

    private Integer cardJiQuantity;

    private Integer cardNaQuantity;

    private Integer cardFuQuantity;

    private Integer operateType;

    private Integer cardSource;

    private String remark;

    private Integer addTime;

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

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public Integer getCardJinQuantity() {
        return cardJinQuantity;
    }

    public void setCardJinQuantity(Integer cardJinQuantity) {
        this.cardJinQuantity = cardJinQuantity;
    }

    public Integer getCardJiQuantity() {
        return cardJiQuantity;
    }

    public void setCardJiQuantity(Integer cardJiQuantity) {
        this.cardJiQuantity = cardJiQuantity;
    }

    public Integer getCardNaQuantity() {
        return cardNaQuantity;
    }

    public void setCardNaQuantity(Integer cardNaQuantity) {
        this.cardNaQuantity = cardNaQuantity;
    }

    public Integer getCardFuQuantity() {
        return cardFuQuantity;
    }

    public void setCardFuQuantity(Integer cardFuQuantity) {
        this.cardFuQuantity = cardFuQuantity;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public Integer getCardSource() {
        return cardSource;
    }

    public void setCardSource(Integer cardSource) {
        this.cardSource = cardSource;
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

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}
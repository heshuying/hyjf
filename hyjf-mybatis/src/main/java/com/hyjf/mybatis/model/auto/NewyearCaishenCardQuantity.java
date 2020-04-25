package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class NewyearCaishenCardQuantity implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer cardJinQuantity;

    private Integer cardJiQuantity;

    private Integer cardNaQuantity;

    private Integer cardFuQuantity;

    private Integer addTime;

    private Integer updateTime;

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
}
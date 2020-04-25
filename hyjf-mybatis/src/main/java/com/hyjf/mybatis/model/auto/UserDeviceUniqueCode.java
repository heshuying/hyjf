package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class UserDeviceUniqueCode implements Serializable {
    private Integer id;

    private String uniqueIdentifier;

    private Integer userId;

    private Integer requestTimesDay;

    private String currentDay;

    private Integer addTime;

    private Integer updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier == null ? null : uniqueIdentifier.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRequestTimesDay() {
        return requestTimesDay;
    }

    public void setRequestTimesDay(Integer requestTimesDay) {
        this.requestTimesDay = requestTimesDay;
    }

    public String getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay == null ? null : currentDay.trim();
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
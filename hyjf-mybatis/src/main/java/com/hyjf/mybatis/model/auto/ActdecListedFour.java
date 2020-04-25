package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class ActdecListedFour implements Serializable {
    private Integer id;

    private Integer userId;

    private String userMobile;

    private String userName;

    private String userTureName;

    private Integer coverUserId;

    private String coverUserMobile;

    private String coverUserName;

    private String coverUserTureName;

    private Integer cumulative;

    private Integer whether;

    private Integer registrationTime;

    private Integer openTime;

    private String createUser;

    private Integer createTime;

    private String updateUser;

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

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile == null ? null : userMobile.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserTureName() {
        return userTureName;
    }

    public void setUserTureName(String userTureName) {
        this.userTureName = userTureName == null ? null : userTureName.trim();
    }

    public Integer getCoverUserId() {
        return coverUserId;
    }

    public void setCoverUserId(Integer coverUserId) {
        this.coverUserId = coverUserId;
    }

    public String getCoverUserMobile() {
        return coverUserMobile;
    }

    public void setCoverUserMobile(String coverUserMobile) {
        this.coverUserMobile = coverUserMobile == null ? null : coverUserMobile.trim();
    }

    public String getCoverUserName() {
        return coverUserName;
    }

    public void setCoverUserName(String coverUserName) {
        this.coverUserName = coverUserName == null ? null : coverUserName.trim();
    }

    public String getCoverUserTureName() {
        return coverUserTureName;
    }

    public void setCoverUserTureName(String coverUserTureName) {
        this.coverUserTureName = coverUserTureName == null ? null : coverUserTureName.trim();
    }

    public Integer getCumulative() {
        return cumulative;
    }

    public void setCumulative(Integer cumulative) {
        this.cumulative = cumulative;
    }

    public Integer getWhether() {
        return whether;
    }

    public void setWhether(Integer whether) {
        this.whether = whether;
    }

    public Integer getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Integer registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Integer getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Integer openTime) {
        this.openTime = openTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
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
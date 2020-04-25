package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class ActSignin implements Serializable {
    private Integer id;

    private Integer userId;

    private String userName;

    private String truename;

    private String mobile;

    private Integer currentSignNum;

    private Integer signTime;

    private String remark;

    private Integer createTime;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename == null ? null : truename.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Integer getCurrentSignNum() {
        return currentSignNum;
    }

    public void setCurrentSignNum(Integer currentSignNum) {
        this.currentSignNum = currentSignNum;
    }

    public Integer getSignTime() {
        return signTime;
    }

    public void setSignTime(Integer signTime) {
        this.signTime = signTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}
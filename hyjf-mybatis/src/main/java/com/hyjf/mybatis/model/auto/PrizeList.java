package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class PrizeList implements Serializable {
    private Integer id;

    private String prizeSelfCode;

    private String prizeName;

    private Integer allPersonCount;

    private Integer joinedPersonCount;

    private Integer prizeStatus;

    private Integer prizeQuantity;

    private String prizeCode;

    private Integer addTime;

    private String addUser;

    private Integer updateTime;

    private String updateUser;

    private Integer delFlg;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrizeSelfCode() {
        return prizeSelfCode;
    }

    public void setPrizeSelfCode(String prizeSelfCode) {
        this.prizeSelfCode = prizeSelfCode == null ? null : prizeSelfCode.trim();
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName == null ? null : prizeName.trim();
    }

    public Integer getAllPersonCount() {
        return allPersonCount;
    }

    public void setAllPersonCount(Integer allPersonCount) {
        this.allPersonCount = allPersonCount;
    }

    public Integer getJoinedPersonCount() {
        return joinedPersonCount;
    }

    public void setJoinedPersonCount(Integer joinedPersonCount) {
        this.joinedPersonCount = joinedPersonCount;
    }

    public Integer getPrizeStatus() {
        return prizeStatus;
    }

    public void setPrizeStatus(Integer prizeStatus) {
        this.prizeStatus = prizeStatus;
    }

    public Integer getPrizeQuantity() {
        return prizeQuantity;
    }

    public void setPrizeQuantity(Integer prizeQuantity) {
        this.prizeQuantity = prizeQuantity;
    }

    public String getPrizeCode() {
        return prizeCode;
    }

    public void setPrizeCode(String prizeCode) {
        this.prizeCode = prizeCode == null ? null : prizeCode.trim();
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser == null ? null : addUser.trim();
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}
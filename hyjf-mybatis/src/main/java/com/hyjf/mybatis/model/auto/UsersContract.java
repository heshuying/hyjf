package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class UsersContract implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer relation;

    private String rlName;

    private String rlPhone;

    private Integer addtime;

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

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public String getRlName() {
        return rlName;
    }

    public void setRlName(String rlName) {
        this.rlName = rlName == null ? null : rlName.trim();
    }

    public String getRlPhone() {
        return rlPhone;
    }

    public void setRlPhone(String rlPhone) {
        this.rlPhone = rlPhone == null ? null : rlPhone.trim();
    }

    public Integer getAddtime() {
        return addtime;
    }

    public void setAddtime(Integer addtime) {
        this.addtime = addtime;
    }
}
package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class BindUsers implements Serializable {
    private Integer id;

    private Integer userId;

    private String bindUniqueId;

    private Integer bindPlatformId;

    private Integer createUserId;

    private Integer createTime;

    private Integer updateUserId;

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

    public String getBindUniqueId() {
        return bindUniqueId;
    }

    public void setBindUniqueId(String bindUniqueId) {
        this.bindUniqueId = bindUniqueId;
    }

    public Integer getBindPlatformId() {
        return bindPlatformId;
    }

    public void setBindPlatformId(Integer bindPlatformId) {
        this.bindPlatformId = bindPlatformId;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
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
package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.util.Date;

public class CertUserTransact implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer isTransact;

    private Date isTransactTime;

    private Date createTime;

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

    public Integer getIsTransact() {
        return isTransact;
    }

    public void setIsTransact(Integer isTransact) {
        this.isTransact = isTransact;
    }

    public Date getIsTransactTime() {
        return isTransactTime;
    }

    public void setIsTransactTime(Date isTransactTime) {
        this.isTransactTime = isTransactTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
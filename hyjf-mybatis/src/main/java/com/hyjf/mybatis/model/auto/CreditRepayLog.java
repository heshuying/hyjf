package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class CreditRepayLog implements Serializable {
    private Integer id;

    private Integer creditNid;

    private Integer createTime;

    private String errorCode;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreditNid() {
        return creditNid;
    }

    public void setCreditNid(Integer creditNid) {
        this.creditNid = creditNid;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode == null ? null : errorCode.trim();
    }
}
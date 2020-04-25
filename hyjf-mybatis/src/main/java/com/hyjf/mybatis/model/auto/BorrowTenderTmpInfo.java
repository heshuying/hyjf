package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class BorrowTenderTmpInfo implements Serializable {
    private Integer id;

    private String ordid;

    private String tmpArray;

    private String addtime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrdid() {
        return ordid;
    }

    public void setOrdid(String ordid) {
        this.ordid = ordid == null ? null : ordid.trim();
    }

    public String getTmpArray() {
        return tmpArray;
    }

    public void setTmpArray(String tmpArray) {
        this.tmpArray = tmpArray == null ? null : tmpArray.trim();
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? null : addtime.trim();
    }
}
package com.hyjf.api.server.borrowDetail;

import java.io.Serializable;

public class BorrowDetailBean implements Serializable {

    // 序列化ID
    private static final long serialVersionUID = 5794716712997390230L;

    /**
     * id唯一标识
     */
    private String id;
    /**
     * 页面key(中文展示)
     */
    private String key;
    /**
     * 结果值
     */
    private String val;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getVal() {
        return val;
    }
    public void setVal(String val) {
        this.val = val;
    }
}

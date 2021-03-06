package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class SmsLogWithBLOBs extends SmsLog implements Serializable {
    private String mobile;

    private String content;

    private static final long serialVersionUID = 1L;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}
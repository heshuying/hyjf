package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class SmsMailTemplateKey implements Serializable {
    private Integer id;

    private String mailValue;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMailValue() {
        return mailValue;
    }

    public void setMailValue(String mailValue) {
        this.mailValue = mailValue == null ? null : mailValue.trim();
    }
}
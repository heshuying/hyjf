package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class SmsTemplateKey implements Serializable {
    private Integer id;

    private String tplCode;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTplCode() {
        return tplCode;
    }

    public void setTplCode(String tplCode) {
        this.tplCode = tplCode == null ? null : tplCode.trim();
    }
}
/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.server;

import java.io.Serializable;

import com.hyjf.common.util.CustomConstants;

/**
 * app接口返回数据的几类
 * @author 朱晓东
 * @version yxpt 1.0
 * @since yxpt 1.0 2016年9月9日
 * @see 下午2:23:01
 */
public class BaseResultBean implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -3589570872364671096L;
    
    public BaseResultBean() {
        super();
        this.status = CustomConstants.APP_STATUS_FAIL;
    }

    public BaseResultBean(String request) {
        super();
        this.status = CustomConstants.APP_STATUS_FAIL;
        this.request = request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    private String status;

    private String statusDesc;

    private String request;

}

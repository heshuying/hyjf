/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.app;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * ajax数据返回基类
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月11日
 * @see 下午2:45:31
 */
public class BaseAjaxResultBean implements Serializable {

	/**
	 * 此处为属性说明
	 */
	private static final long serialVersionUID = -4492942282166034094L;

	// 请求处理是否成功
	private boolean status = false;

	// 分页信息
	private Paginator paginator;

	// web服务地址
	private String host;

	// 返回信息
	private String message;

	// 错误码
	private String errorCode;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void success() {
		this.status = true;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}

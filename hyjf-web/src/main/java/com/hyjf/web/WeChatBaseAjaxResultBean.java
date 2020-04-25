/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.web;

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
public class WeChatBaseAjaxResultBean implements Serializable {

	/**
	 * 此处为属性说明
	 */
	private static final long serialVersionUID = -4492942282166034094L;

	// 请求处理是否成功 0成功 1失败
	private int error = 0;
	// 返回信息
	private String errorDesc;
	// 分页信息
	private Paginator paginator;

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

}

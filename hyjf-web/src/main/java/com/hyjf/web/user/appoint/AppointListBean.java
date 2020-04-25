package com.hyjf.web.user.appoint;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

public class AppointListBean implements Serializable {

	/** 序列化id */
	private static final long serialVersionUID = 3458197418404401541L;

	/** 预约状态 */
	private String appointStatus;

	/** 登陆用户id */
	private String userId;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 翻页功能所用分页大小
	 */
	private int pageSize = 10;
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	public int getPaginatorPage() {
		if (paginatorPage == 0) {
			paginatorPage = 1;
		}
		return paginatorPage;
	}

	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public int getPageSize() {
		if (pageSize == 0) {
			pageSize = 10;
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getAppointStatus() {
		return appointStatus;
	}

	public void setAppointStatus(String appointStatus) {
		this.appointStatus = appointStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
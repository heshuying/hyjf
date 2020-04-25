package com.hyjf.admin.manager.user.appoint;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;

/**
 * @package com.hyjf.admin.maintenance.Config;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class AppointBean implements Serializable {

	private static final long serialVersionUID = 3803722754627032592L;

	/*
	 * 用户Id
	 */
	private String userId;

	/*
	 * 用户名
	 */
	private String username;
	/*
	 * 电话
	 */
	private String mobile;
	/*
	 * 操作时间
	 */
	private String addTime;

	/*
	 * 违约编号
	 */
	private String recodNid;

	/*
	 * 操作结束时间
	 */
	private String addTimeEnd;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

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

	public String getMobile() {
		return mobile;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRecodNid() {
		return recodNid;
	}

	public void setRecodNid(String recodNid) {
		this.recodNid = recodNid;
	}

	public String getAddTimeEnd() {
		return addTimeEnd;
	}

	public void setAddTimeEnd(String addTimeEnd) {
		this.addTimeEnd = addTimeEnd;
	}

}

package com.hyjf.web.user.mytender;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.web.WebUserProjectListCustomize;

public class ProjectListBean extends WebUserProjectListCustomize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3278149257478770256L;

	// 用户id
	public String userId;
	// 出借开始值
	public String startDate;
	// 出借结束值
	public String endDate;
    // 项目状态
    public String projectStatus;
    
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

}

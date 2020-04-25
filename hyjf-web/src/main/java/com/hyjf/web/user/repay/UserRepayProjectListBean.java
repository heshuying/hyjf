package com.hyjf.web.user.repay;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.web.WebUserRepayProjectListCustomize;

public class UserRepayProjectListBean extends WebUserRepayProjectListCustomize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6507895388254615953L;

	// 用户id
	public String userId;
	//用户角色 2借款人，3垫付机构
	public String roleId;
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;
	private int pageSize = 8;
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	public UserRepayProjectListBean() {
		super();
	}

	public int getPaginatorPage() {
		if (paginatorPage == 0) {
			paginatorPage = 1;
		}
		return paginatorPage;
	}
	
	public int getPageSize() {
        if (pageSize == 0) {
            pageSize = 8;
        }
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}

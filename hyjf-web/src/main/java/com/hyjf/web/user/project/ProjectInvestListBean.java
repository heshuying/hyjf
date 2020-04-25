package com.hyjf.web.user.project;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.web.WebProjectInvestListCustomize;

public class ProjectInvestListBean extends WebProjectInvestListCustomize  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3278149257478770256L;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;
	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;
	
	public ProjectInvestListBean() {
		super();
	}
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

}

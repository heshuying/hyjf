package com.hyjf.web.aboutus;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.AdsWithBLOBs;

/**
 * AboutUsPageBean(Copy from HomepageBean)
 * 
 * @author Libin
 *
 */
public class AboutUsPageBean extends AdsWithBLOBs implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;
	
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;
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

	public int getPageSize() {
		if (pageSize == 0) {
			pageSize = 10;
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

}

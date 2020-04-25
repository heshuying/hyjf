package com.hyjf.web.stock;

import java.io.Serializable;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.StockInfo;

/**
 * HomePageBean
 * 
 * @author qingbing
 *
 */
public class StockInfoBean extends StockInfo implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803721754627032581L;
	/**
	 * type 查询类型 1、时分图；2、5天图、3、月图；4、年图
	 */
	private String type;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

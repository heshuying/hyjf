package com.hyjf.admin.manager.config.htlconfig;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.Product;

/**
 * 
 * 手续费
 */
public class HtlConfigBean implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<Product> recordList;

	private String ids;
	private String name;
	private String value;

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

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

	public List<Product> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<Product> recordList) {
		this.recordList = recordList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

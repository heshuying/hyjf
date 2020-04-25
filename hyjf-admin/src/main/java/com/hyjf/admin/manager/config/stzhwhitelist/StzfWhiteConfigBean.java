package com.hyjf.admin.manager.config.stzhwhitelist;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.STZHWhiteListCustomize;

/**
 *
 * @author kdl
 *
 */
public class StzfWhiteConfigBean extends STZHWhiteListCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<STZHWhiteListCustomize> recordList;

	private String ids;
	
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

	public List<STZHWhiteListCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<STZHWhiteListCustomize> recordList) {
		this.recordList = recordList;
	}

}

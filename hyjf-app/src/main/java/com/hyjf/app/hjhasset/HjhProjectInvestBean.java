/**
 * Description:项目出借用户列表查询所用vo
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.app.hjhasset;

import java.io.Serializable;

import com.hyjf.mybatis.model.customize.app.AppProjectInvestListCustomize;

public class HjhProjectInvestBean extends AppProjectInvestListCustomize implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 5013753434843143557L;
	//项目编号
	private String borrowNid;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int page = 1;

	/**
	 * 翻页功能所用分页大小
	 */
	private int pageSize = 10;

	public HjhProjectInvestBean() {
		super();
	}

	public int getPage() {
		if (page == 0) {
			page = 1;
		}
		return page;
	}

	public void setPage(int page) {
		this.page = page;
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

	public String getBorrowNid() {
		return borrowNid;
	}

	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}
	
}

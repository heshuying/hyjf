/**
 * 首页实体
 * @package com.hyjf.app.home
 * @author 王坤
 * @date 2016/02/05 10:30
 * @version V1.0  
 */
package com.hyjf.app.home;

import java.io.Serializable;

public class HomePageBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;

	// 翻页机能用的隐藏变量
	private int page = 1;
	// 分页大小 默认10
	private int pageSize = 10;

	public int getPage() {
		if (page == 0) {
			page = 1;
		}
		return page;
	}

	public void setPage(int page) {
		if (page == 0) {
			this.page = 1;
		}
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

}

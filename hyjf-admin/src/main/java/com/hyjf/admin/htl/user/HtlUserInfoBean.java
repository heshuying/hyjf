package com.hyjf.admin.htl.user;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.HtlUserInfoCustomize;

/**
  * @ClassName: HtlHtlUserInfoBean
  * @Description: TODO
  * @author Michael
  * @date 2015年11月24日 下午2:38:14
 */
public class HtlUserInfoBean extends HtlUserInfoCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<HtlUserInfoCustomize> recordList;

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

	public List<HtlUserInfoCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<HtlUserInfoCustomize> recordList) {
		this.recordList = recordList;
	}

}

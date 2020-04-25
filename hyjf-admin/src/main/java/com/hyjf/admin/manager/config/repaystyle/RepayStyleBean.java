package com.hyjf.admin.manager.config.repaystyle;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.BorrowStyleWithBLOBs;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class RepayStyleBean extends BorrowStyleWithBLOBs implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<BorrowStyleWithBLOBs> recordList;

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

	public List<BorrowStyleWithBLOBs> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<BorrowStyleWithBLOBs> recordList) {
		this.recordList = recordList;
	}

}

package com.hyjf.admin.manager.user.regist;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminRegistListCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class RegistListCustomizeBean extends AdminRegistListCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<AdminRegistListCustomize> recordList;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	// 注册时间 开始
	public String regTimeStart;
	// 注册时间 结束
	public String regTimeEnd;

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

	public List<AdminRegistListCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminRegistListCustomize> recordList) {
		this.recordList = recordList;
	}

	/**
	 * regTimeStart
	 * 
	 * @return the regTimeStart
	 */

	public String getRegTimeStart() {
		return regTimeStart;
	}

	/**
	 * @param regTimeStart
	 *            the regTimeStart to set
	 */

	public void setRegTimeStart(String regTimeStart) {
		this.regTimeStart = regTimeStart;
	}

	/**
	 * regTimeEnd
	 * 
	 * @return the regTimeEnd
	 */

	public String getRegTimeEnd() {
		return regTimeEnd;
	}

	/**
	 * @param regTimeEnd
	 *            the regTimeEnd to set
	 */

	public void setRegTimeEnd(String regTimeEnd) {
		this.regTimeEnd = regTimeEnd;
	}

}

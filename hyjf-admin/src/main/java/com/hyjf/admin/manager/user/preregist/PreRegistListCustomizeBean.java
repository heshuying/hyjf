package com.hyjf.admin.manager.user.preregist;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminPreRegistListCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class PreRegistListCustomizeBean extends AdminPreRegistListCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<AdminPreRegistListCustomize> recordList;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	// 注册时间 开始
	public String preRegTimeStart;
	// 注册时间 结束
	public String preRegTimeEnd;

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

	public List<AdminPreRegistListCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminPreRegistListCustomize> recordList) {
		this.recordList = recordList;
	}

    public String getPreRegTimeStart() {
        return preRegTimeStart;
    }

    public void setPreRegTimeStart(String preRegTimeStart) {
        this.preRegTimeStart = preRegTimeStart;
    }

    public String getPreRegTimeEnd() {
        return preRegTimeEnd;
    }

    public void setPreRegTimeEnd(String preRegTimeEnd) {
        this.preRegTimeEnd = preRegTimeEnd;
    }

}

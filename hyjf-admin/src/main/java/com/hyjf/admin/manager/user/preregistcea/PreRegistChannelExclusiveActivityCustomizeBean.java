package com.hyjf.admin.manager.user.preregistcea;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminPreRegistChannelExclusiveActivityCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions;
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class PreRegistChannelExclusiveActivityCustomizeBean extends AdminPreRegistChannelExclusiveActivityCustomize implements Serializable {

	/**
	 * serialVersionUID
	 */

	private static final long serialVersionUID = 387630498860089653L;

	private List<AdminPreRegistChannelExclusiveActivityCustomize> recordList;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	// 活动时间 开始
	public String preRegChannelExclusiveActivityTimeStart;
	// 活动时间 结束
	public String preRegChannelExclusiveActivityTimeEnd;

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

	public List<AdminPreRegistChannelExclusiveActivityCustomize> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<AdminPreRegistChannelExclusiveActivityCustomize> recordList) {
		this.recordList = recordList;
	}

    public String getPreRegChannelExclusiveActivityTimeStart() {
        return preRegChannelExclusiveActivityTimeStart;
    }

    public void setPreRegChannelExclusiveActivityTimeStart(String preRegChannelExclusiveActivityTimeStart) {
        this.preRegChannelExclusiveActivityTimeStart = preRegChannelExclusiveActivityTimeStart;
    }

    public String getPreRegChannelExclusiveActivityTimeEnd() {
        return preRegChannelExclusiveActivityTimeEnd;
    }

    public void setPreRegChannelExclusiveActivityTimeEnd(String preRegChannelExclusiveActivityTimeEnd) {
        this.preRegChannelExclusiveActivityTimeEnd = preRegChannelExclusiveActivityTimeEnd;
    }
}

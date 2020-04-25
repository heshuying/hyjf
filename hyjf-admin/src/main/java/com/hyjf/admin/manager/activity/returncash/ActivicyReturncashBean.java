package com.hyjf.admin.manager.activity.returncash;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.AdminActivityReturncashCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions;
 * @author GOGTZ-T
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
public class ActivicyReturncashBean extends AdminActivityReturncashCustomize implements Serializable {

    /**
     * serialVersionUID
     */

    private static final long serialVersionUID = 387630498860089653L;

    private String ids;
    
    private String rewardTotalStr;
    
    private String usernameSrch;
    
    private String attributeSrch;
    
    private List<AdminActivityReturncashCustomize> recordList;

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

    public List<AdminActivityReturncashCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<AdminActivityReturncashCustomize> recordList) {
        this.recordList = recordList;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

	public String getUsernameSrch() {
		return usernameSrch;
	}

	public void setUsernameSrch(String usernameSrch) {
		this.usernameSrch = usernameSrch;
	}

	public String getRewardTotalStr() {
		return rewardTotalStr;
	}

	public void setRewardTotalStr(String rewardTotalStr) {
		this.rewardTotalStr = rewardTotalStr;
	}

	public String getAttributeSrch() {
		return attributeSrch;
	}

	public void setAttributeSrch(String attributeSrch) {
		this.attributeSrch = attributeSrch;
	}
}

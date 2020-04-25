package com.hyjf.admin.manager.activity.activitylist;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ActivityList;

/**
 * 活动列表实体类
 * 
 * @author qingbing
 *
 */
public class ActivityListBean extends ActivityList implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3803722754627032581L;

    /**
     * 前台时间接收
     */
    private String startTime;

    private String endTime;

    private String startCreate;

    private String endCreate;

    private String status;
    
    private String ids;
    
    private String isMayActivity;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartCreate() {
        return startCreate;
    }

    public void setStartCreate(String startCreate) {
        this.startCreate = startCreate;
    }

    public String getEndCreate() {
        return endCreate;
    }

    public void setEndCreate(String endCreate) {
        this.endCreate = endCreate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    private List<ActivityList> recordList;

    private List<ActivityListBean> forBack;

    public List<ActivityListBean> getForBack() {
        return forBack;
    }

    public void setForBack(List<ActivityListBean> forBack) {
        this.forBack = forBack;
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

    public List<ActivityList> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<ActivityList> recordList) {
        this.recordList = recordList;
    }

	public String getIsMayActivity() {
		return isMayActivity;
	}

	public void setIsMayActivity(String isMayActivity) {
		this.isMayActivity = isMayActivity;
	}
    
    

}

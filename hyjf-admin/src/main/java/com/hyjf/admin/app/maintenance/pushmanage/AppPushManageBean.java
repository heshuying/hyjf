package com.hyjf.admin.app.maintenance.pushmanage;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.AppPushManage;

import java.io.Serializable;
import java.util.List;

public class AppPushManageBean extends AppPushManage implements Serializable {

    private String ids;
    private String timeStartDiy;
    private String timeEndDiy;
    private String statusSch;
    private int limitStart = -1;
    private int limitEnd = -1;
    private int paginatorPage = 1;
    private Paginator paginator;
    private List<AppPushManage> appPushManageList;
    private List<AppPushManage> recordList;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getTimeStartDiy() {
        return timeStartDiy;
    }

    public void setTimeStartDiy(String timeStartDiy) {
        this.timeStartDiy = timeStartDiy;
    }

    public String getTimeEndDiy() {
        return timeEndDiy;
    }

    public void setTimeEndDiy(String timeEndDiy) {
        this.timeEndDiy = timeEndDiy;
    }

    public String getStatusSch() {
        return statusSch;
    }

    public void setStatusSch(String statusSch) {
        this.statusSch = statusSch;
    }

    public List<AppPushManage> getAppPushManageList() {
        return appPushManageList;
    }

    public void setAppPushManageList(List<AppPushManage> appPushManageList) {
        this.appPushManageList = appPushManageList;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
    }

    public int getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(int limitEnd) {
        this.limitEnd = limitEnd;
    }

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

    public List<AppPushManage> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<AppPushManage> recordList) {
        this.recordList = recordList;
    }
}

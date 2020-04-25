package com.hyjf.admin.manager.activity.worldcupactivity;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.worldcup.GuessingActivitieCustomize;

import java.io.Serializable;
import java.util.List;

/**
 * @author  xiehuili on 2018/6/13.
 */
public class WorldCupActivityBean implements Serializable {


    /**
     * 前台参数接收
     */
    private String username;

    private String mobile;

    private String competitionTime;

    private List<GuessingActivitieCustomize> recordList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompetitionTime() {
        return competitionTime;
    }

    public void setCompetitionTime(String competitionTime) {
        this.competitionTime = competitionTime;
    }

    public List<GuessingActivitieCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<GuessingActivitieCustomize> recordList) {
        this.recordList = recordList;
    }

    public String getPageStatus() {
        return pageStatus;
    }

    public void setPageStatus(String pageStatus) {
        this.pageStatus = pageStatus;
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

    private String   pageStatus;
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



}

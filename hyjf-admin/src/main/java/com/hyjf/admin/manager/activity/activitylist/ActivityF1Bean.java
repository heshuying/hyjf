package com.hyjf.admin.manager.activity.activitylist;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.ActivityF1;

public class ActivityF1Bean extends ActivityF1 implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 7381666744856277587L;

    /** 一级部门 */
    private String first_level_department;

    /** 二级部门 */
    private String second_level_department;

    /** 三级部门 */
    private String third_level_department;

    /** 是否过期 */
    private String isover;

    /** 活动详情列表 */
    private List<ActivityF1> activityInfoRecordList;

    private List<ActivityF1Bean> forBack;

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    public String getFirst_level_department() {
        return first_level_department;
    }

    public void setFirst_level_department(String first_level_department) {
        this.first_level_department = first_level_department;
    }

    public String getSecond_level_department() {
        return second_level_department;
    }

    public void setSecond_level_department(String second_level_department) {
        this.second_level_department = second_level_department;
    }

    public String getThird_level_department() {
        return third_level_department;
    }

    public void setThird_level_department(String third_level_department) {
        this.third_level_department = third_level_department;
    }

    public List<ActivityF1> getActivityInfoRecordList() {
        return activityInfoRecordList;
    }

    public void setActivityInfoRecordList(List<ActivityF1> activityInfoRecordList) {
        this.activityInfoRecordList = activityInfoRecordList;
    }

    public List<ActivityF1Bean> getForBack() {
        return forBack;
    }

    public void setForBack(List<ActivityF1Bean> forBack) {
        this.forBack = forBack;
    }

    public int getPaginatorPage() {

        return paginatorPage == 0 ? 1 : paginatorPage;
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

    public String getIsover() {
        return isover;
    }

    public void setIsover(String isover) {
        this.isover = isover;
    }

}

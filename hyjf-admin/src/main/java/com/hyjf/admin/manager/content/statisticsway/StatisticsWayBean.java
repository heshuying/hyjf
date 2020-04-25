package com.hyjf.admin.manager.content.statisticsway;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.statisticsway.StatisticsWayConfigureCustomize;

import java.io.Serializable;
import java.util.List;

/**
 * 运营报告---统计方式配置
 * @author  by xiehuili on 2018/6/20.
 */
public class StatisticsWayBean  implements Serializable {
    //修改的id
    private String ids;
    //开始时间
    private String startTime;
    //截止时间
    private String endTime;
    //标题名称
    private String titleName;
    //唯一标识
    private String uniqueIdentifier;

    private List<StatisticsWayConfigureCustomize> recordList;

    private String   pageStatus;
    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
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

    
    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public List<StatisticsWayConfigureCustomize> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<StatisticsWayConfigureCustomize> recordList) {
        this.recordList = recordList;
    }

    public String getPageStatus() {
        return pageStatus;
    }

    public void setPageStatus(String pageStatus) {
        this.pageStatus = pageStatus;
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
}

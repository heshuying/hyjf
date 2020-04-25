/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.evaluationconfig.borrowlevelconfiglog;

import com.hyjf.admin.BaseBean;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.EvaluationConfigLog;

import java.util.List;

/**
 * 风险测评信用等级配置logBean
 *
 * @author liuyang
 * @version BorrowLevelConfigLogBean, v0.1 2018/11/29 10:30
 */
public class BorrowLevelConfigLogBean extends BaseBean {

    private List<EvaluationConfigLog> recordList;
    /**
     * 检索条件 limitStart
     */
    private int limitStart = -1;
    /**
     * 检索条件 limitEnd
     */
    private int limitEnd = -1;

    private String startTimeSrch;

    private String endTimeSrch;

    private String updateUserSrch;

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

    public List<EvaluationConfigLog> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<EvaluationConfigLog> recordList) {
        this.recordList = recordList;
    }

    public String getStartTimeSrch() {
        return startTimeSrch;
    }

    public void setStartTimeSrch(String startTimeSrch) {
        this.startTimeSrch = startTimeSrch;
    }

    public String getEndTimeSrch() {
        return endTimeSrch;
    }

    public void setEndTimeSrch(String endTimeSrch) {
        this.endTimeSrch = endTimeSrch;
    }

    public String getUpdateUserSrch() {
        return updateUserSrch;
    }

    public void setUpdateUserSrch(String updateUserSrch) {
        this.updateUserSrch = updateUserSrch;
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
}

/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.nifaconfig;

import com.hyjf.common.paginator.Paginator;

import java.io.Serializable;
import java.util.List;

/**
 * @author nixiaoling
 * @version NifaConfigBean, v0.1 2018/7/4 11:46
 * 字段定义
 */
public class FieldDefinitionBean implements Serializable {
    /**
     * serialVersionUID
     */

    private static final long serialVersionUID = 387630498860089653L;

    private List<NifaFieldDefinitionResponseBean> recordList;
    private String id;
    //借款用途限制
    private String borrowingRestrictions;
    //借款放款日判断依据
    private String judgmentsBased;
    //逾期定义
    private String overdueDefinition;
    //逾期还款责任
    private String overdueResponsibility;
    //逾期还款流程
    private String overdueProcess;


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

    public List<NifaFieldDefinitionResponseBean> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<NifaFieldDefinitionResponseBean> recordList) {
        this.recordList = recordList;
    }

    public String getBorrowingRestrictions() {
        return borrowingRestrictions;
    }

    public void setBorrowingRestrictions(String borrowingRestrictions) {
        this.borrowingRestrictions = borrowingRestrictions;
    }

    public String getJudgmentsBased() {
        return judgmentsBased;
    }

    public void setJudgmentsBased(String judgmentsBased) {
        this.judgmentsBased = judgmentsBased;
    }

    public String getOverdueDefinition() {
        return overdueDefinition;
    }

    public void setOverdueDefinition(String overdueDefinition) {
        this.overdueDefinition = overdueDefinition;
    }

    public String getOverdueResponsibility() {
        return overdueResponsibility;
    }

    public void setOverdueResponsibility(String overdueResponsibility) {
        this.overdueResponsibility = overdueResponsibility;
    }

    public String getOverdueProcess() {
        return overdueProcess;
    }

    public void setOverdueProcess(String overdueProcess) {
        this.overdueProcess = overdueProcess;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void setPaginatorPage(int paginatorPage) {
        this.paginatorPage = paginatorPage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

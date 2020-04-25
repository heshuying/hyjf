package com.hyjf.admin.manager.config.nifaconfig;

import java.io.Serializable;

public class NifaFieldDefinitionResponseBean implements Serializable {
    private Integer id;

    private String borrowingRestrictions;

    private String judgmentsBased;

    private String repayDateRule;

    private String overdueDefinition;

    private String overdueResponsibility;

    private String overdueProcess;

    private String updateDate;

    private String updateUserName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBorrowingRestrictions() {
        return borrowingRestrictions;
    }

    public void setBorrowingRestrictions(String borrowingRestrictions) {
        this.borrowingRestrictions = borrowingRestrictions == null ? null : borrowingRestrictions.trim();
    }

    public String getJudgmentsBased() {
        return judgmentsBased;
    }

    public void setJudgmentsBased(String judgmentsBased) {
        this.judgmentsBased = judgmentsBased == null ? null : judgmentsBased.trim();
    }

    public String getRepayDateRule() {
        return repayDateRule;
    }

    public void setRepayDateRule(String repayDateRule) {
        this.repayDateRule = repayDateRule == null ? null : repayDateRule.trim();
    }

    public String getOverdueDefinition() {
        return overdueDefinition;
    }

    public void setOverdueDefinition(String overdueDefinition) {
        this.overdueDefinition = overdueDefinition == null ? null : overdueDefinition.trim();
    }

    public String getOverdueResponsibility() {
        return overdueResponsibility;
    }

    public void setOverdueResponsibility(String overdueResponsibility) {
        this.overdueResponsibility = overdueResponsibility == null ? null : overdueResponsibility.trim();
    }

    public String getOverdueProcess() {
        return overdueProcess;
    }

    public void setOverdueProcess(String overdueProcess) {
        this.overdueProcess = overdueProcess == null ? null : overdueProcess.trim();
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
}
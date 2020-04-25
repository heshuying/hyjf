/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.olddata.repayplan;

/**
 * @author nxl
 * @version repayPlanBean, v0.1 2019/1/4 15:50
 */
public class CertOldRepayPlanBean {
    private String version;
    private String sourceCode;
    private String sourceProductCode;
    private String userIdcardHash;
    private String totalIssue;
    private String issue;
    private String replanId;
    private String curFund;
    private String curInterest;
    private String repayTime;
    private String groupByDate;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceProductCode() {
        return sourceProductCode;
    }

    public void setSourceProductCode(String sourceProductCode) {
        this.sourceProductCode = sourceProductCode;
    }

    public String getUserIdcardHash() {
        return userIdcardHash;
    }

    public void setUserIdcardHash(String userIdcardHash) {
        this.userIdcardHash = userIdcardHash;
    }

    public String getTotalIssue() {
        return totalIssue;
    }

    public void setTotalIssue(String totalIssue) {
        this.totalIssue = totalIssue;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getReplanId() {
        return replanId;
    }

    public void setReplanId(String replanId) {
        this.replanId = replanId;
    }

    public String getCurFund() {
        return curFund;
    }

    public void setCurFund(String curFund) {
        this.curFund = curFund;
    }

    public String getCurInterest() {
        return curInterest;
    }

    public void setCurInterest(String curInterest) {
        this.curInterest = curInterest;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    public String getGroupByDate() {
        return groupByDate;
    }

    public void setGroupByDate(String groupByDate) {
        this.groupByDate = groupByDate;
    }
}

/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.olddata.creditor;

/**
 * @author nxl
 * @version repayPlanBean, v0.1 2019/1/4 15:50
 */
public class CertOldTenderInfoBean {
    private String version;
    private String finClaimId;
    private String sourceCode;
    private String sourceProductCode;
    private String sourceFinancingCode;
    private String userIdcardHash;
    private String invAmount;
    private String invRate;
    private String invTime;
    private String redpackage;
    private String lockTime;
    private String groupByDate;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFinClaimId() {
        return finClaimId;
    }

    public void setFinClaimId(String finClaimId) {
        this.finClaimId = finClaimId;
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

    public String getSourceFinancingCode() {
        return sourceFinancingCode;
    }

    public void setSourceFinancingCode(String sourceFinancingCode) {
        this.sourceFinancingCode = sourceFinancingCode;
    }

    public String getUserIdcardHash() {
        return userIdcardHash;
    }

    public void setUserIdcardHash(String userIdcardHash) {
        this.userIdcardHash = userIdcardHash;
    }

    public String getInvAmount() {
        return invAmount;
    }

    public void setInvAmount(String invAmount) {
        this.invAmount = invAmount;
    }

    public String getInvRate() {
        return invRate;
    }

    public void setInvRate(String invRate) {
        this.invRate = invRate;
    }

    public String getInvTime() {
        return invTime;
    }

    public void setInvTime(String invTime) {
        this.invTime = invTime;
    }

    public String getRedpackage() {
        return redpackage;
    }

    public void setRedpackage(String redpackage) {
        this.redpackage = redpackage;
    }

    public String getLockTime() {
        return lockTime;
    }

    public void setLockTime(String lockTime) {
        this.lockTime = lockTime;
    }

    public String getGroupByDate() {
        return groupByDate;
    }

    public void setGroupByDate(String groupByDate) {
        this.groupByDate = groupByDate;
    }
}

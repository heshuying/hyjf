/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.hgdatareport.cert.olddata.undertake;

/**
 * @author nxl
 * @version CertCreditInfoBean, v0.1 2018/11/30 17:40
 */
public class CertCreditInfoBean {
    //承接信息编号
    private String unFinClaimId;
    //转让编号
    private String transferId;
    //债权信息编号
    private String finClaimId;
    //承接人用户标示 Hash
    private String userIdcardHash;
    //承接人投资金额(元)
    private String takeAmount;
    //承接利息金额(元)
    private String takeInterest;
    //承接浮动金额(元)
    private String floatMoney;
    //承接预期年化收益率
    private String takeRate;
    //承接时间
    private String takeTime;
    //投资红包
    private String redpackage;
    //封闭截止时间
    private String lockTime;

    public String getUnFinClaimId() {
        return unFinClaimId;
    }

    public void setUnFinClaimId(String unFinClaimId) {
        this.unFinClaimId = unFinClaimId;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getFinClaimId() {
        return finClaimId;
    }

    public void setFinClaimId(String finClaimId) {
        this.finClaimId = finClaimId;
    }

    public String getUserIdcardHash() {
        return userIdcardHash;
    }

    public void setUserIdcardHash(String userIdcardHash) {
        this.userIdcardHash = userIdcardHash;
    }

    public String getTakeAmount() {
        return takeAmount;
    }

    public void setTakeAmount(String takeAmount) {
        this.takeAmount = takeAmount;
    }

    public String getTakeInterest() {
        return takeInterest;
    }

    public void setTakeInterest(String takeInterest) {
        this.takeInterest = takeInterest;
    }

    public String getFloatMoney() {
        return floatMoney;
    }

    public void setFloatMoney(String floatMoney) {
        this.floatMoney = floatMoney;
    }

    public String getTakeRate() {
        return takeRate;
    }

    public void setTakeRate(String takeRate) {
        this.takeRate = takeRate;
    }

    public String getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(String takeTime) {
        this.takeTime = takeTime;
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
}

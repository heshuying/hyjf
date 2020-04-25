/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mongo.hgdatareport.entity;

import com.alibaba.fastjson.JSONArray;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * //合规数据上报 CERT  国家互联网应急中心   旧数据上报  标的信息
 *
 * @author sss
 * @version BaseHgDataReportEntity, v0.1 2018/6/27 10:06
 */
@Document(collection = "ht_cert_borrow")
public class CertBorrowEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 标的号
     */
    private String borrowNid;

    /**
     * 借款人ID
     */
    private String borrowUserId;
    /**
     * 是否上送用户信息
     */
    private String isUserInfo;
    /**
     * 是否上送散标信息
     */
    private String isScatter;
    /**
     * 是否上送散标状态
     */
    private String isStatus;
    /**
     * 是否上送还款计划
     */
    private String isRepayPlan;
    /**
     * 是否上送债权信息
     */
    private String isCredit;
    /**
     * 是否上送转让项目
     */
    private String isTransfer;
    /**
     * 是否上送转让状态
     */
    private String isTransferStatus;
    /**
     * 是否上送承接信息
     */
    private String isUnderTake;
    /**
     * 是否上送交易流水
     */
    private String isTransact;

    public CertBorrowEntity() {
    }
    public CertBorrowEntity(String borrowNid) {
        this.borrowNid = borrowNid;
        this.isUserInfo = "0";
        this.isScatter = "0";
        this.isStatus = "0";
        this.isRepayPlan = "0";
        this.isCredit = "0";
        this.isTransfer = "0";
        this.isTransferStatus = "0";
        this.isUnderTake = "0";
        this.isTransact = "0";
    }

    public String getBorrowNid() {
        return borrowNid;
    }

    public void setBorrowNid(String borrowNid) {
        this.borrowNid = borrowNid;
    }

    public String getIsUserInfo() {
        return isUserInfo;
    }

    public void setIsUserInfo(String isUserInfo) {
        this.isUserInfo = isUserInfo;
    }

    public String getIsScatter() {
        return isScatter;
    }

    public void setIsScatter(String isScatter) {
        this.isScatter = isScatter;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getIsRepayPlan() {
        return isRepayPlan;
    }

    public void setIsRepayPlan(String isRepayPlan) {
        this.isRepayPlan = isRepayPlan;
    }

    public String getIsCredit() {
        return isCredit;
    }

    public void setIsCredit(String isCredit) {
        this.isCredit = isCredit;
    }

    public String getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(String isTransfer) {
        this.isTransfer = isTransfer;
    }

    public String getIsTransferStatus() {
        return isTransferStatus;
    }

    public void setIsTransferStatus(String isTransferStatus) {
        this.isTransferStatus = isTransferStatus;
    }

    public String getIsUnderTake() {
        return isUnderTake;
    }

    public void setIsUnderTake(String isUnderTake) {
        this.isUnderTake = isUnderTake;
    }

    public String getIsTransact() {
        return isTransact;
    }

    public void setIsTransact(String isTransact) {
        this.isTransact = isTransact;
    }

    public String getBorrowUserId() {
        return borrowUserId;
    }

    public void setBorrowUserId(String borrowUserId) {
        this.borrowUserId = borrowUserId;
    }
}

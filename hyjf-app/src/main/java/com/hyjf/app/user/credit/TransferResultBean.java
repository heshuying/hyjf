package com.hyjf.app.user.credit;

import com.hyjf.app.BaseResultBeanFrontEnd;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author fuqiang
 */
public class TransferResultBean extends BaseResultBeanFrontEnd {

    /** 项目细节 */
    private List<ProjectDetail> projectDetail;

    /**  项目还款计划 */
    private List<RepayPlan> repayPlan;

    /**  转让详情 */
    private TransferInfo transferInfo;

    public List<ProjectDetail> getProjectDetail() {
        return projectDetail;
    }

    public void setProjectDetail(List<ProjectDetail> projectDetail) {
        this.projectDetail = projectDetail;
    }

    public List<RepayPlan> getRepayPlan() {
        return repayPlan;
    }

    public void setRepayPlan(List<RepayPlan> repayPlan) {
        this.repayPlan = repayPlan;
    }

    public TransferInfo getTransferInfo() {
        return transferInfo;
    }

    public void setTransferInfo(TransferInfo transferInfo) {
        this.transferInfo = transferInfo;
    }
}

/**
 * 内部类:项目细节
 *
 */
class ProjectDetail implements Serializable{
    /**  项目id */
    private String id;
    /**  项目title */
    private String title;
    /**  项目信息 */
    List<ProjectMsg> msg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ProjectMsg> getMsg() {
        return msg;
    }

    public void setMsg(List<ProjectMsg> msg) {
        this.msg = msg;
    }
}

/**
 * 内部类:项目信息
 *
 */

class ProjectMsg implements Serializable{
    /** 字段id */
    private String id;
    /** 字段名称 */
    private String key;
    /** 字段值 */
    private String val;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}

/**
 * 内部类:项目还款计划
 *
 */
class RepayPlan implements Serializable{
    /** 回款时间 */
    private String time;
    /** 回款期限 */
    private String number;
    /** 回款金额 */
    private String account;
    /** 回款状态 */
    private String status;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

/**
 * 内部类:债权转让详情
 *
 */
class TransferInfo {
    /** 转让时间 */
    private String date;
    /** 转让本金 */
    private BigDecimal transferPrice;
    /** 本金折让率 */
    private BigDecimal discount;
    /** 剩余期限 */
    private Integer remainTime;
    /** 转让价格 */
    private BigDecimal realAmount;
    /** 服务费 */
    private BigDecimal serviceCharge;
    /** 已转让金额 */
    private BigDecimal hadTransfer;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(BigDecimal transferPrice) {
        this.transferPrice = transferPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(Integer remainTime) {
        this.remainTime = remainTime;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getHadTransfer() {
        return hadTransfer;
    }

    public void setHadTransfer(BigDecimal hadTransfer) {
        this.hadTransfer = hadTransfer;
    }
}




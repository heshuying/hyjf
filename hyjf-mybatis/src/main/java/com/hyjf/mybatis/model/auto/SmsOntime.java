package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SmsOntime implements Serializable {
    private Integer id;

    private String channelType;

    private Integer starttime;

    private Integer endtime;

    private Integer status;

    private Integer openAccount;

    private BigDecimal addMoneyCount;

    private String addTimeBegin;

    private String addTimeEnd;

    private String reTimeBegin;

    private String reTimeEnd;

    private String ip;

    private String remark;

    private Integer createUserId;

    private String createUserName;

    private Integer createTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType == null ? null : channelType.trim();
    }

    public Integer getStarttime() {
        return starttime;
    }

    public void setStarttime(Integer starttime) {
        this.starttime = starttime;
    }

    public Integer getEndtime() {
        return endtime;
    }

    public void setEndtime(Integer endtime) {
        this.endtime = endtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOpenAccount() {
        return openAccount;
    }

    public void setOpenAccount(Integer openAccount) {
        this.openAccount = openAccount;
    }

    public BigDecimal getAddMoneyCount() {
        return addMoneyCount;
    }

    public void setAddMoneyCount(BigDecimal addMoneyCount) {
        this.addMoneyCount = addMoneyCount;
    }

    public String getAddTimeBegin() {
        return addTimeBegin;
    }

    public void setAddTimeBegin(String addTimeBegin) {
        this.addTimeBegin = addTimeBegin == null ? null : addTimeBegin.trim();
    }

    public String getAddTimeEnd() {
        return addTimeEnd;
    }

    public void setAddTimeEnd(String addTimeEnd) {
        this.addTimeEnd = addTimeEnd == null ? null : addTimeEnd.trim();
    }

    public String getReTimeBegin() {
        return reTimeBegin;
    }

    public void setReTimeBegin(String reTimeBegin) {
        this.reTimeBegin = reTimeBegin == null ? null : reTimeBegin.trim();
    }

    public String getReTimeEnd() {
        return reTimeEnd;
    }

    public void setReTimeEnd(String reTimeEnd) {
        this.reTimeEnd = reTimeEnd == null ? null : reTimeEnd.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
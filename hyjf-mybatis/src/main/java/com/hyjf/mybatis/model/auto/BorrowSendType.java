package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class BorrowSendType implements Serializable {
    private String sendCd;

    private String sendName;

    private Integer afterTime;

    private String remark;

    private String delFlag;

    private Integer createTime;

    private Integer updateTime;

    private static final long serialVersionUID = 1L;

    public String getSendCd() {
        return sendCd;
    }

    public void setSendCd(String sendCd) {
        this.sendCd = sendCd == null ? null : sendCd.trim();
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName == null ? null : sendName.trim();
    }

    public Integer getAfterTime() {
        return afterTime;
    }

    public void setAfterTime(Integer afterTime) {
        this.afterTime = afterTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }
}
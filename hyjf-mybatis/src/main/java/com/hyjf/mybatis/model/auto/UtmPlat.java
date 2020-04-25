package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class UtmPlat implements Serializable {
    private Integer id;

    private Integer sourceId;

    private String sourceName;

    private String delFlag;

    private Integer attornFlag;

    private String remark;

    private String createGroupCd;

    private String createUserId;

    private String createTime;

    private String updateGroupCd;

    private String updateUserId;

    private String updateTime;

    private Integer sourceType;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName == null ? null : sourceName.trim();
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }

    public Integer getAttornFlag() {
        return attornFlag;
    }

    public void setAttornFlag(Integer attornFlag) {
        this.attornFlag = attornFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCreateGroupCd() {
        return createGroupCd;
    }

    public void setCreateGroupCd(String createGroupCd) {
        this.createGroupCd = createGroupCd == null ? null : createGroupCd.trim();
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getUpdateGroupCd() {
        return updateGroupCd;
    }

    public void setUpdateGroupCd(String updateGroupCd) {
        this.updateGroupCd = updateGroupCd == null ? null : updateGroupCd.trim();
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId == null ? null : updateUserId.trim();
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }
}
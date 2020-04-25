package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.util.Date;

public class UserEvalationResultCustomize implements Serializable {
    private Integer id;

    private Integer userId;

    private String type;

    private String summary;

    private Integer scoreCount;

    private String instCode;

    private String instName;

    private Date lasttime;

    private Date createtime;

    private String remarks;

    private String revaluationMoney;
    private String typeString;

    private static final long serialVersionUID = 1L;

    public String getTypeString() {
        return getType();
    }
    public String getRevaluationMoney() {
        return revaluationMoney;
    }

    public void setRevaluationMoney(String revaluationMoney) {
        this.revaluationMoney = revaluationMoney;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    public Integer getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(Integer scoreCount) {
        this.scoreCount = scoreCount;
    }

    public String getInstCode() {
        return instCode;
    }

    public void setInstCode(String instCode) {
        this.instCode = instCode == null ? null : instCode.trim();
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName == null ? null : instName.trim();
    }

    public Date getLasttime() {
        return lasttime;
    }

    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}
package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.util.Date;

public class EvalationCustomize implements Serializable {
    private Integer id;

    private Integer scoreUp;

    private Integer scoreDown;

    private String type;

    private String summary;

    private Integer status;

    private String createuser;

    private String updateuser;

    private Date createtime;

    private Date updatetime;

    private String remarks;

    private String revaluationMoney;

    private static final long serialVersionUID = 1L;

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

    public Integer getScoreUp() {
        return scoreUp;
    }

    public void setScoreUp(Integer scoreUp) {
        this.scoreUp = scoreUp;
    }

    public Integer getScoreDown() {
        return scoreDown;
    }

    public void setScoreDown(Integer scoreDown) {
        this.scoreDown = scoreDown;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser == null ? null : createuser.trim();
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(String updateuser) {
        this.updateuser = updateuser == null ? null : updateuser.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}
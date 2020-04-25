package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.util.Date;

public class WithdrawalsTimeConfig implements Serializable {
    private Integer id;

    private String ifWorkingday;

    private String withdrawalsStart;

    private String withdrawalsEnd;

    private String immediatelyWithdraw;

    private String quickWithdraw;

    private String normalWithdraw;

    private Integer status;

    private String createuser;

    private String updateuser;

    private Date createtime;

    private Date updatetime;

    private String remarks;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIfWorkingday() {
        return ifWorkingday;
    }

    public void setIfWorkingday(String ifWorkingday) {
        this.ifWorkingday = ifWorkingday == null ? null : ifWorkingday.trim();
    }

    public String getWithdrawalsStart() {
        return withdrawalsStart;
    }

    public void setWithdrawalsStart(String withdrawalsStart) {
        this.withdrawalsStart = withdrawalsStart == null ? null : withdrawalsStart.trim();
    }

    public String getWithdrawalsEnd() {
        return withdrawalsEnd;
    }

    public void setWithdrawalsEnd(String withdrawalsEnd) {
        this.withdrawalsEnd = withdrawalsEnd == null ? null : withdrawalsEnd.trim();
    }

    public String getImmediatelyWithdraw() {
        return immediatelyWithdraw;
    }

    public void setImmediatelyWithdraw(String immediatelyWithdraw) {
        this.immediatelyWithdraw = immediatelyWithdraw == null ? null : immediatelyWithdraw.trim();
    }

    public String getQuickWithdraw() {
        return quickWithdraw;
    }

    public void setQuickWithdraw(String quickWithdraw) {
        this.quickWithdraw = quickWithdraw == null ? null : quickWithdraw.trim();
    }

    public String getNormalWithdraw() {
        return normalWithdraw;
    }

    public void setNormalWithdraw(String normalWithdraw) {
        this.normalWithdraw = normalWithdraw == null ? null : normalWithdraw.trim();
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
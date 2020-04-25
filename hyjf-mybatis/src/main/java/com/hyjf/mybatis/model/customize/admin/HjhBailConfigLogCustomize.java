/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.model.customize.admin;

import com.hyjf.mybatis.model.auto.HjhBailConfigLog;

import java.io.Serializable;

/**
 * @author PC-LIUSHOUYI
 * @version HjhBailConfigLogCustomize, v0.1 2018/7/30 10:47
 */
public class HjhBailConfigLogCustomize extends HjhBailConfigLog implements Serializable {

    private String instName;

    private String createUserName;

    private String createTimeStr;

    private String instCodeSrch;

    private String modifyColumnSrch;

    private String createUserNameSrch;

    private String startDate;

    private String endDate;

    private int limitStart = -1;

    private int limitEnd = -1;

    private static final long serialVersionUID = 1L;

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
    }

    public int getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(int limitEnd) {
        this.limitEnd = limitEnd;
    }

    public String getInstCodeSrch() {
        return instCodeSrch;
    }

    public void setInstCodeSrch(String instCodeSrch) {
        this.instCodeSrch = instCodeSrch;
    }

    public String getModifyColumnSrch() {
        return modifyColumnSrch;
    }

    public void setModifyColumnSrch(String modifyColumnSrch) {
        this.modifyColumnSrch = modifyColumnSrch;
    }

    public String getCreateUserNameSrch() {
        return createUserNameSrch;
    }

    public void setCreateUserNameSrch(String createUserNameSrch) {
        this.createUserNameSrch = createUserNameSrch;
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

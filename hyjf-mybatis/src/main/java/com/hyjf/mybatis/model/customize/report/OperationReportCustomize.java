package com.hyjf.mybatis.model.customize.report;

import com.hyjf.mybatis.model.auto.OperationReport;

import java.io.Serializable;

public class OperationReportCustomize extends OperationReport implements Serializable {
    //运营报告id
    private String ids;
    private String releaseTimeStr;

    private String typeRealName;

    private int sortMonth;
    private int sortDay;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getReleaseTimeStr() {
        return releaseTimeStr;
    }

    public void setReleaseTimeStr(String releaseTimeStr) {
        this.releaseTimeStr = releaseTimeStr;
    }

    public String getTypeRealName() {
        return typeRealName;
    }

    public void setTypeRealName(String typeRealName) {
        this.typeRealName = typeRealName;
    }

    public int getSortMonth() {
        return sortMonth;
    }

    public void setSortMonth(int sortMonth) {
        this.sortMonth = sortMonth;
    }

    public int getSortDay() {
        return sortDay;
    }

    public void setSortDay(int sortDay) {
        this.sortDay = sortDay;
    }
}
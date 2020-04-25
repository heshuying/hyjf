package com.hyjf.mybatis.model.customize;

import com.hyjf.mybatis.model.auto.EveLog;

/**
 * Created by cui on 2018/1/19.
 */
public class EveLogCustomize extends EveLog {

    private Integer limitStart;

    private Integer limitEnd;

    private String cendtString; //交易传输时间

    private String startDate; // 交易传输时间 起始
    private String endDate; // 交易传输时间 结束

    public Integer getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(Integer limitStart) {
        this.limitStart = limitStart;
    }

    public Integer getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(Integer limitEnd) {
        this.limitEnd = limitEnd;
    }

    public String getCendtString() {
        return cendtString;
    }

    public void setCendtString(String cendtString) {
        this.cendtString = cendtString;
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

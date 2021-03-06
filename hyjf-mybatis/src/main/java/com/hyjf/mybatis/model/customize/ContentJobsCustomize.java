package com.hyjf.mybatis.model.customize;


import com.hyjf.mybatis.model.auto.Jobs;

public class ContentJobsCustomize extends Jobs {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // 传参创建时间范围
    protected Integer startCreate;

    protected Integer endCreate;

    protected int limitStart = -1;

    protected int limitEnd = -1;

    public Integer getStartCreate() {
        return startCreate;
    }

    public void setStartCreate(Integer startCreate) {
        this.startCreate = startCreate;
    }

    public Integer getEndCreate() {
        return endCreate;
    }

    public void setEndCreate(Integer endCreate) {
        this.endCreate = endCreate;
    }

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

}

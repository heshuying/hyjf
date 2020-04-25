package com.hyjf.mybatis.model.auto;

import java.io.Serializable;
import java.util.Date;

public class AppAccessStatistics implements Serializable {
    private Long id;

    private Integer sourceId;

    private Date accessTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }
}
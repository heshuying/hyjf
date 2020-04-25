package com.hyjf.web.activity.activityinfo;

import java.io.Serializable;

import com.hyjf.mybatis.model.auto.ActivityF1;

public class ActivityF1ResultBean extends ActivityF1 implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 3833563509083142519L;

    // 排名
    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}

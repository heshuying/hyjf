package com.hyjf.mybatis.model.customize.statisticsway;

import com.hyjf.mybatis.model.auto.StatisticsWayConfigure;

import java.io.Serializable;

/**
 * Created by xiehuili on 2018/6/20.
 */
public class StatisticsWayConfigureCustomize extends StatisticsWayConfigure implements Serializable{

    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

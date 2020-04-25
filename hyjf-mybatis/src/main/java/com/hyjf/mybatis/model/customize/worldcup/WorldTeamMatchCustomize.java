package com.hyjf.mybatis.model.customize.worldcup;

import com.hyjf.mybatis.model.auto.WorldTeamMatch;

import java.io.Serializable;

/**
 * Created by DELL on 2018/6/14.
 */
public class WorldTeamMatchCustomize extends WorldTeamMatch implements Serializable {

    //竞赛时间
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

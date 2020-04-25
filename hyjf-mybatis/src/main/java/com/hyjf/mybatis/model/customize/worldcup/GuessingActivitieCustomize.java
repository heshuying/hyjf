package com.hyjf.mybatis.model.customize.worldcup;

import com.hyjf.mybatis.model.auto.GuessingWinning;

import java.io.Serializable;

/**
 * Created by xiehuili on 2018/6/13.
 */
public class GuessingActivitieCustomize extends GuessingWinning implements Serializable {
    //账户名
    private String username;

    //性名
//    private String name;
    //手机号
    private String mobile;
    //竞猜时间
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //竞猜场次
    private String batchName;
    //用户的竞猜球队名称---支持的球队（竞赛冠军中）
    private String teamName;
    //竞赛结果
    private String matchResult;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }
}

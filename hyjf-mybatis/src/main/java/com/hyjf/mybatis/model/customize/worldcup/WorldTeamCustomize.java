package com.hyjf.mybatis.model.customize.worldcup;

import java.io.Serializable;

/**
 * Created by DELL on 2018/6/19.
 */
public class WorldTeamCustomize implements Serializable {
    //主队id
    private Integer homeId;
    //客队id
    private Integer visitedId;
    //主队logo
    private String homeLogo;
    //客队logo
    private String visitedLogo;
    //主队名称
    private String homeName;
    //客队名称
    private String visitedName;
    //主队球队小组
    private Integer homeTeamGroupings;
    //客队球队小组
    private Integer visitedTeamGroupings;
    //球队比赛结果

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    private String matchResult;

    public Integer getHomeId() {
        return homeId;
    }
    public void setHomeId(Integer homeId) {
        this.homeId = homeId;
    }
    public Integer getVisitedId() {
        return visitedId;
    }
    public void setVisitedId(Integer visitedId) {
        this.visitedId = visitedId;
    }
    public String getHomeLogo() {
        return homeLogo;
    }
    public void setHomeLogo(String homeLogo) {
        this.homeLogo = homeLogo;
    }
    public String getVisitedLogo() {
        return visitedLogo;
    }
    public void setVisitedLogo(String visitedLogo) {
        this.visitedLogo = visitedLogo;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getVisitedName() {
        return visitedName;
    }

    public void setVisitedName(String visitedName) {
        this.visitedName = visitedName;
    }

    public Integer getHomeTeamGroupings() {
        return homeTeamGroupings;
    }

    public void setHomeTeamGroupings(Integer homeTeamGroupings) {
        this.homeTeamGroupings = homeTeamGroupings;
    }

    public Integer getVisitedTeamGroupings() {
        return visitedTeamGroupings;
    }

    public void setVisitedTeamGroupings(Integer visitedTeamGroupings) {
        this.visitedTeamGroupings = visitedTeamGroupings;
    }
}
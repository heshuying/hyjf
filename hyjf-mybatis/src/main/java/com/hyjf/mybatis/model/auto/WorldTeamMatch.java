package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class WorldTeamMatch implements Serializable {
    private Integer id;

    private Integer matchTeam;

    private Integer matchType;

    private Integer homeMatchTeam;

    private Integer visitingMatchTeam;

    private String batchName;

    private Integer matchTime;

    private String matchResult;

    private Integer winTeamId;

    private Integer createTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMatchTeam() {
        return matchTeam;
    }

    public void setMatchTeam(Integer matchTeam) {
        this.matchTeam = matchTeam;
    }

    public Integer getMatchType() {
        return matchType;
    }

    public void setMatchType(Integer matchType) {
        this.matchType = matchType;
    }

    public Integer getHomeMatchTeam() {
        return homeMatchTeam;
    }

    public void setHomeMatchTeam(Integer homeMatchTeam) {
        this.homeMatchTeam = homeMatchTeam;
    }

    public Integer getVisitingMatchTeam() {
        return visitingMatchTeam;
    }

    public void setVisitingMatchTeam(Integer visitingMatchTeam) {
        this.visitingMatchTeam = visitingMatchTeam;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName == null ? null : batchName.trim();
    }

    public Integer getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Integer matchTime) {
        this.matchTime = matchTime;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult == null ? null : matchResult.trim();
    }

    public Integer getWinTeamId() {
        return winTeamId;
    }

    public void setWinTeamId(Integer winTeamId) {
        this.winTeamId = winTeamId;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
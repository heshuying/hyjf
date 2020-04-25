package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class WorldCupTeam implements Serializable {
    private Integer id;

    private String teamYear;

    private Integer teamGroupings;

    private String teamName;

    private String teamLogo;

    private String teamNumber;

    private Integer championNum;

    private Integer isEliminate;

    private Integer createTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeamYear() {
        return teamYear;
    }

    public void setTeamYear(String teamYear) {
        this.teamYear = teamYear == null ? null : teamYear.trim();
    }

    public Integer getTeamGroupings() {
        return teamGroupings;
    }

    public void setTeamGroupings(Integer teamGroupings) {
        this.teamGroupings = teamGroupings;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName == null ? null : teamName.trim();
    }

    public String getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo == null ? null : teamLogo.trim();
    }

    public String getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(String teamNumber) {
        this.teamNumber = teamNumber == null ? null : teamNumber.trim();
    }

    public Integer getChampionNum() {
        return championNum;
    }

    public void setChampionNum(Integer championNum) {
        this.championNum = championNum;
    }

    public Integer getIsEliminate() {
        return isEliminate;
    }

    public void setIsEliminate(Integer isEliminate) {
        this.isEliminate = isEliminate;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
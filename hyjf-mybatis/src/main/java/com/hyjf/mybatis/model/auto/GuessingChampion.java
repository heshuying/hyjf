package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class GuessingChampion implements Serializable {
    private Integer id;

    private Integer userId;

    private String trueName;

    private Integer guessingChampionId;

    private Integer teamWinNum;

    private Integer voteTime;

    private Integer createTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName == null ? null : trueName.trim();
    }

    public Integer getGuessingChampionId() {
        return guessingChampionId;
    }

    public void setGuessingChampionId(Integer guessingChampionId) {
        this.guessingChampionId = guessingChampionId;
    }

    public Integer getTeamWinNum() {
        return teamWinNum;
    }

    public void setTeamWinNum(Integer teamWinNum) {
        this.teamWinNum = teamWinNum;
    }

    public Integer getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(Integer voteTime) {
        this.voteTime = voteTime;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
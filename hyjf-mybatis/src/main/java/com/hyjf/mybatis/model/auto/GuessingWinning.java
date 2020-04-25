package com.hyjf.mybatis.model.auto;

import java.io.Serializable;

public class GuessingWinning implements Serializable {
    private Integer id;

    private Integer userId;

    private String trueName;

    private Integer guessingMatchId;

    private Integer userMatchTeamId;

    private Integer guessingMatchResult;

    private Integer guessingMatchNum;

    private Integer guessingFieldNum;

    private Integer guessingRankings;

    private Integer guessingTime;

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

    public Integer getGuessingMatchId() {
        return guessingMatchId;
    }

    public void setGuessingMatchId(Integer guessingMatchId) {
        this.guessingMatchId = guessingMatchId;
    }

    public Integer getUserMatchTeamId() {
        return userMatchTeamId;
    }

    public void setUserMatchTeamId(Integer userMatchTeamId) {
        this.userMatchTeamId = userMatchTeamId;
    }

    public Integer getGuessingMatchResult() {
        return guessingMatchResult;
    }

    public void setGuessingMatchResult(Integer guessingMatchResult) {
        this.guessingMatchResult = guessingMatchResult;
    }

    public Integer getGuessingMatchNum() {
        return guessingMatchNum;
    }

    public void setGuessingMatchNum(Integer guessingMatchNum) {
        this.guessingMatchNum = guessingMatchNum;
    }

    public Integer getGuessingFieldNum() {
        return guessingFieldNum;
    }

    public void setGuessingFieldNum(Integer guessingFieldNum) {
        this.guessingFieldNum = guessingFieldNum;
    }

    public Integer getGuessingRankings() {
        return guessingRankings;
    }

    public void setGuessingRankings(Integer guessingRankings) {
        this.guessingRankings = guessingRankings;
    }

    public Integer getGuessingTime() {
        return guessingTime;
    }

    public void setGuessingTime(Integer guessingTime) {
        this.guessingTime = guessingTime;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
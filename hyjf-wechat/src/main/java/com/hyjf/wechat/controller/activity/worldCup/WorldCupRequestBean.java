package com.hyjf.wechat.controller.activity.worldCup;

public class WorldCupRequestBean {

	// 比赛场次
	private Integer guessingMatchId;
	// 支持球队ID
	private Integer matchTeamId;
	
	
	public Integer getGuessingMatchId() {
		return guessingMatchId;
	}
	public void setGuessingMatchId(Integer guessingMatchId) {
		this.guessingMatchId = guessingMatchId;
	}
	public Integer getMatchTeamId() {
		return matchTeamId;
	}
	public void setMatchTeamId(Integer matchTeamId) {
		this.matchTeamId = matchTeamId;
	}
	
	
}

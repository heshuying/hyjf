package com.hyjf.app.prize;


import com.hyjf.mybatis.model.auto.Submissions;

public class PrizeBean extends Submissions  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8174562253205841603L;
	
	// 奖品名称
	private String prizeName;
	// 奖品总需人次
	private int allPersonCount;
	// 奖品识别码
	private String prizeSelfCode;
	// 奖品已参与人次
	private int joinedPersonCount;
	// 是否已开奖
	private int prizeStatus;
	// 中奖号码
	private String prizeCode;
	// 中奖用户名
	private String userName;

    // 是否可以继续夺宝
    private boolean canPrize;

	public int getAllPersonCount() {
		return allPersonCount;
	}

	public void setAllPersonCount(int allPersonCount) {
		this.allPersonCount = allPersonCount;
	}

	public String getPrizeSelfCode() {
		return prizeSelfCode;
	}

	public void setPrizeSelfCode(String prizeSelfCode) {
		this.prizeSelfCode = prizeSelfCode;
	}

	public int getJoinedPersonCount() {
		return joinedPersonCount;
	}

	public void setJoinedPersonCount(int joinedPersonCount) {
		this.joinedPersonCount = joinedPersonCount;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public int getPrizeStatus() {
		return prizeStatus;
	}

	public void setPrizeStatus(int prizeStatus) {
		this.prizeStatus = prizeStatus;
	}

	public String getPrizeCode() {
		return prizeCode;
	}

	public void setPrizeCode(String prizeCode) {
		this.prizeCode = prizeCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isCanPrize() {
		return canPrize;
	}

	public void setCanPrize(boolean canPrize) {
		this.canPrize = canPrize;
	}
	
	

}

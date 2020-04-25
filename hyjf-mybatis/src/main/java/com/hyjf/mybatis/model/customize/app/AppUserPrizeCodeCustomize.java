package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;

public class AppUserPrizeCodeCustomize implements Serializable {


    private static final long serialVersionUID = 1L;
    
    private String userId;
    
    private String username;
    
    private String truename;
    
    private String mobile;
    
    private String referrerUserName;
    
    //奖品名称
    private String prizeName;
    //幸运吗
    private String prizeCode;
    //夺宝时间
    private String addTime;
    //添加时间戳
    private Long addTimeStamp;
    //是否中奖
    private String prizeFlg;
    // 奖品编号
    private int prizeId;
    // 用户兑奖号自增编号
    private int prizeIdentityId;
    // 奖品识别码
    private String prizeSelfCode;
    // 总需人次
    private int allPersonCount;
    // 已参与人次
    private int joinedPersonCount;
    // 是否开奖
    private int prizeStatus;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getReferrerUserName() {
        return referrerUserName;
    }

    public void setReferrerUserName(String referrerUserName) {
        this.referrerUserName = referrerUserName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeCode() {
        return prizeCode;
    }

    public void setPrizeCode(String prizeCode) {
        this.prizeCode = prizeCode;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getPrizeFlg() {
        return prizeFlg;
    }

    public void setPrizeFlg(String prizeFlg) {
        this.prizeFlg = prizeFlg;
    }
    
	public int getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(int prizeId) {
		this.prizeId = prizeId;
	}

	public int getPrizeIdentityId() {
		return prizeIdentityId;
	}

	public void setPrizeIdentityId(int prizeIdentityId) {
		this.prizeIdentityId = prizeIdentityId;
	}

	public String getPrizeSelfCode() {
		return prizeSelfCode;
	}

	public void setPrizeSelfCode(String prizeSelfCode) {
		this.prizeSelfCode = prizeSelfCode;
	}

	public int getAllPersonCount() {
		return allPersonCount;
	}

	public void setAllPersonCount(int allPersonCount) {
		this.allPersonCount = allPersonCount;
	}

	public int getJoinedPersonCount() {
		return joinedPersonCount;
	}

	public void setJoinedPersonCount(int joinedPersonCount) {
		this.joinedPersonCount = joinedPersonCount;
	}

	public int getPrizeStatus() {
		return prizeStatus;
	}

	public void setPrizeStatus(int prizeStatus) {
		this.prizeStatus = prizeStatus;
	}

    public Long getAddTimeStamp() {
        return addTimeStamp;
    }

    public void setAddTimeStamp(Long addTimeStamp) {
        this.addTimeStamp = addTimeStamp;
    }

}
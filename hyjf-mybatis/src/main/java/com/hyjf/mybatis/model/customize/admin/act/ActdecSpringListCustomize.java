package com.hyjf.mybatis.model.customize.admin.act;

import com.hyjf.common.paginator.Paginator;

public class ActdecSpringListCustomize {
	
    private Integer id;

    private String userId;

    private String userMobile;

    private String userName;

    private String operType;

    private Integer number;

    private String reward;

    private Integer totalNumber;

    private Integer availableNumber;

    private Integer newRecharge;

    private Integer newInvestment;

    private Integer operAmount;

    private String createUser;

    private String createTime;

    private String updateUser;

    private Integer updateTime;

    private Boolean delFlg;
    
    /** 翻页开始 */
    protected int limitStart = -1;
    /** 翻页结束 */
    protected int limitEnd = -1;

    /**
     * 翻页机能用的隐藏变量
     */
    private int paginatorPage = 1;

    /**
     * 列表画面自定义标签上的用翻页对象：paginator
     */
    private Paginator paginator;
    
/********************检索用字段START**********************/
	
	/**
	 * 用户名（检索用）
	 */
	private String userNameSrch;
	
	/**
	 * 手机号（检索用）
	 */
	private String numberSrch;
	
	/**
     * 检索条件 时间开始
     */
    private String timeStartSrch;

    /**
     * 检索条件 时间结束
     */
    private String timeEndSrch;
    
	/** 
	 * 操作 （检索用）
	 * 
	 */
    private String operTypeSrch;
    
    /** 
	 * 获得奖励 （检索用）
	 * 
	 */
    private String rewardSrch;
	
	/********************检索用字段END**********************/
    

    public int getLimitStart() {
		return limitStart;
	}

	public String getUserNameSrch() {
		return userNameSrch;
	}

	public void setUserNameSrch(String userNameSrch) {
		this.userNameSrch = userNameSrch;
	}

	public String getNumberSrch() {
		return numberSrch;
	}

	public void setNumberSrch(String numberSrch) {
		this.numberSrch = numberSrch;
	}

	public String getTimeStartSrch() {
		return timeStartSrch;
	}

	public void setTimeStartSrch(String timeStartSrch) {
		this.timeStartSrch = timeStartSrch;
	}

	public String getTimeEndSrch() {
		return timeEndSrch;
	}

	public void setTimeEndSrch(String timeEndSrch) {
		this.timeEndSrch = timeEndSrch;
	}

	public String getOperTypeSrch() {
		return operTypeSrch;
	}

	public void setOperTypeSrch(String operTypeSrch) {
		this.operTypeSrch = operTypeSrch;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getLimitEnd() {
		return limitEnd;
	}

	public void setLimitEnd(int limitEnd) {
		this.limitEnd = limitEnd;
	}

	public int getPaginatorPage() {
		return paginatorPage;
	}

	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile == null ? null : userMobile.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }
    

    public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward == null ? null : reward.trim();
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Integer getAvailableNumber() {
        return availableNumber;
    }

    public void setAvailableNumber(Integer availableNumber) {
        this.availableNumber = availableNumber;
    }

    public Integer getNewRecharge() {
        return newRecharge;
    }

    public void setNewRecharge(Integer newRecharge) {
        this.newRecharge = newRecharge;
    }

    public Integer getNewInvestment() {
        return newInvestment;
    }

    public void setNewInvestment(Integer newInvestment) {
        this.newInvestment = newInvestment;
    }

    public Integer getOperAmount() {
        return operAmount;
    }

    public void setOperAmount(Integer operAmount) {
        this.operAmount = operAmount;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }
    

    public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Boolean delFlg) {
        this.delFlg = delFlg;
    }

	public String getRewardSrch() {
		return rewardSrch;
	}

	public void setRewardSrch(String rewardSrch) {
		this.rewardSrch = rewardSrch;
	}
    
}

package com.hyjf.mybatis.model.customize;

import com.hyjf.mybatis.model.auto.AleveLog;

/**
 * Created by cuigq on 2018/1/22.
 */
public class AleveLogCustomize extends AleveLog {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -749099382246115294L;
	
	//自动同步用生成订单id
	private String orderId;
	//userid
	private Integer userId;
	//入账日期起止
    private String startValdate;
    private String endValdate;

    //交易日期起止
    private String startInpdate;
    private String endInpdate;

    //自然日期起止
    private String startReldate;
    private String endReldate;

    private Integer limitStart;

    private Integer limitEnd;

    public String getStartValdate() {
        return startValdate;
    }

    public void setStartValdate(String startValdate) {
        this.startValdate = startValdate;
    }

    public String getEndValdate() {
        return endValdate;
    }

    public void setEndValdate(String endValdate) {
        this.endValdate = endValdate;
    }

    public String getStartInpdate() {
        return startInpdate;
    }

    public void setStartInpdate(String startInpdate) {
        this.startInpdate = startInpdate;
    }

    public String getEndInpdate() {
        return endInpdate;
    }

    public void setEndInpdate(String endInpdate) {
        this.endInpdate = endInpdate;
    }

    public String getStartReldate() {
        return startReldate;
    }

    public void setStartReldate(String startReldate) {
        this.startReldate = startReldate;
    }

    public String getEndReldate() {
        return endReldate;
    }

    public void setEndReldate(String endReldate) {
        this.endReldate = endReldate;
    }

    public Integer getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(Integer limitStart) {
        this.limitStart = limitStart;
    }

    public Integer getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(Integer limitEnd) {
        this.limitEnd = limitEnd;
    }

	/**
	 * orderId
	 * @return the orderId
	 */
		
	public String getOrderId() {
		return orderId;
			
	}

	/**
	 * @param orderId the orderId to set
	 */
		
	public void setOrderId(String orderId) {
		this.orderId = orderId;
			
	}

	/**
	 * userId
	 * @return the userId
	 */
		
	public Integer getUserId() {
		return userId;
			
	}

	/**
	 * @param userId the userId to set
	 */
		
	public void setUserId(Integer userId) {
		this.userId = userId;
			
	}
}

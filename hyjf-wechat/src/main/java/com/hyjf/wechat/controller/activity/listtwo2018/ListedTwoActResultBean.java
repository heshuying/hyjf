package com.hyjf.wechat.controller.activity.listtwo2018;

import com.hyjf.wechat.base.BaseResultBean;

public class ListedTwoActResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;

	/**
     * 领取金额
     */
    private int acceptPrize;
    
    /**
     * 累计冲投金额
     */
    private int cumulativeInvest;

	/**
	 * acceptPrize
	 * @return the acceptPrize
	 */
	
	public int getAcceptPrize() {
		return acceptPrize;
	}

	/**
	 * @param acceptPrize the acceptPrize to set
	 */
	
	public void setAcceptPrize(int acceptPrize) {
		this.acceptPrize = acceptPrize;
	}

	/**
	 * cumulativeInvest
	 * @return the cumulativeInvest
	 */
	
	public int getCumulativeInvest() {
		return cumulativeInvest;
	}

	/**
	 * @param cumulativeInvest the cumulativeInvest to set
	 */
	
	public void setCumulativeInvest(int cumulativeInvest) {
		this.cumulativeInvest = cumulativeInvest;
	}
    
}

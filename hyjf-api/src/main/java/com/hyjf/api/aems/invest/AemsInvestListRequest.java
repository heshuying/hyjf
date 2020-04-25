/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年10月16日 上午9:32:07
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.api.aems.invest;

import com.hyjf.base.bean.BaseBean;

/**
 * @author jijun 20180911
 */
public class AemsInvestListRequest extends BaseBean {

	/*开始时间（必填）*/
	private String startTime;
	/*结束时间（必填）*/
	private String endTime;
	/*电子账号（选填）*/
	private String accountId;
	/*标的编号（选填）*/
	private String borrowNid;

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String getAccountId() {
		return accountId;
	}

	@Override
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getBorrowNid() {
		return borrowNid;
	}
	public void setBorrowNid(String borrowNid) {
		this.borrowNid = borrowNid;
	}
	
	
}

	
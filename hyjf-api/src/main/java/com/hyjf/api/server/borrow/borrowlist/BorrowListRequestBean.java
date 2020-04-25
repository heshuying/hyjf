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
	
package com.hyjf.api.server.borrow.borrowlist;

import com.hyjf.base.bean.BaseBean;

/**
 * @author lb
 */
public class BorrowListRequestBean extends BaseBean {
	private String borrowStatus;
	/**
	 * borrowStatus
	 * @return the borrowStatus
	 */
	
	public String getBorrowStatus() {
		return borrowStatus;
	}
	/**
	 * @param borrowStatus the borrowStatus to set
	 */
	
	public void setBorrowStatus(String borrowStatus) {
		this.borrowStatus = borrowStatus;
	}
}

	
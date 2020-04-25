/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hyjf.mybatis.model.auto.Borrow;

/**
 * @author Administrator
 */

public class BorrowSynCustomize extends Borrow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3442223613907306988L;

	private BigDecimal repayFee;

	public BigDecimal getRepayFee() {
		return repayFee;
	}

	public void setRepayFee(BigDecimal repayFee) {
		this.repayFee = repayFee;
	}

}

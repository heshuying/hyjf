/**
 * Description:计划列表
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by :
 */

package com.hyjf.mybatis.model.customize.web.htj;

import java.io.Serializable;

/**
 * @author 李彬
 */

public class DebtPlanInvestDataCustomize implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 5748630051215873837L;

	// 加入汇添金计划金额总数
	private String totalAmount;
	// 为用户赚取总额
	private String totalEarnAmount;
	// 加入总人次
	private String totalJoinNum;
	
	
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getTotalEarnAmount() {
		return totalEarnAmount;
	}
	public void setTotalEarnAmount(String totalEarnAmount) {
		this.totalEarnAmount = totalEarnAmount;
	}
	public String getTotalJoinNum() {
		return totalJoinNum;
	}
	public void setTotalJoinNum(String totalJoinNum) {
		this.totalJoinNum = totalJoinNum;
	}
	
	


}

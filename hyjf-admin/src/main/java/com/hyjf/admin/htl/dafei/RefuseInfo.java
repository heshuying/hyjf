/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Michael
 * @version: 1.0
 * Created at: 2015年12月8日 上午9:17:50
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.admin.htl.dafei;

/**
 * @author Michael
 */

public class RefuseInfo {
	//合同号
	public  String ContractNo;
	//时间
	public  String ContractDate;
	//客户姓名
	public String CustomerName;
	//金额
	public String CreditAmount;
	//拒绝原因
	public String Reason;
	//合作模式
	public String CreditModel;
	
	public void setContractDate(String contractDate) {
		ContractDate = contractDate;
	}
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	public void setCreditAmount(String creditAmount) {
		CreditAmount = creditAmount;
	}
	public void setCreditModel(String creditModel) {
		CreditModel = creditModel;
	}
	public void setContractNo(String contractNo) {
		ContractNo = contractNo;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
	public String getContractNo() {
		return ContractNo;
	}
	public String getContractDate() {
		return ContractDate;
	}
	public String getCustomerName() {
		return CustomerName;
	}
	public String getCreditAmount() {
		return CreditAmount;
	}
	public String getReason() {
		return Reason;
	}
	public String getCreditModel() {
		return CreditModel;
	}
	
}

	
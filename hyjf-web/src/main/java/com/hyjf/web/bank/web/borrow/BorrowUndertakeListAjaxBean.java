/**
 * Description:项目承接记录列表查询所用vo
 * Copyright: Copyright (HYJF Corporation) 2018
 * Company: HYJF Corporation
 * @author: nixiaoling
 * @version: 1.0
 * Created at: 2018年05月15日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.bank.web.borrow;

import java.io.Serializable;
import java.util.List;

import com.hyjf.bank.service.BaseAjaxResultBean;
import com.hyjf.mybatis.model.customize.web.WebProjectUndertakeListCustomize;

public class BorrowUndertakeListAjaxBean extends BaseAjaxResultBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 5013753434843143557L;

	private List<WebProjectUndertakeListCustomize> projectUndertakeList;
	//承接次数
	private String undertRecordTotle;
	//承接总金额
	private String sumUndertakeAccount;

	public List<WebProjectUndertakeListCustomize> getProjectUndertakeList() {
		return projectUndertakeList;
	}

	public void setProjectUndertakeList(List<WebProjectUndertakeListCustomize> projectUndertakeList) {
		this.projectUndertakeList = projectUndertakeList;
	}

	public String getUndertRecordTotle() {
		return undertRecordTotle;
	}

	public void setUndertRecordTotle(String undertRecordTotle) {
		this.undertRecordTotle = undertRecordTotle;
	}

	public String getSumUndertakeAccount() {
		return sumUndertakeAccount;
	}

	public void setSumUndertakeAccount(String sumUndertakeAccount) {
		this.sumUndertakeAccount = sumUndertakeAccount;
	}

	

}

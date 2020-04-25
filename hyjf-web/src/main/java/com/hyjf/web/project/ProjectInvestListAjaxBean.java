/**
 * Description:项目出借用户列表查询所用vo
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.project;

import java.io.Serializable;
import java.util.List;

import com.hyjf.mybatis.model.customize.web.WebProjectInvestListCustomize;
import com.hyjf.web.WebBaseAjaxResultBean;

public class ProjectInvestListAjaxBean extends WebBaseAjaxResultBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 5013753434843143557L;
	
	private List<WebProjectInvestListCustomize> projectInvestList;
	
	private String investTotal;
	
	private String investTimes;

	public List<WebProjectInvestListCustomize> getProjectInvestList() {
		return projectInvestList;
	}

	public void setProjectInvestList(List<WebProjectInvestListCustomize> projectInvestList) {
		this.projectInvestList = projectInvestList;
	}

	public String getInvestTotal() {
		return investTotal;
	}

	public void setInvestTotal(String investTotal) {
		this.investTotal = investTotal;
	}

	public String getInvestTimes() {
		return investTimes;
	}

	public void setInvestTimes(String investTimes) {
		this.investTimes = investTimes;
	}

}

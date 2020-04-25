/**
 * Description:项目列表查询所用vo
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.bank.web.borrow;

import java.io.Serializable;
import java.util.List;

import com.hyjf.mybatis.model.customize.web.WebProjectListCustomize;
import com.hyjf.web.WebBaseAjaxResultBean;

public class BorrowListAJaxBean extends WebBaseAjaxResultBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -7415746042913857105L;

	private List<WebProjectListCustomize> projectList;

	private int nowTime;
	
	public List<WebProjectListCustomize> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<WebProjectListCustomize> projectList) {
		this.projectList = projectList;
	}

	public int getNowTime() {
		return nowTime;
	}

	public void setNowTime(int nowTime) {
		this.nowTime = nowTime;
	}

}

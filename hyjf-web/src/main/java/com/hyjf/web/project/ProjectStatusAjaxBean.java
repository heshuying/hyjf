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

import com.hyjf.web.WebBaseAjaxResultBean;

public class ProjectStatusAjaxBean extends WebBaseAjaxResultBean implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3947763122451053808L;
	
	private String projectStatus;


	public String getProjectStatus() {
		return projectStatus;
	}


	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	


}

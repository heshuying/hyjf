package com.hyjf.web.user.mytender;

import java.io.Serializable;
import java.util.List;

import com.hyjf.mybatis.model.customize.web.WebUserProjectListCustomize;
import com.hyjf.web.WebBaseAjaxResultBean;

public class ProjectListAjaxBean extends WebBaseAjaxResultBean implements Serializable {

	private static final long serialVersionUID = 3278149257478770256L;
	
	private String projectStatus;

	private List<WebUserProjectListCustomize> projectlist;

	public List<WebUserProjectListCustomize> getProjectlist() {
		return projectlist;
	}

	public void setProjectlist(List<WebUserProjectListCustomize> projectlist) {
		this.projectlist = projectlist;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

}

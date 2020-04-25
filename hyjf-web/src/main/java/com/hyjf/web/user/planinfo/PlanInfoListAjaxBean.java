package com.hyjf.web.user.planinfo;

import java.io.Serializable;
import java.util.List;

import com.hyjf.mybatis.model.customize.PlanLockCustomize;
import com.hyjf.web.WebBaseAjaxResultBean;

public class PlanInfoListAjaxBean extends WebBaseAjaxResultBean implements Serializable {

	private static final long serialVersionUID = 3278149257478770256L;
	
	private String planStatus;
	private List<PlanLockCustomize> projectlist;

	public List<PlanLockCustomize> getProjectlist() {
		return projectlist;
	}

	public void setProjectlist(List<PlanLockCustomize> projectlist) {
		this.projectlist = projectlist;
	}

	public String getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}


}

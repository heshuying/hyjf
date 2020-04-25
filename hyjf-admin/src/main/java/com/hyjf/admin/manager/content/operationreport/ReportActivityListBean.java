package com.hyjf.admin.manager.content.operationreport;

import java.io.Serializable;
import java.util.List;

import com.hyjf.mybatis.model.auto.OperationReportActivity;

public class ReportActivityListBean  implements Serializable {
	private List<OperationReportActivity> activelist;

	public List<OperationReportActivity> getActivelist() {
		return activelist;
	}

	public void setActivelist(List<OperationReportActivity> activelist) {
		this.activelist = activelist;
	}
	
	
}

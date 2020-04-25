package com.hyjf.mybatis.model.customize.apiweb.wdzj;

public class PreapysListCustomize {

	String projectId;
	int deadline;
	String deadlineUnit;
	public String getProjectId() {
		return com.hyjf.mybatis.model.customize.apiweb.wdzj.BorrowListCustomize.encryptBorrowNid(projectId);
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public int getDeadline() {
		return deadline;
	}
	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}
	public String getDeadlineUnit() {
		return deadlineUnit;
	}
	public void setDeadlineUnit(String deadlineUnit) {
		this.deadlineUnit = deadlineUnit;
	}
}

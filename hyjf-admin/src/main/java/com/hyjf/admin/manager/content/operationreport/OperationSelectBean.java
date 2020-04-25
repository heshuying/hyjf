package com.hyjf.admin.manager.content.operationreport;

import java.io.Serializable;

import com.hyjf.mybatis.model.auto.OperationReport;
import com.hyjf.mybatis.model.auto.OperationReportActivity;
import com.hyjf.mybatis.model.auto.TenthOperationReport;
import com.hyjf.mybatis.model.auto.UserOperationReport;

/**
 * 运营报告下拉框
 */
public class OperationSelectBean implements Serializable {
	
	private  String code;
	private  String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

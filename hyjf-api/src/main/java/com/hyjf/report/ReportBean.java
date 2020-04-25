package com.hyjf.report;

import com.hyjf.mybatis.model.auto.*;

import java.io.Serializable;

/**
 * 运营报告详情页面共同字段
 */
public class ReportBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private OperationReport operationReport;
	private OperationReportActivity operationReportActivity;
	private TenthOperationReport tenthOperationReport;
	private UserOperationReport userOperationReport;
	private MonthlyOperationReport monthlyOperationReport;
	private QuarterOperationReport quarterOperationReport;
	private HalfYearOperationReport halfYearOperationReport;
	public OperationReport getOperationReport() {
		return operationReport;
	}
	public void setOperationReport(OperationReport operationReport) {
		this.operationReport = operationReport;
	}
	public OperationReportActivity getOperationReportActivity() {
		return operationReportActivity;
	}
	public void setOperationReportActivity(
			OperationReportActivity operationReportActivity) {
		this.operationReportActivity = operationReportActivity;
	}
	public TenthOperationReport getTenthOperationReport() {
		return tenthOperationReport;
	}
	public void setTenthOperationReport(TenthOperationReport tenthOperationReport) {
		this.tenthOperationReport = tenthOperationReport;
	}
	public UserOperationReport getUserOperationReport() {
		return userOperationReport;
	}
	public void setUserOperationReport(UserOperationReport userOperationReport) {
		this.userOperationReport = userOperationReport;
	}

	public MonthlyOperationReport getMonthlyOperationReport() {
		return monthlyOperationReport;
	}

	public void setMonthlyOperationReport(MonthlyOperationReport monthlyOperationReport) {
		this.monthlyOperationReport = monthlyOperationReport;
	}

	public QuarterOperationReport getQuarterOperationReport() {
		return quarterOperationReport;
	}

	public void setQuarterOperationReport(QuarterOperationReport quarterOperationReport) {
		this.quarterOperationReport = quarterOperationReport;
	}

	public HalfYearOperationReport getHalfYearOperationReport() {
		return halfYearOperationReport;
	}

	public void setHalfYearOperationReport(HalfYearOperationReport halfYearOperationReport) {
		this.halfYearOperationReport = halfYearOperationReport;
	}
}

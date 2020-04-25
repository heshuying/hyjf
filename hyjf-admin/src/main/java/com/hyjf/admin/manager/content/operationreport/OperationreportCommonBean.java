package com.hyjf.admin.manager.content.operationreport;

import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.report.OperationReportActivityCustomize;

import java.io.Serializable;
import java.util.List;

/**
 * 运营报告详情bean
 */
public class OperationreportCommonBean implements Serializable {
    //运营报告的id
    private String operationReportId;
    private static final long serialVersionUID = 1L;
    //运营报告
    private OperationReport operationReport;
    //运营报告活动
    private OperationReportActivity operationReportActivity;
    //运营报告十大出借
    private TenthOperationReport tenthOperationReport;
    //运营报告用户分析
    private UserOperationReport userOperationReport;
    //月度运营报告
    private MonthlyOperationReport monthlyOperationReport;
    //季度运营报告
    private QuarterOperationReport quarterOperationReport;
    //半年度运营报告
    private HalfYearOperationReport halfYearOperationReport;
    //年度运营报告
    private YearOperationReport yearOperationReport;
    //精彩活动
    private List<OperationReportActivityCustomize> wonderfulActivities;
    //足迹
    private List<OperationReportActivityCustomize> footprints;
    //体验优化
    private List<OperationReportActivityCustomize> goodExperiences;
    public String getOperationReportId() {
        return operationReportId;
    }

    public void setOperationReportId(String operationReportId) {
        this.operationReportId = operationReportId;
    }
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

    public void setMonthlyOperationReport(
            MonthlyOperationReport monthlyOperationReport) {
        this.monthlyOperationReport = monthlyOperationReport;
    }

    public List<OperationReportActivityCustomize> getFootprints() {
        return footprints;
    }

    public void setFootprints(List<OperationReportActivityCustomize> footprints) {
        this.footprints = footprints;
    }

    public List<OperationReportActivityCustomize> getGoodExperiences() {
        return goodExperiences;
    }

    public void setGoodExperiences(
            List<OperationReportActivityCustomize> goodExperiences) {
        this.goodExperiences = goodExperiences;
    }

    public QuarterOperationReport getQuarterOperationReport() {
        return quarterOperationReport;
    }

    public void setQuarterOperationReport(
            QuarterOperationReport quarterOperationReport) {
        this.quarterOperationReport = quarterOperationReport;
    }

    public HalfYearOperationReport getHalfYearOperationReport() {
        return halfYearOperationReport;
    }

    public void setHalfYearOperationReport(
            HalfYearOperationReport halfYearOperationReport) {
        this.halfYearOperationReport = halfYearOperationReport;
    }

    public YearOperationReport getYearOperationReport() {
        return yearOperationReport;
    }

    public void setYearOperationReport(YearOperationReport yearOperationReport) {
        this.yearOperationReport = yearOperationReport;
    }

    public List<OperationReportActivityCustomize> getWonderfulActivities() {
        return wonderfulActivities;
    }

    public void setWonderfulActivities(List<OperationReportActivityCustomize> wonderfulActivities) {
        this.wonderfulActivities = wonderfulActivities;
    }
}

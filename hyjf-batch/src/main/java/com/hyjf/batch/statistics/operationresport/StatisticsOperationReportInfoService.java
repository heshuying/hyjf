package com.hyjf.batch.statistics.operationresport;/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */

/**
 * @author yinhui
 * @version StatisticsOperationReportInfoService, v0.1 2018/6/22 16:34
 */
public interface StatisticsOperationReportInfoService {

    //保存月度运营报告
    public void setMonthReport(String year, String month) throws Exception;

    //保存季度运营报告
    public void setQuarterReport(String year, String month) throws Exception;

    //保存半年运营报告
    public void setHalfYearReport(String year, String month) throws Exception;

    //保存全年度运营报告
    public void setYearReport(String year, String month) throws Exception;
}

package com.hyjf.web.report;

import com.hyjf.web.BaseDefine;

public class ReportDefine extends BaseDefine {

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/report";
    
    /**  action @RequestMapping值 */
	public static final String REPORT_INFO = "/reportInfo";
    /**  action @RequestMapping值 */
    public static final String REPORT_LIST = "/reportList";

    /** 进入月度 @RequestMapping值 */
    public static final String INIT_MONTH_REPORT = "/initMonthReport";

    public static final String MONTH_LIST_PTAH = "report/report-month";
    public static final String HALF_LIST_PTAH = "report/report-half";
    public static final String QUARTER_LIST_PTAH = "report/report-quarter";
    public static final String YEAR_LIST_PTAH = "report/report-year";
    
}

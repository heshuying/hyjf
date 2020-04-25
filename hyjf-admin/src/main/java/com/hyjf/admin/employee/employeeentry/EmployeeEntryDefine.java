package com.hyjf.admin.employee.employeeentry;

public class EmployeeEntryDefine {
	/** CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/employee/employeeentry";

	/** 路径 */
	public static final String LIST_PATH = "employee/employeeentry/employeeentry_li";
	/** 列表画面 @RequestMapping值,所有员工列表 */
	public static final String LIST = "/employeeentry_list";
	/** FROM */
	public static final String EMPLOYEEENTRY_FORM = "employeeentryForm";
	
	
	/** 路径 */
	public static final String DETAIL_PATH = "employee/employeeentry/employeeentry_detail";
	/** 列表画面 @RequestMapping值，员工详细 */
	public static final String DETAIL = "/detail";

	/** 路径 */
	public static final String APPROVAL_PATH = "employee/employeeentry/employeeentry_approval";
	/** 列表画面 @RequestMapping值，离职审批 */
	public static final String APPROVAL = "/approval";
	
	public static final String ENTRYPASS_PATH = "employee/employeeentry/pass";
	public static final String ENTRYPASS = "/entrypass";
	
	/** ACTION*/
	/** 查询数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
	
}

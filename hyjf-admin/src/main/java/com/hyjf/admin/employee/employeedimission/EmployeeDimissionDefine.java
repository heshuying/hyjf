package com.hyjf.admin.employee.employeedimission;

public class EmployeeDimissionDefine {
	/** CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/employee/employeedimission";
	
	/** 路径 */
	public static final String LIST_PATH = "employee/employeedimission/employeedimission_li";
	/** 列表画面 @RequestMapping值,所有离职员工列表 */
	public static final String LIST = "/employeedimission_list";
	/** FROM */
	public static final String EMPLOYEEDIMISSION_FORM = "employeedimissionForm";
	
	/** 路径 */
	public static final String DETAIL_PATH = "employee/employeedimission/employeedimission_detail";
	/** 列表画面 @RequestMapping值，员工详细 */
	public static final String DETAIL = "/detail";
	
	/** 路径 */
	public static final String APPROVAL_PATH = "employee/employeedimission/employeedimission_approval";
	/** 列表画面 @RequestMapping值，离职审批 */
	public static final String APPROVAL = "/approval";
	
	public static final String DIMISSIONPASSL_PATH = "employee/employeedimission/pass";
	public static final String DIMISSIONPASS = "/dimissionpass";
	
	/** ACTION*/
	/** 查询数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
    
	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	
}

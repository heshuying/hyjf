package com.hyjf.api.server.group;

/**
 * @author lm
 * @version OrganizationStructureServer, v0.1 2018/1/17 9:31 第三方查询出借信息
 */
public class OrganizationStructureDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/server/company";
	/** 获取集团组织架构信息 @RequestMapping值 */
	public static final String ORGANIZATION_LIST = "/syncCompanyInfo";
	/** 获取员工信息信息 @RequestMapping值 */
	public static final String EMPINFO_LIST = "/syncEmpInfo";
}

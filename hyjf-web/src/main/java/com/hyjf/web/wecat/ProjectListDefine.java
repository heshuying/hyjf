/**
 * Description:汇直投常量定义
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.wecat;

import com.hyjf.web.BaseDefine;

public class ProjectListDefine extends BaseDefine {

	/** 汇直投数据统计 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/wecat/project";

	/** 汇直投列表 @RequestMapping值 */
	public static final String PROJECT_LIST_ACTION = "projectlist";
	/** 汇直投详情 @RequestMapping值 */
	public static final String PROJECT_DETAIL_ACTION = "getProjectDetail";
	
	/** 类名 */
	public static final String THIS_CLASS = ProjectListController.class.getName();

}

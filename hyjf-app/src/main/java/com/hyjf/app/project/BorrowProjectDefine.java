/**
 * Description:获取指定的项目类型的项目常量定义
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.app.project;

import com.hyjf.app.BaseDefine;

public class BorrowProjectDefine extends BaseDefine {

	/** 指定类型的项目 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/borrow";
	/** 项目详情 @RequestMapping值 */
	public static final String PROJECT_DETAIL_ACTION = "/{borrowId}";
	
	//成功编码
	public static final String SUCCESS = "000";
	//成功
	public static final String SUCCESS_MSG = "成功";
	//失败编码
	public static final String FAIL = "99";
	//失败
	public static final String FAIL_MSG = "请登录用户";
	//失败
	public static final String FAIL_BORROW_MSG = "未查到标信息";

	/** 出借记录 @RequestMapping值 */
	public static final String PROJECT_INVEST_ACTION = "/{borrowId}/investRecord";

	/** 项目详情 @Path值 */
	public static final String ERROR_PTAH = "error";

	/** 类名 */
	public static final String THIS_CLASS = ProjectController.class.getName();
    
    /** add by nxl  汇计划二期前端优化  新加承接记录列表显示_承接记录 @RequestMapping值 */
    public static final String PROJECT_UNDERTAKE_ACTION = "/{borrowId}/getBorrowUndertake";

}

/**
 * Description:我的出借常量定义
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.user.project;

import com.hyjf.web.BaseDefine;

public class InvestProjectDefine extends BaseDefine {

	/** 我的出借数据统计 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/statistics";

	/** 我的出借项目列表@RequestMapping值 */
	public static final String PROJECT_LIST_ACTION = "projectlist";
	
	/** 我的出借项目详情企业或者个人 @RequestMapping值 */
	public static final String PROJECT_DETAIL_ACTION = "detail";
	
	/** 我的出借项目出借列表@RequestMapping值 */
	public static final String PROJECT_INVEST_LIST_ACTION = "investlist";
	
	/** 我的出借汇消费项目列表 @RequestMapping值 */
	public static final String PROJECT_CONSUME_LIST_ACTION = "consumelist";
	
	/** 我的出借项目用户协议 @RequestMapping值 */
	public static final String PROJECT_USER_INVEST_LIST_ACTION = "userinvestlist";
	
	/** 我的出借项目还款信息 @RequestMapping值 */
	public static final String PROJECT_REPAY_LIST_ACTION = "repaylist";
	
	/** 统计类名 */
	public static final String THIS_CLASS= InvestProjectController.class.getName();
	
}

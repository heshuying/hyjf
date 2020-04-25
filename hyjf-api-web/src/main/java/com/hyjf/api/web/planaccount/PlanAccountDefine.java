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
package com.hyjf.api.web.planaccount;

import com.hyjf.base.bean.BaseDefine;

public class PlanAccountDefine extends BaseDefine {

	/** 我的计划数据统计 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/planaccount";
	/** 我的计划项目列表@RequestMapping值 */
	public static final String PLAN_LIST_ACTION = "/getPlanList";
	/** 统计类名 */
	public static final String THIS_CLASS = PlanAccountController.class.getName();

}

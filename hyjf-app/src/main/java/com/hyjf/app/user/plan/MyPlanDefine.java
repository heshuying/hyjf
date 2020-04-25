package com.hyjf.app.user.plan;

import com.hyjf.app.BaseDefine;
import com.hyjf.app.agreement.AgreementDefine;
import com.hyjf.app.hjhasset.HjhProjectDefine;
import com.hyjf.common.util.CustomConstants;

/**
 * @author xiasq
 * @version MyPlanDefine, v0.1 2017/11/9 11:10 访问路径定义
 */
public class MyPlanDefine extends BaseDefine {

	/** 公共请求路径 */
	public static final String REQUEST_MAPPING = "/user/plan";

	/** 获取我的计划列表 */
	public static final String MYPLAN_LIST_ACTION = "/getMyPlanList";

	/** 获取我的计划详情 */
	public static final String MYPLAN_DETAIL_ACTION = "/{orderId}";

	/** @RequestMapping值 */
	public static final String MYPLAN_LIST_REQUEST = REQUEST_HOME + REQUEST_MAPPING + MYPLAN_LIST_ACTION;

	/** @RequestMapping值 */
	public static final String MYPLAN_DETAIL_REQUEST = CustomConstants.HOST + REQUEST_MAPPING;
}

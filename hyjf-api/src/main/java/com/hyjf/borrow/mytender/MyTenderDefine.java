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
package com.hyjf.borrow.mytender;

import com.hyjf.base.bean.BaseDefine;

public class MyTenderDefine extends BaseDefine {

	/** 我的出借 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/invest";

	/** 我的出借项目列表(回款中) @RequestMapping值 */
	public static final String REPAY_LIST_ACTION = "/getRepayList";
	
	/** 我的出借项目列表(出借中) @RequestMapping值 */
	public static final String INVEST_LIST_ACTION = "/getInvestList";
	
	/** 我的出借项目列表(已回款) @RequestMapping值 */
	public static final String ALREADY_REPAY_LIST_ACTION = "/getAlreadyRepayList";

	/** (回款中)还款计划 @RequestMapping值 */
	public static final String REPAY_PLAN_LIST_ACTION = "/getRepayPlan";
	
	/** (回款中)合同@RequestMapping值 */
	public static final String REPAY_CONTACT_ACTION = "/getRepayContact";
	
	/** 项目详情 @Path值 */
	public static final String PROJECT_HXF_CONTRACT_PTAH = "contract/hxfcontract";
	
	/** 项目详情 @Path值 */
	public static final String PROJECT_HZT_CONTRACT_PTAH = "contract/hztcontract";;
	
	
}

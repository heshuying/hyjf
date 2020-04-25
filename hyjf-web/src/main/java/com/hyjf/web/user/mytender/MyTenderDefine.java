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
package com.hyjf.web.user.mytender;

import com.hyjf.web.BaseDefine;

public class MyTenderDefine extends BaseDefine {

	/** 我的出借数据统计 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/mytender";

	/** 我的出借项目页面@RequestMapping值 */
	public static final String TO_MYTENDER_ACTION = "init";
	/** 我的出借项目列表@RequestMapping值 */
	public static final String PROJECT_LIST_ACTION = "projectlist";

	/** 我的出借画面 路径 */
	public static final String PROJECT_LIST_PATH = "user/mytender/mytenderlist";
	/** 出借协议画面 路径 */
	public static final String XY_PATH = "user/mytender/xieyi";

	/** 嘉诺融通宝出借协议画面 路径 */
	public static final String RTB_XY_PATH = "user/mytender/rtb-agreement";
	/** 中商储融通宝出借协议画面 路径 */
	public static final String RTB_XY_PATH_ZSC = "user/mytender/rtb-agreement-zsc";

	/** 我的出借项目页面@RequestMapping值 */
	public static final String PDF_EXPORT_ACTION = "createAgreementPDF";

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

	/** 我的优惠券出借项目还款信息 @RequestMapping值 */
	public static final String PROJECT_COUPON_REPAY_LIST_ACTION = "couponrepaylist";

	/** 获取融通宝额外收益 @RequestMapping值 */
	public static final String PROJECT_RTB_EXTRA_ACTION = "getRTBExtraEarn";

	/** 统计类名 */
	public static final String THIS_CLASS = MyTenderController.class.getName();

}

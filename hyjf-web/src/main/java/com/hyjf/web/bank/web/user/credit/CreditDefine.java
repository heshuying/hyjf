package com.hyjf.web.bank.web.user.credit;

import com.hyjf.web.BaseDefine;

public class CreditDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/user/credit";

	/***/
	public static final String USER_CREDIT_LIST = "usercreditlist";
	public static final String USER_CREDIT_LIST_PATH = "/bank/user/credit/usercreditlist";
	/** 我要转让列表页 */
	public static final String USER_CAN_CREDIT_LIST = "userCanCreditList";
	/** 我要债转页面路径 */
	public static final String USER_CAN_CREDIT_LIST_PATH = "/bank/user/credit/usercancreditlist";
	/** 我要转让列表数据 */
	public static final String USER_CAN_CREDIT_LIST_DATA = "canCreditListData";
	/** 用户中心出借可债转资源列表 */
	public static final String TENDER_TO_CREDIT_LIST = "tendertocreditlist";
	/** 用户中心出借是否可债转 */
	public static final String TENDER_ABLE_TO_CREDIT = "tenderabletocredit";
	/** 用户中心出借可债转资源详细 */
	public static final String TENDER_TO_CREDIT_DETAIL = "tendertocreditdetail";
	public static final String TENDER_TO_CREDIT_DETAIL_PATH = "/bank/user/credit/transfer-detail";
	/** 用户中心 债转详细预计服务费计算 */
	public static final String EXCEPT_CREDIT_FEE = "expectcreditfee";
    /** 校验是否可发起债转 */
    public static final String CHECK_CAN_CREDIT = "checkCanCredit";
	/** 用户中心出借可债转保存 */
	public static final String TENDER_TO_CREDIT_SAVE = "savetendertocredit";
	/** 用户中心提交债转跳转页面 */
	public static final String TENDER_TO_CREDIT_RESULT = "tendertocreditresult";
	/** 用户中心提交债转跳转路径(新版) */
	public static final String TENDER_TO_CREDIT_RESULT_PATH = "/bank/user/credit/creditsuccess";
	/** 用户中心提交债转跳转错误画面(新版) */
	public static final String CREDIT_ERROR_PATH = "/bank/user/credit/crediterror";
	/** 用户中心转让中债转列表资源 */
	public static final String CREDIT_INPROGRESS_LIST = "creditinprogresslist";
	/** 用户中心转让记录 */
	public static final String CREDIT_RECORD_LIST = "creditrecordlist";
	/** 用户中心查询已完成债转列表资源 */
	public static final String CREDIT_STOP_LIST = "creditstoplist";
	/** 用户中心查询已承接债转列表资源 */
	public static final String CREDIT_ASSIGN_LIST = "creditassignlist";
	/** 用户中心查询已承接债转的出借列表资源 */
	public static final String CREDIT_ASSIGN_TENDER_LIST = "creditassigntenderlist";
	/** 用户中心查询已承接债转的还款计划列表资源 */
	public static final String CREDIT_REPAY_PLAN_LIST = "creditrepayplanlist";

	/** 前端Web页面查询汇转让数据列表(包含查询条件) */
	public static final String WEB_CREDIT_LIST = "webcreditlist";
	public static final String WEB_CREDIT_PAGE_INIT = "initWebCreditPage";
	public static final String WEB_CREDIT_LIST_PATH = "/bank/user/credit/webcreditlist";
	/** 前端Web页面出借可债转详细(包含查询条件) */
	public static final String WEB_CREDIT_TENDER = "webcredittender";
	/** 前端Web页面出借可债转详细(包含查询条件) */
	public static final String WEB_CREDIT_DETAIL_OLD_PATH = "bank/user/credit/product-transfer-detail-old";
	public static final String WEB_CREDIT_DETAIL_NEW_PATH = "bank/user/credit/product-transfer-detail";
	public static final String WEB_CREDIT_TENDER_PATH = "/bank/user/credit/webcredittender";
	public static final String WEB_CREDIT_REPAY_PAGE = "webcreditrepaypage";
	public static final String WEB_CREDIT_TENDER_PAGE = "webcredittenderpage";
	/** 前端Web页面出借可债转输入出借金额后收益提示(包含查询条件) */
	public static final String WEB_CHECK_CREDIT_TENDER_ASSIGN = "webcheckcredittenderassign";
	/** 前端Web页面出借确定认购提交 */
	public static final String WEB_SUBMIT_CREDIT_TENDER_ASSIGN = "websubmitcredittenderassign";

	/** 请求汇付返回页面 */
	public static final String RETURN_MAPPING = "return";
	/** 汇付回调地址 */
	public static final String CALLBACK_MAPPING = "callback";
	/** 承接债转成功返回画面(新版) */
	public static final String WEB_CREDIT_TENDER_SUCCESS_PATH = "/bank/user/credit/credittendersuccess";
	/** 债转失败页面(新版) */
	public static final String WEB_CREDIT_TENDER_FAILED_PATH = "/bank/user/credit/credittendererror";
	/** 债转错误页面 */
	public static final String WEB_CREDIT_TENDER_ERROR_PATH = "/bank/user/credit/webcrediterror";

	/** 用户中心出借可债转发送短信验证码 */
	public static final String TENDER_TO_CREDIT_SEND_CODE = "sendcode";
	/** 用户中心出借可债转检查短信验证码 */
	public static final String TENDER_TO_CREDIT_CHECK_CODE = "checkcode";

	/** 用户手机号码 */
	public static final String MOBILE = "手机号码";
	/** 手机号码不能为空 */
	public static final String MOBILE_ERROR = "mobile";
	/** 短信验证码 */
	public static final String CODE = "短信验证码";
	/** 短信验证码不能为空 */
	public static final String CODE_ERROR = "code";
	/** 必须 */
	public static final String ERROR_TYPE_REQUIRED = "required";
	/** 重复 */
	public static final String ERROR_TYPE_REPEAT = "repeat";
	/** 其他 */
	public static final String ERROR_TYPE_OTHERS = "other";
	/** 其他 */
	public static final String ERROR_MAXCOUNT_OTHERS = "maxCount";
	public static final String ERROR_INTERVAL_TIME_OTHERS = "intervaltime";
	public static final String ERROR_NOT_EXIST = "notexist";

	/** 类名 */
	public static final String THIS_CLASS = CreditController.class.getName();

	public static final String WEB_CREDIT_INFO = "/bank/user/credit/webcreditinfo";

	/** 前端Web页面出借可债转输入出借金额后计算收益 */
	public static final String WEB_CREDIT_TENDER_INTEREST = "webCreditTenderInterest";
	/** 用户中心查询已承接债转的出借列表资源 */
	public static final String WEB_CREDIT_TENDER_LIST = "webCreditTenderList";
}

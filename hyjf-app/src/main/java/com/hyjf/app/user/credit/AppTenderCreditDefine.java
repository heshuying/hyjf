package com.hyjf.app.user.credit;

import com.hyjf.app.BaseDefine;

public class AppTenderCreditDefine extends BaseDefine {

    /** Controller @RequestMapping */
    public static final String REQUEST_MAPPING = "/tender/credit";

    /** 类名 */
    public static final String THIS_CLASS = AppTenderCreditController.class.getName();

    /** 类名 */
    public static final String TRANSFER_CLASS = TransferController.class.getName();

    /** 汇转让出借列表 @RequestMapping */
    public static final String PROJECT_LIST_ACTION = "/getCreditList";

    /** 汇转让出借详情 @RequestMapping */
    public static final String PROJECT_DETAIL_ACTION = "/getCreditDetail";

    /** 项目信息 @RequestMapping值 */
    public static final String PROJECT_INFO_ACTION = "/getProjectInfo";

    /** 出借记录 @RequestMapping值 */
    public static final String PROJECT_INVEST_ACTION = "/getProjectInvest";

    /** 汇转让出借详情 @RequestMapping */
    public static final String PROJECT_DETAIL_PTAH = "hzr/hzr_project_detail";

    /** 我的债权可债转列表 @RequestMapping */
    public static final String TENDER_TO_CREDIT_LIST = "/tenderToCreditList";
    
    /** 转让设置画面 @RequestMapping */
    public static final String TENDER_TO_CREDIT_DETAIL = "/tenderToCreditDetail";
    
    /** 转让设置画面路径 @RequestMapping */
    public static final String TENDER_TO_CREDIT_DETAIL_PATH = "hzr/usercreditdetail";
    
    /**债转提交保存 @RequestMapping */
    public static final String TENDER_TO_CREDIT_SAVE = "/saveTenderToCredit";
    
    /** 用户中心提交债转跳转页面 */
    public static final String TENDER_TO_CREDIT_RESULT = "/tenderToCreditResult";
    
    /** 用户中心提交债转跳转路径 */
    public static final String TENDER_TO_CREDIT_RESULT_PATH = "hzr/credit_success";
    
    /***/
    public static final String TENDER_TO_CREDIT_ERROR_PATH = "hzr/credit_error";
    
    /** 已承接债转列表 @RequestMapping */
    public static final String CREDIT_ASSIGN_LIST = "/creditAssignList";

    /** 承接详情画面 @RequestMapping */
    public static final String CREDIT_ASSIGN_DETAIL = "/creditAssignDetail";

    /** 承接详情画面路径 @RequestMapping */
    public static final String CREDIT_ASSIGN_DETAIL_PATH = "hzr/credit_assign_detail";

    /** 获取还款计划 @RequestMapping */
    public static final String CREDIT_REPAY_PLAN_LIST = "/creditRepayPlanList";

    /** 转让记录转列表 @RequestMapping */
    public static final String CREDIT_RECORD_LIST = "/searchCreditRecordList";

    /** 获取转让记录详情 @RequestMapping */
    public static final String CREDIT_RECORD_DETAIL = "/getCreditRecordDetail";

    /** 转让记录详情画面路径 @RequestMapping */
    public static final String CREDIT_RECORD_DETAIL_PATH = "hzr/credit_record_detail";

    /** 转让记录明细列表 @RequestMapping */
    public static final String CREDIT_RECORD_DETAIL_LIST = "/getCreditRecordDetailList";

    /** 债转协议 @RequestMapping */
    public static final String CREDIT_CONTRACT = "/userCreditContract";
    
    /** 债转协议路径  @RequestMapping*/
    public static final String CREDIT_CONTRACT_PATH = "hzr/usercreditcontract";
    
    /** 用户中心出借可债转发送短信验证码 */
    public static final String TENDER_TO_CREDIT_SEND_CODE = "sendcode";
    
    /** 用户中心出借可债转检查短信验证码 */
    public static final String TENDER_TO_CREDIT_CHECK_CODE = "checkcode";
    
    /** 返回状态失败 */
    public static final String RESULT_ERROR = "2";
    
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
    
    /** 请求汇付返回页面 */
    public static final String RETURN_MAPPING = "return";
    /** 汇付回调地址 */
    public static final String CALLBACK_MAPPING = "callback";

    /** 用户登录url @Url值 */
    public static final String USER_LOGIN_URL = "://jumpLogin";

    /** 用户开户url @Url值 */
    public static final String USER_OPEN_URL = "://jumpH5Encode";

    /** 用户出借url @Url值 */
    public static final String USER_INVEST_URL = "://jumpPayForProject";

    /** 项目详情 @Path值 */
    public static final String ERROR_PTAH = "error";

    /** 项目信息 @RequestMapping值 */
    public static final String PROJECT_DETAIL_CONSTANT_TABNAME_PROJECTINFO = "债转说明";

    /** 出借记录@RequestMapping值 */
    public static final String PROJECT_DETAIL_CONSTANT_TABNAME_INVEST = "出借记录";

    /** 债转转让记录 @RequestMapping值 */
    public static final String TRANSFER_MAPPING = "/user/transfer";

    public static final String TRANSFER_ACTION = "/{transferId}";

    public static final String SUCCESS = "000";

    public static final String SUCCESS_MSG = "成功";

}

package com.hyjf.app.user.credit;

import com.hyjf.app.BaseDefine;

public class AppTenderCreditBorrowDefine extends BaseDefine {

    /** Controller @RequestMapping */
    public static final String REQUEST_MAPPING = "/user/borrow";

    /** 类名 */
    public static final String THIS_CLASS = AppTenderCreditBorrowDefine.class.getName();

    /** 汇转让出借列表 @RequestMapping */
    public static final String PROJECT_LIST_ACTION = "/getCreditList";

    /** 汇转让出借详情 @RequestMapping */
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



    
    /** 转让设置画面 @RequestMapping */
    public static final String TENDER_TO_CREDIT_DETAIL = "/transfer/setting";
    
    /**债转提交保存 @RequestMapping */
    public static final String TENDER_TO_CREDIT_SAVE = "/saveTenderToCredit";
    
    /** 用户中心提交债转跳转页面 */
    public static final String TENDER_TO_CREDIT_RESULT = "/tenderToCreditResult";

    /** 用户中心出借可债转发送短信验证码 */
    public static final String TENDER_TO_CREDIT_SEND_CODE = "/sendcode";
    
    
    /** 债权转让申请结果页*/
    public static final String JUMP_HTML_SUCCESS_PATH = "/user/borrow/{creditNid}/transfer/result/success";
    /** 债权转让申请结果页*/
    public static final String JUMP_HTML_ERROR_PATH = "/user/borrow/{creditNid}/transfer/result/failed";
    /** 债权转让申请结果页*/
    public static final String JUMP_HTML_HANDLING_PATH = "/user/borrow/{creditNid}/transfer/result/handling";
    
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
    

    
}

package com.hyjf.wechat.controller.hjh;

import com.hyjf.wechat.base.BaseDefine;

/**
 * Created by cuigq on 2018/2/26.
 */
public class WxHjhPlanDefine  extends BaseDefine{


    /**
     * 统一包访问路径 @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "/wx/bank/wechat/plan";

    /**
     * 汇计划项目详情 @RequestMapping值
     */
    public static final String HJH_PLAN_DETAIL_ACTION = "/{planId}";

    /**
     * 汇计划加入记录 @RequestMapping值
     */
    public static final String HJH_PLAN_ACCEDE_ACTION = "/{planId}/investRecord";

    /**
     * 标的组成 @Path值
     */
    public static final String HJH_PLAN_BORROW_ACTION = "/{planId}/borrowComposition";

    /**
     * 获取汇计划出借url
     */
    public static final String TENDER_URL_ACTION = "/getHJHTenderUrl";

    /**
     * 
     * 加入计划
     */
	public static final String INVEST_ACTION = "/tender";
	
	/**本地跳转的页面*/
    public static final String JUMP_HTML = "/jumpHTML";
    /**前端的加入计划成功url*/
	public static final String JUMP_HJH_TENDER_SUCCESS_URL = "/plan/{planId}/result/success";
	/**前端的加入计划失败url*/
	public static final String JUMP_HJH_TENDER_ERROR_URL = "/plan/{planId}/result/failed";

}

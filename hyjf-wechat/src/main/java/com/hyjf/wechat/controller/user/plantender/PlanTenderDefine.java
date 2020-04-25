package com.hyjf.wechat.controller.user.plantender;

import com.hyjf.wechat.base.BaseDefine;

/**
 * 
 * 计划类出借
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月1日
 * @see 上午10:22:15
 */
public class PlanTenderDefine extends BaseDefine {

    public static final String CONTROLLER_NAME="PlanTenderController";
    
    /** 计划类出借 @RequestMapping值 /wx/agreement */
    public static final String REQUEST_MAPPING = "/wx/plantender";
    /** 计划类获取出借信息 @RequestMapping值 /doLogin */
    public static final String DOLOGIN_MAPPING = "/getInvestInfo";

}

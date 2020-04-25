/**
 * Description：用户预约管理常量定义
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.user.appoint;

import com.hyjf.web.BaseDefine;

public class AppointDefine extends BaseDefine {

	/** 用户预约管理 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/user/appoint";
	
	/** 预约管理  action值 */
	public static final String INIT_APPOINT_ACTION = "initAppoint";
	
	/** 预约规则  action值 */
	public static final String INIT_INTRODUCE_ACTION = "appointIntroduce";
	
	/** 预约管理  action值 */
	public static final String INIT_PUNISH_ACTION = "appointPunish";
	
	/** 预约列表  action值 */
	public static final String APPOINT_LIST_ACTION = "appointList";
	
	/** 取消预约  action值 */
	public static final String CANCEL_APPOINT_ACTION = "cancelAppoint";

	/** 交易明细页面地址 @Path值 */
    public static final String APPOINT_LIST_PTAH = "user/appoint/appointList";
    
    /** 预约规则页面地址 @Path值 */
    public static final String APPOINT_INTRODUCE_PTAH = "user/appoint/appointIntroduce";
    
    /** 预约处罚规则页面地址 @Path值 */
    public static final String APPOINT_PUNISH_PTAH = "user/appoint/appointPunish";
    
	/** 类名 */
	public static final String THIS_CLASS = AppointController.class.getName();

}

/**
 * Description:即信查询报文常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年07月07日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.web.api.aems.bind;

import com.hyjf.web.api.base.ApiBaseDefine;

/**
 * @author liubin
 */
public class AemsApiUserBindDefine extends ApiBaseDefine {

    /** 汇晶社查询用户信息@RequestMapping值 */
    public static final String REQUEST_MAPPING = "/api/aems";
   
    /** 跳转登陆授权页面@RequestMapping值 */
    public static final String BIND_API_MAPPING = "bindApi";
    
    /** 授权按钮@RequestMapping值 */
    public static final String BIND_MAPPING = "bind";

    
    /** 用户登陆授权页面 */
    public static final String FAST_AUTH_LOGIN_PTAH = "api/user/auth/fast-authorize-login";

    /** 纳觅财富机构编号 */
    public static final String NMCF_PID = "11000002";
    

    
}

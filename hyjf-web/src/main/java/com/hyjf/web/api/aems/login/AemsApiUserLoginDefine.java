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

package com.hyjf.web.api.aems.login;

import com.hyjf.web.api.base.ApiBaseDefine;

public class AemsApiUserLoginDefine extends ApiBaseDefine {

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/api/aems";
   
    /** 第三方退出登录 @RequestMapping值 */
    public static final String LOGOUT_THIRDAPI = "/thirdLogoutApi";
    
    /** 第三方登录 @RequestMapping值 */
    public static final String LOGIN_THIRDAPI = "/thirdLoginApi";
    
    /** 第三方退出登录 @RequestMapping值 */
    public static final String LOGOUT_THIRDAPI_NOTIFY = "/thirdLogout";
    
    /** 第三方登录 @RequestMapping值 */
    public static final String LOGIN_THIRDAPI_NOTIFY = "/thirdLogin";

    /** 纳觅财富第三方登录 @RequestMapping值 */
    public static final String NMCF_LOGIN = "/nmcfThirdLogin";

}

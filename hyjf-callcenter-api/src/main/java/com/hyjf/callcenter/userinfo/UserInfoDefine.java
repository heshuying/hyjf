/**
 * Description:按照用户名/手机号查询会员资料用常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年07月07日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.callcenter.userinfo;

import com.hyjf.callcenter.base.BaseDefine;

public class UserInfoDefine extends BaseDefine {

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/user";
   
    /** 按照用户名/手机号查询会员资料 */
    public static final String INIT_USER_INFO_ACTION = "getUserInfo";
    
    /** 查询呼叫中心未分配客服的用户 */
    public static final String INIT_NO_SERVIC_USERS_ACTION = "getNewUsers";
    
    /** 更新呼叫中心用户分配客服的状态 */
    public static final String INIT_SERVED_USERS_ACTION = "setServedUsers";
}

package com.hyjf.callcenter.recharge;

import com.hyjf.callcenter.base.BaseDefine;

/**
 * Description:按照用户名/手机号查询充值明细
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 *           Created at: 2017年07月07日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */
public class SrchRechargeInfoDefine extends BaseDefine {
	
    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/recharge";
    /** 按照用户名/手机号查询充值明细 */
    public static final String GET_CONTENT_INFO_MAPPING = "/srchRechargeInfo";
    
    /** 呼叫中心接口用签验密钥(暂时) */
    public static final String RELEASE_CALLCENTER_MSG_ACCESSKEY = "release.callcenter.msg.accesskey";//需要替换

}

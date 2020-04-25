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

package com.hyjf.jixin.chinapnr;

import com.hyjf.base.bean.BaseDefine;

public class JixinChinapnrDefine extends BaseDefine {

    /** vip @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/message";
   
    /** 根据流水号获取报文信息*/
    public static final String GET_CONTENT_INFO_MAPPING = "getMsgInfo";

    /** 根据流水号获取报文信息*/
    public static final String RELEASE_JIXIN_MSG_ACCESSKEY = "release.jixin.msg.accesskey";
}

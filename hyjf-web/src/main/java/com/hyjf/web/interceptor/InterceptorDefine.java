/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.web.interceptor;

import java.util.Arrays;
import java.util.List;

import com.hyjf.common.util.PropUtils;
import com.hyjf.web.BaseDefine;

public class InterceptorDefine extends BaseDefine {
    
    /** 服务地址 */
    public static final String[] HYJF_WEB_DOMAIN_NAMES = PropUtils.getSystem("hyjf.web.domain.name").trim().split(",");
    
    /** 服务地址集合  */
    public static final List<String> HYJF_WEB_DOMAIN_NAMES_LIST = Arrays.asList(HYJF_WEB_DOMAIN_NAMES);
    
    /** 资源版本号 */
    public static final String RESOURCE_VERSION = PropUtils.getSystem("resource.version");

}

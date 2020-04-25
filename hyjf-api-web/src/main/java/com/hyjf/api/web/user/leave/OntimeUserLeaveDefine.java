package com.hyjf.api.web.user.leave;

import com.hyjf.base.bean.BaseDefine;

public class OntimeUserLeaveDefine extends BaseDefine {

    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/userleave";

    /** 更新离职员工信息Action */
    public static final String UPDATE_ACTION = "updateleave";

    /** 类名 */
    public static final String THIS_CLASS = OntimeUserLeaveServer.class.getName();

}

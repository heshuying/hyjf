package com.hyjf.api.server.user.registeropenaccount;

import com.hyjf.base.bean.BaseDefine;

/**
 * 外部服务接口:用户注册加开户Define
 *
 * @author liuyang
 */
public class UserRegisterAndOpenAccountDefine extends BaseDefine {
    /**
     * 类名
     */
    public static final String THIS_CLASS = UserRegisterAndOpenAccountServer.class.getName();

    /** 外部服务接口:用户注册加开户 @RequestMapping */
    public static final String REQUEST_MAPPING = "/server/user/registeropenaccount";

    /** 注册开户 @RequestMapping 值 */
    public static final String REGISTER_OPENACCOUNT_ACTION = "registerAndOpenAccount";

    /** 用户注册成功状态 */
    public static final String STATUS_REGISTER_SUCCESS = "0";

    /** 用户注册失败状态 */
    public static final String STATUS_REGISTER_FAIL = "1";

    /** 用户开户成功状态 */
    public static final String STATUS_OPEN_ACCOUNT_SUCCESS = "2";

    /** 用户开户失败状态 */
    public static final String STATUS_OPEN_ACCOUNT_FAIL = "3";
}

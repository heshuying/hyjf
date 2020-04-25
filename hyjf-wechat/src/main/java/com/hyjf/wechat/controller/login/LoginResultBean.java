package com.hyjf.wechat.controller.login;

import com.hyjf.wechat.base.BaseResultBean;

/**
 * 
 * 登录返回值
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月1日
 * @see 下午4:31:02
 */
public class LoginResultBean extends BaseResultBean{

    private static final long serialVersionUID = -3296145605895692615L;

    private String userId;

    /** 用户类型 */
    private String userType;

    private String sign;
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

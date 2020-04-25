/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.api.aems.register;

import com.hyjf.base.bean.BaseBean;

/**
 * AEMS系统用户注册请求bean
 * @author liuyang
 * @version AemsUserRegisterRequestBean, v0.1 2018/9/4 11:14
 */
public class AemsUserRegisterRequestBean extends BaseBean{

    // 手机号
    private String mobile;
    // 推荐人
    private String recommended;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRecommended() {
        return recommended;
    }

    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }
}

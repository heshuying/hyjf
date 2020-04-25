/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.model.customize;

import java.io.Serializable;

/**
 * 用户信息
 * @author yinhui
 */
public class UserVO  implements Serializable {

    private static final long serialVersionUID = 1L;

    public Integer userId;//用户ID

    public Integer referrer;//推荐人ID

    public String mobile;//手机号码

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getReferrer() {
        return referrer;
    }

    public void setReferrer(Integer referrer) {
        this.referrer = referrer;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.model.customize.loginerror;

import com.hyjf.mybatis.model.auto.LoginErrorLockUser;

/**
 * @author cui
 * @version LoginErrorLockUserExtend, v0.1 2018/7/17 10:14
 */
public class LoginErrorLockUserExtend extends LoginErrorLockUser {

    private String lockTimeStr;

    public String getLockTimeStr() {
        return lockTimeStr;
    }

    public void setLockTimeStr(String lockTimeStr) {
        this.lockTimeStr = lockTimeStr;
    }
}

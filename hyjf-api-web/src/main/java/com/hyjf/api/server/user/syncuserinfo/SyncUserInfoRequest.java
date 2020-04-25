package com.hyjf.api.server.user.syncuserinfo;

import com.hyjf.base.bean.BaseBean;

/**
 * 用户信息查询Bean
 */
public class SyncUserInfoRequest extends BaseBean {

    private String accountIds;

    public String getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(String accountIds) {
        this.accountIds = accountIds;
    }
}

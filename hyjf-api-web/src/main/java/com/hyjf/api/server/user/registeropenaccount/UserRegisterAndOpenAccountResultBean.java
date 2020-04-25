package com.hyjf.api.server.user.registeropenaccount;

import com.hyjf.base.bean.BaseResultBean;

/**
 * 外部服务接口:用户注册加开户返回结果Bean
 *
 * @author liuyang
 */
public class UserRegisterAndOpenAccountResultBean extends BaseResultBean {

    // 汇盈用户ID
    private String userId;
    // 汇盈用户名
    private String userName;
    // 用户是否开户
    private String isOpenAccount;
    // 电子账户号
    private String accountId;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsOpenAccount() {
        return this.isOpenAccount;
    }

    public void setIsOpenAccount(String isOpenAccount) {
        this.isOpenAccount = isOpenAccount;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}

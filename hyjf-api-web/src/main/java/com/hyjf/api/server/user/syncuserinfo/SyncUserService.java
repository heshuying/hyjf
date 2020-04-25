package com.hyjf.api.server.user.syncuserinfo;

import com.hyjf.api.web.BaseService;
import com.hyjf.mybatis.model.auto.BankOpenAccount;

public interface SyncUserService extends BaseService {

    BankOpenAccount getUserByAccountId(String accountId);
}

package com.hyjf.api.aems.syncuserinfo;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.BankOpenAccount;

public interface AemsSyncUserService extends BaseService {

    BankOpenAccount getUserByAccountId(String accountId);
}

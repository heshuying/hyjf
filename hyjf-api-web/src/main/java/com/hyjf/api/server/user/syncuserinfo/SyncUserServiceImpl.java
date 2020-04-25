package com.hyjf.api.server.user.syncuserinfo;

import com.hyjf.api.web.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyncUserServiceImpl extends BaseServiceImpl implements SyncUserService {

    @Override
    public BankOpenAccount getUserByAccountId(String accountId) {
        BankOpenAccountExample accountExample = new BankOpenAccountExample();
        BankOpenAccountExample.Criteria crt = accountExample.createCriteria();
        crt.andAccountEqualTo(accountId);
        List<BankOpenAccount> bankAccounts = this.bankOpenAccountMapper.selectByExample(accountExample);
        if (bankAccounts != null && bankAccounts.size() == 1) {
            return bankAccounts.get(0);
        }
        return null;
    }
}

package com.hyjf.api.aems.syncuserinfo;

import com.hyjf.base.service.BaseServiceImpl;
import java.util.List;

import org.springframework.stereotype.Service;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;

@Service
public class AemsSyncUserServiceImpl extends BaseServiceImpl implements AemsSyncUserService {

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

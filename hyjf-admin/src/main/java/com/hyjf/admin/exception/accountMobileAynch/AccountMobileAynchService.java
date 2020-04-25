package com.hyjf.admin.exception.accountMobileAynch;

import com.hyjf.mybatis.model.auto.AccountMobileAynch;

import java.util.List;

/**
 * @author 李晟
 * @version AccountMobileAynchService, v0.1 2018/5/9 15:17
 */

public interface AccountMobileAynchService {

    /**
     * 检索线下修改信息同步表的数据量
     *
     * @param form
     * @return
     */
    public int countMobileAccountAynch(AccountMobileAynchBean form,int flag);

    public List<AccountMobileAynch> searchAccountMobileAynch(AccountMobileAynchBean form, int offset, int limit,int flag);

    public Integer insertAccountMobileAynch(String username,String flag);

    public boolean deleteMessage(String id);
}

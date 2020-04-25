package com.hyjf.admin.manager.config.account.accountconfig;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.MerchantAccount;;

/**
 * 
 * 账户配置Service
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月6日
 * @see 上午8:52:49
 */
public interface AccountConfigService extends BaseService {

    /**
     * 
     * 获取子账户配置列表数量
     * @author
     * @param form
     * @return
     */
    public int countSubAccountList(AccountConfigBean form);

    /**
     * 
     * 检索账户配置列表
     * @author liuyang
     * @param form
     * @return
     */
    public List<MerchantAccount> searchSubAccountList(AccountConfigBean form, int limitStart, int limitEnd);

    /**
     * 
     * 根据id检索账户配置详情
     * @author liuyang
     * @param ids
     * @return
     */
    public MerchantAccount getAccountListInfo(String ids);

    /**
     * 
     * 插入账户配置表
     * @author liuyang
     * @param form
     * @return
     */
    public int insertAccountListInfo(AccountConfigBean form);

    /**
     * 
     * 更新账户配置表
     * @author liuyang
     * @param form
     * @return
     */
    public int updateAccountListInfo(AccountConfigBean form);

    /**
     * 
     * 根据子账户名称检索
     * @author liuyang
     * @param subAccountName
     * @return
     */
    public int countAccountListInfoBySubAccountName(String ids, String subAccountName);

    /**
     * 
     * 根据子账户代号检索
     * @author liuyang
     * @param subAccountCode
     * @return
     */
    public int countAccountListInfoBySubAccountCode(String ids, String subAccountCode);
}

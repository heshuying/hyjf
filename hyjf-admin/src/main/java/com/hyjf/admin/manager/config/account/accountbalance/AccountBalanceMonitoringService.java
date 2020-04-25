package com.hyjf.admin.manager.config.account.accountbalance;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.MerchantAccount;

/**
 * 
 * 余额监控Service
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月7日
 * @see 上午8:51:23
 */
public interface AccountBalanceMonitoringService extends BaseService {
    /**
     * 
     * 获取余额配置列表数量
     * @author
     * @param form
     * @return
     */
    public int countSubAccountList(AccountBalanceMonitoringBean form);

    /**
     * 
     * 检索余额配置列表
     * @author liuyang
     * @param form
     * @return
     */
    public List<MerchantAccount> searchSubAccountList(AccountBalanceMonitoringBean form, int limitStart, int limitEnd);

    /**
     * 
     * 余额配置更新
     * @author liuyang
     * @param form
     * @return
     */
    public int updateSubAccountList(List<SubAccountListResult> updateList);
    
    
    /**
     * 
     * 编辑画面检索列表
     * @author liuyang
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     */
    public List<MerchantAccount> searchAllSubAccountList(AccountBalanceMonitoringBean form, int limitStart, int limitEnd);

}

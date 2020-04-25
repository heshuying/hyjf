package com.hyjf.admin.manager.user.bankcardlog;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BankAccountLog;
import com.hyjf.mybatis.model.auto.BankConfig;

public interface BankAccountLogService extends BaseService{

    /**
     * 获取着落页列表
     * 
     * @return
     */
    public List<BankAccountLog> getRecordList(BankAccountLogBean bean, int limitStart, int limitEnd);

    /**
     * 获取着落页列表记录数
     * 
     * @return
     */
    public Integer getRecordCount(BankAccountLogBean form);

	/**
	 * 获取银行列表
	 * 
	 * @param string
	 * @return
	 */
	public List<BankConfig> getBankcardList();
    
}
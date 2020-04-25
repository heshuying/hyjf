package com.hyjf.admin.manager.user.bankcard;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.customize.admin.AdminBankcardListCustomize;

public interface BankcardService extends BaseService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<AdminBankcardListCustomize> getRecordList(Map<String, Object> bankCardUser, int limitStart, int limitEnd);

	/**
	 * @param form
	 * @return
	 */
	public int countRecordTotal(Map<String, Object> bankCardUser);

	/**
	 * 获取银行列表
	 * 
	 * @param string
	 * @return
	 */
	public List<BankConfig> getBankcardList();
	/**
	 * 
	 * 获取银行卡管理列表数量
	 * @author pcc
	 * @param bankCardUser
	 * @return
	 */
    public int countRecordTotalNew(Map<String, Object> bankCardUser);
    /**
     * 
     * 获取银行卡管理列表
     * @author pcc
     * @param bankCardUser
     * @param offset
     * @param limit
     * @return
     */
    public List<AdminBankcardListCustomize> getRecordListNew(Map<String, Object> bankCardUser, int limitStart, int limitEnd);
}

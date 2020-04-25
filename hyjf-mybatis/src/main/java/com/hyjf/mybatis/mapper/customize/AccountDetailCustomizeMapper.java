package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountTrade;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.mybatis.model.customize.CertAccountListCustomize;
import com.hyjf.mybatis.model.customize.CertAccountListIdCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminAccountDetailDataRepairCustomize;

public interface AccountDetailCustomizeMapper {
	/**
	 * 资金明细 （明细数量）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryAccountDetailCount(AccountDetailCustomize accountDetailCustomize);
	/**
	 * 资金明细 （明细数量）总数
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryAccountDetailCountAll(AccountDetailCustomize accountDetailCustomize);

	/**
	 * 资金明细 （列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<AccountDetailCustomize> queryAccountDetails(AccountDetailCustomize accountDetailCustomize);

	/**
	 * 查询出20170120还款后有问题的用户的交易明细
	 * 
	 * @return
	 */
	public List<AdminAccountDetailDataRepairCustomize> queryAccountDetailErrorUserList();

	/**
	 * 查询用户的最小的交易明细id
	 * 
	 * @param userId
	 * @return
	 */
	public AdminAccountDetailDataRepairCustomize queryAccountDetailIdByUserId(Integer userId);

	/**
	 * 根据trade检索AccountTrade
	 * 
	 * @param trade
	 * @return
	 */
	public AccountTrade selectAccountTrade(String trade);

	/**
	 * 根据用户id,交易明细id查询出下一条交易明细
	 * 
	 * @param userId
	 * @param accountListId
	 * @return
	 */
	public AccountList selectNextAccountList(Map<String, Object> param);
	
	/**
	 * 资金明细 （列表）
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<AccountDetailCustomize> queryApiAccountDetails(AccountDetailCustomize accountDetailCustomize);

	/**
	 * 测评获取冻结金额和代收本经明细
	 *
	 * @param accountDetailCustomize
	 * @return
	 */
	public List<AccountDetailCustomize> queryAccountEvalDetail(AccountDetailCustomize accountDetailCustomize);



	/**
	 * 国家应急交易明细上送联合查询
	 * @param param
	 * @return
	 */
	public List<CertAccountListCustomize> queryCertAccountList(Map<String, Object> param);
	/**
	 * 国家应急交易明细上送联合查询
	 * @param param
	 * @return
	 */
	public List<CertAccountListCustomize> queryOldCertAccountList(Map<String, Object> param);
	
	
	
	/**
	 * 国家应急交易明细分批id查询
	 * @param param
	 * @return
	 */
	public CertAccountListIdCustomize queryCertAccountListId(Map<String, Object> param);
	public int queryOldCertAccountListMaxId();
	
	public int queryOldCertAccountListMinTime();
	public List<CertAccountListCustomize> queryOldCertAccountListByRepaySuccess(
			Map<String, Object> param);
	
}

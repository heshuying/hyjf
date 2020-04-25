package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.customize.admin.AdminBankAccountCheckCustomize;

/**
 * 
 * @author cwyang
 * add by 2017/4/7
 *  银行对账mybaties查询接口
 */
public interface AdminBankAccountCheckCustomizeMapper {

	/**
	 * 查询银行对账列表
	 * @param param
	 * @return
	 */
	public List<AdminBankAccountCheckCustomize> queryBankAccountCheckList(AdminBankAccountCheckCustomize param);

	/**
	 * 插询银行对账列表数目
	 * @param customize
	 * @return
	 */
	public Integer queryBankAccountCheckListCount(AdminBankAccountCheckCustomize customize);


	/**
	 * 查询所有用户开户行数据
	 * @return
	 */
	public List<AdminBankAccountCheckCustomize> queryAllBankOpenAccount(AdminBankAccountCheckCustomize customize);

	/**
	 * 根据流水号查找对应的未入账交易明细
	 * @param bankSeqNo
	 * @return
	 */
	public AdminBankAccountCheckCustomize queryAccountDeatilByBankSeqNo(String bankSeqNo);
	/**
	 * 插入对账异常数据
	 */
	public void insert(AccountList info);
	/**
	 * 根据用户ID获得账户ID
	 * @param userId
	 * @return
	 */
	public Account queryAccountIdByUserId(int userId);
}

package com.hyjf.admin.exception.bankcard;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.customize.admin.AdminBankCardExceptionCustomize;

public interface BankCardExceptionService extends BaseService {
	/**
	 * 根据条件查询银行卡个数
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryAccountBankCount(AdminBankCardExceptionCustomize bankCardExceptionCustomize);

	/**
	 * 查询所有开户信息
	 * 
	 * @return
	 */
	public List<AccountChinapnr> queryAllAccountBankCount();

	/**
	 * 根据条件查询银行卡列表
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<AdminBankCardExceptionCustomize> queryAccountBankList(AdminBankCardExceptionCustomize bankCardExceptionCustomize,
			int limitStart, int limitEnd);

	/**
	 * 根据userId调用汇付接口更新该用户的银行卡信息
	 * 
	 * @param userId
	 *            用户在汇盈金服的userId
	 * @return
	 */
	public String updateAccountBankByUserId(Integer userId);

}

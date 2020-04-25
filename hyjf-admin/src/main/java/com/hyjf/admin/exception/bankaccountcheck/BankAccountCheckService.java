package com.hyjf.admin.exception.bankaccountcheck;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.AdminBankAccountCheckCustomize;
/**
 * 
 * @author cwyang
 *  add by 2017/4/7
 *	银行对账业务层
 */
public interface BankAccountCheckService {

	/**
	 * 查询银行对账列表
	 * @param form
	 * @return
	 */
	public List<AdminBankAccountCheckCustomize> queryAccountCheckList(AdminBankAccountCheckCustomize form);

	/**
	 * 查询银行对账数据条数
	 * @param customize
	 * @return
	 */
	public Integer queryAccountCheckListCount(AdminBankAccountCheckCustomize customize);

}

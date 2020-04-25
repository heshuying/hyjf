package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.customize.admin.AdminBankCardExceptionCustomize;

/**
 * 银行卡异常查询Mapper
 * 
 * @author 孙亮
 * @since 2016年1月20日 下午4:35:30
 */
public interface AdminBankCardExceptionCustomizeMapper {
	/**
	 * 获取所有需要更新银行卡的账号
	 * 
	 * @return
	 */
	public List<AccountChinapnr> queryAllAccountBankCount();

	/**
	 * 根据条件查询银行卡个数
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryAccountBankCount(AdminBankCardExceptionCustomize bankCardExceptionCustomize);

	/**
	 * 根据条件查询银行卡列表
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public List<AdminBankCardExceptionCustomize> queryAccountBankList(AdminBankCardExceptionCustomize bankCardExceptionCustomize);
}
package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminBankDebtEndCustomize;

/**
 * 债权结束CustomizeMapper
 * 
 * @author liuyang
 *
 */
public interface AdminBankDebtEndCustomizeMapper {
	/**
	 * 根据条件查询结束债权件数
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public Integer countBankDebtEndList(AdminBankDebtEndCustomize adminBankDebtEndCustomize);

	/**
	 * 根据条件查询结束债权列表
	 * 
	 * @param bean
	 * @return
	 */
	public List<AdminBankDebtEndCustomize> selectRecordList(AdminBankDebtEndCustomize bean);
	
	/**
	 * 根据条件查询结束债权件数
	 * 
	 * @param accountManageBean
	 * @return
	 */
	public Integer countNewBankDebtEndList(AdminBankDebtEndCustomize adminBankDebtEndCustomize);
	
	/**
	 * 根据条件查询结束债权列表
	 * 
	 * @param bean
	 * @return
	 */
	public List<AdminBankDebtEndCustomize> selectNewRecordList(AdminBankDebtEndCustomize bean);

	/**
	 * 检索待结束的债权列表
	 * @param param
	 * @return
	 */
    List<AdminBankDebtEndCustomize> selectDebtEndList(Map<String, Object> param);
}

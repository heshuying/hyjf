package com.hyjf.admin.exception.bankdebtend;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminBankDebtEndCustomize;

public interface BankDebtEndService extends BaseService {

	/**
	 * 检索待结束的债权件数
	 * 
	 * @param form
	 * @return
	 */
	public int countBankDebtEndList(BankDebtEndBean form) throws Exception;

	/**
	 * 检索待结束的债权列表
	 * 
	 * @param form
	 * @return
	 */
	public List<AdminBankDebtEndCustomize> selectRecordList(BankDebtEndBean form) throws Exception;

	/**
	 * 结束债权
	 * 
	 * @param borrowNid
	 * @param tenderNid
	 * @param userId
	 * @return
	 */
	public boolean requestDebtEnd(String borrowNid, String tenderNid, Integer userId) throws Exception;
	
	/**
	 * 检索待结束的债权件数
	 * 
	 * @param form
	 * @return
	 */
	public int countNewBankDebtEndList(BankDebtEndBean form) throws Exception;
	
	/**
	 * 检索待结束的债权列表
	 * 
	 * @param form
	 * @return
	 */
	public List<AdminBankDebtEndCustomize> selectNewRecordList(BankDebtEndBean form) throws Exception;
}

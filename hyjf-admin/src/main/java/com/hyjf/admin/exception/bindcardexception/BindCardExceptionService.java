package com.hyjf.admin.exception.bindcardexception;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.BindCardExceptionCustomize;

public interface BindCardExceptionService extends BaseService {

	/**
	 * 检索用户银行卡列表件数
	 * 
	 * @param param
	 * @return
	 */
	public int countBankCardList(Map<String, Object> param);

	/**
	 * 检索用户银行卡列表
	 * 
	 * @param param
	 * @return
	 */
	public List<BindCardExceptionCustomize> selectBankCardList(Map<String, Object> param);

	/**
	 * 更新用户银行卡信息
	 * 
	 * @param userId
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
	public void updateBindCard(Integer userId, String accountId) throws Exception;

}

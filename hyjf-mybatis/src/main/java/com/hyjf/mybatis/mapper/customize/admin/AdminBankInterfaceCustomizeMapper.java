/**
 * Description:会员管理银行卡管理初始化列表查询
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */
	
package com.hyjf.mybatis.mapper.customize.admin;

import com.hyjf.mybatis.model.customize.admin.AdminBankInterfaceCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminBankcardListCustomize;

import java.util.List;
import java.util.Map;


public interface AdminBankInterfaceCustomizeMapper {

	/**
	 * 根据用户查询条件查询用户银行卡列表
	 * @param bankInterface
	 * @return
	 */
		
	List<AdminBankInterfaceCustomize> selectBankInterfaceList(Map<String, Object> bankInterface);

	/**
	 * @param bankInterface
	 * @return
	 */
	Integer countRecordTotal(Map<String, Object> bankInterface);
}

	
/**
 * Description:资金管理-用户转账初始化列表查询
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2016年5月23日 上午10:01:57
 * Modification History:
 * Modified by :
 * */

package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminTransferListCustomize;

public interface AdminTransferCustomizeMapper {

	/**
	 * 根据用户的查询条件查询用户开户列表
	 *
	 * @param accountUser
	 * @return
	 */
	List<AdminTransferListCustomize> selectTransferList(Map<String, Object> accountUser);

	/**
	 * @param userListCustomizeBean
	 * @return
	 */
	int countRecordTotal(Map<String, Object> accountUser);

}

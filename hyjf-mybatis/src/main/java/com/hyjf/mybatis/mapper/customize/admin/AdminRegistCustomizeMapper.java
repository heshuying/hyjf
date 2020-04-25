/**
 * Description:会员管理用户注册列表初始化列表查询
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */
	
package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminRegistListCustomize;



public interface AdminRegistCustomizeMapper {

	/**
	 * 根据用户查询条件查询用户注册列表
	 * @param user
	 * @return
	 */
		
	List<AdminRegistListCustomize> selectRegistList(Map<String, Object> user);

	/**
	 * @param registUser
	 * @return
	 */
		
	int countRecordTotal(Map<String, Object> registUser);

}

	
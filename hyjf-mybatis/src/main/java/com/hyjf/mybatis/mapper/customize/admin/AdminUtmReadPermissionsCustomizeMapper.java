/**
 * 渠道账号管理
 */
package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.AdminUtmReadPermissionsCustomize;

public interface AdminUtmReadPermissionsCustomizeMapper {

	/**
	 * 获取列表
	 * @param adminUtmReadPermissionsCustomize
	 * @return
	 */
	List<AdminUtmReadPermissionsCustomize> selectAdminUtmReadPermissionsRecord(AdminUtmReadPermissionsCustomize adminUtmReadPermissionsCustomize);


	/**
	 * 获得列表数
	 * 
	 * @param adminUtmReadPermissionsCustomize
	 * @return
	 */
	Integer countAdminUtmReadPermissionsRecord(AdminUtmReadPermissionsCustomize adminUtmReadPermissionsCustomize);

}

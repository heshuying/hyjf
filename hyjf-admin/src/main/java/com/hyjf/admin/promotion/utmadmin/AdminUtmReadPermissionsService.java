package com.hyjf.admin.promotion.utmadmin;

import java.util.List;

import com.hyjf.mybatis.model.auto.AdminUtmReadPermissions;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.admin.AdminUtmReadPermissionsCustomize;

public interface AdminUtmReadPermissionsService {

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
	
	
	/**
	 * 获取单表
	 * 
	 * @return
	 */
	public AdminUtmReadPermissions getRecord(Integer id);

	
	/**
	 * 插入
	 * 
	 * @param record
	 */
	public void insertRecord(AdminUtmReadPermissionsBean record);

	/**
	 * 更新
	 * 
	 * @param record
	 */
	public void updateRecord(AdminUtmReadPermissionsBean form);

	/**
	 * 删除
	 * 
	 * @param record
	 */
	public void deleteRecord(Integer id);
	
	
	/**
	 * 用户是否存在
	 * 
	 * @param username
	 * @return
	 */
	public int isExistsAdminUser(String userName);
	
	/**
	 * 获取渠道数据
	 * @return
	 */
	public List<UtmPlat> getUtmPlatList();

}

package com.hyjf.admin.maintenance.menu;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.AdminCustomize;
import com.hyjf.mybatis.model.customize.AdminSystem;

public interface AdminMenuService extends BaseService {

	/**
	 * 获取菜单列表
	 *
	 * @return
	 */
	public JSONArray getRecordList(AdminMenuBean form);

	/**
	 * 获取单个菜单
	 *
	 * @return
	 */
	public AdminCustomize getRecord(Integer id);

	/**
	 * 根据主键判断菜单数据是否存在
	 *
	 * @return
	 */
	public boolean isExistsRecord(Integer id);

	/**
	 * 菜单插入
	 *
	 * @param record
	 */
	public void insertRecord(AdminMenuBean record);

	/**
	 * 菜单更新
	 *
	 * @param record
	 */
	public void updateRecord(AdminMenuBean record);

	/**
	 * 菜单删除
	 *
	 * @param ids
	 */
	public void deleteRecord(List<String> ids);

    /**
     * 取得菜单权限列表
     *
     * @param ids
     */
    public List<AdminSystem> getMenuPermissionsList(String menuUuid);

    /**
     * 菜单权限更新
     *
     * @param record
     */
    public void updateMenuPermissions(AdminMenuBean record);
}

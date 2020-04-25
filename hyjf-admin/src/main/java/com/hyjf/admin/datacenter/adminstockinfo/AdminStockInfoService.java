package com.hyjf.admin.datacenter.adminstockinfo;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminStockInfoCustomize;

public interface AdminStockInfoService extends BaseService {
	/**
	 * 查询数量
	 * @param customerCustomize
	 * @return
	 */
    public Integer getAdminStockInfocount(AdminStockInfoCustomize entity);
	/**
	 * 查询信息
	 * @param customerCustomize
	 * @return
	 */
	public List<AdminStockInfoCustomize> getAdminStockInfoList(AdminStockInfoCustomize entity);
	/**
     * 新增信息
     * @param customerCustomize
     * @return
     */
	public void insertAdminStockInfo(AdminStockInfoCustomize entity);
	/**
     * 修改信息
     * @param customerCustomize
     * @return
     */
	public void updateAdminStockInfo(AdminStockInfoCustomize entity);
	/**
     * 删除信息
     * @param customerCustomize
     * @return
     */
    public void deleteAdminStockInfo(int id);
    /**
     * 获取单条信息
     * @param customerCustomize
     * @return
     */
    public AdminStockInfoCustomize getAdminStockInfoById(int id);
	
}

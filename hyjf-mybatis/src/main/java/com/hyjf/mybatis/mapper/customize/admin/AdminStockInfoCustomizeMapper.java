package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;

import com.hyjf.mybatis.model.customize.admin.AdminStockInfoCustomize;

public interface AdminStockInfoCustomizeMapper {
    
	public void insertAdminStockInfo(AdminStockInfoCustomize entity);

    public void updateAdminStockInfo(AdminStockInfoCustomize entity);

    public void deleteAdminStockInfo(int id);

    public AdminStockInfoCustomize getAdminStockInfoById(int id);

    public Integer getAdminStockInfocount(AdminStockInfoCustomize entity);

    public List<AdminStockInfoCustomize>  getAdminStockInfoList(AdminStockInfoCustomize entity);

    public List<AdminStockInfoCustomize>  getAdminStockInfoPage(AdminStockInfoCustomize entity);
}

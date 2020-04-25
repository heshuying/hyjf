package com.hyjf.admin.datacenter.adminstockinfo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.AdminStockInfoCustomize;

@Service
public class AdminStockInfoServiceImpl extends BaseServiceImpl implements AdminStockInfoService {
	/**
	 * 查询数量
	 * 
	 * @param customerCustomize
	 * @return
	 */
	@Override
	public Integer getAdminStockInfocount(AdminStockInfoCustomize entity) {
		Integer count = this.adminStockInfoCustomizeMapper.getAdminStockInfocount(entity);
		return count;
	}

	/**
	 * 查询信息
	 * 
	 * @param customerCustomize
	 * @return
	 */
	@Override
	public List<AdminStockInfoCustomize> getAdminStockInfoList(AdminStockInfoCustomize entity) {
		List<AdminStockInfoCustomize> list = this.adminStockInfoCustomizeMapper.getAdminStockInfoList(entity);
		return list;
	}
	
	/**
     * 新增信息
     * @param customerCustomize
     * @return
     */
    public void insertAdminStockInfo(AdminStockInfoCustomize entity){
        this.adminStockInfoCustomizeMapper.insertAdminStockInfo(entity);
    }
    /**
     * 修改信息
     * @param customerCustomize
     * @return
     */
    public void updateAdminStockInfo(AdminStockInfoCustomize entity){
        this.adminStockInfoCustomizeMapper.updateAdminStockInfo(entity);
    }
    /**
     * 删除信息
     * @param customerCustomize
     * @return
     */
    public void deleteAdminStockInfo(int id){
        this.adminStockInfoCustomizeMapper.deleteAdminStockInfo(id);
    }
    /**
     * 获取单条信息
     * @param customerCustomize
     * @return
     */
    public AdminStockInfoCustomize getAdminStockInfoById(int id){
        return this.adminStockInfoCustomizeMapper.getAdminStockInfoById(id);
    }

}

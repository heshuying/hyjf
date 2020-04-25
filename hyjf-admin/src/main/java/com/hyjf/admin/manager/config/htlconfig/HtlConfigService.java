package com.hyjf.admin.manager.config.htlconfig;

import java.util.List;

import com.hyjf.mybatis.model.auto.Product;

public interface HtlConfigService {

    /**
     * 获取惠天利配置列表
     * 
     * @return
     */
    public List<Product> getRecordList(Product product, int limitStart, int limitEnd);

    /**
     * 获取单个取惠天利配置
     * 
     * @return
     */
    public Product getRecord(Integer record);

    /**
     * 根据主键判断取惠天利配置中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(Product record);

    /**
     * 取惠天利配置更新,汇天利的更新和其他不太一样,每次只更新其中一列
     * 
     * @param record
     */
    public void updateRecord(HtlConfigBean record);

    /**
     * 取惠天利配置删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);
}

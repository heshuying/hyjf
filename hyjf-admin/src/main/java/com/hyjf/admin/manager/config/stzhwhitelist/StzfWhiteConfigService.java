package com.hyjf.admin.manager.config.stzhwhitelist;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.HjhInstConfigExample;
import com.hyjf.mybatis.model.customize.STZHWhiteListCustomize;


public interface StzfWhiteConfigService {

    /**
     * 获取提成配置列表
     * 
     * @return
     */
    public List<STZHWhiteListCustomize> getRecordList(int limitStart, int limitEnd);

    /**
     * 获取单个取提成配置
     * 
     * @return
     */
    public STZHWhiteListCustomize getRecord(Integer record);

    /**
     * 根据主键判断取提成配置中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(STZHWhiteListCustomize record);

    /**
     * 取提成配置插入
     * 
     * @param record
     */
    public int insertRecord(STZHWhiteListCustomize record);

    /**
     * 取提成配置更新
     * 
     * @param record
     */
    public int updateRecord(STZHWhiteListCustomize record);
    
    /**
     * 根据名称获取单个取提成配置
     * 
     * @return
     */
    public STZHWhiteListCustomize selectByObject(STZHWhiteListCustomize record);
    /**
     * 获取机构列表
     * 
     * @return
     */
    public List<HjhInstConfig> getRegionList();
    /**
     * 按照机构编号获取机构名称
     * 
     * @return
     */
    public List<HjhInstConfig> getRegionName(HjhInstConfigExample hjhInstConfigExample);
    
}

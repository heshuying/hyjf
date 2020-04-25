package com.hyjf.admin.manager.config.feeconfig;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.FeeConfig;

public interface FeeConfigService extends BaseService {

    /**
     * 获取手续费列表列表
     * 
     * @return
     */
    public List<FeeConfig> getRecordList(FeeConfig feeConfig, int limitStart, int limitEnd);

    /**
     * 获取单个手续费列表维护
     * 
     * @return
     */
    public FeeConfig getRecord(Integer record);

    /**
     * 根据主键判断手续费列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(FeeConfig record);

    /**
     * 手续费列表插入
     * 
     * @param record
     */
    public void insertRecord(FeeConfigBean record);

    /**
     * 手续费列表更新
     * 
     * @param record
     */
    public void updateRecord(FeeConfigBean record);
    
    /**
     * 配置删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);


}
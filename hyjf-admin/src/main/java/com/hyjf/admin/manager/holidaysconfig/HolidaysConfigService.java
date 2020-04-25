package com.hyjf.admin.manager.holidaysconfig;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.HolidaysConfig;

public interface HolidaysConfigService extends BaseService {

    /**
     * 获取手续费列表列表
     * 
     * @return
     */
    public List<HolidaysConfig> getRecordList(HolidaysConfig holidaysConfig, int limitStart, int limitEnd);

    /**
     * 获取单个手续费列表维护
     * 
     * @return
     */
    public HolidaysConfig getRecord(Integer record);



    /**
     * 手续费列表插入
     * 
     * @param record
     */
    public void insertRecord(HolidaysConfigBean record);

    /**
     * 手续费列表更新
     * 
     * @param record
     */
    public void updateRecord(HolidaysConfigBean record);
    



}
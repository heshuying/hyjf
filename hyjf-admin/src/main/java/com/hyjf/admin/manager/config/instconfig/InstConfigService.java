package com.hyjf.admin.manager.config.instconfig;

import java.util.List;

import com.hyjf.mybatis.model.auto.HjhInstConfig;

import javax.servlet.http.HttpServletRequest;

public interface InstConfigService {

    /**
     * 获取提成配置列表
     * 
     * @return
     */
    List<HjhInstConfig> getRecordList(int limitStart, int limitEnd);

    /**
     * 获取单个取提成配置
     * 
     * @return
     */
    HjhInstConfig getRecord(Integer record);

    /**
     * 根据主键判断取提成配置中数据是否存在
     * 
     * @return
     */
    boolean isExistsRecord(HjhInstConfig record);

    /**
     * 取提成配置插入
     * 
     * @param record
     */
    int insertRecord(HjhInstConfig record, String instCode);

    /**
     * 取提成配置更新
     * 
     * @param record
     */
    int updateRecord(HjhInstConfig record);

    /**
     * 取提成配置删除
     * 
     * @param recordList
     */
    void deleteRecord(List<Integer> recordList);

    /**
     * 根据机构属性获取机构配置
     * @param instType
     * @return
     */
    List<HjhInstConfig> getInstConfigByType(int instType);

    /**
     * 判断资产编号是否存在
     * @param request
     * @return
     */
    String isExists(HttpServletRequest request);
}

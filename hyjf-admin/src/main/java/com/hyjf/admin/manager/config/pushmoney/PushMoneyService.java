package com.hyjf.admin.manager.config.pushmoney;

import java.util.List;

import com.hyjf.mybatis.model.auto.PushMoney;

public interface PushMoneyService {

    /**
     * 获取提成配置列表
     * 
     * @return
     */
    public List<PushMoney> getRecordList(PushMoney PushMoney, int limitStart, int limitEnd);

    /**
     * 获取单个取提成配置
     * 
     * @return
     */
    public PushMoney getRecord(Integer record);

    /**
     * 根据主键判断取提成配置中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(PushMoney record);

    /**
     * 取提成配置插入
     * 
     * @param record
     */
    public void insertRecord(PushMoney record);

    /**
     * 取提成配置更新
     * 
     * @param record
     */
    public void updateRecord(PushMoney record);

    /**
     * 取提成配置删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);
}

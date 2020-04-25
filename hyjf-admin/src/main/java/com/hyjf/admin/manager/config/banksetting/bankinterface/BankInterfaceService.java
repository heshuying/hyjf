package com.hyjf.admin.manager.config.banksetting.bankinterface;

import com.hyjf.mybatis.model.auto.BankInterface;
import com.hyjf.mybatis.model.customize.admin.AdminBankInterfaceCustomize;

import java.util.List;
import java.util.Map;

public interface BankInterfaceService {

    /**
     * 获取接口配置列表
     * 
     * @return
     */
    public List<AdminBankInterfaceCustomize> selectRecordList(Map<String, Object> paraMap);

    /**
     * 获取接口配置表数据数量
     *
     * @return
     */
    public Integer getRecordCount(Map<String, Object> paraMap);

    /**
     * 根据id获取数据
     *
     * @return
     */
    public BankInterface getRecord(Integer id);

    /**
     * 接口配置删除
     *
     * @param record
     */
    void deleteRecord(BankInterface record);

    /**
     * 禁用/启用
     *
     * @param record
     */
    void updateRecord(BankInterface record);

}
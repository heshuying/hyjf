/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.nifaconfig;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.NifaContractTemplate;
import com.hyjf.mybatis.model.auto.NifaFieldDefinition;
import com.hyjf.mybatis.model.customize.FddTempletCustomize;

import java.util.List;

/**
 * @author nixiaoling
 * @version NifaConfigBean, v0.1 2018/7/4 11:46
 */
public interface NifaConfigService extends BaseService {
    /**
     * 插入字典定义表
     *
     * @param record
     */
    int insertRecordFieldDefinition(NifaFieldDefinition record);

    List<NifaFieldDefinition> getNifaFieldDefinitionList(int limtStart, int limtEnd);
    /**
     * 根据id查找字段定义数据
     * @param fieldDefinitId
     * @return
     */
    NifaFieldDefinition selectNifaFieldDefinitionInterfaceById(int fieldDefinitId);

    /**
     * 修改字段定义
     * @param record
     * @return
     */
    int updateRecordFieldDefinition(NifaFieldDefinition record);

    /**
     * 分页显示合同模板条款
     * @param limtStart
     * @param limtEnd
     * @return
     */
    List<NifaContractTemplate> selectNifaContractTemplateInterface(int limtStart, int limtEnd);
    /**
     * 插入合同模板条款
     * @param nifaContractTemplateInterface
     * @return
     */
    int insertNifaContractTemplateInterface(NifaContractTemplate nifaContractTemplateInterface);

    /**
     * 修改合同模板条款
     * @param nifaContractTemplateInterface
     * @return
     */
    int updateNifaContractTemplateInterface(NifaContractTemplate nifaContractTemplateInterface);

    /**
     * 根据id获取合同模板条款
     * @param contractId
     * @return
     */
    NifaContractTemplate selectNifaContractTemplateInterfaceById(int contractId);

    /**
     * 查找不再合同模版约定条款表里的协议模板号
     * @return
     */
    List<FddTempletCustomize> selectContractTempId();
    /**
     * 合同模版约定条款删除
     * @param recordList
     */
    void deleteRecordContractTemplate(List<Integer> recordList);
}

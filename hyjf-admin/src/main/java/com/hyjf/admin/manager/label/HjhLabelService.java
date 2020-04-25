package com.hyjf.admin.manager.label;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.admin.manager.allocationengine.AllocationEngineBean;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.HjhAllocationEngine;
import com.hyjf.mybatis.model.auto.HjhAssetType;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.HjhLabel;
import com.hyjf.mybatis.model.customize.HjhLabelCustomize;

public interface HjhLabelService  extends BaseService{

    /**
     * 标签配置列表
     * 
     * @return
     */
    public List<HjhLabelCustomize> getRecordList(HjhLabelBean hjhLabel, int limitStart, int limitEnd);

    
    /**
     * 资产来源
     * @param string
     * @return
     */
    public List<HjhInstConfig> hjhInstConfigList(String instCode);
    
    
    /**
     * 根据资金来源查询产品类型
     * @param string
     * @return
     */
    public List<HjhAssetType> hjhAssetTypeList(String instCode);
    
    /**
     * 项目类型
     * 
     * @return
     */
    public List<BorrowProjectType> borrowProjectTypeList(String borrowTypeCd);
    

    /**
     * 还款方式
     * 
     * @return
     */
    public List<BorrowStyle> borrowStyleList(String nid);


    /**
     * 获取单个取提成配置
     * 
     * @return
     */
    public HjhLabelCustomize getRecord(Integer record);

    /**
     * 根据主键判断取提成配置中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(HjhLabel record);
    
    /**
     * 根据标签名称判断取提成配置中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecordByName(HjhLabel record);

    /**
     * 取提成配置插入
     * 
     * @param record
     */
    public void insertRecord(HjhLabel record);

    /**
     * 取提成配置更新
     * 
     * @param record
     */
    public void updateRecord(HjhLabel record);
    
    /**
     * 根据ID获取单个还款方式
     * @param recordID
     * @return
     */
    public HjhLabel getRecordById(Integer recordID);
    /**
     * 通过标签名称获取激计划配置
     * 
     * @return
     */
    List<HjhAllocationEngine> getAllocationEngineRecordList(AllocationEngineBean bean);
    /**
     * 更改计划配置标签名称
     * 
     * @return
     */
    void updatePlanConfigRecord(HjhAllocationEngine record);

}

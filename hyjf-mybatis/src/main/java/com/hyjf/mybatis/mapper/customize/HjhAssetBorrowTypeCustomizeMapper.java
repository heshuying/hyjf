package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.customize.HjhAssetBorrowTypeCustomize;

public interface HjhAssetBorrowTypeCustomizeMapper {
    /**
     * 
     * 件数
     * @author liubin
     * @param borrowFinmanNewChargeCustomize
     * @return
     */
    public int countRecord(HjhAssetBorrowTypeCustomize hjhAssetBorrowTypeCustomize);

    /**
     * 
     * 列表
     * @author liubin
     * @param borrowFinmanNewChargeCustomize
     * @return
     */
    public List<HjhAssetBorrowTypeCustomize> getRecordList(
        HjhAssetBorrowTypeCustomize hjhAssetBorrowTypeCustomize);
    
    /**
     * 获取标的流程配置 add by liushouyi
     * 
     * @param borrowNid
     * @return
     */
    public List<HjhAssetBorrowType> selectAssetBorrowType(@Param("borrowNid") String borrowNid);
}

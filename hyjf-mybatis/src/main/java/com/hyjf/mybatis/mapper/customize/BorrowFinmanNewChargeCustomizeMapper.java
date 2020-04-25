package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.BorrowFinmanNewChargeCustomize;

public interface BorrowFinmanNewChargeCustomizeMapper {
    /**
     * 
     * 检索融资管理费件数
     * @author liuyang
     * @param borrowFinmanNewChargeCustomize
     * @return
     */
    public int countRecordTotal(BorrowFinmanNewChargeCustomize borrowFinmanNewChargeCustomize);

    /**
     * 
     * 检索融资管理费列表
     * @author liuyang
     * @param borrowFinmanNewChargeCustomize
     * @return
     */
    public List<BorrowFinmanNewChargeCustomize> getRecordList(
        BorrowFinmanNewChargeCustomize borrowFinmanNewChargeCustomize);
}

/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.mapper.customize.nifa;

import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.NifaContractStatus;
import com.hyjf.mybatis.model.customize.nifa.NifaContractStatusCustomize;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaContractStatusCustomizeMapper, v0.1 2018/7/6 11:39
 */
public interface NifaContractStatusCustomizeMapper {
    /**
     * 获取前一天合同状态变更记录
     * @return
     */
    List<NifaContractStatusCustomize> selectNifaContractStatus();

    /**
     * 获取未更新合同状态的完全债转的订单
     *
     * @return
     */
    List<BorrowRecover> selectBorrowRecoverCredit();
}

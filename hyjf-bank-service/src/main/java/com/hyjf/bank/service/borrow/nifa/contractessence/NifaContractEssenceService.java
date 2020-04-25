/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.borrow.nifa.contractessence;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.BorrowTender;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaContractEssenceService, v0.1 2018/7/9 14:30
 */
public interface NifaContractEssenceService extends BaseService {

    /**
     * 插入合同要素信息数据
     *
     * @param borrowNid
     * @param ordid
     * @return
     */
    boolean insertContractEssence(String borrowNid, String ordid, BorrowTender borrowTender);
    // del by liushouyi nifa2 20181128 tenderListByBorrowNid

    /**
     * 主从不同步问题主库查询数据（微服务优化掉）
     *
     * @param borrowNid
     * @return
     */
    List<BorrowTender> tenderListByBorrowNid(String borrowNid);
}

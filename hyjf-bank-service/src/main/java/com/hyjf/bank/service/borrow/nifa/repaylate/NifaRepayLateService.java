/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.borrow.nifa.repaylate;

import com.hyjf.bank.service.BaseService;

/**
 * @author PC-LIUSHOUYI
 * @version NifaRepayLateService, v0.1 2018/7/14 10:18
 */
public interface NifaRepayLateService extends BaseService {
    /**
     * 插入合同状态变更表逾期数据
     *
     * @param borrowNid
     * @return
     */
    boolean insertNifaRepayLateInfo(String borrowNid);

    /**
     *  插入合同状态变更表全部债转数据
     *
     * @param nid
     * @param nid
     * @return
     */
    boolean insertNifaRepayCreaditInfo(String nid);
}

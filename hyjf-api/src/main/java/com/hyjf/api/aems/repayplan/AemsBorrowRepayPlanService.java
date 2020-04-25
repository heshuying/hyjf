/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.api.aems.repayplan;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.customize.apiweb.aems.AemsBorrowRepayPlanCustomize;

import java.util.List;

/**
 * AEMS系统:查询还款计划Service
 *
 * @author liuyang
 * @version AemsBorrowRepayPlanService, v0.1 2018/10/16 17:34
 */
public interface AemsBorrowRepayPlanService extends BaseService {

    /**
     * 获取标的列表
     *
     * @param requestBean
     * @return
     */
    List<AemsBorrowRepayPlanCustomize> selectBorrowRepayPlanList(AemsBorrowRepayPlanRequestBean requestBean);


    /**
     * 根据机构编号,查询还款计划数量
     *
     * @param requestBean
     * @return
     */
    Integer selectBorrowRepayPlanCountsByInstCode(AemsBorrowRepayPlanRequestBean requestBean);


    /**
     * 根据标的号获取还款计划
     *
     * @param borrowNid
     * @return
     */
    List<BorrowRepayPlan> selectBorrowRepayPlanByBorrowNid(String borrowNid);

    /**
     * 根据标的编号查询资产推送表
     *
     * @param borrowNid
     * @return
     */
    HjhPlanAsset selectHjhPlanAssetByBorrowNid(String borrowNid);
}

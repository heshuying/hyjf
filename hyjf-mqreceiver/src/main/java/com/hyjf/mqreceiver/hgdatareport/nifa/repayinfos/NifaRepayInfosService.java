/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 *
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:43:49
 * Modification History:
 * Modified by :
 */

package com.hyjf.mqreceiver.hgdatareport.nifa.repayinfos;

import com.hyjf.mongo.hgdatareport.entity.NifaBorrowInfoEntity;
import com.hyjf.mqreceiver.hgdatareport.BaseHgDateReportService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRepay;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author liushouyi
 */

public interface NifaRepayInfosService extends BaseHgDateReportService {

    /**
     * 处理还款上送数据
     *
     * @param historyData
     * @param borrowPeriod
     * @param borrow
     * @param borrowRepay
     * @param borrowRecoverList
     * @param recoverFee
     * @param lateCounts
     * @param nifaRepayInfoEntity
     * @return
     */
    boolean selectDualNifaRepayInfo(String historyData, Integer borrowPeriod, Borrow borrow, BorrowRepay borrowRepay, List<BorrowRecover> borrowRecoverList, BigDecimal recoverFee, String lateCounts, NifaBorrowInfoEntity nifaRepayInfoEntity);
}
package com.hyjf.mqreceiver.wrb.service;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.customize.wrb.WrbTenderNotifyCustomize;

/**
 * @author xiasq
 * @version WrbCallBackService, v0.1 2018/3/8 11:24
 */
public interface WrbCallBackService extends BaseService {
    /**
     * 风车理财根据出借订单号查询出借信息
     * @param nid
     * @param userId
     * @return
     */
    WrbTenderNotifyCustomize searchBorrowTenderByNid(String nid, Integer userId);
}

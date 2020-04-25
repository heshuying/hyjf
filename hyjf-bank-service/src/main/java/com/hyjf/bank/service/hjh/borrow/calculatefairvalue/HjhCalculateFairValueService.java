package com.hyjf.bank.service.hjh.borrow.calculatefairvalue;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.HjhAccede;

import java.util.List;

/**
 * 计划加入订单计算公允价值Service
 * @author liuyang
 */
public interface HjhCalculateFairValueService extends BaseService {

    /**
     * 根据加入订单号查询
     * @param accedeOrderId
     * @return
     */
    HjhAccede selectHjhAccedeByAccedeOrderId(String accedeOrderId);

    /**
     * 计划加入订单的公允价值
     * @param hjhAccede
     * @param calculateType
     */
    void calculateFairValue(HjhAccede hjhAccede,Integer calculateType);

}

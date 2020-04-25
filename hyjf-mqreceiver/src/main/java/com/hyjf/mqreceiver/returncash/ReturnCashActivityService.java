/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mqreceiver.returncash;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;

import java.math.BigDecimal;

/**
 * @author yaoyong
 * @version UserOperationLogService, v0.1 2018/10/9 15:48
 */
public interface ReturnCashActivityService extends BaseService {

    boolean saveReturnCash(Integer userId, String orderId, Integer productType, BigDecimal investMoney);

    void  updateJoinTime(String borrowNid,Integer nowTime);

}

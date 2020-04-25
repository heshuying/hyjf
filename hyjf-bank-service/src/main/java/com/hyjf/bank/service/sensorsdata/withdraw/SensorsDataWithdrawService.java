/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.sensorsdata.withdraw;

import com.hyjf.bank.service.BaseService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;

import java.io.IOException;

/**
 * 神策数据统计:提现业务相关Service
 *
 * @author liuyang
 * @version SensorsDataWithdrawService, v0.1 2018/7/13 15:12
 */
public interface SensorsDataWithdrawService extends BaseService {

    /**
     * 发送神策埋点数据
     *
     * @param sensorsDataBean
     * @throws IOException
     * @throws InvalidArgumentException
     */
    void sendSensorsData(SensorsDataBean sensorsDataBean) throws IOException, InvalidArgumentException;
}

/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.sensorsdata.openaccount;

import com.hyjf.bank.service.BaseService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;

import java.io.IOException;

/**
 * 神策数据统计:用户开户Service
 *
 * @author liuyang
 * @version SensorsDataOpenAccountService, v0.1 2018/9/27 9:14
 */
public interface SensorsDataOpenAccountService extends BaseService {
    /**
     * 发送神策埋点数据
     *
     * @param sensorsDataBean
     */
    void sendSensorsData(SensorsDataBean sensorsDataBean) throws IOException, InvalidArgumentException;
}

/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.sensorsdata.register;

import com.hyjf.bank.service.BaseService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;

import java.io.IOException;

/**
 * 神策数据统计:注册业务相关Service
 *
 * @author liuyang
 * @version SensorsDataRegisterService, v0.1 2018/7/16 17:13
 */
public interface SensorsDataRegisterService extends BaseService {

    /**
     * 发送神策埋点数据
     *
     * @param sensorsDataBean
     * @throws IOException
     * @throws InvalidArgumentException
     */
    void sendSensorsData(SensorsDataBean sensorsDataBean) throws IOException, InvalidArgumentException;
}

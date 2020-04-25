/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.sensorsdata.login;

import com.hyjf.bank.service.BaseService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;

import java.io.IOException;

/**
 * 神策数据统计:登录相关Service
 *
 * @author liuyang
 * @version SensorsDataLoginService, v0.1 2018/7/19 14:59
 */
public interface SensorsDataLoginService extends BaseService {
    /**
     * 发送神策数据
     *
     * @param sensorsDataBean
     * @throws IOException
     * @throws InvalidArgumentException
     */
    void sendSensorsData(SensorsDataBean sensorsDataBean) throws IOException, InvalidArgumentException;
}

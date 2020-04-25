/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.sensorsdata.hjh;

import com.hyjf.bank.service.BaseService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;

import java.io.IOException;

/**
 * 神策数据统计:汇计划相关Service
 *
 * @author liuyang
 * @version SensorsDataHjhInvestService, v0.1 2018/7/21 18:16
 */
public interface SensorsDataHjhInvestService extends BaseService {


    /**
     * 发送神策埋点数据
     *
     * @param sensorsDataBean
     */
    void sendSensorsData(SensorsDataBean sensorsDataBean) throws IOException, InvalidArgumentException;
}

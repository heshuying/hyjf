/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.datacenter.certreportlog;

import com.hyjf.mybatis.model.auto.CertLog;

import java.util.List;


public interface CertReportLogService {


    /**
     * 根据分页查找数据
     * @param limtStart
     * @param limtEnd
     * @param form
     * @return
     */
    List<CertLog> selectCertReportLog(int limtStart, int limtEnd, CertReportLogBean form);

    /**
     * 查找数量
     * @param form
     * @return
     */
    Integer selectCertReportLogCount(CertReportLogBean form);

    /**
     * 数据同步 发送MQ
     * @param dataType
     */
    void doDdataSyn(String dataType);
}

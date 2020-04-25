/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.datacenter.nifareportlog;

import com.hyjf.mybatis.model.auto.NifaReportLog;

import java.util.List;

/**
 * @author nixiaoling
 * @version NifaConfigBean, v0.1 2018/7/4 11:46
 */
public interface NifaReportLogService {

    /**
     * 根据分页查找数据
     *
     * @param limtStart
     * @param limtEnd
     * @return
     */
    List<NifaReportLog> selectNifaReportLog(int limtStart, int limtEnd,NifaReportLogBean form);
    /**
     * 根据id查找日志信息
     * @param recordId
     * @return
     */
    NifaReportLog getNifaReportLogById (int recordId);

}

/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.bifa.exceptiondata;

import com.hyjf.batch.hgdatareport.bifa.operationdata.BifaOperationDataReportTask;
import com.hyjf.common.util.CustomConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 北互金异常数据处理定时任务
 * @author jun
 * @version BifaExceptionDataHandleTask, v0.1 2018/11/30 15:02
 */
public class BifaExceptionDataHandleTask {

    Logger _log = LoggerFactory.getLogger(BifaOperationDataReportTask.class);

    private String thisMessName = "异常数据处理定时任务";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessName + "】";

    @Autowired
    private BifaExceptionDataService bifaExceptionDataService;

    /** 运行状态 */
    private static int isRun = 0;

    public void run() {
        if (isRun == 0) {
            isRun = 1;
            try {
                _log.info(logHeader + "处理数据开始！！");
                bifaExceptionDataService.executeExceptionDataHandle();
                _log.info(logHeader + "处理数据成功。");
            } catch (Exception e) {
                _log.info(logHeader + "处理数据失败！！"+e.getMessage());
            } finally {
                isRun = 0;
            }
        }

    }

}

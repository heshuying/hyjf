/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.bifa.indexdata;

import com.hyjf.batch.hgdatareport.bifa.operationdata.BifaOperationDataReportTask;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 北互金索引数据上报定时任务
 * @author jun
 */
public class BifaIndexDataReportHandleTask {

    Logger _log = LoggerFactory.getLogger(BifaOperationDataReportTask.class);

    private String thisMessName = "索引数据上报定时任务";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessName + "】";

    @Autowired
    private BifaIndexDataReportService bifaIndexDataReportService;

    /** 运行状态 */
    private static int isRun = 0;

    public void run() {
        if (isRun == 0) {
            isRun = 1;
            try {
                _log.info(logHeader + "执行上报定时任务！！");

                // 取得当前日期为基准日期
                Integer stdDate = GetDate.getDayStart10(new Date());
                //结束时间为当前时间的前一天的23:59:59
                //生产用
                Integer endDate = stdDate - 1;
//                //测试用
//                Integer endDate = GetDate.getDayEnd10(new Date());
                //先獲取查詢返回 一周以內的日期 多查一天
                Integer startDate = GetDate.countDate(stdDate, 5, -7);

                _log.info(logHeader + "上报时间范围 " + GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(startDate) + "~" + GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(endDate));

                bifaIndexDataReportService.executeIndexDataReport(startDate, endDate);

            } catch (Exception e) {
                _log.error(logHeader + "定时任务处理失败！！", e);
            } finally {
                isRun = 0;
            }
        }

    }

}

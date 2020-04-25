/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.bifa.indexdata;

import com.hyjf.batch.hgdatareport.bifa.operationdata.BifaOperationDataReportTask;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * 北互金索引历史数据上报处理定时任务
 * @author liubin
 */
public class BifaIndexHistoryDataReportTask {

    Logger _log = LoggerFactory.getLogger(BifaOperationDataReportTask.class);

    private String thisMessName = "北互金索引历史数据上报定时任务";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessName + "】";

    @Autowired
    private BifaIndexDataReportService bifaIndexDataReportService;

    /** 运行状态 */
    private static int isRun = 0;

    public void run() {
        if (isRun == 0) {
            isRun = 1;
            _log.debug(logHeader + "==BIFA_HISTORY_OPEN_FLAG:" + RedisUtils.get(RedisConstants.BIFA_HISTORY_OPEN_FLAG));
            _log.debug(logHeader + "==BIFA_HISTORY_START_YYYYMMDD:" + RedisUtils.get(RedisConstants.BIFA_HISTORY_START_YYYYMMDD));
            _log.debug(logHeader + "==BIFA_HISTORY_END_YYYYMMDD:" + RedisUtils.get(RedisConstants.BIFA_HISTORY_END_YYYYMMDD));

            try {
                // 启动限制开关 redis.BIFA_HISTORY_OPEN_FLAG = 1 时，执行任务。
                if ( !(RedisUtils.get(RedisConstants.BIFA_HISTORY_OPEN_FLAG) != null
                        && RedisUtils.get(RedisConstants.BIFA_HISTORY_OPEN_FLAG).equals("1")) ) {
                    return;
                }

                _log.info(logHeader + "执行历史上报定时任务！！");

                // 取得当前日期为基准日期
                Integer start = GetDate.strYYYYMMDD3Timestamp3(RedisUtils.get(RedisConstants.BIFA_HISTORY_START_YYYYMMDD));
                Integer end = GetDate.strYYYYMMDD3Timestamp3(RedisUtils.get(RedisConstants.BIFA_HISTORY_END_YYYYMMDD));
                if (start.compareTo(end) > 0){
                    _log.error(logHeader
                            + "开始>结束 start:" + RedisUtils.get(RedisConstants.BIFA_HISTORY_START_YYYYMMDD)
                            + "  end:" + RedisUtils.get(RedisConstants.BIFA_HISTORY_END_YYYYMMDD));
                    throw new Exception("开始>结束");
                }
                Integer endNextMonth = GetDate.countDate(end, 2, 1);
                Integer startDate = 0;
                Integer endDate = 0;
                _log.info(logHeader + "上报时间范围 " + GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(start) + "~" + GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(GetDate.countDate(end, 5, 1) - 1));
                for (int i=1; GetDate.countDate(start, 2, i) <= endNextMonth; i++){
                    if (i==1){
                        if (GetDate.countDate(start, 2, i) >= end){
                            startDate = start;
                            endDate = GetDate.countDate(end, 5, 1) - 1;
                        }else{
                            startDate = start;
                            endDate = GetDate.countDate(start, 2, i) - 1;
                        }
                    }else if(GetDate.countDate(start, 2, i) >= end){
                        startDate = endDate + 1;
                        endDate = GetDate.countDate(end, 5, 1) - 1;
                    } else{
                        startDate = endDate + 1;
                        endDate = GetDate.countDate(start, 2, i) - 1;
                    }
                    _log.info(logHeader + "历史上报时间范围 " + GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(startDate) + "~" + GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(endDate));
                    bifaIndexDataReportService.executeIndexDataReport(startDate, endDate);
                }
                _log.info(logHeader + "定时任务处理成功。");
            } catch (Exception e) {
                _log.error(logHeader + "定时任务处理失败！！", e);
            } finally {
                isRun = 0;
            }
        }
    }

    public static void main(String[] args) {
        // 取得当前日期为基准日期
        Integer start = GetDate.strYYYYMMDD3Timestamp3("20131230");
        Integer end = GetDate.strYYYYMMDD3Timestamp3("20140228");
        Integer endNextMonth = GetDate.countDate(end, 2, 1);
        Integer startDate = 0;
        Integer endDate = 0;
        System.out.println("上报时间范围 " + GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(start) + "~" + GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(GetDate.countDate(end, 5, 1) - 1));
        for (int i=1; GetDate.countDate(start, 2, i) <= endNextMonth; i++){
            if (i==1){
                if (GetDate.countDate(start, 2, i) >= end){
                    startDate = start;
                    endDate = GetDate.countDate(end, 5, 1) - 1;
                }else{
                    startDate = start;
                    endDate = GetDate.countDate(start, 2, i) - 1;
                }
            }else if(GetDate.countDate(start, 2, i) >= end){
                startDate = endDate + 1;
                endDate = GetDate.countDate(end, 5, 1) - 1;
            } else{
                startDate = endDate + 1;
                endDate = GetDate.countDate(start, 2, i) - 1;
            }
            System.out.println(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(startDate) + "~" + GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(endDate));

//            if (endDate.compareTo(GetDate.getDayStart10(new Date())-1) <= 0){
//                System.out.println(endDate +":" + (GetDate.getDayStart10(new Date())-1) +"------"+GetDate.timestamptoNUMStrYYYYMMDDHHMMSS2(endDate));
//            }
        }
    }
}

/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.bifa.operationdata;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.hgdatareport.entity.BifaOperationDataEntity;
import com.hyjf.mongo.operationreport.entity.OperationReportEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 运营数据上报北互金
 * @author jun
 * @version BifaOperationDataReportTask, v0.1 2018/11/29 19:31
 */
public class BifaOperationDataReportTask {

    Logger _log = LoggerFactory.getLogger(BifaOperationDataReportTask.class);

    private String thisMessName = "运营数据上报";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessName + "】";

    @Autowired
    private BifaOperationDataService bifaOperationDataService;

    /** 运行状态 */
    private static int isRun = 0;

    // --> 获取平台运营数据
    // -->运行运营数据上报北互金定时任务
    public void run() {
        if (isRun == 0) {
            isRun = 1;
            try {
                // --> 数据变换
                BifaOperationDataEntity bifaOperationDataEntity = new BifaOperationDataEntity();
                boolean result = this.bifaOperationDataService.convertBifaOperationData(bifaOperationDataEntity);
                if (!result){
                    throw new Exception(logHeader + "数据变换失败！！");
                }
                //数据上报
                //上报数据失败时 将数据存放到mongoDB
                String methodName = "addOperationData";
                BifaOperationDataEntity reportResult = this.bifaOperationDataService.reportData(methodName,bifaOperationDataEntity);
                if ("1".equals(reportResult.getReportStatus())) {
                    _log.info(logHeader + "数据上报北互金成功！！"+JSONObject.toJSONString(bifaOperationDataEntity));
                }else if ("9".equals(reportResult.getReportStatus())){
                    _log.info(logHeader + "数据上报北互金失败！！"+JSONObject.toJSONString(bifaOperationDataEntity));
                }

                // --> 保存上报数据
                result = this.bifaOperationDataService.insertReportData(bifaOperationDataEntity);
                if (!result) {
                    _log.info(logHeader + "上报北互金数据保存失败！！"+JSONObject.toJSONString(bifaOperationDataEntity));
                }else {
                    _log.info(logHeader + "上报北互金数据保存成功！！"+JSONObject.toJSONString(bifaOperationDataEntity));
                }

            } catch (Exception e) {
                _log.info(logHeader + "运营数据上报北互金失败！！"+e.getMessage());
            } finally {
                isRun = 0;
            }
        }

    }



}

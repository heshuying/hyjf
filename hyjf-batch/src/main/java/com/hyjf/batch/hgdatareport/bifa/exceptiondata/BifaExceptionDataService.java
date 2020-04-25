/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.bifa.exceptiondata;

import com.hyjf.batch.hgdatareport.BaseHgDateReportService;
import com.hyjf.mongo.hgdatareport.entity.BifaOperationDataEntity;
import com.hyjf.mongo.operationreport.entity.OperationReportEntity;

/**
 * 北互金异常数据处理定时任务
 * @author jun
 * @version BifaOperationDataService, v0.1 2018/11/30 9:34
 */
public interface BifaExceptionDataService extends BaseHgDateReportService {

    void executeExceptionDataHandle();
}

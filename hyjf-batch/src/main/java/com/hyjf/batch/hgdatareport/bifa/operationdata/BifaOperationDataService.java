/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.bifa.operationdata;

import com.hyjf.batch.hgdatareport.BaseHgDateReportService;
import com.hyjf.mongo.hgdatareport.entity.BifaOperationDataEntity;
import com.hyjf.mongo.operationreport.entity.OperationReportEntity;

/**
 * 运营数据上报北互金
 * @author jun
 * @version BifaOperationDataService, v0.1 2018/11/30 9:34
 */
public interface BifaOperationDataService extends BaseHgDateReportService {

    boolean convertBifaOperationData(BifaOperationDataEntity bifaOperationDataEntity);

    OperationReportEntity getOperationDataFromPlat();

    boolean insertReportData(BifaOperationDataEntity bifaOperationDataEntity);
}

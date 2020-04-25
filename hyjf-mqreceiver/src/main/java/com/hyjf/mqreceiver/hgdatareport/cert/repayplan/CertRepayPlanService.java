package com.hyjf.mqreceiver.hgdatareport.cert.repayplan;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;


/**
 * @author nxl
 */

public interface CertRepayPlanService extends BaseHgCertReportService {
    /**
     * 获取标的的还款信息
     * @param borrowNid
     * @param json
     * @param isOld
     * @return
     */
    JSONArray getBorrowReyapPlan(String borrowNid, JSONArray json,boolean isOld);


}
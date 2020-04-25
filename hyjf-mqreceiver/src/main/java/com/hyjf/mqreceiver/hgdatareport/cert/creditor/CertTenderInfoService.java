package com.hyjf.mqreceiver.hgdatareport.cert.creditor;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;


/**
 * @author nxl
 */

public interface CertTenderInfoService extends BaseHgCertReportService {
    /**
     * 获取标的的还款信息
     * @param borrwoNid
     * @return
     */
    JSONArray getBorrowTender(String borrwoNid,JSONArray json,boolean isOld);


}
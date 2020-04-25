package com.hyjf.mqreceiver.hgdatareport.cert.transact;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;


/**
 * @author pcc
 */

public interface CertTransactService extends BaseHgCertReportService {

	JSONArray createDate(String minId, String maxId);



}
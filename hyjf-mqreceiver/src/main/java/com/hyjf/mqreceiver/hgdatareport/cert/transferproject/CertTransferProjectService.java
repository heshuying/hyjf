package com.hyjf.mqreceiver.hgdatareport.cert.transferproject;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;


/**
 * @author pcc
 */

public interface CertTransferProjectService extends BaseHgCertReportService {

	JSONArray createDate(String creditNid, String flag);


}
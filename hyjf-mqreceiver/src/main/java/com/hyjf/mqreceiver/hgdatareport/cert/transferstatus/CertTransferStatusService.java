package com.hyjf.mqreceiver.hgdatareport.cert.transferstatus;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;


/**
 * @author pcc
 */

public interface CertTransferStatusService extends BaseHgCertReportService {


	Map<String, Object> getMap(String creditNid, String flag, String status, String borroNid);

	JSONArray createDate(Map<String, Object> map, String flag);


}
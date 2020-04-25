package com.hyjf.mqreceiver.hgdatareport.cert.olddata.transact;

import java.util.List;
import java.util.Map;

import com.hyjf.mongo.hgdatareport.entity.CertAccountList;
import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;


/**
 * @author pcc
 */

public interface OldCertTransactService extends BaseHgCertReportService {



	List<Map<String, Object>> getTransactMap(Map<String, Object> param);

	void insertAndSendPostOld(CertAccountList certAccountList);


}

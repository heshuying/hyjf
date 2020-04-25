package com.hyjf.mqreceiver.hgdatareport.cert.olddata.transferstatus;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mongo.hgdatareport.entity.CertBorrowEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;
import com.hyjf.mybatis.model.auto.CertBorrow;
import com.hyjf.mybatis.model.customize.cert.CertBorrowUpdate;


/**
 * @author pcc
 */

public interface OldCertTransferStatusService extends BaseHgCertReportService {


	List<CertBorrow> insertCertBorrowEntityList();


	List<Map<String, Object>> createList(String borrowNid);


	int updateCertBorrowStatusBatch(CertBorrowUpdate update);


}
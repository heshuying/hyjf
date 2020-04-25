package com.hyjf.mqreceiver.hgdatareport.cert.olddata.borrow;

import com.hyjf.mongo.hgdatareport.entity.CertBorrowEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;

import java.util.List;
import java.util.Map;


/**
 * 合规数据上报 CERT 国家互联网应急中心 标的老数据上报
 * @author sss
 */

public interface CertOldBorrowService extends BaseHgCertReportService {

    /**
     * 查未上报的标的
     * @return
     */
    List<CertBorrowEntity> getCertBorrow();

    /**
     * 修改结果
     * @param param
     */
    void updateResult(List<Map<String,String>> param);
}
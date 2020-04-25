package com.hyjf.mqreceiver.hgdatareport.cert.olddata.borrowuser;

import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;
import com.hyjf.mybatis.model.auto.CertBorrow;
import com.hyjf.mybatis.model.customize.cert.CertBorrowUpdate;

import java.util.List;


/**
 * 合规数据上报 CERT 国家互联网应急中心 借款人老数据上报
 * @author sss
 */

public interface CertOldBorrowUserService extends BaseHgCertReportService {

    /**
     * 查未上报的标的
     * @return
     */
    List<CertBorrow> insertCertBorrowUsers();

    /**
     * 查询未上报标的的
     * @return
     */
    List<CertBorrow> insertCertBorrow();

    /**
     * 批量修改状态
     * @param update
     */
    int updateCertBorrowStatusBatch(CertBorrowUpdate update);
}
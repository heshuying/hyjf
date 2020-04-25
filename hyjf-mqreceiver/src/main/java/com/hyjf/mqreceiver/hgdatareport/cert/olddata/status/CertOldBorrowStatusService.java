package com.hyjf.mqreceiver.hgdatareport.cert.olddata.status;

import com.hyjf.mongo.hgdatareport.entity.CertBorrowEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;
import com.hyjf.mybatis.model.auto.CertBorrow;

import java.util.List;


/**
 * @author nxl
 */

public interface CertOldBorrowStatusService extends BaseHgCertReportService {

    /**
     * 查找Mongon中ht_cert_borrow标的信息
     * @return
     */
    List<CertBorrowEntity> getCertBorrowEntityList();

    /**
     * 查找数据库里待上报散标状态的数据
     * @return
     */
    List<CertBorrow> insertCertBorrowStatusList();
    /**
     * 以下为测试
     * @return
     */
   List<CertBorrow> getCertBorrowStatusList1();
}
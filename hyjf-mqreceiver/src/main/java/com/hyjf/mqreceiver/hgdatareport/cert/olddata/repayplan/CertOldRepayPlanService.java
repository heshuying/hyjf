package com.hyjf.mqreceiver.hgdatareport.cert.olddata.repayplan;

import com.hyjf.mongo.hgdatareport.entity.CertBorrowEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;
import com.hyjf.mybatis.model.auto.CertBorrow;

import java.util.List;
import java.util.Map;


/**
 * @author nxl
 */

public interface CertOldRepayPlanService extends BaseHgCertReportService {
    /**
     * 查找数据库里待上还款计划的数据
     *
     * @return
     */
   List<CertBorrow> insertCertBorrowRapayList();


}
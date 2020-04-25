package com.hyjf.mqreceiver.hgdatareport.cert.olddata.creditor;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mongo.hgdatareport.entity.CertBorrowEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;
import com.hyjf.mybatis.model.auto.CertBorrow;

import java.util.List;


/**
 * @author nxl
 */

public interface CertOldTenderInfoService extends BaseHgCertReportService {


    /**
     * 查找数据库里待上债权信息的数据
     *
     * @return
     */
    List<CertBorrow> insertCertBorrowCreditList();


}
package com.hyjf.mqreceiver.hgdatareport.cert.olddata.undertake;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;
import com.hyjf.mybatis.model.auto.CertBorrow;

import java.util.List;


/**
 * @author nxl
 */

public interface CertOldCreditInfoService extends BaseHgCertReportService {

    /**
     * 查找数据库里待上承接信息的数据
     *
     * @return
     */
    List<CertBorrow> getCertBorrowUnderTakeList();

    /**
     * 获取承接信息,组装数据
     *
     * @param borrowNid
     * @return
     */
    JSONArray getCertOldCreditInfos(String borrowNid,JSONArray jsonArray);
}
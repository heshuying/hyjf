package com.hyjf.mqreceiver.hgdatareport.cert.status;

import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;
import com.hyjf.mybatis.model.auto.BorrowRecover;

import java.util.Map;


/**
 * @author nxl
 */

public interface CertBorrowStatusService extends BaseHgCertReportService {
    /**
     * 获取标的的还款信息
     * @param borrwoNid
     * @return
     */
    Map<String,Object> selectBorrowByBorrowNid(String borrwoNid, String statusAfter,boolean isUserInfo,boolean isOld);

    /**
     * 获取放款信息
     * @param borrowNid
     * @return
     */
    BorrowRecover selectBorrowRecover(String borrowNid);

}
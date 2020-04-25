package com.hyjf.mqreceiver.hgdatareport.cert.undertake;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhDebtCreditTender;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author nxl
 */

public interface CertCreditInfoService extends BaseHgCertReportService {
    /**
     * 获取标的的还款信息
     *
     * @param borrwoNid
     * @return
     */
    JSONArray getBorrowTender(String borrwoNid, String flg);

    /**
     * 获取投资红包金额
     *
     * @param realTenderId
     * @return
     */
    BigDecimal getRedPackageSum(String realTenderId);

    /**
     * 根据加入计划单号查找加入计划信息
     *
     * @param orderId
     * @return
     */
    HjhAccede getHjhAccedeByOrderId(String orderId);

    /**
     * 获取散标承接信息
     *
     * @param creditTenderList
     * @return
     */
    JSONArray getBorrowCreditTenderInfo(List<CreditTender> creditTenderList,JSONArray json ,boolean isOld);

    /**
     * 获取智投承接信息
     *
     * @param hjhDebtCreditTenderList
     * @return
     */
    JSONArray getHjhDebtCreditInfo(List<HjhDebtCreditTender> hjhDebtCreditTenderList,JSONArray json,boolean isOld );

    /**
     * 日期转换,数据存的int10的时间戳
     *
     * @param repayTime
     * @return
     */
    String dateFormatTransformation(String repayTime, String flg);
}
package com.hyjf.mqreceiver.hgdatareport.cert.scatterinvest;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;
import com.hyjf.mybatis.model.auto.CertUser;
import com.hyjf.mybatis.model.auto.Users;

import java.util.Map;


/**
 * @author sss
 */

public interface CertScatterInveService extends BaseHgCertReportService {


    /**
     * 组装调用应急中心日志
     * @param borrowNid
     * @return
     */
    Map<String, Object> getSendData(String borrowNid,String idcardHash) throws Exception;

}
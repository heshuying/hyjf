package com.hyjf.mqreceiver.hgdatareport.cert.exception;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;
import com.hyjf.mybatis.model.auto.CertUser;
import com.hyjf.mybatis.model.auto.Users;


/**
 * @author sss
 */

public interface CertSendErrorService extends BaseHgCertReportService {

    /**
     * 重新上报  并修改数据库
     * @param list
     */
    void insertData(JSONArray list);
}
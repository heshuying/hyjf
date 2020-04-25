package com.hyjf.mqreceiver.hgdatareport.cert.getyibumessage;


import com.hyjf.mongo.hgdatareport.entity.CertReportEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportService;
import com.hyjf.mybatis.model.auto.CertLog;

import java.util.List;

/**
 * 查询批次数据入库消息
 * @Author nxl
 * @Date 2018/12/25 17:10
 */
public interface CertGetYiBuMessageService extends BaseHgCertReportService {

	CertReportEntity updateYiBuMessage(String batchNum, String certLogId,String infType);

	List<CertLog> getCertLog();
}

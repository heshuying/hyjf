package com.hyjf.batch.exception.cert;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.batch.exception.bankwithdraw.BankWithdrawExceptionService;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.CertErrLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description 合规数据上报 CERT 国家互联网应急中心异常处理
 * @Author sunss
 * @Date 2018/11/30 14:10
 */
public class CertSendExceptionTask {
	Logger logger = LoggerFactory.getLogger(CertSendExceptionTask.class);

	private String thisMessName = "异常处理";
	private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";


	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private CertSendExceptionService certSendExceptionService;

	public void run() {
		runJob();
	}

	private boolean runJob() {
		if (isRun == 0) {
			isRun = 1;
			try {
				// 检索处理失败的订单  处理次数少于三次的
				List<CertErrLog> certErrLogs = this.certSendExceptionService.selectCertSendErrLogList();
				if (certErrLogs != null && certErrLogs.size() > 0) {
					// 组装数据
					JSONArray errors = this.certSendExceptionService.getErrorData(certErrLogs);
					// 调用MQ处理
					logger.info(logHeader+"待异常处理数据数量："+errors.size());
					this.certSendExceptionService.sendToMQ(errors,RabbitMQConstants.ROUTINGKEY_CERT_ERROR_SEND);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
		}
		return true;
	}
}

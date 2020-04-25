package com.hyjf.batch.cert.getyibumessage;

import com.hyjf.batch.cert.mobilehash.CertMobileHashTask;
import com.hyjf.common.util.CustomConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description 合规数据上报 CERT 查询批次数据入库消息
 * @Author nxl
 * @Date 2018/12/25 14:10
 */
public class CertGetYiBuMessageTask {
	Logger _log = LoggerFactory.getLogger(CertGetYiBuMessageTask.class);
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private CertGetYiBuMessageService certGetYiBuMessageService;
	private String thisMessName = "查询批次数据入库消息";
	private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";
	Logger logger = LoggerFactory.getLogger(CertMobileHashTask.class);

	public void run() {
		runJob();
	}

	private boolean runJob() {
		logger.info("国家应急中心查询批次数据入库消息 start");
		if (isRun == 0) {
			isRun = 1;
			try {
				logger.info(logHeader+"开始执行MQ");
				this.certGetYiBuMessageService.getYibuMessage();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
		}
		_log.info(" 国家应急中心查询批次数据入库消息 end");
		return true;
	}
}

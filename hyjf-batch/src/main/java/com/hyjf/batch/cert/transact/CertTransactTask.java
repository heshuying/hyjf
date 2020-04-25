package com.hyjf.batch.cert.transact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description 合规数据上报 CERT 国家互联网应急中心交易流水上报
 * @Author pcc
 * @Date 2018/12/03 14:10
 */
public class CertTransactTask {
	Logger _log = LoggerFactory.getLogger(CertTransactTask.class);
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private CertTransactService certTransactOtherService;

	public void run() {
		runJob();
	}

	private boolean runJob() {
		_log.info("国家互联网应急中心交易流水上报 start");
		if (isRun == 0) {
			isRun = 1;
			try {
				this.certTransactOtherService.certTransact();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
		}
		_log.info(" 国家互联网应急中心交易流水上报 end");
		return true;
	}
}

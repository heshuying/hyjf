package com.hyjf.batch.cert.oldtransact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.cache.RedisUtils;

/**
 * @Description 合规数据上报 CERT 国家互联网应急中心交易流水历史数据生成
 * @Author pcc
 * @Date 2018/12/03 14:10
 */
public class OldCertTransactTask {
	Logger _log = LoggerFactory.getLogger(OldCertTransactTask.class);
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private OldCertTransactService oldCertTransactService;

	public void run() {
		runJob();
	}

	private boolean runJob() {
		_log.info("国家互联网应急中心交易流水历史数据生成 start");
		if (isRun == 0) {
			isRun = 1;
			
			String oldCertTransactMqIsRun = RedisUtils.get("oldCertTransactMqIsRun");
			String oldCertTransactMqStop = RedisUtils.get("oldCertTransactMqStop");
			if("1".equals(oldCertTransactMqIsRun)){
				isRun = 0;
				return true;
			}
			if("1".equals(oldCertTransactMqStop)){
				isRun = 0;
				return true;
			}
			try {
				this.oldCertTransactService.certTransact();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
		}
		_log.info("国家互联网应急中心交易流水历史数据生成 end");
		return true;
	}
}

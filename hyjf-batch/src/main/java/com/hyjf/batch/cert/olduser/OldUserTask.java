package com.hyjf.batch.cert.olduser;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.batch.exception.cert.CertSendExceptionService;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.CertErrLog;
import com.hyjf.mybatis.model.auto.Users;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description 合规数据上报 CERT 国家互联网应急中心 投资人老数据上报
 * @Author sunss
 * @Date 2018/11/30 14:10
 */
public class OldUserTask {
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private OldUserService oldUserService;

	public void run() {
		runJob();
	}

	private boolean runJob() {
		if (isRun == 0) {
			isRun = 1;
			try {
				// 检索未上报的投资人
				Integer countUsers = this.oldUserService.getCertUserNotSendCount();
				if (countUsers != null && countUsers.intValue() > 0) {
					// 调用MQ处理
					this.oldUserService.sendToMQ(RabbitMQConstants.ROUTINGKEY_CERT_ERROR_SEND);
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

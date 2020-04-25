package com.hyjf.batch.cert.oldborrowuser;

import com.hyjf.batch.cert.olduser.OldUserService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.model.auto.Borrow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description 合规数据上报 CERT 国家互联网应急中心 借款人老数据上报
 * @Author sunss
 * @Date 2018/11/30 14:10
 */
public class OldBorrowUserTask {
	Logger logger = LoggerFactory.getLogger(OldBorrowUserTask.class);
	private String thisMessName = "国家互联网应急中心老数据上报";
	private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private OldBorrowUserService oldBorrowUserService;

	public void run() {
		runJob();
	}

	private boolean runJob() {
		if (isRun == 0) {
			isRun = 1;
			try {
				// 交给DBA处理
				// 先判断redis里面有没有值  有值的话就不用初始化了  没有值的话就初始化  存到mongo里面
				/*String isInit = RedisUtils.get(RedisConstants.CERT_BATCH_IS_INIT_BORROW);

				if(isInit == null || "".equals(isInit)){
					// 初始化
					List<String> list = oldBorrowUserService.getCertBorrowNotInit();
					oldBorrowUserService.insertInitBorrow(list);
				}*/
				// 先上报用户
				Long countUsers = this.oldBorrowUserService.getCertUserCount();
				if (countUsers != null && countUsers.intValue() > 0) {
					logger.info(logHeader+"待处理的借款人总数为："+countUsers);
					// 调用MQ处理 用户信息
					this.oldBorrowUserService.sendToMQ(RabbitMQConstants.ROUTINGKEY_CERT_BORROW_USER);
				}
				// 上报标的
				Long countborrow  = this.oldBorrowUserService.getCertBorrowCount();
				if (countborrow != null && countborrow.intValue() > 0) {
					// 调用MQ处理 用户信息
					logger.info(logHeader+"待处理的标的总数为："+countborrow);

				}
				// 上报xxxxxxx
			} catch (Exception e) {
				logger.error(logHeader+"出错了：",e);
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
		}
		return true;
	}
}

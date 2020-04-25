package com.hyjf.batch.cert.olduser;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.customize.CertSendUser;

import java.util.List;

/**
* 合规数据上报 CERT 国家互联网应急中心 投资人老数据上报
* @author LiuBin
* @date 2017年8月1日 上午9:59:24
*
*/ 
public interface OldUserService extends BaseService {


	/**
	 * 发送MQ
	 * @param routingkey
	 */
	void sendToMQ(String routingkey);

	/**
	 * 查询未上报的投资人
	 * @return
	 */
	List<CertSendUser> selectCertUserNotSend();

	/**
	 * 查询未上报的投资人 数量
	 * @return
	 */
	Integer getCertUserNotSendCount();
}

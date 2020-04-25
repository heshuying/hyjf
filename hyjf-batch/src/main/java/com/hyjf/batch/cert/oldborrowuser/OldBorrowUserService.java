package com.hyjf.batch.cert.oldborrowuser;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.customize.CertSendUser;

import java.util.List;

/**
* 合规数据上报 CERT 国家互联网应急中心 借款人老数据上报
* @author LiuBin
* @date 2017年8月1日 上午9:59:24
*
*/ 
public interface OldBorrowUserService extends BaseService {


	/**
	 * 发送MQ
	 * @param routingkey
	 */
	void sendToMQ(String routingkey);

	/**
	 * 检索未上报的在贷标的数量
	 * @return
	 */
	Long getCertUserCount();

	/**
	 * 初始化标的
	 */
	void insertInitBorrow(List<String> list);

	/**
	 * 获得所有在贷的标的
	 * @return
	 */
	List<String> getCertBorrowNotInit();

	/**
	 * 获得所有已经上报成功用户信息的标的
	 * @return
	 */
	Long getCertBorrowCount();
}

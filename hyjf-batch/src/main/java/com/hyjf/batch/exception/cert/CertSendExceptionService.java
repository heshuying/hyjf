package com.hyjf.batch.exception.cert;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.CertErrLog;

import java.util.List;

/**
* 应急中心异常处理
* @author LiuBin
* @date 2017年8月1日 上午9:59:24
*
*/ 
public interface CertSendExceptionService extends BaseService {

	/**
	 * 检索上报失败的记录
	 * @return
	 */
	List<CertErrLog> selectCertSendErrLogList();

	/**
	 * 发送MQ
	 * @param data
	 * @param routingkey
	 */
	void sendToMQ(JSONArray data, String routingkey);

	/**
	 * 组装数据
	 * @param certErrLogs
	 * @return
	 */
	JSONArray getErrorData(List<CertErrLog> certErrLogs);
}

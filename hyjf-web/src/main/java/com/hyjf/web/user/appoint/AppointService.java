/**
 * Description：用户预约管理service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.user.appoint;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.FreezeList;
import com.hyjf.mybatis.model.customize.web.WebBorrowAppointCustomize;
import com.hyjf.web.BaseService;

public interface AppointService extends BaseService {

	/**
	 * 查询用户的预约总数
	 * @param params
	 * @return
	 */
		
	int countAppointRecordTotal(Map<String, Object> params);

	/**
	 * 查询用户预约数量
	 * @param params
	 * @return
	 */
		
	List<WebBorrowAppointCustomize> selectAppointRecordList(Map<String, Object> params);

	/**
	 * 校验预约信息
	 * @param orderId
	 * @param userId
	 * @return
	 */
		
	int checkAppointStatus(String orderId, Integer userId);

	/**
	 * 更新预约状态为取消预约，回滚用户的余额
	 * @param orderId
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
		
	boolean userCancelAppoint(String orderId, Integer userId) throws Exception;

	/**
	 * @param userId
	 * @param orderId
	 * @return
	 */
		
	FreezeList getUserFreeze(Integer userId, String orderId);

	/**
	 * 解冻订单
	 * @param userId
	 * @param orderId
	 * @param trxId
	 * @param freezeOrderDate
	 * @return
	 * @throws Exception 
	 */
		
	boolean unFreezeOrder(Integer userId, String orderId, String trxId, String orderDate) throws Exception;

}

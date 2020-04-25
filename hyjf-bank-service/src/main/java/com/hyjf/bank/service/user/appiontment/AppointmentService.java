package com.hyjf.bank.service.user.appiontment;


import java.util.Map;

import com.hyjf.bank.service.BaseService;

public interface AppointmentService  extends BaseService {

	
   	/**
	 * 
	 * @param usrId
	 * @param appointment  1授权，2取消授权
	 * @return
	 */
	public Map<String, Object> checkAppointmentStatus(Integer usrId, int appointment) ;
	/**
	 * 
	 * @method: updateUserAuthStatus
	 * @description: 更新预约授权数据			
	 *  @param tenderPlanType  P  部分授权  W 完全授权
	 *  @param appointment 预约授权状态：0：未授权1：已授权
	 * @param userId 
	 * @return: void
	* @mender: zhouxiaoshuai
	 * @date:   2016年7月26日 下午3:24:55
	 */
	public boolean updateUserAuthStatus(String tenderPlanType, String appointment, String userId);

 }



package com.hyjf.admin.exception.autotenderexception;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeListCustomize;

/**
 * 汇计划加入明细Service
 * 
 * @ClassName AccedeListService
 * @author LIBIN
 * @date 2017年8月16日 上午9:47:35
 */
public interface AutoTenderExceptionService extends BaseService {
	
	/**
	 * 检索加入明细件数
	 * 
	 * @Title countAccedeRecord
	 * @param form
	 * @return
	 */
	int countAccedeRecord(AutoTenderExceptionBean form);
	
	/**
	 * 检索加入明细列表
	 * 
	 * @Title selectAccedeRecordList
	 * @param form
	 * @return
	 */
	List<AdminPlanAccedeListCustomize> selectAccedeRecordList(AutoTenderExceptionBean form);
	
	/**
	 * 异常处理
	 * @author pcc
	 * @param userid
	 * @param planOrderId
	 * @param debtPlanNid
	 */
	String tenderExceptionAction(String userid, String planOrderId, String debtPlanNid);
}

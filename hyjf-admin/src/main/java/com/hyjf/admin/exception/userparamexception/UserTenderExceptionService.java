package com.hyjf.admin.exception.userparamexception;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowTender;

public interface UserTenderExceptionService extends BaseService {
	
	/**
	 * 查询指定时间范围的用户出借数据
	 * 
	 * @param repairStartDate
	 * @param repairEndDate
	 * @return
	 */
	public List<BorrowTender> selectBorrowTenderList(String repairStartDate, String repairEndDate);

	/**
	 * 更新用户的出借记录
	 * 
	 * @param borrowTender
	 * @param repairEndDate 
	 * @param repairStartDate 
	 */
	public void updateUserTender(BorrowTender borrowTender, String repairStartDate, String repairEndDate);
}

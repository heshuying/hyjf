package com.hyjf.admin.manager.borrow.appoint;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminBorrowAppointCustomize;

public interface BorrowAppointService extends BaseService {

	/**
	 * 根据开户参数，获取开户信息
	 * @param accountUser
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	 
	public List<AdminBorrowAppointCustomize> getRecordList(Map<String, Object> accountUser, int limitStart, int limitEnd);

	/**
	 * 统计开户记录总数
	 * @param userListCustomizeBean
	 * @return
	 */

	public int countRecordTotal(Map<String, Object> accountUser);

	
}

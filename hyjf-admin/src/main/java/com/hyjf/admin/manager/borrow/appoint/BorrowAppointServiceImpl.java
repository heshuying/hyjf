package com.hyjf.admin.manager.borrow.appoint;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.AdminBorrowAppointCustomize;

@Service
public class BorrowAppointServiceImpl extends BaseServiceImpl implements BorrowAppointService {

	/**
	 * 获取相应的用户预约列表
	 * 
	 * @return
	 */
	public List<AdminBorrowAppointCustomize> getRecordList(Map<String, Object> accountUser, int limitStart,int limitEnd) {
		
		if (limitStart == 0 || limitStart > 0) {
			accountUser.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			accountUser.put("limitEnd", limitEnd);
		}
		// 查询用户列表
		List<AdminBorrowAppointCustomize> users = adminBorrowAppointCustomizeMapper.selectAppointRecordList(accountUser);
		return users;

	}

	/**
	 * 统计用户的预约记录总数
	 * 
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countRecordTotal(Map<String, Object> accountUser) {
		return adminBorrowAppointCustomizeMapper.countAppointRecordTotal(accountUser);
	}

}

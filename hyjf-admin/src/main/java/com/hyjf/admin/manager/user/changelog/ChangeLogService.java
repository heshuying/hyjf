package com.hyjf.admin.manager.user.changelog;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.UsersChangeLog;
import com.hyjf.mybatis.model.customize.ChangeLogCustomize;

public interface ChangeLogService extends BaseService {

	/**
	 * 获取用户信息修改列表
	 * 
	 * @return
	 */
	public List<UsersChangeLog> getRecordList(ChangeLogBean userChangeLog, int limitStart, int limitEnd);

	public List<ChangeLogCustomize> getChangeLogList(ChangeLogBean userChangeLog);
	
	/**
	 * 
	 * 获取指定用户的信息修改列表
	 * @author yyc
	 * @param userid
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	public List<ChangeLogCustomize> getUserRecordList(ChangeLogBean userChangeLog, int limitStart, int limitEnd);
	    
	/**
	 * 获取用户信息修改记录数
	 * @param form
	 * @return
	 */
	public int countRecordTotal(ChangeLogCustomize userChangeLog);
	/**
	 * 合规四期(手机号加密显示)
	 * @param userChangeLog
	 * @return
	 * add by nxl
	 */
	List<ChangeLogCustomize> selectChangeLogList(ChangeLogBean userChangeLog);

}

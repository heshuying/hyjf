package com.hyjf.admin.maintenance.log;

import java.util.List;

import com.hyjf.mybatis.model.customize.MaintenanceLogCustomize;

public interface MaintenanceLogService {
	/**
	 * 查询日志列表
	 */
	public List<MaintenanceLogCustomize> queryLogList(MaintenanceLogCustomize maintenanceLogCustomize);

}

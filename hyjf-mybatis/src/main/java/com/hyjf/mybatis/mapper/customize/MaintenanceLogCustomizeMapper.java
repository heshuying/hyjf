package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.MaintenanceLogCustomize;

public interface MaintenanceLogCustomizeMapper {
	/**
	 * 所有日志信息
	 */
	public List<MaintenanceLogCustomize> selectAllLog(MaintenanceLogCustomize maintenanceLogCustomize);

}

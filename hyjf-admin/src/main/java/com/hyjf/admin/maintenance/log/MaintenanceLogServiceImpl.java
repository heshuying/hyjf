package com.hyjf.admin.maintenance.log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.mapper.customize.MaintenanceLogCustomizeMapper;
import com.hyjf.mybatis.model.customize.MaintenanceLogCustomize;

@Service
public class MaintenanceLogServiceImpl implements MaintenanceLogService{
	@Autowired
	protected MaintenanceLogCustomizeMapper maintenanceLogCustomizeMapper;
	
	@Override
	public List<MaintenanceLogCustomize> queryLogList(MaintenanceLogCustomize maintenanceLogCustomize) {
		List<MaintenanceLogCustomize> list = this.maintenanceLogCustomizeMapper.selectAllLog(maintenanceLogCustomize);
		return list;		
	}
	

}

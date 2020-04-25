package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminEmployeeLeaveCustomize;

public interface AdminEmployeeLeaveCustomizeMapper {

	public List<AdminEmployeeLeaveCustomize> selectUserLeaveByUserId(Map<String, Object> params);

}

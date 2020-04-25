package com.hyjf.mybatis.mapper.customize.admin;


import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.customize.admin.AdminEmployeeUserCustomize;

public interface AdminEmployeeUserCustomizeMapper {

	/**
	 * 获取员工信息
	 * 
	 * @param  
	 * @return
	 */
	AdminEmployeeUserCustomize selectEmployeeByUserId(@Param("userId") Integer userId);

}
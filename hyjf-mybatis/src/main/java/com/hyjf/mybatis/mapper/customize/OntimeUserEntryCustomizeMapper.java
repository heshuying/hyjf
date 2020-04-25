package com.hyjf.mybatis.mapper.customize;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hyjf.mybatis.model.auto.UsersInfo;

/**
 * 定时 员工入职
 * @author HBZ
 */
public interface OntimeUserEntryCustomizeMapper {


	/**
	 * 查询符合条件的入职员工 列表
	 * @param attribute
	 * @return
	 */
	public List<UsersInfo> queryEmployeeList();
	
	/**
	 * 客户变员工后，其所推荐客户变为‘有主单’
	 * @param referrer
	 * @return
	 */
	public int updateSpreadAttribute(@Param("referrer") Integer referrer);
	
	/**
     * 查询符合条件的入职员工
     * @param attribute
     * @return
     */
    public List<UsersInfo> queryEmployeeById(@Param("userId") String userId);
}




	
package com.hyjf.batch.user.leave;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.Users;

public interface OntimeUserLeaveService  extends BaseService {


	/**
	 * 离职员工
	 * @param ontime
	 * @return
	 */
	public List<Users> queryEmployeeList();
	/**
	 * 修改用户属性信息
	 * @param record,example
	 * @return
	 * @throws Exception 
	 */
	public boolean updateEmployeeByExampleSelective(Users record) throws Exception;

	/**
	 * 员工离职后，其所推荐客户变为‘无主单’
	 * @param referrer
	 * @return
	 */
	public int updateSpreadAttribute(Integer referrer);
	
}

	
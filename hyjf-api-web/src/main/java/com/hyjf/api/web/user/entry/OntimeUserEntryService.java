package com.hyjf.api.web.user.entry;

import java.util.List;

import com.hyjf.api.web.BaseService;
import com.hyjf.mybatis.model.auto.UsersInfo;

public interface OntimeUserEntryService  extends BaseService {


	/**
	 * 查询符合条件的员工信息
	 * @param ontime
	 * @return
	 */
    public List<UsersInfo> queryEmployeeEntryList(String userId);
	/**
	 * 修改用户属性信息
	 * @param record,example
	 * @return
	 */
	public int updateEmployeeByExampleSelective(UsersInfo record);
	/**
	 * 入职员工要删除推荐人
	 * @param integer
	 * @return
	 * @throws Exception 
	 */
	public boolean deleteReferrer(Integer integer) throws Exception;
	/**
	 * 客户变员工后，其所推荐客户变为‘有主单’
	 * @param referrer
	 * @return
	 */
	public int updateSpreadAttribute(Integer referrer);
	
}

	
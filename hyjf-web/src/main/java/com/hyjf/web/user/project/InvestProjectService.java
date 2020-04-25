/**
 * Description:我的出借service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.user.project;

import java.util.List;

import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserProjectListCustomize;
import com.hyjf.web.BaseService;

public interface InvestProjectService extends BaseService {

	/**
	 * 我的出借列表查询
	 * @param hzt
	 * @param i
	 * @param pageSize
	 * @return
	 */
	List<WebUserProjectListCustomize> selectUserProjectList(ProjectListBean hzt, int i, int pageSize);
	
	/**
	 * 我的出借数据总数
	 * @param form
	 * @return
	 */
	int countUserProjectRecordTotal(ProjectListBean form);

	/**
	 * 用户协议相应的用户出借列表
	 * @param borrowNid
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<WebUserInvestListCustomize> selectUserInvestList(UserInvestListBean form, int offset, int limit);
	
	/**
	 * 用户协议想用的用户出借总数
	 * @param borrowNid
	 * @return
	 */
	int countUserInvestRecordTotal(UserInvestListBean form);

	/**
	 * 用户出借为分期时的用户还款总数
	 * @param form
	 * @return
	 */
	int countProjectRepayRecordTotal(ProjectRepayListBean form);

	/**
	 * 用户出借为分期时用户还款信息
	 * @param form
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<WebProjectRepayListCustomize> selectProjectRepayList(ProjectRepayListBean form, int offset, int limit);
}

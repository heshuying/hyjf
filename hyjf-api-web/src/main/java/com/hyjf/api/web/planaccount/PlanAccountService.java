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
package com.hyjf.api.web.planaccount;

import java.util.List;
import java.util.Map;

import com.hyjf.api.web.BaseService;
import com.hyjf.mybatis.model.customize.apiweb.plan.WeChatPlanAccountCustomize;

public interface PlanAccountService extends BaseService {

	/**
	 * 我的出借列表查询
	 * 
	 * @param hzt
	 * @param i
	 * @param pageSize
	 * @return
	 */
	List<WeChatPlanAccountCustomize> selectUserProjectList(Map<String, Object> params);

	/**
	 * 我的出借数据总数
	 * 
	 * @param params
	 * @return
	 */
	int countUserProjectRecordTotal(Map<String, Object> params);

}

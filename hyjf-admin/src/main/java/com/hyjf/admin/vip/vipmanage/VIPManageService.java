/**
 * Description:用户信息管理业务处理类接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:05:26
 * Modification History:
 * Modified by : 
 */

package com.hyjf.admin.vip.vipmanage;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.VIPManageListCustomize;

/**
 * @author 王坤
 */

public interface VIPManageService extends BaseService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<VIPManageListCustomize> getRecordList(Map<String, Object> userListCustomizes, int limitStart, int limitEnd);

	/**
	 * 获取总记录条数
	 * 
	 * @param userListCustomizeBean
	 * @return
	 */

	public int countRecordTotal(Map<String, Object> userListCustomizeBean);


}

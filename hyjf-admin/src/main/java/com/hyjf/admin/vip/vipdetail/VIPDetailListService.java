package com.hyjf.admin.vip.vipdetail;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.VIPDetailListCustomize;

public interface VIPDetailListService extends BaseService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<VIPDetailListCustomize> getRecordList(Map<String, Object> param, int limitStart, int limitEnd);

	/**
	 * 获取总记录条数
	 * 
	 * @param userListCustomizeBean
	 * @return
	 */

	public int countRecordTotal(Map<String, Object> param);


}

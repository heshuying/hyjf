/**
 * Description:用户信息管理业务处理类接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 上午11:05:26
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.admin.manager.asset.assetlist;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.AssetDetailCustomize;
import com.hyjf.mybatis.model.customize.AssetListCustomize;

/**
 * @author liubin
 */

public interface AssetListService extends BaseService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<AssetListCustomize> getRecordList(Map<String, Object> conditionMap, int limitStart, int limitEnd);

	/**
	 * 获取记录数
	 * @param form
	 * @return
	 */
	public Integer getRecordCount(Map<String, Object> conditionMap);
	
	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public AssetDetailCustomize getDetailById(String assetId, String instCode);

	/**
	 * 总额合计
	 * 
	 * @return
	 */
	public BigDecimal sumAccount(Map<String, Object> conditionMap);
}

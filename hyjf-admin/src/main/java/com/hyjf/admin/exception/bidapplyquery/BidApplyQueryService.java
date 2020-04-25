/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
/**
 * 
 */
package com.hyjf.admin.exception.bidapplyquery;

import com.alibaba.fastjson.JSONObject;

/**
 * @author libin
 * 出借人投标申请查询Service
 * @version BidApplyQueryService.java, v0.1 2018年8月16日 上午10:06:50
 */
public interface BidApplyQueryService {
	
	/**
	 * 出借人投标申请查询
	 * @param form
	 * @return
	 * @author libin
	 * @date 2018年8月16日 上午10:06:50
	 */
	public JSONObject bidApplyQuerySearch(BidApplyQueryBean form);
}

package com.hyjf.admin.exception.singletradeinfo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.BorrowRegistCustomize;

/**
* 接口：单笔资金类业务交易查询  Service接口类
* @author LiuBin
* @date 2017年7月31日 上午9:31:11
* 
*/
public interface SingleTradeInfoService {

	/**
	 * 单笔资金类业务交易查询
	 * @param form
	 * @return
	 * @author LiuBin
	 * @date 2017年7月31日 上午9:41:10
	 */
	public JSONObject singleTradeInfoSearch(SingleTradeInfoBean form);
}

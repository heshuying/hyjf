/**
 * Description:会员用户开户记录初始化列表查询
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */

package com.hyjf.mybatis.mapper.customize.web;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.web.WebUserRechargeListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserTradeListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserWithdrawListCustomize;

public interface WebUserTradeDetailCustomizeMapper {

	List<WebUserTradeListCustomize> selectUserTradeList(Map<String, Object> params);

	int countUserTradeRecordTotal(Map<String, Object> params);

	List<WebUserRechargeListCustomize> selectUserRechargeList(Map<String, Object> params);

	int countUserRechargeRecordTotal(Map<String, Object> params);

	List<WebUserWithdrawListCustomize> selectUserWithdrawList(Map<String, Object> params);

	int countUserWithdrawRecordTotal(Map<String, Object> params);

}

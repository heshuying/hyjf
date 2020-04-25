/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2018
 * Company: HYJF Corporation
 * @author: PC-LIUSHOUYI
 * @version: 1.0
 * Created at: 2018年3月29日 下午5:40:11
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.mybatis.mapper.customize.web.hjhreinvestdetail;

import java.util.List;

import com.hyjf.mybatis.model.customize.web.hjh.HjhReInvestDetailCustomize;

/**
 * @author PC-LIUSHOUYI
 */

public interface HjhReInvestDetailCustomizeMapper {
	
	/**
	 * 复投详情总数
	 * 
	 * @param hjhReInvestDetailCustomize
	 * @return
	 */
	Integer queryReInvestDetailCount(HjhReInvestDetailCustomize hjhReInvestDetailCustomize);
	
	/**
	 * 复投详情
	 * 
	 * @param hjhReInvestDetailCustomize
	 * @return
	 */
	List<HjhReInvestDetailCustomize> queryReInvestDetails(HjhReInvestDetailCustomize hjhReInvestDetailCustomize);
	
	/**
	 * 合计值
	 * 
	 * @param hjhReInvestDetailCustomize
	 * @return
	 */
	String queryReInvestDetailTotal(HjhReInvestDetailCustomize hjhReInvestDetailCustomize);
	
	/**
	 * 导出
	 * 
	 * @param hjhReInvestDetailCustomize
	 * @return
	 */
	List<HjhReInvestDetailCustomize> exportReInvestDetails(HjhReInvestDetailCustomize hjhReInvestDetailCustomize);
}

	
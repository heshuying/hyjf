/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: jijun
 * @version: 1.0
 * Created at: 2018年09月10日 下午2:11:50
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.api.aems.borrowlist;

import java.util.List;

import com.hyjf.api.aems.group.AemsOrganizationStructureBean;
import com.hyjf.api.aems.invest.AemsInvestListRequest;
import com.hyjf.api.aems.invest.AemsInvestRepayBean;
import com.hyjf.api.aems.invest.AemsRepayListRequest;
import com.hyjf.api.server.group.OrganizationStructureBean;
import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.customize.web.ApiProjectListCustomize;
import com.hyjf.mybatis.model.customize.web.Empinfo;
import com.hyjf.mybatis.model.customize.web.InvestListCustomize;
import com.hyjf.mybatis.model.customize.web.InvestRepayCustomize;
import com.hyjf.mybatis.model.customize.web.OrganizationStructure;
import com.hyjf.mybatis.model.customize.web.RepayListCustomize;

/**
 * 标的列表查询
 * @author jijun 20180907
 */

public interface AemsBorrowListService extends BaseService {

	List<ApiProjectListCustomize> searchProjectListNew(AemsBorrowListRequestBean bean);

	/**
	 * 获取投资详细信息
	 * @param bean{instCode：机构编号（必填）,startTime:开始时间（必填），endTime:结束时间（必填），account：电子账号（选填），borrowNid：标的编号}
	 * @return
	 */
	List<InvestListCustomize> searchInvestListNew(AemsInvestListRequest bean);

	/**
	 * 获取回款记录信息
	 * @param bean{instCode：机构编号（必填）,startTime:开始时间（必填），endTime:结束时间（必填），account：电子账号（选填），borrowNid：标的编号}
	 * @return
	 */
	List<RepayListCustomize> searchRepayList(AemsRepayListRequest bean);
	
	/**
	 * 获取集团组织架构信息
	 * @param bean
	 * @return
	 */
	List<OrganizationStructure> searchOrganizationList(AemsOrganizationStructureBean bean);

	/**
	 * 获取员工信息
	 * @param bean
	 * @return
	 */
	List<Empinfo> searchEmpInfoList(AemsOrganizationStructureBean bean);

	/**
	 * 根据汇盈金服用户ID获取用户开户信息，用户ID为String字符串，例如：1，,13,115
	 * @param bean
	 * @return
	 */
	List<InvestRepayCustomize> searchInvestRepaysListNew(AemsInvestRepayBean bean);
}

	
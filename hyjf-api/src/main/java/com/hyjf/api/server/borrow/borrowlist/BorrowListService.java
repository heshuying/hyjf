/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年10月12日 下午2:11:50
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.api.server.borrow.borrowlist;

import com.hyjf.api.server.group.OrganizationStructureBean;
import com.hyjf.api.server.invest.InvestListRequest;
import com.hyjf.api.server.invest.InvestRepayBean;
import com.hyjf.api.server.invest.RepayListRequest;
import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.customize.web.*;

import java.util.List;

/**
 * @author lb
 */

public interface BorrowListService extends BaseService {
	List<ApiProjectListCustomize> searchProjectListNew(BorrowListRequestBean bean);

	/**
	 * 获取出借详细信息
	 * @param bean{instCode：机构编号（必填）,startTime:开始时间（必填），endTime:结束时间（必填），account：电子账号（选填），borrowNid：标的编号}
	 * @return
	 */
	List<InvestListCustomize> searchInvestListNew(InvestListRequest bean);

	/**
	 * 获取回款记录信息
	 * @param bean{instCode：机构编号（必填）,startTime:开始时间（必填），endTime:结束时间（必填），account：电子账号（选填），borrowNid：标的编号}
	 * @return
	 */
	List<RepayListCustomize> searchRepayList(RepayListRequest bean);
	
	/**
	 * 获取集团组织架构信息
	 * @param bean
	 * @return
	 */
	List<OrganizationStructure> searchOrganizationList(
			OrganizationStructureBean bean);

	/**
	 * 获取员工信息
	 * @param bean
	 * @return
	 */
	List<Empinfo> searchEmpInfoList(OrganizationStructureBean bean);

	/**
	 * 根据汇盈金服用户ID获取用户开户信息，用户ID为String字符串，例如：1，,13,115
	 * @param bean
	 * @return
	 */
	List<InvestRepayCustomize> searchInvestRepaysListNew(InvestRepayBean bean);
}

	
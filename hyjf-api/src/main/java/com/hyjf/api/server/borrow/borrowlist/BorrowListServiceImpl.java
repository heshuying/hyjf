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
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.web.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lb
 */

@Service
public class BorrowListServiceImpl extends BaseServiceImpl implements BorrowListService {

	private Logger logger = LoggerFactory.getLogger(BorrowListServiceImpl.class);
	/**
	 * 取得可投标的信息
	 * 
	 * @param bean
	 * @return
	 * @author lb
	 */
	@Override
	public List<ApiProjectListCustomize> searchProjectListNew(BorrowListRequestBean bean) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("projectType", "HZT");
		params.put("borrowClass", "");
		params.put("status", bean.getBorrowStatus());// 获取 出借中

		// add by xiashuqing 20171130 begin
		// 定向标过滤
		params.put("publishInstCode", bean.getInstCode());
		// add by xiashuqing 20171130 end

		params.put("limitStart", bean.getLimitStart());
		params.put("limitEnd", bean.getLimitEnd());
		List<ApiProjectListCustomize> list = apiProjectListCustomizeMapper.selectProjectListNew(params);
		return list;
	}

	/**
	 * 获取出借详细信息
	 * 
	 * @param bean{instCode：机构编号（必填）,startTime:开始时间（必填），
	 *            endTime:结束时间（必填），account：电子账号（选填），borrowNid：标的编号}
	 * @return
	 */
	@Override
	public List<InvestListCustomize> searchInvestListNew(InvestListRequest bean) {
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("instCode", bean.getInstCode());
		params.put("startTime", bean.getStartTime());
		params.put("endTime", bean.getEndTime());
		params.put("account", bean.getAccountId());
		params.put("borrowNid", bean.getBorrowNid());

		params.put("limitStart",bean.getLimitStart());
		params.put("limitEnd",bean.getLimitEnd());

		List<InvestListCustomize> list = apiProjectListCustomizeMapper.searchInvestListNew(params);
		return list;
	}


	/**
	 * 获取回款记录信息
	 *
	 * @param bean{instCode：机构编号（必填）,startTime:开始时间（必填），
	 *            endTime:结束时间（必填），account：电子账号（选填），borrowNid：标的编号}
	 * @return
	 */
	@Override
	public List<RepayListCustomize> searchRepayList(RepayListRequest bean) {
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("instCode", bean.getInstCode());
		params.put("startTime", bean.getStartTime());
		params.put("endTime", bean.getEndTime());
		params.put("account", bean.getAccountId());
		params.put("borrowNid", bean.getBorrowNid());

		params.put("limitStart",bean.getLimitStart());
		params.put("limitEnd",bean.getLimitEnd());

		List<RepayListCustomize> list = apiProjectListCustomizeMapper.searchRepayList(params);
		return list;
	}
	
	@Override
	public List<OrganizationStructure> searchOrganizationList(
			OrganizationStructureBean bean) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<OrganizationStructure> list = new ArrayList<>();

		try {
			list = apiProjectListCustomizeMapper.searchOrganizationList(params);
		} catch (Exception e) {
			logger.error("查询异常",e);
		}
		return list;
	}

	@Override
	public List<Empinfo> searchEmpInfoList(OrganizationStructureBean bean) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("limitStart",bean.getLimitStart());
		params.put("limitEnd",bean.getLimitEnd());

		List<Empinfo> list = apiProjectListCustomizeMapper.searchEmpInfoList(params);
		return list;
	}

	@Override
	public List<InvestRepayCustomize> searchInvestRepaysListNew(
			InvestRepayBean bean) {
		List<Integer> userIdList = new ArrayList<>();
		String userIds = bean.getUserIds();
		String uId[] = userIds.split(","); 
		for (int i = 0; i < uId.length; i++) {
			if(!uId[i].isEmpty()){
				userIdList.add(Integer.parseInt(uId[i]));
			}
		}
		List<InvestRepayCustomize> list = apiProjectListCustomizeMapper.searchInvestRepaysListNew(userIdList);
		return list;
	}
}

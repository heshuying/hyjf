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
package com.hyjf.api.aems.borrowlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyjf.api.aems.group.AemsOrganizationStructureBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.api.aems.invest.AemsInvestListRequest;
import com.hyjf.api.aems.invest.AemsInvestRepayBean;
import com.hyjf.api.aems.invest.AemsRepayListRequest;
import com.hyjf.api.server.group.OrganizationStructureBean;
import com.hyjf.base.service.BaseServiceImpl;
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

@Service
public class AemsBorrowListServiceImpl extends BaseServiceImpl implements AemsBorrowListService {

	private Logger logger = LoggerFactory.getLogger(AemsBorrowListServiceImpl.class);
	/**
	 * 取得可投标的信息
	 * 
	 * @param bean
	 * @return
	 * @author lb
	 */
	@Override
	public List<ApiProjectListCustomize> searchProjectListNew(AemsBorrowListRequestBean bean) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("projectType", "HZT");
		params.put("borrowClass", "");
		params.put("status", bean.getBorrowStatus());// 获取 投资中

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
	 * 获取投资详细信息
	 * 
	 * @param bean{instCode：机构编号（必填）,startTime:开始时间（必填），
	 *            endTime:结束时间（必填），account：电子账号（选填），borrowNid：标的编号}
	 * @return
	 */
	@Override
	public List<InvestListCustomize> searchInvestListNew(AemsInvestListRequest bean) {
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
	public List<RepayListCustomize> searchRepayList(AemsRepayListRequest bean) {
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
	public List<OrganizationStructure> searchOrganizationList(AemsOrganizationStructureBean bean) {
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
	public List<Empinfo> searchEmpInfoList(AemsOrganizationStructureBean bean) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("limitStart",bean.getLimitStart());
		params.put("limitEnd",bean.getLimitEnd());

		List<Empinfo> list = apiProjectListCustomizeMapper.searchEmpInfoList(params);
		return list;
	}

	@Override
	public List<InvestRepayCustomize> searchInvestRepaysListNew(AemsInvestRepayBean bean) {
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

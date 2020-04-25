/**
 * Description:我的出借service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.app.user.project;

import java.util.List;
import java.util.Map;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.customize.app.*;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;

public interface InvestProjectService extends BaseService {

	/**
	 * 我的出借（已回款）
	 * 
	 * @param params
	 * @return
	 */
	List<AppAlreadyRepayListCustomize> selectAlreadyRepayList(Map<String, Object> params);

	/**
	 * 统计我的出借（已回款）项目总数
	 * 
	 * @param params
	 * @return
	 */
	int countAlreadyRepayListRecordTotal(Map<String, Object> params);

	/**
	 * 还款计划
	 * 
	 * @param params
	 * @return
	 */
	List<AppRepayPlanListCustomize> selectRepayPlanList(Map<String, Object> params);

	/**
	 * 统计还款计划总数
	 * 
	 * @param params
	 * @return
	 */

	int countRepayPlanListRecordTotal(Map<String, Object> params);

	Borrow selectBorrowByBorrowNid(String borrowNid);

	BorrowStyle selectBorrowStyleByStyle(String borrowStyle);

	int countRepayRecoverListRecordTotal(Map<String, Object> params);

	List<AppRepayPlanListCustomize> selectRepayRecoverList(Map<String, Object> params);

	List<AppProjectContractRecoverPlanCustomize> selectProjectContractRecoverPlan(Map<String, Object> params);

	AppProjectContractDetailCustomize selectProjectContractDetail(Map<String, Object> params);

	List<AppRepayPlanListCustomize> selectCouponRepayRecoverList(Map<String, Object> params);

	int countCouponRepayRecoverListRecordTotal(Map<String, Object> params);

	String selectReceivedInterest(Map<String, Object> params);

	/**
	 * 
	 * 获取回款中的项目详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public AppRepayDetailCustomize selectRepayDetail(Map<String, Object> params);

	/**
	 * 
	 * 优惠券出借项目详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public AppRepayDetailCustomize selectCouponRepayDetail(Map<String, Object> params);

	/**
	 * 
	 * 获取出借中的项目详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public AppRepayDetailCustomize selectInvestProjectDetail(Map<String, Object> params);

	/**
	 * 
	 * 获取出借中的优惠券项目详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public AppRepayDetailCustomize selectCouponInvestProjectDetail(Map<String, Object> params);

	/***
	 * 
	 * 获取已回款的项目详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public AppRepayDetailCustomize selectRepayedProjectDetail(Map<String, Object> params);

	/**
	 * 
	 * 获取已回款的优惠券项目详情
	 * 
	 * @author liuyang
	 * @param params
	 * @return
	 */
	public AppRepayDetailCustomize selectCouponRepayedProjectDetail(Map<String, Object> params);

	String getInvestingData(String userId, String host, String page, String pageSize);

	String getRepayedData(String userId, String host, String page, String pageSize);

	String getRepayData(String userId, String host, String hostContact, String page, String pageSize);

	CouponConfig getCouponConfigByOrderId(String nid);

	List<WebUserInvestListCustomize> selectUserInvestList(String borrowNid, Integer userId, String nid, int limitStart,
			int limitEnd);

	/**
	 * 判断用户所在渠道是否允许债转
	 * @param userId
	 * @return
	 */
	boolean isAllowChannelAttorn(Integer userId);
}

package com.hyjf.app.user.plan;

import java.util.List;
import java.util.Map;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.HjhRepay;
import com.hyjf.mybatis.model.customize.app.AppCouponCustomize;
import com.hyjf.mybatis.model.customize.app.AppMyHjhDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppMyPlanCustomize;
import com.hyjf.mybatis.model.customize.web.CurrentHoldRepayMentPlanListCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistListCustomize;

/**
 * @author xiasq
 * @version MyPlanService, v0.1 2017/11/9 11:18
 */
public interface MyPlanService extends BaseService {

	/**
	 * app获取用户持有计划，不包含汇天金，和web页面略有不同
	 * 
	 * @param params
	 * @return
	 */
	List<AppMyPlanCustomize> selectAppMyPlanList(Map<String, Object> params);

	/**
	 *
	 * 查询用户汇计划出借明细
	 * 
	 * @author pcc
	 * @param params
	 * @return
	 */
	AppMyHjhDetailCustomize selectUserHjhInvistDetail(Map<String, Object> params);

	/**
	 *
	 * 查询用户汇计划出借关联项目
	 * 
	 * @author pcc
	 * @param params
	 * @return
	 */
	List<UserHjhInvistListCustomize> selectUserHjhInvistBorrowList(Map<String, Object> params);

	AppCouponCustomize selectAppMyPlanCouponInfo(Map<String, Object> params);

	/**
	 * 查询我的汇计划总数
	 * 
	 * @param params
	 * @return
	 */
	Integer countAppMyPlan(Map<String, Object> params);

	/**
	 * 获取我的计划待收总额
	 * 
	 * @param params
	 * @return
	 */
	String getMyPlanWaitAmountTotal(Integer userId);

	/**
	 * 获取优惠券还款计划
	 * 
	 * @param nid
	 * @return
	 */
	List<CurrentHoldRepayMentPlanListCustomize> getPlanCouponRepayment(String nid);

	/**
	 * 获取汇计划还款计划
	 * 
	 * @param orderId
	 * @return
	 */
	HjhRepay getPlanRepayment(String orderId);
}

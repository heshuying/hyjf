package com.hyjf.app.user.plan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.HjhRepay;
import com.hyjf.mybatis.model.auto.HjhRepayExample;
import com.hyjf.mybatis.model.customize.app.AppCouponCustomize;
import com.hyjf.mybatis.model.customize.app.AppMyHjhDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppMyPlanCustomize;
import com.hyjf.mybatis.model.customize.web.CurrentHoldRepayMentPlanListCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistListCustomize;

/**
 * @author xiasq
 * @version PlanServiceImpl, v0.1 2017/11/9 11:19
 */
@Service
public class MyPlanServiceImpl extends BaseServiceImpl implements MyPlanService {
	private Logger logger = LoggerFactory.getLogger(MyPlanServiceImpl.class);

	/**
	 * app获取用户持有计划，不包含汇天金，和web页面略有不同
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public List<AppMyPlanCustomize> selectAppMyPlanList(Map<String, Object> params) {
		List<AppMyPlanCustomize> planList = assetManageCustomizeMapper.selectAppMyPlanList(params);
		CommonUtils.convertNullToEmptyString(planList);
		return planList;
	}

	/**
	 *
	 * 查询用户汇计划出借明细
	 * 
	 * @author pcc
	 * @param params
	 * @return
	 */
	@Override
	public AppMyHjhDetailCustomize selectUserHjhInvistDetail(Map<String, Object> params) {
		return hjhPlanCustomizeMapper.selectAppMyHjhDetail(params);
	}

	/**
	 *
	 * 查询用户汇计划出借关联项目
	 * 
	 * @author pcc
	 * @param params
	 * @return
	 */
	@Override
	public List<UserHjhInvistListCustomize> selectUserHjhInvistBorrowList(Map<String, Object> params) {
		return hjhPlanCustomizeMapper.selectUserHjhInvistBorrowList(params);
	}

	@Override
	public AppCouponCustomize selectAppMyPlanCouponInfo(Map<String, Object> params) {
		AppCouponCustomize appCouponCustomize = assetManageCustomizeMapper.selectAppMyPlanCouponInfo(params);
		return appCouponCustomize;
	}

	/**
	 * 查询我的汇计划总数
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public Integer countAppMyPlan(Map<String, Object> params) {
		return assetManageCustomizeMapper.countAppMyPlan(params);
	}

	/**
	 * 获取我的计划待收总额
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public String getMyPlanWaitAmountTotal(Integer userId) {
		Account account = this.getAccount(userId);
		if (account != null && account.getPlanAccountWait() != null) {
			return account.getPlanAccountWait().toString();
		}
		return "0.00";
	}

	/**
	 * 获取优惠券还款计划
	 * 
	 * @param nid
	 * @return
	 */
	@Override
	public List<CurrentHoldRepayMentPlanListCustomize> getPlanCouponRepayment(String nid) {
		logger.info("getRepaymentPlan, nid is: {}", nid);
		Map<String, Object> params = new HashMap<>();
		params.put("nid", nid);
		return assetManageCustomizeMapper.couponRepaymentPlanList(params);
	}

	/**
	 * 获取汇计划还款计划
	 * 
	 * @param orderId
	 * @return
	 */
	@Override
	public HjhRepay getPlanRepayment(String orderId) {
		HjhRepayExample hjhRepayExample = new HjhRepayExample();
		HjhRepayExample.Criteria criteria = hjhRepayExample.createCriteria();
		criteria.andAccedeOrderIdEqualTo(orderId);
		List<HjhRepay> list = hjhRepayMapper.selectByExample(hjhRepayExample);
		if (!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}
}

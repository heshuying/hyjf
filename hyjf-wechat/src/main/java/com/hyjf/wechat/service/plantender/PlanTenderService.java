package com.hyjf.wechat.service.plantender;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import com.hyjf.mybatis.model.customize.coupon.PlanCouponResultBean;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.base.BaseService;
import com.hyjf.wechat.controller.user.plantender.InvestInfoResultVo;

/**
 * 
 * 计划类出借相关
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月1日
 * @see 下午2:44:00
 */
public interface PlanTenderService extends BaseService {

	HjhPlan getPlanByNid(String planNid);

	DebtPlanDetailCustomize selectDebtPlanDetail(String planNid);

	JSONObject getHJHProjectUserCoupon(String planNid, Integer userId, String money, String platform);

	BigDecimal setProspectiveEarnings(InvestInfoResultVo resultVo, UserCouponConfigCustomize couponConfig, String planNid, Integer userId, String platform, String money);

	UserCouponConfigCustomize getCouponById(String couponId);

	boolean isHjhCouponAvailable(String userCouponId, JSONObject result);

}

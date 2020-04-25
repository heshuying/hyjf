package com.hyjf.wechat.service.plantender;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
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
import com.hyjf.wechat.base.BaseServiceImpl;
import com.hyjf.wechat.controller.user.plantender.InvestInfoResultVo;

/**
 * 
 * 计划类出借服务
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月1日
 * @see 下午2:44:24
 */
@Service
public class PlanTenderServiceImpl extends BaseServiceImpl implements PlanTenderService{

    @Override
	public HjhPlan getPlanByNid(String planNid) {
        HjhPlanExample example = new HjhPlanExample();
        HjhPlanExample.Criteria cri = example.createCriteria();
        cri.andPlanNidEqualTo(planNid);
        List<HjhPlan> debtBorrowList = hjhPlanMapper.selectByExample(example);
        if (debtBorrowList != null && debtBorrowList.size() > 0) {
            return debtBorrowList.get(0);
        } else {
            return null;
        }
    }
    
    @Override
	public DebtPlanDetailCustomize selectDebtPlanDetail(String planNid) {
        DebtPlanDetailCustomize planDetail = this.hjhPlanCustomizeMapper.selectDebtPlanDetail(planNid);
        return planDetail;
    }
    
	@Override
	public JSONObject getHJHProjectUserCoupon(String planNid, Integer userId,
			String money, String platform) {
		JSONObject json = new JSONObject();
        JSONObject jsonObject = CommonSoaUtils.getProjectAvailableUserCoupon(platform, planNid, money, userId);
        
        
        json.put("availableCouponList", JSONArray.parseArray(
                JSONObject.toJSONString(jsonObject.get("availableCouponList")), PlanCouponResultBean.class));
        json.put("notAvailableCouponList", JSONArray.parseArray(
                JSONObject.toJSONString(jsonObject.get("notAvailableCouponList")), PlanCouponResultBean.class));
        json.put("availableCouponListCount", jsonObject.get("availableCouponListCount"));
        json.put("notAvailableCouponListCount", jsonObject.get("notAvailableCouponListCount"));
		return json;
	}
	
    @Override
	public BigDecimal setProspectiveEarnings(InvestInfoResultVo resultVo, UserCouponConfigCustomize couponConfig,
            String planNid, Integer userId, String platform, String money) {
            DecimalFormat df = CustomConstants.DF_FOR_VIEW;
            df.setRoundingMode(RoundingMode.FLOOR);
            HjhPlan plan = getPlanByNid(planNid);
            BigDecimal earnings = new BigDecimal("0");
            if (null != plan) {
                // 如果出借金额不为空
    			if (!StringUtils.isBlank(money) && (new BigDecimal(money).compareTo(BigDecimal.ZERO) > 0)
    					|| !(StringUtils.isEmpty(money) && couponConfig != null && couponConfig.getCouponType() == 1
    							&& couponConfig.getAddFlg() == 1)) {
                    // 收益率
                    BigDecimal borrowApr = plan.getExpectApr();
                    // 周期
                    Integer borrowPeriod = plan.getLockPeriod();
                    // 还款方式
                    String borrowStyle = plan.getBorrowStyle();//endday
                    
                    if (StringUtils.isNotEmpty(borrowStyle)) {
                        if (StringUtils.equals("endday", borrowStyle)){
                            // 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*年化收益÷365*锁定期；
                            earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(money), borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
                        } else {
                            // 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
                            earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(money), borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
                        }
                        resultVo.setBorrowInterest("预期收益 " + df.format(earnings) + "元");
                        resultVo.setProspectiveEarnings(df.format(earnings) + "元");
                        resultVo.setInterest(df.format(earnings));
                    }   
                } else {
                    resultVo.setBorrowInterest("预期收益 " + "0元");
                    resultVo.setProspectiveEarnings(df.format(earnings) + "元");
                    resultVo.setInterest(df.format(earnings));
                }
            }
            return earnings;
        }

    
	/**
	 * 获取最优优惠券
	 * 
	 * @param couponId
	 * @return
	 */

	@Override
	public UserCouponConfigCustomize getCouponById(String couponId) {
		UserCouponConfigCustomize couponConfig = couponConfigCustomizeMapper.getBestCouponById(couponId);
		return couponConfig;
	}
	
	/**
	 * 优惠券判断优惠券是否可用
	 * @param userCouponId
	 * @param result
	 * @return
	 */
	@Override
	public boolean isHjhCouponAvailable(String userCouponId, JSONObject result) {
		String obj = JSONObject.toJSONString(result.get("availableCouponList"));
		List<PlanCouponResultBean> coupons = JSONArray.parseArray(obj, PlanCouponResultBean.class);
		if (!CollectionUtils.isEmpty(coupons)) {
			for (PlanCouponResultBean couponBean : coupons) {
				if (userCouponId.equals(couponBean.getUserCouponId())) {
					return true;
				}
			}
		}
		return false;
	}

}

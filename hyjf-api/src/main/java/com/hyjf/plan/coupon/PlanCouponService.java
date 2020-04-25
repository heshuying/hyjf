package com.hyjf.plan.coupon;

import java.util.Map;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;

public interface PlanCouponService extends BaseService {
    /**
     * 
     * 获取用户优惠券总张数
     * @author pcc
     * @param paramBean
     * @return
     */
    PlanCountCouponUsersResultBean countCouponUsers(PlanCouponBean paramBean);
    /**
     * 
     * 获取用户可用优惠券总张数
     * @author pcc
     * @param planNid
     * @param userId
     * @param money
     * @param platform
     * @return
     */
    Integer getUserCouponAvailableCount(String planNid, Integer userId, String money, String platform);
    /**
     * 
     * 获取用户最优优惠券信息
     * @author pcc
     * @param planNid
     * @param userId
     * @param money
     * @param platform
     * @return
     */
    UserCouponConfigCustomize getBestCoupon(String planNid, Integer userId, String money, String platform);
    /**
     * 
     * 计算优惠券预期收益
     * @author pcc
     * @param planNid
     * @param couponGrantId
     * @param money
     * @return
     */
    String getCouponInterest(String planNid, String couponGrantId, String money);
    /**
     * 
     * 根据borrowNid和用户id获取用户可用优惠券和不可用优惠券列表
     * @author pcc
     * @param paramBean
     * @return
     */
    PlanAvailableCouponResultBean getProjectAvailableUserCoupon(PlanCouponBean paramBean);
    /**
     * 
     * 查询项目信息
     * @author pcc
     * @param planNid
     * @return
     */
    DebtPlanDetailCustomize selectDebtPlanDetail(String planNid);

    CouponConfigCustomizeV2 getCouponUser(String couponGrantId, int userId);

    DebtPlan getPlanByNid(String planNid);
    /**
     * 
     * 体验金出借
     * @author pcc
     * @param couponGrantId
     * @param planNid
     * @param ordDate
     * @param userId
     * @param account
     * @param ip
     * @param client
     * @param couponOldTime
     * @param ordId
     * @param retMap
     * @return
     * @throws Exception
     */
    boolean updateCouponTender(String couponGrantId, String planNid, String ordDate, int userId, String account,
        String ip, int client, int couponOldTime, String ordId, Map<String, Object> retMap) throws Exception;
    /**
     * 计划满标或进入锁定期，更新优惠券还款收益及还款时间等
     * 此处为方法说明
     * @author pcc
     * @param nid
     * @param recoverTime
     * @return
     * @throws RuntimeException
     */
    void updateCouponRecoverHtj(String planNid) throws RuntimeException;
    /**
     * 
     * 获取计划信息
     * @author pcc
     * @param planNid
     * @return
     */
    HjhPlan getHjhPlanByPlanNid(String planNid);
    
}

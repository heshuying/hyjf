package com.hyjf.app.user.coupon.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponCheckUtil {

    @Autowired
    private CouponCheckService couponCheckService;
    
    /**
     * 
     * 验证活动是否过期
     * @author pcc
     * @param activityId  活动id
     * @return
     */
    public String checkActivityIfAvailable(String activityId) {
        String msg=couponCheckService.checkActivityIfAvailable(activityId);
        return msg;
//        return "";
    }
    
    /**
     * 
     * 验证优惠券是否过期
     * @author pcc
     * @param couponUserId 用户优惠券id
     * @return
     */
    public String checkCouponUserExpiryDate(String couponUserId) {
        String msg=couponCheckService.checkCouponUserExpiryDate(couponUserId);
        return msg;
    }
    
    
    /**
     * 
     * 验证优惠券是否可用
     * @author pcc
     * @param couponUserId 用户优惠券id
     * @return
     */
    public String checkCouponUserFlag(String couponUserId) {
        String msg=couponCheckService.checkCouponUserFlag(couponUserId);
        return msg;
    }
    
    
    /**
     * 
     * 验证用户优惠券使用项目
     * @author pcc
     * @param couponUserId 用户优惠券id
     * @param projectType 出借项目Code
     * @return
     */
    public String checkCouponUserProjectType(String couponUserId,String projectTypeCode) {
        String msg=couponCheckService.checkCouponUserProjectType(couponUserId,projectTypeCode);
        return msg;
    }
    
    
    /**
     * 
     * 验证用户优惠券使用项目时长
     * @author pcc
     * @param couponUserId 用户优惠券id
     * @param borrowNid 出借标的Nid
     * @return
     */
    public String checkProjectTerm(String couponUserId,String borrowNid) {
        String msg=couponCheckService.checkProjectTerm(couponUserId,borrowNid);
        return msg;
    }
    
    /**
     * 
     * 验证用户优惠券使用项目金额
     * @author pcc
     * @param couponUserId 用户优惠券id
     * @param borrowNid 出借标的Nid
     * @return
     */
    public String checkProjectAmount(String couponUserId,String borrowNid) {
        String msg=couponCheckService.checkProjectAmount(couponUserId,borrowNid);
        return msg;
    }
    
    
    /**
     * 
     * 判断活动适用终端
     * @author pcc
     * @param activityId 活动id
     * @param platform 平台标示
     * @return
     */
    public String checkActivityPlatform(String activityId,String platform) {
        String msg=couponCheckService.checkActivityPlatform(activityId,platform);
        return msg;
    }
    
    /**
     * 
     * 判断用户是否是老用户
     * @author pcc
     * @param activityId 活动id
     * @param platform 平台标示
     * @return
     */
    public String checkOldUser(String activityId,String userId) {
        String msg=couponCheckService.checkOldUser(activityId,userId);
        return msg;
    }
    
    /**
     * 
     * 判断用户是否已经参与活动并发放优惠券
     * @author pcc
     * @param activityId 活动id
     * @param platform 平台标示
     * @return
     */
    public String checkActivityIfInvolvement(String activityId,String userId) {
        String msg=couponCheckService.checkActivityIfInvolvement(activityId,userId);
        return msg;
    }
}

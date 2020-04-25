package com.hyjf.app.user.coupon.util;

import com.hyjf.app.BaseService;

public interface CouponCheckService extends BaseService {
    /**
     * 
     * 验证活动是否过期
     * @author pcc
     * @param activityId
     * @return
     */
    String checkActivityIfAvailable(String activityId);
    /**
     * 
     * 验证优惠券是否有效
     * @author pcc
     * @param activityId
     * @return
     */
    String checkCouponUserExpiryDate(String couponUserId);
    /**
     * 
     * 验证用户优惠券使用项目
     * @author pcc
     * @param couponUserId 用户优惠券id
     * @param projectType 出借项目Code
     * @return
     */
    String checkCouponUserProjectType(String couponUserId, String projectTypeCode);
    /**
     * 
     * 验证优惠券是否可用
     * @author pcc
     * @param activityId
     * @return
     */
    String checkCouponUserFlag(String couponUserId);
    /**
     * 
     * 验证用户优惠券使用项目时长
     * @author pcc
     * @param couponUserId 用户优惠券id
     * @param borrowNid 出借标的Nid
     * @return
     */
    String checkProjectTerm(String couponUserId, String borrowNid);
    /**
     * 
     * 验证用户优惠券使用项目金额
     * @author pcc
     * @param couponUserId 用户优惠券id
     * @param borrowNid 出借标的Nid
     * @return
     */
    String checkProjectAmount(String couponUserId, String borrowNid);
    /**
     * 
     * 判断活动适用终端
     * @author pcc
     * @param activityId
     * @param platform
     * @return
     */
    String checkActivityPlatform(String activityId, String platform);
    
    /**
     * 
     * 检查是否为老用户
     * @author pcc
     * @param activityId
     * @param userId
     * @return
     */
    String checkOldUser(String activityId, String userId);
    /**
     * 
     * 验证用户是否领取过活动优惠券
     * @author pcc
     * @param activityId
     * @param userId
     * @param couponCodeList 
     * @return
     */
    String checkActivityIfInvolvement(String activityId, String userId);
    
    
	
      
}

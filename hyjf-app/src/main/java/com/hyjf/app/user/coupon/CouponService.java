/**
 * Description:用户出借服务
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.app.user.coupon;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderDetailCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponUserForAppCustomize;

public interface CouponService extends BaseService {
    /**
     * 
     * 加载
     * @author pcc
     * @param borrowNid
     * @param sign
     * @param platform 
     * @param ret
     * @param money 
     */
    public void getProjectAvailableUserCoupon(String platform,String borrowNid, String sign, JSONObject ret, String money);

    /**
     * 获取用户的优惠券列表
     * @return
     */
    List<CouponUserForAppCustomize> getCouponUserList(Map<String, Object> paraMap);
    
    /**
     * 用户的优惠券总数
     * @author hsy
     * @param couponUserCustomize
     * @return
     */
    Integer countCouponUsers(Map<String, Object> paraMap);
    /**
     * 
     * 过滤用户可用优惠券
     * @author pcc
     * @param borrowNid
     * @param userId
     * @param platform 
     * @param money 
     * @return
     */
    public String getUserCouponAvailableCount(String borrowNid, Integer userId, String money, String platform);

    CouponTenderDetailCustomize getUserCouponDetail(Map<String,Object> paramMap);

    List<CouponRecoverCustomize> getUserCouponRecover(Map<String,Object> paramMap);

    List<BorrowProjectType> getProjectTypeList();

    int updateCouponReadFlag(Integer userId, Integer readFlag);

    String getUserCouponsData(String couponStatus, Integer page, Integer pageSize, Integer userId, String host);

    String getCouponsDetailData(String couponId);

    public void getHJHProjectAvailableUserCoupon(String platform, String borrowNid, String sign, JSONObject ret,
        String money);
    //从web 中拷贝的方法
    int countUserCouponTotal(int userId);

    /**
     * 根据用户ID和borrowNid判断获取用户的优惠券
     * @param borrowNid
     * @param userId 用户ID
     * @param money 出借金额
     * @param platform 平台
     * @return
     */
	public JSONObject getUserCoupon(String borrowNid, Integer userId,
			String money, String platform);

	/**
	 * 根据用户ID和borrowNid判断获取用户的优惠券
	 * @param borrowNid
	 * @param userId 用户ID
	 * @param money 出借金额
	 * @param platform 平台
	 * @return
	 */
	public JSONObject getHJHProjectUserCoupon(String borrowNid, Integer userId,
			String money, String platform);

}

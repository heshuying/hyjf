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

package com.hyjf.web.coupon;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.customize.web.CouponUserListCustomize;
import com.hyjf.web.BaseService;

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
    public void getProjectAvailableUserCoupon(String platform,String borrowNid, Integer userId, JSONObject ret, String money);

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


    List<BorrowProjectType> getProjectTypeList();

    int updateCouponReadFlag(Integer userId, Integer readFlag);

    int countUserCouponTotal(CouponListBean form);

    List<CouponUserListCustomize> selectUserCouponList(CouponListBean form, int limitStart, int limitEnd);

    /**
     * 用户优惠券总数
     * @author hsy
     * @param couponUserCustomize
     * @return
     */
    Integer countCouponUsers(Map<String, Object> paraMap);

	Integer selectCouponValidCount(Integer userId);

    public void getHJHProjectAvailableUserCoupon(String platform, String borrowNid, JSONObject ret,
        String money, String userId);

}

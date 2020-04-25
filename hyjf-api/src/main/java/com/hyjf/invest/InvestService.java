package com.hyjf.invest;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;

public interface InvestService extends BaseService {

    AvailableCouponResultBean getProjectAvailableUserCoupon(InvestBean investBean);

    String validateCouponProjectType(String projectType, String projectType2);

    Borrow getBorrowByNid(String borrowNid);

    CouponConfigCustomizeV2 getCouponUser(String couponGrantId, int userId);

    boolean updateCouponTender(String couponGrantId, String borrowNid, String ordDate, int userId, String account,
        String ip, int client, int couponOldTime, String mainTenderNid, Map<String, Object> retMap) throws Exception;

    String getUserCouponAvailableCount(String borrowNid, Integer userId, String money, String platform);

    AppProjectDetailCustomize selectProjectDetail(String borrowNid);

    UserCouponConfigCustomize getBestCoupon(String borrowNid, Integer userId, String money, String platform);

    UserCouponConfigCustomize getBestCouponById(String couponId);

    JSONObject checkParam(String borrowNid, String account, String userId, String platform,
        CouponConfigCustomizeV2 couponConfig);

}

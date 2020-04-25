package com.hyjf.batch.coupon.push;

import com.hyjf.batch.BaseService;

public interface CouponExpiredPushService extends BaseService {

    void sendExpiredMsgAct();
    
}

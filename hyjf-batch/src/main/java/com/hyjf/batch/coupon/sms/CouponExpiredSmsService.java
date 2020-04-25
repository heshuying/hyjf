package com.hyjf.batch.coupon.sms;

import com.hyjf.batch.BaseService;

public interface CouponExpiredSmsService extends BaseService {

    void sendExpiredMsgAct();
    
}

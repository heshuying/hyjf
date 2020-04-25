package com.hyjf.batch.vip.push;

import com.hyjf.batch.BaseService;

public interface VipExpiredPushService extends BaseService {

    void sendExpiredMsgAct();
    
}

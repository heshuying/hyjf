package com.hyjf.app.setmsgmail;

import com.hyjf.app.BaseService;

/**
 * Created by yaoyong on 2017/12/7.
 */
public interface MsgMailService  extends BaseService {

    void updateStatusByUserId(Integer userId, String smsOpenStatus , String emailOpenStatus);


}

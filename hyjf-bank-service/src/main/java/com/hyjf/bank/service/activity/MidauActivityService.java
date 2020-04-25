/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.activity;

import java.math.BigDecimal;

/**
 * 中秋国庆活动
 * @author yinhui
 * @version MidauActivityService, v0.1 2018/9/8 14:40
 */
public interface MidauActivityService {

    boolean sendUserAward(Integer userId,String orderId,int productType,BigDecimal investMoney);

    String checkActivityIfAvailable(String activityId);
}

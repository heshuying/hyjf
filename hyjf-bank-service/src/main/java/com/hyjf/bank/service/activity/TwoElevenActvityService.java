/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.activity;

import java.math.BigDecimal;

/**
 * @author yinhui
 * @version TwoElevenActvityService, v0.1 2018/10/16 9:32
 */
public interface TwoElevenActvityService {

    boolean saveTwoelevenInvestment(Integer userId, String orderId, Integer productType, BigDecimal investMoney);
}

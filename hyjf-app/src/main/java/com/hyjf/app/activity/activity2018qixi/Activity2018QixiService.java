/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.app.activity.activity2018qixi;

import com.hyjf.app.BaseService;

import java.math.BigDecimal;

/**
 * @author fq
 * @version Activity2018QixiService, v0.1 2018/7/23 16:20
 */
public interface Activity2018QixiService extends BaseService {
    /**
     * 更新点击数
     * @param userId
     */
    void updateClickCount(Integer userId);

    /**
     * 查询活动期间内出借总额
     * @return
     */
    BigDecimal selectInvestSum(Integer userId, int startTime, int endTime);

    /**
     * 查找获奖类型
     * @param userId
     * @return
     */
    Integer selectAwartType(Integer userId);
}

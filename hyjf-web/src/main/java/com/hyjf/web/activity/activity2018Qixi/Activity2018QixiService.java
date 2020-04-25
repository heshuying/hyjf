/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.web.activity.activity2018Qixi;

import com.hyjf.web.BaseService;

import java.math.BigDecimal;

/**
 * @author fq
 * @version Activity2018QixiService, v0.1 2018/7/23 16:20
 */
public interface Activity2018QixiService extends BaseService {
    /**
     * 更新七夕活动点击次数
     * @param userId
     */
    void updateClickCount(Integer userId);

    /**
     * 获取用户累计出借总额
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

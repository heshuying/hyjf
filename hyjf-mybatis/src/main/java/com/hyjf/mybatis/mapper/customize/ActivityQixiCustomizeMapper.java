/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.batch.BatchActivityQixiCustomize;

/**
 * @author fq
 * @version ActivityQixiCustomizeMapper, v0.1 2018/7/25 17:53
 */
public interface ActivityQixiCustomizeMapper {
    /**
     * 更新七夕活动表
     */
    void updateActivityQixi(List<BatchActivityQixiCustomize> list);
}

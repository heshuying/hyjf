/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.BorrowManinfo;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version BorrowManinfoCustomizeMapper, v0.1 2018/12/6 14:14
 */
public interface BorrowManinfoCustomizeMapper {
    /**
     * 获取借款列表
     *
     * @param borrowNid
     * @return
     */
    List<BorrowManinfo> selectBorrowManInfoByBorrowNid(String borrowNid);

}

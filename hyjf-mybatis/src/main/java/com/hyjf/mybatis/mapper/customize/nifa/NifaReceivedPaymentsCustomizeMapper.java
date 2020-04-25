/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.mapper.customize.nifa;

import com.hyjf.mybatis.model.customize.nifa.NifaReceivedPaymentsCustomize;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaReceivedPaymentsCustomizeMapper, v0.1 2018/7/6 11:49
 */
public interface NifaReceivedPaymentsCustomizeMapper {
    /**
     * 获取前一天出借人回款记录
     * @return
     */
    List<NifaReceivedPaymentsCustomize> selectNifaReceivedPaymentsList();
}

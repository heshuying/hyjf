package com.hyjf.admin.manager.activity.namimarketing.reffer;

import com.hyjf.mybatis.model.customize.NaMiMarketing.NaMiMarketingCustomize;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author xiehuili on 2018/11/8.
 */
public interface NaMiMarketingRefferService {


    /**
     * 查询邀请人返现明细 条数
     * @param paraMap
     * @return
     */
    int selectNaMiMarketingRefferCount(Map<String, Object> paraMap);

    /**
     * 查询邀请人返现明细 列表
     * @param paraMap
     * @return
     */
    List<NaMiMarketingCustomize> selectNaMiMarketingRefferList(Map<String, Object> paraMap);

    /**
     * 查询邀请人返现统计 条数
     * @param paraMap
     * @return
     */
    int selectNaMiMarketingRefferTotalCount(Map<String, Object> paraMap);

    /**
     * 查询邀请人返现统计 列表
     * @param paraMap
     * @return
     */
    List<NaMiMarketingCustomize> selectNaMiMarketingRefferTotalList(Map<String, Object> paraMap);

    /**
     * 查询邀请人返现统计 合计
     * @param paraMap
     * @return
     */
    BigDecimal selectNaMiMarketingRefferTotalAmount(Map<String, Object> paraMap);
    /**
     * 查询 月份列表
     * @param paraMap
     * @return
     */
    List<String> selectMonthList();

}

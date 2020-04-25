package com.hyjf.admin.manager.activity.namimarketing;

import com.hyjf.mybatis.model.auto.PerformanceReturnDetail;
import com.hyjf.mybatis.model.customize.NaMiMarketing.NaMiMarketingCustomize;

import java.util.List;
import java.util.Map;

/**
 * @author xiehuili on 2018/11/8.
 */
public interface NaMiMarketingService {

    /**
     * 查询邀请明细条数
     * @param paraMap
     * @return
     */
    List<Integer> selectNaMiMarketingCount(Map<String, Object> paraMap);

    /**
     * 查询邀请明细列表
     * @param paraMap
     * @return
     */
    List<NaMiMarketingCustomize> selectNaMiMarketingList(Map<String, Object> paraMap);

    /**
     * 查询业绩返现详情条数
     * @param paraMap
     * @return
     */
    int selectNaMiMarketingPerfanceCount(Map<String, Object> paraMap);

    /**
     * 查询业绩返现详情列表
     * @param paraMap
     * @return
     */
    List<NaMiMarketingCustomize> selectNaMiMarketingPerfanceList(Map<String, Object> paraMap);

    /**
     * 查询业绩返现详情
     * @param form
     * @return
     */
    List<PerformanceReturnDetail> selectNaMiMarketingPerfanceInfo(NaMiMarketingBean form);


}

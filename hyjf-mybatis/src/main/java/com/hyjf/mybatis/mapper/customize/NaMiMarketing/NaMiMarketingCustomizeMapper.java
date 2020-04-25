package com.hyjf.mybatis.mapper.customize.NaMiMarketing;

import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.PerformanceReturnDetail;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.NaMiMarketing.NaMiMarketingCustomize;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author xiehuili on 2018/11/8.
 */
public interface NaMiMarketingCustomizeMapper {

    /**
     *查询合伙人
     * @param paraMap
     * @return
     */
    List<Users> selectId0List(@Param("activityList") ActivityList activityList);


    /**
     * //根据邀请人账户名，查询邀请人id
     * @param refferName
     * @return
     */
    int selectRefferIdByName(String refferName);

    /**
     *查询邀请明细 好友id
     * @param paraMap
     * @return
     */
    List<Integer> selectIdList(Map<String, Object> paraMap);

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
     * 业绩返现详情 根据推荐人用户名查询
     * @param id
     * @return
     */
    PerformanceReturnDetail selectReturnDetail(@Param("referName") String referName );

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
     * @return
     */
    public List<String> selectMonthList();
}

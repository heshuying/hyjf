/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.ActivityMidauInfo;
import com.hyjf.mybatis.model.customize.admin.DoubleSectionActivityCustomize;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author yinhui
 * @version ActivityMidauRecodCustomizeMapper, v0.1 2018/9/8 15:13
 */
public interface ActivityMidauInfoCustomizeMapper {

    //查询加出借散标的出借信息
    List<ActivityMidauInfo> queryTenderList(@Param("orderid")String orderid,@Param("userid")Integer userid);

    List<ActivityMidauInfo> queryTenderRecoverList(@Param("orderid")String orderid,@Param("userid")Integer userid);


    //查询加入汇计划的出借信息
    List<ActivityMidauInfo> queryPlanList(@Param("orderid")String orderid,@Param("userid")Integer userid);

    /**
     * @Author walter.limeng
     * @Description  根据条件查询总数
     * @Date 10:54 2018/9/11
     * @Param paraMap
     * @return
     */
    Integer selectDouSectionActivityCount(Map<String,Object> paraMap);

    /**
     * @Author walter.limeng
     * @Description  分页查询单笔出借情况数据
     * @Date 10:55 2018/9/11
     * @Param paraMap
     * @return
     */
    List<DoubleSectionActivityCustomize> selectDouSectionActivityList(Map<String,Object> paraMap);

    /**
     * @Author walter.limeng
     * @Description  奖励明细查询总数
     * @Date 11:42 2018/9/11
     * @Param paraMap
     * @return
     */
    Integer selectSectionActivityAwardCount(Map<String,Object> paraMap);

    /**
     * @Author walter.limeng
     * @Description  分页查询奖励明细数据
     * @Date 11:42 2018/9/11
     * @Param paraMap
     * @return
     */
    List<DoubleSectionActivityCustomize> selectSectionActivityAwardList(Map<String,Object> paraMap);
}

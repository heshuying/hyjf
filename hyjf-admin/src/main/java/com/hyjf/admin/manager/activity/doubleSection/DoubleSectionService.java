package com.hyjf.admin.manager.activity.doubleSection;

import com.hyjf.mybatis.model.auto.ActivityMidauInfo;
import com.hyjf.mybatis.model.auto.ActivityQixi;
import com.hyjf.mybatis.model.customize.admin.DoubleSectionActivityCustomize;

import java.util.List;
import java.util.Map;

/**
 * @Auther: walter.limeng
 * @Date: 2018/9/11 10:48
 * @Description: DoubleSectionService
 */
public interface DoubleSectionService {

    /**
     * @Author walter.limeng
     * @Description  根据条件查询总数
     * @Date 10:51 2018/9/11
     * @Param paraMap
     * @return
     */
    Integer selectDouSectionActivityCount(Map<String,Object> paraMap);

    /**
     * @Author walter.limeng
     * @Description  分页查询数据
     * @Date 10:51 2018/9/11
     * @Param paraMap
     * @return
     */
    List<DoubleSectionActivityCustomize> selectDouSectionActivityList(Map<String,Object> paraMap);

    /**
     * @Author walter.limeng
     * @Description  奖励明细查询总数
     * @Date 11:41 2018/9/11
     * @Param paraMap
     * @return
     */
    Integer selectSectionActivityAwardCount(Map<String,Object> paraMap);

    /**
     * @Author walter.limeng
     * @Description  奖励明细分页查询数据
     * @Date 11:41 2018/9/11
     * @Param paraMap
     * @return
     */
    List<DoubleSectionActivityCustomize> selectSectionActivityAwardList(Map<String,Object> paraMap);

    /**
     * @Author walter.limeng
     * @Description  修改发放状态
     * @Date 16:33 2018/9/11
     * @Param from
     * @return
     */
    void updateSectionActivityAward(ActivityMidauInfo from);
}

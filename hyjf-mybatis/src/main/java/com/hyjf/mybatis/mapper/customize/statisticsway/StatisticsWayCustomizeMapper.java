package com.hyjf.mybatis.mapper.customize.statisticsway;

import com.hyjf.mybatis.model.customize.statisticsway.StatisticsWayConfigureCustomize;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 运营报告---统计方式配置
 * Created by xiehuili on 2018/6/20.
 */
public interface StatisticsWayCustomizeMapper {

    /**
     * 根据条件查询数据
     *
     * @param map
     * @return
     */
     Integer countRecordList(Map<String, Object> map);

    /**
     * 根据条件查询数据
     *
     * @param
     * @return
     */
     List<StatisticsWayConfigureCustomize> selectRecordList(Map<String, Object> map);

    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    StatisticsWayConfigureCustomize selectstatisticsWayById(@Param("id")Integer id);

    /**
     * 根据唯一标识查询
     *
     * @param uniqueIdentifier
     * @return
     */
    Integer validatorFieldCheck(@Param("uniqueIdentifier")String uniqueIdentifier);

}

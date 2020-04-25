package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.StatisticsWayConfigure;
import com.hyjf.mybatis.model.auto.StatisticsWayConfigureExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatisticsWayConfigureMapper {
    int countByExample(StatisticsWayConfigureExample example);

    int deleteByExample(StatisticsWayConfigureExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StatisticsWayConfigure record);

    int insertSelective(StatisticsWayConfigure record);

    List<StatisticsWayConfigure> selectByExample(StatisticsWayConfigureExample example);

    StatisticsWayConfigure selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StatisticsWayConfigure record, @Param("example") StatisticsWayConfigureExample example);

    int updateByExample(@Param("record") StatisticsWayConfigure record, @Param("example") StatisticsWayConfigureExample example);

    int updateByPrimaryKeySelective(StatisticsWayConfigure record);

    int updateByPrimaryKey(StatisticsWayConfigure record);
}
package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AppAccessStatistics;
import com.hyjf.mybatis.model.auto.AppAccessStatisticsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppAccessStatisticsMapper {
    int countByExample(AppAccessStatisticsExample example);

    int deleteByExample(AppAccessStatisticsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AppAccessStatistics record);

    int insertSelective(AppAccessStatistics record);

    List<AppAccessStatistics> selectByExample(AppAccessStatisticsExample example);

    AppAccessStatistics selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AppAccessStatistics record, @Param("example") AppAccessStatisticsExample example);

    int updateByExample(@Param("record") AppAccessStatistics record, @Param("example") AppAccessStatisticsExample example);

    int updateByPrimaryKeySelective(AppAccessStatistics record);

    int updateByPrimaryKey(AppAccessStatistics record);
}
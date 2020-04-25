package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AppChannelStatistics;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppChannelStatisticsMapper {
    int countByExample(AppChannelStatisticsExample example);

    int deleteByExample(AppChannelStatisticsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AppChannelStatistics record);

    int insertSelective(AppChannelStatistics record);

    List<AppChannelStatistics> selectByExample(AppChannelStatisticsExample example);

    AppChannelStatistics selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AppChannelStatistics record, @Param("example") AppChannelStatisticsExample example);

    int updateByExample(@Param("record") AppChannelStatistics record, @Param("example") AppChannelStatisticsExample example);

    int updateByPrimaryKeySelective(AppChannelStatistics record);

    int updateByPrimaryKey(AppChannelStatistics record);
}
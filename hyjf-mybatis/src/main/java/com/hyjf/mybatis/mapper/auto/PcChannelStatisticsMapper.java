package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.PcChannelStatistics;
import com.hyjf.mybatis.model.auto.PcChannelStatisticsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PcChannelStatisticsMapper {
    int countByExample(PcChannelStatisticsExample example);

    int deleteByExample(PcChannelStatisticsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PcChannelStatistics record);

    int insertSelective(PcChannelStatistics record);

    List<PcChannelStatistics> selectByExample(PcChannelStatisticsExample example);

    PcChannelStatistics selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PcChannelStatistics record, @Param("example") PcChannelStatisticsExample example);

    int updateByExample(@Param("record") PcChannelStatistics record, @Param("example") PcChannelStatisticsExample example);

    int updateByPrimaryKeySelective(PcChannelStatistics record);

    int updateByPrimaryKey(PcChannelStatistics record);
}
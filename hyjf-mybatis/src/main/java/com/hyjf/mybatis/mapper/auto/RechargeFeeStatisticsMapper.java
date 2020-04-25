package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.RechargeFeeStatistics;
import com.hyjf.mybatis.model.auto.RechargeFeeStatisticsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RechargeFeeStatisticsMapper {
    int countByExample(RechargeFeeStatisticsExample example);

    int deleteByExample(RechargeFeeStatisticsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RechargeFeeStatistics record);

    int insertSelective(RechargeFeeStatistics record);

    List<RechargeFeeStatistics> selectByExample(RechargeFeeStatisticsExample example);

    RechargeFeeStatistics selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RechargeFeeStatistics record, @Param("example") RechargeFeeStatisticsExample example);

    int updateByExample(@Param("record") RechargeFeeStatistics record, @Param("example") RechargeFeeStatisticsExample example);

    int updateByPrimaryKeySelective(RechargeFeeStatistics record);

    int updateByPrimaryKey(RechargeFeeStatistics record);
}
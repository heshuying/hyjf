package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.RechargeFeeBalanceStatistics;
import com.hyjf.mybatis.model.auto.RechargeFeeBalanceStatisticsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RechargeFeeBalanceStatisticsMapper {
    int countByExample(RechargeFeeBalanceStatisticsExample example);

    int deleteByExample(RechargeFeeBalanceStatisticsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RechargeFeeBalanceStatistics record);

    int insertSelective(RechargeFeeBalanceStatistics record);

    List<RechargeFeeBalanceStatistics> selectByExample(RechargeFeeBalanceStatisticsExample example);

    RechargeFeeBalanceStatistics selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RechargeFeeBalanceStatistics record, @Param("example") RechargeFeeBalanceStatisticsExample example);

    int updateByExample(@Param("record") RechargeFeeBalanceStatistics record, @Param("example") RechargeFeeBalanceStatisticsExample example);

    int updateByPrimaryKeySelective(RechargeFeeBalanceStatistics record);

    int updateByPrimaryKey(RechargeFeeBalanceStatistics record);
}
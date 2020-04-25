package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppChannelStatisticsDetailMapper {
    int countByExample(AppChannelStatisticsDetailExample example);

    int deleteByExample(AppChannelStatisticsDetailExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AppChannelStatisticsDetail record);

    int insertSelective(AppChannelStatisticsDetail record);

    List<AppChannelStatisticsDetail> selectByExample(AppChannelStatisticsDetailExample example);

    AppChannelStatisticsDetail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AppChannelStatisticsDetail record, @Param("example") AppChannelStatisticsDetailExample example);

    int updateByExample(@Param("record") AppChannelStatisticsDetail record, @Param("example") AppChannelStatisticsDetailExample example);

    int updateByPrimaryKeySelective(AppChannelStatisticsDetail record);

    int updateByPrimaryKey(AppChannelStatisticsDetail record);
}
package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.StatisticsTzjHour;
import com.hyjf.mybatis.model.auto.StatisticsTzjHourExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StatisticsTzjHourMapper {
    int countByExample(StatisticsTzjHourExample example);

    int deleteByExample(StatisticsTzjHourExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StatisticsTzjHour record);

    int insertSelective(StatisticsTzjHour record);

    List<StatisticsTzjHour> selectByExample(StatisticsTzjHourExample example);

    StatisticsTzjHour selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StatisticsTzjHour record, @Param("example") StatisticsTzjHourExample example);

    int updateByExample(@Param("record") StatisticsTzjHour record, @Param("example") StatisticsTzjHourExample example);

    int updateByPrimaryKeySelective(StatisticsTzjHour record);

    int updateByPrimaryKey(StatisticsTzjHour record);
}
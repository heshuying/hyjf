package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.StatisticsTzj;
import com.hyjf.mybatis.model.auto.StatisticsTzjExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StatisticsTzjMapper {
    int countByExample(StatisticsTzjExample example);

    int deleteByExample(StatisticsTzjExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StatisticsTzj record);

    int insertSelective(StatisticsTzj record);

    List<StatisticsTzj> selectByExample(StatisticsTzjExample example);

    StatisticsTzj selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StatisticsTzj record, @Param("example") StatisticsTzjExample example);

    int updateByExample(@Param("record") StatisticsTzj record, @Param("example") StatisticsTzjExample example);

    int updateByPrimaryKeySelective(StatisticsTzj record);

    int updateByPrimaryKey(StatisticsTzj record);
}
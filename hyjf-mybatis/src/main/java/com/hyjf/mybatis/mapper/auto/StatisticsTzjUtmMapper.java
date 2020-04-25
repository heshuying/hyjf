package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.StatisticsTzjUtm;
import com.hyjf.mybatis.model.auto.StatisticsTzjUtmExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StatisticsTzjUtmMapper {
    int countByExample(StatisticsTzjUtmExample example);

    int deleteByExample(StatisticsTzjUtmExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StatisticsTzjUtm record);

    int insertSelective(StatisticsTzjUtm record);

    List<StatisticsTzjUtm> selectByExample(StatisticsTzjUtmExample example);

    StatisticsTzjUtm selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StatisticsTzjUtm record, @Param("example") StatisticsTzjUtmExample example);

    int updateByExample(@Param("record") StatisticsTzjUtm record, @Param("example") StatisticsTzjUtmExample example);

    int updateByPrimaryKeySelective(StatisticsTzjUtm record);

    int updateByPrimaryKey(StatisticsTzjUtm record);
}
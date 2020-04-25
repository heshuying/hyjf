package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.Events;
import com.hyjf.mybatis.model.auto.EventsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EventsMapper {
    int countByExample(EventsExample example);

    int deleteByExample(EventsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Events record);

    int insertSelective(Events record);

    List<Events> selectByExampleWithBLOBs(EventsExample example);

    List<Events> selectByExample(EventsExample example);

    Events selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Events record, @Param("example") EventsExample example);

    int updateByExampleWithBLOBs(@Param("record") Events record, @Param("example") EventsExample example);

    int updateByExample(@Param("record") Events record, @Param("example") EventsExample example);

    int updateByPrimaryKeySelective(Events record);

    int updateByPrimaryKeyWithBLOBs(Events record);

    int updateByPrimaryKey(Events record);
}
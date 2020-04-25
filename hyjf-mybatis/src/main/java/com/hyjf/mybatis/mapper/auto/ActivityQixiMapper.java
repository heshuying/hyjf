package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActivityQixi;
import com.hyjf.mybatis.model.auto.ActivityQixiExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityQixiMapper {
    int countByExample(ActivityQixiExample example);

    int deleteByExample(ActivityQixiExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityQixi record);

    int insertSelective(ActivityQixi record);

    List<ActivityQixi> selectByExample(ActivityQixiExample example);

    ActivityQixi selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActivityQixi record, @Param("example") ActivityQixiExample example);

    int updateByExample(@Param("record") ActivityQixi record, @Param("example") ActivityQixiExample example);

    int updateByPrimaryKeySelective(ActivityQixi record);

    int updateByPrimaryKey(ActivityQixi record);
}
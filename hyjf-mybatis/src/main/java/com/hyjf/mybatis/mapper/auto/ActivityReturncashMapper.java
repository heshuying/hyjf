package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActivityReturncash;
import com.hyjf.mybatis.model.auto.ActivityReturncashExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityReturncashMapper {
    int countByExample(ActivityReturncashExample example);

    int deleteByExample(ActivityReturncashExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityReturncash record);

    int insertSelective(ActivityReturncash record);

    List<ActivityReturncash> selectByExample(ActivityReturncashExample example);

    ActivityReturncash selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActivityReturncash record, @Param("example") ActivityReturncashExample example);

    int updateByExample(@Param("record") ActivityReturncash record, @Param("example") ActivityReturncashExample example);

    int updateByPrimaryKeySelective(ActivityReturncash record);

    int updateByPrimaryKey(ActivityReturncash record);
}
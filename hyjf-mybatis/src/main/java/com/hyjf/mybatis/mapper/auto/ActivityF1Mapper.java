package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActivityF1;
import com.hyjf.mybatis.model.auto.ActivityF1Example;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityF1Mapper {
    int countByExample(ActivityF1Example example);

    int deleteByExample(ActivityF1Example example);

    int deleteByPrimaryKey(Integer userId);

    int insert(ActivityF1 record);

    int insertSelective(ActivityF1 record);

    List<ActivityF1> selectByExample(ActivityF1Example example);

    ActivityF1 selectByPrimaryKey(Integer userId);

    int updateByExampleSelective(@Param("record") ActivityF1 record, @Param("example") ActivityF1Example example);

    int updateByExample(@Param("record") ActivityF1 record, @Param("example") ActivityF1Example example);

    int updateByPrimaryKeySelective(ActivityF1 record);

    int updateByPrimaryKey(ActivityF1 record);
}
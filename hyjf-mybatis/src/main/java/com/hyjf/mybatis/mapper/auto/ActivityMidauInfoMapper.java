package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActivityMidauInfo;
import com.hyjf.mybatis.model.auto.ActivityMidauInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityMidauInfoMapper {
    int countByExample(ActivityMidauInfoExample example);

    int deleteByExample(ActivityMidauInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityMidauInfo record);

    int insertSelective(ActivityMidauInfo record);

    List<ActivityMidauInfo> selectByExample(ActivityMidauInfoExample example);

    ActivityMidauInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActivityMidauInfo record, @Param("example") ActivityMidauInfoExample example);

    int updateByExample(@Param("record") ActivityMidauInfo record, @Param("example") ActivityMidauInfoExample example);

    int updateByPrimaryKeySelective(ActivityMidauInfo record);

    int updateByPrimaryKey(ActivityMidauInfo record);
}
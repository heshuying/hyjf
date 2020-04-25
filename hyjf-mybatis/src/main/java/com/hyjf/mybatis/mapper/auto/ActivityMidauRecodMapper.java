package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActivityMidauRecod;
import com.hyjf.mybatis.model.auto.ActivityMidauRecodExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityMidauRecodMapper {
    int countByExample(ActivityMidauRecodExample example);

    int deleteByExample(ActivityMidauRecodExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityMidauRecod record);

    int insertSelective(ActivityMidauRecod record);

    List<ActivityMidauRecod> selectByExample(ActivityMidauRecodExample example);

    ActivityMidauRecod selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActivityMidauRecod record, @Param("example") ActivityMidauRecodExample example);

    int updateByExample(@Param("record") ActivityMidauRecod record, @Param("example") ActivityMidauRecodExample example);

    int updateByPrimaryKeySelective(ActivityMidauRecod record);

    int updateByPrimaryKey(ActivityMidauRecod record);
}
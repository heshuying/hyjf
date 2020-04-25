package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.PreRegistChannelExclusiveActivity;
import com.hyjf.mybatis.model.auto.PreRegistChannelExclusiveActivityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PreRegistChannelExclusiveActivityMapper {
    int countByExample(PreRegistChannelExclusiveActivityExample example);

    int deleteByExample(PreRegistChannelExclusiveActivityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PreRegistChannelExclusiveActivity record);

    int insertSelective(PreRegistChannelExclusiveActivity record);

    List<PreRegistChannelExclusiveActivity> selectByExample(PreRegistChannelExclusiveActivityExample example);

    PreRegistChannelExclusiveActivity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PreRegistChannelExclusiveActivity record, @Param("example") PreRegistChannelExclusiveActivityExample example);

    int updateByExample(@Param("record") PreRegistChannelExclusiveActivity record, @Param("example") PreRegistChannelExclusiveActivityExample example);

    int updateByPrimaryKeySelective(PreRegistChannelExclusiveActivity record);

    int updateByPrimaryKey(PreRegistChannelExclusiveActivity record);
}
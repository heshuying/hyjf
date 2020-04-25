package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActivityInviteSeven;
import com.hyjf.mybatis.model.auto.ActivityInviteSevenExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityInviteSevenMapper {
    int countByExample(ActivityInviteSevenExample example);

    int deleteByExample(ActivityInviteSevenExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityInviteSeven record);

    int insertSelective(ActivityInviteSeven record);

    List<ActivityInviteSeven> selectByExample(ActivityInviteSevenExample example);

    ActivityInviteSeven selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActivityInviteSeven record, @Param("example") ActivityInviteSevenExample example);

    int updateByExample(@Param("record") ActivityInviteSeven record, @Param("example") ActivityInviteSevenExample example);

    int updateByPrimaryKeySelective(ActivityInviteSeven record);

    int updateByPrimaryKey(ActivityInviteSeven record);
}
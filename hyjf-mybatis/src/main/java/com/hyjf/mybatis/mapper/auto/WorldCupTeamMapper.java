package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.WorldCupTeam;
import com.hyjf.mybatis.model.auto.WorldCupTeamExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WorldCupTeamMapper {
    int countByExample(WorldCupTeamExample example);

    int deleteByExample(WorldCupTeamExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WorldCupTeam record);

    int insertSelective(WorldCupTeam record);

    List<WorldCupTeam> selectByExample(WorldCupTeamExample example);

    WorldCupTeam selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WorldCupTeam record, @Param("example") WorldCupTeamExample example);

    int updateByExample(@Param("record") WorldCupTeam record, @Param("example") WorldCupTeamExample example);

    int updateByPrimaryKeySelective(WorldCupTeam record);

    int updateByPrimaryKey(WorldCupTeam record);
}
package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.WorldTeamMatch;
import com.hyjf.mybatis.model.auto.WorldTeamMatchExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WorldTeamMatchMapper {
    int countByExample(WorldTeamMatchExample example);

    int deleteByExample(WorldTeamMatchExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WorldTeamMatch record);

    int insertSelective(WorldTeamMatch record);

    List<WorldTeamMatch> selectByExample(WorldTeamMatchExample example);

    WorldTeamMatch selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WorldTeamMatch record, @Param("example") WorldTeamMatchExample example);

    int updateByExample(@Param("record") WorldTeamMatch record, @Param("example") WorldTeamMatchExample example);

    int updateByPrimaryKeySelective(WorldTeamMatch record);

    int updateByPrimaryKey(WorldTeamMatch record);
}
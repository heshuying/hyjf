package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.GuessingChampion;
import com.hyjf.mybatis.model.auto.GuessingChampionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GuessingChampionMapper {
    int countByExample(GuessingChampionExample example);

    int deleteByExample(GuessingChampionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GuessingChampion record);

    int insertSelective(GuessingChampion record);

    List<GuessingChampion> selectByExample(GuessingChampionExample example);

    GuessingChampion selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GuessingChampion record, @Param("example") GuessingChampionExample example);

    int updateByExample(@Param("record") GuessingChampion record, @Param("example") GuessingChampionExample example);

    int updateByPrimaryKeySelective(GuessingChampion record);

    int updateByPrimaryKey(GuessingChampion record);
}
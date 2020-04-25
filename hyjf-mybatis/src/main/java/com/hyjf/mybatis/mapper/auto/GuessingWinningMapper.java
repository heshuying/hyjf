package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.GuessingWinning;
import com.hyjf.mybatis.model.auto.GuessingWinningExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GuessingWinningMapper {
    int countByExample(GuessingWinningExample example);

    int deleteByExample(GuessingWinningExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GuessingWinning record);

    int insertSelective(GuessingWinning record);

    List<GuessingWinning> selectByExample(GuessingWinningExample example);

    GuessingWinning selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GuessingWinning record, @Param("example") GuessingWinningExample example);

    int updateByExample(@Param("record") GuessingWinning record, @Param("example") GuessingWinningExample example);

    int updateByPrimaryKeySelective(GuessingWinning record);

    int updateByPrimaryKey(GuessingWinning record);
}
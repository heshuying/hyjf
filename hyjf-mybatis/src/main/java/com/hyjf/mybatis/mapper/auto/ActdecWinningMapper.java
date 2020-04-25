package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ActdecWinning;
import com.hyjf.mybatis.model.auto.ActdecWinningExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActdecWinningMapper {
    int countByExample(ActdecWinningExample example);

    int deleteByExample(ActdecWinningExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActdecWinning record);

    int insertSelective(ActdecWinning record);

    List<ActdecWinning> selectByExample(ActdecWinningExample example);

    ActdecWinning selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActdecWinning record, @Param("example") ActdecWinningExample example);

    int updateByExample(@Param("record") ActdecWinning record, @Param("example") ActdecWinningExample example);

    int updateByPrimaryKeySelective(ActdecWinning record);

    int updateByPrimaryKey(ActdecWinning record);
}
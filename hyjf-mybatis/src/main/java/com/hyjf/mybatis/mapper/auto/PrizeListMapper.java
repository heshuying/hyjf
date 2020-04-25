package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.PrizeList;
import com.hyjf.mybatis.model.auto.PrizeListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PrizeListMapper {
    int countByExample(PrizeListExample example);

    int deleteByExample(PrizeListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PrizeList record);

    int insertSelective(PrizeList record);

    List<PrizeList> selectByExample(PrizeListExample example);

    PrizeList selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PrizeList record, @Param("example") PrizeListExample example);

    int updateByExample(@Param("record") PrizeList record, @Param("example") PrizeListExample example);

    int updateByPrimaryKeySelective(PrizeList record);

    int updateByPrimaryKey(PrizeList record);
}
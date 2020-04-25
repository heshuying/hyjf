package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.CardBin;
import com.hyjf.mybatis.model.auto.CardBinExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CardBinMapper {
    int countByExample(CardBinExample example);

    int deleteByExample(CardBinExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CardBin record);

    int insertSelective(CardBin record);

    List<CardBin> selectByExample(CardBinExample example);

    CardBin selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CardBin record, @Param("example") CardBinExample example);

    int updateByExample(@Param("record") CardBin record, @Param("example") CardBinExample example);

    int updateByPrimaryKeySelective(CardBin record);

    int updateByPrimaryKey(CardBin record);
}
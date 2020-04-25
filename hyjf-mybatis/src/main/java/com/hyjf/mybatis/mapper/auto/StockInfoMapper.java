package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.StockInfo;
import com.hyjf.mybatis.model.auto.StockInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StockInfoMapper {
    int countByExample(StockInfoExample example);

    int deleteByExample(StockInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StockInfo record);

    int insertSelective(StockInfo record);

    List<StockInfo> selectByExample(StockInfoExample example);

    StockInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StockInfo record, @Param("example") StockInfoExample example);

    int updateByExample(@Param("record") StockInfo record, @Param("example") StockInfoExample example);

    int updateByPrimaryKeySelective(StockInfo record);

    int updateByPrimaryKey(StockInfo record);
}
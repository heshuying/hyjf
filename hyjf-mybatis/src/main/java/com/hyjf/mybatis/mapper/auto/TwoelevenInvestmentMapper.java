package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.TwoelevenInvestment;
import com.hyjf.mybatis.model.auto.TwoelevenInvestmentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TwoelevenInvestmentMapper {
    int countByExample(TwoelevenInvestmentExample example);

    int deleteByExample(TwoelevenInvestmentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TwoelevenInvestment record);

    int insertSelective(TwoelevenInvestment record);

    List<TwoelevenInvestment> selectByExample(TwoelevenInvestmentExample example);

    TwoelevenInvestment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TwoelevenInvestment record, @Param("example") TwoelevenInvestmentExample example);

    int updateByExample(@Param("record") TwoelevenInvestment record, @Param("example") TwoelevenInvestmentExample example);

    int updateByPrimaryKeySelective(TwoelevenInvestment record);

    int updateByPrimaryKey(TwoelevenInvestment record);
}
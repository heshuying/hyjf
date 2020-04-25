package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CalculateInvestInterestMapper {
    int countByExample(CalculateInvestInterestExample example);

    int deleteByExample(CalculateInvestInterestExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CalculateInvestInterest record);

    int insertSelective(CalculateInvestInterest record);

    List<CalculateInvestInterest> selectByExample(CalculateInvestInterestExample example);

    CalculateInvestInterest selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CalculateInvestInterest record, @Param("example") CalculateInvestInterestExample example);

    int updateByExample(@Param("record") CalculateInvestInterest record, @Param("example") CalculateInvestInterestExample example);

    int updateByPrimaryKeySelective(CalculateInvestInterest record);

    int updateByPrimaryKey(CalculateInvestInterest record);
}
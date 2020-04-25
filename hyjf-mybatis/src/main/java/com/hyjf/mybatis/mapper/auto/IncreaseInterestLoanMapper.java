package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.IncreaseInterestLoan;
import com.hyjf.mybatis.model.auto.IncreaseInterestLoanExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IncreaseInterestLoanMapper {
    int countByExample(IncreaseInterestLoanExample example);

    int deleteByExample(IncreaseInterestLoanExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(IncreaseInterestLoan record);

    int insertSelective(IncreaseInterestLoan record);

    List<IncreaseInterestLoan> selectByExample(IncreaseInterestLoanExample example);

    IncreaseInterestLoan selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") IncreaseInterestLoan record, @Param("example") IncreaseInterestLoanExample example);

    int updateByExample(@Param("record") IncreaseInterestLoan record, @Param("example") IncreaseInterestLoanExample example);

    int updateByPrimaryKeySelective(IncreaseInterestLoan record);

    int updateByPrimaryKey(IncreaseInterestLoan record);
}
package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.Loan;
import com.hyjf.mybatis.model.auto.LoanExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LoanMapper {
    int countByExample(LoanExample example);

    int deleteByExample(LoanExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Loan record);

    int insertSelective(Loan record);

    List<Loan> selectByExampleWithBLOBs(LoanExample example);

    List<Loan> selectByExample(LoanExample example);

    Loan selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Loan record, @Param("example") LoanExample example);

    int updateByExampleWithBLOBs(@Param("record") Loan record, @Param("example") LoanExample example);

    int updateByExample(@Param("record") Loan record, @Param("example") LoanExample example);

    int updateByPrimaryKeySelective(Loan record);

    int updateByPrimaryKeyWithBLOBs(Loan record);

    int updateByPrimaryKey(Loan record);
}
package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.DebtCredit;
import com.hyjf.mybatis.model.auto.DebtCreditExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DebtCreditMapper {
    int countByExample(DebtCreditExample example);

    int deleteByExample(DebtCreditExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DebtCredit record);

    int insertSelective(DebtCredit record);

    List<DebtCredit> selectByExample(DebtCreditExample example);

    DebtCredit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DebtCredit record, @Param("example") DebtCreditExample example);

    int updateByExample(@Param("record") DebtCredit record, @Param("example") DebtCreditExample example);

    int updateByPrimaryKeySelective(DebtCredit record);

    int updateByPrimaryKey(DebtCredit record);
}
package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.DebtCreditRepay;
import com.hyjf.mybatis.model.auto.DebtCreditRepayExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DebtCreditRepayMapper {
    int countByExample(DebtCreditRepayExample example);

    int deleteByExample(DebtCreditRepayExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DebtCreditRepay record);

    int insertSelective(DebtCreditRepay record);

    List<DebtCreditRepay> selectByExample(DebtCreditRepayExample example);

    DebtCreditRepay selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DebtCreditRepay record, @Param("example") DebtCreditRepayExample example);

    int updateByExample(@Param("record") DebtCreditRepay record, @Param("example") DebtCreditRepayExample example);

    int updateByPrimaryKeySelective(DebtCreditRepay record);

    int updateByPrimaryKey(DebtCreditRepay record);
}
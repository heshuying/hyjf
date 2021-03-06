package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.DebtInvest;
import com.hyjf.mybatis.model.auto.DebtInvestExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DebtInvestMapper {
    int countByExample(DebtInvestExample example);

    int deleteByExample(DebtInvestExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DebtInvest record);

    int insertSelective(DebtInvest record);

    List<DebtInvest> selectByExample(DebtInvestExample example);

    DebtInvest selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DebtInvest record, @Param("example") DebtInvestExample example);

    int updateByExample(@Param("record") DebtInvest record, @Param("example") DebtInvestExample example);

    int updateByPrimaryKeySelective(DebtInvest record);

    int updateByPrimaryKey(DebtInvest record);
}
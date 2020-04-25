package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhDebtCreditExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HjhDebtCreditMapper {
    int countByExample(HjhDebtCreditExample example);

    int deleteByExample(HjhDebtCreditExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HjhDebtCredit record);

    int insertSelective(HjhDebtCredit record);

    List<HjhDebtCredit> selectByExample(HjhDebtCreditExample example);

    HjhDebtCredit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HjhDebtCredit record, @Param("example") HjhDebtCreditExample example);

    int updateByExample(@Param("record") HjhDebtCredit record, @Param("example") HjhDebtCreditExample example);

    int updateByPrimaryKeySelective(HjhDebtCredit record);

    int updateByPrimaryKey(HjhDebtCredit record);
}
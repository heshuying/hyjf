package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.BankCreditEnd;
import com.hyjf.mybatis.model.auto.BankCreditEndExample;
import org.apache.ibatis.annotations.Param;

public interface BankCreditEndCustomizeMapper {
    int updateByExampleSelective(@Param("record") BankCreditEnd record, @Param("example") BankCreditEndExample example);
}
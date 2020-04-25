package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AccountFunds;
import com.hyjf.mybatis.model.auto.AccountFundsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountFundsMapper {
    int countByExample(AccountFundsExample example);

    int deleteByExample(AccountFundsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AccountFunds record);

    int insertSelective(AccountFunds record);

    List<AccountFunds> selectByExample(AccountFundsExample example);

    AccountFunds selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AccountFunds record, @Param("example") AccountFundsExample example);

    int updateByExample(@Param("record") AccountFunds record, @Param("example") AccountFundsExample example);

    int updateByPrimaryKeySelective(AccountFunds record);

    int updateByPrimaryKey(AccountFunds record);
}
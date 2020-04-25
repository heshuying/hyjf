package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AccountMobileAynch;
import com.hyjf.mybatis.model.auto.AccountMobileAynchExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountMobileAynchMapper {
    int countByExample(AccountMobileAynchExample example);

    int deleteByExample(AccountMobileAynchExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AccountMobileAynch record);

    int insertSelective(AccountMobileAynch record);

    List<AccountMobileAynch> selectByExample(AccountMobileAynchExample example);

    AccountMobileAynch selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AccountMobileAynch record, @Param("example") AccountMobileAynchExample example);

    int updateByExample(@Param("record") AccountMobileAynch record, @Param("example") AccountMobileAynchExample example);

    int updateByPrimaryKeySelective(AccountMobileAynch record);

    int updateByPrimaryKey(AccountMobileAynch record);

    int insertAccount(String username);

    int insertMobile(String username);


}
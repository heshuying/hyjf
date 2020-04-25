package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AccountLog;
import com.hyjf.mybatis.model.auto.AccountLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountLogMapper {
    int countByExample(AccountLogExample example);

    int deleteByExample(AccountLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AccountLog record);

    int insertSelective(AccountLog record);

    List<AccountLog> selectByExample(AccountLogExample example);

    AccountLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AccountLog record, @Param("example") AccountLogExample example);

    int updateByExample(@Param("record") AccountLog record, @Param("example") AccountLogExample example);

    int updateByPrimaryKeySelective(AccountLog record);

    int updateByPrimaryKey(AccountLog record);
}
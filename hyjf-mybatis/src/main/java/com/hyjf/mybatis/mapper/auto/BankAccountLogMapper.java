package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BankAccountLog;
import com.hyjf.mybatis.model.auto.BankAccountLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BankAccountLogMapper {
    int countByExample(BankAccountLogExample example);

    int deleteByExample(BankAccountLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BankAccountLog record);

    int insertSelective(BankAccountLog record);

    List<BankAccountLog> selectByExample(BankAccountLogExample example);

    BankAccountLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BankAccountLog record, @Param("example") BankAccountLogExample example);

    int updateByExample(@Param("record") BankAccountLog record, @Param("example") BankAccountLogExample example);

    int updateByPrimaryKeySelective(BankAccountLog record);

    int updateByPrimaryKey(BankAccountLog record);
}
package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BankOpenAccountLog;
import com.hyjf.mybatis.model.auto.BankOpenAccountLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BankOpenAccountLogMapper {
    int countByExample(BankOpenAccountLogExample example);

    int deleteByExample(BankOpenAccountLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BankOpenAccountLog record);

    int insertSelective(BankOpenAccountLog record);

    List<BankOpenAccountLog> selectByExample(BankOpenAccountLogExample example);

    BankOpenAccountLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BankOpenAccountLog record, @Param("example") BankOpenAccountLogExample example);

    int updateByExample(@Param("record") BankOpenAccountLog record, @Param("example") BankOpenAccountLogExample example);

    int updateByPrimaryKeySelective(BankOpenAccountLog record);

    int updateByPrimaryKey(BankOpenAccountLog record);
}
package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.ExceptionAccount;
import com.hyjf.mybatis.model.auto.ExceptionAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ExceptionAccountMapper {
    int countByExample(ExceptionAccountExample example);

    int deleteByExample(ExceptionAccountExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ExceptionAccount record);

    int insertSelective(ExceptionAccount record);

    List<ExceptionAccount> selectByExample(ExceptionAccountExample example);

    ExceptionAccount selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ExceptionAccount record, @Param("example") ExceptionAccountExample example);

    int updateByExample(@Param("record") ExceptionAccount record, @Param("example") ExceptionAccountExample example);

    int updateByPrimaryKeySelective(ExceptionAccount record);

    int updateByPrimaryKey(ExceptionAccount record);
}
package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.CertUserTransact;
import com.hyjf.mybatis.model.auto.CertUserTransactExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CertUserTransactMapper {
    int countByExample(CertUserTransactExample example);

    int deleteByExample(CertUserTransactExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CertUserTransact record);

    int insertSelective(CertUserTransact record);

    List<CertUserTransact> selectByExample(CertUserTransactExample example);

    CertUserTransact selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CertUserTransact record, @Param("example") CertUserTransactExample example);

    int updateByExample(@Param("record") CertUserTransact record, @Param("example") CertUserTransactExample example);

    int updateByPrimaryKeySelective(CertUserTransact record);

    int updateByPrimaryKey(CertUserTransact record);
}
package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.CertMobileHash;
import com.hyjf.mybatis.model.auto.CertMobileHashExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CertMobileHashMapper {
    int countByExample(CertMobileHashExample example);

    int deleteByExample(CertMobileHashExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CertMobileHash record);

    int insertSelective(CertMobileHash record);

    List<CertMobileHash> selectByExample(CertMobileHashExample example);

    CertMobileHash selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CertMobileHash record, @Param("example") CertMobileHashExample example);

    int updateByExample(@Param("record") CertMobileHash record, @Param("example") CertMobileHashExample example);

    int updateByPrimaryKeySelective(CertMobileHash record);

    int updateByPrimaryKey(CertMobileHash record);
}
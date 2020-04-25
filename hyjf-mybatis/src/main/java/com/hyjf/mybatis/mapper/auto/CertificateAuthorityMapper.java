package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.CertificateAuthority;
import com.hyjf.mybatis.model.auto.CertificateAuthorityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CertificateAuthorityMapper {
    int countByExample(CertificateAuthorityExample example);

    int deleteByExample(CertificateAuthorityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CertificateAuthority record);

    int insertSelective(CertificateAuthority record);

    List<CertificateAuthority> selectByExample(CertificateAuthorityExample example);

    CertificateAuthority selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CertificateAuthority record, @Param("example") CertificateAuthorityExample example);

    int updateByExample(@Param("record") CertificateAuthority record, @Param("example") CertificateAuthorityExample example);

    int updateByPrimaryKeySelective(CertificateAuthority record);

    int updateByPrimaryKey(CertificateAuthority record);
}
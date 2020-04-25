package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.LoanSubjectCertificateAuthority;
import com.hyjf.mybatis.model.auto.LoanSubjectCertificateAuthorityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LoanSubjectCertificateAuthorityMapper {
    int countByExample(LoanSubjectCertificateAuthorityExample example);

    int deleteByExample(LoanSubjectCertificateAuthorityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LoanSubjectCertificateAuthority record);

    int insertSelective(LoanSubjectCertificateAuthority record);

    List<LoanSubjectCertificateAuthority> selectByExample(LoanSubjectCertificateAuthorityExample example);

    LoanSubjectCertificateAuthority selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LoanSubjectCertificateAuthority record, @Param("example") LoanSubjectCertificateAuthorityExample example);

    int updateByExample(@Param("record") LoanSubjectCertificateAuthority record, @Param("example") LoanSubjectCertificateAuthorityExample example);

    int updateByPrimaryKeySelective(LoanSubjectCertificateAuthority record);

    int updateByPrimaryKey(LoanSubjectCertificateAuthority record);
}
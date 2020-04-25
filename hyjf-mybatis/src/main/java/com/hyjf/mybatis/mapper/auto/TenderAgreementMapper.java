package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.TenderAgreement;
import com.hyjf.mybatis.model.auto.TenderAgreementExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TenderAgreementMapper {
    int countByExample(TenderAgreementExample example);

    int deleteByExample(TenderAgreementExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TenderAgreement record);

    int insertSelective(TenderAgreement record);

    List<TenderAgreement> selectByExample(TenderAgreementExample example);

    TenderAgreement selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TenderAgreement record, @Param("example") TenderAgreementExample example);

    int updateByExample(@Param("record") TenderAgreement record, @Param("example") TenderAgreementExample example);

    int updateByPrimaryKeySelective(TenderAgreement record);

    int updateByPrimaryKey(TenderAgreement record);
}
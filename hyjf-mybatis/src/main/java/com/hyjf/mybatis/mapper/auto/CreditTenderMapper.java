package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.CreditTenderExample;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CreditTenderMapper {
    int countByExample(CreditTenderExample example);

    int deleteByExample(CreditTenderExample example);

    int deleteByPrimaryKey(Integer assignId);

    int insert(CreditTender record);

    int insertSelective(CreditTender record);

    List<CreditTender> selectByExample(CreditTenderExample example);

    CreditTender selectByPrimaryKey(Integer assignId);

    int updateByExampleSelective(@Param("record") CreditTender record, @Param("example") CreditTenderExample example);

    int updateByExample(@Param("record") CreditTender record, @Param("example") CreditTenderExample example);

    int updateByPrimaryKeySelective(CreditTender record);

    int updateByPrimaryKey(CreditTender record);

    BigDecimal selectByCreditNid(String creditNid);
}
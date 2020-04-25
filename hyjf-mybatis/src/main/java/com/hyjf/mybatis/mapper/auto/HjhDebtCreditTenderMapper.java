package com.hyjf.mybatis.mapper.auto;

import com.hyjf.common.util.StringUtil;
import com.hyjf.mybatis.model.auto.HjhDebtCreditTender;
import com.hyjf.mybatis.model.auto.HjhDebtCreditTenderExample;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HjhDebtCreditTenderMapper {
    int countByExample(HjhDebtCreditTenderExample example);

    int deleteByExample(HjhDebtCreditTenderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HjhDebtCreditTender record);

    int insertSelective(HjhDebtCreditTender record);

    List<HjhDebtCreditTender> selectByExample(HjhDebtCreditTenderExample example);

    HjhDebtCreditTender selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HjhDebtCreditTender record, @Param("example") HjhDebtCreditTenderExample example);

    int updateByExample(@Param("record") HjhDebtCreditTender record, @Param("example") HjhDebtCreditTenderExample example);

    int updateByPrimaryKeySelective(HjhDebtCreditTender record);

    int updateByPrimaryKey(HjhDebtCreditTender record);

    BigDecimal selectServiceFeeSumByCreditNid(String creditNid);
}
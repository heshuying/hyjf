package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.HjhDebtCreditTenderLog;
import com.hyjf.mybatis.model.auto.HjhDebtCreditTenderLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HjhDebtCreditTenderLogMapper {
    int countByExample(HjhDebtCreditTenderLogExample example);

    int deleteByExample(HjhDebtCreditTenderLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HjhDebtCreditTenderLog record);

    int insertSelective(HjhDebtCreditTenderLog record);

    List<HjhDebtCreditTenderLog> selectByExample(HjhDebtCreditTenderLogExample example);

    HjhDebtCreditTenderLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HjhDebtCreditTenderLog record, @Param("example") HjhDebtCreditTenderLogExample example);

    int updateByExample(@Param("record") HjhDebtCreditTenderLog record, @Param("example") HjhDebtCreditTenderLogExample example);

    int updateByPrimaryKeySelective(HjhDebtCreditTenderLog record);

    int updateByPrimaryKey(HjhDebtCreditTenderLog record);
}
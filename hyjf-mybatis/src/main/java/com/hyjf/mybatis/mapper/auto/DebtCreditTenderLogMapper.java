package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.DebtCreditTenderLog;
import com.hyjf.mybatis.model.auto.DebtCreditTenderLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DebtCreditTenderLogMapper {
    int countByExample(DebtCreditTenderLogExample example);

    int deleteByExample(DebtCreditTenderLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DebtCreditTenderLog record);

    int insertSelective(DebtCreditTenderLog record);

    List<DebtCreditTenderLog> selectByExample(DebtCreditTenderLogExample example);

    DebtCreditTenderLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DebtCreditTenderLog record, @Param("example") DebtCreditTenderLogExample example);

    int updateByExample(@Param("record") DebtCreditTenderLog record, @Param("example") DebtCreditTenderLogExample example);

    int updateByPrimaryKeySelective(DebtCreditTenderLog record);

    int updateByPrimaryKey(DebtCreditTenderLog record);
}
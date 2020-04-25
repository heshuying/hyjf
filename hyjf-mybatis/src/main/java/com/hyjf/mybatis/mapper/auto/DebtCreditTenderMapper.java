package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.DebtCreditTender;
import com.hyjf.mybatis.model.auto.DebtCreditTenderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DebtCreditTenderMapper {
    int countByExample(DebtCreditTenderExample example);

    int deleteByExample(DebtCreditTenderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DebtCreditTender record);

    int insertSelective(DebtCreditTender record);

    List<DebtCreditTender> selectByExample(DebtCreditTenderExample example);

    DebtCreditTender selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DebtCreditTender record, @Param("example") DebtCreditTenderExample example);

    int updateByExample(@Param("record") DebtCreditTender record, @Param("example") DebtCreditTenderExample example);

    int updateByPrimaryKeySelective(DebtCreditTender record);

    int updateByPrimaryKey(DebtCreditTender record);
}
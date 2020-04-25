package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BankWithdrawType;
import com.hyjf.mybatis.model.auto.BankWithdrawTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BankWithdrawTypeMapper {
    int countByExample(BankWithdrawTypeExample example);

    int deleteByExample(BankWithdrawTypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BankWithdrawType record);

    int insertSelective(BankWithdrawType record);

    List<BankWithdrawType> selectByExample(BankWithdrawTypeExample example);

    BankWithdrawType selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BankWithdrawType record, @Param("example") BankWithdrawTypeExample example);

    int updateByExample(@Param("record") BankWithdrawType record, @Param("example") BankWithdrawTypeExample example);

    int updateByPrimaryKeySelective(BankWithdrawType record);

    int updateByPrimaryKey(BankWithdrawType record);
}
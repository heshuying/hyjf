package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BankCardMapper {
    int countByExample(BankCardExample example);

    int deleteByExample(BankCardExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BankCard record);

    int insertSelective(BankCard record);

    List<BankCard> selectByExample(BankCardExample example);

    BankCard selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BankCard record, @Param("example") BankCardExample example);

    int updateByExample(@Param("record") BankCard record, @Param("example") BankCardExample example);

    int updateByPrimaryKeySelective(BankCard record);

    int updateByPrimaryKey(BankCard record);
}
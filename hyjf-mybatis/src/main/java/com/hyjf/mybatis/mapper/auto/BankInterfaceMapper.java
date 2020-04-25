package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BankInterface;
import com.hyjf.mybatis.model.auto.BankInterfaceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BankInterfaceMapper {
    int countByExample(BankInterfaceExample example);

    int deleteByExample(BankInterfaceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BankInterface record);

    int insertSelective(BankInterface record);

    List<BankInterface> selectByExample(BankInterfaceExample example);

    BankInterface selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BankInterface record, @Param("example") BankInterfaceExample example);

    int updateByExample(@Param("record") BankInterface record, @Param("example") BankInterfaceExample example);

    int updateByPrimaryKeySelective(BankInterface record);

    int updateByPrimaryKey(BankInterface record);
}
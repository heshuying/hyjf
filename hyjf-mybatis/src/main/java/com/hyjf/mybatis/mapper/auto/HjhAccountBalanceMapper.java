package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.HjhAccountBalance;
import com.hyjf.mybatis.model.auto.HjhAccountBalanceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HjhAccountBalanceMapper {
    int countByExample(HjhAccountBalanceExample example);

    int deleteByExample(HjhAccountBalanceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HjhAccountBalance record);

    int insertSelective(HjhAccountBalance record);

    List<HjhAccountBalance> selectByExample(HjhAccountBalanceExample example);

    HjhAccountBalance selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HjhAccountBalance record, @Param("example") HjhAccountBalanceExample example);

    int updateByExample(@Param("record") HjhAccountBalance record, @Param("example") HjhAccountBalanceExample example);

    int updateByPrimaryKeySelective(HjhAccountBalance record);

    int updateByPrimaryKey(HjhAccountBalance record);
}
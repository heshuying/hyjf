package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.BankRechargeLimitConfig;
import com.hyjf.mybatis.model.auto.BankRechargeLimitConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BankRechargeLimitConfigMapper {
    int countByExample(BankRechargeLimitConfigExample example);

    int deleteByExample(BankRechargeLimitConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BankRechargeLimitConfig record);

    int insertSelective(BankRechargeLimitConfig record);

    List<BankRechargeLimitConfig> selectByExample(BankRechargeLimitConfigExample example);

    BankRechargeLimitConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BankRechargeLimitConfig record, @Param("example") BankRechargeLimitConfigExample example);

    int updateByExample(@Param("record") BankRechargeLimitConfig record, @Param("example") BankRechargeLimitConfigExample example);

    int updateByPrimaryKeySelective(BankRechargeLimitConfig record);

    int updateByPrimaryKey(BankRechargeLimitConfig record);
}
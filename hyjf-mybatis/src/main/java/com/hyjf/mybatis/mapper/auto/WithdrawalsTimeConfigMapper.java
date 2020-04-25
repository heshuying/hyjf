package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.WithdrawalsTimeConfig;
import com.hyjf.mybatis.model.auto.WithdrawalsTimeConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WithdrawalsTimeConfigMapper {
    int countByExample(WithdrawalsTimeConfigExample example);

    int deleteByExample(WithdrawalsTimeConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WithdrawalsTimeConfig record);

    int insertSelective(WithdrawalsTimeConfig record);

    List<WithdrawalsTimeConfig> selectByExample(WithdrawalsTimeConfigExample example);

    WithdrawalsTimeConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WithdrawalsTimeConfig record, @Param("example") WithdrawalsTimeConfigExample example);

    int updateByExample(@Param("record") WithdrawalsTimeConfig record, @Param("example") WithdrawalsTimeConfigExample example);

    int updateByPrimaryKeySelective(WithdrawalsTimeConfig record);

    int updateByPrimaryKey(WithdrawalsTimeConfig record);
}
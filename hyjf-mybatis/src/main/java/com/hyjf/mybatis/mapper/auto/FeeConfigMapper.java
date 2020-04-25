package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.FeeConfig;
import com.hyjf.mybatis.model.auto.FeeConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FeeConfigMapper {
    int countByExample(FeeConfigExample example);

    int deleteByExample(FeeConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FeeConfig record);

    int insertSelective(FeeConfig record);

    List<FeeConfig> selectByExample(FeeConfigExample example);

    FeeConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FeeConfig record, @Param("example") FeeConfigExample example);

    int updateByExample(@Param("record") FeeConfig record, @Param("example") FeeConfigExample example);

    int updateByPrimaryKeySelective(FeeConfig record);

    int updateByPrimaryKey(FeeConfig record);
}
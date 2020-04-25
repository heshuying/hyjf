package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.HzrConfig;
import com.hyjf.mybatis.model.auto.HzrConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HzrConfigMapper {
    int countByExample(HzrConfigExample example);

    int deleteByExample(HzrConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HzrConfig record);

    int insertSelective(HzrConfig record);

    List<HzrConfig> selectByExampleWithBLOBs(HzrConfigExample example);

    List<HzrConfig> selectByExample(HzrConfigExample example);

    HzrConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HzrConfig record, @Param("example") HzrConfigExample example);

    int updateByExampleWithBLOBs(@Param("record") HzrConfig record, @Param("example") HzrConfigExample example);

    int updateByExample(@Param("record") HzrConfig record, @Param("example") HzrConfigExample example);

    int updateByPrimaryKeySelective(HzrConfig record);

    int updateByPrimaryKeyWithBLOBs(HzrConfig record);

    int updateByPrimaryKey(HzrConfig record);
}
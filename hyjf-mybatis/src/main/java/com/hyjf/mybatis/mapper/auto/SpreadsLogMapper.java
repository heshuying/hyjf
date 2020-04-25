package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.SpreadsLog;
import com.hyjf.mybatis.model.auto.SpreadsLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SpreadsLogMapper {
    int countByExample(SpreadsLogExample example);

    int deleteByExample(SpreadsLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SpreadsLog record);

    int insertSelective(SpreadsLog record);

    List<SpreadsLog> selectByExampleWithBLOBs(SpreadsLogExample example);

    List<SpreadsLog> selectByExample(SpreadsLogExample example);

    SpreadsLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SpreadsLog record, @Param("example") SpreadsLogExample example);

    int updateByExampleWithBLOBs(@Param("record") SpreadsLog record, @Param("example") SpreadsLogExample example);

    int updateByExample(@Param("record") SpreadsLog record, @Param("example") SpreadsLogExample example);

    int updateByPrimaryKeySelective(SpreadsLog record);

    int updateByPrimaryKeyWithBLOBs(SpreadsLog record);

    int updateByPrimaryKey(SpreadsLog record);
}
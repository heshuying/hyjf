package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.FreezeHistory;
import com.hyjf.mybatis.model.auto.FreezeHistoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FreezeHistoryMapper {
    int countByExample(FreezeHistoryExample example);

    int deleteByExample(FreezeHistoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FreezeHistory record);

    int insertSelective(FreezeHistory record);

    List<FreezeHistory> selectByExample(FreezeHistoryExample example);

    FreezeHistory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FreezeHistory record, @Param("example") FreezeHistoryExample example);

    int updateByExample(@Param("record") FreezeHistory record, @Param("example") FreezeHistoryExample example);

    int updateByPrimaryKeySelective(FreezeHistory record);

    int updateByPrimaryKey(FreezeHistory record);
}
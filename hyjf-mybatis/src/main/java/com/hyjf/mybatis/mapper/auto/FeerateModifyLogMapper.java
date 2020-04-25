package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.FeerateModifyLog;
import com.hyjf.mybatis.model.auto.FeerateModifyLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FeerateModifyLogMapper {
    int countByExample(FeerateModifyLogExample example);

    int deleteByExample(FeerateModifyLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FeerateModifyLog record);

    int insertSelective(FeerateModifyLog record);

    List<FeerateModifyLog> selectByExample(FeerateModifyLogExample example);

    FeerateModifyLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FeerateModifyLog record, @Param("example") FeerateModifyLogExample example);

    int updateByExample(@Param("record") FeerateModifyLog record, @Param("example") FeerateModifyLogExample example);

    int updateByPrimaryKeySelective(FeerateModifyLog record);

    int updateByPrimaryKey(FeerateModifyLog record);
}
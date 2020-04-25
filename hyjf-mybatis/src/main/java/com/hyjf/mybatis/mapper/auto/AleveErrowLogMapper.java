package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.AleveErrowLog;
import com.hyjf.mybatis.model.auto.AleveErrowLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AleveErrowLogMapper {
    int countByExample(AleveErrowLogExample example);

    int deleteByExample(AleveErrowLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AleveErrowLog record);

    int insertSelective(AleveErrowLog record);

    List<AleveErrowLog> selectByExample(AleveErrowLogExample example);

    AleveErrowLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AleveErrowLog record, @Param("example") AleveErrowLogExample example);

    int updateByExample(@Param("record") AleveErrowLog record, @Param("example") AleveErrowLogExample example);

    int updateByPrimaryKeySelective(AleveErrowLog record);

    int updateByPrimaryKey(AleveErrowLog record);
}
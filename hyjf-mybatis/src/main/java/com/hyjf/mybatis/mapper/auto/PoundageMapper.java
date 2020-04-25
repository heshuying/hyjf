package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.Poundage;
import com.hyjf.mybatis.model.auto.PoundageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PoundageMapper {
    int countByExample(PoundageExample example);

    int deleteByExample(PoundageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Poundage record);

    int insertSelective(Poundage record);

    List<Poundage> selectByExample(PoundageExample example);

    Poundage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Poundage record, @Param("example") PoundageExample example);

    int updateByExample(@Param("record") Poundage record, @Param("example") PoundageExample example);

    int updateByPrimaryKeySelective(Poundage record);

    int updateByPrimaryKey(Poundage record);
}
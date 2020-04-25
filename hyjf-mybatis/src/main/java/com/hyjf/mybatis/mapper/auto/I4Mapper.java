package com.hyjf.mybatis.mapper.auto;

import com.hyjf.mybatis.model.auto.I4;
import com.hyjf.mybatis.model.auto.I4Example;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface I4Mapper {
    int countByExample(I4Example example);

    int deleteByExample(I4Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(I4 record);

    int insertSelective(I4 record);

    List<I4> selectByExample(I4Example example);

    I4 selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") I4 record, @Param("example") I4Example example);

    int updateByExample(@Param("record") I4 record, @Param("example") I4Example example);

    int updateByPrimaryKeySelective(I4 record);

    int updateByPrimaryKey(I4 record);
}